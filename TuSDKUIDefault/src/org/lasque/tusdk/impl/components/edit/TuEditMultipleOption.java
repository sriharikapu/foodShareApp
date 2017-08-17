/** 
 * TuSDKCore
 * TuEditMultipleOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 上午11:59:11 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import java.util.List;

import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;

/**
 * 多功能图像编辑控制器配置选项
 * 
 * @author Clear
 */
public class TuEditMultipleOption extends TuImageResultOption
{

	/**
	 * 多功能图像编辑控制器类
	 * 
	 * @return 多功能图像编辑控制器类 (默认: TuEditMultipleFragment，如需自定义请继承自
	 *         TuEditMultipleFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditMultipleFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.edit.TuEditMultipleFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_multiple_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditMultipleFragment.getLayoutId();
	}

	/********************************** Config ***********************************/

	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	private int mLimitSideSize;
	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	private boolean mLimitForScreen;
	/** 是否禁用操作步骤记录 */
	private boolean mDisableStepsSave;
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	private TuSdkWaterMarkOption mWaterMarkOption;
	/** 功能模块列表 TuEditActionType (默认全部加载, TuEditActionType.multipleActionTypes()) */
	private List<TuEditActionType> mModules = TuEditActionType.multipleActionTypes();

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

	/** 是否禁用操作步骤记录 */
	public boolean isDisableStepsSave()
	{
		return mDisableStepsSave;
	}

	/** 是否禁用操作步骤记录 */
	public void setDisableStepsSave(boolean mDisableStepsSave)
	{
		this.mDisableStepsSave = mDisableStepsSave;
	}
	
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	public void setWaterMarkOption(TuSdkWaterMarkOption mWaterMarkOption)
	{
		this.mWaterMarkOption = mWaterMarkOption;
	}
	
	/** 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) */
	public TuSdkWaterMarkOption getWaterMarkOption()
	{
		return this.mWaterMarkOption;
	}

	/** 功能模块列表 TuEditActionType (默认全部加载, TuEditActionType.multipleActionTypes()) */
	public List<TuEditActionType> getModules()
	{
		return mModules;
	}

	/**
	 * 禁用功能模块
	 * 
	 * @param actionType
	 *            图片编辑动作类型
	 */
	public void disableModule(TuEditActionType actionType)
	{
		if (actionType == null) return;
		this.mModules.remove(actionType);
	}

	/** 创建多功能图像编辑控制器对象 */
	public TuEditMultipleFragment fragment()
	{
		TuEditMultipleFragment fragment = this.fragmentInstance();

		// 最大输出图片边长 (默认:0, 不限制图片宽高)
		fragment.setLimitSideSize(this.getLimitSideSize());
		// 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)
		fragment.setLimitForScreen(this.isLimitForScreen());
		// 是否禁用操作步骤记录
		fragment.setDisableStepsSave(this.isDisableStepsSave());
		// 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印) 
		fragment.setWaterMarkOption(this.getWaterMarkOption());
		// 功能模块列表 TuEditActionType (默认全部加载,
		// TuEditActionType.multipleActionTypes())
		fragment.setModules(this.getModules());

		return fragment;
	}
}