/** 
 * TuSDKCore
 * TuEditVignetteOption.java
 *
 * @author 		Clear
 * @Date 		2015-5-8 下午2:53:27 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 暗角功能控制器配置选项
 * 
 * @author Clear
 */
public class TuEditVignetteOption extends TuImageResultOption
{
	/**
	 * 锐化控制器
	 * 
	 * @return 锐化控制器(默认: TuEditVignetteFragment，如需自定义请继承自
	 *         TuEditVignetteFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditVignetteFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditVignetteFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditVignetteFragment.getLayoutId();
	}

	/** 创建暗角功能控制器对象 */
	public TuEditVignetteFragment fragment()
	{
		TuEditVignetteFragment fragment = this.fragmentInstance();
		return fragment;
	}
}
