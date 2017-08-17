/** 
 * TuSDKCore
 * TuAlbumMultipleListOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午1:13:36 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper.PhotoSortDescriptor;
import org.lasque.tusdk.modules.components.TuSdkComponentOption;

/**
 * 系统相册控制器配置选项
 * 
 * @author Clear
 */
public class TuAlbumMultipleListOption extends TuSdkComponentOption
{
	/**
	 * 系统相册列表控制类
	 * 
	 * @param mComponentClazz
	 *            系统相册列表控制类 (默认: TuAlbumListFragment，如需自定义请继承自
	 *            TuAlbumListFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuAlbumMultipleListFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumMultipleListFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_album_multiple_list_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuAlbumMultipleListFragment.getLayoutId();
	}

	/**
	 * 视图布局资源ID
	 */
	private int mPreviewViewLayoutId = 0;

	/**
	 * 控制器类型
	 */
	private Class<?> mPreviewClazz;

	/**
	 * 多选相册预览界面控制器
	 * 
	 * @return
	 */
	protected Class<?> getDefaultPreviewClazz()
	{
		return TuAlbumMultiplePreviewFragment.class;
	}

	/**
	 * 多选相册预览界面资源ID
	 * 
	 * @return
	 */
	protected int getDefaultPreviewLayoutId()
	{
		return TuAlbumMultiplePreviewFragment.getLayoutId();
	}
	
	/**
	 * 控制器类型
	 * 
	 * @return 控制器类型
	 */
	public Class<?> getPreviewClazz()
	{
		if (mPreviewClazz == null)
		{
			mPreviewClazz = getDefaultPreviewClazz();
		}
		return mPreviewClazz;
	}

	/**
	 * 控制器类型
	 * 
	 * @param mComponentClazz
	 *            控制器类型
	 */
	public void setPreviewClazz(Class<?> previewClazz)
	{
		if (previewClazz != null
				&& this.getPreviewClazz() != null
				&& this.getDefaultPreviewClazz().isAssignableFrom(
						previewClazz))
		{
			this.mPreviewClazz = previewClazz;
		}
	}

	/**
	 * 设置预览视图布局资源 ID
	 * 
	 * @param previewLayoutId
	 *            预览视图布局资源ID
	 */
	public void setPreviewLayoutId(int previewLayoutId)
	{
		this.mPreviewViewLayoutId = previewLayoutId;
	}

	/**
	 * 预览视图布局资源ID
	 * 
	 * @return 预览视图布局资源ID
	 */
	public int getPreviewLayoutId()
	{
		if (mPreviewViewLayoutId == 0)
		{
			mPreviewViewLayoutId = this.getDefaultPreviewLayoutId();
		}
		return this.mPreviewViewLayoutId;
	}

	/********************************** Config ***********************************/
	/**
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 */
	private String mSkipAlbumName;

	/**
	 * 行视图布局ID
	 */
	private int mCellLayoutId;
	
	/**
	 * 照片行视图布局ID
	 */
	private int mPhotoCellLayoutId;
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 */
	private int mMaxSelection = 3;
	
	/**
	 *  选择照片的尺寸限制 默认：CGSize(8000,8000)
	 */
	private TuSdkSize mMaxSelectionImageSize;
	
	/**
	 * 允许在多个相册中选择 (默认: 开启)
	 */
	private boolean mEnableShareSelection = true;
	
	/**
	 * 是否显示相机图标 (默认: 开启)
	 */
	private boolean mDisplayCameraCell = true;
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 */
	private int mPhotoColumnNumber;

	/**
	 * 弹出相册列表的高度，默认是64
	 */
	private int mPopListRowHeight = 64;
	
	/**
	 * 相册照片排序方式 默认按照修改时间排序 Media.DATE_MODIFIED
	 */
	private PhotoSortDescriptor mPhotosSortDescriptor;

	/**
	 * 设置相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @param resId
	 *            行视图布局ID (默认: tusdk_impl_component_album_pop_list_cell)
	 */
	public void setAlbumCellLayoutId(int resId)
	{
		mCellLayoutId = resId;
	}

	/**
	 * 相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_pop_list_cell)
	 */
	public int getAlbumCellLayoutId()
	{
		if (mCellLayoutId == 0)
		{
			mCellLayoutId = TuAlbumPopListCell.getLayoutId();
		}
		return mCellLayoutId;
	}
	
	/**
	 * 设置照片行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @param resId
	 *            照片行视图布局ID (默认: tusdk_impl_component_album_photo_grid_list_cell)
	 */
	public void setPhotoCellLayoutId(int resId)
	{
		mPhotoCellLayoutId = resId;
	}

	/**
	 * 照片行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_photo_grid_list_cell)
	 */
	public int getPhotoCellLayoutId()
	{
		if (mPhotoCellLayoutId == 0)
		{
			mPhotoCellLayoutId = TuPhotoGridListViewCell.getLayoutId();
		}
		return mPhotoCellLayoutId;
	}
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 */
	public void setMaxSelection(int mMaxSelection)
	{
		this.mMaxSelection = mMaxSelection;
	}
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 * 
	 * @return the mMaxSelection
	 */
	public int getMaxSelection()
	{
		return this.mMaxSelection;
	}
	
	/**
	 * 允许在多个相册中选择 (默认: 开启)
	 * 
	 * @param mEnableShareSelection
	 *            true or false
	 */
	public void setEnableShareSelection(boolean mEnableShareSelection)
	{
		this.mEnableShareSelection = mEnableShareSelection;
	}
	
	/**
	 *  允许在多个相册中选择 (默认: 开启)
	 *  
	 *  @return the mMaxSelection
	 */
	public boolean isEnableShareSelection()
	{
		return this.mEnableShareSelection;
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
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 * 
	 * @return the skipAlbumName
	 */
	public String getSkipAlbumName()
	{
		return mSkipAlbumName;
	}

	/**
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 * 
	 * @param skipAlbumName
	 *            the skipAlbumName to set
	 */
	public void setSkipAlbumName(String skipAlbumName)
	{
		this.mSkipAlbumName = skipAlbumName;
	}
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 * @param mPhotoColumnNumber
	 *            the photoColumnNumber to set
	 */
	public void setPhotoColumnNumber(int mPhotoColumnNumber)
	{
		this.mPhotoColumnNumber = mPhotoColumnNumber;
	}
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 * @return the photoColumnNumber
	 */
	public int getPhotoColumnNumber()
	{
		return this.mPhotoColumnNumber;
	}

	/**
	 * 设置弹出相册列表每一行的高度，默认是64
	 * 
	 * @param rowHeight
	 */
	public void setPopListRowHeight(int rowHeight) 
	{
		this.mPopListRowHeight = rowHeight;
	}
	
	/**
	 * 获取弹出相册列表每一行的高度，默认是64
	 * 
	 * @return
	 */
	public int getPopListRowHeight() 
	{
		return this.mPopListRowHeight;
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
	
	
	/**
	 * 选择照片的尺寸限制 默认：CGSize(8000,8000)
	 * 
	 * @param maxSelectionImageSize  默认：CGSize(8000,8000)
	 */
	public void setMaxSelectionImageSize(TuSdkSize maxSelectionImageSize) 
	{
		this.mMaxSelectionImageSize = maxSelectionImageSize;
	}
	
	/**
	 * 选择照片的尺寸限制 默认：CGSize(8000,8000)
	 *  
	 * @return TuSdkSize
	 */
	public TuSdkSize getMaxSelectionImageSize() 
	{
		if(mMaxSelectionImageSize == null)
		{
			mMaxSelectionImageSize = new TuSdkSize(8000, 8000);
		}       
		
		return mMaxSelectionImageSize;
	}
	
	
	/**
	 * 系统相册控制器配置选项
	 */
	public TuAlbumMultipleListOption()
	{

	}

	/**
	 * 创建系统相册列表控制器对象
	 * 
	 * @return 系统相册列表控制器对象
	 */
	public TuAlbumMultipleListFragment fragment()
	{
		TuAlbumMultipleListFragment fragment = this.fragmentInstance();
		// 相册行视图布局ID
		fragment.setAlbumCellLayoutId(this.getAlbumCellLayoutId());
		// 照片行视图布局ID
		fragment.setPhotoCellLayoutId(this.getPhotoCellLayoutId());
		// 一次选择的最大照片数量 (默认: 3, 0 < n <= 9)
		fragment.setMaxSelection(this.getMaxSelection());
		// 允许在多个相册中选择 (默认: 开启)
		fragment.setEnableShareSelection(this.isEnableShareSelection());
		// 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
		fragment.setSkipAlbumName(this.getSkipAlbumName());
		// 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
		fragment.setPhotoColumnNumber(this.getPhotoColumnNumber());
		// 设置弹出相册列表每一行的高度，默认是64
		fragment.setPopListRowHeight(this.getPopListRowHeight());
		// 是否显示相机图标 (默认：开启)
		fragment.setDisplayCameraCell(this.isDisplayCameraCell());
		// 设置预览视图控制器
		fragment.setPreviewFragmentClazz(this.getPreviewClazz());
		// 设置预览视图布局视图ID
		fragment.setPreviewFragmentLayoutId(this.getPreviewLayoutId());
		// 设置相册照片排序方式
		fragment.setPhotosSortDescriptor(this.getPhotosSortDescriptor());
		// 设置选择图片的最大尺寸
		fragment.setMaxSelectionImageSize(this.getMaxSelectionImageSize());
		
		return fragment;
	}
}