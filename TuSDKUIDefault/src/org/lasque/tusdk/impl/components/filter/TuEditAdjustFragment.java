/** 
 * TuSDKCore
 * TuEditAdjustFragment.java
 *
 * @author 		Clear
 * @Date 		2015-4-29 下午8:23:54 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.filter.TuEditAdjustFragmentBase;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 颜色调整控制器
 * 
 * @author Clear
 */
public class TuEditAdjustFragment extends TuEditAdjustFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_adjust_fragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.setRootViewLayoutId(getLayoutId());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/*************************** view *******************************/
	/** 图片包装视图 */
	private RelativeLayout mImageWrapView;
	/** 参数配置视图 */
	private ParameterConfigViewInterface mConfigView;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;

	/** 选项列表包装 */
	private View mOptionsWrap;
	/** 动作列表包装视图 */
	private LinearLayout mActionsWrap;
	/** 配置操作按钮包装 */
	private ViewGroup mConfigActionBar;
	/** 配置完成按钮 */
	private TuSdkImageButton mConfigCompeleteButton;
	/** 配置完成按钮 */
	private TuSdkImageButton mConfigCancelButton;

	/** 图片包装视图 */
	@Override
	public RelativeLayout getImageWrapView()
	{
		if (mImageWrapView == null)
		{
			mImageWrapView = this.getViewById("lsq_imageWrapView");
		}
		return mImageWrapView;
	}

	/** 取消按钮 */
	@Override
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

	/** 完成按钮 */
	@Override
	public TuSdkImageButton getCompleteButton()
	{
		if (mCompleteButton == null)
		{
			mCompleteButton = this.getViewById("lsq_completeButton");
			if (mCompleteButton != null)
			{
				mCompleteButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCompleteButton;
	}

	/** 参数配置视图 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & ParameterConfigViewInterface> T getConfigView()
	{
		if (mConfigView == null)
		{
			View view = this.getViewById("lsq_param_config_view");
			if (view == null || !(view instanceof ParameterConfigViewInterface)) return null;
			mConfigView = (ParameterConfigViewInterface) view;
			if (mConfigView != null)
			{
				mConfigView.setDelegate(this);
			}
		}
		return (T) mConfigView;
	}

	/** 选项列表包装 */
	public View getOptionsWrap()
	{
		if (mOptionsWrap == null)
		{
			mOptionsWrap = this.getViewById("lsq_option_wrap");
		}
		return mOptionsWrap;
	}

	/** 动作列表包装视图 */
	public LinearLayout getActionsWrap()
	{
		if (mActionsWrap == null)
		{
			mActionsWrap = this.getViewById("lsq_actions_wrapview");
		}
		return mActionsWrap;
	}

	/** 配置操作包装 */
	public ViewGroup getConfigActionBar()
	{
		if (mConfigActionBar == null)
		{
			mConfigActionBar = this.getViewById("lsq_config_bottomBar");
		}
		return mConfigActionBar;
	}
	
	/** 配置取消按钮 */
	public TuSdkImageButton getConfigCancelButton()
	{
		if (mConfigCancelButton == null)
		{
			mConfigCancelButton = this.getViewById("lsq_configCancelButton");
			if (mConfigCancelButton != null)
			{
				mConfigCancelButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mConfigCancelButton;
	}

	/** 配置完成按钮 */
	public TuSdkImageButton getConfigCompeleteButton()
	{
		if (mConfigCompeleteButton == null)
		{
			mConfigCompeleteButton = this.getViewById("lsq_configCompleteButton");
			if (mConfigCompeleteButton != null)
			{
				mConfigCompeleteButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mConfigCompeleteButton;
	}

	/** 分发视图点击事件 */
	protected void dispatcherViewClick(View v)
	{
		super.dispatcherViewClick(v);
		if (this.equalViewIds(v, this.getConfigCompeleteButton()))
		{
			this.handleConfigCompeleteButton();
		}
		else if (this.equalViewIds(v, this.getConfigCancelButton()))
		{
			super.onParameterConfigRest(this.getConfigView(), this.getCurrentAction());
			this.handleConfigCompeleteButton();
		}
		else if (v.getTag() != null && (v.getTag() instanceof Integer))
		{
			this.handleAction((Integer) v.getTag());
		}
	}

	/********************************** loadView ***********************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 选项列表包装
		getOptionsWrap();
		// 动作列表包装视图
		getActionsWrap();
		// 配置完成按钮
		this.showViewIn(getConfigActionBar(), false);
		
		this.getImageView().setImageBackgroundColor(TuSdkContext.getColor("lsq_background_editor"));
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		this.loadImageWithThread();
		if (getConfigActionBar() != null) ViewCompat.setTranslationY(getConfigActionBar(), getConfigActionBar().getHeight());
	}

	/** 创建动作按钮列表 */
	@Override
	protected void buildActionButtons()
	{
		LinearLayout wrap = this.getActionsWrap();
		if (wrap == null) return;
		wrap.removeAllViews();

		super.buildActionButtons();
	}

	/** 创建动作按钮视图 */
	@Override
	protected View buildActionButton(String type, int index)
	{
		if (type == null) return null;

		String title = String.format("lsq_filter_set_%s", type);
		String icon = String.format("lsq_style_default_edit_icon_%s", type);
		
		TuSdkSize screenSize = TuSdkContext.getDisplaySize();
		int btnWidth = (int) Math.floor(screenSize.width / 4.5);

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

		btn.setTag(index);
		btn.setOnClickListener(mButtonClickListener);

		if (this.getActionsWrap() != null) this.getActionsWrap().addView(btn);

		return btn;
	}

	/*************************** Index *************************/

	/** 设置配置视图隐藏状态 */
	@Override
	protected void setConfigViewShowState(final boolean isShow)
	{
		this.showViewIn(this.getOptionsWrap(), true);
		this.showViewIn(this.getConfigActionBar(), true);

		int top = isShow ? 0 : this.getConfigActionBar().getHeight();

		ViewCompat.animate(this.getOptionsWrap()).alpha(isShow ? 0 : 1).setDuration(220);
		ViewPropertyAnimatorListener listener = new TuSdkViewAnimatorAdapter()
		{
			@Override
			public void onAnimationEnd(View view, boolean cancelled)
			{
				if (cancelled) return;
				showViewIn(getOptionsWrap(), !isShow);
				showViewIn(getConfigActionBar(), isShow);
			}
		};
		ViewCompat.animate(this.getConfigActionBar()).translationY(top).setDuration(220).setListener(listener);
	}
}