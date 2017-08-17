/** 
 * TuSDKCore
 * TuEditFilterOption.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午4:38:43 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 图片编辑滤镜控制器配置选项
 * 
 * @author Clear
 */
public class TuEditFilterOption extends TuImageResultOption
{
	/** 图片编辑滤镜控制器配置选项 */
	public TuEditFilterOption()
	{

	}

	/**
	 * 图片编辑滤镜控制器类
	 * 
	 * @return 图片编辑滤镜控制器类 (默认: TuEditFilterFragment，如需自定义请继承自
	 *         TuEditFilterFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditFilterFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditFilterFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_filter_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditFilterFragment.getLayoutId();
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

	/** 行视图宽度 (单位:DP) */
	public void setGroupFilterCellWidthDP(int mGroupFilterCellWidthDP)
	{
		this.setGroupFilterCellWidth(TuSdkContext.dip2px(mGroupFilterCellWidthDP));
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
	 *      {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
	 * @param 滤镜分组列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
	 *            GroupFilterItemView)
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

	/** 滤镜组选择栏高度 (单位:DP) */
	public void setFilterBarHeightDP(int mFilterBarHeightDP)
	{
		this.setFilterBarHeight(TuSdkContext.dip2px(mFilterBarHeightDP));
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

	/** 创建图片编辑滤镜控制器对象 */
	public TuEditFilterFragment fragment()
	{
		TuEditFilterFragment fragment = this.fragmentInstance();

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		fragment.setFilterGroup(this.getFilterGroup());
		// 开启滤镜配置选项
		fragment.setEnableFilterConfig(this.isEnableFilterConfig());
		// 是否仅返回滤镜，不返回处理图片(默认：false)
		fragment.setOnlyReturnFilter(this.isOnlyReturnFilter());
		// 行视图宽度
		fragment.setGroupFilterCellWidth(this.getGroupFilterCellWidth());
		// 滤镜组选择栏高度
		fragment.setFilterBarHeight(this.getFilterBarHeight());
		// 滤镜分组列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
		// GroupFilterGroupView)
		fragment.setGroupTableCellLayoutId(this.getGroupTableCellLayoutId());
		// 滤镜列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
		// GroupFilterItemView)
		fragment.setFilterTableCellLayoutId(this.getFilterTableCellLayoutId());
		// 开启用户滤镜历史记录
		fragment.setEnableFiltersHistory(this.isEnableFiltersHistory());
		// 显示滤镜标题视图
		fragment.setDisplayFiltersSubtitles(this.isDisplayFiltersSubtitles());
		// 开启无效果滤镜 (默认: true)
		fragment.setEnableNormalFilter(this.isEnableNormalFilter());
		// 开启在线滤镜
		fragment.setEnableOnlineFilter(this.isEnableOnlineFilter());
		// 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
		fragment.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		fragment.setRenderFilterThumb(this.isRenderFilterThumb());
		return fragment;
	}
}