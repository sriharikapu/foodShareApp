/** 
 * TuSDK.Gee.V1
 * TuEditHolyLightOption.java
 *
 * @author 		Clear
 * @Date 		2015-12-15 下午6:53:18 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */ 
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 圣光编辑功能控制器选项
 * 
 * @author Clear
 */
public class TuEditHolyLightOption extends TuImageResultOption
{
	/**
	 * 圣光编辑功能控制器选项
	 * 
	 * @return 圣光编辑功能控制器(默认: TuEditHolyLightFragment，如需自定义请继承自
	 *         TuEditHolyLightFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditHolyLightFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see # {@link org.lasque.tusdk.impl.components.filter.TuEditHolyLightFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditHolyLightFragment.getLayoutId();
	}

	/** 创建多圣光编辑功能控制器选项*/
	public TuEditHolyLightFragment fragment()
	{
		TuEditHolyLightFragment fragment = this.fragmentInstance();
		return fragment;
	}
}
