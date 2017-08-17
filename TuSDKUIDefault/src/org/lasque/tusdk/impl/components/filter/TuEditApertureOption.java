/** 
 * TuSDKCore
 * TuEditApertureOption.java
 *
 * @author 		Clear
 * @Date 		2015-5-8 下午3:20:50 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 大光圈控制器配置选项
 * 
 * @author Clear
 */
public class TuEditApertureOption extends TuImageResultOption
{
	/**
	 * 锐化控制器
	 * 
	 * @return 锐化控制器(默认: TuEditApertureFragment，如需自定义请继承自
	 *         TuEditApertureFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditApertureFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.filter.TuEditApertureFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditApertureFragment.getLayoutId();
	}

	/** 创建大光圈控制器对象 */
	public TuEditApertureFragment fragment()
	{
		TuEditApertureFragment fragment = this.fragmentInstance();
		return fragment;
	}
}