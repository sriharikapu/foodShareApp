/** 
 * TuSDKCore
 * StickerListGrid.java
 *
 * @author 		Clear
 * @Date 		2014-12-30 下午6:28:04 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.view.TuSdkImageView;
import org.lasque.tusdk.modules.view.widget.sticker.StickerListGridBase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 贴纸预览单元格视图
 * 
 * @author Clear
 */
public class StickerListGrid extends StickerListGridBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_list_grid");
	}

	public StickerListGrid(Context context)
	{
		super(context);
	}

	public StickerListGrid(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerListGrid(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 包装视图 */
	private RelativeLayout mWrapView;

	/** 图片视图 */
	private TuSdkImageView mPosterView;

	/** 包装视图 */
	public RelativeLayout getWrapView()
	{
		if (mWrapView == null)
		{
			mWrapView = this.getViewById("lsq_posterWrap");
		}
		return mWrapView;
	}

	/** 图片视图 */
	@Override
	public TuSdkImageView getPosterView()
	{
		if (mPosterView == null)
		{
			mPosterView = this.getViewById("lsq_posterView");
		}
		return mPosterView;
	}

	@Override
	protected void onLayouted()
	{
		super.onLayouted();
		if (this.getPosterView() != null)
		{
			this.getPosterView().setCornerRadius(this.getPosterView().getWidth() / 2);
		}
	}

	/** 设置图片边距 */
	public void setInnerWarpSpace(int space)
	{
		if (this.getWrapView() == null || space < 0) return;
		this.setMargin(this.getWrapView(), space, space, space, space);
	}

	/** 设置图片边距 */
	public void setInnerWarpSpaceDP(int space)
	{
		this.setInnerWarpSpace(ContextUtils.dip2px(this.getContext(), space));
	}
}