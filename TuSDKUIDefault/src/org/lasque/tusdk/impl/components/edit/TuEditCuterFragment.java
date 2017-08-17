/** 
 * TuSDKCore
 * TuEditCuterFragment.java
 *
 * @author 		Clear
 * @Date 		2014-12-23 下午6:06:50 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface.LsqImageChangeType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.edit.TuEditCuterFragmentBase;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 图片编辑裁切旋转控制器
 * 
 * @author Clear
 */
public class TuEditCuterFragment extends TuEditCuterFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_cuter_fragment");
	}

	/** 图片编辑裁切旋转控制器委托 */
	public interface TuEditCuterFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑裁切旋转控制器
		 * @param result
		 *            图片编辑裁切旋转控制器处理结果
		 */
		void onTuEditCuterFragmentEdited(TuEditCuterFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑裁切旋转控制器
		 * @param result
		 *            图片编辑裁切旋转控制器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditCuterFragmentEditedAsync(TuEditCuterFragment fragment, TuSdkResult result);
	}

	/** 图片编辑裁切旋转控制器委托 */
	private TuEditCuterFragmentDelegate mDelegate;

	/** 图片编辑裁切旋转控制器委托 */
	public TuEditCuterFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑裁切旋转控制器委托 */
	public void setDelegate(TuEditCuterFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑裁切旋转控制器 */
	public TuEditCuterFragment()
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

	/**
	 * 通知处理结果
	 * 
	 * @param result
	 *            SDK处理结果
	 */
	@Override
	protected void notifyProcessing(TuSdkResult result)
	{
		// 显示测试预览视图
		if (this.showResultPreview(result)) return;

		if (this.mDelegate == null) return;
		this.mDelegate.onTuEditCuterFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditCuterFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/
	/** 是否开启图片旋转 (默认: false) */
	private boolean mEnableTrun;
	/** 是否开启图片镜像(默认: false) */
	private boolean mEnableMirror;
	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	private int mRatioType;
	/**
	 *  裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)  
	 *  
	 *  设置 int 型数组来控制显示的按钮顺序， 例如:
	 *	new int[] {RatioType.ratio_orgin, RatioType.ratio_1_1, RatioType.ratio_2_3, RatioType.ratio_3_4}
	 *
	 */
	private int[] mRatioTypeList;
	/** 是否仅返回裁切参数，不返回处理图片 */
	private boolean mOnlyReturnCuter;

	/** 是否开启图片旋转(默认: false) */
	public boolean isEnableTrun()
	{
		return mEnableTrun;
	}

	/** 是否开启图片旋转(默认: false) */
	public void setEnableTrun(boolean mEnableTrun)
	{
		this.mEnableTrun = mEnableTrun;
	}

	/** 是否开启图片镜像(默认: false) */
	public boolean isEnableMirror()
	{
		return mEnableMirror;
	}

	/** 是否开启图片镜像(默认: false) */
	public void setEnableMirror(boolean mEnableMirror)
	{
		this.mEnableMirror = mEnableMirror;
	}

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	public int getRatioType()
	{
		return mRatioType;
	}

	/** 裁剪比例 (默认:RatioType.ratio_all) */
	public void setRatioType(int mRatioType)
	{
		this.mRatioType = mRatioType;
	}
	
	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)  */
	public final int[] getRatioTypeList()
	{
		return mRatioTypeList;
	}

	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)  */
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
			list = RatioType.defaultRatioTypes;
		}
		
		return list;
	}

	/** 是否仅返回裁切参数，不返回处理图片 */
	@Override
	public boolean isOnlyReturnCuter()
	{
		return mOnlyReturnCuter;
	}

	/** 是否仅返回裁切参数，不返回处理图片 */
	public void setOnlyReturnCuter(boolean mOnlyReturnCuter)
	{
		this.mOnlyReturnCuter = mOnlyReturnCuter;
	}

	/*************************** view *******************************/
	/** 图片包装视图 */
	private RelativeLayout mImageWrapView;
	/** 裁剪选取视图 */
	private TuMaskRegionView mCutRegionView;
	/** 配置调节栏目 */
	private LinearLayout mOptionBar;
	/** 比例列表按钮 */
	private List<TuSdkTextButton> mRatioButtons;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;
	/** 旋转按钮 */
	private TuSdkImageButton mTrunButton;
	/** 镜像按钮 */
	private TuSdkImageButton mMirrorButton;
	/** 选项列表包装 */
	private View mOptionsWrap;

	/** 图片包装视图 */
	@Override
	public RelativeLayout getImageWrapView()
	{
		if (mImageWrapView == null)
		{
			mImageWrapView = this.getViewById("lsq_imageWrapView");
			if (mImageWrapView != null)
			{
				mImageWrapView.setClickable(false);
				mImageWrapView.setClipChildren(false);
			}
		}
		return mImageWrapView;
	}

	/** 裁剪选取视图 */
	@Override
	public TuMaskRegionView getCutRegionView()
	{
		if (mCutRegionView == null)
		{
			mCutRegionView = this.getViewById("lsq_cutRegionView");
			if (mCutRegionView != null)
			{
				mCutRegionView.setEdgeMaskColor(0x80000000);
				mCutRegionView.setEdgeSideColor(0x80FFFFFF);
				mCutRegionView.setEdgeSideWidthDP(2);
				mCutRegionView.addOnLayoutChangeListener(mRegionLayoutChangeListener);
			}
		}
		return mCutRegionView;
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

	/** 配置调节栏目 */
	public LinearLayout getOptionBar()
	{
		if (mOptionBar == null)
		{
			mOptionBar = this.getViewById("lsq_optionBar");

			if (mOptionBar != null && getRatioTypes().length < 2)
			{
				this.showView(mOptionBar, false);
			}
		}
		return mOptionBar;
	}

	/** 比例列表按钮 */
	public List<TuSdkTextButton> getRatioButtons()
	{
		if (mRatioButtons == null && getOptionBar() != null)
		{
			LinearLayout parent = getOptionBar();
			parent.removeAllViews();
			
			mRatioButtons = new ArrayList<TuSdkTextButton>(parent.getChildCount());
			
			int[] ratioTypes = getRatioTypes();
			for(int i = 0; i<ratioTypes.length; i++)
			{
				int ratio = ratioTypes[i];
				
				TuSdkTextButton btn = buildRatioActionButton(ratio, ratioTypes.length);
				if (btn == null) continue;
				
				btn.index = ratio;
				btn.setOnClickListener(mButtonClickListener);

				boolean selected = false;
				// 如果没有设置指定比例， 默认选中第一个
				if (this.getCurrentRatioType() < 1)
				{
					selected = (mRatioButtons.size() == 0);
				}
				else
				{
					selected = (ratio == this.getCurrentRatioType());
				}

				btn.setSelected(selected);
				
				parent.addView(btn);
				mRatioButtons.add(btn);
			}
		}
		return mRatioButtons;
	}
	
	/** 创建动作按钮视图 */
	protected TuSdkTextButton buildRatioActionButton(int ratioType, int totalNum)
	{
		String title, icon;

		switch (ratioType)
		{
			case RatioType.ratio_orgin:
				title = "lsq_edit_cuter_ratio_orgin";
				icon = "lsq_style_default_ratio_orgin";
				break;
			case RatioType.ratio_1_1:
				title = "lsq_edit_cuter_ratio_1_1";
				icon = "lsq_style_default_ratio_1_1";
				break;
			case RatioType.ratio_2_3:
				title = "lsq_edit_cuter_ratio_2_3";
				icon = "lsq_style_default_ratio_2_3";
				break;
			case RatioType.ratio_3_4:
				title = "lsq_edit_cuter_ratio_3_4";
				icon = "lsq_style_default_ratio_3_4";
				break;
			case RatioType.ratio_9_16:
				title = "lsq_edit_cuter_ratio_9_16";
				icon = "lsq_style_default_ratio_9_16";
				break;
			case RatioType.ratio_3_2:
				title = "lsq_edit_cuter_ratio_3_2";
				icon = "lsq_style_default_ratio_3_2";
				break;
			case RatioType.ratio_4_3:
				title = "lsq_edit_cuter_ratio_4_3";
				icon = "lsq_style_default_ratio_4_3";
				break;
			case RatioType.ratio_16_9:
				title = "lsq_edit_cuter_ratio_16_9";
				icon = "lsq_style_default_ratio_16_9";
				break;
			default:
				return null;
		}
		
		int color = TuSdkContext.getColor("lsq_color_orange");
		
		TuSdkSize screenSize = TuSdkContext.getDisplaySize();

		float num = (totalNum <= 5) ? totalNum : 4.5f;
		int btnWidth = (int) Math.floor(screenSize.width / num);

		TuSdkTextButton btn = new TuSdkTextButton(this.getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		btn.setLayoutParams(params);

		btn.setGravity(Gravity.CENTER);
		int padding = TuSdkContext.dip2px(18);
		btn.setPadding(0, padding, 0, TuSdkContext.dip2px(10));
		btn.setSelectedColor(color);
		btn.setText(TuSdkContext.getString(title));
		btn.setCompoundDrawables(null, TuSdkContext.getDrawable(icon), null, null);

		return btn;
	}

	/** 取消按钮 */
	public final TuSdkImageButton getCancelButton()
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
	public final TuSdkImageButton getCompleteButton()
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

	/** 旋转按钮 */
	public ImageView getTrunButton()
	{
		if (mTrunButton == null)
		{
			mTrunButton = this.getViewById("lsq_trunButton");
			if (mTrunButton != null)
			{
				mTrunButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mTrunButton;
	}

	/** 镜像按钮 */
	public ImageView getMirrorButton()
	{
		if (mMirrorButton == null)
		{
			mMirrorButton = this.getViewById("lsq_mirrorButton");
			if (mMirrorButton != null)
			{
				mMirrorButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mMirrorButton;
	}

	/** 按钮点击事件 */
	private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
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
		if (this.equalViewIds(v, this.getCancelButton()))
		{
			this.handleBackButton();
		}
		else if (this.equalViewIds(v, this.getCompleteButton()))
		{
			this.handleCompleteButton();
		}
		else if (this.equalViewIds(v, this.getTrunButton()))
		{
			this.handleTrunButton();
		}
		else if (this.equalViewIds(v, this.getMirrorButton()))
		{
			this.handleMirrorButton();
		}
		else if (v instanceof TuSdkTextButton)
		{
			this.handleRatioButton((TuSdkTextButton) v);
		}
	}

	/************************** loadView *****************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 图片视图
		getImageView();
		// 裁剪选取视图
		getCutRegionView();
		// 比例列表按钮
		getRatioButtons();
		// 选项列表包装
		getOptionsWrap();
		// 取消按钮
		getCancelButton();
		// 完成按钮
		getCompleteButton();
		// 旋转按钮
		this.showViewIn(getTrunButton(), isEnableTrun());
		// 镜像按钮
		this.showViewIn(getMirrorButton(), isEnableMirror());
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
	}

	/************************** handle event *****************************/
	/** 后退按钮 */
	protected void handleBackButton()
	{
		this.navigatorBarBackAction(null);
	}

	/** 旋转动作 */
	protected void handleTrunButton()
	{
		if (this.getImageView() == null || this.getImageView().isInAnimation()) return;

		this.getImageView().changeImageAnimation(LsqImageChangeType.TypeImageChangeTurnLeft);
	}

	/** 镜像动作 */
	protected void handleMirrorButton()
	{
		if (this.getImageView() == null || this.getImageView().isInAnimation()) return;

		this.getImageView().changeImageAnimation(LsqImageChangeType.TypeImageChangeMirrorHorizontal);
	}

	/** 改变比例按钮 */
	protected void handleRatioButton(TuSdkTextButton v)
	{
		int ratioType = v.index;
		if (this.getCurrentRatioType() == ratioType) return;

		this.setCurrentRatioType(ratioType);

		// 设置按钮选中状态
		List<TuSdkTextButton> btns = getRatioButtons();
		if (btns == null) return;
		for (TuSdkTextButton tuSdkTextButton : btns)
		{
			tuSdkTextButton.setSelected(tuSdkTextButton.equals(v));
		}

		if (this.getCutRegionView() != null)
		{
			Rect rect = this.getCutRegionView().changeRegionRatio(this.getCurrentRatio());
			if (this.getImageView() != null)
			{
				this.getImageView().changeRegionRatio(rect, this.getCurrentRatio());
			}
		}
	}
}