/** 
 * TuSDKCore
 * StickerListEmptyView.java
 *
 * @author 		Clear
 * @Date 		2015-3-19 下午6:35:31 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 贴纸列表空视图
 * 
 * @author Clear
 */
public class StickerListEmptyView extends TuSdkRelativeLayout
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_list_empty_view");
	}

	public StickerListEmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public StickerListEmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerListEmptyView(Context context)
	{
		super(context);
	}

	/** 更多按钮 */
	private View mMoreButton;

	/** 更多按钮 */
	public View getMoreButton()
	{
		if (mMoreButton == null)
		{
			mMoreButton = this.getViewById("lsq_empty_more_button");
		}
		return mMoreButton;
	}
}