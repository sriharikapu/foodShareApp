/** 
 * TuSDKCore
 * TuEditFilterBarView.java
 *
 * @author 		Clear
 * @Date 		2015-2-15 下午5:54:41 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.widget.filter.GroupFilterBar;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface.ParameterConfigViewDelegate;
import org.lasque.tusdk.modules.view.widget.filter.FilterSubtitleViewInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface.GroupFilterAction;
import org.lasque.tusdk.modules.view.widget.filter.TuEditFilterViewBase;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 图片编辑滤镜控制器滤镜栏视图
 * 
 * @author Clear
 */
public class TuEditFilterBarView extends TuEditFilterViewBase implements ParameterConfigViewDelegate
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_filter_bar_view");
	}

	public TuEditFilterBarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuEditFilterBarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuEditFilterBarView(Context context)
	{
		super(context);
	}

	/** 图片编辑滤镜控制器滤镜栏视图委托 */
	public interface TuEditFilterBarDelegate
	{
		/**
		 * 选中一个滤镜
		 * 
		 * @param view
		 *            图片编辑滤镜控制器滤镜栏视图
		 * @param itemData
		 *            滤镜分组元素
		 * @return
		 */
		boolean onFilterSelected(TuEditFilterBarView view, GroupFilterItem itemData);

		/**
		 * 通知重新绘制
		 * 
		 * @param view
		 *            图片编辑滤镜控制器滤镜栏视图
		 */
		void onFilterConfigRequestRender(TuEditFilterBarView view);
	}

	/** 图片编辑滤镜控制器滤镜栏视图委托 */
	private TuEditFilterBarDelegate mDelegate;

	/** 滤镜组选择栏底部距离 (默认：44dp) */
	private int mFilterBarBottom;

	/** 图片编辑滤镜控制器滤镜栏视图委托 */
	public TuEditFilterBarDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑滤镜控制器滤镜栏视图委托 */
	public void setDelegate(TuEditFilterBarDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/** 滤镜组选择栏底部距离 (默认：44dp) */
	public int getFilterBarBottom()
	{
		return mFilterBarBottom;
	}

	/** 滤镜组选择栏底部距离 (默认：44dp) */
	public void setFilterBarBottom(int mFilterBarBottom)
	{
		this.mFilterBarBottom = mFilterBarBottom;
		if (this.getGroupFilterBar() != null)
		{
			this.getGroupFilterBar().setMarginBottom(this.getFilterBarBottom());
		}
	}

	/*************** View **************/
	/** 参数配置视图 */
	private ParameterConfigViewInterface mConfigView;
	/** 控制栏 */
	private LinearLayout mBottomBar;
	/** 滤镜组选择栏 */
	private GroupFilterBar mGroupFilterBar;
	/** 滤镜标题视图 */
	private FilterSubtitleViewInterface mFilterTitleView;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;

	/** 参数配置视图 */
	@SuppressWarnings("unchecked")
	public <T extends View & ParameterConfigViewInterface> T getConfigView()
	{
		if (mConfigView == null)
		{
			View view = this.getViewById("lsq_param_config_view");
			if (view == null || !(view instanceof ParameterConfigViewInterface)) return null;
			mConfigView = (ParameterConfigViewInterface) view;
			if (mConfigView != null)
			{
				mConfigView.setDelegate(this);
			}
		}
		return (T) mConfigView;
	}

	/** 控制栏 */
	public LinearLayout getBottomBar()
	{
		if (mBottomBar == null)
		{
			mBottomBar = this.getViewById("lsq_bar_bottomBar");
		}
		return mBottomBar;
	}

	/** 滤镜标题视图 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & FilterSubtitleViewInterface> T getFilterTitleView()
	{
		if (mFilterTitleView == null)
		{
			View view = this.getViewById("lsq_filter_title_view");
			if (view == null || !(view instanceof FilterSubtitleViewInterface)) return null;
			mFilterTitleView = (FilterSubtitleViewInterface) view;
		}
		return (T) mFilterTitleView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GroupFilterBar getGroupFilterBar()
	{
		if (mGroupFilterBar == null)
		{
			mGroupFilterBar = this.getViewById("lsq_group_filter_bar");
			this.configGroupFilterBar(mGroupFilterBar, GroupFilterAction.ActionEdit);
		}
		return mGroupFilterBar;
	}

	/** 取消按钮 */
	public final TuSdkImageButton getCancelButton()
	{
		if (mCancelButton == null)
		{
			mCancelButton = this.getViewById("lsq_bar_cancelButton");
			if (mCancelButton != null)
			{
				mCancelButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCancelButton;
	}

	/** 完成按钮 */
	public final TuSdkImageButton getCompleteButton()
	{
		if (mCompleteButton == null)
		{
			mCompleteButton = this.getViewById("lsq_bar_completeButton");
			if (mCompleteButton != null)
			{
				mCompleteButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCompleteButton;
	}

	/** 按钮点击事件 */
	private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getCancelButton()))
			{
				handleCancelAction();
			}
			else if (equalViewIds(v, getCompleteButton()))
			{
				handleCompleteAction();
			}
		}
	};

	@Override
	public void loadView()
	{
		super.loadView();
		getCancelButton();
		getCompleteButton();
		getBottomBar();
		getConfigView();
		getGroupFilterBar();
	}

	/** 取消设置 */
	@Override
	protected void handleCancelAction()
	{
		this.showConfigView(false);

		super.handleCancelAction();
	}

	/** 完成设置 */
	protected void handleCompleteAction()
	{
		this.showConfigView(false);
	}

	@Override
	public void setDefaultShowState(boolean isShow)
	{
		this.showViewIn(this.getConfigView(), false);
		if (this.getBottomBar() != null)
		{
			ViewCompat.setTranslationY(this.getBottomBar(), this.getBottomBar().getHeight());
			this.showViewIn(this.getBottomBar(), false);
		}
	}

	/** 加载滤镜 */
	public void loadFilters(FilterOption option)
	{
		if (getGroupFilterBar() != null)
		{
			getGroupFilterBar().loadFilters(option);
		}
	}

	/** 选中一个滤镜数据 */
	protected boolean onFilterSelected(GroupFilterItem itemData)
	{
		if (mDelegate == null) return true;
		return mDelegate.onFilterSelected(this, itemData);
	}

	/** 显示配置视图 */
	@Override
	protected void showConfigView(final boolean isShow)
	{
		this.showViewIn(this.getConfigView(), true);

		if (this.getBottomBar() != null)
		{
			this.showViewIn(this.getBottomBar(), true);
			int tranY = isShow ? 0 : this.getBottomBar().getHeight();
			ViewCompat.animate(this.getBottomBar()).translationY(tranY).setDuration(220).setInterpolator(new AccelerateDecelerateInterpolator());
		}

		if (this.getGroupFilterBar() == null) return;
		this.showViewIn(this.getGroupFilterBar(), true);
		ViewCompat.animate(this.getGroupFilterBar()).alpha(isShow ? 0 : 1).setDuration(220).setListener(new TuSdkViewAnimatorAdapter()
		{
			@Override
			public void onAnimationEnd(View view, boolean cancelled)
			{
				if (cancelled) return;
				showViewIn(getGroupFilterBar(), !isShow);
				showViewIn(getConfigView(), isShow);
				showViewIn(getBottomBar(), isShow);
			}
		});
	}

	/**************** Filter config ******************/

	/** 设置配置视图参数 */
	@Override
	protected void setConfigViewParams(List<String> keys)
	{
		if (this.getConfigView() == null || keys == null || keys.size() == 0) return;
		this.getConfigView().setParams(keys, 0);
	}

	/** 请求渲染 */
	@Override
	protected void requestRender()
	{
		super.requestRender();
		if (mDelegate != null)
		{
			mDelegate.onFilterConfigRequestRender(this);
		}
	}

	/****************** ParameterConfigViewDelegate *******************/
	/**
	 * 参数数据改变
	 * 
	 * @param view
	 *            参数配置视图
	 * @param index
	 *            参数索引
	 * @param progress
	 *            百分比进度
	 */
	@Override
	public void onParameterConfigDataChanged(ParameterConfigViewInterface view, int index, float progress)
	{
		FilterArg arg = this.getFilterArg(index);
		if (arg == null) return;

		arg.setPrecentValue(progress);
		this.requestRender();
	}

	/** 重置参数 */
	@Override
	public void onParameterConfigRest(ParameterConfigViewInterface view, int index)
	{
		FilterArg arg = this.getFilterArg(index);
		if (arg == null) return;

		arg.reset();
		this.requestRender();

		view.seekTo(arg.getPrecentValue());
	}

	/** 读取参数值 */
	@Override
	public float readParameterValue(ParameterConfigViewInterface view, int index)
	{
		FilterArg arg = this.getFilterArg(index);
		if (arg == null) return 0;
		return arg.getPrecentValue();
	}
}