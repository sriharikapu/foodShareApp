/** 
 * TuSDKCore
 * StickerLocalListFragment.java
 *
 * @author 		Clear
 * @Date 		2014-12-30 下午3:03:01 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerListHeader.StickerListHeaderAction;
import org.lasque.tusdk.impl.components.widget.sticker.StickerListView.StickerListGridDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 本地贴纸列表控制器
 * 
 * @author Clear
 */
public class StickerLocalListFragment extends TuFragment
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_list_fragment");
	}

	/** 本地贴纸列表控制器委托 */
	public interface StickerLocalListFragmentDelegate
	{
		/**
		 * 选中贴纸
		 * 
		 * @param fragment
		 *            控制器
		 * @param data
		 *            贴纸数据
		 */
		void onStickerLocalListFragmentSelected(StickerLocalListFragment fragment, StickerData data);

		/**
		 * 删除一个贴纸包
		 * 
		 * @param fragment
		 *            控制器
		 * @param group
		 *            贴纸包
		 * @param action
		 *            贴纸列表分组头视图动作
		 */
		void onStickerLocalListFragmentGroup(StickerLocalListFragment fragment, StickerGroup group, StickerListHeaderAction action);

		/**
		 * 更多动作
		 * 
		 * @param fragment
		 */
		void onStickerLocalListFragmentAction(StickerLocalListFragment fragment);
	}

	/** 本地贴纸列表控制器委托 */
	private StickerLocalListFragmentDelegate mDelegate;

	/** 本地贴纸列表控制器委托 */
	public StickerLocalListFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 本地贴纸列表控制器委托 */
	public void setDelegate(StickerLocalListFragmentDelegate delegate)
	{
		this.mDelegate = delegate;
	}

	/** 本地贴纸列表控制器 */
	public StickerLocalListFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private StickerCategory mCategory;

	/** 贴纸分类 */
	public void setCategory(StickerCategory category)
	{
		mCategory = category;
	}

	/** 贴纸分类 */
	public StickerCategory getCategory()
	{
		return mCategory;
	}

	/*************************** config *******************************/

	/** 行视图布局ID */
	private int mCellLayoutId;
	/** 分组头部视图布局ID */
	private int mHeaderLayoutId;
	/** 统计格式化字符 */
	private String mTotalFooterFormater;
	/** 空视图布局ID */
	private int mEmptyViewLayouId;

	/**
	 * 设置行视图布局ID
	 * 
	 * @see #
	 *      {@link StickerListCell}
	 * @param resId
	 *            行视图布局ID (默认: tusdk_impl_component_widget_sticker_list_cell)
	 */
	public void setCellLayoutId(int resId)
	{
		mCellLayoutId = resId;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @see #
	 *      {@link StickerListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_widget_sticker_list_cell)
	 */
	public int getCellLayoutId()
	{
		if (mCellLayoutId == 0)
		{
			mCellLayoutId = StickerListCell.getLayoutId();
		}
		return mCellLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #
	 *      {@link StickerListHeader}
	 * @return 分组头部视图布局ID (默认: tusdk_impl_component_widget_sticker_list_header)
	 */
	public int getHeaderLayoutId()
	{
		if (mHeaderLayoutId == 0)
		{
			mHeaderLayoutId = StickerListHeader.getLayoutId();
		}
		return mHeaderLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #
	 *      {@link StickerListHeader}
	 * @param mHeaderLayoutId
	 *            分组头部视图布局ID (默认:
	 *            tusdk_impl_component_widget_sticker_list_header)
	 */
	public void setHeaderLayoutId(int mHeaderLayoutId)
	{
		this.mHeaderLayoutId = mHeaderLayoutId;
	}

	/** 统计格式化字符 (默认: lsq_sticker_total_format [%1$s 张贴纸]) */
	public String getTotalFooterFormater()
	{
		if (mTotalFooterFormater == null)
		{
			mTotalFooterFormater = this.getResString("lsq_sticker_total_format");
		}
		return mTotalFooterFormater;
	}

	/** 统计格式化字符 (默认: lsq_sticker_total_format [%1$s 张贴纸]) */
	public void setTotalFooterFormater(String mTotalFooterFormater)
	{
		this.mTotalFooterFormater = mTotalFooterFormater;
	}

	/** 空视图布局ID */
	public int getEmptyViewLayouId()
	{
		if (mEmptyViewLayouId == 0)
		{
			mEmptyViewLayouId = StickerListEmptyView.getLayoutId();
		}
		return mEmptyViewLayouId;
	}

	/** 空视图布局ID */
	public void setEmptyViewLayouId(int mEmptyViewLayouId)
	{
		this.mEmptyViewLayouId = mEmptyViewLayouId;
	}

	/************************ view *************************/

	/** 贴纸列表视图 */
	private StickerListView mListView;

	/** 贴纸列表视图 */
	public StickerListView getListView()
	{
		if (mListView == null)
		{
			mListView = this.getViewById("lsq_listView");
			if (mListView != null)
			{
				mListView.setCellLayoutId(this.getCellLayoutId());
				mListView.setHeaderLayoutId(this.getHeaderLayoutId());
				mListView.setTotalFooterFormater(this.getTotalFooterFormater());
				mListView.setEmptyView(this.getEmptyViewLayouId());
				this.initEmptyView(mListView.getEmptyView());
			}
		}
		return mListView;
	}

	/** 初始化空视图 */
	private void initEmptyView(View view)
	{
		if (view == null || !(view instanceof StickerListEmptyView)) return;
		StickerListEmptyView emptyView = (StickerListEmptyView) view;
		if (emptyView.getMoreButton() == null) return;
		emptyView.getMoreButton().setOnClickListener(mEmptyViewClickListener);
	}

	/** 点击空视图更多按钮 */
	private OnClickListener mEmptyViewClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			onStickerAction();
		}
	};

	@Override
	protected void loadView(ViewGroup view)
	{
		StickerListView listView = this.getListView();
		if (listView != null)
		{
			listView.loadStickers(this.getCategory());
			listView.setGridDelegate(mStickerListGridDelegate);
		}
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		if (this.getListView() != null)
		{
			this.getListView().emptyNeedFullHeight();
		}
	}

	/** 选中一个贴纸 */
	protected void onStickerSelected(StickerListGrid view, StickerData data)
	{
		if (mDelegate == null) return;
		mDelegate.onStickerLocalListFragmentSelected(this, data);
	}

	/** 删除一个贴纸 */
	protected void onStickerGroup(StickerGroup group, StickerListHeaderAction action)
	{
		if (mDelegate == null) return;
		mDelegate.onStickerLocalListFragmentGroup(this, group, action);
	}

	/** 更多动作 */
	protected void onStickerAction()
	{
		if (mDelegate == null) return;
		mDelegate.onStickerLocalListFragmentAction(this);
	}

	/** 单元格点击委托 */
	protected StickerListGridDelegate mStickerListGridDelegate = new StickerListGridDelegate()
	{
		@Override
		public void onGridItemClick(StickerListGrid view, StickerData data)
		{
			onStickerSelected(view, data);
		}

		/** 删除一个贴纸包 */
		@Override
		public void onStickerListHeaderAction(StickerGroup group, StickerListHeaderAction action)
		{
			onStickerGroup(group, action);
		}
	};
}