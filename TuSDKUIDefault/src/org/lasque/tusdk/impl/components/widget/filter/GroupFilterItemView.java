/** 
 * TuSDKCore
 * GroupFilterItemView.java
 *
 * @author 		Clear
 * @Date 		2015-2-11 下午1:46:39 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewBase;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 滤镜元素视图
 * 
 * @author Clear
 */
public class GroupFilterItemView extends GroupFilterItemViewBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_group_filter_item_view");
	}

	public GroupFilterItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public GroupFilterItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GroupFilterItemView(Context context)
	{
		super(context);
	}

	/***************** view *******************/
	/** 包装视图 */
	private RelativeLayout mWrapView;
	/** 图片视图 */
	private ImageView mImageView;
	/** 标题视图 */
	private TextView mTitleView;
	/** 选中状态视图 */
	private RelativeLayout mSelectedView;
	/** 图标视图 */
	private ImageView mIconView;

	/** 包装视图 */
	public RelativeLayout getWrapView()
	{
		if (mWrapView == null) mWrapView = this.getViewById("lsq_item_wrap");
		return mWrapView;
	}

	/** 图片视图 */
	@Override
	public ImageView getImageView()
	{
		if (mImageView == null) mImageView = this.getViewById("lsq_item_image");
		return mImageView;
	}

	/** 标题视图 */
	public TextView getTitleView()
	{
		if (mTitleView == null) mTitleView = this.getViewById("lsq_item_title");
		return mTitleView;
	}

	/** 获取选中视图 */
	public RelativeLayout getSelectedView()
	{
		if (mSelectedView == null) mSelectedView = this.getViewById("lsq_item_selected");
		return mSelectedView;
	}

	/** 图标视图 */
	public ImageView getIconView()
	{
		if (mIconView == null) mIconView = this.getViewById("lsq_item_icon");
		return mIconView;
	}

	@Override
	public void loadView()
	{
		super.loadView();
		this.getWrapView();
		this.getImageView();
		this.getTitleView();
		this.getSelectedView();
		this.getIconView();
	}

	/** 设置选中图标 */
	@Override
	protected void setSelectedIcon(GroupFilterItem model, boolean needIcon)
	{
		ImageView iconView = this.getIconView();
		if (iconView == null) return;

		// 相机时需要到激活状态才显示图标
		this.showViewIn(iconView, !this.isCameraAction());
		if (!needIcon) return;

		int resId = 0;

		switch (this.getAction())
			{
			case ActionEdit:
				if(isDisplaySelectionIcon())
					resId = TuSdkContext.getDrawableResId("lsq_style_default_filter_adjust");
				break;
			case ActionCamera:
				resId = TuSdkContext.getDrawableResId("lsq_style_default_filter_capture");
				break;
			default:
				break;
			}

		if (resId == 0) return;
		iconView.setImageResource(resId);
	}

	/** 原始效果 */
	@Override
	protected void handleTypeOrgin(GroupFilterItem model)
	{
		if (this.getImageView() == null) return;

		this.setTextViewText(this.getTitleView(), this.getResString("lsq_filter_Normal"));
		if (this.isRenderFilterThumb()) super.handleTypeOrgin(model);
		else
		{
			this.getImageView().setImageResource(TuSdkContext.getDrawableResId("lsq_style_default_filter_normal"));
		}

		this.getImageView().setScaleType(ScaleType.CENTER_CROP);
	}

	/** 滤镜分组 */
	@Override
	protected void handleTypeGroup(GroupFilterItem model)
	{
		if (model.filterGroup == null) return;
		this.setTextViewText(this.getTitleView(), model.filterGroup.getName());

		super.handleTypeGroup(model);
	}

	/** 滤镜 */
	@Override
	protected void handleTypeFilter(GroupFilterItem model)
	{
		if (model.filterOption == null) return;
		this.setTextViewText(this.getTitleView(), model.filterOption.getName());

		super.handleTypeFilter(model);
	}

	/** 历史 */
	@Override
	protected void handleTypeHistory(GroupFilterItem model)
	{
		this.handleBlockView(GroupFilterItem.Backgroud_History, TuSdkContext.getDrawableResId("lsq_style_default_filter_history"));
	}

	/** 在线滤镜 */
	@Override
	protected void handleTypeOnlie(GroupFilterItem model)
	{
		this.handleBlockView(GroupFilterItem.Backgroud_Online, TuSdkContext.getDrawableResId("lsq_style_default_filter_online"));
	}

	/** 设置功能块视图 */
	@Override
	protected void handleBlockView(int color, int icon)
	{
		super.handleBlockView(color, icon);
		this.setImageColor(color);
	}

	@Override
	public void viewNeedRest()
	{
		super.viewNeedRest();

		this.setTextViewText(this.getTitleView(), null);
		this.showViewIn(this.getWrapView(), true);
		this.showViewIn(this.getSelectedView(), false);
		this.setImageColor(Color.TRANSPARENT);

		if (this.getIconView() != null)
		{
			this.getIconView().setImageBitmap(null);
			this.showViewIn(this.getIconView(), false);
		}
	}

	/** 设置包装视图颜色 */
	protected void setImageColor(int color)
	{
		if (this.getImageView() == null) return;
		this.getImageView().setBackgroundColor(color);
	}

	@Override
	public void onCellSelected(int position)
	{
		super.onCellSelected(position);
		this.showViewIn(this.getSelectedView(), true);

		if (this.getModel() != null && this.getModel().type == GroupFilterItem.GroupFilterItemType.TypeHolder)
		{
			this.showViewIn(this.getSelectedView(), false);
		}
	}

	@Override
	public void onCellDeselected()
	{
		super.onCellDeselected();
		this.showViewIn(this.getSelectedView(), false);

		if (this.getTitleView() == null) return;
		this.getTitleView().setTextColor(TuSdkContext.getColor("lsq_color_white"));
		this.getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_PX, TuSdkContext.getDimen("lsq_font_size_20"));
	}

	/**
	 * 启动激活状态
	 * 
	 * @param waitMillis
	 *            等待毫秒
	 */
	@Override
	public void waitInActivate(long waitMillis)
	{
		if (this.isActivating()) return;
		this.showViewIn(this.getIconView(), true);
		ViewCompat.setAlpha(this.getIconView(), 1);
		ViewCompat.animate(this.getIconView()).alpha(0).setDuration(waitMillis).setInterpolator(new AccelerateInterpolator());

		super.waitInActivate(waitMillis);
	}

	/** 停止激活 */
	@Override
	public void stopActivating()
	{
		super.stopActivating();

		if (!this.isActivating()) return;
		this.showViewIn(this.getIconView(), false);
	}
}