/** 
 * TuSDKCore
 * TuEditMultipleFragment.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 上午11:54:36 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;
import org.lasque.tusdk.modules.components.edit.TuEditMultipleFragmentBase;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 多功能图像编辑控制器
 * 
 * @author Clear
 */
public class TuEditMultipleFragment extends TuEditMultipleFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_multiple_fragment");
	}

	/** 多功能图像编辑控制器委托 */
	public interface TuEditMultipleFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            多功能图像编辑控制器
		 * @param result
		 *            Sdk执行结果
		 */
		void onTuEditMultipleFragmentEdited(TuEditMultipleFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            多功能图像编辑控制器
		 * @param result
		 *            Sdk执行结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditMultipleFragmentEditedAsync(TuEditMultipleFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑动作
		 * 
		 * @param fragment
		 *            多功能图像编辑控制器
		 * @param actionType
		 *            图片编辑动作类型
		 */
		void onTuEditMultipleFragmentAction(TuEditMultipleFragment fragment, TuEditActionType actionType);
	}

	/** 多功能图像编辑控制器委托 */
	private TuEditMultipleFragmentDelegate mDelegate;

	/** 多功能图像编辑控制器委托 */
	public TuEditMultipleFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 多功能图像编辑控制器委托 */
	public void setDelegate(TuEditMultipleFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑入口控制器 */
	public TuEditMultipleFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/** 通知处理结果 */
	@Override
	protected void notifyProcessing(TuSdkResult result)
	{
		// 显示测试预览视图
		if (this.showResultPreview(result)) return;

		if (this.mDelegate == null) return;
		this.mDelegate.onTuEditMultipleFragmentEdited(this, result);
		// 销毁资源
		result.destroy();
	}

	/**
	 * 异步通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	protected boolean asyncNotifyProcessing(TuSdkResult result)
	{
		if (this.mDelegate == null) return false;
		return this.mDelegate.onTuEditMultipleFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	private int mRatioType;
	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes) */
	private int[] mRatioTypeList;

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	public int getRatioType()
	{
		return mRatioType;
	}

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	public final void setRatioType(int mRatioType)
	{
		this.mRatioType = mRatioType;
	}

	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes) */
	public final int[] getRatioTypeList()
	{
		return mRatioTypeList;
	}

	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes) */
	public final void setRatioTypeList(int[] mRatioTypeList)
	{
		this.mRatioTypeList = mRatioTypeList;
	}

	/** 获取显示的比例类型列表 */
	@Override
	public final int[] getRatioTypes()
	{
		int[] list = getRatioTypeList();

		if (list != null && list.length > 0)
		{
			list = RatioType.validRatioTypes(list);
		}
		else
		{
			list = RatioType.getRatioTypesByValue(getRatioType());
		}

		if (list == null || list.length <= 0)
		{
			list = RatioType.ratioTypes;
		}

		return list;
	}

	/*************************** view *******************************/
	/** 图片视图 */
	private ImageView mImageView;
	/** 操作步骤包装视图 */
	private LinearLayout mStepwrap;
	/** 返回前一步按钮 */
	private TuSdkTextButton mStepPrevButton;
	/** 下一步按钮 */
	private TuSdkTextButton mStepNextButton;
	/** 自动校色按钮 */
	private TuSdkTextButton mAutoAdjustButton;
	/** 返回按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mDoneButton;
	/** 动作列表包装视图 */
	private LinearLayout mActionsWrap;

	/** 图片视图 */
	public ImageView getImageView()
	{
		if (mImageView == null) mImageView = this.getViewById("lsq_imageView");
		return mImageView;
	}

	/** 操作步骤包装视图 */
	public LinearLayout getStepwrap()
	{
		if (mStepwrap == null) mStepwrap = this.getViewById("lsq_stepwrap");
		return mStepwrap;
	}

	/** 返回前一步按钮 */
	public TuSdkTextButton getStepPrevButton()
	{
		if (mStepPrevButton == null)
		{
			mStepPrevButton = this.getViewById("lsq_step_prev");
			if (mStepPrevButton != null)
			{
				mStepPrevButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mStepPrevButton;
	}

	/** 下一步按钮 */
	public TuSdkImageButton getCancelButton()
	{
		if (mCancelButton == null)
		{
			mCancelButton = this.getViewById("lsq_cancelButton");
			if (mCancelButton != null)
			{
				mCancelButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCancelButton;
	}

	/** 返回按钮 */
	public TuSdkImageButton getDoneButton()
	{
		if (mDoneButton == null)
		{
			mDoneButton = this.getViewById("lsq_doneButton");
			if (mDoneButton != null)
			{
				mDoneButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mDoneButton;
	}

	/** 下一步按钮 */
	public TuSdkTextButton getStepNextButton()
	{
		if (mStepNextButton == null)
		{
			mStepNextButton = this.getViewById("lsq_step_next");
			if (mStepNextButton != null)
			{
				mStepNextButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mStepNextButton;
	}

	/** 自动校色按钮 */
	public TuSdkTextButton getAutoAdjustButton()
	{
		if (mAutoAdjustButton == null)
		{
			mAutoAdjustButton = this.getViewById("lsq_auto_adjust");
			if (mAutoAdjustButton != null)
			{
				mAutoAdjustButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mAutoAdjustButton;
	}

	/** 动作列表包装视图 */
	public LinearLayout getActionsWrap()
	{
		if (mActionsWrap == null) mActionsWrap = this.getViewById("lsq_actions_wrapview");
		return mActionsWrap;
	}

	/** 按钮点击事件 */
	protected OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			// 分发视图点击事件
			dispatcherViewClick(v);
		}
	};

	/** 分发视图点击事件 */
	protected void dispatcherViewClick(View v)
	{
		if (this.equalViewIds(v, this.getStepPrevButton()))
		{
			this.handleStepPrevButton();
		}
		else if (this.equalViewIds(v, this.getStepNextButton()))
		{
			this.handleStepNextButton();
		}
		else if (this.equalViewIds(v, this.getAutoAdjustButton()))
		{
			this.handleAutoAdjust();
		}
		else if (v.getTag() != null && (v.getTag() instanceof TuEditActionType))
		{
			this.handleAction((TuEditActionType) v.getTag());
		}
		else if (this.equalViewIds(v, this.getCancelButton()))
		{
			this.navigatorBarCancelAction(null);
		}
		else if (this.equalViewIds(v, this.getDoneButton()))
		{
			this.handleCompleteButton();
		}
	}

	/************************** loadView *****************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 图片视图
		getImageView();
		// 操作步骤包装视图
		this.showView(getStepwrap(), !this.isDisableStepsSave());
		// 返回前一步按钮
		getStepPrevButton();
		// 下一步按钮
		getStepNextButton();
		// 自动校色按钮
		getAutoAdjustButton();
		// 返回按钮
		getCancelButton();
		// 完成按钮
		getDoneButton();
		// 创建动作按钮列表
		this.buildActionButtons();
		// 刷新操作步骤状态
		this.refreshStepStates();
	}

	/** 创建动作按钮列表 */
	private void buildActionButtons()
	{
		LinearLayout wrap = this.getActionsWrap();
		if (wrap == null) return;
		wrap.removeAllViews();

		for (TuEditActionType type : this.getModules())
		{
			View btn = this.buildActionButton(type);
			if (btn == null) continue;
			btn.setTag(type);
			btn.setOnClickListener(mButtonClickListener);
			wrap.addView(btn);
		}
	}

	/** 创建动作按钮视图 */
	protected View buildActionButton(TuEditActionType type)
	{
		if (type == null) return null;

		String title, icon;

		switch (type)
			{
			case TypeCuter:
				title = "lsq_edit_entry_cuter";
				icon = "lsq_style_default_edit_icon_edit";
				break;
			case TypeFilter:
				title = "lsq_edit_entry_filter";
				icon = "lsq_style_default_edit_icon_filter";
				break;
			case TypeSticker:
				title = "lsq_edit_entry_sticker";
				icon = "lsq_style_default_edit_icon_sticker";
				break;
			case TypeSkin:
				title = "lsq_edit_skin_title";
				icon = "lsq_style_default_edit_icon_skin";
				break;
			case TypeAdjust:
				title = "lsq_filter_set_adjustment";
				icon = "lsq_style_default_edit_icon_adjustment";
				break;
			case TypeSmudge:
				title = "lsq_edit_entry_smudge";
				icon = "lsq_style_default_edit_icon_smudge";
				break;
			case TypeWipeFilter:
				title = "lsq_edit_entry_smudge_filter";
				icon = "lsq_style_default_edit_icon_wipe_filter";
				break;
			case TypeSharpness:
				title = "lsq_filter_set_sharpness";
				icon = "lsq_style_default_edit_icon_sharpness";
				break;
			case TypeVignette:
				title = "lsq_filter_set_vignette";
				icon = "lsq_style_default_edit_icon_vignette";
				break;
			case TypeAperture:
				title = "lsq_filter_set_aperture";
				icon = "lsq_style_default_edit_icon_depthfield";
				break;
			case TypeHolyLight:
				title = "lsq_edit_holy_light";
				icon = "lsq_style_default_edit_holy_light";
				break;
			case TypeHDR:
				title = "lsq_edit_entry_hdr";
				icon = "lsq_style_default_edit_icon_hdr";
				break;
			default:
				return null;
			}

		TuSdkSize screenSize = TuSdkContext.getDisplaySize();

		float count = getModules().size();
		count = (count <= 5) ? count : 4.6f;
		int btnWidth = (int) Math.floor(screenSize.width / count);

		TuSdkTextButton btn = new TuSdkTextButton(this.getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		btn.setLayoutParams(params);

		btn.setGravity(Gravity.CENTER);
		int padding = TuSdkContext.dip2px(18);
		btn.setPadding(0, padding, 0, TuSdkContext.dip2px(10));
		btn.setTextColor(Color.WHITE);
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		btn.setText(TuSdkContext.getString(title));
		btn.setCompoundDrawables(null, TuSdkContext.getDrawable(icon), null, null);

		return btn;
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
		
		if (hasRequiredPermission())
		{
			this.loadImageWithThread();
		}
		else
		{
			requestRequiredPermissions();
		}
	}

	/**
	 * 是否已被授予权限
	 * 
	 * @param permissionGranted
	 */
	protected void onPermissionGrantedResult(boolean permissionGranted)
	{
		if (permissionGranted)
		{
			this.loadImageWithThread();
		}
		else
		{
			String msg = TuSdkContext.getString("lsq_edit_no_access", ContextUtils.getAppName(getContext()));
			
			TuSdkViewHelper.alert(permissionAlertDelegate, this.getContext(), TuSdkContext.getString("lsq_edit_alert_title"), 
					msg, TuSdkContext.getString("lsq_button_close"), TuSdkContext.getString("lsq_button_setting")
			);
		}
	}
	
	/**
	 * 处理编辑动作
	 * 
	 * @param actionType
	 *            图片编辑动作类型
	 */
	protected void handleAction(TuEditActionType actionType)
	{
		if (this.mDelegate == null) return;
		this.mDelegate.onTuEditMultipleFragmentAction(this, actionType);
	}

	/** 设置显示的图片 */
	@Override
	public void setDisplayImage(Bitmap image)
	{
		if (image == null) return;
		this.setImage(image);

		if (this.getImageView() != null)
		{
			this.getImageView().setImageBitmap(image);
		}
	}

	/***
	 * 刷新操作步骤状态
	 * 
	 * @param histories
	 *            历史记录数
	 * @param brushies
	 *            丢弃的记录数
	 */
	@Override
	protected void onRefreshStepStates(int histories, int brushies)
	{
		// 后退按钮
		this.setEnabled(getStepPrevButton(), histories > 1);
		// 前进按钮
		this.setEnabled(getStepNextButton(), brushies > 0);
		// 自动校色按钮
		this.setEnabled(getAutoAdjustButton(), histories <= 1);
	}

	/** 设置视图是否开启 */
	private void setEnabled(View view, boolean isEnabled)
	{
		if (view == null) return;
		view.setEnabled(isEnabled);
		ViewCompat.setAlpha(view, isEnabled ? 1.0f : 0.5f);
	}
}