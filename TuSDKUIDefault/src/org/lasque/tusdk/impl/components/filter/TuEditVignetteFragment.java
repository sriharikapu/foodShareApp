/** 
 * TuSDKCore
 * TuEditVignetteFragment.java
 *
 * @author 		Clear
 * @Date 		2015-5-8 下午2:49:47 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.filter.TuEditVignetteFragmentBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 暗角控制器
 * 
 * @author Clear
 */
public class TuEditVignetteFragment extends TuEditVignetteFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_skin_fragment");
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
	
	/********************************** loadView ***********************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);
		
		this.getImageView().setImageBackgroundColor(TuSdkContext.getColor("lsq_background_editor"));
	}
}