/** 
 * TuSDKCore
 * TuAvatarComponent.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午2:47:43 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.widget.TuSdkActionSheet;
import org.lasque.tusdk.core.view.widget.TuSdkActionSheet.ActionSheetClickDelegate;
import org.lasque.tusdk.impl.components.album.TuPhotoListFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate;
import org.lasque.tusdk.impl.view.widget.TuActionSheet;
import org.lasque.tusdk.modules.components.ComponentErrorType;

import android.app.Activity;

/**
 * 头像设置组件
 * 
 * @author Clear
 */
public class TuAvatarComponent extends TuAlbumComponent implements TuCameraFragmentDelegate, TuEditTurnAndCutFragmentDelegate
{
	/** 头像设置配置选项 */
	private TuAvatarComponentOption mComponentOption;

	/** 头像设置配置选项 */
	@Override
	public TuAvatarComponentOption componentOption()
	{
		if (mComponentOption == null)
		{
			mComponentOption = new TuAvatarComponentOption();
		}
		return mComponentOption;
	}

	/**
	 * 启动头像设置组件
	 * 
	 * @param activity
	 *            内容上下文
	 * @param delegate
	 *            组件委托
	 * @return 头像设置组件
	 */
	public static TuAvatarComponent component(Activity activity, TuSdkComponentDelegate delegate)
	{
		TuAvatarComponent component = new TuAvatarComponent(activity);
		component.setDelegate(delegate);
		return component;
	}

	/** 头像设置组件 */
	public TuAvatarComponent(Activity activity)
	{
		super(activity);
	}

	/** 初始化组件 */
	@Override
	protected void initComponent()
	{

	}

	/** 显示组件 */
	@Override
	public TuAvatarComponent showComponent()
	{
		TuActionSheet actionsheet = new TuActionSheet(this.activity());
		actionsheet.init(getResString("lsq_avatar_actionsheet_title"), getResString("lsq_avatar_actionsheet_cancel"), null,
				getResString("lsq_avatar_actionsheet_camera"), getResString("lsq_avatar_actionsheet_album"));
		actionsheet.showInView(mActionSheetClickDelegate);
		return this;
	}

	/** 点击ActionSheet */
	protected ActionSheetClickDelegate mActionSheetClickDelegate = new ActionSheetClickDelegate()
	{
		@Override
		public void onActionSheetClicked(TuSdkActionSheet actionSheet, int buttonIndex)
		{
			if (actionSheet.getCancelIndex() == buttonIndex) return;

			actionSheet.dismissRightNow();
			switch (buttonIndex)
				{
				case 0:
					// 显示相机
					handleShowCamera();
					break;
				case 1:
					// 显示相册
					handleShowAlbum();
					break;
				default:
					break;
				}
		}
	};

	/************************* handleShowCamera ************************/
	/** 显示相机 */
	protected void handleShowCamera()
	{
		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(this.activity()))
		{
			this.onComponentError(null, null, ComponentErrorType.TypeUnsupportCamera.getError(this));
			return;
		}
		// 无法存储文件
		else if (this.showAlertIfCannotSaveFile())
		{
			return;
		}

		TuCameraFragment fragment = this.componentOption().cameraOption().fragment();
		fragment.setDelegate(this);
		this.presentModalNavigationActivity(fragment, true);
	}

	/**
	 * 获取一个拍摄结果
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 */
	@Override
	public void onTuCameraFragmentCaptured(TuCameraFragment fragment, TuSdkResult result)
	{
		fragment.hubDismissRightNow();

		TuEditTurnAndCutFragment editFragment = this.componentOption().editTurnAndCutOption().fragment();

		editFragment.setImage(result.image);
		editFragment.setTempFilePath(result.imageFile);
		editFragment.setImageSqlInfo(result.imageSqlInfo);

		editFragment.setDelegate(this);
		fragment.pushFragment(editFragment);
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
	public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment, TuSdkResult result)
	{
		return false;
	}
	
	/**
	 * 请求从相机界面跳转到相册界面。只有 设置mDisplayAlbumPoster为true (默认:false) 才会发生该事件
	 * 
	 * @param fragment
	 *            系统相册控制器
	 */
	@Override
	public void onTuAlbumDemand(TuCameraFragment fragment)
	{
		// to show album
	}

	/************************* handleShowAlbum ************************/
	/**
	 * 选中相片
	 * 
	 * @param fragment
	 *            系统相册控制器
	 * @param imageSqlInfo
	 *            相片信息
	 */
	@Override
	public void onTuPhotoFragmentSelected(TuPhotoListFragment fragment, ImageSqlInfo imageSqlInfo)
	{
		TuEditTurnAndCutFragment editFragment = this.componentOption().editTurnAndCutOption().fragment();

		editFragment.setImageSqlInfo(imageSqlInfo);
		editFragment.setDelegate(this);
		fragment.pushFragment(editFragment);
	}

	/************************* edit result ************************/

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 */
	@Override
	public void onTuEditTurnAndCutFragmentEdited(TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		fragment.hubDismissRightNow();
		this.notifyResult(result, null, fragment);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditTurnAndCutFragmentEditedAsync(TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		TLog.d("onTuEditTurnAndCutFragmentEditedAsync: %s", result);
		return false;
	}
}