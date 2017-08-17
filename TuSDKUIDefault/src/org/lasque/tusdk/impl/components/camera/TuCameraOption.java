/** 
 * TuSDKCore
 * TuCameraOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午5:51:43 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.camera;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.activity.TuResultOption;

/**
 * 相机控制器配置选项
 * 
 * @author Clear
 */
public class TuCameraOption extends TuResultOption
{
	/**
	 * 相机控制器控制类
	 * 
	 * @return 系统相册列表控制类 (默认: TuCameraFragment，如需自定义请继承自
	 *         TuCameraFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuCameraFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.camera.TuCameraFragment}
	 * @return
	 *         根视图布局资源ID (默认: tusdk_impl_component_camera_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuCameraFragment.getLayoutId();
	}

	/********************************** Config ***********************************/
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
	private CameraFlash mDefaultFlashMode = CameraFlash.Off;
	// 是否开启滤镜支持 (默认: 关闭)
	private boolean mEnableFilters;
	// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
	private boolean mShowFilterDefault;
	// 行视图宽度
	private int mGroupFilterCellWidth;
	// 滤镜分组列表行视图布局资源ID
	private int mGroupTableCellLayoutId;
	// 滤镜列表行视图布局资源ID
	private int mFilterTableCellLayoutId;
	// 滤镜组选择栏高度
	private int mFilterBarHeight;
	// 需要显示的滤镜组
	private List<String> mFilterGroup;
	// 开启滤镜配置选项(默认：true)
	private boolean mEnableFilterConfig = true;
	// 是否保存最后一次使用的滤镜
	private boolean mSaveLastFilter;
	// 自动选择分组滤镜指定的默认滤镜
	private boolean mAutoSelectGroupDefaultFilter;
	// 触摸聚焦视图ID
	private int mFocusTouchViewId;
	// 视频视图显示比例
	private float mCameraViewRatio;
	// 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
	// 将忽略RatioType)
	private int mRatioType = RatioType.ratio_default;
	// 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
	private boolean mOutputImageData;
	/** 禁用系统拍照声音 */
	private boolean mDisableCaptureSound;
	// 自定义拍照声音RAW ID，默认关闭系统发声
	private int mCaptureSoundRawId;
	// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
	private boolean mAutoReleaseAfterCaptured;
	// 开启长按拍摄
	private boolean mEnableLongTouchCapture;
	/** 设置焦距初始值*/
	private int mFocalDistanceScale = 0;
	/** 开启手势调焦，默认开启 */
	private boolean mEnableFocalDistance = true;
	/** 禁用聚焦声音 */
	private boolean mDisableFocusBeep;
	/** 禁用持续自动对焦 */
	private boolean mDisableContinueFoucs;
	// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
	private boolean mUnifiedParameters = false;
	// 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
	private float mPreviewEffectScale;
	// 视频覆盖区域颜色 (默认：0x403e43)
	private int mRegionViewColor = TuSdkContext.getColor("lsq_background_camera");
	// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
	private boolean mDisableMirrorFrontFacing;
	// 开启用户滤镜历史记录
	private boolean mEnableFiltersHistory;
	// 显示滤镜标题视图
	private boolean mDisplayFiltersSubtitles;
	/** 开启无效果滤镜 (默认: true) */
	private boolean mEnableNormalFilter = true;
	/** 开启在线滤镜 */
	private boolean mEnableOnlineFilter;
	/** 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册) */
	private boolean mDisplayAlbumPoster;
	/** 是否显示辅助线 (默认: false) */
	private boolean mDisplayGuideLine;
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	private TuSdkWaterMarkOption mWaterMarkOption;
	/** 是否允许音量键拍照 (默认关闭) */
	private boolean mEnableCaptureWithVolumeKeys;

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
	 * TuFilterOnlineFragmentInterface接口)
	 */
	private Class<?> mOnlineFragmentClazz;
	
	/** 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限) */
	public boolean enableFaceDetection;

	/**
	 * 相机方向
	 * 
	 * @return 相机方向 (默认:CameraFacing.Back)
	 *         {@link CameraFacing}
	 */
	public CameraFacing getAvPostion()
	{
		if (mAvPostion == null)
		{
			mAvPostion = CameraFacing.Back;
		}
		return mAvPostion;
	}

	/**
	 * 相机方向
	 * 
	 * @param mAvPostion
	 *            相机方向 (默认:CameraFacing.Back)
	 *            {@link CameraFacing}
	 */
	public void setAvPostion(CameraFacing mAvPostion)
	{
		this.mAvPostion = mAvPostion;
	}

	/**
	 * 照片输出图片长宽 (默认：全屏)
	 * 
	 * @return the mOutputSize
	 */
	public TuSdkSize getOutputSize()
	{
		return mOutputSize;
	}

	/**
	 * 照片输出分辨率
	 * 
	 * @param mOutputSize
	 *            照片输出图片长宽 (默认：全屏)
	 */
	public void setOutputSize(TuSdkSize mOutputSize)
	{
		this.mOutputSize = mOutputSize;
	}

	/**
	 * 闪关灯模式
	 * 
	 * @see #
	 *      {@link CameraFlash}
	 * @return 闪关灯模式 (默认：CameraFlash.Off)
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

	/**
	 * 是否开启滤镜支持
	 * 
	 * @return 是否开启滤镜支持 (默认: 关闭)
	 */
	public boolean isEnableFilters()
	{
		return mEnableFilters;
	}

	/**
	 * 是否开启滤镜支持
	 * 
	 * @param mEnableFilters
	 *            是否开启滤镜支持 (默认: 关闭)
	 */
	public void setEnableFilters(boolean mEnableFilters)
	{
		this.mEnableFilters = mEnableFilters;
	}

	/**
	 * 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
	 * 
	 * @return the mShowFilterDefault
	 */
	public boolean isShowFilterDefault()
	{
		return mShowFilterDefault;
	}

	/**
	 * 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
	 * 
	 * @param mShowFilterDefault
	 *            the mShowFilterDefault to set
	 */
	public void setShowFilterDefault(boolean mShowFilterDefault)
	{
		this.mShowFilterDefault = mShowFilterDefault;
	}

	/**
	 * 行视图宽度
	 * 
	 * @return the mGroupFilterCellWidth
	 */
	public int getGroupFilterCellWidth()
	{
		return mGroupFilterCellWidth;
	}

	/**
	 * 行视图宽度
	 * 
	 * @param mGroupFilterCellWidth
	 *            the mGroupFilterCellWidth to set
	 */
	public void setGroupFilterCellWidth(int mGroupFilterCellWidth)
	{
		this.mGroupFilterCellWidth = mGroupFilterCellWidth;
	}

	/**
	 * 行视图宽度 (单位:DP)
	 * 
	 * @param mGroupFilterCellWidthDP
	 *            the mGroupFilterCellWidthDP to set
	 */
	public void setGroupFilterCellWidthDP(int mGroupFilterCellWidthDP)
	{
		this.setGroupFilterCellWidth(TuSdkContext.dip2px(mGroupFilterCellWidthDP));
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
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemView)
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

	/**
	 * 滤镜组选择栏高度
	 * 
	 * @return the mFilterBarHeight
	 */
	public int getFilterBarHeight()
	{
		return mFilterBarHeight;
	}

	/**
	 * 滤镜组选择栏高度
	 * 
	 * @param mFilterBarHeight
	 *            the mFilterBarHeight to set
	 */
	public void setFilterBarHeight(int mFilterBarHeight)
	{
		this.mFilterBarHeight = mFilterBarHeight;
	}

	/**
	 * 滤镜组选择栏高度 (单位:DP)
	 * 
	 * @param mFilterBarHeightDP
	 *            the mFilterBarHeightDP to set
	 */
	public void setFilterBarHeightDP(int mFilterBarHeightDP)
	{
		this.setFilterBarHeight(TuSdkContext.dip2px(mFilterBarHeightDP));
	}

	/**
	 * 开启用户滤镜历史记录
	 * 
	 * @return the mEnableFiltersHistory
	 */
	public boolean isEnableFiltersHistory()
	{
		return mEnableFiltersHistory;
	}

	/**
	 * 开启用户滤镜历史记录
	 * 
	 * @param mEnableFiltersHistory
	 *            the mEnableFiltersHistory to set
	 */
	public void setEnableFiltersHistory(boolean mEnableFiltersHistory)
	{
		this.mEnableFiltersHistory = mEnableFiltersHistory;
	}

	/**
	 * 显示滤镜标题视图
	 * 
	 * @return the mDisplayFiltersSubtitles
	 */
	public boolean isDisplayFiltersSubtitles()
	{
		return mDisplayFiltersSubtitles;
	}

	/**
	 * 显示滤镜标题视图
	 * 
	 * @param mDisplayFiltersSubtitles
	 *            the mDisplayFiltersSubtitles to set
	 */
	public void setDisplayFiltersSubtitles(boolean mDisplayFiltersSubtitles)
	{
		this.mDisplayFiltersSubtitles = mDisplayFiltersSubtitles;
	}

	/**
	 * 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
	 * 
	 * @return the mFilterGroup
	 */
	public List<String> getFilterGroup()
	{
		return mFilterGroup;
	}

	/**
	 * 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
	 * 
	 * @param mFilterGroup
	 *            the mFilterGroup to set
	 */
	public void setFilterGroup(List<String> mFilterGroup)
	{
		this.mFilterGroup = mFilterGroup;
	}

	/**
	 * 开启滤镜配置选项
	 * 
	 * @return the mEnableFilterConfig
	 */
	public boolean isEnableFilterConfig()
	{
		return mEnableFilterConfig;
	}

	/**
	 * 开启滤镜配置选项
	 * 
	 * @param mEnableFilterConfig
	 *            the mEnableFilterConfig to set
	 */
	public void setEnableFilterConfig(boolean mEnableFilterConfig)
	{
		this.mEnableFilterConfig = mEnableFilterConfig;
	}

	/**
	 * 是否保存最后一次使用的滤镜
	 * 
	 * @return the mSaveLastFilter
	 */
	public boolean isSaveLastFilter()
	{
		return mSaveLastFilter;
	}

	/**
	 * 是否保存最后一次使用的滤镜
	 * 
	 * @param mSaveLastFilter
	 *            the mSaveLastFilter to set
	 */
	public void setSaveLastFilter(boolean mSaveLastFilter)
	{
		this.mSaveLastFilter = mSaveLastFilter;
	}

	/**
	 * 自动选择分组滤镜指定的默认滤镜
	 * 
	 * @return the mAutoSelectGroupDefaultFilter
	 */
	public boolean isAutoSelectGroupDefaultFilter()
	{
		return mAutoSelectGroupDefaultFilter;
	}

	/**
	 * 自动选择分组滤镜指定的默认滤镜
	 * 
	 * @param mAutoSelectGroupDefaultFilter
	 *            the mAutoSelectGroupDefaultFilter to set
	 */
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

	/**
	 * 视频视图显示比例
	 * 
	 * @return 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示)
	 */
	public float getCameraViewRatio()
	{
		if (mCameraViewRatio < 0)
		{
			mCameraViewRatio = 0;
		}
		return mCameraViewRatio;
	}

	/**
	 * 视频视图显示比例
	 * 
	 * @param mCameraViewRatio
	 *            视频视图显示比例 (默认:0, 0 < mCameraViewRatio <= 1)
	 */
	public void setCameraViewRatio(float mCameraViewRatio)
	{
		this.mCameraViewRatio = mCameraViewRatio;
	}

	/**
	 * 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
	 * 将忽略RatioType)
	 * 
	 * @return the mRatioType
	 */
	public final int getRatioType()
	{
		return mRatioType;
	}

	/**
	 * 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
	 * 将忽略RatioType)
	 * 
	 * @param mRatioType
	 *            the mRatioType to set
	 */
	public final void setRatioType(int mRatioType)
	{
		this.mRatioType = mRatioType;
	}

	/**
	 * 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
	 * 
	 * @return the outputImageData
	 */
	public boolean isOutputImageData()
	{
		return mOutputImageData;
	}

	/**
	 * 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
	 * 
	 * @param outputImageData
	 *            the outputImageData to set
	 */
	public void setOutputImageData(boolean outputImageData)
	{
		this.mOutputImageData = outputImageData;
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

	/**
	 * 自定义拍照声音RAW ID，默认关闭系统发声
	 * 
	 * @return the captureSoundRawId
	 */
	public int getCaptureSoundRawId()
	{
		return mCaptureSoundRawId;
	}

	/**
	 * 自定义拍照声音RAW ID，默认关闭系统发声
	 * 
	 * @param captureSoundRawId
	 *            the captureSoundRawId to set
	 */
	public void setCaptureSoundRawId(int captureSoundRawId)
	{
		this.mCaptureSoundRawId = captureSoundRawId;
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

	/**
	 * 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
	 * 
	 * @return the autoReleaseAfterCaptured
	 */
	public boolean isAutoReleaseAfterCaptured()
	{
		return mAutoReleaseAfterCaptured;
	}

	/**
	 * 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
	 * 
	 * @param autoReleaseAfterCaptured
	 *            the autoReleaseAfterCaptured to set
	 */
	public void setAutoReleaseAfterCaptured(boolean autoReleaseAfterCaptured)
	{
		this.mAutoReleaseAfterCaptured = autoReleaseAfterCaptured;
	}

	/**
	 * 开启长按拍摄 (默认：false)
	 * 
	 * @return the enableLongTouchCapture
	 */
	public boolean isEnableLongTouchCapture()
	{
		return mEnableLongTouchCapture;
	}

	/**
	 * 开启长按拍摄 (默认：false)
	 * 
	 * @param enableLongTouchCapture
	 *            the enableLongTouchCapture to set
	 */
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

	/**
	 * 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
	 * 
	 * @return the unifiedParameters
	 */
	public boolean isUnifiedParameters()
	{
		return mUnifiedParameters;
	}

	/**
	 * 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
	 * 
	 * @param unifiedParameters
	 *            the unifiedParameters to set
	 */
	public void setUnifiedParameters(boolean unifiedParameters)
	{
		this.mUnifiedParameters = unifiedParameters;
	}

	/**
	 * 预览视图实时缩放比例
	 * 
	 * @return 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 <
	 *         mPreviewEffectScale <= 1)
	 */
	public float getPreviewEffectScale()
	{
		return mPreviewEffectScale;
	}

	/**
	 * 预览视图实时缩放比例
	 * 
	 * @param mPreviewEffectScale
	 *            预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 <
	 *            mPreviewEffectScale <= 1)
	 */
	public void setPreviewEffectScale(float mPreviewEffectScale)
	{
		this.mPreviewEffectScale = mPreviewEffectScale;
	}

	/**
	 * 视频覆盖区域颜色 (默认：0xFF000000)
	 * 
	 * @return the mRegionViewColor
	 */
	public int getRegionViewColor()
	{
		return mRegionViewColor;
	}

	/**
	 * 视频覆盖区域颜色 (默认：0xFF000000)
	 * 
	 * @param mRegionViewColor
	 *            the mRegionViewColor to set
	 */
	public void setRegionViewColor(int mRegionViewColor)
	{
		this.mRegionViewColor = mRegionViewColor;
	}

	/**
	 * 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
	 * 
	 * @return the mDisableMirrorFrontFacing
	 */
	public boolean isDisableMirrorFrontFacing()
	{
		return mDisableMirrorFrontFacing;
	}

	/**
	 * 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
	 * 
	 * @param mDisableMirrorFrontFacing
	 *            the mDisableMirrorFrontFacing to set
	 */
	public void setDisableMirrorFrontFacing(boolean mDisableMirrorFrontFacing)
	{
		this.mDisableMirrorFrontFacing = mDisableMirrorFrontFacing;
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

	/** 是否显示辅助线 (默认: false) */
	public void setDisplayGuideLine(boolean mDisplayGuideLine)
	{
		this.mDisplayGuideLine = mDisplayGuideLine;
	}

	/** 是否显示辅助线 (默认: false) */
	public boolean isDisplayGuideLine()
	{
		return mDisplayGuideLine;
	}
	
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	public void setWaterMarkOption(TuSdkWaterMarkOption mWaterMarkOption)
	{
		this.mWaterMarkOption = mWaterMarkOption;
	}
	
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	public TuSdkWaterMarkOption getWaterMarkOption()
	{
		return this.mWaterMarkOption;
	}

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
	 * 
	 * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
	 */
	public Class<?> getOnlineFragmentClazz()
	{
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

	/**
	 * 相机控制器配置选项
	 */
	public TuCameraOption()
	{

	}

	/**
	 * 创建相机控制器对象
	 * 
	 * @return 相机控制器对象
	 */
	public TuCameraFragment fragment()
	{
		TuCameraFragment fragment = this.fragmentInstance();

		// 相机方向 {@link android.hardware.Camera.CameraInfo}
		fragment.setAvPostion(this.getAvPostion());
		// 照片输出图片长宽 (默认：全屏)
		fragment.setOutputSize(this.getOutputSize());
		// 闪关灯模式
		fragment.setDefaultFlashMode(this.getDefaultFlashMode());
		// 是否开启滤镜支持 (默认: 关闭)
		fragment.setEnableFilters(this.isEnableFilters());
		// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
		fragment.setShowFilterDefault(this.isShowFilterDefault());
		// 行视图宽度
		fragment.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		// 滤镜组选择栏高度
		fragment.setFilterBarHeight(this.getFilterBarHeight());
		// 滤镜分组列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
		// GroupFilterGroupView)
		fragment.setGroupTableCellLayoutId(this.getGroupTableCellLayoutId());
		// 滤镜列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
		// GroupFilterItemView)
		fragment.setFilterTableCellLayoutId(this.getFilterTableCellLayoutId());
		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		fragment.setFilterGroup(this.getFilterGroup());
		// 开启滤镜配置选项
		fragment.setEnableFilterConfig(this.isEnableFilterConfig());
		// 是否保存最后一次使用的滤镜
		fragment.setSaveLastFilter(this.isSaveLastFilter());
		// 自动选择分组滤镜指定的默认滤镜
		fragment.setAutoSelectGroupDefaultFilter(this.isAutoSelectGroupDefaultFilter());
		// 触摸聚焦视图ID
		fragment.setFocusTouchViewId(this.getFocusTouchViewId());
		// 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示)
		fragment.setCameraViewRatio(this.getCameraViewRatio());
		// 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
		// 将忽略RatioType)
		fragment.setRatioType(this.getRatioType());
		// 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
		fragment.setOutputImageData(this.isOutputImageData());
		// 可选，禁用系统拍照声音 (默认:false)
		fragment.setDisableCaptureSound(this.isDisableCaptureSound());
		// 自定义拍照声音RAW ID，默认关闭系统发声
		fragment.setCaptureSoundRawId(this.getCaptureSoundRawId());
		// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
		fragment.setAutoReleaseAfterCaptured(this.isAutoReleaseAfterCaptured());
		// 开启长按拍摄
		fragment.setEnableLongTouchCapture(this.isEnableLongTouchCapture());
		// 开启手势调焦 (默认：true)
		fragment.setEnableFocalDistance(this.isEnableFocalDistance());
		// 设置焦距初始值
		fragment.setFocalDistanceScale(this.getFocalDistanceScale());
		// 禁用聚焦声音 (默认：false)
		fragment.setDisableFocusBeep(this.isDisableFocusBeep());
		// 禁用持续自动对焦 (默认：false)
		fragment.setDisableContinueFoucs(this.isDisableContinueFoucs());
		// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
		fragment.setUnifiedParameters(this.isUnifiedParameters());
		// 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
		// <= 1)
		fragment.setPreviewEffectScale(this.getPreviewEffectScale());
		// 视频覆盖区域颜色 (默认：0xFF000000)
		fragment.setRegionViewColor(this.getRegionViewColor());
		// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
		fragment.setDisableMirrorFrontFacing(this.isDisableMirrorFrontFacing());
		// 开启用户滤镜历史记录
		fragment.setEnableFiltersHistory(this.isEnableFiltersHistory());
		// 显示滤镜标题视图
		fragment.setDisplayFiltersSubtitles(this.isDisplayFiltersSubtitles());
		// 开启无效果滤镜 (默认: true)
		fragment.setEnableNormalFilter(this.isEnableNormalFilter());
		// 开启在线滤镜
		fragment.setEnableOnlineFilter(this.isEnableOnlineFilter());
		// 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
		fragment.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
		// 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册)
		fragment.setDisplayAlbumPoster(this.isDisplayAlbumPoster());
		// 是否显示辅助线 (默认: false)
		fragment.setDisplayGuideLine(this.isDisplayGuideLine());
		// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
		fragment.setEnableFaceDetection(this.enableFaceDetection);
		// 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) 
		fragment.setWaterMarkOption(this.getWaterMarkOption());
		// 是否允许音量键拍照 (默认关闭)
		fragment.setEnableCaptureWithVolumeKeys(this.isEnableCaptureWithVolumeKeys());
		return fragment;
	}
}
