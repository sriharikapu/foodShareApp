/** 
 * TuSDK
 * TuSdkGeeV1.java
 *
 * @author 		Clear
 * @Date 		2015-8-30 下午1:55:54 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk;

import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumComponent;
import org.lasque.tusdk.impl.components.TuAlbumMultipleComponent;
import org.lasque.tusdk.impl.components.TuAvatarComponent;
import org.lasque.tusdk.impl.components.TuEditComponent;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;

import android.app.Activity;

/**
 * Tusdk 组件 V1版本
 * 
 * @author Clear
 */
public class TuSdkGeeV1
{
	/** Gee版本号 */
	public static final String GEE_VERSION = "1.9.0";

	/**
	 * 获取系统相册组件
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 相册组件
	 */
	public static TuAlbumComponent albumCommponent(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuAlbumComponent cp = TuAlbumComponent.component(activity, delegate);
		return cp;
	}
	
	/**
	 * 获取系统多功能相册组件，只选择一张照片
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 多功能相册组件
	 */
	public static TuAlbumMultipleComponent albumMultipleCommponent(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuAlbumMultipleComponent cp = TuAlbumMultipleComponent.component(activity, delegate);
		cp.componentOption().albumListOption().setMaxSelection(1);
		return cp;
	}
	
	/**
	 * 获取系统多功能相册组件，支持多选
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @param maxSelection
	 *            一次选择的最大照片数量 (默认: 3)
	 * @return 多功能相册组件
	 */
	public static TuAlbumMultipleComponent albumMultipleCommponent(Activity activity, TuSdkComponentDelegate delegate, int maxSelection)
	{
		TuAlbumMultipleComponent cp = TuAlbumMultipleComponent.component(activity, delegate);
		cp.componentOption().albumListOption().setMaxSelection(maxSelection);
		return cp;
	}

	/**
	 * 获取头像设置组件
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 头像设置组件
	 */
	public static TuAvatarComponent avatarCommponent(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuAvatarComponent cp = TuAvatarComponent.component(activity, delegate);
		return cp;
	}

	/**
	 * 获取图片编辑组件
	 * 
	 * @param fragment
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 图片编辑组件
	 */
	public static TuEditComponent editCommponent(TuFragment fragment, TuSdkComponentDelegate delegate)
	{
		TuEditComponent cp = TuEditComponent.component(fragment, delegate);
		return cp;
	}

	/**
	 * 获取图片编辑组件
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 图片编辑组件
	 */
	public static TuEditComponent editCommponent(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuEditComponent cp = TuEditComponent.component(activity, delegate);
		return cp;
	}

	/**
	 * 获取多功能图像编辑组件
	 * 
	 * @param fragment
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 图片编辑组件
	 */
	public static TuEditMultipleComponent editMultipleCommponent(TuFragment fragment, TuSdkComponentDelegate delegate)
	{
		TuEditMultipleComponent cp = TuEditMultipleComponent.component(fragment, delegate);
		return cp;
	}

	/**
	 * 获取多功能图像编辑组件
	 * 
	 * @param activity
	 *            来源控制器
	 * @param delegate
	 *            组件委托
	 * @return 图片编辑组件
	 */
	public static TuEditMultipleComponent editMultipleCommponent(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuEditMultipleComponent cp = TuEditMultipleComponent.component(activity, delegate);
		return cp;
	}
}