/** 
 * TuSDKCore
 * GroupFilterBar.java
 *
 * @author 		Clear
 * @Date 		2015-2-12 下午3:48:22 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView.TuSdkTableViewItemClickDelegate;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterTableViewInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 滤镜组选择栏
 * 
 * @author Clear
 */
public class GroupFilterBar extends GroupFilterBarBase implements GroupFilterBarInterface
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_group_filter_bar");
	}

	public GroupFilterBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public GroupFilterBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GroupFilterBar(Context context)
	{
		super(context);
	}

	/************** 参数 ***************/
	/** 行视图宽度 */
	private int mGroupFilterCellWidth;
	/** 滤镜分组列表行视图布局资源ID */
	private int mGroupTableCellLayoutId;
	/** 滤镜列表行视图布局资源ID */
	private int mFilterTableCellLayoutId;

	/** 行视图宽度 */
	public int getGroupFilterCellWidth()
	{
		return mGroupFilterCellWidth;
	}

	/** 行视图宽度 */
	@Override
	public void setGroupFilterCellWidth(int mGroupFilterCellWidth)
	{
		this.mGroupFilterCellWidth = mGroupFilterCellWidth;
		if (this.getGroupTable() != null)
		{
			this.getGroupTable().setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		}
		if (this.getFilterTable() != null)
		{
			this.getFilterTable().setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		}

		if (this.getGroupFilterCellWidth() > 0 && this.getBackButton() != null)
		{
			MarginLayoutParams params = TuSdkViewHelper.getMarginLayoutParams(this.getBackButton());
			if (params != null)
			{
				this.setWidth(this.getBackButton(), this.getGroupFilterCellWidth() - params.leftMargin * 2);
			}
		}
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.filter.GroupFilterGroupViewBase}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *            GroupFilterGroupViewBase)
	 */
	public int getGroupTableCellLayoutId()
	{
		if (mGroupTableCellLayoutId == 0)
		{
			mGroupTableCellLayoutId = GroupFilterGroupView.getLayoutId();
		}
		return mGroupTableCellLayoutId;
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.filter.GroupFilterGroupViewBase}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *            GroupFilterGroupViewBase)
	 */
	@Override
	public void setGroupTableCellLayoutId(int mGroupTableCellLayoutId)
	{
		this.mGroupTableCellLayoutId = mGroupTableCellLayoutId;
		if (this.getGroupTable() != null)
		{
			this.getGroupTable().setCellLayoutId(this.getGroupTableCellLayoutId());
		}
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link GroupFilterItemViewBase}
	 * @param 滤镜列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemViewBase)
	 */
	public int getFilterTableCellLayoutId()
	{
		if (mFilterTableCellLayoutId == 0)
		{
			mFilterTableCellLayoutId = GroupFilterItemView.getLayoutId();
		}
		return mFilterTableCellLayoutId;
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link GroupFilterItemViewBase}
	 * @param 滤镜列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemViewBase)
	 */
	@Override
	public void setFilterTableCellLayoutId(int mFilterTableCellLayoutId)
	{
		this.mFilterTableCellLayoutId = mFilterTableCellLayoutId;
		if (this.getFilterTable() != null)
		{
			this.getFilterTable().setCellLayoutId(this.getFilterTableCellLayoutId());
		}
	}

	/************** view ***************/
	/** 滤镜分组列表 */
	private GroupFilterTableViewInterface mGroupTable;
	/** 滤镜列表 */
	private GroupFilterTableViewInterface mFilterTable;
	/** 后退按钮 */
	private View mBackButton;

	/** 滤镜分组列表 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & GroupFilterTableViewInterface> T getGroupTable()
	{
		if (mGroupTable == null)
		{
			View view = this.getViewById("lsq_group_list_view");
			if (view == null || !(view instanceof GroupFilterTableViewInterface)) return null;
			mGroupTable = (GroupFilterTableViewInterface) view;

			mGroupTable.setCellLayoutId(this.getGroupTableCellLayoutId());
			mGroupTable.setDisplaySelectionIcon(this.isEnableFilterConfig());
			mGroupTable.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
			mGroupTable.setItemClickDelegate(mGroupTableItemClickDelegate);
			mGroupTable.reloadData();
		}
		return (T) mGroupTable;
	}

	/** 滤镜列表 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & GroupFilterTableViewInterface> T getFilterTable()
	{
		if (mFilterTable == null)
		{
			View view = this.getViewById("lsq_filter_list_view");
			if (view == null || !(view instanceof GroupFilterTableViewInterface)) return null;

			mFilterTable = (GroupFilterTableViewInterface) view;

			mFilterTable.setCellLayoutId(this.getFilterTableCellLayoutId());
			mFilterTable.setDisplaySelectionIcon(this.isEnableFilterConfig());
			mFilterTable.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
			mFilterTable.setItemClickDelegate(mFilterTableItemClickDelegate);
			mFilterTable.reloadData();
		}
		return (T) mFilterTable;
	}

	/** 后退按钮 */
	public View getBackButton()
	{
		if (mBackButton == null)
		{
			mBackButton = this.getViewById("lsq_filter_back_button");
			if (mBackButton != null)
			{
				mBackButton.setOnClickListener(mOnClickListener);
			}
		}
		return mBackButton;
	}

	/** 按钮点击事件 */
	protected OnSafeClickListener mOnClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getBackButton()))
			{
				handleBackAction();
			}
		}
	};

	/** 滤镜分组列表点击事件 */
	private TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase> mGroupTableItemClickDelegate = new TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase>()
	{
		@Override
		public void onTableViewItemClick(GroupFilterItem itemData, GroupFilterItemViewBase itemView, int position)
		{
			onGroupItemSeleced(itemData, itemView, position);
		}
	};

	/** 滤镜列表点击事件 */
	private TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase> mFilterTableItemClickDelegate = new TuSdkTableViewItemClickDelegate<GroupFilterItem, GroupFilterItemViewBase>()
	{
		@Override
		public void onTableViewItemClick(GroupFilterItem itemData, GroupFilterItemViewBase itemView, int position)
		{
			onFilterItemSeleced(itemData, itemView, position);
		}
	};

	/** 加载视图 */
	@Override
	public void loadView()
	{
		super.loadView();
		// 滤镜分组列表
		getGroupTable();
		// 滤镜列表
		this.showViewIn(getFilterTable(), false);
		// 后退按钮
		this.showViewIn(getBackButton(), false);
	}

	/**
	 * 显示 滤镜列表视图
	 * 
	 * @param viewCenterX
	 * @param isShow
	 *            是否显示
	 */
	@Override
	protected void showFilterTable(int viewCenterX, boolean isShow)
	{
		this.showViewIn(this.getBackButton(), isShow);
		super.showFilterTable(viewCenterX, isShow);
	}
}