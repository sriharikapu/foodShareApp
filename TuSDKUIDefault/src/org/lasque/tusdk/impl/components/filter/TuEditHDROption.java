/**
 * TuSDK.Gee.V1
 * TuEditHDROption.java
 *
 * @author  Yanlin
 * @Date  Apr 12, 2017 3:54:42 PM
 * @Copright (c) 2016 tusdk.com. All rights reserved.
 *
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 
 * HDR 控制器配置选项
 * 
 * @author Yanlin
 *
 */
public class TuEditHDROption extends TuImageResultOption
{
	/**
	 * HDR 控制器
	 * 
	 * @return HDR 控制器(默认: TuEditHDRFragment，如需自定义请继承自
	 *         TuEditHDRFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditHDRFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditHDRFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditHDRFragment.getLayoutId();
	}

	/** 创建HDR 控制器对象 */
	public TuEditHDRFragment fragment()
	{
		TuEditHDRFragment fragment = this.fragmentInstance();
		return fragment;
	}
}
