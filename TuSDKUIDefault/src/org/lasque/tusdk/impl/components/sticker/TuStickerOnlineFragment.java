/** 
 * TuSDKCore
 * TuStickerOnlineFragment.java
 *
 * @author 		Clear
 * @Date 		2015-3-21 下午3:05:42 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkWebView;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.modules.components.sticker.TuStickerOnlineFragmentBase;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * 在线贴纸控制器
 * 
 * @author Clear
 */
public class TuStickerOnlineFragment extends TuStickerOnlineFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_activity_webview_fragment");
	}

	/** 在线贴纸控制器委托 */
	public interface TuStickerOnlineFragmentDelegate
	{
		/**
		 * 选中一个贴纸
		 * 
		 * @param fragment
		 *            在线贴纸控制器
		 * @param data
		 *            贴纸数据
		 */
		void onTuStickerOnlineFragmentSelected(TuStickerOnlineFragment fragment, StickerData data);
	}

	/** 在线贴纸控制器委托 */
	private TuStickerOnlineFragmentDelegate mDelegate;

	/** 在线贴纸控制器委托 */
	public TuStickerOnlineFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 在线贴纸控制器委托 */
	public void setDelegate(TuStickerOnlineFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
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

	/************************* view ******************************/
	/** 横向进度条 */
	private ProgressBar mProgressBar;
	/** web视图 */
	private TuSdkWebView mWebview;

	/** 横向进度条 */
	public ProgressBar getProgressBar()
	{
		if (mProgressBar == null)
		{
			mProgressBar = this.getViewById("lsq_progress_bar");
		}
		return mProgressBar;
	}

	/** web视图 */
	@Override
	public TuSdkWebView getWebview()
	{
		if (mWebview == null)
		{
			mWebview = this.getViewById("lsq_webview");
			mWebview.setProgressBar(this.getProgressBar());
		}
		return mWebview;
	}

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
	}

	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		super.navigatorBarLoaded(navigatorBar);

		this.setTitle(this.getResString("lsq_sticker_online_title"));
		this.setNavLeftButton(this.getResString("lsq_nav_back"));
		this.setNavRightButton(this.getResString("lsq_nav_cancel"), TuSdkContext.getColor("lsq_navigator_button_right_title"));
	}
	
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(null);
	}

	/** 选中对象ID */
	@Override
	protected void onHandleSelected(StickerData data)
	{
		if (this.getDelegate() == null || data == null) return;
		this.getDelegate().onTuStickerOnlineFragmentSelected(this, data);
	}

	/** 选中对象ID */
	@Override
	protected void onHandleDetail(long id)
	{
		TuStickerOnlineFragment fragment = new TuStickerOnlineFragment();
		fragment.setDelegate(this.getDelegate());
		fragment.setDetailDataId(id);
		this.pushFragment(fragment);
	}
}