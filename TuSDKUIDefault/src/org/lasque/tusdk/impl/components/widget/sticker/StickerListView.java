/** 
 * TuSDKCore
 * StickerListView.java
 *
 * @author 		Clear
 * @Date 		2014-12-30 下午7:06:51 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import java.util.List;

import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.impl.components.widget.sticker.StickerListHeader.StickerListHeaderDelegate;
import org.lasque.tusdk.impl.view.widget.listview.TuGroupListView;
import org.lasque.tusdk.impl.view.widget.listview.TuListGridCellView.TuListGridCellViewDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerListDataSource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 贴纸列表视图
 * 
 * @author Clear
 */
public class StickerListView extends TuGroupListView<List<StickerData>, StickerListCell, StickerGroup, StickerListHeader>
{
	/** 单元格点击委托 */
	public interface StickerListGridDelegate extends TuListGridCellViewDelegate<StickerData, StickerListGrid>, StickerListHeaderDelegate
	{

	}

	/** 单元格点击委托 */
	private StickerListGridDelegate mGridDelegate;

	/** 单元格点击委托 */
	public StickerListGridDelegate getGridDelegate()
	{
		return mGridDelegate;
	}

	/** 单元格点击委托 */
	public void setGridDelegate(StickerListGridDelegate mGridDelegate)
	{
		this.mGridDelegate = mGridDelegate;
	}

	public StickerListView(Context context)
	{
		super(context);
	}

	public StickerListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onGroupListViewCreated(StickerListCell view, TuSdkIndexPath indexPath)
	{
		view.setGridDelegate(mGridDelegate);
	}

	@Override
	protected void onGroupListHeaderCreated(StickerListHeader view, TuSdkIndexPath indexPath)
	{
		view.setDelegate(mGridDelegate);
	}

	/**
	 * 加载贴纸列表
	 * 
	 * @param cate
	 *            贴纸分类
	 */
	public void loadStickers(StickerCategory cate)
	{
		this.setDataSource(new StickerListDataSourceImpl(cate));
	}

	/**
	 * 数组列表数据源
	 * 
	 * @author Clear
	 */
	private class StickerListDataSourceImpl extends StickerListDataSource
	{
		/**
		 * 数组列表数据源
		 * 
		 * @param cate
		 *            贴纸分类
		 */
		public StickerListDataSourceImpl(StickerCategory cate)
		{
			super(cate);
		}

		/**
		 * 视图创建
		 * 
		 * @param indexPath
		 * @param view
		 */
		@SuppressWarnings("unchecked")
		public void onViewBinded(TuSdkIndexPath indexPath, View view)
		{
			if (!(view instanceof TuSdkCellViewInterface)) return;

			Object mode = this.getItem(indexPath);

			if (view instanceof StickerListCell)
			{
				((StickerListCell) view).setModel((List<StickerData>) mode);
			}
			else if (view instanceof StickerListHeader)
			{
				((StickerListHeader) view).setModel((StickerGroup) mode);
			}
		}
	}
}