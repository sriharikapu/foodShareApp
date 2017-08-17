/** 
 * TuSDK
 * TuAlbumComponentOption.java
 *
 * @author 		Clear
 * @Date 		2015-8-30 下午1:25:03 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.impl.components.album.TuAlbumListOption;
import org.lasque.tusdk.impl.components.album.TuPhotoListOption;

/**
 * 系统相册组件配置选项
 * 
 * @author Clear
 */
public class TuAlbumComponentOption
{
	/** 系统相册控制器配置选项 */
	private TuAlbumListOption mAlbumListOption;

	/** 系统相册控制器配置选项 */
	private TuPhotoListOption mPhotoListOption;

	/** 系统相册控制器配置选项 */
	public TuAlbumListOption albumListOption()
	{
		if (mAlbumListOption == null)
		{
			mAlbumListOption = new TuAlbumListOption();
		}
		return mAlbumListOption;
	}

	/** 系统相册控制器配置选项 */
	public TuPhotoListOption photoListOption()
	{
		if (mPhotoListOption == null)
		{
			mPhotoListOption = new TuPhotoListOption();
		}
		return mPhotoListOption;
	}

	/**
	 * 系统相册组件配置选项
	 */
	public TuAlbumComponentOption()
	{

	}
}