/** 
 * TuSDKCore
 * TuAvatarComponentOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午2:36:26 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.components.camera.TuCameraOption;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutOption;

/**
 * 头像设置配置选项
 * 
 * @author Clear
 */
public class TuAvatarComponentOption extends TuAlbumComponentOption
{
	/**
	 * 相机控制器配置选项
	 */
	private TuCameraOption mCameraOption;

	/**
	 * 裁剪与缩放控制器配置选项
	 */
	private TuEditTurnAndCutOption mTuEditTurnAndCutOption;

	/**
	 * 相机控制器配置选项<br>
	 * 默认配置：<br>
	 * // 前置摄像头优先<br>
	 * mCameraOption.setAvPostion(CameraFacing.Front);<br>
	 * // 开启滤镜<br>
	 * mCameraOption.setEnableFilters(true);<br>
	 * // 自动选择分组滤镜指定的默认滤镜<br>
	 * mCameraOption.setAutoSelectGroupDefaultFilter(true);<br>
	 * // 自动闪光灯模式<br>
	 * mCameraOption.setDefaultFlashMode(CameraFlash.Off);<br>
	 * // 保存拍摄图片到缓存文件<br>
	 * mCameraOption.setSaveToTemp(true);<br>
	 * // 开启长按拍摄<br>
	 * mCameraOption.setEnableLongTouchCapture(true);<br>
	 * // 开启拍摄后自动释放相机<br>
	 * mCameraOption.setAutoReleaseAfterCaptured(true);<br>
	 * // 视频覆盖区域颜色 (默认：0xFF000000)<br>
	 * mCameraOption.setRegionViewColor(0xFF333333);<br>
	 * // 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
	 * // 将忽略RatioType)<br>
	 * mCameraOption.setRatioType(RatioType.ratio_all);<br>
	 * // 开启用户滤镜历史记录<br>
	 * mCameraOption.setEnableFiltersHistory(true);<br>
	 * // 开启在线滤镜<br>
	 * mCameraOption.setEnableOnlineFilter(true);<br>
	 * // 显示滤镜标题视图<br>
	 * mCameraOption.setDisplayFiltersSubtitles(true);<br>
	 * // 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)<br>
	 * mCameraOption.enableFaceDetection = true;<br>
	 * 
	 * @return the mCameraOption
	 */
	public TuCameraOption cameraOption()
	{
		if (mCameraOption == null)
		{
			mCameraOption = new TuCameraOption();
			mCameraOption.setAvPostion(CameraFacing.Front);
			mCameraOption.setEnableFilters(true);
			// 自动选择分组滤镜指定的默认滤镜
			mCameraOption.setAutoSelectGroupDefaultFilter(true);
			mCameraOption.setDefaultFlashMode(CameraFlash.Off);
			mCameraOption.setSaveToTemp(true);
			mCameraOption.setEnableLongTouchCapture(true);
			mCameraOption.setAutoReleaseAfterCaptured(true);
			mCameraOption.setRegionViewColor(0xFF333333);
			// 视频视图显示比例类型 (默认:RatioType.ratio_all, 如果设置CameraViewRatio > 0,
			// 将忽略RatioType)
			mCameraOption.setRatioType(RatioType.ratio_all);
			// mCameraOption.setOutputSize(TuSdkSize.create(1088, 1988));
			// mCameraOption.setCameraViewRatio(0.5f);
			// 开启用户滤镜历史记录
			mCameraOption.setEnableFiltersHistory(true);
			// 开启在线滤镜
			mCameraOption.setEnableOnlineFilter(true);
			// 显示滤镜标题视图
			mCameraOption.setDisplayFiltersSubtitles(true);
			// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
			mCameraOption.enableFaceDetection = true;
		}
		return mCameraOption;
	}

	/**
	 * 裁剪与缩放控制器配置选项<br>
	 * 默认配置：<br>
	 * // 开启滤镜<br>
	 * mTuEditTurnAndCutOption.setEnableFilters(true);<br>
	 * // 裁剪大小<br>
	 * mTuEditTurnAndCutOption.setCutSize(new TuSdkSize(640, 640));<br>
	 * // 处理完毕保存到系统相册<br>
	 * mTuEditTurnAndCutOption.setSaveToAlbum(true);<br>
	 * // 是否在控制器结束后自动删除临时文件<br>
	 * mTuEditTurnAndCutOption.setAutoRemoveTemp(true);<br>
	 * // 开启用户滤镜历史记录<br>
	 * mTuEditTurnAndCutOption.setEnableFiltersHistory(true);<br>
	 * // 开启在线滤镜<br>
	 * mTuEditTurnAndCutOption.setEnableOnlineFilter(true);<br>
	 * // 显示滤镜标题视图<br>
	 * mTuEditTurnAndCutOption.setDisplayFiltersSubtitles(true);<br>
	 * 
	 * @return the mTuEditTurnAndCutOption
	 */
	public TuEditTurnAndCutOption editTurnAndCutOption()
	{
		if (mTuEditTurnAndCutOption == null)
		{
			mTuEditTurnAndCutOption = new TuEditTurnAndCutOption();
			mTuEditTurnAndCutOption.setEnableFilters(true);
			mTuEditTurnAndCutOption.setCutSize(new TuSdkSize(640, 640));
			mTuEditTurnAndCutOption.setSaveToAlbum(true);
			mTuEditTurnAndCutOption.setAutoRemoveTemp(true);
			// mTuEditTurnAndCutOption.setShowResultPreview(true);
			// 开启用户滤镜历史记录
			mTuEditTurnAndCutOption.setEnableFiltersHistory(true);
			// 开启在线滤镜
			mTuEditTurnAndCutOption.setEnableOnlineFilter(true);
			// 显示滤镜标题视图
			mTuEditTurnAndCutOption.setDisplayFiltersSubtitles(true);
		}
		return mTuEditTurnAndCutOption;
	}

	/** 头像设置配置选项 */
	public TuAvatarComponentOption()
	{

	}
}