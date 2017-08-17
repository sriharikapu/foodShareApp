/** 
 * TuSDKCore
 * TuPhotoListOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午1:20:28 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.modules.components.TuSdkComponentOption;

/**
 * 相册照片列表控制器配置选项
 * 
 * @author Clear
 */
public class TuPhotoListOption extends TuSdkComponentOption
{
	/**
	 * 相册照片列表控制器
	 * 
	 * @return 相册照片列表控制器 (默认: TuPhotoListFragment，如需自定义请继承自
	 *         TuPhotoListFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuPhotoListFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoListFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_album_photo_list_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuPhotoListFragment.getLayoutId();
	}

	/********************************** Config ***********************************/

	/** 行视图布局ID */
	private int mCellLayoutId;

	/** 分组头部视图布局ID */
	private int mHeaderLayoutId;

	/** 统计格式化字符 */
	private String mTotalFooterFormater;

	/** 空视图布局ID */
	private int mEmptyViewLayouId;
	
	/**
	 *  选择照片的尺寸限制 默认：CGSize(8000,8000)
	 */
	private TuSdkSize mMaxSelectionImageSize;

	/**
	 * 设置行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoListCell}
	 * @param resId
	 *            行视图布局ID (默认: tusdk_impl_component_album_photo_list_cell)
	 */
	public void setCellLayoutId(int resId)
	{
		mCellLayoutId = resId;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_photo_list_cell)
	 */
	public int getCellLayoutId()
	{
		if (mCellLayoutId == 0)
		{
			mCellLayoutId = TuPhotoListCell.getLayoutId();
		}
		return mCellLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoListHeader}
	 * @return 分组头部视图布局ID (默认: tusdk_impl_component_album_photo_list_header)
	 */
	public int getHeaderLayoutId()
	{
		if (mHeaderLayoutId == 0)
		{
			mHeaderLayoutId = TuPhotoListHeader.getLayoutId();
		}
		return mHeaderLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoListHeader}
	 * @param mHeaderLayoutId
	 *            分组头部视图布局ID (默认: tusdk_impl_component_album_photo_list_header)
	 */
	public void setHeaderLayoutId(int mHeaderLayoutId)
	{
		this.mHeaderLayoutId = mHeaderLayoutId;
	}

	/**
	 * 统计格式化字符
	 * 
	 * @return 统计格式化字符 (默认: lsq_album_total_format [%1$s 张照片])
	 */
	public String getTotalFooterFormater()
	{
		return mTotalFooterFormater;
	}

	/**
	 * 统计格式化字符
	 * 
	 * @param mTotalFooterFormater
	 *            统计格式化字符 (默认: lsq_album_total_format [%1$s 张照片])
	 */
	public void setTotalFooterFormater(String mTotalFooterFormater)
	{
		this.mTotalFooterFormater = mTotalFooterFormater;
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

	/**
	 * 选择照片的尺寸限制 默认：CGSize(8000,8000)
	 * 
	 * @param maxSelectionImageSize  默认：CGSize(8000,8000)
	 */
	public void setMaxSelectionImageSize(TuSdkSize maxSelectionImageSize) 
	{
		this.mMaxSelectionImageSize = maxSelectionImageSize;
	}
	
	/**
	 * 选择照片的尺寸限制 默认：CGSize(8000,8000)
	 *  
	 * @return TuSdkSize
	 */
	public TuSdkSize getMaxSelectionImageSize() 
	{
		if(mMaxSelectionImageSize == null)
		{
			mMaxSelectionImageSize = new TuSdkSize(8000, 8000);
		}
		
		return mMaxSelectionImageSize;
	}
	
	/** 相册照片列表控制器配置选项 */
	public TuPhotoListOption()
	{

	}

	/** 创建系统相册列表控制器对象 */
	public TuPhotoListFragment fragment()
	{
		TuPhotoListFragment fragment = this.fragmentInstance();
		// 设置选择照片的最大尺寸
		fragment.setMaxSelectionImageSize(this.getMaxSelectionImageSize());
		// 行视图布局ID
		fragment.setCellLayoutId(this.getCellLayoutId());
		// 分组头部视图布局ID
		fragment.setHeaderLayoutId(this.getHeaderLayoutId());
		// 统计格式化字符
		fragment.setTotalFooterFormater(this.getTotalFooterFormater());
		// 空视图布局ID
		fragment.setEmptyViewLayouId(this.getEmptyViewLayouId());

		return fragment;
	}
}