/** 
 * TuSDKGee
 * TuEditFilterOption.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-15 下午5:08:23 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.smudge;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;

/**
 * 图片编辑涂抹控制器配置选项
 * 
 * @author Yanlin
 */
public class TuEditSmudgeOption extends TuImageResultOption
{
	/** 图片编辑涂抹控制器配置选项 */
	public TuEditSmudgeOption()
	{

	}

	/**
	 * 图片编辑涂抹控制器类
	 * 
	 * @return 图片编辑滤镜控制器类 (默认: TuEditSmudgeFragment，如需自定义请继承自
	 *         TuEditFilterFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditSmudgeFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.smudge.TuEditSmudgeFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_smudge_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditSmudgeFragment.getLayoutId();
	}

	/********************************** Config ***********************************/
	/** 需要显示的笔刷组 */
	private List<String> mBrushGroup;
	/** 行视图宽度 */
	private int mBrushBarCellWidth;
	/** 笔刷列表行视图布局资源ID */
	private int mBrushBarCellLayoutId;
	/** 笔刷栏高度 */
	private int mBrushBarHeight;
	/** 记住用户最后一次使用的笔刷 (默认: true) */
	private boolean mSaveLastBrush = true;
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	private BrushSize.SizeType mDefaultBrushSize;
	/** 允许撤销的次数 (默认: 5) */
	private int mMaxUndoCount = 5;
	
	/** 需要显示的笔刷组 (如果为空将显示所有自定义笔刷) */
	public List<String> getBrushGroup()
	{
		return mBrushGroup;
	}

	/** 需要显示的笔刷组 (如果为空将显示所有自定义笔刷) */
	public void setBrushGroup(List<String> mBrushGroup)
	{
		this.mBrushGroup = mBrushGroup;
	}

	/** 行视图宽度 */
	public int getBrushBarCellWidth()
	{
		return mBrushBarCellWidth;
	}

	/** 行视图宽度 */
	public void setBrushBarCellWidth(int mBrushBarCellWidth)
	{
		this.mBrushBarCellWidth = mBrushBarCellWidth;
	}

	/** 行视图宽度 (单位:DP) */
	public void setBrushBarCellWidthDP(int mBrushBarCellWidthDP)
	{
		this.setBrushBarCellWidth(TuSdkContext.dip2px(mBrushBarCellWidthDP));
	}

	/**
	 * 笔刷列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.sumdge.BrushBarItemCellBase}
	 * @param 笔刷列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_brush_bar_item_cell，如需自定义请继承自
	 *            BrushBarItemCellBase)
	 */
	public int getBrushBarCellLayoutId()
	{
		return mBrushBarCellLayoutId;
	}

	/**
	 * 笔刷列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.sumdge.BrushBarItemCellBase}
	 * @param 笔刷列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_brush_bar_item_cell，如需自定义请继承自
	 *            BrushBarItemCellBase)
	 */
	public void setBrushBarCellLayoutId(int mBrushBarCellLayoutId)
	{
		this.mBrushBarCellLayoutId = mBrushBarCellLayoutId;
	}

	/** 笔刷栏高度 */
	public int getBrushBarHeight()
	{
		return mBrushBarHeight;
	}

	/** 笔刷栏高度*/
	public void setBrushBarHeight(int mBrushBarHeight)
	{
		this.mBrushBarHeight = mBrushBarHeight;
	}

	/** 笔刷栏高度 (单位:DP) */
	public void setBrushBarHeightDP(int mBrushBarHeightDP)
	{
		this.setBrushBarHeight(TuSdkContext.dip2px(mBrushBarHeightDP));
	}

	/** 记住用户最后一次使用的笔刷 */
	public boolean isSaveLastBrush()
	{
		return mSaveLastBrush;
	}

	/** 记住用户最后一次使用的笔刷 */
	public void setSaveLastBrush(boolean mSaveLastBrush)
	{
		this.mSaveLastBrush = mSaveLastBrush;
	}
	
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	public BrushSize.SizeType getDefaultBrushSize()
	{
		return mDefaultBrushSize;
	}

	/** 默认的笔刷大小 (默认: lsqBrushMedium，中等粗细) */
	public void setDefaultBrushSize(BrushSize.SizeType mDefaultBrushSize)
	{
		this.mDefaultBrushSize = mDefaultBrushSize;
	}
	
	/** 允许撤销的次数 (默认: 5) */
	public int getMaxUndoCount()
	{
		return mMaxUndoCount;
	}
	
	/** 允许撤销的次数 (默认: 5) */
	public void setMaxUndoCount(int mMaxUndoCount)
	{
		this.mMaxUndoCount = mMaxUndoCount;
	}

	/** 创建图片编辑滤镜控制器对象 */
	public TuEditSmudgeFragment fragment()
	{
		TuEditSmudgeFragment fragment = this.fragmentInstance();

		// 需要显示的笔刷组 (如果为空将显示所有自定义笔刷)
		fragment.setBrushGroup(this.getBrushGroup());
		// 行视图宽度
		fragment.setBrushBarCellWidth(this.getBrushBarCellWidth());
		// 笔刷栏高度
		fragment.setBrushBarHeight(this.getBrushBarHeight());
		// 笔刷列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_brush_bar_item_cell，如需自定义请继承自
		// BrushBarItemCellBase)
		fragment.setBrushBarCellLayoutId(this.getBrushBarCellLayoutId());
		// 记住用户最后一次使用的笔刷
		fragment.setSaveLastBrush(this.isSaveLastBrush());
		// 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细)
		fragment.setDefaultBrushSize(this.getDefaultBrushSize());
		/** 允许撤销的次数 (默认: 5) */
		fragment.setMaxUndoCount(this.getMaxUndoCount());
		return fragment;
	}
}