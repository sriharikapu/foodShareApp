/** 
 * TuSDKCore
 * StickerListCell.java
 *
 * @author 		Clear
 * @Date 		2014-12-30 下午6:22:25 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.sticker;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.impl.view.widget.listview.TuListGridCellView;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 贴纸列表行视图
 * 
 * @author Clear
 */
public class StickerListCell extends TuListGridCellView<StickerData, StickerListGrid>
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_list_cell");
	}

	public StickerListCell(Context context)
	{
		super(context);
	}

	public StickerListCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public StickerListCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
}