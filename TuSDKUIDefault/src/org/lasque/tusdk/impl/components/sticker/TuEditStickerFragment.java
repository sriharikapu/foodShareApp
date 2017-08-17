/** 
 * TuSDKCore
 * TuEditStickerFragment.java
 *
 * @author 		Clear
 * @Date 		2014-12-23 下午6:09:16 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.sticker;

import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment.TuStickerChooseFragmentDelegate;
import org.lasque.tusdk.impl.components.sticker.TuStickerOnlineFragment.TuStickerOnlineFragmentDelegate;
import org.lasque.tusdk.impl.components.widget.sticker.StickerBarView;
import org.lasque.tusdk.impl.components.widget.sticker.StickerBarView.StickerBarViewDelegate;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView.StickerViewDelegate;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.sticker.TuEditStickerFragmentBase;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 图片编辑贴纸选择控制器
 * 
 * @author Clear
 */
public class TuEditStickerFragment extends TuEditStickerFragmentBase implements TuStickerOnlineFragmentDelegate, TuStickerChooseFragmentDelegate,
		StickerBarViewDelegate
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_sticker_edit_sticker_fragment");
	}

	/** 图片编辑贴纸选择控制器委托 */
	public interface TuEditStickerFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑贴纸选择控制器
		 * @param result
		 *            处理结果
		 */
		void onTuEditStickerFragmentEdited(TuEditStickerFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑贴纸选择控制器
		 * @param result
		 *            器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditStickerFragmentEditedAsync(TuEditStickerFragment fragment, TuSdkResult result);
	}

	/** 图片编辑贴纸选择控制器委托 */
	private TuEditStickerFragmentDelegate mDelegate;

	/** 图片编辑贴纸选择控制器委托 */
	public TuEditStickerFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑贴纸选择控制器委托 */
	public void setDelegate(TuEditStickerFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑贴纸选择控制器 */
	public TuEditStickerFragment()
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
		this.mDelegate.onTuEditStickerFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditStickerFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/
	/** 贴纸分类列表 */
	private List<StickerCategory> mCategories;

	/** 单元格宽度 */
	private int mGridWidth;
	/** 单元格高度 */
	private int mGridHeight;
	/** 单元格间距 */
	private int mGridPadding = -1;
	/** 单元格布局资源ID */
	private int mGridLayoutId;
	/** 贴纸视图委托 */
	private StickerViewDelegate mStickerViewDelegate;

	/** 单元格宽度 */
	public int getGridWidth()
	{
		return mGridWidth;
	}

	/** 单元格宽度 */
	public void setGridWidth(int mGridWidth)
	{
		this.mGridWidth = mGridWidth;
	}

	/** 单元格高度 */
	public int getGridHeight()
	{
		return mGridHeight;
	}

	/** 单元格高度 */
	public void setGridHeight(int mGridHeight)
	{
		this.mGridHeight = mGridHeight;
	}

	/** 单元格间距 */
	public int getGridPadding()
	{
		return mGridPadding;
	}

	/** 单元格间距 */
	public void setGridPadding(int mGridPadding)
	{
		this.mGridPadding = mGridPadding;
	}

	/**
	 * 单元格布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListGrid}
	 * @return 行视图布局ID (默认: tusdk_impl_component_widget_sticker_list_grid)
	 */
	public int getGridLayoutId()
	{
		return mGridLayoutId;
	}

	/**
	 * 单元格布局资源ID
	 * 
	 * @see #
	 *      {@link org.lasque.tusdk.impl.components.widget.sticker.StickerListGrid}
	 * @param resId
	 *            单元格布局资源ID (默认: tusdk_impl_component_widget_sticker_list_grid)
	 */
	public void setGridLayoutId(int mGridLayoutId)
	{
		this.mGridLayoutId = mGridLayoutId;
	}

	/** 贴纸分类列表 */
	public List<StickerCategory> getCategories()
	{
		return mCategories;
	}

	/** 贴纸分类列表 */
	public void setCategories(List<StickerCategory> mCategories)
	{
		this.mCategories = mCategories;
	}

	/** 贴纸视图委托 */
	public StickerViewDelegate getStickerViewDelegate()
	{
		return mStickerViewDelegate;
	}

	/** 贴纸视图委托 */
	public void setStickerViewDelegate(StickerViewDelegate mStickerViewDelegate)
	{
		this.mStickerViewDelegate = mStickerViewDelegate;
	}

	/************************* view ******************************/
	/** 图片视图 */
	private ImageView mImageView;
	/** 贴纸视图 */
	private StickerView mStickerView;
	/** 裁剪选区视图 */
	private TuMaskRegionView mCutRegionView;
	/** 贴纸栏视图 */
	private StickerBarView mStickerBarView;
	/** 取消按钮 */
	private TuSdkImageButton mCancelButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;
	/** 列表按钮 */
	private TuSdkImageButton mListButton;
	/** 在线按钮 */
	private TuSdkImageButton mOnlineButton;

	/** 图片视图 */
	public ImageView getImageView()
	{
		if (mImageView == null)
		{
			mImageView = this.getViewById("lsq_imageView");
		}
		return mImageView;
	}

	/** 贴纸视图 */
	@Override
	public StickerView getStickerView()
	{
		if (mStickerView == null)
		{
			mStickerView = this.getViewById("lsq_stickerView");
			if (mStickerView != null)
			{
				mStickerView.setDelegate(this.getStickerViewDelegate());
			}
		}
		return mStickerView;
	}

	/** 裁剪选区视图 */
	@Override
	public TuMaskRegionView getCutRegionView()
	{
		if (mCutRegionView == null)
		{
			mCutRegionView = this.getViewById("lsq_cutRegionView");
			if (mCutRegionView != null)
			{
				mCutRegionView.setEdgeMaskColor(TuSdkContext.getColor("lsq_background_editor"));
				mCutRegionView.setEdgeSideColor(0x80FFFFFF);
			}
		}
		return mCutRegionView;
	}

	/** 贴纸栏视图 */
	public StickerBarView getStickerBarView()
	{
		if (mStickerBarView == null)
		{
			mStickerBarView = this.getViewById("lsq_sticker_bar");
			if (mStickerBarView != null)
			{
				mStickerBarView.setGridLayoutId(this.getGridLayoutId());
				mStickerBarView.setGridWidth(this.getGridWidth());
				mStickerBarView.setGridPadding(this.getGridPadding());
				mStickerBarView.setGridHeight(this.getGridHeight());
				mStickerBarView.setDelegate(this);
			}
		}
		return mStickerBarView;
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

	/** 列表按钮 */
	public TuSdkImageButton getListButton()
	{
		if (mListButton == null)
		{
			mListButton = this.getViewById("lsq_bar_listButton");
			if (mListButton != null)
			{
				mListButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mListButton;
	}

	/** 在线按钮 */
	public TuSdkImageButton getOnlineButton()
	{
		if (mOnlineButton == null)
		{
			mOnlineButton = this.getViewById("lsq_bar_onlineButton");
			if (mOnlineButton != null)
			{
				mOnlineButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mOnlineButton;
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
		if (this.equalViewIds(v, this.getCancelButton()))
		{
			this.handleBackButton();
		}
		else if (this.equalViewIds(v, this.getCompleteButton()))
		{
			this.handleCompleteButton();
		}
		else if (this.equalViewIds(v, this.getListButton()))
		{
			this.handleListButton();
		}
		else if (this.equalViewIds(v, this.getOnlineButton()))
		{
			this.handleOnlineButton();
		}
	}

	/********************************** loadView ***********************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		getImageView();
		getStickerView();
		getCutRegionView();
		getCancelButton();
		getCompleteButton();
		getListButton();
		getOnlineButton();

		if (this.getStickerBarView() != null)
		{
			this.getStickerBarView().loadCategories(this.getCategories());
		}
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);
		this.loadImageWithThread();
	}

	/** 异步加载图片完成 */
	@Override
	protected void asyncLoadImageCompleted(Bitmap image)
	{
		super.asyncLoadImageCompleted(image);
		if (image == null) return;

		this.setImageRegionMask(image);
	}

	/** 设置图片选区遮罩 */
	protected void setImageRegionMask(Bitmap image)
	{
		if (image == null) return;

		if (this.getImageView() != null)
		{
			this.getImageView().setImageBitmap(image);
		}

		if (this.getCutRegionView() != null)
		{
			this.getCutRegionView().setRegionRatio(TuSdkSize.create(image).getRatioFloat());
		}
	}

	/********************************** handleListButton ***********************************/
	/** 开启完整贴纸选择列表 */
	protected void handleListButton()
	{
		TuStickerChooseFragment ofragment = new TuStickerChooseFragment();
		ofragment.setDelegate(this);
		this.presentModalNavigationActivity(ofragment, true);
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
		if (fragment != null)
		{
			fragment.dismissActivityWithAnim();
		}
		this.appendStickerItem(data);
	}

	/********************************** handleOnlineButton ***********************************/
	/** 开启在线贴纸视图 */
	protected void handleOnlineButton()
	{
		TuStickerOnlineFragment ofragment = new TuStickerOnlineFragment();
		ofragment.setDelegate(this);
		this.presentModalNavigationActivity(ofragment, true);
	}

	/**
	 * 选中一个贴纸
	 * 
	 * @param fragment
	 *            在线贴纸控制器
	 * @param data
	 *            贴纸数据
	 */
	@Override
	public void onTuStickerOnlineFragmentSelected(TuStickerOnlineFragment fragment, StickerData data)
	{
		if (fragment != null)
		{
			fragment.dismissActivityWithAnim();
		}

		this.appendStickerItem(data);
	}

	/****************************** StickerBarViewDelegate *************************************/
	/**
	 * 选中一个贴纸数据
	 * 
	 * @param view
	 *            贴纸栏视图
	 * @param data
	 *            贴纸数据
	 */
	@Override
	public void onStickerBarViewSelected(StickerBarView view, StickerData data)
	{
		this.appendStickerItem(data);
	}

	/** 选择一个空分类 */
	@Override
	public void onStickerBarViewEmpty(StickerBarView view, StickerCategory cate)
	{
		this.handleOnlineButton();
	}
}