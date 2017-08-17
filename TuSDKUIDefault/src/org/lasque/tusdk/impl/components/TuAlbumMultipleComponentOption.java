/** 
 * TuSDKCore
 * TuAlbumComponentOption.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午1:06:23 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.impl.components.album.TuAlbumMultipleListOption;
import org.lasque.tusdk.impl.components.camera.TuCameraOption;

/**
 * 系统相册组件配置选项
 * 
 * @author Clear
 */
public class TuAlbumMultipleComponentOption
{
	/**
	 * 系统相册控制器配置选项
	 */
	private TuAlbumMultipleListOption mAlbumListOption;

	/**
	 * 系统相册控制器配置选项
	 */
	private TuCameraOption mCameraOption;

	/**
	 * 系统相册控制器配置选项
	 * 默认配置：
	 * 默认: true, 如果没有设定相册组名称，自动跳转到系统相册组
	 * mAlbumListOption.setAutoSkipToPhotoList(true);
	 * 
	 * @return the mAlbumListOption
	 */
	public TuAlbumMultipleListOption albumListOption()
	{
		if (mAlbumListOption == null)
		{
			mAlbumListOption = new TuAlbumMultipleListOption();
		}
		return mAlbumListOption;
	}

	/**
	 * 系统相册控制器配置选项
	 * 
	 * @return the mCameraListOption
	 */
	public TuCameraOption cameraOption()
	{
		if (mCameraOption == null)
		{
			mCameraOption = new TuCameraOption();
			
			// 是否开启滤镜支持 (默认: 关闭)
			mCameraOption.setEnableFilters(true);
			// 开启滤镜配置选项
			mCameraOption.setEnableFilterConfig(true);
			// 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册) 
			mCameraOption.setDisplayAlbumPoster(true);
			// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
			mCameraOption.setAutoReleaseAfterCaptured(true);
			// 开启长按拍摄 (默认：false)
			mCameraOption.setEnableLongTouchCapture(true);
			// 开启用户滤镜历史记录
			mCameraOption.setEnableFiltersHistory(true);
			// 开启在线滤镜
			mCameraOption.setEnableOnlineFilter(true);
			// 显示滤镜标题视图
			mCameraOption.setDisplayFiltersSubtitles(true);
			// 需要保存为临时文件
			mCameraOption.setSaveToTemp(true);
			
		}
		return mCameraOption;
	}

	/**
	 * 系统相册组件配置选项
	 */
	public TuAlbumMultipleComponentOption()
	{

	}

}
