/** 
 * TuSDKCore
 * TuEditComponent.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午3:25:39 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.TuAnimType;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.edit.TuEditCuterFragment;
import org.lasque.tusdk.impl.components.edit.TuEditCuterFragment.TuEditCuterFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditEntryFragment;
import org.lasque.tusdk.impl.components.edit.TuEditEntryFragment.TuEditEntryFragmentDelegate;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment.TuEditFilterFragmentDelegate;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment.TuStickerChooseFragmentDelegate;
import org.lasque.tusdk.modules.components.TuEditComponentBase;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.app.Activity;

/**
 * 图片编辑组件
 * 
 * @author Clear
 */
public class TuEditComponent extends TuEditComponentBase implements TuEditEntryFragmentDelegate, TuEditFilterFragmentDelegate, TuEditCuterFragmentDelegate,
		TuStickerChooseFragmentDelegate
{
	/**
	 * 图片编辑组件配置
	 */
	private TuEditComponentOption mComponentOption;

	/**
	 * 图片编辑组件配置
	 * 
	 * @return 图片编辑组件配置
	 */
	public TuEditComponentOption componentOption()
	{
		if (mComponentOption == null)
		{
			mComponentOption = new TuEditComponentOption();
		}
		return mComponentOption;
	}

	/**
	 * 图片编辑组件配置
	 * 
	 * @param mComponentOption
	 */
	public void setComponentOption(TuEditComponentOption mComponentOption)
	{
		this.mComponentOption = mComponentOption;
	}

	/*************************** init *******************************/
	/**
	 * 启动图片编辑组件
	 * 
	 * @param fragment
	 *            控制器
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuEditComponent component(TuFragment fragment, TuSdkComponentDelegate delegate)
	{
		TuEditComponent component = new TuEditComponent(fragment);
		component.setDelegate(delegate);
		return component;
	}

	/**
	 * 启动图片编辑组件
	 * 
	 * @param activity
	 *            内容上下文
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuEditComponent component(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuEditComponent component = new TuEditComponent(activity);
		component.setDelegate(delegate);
		return component;
	}

	/**
	 * 来源控制器
	 */
	private TuFragment mOrginFragment;

	/**
	 * 编辑入口控制器
	 */
	private TuEditEntryFragment mEditEntryFragment;

	/**
	 * 是否为全屏
	 */
	private boolean mIsFullscreen;

	/**
	 * 图片编辑组件
	 * 
	 * @param fragment
	 */
	public TuEditComponent(TuFragment fragment)
	{
		this(fragment.getActivity());
		mOrginFragment = fragment;
		mIsFullscreen = mOrginFragment.isFullScreen();
	}

	/** 图片编辑组件 */
	public TuEditComponent(Activity activity)
	{
		super(activity);
	}

	@Override
	protected void initComponent()
	{

	}

	@Override
	public TuEditComponent showComponent()
	{
		// 无法存储文件
		if (this.showAlertIfCannotSaveFile()) return this;

		TuEditEntryFragment fragment = this.componentOption().editEntryOption().fragment();
		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(this.getImage());
		// 输入的临时文件目录 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setTempFilePath(this.getTempFilePath());
		// 输入的相册图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImageSqlInfo(this.getImageSqlInfo());
		// 裁剪比例类型 (默认:RatioType.ratio_all)
		fragment.setRatioType(this.componentOption().editCuterOption().getRatioType());

		fragment.setDelegate(this);
		if (mOrginFragment != null)
		{
			// mOrginFragment.pushFragment(fragment);
			// 同一个Activity只允许有一个GLView
			mOrginFragment.presentModalNavigationActivity(fragment, TuAnimType.push, TuAnimType.pop, true);
		}
		else
		{
			this.presentModalNavigationActivity(fragment);
		}
		mEditEntryFragment = fragment;

		return this;
	}
	
	/**
	 * 通知组件操作完成
	 * 
	 * @param result
	 *            返回结果
	 * @param error
	 *            异常信息
	 * @param lastFragment
	 *            最后显示的控制器
	 */
	protected void notifyResult(TuSdkResult result, Error error, TuFragment lastFragment)
	{
		// 是否在组件执行完成后自动关闭组件 (默认:false)
		if (this.isAutoDismissWhenCompleted() && mOrginFragment != null)
		{
			mOrginFragment.dismissActivityWithAnim();
		}

		super.notifyResult(result, error, lastFragment);
	}

	/******************************* TuEditEntryFragmentDelegate ********************************/
	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑入口控制器
	 * @param result
	 *            旋图片编辑入口控制器处理结果
	 */
	@Override
	public void onTuEditEntryFragmentEdited(TuEditEntryFragment fragment, TuSdkResult result)
	{
		if (!fragment.isShowResultPreview())
		{
			fragment.hubDismissRightNow();
		}
		this.notifyResult(result, null, fragment);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑入口控制器
	 * @param result
	 *            旋图片编辑入口控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditEntryFragmentEditedAsync(TuEditEntryFragment fragment, TuSdkResult result)
	{
		return false;
	}

	/**
	 * 图片编辑动作
	 * 
	 * @param fragment
	 *            图片编辑入口控制器
	 * @param actionType
	 *            图片编辑动作类型
	 */
	@Override
	public void onTuEditEntryFragmentAction(TuEditEntryFragment fragment, TuEditActionType actionType)
	{
		if (actionType == null) return;

		switch (actionType)
			{
			case TypeCuter:
				this.handleCutButton(fragment);
				break;
			case TypeFilter:
				this.handleFilterButton(fragment);
				break;
			case TypeSticker:
				this.handleStickerButton(fragment);
				break;
			default:
				break;
			}
	}

	/**
	 * 裁切按钮
	 */
	protected void handleCutButton(TuEditEntryFragment editFragment)
	{
		TuEditCuterFragment fragment = this.componentOption().editCuterOption().fragment();
		fragment.setImage(editFragment.getFilterImage());
		fragment.setCuterResult(editFragment.getCuterResult());

		fragment.setDelegate(this);
		this.presentModalNavigationActivity(fragment, TuAnimType.fade, TuAnimType.fade, mIsFullscreen);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑裁切旋转控制器
	 * @param result
	 *            图片编辑裁切旋转控制器处理结果
	 */
	@Override
	public void onTuEditCuterFragmentEdited(TuEditCuterFragment fragment, TuSdkResult result)
	{
		if (mEditEntryFragment == null || fragment == null || result == null) return;
		fragment.hubDismissRightNow();
		fragment.navigatorBarBackAction(null);
		mEditEntryFragment.setCuterResult(result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑裁切旋转控制器
	 * @param result
	 *            图片编辑裁切旋转控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditCuterFragmentEditedAsync(TuEditCuterFragment fragment, TuSdkResult result)
	{
		return false;
	}

	/*************************** handleFilterButton *******************************/
	/**
	 * 滤镜按钮
	 */
	protected void handleFilterButton(TuEditEntryFragment editFragment)
	{
		TuEditFilterFragment fragment = this.componentOption().editFilterOption().fragment();

		fragment.setImage(editFragment.getCuterImage());
		fragment.setFilterWrap(editFragment.getFilterWrap());
		fragment.setDelegate(this);

		this.presentModalNavigationActivity(fragment, TuAnimType.fade, TuAnimType.fade, mIsFullscreen);
	}

	/*************************** handleFilterButton TuEditFilterFragmentDelegate *******************************/

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑滤镜控制器
	 * @param result
	 *            图片编辑滤镜控制器处理结果
	 */
	@Override
	public void onTuEditFilterFragmentEdited(TuEditFilterFragment fragment, TuSdkResult result)
	{
		if (mEditEntryFragment == null || fragment == null || result == null) return;
		fragment.hubDismissRightNow();
		fragment.navigatorBarBackAction(null);
		mEditEntryFragment.setFilterWrap(fragment.getFilterWrap());
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑滤镜控制器
	 * @param result
	 *            图片编辑滤镜控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditFilterFragmentEditedAsync(TuEditFilterFragment fragment, TuSdkResult result)
	{
		return false;
	}

	/*************************** handleStickerButton *******************************/
	/**
	 * 贴纸按钮
	 */
	protected void handleStickerButton(TuEditEntryFragment editFragment)
	{
		TuStickerChooseFragment fragment = this.componentOption().editStickerOption().fragment();
		fragment.setDelegate(this);
		this.presentModalNavigationActivity(fragment, true);
		// this.presentModalNavigationActivity(fragment, TuAnimType.fade,
		// TuAnimType.fade, mIsFullscreen);
	}

	/**
	 * 选中贴纸
	 * 
	 * @param fragment
	 *            控制器
	 * @param data
	 *            贴纸元素
	 */
	@Override
	public void onTuStickerChooseFragmentSelected(TuStickerChooseFragment fragment, StickerData data)
	{
		if (mEditEntryFragment == null || fragment == null || data == null) return;
		fragment.dismissActivityWithAnim();
		mEditEntryFragment.appendStickerItem(data);
	}
}
