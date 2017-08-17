/** 
 * TuSDKCore
 * TuAlbumListOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午1:13:36 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.modules.components.TuSdkComponentOption;

/**
 * 系统相册控制器配置选项
 * 
 * @author Clear
 */
public class TuAlbumListOption extends TuSdkComponentOption
{
	/**
	 * 系统相册列表控制类
	 * 
	 * @param mComponentClazz
	 *            系统相册列表控制类 (默认: TuAlbumListFragment，如需自定义请继承自
	 *            TuAlbumListFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuAlbumListFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumListFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_album_list_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuAlbumListFragment.getLayoutId();
	}

	/********************************** Config ***********************************/
	/** 是否禁用自动选择相册组 (默认: false, 如果没有设定相册组名称，自动跳转到系统相册组) */
	private boolean mDisableAutoSkipToPhotoList;

	/** 需要自动跳转到相册组名称 */
	private String mSkipAlbumName;

	/** 行视图布局ID */
	private int mCellLayoutId;

	/** 空视图布局ID */
	private int mEmptyViewLayouId;

	/**
	 * 设置行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumListCell}
	 * @param resId
	 *            行视图布局ID (默认: tusdk_impl_component_album_list_cell)
	 */
	public void setCellLayoutId(int resId)
	{
		mCellLayoutId = resId;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_list_cell)
	 */
	public int getCellLayoutId()
	{
		if (mCellLayoutId == 0)
		{
			mCellLayoutId = TuAlbumListCell.getLayoutId();
		}
		return mCellLayoutId;
	}

	/** 空视图布局ID */
	public int getEmptyViewLayouId()
	{
		if (mEmptyViewLayouId == 0)
		{
			mEmptyViewLayouId = TuAlbumEmptyView.getLayoutId();
		}
		return mEmptyViewLayouId;
	}

	/** 空视图布局ID */
	public void setEmptyViewLayouId(int mEmptyViewLayouId)
	{
		this.mEmptyViewLayouId = mEmptyViewLayouId;
	}

	/** 是否禁用自动选择相册组 (默认: false, 如果没有设定相册组名称，自动跳转到系统相册组) */
	public boolean isDisableAutoSkipToPhotoList()
	{
		return mDisableAutoSkipToPhotoList;
	}

	/** 是否禁用自动选择相册组 (默认: false, 如果没有设定相册组名称，自动跳转到系统相册组) */
	public void setDisableAutoSkipToPhotoList(boolean mDisableAutoSkipToPhotoList)
	{
		this.mDisableAutoSkipToPhotoList = mDisableAutoSkipToPhotoList;
	}

	/** 需要自动跳转到相册组名称 */
	public String getSkipAlbumName()
	{
		return mSkipAlbumName;
	}

	/** 需要自动跳转到相册组名称 */
	public void setSkipAlbumName(String skipAlbumName)
	{
		this.mSkipAlbumName = skipAlbumName;
	}

	/**
	 * 系统相册控制器配置选项
	 */
	public TuAlbumListOption()
	{

	}

	/**
	 * 创建系统相册列表控制器对象
	 * 
	 * @return 系统相册列表控制器对象
	 */
	public TuAlbumListFragment fragment()
	{
		TuAlbumListFragment fragment = this.fragmentInstance();

		// 行视图布局ID
		fragment.setCellLayoutId(this.getCellLayoutId());
		// 空视图布局ID
		fragment.setEmptyViewLayouId(this.getEmptyViewLayouId());
		// 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
		fragment.setSkipAlbumName(this.getSkipAlbumName());
		// 是否禁用自动选择相册组 (默认: false, 如果没有设定相册组名称，自动跳转到系统相册组)
		fragment.setDisableAutoSkipToPhotoList(this.isDisableAutoSkipToPhotoList());
		return fragment;
	}
}