/** 
 * TuSDKCore
 * TuCameraFilterView.java
 *
 * @author 		Clear
 * @Date 		2015-2-11 上午10:48:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.camera;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.components.widget.filter.GroupFilterBar;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface.GroupFilterAction;
import org.lasque.tusdk.modules.view.widget.filter.TuCameraFilterViewBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 滤镜分组视图
 * 
 * @author Clear
 */
public class TuCameraFilterView extends TuCameraFilterViewBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_camera_filter_view");
	}

	/** 等待拍照状态激活毫秒 */
	public static final long CaptureActivateWaitMillis = 3000;

	/** 滤镜分组视图委托 */
	public interface TuCameraFilterViewDelegate
	{
		/**
		 * @param view
		 *            滤镜分组视图
		 * @param itemData
		 *            滤镜分组元素
		 * @param canCapture
		 *            是否允许拍摄
		 * @return 是否允许继续执行
		 */
		boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture);

		/**
		 * 滤镜分组视图显示状态改变
		 * 
		 * @param view
		 *            滤镜分组视图
		 * @param isShow
		 *            是否显示
		 */
		void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow);
	}

	public TuCameraFilterView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuCameraFilterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuCameraFilterView(Context context)
	{
		super(context);
	}

	/** 滤镜分组视图委托 */
	private TuCameraFilterViewDelegate mDelegate;

	/** 滤镜分组视图委托 */
	public TuCameraFilterViewDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 滤镜分组视图委托 */
	public void setDelegate(TuCameraFilterViewDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/** 通知委托 */
	private void notifyDelegate(boolean isShow)
	{
		this.showViewIn(isShow);
		if (mDelegate == null) return;
		mDelegate.onGroupFilterShowStateChanged(this, isShow);
	}

	/** 等待拍照状态激活毫秒 */
	@Override
	protected long getCaptureActivateWaitMillis()
	{
		return CaptureActivateWaitMillis;
	}

	/************** view ***************/
	/** 底部栏目 */
	private LinearLayout mBottomView;
	/** 滤镜组选择栏 */
	private GroupFilterBar mGroupFilterBar;
	/** 滤镜标题视图 */
	private FilterSubtitleViewInterface mFilterTitleView;
	/** 关闭按钮 */
	private View mCloseButton;

	/** 底部栏目 */
	public LinearLayout getBottomView()
	{
		if (mBottomView == null)
		{
			mBottomView = this.getViewById("lsq_group_bottom_view");
		}
		return mBottomView;
	}

	/** 滤镜标题视图 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & FilterSubtitleViewInterface> T getFilterTitleView()
	{
		if (mFilterTitleView == null)
		{
			View view = this.getViewById("lsq_filter_title_view");
			if (view == null || !(view instanceof FilterSubtitleViewInterface)) return null;
			mFilterTitleView = (FilterSubtitleViewInterface) view;
		}
		return (T) mFilterTitleView;
	}

	/** 滤镜组选择栏 */
	@SuppressWarnings("unchecked")
	@Override
	public GroupFilterBar getGroupFilterBar()
	{
		if (mGroupFilterBar == null)
		{
			mGroupFilterBar = this.getViewById("lsq_group_filter_bar");
			this.configGroupFilterBar(mGroupFilterBar, GroupFilterAction.ActionCamera);
		}
		return mGroupFilterBar;
	}

	/** 关闭按钮 */
	public View getCloseButton()
	{
		if (mCloseButton == null)
		{
			mCloseButton = this.getViewById("lsq_close_button");
			if (mCloseButton != null)
			{
				mCloseButton.setOnClickListener(mOnClickListener);
			}
		}
		return mCloseButton;
	}

	/** 选择一个滤镜组 */
	@Override
	protected boolean onGroupFilterSelected(GroupFilterItem itemData, boolean canCapture)
	{
		boolean result = true;
		if (mDelegate != null)
		{
			result = mDelegate.onGroupFilterSelected(this, itemData, canCapture);
		}
		return result;
	}

	/** 按钮点击事件 */
	protected OnSafeClickListener mOnClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getCloseButton()))
			{
				handleCloseAction();
			}
		}
	};

	/** 关闭视图 */
	protected void handleCloseAction()
	{
		if (this.isStateHidden() || this.getBottomView() == null) return;
		this.setStateHidden(true);
		this.exitRemoveState();

		ViewCompat.animate(this.getBottomView()).translationY(this.getBottomView().getHeight()).setDuration(220)
				.setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(View view)
					{
						view.setVisibility(View.INVISIBLE);
						notifyDelegate(false);
					}
				});
	}

	/** 显示滤镜组视图 */
	public void showGroupView()
	{
		if (!this.isStateHidden() || this.getBottomView() == null) return;
		this.setStateHidden(false);

		this.showViewIn(true);
		this.showViewIn(this.getBottomView(), true);

		ViewCompat.animate(this.getBottomView()).translationY(0).setDuration(220).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(null);
	}

	/** 加载视图 */
	@Override
	public void loadView()
	{
		super.loadView();
		// 底部栏目
		getBottomView();
		// 关闭按钮
		getCloseButton();
	}

	/** 设置默认显示状态 */
	@Override
	public void setDefaultShowState(boolean isShow)
	{
		if (this.getBottomView() == null)
		{
			this.showViewIn(false);
			return;
		}

		this.setStateHidden(!isShow);
		if (isShow)
		{
			ViewCompat.setTranslationY(this.getBottomView(), 0);
		}
		else
		{
			ViewCompat.setTranslationY(this.getBottomView(), this.getBottomView().getHeight());
		}
		this.showViewIn(isShow);
		this.showViewIn(this.getBottomView(), isShow);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!this.isStateHidden())
		{
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				this.handleCloseAction();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
}