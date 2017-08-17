/** 
 * TuSDKGee
 * BrushBarTableView.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-27 下午1:35:08 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.smudge;

import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;
import org.lasque.tusdk.modules.view.widget.smudge.BrushBarItemCellBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushTableViewInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 笔刷栏列表视图
 * 
 * @author Yanlin
 */
public class BrushBarTableView extends TuSdkTableView<BrushData, BrushBarItemCellBase> implements BrushTableViewInterface
{

	public BrushBarTableView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public BrushBarTableView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BrushBarTableView(Context context)
	{
		super(context);
	}
	
	/** 笔刷元素动作类型 */
	private BrushAction mAction;
	/** 单元格视图宽度 */
	private int mCellWidth;

	/** 单元格视图宽度 */
	public int getCellWidth()
	{
		return mCellWidth;
	}

	/** 单元格宽度 */
	public void setCellWidth(int mCellWidth)
	{
		this.mCellWidth = mCellWidth;
	}

	@Override
	public int getCellLayoutId()
	{
		if (super.getCellLayoutId() == 0)
		{
			this.setCellLayoutId(BrushBarItemCell.getLayoutId());
		}
		return super.getCellLayoutId();
	}
	
	/** 笔刷元素动作类型 */
	public BrushAction getAction()
	{
		return mAction;
	}

	/** 笔刷元素动作类型 */
	@Override
	public void setAction(BrushAction mAction)
	{
		this.mAction = mAction;
	}
	
	@Override
	public void loadView()
	{
		super.loadView();
		this.setHasFixedSize(true);
	}

	@Override
	protected void onViewCreated(BrushBarItemCellBase view, ViewGroup parent, int viewType)
	{		
		if (this.getCellWidth() > 0)
		{
			view.setWidth(this.getCellWidth());
		}
	}

	@Override
	protected void onViewBinded(BrushBarItemCellBase view, int position)
	{

	}
}