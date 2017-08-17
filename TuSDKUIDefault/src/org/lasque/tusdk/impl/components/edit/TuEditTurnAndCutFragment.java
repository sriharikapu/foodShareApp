/** 
 * TuSDKCore
 * TuEditTurnAndCutFragment.java
 *
 * @author 		Clear
 * @Date 		2014-11-30 下午8:00:33 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface.LsqImageChangeType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.impl.components.edit.TuNormalFilterView.TuNormalFilterViewDelegate;
import org.lasque.tusdk.impl.components.filter.TuFilterOnlineFragment;
import org.lasque.tusdk.modules.components.ComponentErrorType;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.edit.TuEditTurnAndCutFragmentBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem.GroupFilterItemType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 裁剪与缩放控制器
 * 
 * @author Clear
 */
public class TuEditTurnAndCutFragment extends TuEditTurnAndCutFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_turn_and_cut_fragment");
	}

	/** 裁剪与缩放控制器委托 */
	public interface TuEditTurnAndCutFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            旋转和裁剪视图控制器
		 * @param result
		 *            旋转和裁剪视图控制器处理结果
		 */
		void onTuEditTurnAndCutFragmentEdited(TuEditTurnAndCutFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            旋转和裁剪视图控制器
		 * @param result
		 *            旋转和裁剪视图控制器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditTurnAndCutFragmentEditedAsync(TuEditTurnAndCutFragment fragment, TuSdkResult result);
	}

	/** 裁剪与缩放控制器委托 */
	private TuEditTurnAndCutFragmentDelegate mDelegate;

	/** 裁剪与缩放控制器委托 */
	public TuEditTurnAndCutFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 裁剪与缩放控制器委托 */
	public void setDelegate(TuEditTurnAndCutFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 裁剪与缩放控制器 */
	public TuEditTurnAndCutFragment()
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

	/**
	 * 通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 */
	@Override
	protected void notifyProcessing(TuSdkResult result)
	{
		// 显示测试预览视图
		if (this.showResultPreview(result)) return;

		if (this.mDelegate == null) return;
		this.mDelegate.onTuEditTurnAndCutFragmentEdited(this, result);
	}

	/**
	 * 异步通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	protected boolean asyncNotifyProcessing(TuSdkResult result)
	{
		if (this.mDelegate == null) return false;
		return this.mDelegate.onTuEditTurnAndCutFragmentEditedAsync(this, result);
	}

	/*************************** config *******************************/
	/** 是否开启滤镜支持 (默认: 关闭) */
	private boolean mEnableFilters;
	/** 需要显示的滤镜组 */
	private List<String> mFilterGroup;
	/** 需要裁剪的长宽 */
	private TuSdkSize mCutSize;
	/** 行视图宽度 */
	private int mGroupFilterCellWidth;
	/** 滤镜分组列表行视图布局资源ID */
	private int mGroupTableCellLayoutId;
	/** 滤镜列表行视图布局资源ID */
	private int mFilterTableCellLayoutId;
	/** 滤镜组选择栏高度 */
	private int mFilterBarHeight;
	/** 开启用户滤镜历史记录 */
	private boolean mEnableFiltersHistory;
	/** 显示滤镜标题视图 */
	private boolean mDisplayFiltersSubtitles;
	/** 开启在线滤镜 */
	private boolean mEnableOnlineFilter;
	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
	 * TuFilterOnlineFragmentInterface接口)
	 */
	private Class<?> mOnlineFragmentClazz;
	/** 是否渲染滤镜封面 */
	private boolean mRenderFilterThumb;

	/** 是否开启滤镜支持 (默认: 关闭) */
	public boolean isEnableFilters()
	{
		return mEnableFilters;
	}

	/** 是否开启滤镜支持 (默认: 关闭) */
	public void setEnableFilters(boolean mEnableFilters)
	{
		this.mEnableFilters = mEnableFilters;
	}

	/** 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜) */
	public List<String> getFilterGroup()
	{
		return mFilterGroup;
	}

	/** 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜) */
	public void setFilterGroup(List<String> mFilterGroup)
	{
		this.mFilterGroup = mFilterGroup;
	}

	/** 需要裁剪的长宽 */
	@Override
	public TuSdkSize getCutSize()
	{
		return mCutSize;
	}

	/** 需要裁剪的长宽 */
	public void setCutSize(TuSdkSize mCutSize)
	{
		this.mCutSize = mCutSize;
	}

	/** 行视图宽度 */
	public int getGroupFilterCellWidth()
	{
		return mGroupFilterCellWidth;
	}

	/** 行视图宽度 */
	public void setGroupFilterCellWidth(int mGroupFilterCellWidth)
	{
		this.mGroupFilterCellWidth = mGroupFilterCellWidth;
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
	 * @return 滤镜分组列表行视图布局资源ID (默认:
	 *         tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *         GroupFilterGroupView)
	 */
	public int getGroupTableCellLayoutId()
	{
		return mGroupTableCellLayoutId;
	}

	/**
	 * 滤镜分组列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
	 *            GroupFilterGroupView)
	 */
	public void setGroupTableCellLayoutId(int mGroupTableCellLayoutId)
	{
		this.mGroupTableCellLayoutId = mGroupTableCellLayoutId;
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @return 滤镜列表行视图布局资源ID (默认:
	 *         tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *         GroupFilterItemView)
	 */
	public int getFilterTableCellLayoutId()
	{
		return mFilterTableCellLayoutId;
	}

	/**
	 * 滤镜列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @param 滤镜列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemView)
	 */
	public void setFilterTableCellLayoutId(int mFilterTableCellLayoutId)
	{
		this.mFilterTableCellLayoutId = mFilterTableCellLayoutId;
	}

	/** 滤镜组选择栏高度 */
	public int getFilterBarHeight()
	{
		return mFilterBarHeight;
	}

	/** 滤镜组选择栏高度 */
	public void setFilterBarHeight(int mFilterBarHeight)
	{
		this.mFilterBarHeight = mFilterBarHeight;
	}

	/** 开启用户滤镜历史记录 */
	public boolean isEnableFiltersHistory()
	{
		return mEnableFiltersHistory;
	}

	/** 开启用户滤镜历史记录 */
	public void setEnableFiltersHistory(boolean mEnableFiltersHistory)
	{
		this.mEnableFiltersHistory = mEnableFiltersHistory;
	}

	/** 显示滤镜标题视图 */
	public boolean isDisplayFiltersSubtitles()
	{
		return mDisplayFiltersSubtitles;
	}

	/** 显示滤镜标题视图 */
	public void setDisplayFiltersSubtitles(boolean mDisplayFiltersSubtitles)
	{
		this.mDisplayFiltersSubtitles = mDisplayFiltersSubtitles;
	}

	/** 开启在线滤镜 */
	public boolean isEnableOnlineFilter()
	{
		return mEnableOnlineFilter;
	}

	/** 开启在线滤镜 */
	public void setEnableOnlineFilter(boolean mEnableOnlineFilter)
	{
		this.mEnableOnlineFilter = mEnableOnlineFilter;
	}

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
	 * 
	 * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
	 */
	public Class<?> getOnlineFragmentClazz()
	{
		if (mOnlineFragmentClazz == null)
		{
			mOnlineFragmentClazz = TuFilterOnlineFragment.class;
		}
		return mOnlineFragmentClazz;
	}

	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
	 * 
	 * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
	 */
	public void setOnlineFragmentClazz(Class<?> mOnlineFragmentClazz)
	{
		this.mOnlineFragmentClazz = mOnlineFragmentClazz;
	}

	/** 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台) */
	public boolean isRenderFilterThumb()
	{
		return mRenderFilterThumb;
	}

	/** 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台) */
	public void setRenderFilterThumb(boolean isRender)
	{
		mRenderFilterThumb = isRender;
	}

	/*************************** view *******************************/
	/** 图片包装视图 */
	private RelativeLayout mImageWrapView;
	/** 裁剪选取视图 */
	private TuMaskRegionView mCutRegionView;
	/** 裁剪与缩放控制器滤镜视图 */
	private TuNormalFilterView mGroupFilterView;
	/** 滤镜按钮 */
	private ImageView mFilterButton;
	/** 旋转按钮 */
	private ImageView mTrunButton;
	/** 镜像按钮 */
	private ImageView mMirrorButton;
	/** 完成按钮 */
	private TextView mCompleteButton;

	/** 图片包装视图 */
	@Override
	public RelativeLayout getImageWrapView()
	{
		if (mImageWrapView == null)
		{
			mImageWrapView = this.getViewById("lsq_imageWrapView");
			if (mImageWrapView != null)
			{
				mImageWrapView.setClickable(false);
				mImageWrapView.setClipChildren(false);
			}
		}
		return mImageWrapView;
	}

	/** 裁剪选取视图 */
	@Override
	public TuMaskRegionView getCutRegionView()
	{
		if (mCutRegionView == null)
		{
			mCutRegionView = this.getViewById("lsq_cutRegionView");
			if (mCutRegionView != null)
			{
				mCutRegionView.setEdgeMaskColor(0x80000000);
				mCutRegionView.setEdgeSideColor(0x80FFFFFF);
				mCutRegionView.setEdgeSideWidthDP(2);
				mCutRegionView.addOnLayoutChangeListener(mRegionLayoutChangeListener);
			}
		}
		return mCutRegionView;
	}

	/** 裁剪与缩放控制器滤镜视图 */
	public TuNormalFilterView getGroupFilterBar()
	{
		if (mGroupFilterView == null)
		{
			mGroupFilterView = this.getViewById("lsq_group_filter_view");
			if (mGroupFilterView != null)
			{
				this.configGroupFilterView(mGroupFilterView);
				// 裁剪与缩放控制器滤镜视图委托
				mGroupFilterView.setDelegate(mFilterViewDelegate);
			}
		}
		return mGroupFilterView;
	}

	/**
	 * 配置滤镜栏视图
	 * 
	 * @param view
	 *            滤镜分组视图基类
	 */
	protected void configGroupFilterView(GroupFilterBaseView view)
	{
		if (view == null) return;
		// 行视图宽度
		view.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		// 滤镜组选择栏高度
		view.setFilterBarHeight(this.getFilterBarHeight());
		// 滤镜分组列表行视图布局资源ID
		view.setGroupTableCellLayoutId(this.getGroupTableCellLayoutId());
		// 滤镜列表行视图布局资源ID
		view.setFilterTableCellLayoutId(this.getFilterTableCellLayoutId());
		// 指定显示的滤镜组
		view.setFilterGroup(this.getFilterGroup());
		// 开启用户滤镜历史记录
		view.setEnableHistory(this.isEnableFiltersHistory());
		// 显示滤镜标题视图
		view.setDisplaySubtitles(this.isDisplayFiltersSubtitles());
		// 设置控制器
		view.setActivity(this.getActivity());
		// 开启在线滤镜
		view.setEnableOnlineFilter(this.isEnableOnlineFilter());
		// 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
		view.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		view.setRenderFilterThumb(this.isRenderFilterThumb());
	}

	/** 滤镜按钮 */
	public ImageView getFilterButton()
	{
		if (mFilterButton == null)
		{
			mFilterButton = this.getViewById("lsq_filterButton");
			if (mFilterButton != null)
			{
				mFilterButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mFilterButton;
	}

	/** 旋转按钮 */
	public ImageView getTrunButton()
	{
		if (mTrunButton == null)
		{
			mTrunButton = this.getViewById("lsq_trunButton");
			if (mTrunButton != null)
			{
				mTrunButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mTrunButton;
	}

	/** 镜像按钮 */
	public ImageView getMirrorButton()
	{
		if (mMirrorButton == null)
		{
			mMirrorButton = this.getViewById("lsq_mirrorButton");
			if (mMirrorButton != null)
			{
				mMirrorButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mMirrorButton;
	}

	/** 完成按钮 */
	public TextView getCompleteButton()
	{
		if (mCompleteButton == null)
		{
			mCompleteButton = this.getViewById("lsq_completeButton");
			if (mCompleteButton != null)
			{
				mCompleteButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCompleteButton;
	}

	/** 裁剪与缩放控制器滤镜视图委托 */
	private TuNormalFilterViewDelegate mFilterViewDelegate = new TuNormalFilterViewDelegate()
	{
		@Override
		public boolean onTuNormalFilterViewSelected(TuNormalFilterView view, GroupFilterItem itemData)
		{
			if (itemData.type == GroupFilterItemType.TypeFilter)
			{
				return handleSwitchFilter(itemData.getFilterCode());
			}
			return true;
		}
	};

	/** 按钮点击事件 */
	private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
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
		if (this.equalViewIds(v, this.getFilterButton()))
		{
			this.handleFilterButton();
		}
		else if (this.equalViewIds(v, this.getTrunButton()))
		{
			this.handleTrunButton();
		}
		else if (this.equalViewIds(v, this.getMirrorButton()))
		{
			this.handleMirrorButton();
		}
		else if (this.equalViewIds(v, this.getCompleteButton()))
		{
			this.handleCompleteButton();
		}
	}

	/************************** loadView *****************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		this.getImageView();
		this.showViewIn(this.getCutRegionView(), this.getCutSize() != null);
		this.getGroupFilterBar();
		this.showViewIn(this.getFilterButton(), this.isEnableFilters());
		this.getTrunButton();
		this.getMirrorButton();
		this.getCompleteButton();
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		// 没有输入图片
		if (this.getImage() == null)
		{
			this.notifyError(null, ComponentErrorType.TypeInputImageEmpty);
			TLog.e("Can not find input image.");
			return;
		}

		super.viewDidLoad(view);

		// 加载滤镜
		if (this.getGroupFilterBar() != null && this.isEnableFilters())
		{
			this.getGroupFilterBar().setThumbImage(this.getImage());
			this.getGroupFilterBar().loadFilters();
		}
		// 初始化默认不显示滤镜栏
		if (this.getGroupFilterBar() != null) 
			this.getGroupFilterBar().setDefaultShowState(false);

	}

	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		super.navigatorBarLoaded(navigatorBar);
		this.setTitle(this.getResString("lsq_edit_title"));
		this.setNavLeftButton(this.getResString("lsq_nav_back"));

		navigatorBar.bringToFront();
	}

	/** 点击取消 */
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(button);
	}

	/** 切换滤镜视图显示 */
	protected void handleFilterButton()
	{
		if (this.getGroupFilterBar() != null)
		{
			// 切换滤镜栏显示状态
			this.getGroupFilterBar().toggleBarShowState();
		}
	}

	/** 旋转动作 */
	protected void handleTrunButton()
	{
		if (this.getImageView() == null || this.getImageView().isInAnimation()) return;

		this.getImageView().changeImageAnimation(LsqImageChangeType.TypeImageChangeTurnLeft);
	}

	/** 镜像动作 */
	protected void handleMirrorButton()
	{
		if (this.getImageView() == null || this.getImageView().isInAnimation()) return;

		this.getImageView().changeImageAnimation(LsqImageChangeType.TypeImageChangeMirrorHorizontal);
	}
}