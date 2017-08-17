/** 
 * TuSDKCore
 * TuEditTurnAndCutFilterView.java
 *
 * @author 		Clear
 * @Date 		2015-2-15 下午3:35:37 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;
import org.lasque.tusdk.impl.components.widget.filter.GroupFilterBar;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem.GroupFilterItemType;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface.GroupFilterAction;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

/**
 * 默认滤镜视图
 * 
 * @author Clear
 */
public class TuNormalFilterView extends GroupFilterBaseView
{
	/** 默认滤镜视图委托 */
	public interface TuNormalFilterViewDelegate
	{
		/**
		 * 选中一个滤镜
		 * 
		 * @param view
		 *            默认滤镜视图
		 * @param itemData
		 *            滤镜分组元素
		 * @return
		 */
		boolean onTuNormalFilterViewSelected(TuNormalFilterView view, GroupFilterItem itemData);
	}

	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_normal_filter_view");
	}

	public TuNormalFilterView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuNormalFilterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuNormalFilterView(Context context)
	{
		super(context);
	}

	/** 默认滤镜视图委托 */
	private TuNormalFilterViewDelegate mDelegate;

	/** 默认滤镜视图委托 */
	public TuNormalFilterViewDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 默认滤镜视图委托 */
	public void setDelegate(TuNormalFilterViewDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/** 滤镜组选择栏 */
	private GroupFilterBar mGroupFilterBar;
	/** 滤镜标题视图 */
	private FilterSubtitleViewInterface mFilterTitleView;

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
			this.configGroupFilterBar(mGroupFilterBar, GroupFilterAction.ActionNormal);
		}
		return mGroupFilterBar;
	}

	/** 开关视图 */
	public void toggleBarShowState()
	{
		this.setStateHidden(!this.isStateHidden());
		this.showView(this, true);
		if (this.getGroupFilterBar() == null) return;

		int transY = 0;
		if (this.isStateHidden())
		{
			transY = this.getGroupFilterBar().getHeight();
			this.exitRemoveState();
		}

		ViewPropertyAnimatorListener listener = new TuSdkViewAnimatorAdapter()
		{
			@Override
			public void onAnimationEnd(View view, boolean cancelled)
			{
				if (cancelled) return;

				if (isStateHidden()) showViewIn(false);
			}
		};
		ViewCompat.animate(this.getGroupFilterBar()).translationY(transY).setDuration(220).setInterpolator(new AccelerateDecelerateInterpolator())
				.setListener(listener);
	}

	/** 设置默认显示状态 */
	@Override
	public void setDefaultShowState(boolean isShow)
	{
		if (this.getGroupFilterBar() == null)
		{
			this.showViewIn(false);
			return;
		}

		this.setStateHidden(!isShow);

		if (isShow)
		{
			ViewCompat.setTranslationY(this.getGroupFilterBar(), 0);
		}
		else
		{
			ViewCompat.setTranslationY(this.getGroupFilterBar(), this.getGroupFilterBar().getHeight());
		}
		this.showViewIn(isShow);
	}

	/**
	 * 选中一个滤镜数据
	 * 
	 * @param filterBar
	 *            滤镜组选择栏
	 * @param itemView
	 *            滤镜分组元素视图
	 * @param itemData
	 *            滤镜分组元素
	 * @return 是否允许继续执行
	 */
	@Override
	protected boolean onDispatchGroupFilterSelected(GroupFilterBarInterface filterBar, GroupFilterItemViewInterface itemView, GroupFilterItem itemData)
	{
		if (mDelegate == null) return true;

		if (itemData.type == GroupFilterItemType.TypeFilter)
		{
			if (!this.notifyTitle(itemView, itemData)) return true;
		}
		return mDelegate.onTuNormalFilterViewSelected(this, itemData);
	}
}