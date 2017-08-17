/** 
 * TuSDKCore
 * StickerListHeader.java
 *
 * @author 		Clear
 * @Date 		2015-3-19 下午5:42:27 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 贴纸列表分组头视图
 * 
 * @author Clear
 */
public class StickerListHeader extends TuSdkCellRelativeLayout<StickerGroup>
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_list_header");
	}

	/** 贴纸列表分组头视图动作 */
	public enum StickerListHeaderAction
	{
		/** 删除动作 */
		ActionRemove,
		/** 查看详细动作 */
		ActionDetail,
	}

	/** 贴纸列表分组头视图委托 */
	public interface StickerListHeaderDelegate
	{
		/**
		 * 删除一个贴纸包
		 * 
		 * @param group
		 *            贴纸包
		 * @param action
		 *            分组头视图动作
		 */
		void onStickerListHeaderAction(StickerGroup group, StickerListHeaderAction action);
	}

	public StickerListHeader(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public StickerListHeader(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerListHeader(Context context)
	{
		super(context);
	}

	/** 贴纸列表分组头视图委托 */
	private StickerListHeaderDelegate mDelegate;

	/** 贴纸列表分组头视图委托 */
	public StickerListHeaderDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 贴纸列表分组头视图委托 */
	public void setDelegate(StickerListHeaderDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/** 标题标签 */
	private TextView mTitleLabel;

	/** 删除按钮 */
	private View mRemoveButton;

	/** 标题标签 */
	public TextView getTitleLabel()
	{
		if (mTitleLabel == null)
		{
			mTitleLabel = this.getViewById("lsq_titleLabel");
			if (mTitleLabel != null)
			{
				mTitleLabel.setOnClickListener(mClickListener);
			}
		}
		return mTitleLabel;
	}

	/** 删除按钮 */
	public View getRemoveButton()
	{
		if (mRemoveButton == null)
		{
			mRemoveButton = this.getViewById("lsq_removebutton");
			if (mRemoveButton != null)
			{
				mRemoveButton.setOnClickListener(mClickListener);
			}
		}
		return mRemoveButton;
	}

	@Override
	public void loadView()
	{
		super.loadView();
		this.showViewIn(getRemoveButton(), false);
	}

	@Override
	protected void bindModel()
	{
		if (this.getModel() == null) return;

		this.setTextViewText(this.getTitleLabel(), TuSdkContext.getString(this.getModel().getNameOfLanguage()));

		this.showViewIn(getRemoveButton(), this.getModel().isDownload);
	}

	/** 点击单元格 */
	private OnClickListener mClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getRemoveButton()))
			{
				onReceivedAction(StickerListHeaderAction.ActionRemove);
			}
			else if (equalViewIds(v, getTitleLabel()))
			{
				onReceivedAction(StickerListHeaderAction.ActionDetail);
			}
		}
	};

	/** 删除动作 */
	protected void onReceivedAction(StickerListHeaderAction action)
	{
		if (mDelegate == null) return;
		mDelegate.onStickerListHeaderAction(this.getModel(), action);
	}
}