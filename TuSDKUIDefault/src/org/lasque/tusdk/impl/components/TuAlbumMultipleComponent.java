/** 
 * TuSDKCore
 * TuAlbumMultipleComponent.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午1:08:20 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import java.util.ArrayList;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.components.album.TuAlbumMultipleListFragment;
import org.lasque.tusdk.impl.components.album.TuAlbumMultipleListFragment.TuAlbumMultipleListFragmentDelegate;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;
import org.lasque.tusdk.modules.components.TuAlbumMultipleComponentBase;

import android.app.Activity;

/**
 * 系统相册组件（带相机）
 * 
 * @author Clear
 */
public class TuAlbumMultipleComponent extends TuAlbumMultipleComponentBase implements
		TuAlbumMultipleListFragmentDelegate, TuCameraFragmentDelegate
{
	/**
	 * 系统相册组件配置选项
	 */
	private TuAlbumMultipleComponentOption mComponentOption;

	/**
	 * 系统相册组件配置选项
	 * 
	 * @return 系统相册组件配置选项
	 */
	public TuAlbumMultipleComponentOption componentOption()
	{
		if (mComponentOption == null)
		{
			mComponentOption = new TuAlbumMultipleComponentOption();
		}
		return mComponentOption;
	}

	/**
	 * 系统相册组件配置选项
	 * 
	 * @param mComponentOption
	 */
	public void setComponentOption(TuAlbumMultipleComponentOption mComponentOption)
	{
		this.mComponentOption = mComponentOption;
	}

	/**
	 * 启动系统相册组件
	 * 
	 * @param activity
	 *            内容上下文
	 * @param delegate
	 *            组件委托
	 * @return 系统相册组件
	 */
	public static TuAlbumMultipleComponent component(Activity activity,
			TuSdkComponentDelegate delegate)
	{
		TuAlbumMultipleComponent component = new TuAlbumMultipleComponent(activity);
		component.setDelegate(delegate);
		return component;
	}

	public TuAlbumMultipleComponent(Activity activity)
	{
		super(activity);
	}

	@Override
	protected void initComponent()
	{

	}

	@Override
	public TuAlbumMultipleComponent showComponent()
	{
		this.handleShowAlbum();
		// sdk统计代码
		StatisticsManger.appendComponent(ComponentActType.albumComponent);
		return this;
	}

	/************************* handleShowAlbum ************************/
	/**
	 * 显示相册
	 */
	protected void handleShowAlbum()
	{
		// 无法存储文件
		if (this.showAlertIfCannotSaveFile()) return;

		TuAlbumMultipleListFragment fragment = this.componentOption().albumListOption()
				.fragment();
		fragment.setDelegate(this);
		this.presentModalNavigationActivity(fragment, true);
	}

	/**
	 * 选中相册组
	 * 
	 * @param fragment
	 *            系统相册控制器
	 * @param group
	 *            相册组
	 */
	@Override
	public void onTuAlbumFragmentSelected(TuAlbumMultipleListFragment fragment,
			AlbumSqlInfo group)
	{
		//put code here if you want to do something
	}

	/**
	 * 选中相片
	 * 
	 * @param fragment
	 *            系统相册控制器
	 * @param images
	 *            选中的相片列表
	 */
	public void onTuPhotoFragmentSelected(TuAlbumMultipleListFragment fragment,
			ArrayList<ImageSqlInfo> images)
	{
		TuSdkResult result = new TuSdkResult();
		
		if (fragment.getMaxSelection() > 1)
		{
			result.images = images;
		}
		else
		{
			if (images != null && images.size() > 0)
			{
				result.imageSqlInfo = images.get(0);
			}
			else
			{
				result.imageSqlInfo = null;
			}
		}
		this.notifyResult(result, null, fragment);
	}
	
	/**
	 * 请求从相册界面跳转到相机界面
	 * 
	 * @param fragment
	 *            系统相册控制器
	 */
	public void onTuCameraDemand(TuAlbumMultipleListFragment fragment)
	{
		handleShowCamera(fragment);
	}
	
	/**
	 * 显示相机
	 */
	protected void handleShowCamera(TuAlbumMultipleListFragment lastFragment)
	{
		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(lastFragment.getActivity()))
		{
			this.onComponentError(null, null,
					ComponentErrorType.TypeUnsupportCamera.getError(this));
			return;
		}
		// 无法存储文件
		else if (this.showAlertIfCannotSaveFile())
		{
			return;
		}

		TuCameraFragment fragment = this.componentOption().cameraOption()
				.fragment();
		fragment.setDelegate(this);
		
		this.pushModalNavigationActivity(fragment, true);
		
		if(lastFragment != null)
		{
			lastFragment.dismissActivity();
		}
	}
	
	/*************************TuCameraFragmentDelegate*********************/
	/**
	 * 获取一个拍摄结果
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 */
	@Override
	public void onTuCameraFragmentCaptured(TuCameraFragment fragment,
			TuSdkResult result)
	{
		fragment.hubDismissRightNow();
		this.notifyResult(result, null, fragment);
	}

	/**
	 * 获取一个拍摄结果 (异步方法)
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment,
			TuSdkResult result)
	{
		return false;
	}
	
	/**
	 * 请求从相机界面跳转到相册界面
	 * 
	 * @param fragment
	 *            系统相册控制器
	 */
	@Override
	public void onTuAlbumDemand(TuCameraFragment fragment)
	{
		handleShowAlbum();
		
		fragment.dismissActivity();
	}
}
