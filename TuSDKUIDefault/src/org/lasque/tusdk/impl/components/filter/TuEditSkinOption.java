/** 
 * TuSDKCore
 * TuEditSkinOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-22 下午7:06:17 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 美肤编辑功能控制器配置选项
 * 
 * @author Clear
 */
public class TuEditSkinOption extends TuImageResultOption
{
	/**
	 * 美肤编辑功能控制器
	 * 
	 * @return 美肤编辑功能控制器(默认: TuEditSkinFragment，如需自定义请继承自
	 *         TuEditSkinFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuEditSkinFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see # {@link org.lasque.tusdk.impl.components.filter.TuEditSkinFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_skin_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuEditSkinFragment.getLayoutId();
	}

	/** 创建多功能图像编辑控制器对象 */
	public TuEditSkinFragment fragment()
	{
		TuEditSkinFragment fragment = this.fragmentInstance();
		return fragment;
	}
}
