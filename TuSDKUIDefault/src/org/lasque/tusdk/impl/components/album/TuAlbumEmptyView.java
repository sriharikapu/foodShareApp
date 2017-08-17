/** 
 * TuSDKCore
 * TuAlbumEmptyView.java
 *
 * @author 		Clear
 * @Date 		2014-12-12 下午8:32:28 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 相册空视图
 * 
 * @author Clear
 */
public class TuAlbumEmptyView extends TuSdkRelativeLayout
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_empty_view");
	}

	public TuAlbumEmptyView(Context context)
	{
		super(context);
	}

	public TuAlbumEmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuAlbumEmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
}