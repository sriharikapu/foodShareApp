/** 
 * TuSDKGee
 * TuEditWipeAndBlurFragment.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-25 下午17:10:28 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.filter;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.filter.TuEditWipeAndFilterFragmentBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize.SizeType;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 图片编辑模糊控制器
 * 
 * @author Yanlin
 */
public class TuEditWipeAndFilterFragment extends TuEditWipeAndFilterFragmentBase 
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_wipe_and_filter_fragment");
	}

	/** 图片编辑模糊控制器委托 */
	public interface TuEditWipeAndFilterFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑模糊控制器
		 * @param result
		 *            处理结果
		 */
		void onTuEditWipeAndFilterFragmentEdited(TuEditWipeAndFilterFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑模糊控制器
		 * @param result
		 *            器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditWipeAndFilterFragmentEditedAsync(TuEditWipeAndFilterFragment fragment, TuSdkResult result);
	}

	/** 图片编辑模糊控制器委托 */
	private TuEditWipeAndFilterFragmentDelegate mDelegate;

	/** 图片编辑模糊控制器委托 */
	public TuEditWipeAndFilterFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑模糊控制器委托 */
	public void setDelegate(TuEditWipeAndFilterFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.setRootViewLayoutId(getLayoutId());
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
		this.mDelegate.onTuEditWipeAndFilterFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditWipeAndFilterFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/
	
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	private SizeType mDefaultBrushSize;
	/** 当前笔刷大小 */
	private SizeType mCurrentBrushSize;
	/** 允许撤销的次数 (默认: 5) */
	private int mMaxUndoCount = 5;
	
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	public SizeType getDefaultBrushSize()
	{
		if (mDefaultBrushSize == null)
		{
			mDefaultBrushSize = SizeType.MediumBrush;
		}
		return mDefaultBrushSize;
	}

	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	public void setDefaultBrushSize(SizeType mDefaultBrushSize)
	{
		this.mDefaultBrushSize = mDefaultBrushSize;
	}
	
	/** 允许撤销的次数 (默认: 5) */
	public int getMaxUndoCount()
	{
		return mMaxUndoCount;
	}
	
	/** 允许撤销的次数 (默认: 5) */
	public void setMaxUndoCount(int mMaxUndoCount)
	{
		this.mMaxUndoCount = mMaxUndoCount;
	}
	
	/************************* view ******************************/
	/** 涂抹视图 */
	private SmudgeView mSmudgeView;
	/** 小笔刷设置按钮 */
	private TuSdkImageButton mSmallSizeButton;
	/** 中笔刷设置按钮 */
	private TuSdkImageButton mMediumSizeButton;
	/** 大笔刷设置按钮 */
	private TuSdkImageButton mLargeSizeButton;
	
	/** 放大区域图 */
	private ImageView mZoomInImage;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;
	/** 撤销按钮 */
	private TuSdkImageButton mUndoButton;
	/** 重做按钮 */
	private TuSdkImageButton mRedoButton;
	/** 查看原图按钮 */
	private TuSdkImageButton mOriginalButton;

	/** 涂抹视图 */
	@Override
	public SmudgeView getSmudgeView()
	{
		if (mSmudgeView == null)
		{
			mSmudgeView = this.getViewById("lsq_smudgeView");
			if (mSmudgeView != null)
			{
				mSmudgeView.setDelegate(this);
				mSmudgeView.setMaxUndoCount(this.getMaxUndoCount());
			}
		}
		return mSmudgeView;
	}
	
	/** 放大区域图 */
	@Override
	public ImageView getZoomInImage()
	{
		if (mZoomInImage == null)
		{
			mZoomInImage = this.getViewById("lsq_zoomInView");
		}
		return mZoomInImage;
	}
	
	/** 小尺寸笔刷设置按钮 */
	public TuSdkImageButton getSmallSizeButton()
	{
		if (mSmallSizeButton == null)
		{
			mSmallSizeButton = this.getViewById("lsq_small_size");
			if (mSmallSizeButton != null)
			{
				mSmallSizeButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mSmallSizeButton;
	}

	/** 中尺寸笔刷设置按钮 */
	public TuSdkImageButton getMediumSizeButton()
	{
		if (mMediumSizeButton == null)
		{
			mMediumSizeButton = this.getViewById("lsq_medium_size");
			if (mMediumSizeButton != null)
			{
				mMediumSizeButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mMediumSizeButton;
	}
	
	/** 大尺寸笔刷设置按钮 */
	public TuSdkImageButton getLargeSizeButton()
	{
		if (mLargeSizeButton == null)
		{
			mLargeSizeButton = this.getViewById("lsq_large_size");
			if (mLargeSizeButton != null)
			{
				mLargeSizeButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mLargeSizeButton;
	}
	/** 取消按钮 */
	public TuSdkImageButton getCancelButton()
	{
		if (mCancelButton == null)
		{
			mCancelButton = this.getViewById("lsq_bar_cancelButton");
			if (mCancelButton != null)
			{
				mCancelButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCancelButton;
	}

	/** 完成按钮 */
	public TuSdkImageButton getCompleteButton()
	{
		if (mCompleteButton == null)
		{
			mCompleteButton = this.getViewById("lsq_bar_completeButton");
			if (mCompleteButton != null)
			{
				mCompleteButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCompleteButton;
	}

	/** 撤销按钮 */
	public TuSdkImageButton getUndoButton()
	{
		if (mUndoButton == null)
		{
			mUndoButton = this.getViewById("lsq_bar_undoButton");
			if (mUndoButton != null)
			{
				mUndoButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mUndoButton;
	}

	/** 重做按钮 */
	public TuSdkImageButton getRedoButton()
	{
		if (mRedoButton == null)
		{
			mRedoButton = this.getViewById("lsq_bar_redoButton");
			if (mRedoButton != null)
			{
				mRedoButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mRedoButton;
	}
	
	/** 查看原图按钮 */
	public TuSdkImageButton getOriginalButton()
	{
		if (mOriginalButton == null)
		{
			mOriginalButton = this.getViewById("lsq_bar_originalButton");
			if (mOriginalButton != null)
			{
				mOriginalButton.setOnTouchListener(mOnTouchListener);
			}
		}
		return mOriginalButton;
	}
	
	/**
	 *  用户操作导致撤销/重做数据发生变化
	 *
	 *  @param undoCount 可以撤销的次数
	 *  @param redoCount 可以重做的次数
	 */
	@Override
	public void onRefreshStepStatesWithHistories(int undoCount, int redoCount)
	{
		setEnabled(getUndoButton(), undoCount>0);
		setEnabled(getRedoButton(), redoCount>0);
	}

	/** 设置视图是否开启 */
	private void setEnabled(View view, boolean isEnabled)
	{
		if (view == null) return;
		view.setEnabled(isEnabled);
		ViewCompat.setAlpha(view, isEnabled ? 1.0f : 0.5f);
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
	
	/**
	 * 触摸事件
	 */
	@SuppressLint("ClickableViewAccessibility")
	protected OnTouchListener mOnTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			// 仅允许单点触摸
			if (mOriginalButton == null || event.getPointerCount() > 1) return false;
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					handleOriginalButton(true);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
					handleOriginalButton(false);
					break;
			}
			return true;
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
		else if (this.equalViewIds(v, this.getUndoButton()))
		{
			this.handleUndoButton();
		}
		else if (this.equalViewIds(v, this.getRedoButton()))
		{
			this.handleRedoButton();
		}
		else if (this.equalViewIds(v, this.getSmallSizeButton()))
		{
			setBrushSize(SizeType.SmallBrush);
		}
		else if (this.equalViewIds(v, this.getMediumSizeButton()))
		{
			setBrushSize(SizeType.MediumBrush);
		}
		else if (this.equalViewIds(v, this.getLargeSizeButton()))
		{
			setBrushSize(SizeType.LargeBrush);
		}
	}
	
	/**
	 * 处理点击显示原图按钮的动作
	 * 
	 * @param isDown
	 */
	protected void handleOriginalButton(boolean isDown)
	{
		if (isDown)
		{
			this.handleOrigianlButtonDown();
		}
		else
		{
			this.handleOrigianlButtonUp();
		}
	}
	
	@Override
	protected void setBrushSize(SizeType size)
	{
		super.setBrushSize(size);
		
		updateSizeButtonIcon(getSmallSizeButton(), "small", size == SizeType.SmallBrush);
		updateSizeButtonIcon(getMediumSizeButton(), "medium", size == SizeType.MediumBrush);
		updateSizeButtonIcon(getLargeSizeButton(), "large", size == SizeType.LargeBrush);
	}
	
	private void updateSizeButtonIcon(TuSdkImageButton button, String iconName, boolean selected)
	{
		String key = String.format("lsq_style_default_edit_pen_%s", iconName);
		
		if (selected)
		{
			key += "_selected";
		}
		button.setImageResource(TuSdkContext.getDrawableResId(key));
	}

	/********************************** loadView ***********************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		getSmudgeView();
		getCancelButton();
		getCompleteButton();
		getUndoButton();
		getRedoButton();
		getOriginalButton();
		getZoomInImage();
		
		getSmallSizeButton();
		getMediumSizeButton();
		getLargeSizeButton();
		
		setEnabled(getUndoButton(), false);
		setEnabled(getRedoButton(), false);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
		this.loadImageWithThread();
		
		// 默认为中等尺寸
		SizeType currentSize = this.getDefaultBrushSize();
		setBrushSize(currentSize);
	}
	
	/** 异步加载图片完成 */
	@Override
	protected void asyncLoadImageCompleted(Bitmap image)
	{
		super.asyncLoadImageCompleted(image);
		if (image == null) return;
		
		if (this.getSmudgeView() != null)
		{
			this.getSmudgeView().setImageBitmap(image);
		}
	}

	/****************************** BrushBarViewDelegate *************************************/
	
	/**
	 *  点击笔刷粗细按钮，请求切换尺寸
	 */
	public void onBrushSizeButtonClick()
	{
		SizeType nextSize = BrushSize.nextBrushSize(mCurrentBrushSize);
	    // 更新图标文字和画布设置
	    setBrushSize(nextSize);
	}
}