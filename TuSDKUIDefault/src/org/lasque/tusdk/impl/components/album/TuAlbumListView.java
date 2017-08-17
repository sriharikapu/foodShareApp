/** 
 * TuSDKCore
 * TuAblumListView.java
 *
 * @author 		Clear
 * @Date 		2014-11-26 下午5:09:44 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.impl.view.widget.listview.TuArrayListView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 相册列表视图
 * 
 * @author Clear
 */
public class TuAlbumListView extends TuArrayListView<AlbumSqlInfo, TuAlbumListCell>
{
	public TuAlbumListView(Context context)
	{
		super(context);
	}

	public TuAlbumListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuAlbumListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onArrayListViewCreated(TuAlbumListCell view, TuSdkIndexPath indexPath)
	{

	}

	@Override
	protected void onArrayListViewBinded(TuAlbumListCell view, TuSdkIndexPath indexPath)
	{

	}
}