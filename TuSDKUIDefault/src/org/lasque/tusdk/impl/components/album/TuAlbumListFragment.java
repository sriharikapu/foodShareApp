/** 
 * TuSDK
 * TuAlbumListFragment.java
 *
 * @author 		Clear
 * @Date 		2015-8-30 下午12:08:20 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.listview.TuSdkArrayListView.ArrayListViewItemClickListener;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.album.TuAlbumListFragmentBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 系统相册控制器
 * 
 * @author Clear
 */
public class TuAlbumListFragment extends TuAlbumListFragmentBase
{

	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_list_fragment");
	}

	/** 系统相册委托 */
	public interface TuAlbumListFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 选中相册组
		 * 
		 * @param fragment
		 *            系统相册控制器
		 * @param group
		 *            相册组
		 */
		void onTuAlbumFragmentSelected(TuAlbumListFragment fragment, AlbumSqlInfo group);
	}

	/** 系统相册控制器 */
	public TuAlbumListFragment()
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

	/** 系统相册委托 */
	private TuAlbumListFragmentDelegate mDelegate;

	/** 系统相册列表 */
	private List<AlbumSqlInfo> mGroups;

	/** 系统相册列表 */
	@Override
	public List<AlbumSqlInfo> getGroups()
	{
		return mGroups;
	}

	/**
	 * 系统相册委托
	 * 
	 * @return the delegate
	 */
	public TuAlbumListFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/**
	 * 系统相册委托
	 * 
	 * @param delegate
	 *            the delegate to set
	 */
	public void setDelegate(TuAlbumListFragmentDelegate delegate)
	{
		this.mDelegate = delegate;
		this.setErrorListener(mDelegate);
	}

	/*************************** config *******************************/
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

	/**
	 * 空视图布局ID
	 * 
	 * @return the mEmptyViewLayouId
	 */
	public int getEmptyViewLayouId()
	{
		if (mEmptyViewLayouId == 0)
		{
			mEmptyViewLayouId = TuAlbumEmptyView.getLayoutId();
		}
		return mEmptyViewLayouId;
	}

	/**
	 * 空视图布局ID
	 * 
	 * @param mEmptyViewLayouId
	 *            the mEmptyViewLayouId to set
	 */
	public void setEmptyViewLayouId(int mEmptyViewLayouId)
	{
		this.mEmptyViewLayouId = mEmptyViewLayouId;
	}

	/*************************** view *******************************/

	/** 相册列表视图 */
	private TuAlbumListView mListView;

	/** 相册列表视图 */
	public TuAlbumListView getListView()
	{
		if (mListView == null)
		{
			mListView = this.getViewById("lsq_listView");
			mListView.setCellLayoutId(this.getCellLayoutId());
			mListView.setEmptyView(this.getEmptyViewLayouId());
		}
		return mListView;
	}

	/** 通知获取一个相册组 */
	@Override
	public void notifySelectedGroup(AlbumSqlInfo group)
	{
		if (this.mDelegate == null) return;
		this.mDelegate.onTuAlbumFragmentSelected(this, group);
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
			initView();
		}
		else
		{
			String msg = TuSdkContext.getString("lsq_album_no_access", ContextUtils.getAppName(getContext()));
			
			TuSdkViewHelper.alert(permissionAlertDelegate, this.getContext(), TuSdkContext.getString("lsq_album_alert_title"), 
					msg, TuSdkContext.getString("lsq_button_close"), TuSdkContext.getString("lsq_button_setting")
			);
		}
	}
	
	/** 初始化视图，loadView 之后调用 */
	protected void initView()
	{
		hubStatus(TuSdkContext.getString("lsq_refresh_list_view_state_loading"));
		
		ThreadHelper.runThread(new Runnable() {
			
			@Override
			public void run() 
			{
				mGroups = ImageSqlHelper.getAlbumList(getActivity());
					
				runOnUiThread(new Runnable() {
					@Override
					public void run() 
					{
						TuAlbumListView listView = getListView();
						if (listView != null)
						{
							listView.setItemClickListener(new ListItemClickDelegate());
							listView.setModeList(mGroups);
						}
						
						hubDismiss();
					}
				});
				
				autoSelectedAblumGroupAction(getGroups());
			}
		});
	}

	/** 初始化导航栏 */
	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		this.setTitle(TuSdkContext.getString("lsq_album_title"));
		this.setNavRightButton(TuSdkContext.getString("lsq_nav_cancel"), TuSdkContext.getColor("lsq_navigator_button_right_title"));
	}

	/** 取消按钮 */
	@Override
	public void navigatorBarRightAction(NavigatorBarButtonInterface button)
	{
		this.dismissActivityWithAnim();
	}

	/** 初始化视图 */
	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);

		if (this.getListView() != null)
		{
			this.getListView().emptyNeedFullHeight();
		}
		
		if (hasRequiredPermission())
		{
			initView();
		}
		else
		{
			requestRequiredPermissions();
		}
	}
	
	@Override
	public void onDestroyView() 
	{
		hubDismissRightNow();
		super.onDestroyView();
	}

	/** 列表项点击事件委托 */
	private class ListItemClickDelegate implements ArrayListViewItemClickListener<AlbumSqlInfo, TuAlbumListCell>
	{
		/**
		 * 列表项点击事件
		 * 
		 * @param itemData
		 *            数据
		 * @param itemView
		 *            视图
		 * @param indexPath
		 *            索引
		 */
		@Override
		public void onArrayListViewItemClick(AlbumSqlInfo itemData, TuAlbumListCell itemView, TuSdkIndexPath indexPath)
		{
			if (isHidden()) return;
			notifySelectedGroup(itemData);
		}
	}
}
