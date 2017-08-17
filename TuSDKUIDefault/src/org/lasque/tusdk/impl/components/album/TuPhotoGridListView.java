/** 
 * TuSDKCore
 * TuPhotoMultipleList.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 上午11:17:42 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.util.ArrayList;

import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper.PhotoSortDescriptor;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell.TuPhotoCellCheckedDelegate;
import org.lasque.tusdk.impl.components.widget.view.TuSdkGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 相册组照片列表控制器，使用网格化布局显示照片
 * 
 * @author Clear
 */
public class TuPhotoGridListView extends TuSdkGridView<ImageSqlInfo, TuPhotoGridListViewCell> implements TuPhotoCellCheckedDelegate
{
	
	public TuPhotoGridListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuPhotoGridListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoGridListView(Context context)
	{
		super(context);
	}
	
	/**
	 * 图片选中事件委托
	 * 
	 * @author Clear
	 */
	public interface TuPhotoListCheckedDelegate
	{
		/**
		 * 选中图片
		 * 
		 * @param data
		 *          选中图片信息
		 * @param position
		 *          选中图片所在相册位置 
		 */
		void onPhotoListChecked(ImageSqlInfo data, int position);
	}
	
	/**
	 * 图片选中事件委托
	 */
	private TuPhotoListCheckedDelegate mDelegate;
	
	/**
	 * 图片选中事件委托
	 * 
	 * @param delegate
	 */
	public void setDelegate(TuPhotoListCheckedDelegate delegate)
	{
		this.mDelegate = delegate;
	}

	/**
	 * 图片选中事件委托
	 * 
	 * @return the delegate
	 */
	public TuPhotoListCheckedDelegate getDelegate()
	{
		return this.mDelegate;
	}

	/**
	 * 系统相册数据库信息
	 */
	private AlbumSqlInfo mAlbumInfo;
	
	/**
	 * 系统相册数据库信息
	 * 
	 * @return the mAlbumInfo
	 */
	public AlbumSqlInfo getAlbumInfo()
	{
		return mAlbumInfo;
	}

	/**
	 * 系统相册数据库信息
	 * 
	 * @param mAlbumInfo
	 *            the mAlbumInfo to set
	 */
	public void setAlbumInfo(AlbumSqlInfo mAlbumInfo)
	{
		this.mAlbumInfo = mAlbumInfo;
		
		ArrayList<ImageSqlInfo> mPhotos;
		
		if (mAlbumInfo != null) {
			mPhotos = ImageSqlHelper.getPhotoList(getContext(), mAlbumInfo.id,this.getPhotosSortDescriptor());
		}
		else
		{
			mPhotos = new ArrayList<ImageSqlInfo>();
		}
		
		// 是否添加相机图标
		if(this.isDisplayCameraCell())
		{
			ImageSqlInfo cameraInfo = new ImageSqlInfo();
			cameraInfo.id = TuPhotoGridListViewCell.CAMERA_PLACEHOLDER;

			mPhotos.add(0, cameraInfo);
		}
		
		this.setModeList(mPhotos);
		
		this.reloadData();
	}
	
	// 行视图宽度
	private int mPhotoGridWidth;

	/**
	 * 是否显示相机图标 (默认: 开启)
	 */
	private boolean mDisplayCameraCell = true;
	
	/**
	 * 相册照片排序方式 默认按照修改时间排序 Media.DATE_MODIFIED
	 */
	private PhotoSortDescriptor mPhotosSortDescriptor;

	
	/**
	 * 行视图宽度
	 * 
	 * @return the mPhotoGridWidth
	 */
	public int getPhotoGridWidth()
	{
		return mPhotoGridWidth;
	}

	/**
	 * 行视图宽度
	 * 
	 * @param mPhotoGridWidth
	 *            the mPhotoGridWidth to set
	 */
	public void setPhotoGridWidth(int mPhotoGridWidth)
	{
		this.mPhotoGridWidth = mPhotoGridWidth;
	}	

	/**
	 * 是否显示相机图标 (默认：开启)
	 * 
	 * @param mDisplayCameraCell
	 */
	public void setDisplayCameraCell(boolean mDisplayCameraCell) 
	{
		this.mDisplayCameraCell = mDisplayCameraCell;
	}

	/**
	 * 是否显示相机图标 (默认：开启)
	 * 
	 * @return
	 */
	public boolean isDisplayCameraCell() 
	{
		return this.mDisplayCameraCell;
	}
	
	/**
	 * 设置相册照片排序方式 默认按照修改时间排序 Media.DATE_MODIFIED
	 * @param mPhotosSortDescriptor
	 */
	public void setPhotosSortDescriptor(PhotoSortDescriptor photosSortDescriptor) 
	{
		this.mPhotosSortDescriptor = photosSortDescriptor;
	}

	/**
	 * 获取相册照片排序方式 默认按照修改时间排序 Media.DATE_MODIFIED
	 * @return
	 */
	public PhotoSortDescriptor getPhotosSortDescriptor() 
	{
		if(mPhotosSortDescriptor == null)
			mPhotosSortDescriptor = PhotoSortDescriptor.Date_Modified;
		
		return mPhotosSortDescriptor;
	}
	
	
	
	@Override
	public void loadView()
	{
		super.loadView();
		
		this.setItemAnimator(null);
		
		this.setHasFixedSize(true);
	}
	
	@Override
	protected void onViewCreated(TuPhotoGridListViewCell view, ViewGroup parent,
			int viewType)
	{
		if (this.getPhotoGridWidth() > 0)
		{
			view.setWidth(this.getPhotoGridWidth());
			view.setHeight(this.getPhotoGridWidth());
		}
	}

	@Override
	protected void onViewBinded(TuPhotoGridListViewCell view, int position)
	{
		view.setPosition(position);
		view.setDelegate(this);
	}

	/**
	 * 图片选中事件委托
	 */
	@Override
	public void onPhotoCellChecked(ImageSqlInfo data, int position) 
	{
    	if(getDelegate() == null) return;
    	
    	getDelegate().onPhotoListChecked(data, position);
	}
}
