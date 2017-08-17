/** 
 * TuSDKGee
 * BrushBarView.java
 *
 * @author 		Yanlin
 * @Date 		2015-11-13 下午18:25:28 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.smudge;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView.TuSdkTableViewItemClickDelegate;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.modules.view.widget.smudge.BrushBarItemCellBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushBarViewBase;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdk.modules.view.widget.smudge.BrushTableViewInterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 笔刷栏视图
 * 
 * @author Yanlin
 */
public class BrushBarView extends BrushBarViewBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_brush_bar_view");
	}

	/** 笔刷栏视图委托 */
	public interface BrushBarViewDelegate
	{
		/**
		 * 选中一个笔刷数据
		 * 
		 * @param view
		 *            笔刷栏视图
		 * @param data
		 *            笔刷数据
		 */
		void onBrushBarViewSelected(BrushBarView view, BrushData data);  
		
		/**
		 *  点击笔刷粗细按钮，请求切换尺寸
		 */
		void onBrushSizeButtonClick();
	}

	public BrushBarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public BrushBarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BrushBarView(Context context)
	{
		super(context);
	}

	/** 笔刷栏视图委托 */
	private BrushBarViewDelegate mDelegate;

	/** 笔刷栏视图委托 */
	public BrushBarViewDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 笔刷栏视图委托 */
	public void setDelegate(BrushBarViewDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/************************* view ******************************/
	/** 尺寸配置包装视图 */
	private RelativeLayout mConfigWrap;
	/** 笔刷尺寸文本 */
	private TextView mBrushSizeLabel;
	/** 笔刷尺寸图标 */
	private TuSdkImageButton mBrushSizeImage;
	/** 列表视图 */
	private BrushTableViewInterface mTableView;
	/** 行视图宽度 */
	private int mBrushBarCellWidth;
	/** 笔刷列表行视图布局资源ID */
	private int mBrushBarCellLayoutId;

	/** 列表视图 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends View & BrushTableViewInterface> T getTableView()
	{
		if (mTableView == null)
		{
			View view = this.getViewById("lsq_brush_list_view");
			if (view == null || !(view instanceof BrushTableViewInterface)) return null;

			mTableView = (BrushTableViewInterface) view;
			if (mTableView != null)
			{
				mTableView.setItemClickDelegate(mTuSdkTableViewItemClickDelegate);
				mTableView.setCellLayoutId(this.getBrushBarCellLayoutId());
				
				if (this.getBrushBarCellWidth() > 0)
				{
					mTableView.setCellWidth(this.getBrushBarCellWidth());
				}
				mTableView.reloadData();
			}
		}
		return (T) mTableView;
	}

	/** 尺寸配置包装视图 */
	public RelativeLayout getConfigWrap()
	{
		if (mConfigWrap == null)
		{
			mConfigWrap = this.getViewById("lsq_configWrap");
		}
		return mConfigWrap;
	}
	
	/** 笔刷尺寸文本 */
	public TextView getBrushSizeLabel()
	{
		if (mBrushSizeLabel == null)
		{
			mBrushSizeLabel = this.getViewById("lsq_size_title");
		}
		return mBrushSizeLabel;
	}
	
	/** 笔刷尺寸图标 */
	public TuSdkImageButton getBrushSizeImage()
	{
		if (mBrushSizeImage == null)
		{
			mBrushSizeImage = this.getViewById("lsq_size_image");
			mBrushSizeImage.setOnClickListener(mButtonClickListener);
		}
		return mBrushSizeImage;
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
		if (this.equalViewIds(v, this.getBrushSizeImage()))
		{
			if (this.getDelegate() != null)
			{
				this.getDelegate().onBrushSizeButtonClick();
			}
		}
	}

	/************************* config ******************************/
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

	/** 笔刷数据点击事件 */
	private TuSdkTableViewItemClickDelegate<BrushData, BrushBarItemCellBase> mTuSdkTableViewItemClickDelegate = new TuSdkTableViewItemClickDelegate<BrushData, BrushBarItemCellBase>()
	{
		@Override
		public void onTableViewItemClick(BrushData itemData, BrushBarItemCellBase itemView, int position)
		{
			onSelectedBrush(itemData, itemView, position);
		}
	};

	/** 选中一个笔刷数据 */
	private void onSelectedBrush(BrushData itemData, BrushBarItemCellBase itemView, int position)
	{
		this.notifySelectedBrush(itemData);
		
		super.selectBrush(itemData, itemView, position);
	}
	
	/** 加载视图 */
	public void loadView()
	{
		super.loadView();

		// 列表视图
		getTableView();
		// 列表包装视图
		getConfigWrap();
		// 笔刷尺寸文本
		getBrushSizeLabel();
		// 笔刷尺寸图标
		getBrushSizeImage();
	}

	/************************* brush data ******************************/
	/** 笔刷分类列表 */
	@Override
	public void loadBrushes()
	{
		super.loadBrushes();
	}
	
	@Override
	public void viewDidLoad() 
	{
		super.viewDidLoad();
		
		if(this.getTableView() != null)
		{
			mTableView.setCellLayoutId(this.getBrushBarCellLayoutId());
		}
	}
	
	/**
	 * 显示当前笔刷粗细
	 *
	 * @param mBrushSize 
	 *            笔刷粗细
	 */
	public void setBrushSize(BrushSize.SizeType mBrushSize)
	{
	    String brushName = BrushSize.nameForSize(mBrushSize);
	    
	    String key = String.format("lsq_brush_size_%s", brushName);
	    getBrushSizeLabel().setText(TuSdkContext.getString(key));
	    
	    key = String.format("lsq_style_default_edit_brush_%s", brushName);
	    getBrushSizeImage().setImageResource(TuSdkContext.getDrawableResId(key));
	}

	/** 刷新笔刷列表 */
	@Override
	protected void refreshBrushDatas()
	{
		if (this.getTableView() == null) return;
	}
	
	/** 刷新笔刷列表 */
	@Override
	protected void notifySelectedBrush(BrushData brush)
	{
		if (brush == null || this.getDelegate() == null) return;
	    this.getDelegate().onBrushBarViewSelected(this, brush); 
	}
}