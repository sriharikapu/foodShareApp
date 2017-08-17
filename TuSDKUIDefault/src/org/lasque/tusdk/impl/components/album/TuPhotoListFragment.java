/** 
 * TuSDK
 * TuPhotoListFragment.java
 *
 * @author 		Clear
 * @Date 		2015-8-30 下午12:37:34 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.io.File;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.impl.components.album.TuPhotoListView.TuPhotoListGridDelegate;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.album.TuPhotoListFragmentBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 相册照片列表控制器
 * 
 * @author Clear
 */
public class TuPhotoListFragment extends TuPhotoListFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_photo_list_fragment");
	}

	/** 相册照片列表控制器委托 */
	public interface TuPhotoListFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 选中相片
		 * 
		 * @param fragment
		 *            系统相册控制器
		 * @param imageSqlInfo
		 *            相片信息
		 */
		void onTuPhotoFragmentSelected(TuPhotoListFragment fragment, ImageSqlInfo imageSqlInfo);
	}

	/** 相册照片列表控制器 */
	public TuPhotoListFragment()
	{

	}

	/** 相册照片列表控制器 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/** 相册照片列表控制器委托 */
	private TuPhotoListFragmentDelegate mDelegate;

	/**
	 * 通知获取一个相册组
	 * 
	 * @param group
	 *            相册组
	 */
	@Override
	public void notifySelectedPhoto(ImageSqlInfo imageSqlInfo)
	{
		if (imageSqlInfo == null || !new File(imageSqlInfo.path).exists())
		{
			TuSdkContext.ins().toast(TuSdkContext.getString("lsq_album_broken_msg"));
			return;
		}
		
		// 验证图片是否过大
		if(imageSqlInfo.size.width > this.getMaxSelectionImageSize().width || imageSqlInfo.size.height > this.getMaxSelectionImageSize().height)
		{
			TuSdk.messageHub().showError(this.getContext(),TuSdkContext.getString("lsq_album_image_size_limited", this.getMaxSelectionImageSize().width,this.getMaxSelectionImageSize().height));
			return;
		}

		if (this.mDelegate == null) return;
		this.mDelegate.onTuPhotoFragmentSelected(this, imageSqlInfo);
	}

	/** 相册照片列表控制器委托 */
	public TuPhotoListFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 相册照片列表控制器委托 */
	public void setDelegate(TuPhotoListFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/*************************** config *******************************/

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
		if (mTotalFooterFormater == null)
		{
			mTotalFooterFormater = TuSdkContext.getString("lsq_album_total_format");
		}
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

	/*************************** view *******************************/
	/** 相片列表视图 */
	private TuPhotoListView mListView;

	/** 相片列表视图 */
	public TuPhotoListView getListView()
	{
		if (mListView == null)
		{
			mListView = this.getViewById("lsq_listView");
			if (mListView != null)
			{
				mListView.setCellLayoutId(this.getCellLayoutId());
				mListView.setHeaderLayoutId(this.getHeaderLayoutId());
				mListView.setTotalFooterFormater(this.getTotalFooterFormater());
				mListView.setEmptyView(this.getEmptyViewLayouId());
			}
		}
		return mListView;
	}

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		TuPhotoListView listView = this.getListView();
		if (listView != null)
		{
			mListView.setGridDelegate(new GridItemClickDelegate());
			listView.loadPhotos(this.getAlbumInfo());
		}
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);

		if (this.getListView() != null)
		{
			this.getListView().emptyNeedFullHeight();
		}
	}

	/** 初始化导航栏 */
	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		if (this.getAlbumInfo() != null) this.setTitle(this.getAlbumInfo().title);

		this.setNavRightButton(this.getResString("lsq_nav_cancel"), TuSdkContext.getColor("lsq_navigator_button_right_title"));
	}

	/** 取消按钮 */
	@Override
	public void navigatorBarRightAction(NavigatorBarButtonInterface button)
	{
		this.dismissActivityWithAnim();
	}

	/** 单元格点击事件 */
	private class GridItemClickDelegate implements TuPhotoListGridDelegate
	{
		/**
		 * 点击单元格
		 */
		@Override
		public void onGridItemClick(TuPhotoListGrid view, ImageSqlInfo data)
		{
			if (isHidden()) return;
			notifySelectedPhoto(data);
		}
	}
}