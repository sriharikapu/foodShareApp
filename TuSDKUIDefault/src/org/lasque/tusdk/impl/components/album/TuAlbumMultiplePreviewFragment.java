/** 
 * TuSDKGee
 * TuAlbumMultiplePreviewFragment.java
 *
 * @author 		Clear
 * @Date 		2016-08-26 下午4:32:37 
 * @Copyright 	(c) 2016 Lasque. All rights reserved.
 * 
 */

package org.lasque.tusdk.impl.components.album;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper.PhotoSortDescriptor;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.modules.components.album.TuAlbumMultiplePreviewFragmentBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 多选相册，图片预览控制器
 * 
 * @author Clear
 */
public class TuAlbumMultiplePreviewFragment extends TuAlbumMultiplePreviewFragmentBase 
 			implements OnPageChangeListener
{
	/**
	 * 布局ID
	 * 
	 * @return
	 */
	public static int getLayoutId()
	{
		return TuSdkContext
				.getLayoutResId("tusdk_impl_component_album_multiple_preview_fragment");
	}

	/**
	 * 预览控制器委托 
	 * 
	 * @author Clear
	 */
	public interface TuAlbumMultiplePreviewDelegate
	{
		/**
		 * 点击完成按钮
		 */
		void onCompleteButtonClicked();
		
		/**
		 * 点击选择按钮
		 * 
		 * @param itemData
		 * 			  当前图片信息
		 * @param position
		 * 			 当前图片相册中的位置
		 */
		void onSelectButtonClicked(ImageSqlInfo itemData, int position);
	}
	
	/**
	 * 预览控制器委托 
	 */
	private TuAlbumMultiplePreviewDelegate mDelegate;
	
	/**
	 * 预览控制器委托 
	 * 
	 * @param delegate
	 */
	public void setDelegate(TuAlbumMultiplePreviewDelegate delegate)
	{
		this.mDelegate = delegate;
	}
	
	/**
	 * 预览控制器委托 
	 * 
	 * @return the delegate
	 */
	public TuAlbumMultiplePreviewDelegate getDelegate()
	{
		return this.mDelegate;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 图片展示视图
	 */
	private ViewPager mViewPager;

	/**
	 * 返回按钮
	 */
	private TuSdkTextButton mBackButton;

	/**
	 * 选择视图包装
	 */
	private LinearLayout mSelectButtonWrap;

	/**
	 * 选择包装中字符按钮
	 */
	private TuSdkTextButton mSelectTextButton;
	
	/**
	 * 选择包装中图片按钮
	 */
	private TuSdkImageButton mSelectImageButton;

	/**
	 * 完成按钮
	 */
	private TuSdkButton mCompleteButton;
	
	/**
	 * 顶部栏
	 */
	private RelativeLayout mTopBar;
	
	/**
	 * 底部栏
	 */
	private RelativeLayout mBottomBar;

	
	/**
	 * 图片展示视图
	 * 
	 * @return the ImageView
	 */
	public ViewPager getViewPager()
	{
		if (mViewPager == null)
		{
			mViewPager = this.getViewById("lsq_viewPager");
			
			ViewPagerAdapter adapter = new ViewPagerAdapter();
			mViewPager.setAdapter(adapter);
			
			mViewPager.setCurrentItem(getCurrentPosition() - 1);
			mViewPager.addOnPageChangeListener(this);
		}
		return mViewPager;
	}
	
	/**
	 * 选择视图包装
	 * 
	 * @return the LinearLayout
	 */
	public LinearLayout getSelectImageWrap()
	{
		if (mSelectButtonWrap == null)
		{
			mSelectButtonWrap = this.getViewById("lsq_selectImageWrap");
			mSelectButtonWrap.setOnClickListener(mButtonClickListener);
		}
		return mSelectButtonWrap;
	}
	
	/**
	 * 完成按钮
	 * 
	 * @return the TuSdkButton
	 */
	public TuSdkButton getCompleteButton()
	{
		if (mCompleteButton == null)
		{
			mCompleteButton = this.getViewById("lsq_completeButton");
			mCompleteButton.setOnClickListener(mButtonClickListener);
		}
		return mCompleteButton;
	}
	
	/**
	 * 获取顶部栏
	 * 
	 * @return
	 */
	public RelativeLayout getTopBarView()
	{
		if (mTopBar == null)
		{
			mTopBar = this.getViewById("lsq_topBar");
		}
		return mTopBar;
	}
	
	/**
	 * 获取底部栏
	 * 
	 * @return
	 */
	public RelativeLayout getBottomBarView()
	{
		if (mBottomBar == null)
		{
			mBottomBar = this.getViewById("lsq_bottomBar");
		}
		return mBottomBar;
	}
	
	/**
	 * 返回按钮
	 * 
	 * @return the TuSdkTextButton
	 */
	public TuSdkTextButton getBackButton()
	{
		if (mBackButton == null)
		{
			mBackButton = this.getViewById("lsq_backButton");
			mBackButton.setOnClickListener(mButtonClickListener);
		}
		return mBackButton;
	}

	/**
	 * 选择包装中图片按钮
	 * 
	 * @return
	 */
	public TuSdkImageButton getSelectImageButton()
	{
		if (mSelectImageButton == null)
		{
			mSelectImageButton = this.getViewById("lsq_selectImageButton");
		}
		return mSelectImageButton;
	}
	
	/**
	 * 选择包装中字符按钮
	 * 
	 * @return
	 */
	public TuSdkTextButton getSelectTextButton()
	{
		if (mSelectTextButton == null)
		{
			mSelectTextButton = this.getViewById("lsq_select_tb");
		}
		return mSelectTextButton;
	}

	/** 按钮点击事件 */
	protected OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			// 分发视图点击事件
			dispatcherViewClick(v);
		}
	};

	/** 分发视图点击事件 */
	protected void dispatcherViewClick(View v)
	{
		if (this.equalViewIds(v, this.getCompleteButton()))
		{
			handleCompleteButton();
		}
		else if(this.equalViewIds(v, this.getBackButton()))
		{
			handleBackButton();
		}
		else if(this.equalViewIds(v, this.getSelectImageWrap()))
		{
			handleSelectWrap();
		}
		else if(this.equalViewIds(v, this.getViewPager()))
		{
			handleImageSingleTap();
		}
	}
	
	/**
	 * 轻点图片区域隐藏顶部栏、底部栏
	 */
	protected void handleImageSingleTap() 
	{
		setDisplayBars(!isDisplayBars());
		
		this.getTopBarView().setVisibility(isDisplayBars()? View.VISIBLE: View.INVISIBLE);
		this.getBottomBarView().setVisibility(isDisplayBars()? View.VISIBLE: View.INVISIBLE);
	}
	
	/**
	 * 点击完成按钮
	 */
	protected void handleCompleteButton()
	{
		// 如果没有选择任何图片
		if(getMaxSelecteNumber() > 1 && getSelectedPhotolist().size() == 0)
		{
			TuSdk.messageHub().showToast(this.getActivity(), TuSdkContext.getString("lsq_album_empty_selection_msg"));
			return;
		}
		
		// 关闭页面
		navigatorBarBackAction(null);
		
		// 通知相册照片已经选择完毕
		if(getDelegate() != null)
			getDelegate().onCompleteButtonClicked();
	}
	
	/**
	 * 点击后退按钮
	 */
	protected void handleBackButton()
	{
		navigatorBarBackAction(null);
	}

	/**
	 * 点击选择区域
	 */
	protected void handleSelectWrap() 
	{
		if(getSelectedPhotolist().size() >= getMaxSelecteNumber() && !isCurrentPhotoSelected())
		{
			TLog.w("[%d] photos have been selected already", this.getMaxSelecteNumber());
			TuSdk.messageHub().showToast(this.getActivity(), TuSdkContext.getString("lsq_album_max_selection_msg", this.getMaxSelecteNumber()));

			return;
		}
		
		ImageSqlInfo photoSqlInfo = getCurrentPhotoSqlInfo(getCurrentPosition());
		
		// 验证图片是否过大
		if(photoSqlInfo.size.width > this.getMaxSelectionImageSize().width || photoSqlInfo.size.height > this.getMaxSelectionImageSize().height)
		{
			TuSdk.messageHub().showError(this.getContext(),TuSdkContext.getString("lsq_album_image_size_limited", this.getMaxSelectionImageSize().width,this.getMaxSelectionImageSize().height));
			return;
		}
		
		// 在预览界面选中图片后要通知多选界面选中同一图片
		if(getDelegate() != null)
			getDelegate().onSelectButtonClicked(photoSqlInfo, getCurrentPosition());
		
		
		// 更新选择按钮、文字状态
		updateSelectWrap();
		// 更新完成按钮、文字状态
		updateCompleteButtonText();
	}

	/**
	 * 当前图片在相册中的位置
	 */
	private int mPosition;
	
	/**
	 * 允许最大选择的数目
	 */
	private int mMaxSelecteNumber;
	
	/**
	 * 当前相册的 AlbumSqlInfo信息
	 */
	private AlbumSqlInfo mAlbumSqlInfo;
	
	/**
	 * 已选择的图片列表
	 */
	private List<ImageSqlInfo> mSelectedPhotoList;
	
	/** 
	 * 相片信息列表 
	 * */
	private ArrayList<ImageSqlInfo> mPhotos;

	/**
	 * 顶部栏、底部栏是否是显示状态
	 */
	private boolean isDisplayBars = true;
	
	/**
	 * 是否可以继续滑动 (防止出现滑动图片过快导致的图片滑动效果出现延迟现象)
	 */
	private boolean canContinueScroll = true;


	/**
	 * 相册照片排序方式 默认按照修改时间排序 Media.DATE_MODIFIED
	 */
	private PhotoSortDescriptor mPhotosSortDescriptor;
	
	/**
	 *  选择照片的尺寸限制 默认：CGSize(8000,8000)
	 */
	private TuSdkSize mMaxSelectionImageSize;
	
	/**
	 * 当前图片在相册中的位置
	 * 
	 * @param position
	 */
	public void setCurrentPosition(int position)
	{
		this.mPosition = position;
	}
	
	/**
	 * 当前图片在相册中的位置
	 * 
	 * @return
	 */
	public int getCurrentPosition()
	{
		return this.mPosition;
	}
	
	/**
	 * 当前相册中图片的数目
	 * 
	 * @return
	 */
	public int getPhotoCountInAlbum()
	{
		return this.mAlbumSqlInfo.total;
	}

	/**
	 * 允许最大选择的数目
	 * 
	 * @param maxSelecteNumber
	 */
	public void setMaxSelecteNumber(int maxSelecteNumber)
	{
		this.mMaxSelecteNumber = maxSelecteNumber;
	}
	
	/**
	 * 允许最大选择的数目
	 * 
	 * @return
	 */
	public int getMaxSelecteNumber()
	{
		return this.mMaxSelecteNumber;
	}
	
	/**
	 * 顶部栏、底部栏是否是显示状态
	 * 
	 * @param isDisplay
	 */
	private void setDisplayBars(boolean isDisplay)
	{
		this.isDisplayBars = isDisplay;
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
	 * 顶部栏、底部栏是否是显示状态
	 * 
	 * @return
	 */
	private boolean isDisplayBars()
	{
		return this.isDisplayBars;
	}
	
	/**
	 * 当前图片是否是选择状态
	 * 
	 * @return
	 */
	private boolean isCurrentPhotoSelected()
	{
		if (mSelectedPhotoList == null || mSelectedPhotoList.size() == 0) return false;
		
		ImageSqlInfo itemData = getCurrentPhotoSqlInfo(getCurrentPosition());
		
		for(int i = 0; i<mSelectedPhotoList.size(); i++)
		{
			ImageSqlInfo item = mSelectedPhotoList.get(i);
			
			if (item.albumId == itemData.albumId && item.id == itemData.id)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 当前图片的 ImageSqlInfo信息
	 * @param position 
	 * 
	 * @return
	 */
	public ImageSqlInfo getCurrentPhotoSqlInfo(int position)
	{
		if(mPhotos == null) return null;
		
		// position 越界时
		if(position < 1 || position > getPhotoCountInAlbum())
			return null;

		return mPhotos.get(position - 1);
	}
	
	/**
	 * 当前相册的 AlbumSqlInfo信息
	 * 
	 * @param info
	 */
	public void setCurrentAlbumSqlInfo(AlbumSqlInfo info)
	{
		this.mAlbumSqlInfo = info;
	}
	
	/**
	 * 当前相册的 AlbumSqlInfo信息
	 * 
	 * @return
	 */
	public AlbumSqlInfo getCurrentAlbumSqlInfo()
	{
		return this.mAlbumSqlInfo;
	}
	
	/**
	 * 已选择的图片列表
	 * 
	 * @param selectedPhotoList
	 */
	public void setSelectedPhotolist(List<ImageSqlInfo> selectedPhotoList)
	{
		this.mSelectedPhotoList = selectedPhotoList;
	}
	
	/**
	 * 已选择的图片列表
	 * 
	 * @return
	 */
	public List<ImageSqlInfo> getSelectedPhotolist()
	{
		return this.mSelectedPhotoList;
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
	protected void loadView(ViewGroup view) 
	{
		super.loadView(view);
		
		this.getBackButton();
		this.getCompleteButton();
		this.getSelectImageButton();
		this.getSelectImageWrap();
		this.getSelectTextButton();
		this.getTopBarView();
		this.getBottomBarView();
	}

	// 给 ViewPager 设置监听事件
	private OnTouchListener mOnTouchListener = 
			new OnSwipeTouchListener(getActivity())
	{
		// 手指向左滑动
		@Override
		public void onSwipeLeft() 
		{
			getViewPager().setCurrentItem(getCurrentPosition(), true);
		}
		
		// 手指向右滑动
		@Override
		public void onSwipeRight() 
		{
			getViewPager().setCurrentItem(getCurrentPosition() - 2, true);
		}
		
		// 轻点图片区域隐藏顶部栏、底部栏
		@Override
		public void onTouch(MotionEvent e) 
		{
			handleImageSingleTap();
		}
	};
	
	@Override
	protected void viewDidLoad(ViewGroup view) 
	{		
		super.viewDidLoad(view);
		
		// 获取当前相册图片列表
		mPhotos = ImageSqlHelper.getPhotoList(getActivity(), mAlbumSqlInfo.id,this.getPhotosSortDescriptor());
		
		// 给 Viewpager 设置 OnTouch 事件监听
		this.getViewPager().setOnTouchListener(mOnTouchListener);

		// 更新当前图片位置信息视图
		updatePositionTitleView();
		// 更新完成按钮、文字状态
		updateCompleteButtonText();
		// 更新选择按钮、文字状态
		updateSelectWrap();
	}
	
	@Override
	public void onDestroyView()
	{
		if (getViewPager() != null)
		{
			ViewPagerAdapter adapter = (ViewPagerAdapter)(getViewPager().getAdapter());
			adapter.destroy();
			getViewPager().setAdapter(null);
		}
		
		super.onDestroyView();
	}
	
	@Override
	public void onPageScrollStateChanged(int state) 
	{
		// 用户正在拖动图片过程中和图片正在滚动过程中，设置不可以继续滑动，防止图片滑动出现延迟现象
		if(state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_SETTLING)
		{
			canContinueScroll = false;
		}
		// 图片已经静止时，设置可以继续滑动
		else if(state == ViewPager.SCROLL_STATE_IDLE)
		{
			canContinueScroll = true;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int currentPage)
	{
		setCurrentPosition(currentPage + 1);
		
		// 更新当前图片位置信息视图
		updatePositionTitleView();
		// 更新选择按钮、文字状态
		updateSelectWrap();
	}

	/**
	 * 更新当前图片位置信息视图
	 */
	private void updatePositionTitleView() 
	{
		this.getBackButton().setText(String.format("%s/%s", getCurrentPosition(), getPhotoCountInAlbum()));
	}

	/**
	 * 更新选择按钮、文字状态
	 */
	private void updateSelectWrap()
	{		
		int imageButtonRes, textButtonRes;
		
		if(isCurrentPhotoSelected())
		{
			imageButtonRes = TuSdkContext.getDrawableResId("lsq_style_default_multi_album_preview_selected_button");
			textButtonRes = TuSdkContext.getColor("lsq_text_multi_album_preview_select");
		}
		else
		{
			imageButtonRes = TuSdkContext.getDrawableResId("lsq_style_default_multi_album_preview_unselected_button");
			textButtonRes = TuSdkContext.getColor("lsq_color_white");
		}
		
		getSelectImageButton().setImageResource(imageButtonRes);
		getSelectTextButton().setTextColor(textButtonRes);
	}
	
	/**
	 * 更新完成按钮、文字状态
	 */
	private void updateCompleteButtonText()
	{
		this.getCompleteButton().setText(String.format("%s (%s/%s)", TuSdkContext.getString("lsq_album_multi_preview_done"), 
					getSelectedPhotolist().size(), getMaxSelecteNumber()));
		
		int resource;
		
		if(mSelectedPhotoList.size() > 0)
			resource = TuSdkContext.getDrawableResId("lsq_style_default_multi_album_preview_finish_selected_button");
		else
			resource = TuSdkContext.getDrawableResId("lsq_style_default_multi_album_preview_finish_unselected_button");

		getCompleteButton().setBackgroundResource(resource);
	}
	
	/**
	 * 该类重写了 ViewPager 中对滑动手势的默认识别，使之对翻页的识别更灵活 
	 */
	class OnSwipeTouchListener implements OnTouchListener
	{
		private GestureDetector gestureDetector;
		
		public OnSwipeTouchListener(Context context)
		{
			gestureDetector = new GestureDetector(context, new GestureListener());
		}
		
		@Override
		public boolean onTouch(final View v, final MotionEvent event)
		{
			return gestureDetector.onTouchEvent(event);
		}
	
		private final class GestureListener extends SimpleOnGestureListener 
		{
			// 滑动距离阀值
			private static final int SWIPE_THRESHOLD = 10;
			// 滑动速度阀值
		    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
	
			@Override
			public boolean onDown(MotionEvent e) 
			{
				return true;
			}
			
			// 触发隐藏顶部栏、底部栏动作
		    @Override
		    public boolean onSingleTapConfirmed(MotionEvent e) 
		    {
		        onTouch(e);
		        return true;
		    }
	
		    // 滑动手势
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) 
			{
				boolean result = false;
				
				float diffX = e2.getX() - e1.getX();
				
				if(Math.abs(diffX) > SWIPE_THRESHOLD 
						&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD 
							&& canContinueScroll)
				{
					if(diffX > 0)
					{
						onSwipeRight();
						result = true;
					}
					else if(diffX < 0)
					{
						onSwipeLeft();
						result = true;
					}
				}
				return result;
			}
		}
	
		// 点击图片事件
		public void onTouch(MotionEvent e) 
		{
			
		}
		
		// 手势右滑事件
		public void onSwipeRight() 
		{
			
		}
		
		// 手势左滑事件
		public void onSwipeLeft() 
		{
			
		}
	}

	/**
	 * ViewPager 适配器
	 */
	class ViewPagerAdapter extends PagerAdapter 
	{	    
	    // universal-image-loader option
		DisplayImageOptions localOptions;
	
		HashMap<View, ImageOrientation>  map = new HashMap<View, ImageOrientation>();
		
		ImageOrientation orientation;
		public ViewPagerAdapter()
		{
	        localOptions = new DisplayImageOptions.Builder()
		        .cacheInMemory(true)
		        .considerExifParams(true)
		        .cacheOnDisk(false)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.EXACTLY)
		        .displayer(new SimpleBitmapDisplayer()).build();
		}
		
		public void destroy()
		{
			if (map != null)
			{
				map.clear();
				map = null;
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			// 获取 ViewPager 中 item 的视图
			View imageLayout = View.inflate(getActivity(), 
					TuSdkContext.getLayoutResId("tusdk_impl_component_album_multiple_preview_item_view"), null);
			container.addView(imageLayout);
	
			final ImageView imageView = (ImageView) imageLayout.findViewById(TuSdkContext.getIDResId("imageView"));
			RelativeLayout loadingView = (RelativeLayout) imageLayout.findViewById(TuSdkContext.getIDResId("loading_view"));
			imageView.setTag(loadingView);
			
			Uri uri = null;
			ImageSqlInfo info = getCurrentPhotoSqlInfo(position + 1);
			if(info == null) return null;
			
			orientation = ImageOrientation.getValue(info.orientation, false);
			map.put(imageView, orientation);

			// 获取 Uri
			uri = getImageContentUri(getActivity(), info.path);
			
			// 使用 universal-image-loader 加载图片
			ImageLoader.getInstance().displayImage(uri.toString(), 
						imageView, localOptions, loadingListenerr);
							
			return imageLayout;
		}
	
		/**
		 * universal-image-loader 加载图片监听
		 */
	    private ImageLoadingListener loadingListenerr = new ImageLoadingListener()
	    {
	
			@Override
			public void onLoadingCancelled(String arg0, View arg1) 
			{
			}
	
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap bitmap)
			{
				ImageOrientation orientation = map.get(view);
				
				if(orientation != ImageOrientation.Up)
				{
					Bitmap rotateBitmap = BitmapHelper.imageRotaing(bitmap, orientation);
	                if(rotateBitmap != null)
	                {
	                    if(view instanceof ImageView)
	                    {
	                        ((ImageView) view).setImageBitmap(rotateBitmap);
	                    }
	                }
				}

				// view 为放置图片的 ImageView
	            view.setVisibility(View.VISIBLE);
	            view.getParent().bringChildToFront(view);
	            
	            // 防止某些特殊图片情况下 loadingView 不消失的问题
	            View loadingView = (View)view.getTag();
	            if(loadingView == null) return;
	            loadingView.setVisibility(View.GONE);
			}
	
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) 
			{
			}
	
			@Override
			public void onLoadingStarted(String arg0, View arg1) 
			{
			}
	    };
	
	    /**
	     * 根据文件路径获取文件 Uri
	     * 
	     * @param context
	     * @param filePath
	     * @return
	     */
	    public Uri getImageContentUri(Context context, String filePath) 
	    {
	        Cursor cursor = context.getContentResolver().query(
	                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	                new String[] { MediaStore.Images.Media._ID },
	                MediaStore.Images.Media.DATA + "=? ",
	                new String[] { filePath }, null);
	        
	        if (cursor != null && cursor.moveToFirst()) 
	        {
	            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
	            cursor.close();
	            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
	        } 
	        else 
	        {
	            if (new File(filePath).exists())
	            {
	                ContentValues values = new ContentValues();
	                values.put(MediaStore.Images.Media.DATA, filePath);
	                return context.getContentResolver().insert(
	                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	            } 
	            else 
	            {
	                return null;
	            }
	        }
	    }
	
		@Override
		public int getCount()
		{
			return getPhotoCountInAlbum();
		}
	
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == arg1;
		}
	
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 
		{
			View view = (View) object;
			container.removeView(view);
		}
	 }
}
