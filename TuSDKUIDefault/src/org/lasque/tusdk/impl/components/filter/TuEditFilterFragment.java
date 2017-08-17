/** 
 * TuSDKCore
 * TuEditFilterFragment.java
 *
 * @author 		Clear
 * @Date 		2014-12-23 下午6:05:26 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.filter.TuEditFilterBarView.TuEditFilterBarDelegate;
import org.lasque.tusdk.modules.components.ComponentErrorType;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.filter.TuEditFilterFragmentBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem.GroupFilterItemType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 图片编辑滤镜控制器
 * 
 * @author Clear
 */
public class TuEditFilterFragment extends TuEditFilterFragmentBase implements TuEditFilterBarDelegate
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_filter_fragment");
	}

	/** 图片编辑滤镜控制器委托 */
	public interface TuEditFilterFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑滤镜控制器
		 * @param result
		 *            图片编辑滤镜控制器处理结果
		 */
		void onTuEditFilterFragmentEdited(TuEditFilterFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑滤镜控制器
		 * @param result
		 *            图片编辑滤镜控制器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditFilterFragmentEditedAsync(TuEditFilterFragment fragment, TuSdkResult result);
	}

	/** 图片编辑滤镜控制器委托 */
	private TuEditFilterFragmentDelegate mDelegate;

	/** 图片编辑滤镜控制器委托 */
	public TuEditFilterFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑滤镜控制器委托 */
	public void setDelegate(TuEditFilterFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑滤镜控制器 */
	public TuEditFilterFragment()
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
		this.mDelegate.onTuEditFilterFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditFilterFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/
	/** 需要显示的滤镜组 */
	private List<String> mFilterGroup;
	/** 开启滤镜配置选项 (默认: 开启) */
	private boolean mEnableFilterConfig = true;
	/** 是否仅返回滤镜，不返回处理图片 */
	private boolean mOnlyReturnFilter;
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
	/** 开启无效果滤镜 (默认: true) */
	private boolean mEnableNormalFilter = true;
	/** 开启在线滤镜 */
	private boolean mEnableOnlineFilter;
	/**
	 * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
	 * TuFilterOnlineFragmentInterface接口)
	 */
	private Class<?> mOnlineFragmentClazz;
	/** 是否渲染滤镜封面 */
	private boolean mRenderFilterThumb;

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

	/** 开启滤镜配置选项 (默认: 开启) */
	public boolean isEnableFilterConfig()
	{
		return mEnableFilterConfig;
	}

	/** 开启滤镜配置选项 (默认: 开启) */
	public void setEnableFilterConfig(boolean mEnableFilterConfig)
	{
		this.mEnableFilterConfig = mEnableFilterConfig;
	}

	/** 是否仅返回滤镜，不返回处理图片(默认：false) */
	@Override
	public boolean isOnlyReturnFilter()
	{
		return mOnlyReturnFilter;
	}

	/** 是否仅返回滤镜，不返回处理图片(默认：false) */
	public void setOnlyReturnFilter(boolean mOnlyReturnFilter)
	{
		this.mOnlyReturnFilter = mOnlyReturnFilter;
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
	
	/** 开启无效果滤镜 (默认: true) */
	public boolean isEnableNormalFilter()
	{
		return mEnableNormalFilter;
	}

	/** 开启无效果滤镜 (默认: true) */
	public void setEnableNormalFilter(boolean mEnableNormalFilter)
	{
		this.mEnableNormalFilter = mEnableNormalFilter;
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
	/** 图片编辑滤镜控制器滤镜栏视图 */
	private TuEditFilterBarView mFilterbar;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;

	/** 图片包装视图 */
	@Override
	public RelativeLayout getImageWrapView()
	{
		if (mImageWrapView == null)
		{
			mImageWrapView = this.getViewById("lsq_imageWrapView");
		}
		return mImageWrapView;
	}

	/** 取消按钮 */
	public TuSdkImageButton getCancelButton()
	{
		if (mCancelButton == null)
		{
			mCancelButton = this.getViewById("lsq_cancelButton");
			if (mCancelButton != null)
			{
				mCancelButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCancelButton;
	}

	/** 完成按钮 */
	public TuSdkImageButton getCompleteButton()
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

	/** 滤镜栏 */
	public TuEditFilterBarView getFilterbar()
	{
		if (mFilterbar == null)
		{
			mFilterbar = this.getViewById("lsq_filter_bar");
			if (mFilterbar != null)
			{
				this.configGroupFilterView(mFilterbar);
				// 绑定选择委托
				mFilterbar.setDelegate(this);
			}
		}
		return mFilterbar;
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
		// 开启滤镜配置选项
		view.setEnableFilterConfig(this.isEnableFilterConfig());
		// 开启用户滤镜历史记录
		view.setEnableHistory(this.isEnableFiltersHistory());
		// 显示滤镜标题视图
		view.setDisplaySubtitles(this.isDisplayFiltersSubtitles());
		// 设置控制器
		view.setActivity(this.getActivity());
		// 开启无效果滤镜 (默认: true)
		view.setEnableNormalFilter(this.isEnableNormalFilter());
		// 开启在线滤镜
		view.setEnableOnlineFilter(this.isEnableOnlineFilter());
		// 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
		view.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		view.setRenderFilterThumb(this.isRenderFilterThumb());
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
		if (this.equalViewIds(v, this.getCancelButton()))
		{
			this.handleBackButton();
		}
		else if (this.equalViewIds(v, this.getCompleteButton()))
		{
			this.handleCompleteButton();
		}
	}

	/********************************** loadView ***********************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);
		// 取消按钮
		getCancelButton();
		// 完成按钮
		getCompleteButton();
		// 滤镜栏
		getFilterbar();

		this.getImageView().setImageBackgroundColor(TuSdkContext.getColor("lsq_background_editor"));
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

		if (this.getFilterbar() == null) return;

		this.getFilterbar().setThumbImage(this.getImage());
		this.getFilterbar().setDefaultShowState(true);

		if (this.getFilterWrap() != null)
		{
			this.getFilterbar().loadFilters(this.getFilterWrap().getOption());
		}
		else
		{
			this.getFilterbar().loadFilters(null);
		}
	}

	/** 后退按钮 */
	protected void handleBackButton()
	{
		this.navigatorBarBackAction(null);
	}

	/*************************** FilterConfigView *************************/
	/** 通知滤镜配置视图 */
	@Override
	public void notifyFilterConfigView()
	{
		if (this.getFilterbar() == null) return;
		this.getFilterbar().setFilter(this.getFilterWrap());
	}

	/** 通知重新绘制 */
	@Override
	public void onFilterConfigRequestRender(TuEditFilterBarView configView)
	{
		if (this.getImageView() != null) this.getImageView().requestRender();
	}

	/**
	 * 选中一个滤镜
	 * 
	 * @param view
	 *            图片编辑滤镜控制器滤镜栏视图
	 * @param itemData
	 *            滤镜分组元素
	 * @return
	 */
	@Override
	public boolean onFilterSelected(TuEditFilterBarView view, GroupFilterItem itemData)
	{
		if (itemData.type == GroupFilterItemType.TypeFilter)
		{
			return handleSwitchFilter(itemData.getFilterCode());
		}
		return true;
	}
}