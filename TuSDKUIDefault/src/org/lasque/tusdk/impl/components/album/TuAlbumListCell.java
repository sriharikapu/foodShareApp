/** 
 * TuSDKCore
 * TuAlbumListCell.java
 *
 * @author 		Clear
 * @Date 		2014-11-26 下午5:11:02 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.modules.components.album.TuAlbumListCellBase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 系统相册列表行
 * 
 * @author Clear
 */
public class TuAlbumListCell extends TuAlbumListCellBase
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_list_cell");
	}

	public TuAlbumListCell(Context context)
	{
		super(context);
	}

	public TuAlbumListCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuAlbumListCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 封面视图 */
	private ImageView mPosterView;

	/** 标题视图 */
	private TextView mTitleView;

	/** 封面视图 */
	@Override
	public ImageView getPosterView()
	{
		if (mPosterView == null)
		{
			mPosterView = this.getViewById("lsq_posterView");
		}
		return mPosterView;
	}

	/** 标题视图 */
	public TextView getTitleView()
	{
		if (mTitleView == null)
		{
			mTitleView = this.getViewById("lsq_titleView");
		}
		return mTitleView;
	}

	@Override
	protected void bindModel()
	{
		super.bindModel();
		AlbumSqlInfo mode = this.getModel();
		if (mode == null) return;

		this.setTitle(mode);
	}

	/** 设置标题 */
	private void setTitle(AlbumSqlInfo mode)
	{
		TextView view = this.getTitleView();
		if (view == null) return;

		view.setText(String.format("%s (%s)", mode.title, mode.total));
	}
}