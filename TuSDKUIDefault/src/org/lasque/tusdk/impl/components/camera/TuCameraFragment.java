/** 
 * TuSDKCore
 * TuCameraFragment.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午5:46:47 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.camera;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity.TuSdkFragmentActivityEventListener;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.TuSdkImageView;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdk.impl.components.filter.TuFilterOnlineFragment;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.camera.TuCameraFragmentBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 相机控制器
 * 
 * @author Clear
 */
@SuppressWarnings("deprecation")
public class TuCameraFragment extends TuCameraFragmentBase implements TuSdkFragmentActivityEventListener
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext
				.getLayoutResId("tusdk_impl_component_camera_fragment");
	}

	/** 相机控制器委托 */
	public interface TuCameraFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 获取一个拍摄结果
		 * 
		 * @param fragment
		 *            默认相机视图控制器
		 * @param result
		 *            拍摄结果
		 */
		void onTuCameraFragmentCaptured(TuCameraFragment fragment, TuSdkResult result);

		/**
		 * 获取一个拍摄结果 (异步方法)
		 * 
		 * @param fragment
		 *            默认相机视图控制器
		 * @param result
		 *            拍摄结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment, TuSdkResult result);

		/**
		 * 请求从相机界面跳转到相册界面。只有 设置mDisplayAlbumPoster为true (默认:false) 才会发生该事件
		 * 
		 * @param fragment
		 *            系统相册控制器
		 */
		void onTuAlbumDemand(TuCameraFragment fragment);
	}

	/** 相册照片列表控制器委托 */
	private TuCameraFragmentDelegate mDelegate;

	/** 相机控制器委托 */
	public TuCameraFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 相机控制器委托 */
	public void setDelegate(TuCameraFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 相机控制器 */
	public TuCameraFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/******************************* Config ********************************/

	/**
	 * 相机方向
	 * {@link CameraFacing}
	 */
	private CameraFacing mAvPostion = CameraFacing.Back;
	/** 照片输出分辨率 (默认：1440 * 1920) */
	private TuSdkSize mOutputSize;

	/**
	 * 闪关灯模式
	 * 
	 * @see #
	 *      {@link CameraFlash}
	 */
	private CameraFlash mDefaultFlashMode;
	/** 是否开启滤镜支持 (默认: 关闭) */
	private boolean mEnableFilters;
	/** 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效) */
	private boolean mShowFilterDefault;
	/** 行视图宽度 */
	private int mGroupFilterCellWidth;
	/** 滤镜分组列表行视图布局资源ID */
	private int mGroupTableCellLayoutId;
	/** 滤镜列表行视图布局资源ID */
	private int mFilterTableCellLayoutId;
	/** 滤镜组选择栏高度 */
	private int mFilterBarHeight;
	/** 需要显示的滤镜组 */
	private List<String> mFilterGroup;
	/** 开启滤镜配置选项 */
	private boolean mEnableFilterConfig;
	/** 是否保存最后一次使用的滤镜 */
	private boolean mSaveLastFilter;
	/** 自动选择分组滤镜指定的默认滤镜 */
	private boolean mAutoSelectGroupDefaultFilter;
	/** 触摸聚焦视图ID */
	private int mFocusTouchViewId;
	/** 视频视图显示比例 */
	private float mCameraViewRatio;
	/** 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap) */
	private boolean mOutputImageData;
	/** 禁用系统拍照声音 */
	private boolean mDisableCaptureSound;
	/** 自定义拍照声音RAW ID，默认关闭系统发声 */
	private int mCaptureSoundRawId;
	/** 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动) */
	private boolean mAutoReleaseAfterCaptured;
	/** 开启长按拍摄 */
	private boolean mEnableLongTouchCapture;
	/** 开启手势调焦，默认开启 */
	private boolean mEnableFocalDistance = true;
	/** 设置焦距初始值*/
	private int mFocalDistanceScale = 0;
	/** 禁用聚焦声音 */
	private boolean mDisableFocusBeep;
	/** 禁用持续自动对焦 */
	private boolean mDisableContinueFoucs;
	/** 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化) */
	private boolean mUnifiedParameters = false;
	/**
	 * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
	 */
	private float mPreviewEffectScale;
	/** 视频覆盖区域颜色 (默认：0xFF000000) */
	private int mRegionViewColor = 0xFF000000;
	/** 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像) */
	private boolean mDisableMirrorFrontFacing;
	/** 开启用户滤镜历史记录 */
	private boolean mEnableFiltersHistory;
	/** 显示滤镜标题视图 */
	private boolean mDisplayFiltersSubtitles;
	/** 开启无效果滤镜 (默认: true) */
	private boolean mEnableNormalFilter = true;
	/** 开启在线滤镜 */
	private boolean mEnableOnlineFilter;
	/** 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册) */
	private boolean mDisplayAlbumPoster;
	/** 是否显示辅助线 (默认: false) */
	private boolean mDisplayGuideLine;
	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
	 * TuFilterOnlineFragmentInterface接口)
	 */
	private Class<?> mOnlineFragmentClazz;
	/** 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限) */
	private boolean mEnableFaceDetection;
	/** 是否允许音量键拍照 (默认关闭) */
	private boolean mEnableCaptureWithVolumeKeys;

	/**
	 * 相机方向 (默认:CameraInfo.CAMERA_FACING_BACK)
	 * {@link android.hardware.Camera.CameraInfo}
	 */
	@Override
	public CameraFacing getAvPostion()
	{
		if (mAvPostion == null)
		{
			mAvPostion = CameraFacing.Back;
		}
		return mAvPostion;
	}

	/**
	 * 相机方向 (默认:CameraFacing.Back)
	 * {@link CameraFacing}
	 */
	public void setAvPostion(CameraFacing mAvPostion)
	{
		this.mAvPostion = mAvPostion;
	}

	/** 照片输出图片长宽 (默认：全屏) */
	public TuSdkSize getOutputSize()
	{
		return mOutputSize;
	}

	/** 照片输出分辨率 */
	public void setOutputSize(TuSdkSize mOutputSize)
	{
		this.mOutputSize = mOutputSize;
	}

	/**
	 * 闪关灯模式
	 * 
	 * @see #
	 *      {@link CameraFlash}
	 * @return 闪关灯模式 (默认：Camera.Parameters.FLASH_MODE_OFF)
	 */
	public CameraFlash getDefaultFlashMode()
	{
		if (mDefaultFlashMode == null)
		{
			mDefaultFlashMode = CameraFlash.Off;
		}
		return mDefaultFlashMode;
	}

	/**
	 * 闪关灯模式
	 * 
	 * @param mDefaultFlashMode
	 *            闪关灯模式
	 * @see #
	 *      {@link CameraFlash}
	 */
	public void setDefaultFlashMode(CameraFlash mDefaultFlashMode)
	{
		this.mDefaultFlashMode = mDefaultFlashMode;
	}

	/** 是否开启滤镜支持 (默认: 关闭) */
	public boolean isEnableFilters()
	{
		return mEnableFilters;
	}

	/** 是否开启滤镜支持 (默认: 关闭) */
	public void setEnableFilters(boolean mEnableFilters)
	{
		this.mEnableFilters = mEnableFilters;
	}

	/** 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效) */
	public boolean isShowFilterDefault()
	{
		return mShowFilterDefault;
	}

	/** 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效) */
	public void setShowFilterDefault(boolean mShowFilterDefault)
	{
		this.mShowFilterDefault = mShowFilterDefault;
	}

	/** 行视图宽度 */
	public int getGroupFilterCellWidth()
	{
		return mGroupFilterCellWidth;
	}

	/** 行视图宽度 */
	public void setGroupFilterCellWidth(int mGroupFilterCellWidth)
	{
		this.mGroupFilterCellWidth = mGroupFilterCellWidth;
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
	 * @return 滤镜分组列表行视图布局资源ID (默认:
	 *         tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *         GroupFilterGroupView)
	 */
	public int getGroupTableCellLayoutId()
	{
		return mGroupTableCellLayoutId;
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *            GroupFilterGroupView)
	 */
	public void setGroupTableCellLayoutId(int mGroupTableCellLayoutId)
	{
		this.mGroupTableCellLayoutId = mGroupTableCellLayoutId;
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @return 滤镜列表行视图布局资源ID (默认:
	 *         tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *         GroupFilterItemView)
	 */
	public int getFilterTableCellLayoutId()
	{
		return mFilterTableCellLayoutId;
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @param 滤镜列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemView)
	 */
	public void setFilterTableCellLayoutId(int mFilterTableCellLayoutId)
	{
		this.mFilterTableCellLayoutId = mFilterTableCellLayoutId;
	}

	/** 滤镜组选择栏高度 */
	public int getFilterBarHeight()
	{
		return mFilterBarHeight;
	}

	/** 滤镜组选择栏高度 */
	public void setFilterBarHeight(int mFilterBarHeight)
	{
		this.mFilterBarHeight = mFilterBarHeight;
	}

	/** 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜) */
	public List<String> getFilterGroup()
	{
		return mFilterGroup;
	}

	/** 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜) */
	public void setFilterGroup(List<String> mFilterGroup)
	{
		this.mFilterGroup = mFilterGroup;
	}

	/** 开启滤镜配置选项 */
	public boolean isEnableFilterConfig()
	{
		return mEnableFilterConfig;
	}

	/** 开启滤镜配置选项 */
	public void setEnableFilterConfig(boolean mEnableFilterConfig)
	{
		this.mEnableFilterConfig = mEnableFilterConfig;
	}

	/** 是否保存最后一次使用的滤镜 */
	public boolean isSaveLastFilter()
	{
		return mSaveLastFilter;
	}

	/** 是否保存最后一次使用的滤镜 */
	public void setSaveLastFilter(boolean mSaveLastFilter)
	{
		this.mSaveLastFilter = mSaveLastFilter;
	}

	/** 自动选择分组滤镜指定的默认滤镜 */
	public boolean isAutoSelectGroupDefaultFilter()
	{
		return mAutoSelectGroupDefaultFilter;
	}

	/** 自动选择分组滤镜指定的默认滤镜 */
	public void setAutoSelectGroupDefaultFilter(boolean mAutoSelectGroupDefaultFilter)
	{
		this.mAutoSelectGroupDefaultFilter = mAutoSelectGroupDefaultFilter;
	}

	/**
	 * 触摸聚焦视图ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.core.utils.hardware.TuSdkVideoCameraExtendViewInterface}
	 * @return 触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
	 */
	public int getFocusTouchViewId()
	{
		if (mFocusTouchViewId == 0)
		{
			mFocusTouchViewId = TuFocusTouchView.getLayoutId();
		}
		return mFocusTouchViewId;
	}

	/**
	 * 触摸聚焦视图ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.core.utils.hardware.TuSdkVideoCameraExtendViewInterface}
	 * @param mFocusTouchViewId
	 *            触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
	 */
	public void setFocusTouchViewId(int mFocusTouchViewId)
	{
		this.mFocusTouchViewId = mFocusTouchViewId;
	}

	/** 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示) */
	@Override
	public float getCameraViewRatio()
	{
		if (mCameraViewRatio < 0)
		{
			mCameraViewRatio = 0;
		}
		return mCameraViewRatio;
	}

	/** 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示) */
	public void setCameraViewRatio(float mCameraViewRatio)
	{
		this.mCameraViewRatio = mCameraViewRatio;
	}

	/** 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap) */
	public boolean isOutputImageData()
	{
		return mOutputImageData;
	}

	/** 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap) */
	public void setOutputImageData(boolean outputImageData)
	{
		this.mOutputImageData = outputImageData;
	}
	
	/** 设置焦距初始值  */
	public void setFocalDistanceScale(int scale)
	{
		this.mFocalDistanceScale = scale;
	}

	/** 设置焦距初始值 */
	public int getFocalDistanceScale()
	{
		return mFocalDistanceScale;
	}
	
	/** 禁用系统拍照声音 (默认:false) */
	public boolean isDisableCaptureSound()
	{
		return mDisableCaptureSound;
	}

	/** 禁用系统拍照声音 (默认:false) */
	public void setDisableCaptureSound(boolean disbleCaptureSound)
	{
		this.mDisableCaptureSound = disbleCaptureSound;
	}

	/** 自定义拍照声音RAW ID，默认关闭系统发声 */
	public int getCaptureSoundRawId()
	{
		return mCaptureSoundRawId;
	}

	/** 自定义拍照声音RAW ID，默认关闭系统发声 */
	public void setCaptureSoundRawId(int captureSoundRawId)
	{
		this.mCaptureSoundRawId = captureSoundRawId;
	}

	/** 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动) */
	public boolean isAutoReleaseAfterCaptured()
	{
		return mAutoReleaseAfterCaptured;
	}

	/** 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动) */
	public void setAutoReleaseAfterCaptured(boolean autoReleaseAfterCaptured)
	{
		this.mAutoReleaseAfterCaptured = autoReleaseAfterCaptured;
	}

	/** 开启长按拍摄 (默认：false) */
	public boolean isEnableLongTouchCapture()
	{
		return mEnableLongTouchCapture;
	}
	
	/** 开启长按拍摄 (默认：false) */
	public void setEnableLongTouchCapture(boolean enableLongTouchCapture)
	{
		this.mEnableLongTouchCapture = enableLongTouchCapture;
	}
	
	/** 开启手势调焦 (默认：true) */
	public void setEnableFocalDistance(boolean enableFocalDistance)
	{
		this.mEnableFocalDistance = enableFocalDistance;
	}

	/** 开启手势调焦 (默认：true) */
	public boolean isEnableFocalDistance()
	{
		return mEnableFocalDistance;
	}

	/** 禁用聚焦声音 (默认：false) */
	public boolean isDisableFocusBeep()
	{
		return mDisableFocusBeep;
	}

	/** 禁用聚焦声音 (默认：false) */
	public void setDisableFocusBeep(boolean disableFocusBeep)
	{
		this.mDisableFocusBeep = disableFocusBeep;
	}

	/** 禁用持续自动对焦 (默认：false) */
	public boolean isDisableContinueFoucs()
	{
		return mDisableContinueFoucs;
	}

	/** 禁用持续自动对焦 (默认：false) */
	public void setDisableContinueFoucs(boolean disableContinueFoucs)
	{
		this.mDisableContinueFoucs = disableContinueFoucs;
	}

	/** 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化) */
	public boolean isUnifiedParameters()
	{
		return mUnifiedParameters;
	}

	/** 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化) */
	public void setUnifiedParameters(boolean unifiedParameters)
	{
		this.mUnifiedParameters = unifiedParameters;
	}

	/**
	 * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
	 */
	public float getPreviewEffectScale()
	{
		return mPreviewEffectScale;
	}

	/**
	 * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
	 */
	public void setPreviewEffectScale(float mPreviewEffectScale)
	{
		this.mPreviewEffectScale = mPreviewEffectScale;
	}

	/** 视频覆盖区域颜色 (默认：0xFF000000) */
	public int getRegionViewColor()
	{
		return mRegionViewColor;
	}

	/** 视频覆盖区域颜色 (默认：0xFF000000) */
	public void setRegionViewColor(int mRegionViewColor)
	{
		this.mRegionViewColor = mRegionViewColor;
	}

	/** 是否显示辅助线 */
	public void setDisplayGuideLine(boolean mDisplayGuideLine)
	{
		this.mDisplayGuideLine = mDisplayGuideLine;
	}

	/** 是否显示辅助线 */
	public boolean isDisplayGuideLine()
	{
		return mDisplayGuideLine;
	}

	/** 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像) */
	public boolean isDisableMirrorFrontFacing()
	{
		return mDisableMirrorFrontFacing;
	}

	/** 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像) */
	public void setDisableMirrorFrontFacing(boolean mDisableMirrorFrontFacing)
	{
		this.mDisableMirrorFrontFacing = mDisableMirrorFrontFacing;
	}

	/** 开启用户滤镜历史记录 */
	public boolean isEnableFiltersHistory()
	{
		return mEnableFiltersHistory;
	}

	/** 开启用户滤镜历史记录 */
	public void setEnableFiltersHistory(boolean mEnableFiltersHistory)
	{
		this.mEnableFiltersHistory = mEnableFiltersHistory;
	}

	/** 显示滤镜标题视图 */
	public boolean isDisplayFiltersSubtitles()
	{
		return mDisplayFiltersSubtitles;
	}

	/** 显示滤镜标题视图 */
	public void setDisplayFiltersSubtitles(boolean mDisplayFiltersSubtitles)
	{
		this.mDisplayFiltersSubtitles = mDisplayFiltersSubtitles;
	}
	
	/** 开启无效果滤镜 (默认: true) */
	public boolean isEnableNormalFilter()
	{
		return mEnableNormalFilter;
	}

	/** 开启无效果滤镜 (默认: true) */
	public void setEnableNormalFilter(boolean mEnableNormalFilter)
	{
		this.mEnableNormalFilter = mEnableNormalFilter;
	}

	/** 开启在线滤镜 */
	public boolean isEnableOnlineFilter()
	{
		return mEnableOnlineFilter;
	}

	/** 开启在线滤镜 */
	public void setEnableOnlineFilter(boolean mEnableOnlineFilter)
	{
		this.mEnableOnlineFilter = mEnableOnlineFilter;
	}

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
	 * 
	 * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
	 */
	public Class<?> getOnlineFragmentClazz()
	{
		if (mOnlineFragmentClazz == null)
		{
			mOnlineFragmentClazz = TuFilterOnlineFragment.class;
		}
		return mOnlineFragmentClazz;
	}

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
	 * 
	 * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
	 */
	public void setOnlineFragmentClazz(Class<?> mOnlineFragmentClazz)
	{
		this.mOnlineFragmentClazz = mOnlineFragmentClazz;
	}

	/** 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册) */
	public boolean isDisplayAlbumPoster()
	{
		return mDisplayAlbumPoster;
	}

	/** 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册) */
	public void setDisplayAlbumPoster(boolean mDisplayAlbumPoster)
	{
		this.mDisplayAlbumPoster = mDisplayAlbumPoster;
	}

	/** 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限) */
	public boolean isEnableFaceDetection()
	{
		return mEnableFaceDetection;
	}

	/** 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限) */
	public void setEnableFaceDetection(boolean enableFaceDetection)
	{
		this.mEnableFaceDetection = enableFaceDetection;
	}
	
	/**
	 * 是否允许音量键拍照 (默认关闭)
	 * 
	 * @param mEnableCaptureWithVolumeKeys
	 */
	public void setEnableCaptureWithVolumeKeys(Boolean mEnableCaptureWithVolumeKeys)
	{
		this.mEnableCaptureWithVolumeKeys = mEnableCaptureWithVolumeKeys;
	}

	/**
	 * 是否允许音量键拍照 (默认关闭)
	 * 
	 * @return
	 */
	public boolean isEnableCaptureWithVolumeKeys()
	{
		return mEnableCaptureWithVolumeKeys;
	}

	/******************************* View ********************************/
	/** 相机视图 */
	private RelativeLayout mCameraView;
	/** 顶部配置栏 */
	private ViewGroup mConfigBar;
	/** 关闭按钮 */
	private TuSdkImageButton mCloseButton;
	/** 闪光灯按钮 */
	private TuSdkImageButton mFlashButton;
	/** 切换镜头按钮 */
	private TuSdkImageButton mSwitchButton;
	/** 相机比例切换按钮 */
	private TuSdkImageButton mRatioButton;
	/** 相机辅助线切换按钮 */
	private TuSdkImageButton mGuideLineButton;
	/** 滤镜分组视图 */
	private TuCameraFilterView mGroupFilterView;
	/** 底部栏目 */
	private RelativeLayout mBottomBar;
	/** 拍摄按钮 */
	private TuSdkImageButton mCaptureButton;
	/** 滤镜开关按钮 */
	private TuSdkImageButton mFilterButton;
	/** 滤镜选项视图 */
	private RelativeLayout mFlashView;
	/** 自动闪光灯选项 */
	private TextView mFlashModelAuto;
	/** 开启闪光灯选项 */
	private TextView mFlashModelOpen;
	/** 关闭闪光灯选项 */
	private TextView mFlashModelOff;
	/** 启动视图 */
	private ImageView mStartingView;
	/** 相册照片 */
	private TuSdkImageView mAlbumPoster;

	/** 相机视图 */
	@Override
	public RelativeLayout getCameraView()
	{
		if (mCameraView == null)
		{
			mCameraView = this.getViewById("lsq_cameraView");
		}
		return mCameraView;
	}

	/** 顶部配置栏 */
	public ViewGroup getConfigBar()
	{
		if (mConfigBar == null)
		{
			mConfigBar = this.getViewById("lsq_configBar");
		}
		return mConfigBar;
	}

	/** 关闭按钮 */
	public TuSdkImageButton getCloseButton()
	{
		if (mCloseButton == null)
		{
			mCloseButton = this.getViewById("lsq_closeButton");
			if (mCloseButton != null)
			{
				mCloseButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCloseButton;
	}

	/** 闪光灯按钮 */
	public TuSdkImageButton getFlashButton()
	{
		if (mFlashButton == null)
		{
			mFlashButton = this.getViewById("lsq_flashButton");
			if (mFlashButton != null)
			{
				mFlashButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mFlashButton;
	}

	/** 切换镜头按钮 */
	public TuSdkImageButton getSwitchButton()
	{
		if (mSwitchButton == null)
		{
			mSwitchButton = this.getViewById("lsq_switchButton");
			if (mSwitchButton != null)
			{
				mSwitchButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mSwitchButton;
	}

	/** 相机比例切换按钮 */
	public TuSdkImageButton getRatioButton()
	{
		if (mRatioButton == null)
		{
			mRatioButton = this.getViewById("lsq_ratioButton");
			if (mRatioButton != null)
			{
				mRatioButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mRatioButton;
	}

	/** 相机比例切换按钮 */
	public TuSdkImageButton getGuideLineButton()
	{
		if (mGuideLineButton == null)
		{
			mGuideLineButton = this.getViewById("lsq_guideLineButton");
			if (mGuideLineButton != null)
			{
				mGuideLineButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mGuideLineButton;
	}

	/** 相册照片 */
	public TuSdkImageView getAlbumPosterView()
	{
		if (mAlbumPoster == null)
		{
			mAlbumPoster = this.getViewById("lsq_albumPosterView");
			if (mAlbumPoster != null)
			{
				mAlbumPoster.setOnClickListener(mButtonClickListener);
			}
		}
		return mAlbumPoster;
	}

	/** 滤镜分组视图 */
	public TuCameraFilterView getGroupFilterView()
	{
		if (mGroupFilterView == null)
		{
			mGroupFilterView = this.getViewById("lsq_group_filter_view");
			if (mGroupFilterView != null)
			{
				this.configGroupFilterView(mGroupFilterView);
				// 绑定选择委托
				mGroupFilterView.setDelegate(mFilterBarDelegate);
			}
		}
		return mGroupFilterView;
	}

	/** 配置滤镜栏视图 */
	protected void configGroupFilterView(GroupFilterBaseView view)
	{
		if (view == null) return;
		// 行视图宽度
		view.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		// 滤镜组选择栏高度
		view.setFilterBarHeight(this.getFilterBarHeight());
		// 滤镜分组列表行视图布局资源ID
		view.setGroupTableCellLayoutId(this.getGroupTableCellLayoutId());
		// 滤镜列表行视图布局资源ID
		view.setFilterTableCellLayoutId(this.getFilterTableCellLayoutId());
		// 指定显示的滤镜组
		view.setFilterGroup(this.getFilterGroup());
		// 是否保存最后一次使用的滤镜
		view.setSaveLastFilter(this.isSaveLastFilter());
		// 自动选择分组滤镜指定的默认滤镜
		view.setAutoSelectGroupDefaultFilter(this.isAutoSelectGroupDefaultFilter());
		// 开启用户滤镜历史记录
		view.setEnableHistory(this.isEnableFiltersHistory());
		// 显示滤镜标题视图
		view.setDisplaySubtitles(this.isDisplayFiltersSubtitles());
		// 设置控制器
		view.setActivity(this.getActivity());
		// 开启无效果滤镜
		view.setEnableNormalFilter(this.isEnableNormalFilter());
		// 开启在线滤镜
		view.setEnableOnlineFilter(this.isEnableOnlineFilter());
		// 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
		view.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
	}

	/** 底部栏目 */
	public RelativeLayout getBottomBar()
	{
		if (mBottomBar == null)
		{
			mBottomBar = this.getViewById("lsq_bottomBar");
		}
		return mBottomBar;
	}

	/** 拍摄按钮 */
	public TuSdkImageButton getCaptureButton()
	{
		if (mCaptureButton == null)
		{
			mCaptureButton = this.getViewById("lsq_captureButton");
			if (mCaptureButton != null)
			{
				mCaptureButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCaptureButton;
	}

	/** 滤镜开关按钮 */
	public TuSdkImageButton getFilterButton()
	{
		if (mFilterButton == null)
		{
			mFilterButton = this.getViewById("lsq_filterButton");
			if (mFilterButton != null)
			{
				mFilterButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mFilterButton;
	}

	/** 滤镜选项视图 */
	public RelativeLayout getFlashView()
	{
		if (mFlashView == null)
		{
			mFlashView = this.getViewById("lsq_flashView");
			if (mFlashView != null)
			{
				mFlashView.setOnClickListener(mButtonClickListener);
			}
		}
		return mFlashView;
	}

	/** 滤镜选项容器 */
	public LinearLayout getFlashWrapView()
	{
		return this.getViewById("lsq_flashWrapView");
	}

	/** 自动闪光灯选项 */
	public TextView getFlashModelAuto()
	{
		if (mFlashModelAuto == null)
		{
			mFlashModelAuto = this.getViewById("lsq_flash_model_auto");
			if (mFlashModelAuto != null)
			{
				mFlashModelAuto.setOnClickListener(mButtonClickListener);
			}
		}
		return mFlashModelAuto;
	}

	/** 开启闪光灯选项 */
	public TextView getFlashModelOpen()
	{
		if (mFlashModelOpen == null)
		{
			mFlashModelOpen = this.getViewById("lsq_flash_model_open");
			if (mFlashModelOpen != null)
			{
				mFlashModelOpen.setOnClickListener(mButtonClickListener);
			}
		}
		return mFlashModelOpen;
	}

	/** 关闭闪光灯选项 */
	public TextView getFlashModelOff()
	{
		if (mFlashModelOff == null)
		{
			mFlashModelOff = this.getViewById("lsq_flash_model_off");
			if (mFlashModelOff != null)
			{
				mFlashModelOff.setOnClickListener(mButtonClickListener);
			}
		}
		return mFlashModelOff;
	}

	/** 启动视图 */
	public ImageView getStartingView()
	{
		if (mStartingView == null)
		{
			mStartingView = this.getViewById("lsq_startingView");
			if (mStartingView != null)
			{
				mStartingView.setOnClickListener(mButtonClickListener);
			}
		}
		return mStartingView;
	}

	/** 滤镜选择栏委托 */
	private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate()
	{
		/**
		 * @param view
		 *            滤镜分组视图
		 * @param itemData
		 *            滤镜分组元素
		 * @param canCapture
		 *            是否允许拍摄
		 * @return 是否允许继续执行
		 */
		@Override
		public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture)
		{
			// 直接拍照
			if (canCapture)
			{
				handleCaptureButton();
				return true;
			}

			switch (itemData.type)
				{
				case TypeFilter:
					// 设置滤镜
					return handleSwitchFilter(itemData.getFilterCode());
				default:
					break;
				}
			return true;
		}

		@Override
		public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow)
		{
			if (isShow) return;
			onGroupFilterHidden(view);
		}
	};

	/** 按钮点击事件 */
	private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			// 分发视图点击事件
			dispatcherViewClick(v);
		}
	};

	/** 分发视图点击事件 */
	protected void dispatcherViewClick(View v)
	{
		if (this.equalViewIds(v, this.getCloseButton()))
		{
			this.handleCloseButton();
		}
		else if (this.equalViewIds(v, this.getFlashButton()))
		{
			this.handleFlashButton();
		}
		else if (this.equalViewIds(v, this.getGuideLineButton()))
		{
			this.handleGuideLineButton();
		}
		else if (this.equalViewIds(v, this.getSwitchButton()))
		{
			this.handleSwitchButton();
		}
		else if (this.equalViewIds(v, this.getCaptureButton()))
		{
			this.handleCaptureButton();
		}
		else if (this.equalViewIds(v, this.getFilterButton()))
		{
			this.handleFilterButton();
		}
		else if (this.equalViewIds(v, this.getFlashView()))
		{
			this.handleFlashView();
		}
		else if (this.equalViewIds(v, this.getFlashModelAuto()))
		{
			this.handleFlashModel(CameraFlash.Auto);
		}
		else if (this.equalViewIds(v, this.getFlashModelOpen()))
		{
			this.handleFlashModel(CameraFlash.On);
		}
		else if (this.equalViewIds(v, this.getFlashModelOff()))
		{
			this.handleFlashModel(CameraFlash.Off);
		}
		else if (this.equalViewIds(v, this.getRatioButton()))
		{
			this.handleCameraRatio();
		}
		else if (this.equalViewIds(v, this.getAlbumPosterView()))
		{
			if (this.mDelegate != null) this.mDelegate.onTuAlbumDemand(this);
		}
	}

	/************************** loadView *****************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		this.getCameraView();
		this.getConfigBar();
		this.getCloseButton();
		this.handleFlashModel(this.getDefaultFlashMode());
		this.initDefaultRatio(this.getRatioButton());
		this.showViewIn(this.getSwitchButton(), CameraHelper.cameraCounts() > 1);
		this.getBottomBar();
		this.getCaptureButton();
		// 滤镜分组视图
		this.getGroupFilterView();
		this.showViewIn(this.getFilterButton(), this.isEnableFilters());
		this.showViewIn(this.getAlbumPosterView(), this.isDisplayAlbumPoster());

		this.showViewIn(this.getFlashView(), false);
		this.getFlashModelAuto();
		this.getFlashModelOpen();
		this.getFlashModelOff();
		this.getStartingView();
		
		// 先隐藏滤镜栏，稍后再处理
		if ( this.getGroupFilterView() != null) ViewCompat.setAlpha(this.getGroupFilterView(), 0);

		this.setGuideLineButtonState();
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
		
		if (hasRequiredPermission())
		{
			// 延迟加载视图
			ThreadHelper.postDelayed(new Runnable(){  
			     public void run() {  
			    	 initCameraView();
			     }  
			}, 0); 
		}
		else
		{
			requestRequiredPermissions();
		}
	}
	
	/**
	 * 是否已被授予权限
	 * 
	 * @param permissionGranted
	 */
	protected void onPermissionGrantedResult(boolean permissionGranted)
	{
		if (permissionGranted)
		{
			ThreadHelper.post(new Runnable() 
			{
				@Override
				public void run() 
				{
					initCameraView();
				}
			});
			
		}
		else
		{
			String msg = TuSdkContext.getString("lsq_carema_no_access", ContextUtils.getAppName(getContext()));
			
			TuSdkViewHelper.alert(permissionAlertDelegate, this.getContext(), TuSdkContext.getString("lsq_carema_alert_title"), 
					msg, TuSdkContext.getString("lsq_button_close"), TuSdkContext.getString("lsq_button_setting")
			);
		}
	}
	
	/** 初始化相机及控件, 在 viewDidLoad 之后调用 */
	@Override
	protected void initCameraView()
	{
		super.initCameraView();
		// 加载滤镜视图
		this.loadFilterView();

		// 初始化相机焦距
		handleFocalDistance(this.getFocalDistanceScale(), true);
		// 记载相册最新照片
		this.loadAlbumPosterImage();

		// 计算顶部按钮位置
		TuSdkSize screenSize = ContextUtils.getDisplaySize(this.getActivity());
		int margin = TuSdkContext.dip2px(12);
		int buttonWidth = TuSdkContext.dip2px(44);
		int padding = (screenSize.width - margin * 2 - buttonWidth * 5) / 4;

		// 闪光灯菜单位置
		RelativeLayout.LayoutParams layoutParas = (RelativeLayout.LayoutParams) getFlashWrapView().getLayoutParams();
		layoutParas.setMargins(0, buttonWidth, padding + buttonWidth * 3 / 2 + margin - layoutParas.width / 2, 0);
	}

	/** 加载滤镜视图 */
	protected void loadFilterView()
	{
		// 设置滤镜组
		if (this.getGroupFilterView() == null) return;

		if (!this.isEnableFilters())
		{
			this.getGroupFilterView().setDefaultShowState(false);
			return;
		}

		// 默认显示
		if (this.isShowFilterDefault())
		{
			ViewCompat.setAlpha(this.getBottomBar(), 0);
			
			ViewCompat.animate(this.getGroupFilterView()).alpha(1).setDuration(300).setListener(null);
		}
		else
		{
			ViewCompat.setAlpha(this.getGroupFilterView(), 1);
			
			this.getGroupFilterView().setDefaultShowState(false);
		}

		this.getGroupFilterView().loadFilters();
	}

	/** 加载相册最新照片缩略图 */
	protected void loadAlbumPosterImage()
	{
		if (this.isDisplayAlbumPoster() == false) return;

		ArrayList<AlbumSqlInfo> albumGroups = ImageSqlHelper.getAlbumList(this.getActivity());

		AlbumSqlInfo cameraAlbum = null;

		TuSdkImageView img = this.getAlbumPosterView();

		if (albumGroups != null)
		{
			AlbumSqlInfo item;
			for (int i = 0, j = albumGroups.size(); i < j; i++)
			{
				item = albumGroups.get(i);
				// 找到相机相册
				if (AlbumSqlInfo.CAMERA_FOLDER.equalsIgnoreCase(item.title))
				{
					cameraAlbum = item;
					break;
				}
			}
		}

		Boolean takenPhotoFound = false;

		if (cameraAlbum != null)
		{
			ArrayList<ImageSqlInfo> photos = ImageSqlHelper.getPhotoList(getActivity(), cameraAlbum.id);

			if (photos != null && photos.size() > 0)
			{
				takenPhotoFound = true;
				final ImageSqlInfo latestItem = photos.get(0);
				img.setScaleType(ScaleType.CENTER);

				ThreadHelper.runThread(new Runnable()
				{
					@Override
					public void run()
					{
						final Bitmap image = BitmapHelper.getBitmap(latestItem, TuSdkContext.dip2px(28), false);

						final Bitmap resizedImage = BitmapHelper.imageCorp(image, 1);

						runOnUiThread(new Runnable()
						{
							public void run()
							{
								onPosterImageLoaded(resizedImage);
							}
						});
					}
				});
			}
		}

		if (takenPhotoFound == false)
		{
			// lsq_style_default_album_cover_empty.png
			Drawable emptyPoster = TuSdkContext.getDrawable("lsq_style_default_camera_album_poster_empty");
			img.setBackgroundColor(TuSdkContext.getColor("lsq_background_album_cover"));
			img.setScaleType(ScaleType.CENTER);
			img.setImageDrawable(emptyPoster);
		}
	}

	/**
	 * 相册最新缩略图加载完毕
	 * 
	 * @param bm
	 */
	private void onPosterImageLoaded(Bitmap bm)
	{
		this.getAlbumPosterView().setImageBitmap(bm);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!this.isFragmentPause() && this.getCamera() != null)
		{
			this.getCamera().startCameraCapture();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (this.getCamera() != null)
		{
			this.showViewIn(this.getStartingView(), true);
			this.getCamera().stopCameraCapture();
		}
	}

	/** 配置相机参数 */
	@Override
	protected void configCamera(TuSdkStillCameraInterface camera)
	{
		// 可选：设置输出图片分辨率
		// 注意：因为移动设备内存问题，可能会限制部分机型使用最高分辨率
		// 请使用 TuSdkGPU.getGpuType().getSize() 查看当前设备所能够进行处理的图片尺寸
		// 默认使用 1920 * 1080分辨率
		camera.setOutputSize(this.getOutputSize());
		// 视频预览显示比例 (默认：0， 0 <= RegionRatio, 当设置为0时全屏显示)
		camera.adapter().setRegionRatio(this.getCurrentRatio());
		// 可选，设置相机手动聚焦
		camera.adapter().setFocusTouchView(this.getFocusTouchViewId());
		// 可选，开启长按拍摄
		camera.adapter().setEnableLongTouchCapture(this.isEnableLongTouchCapture());
		// 禁用聚焦声音 (默认：false)
		camera.adapter().setDisableFocusBeep(this.isDisableFocusBeep());
		// 禁用持续自动对焦 (默认：false)
		camera.adapter().setDisableContinueFoucs(this.isDisableContinueFoucs());
		// 可选，是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
		camera.adapter().setOutputImageData(this.isOutputImageData());
		// 可选，禁用系统拍照声音 (默认:false)
		camera.adapter().setDisableCaptureSound(this.isDisableCaptureSound());
		// 可选，自定义拍照声音RAW ID，默认关闭系统发声
		camera.adapter().setCaptureSoundRawId(this.getCaptureSoundRawId());
		// 可选，自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
		camera.setAutoReleaseAfterCaptured(this.isAutoReleaseAfterCaptured());
		// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
		camera.setUnifiedParameters(this.isUnifiedParameters());
		// 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
		// <= 1)
		camera.setPreviewEffectScale(this.getPreviewEffectScale());
		// 视频覆盖区域颜色 (默认：0xFF000000)
		camera.adapter().setRegionViewColor(this.getRegionViewColor());
		// 是否显示辅助线 (默认: false)
		camera.adapter().setDisplayGuideLine(this.isDisplayGuideLine());
		// 开启滤镜配置选项
		camera.adapter().setEnableFilterConfig(this.isEnableFilterConfig());
		// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
		camera.setDisableMirrorFrontFacing(this.isDisableMirrorFrontFacing());
		// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
		camera.setEnableFaceDetection(this.isEnableFaceDetection());
	}

	/** 设备旋转通知 */
	@Override
	public void onOrientationChanged(InterfaceOrientation orien)
	{
		AnimHelper.rotateAnimation(this.getFilterButton(), orien, 200);
		AnimHelper.rotateAnimation(this.getRatioButton(), orien, 200);
		if (this.getGuideLineButton() != null)
		{
			AnimHelper.rotateAnimation(this.getGuideLineButton(), orien, 200);
		}
		AnimHelper.rotateAnimation(this.getFlashButton(), orien, 200);
		AnimHelper.rotateAnimation(this.getSwitchButton(), orien, 200);
	}

	/************************** camera action *****************************/

	/** 相机状态改变 */
	protected void onCameraStateChangedImpl(TuSdkStillCameraInterface camera, CameraState state)
	{
		if (state == CameraState.StateCaptured)
		{
			this.hubStatus(this.getResString("lsq_carema_image_process"));
			return;
		}

		if (state != CameraState.StateStarted) return;

		this.showViewIn(this.getStartingView(), false);
		this.showViewIn(this.getFlashView(), false);
		
	
		if (camera.canSupportFlash())
			camera.setFlashMode(this.getDefaultFlashMode());

		this.getFlashButton().setEnabled(camera.canSupportFlash());
		

		// 是否允许存储文件
		this.canSaveFile();
		// CameraHelper.logParameters(mCamera.inputCameraParameters());
	}

	/** 获取拍摄图片 */
	protected void onCameraTakedPictureImpl(TuSdkStillCameraInterface camera, final TuSdkResult result)
	{
		// 异步处理如果需要保存文件 (默认完成后执行:notifyProcessing(TuSdkResult result))
		ThreadHelper.runThread(new Runnable()
		{
			@Override
			public void run()
			{
				asyncProcessingIfNeedSave(result);
			}
		});
	}

	/**
	 * 通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 */
	@Override
	protected void notifyProcessing(TuSdkResult result)
	{
		this.hubSuccess(this.getResString("lsq_carema_image_process_completed"));
		if (this.mDelegate == null) return;
		this.mDelegate.onTuCameraFragmentCaptured(this, result);
	}

	/**
	 * 异步通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	protected boolean asyncNotifyProcessing(TuSdkResult result)
	{
		if (this.mDelegate == null) return false;
		return this.mDelegate.onTuCameraFragmentCapturedAsync(this, result);
	}

	/** 切换闪光灯模式 */
	@Override
	protected void handleFlashModel(CameraFlash flashMode)
	{
		this.handleFlashView();

		this.setDefaultFlashMode(flashMode);
		if (flashMode == null) return;

		int flashIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_flash_auto");

		if (flashMode == CameraFlash.On)
		{
			flashIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_flash_on");
		}
		else if (flashMode == CameraFlash.Off)
		{
			flashIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_flash_off");
		}
		if (this.getFlashButton() != null)
		{
			this.getFlashButton().setImageResource(flashIcon);
		}

		super.handleFlashModel(flashMode);
	}

	/** 关闭闪光灯选项视图 */
	protected void handleFlashView()
	{
		AnimHelper.alphaHidden(this.getFlashView());
	}

	/** 开启闪光灯选项视图 */
	protected void handleFlashButton()
	{
		AnimHelper.alphaShow(this.getFlashView());
	}

	/** 切换前后摄像头 */
	@Override
	protected void handleSwitchButton()
	{
		this.showViewIn(this.getStartingView(), true);
		super.handleSwitchButton();
	}

	/** 切换辅助线显示 */
	@Override
	protected void handleGuideLineButton()
	{
		super.handleGuideLineButton();

		this.setDisplayGuideLine(!this.isDisplayGuideLine());

		setGuideLineButtonState();
	}

	/** 切换滤镜视图显示 */
	protected void handleFilterButton()
	{
		if (this.getGroupFilterView() != null)
		{
			// 切换滤镜栏显示状态
			this.getGroupFilterView().showGroupView();
			ViewCompat.animate(this.getBottomBar()).alpha(0).setDuration(80);
		}
	}

	/** 滤镜组隐藏 */
	protected void onGroupFilterHidden(TuCameraFilterView view)
	{
		ViewCompat.animate(this.getBottomBar()).alpha(1).setDuration(80);
	}

	/****************************** CameraRatio ***********************************/

	/** 初始化默认相机显示比例 */
	private void initDefaultRatio(TuSdkImageButton btn)
	{
		if (btn == null) return;

		// 设置了固定比例
		if (this.getCameraViewRatio() > 0)
		{
			this.showViewIn(btn, false);
			return;
		}

		// 设置了固定比例，或者仅有一种比例可选时，不显示比例开关
		if (RatioType.ratioCount(this.getRatioType()) == 1) this.showViewIn(btn, false);

		this.setCurrentRatioType(RatioType.firstRatioType(this.getRatioType()));
	}

	private void setGuideLineButtonState()
	{
		if (this.getGuideLineButton() == null) return;

		Drawable icon = this.getGuideLineButton().getDrawable();
		icon.clearColorFilter();

		if (this.isDisplayGuideLine())
		{
			int color = TuSdkContext.getColor("lsq_color_orange");
			icon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}

		this.getGuideLineButton().setImageDrawable(icon);
	}

	/** 设置当前比例类型 */
	@Override
	protected void setCurrentRatioType(int ratioType)
	{
		super.setCurrentRatioType(ratioType);
		int ratioIcon;
		switch (ratioType)
			{
			case RatioType.ratio_9_16:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_9_16");
				break;
			case RatioType.ratio_3_4:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_3_4");
				break;
			case RatioType.ratio_2_3:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_2_3");
				break;
			case RatioType.ratio_1_1:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_1_1");
				break;
			case RatioType.ratio_3_2:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_3_2");
				break;
			case RatioType.ratio_4_3:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_4_3");
				break;
			case RatioType.ratio_16_9:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_16_9");
				break;
			default:
				ratioIcon = TuSdkContext.getDrawableResId("lsq_style_default_camera_ratio_orgin");
				break;
			}
		this.getRatioButton().setImageResource(ratioIcon);
	}

	/**
	 * 获取音量键触发事件
	 */
	@Override
	public boolean onActivityKeyDispatcher(TuSdkFragmentActivity activity,
			int keyCode) 
	{
		if(!this.isEnableCaptureWithVolumeKeys())
			return false;
		
		switch (keyCode) 
		{
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			case KeyEvent.KEYCODE_VOLUME_UP:
				this.handleCaptureWithVolume();
				return true;
			default:
				break;
		}

		return false;
	}

	/**
	 * 获取手势放大和缩小操作
	 */
	@Override
	public boolean onActivityTouchMotionDispatcher(TuSdkFragmentActivity activity, boolean isZoomIn)
	{
		if(!this.isEnableFocalDistance()) return false;
		
		if(isZoomIn)
		{
			// 放大手势操作
			handleFocalDistance(-1, true);
		}
		else
		{
			// 缩小手势操作
			handleFocalDistance(-1, false);
		}
		
		return true;
	}
	
	/**
	 * 处理相机焦距变化
	 * 
	 * @param isZoomIn 是否放大焦距
	 */
	private void handleFocalDistance(int scale, boolean isZoomIn)
	{
		Parameters params = ((TuSdkStillCamera)getCamera()).inputCameraParameters();
		if(params == null) return;
		
		int result = 0;
		if (params.isZoomSupported())
		{
			int maxZoom = params.getMaxZoom();
			int zoom = params.getZoom();
			
			if(scale < 0){
				if (isZoomIn && zoom < maxZoom){
					zoom++;
				}
				else if (zoom > 0){
					zoom--;
				}
				
				result = zoom;
			}else if(scale >= 0 && scale <= params.getMaxZoom()){
				result = scale;
			}
			params.setZoom(result);
			
			((TuSdkStillCamera)getCamera()).inputCamera().setParameters(params);
		}
		else
		{
			TLog.e("Device not support Zoom");
		}
	}
}