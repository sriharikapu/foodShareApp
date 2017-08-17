/** 
 * TuSDKCore
 * StickerBarTableView.java
 *
 * @author 		Clear
 * @Date 		2015-4-27 下午1:35:08 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 贴纸栏列表视图
 * 
 * @author Clear
 */
public class StickerBarTableView extends TuSdkTableView<StickerData, StickerListGrid>
{

	public StickerBarTableView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public StickerBarTableView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerBarTableView(Context context)
	{
		super(context);
	}

	/** 单元格视图宽度 */
	private int mCellWidth;
	/** 单元格间距 */
	private int mCellPadding = -1;

	/** 单元格视图宽度 */
	public int getCellWidth()
	{
		if (mCellWidth == 0)
		{
			mCellWidth = this.getHeight();
		}
		return mCellWidth;
	}

	/** 单元格宽度 */
	public void setCellWidth(int mCellWidth)
	{
		this.mCellWidth = mCellWidth;
	}

	/** 单元格间距 */
	public int getCellPadding()
	{
		return mCellPadding;
	}

	/** 单元格间距 */
	public void setCellPadding(int mCellPadding)
	{
		this.mCellPadding = mCellPadding;
	}

	@Override
	public int getCellLayoutId()
	{
		if (super.getCellLayoutId() == 0)
		{
			this.setCellLayoutId(StickerListGrid.getLayoutId());
		}
		return super.getCellLayoutId();
	}

	@Override
	protected void onViewCreated(StickerListGrid view, ViewGroup parent, int viewType)
	{
		view.setWidth(this.getCellWidth());
		view.setInnerWarpSpace(this.getCellPadding());
	}

	@Override
	protected void onViewBinded(StickerListGrid view, int position)
	{

	}
}