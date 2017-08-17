/** 
 * TuSDKCore
 * TuEditEntryOption.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午12:56:13 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView.StickerViewDelegate;

/**
 * 图片编辑入口配置选项
 * 
 * @author Clear
 */
public class TuEditEntryOption extends TuImageResultOption
{
	/** 图片编辑入口配置选项 */
	public TuEditEntryOption()
	{

	}

	/**
	 * 图片编辑入口控制器类
	 * 
	 * @return 图片编辑入口控制器类 (默认: TuEditEntryFragment，如需自定义请继承自
	 *         TuEditEntryFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditEntryFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see # {@link org.lasque.tusdk.impl.components.edit.TuEditEntryFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_entry_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditEntryFragment.getLayoutId();
	}

	/********************************** Config ***********************************/

	/** 开启裁剪旋转功能 */
	private boolean mEnableCuter;
	/** 开启滤镜功能 */
	private boolean mEnableFilter;
	/** 开启贴纸功能 */
	private boolean mEnableSticker;
	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	private int mLimitSideSize;
	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	private boolean mLimitForScreen;
	/** 贴纸视图委托 */
	private StickerViewDelegate mStickerViewDelegate;

	/** 开启裁剪旋转功能 */
	public final boolean isEnableCuter()
	{
		return mEnableCuter;
	}

	/** 开启裁剪旋转功能 */
	public final void setEnableCuter(boolean mEnableCuter)
	{
		this.mEnableCuter = mEnableCuter;
	}

	/** 开启滤镜功能 */
	public final boolean isEnableFilter()
	{
		return mEnableFilter;
	}

	/** 开启滤镜功能 */
	public final void setEnableFilter(boolean mEnableFilter)
	{
		this.mEnableFilter = mEnableFilter;
	}

	/** 开启贴纸功能 */
	public final boolean isEnableSticker()
	{
		return mEnableSticker;
	}

	/** 开启贴纸功能 */
	public final void setEnableSticker(boolean mEnableSticker)
	{
		this.mEnableSticker = mEnableSticker;
	}

	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	public final int getLimitSideSize()
	{
		return mLimitSideSize;
	}

	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	public final void setLimitSideSize(int mLimitSideSize)
	{
		this.mLimitSideSize = mLimitSideSize;
	}

	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	public final boolean isLimitForScreen()
	{
		return mLimitForScreen;
	}

	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	public final void setLimitForScreen(boolean mLimitForScreen)
	{
		this.mLimitForScreen = mLimitForScreen;
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

	/** 创建图片编辑入口控制器对象 */
	public TuEditEntryFragment fragment()
	{
		TuEditEntryFragment fragment = this.fragmentInstance();
		// 开启裁剪旋转功能
		fragment.setEnableCuter(this.isEnableCuter());
		// 开启滤镜功能
		fragment.setEnableFilter(this.isEnableFilter());
		// 开启贴纸功能
		fragment.setEnableSticker(this.isEnableSticker());
		// 最大输出图片边长 (默认:0, 不限制图片宽高)
		fragment.setLimitSideSize(this.getLimitSideSize());
		// 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)
		fragment.setLimitForScreen(this.isLimitForScreen());
		// 贴纸视图委托
		fragment.setStickerViewDelegate(this.getStickerViewDelegate());
		return fragment;
	}
}