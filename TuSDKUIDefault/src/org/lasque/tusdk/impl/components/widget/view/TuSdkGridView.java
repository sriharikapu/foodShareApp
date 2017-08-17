/** 
 * TuSDKCore
 * TuSdkGridView.java
 *
 * @author 		Clear
 * @Date 		2014-11-25 下午6:06:27 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.view;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import org.lasque.tusdk.core.view.recyclerview.TuSdkAdapter;
import org.lasque.tusdk.core.view.recyclerview.TuSdkMultiSelectableCellViewInterface;
import org.lasque.tusdk.core.view.recyclerview.TuSdkRecyclerView;
import org.lasque.tusdk.core.view.recyclerview.TuSdkViewHolder;
import org.lasque.tusdk.core.view.recyclerview.TuSdkViewHolder.TuSdkViewHolderItemClickListener;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 列表视图
 * 
 * @author Clear
 * @param <T>
 *            数据类型
 * @param <V>
 *            视图类型
 */
public abstract class TuSdkGridView<T, V extends View> extends
		TuSdkRecyclerView
{
	/**
	 * 列表项点击事件委托
	 * 
	 * @author Clear
	 */
	public interface TuSdkGridViewItemClickDelegate<T, V extends View>
	{
		/**
		 * 列表项点击事件
		 * 
		 * @param itemData
		 *            数据
		 * @param itemView
		 *            视图
		 * @param position
		 *            视图位置
		 */
		public void onGridViewItemClick(T itemData, V itemView, int position);
	}

	public TuSdkGridView(Context context)
	{
		super(context);
		
		initWithSdkConfig();
	}

	public TuSdkGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		initWithSdkConfig();
	}

	public TuSdkGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		initWithSdkConfig();
	}

	/**
	 * 列表项点击事件委托
	 */
	private TuSdkGridViewItemClickDelegate<T, V> mItemClickDelegate;

	/**
	 * SDK适配器
	 */
	private TuSdkAdapter<T> mSdkAdapter;

	/**
	 * SDK RecyclerView 网格布局管理器
	 */
	private GridLayoutManager mLayoutManager;

	/**
	 * 行视图布局ID
	 */
	private int mCellLayoutId;
	
	/**
	 * 显示的列/行数
	 */
	private int mGridSize = 3;
	
	/**
	 * 是否支持多选 (默认: true)
	 */
	private boolean mEnableMultiSelection = true;

	/**
	 * 模型数据列表
	 */
	private List<T> mModeList;

	/**
	 * 滚动方向 Layout orientation. Should be {@link #HORIZONTAL} or
	 * {@link #VERTICAL}.
	 */
	private int mOrientation = GridLayoutManager.VERTICAL;

	/**
	 * 是否从最后开始 reverseLayout When set to true, layouts from end to start.
	 */
	private boolean mReverseLayout;

	/**
	 * 列表项点击事件委托
	 * 
	 * @param itemClickDelegate
	 *            列表项点击事件委托
	 */
	public void setItemClickDelegate(
			TuSdkGridViewItemClickDelegate<T, V> itemClickDelegate)
	{
		this.mItemClickDelegate = itemClickDelegate;
		if (mSdkAdapter == null) return;

		if (itemClickDelegate == null)
		{
			mSdkAdapter.setItemClickListener(null);
		}
		else
		{
			mSdkAdapter.setItemClickListener(mViewHolderItemClickListener);
		}
	}

	/**
	 * ViewHolder点击事件
	 */
	private TuSdkViewHolderItemClickListener<T> mViewHolderItemClickListener = new TuSdkViewHolderItemClickListener<T>()
	{
		@SuppressWarnings({ "unchecked" })
		@Override
		public void onViewHolderItemClick(TuSdkViewHolder<T> holder)
		{
			if (mItemClickDelegate == null) return;
			if (holder.itemView instanceof TuSdkCellViewInterface)
			{
				mItemClickDelegate.onGridViewItemClick(holder.getModel(),
						(V) holder.itemView, holder.getPosition());
			}
		}
	};

	/**
	 * 列表项点击事件委托
	 * 
	 * @return
	 */
	public TuSdkGridViewItemClickDelegate<T, V> getItemClickDelegate()
	{
		return this.mItemClickDelegate;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @return the cellLayoutId
	 */
	public int getCellLayoutId()
	{
		return mCellLayoutId;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @param cellLayoutId
	 *            the cellLayoutId to set
	 */
	public void setCellLayoutId(int cellLayoutId)
	{
		this.mCellLayoutId = cellLayoutId;

		if (cellLayoutId > 0 && mSdkAdapter != null)
		{
			mSdkAdapter.setViewLayoutId(this.getCellLayoutId());
		}
	}
	
	/**
	 * 网格列、行数
	 * 
	 * @return the gridSize
	 */
	public int getGridSize()
	{
		return mGridSize;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @param size
	 *            the gridSize to set
	 */
	public void setGridSize(int size)
	{
		if(size > 0 && size != this.mGridSize) 
		{
			this.mGridSize = size;

			this.getSdkLayoutManager().setSpanCount(size);
		}
	}
	
	/**
	 * 是否支持多选 (默认: true)
	 */
	public void setEnableMultiSelection(boolean mEnableMultiSelection)
	{
		this.mEnableMultiSelection = mEnableMultiSelection;
		
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null)
			gridAdapter.setEnableMultiSelection(mEnableMultiSelection);
	}
	
	/**
	 * 是否支持多选 (默认: true)
	 */
	public boolean getEnableMultiSelection()
	{
		return this.mEnableMultiSelection;
	}

	/**
	 * 模型数据列表
	 * 
	 * @return the modeList
	 */
	public List<T> getModeList()
	{
		return mModeList;
	}

	/**
	 * 模型数据列表
	 * 
	 * @param modeList
	 *            the modeList to set
	 */
	public void setModeList(List<T> modeList)
	{
		this.mModeList = modeList;
		if (mSdkAdapter != null)
		{
			mSdkAdapter.setModeList(this.mModeList);
		}
	}

	/**
	 * SDK适配器
	 * 
	 * @return the SdkAdapter
	 */
	public TuSdkAdapter<T> getSdkAdapter()
	{
		if (mSdkAdapter == null)
		{
			mSdkAdapter = new GridViewAdapter(this.getCellLayoutId(),
					mModeList);
			if (mItemClickDelegate != null)
			{
				mSdkAdapter.setItemClickListener(mViewHolderItemClickListener);
			}
			
			((GridViewAdapter)mSdkAdapter).setEnableMultiSelection(this.getEnableMultiSelection());
		}
		return mSdkAdapter;
	}

	/**
	 * 设置数据适配器
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setAdapter(Adapter adapter)
	{
		if (adapter instanceof TuSdkAdapter)
		{
			mSdkAdapter = (TuSdkAdapter<T>) adapter;
		}
		super.setAdapter(adapter);
	}

	/**
	 * SDK RecyclerView 线性布局管理器
	 * 
	 * @return the SdkLayoutManager
	 */
	public GridLayoutManager getSdkLayoutManager()
	{
		if (mLayoutManager == null)
		{
			mLayoutManager = new GridLayoutManager(this.getContext(), getGridSize());
		}
		return mLayoutManager;
	}

	/**
	 * 设置布局管理器
	 */
	@Override
	public void setLayoutManager(LayoutManager layout)
	{
		if (layout instanceof GridLayoutManager)
		{
			mLayoutManager = (GridLayoutManager) layout;
		}
		super.setLayoutManager(layout);
	}

	/**
	 * 滚动方向 Layout orientation. Should be {@link #HORIZONTAL} or
	 * {@link #VERTICAL}.
	 * 
	 * @return the mOrientation
	 */
	public int getOrientation()
	{
		return mOrientation;
	}

	/**
	 * 滚动方向 Layout orientation. Should be {@link #HORIZONTAL} or
	 * {@link #VERTICAL}.
	 * 
	 * @param Orientation
	 *            the Orientation to set
	 */
	public void setOrientation(int orientation)
	{
		this.mOrientation = orientation;
		if (mLayoutManager != null)
		{
			mLayoutManager.setOrientation(this.mOrientation);
		}
	}

	/**
	 * 滚动到指定索引并偏移
	 * 
	 * @param position
	 * @param offset
	 */
	public void scrollToPositionWithOffset(int position, int offset)
	{
		if (mLayoutManager != null)
		{
			mLayoutManager.scrollToPositionWithOffset(position, offset);
		}
	}

	/**
	 * 滑动到指定视图居中位置
	 * 
	 * @param view
	 */
	public void smoothScrollByCenter(View view)
	{
		if (view == null) return;
		// 水平
		if (this.mOrientation == RecyclerView.HORIZONTAL)
		{
			int left = TuSdkViewHelper.locationInWindowLeft(view)
					- TuSdkViewHelper.locationInWindowLeft(this);

			left -= (this.getWidth() - view.getWidth()) / 2;
			this.smoothScrollBy(left, 0);
		}
		// 垂直
		else if (this.mOrientation == RecyclerView.VERTICAL)
		{
			int top = TuSdkViewHelper.locationInWindowTop(view)
					- TuSdkViewHelper.locationInWindowTop(this);

			top -= (this.getHeight() - view.getHeight()) / 2;
			this.smoothScrollBy(0, top);
		}
	}

	/**
	 * 当前选中的元素列表
	 * 
	 * @return the selectedItems
	 */
	public ArrayList<T> getSelectedItems()
	{
		if (mSdkAdapter == null) return null;
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null)  return gridAdapter.getSelectedItems();
		
		return null;
	}
	
	/**
	 * 设置选中的元素列表
	 * 
	 * @param items
	 *            选中的元素列表
	 */
	public void setSelectedItems(ArrayList<T> items)
	{
		if (mSdkAdapter == null) return;
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null) gridAdapter.setSelectedItems(items);
	}

	/**
	 * 设置元素的选择状态
	 * 
	 * @param position
	 *            the position to set
	 * @param selected
	 *            true / false
	 */
	public void setItemSelected(int position, boolean selected)
	{
		if (mSdkAdapter == null) return;
		
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null)  gridAdapter.setItemSelected(position, selected);
	}
	
	/**
	 * 获取元素的选择索引
	 * 
	 * @param position
	 *            the item position in Adapter source
	 * @return 元素的选择索引，如果为-1表示没有选择
	 */
	public int getItemSelectionIndex(int position)
	{
		int index = -1;
		
		if (mSdkAdapter == null) return index;
		
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null) 
		{
			index = gridAdapter.getItemSelectionIndex(position);
		}
		return index;
	}

	/**
	 * 清除选择数据
	 */
	public void resetSelections()
	{
		if (mSdkAdapter == null) return;
		GridViewAdapter gridAdapter = (GridViewAdapter)mSdkAdapter;
		if (gridAdapter != null) gridAdapter.resetSelections();
	}

	/**
	 * 是否从最后开始 reverseLayout When set to true, layouts from end to start.
	 * 
	 * @return the ReverseLayout
	 */
	public boolean isReverseLayout()
	{
		return mReverseLayout;
	}

	/**
	 * 是否从最后开始 reverseLayout When set to true, layouts from end to start.
	 * 
	 * @param reverseLayout
	 *            the reverseLayout to set
	 */
	public void setReverseLayout(boolean reverseLayout)
	{
		this.mReverseLayout = reverseLayout;
		if (mLayoutManager != null)
		{
			mLayoutManager.setReverseLayout(this.mReverseLayout);
		}
	}

	/**
	 * 使用SDK选项自动配置
	 */
	private void initWithSdkConfig()
	{
		if (this.getLayoutManager() == null)
		{
			this.setLayoutManager(this.getSdkLayoutManager());
		}

		if (this.getAdapter() == null)
		{
			this.setAdapter(this.getSdkAdapter());
		}
	}

	/**
	 * 刷新数据
	 */
	public void reloadData()
	{
		if (this.getAdapter() == null)
		{
			this.initWithSdkConfig();
		}
		else
		{
			this.getAdapter().notifyDataSetChanged();
		}
	}

	/**
	 * 视图创建
	 * 
	 * @param view
	 *            创建的视图
	 * @param parent
	 *            父对象
	 * @param viewType
	 *            视图类型
	 */
	protected abstract void onViewCreated(V view, ViewGroup parent, int viewType);

	/**
	 * 绑定视图数据
	 * 
	 * @param view
	 *            创建的视图
	 * @param position
	 *            索引位置
	 */
	protected abstract void onViewBinded(V view, int position);

	/**
	 * 列表视图适配器
	 * 
	 * @author Clear
	 */
	protected class GridViewAdapter extends TuSdkAdapter<T>
	{
		public GridViewAdapter()
		{
			super();
		}

		public GridViewAdapter(int viewLayoutId, List<T> modeList)
		{
			super(viewLayoutId, modeList);
		}

		public GridViewAdapter(int viewLayoutId)
		{
			super(viewLayoutId);
		}

		/**
		 * 创建视图
		 */
		@SuppressWarnings("unchecked")
		@Override
		public TuSdkViewHolder<T> onCreateViewHolder(ViewGroup parent,
				int viewType)
		{
			TuSdkViewHolder<T> viewHolder = super.onCreateViewHolder(parent,
					viewType);

			if (viewHolder.itemView instanceof TuSdkCellViewInterface)
			{
				onViewCreated((V) viewHolder.itemView, parent, viewType);
			}

			return viewHolder;
		}

		/**
		 * 绑定视图
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onBindViewHolder(TuSdkViewHolder<T> holder, int position)
		{
			super.onBindViewHolder(holder, position);

			if (holder.itemView instanceof TuSdkCellViewInterface)
			{
				onViewBinded((V) holder.itemView, position);
			}
			
			updateCellSelectionState(holder, position);
		}

		@Override
		public void onViewAttachedToWindow(TuSdkViewHolder<T> holder)
		{
//			super.onViewAttachedToWindow(null);
			
			int position = holder.getPosition();
			updateCellSelectionState(holder, position);
			
			if (holder.itemView instanceof TuSdkMultiSelectableCellViewInterface)
			{
				((TuSdkMultiSelectableCellViewInterface) holder.itemView).onCellInit(this.getEnableMultiSelection());
			}
		}
		
		private void updateCellSelectionState(TuSdkViewHolder<T> holder, int position)
		{
			if (holder.itemView instanceof TuSdkMultiSelectableCellViewInterface)
			{
				int selectionIndex = getItemSelectionIndex(position);
				if (selectionIndex > -1)
				{
					((TuSdkMultiSelectableCellViewInterface) holder.itemView)
							.onCellSelected(position, selectionIndex);
				}
				else
				{
					((TuSdkMultiSelectableCellViewInterface) holder.itemView)
							.onCellDeselected();
				}
			}
		}
		
		//---------------------------- multi selection -------------------------------------
		
		/**
		 * 当前选中的数据列表
		 */
		private ArrayList<T> mSelectedItems = new ArrayList<T>();
		
		/**
		 * 是否支持多选 (默认: true)
		 */
		private boolean mEnableMultiSelection = true;
		
		/**
		 * 是否支持多选 (默认: true)
		 */
		public void setEnableMultiSelection(boolean mEnableMultiSelection)
		{
			this.mEnableMultiSelection = mEnableMultiSelection;
		}
		
		/**
		 * 是否支持多选 (默认: true)
		 */
		public boolean getEnableMultiSelection()
		{
			return this.mEnableMultiSelection;
		}
		
		/**
		 * 当前选中的元素列表
		 * 
		 * @return the selectedItems
		 */
		public ArrayList<T> getSelectedItems()
		{
			return mSelectedItems;
		}
		
		/**
		 * 设置选中的元素列表
		 * 
		 * @param items
		 *            选中的元素列表
		 */
		public void setSelectedItems(ArrayList<T> items)
		{
			mSelectedItems = items;
			
			notifySelectionItemChanged();
		}

		/**
		 * 设置元素的选择状态
		 * 
		 * @param position
		 *            the position to set
		 * @param selected
		 *            true / false
		 */
		public void setItemSelected(int position, boolean selected)
		{
			T item = getItem(position);
			
			if (item == null) return;
			
			int index = this.getItemSelectionIndex(item);
			
			if (selected)
			{
				if (index == -1)
				{
					mSelectedItems.add(item);
					notifyItemChanged(position);
				}
				else
				{
					TLog.w("photo in [%d] has been selected already", position);
				}
			}
			else
			{
				if (index > -1)
				{
					mSelectedItems.remove(index);
					notifyItemChanged(position);
					
					notifySelectionItemChanged();
				}
				else
				{
					TLog.d("please select photo in [%d] first", position);
				}
			}
		}
		
		/**
		 * 选择列表发生变化
		 */
		private void notifySelectionItemChanged()
		{
			if (mSelectedItems == null || mSelectedItems.size() == 0) return;
			
			for (int i = 0; i<mSelectedItems.size(); i++)
			{
				T theItem = mSelectedItems.get(i);
				
				int position = getItemPosition(theItem);
				if (position > -1)
				{
					notifyItemChanged(position);
				}
			}
		}

		/**
		 * 获取元素的选择索引
		 * 
		 * @param item
		 *            the item
		 * @return 元素的选择索引，如果为-1表示没有选择
		 */
		public int getItemSelectionIndex(T item)
		{
			int index = -1;
			
			if (item != null && mSelectedItems != null && mSelectedItems.size() > 0)
			{
				for (int i = 0; i<mSelectedItems.size(); i++)
				{
					T theItem = mSelectedItems.get(i);
					if (theItem.equals(item))
					{
						index = i;
						break;
					}
				}
			}
			return index;
		}
		
		/**
		 * 获取元素的选择索引
		 * 
		 * @param position
		 *            数据源中的位置索引
		 * @return 元素的选择索引，如果为-1表示没有选择
		 */
		public int getItemSelectionIndex(int position)
		{
			T item = getItem(position);
			
			if (item == null) return -1;
			
			return this.getItemSelectionIndex(item);
		}

		/**
		 * 清除选择数据
		 */
		public void resetSelections()
		{
			if (mSelectedItems != null) mSelectedItems.clear();

			notifyDataSetChanged();
		}
	}
}
