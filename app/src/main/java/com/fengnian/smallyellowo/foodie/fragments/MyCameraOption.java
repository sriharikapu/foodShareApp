/** 
 * TuSDKCore
 * TuCameraOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午5:51:43 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package com.fengnian.smallyellowo.foodie.fragments;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.activity.TuResultOption;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;

/**
 * 相机控制器配置选项
 * 
 * @author Clear
 */
public class MyCameraOption extends TuResultOption
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
	public MyCameraOption()
	{

	}

	/**
	 * 创建相机控制器对象
	 * @return 相机控制器对象
	 */
	public MyCameraFragment fragment()
	{
		MyCameraFragment fragment = new MyCameraFragment();

//		fragment.setRootViewLayoutId(this.getRootViewLayoutId());
		fragment.setSaveToTemp(this.isSaveToTemp());
		fragment.setSaveToAlbum(this.isSaveToAlbum());
		fragment.setSaveToAlbumName(this.getSaveToAlbumName());
		fragment.setOutputCompress(this.getOutputCompress());

		// 相机方向 {@link android.hardware.Camera.CameraInfo}
		fragment.setAvPostion(this.getAvPostion());
		// 照片输出图片长宽 (默认：全屏)
		fragment.setOutputSize(this.getOutputSize());
		// 闪关灯模式
		fragment.setDefaultFlashMode(this.getDefaultFlashMode());
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
