/** 
 * TuSDKCore
 * StickerBarView.java
 *
 * @author 		Clear
 * @Date 		2015-4-27 下午1:26:26 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView.TuSdkTableViewItemClickDelegate;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.modules.view.widget.sticker.StickerBarViewBase;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 贴纸栏视图
 * 
 * @author Clear
 */
public class StickerBarView extends StickerBarViewBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_bar_view");
	}

	/** 贴纸栏视图委托 */
	public interface StickerBarViewDelegate
	{
		/**
		 * 选中一个贴纸数据
		 * 
		 * @param view
		 *            贴纸栏视图
		 * @param data
		 *            贴纸数据
		 */
		void onStickerBarViewSelected(StickerBarView view, StickerData data);

		/**
		 * 选择一个空分类
		 * 
		 * @param view
		 * @param cate
		 */
		void onStickerBarViewEmpty(StickerBarView view, StickerCategory cate);
	}

	public StickerBarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public StickerBarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerBarView(Context context)
	{
		super(context);
	}

	/** 贴纸栏视图委托 */
	private StickerBarViewDelegate mDelegate;

	/** 贴纸栏视图委托 */
	public StickerBarViewDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 贴纸栏视图委托 */
	public void setDelegate(StickerBarViewDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/************************* view ******************************/
	/** 参数选项视图 */
	private LinearLayout mParamsView;
	/** 列表包装视图 */
	private RelativeLayout mListWrap;
	/** 列表视图 */
	private StickerBarTableView mTableView;
	/** 数据空视图 */
	private TextView mEmptyView;
	/** 单元格宽度 */
	private int mGridWidth;
	/** 单元格高度 */
	private int mGridHeight;
	/** 单元格间距 */
	private int mGridPadding = -1;
	/** 单元格布局资源ID */
	private int mGridLayoutId;

	/** 参数选项视图 */
	public LinearLayout getParamsView()
	{
		if (mParamsView == null)
		{
			mParamsView = this.getViewById("lsq_params_view");
			if (mParamsView != null)
			{
				mParamsView.removeAllViews();
			}
		}
		return mParamsView;
	}

	/** 列表视图 */
	public StickerBarTableView getTableView()
	{
		if (mTableView == null)
		{
			mTableView = this.getViewById("lsq_sticker_table_view");
			if (mTableView != null)
			{
				mTableView.setItemClickDelegate(mTuSdkTableViewItemClickDelegate);
				mTableView.reloadData();
			}
		}
		return mTableView;
	}

	/** 列表包装视图 */
	public RelativeLayout getListWrap()
	{
		if (mListWrap == null)
		{
			mListWrap = this.getViewById("lsq_list_wrap");
		}
		return mListWrap;
	}

	/** 空数据视图 */
	public TextView getEmptyView()
	{
		if (mEmptyView == null)
		{
			mEmptyView = this.getViewById("lsq_sticker_empty");
			mEmptyView.setOnClickListener(mButtonClickListener);
		}
		return mEmptyView;
	}

	/** 单元格宽度 */
	public int getGridWidth()
	{
		return mGridWidth;
	}

	/** 单元格宽度 */
	public void setGridWidth(int mGridWidth)
	{
		if (mGridWidth <= 0 || this.getTableView() == null) return;
		this.mGridWidth = mGridWidth;
		this.getTableView().setCellWidth(mGridWidth);
	}

	/** 单元格高度 */
	public int getGridHeight()
	{
		return mGridHeight;
	}

	/** 单元格高度 */
	public void setGridHeight(int mGridHeight)
	{
		if (mGridHeight <= 0 || this.getTableView() == null) return;
		this.mGridHeight = mGridHeight;
		this.setHeight(this.getListWrap(), mGridHeight);
	}

	/** 单元格间距 */
	public int getGridPadding()
	{
		return mGridPadding;
	}

	/** 单元格间距 */
	public void setGridPadding(int mGridPadding)
	{
		if (mGridPadding < 0 || this.getTableView() == null) return;
		this.mGridPadding = mGridPadding;
		this.getTableView().setCellPadding(mGridPadding);
	}

	/** 单元格布局资源ID */
	public int getGridLayoutId()
	{
		return mGridLayoutId;
	}

	/** 单元格布局资源ID */
	public void setGridLayoutId(int mGridLayoutId)
	{
		if (mGridHeight == 0 || this.getTableView() == null) return;
		this.mGridLayoutId = mGridLayoutId;
		this.getTableView().setCellLayoutId(mGridLayoutId);
	}

	/** 贴纸数据点击事件 */
	private TuSdkTableViewItemClickDelegate<StickerData, StickerListGrid> mTuSdkTableViewItemClickDelegate = new TuSdkTableViewItemClickDelegate<StickerData, StickerListGrid>()
	{
		@Override
		public void onTableViewItemClick(StickerData itemData, StickerListGrid itemView, int position)
		{
			onSelectedSticker(itemData);
		}
	};

	/** 选中一个贴纸数据 */
	private void onSelectedSticker(StickerData itemData)
	{
		if (itemData == null || this.getDelegate() == null) return;
		this.getDelegate().onStickerBarViewSelected(this, itemData);
	}

	/** 处理空数据视图 */
	private void handleEmptyAction()
	{
		if (this.getDelegate() == null) return;
		this.getDelegate().onStickerBarViewEmpty(this, this.getCurrentCate());
	}

	/** 加载视图 */
	public void loadView()
	{
		super.loadView();

		// 参数选项视图
		getParamsView();
		// 列表视图
		getTableView();
		// 列表包装视图
		getListWrap();
		// 空数据视图
		this.showViewIn(getEmptyView(), false);
	}

	/************************* config ******************************/
	/** 贴纸分类列表 */
	public void loadCategories(List<StickerCategory> categories)
	{
		if (this.getParamsView() == null) return;
		this.getParamsView().removeAllViews();

		super.loadCategories(categories);
	}

	/** 创建分类按钮 */
	@Override
	protected View buildCateButton(StickerCategory cate, int index, LayoutParams params)
	{
		TuSdkTextButton textView = new TuSdkTextButton(this.getContext());
		int[] colors = new int[] {
				TuSdkContext.getColor("lsq_filter_config_highlight"), TuSdkContext.getColor("lsq_color_white") };
		int[][] states = new int[][] {
				new int[] { android.R.attr.state_selected }, new int[] {} };

		ColorStateList colorStateList = new ColorStateList(states, colors);

		textView.setTextColor(colorStateList);
		textView.setGravity(Gravity.CENTER);
		textView.setText(TuSdkContext.getString(cate.name));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		textView.setEllipsize(TruncateAt.END);
		textView.setLines(1);
		textView.setTag(index);

		textView.setOnClickListener(mButtonClickListener);
		this.getParamsView().addView(textView, params);
		return textView;
	}

	/** 按钮点击事件 */
	protected OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getEmptyView()))
			{
				handleEmptyAction();
				return;
			}
			if (v.getTag() == null || !(v.getTag() instanceof Integer)) return;
			selectCateIndex((Integer) v.getTag());
		}
	};

	/** 选中分类按钮 */
	@Override
	protected void selectCateButton(Integer index)
	{
		if (this.getParamsView() == null || this.getParamsView().getChildCount() == 0) return;

		for (int i = 0, j = this.getParamsView().getChildCount(); i < j; i++)
		{
			View view = this.getParamsView().getChildAt(i);
			view.setSelected(i == index);
		}
	}

	/** 刷新分类数据 */
	@Override
	protected void refreshCateDatas()
	{
		if (this.getTableView() == null) return;

		StickerCategory cate = this.getCurrentCate();
		if (cate == null) return;

		List<StickerData> datas = null;

		if (cate.extendType == null)
		{
			datas = this.getStickerDatas(cate.id);
		}
		else
		{
			switch (cate.extendType)
				{
				case ExtendTypeAll:
					datas = this.getAllStickerDatas();
					break;
				default:
					break;
				}
		}

		if (datas == null) return;
		// 处理空视图
		this.showViewIn(getEmptyView(), datas.size() == 0);

		this.getTableView().setModeList(datas);
		if (this.getTableView().getModeList() != null && this.getTableView().getModeList().size() > 0)
		{
			this.getTableView().scrollToPosition(0);
		}
		this.getTableView().reloadData();
	}
}