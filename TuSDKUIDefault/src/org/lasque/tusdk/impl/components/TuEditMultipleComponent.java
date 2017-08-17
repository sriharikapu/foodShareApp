/** 
 * TuSDKCore
 * TuEditMultipleComponent.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午6:48:01 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.TuAnimType;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment.TuFilterResultFragmentDelegate;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.impl.components.edit.TuEditCuterFragment;
import org.lasque.tusdk.impl.components.edit.TuEditCuterFragment.TuEditCuterFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleFragment;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleFragment.TuEditMultipleFragmentDelegate;
import org.lasque.tusdk.impl.components.filter.TuEditAdjustFragment;
import org.lasque.tusdk.impl.components.filter.TuEditApertureFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment.TuEditFilterFragmentDelegate;
import org.lasque.tusdk.impl.components.filter.TuEditHDRFragment;
import org.lasque.tusdk.impl.components.filter.TuEditHolyLightFragment;
import org.lasque.tusdk.impl.components.filter.TuEditSharpnessFragment;
import org.lasque.tusdk.impl.components.filter.TuEditSkinFragment;
import org.lasque.tusdk.impl.components.filter.TuEditVignetteFragment;
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterFragment.TuEditWipeAndFilterFragmentDelegate;
import org.lasque.tusdk.impl.components.smudge.TuEditSmudgeFragment;
import org.lasque.tusdk.impl.components.smudge.TuEditSmudgeFragment.TuEditSmudgeFragmentDelegate;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment.TuEditStickerFragmentDelegate;
import org.lasque.tusdk.modules.components.TuEditMultipleComponentBase;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * 多功能图像编辑组件
 * 
 * @author Clear
 */
public class TuEditMultipleComponent extends TuEditMultipleComponentBase implements TuEditMultipleFragmentDelegate, TuEditCuterFragmentDelegate,
		TuEditFilterFragmentDelegate, TuFilterResultFragmentDelegate, TuEditStickerFragmentDelegate, TuEditSmudgeFragmentDelegate, TuEditWipeAndFilterFragmentDelegate
{
	/** 多功能图像编辑组件选项 */
	private TuEditMultipleComponentOption mComponentOption;

	/** 多功能图像编辑组件选项 */
	public TuEditMultipleComponentOption componentOption()
	{
		if (mComponentOption == null)
		{
			mComponentOption = new TuEditMultipleComponentOption();
		}
		return mComponentOption;
	}

	/** 多功能图像编辑组件选项 */
	public void setComponentOption(TuEditMultipleComponentOption mComponentOption)
	{
		this.mComponentOption = mComponentOption;
	}

	/*************************** init *******************************/
	/**
	 * 启动多功能图像编辑组件
	 * 
	 * @param fragment
	 *            控制器
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuEditMultipleComponent component(TuFragment fragment, TuSdkComponentDelegate delegate)
	{
		TuEditMultipleComponent component = new TuEditMultipleComponent(fragment);
		component.setDelegate(delegate);
		return component;
	}

	/**
	 * 启动多功能图像编辑组件
	 * 
	 * @param activity
	 *            内容上下文
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuEditMultipleComponent component(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuEditMultipleComponent component = new TuEditMultipleComponent(activity);
		component.setDelegate(delegate);
		return component;
	}

	/** 来源控制器 */
	private TuFragment mOrginFragment;

	/** 编辑入口控制器 */
	private TuEditMultipleFragment mEditMultipleFragment;

	/** 是否为全屏 */
	private boolean mIsFullscreen;

	/** 多功能图像编辑组件 */
	public TuEditMultipleComponent(TuFragment fragment)
	{
		this(fragment.getActivity());
		mOrginFragment = fragment;
		mIsFullscreen = mOrginFragment.isFullScreen();
	}

	/** 多功能图像编辑组件 */
	public TuEditMultipleComponent(Activity activity)
	{
		super(activity);
	}

	@Override
	protected void initComponent()
	{

	}

	@Override
	public TuEditMultipleComponent showComponent()
	{
		// 无法存储文件
		if (this.showAlertIfCannotSaveFile()) return this;
		TuEditMultipleFragment fragment = this.componentOption().editMultipleOption().fragment();
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
			this.presentModalNavigationActivity(fragment, true);
		}
		mEditMultipleFragment = fragment;
		return this;
	}

	/** 设置入口显示图片 */
	private void setEntryDisplayImage(Bitmap image)
	{
		if (mEditMultipleFragment == null) return;

		final Bitmap dispaly = BitmapHelper.imageResize(image, mEditMultipleFragment.getImageDisplaySize(), true);

		if (dispaly == null) return;

		mEditMultipleFragment.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mEditMultipleFragment.setDisplayImage(dispaly);
			}
		});
	}

	/** 执行控制器 */
	protected void handleAction(TuEditMultipleFragment editFragment, TuImageResultFragment fragment)
	{
		fragment.setImage(editFragment.getImage());
		fragment.setTempFilePath(editFragment.getLastSteps());

		this.presentModalNavigationActivity(fragment, TuAnimType.fade, TuAnimType.fade, mIsFullscreen);
	}

	/**
	 * 控制器编辑完成
	 * 
	 * @param fragment
	 *            控制器
	 * @param result
	 *            处理结果
	 */
	protected void onActionEdited(TuImageResultFragment fragment, TuSdkResult result)
	{
		if (mEditMultipleFragment == null || fragment == null || result == null) return;
		fragment.hubDismissRightNow();
		fragment.navigatorBarBackAction(null);
		mEditMultipleFragment.appendHistory(result.imageFile);
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

	/*************************** TuEditMultipleFragmentDelegate *******************************/

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            多功能图像编辑控制器
	 * @param result
	 *            Sdk执行结果
	 */
	@Override
	public void onTuEditMultipleFragmentEdited(TuEditMultipleFragment fragment, TuSdkResult result)
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
	 *            多功能图像编辑控制器
	 * @param result
	 *            Sdk执行结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditMultipleFragmentEditedAsync(TuEditMultipleFragment fragment, TuSdkResult result)
	{
		return false;
	}

	/**
	 * 图片编辑动作
	 * 
	 * @param fragment
	 *            多功能图像编辑控制器
	 * @param actionType
	 *            图片编辑动作类型
	 */
	@Override
	public void onTuEditMultipleFragmentAction(TuEditMultipleFragment fragment, TuEditActionType actionType)
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
			case TypeSkin:
				this.handleSkinButton(fragment);
				break;
			case TypeSmudge:
				this.handleSmudgeButton(fragment);
				break;
			case TypeAdjust:
				this.handleAdjustButton(fragment);
				break;
			case TypeWipeFilter:
				this.handleWipeFilterButton(fragment);
				break;
			case TypeSharpness:
				this.handleSharpnessButton(fragment);
				break;
			case TypeVignette:
				this.handleVignetteButton(fragment);
				break;
			case TypeAperture:
				this.handleApertureButton(fragment);
				break;
			case TypeHolyLight:
				this.handleTypeHolyLightButton(fragment);
				break;
			case TypeHDR:
				this.handleTypeHDRButton(fragment);
			default:
				break;
			}
	}

	/*************************** handleCutButton *******************************/

	/** 裁切按钮 */
	protected void handleCutButton(TuEditMultipleFragment editFragment)
	{
		TuEditCuterFragment fragment = this.componentOption().editCuterOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
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
		this.onActionEdited(fragment, result);
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
		this.setEntryDisplayImage(result.image);
		return false;
	}

	/*************************** handleFilterButton *******************************/
	/** 滤镜按钮 */
	protected void handleFilterButton(TuEditMultipleFragment editFragment)
	{
		TuEditFilterFragment fragment = this.componentOption().editFilterOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

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
		this.onActionEdited(fragment, result);
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
		this.setEntryDisplayImage(result.image);
		return false;
	}

	/*************************** handleSkinButton *******************************/
	/** 美颜按钮 */
	private void handleSkinButton(TuEditMultipleFragment editFragment)
	{
		TuEditSkinFragment fragment = this.componentOption().editSkinOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            滤镜控制器
	 * @param result
	 *            处理结果
	 */
	@Override
	public void onTuFilterResultFragmentEdited(TuFilterResultFragment fragment, TuSdkResult result)
	{
		this.onActionEdited(fragment, result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            滤镜控制器
	 * @param result
	 *            处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuFilterResultFragmentEditedAsync(TuFilterResultFragment fragment, TuSdkResult result)
	{
		this.setEntryDisplayImage(result.image);
		return false;
	}

	/*************************** handleStickerButton *******************************/
	/** 贴纸按钮 */
	protected void handleStickerButton(TuEditMultipleFragment editFragment)
	{
		TuEditStickerFragment fragment = this.componentOption().editStickerOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑贴纸选择控制器
	 * @param result
	 *            处理结果
	 */
	@Override
	public void onTuEditStickerFragmentEdited(TuEditStickerFragment fragment, TuSdkResult result)
	{
		this.onActionEdited(fragment, result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑贴纸选择控制器
	 * @param result
	 *            器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditStickerFragmentEditedAsync(TuEditStickerFragment fragment, TuSdkResult result)
	{
		this.setEntryDisplayImage(result.image);
		return false;
	}

	/*************************** handleAdjustButton *******************************/
	/** 图像调整 */
	private void handleAdjustButton(TuEditMultipleFragment editFragment)
	{
		TuEditAdjustFragment fragment = this.componentOption().editAdjustOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/*************************** handleSharpnessButton *******************************/
	/** 图像锐化 */
	private void handleSharpnessButton(TuEditMultipleFragment editFragment)
	{
		TuEditSharpnessFragment fragment = this.componentOption().editSharpnessOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/*************************** handleVignetteButton *******************************/
	/** 图像晕角 */
	private void handleVignetteButton(TuEditMultipleFragment editFragment)
	{
		TuEditVignetteFragment fragment = this.componentOption().editVignetteOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/*************************** handleDepthFieldButton *******************************/
	/** 景深 */
	private void handleApertureButton(TuEditMultipleFragment editFragment)
	{
		TuEditApertureFragment fragment = this.componentOption().editApertureOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}
	
	/*************************** handleTypeHolyLightButton *******************************/
	/** 圣光 */
	private void handleTypeHolyLightButton(TuEditMultipleFragment editFragment)
	{
		TuEditHolyLightFragment fragment = this.componentOption().editHolyLightOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}
	
	/*************************** handleSmudgeButton *******************************/
	/** 涂抹 */
	private void handleSmudgeButton(TuEditMultipleFragment editFragment)
	{
		TuEditSmudgeFragment fragment = this.componentOption().editSmudgeOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}
	
	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑涂抹控制器
	 * @param result
	 *            处理结果
	 */
	public void onTuEditSmudgeFragmentEdited(TuEditSmudgeFragment fragment, TuSdkResult result)
	{
		this.onActionEdited(fragment, result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑涂抹控制器
	 * @param result
	 *            器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	public boolean onTuEditSmudgeFragmentEditedAsync(TuEditSmudgeFragment fragment, TuSdkResult result)
	{
		this.setEntryDisplayImage(result.image);
		return false;
	}
	
	/*************************** handleWipeFilterButton *******************************/
	/** 模糊 */
	private void handleWipeFilterButton(TuEditMultipleFragment editFragment)
	{
		TuEditWipeAndFilterFragment fragment = this.componentOption().editWipeAndFilterOption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            图片编辑模糊控制器
	 * @param result
	 *            处理结果
	 */
	public void onTuEditWipeAndFilterFragmentEdited(TuEditWipeAndFilterFragment fragment, TuSdkResult result)
	{
		this.onActionEdited(fragment, result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            图片编辑模糊控制器
	 * @param result
	 *            器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	public boolean onTuEditWipeAndFilterFragmentEditedAsync(TuEditWipeAndFilterFragment fragment, TuSdkResult result)
	{
		this.setEntryDisplayImage(result.image);
		return false;
	}
	
	
	/*************************** handleHDRFilterButton *******************************/
	/** 模糊 */
	private void handleTypeHDRButton(TuEditMultipleFragment editFragment)
	{
		TuEditHDRFragment fragment = this.componentOption().editHDROption().fragment();
		fragment.setDelegate(this);

		this.handleAction(editFragment, fragment);
	}
}