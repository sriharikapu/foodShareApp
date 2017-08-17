/** 
 * TuSDKCore
 * TuStickerChooseOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-27 下午12:31:48 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.sticker;

import java.util.List;

import org.lasque.tusdk.modules.components.TuSdkComponentOption;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;

/**
 * 贴纸选择控制器配置选项
 * 
 * @author Clear
 */
public class TuStickerChooseOption extends TuSdkComponentOption
{
	/** 贴纸选择控制器配置选项 */
	public TuStickerChooseOption()
	{

	}

	/**
	 * 图片编辑贴纸选择控制器类
	 * 
	 * @return 图片编辑贴纸选择控制器类 (默认: TuStickerChooseFragment，如需自定义请继承自
	 *         TuStickerChooseFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuStickerChooseFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment}
	 * @return 根视图布局资源ID (默认:
	 *         tusdk_impl_component_sticker_choose_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuStickerChooseFragment.getLayoutId();
	}

	/********************************** Config ***********************************/

	/** 贴纸分类列表 */
	private List<StickerCategory> mCategories;
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

	/** 创建贴纸选择控制器对象 */
	public TuStickerChooseFragment fragment()
	{
		TuStickerChooseFragment fragment = this.fragmentInstance();
		// 行视图布局ID
		fragment.setCellLayoutId(this.getCellLayoutId());
		// 分组头部视图布局ID
		fragment.setHeaderLayoutId(this.getHeaderLayoutId());
		// 统计格式化字符
		fragment.setTotalFooterFormater(this.getTotalFooterFormater());
		// 空视图布局ID
		fragment.setEmptyViewLayouId(this.getEmptyViewLayouId());
		// 贴纸分类列表(默认: StickerLocalPackage.shared)
		fragment.setCategories(this.getCategories());
		return fragment;
	}
}