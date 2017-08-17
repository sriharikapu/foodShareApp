/** 
 * TuSDKCore
 * FilterTitleView.java
 *
 * @author 		Clear
 * @Date 		2015-2-13 下午9:19:51 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewBase;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 滤镜标题视图
 * 
 * @author Clear
 */
public class FilterTitleView extends FilterSubtitleViewBase implements FilterSubtitleViewInterface
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_filter_title_view");
	}

	public FilterTitleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public FilterTitleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FilterTitleView(Context context)
	{
		super(context);
	}

	/** 滤镜标题视图 */
	private TextView mFilterTitleView;
	/** 滤镜组标题视图 */
	private TextView mGroupTitleView;

	/** 滤镜标题视图 */
	public TextView getFilterTitleView()
	{
		if (mFilterTitleView == null)
		{
			mFilterTitleView = this.getViewById("lsq_filter_title");
		}
		return mFilterTitleView;
	}

	/** 滤镜标题视图 */
	public TextView getGroupTitleView()
	{
		if (mGroupTitleView == null)
		{
			mGroupTitleView = this.getViewById("lsq_filter_group_title");
		}
		return mGroupTitleView;
	}

	@Override
	public void loadView()
	{
		super.loadView();
		getFilterTitleView();
		getGroupTitleView();
	}

	/** 设置滤镜参数 */
	@Override
	public void setFilter(FilterOption option)
	{
		if (option == null)
		{
			this.setTextViewText(this.getFilterTitleView(), this.getResString("lsq_filter_Normal"));
			this.setTextViewText(this.getGroupTitleView(), null);
		}
		else
		{
			this.setTextViewText(this.getFilterTitleView(), option.getName());
			this.setTextViewText(this.getGroupTitleView(), this.getGroupName(option));
		}

		/** 启动缩放动画 */
		this.startScaleAnimation();
	}
}