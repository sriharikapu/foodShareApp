/** 
 * TuSDKGee
 * TuEditWipeAndFilterOption.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-15 下午5:08:23 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;

/**
 * 图片编辑模糊控制器配置选项
 * 
 * @author Yanlin
 */
public class TuEditWipeAndFilterOption extends TuImageResultOption
{
	/** 图片编辑模糊控制器配置选项 */
	public TuEditWipeAndFilterOption()
	{

	}

	/**
	 * 图片编辑模糊控制器类
	 * 
	 * @return 图片编辑模糊控制器类 (默认: TuEditWipeAndFilterFragment，如需自定义请继承自
	 *         TuEditFilterFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditWipeAndFilterFragment.class;
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
		return TuEditWipeAndFilterFragment.getLayoutId();
	}

	/********************************** Config ***********************************/
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	private BrushSize.SizeType mDefaultBrushSize;
	/** 允许撤销的次数 (默认: 5) */
	private int mMaxUndoCount = 5;
	/** 笔刷效果强度 (默认: 0.2, 范围为0 ~ 1，值为1时强度最高) */
	private float mBrushStrength = 0.2f;
	/** 显示放大镜 (默认: true) */
	private boolean mDisplayMagnifier = true;
	
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
	
	/** 笔刷效果强度 (默认: 0.2, 范围为0 ~ 1，值为1时强度最高) */
	public float getBrushStrength()
	{
		return mBrushStrength;
	}
	
	/** 笔刷效果强度 (默认: 0.2, 范围为0 ~ 1，值为1时强度最高) */
	public void setBrushStrength(float mBrushStrength)
	{
		if (mBrushStrength >= 0 && mBrushStrength <= 1.0f)
		{
			this.mBrushStrength = mBrushStrength;
		}
	}
	
	/** 显示放大镜 (默认: true) */
	public boolean isDisplayMagnifier()
	{
		return mDisplayMagnifier;
	}
	
	/** 显示放大镜 (默认: true) */
	public void setDisplayMagnifier(boolean mDisplayMagnifier)
	{
		this.mDisplayMagnifier = mDisplayMagnifier;
	}

	/** 创建图片编辑滤镜控制器对象 */
	public TuEditWipeAndFilterFragment fragment()
	{
		TuEditWipeAndFilterFragment fragment = this.fragmentInstance();
		/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
		fragment.setDefaultBrushSize(this.getDefaultBrushSize());
		/** 允许撤销的次数 (默认: 5) */
		fragment.setMaxUndoCount(this.getMaxUndoCount());
		/** 笔刷效果强度 (默认: 0.2, 范围为0 ~ 1，值为1时强度最高) */
		fragment.setBrushStrength(this.getBrushStrength());
		/** 显示放大镜 (默认: true) */
		fragment.setDisplayMagnifier(this.isDisplayMagnifier());
		return fragment;
	}
}