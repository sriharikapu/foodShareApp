/** 
 * TuSDKCore
 * TuPhotoListGrid.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 下午7:48:17 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.modules.components.album.TuPhotoListGridBase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 相片列表单元格视图
 * 
 * @author Clear
 */
public class TuPhotoListGrid extends TuPhotoListGridBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_photo_list_grid");
	}

	public TuPhotoListGrid(Context context)
	{
		super(context);
	}

	public TuPhotoListGrid(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoListGrid(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 图片视图 */
	private ImageView mPosterView;

	/** 图片视图 */
	@Override
	public ImageView getPosterView()
	{
		if (mPosterView == null)
		{
			mPosterView = this.getViewById("lsq_posterView");
		}
		return mPosterView;
	}
}