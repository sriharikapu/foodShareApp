/** 
 * TuSDKCore
 * TuStickerChooseFragment.java
 *
 * @author 		Clear
 * @Date 		2015-4-27 下午12:30:57 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.sticker;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.AlertDelegate;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.TuSdkViewPager;
import org.lasque.tusdk.core.view.widget.TuSdkViewPager.TuSdkViewPagerDelegate;
import org.lasque.tusdk.core.view.widget.TuSdkViewPager.TuSdkViewPagerDelegateImpl;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.components.sticker.TuStickerOnlineFragment.TuStickerOnlineFragmentDelegate;
import org.lasque.tusdk.impl.components.widget.sticker.StickerListHeader.StickerListHeaderAction;
import org.lasque.tusdk.impl.components.widget.sticker.StickerLocalListFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerLocalListFragment.StickerLocalListFragmentDelegate;
import org.lasque.tusdk.modules.components.sticker.TuStickerChooseFragmentBase;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 贴纸选择控制器
 * 
 * @author Clear
 */
public class TuStickerChooseFragment extends TuStickerChooseFragmentBase implements StickerLocalListFragmentDelegate, TuStickerOnlineFragmentDelegate
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_sticker_choose_fragment");
	}

	/** 图片编辑贴纸选择控制器委托 */
	public interface TuStickerChooseFragmentDelegate
	{
		/**
		 * 选中贴纸
		 * 
		 * @param fragment
		 *            控制器
		 * @param data
		 *            贴纸元素
		 */
		void onTuStickerChooseFragmentSelected(TuStickerChooseFragment fragment, StickerData data);
	}

	/** 图片编辑贴纸选择控制器委托 */
	private TuStickerChooseFragmentDelegate mDelegate;

	/** 图片编辑贴纸选择控制器委托 */
	public TuStickerChooseFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑贴纸选择控制器委托 */
	public void setDelegate(TuStickerChooseFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/** 贴纸选择控制器 */
	public TuStickerChooseFragment()
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

	/********************************** Config ***********************************/
	/** 分类按钮列表 */
	private List<TuSdkTextButton> mCateBtns;
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
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListCell}
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
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_widget_sticker_list_cell)
	 */
	public int getCellLayoutId()
	{
		return mCellLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListHeader}
	 * @return 分组头部视图布局ID (默认: tusdk_impl_component_widget_sticker_list_header)
	 */
	public int getHeaderLayoutId()
	{
		return mHeaderLayoutId;
	}

	/**
	 * 分组头部视图布局ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListHeader}
	 * @param mHeaderLayoutId
	 *            分组头部视图布局ID (默认:
	 *            tusdk_impl_component_widget_sticker_list_header)
	 */
	public void setHeaderLayoutId(int mHeaderLayoutId)
	{
		this.mHeaderLayoutId = mHeaderLayoutId;
	}

	/** 统计格式化字符 (默认: lsq_album_total_format [%1$s 张照片]) */
	public String getTotalFooterFormater()
	{
		return mTotalFooterFormater;
	}

	/** 统计格式化字符 (默认: lsq_album_total_format [%1$s 张照片]) */
	public void setTotalFooterFormater(String mTotalFooterFormater)
	{
		this.mTotalFooterFormater = mTotalFooterFormater;
	}

	/** 空视图布局ID */
	public int getEmptyViewLayouId()
	{
		return mEmptyViewLayouId;
	}

	/** 空视图布局ID */
	public void setEmptyViewLayouId(int mEmptyViewLayouId)
	{
		this.mEmptyViewLayouId = mEmptyViewLayouId;
	}

	/************************* view ******************************/
	/** 分页视图 */
	private TuSdkViewPager mViewPager;
	/** 分类包装视图 */
	private RelativeLayout mCategoryWrap;
	/** 分类视图 */
	private LinearLayout mCategoryView;
	/** 分类选中游标 */
	private View mCursor;

	/** 分页视图 */
	public final TuSdkViewPager getViewPager()
	{
		if (mViewPager == null)
		{
			mViewPager = this.getViewById("lsq_viewPager");
			if (mViewPager != null)
			{
				mViewPager.bindView(this.getFragmentManager(), mTuSdkViewPagerDelegate);
				// mViewPager.setOffscreenPageLimit(3);
			}
		}
		return mViewPager;
	}

	/** 分类包装视图 */
	public final RelativeLayout getCategoryWrap()
	{
		if (mCategoryWrap == null)
		{
			mCategoryWrap = this.getViewById("lsq_categoryWrap");
		}
		return mCategoryWrap;
	}

	/** 分类视图 */
	public final LinearLayout getCategoryView()
	{
		if (mCategoryView == null)
		{
			mCategoryView = this.getViewById("lsq_categoryView");
		}
		return mCategoryView;
	}

	/** 分类选中游标 */
	public final View getCursor()
	{
		if (mCursor == null)
		{
			mCursor = this.getViewById("lsq_selectedCursor");
		}
		return mCursor;
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

	/** 分发视图点击事件 */
	protected void dispatcherViewClick(View v)
	{
		if (v instanceof TuSdkTextButton)
		{
			this.handleCategoryButton(((TuSdkTextButton) v).index);
		}
	}

	/********************************** loadView ***********************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 分页视图
		getViewPager();
		// 分类包装视图
		getCategoryWrap();
		// 分类选中游标
		this.showViewIn(getCursor(), false);
		// 分类视图
		this.buildCategoryButtons(getCategoryView());
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);

		// 初始化分类游标
		this.initCategoryCursor(getCursor());
		// 选中第一个分类
		this.onSelectCategory(0);
	}

	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		this.setTitle(this.getResString("lsq_sticker_title"));
		this.setNavLeftButton(this.getResString("lsq_nav_cancel"));
		this.setNavRightButton(this.getResString("lsq_nav_more"), TuSdkContext.getColor("lsq_navigator_button_right_title"));
	}

	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(null);
	}

	/** 更多 */
	@Override
	public void navigatorBarRightAction(NavigatorBarButtonInterface button)
	{
		TuStickerOnlineFragment fragment = new TuStickerOnlineFragment();
		fragment.setDelegate(this);
		this.pushFragment(fragment);
	}

	/** 创建分类按钮 */
	private void buildCategoryButtons(LinearLayout categoryView)
	{
		if (categoryView == null || this.getCategories() == null) return;

		categoryView.removeAllViews();

		mCateBtns = new ArrayList<TuSdkTextButton>(this.getCategories().size());

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

		for (StickerCategory cate : this.getCategories())
		{
			TuSdkTextButton btn = this.createCategoryButton(cate);
			if (btn != null)
			{
				btn.index = mCateBtns.size();
				btn.setOnClickListener(mButtonClickListener);
				categoryView.addView(btn, params);
				mCateBtns.add(btn);
			}
		}
	}

	/** 创建分类按钮 */
	protected TuSdkTextButton createCategoryButton(StickerCategory cate)
	{
		if (cate == null) return null;
		TuSdkTextButton btn = new TuSdkTextButton(this.getActivity());

		int[] colors = new int[] {
				TuSdkContext.getColor("lsq_sticker_title_selected_color"), TuSdkContext.getColor("lsq_sticker_title_color") };
		int[][] states = new int[][] {
				new int[] { android.R.attr.state_selected }, new int[] {} };

		ColorStateList colorStateList = new ColorStateList(states, colors);

		btn.setTextColor(colorStateList);
		btn.setGravity(Gravity.CENTER);
		btn.setText(TuSdkContext.getString(cate.name));
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		return btn;
	}

	/** 选中分类 */
	protected void handleCategoryButton(int index)
	{
		if (this.getViewPager() != null)
		{
			this.getViewPager().setCurrentItem(index);
		}
	}

	/** 选中一个分类 */
	private void onSelectCategory(int index)
	{
		if (mCateBtns == null) return;

		for (TuSdkTextButton btn : mCateBtns)
		{
			btn.setSelected(btn.index == index);
		}

		moveCursorTo(index);
	}

	/** 创建贴纸列表控制器 */
	protected Fragment buildStickerListFragment(int index)
	{
		StickerLocalListFragment fragment = new StickerLocalListFragment();

		// 行视图布局ID
		fragment.setCellLayoutId(this.getCellLayoutId());
		// 分组头部视图布局ID
		fragment.setHeaderLayoutId(this.getHeaderLayoutId());
		// 统计格式化字符
		fragment.setTotalFooterFormater(this.getTotalFooterFormater());
		// 空视图布局ID
		fragment.setEmptyViewLayouId(this.getEmptyViewLayouId());

		fragment.setCategory(this.getCategory(index));
		fragment.setDelegate(this);

		return fragment;
	}

	/**
	 * 选中一个贴纸
	 * 
	 * @param fragment
	 *            在线贴纸控制器
	 * @param data
	 *            贴纸数据
	 */
	@Override
	public void onTuStickerOnlineFragmentSelected(TuStickerOnlineFragment fragment, StickerData data)
	{
		if (data == null) return;
		this.onStickerLocalListFragmentSelected(null, data);
	}

	/** 更多动作 */
	@Override
	public void onStickerLocalListFragmentAction(StickerLocalListFragment fragment)
	{
		this.navigatorBarRightAction(null);
	}

	/**
	 * 选中贴纸
	 * 
	 * @param fragment
	 *            控制器
	 * @param data
	 *            贴纸数据
	 */
	@Override
	public void onStickerLocalListFragmentSelected(StickerLocalListFragment fragment, StickerData data)
	{
		if (mDelegate != null)
		{
			mDelegate.onTuStickerChooseFragmentSelected(this, data);
		}
	}

	/**
	 * 删除一个贴纸包
	 * 
	 * @param fragment
	 *            控制器
	 * @param group
	 *            贴纸包
	 */
	@Override
	public void onStickerLocalListFragmentGroup(StickerLocalListFragment fragment, StickerGroup group, StickerListHeaderAction action)
	{
		if (group == null || action == null) return;

		if (action == StickerListHeaderAction.ActionDetail)
		{
			TuStickerOnlineFragment ofragment = new TuStickerOnlineFragment();
			ofragment.setDetailDataId(group.groupId);
			ofragment.setDelegate(this);
			this.pushFragment(ofragment);
			return;
		}

		mRemoveGroup = group;
		TuSdkViewHelper.alert(mAlertDelegate, this.getActivity(), this.getResString("lsq_sticker_remove_title"),
				this.getResString("lsq_sticker_remove_msg", group.name), this.getResString("lsq_nav_cancel"), this.getResString("lsq_nav_remove"));
	}

	/** 需要删除的贴纸包 */
	private StickerGroup mRemoveGroup;

	/** 警告信息委托 */
	private AlertDelegate mAlertDelegate = new AlertDelegate()
	{
		@Override
		public void onAlertConfirm(AlertDialog dialog)
		{
			removeStickerGroup(mRemoveGroup);
			mRemoveGroup = null;
		}
	};

	/************************* Cursor ****************************/
	/** 分类按钮宽度 */
	private int mCateBtnWidth;

	/** 初始化分类游标 */
	private void initCategoryCursor(View cursor)
	{
		if (cursor == null || this.getCategoryWrap() == null || mCateBtns == null || mCateBtns.isEmpty()) return;
		TuSdkViewHelper.getViewRect(cursor);

		MarginLayoutParams params = TuSdkViewHelper.getMarginLayoutParams(cursor);
		// 分类按钮宽度
		mCateBtnWidth = this.getCategoryWrap().getWidth() / mCateBtns.size();

		params.width = mCateBtnWidth - params.leftMargin - params.rightMargin;
		cursor.setLayoutParams(params);

		this.showViewIn(cursor, true);
	}

	/** 移动游标 */
	private void moveCursorTo(int index)
	{
		if (this.getCursor() == null) return;
		ViewCompat.animate(this.getCursor()).translationX(index * mCateBtnWidth).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
	}

	/*********************** TuSdkViewPagerDelegate ******************************/
	/** 分页滚动视图委托 */
	protected TuSdkViewPagerDelegate mTuSdkViewPagerDelegate = new TuSdkViewPagerDelegateImpl()
	{
		/** 包含页数 */
		@Override
		public int tuSdkViewPagerTotal()
		{
			if (getCategories() != null)
			{
				return getCategories().size();
			}
			return 0;
		}

		/** 创建包含的Fragment */
		@Override
		public Fragment onTuSdkViewPagerBuild(int index)
		{
			return buildStickerListFragment(index);
		}

		/** 视图页数改变 */
		@Override
		public void onTuSdkViewPagerChanged(int index)
		{
			onSelectCategory(index);
		}
	};

	/***************** StickerPackageDelegate *****************/
	/** 重新加载贴纸 */
	@Override
	protected void reloadStickers()
	{
		super.reloadStickers();
		// 刷新下载数据
		this.getViewPager().reloadData();
	}
}