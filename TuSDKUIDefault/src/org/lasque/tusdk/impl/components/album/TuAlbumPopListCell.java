/** 
 * TuSDKCore
 * TuAlbumMultipleListCell.java
 *
 * @author 		Clear
 * @Date 		2014-11-26 下午5:11:02 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 系统相册列表行
 * 
 * @author Clear
 */
public class TuAlbumPopListCell extends TuSdkCellRelativeLayout<AlbumSqlInfo>
{
	/**
	 * 布局ID
	 * 
	 * @return
	 */
	public static int getLayoutId()
	{
		return TuSdkContext
				.getLayoutResId("tusdk_impl_component_album_pop_list_cell");
	}

	public TuAlbumPopListCell(Context context)
	{
		super(context);
	}

	public TuAlbumPopListCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuAlbumPopListCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 封面视图
	 */
	private ImageView mPosterView;

	/**
	 * 标题视图
	 */
	private TextView mTitleView;

	/**
	 * 封面视图
	 * 
	 * @return the posterView
	 */
	public ImageView getPosterView()
	{
		if (mPosterView == null)
		{
			mPosterView = this.getViewById("lsq_posterView");
		}
		return mPosterView;
	}

	/**
	 * 标题视图
	 * 
	 * @return the titleView
	 */
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
		AlbumSqlInfo mode = this.getModel();
		if (mode == null) return;

		this.setTitle(mode);
		this.setImage(mode);
	}
	
	public void setHeight(int height)
	{
		super.setHeight(height);
		
		ViewGroup.LayoutParams p = getPosterView().getLayoutParams();
		p.width = height - 5;
		p.height = height - 5;
		
		getPosterView().setLayoutParams(p);
	}

	/**
	 * 设置标题
	 * 
	 * @param mode
	 */
	private void setTitle(AlbumSqlInfo mode)
	{
		TextView view = this.getTitleView();
		if (view == null) return;

		view.setText(String.format("%s (%s)", mode.title, mode.total));
	}

	/**
	 * 设置图片
	 * 
	 * @param mode
	 */
	private void setImage(AlbumSqlInfo mode)
	{
		ImageView view = this.getPosterView();
		if (view == null) return;
		if (mode == null)
		{
			view.setImageBitmap(null);
			return;
		}

		AlbumTaskManager.shared.loadThumbMiniImage(view, mode.cover);
	}

	@Override
	public void viewNeedRest()
	{
		AlbumTaskManager.shared.cancelLoadImage(this.getPosterView());
		this.setImage(null);
		super.viewNeedRest();
	}

	@Override
	public void viewWillDestory()
	{
		this.viewNeedRest();
		super.viewWillDestory();
	}
}
