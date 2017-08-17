/** 
 * TuSDKCore
 * TuEditApertureFragment.java
 *
 * @author 		Clear
 * @Date 		2015-5-8 下午3:18:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.filter.TuEditApertureFragmentBase;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 大光圈控制器
 * 
 * @author Clear
 */
public class TuEditApertureFragment extends TuEditApertureFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_aperture_fragment");
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
	private View mOptionsBar;
	/** 关闭按钮 */
	private TuSdkTextButton mCloseButton;
	/** 径向按钮 */
	private TuSdkTextButton mRadialButton;
	/** 线性按钮 */
	private TuSdkTextButton mLinearButton;
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
	public View getOptionsBar()
	{
		if (mOptionsBar == null)
		{
			mOptionsBar = this.getViewById("lsq_optionBar");
		}
		return mOptionsBar;
	}

	/** 关闭按钮 */
	public TuSdkTextButton getCloseButton()
	{
		if (mCloseButton == null)
		{
			mCloseButton = this.getViewById("lsq_closeButton");
			if (mCloseButton != null)
			{
				mCloseButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCloseButton;
	}

	/** 径向按钮 */
	public TuSdkTextButton getRadialButton()
	{
		if (mRadialButton == null)
		{
			mRadialButton = this.getViewById("lsq_radialButton");
			if (mRadialButton != null)
			{
				mRadialButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mRadialButton;
	}

	/** 线性按钮 */
	public TuSdkTextButton getLinearButton()
	{
		if (mLinearButton == null)
		{
			mLinearButton = this.getViewById("lsq_linearButton");
			if (mLinearButton != null)
			{
				mLinearButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mLinearButton;
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
		if (this.equalViewIds(v, this.getCloseButton()))
		{
			this.handleSelectiveAction(0, 0);
		}
		else if (this.equalViewIds(v, this.getRadialButton()))
		{
			this.handleSelectiveAction(1, 0.1f);
		}
		else if (this.equalViewIds(v, this.getLinearButton()))
		{
			this.handleSelectiveAction(2, 0.2f);
		}
		else if (this.equalViewIds(v, this.getConfigCompeleteButton()))
		{
			this.handleConfigCompeleteButton();
		}
		else if (this.equalViewIds(v, this.getConfigCancelButton()))
		{
			super.onParameterConfigRest(this.getConfigView(), 0);
			
			this.handleConfigCompeleteButton();
		}
	}

	/********************************** loadView ***********************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 选项列表包装
		getOptionsBar();
		// 关闭按钮
		getCloseButton();
		// 径向按钮
		getRadialButton();
		// 线性按钮
		getLinearButton();
		// 配置完成按钮
		this.showViewIn(getConfigActionBar(), false);
		
		this.getImageView().setImageBackgroundColor(TuSdkContext.getColor("lsq_background_editor"));
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		this.loadImageWithThread();
		if (getConfigActionBar() != null)
		{
			ViewCompat.setTranslationY(getConfigActionBar(), getConfigActionBar().getHeight());
		}
	}

	/*************************** Config *************************/

	/** 设置配置视图隐藏状态 */
	@Override
	protected void setConfigViewShowState(final boolean isShow)
	{
		this.showViewIn(this.getOptionsBar(), true);
		this.showViewIn(this.getConfigActionBar(), true);

		int top = isShow ? 0 : this.getConfigActionBar().getHeight();

		ViewCompat.animate(this.getOptionsBar()).alpha(isShow ? 0 : 1).setDuration(220);
		ViewCompat.animate(this.getConfigActionBar()).translationY(top).setDuration(220).setListener(new TuSdkViewAnimatorAdapter()
		{
			@Override
			public void onAnimationEnd(View view, boolean cancelled)
			{
				if (cancelled) return;
				showViewIn(getOptionsBar(), !isShow);
				showViewIn(getConfigActionBar(), isShow);
			}
		});
	}
}