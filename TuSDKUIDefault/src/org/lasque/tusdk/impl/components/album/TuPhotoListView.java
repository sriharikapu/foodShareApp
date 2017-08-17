/** 
 * TuSDKCore
 * TuPhotoListView.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 下午7:34:21 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.impl.view.widget.listview.TuGroupListView;
import org.lasque.tusdk.impl.view.widget.listview.TuListGridCellView.TuListGridCellViewDelegate;
import org.lasque.tusdk.modules.components.album.TuPhotoListDataSource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 相片列表视图
 * 
 * @author Clear
 */
public class TuPhotoListView extends TuGroupListView<List<ImageSqlInfo>, TuPhotoListCell, TuSdkDate, TuPhotoListHeader>
{

	/** 单元格点击委托 */
	public interface TuPhotoListGridDelegate extends TuListGridCellViewDelegate<ImageSqlInfo, TuPhotoListGrid>
	{

	}

	public TuPhotoListView(Context context)
	{
		super(context);
	}

	public TuPhotoListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 单元格点击 */
	private TuPhotoListGridDelegate mGridDelegate;

	/** 单元格点击委托 */
	public TuPhotoListGridDelegate getGridDelegate()
	{
		return mGridDelegate;
	}

	/** 单元格点击委托 */
	public void setGridDelegate(TuPhotoListGridDelegate mGridDelegate)
	{
		this.mGridDelegate = mGridDelegate;
	}

	@Override
	protected void onGroupListViewCreated(TuPhotoListCell view, TuSdkIndexPath indexPath)
	{
		view.setGridDelegate(mGridDelegate);
	}

	@Override
	protected void onGroupListHeaderCreated(TuPhotoListHeader view, TuSdkIndexPath indexPath)
	{

	}

	/** 加载相片列表 */
	public void loadPhotos(AlbumSqlInfo mAlbumInfo)
	{
		this.setDataSource(new PhotoListDataSource(this.getContext(), mAlbumInfo, true));
	}

	/** 相片列表数据源 */
	private class PhotoListDataSource extends TuPhotoListDataSource
	{
		/***
		 * 数组列表数据源
		 * 
		 * @param context
		 * @param mAlbumInfo
		 *            系统相册数据库信息
		 * @param desc
		 *            是否为倒序
		 */
		public PhotoListDataSource(Context context, AlbumSqlInfo mAlbumInfo, boolean desc)
		{
			super(context, mAlbumInfo, desc);
		}

		/** 视图创建 */
		@SuppressWarnings("unchecked")
		@Override
		public void onViewBinded(TuSdkIndexPath indexPath, View view)
		{
			if (!(view instanceof TuSdkCellViewInterface)) return;

			Object mode = this.getItem(indexPath);

			if (view instanceof TuPhotoListCell)
			{
				((TuPhotoListCell) view).setModel((ArrayList<ImageSqlInfo>) mode);
			}
			else if (view instanceof TuPhotoListHeader)
			{
				((TuPhotoListHeader) view).setModel((TuSdkDate) mode);
			}
		}
	}
}