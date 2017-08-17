/** 
 * TuSDKCore
 * TuEditSharpnessOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-29 下午7:49:30 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 锐化控制器配置选项
 * 
 * @author Clear
 */
public class TuEditSharpnessOption extends TuImageResultOption
{
	/**
	 * 锐化控制器
	 * 
	 * @return 锐化控制器(默认: TuEditSharpnessFragment，如需自定义请继承自
	 *         TuEditSharpnessFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditSharpnessFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditSharpnessFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditSharpnessFragment.getLayoutId();
	}

	/** 创建锐化控制器对象 */
	public TuEditSharpnessFragment fragment()
	{
		TuEditSharpnessFragment fragment = this.fragmentInstance();
		return fragment;
	}
}
