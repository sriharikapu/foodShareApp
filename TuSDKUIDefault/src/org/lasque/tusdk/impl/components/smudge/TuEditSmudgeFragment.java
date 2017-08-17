/** 
 * TuSDKGee
 * TuEditSmudgeFragment.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-13 下午17:10:28 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.smudge;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.widget.smudge.BrushBarView;
import org.lasque.tusdk.impl.components.widget.smudge.BrushBarView.BrushBarViewDelegate;
import org.lasque.tusdk.impl.components.widget.smudge.SmudgeView;
import org.lasque.tusdk.impl.components.widget.smudge.TuBrushSizeAnimView;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.smudge.TuEditSmudgeFragmentBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdk.modules.view.widget.smudge.BrushTableViewInterface.BrushAction;

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

/**
 * 图片编辑涂抹控制器
 * 
 * @author Yanlin
 */
public class TuEditSmudgeFragment extends TuEditSmudgeFragmentBase implements BrushBarViewDelegate
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_smudge_fragment");
	}

	/** 图片编辑笔刷选择控制器委托 */
	public interface TuEditSmudgeFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑涂抹控制器
		 * @param result
		 *            处理结果
		 */
		void onTuEditSmudgeFragmentEdited(TuEditSmudgeFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑涂抹控制器
		 * @param result
		 *            器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditSmudgeFragmentEditedAsync(TuEditSmudgeFragment fragment, TuSdkResult result);
	}

	/** 图片编辑笔刷选择控制器委托 */
	private TuEditSmudgeFragmentDelegate mDelegate;

	/** 图片编辑笔刷选择控制器委托 */
	public TuEditSmudgeFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑笔刷选择控制器委托 */
	public void setDelegate(TuEditSmudgeFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑笔刷选择控制器 */
	public TuEditSmudgeFragment()
	{

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
		this.mDelegate.onTuEditSmudgeFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditSmudgeFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/
	
	/** 需要显示的笔刷组 */
	private List<String> mBrushGroup;
	/** 行视图宽度 */
	private int mBrushBarCellWidth;
	/** 笔刷列表行视图布局资源ID */
	private int mBrushBarCellLayoutId;
	/** 笔刷栏高度 */
	private int mBrushBarHeight;
	/** 记住用户最后一次使用的笔刷 */
	private boolean mSaveLastBrush;
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	private BrushSize.SizeType mDefaultBrushSize;
	/** 当前笔刷大小 */
	private BrushSize.SizeType mCurrentBrushSize;
	/** 允许撤销的次数 (默认: 5) */
	private int mMaxUndoCount = 5;

	/** 行视图宽度 */
	public int getBrushBarCellWidth()
	{
		return mBrushBarCellWidth;
	}

	/** 行视图宽度 */
	public void setBrushBarCellWidth(int mBrushBarCellWidth)
	{
		this.mBrushBarCellWidth = mBrushBarCellWidth;
	}

	/** 行视图宽度 (单位:DP) */
	public void setBrushBarCellWidthDP(int mBrushBarCellWidthDP)
	{
		this.setBrushBarCellWidth(TuSdkContext.dip2px(mBrushBarCellWidthDP));
	}

	/**
	 * 笔刷列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.sumdge.BrushBarItemCellBase}
	 * @param 笔刷列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_brush_bar_item_cell，如需自定义请继承自
	 *            BrushBarItemCellBase)
	 */
	public int getBrushBarCellLayoutId()
	{
		return mBrushBarCellLayoutId;
	}

	/**
	 * 笔刷列表行视图布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.modules.view.widget.sumdge.BrushBarItemCellBase}
	 * @param 笔刷列表行视图布局资源ID
	 *            (默认:
	 *            tusdk_impl_component_widget_brush_bar_item_cell，如需自定义请继承自
	 *            BrushBarItemCellBase)
	 */
	public void setBrushBarCellLayoutId(int mBrushBarCellLayoutId)
	{
		this.mBrushBarCellLayoutId = mBrushBarCellLayoutId;
	}

	/** 笔刷栏高度 */
	public int getBrushBarHeight()
	{
		return mBrushBarHeight;
	}

	/** 笔刷栏高度*/
	public void setBrushBarHeight(int mBrushBarHeight)
	{
		this.mBrushBarHeight = mBrushBarHeight;
	}

	/** 笔刷栏高度 (单位:DP) */
	public void setBrushBarHeightDP(int mBrushBarHeightDP)
	{
		this.setBrushBarHeight(TuSdkContext.dip2px(mBrushBarHeightDP));
	}

	/** 记住用户最后一次使用的笔刷 */
	public boolean isSaveLastBrush()
	{
		return mSaveLastBrush;
	}

	/** 记住用户最后一次使用的笔刷 */
	public void setSaveLastBrush(boolean mSaveLastBrush)
	{
		this.mSaveLastBrush = mSaveLastBrush;
	}
	
	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	public BrushSize.SizeType getDefaultBrushSize()
	{
		if (mDefaultBrushSize == null)
		{
			mDefaultBrushSize = BrushSize.SizeType.MediumBrush;
		}
		return mDefaultBrushSize;
	}

	/** 默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细) */
	public void setDefaultBrushSize(BrushSize.SizeType mDefaultBrushSize)
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
	
	/** 需要显示的笔刷组 (如果为空将显示所有自定义笔刷) */
	public List<String> getBrushGroup()
	{
		return mBrushGroup;
	}

	/** 需要显示的笔刷组 (如果为空将显示所有自定义笔刷) */
	public void setBrushGroup(List<String> mBrushGroup)
	{
		this.mBrushGroup = mBrushGroup;
	}

	/************************* view ******************************/
	/** 涂抹视图 */
	private SmudgeView mSmudgeView;
	/** 笔刷尺寸动画视图 */
	private TuBrushSizeAnimView mSizeAnimView;
	/** 笔刷栏视图 */
	private BrushBarView mBrushBarView;
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
	
	/** 笔刷尺寸动画视图 */
	@Override
	public TuBrushSizeAnimView getSizeAnimView()
	{
		if (mSizeAnimView == null)
		{
			mSizeAnimView = this.getViewById("lsq_size_anim_view");
		}
		return mSizeAnimView;
	}
	
	/** 笔刷栏视图 */
	public BrushBarView getBrushBarView()
	{
		if (mBrushBarView == null)
		{
			mBrushBarView = this.getViewById("lsq_brush_bar");
			if (mBrushBarView != null)
			{
				mBrushBarView.setBrushGroup(this.getBrushGroup());
				mBrushBarView.setSaveLastBrush(this.isSaveLastBrush());
				mBrushBarView.setBrushBarCellLayoutId(this.getBrushBarCellLayoutId());
				mBrushBarView.setBrushBarCellWidth(this.getBrushBarCellWidth());
				mBrushBarView.setAction(BrushAction.ActionEdit);
				
				if (this.getBrushBarHeight() >0)
				{
					mBrushBarView.setHeight(this.getBrushBarHeight());
				}
				mBrushBarView.setDelegate(this);
			}
		}
		return mBrushBarView;
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
	}
	
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
		getSizeAnimView();
		
		setEnabled(getUndoButton(), false);
		setEnabled(getRedoButton(), false);

		if (this.getBrushBarView() != null)
		{
			this.getBrushBarView().loadBrushes();
		}
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
		this.loadImageWithThread();
		
		// 默认为中等尺寸
		BrushSize.SizeType currentSize = this.getDefaultBrushSize();
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
	
	protected void setBrushSize(BrushSize.SizeType mBrushSize)
	{
		mCurrentBrushSize = mBrushSize;
		
		getBrushBarView().setBrushSize(mBrushSize);
		
		if (getSmudgeView() != null)
		{
			getSmudgeView().setBrushSize(mBrushSize);
		}
	}

	/****************************** BrushBarViewDelegate *************************************/
	/**
	 * 选中一个笔刷数据
	 * 
	 * @param view
	 *            笔刷栏视图
	 * @param data
	 *            笔刷数据
	 */
	public void onBrushBarViewSelected(BrushBarView view, BrushData data)
	{
		super.selectBrushCode(data.code);
	}
	
	/**
	 *  点击笔刷粗细按钮，请求切换尺寸
	 */
	public void onBrushSizeButtonClick()
	{
		int currentValue = mCurrentBrushSize.getValue();
		BrushSize.SizeType nextSize = BrushSize.nextBrushSize(mCurrentBrushSize);
	    // 更新图标文字和画布设置
	    setBrushSize(nextSize);
	    
	    int sizeLevel = 12;
	    // 显示切换动画
	    startSizeAnimation(currentValue * sizeLevel, nextSize.getValue() * sizeLevel);
	}
}