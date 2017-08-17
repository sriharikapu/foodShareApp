/** 
 * TuSDKCore
 * TuPhotoListCell.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 下午7:44:49 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.view.widget.listview.TuListGridCellView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 相片列表行视图
 * 
 * @author Clear
 */
public class TuPhotoListCell extends TuListGridCellView<ImageSqlInfo, TuPhotoListGrid>
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_photo_list_cell");
	}

	public TuPhotoListCell(Context context)
	{
		super(context);
	}

	public TuPhotoListCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoListCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
}