/** 
 * TuSDKCore
 * TuEditEntryFragment.java
 *
 * @author 		Clear
 * @Date 		2014-12-23 下午5:01:43 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.edit;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView.StickerViewDelegate;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.edit.TuEditActionType;
import org.lasque.tusdk.modules.components.edit.TuEditEntryFragmentBase;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 图片编辑入口控制器
 * 
 * @author Clear
 */
public class TuEditEntryFragment extends TuEditEntryFragmentBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_entry_fragment");
	}

	/** 图片编辑入口控制器委托 */
	public interface TuEditEntryFragmentDelegate extends TuSdkComponentErrorListener
	{
		/**
		 * 图片编辑完成
		 * 
		 * @param fragment
		 *            图片编辑入口控制器
		 * @param result
		 *            旋图片编辑入口控制器处理结果
		 */
		void onTuEditEntryFragmentEdited(TuEditEntryFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑完成 (异步方法)
		 * 
		 * @param fragment
		 *            图片编辑入口控制器
		 * @param result
		 *            旋图片编辑入口控制器处理结果
		 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
		 */
		boolean onTuEditEntryFragmentEditedAsync(TuEditEntryFragment fragment, TuSdkResult result);

		/**
		 * 图片编辑动作
		 * 
		 * @param fragment
		 *            图片编辑入口控制器
		 * @param actionType
		 *            图片编辑动作类型
		 */
		void onTuEditEntryFragmentAction(TuEditEntryFragment fragment, TuEditActionType actionType);
	}

	/** 图片编辑入口控制器委托 */
	private TuEditEntryFragmentDelegate mDelegate;

	/** 图片编辑入口控制器委托 */
	public TuEditEntryFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 图片编辑入口控制器委托 */
	public void setDelegate(TuEditEntryFragmentDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
		this.setErrorListener(mDelegate);
	}

	/** 图片编辑入口控制器 */
	public TuEditEntryFragment()
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
		this.mDelegate.onTuEditEntryFragmentEdited(this, result);
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
		return this.mDelegate.onTuEditEntryFragmentEditedAsync(this, result);
	}

	/********************************** Config ***********************************/

	/** 开启裁剪旋转功能 */
	private boolean mEnableCuter;
	/** 开启滤镜功能 */
	private boolean mEnableFilter;
	/** 开启贴纸功能 */
	private boolean mEnableSticker;
	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	private int mLimitSideSize;
	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	private boolean mLimitForScreen;
	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	private int mRatioType;
	/** 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)  */
	private int[] mRatioTypeList;
	/** 贴纸视图委托 */
	private StickerViewDelegate mStickerViewDelegate;

	/** 开启裁剪旋转功能 */
	public boolean isEnableCuter()
	{
		return mEnableCuter;
	}

	/** 开启裁剪旋转功能 */
	public void setEnableCuter(boolean mEnableCuter)
	{
		this.mEnableCuter = mEnableCuter;
	}

	/** 开启滤镜功能 */
	public boolean isEnableFilter()
	{
		return mEnableFilter;
	}

	/** 开启滤镜功能 */
	public void setEnableFilter(boolean mEnableFilter)
	{
		this.mEnableFilter = mEnableFilter;
	}

	/** 开启贴纸功能 */
	public boolean isEnableSticker()
	{
		return mEnableSticker;
	}

	/** 开启贴纸功能 */
	public void setEnableSticker(boolean mEnableSticker)
	{
		this.mEnableSticker = mEnableSticker;
	}

	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	@Override
	public int getLimitSideSize()
	{
		return mLimitSideSize;
	}

	/** 最大输出图片边长 (默认:0, 不限制图片宽高) */
	public void setLimitSideSize(int mLimitSideSize)
	{
		this.mLimitSideSize = mLimitSideSize;
	}

	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	@Override
	public boolean isLimitForScreen()
	{
		return mLimitForScreen;
	}

	/** 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen) */
	public void setLimitForScreen(boolean mLimitForScreen)
	{
		this.mLimitForScreen = mLimitForScreen;
	}

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
	public int getRatioType()
	{
		return mRatioType;
	}

	/** 裁剪比例类型 (默认:RatioType.ratio_all) */
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
			list = RatioType.ratioTypes;
		}
		
		return list;
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

	/*************************** view *******************************/
	/** 图片包装视图 */
	private RelativeLayout mImageWrapView;
	/** 贴纸视图 */
	private StickerView mStickerView;
	/** 裁剪选取视图 */
	private TuMaskRegionView mCutRegionView;
	/** 后退按钮 */
	private TuSdkImageButton mBackButton;
	/** 完成按钮 */
	private TuSdkImageButton mCompleteButton;
	/** 裁切入口按钮 */
	private TuSdkTextButton mCutButton;
	/** 滤镜入口按钮 */
	private TuSdkTextButton mFilterButton;
	/** 贴纸入口按钮 */
	private TuSdkTextButton mStickerButton;

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

	/** 后退按钮 */
	public TuSdkImageButton getBackButton()
	{
		if (mBackButton == null)
		{
			mBackButton = this.getViewById("lsq_cancalButton");
			if (mBackButton != null)
			{
				mBackButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mBackButton;
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
		}
		return mCutRegionView;
	}

	/** 完成按钮 */
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

	/** 裁切入口按钮 */
	public TuSdkTextButton getCutButton()
	{
		if (mCutButton == null)
		{
			mCutButton = this.getViewById("lsq_cutButton");
			if (mCutButton != null)
			{
				mCutButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mCutButton;
	}

	/** 滤镜入口按钮 */
	public TuSdkTextButton getFilterButton()
	{
		if (mFilterButton == null)
		{
			mFilterButton = this.getViewById("lsq_filterButton");
			if (mFilterButton != null)
			{
				mFilterButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mFilterButton;
	}

	/** 贴纸入口按钮 */
	public TuSdkTextButton getStickerButton()
	{
		if (mStickerButton == null)
		{
			mStickerButton = this.getViewById("lsq_stickerButton");
			if (mStickerButton != null)
			{
				mStickerButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mStickerButton;
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
		if (this.equalViewIds(v, this.getBackButton()))
		{
			this.handleBackButton();
		}
		else if (this.equalViewIds(v, this.getCompleteButton()))
		{
			this.handleCompleteButton();
		}
		else if (this.equalViewIds(v, this.getCutButton()))
		{
			this.handleAction(TuEditActionType.TypeCuter);
		}
		else if (this.equalViewIds(v, this.getFilterButton()))
		{
			this.handleAction(TuEditActionType.TypeFilter);
		}
		else if (this.equalViewIds(v, this.getStickerButton()))
		{
			this.handleAction(TuEditActionType.TypeSticker);
		}
	}

	/************************** loadView *****************************/

	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);

		// 贴纸视图
		getStickerView();
		// 裁剪选区视图
		getCutRegionView();
		// 后退按钮
		getBackButton();
		// 完成按钮
		getCompleteButton();
		// 裁切入口按钮
		this.showView(getCutButton(), this.isEnableCuter());
		// 滤镜入口按钮
		this.showView(getFilterButton(), this.isEnableFilter());
		// 贴纸入口按钮
		this.showView(getStickerButton(), this.isEnableSticker());
	}
	
	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		super.navigatorBarLoaded(navigatorBar);
		this.setTitle(this.getResString("lsq_edit_title"));
		
		this.setNavLeftButton(this.getResString("lsq_nav_back"));
	}
	
	/**
	 * 取消按钮
	 */
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(null);
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

	/** 异步加载图片 */
	protected Bitmap asyncLoadImage()
	{
		return this.getCuterImage();
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
			this.getImageView().setImage(image);
		}

		if (this.getCutRegionView() != null)
		{
			this.getCutRegionView().setRegionRatio(TuSdkSize.create(image).getRatioFloat());
		}
	}

	/** 后退按钮 */
	private void handleBackButton()
	{
		this.navigatorBarBackAction(null);
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
		this.mDelegate.onTuEditEntryFragmentAction(this, actionType);
	}

	/********************************** inner Config ***********************************/
	/** 裁切参数 */
	private TuSdkResult mCuterResult;

	/** 获取裁剪结果 */
	@Override
	public TuSdkResult getCuterResult()
	{
		return mCuterResult;
	}

	/** 设置裁剪结果 */
	public void setCuterResult(TuSdkResult result)
	{
		this.mCuterResult = result;
		if (this.mCuterResult == null) return;

		Bitmap image = this.getCuterImage();

		// 设置图片选区遮罩
		this.setImageRegionMask(image);
	}
}