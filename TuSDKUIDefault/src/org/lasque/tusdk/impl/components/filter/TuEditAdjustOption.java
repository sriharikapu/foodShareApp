/** 
 * TuSDKCore
 * TuEditAdjustOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-29 下午8:25:01 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 颜色调整控制器配置选项
 * 
 * @author Clear
 */
public class TuEditAdjustOption extends TuImageResultOption
{
	/**
	 * 颜色调整控制器
	 * 
	 * @return 颜色调整控制器(默认: TuEditAdjustFragment，如需自定义请继承自
	 *         TuEditAdjustFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditAdjustFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditAdjustFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_adjust_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditAdjustFragment.getLayoutId();
	}

	/** 创建颜色调整控制器对象 */
	public TuEditAdjustFragment fragment()
	{
		TuEditAdjustFragment fragment = this.fragmentInstance();
		return fragment;
	}
}