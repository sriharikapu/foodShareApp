/** 
 * TuSDKCore
 * TuEditStickerOption.java
 *
 * @author 		Clear
 * @Date 		2014-12-30 下午5:02:00 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.sticker;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView.StickerViewDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;

/**
 * 图片编辑贴纸控制器配置选项
 * 
 * @author Clear
 */
public class TuEditStickerOption extends TuImageResultOption
{
	/** 图片编辑贴纸控制器配置选项 */
	public TuEditStickerOption()
	{

	}

	/**
	 * 图片编辑贴纸控制器类
	 * 
	 * @return 图片编辑贴纸控制器类 (默认: TuEditStickerFragment，如需自定义请继承自
	 *         TuEditStickerFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditStickerFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment}
	 * @return 根视图布局资源ID (默认:
	 *         tusdk_impl_component_sticker_edit_sticker_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditStickerFragment.getLayoutId();
	}

	/********************************** Config ***********************************/

	/** 贴纸分类列表 */
	private List<StickerCategory> mCategories;

	/** 单元格宽度 */
	private int mGridWidth;
	/** 单元格高度 */
	private int mGridHeight;
	/** 单元格间距 */
	private int mGridPadding = -1;
	/** 单元格布局资源ID */
	private int mGridLayoutId;
	/** 贴纸视图委托 */
	private StickerViewDelegate mStickerViewDelegate;

	/** 单元格宽度 */
	public int getGridWidth()
	{
		return mGridWidth;
	}

	/** 单元格宽度 */
	public void setGridWidth(int mGridWidth)
	{
		this.mGridWidth = mGridWidth;
	}

	/** 单元格高度 */
	public int getGridHeight()
	{
		return mGridHeight;
	}

	/** 单元格高度 */
	public void setGridHeight(int mGridHeight)
	{
		this.mGridHeight = mGridHeight;
	}

	/** 单元格间距 */
	public int getGridPadding()
	{
		return mGridPadding;
	}

	/** 单元格间距 */
	public void setGridPadding(int mGridPadding)
	{
		this.mGridPadding = mGridPadding;
	}

	/** 单元格间距 (单位：DP) */
	public void setGridPaddingDP(int mGridPaddingDP)
	{
		if (mGridPaddingDP < 0) return;
		this.setGridPadding(TuSdkContext.dip2px(mGridPaddingDP));
	}

	/**
	 * 单元格布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListGrid}
	 * @return 行视图布局ID (默认: tusdk_impl_component_widget_sticker_list_grid)
	 */
	public int getGridLayoutId()
	{
		return mGridLayoutId;
	}

	/**
	 * 单元格布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListGrid}
	 * @param resId
	 *            单元格布局资源ID (默认: tusdk_impl_component_widget_sticker_list_grid)
	 */
	public void setGridLayoutId(int mGridLayoutId)
	{
		this.mGridLayoutId = mGridLayoutId;
	}

	/** 贴纸分类列表(默认: StickerLocalPackage.shared().getCategories()) */
	public List<StickerCategory> getCategories()
	{
		return mCategories;
	}

	/** 贴纸分类列表(默认: StickerLocalPackage.shared().getCategories()) */
	public void setCategories(List<StickerCategory> mCategories)
	{
		this.mCategories = mCategories;
	}

	/** 贴纸视图委托 */
	public StickerViewDelegate getStickerViewDelegate()
	{
		return mStickerViewDelegate;
	}

	/** 贴纸视图委托 */
	public void setStickerViewDelegate(StickerViewDelegate mStickerViewDelegate)
	{
		this.mStickerViewDelegate = mStickerViewDelegate;
	}

	/** 创建图片编辑贴纸选择控制器对象 */
	public TuEditStickerFragment fragment()
	{
		TuEditStickerFragment fragment = this.fragmentInstance();
		// 单元格布局资源ID
		fragment.setGridLayoutId(this.getGridLayoutId());
		// 单元格宽度
		fragment.setGridWidth(this.getGridWidth());
		// 单元格高度
		fragment.setGridHeight(this.getGridHeight());
		// 单元格间距
		fragment.setGridPadding(this.getGridPadding());
		// 贴纸分类列表(默认: StickerLocalPackage.shared)
		fragment.setCategories(this.getCategories());
		// 贴纸视图委托
		fragment.setStickerViewDelegate(this.getStickerViewDelegate());
		return fragment;
	}
}