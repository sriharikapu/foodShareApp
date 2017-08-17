/** 
 * TuSDKCore
 * TuFilterOnlineFragment.java
 *
 * @author 		Clear
 * @Date 		2015-5-18 下午2:47:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkWebView;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentBase;
import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * 在线滤镜控制器
 * 
 * @author Clear
 */
public class TuFilterOnlineFragment extends TuFilterOnlineFragmentBase implements TuFilterOnlineFragmentInterface
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_activity_webview_fragment");
	}

	/** 在线滤镜控制器委托 */
	private TuFilterOnlineFragmentDelegate mDelegate;

	/** 在线滤镜控制器委托 */
	public TuFilterOnlineFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 在线滤镜控制器委托 */
	@Override
	public void setDelegate(TuFilterOnlineFragmentDelegate mDelegate)
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
		
		this.wantFullScreen(true);
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

		this.setTitle(this.getResString("lsq_filter_online_title"));
		this.setNavRightButton(this.getResString("lsq_nav_cancel"), TuSdkContext.getColor("lsq_navigator_button_right_title"));
	}
	
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(null);
	}

	/** 选中对象ID */
	protected void onHandleSelected(long groupId)
	{
		if (this.getDelegate() == null) return;
		this.getDelegate().onTuFilterOnlineFragmentSelected(this, groupId);
	}

	/** 选中对象ID */
	protected void onHandleDetail(long groupId)
	{
		TuFilterOnlineFragment fragment = new TuFilterOnlineFragment();
		fragment.setDelegate(this.getDelegate());
		fragment.setDetailDataId(groupId);
		this.pushFragment(fragment);
	}
}