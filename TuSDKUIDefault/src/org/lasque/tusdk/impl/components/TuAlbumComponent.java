/** 
 * TuSDK
 * TuAlbumComponent.java
 *
 * @author 		Clear
 * @Date 		2015-8-30 下午1:26:29 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.components.album.TuAlbumListFragment;
import org.lasque.tusdk.impl.components.album.TuAlbumListFragment.TuAlbumListFragmentDelegate;
import org.lasque.tusdk.impl.components.album.TuPhotoListFragment;
import org.lasque.tusdk.impl.components.album.TuPhotoListFragment.TuPhotoListFragmentDelegate;
import org.lasque.tusdk.modules.components.TuAlbumComponentBase;

import android.app.Activity;

/**
 * 系统相册组件
 * 
 * @author Clear
 */
public class TuAlbumComponent extends TuAlbumComponentBase implements TuAlbumListFragmentDelegate, TuPhotoListFragmentDelegate
{
	/** 系统相册组件配置选项 */
	private TuAlbumComponentOption mComponentOption;

	/** 系统相册组件配置选项 */
	public TuAlbumComponentOption componentOption()
	{
		if (mComponentOption == null)
		{
			mComponentOption = new TuAlbumComponentOption();
		}
		return mComponentOption;
	}

	/** 系统相册组件配置选项 */
	public void setComponentOption(TuAlbumComponentOption mComponentOption)
	{
		this.mComponentOption = mComponentOption;
	}

	/**
	 * 启动系统相册组件
	 * 
	 * @param activity
	 *            内容上下文
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuAlbumComponent component(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuAlbumComponent component = new TuAlbumComponent(activity);
		component.setDelegate(delegate);
		return component;
	}

	public TuAlbumComponent(Activity activity)
	{
		super(activity);
	}

	@Override
	protected void initComponent()
	{

	}

	@Override
	public TuAlbumComponentBase showComponent()
	{
		this.handleShowAlbum();
		return this;
	}

	/************************* handleShowAlbum ************************/
	/** 显示相册 */
	protected void handleShowAlbum()
	{
		// 无法存储文件
		if (this.showAlertIfCannotSaveFile()) return;

		TuAlbumListFragment fragment = this.componentOption().albumListOption().fragment();
		fragment.setDelegate(this);
		this.presentModalNavigationActivity(fragment, true);
	}

	/**
	 * 选中相册组
	 * 
	 * @param fragment
	 *            系统相册控制器
	 * @param group
	 *            相册组
	 */
	@Override
	public void onTuAlbumFragmentSelected(TuAlbumListFragment fragment, AlbumSqlInfo group)
	{
		TuPhotoListFragment photoFragment = this.componentOption().photoListOption().fragment();
		photoFragment.setAlbumInfo(group);
		photoFragment.setDelegate(this);
		fragment.pushFragment(photoFragment);
	}

	/**
	 * 选中相片
	 * 
	 * @param fragment
	 *            系统相册控制器
	 * @param imageSqlInfo
	 *            相片信息
	 */
	@Override
	public void onTuPhotoFragmentSelected(TuPhotoListFragment fragment, ImageSqlInfo imageSqlInfo)
	{
		TuSdkResult result = new TuSdkResult();
		result.imageSqlInfo = imageSqlInfo;
		this.notifyResult(result, null, fragment);
	}
}