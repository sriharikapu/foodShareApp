/** 
 * TuSDKGee
 * BrushBarItemCell.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-13 下午18:25:28 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.smudge;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.AlertDelegate;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.smudge.BrushBarItemCellBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 笔刷栏单元格视图
 * 
 * @author Yanlin
 */
public class BrushBarItemCell extends BrushBarItemCellBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_brush_bar_item_cell");
	}

	public BrushBarItemCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public BrushBarItemCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BrushBarItemCell(Context context)
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
	/** 删除按钮视图 */
	private View mRemoveButton;

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

	/** 删除按钮视图 */
	public View getRemoveButton()
	{
		if (mRemoveButton == null)
		{
			mRemoveButton = this.getViewById("lsq_item_remove_button");
			if (mRemoveButton != null)
			{
				mRemoveButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mRemoveButton;
	}
	
	/** 按钮点击事件 */
	protected OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			// 分发视图点击事件
			dispatcherViewClick(v);
		}
	};
	
	/** 分发点击事件 */
	protected void dispatcherViewClick(View v)
	{
		if (this.equalViewIds(v, this.getRemoveButton()))
		{
			this.handleRemoveButton();
		}
	}

	@Override
	public void loadView()
	{
		super.loadView();
		this.getWrapView();
		this.getImageView();
		this.getTitleView();
		this.getSelectedView();
		this.showViewIn(getRemoveButton(), false);
	}

	/** 橡皮擦 */
	@Override
	protected void handleTypeEraser(BrushData model)
	{
		if (this.getImageView() == null) return;

		this.setTextViewText(this.getTitleView(), this.getResString("lsq_brush_Eraser"));
		
		this.getImageView().setScaleType(ScaleType.CENTER_CROP);
		this.getImageView().setImageDrawable(TuSdkContext.getDrawable("lsq_style_default_brush_eraser"));
	}

	/** 笔刷 */
	@Override
	protected void handleTypeBrush(BrushData model)
	{
		super.handleTypeBrush(model);
		
		this.setTextViewText(this.getTitleView(), this.getResString(model.getNameKey()));
	}

	/** 在线笔刷 */
	@Override
	protected void handleTypeOnline(BrushData model)
	{
		this.handleBlockView(GroupFilterItem.Backgroud_Online, TuSdkContext.getDrawableResId("lsq_style_default_filter_online"));
	}

	/** 设置功能块视图 */
	@Override
	protected void handleBlockView(int color, int icon)
	{
		super.handleBlockView(color, icon);
	}

	@Override
	public void viewNeedRest()
	{
		super.viewNeedRest();

		this.setTextViewText(this.getTitleView(), null);
		this.showViewIn(this.getWrapView(), true);
		this.showViewIn(this.getSelectedView(), false);
		this.setImageColor(Color.TRANSPARENT);
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
	}

	@Override
	public void onCellDeselected()
	{
		super.onCellDeselected();
		this.showViewIn(this.getSelectedView(), false);
	}
	
	/** 处理删除动作 */
	protected void handleRemoveButton()
	{
		if (this.getTitleView() == null || this.canHiddenRemoveFlag()) return;

		AlertDelegate mAlertDelegate = new AlertDelegate()
		{
			@Override
			public void onAlertConfirm(AlertDialog dialog)
			{
				onRemoveConfirm();
			}
		};

		TuSdkViewHelper.alert(mAlertDelegate, this.getContext(), this.getResString("lsq_brush_remove_title"),
				this.getResString("lsq_brush_remove_msg", this.getTitleView().getText()), this.getResString("lsq_nav_cancel"),
				this.getResString("lsq_nav_remove"));
	}

	/** 确认删除 */
	private void onRemoveConfirm()
	{
		if (this.getDelegate() == null) return;
		// TLog.d("onRemoveConfirm");
		this.getDelegate().onBrushCellRemove(this);
	}
}