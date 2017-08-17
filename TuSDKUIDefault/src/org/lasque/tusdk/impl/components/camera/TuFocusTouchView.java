/** 
 * TuSDKCore
 * TuFocusTouchView.java
 *
 * @author 		Clear
 * @Date 		2015-8-18 上午11:02:27 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.camera;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkVideoCameraExtendViewInterface;
import org.lasque.tusdk.core.view.widget.TuGuideRegionView;
import org.lasque.tusdk.impl.components.widget.FilterConfigView;
import org.lasque.tusdk.modules.components.camera.TuFocusRangeViewInterface;
import org.lasque.tusdk.modules.components.camera.TuFocusTouchViewBase;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Focus Touch View
 * 
 * @author Clear
 */
public class TuFocusTouchView extends TuFocusTouchViewBase implements TuSdkVideoCameraExtendViewInterface
{
	/** 长按时间 */
	public static final int LongPressDistance = 600;
	/** 长按偏移坐标 */
	public static final int LongPressOffset = 20;

	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_camera_focus_touch_view");
	}

	/** Focus Touch View */
	public TuFocusTouchView(Context context)
	{
		super(context);
	}

	/** Focus Touch View */
	public TuFocusTouchView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/** Focus Touch View */
	public TuFocusTouchView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 聚焦区域视图 */
	private TuFocusRangeViewInterface mRangeView;
	/** 视频显示区域 */
	private TuGuideRegionView mGuideRegionView;
	/** 滤镜配置视图 */
	private FilterConfigView mFilterConfigView;
	/** 脸部定位视图布局ID */
	private int mFaceDetectionLayoutID;

	/** 开启长按拍摄 */
	private boolean mEnableLongTouchCapture;
	/** 禁用聚焦声音 */
	private boolean mDisableFocusBeep;
	/** 禁用持续自动对焦 */
	private boolean mDisableContinueFoucs;
	/** 开启滤镜配置选项 */
	private boolean mEnableFilterConfig;

	/** 长按时间 */
	@Override
	protected long getLongPressDistance()
	{
		return LongPressDistance;
	}

	/** 长按偏移坐标 */
	@Override
	protected int getLongPressOffset()
	{
		return LongPressOffset;
	}

	/** 开启长按拍摄 (默认：false) */
	@Override
	public boolean isEnableLongTouchCapture()
	{
		return mEnableLongTouchCapture;
	}

	/** 开启长按拍摄 (默认：false) */
	@Override
	public void setEnableLongTouchCapture(boolean enableLongTouchCapture)
	{
		this.mEnableLongTouchCapture = enableLongTouchCapture;
	}

	/** 禁用聚焦声音 (默认：false) */
	@Override
	public boolean isDisableFocusBeep()
	{
		return mDisableFocusBeep;
	}

	/** 禁用聚焦声音 (默认：false) */
	@Override
	public void setDisableFocusBeep(boolean disableFocusBeep)
	{
		this.mDisableFocusBeep = disableFocusBeep;
	}

	/** 禁用持续自动对焦 (默认：false) */
	@Override
	public boolean isDisableContinueFoucs()
	{
		return mDisableContinueFoucs;
	}

	/** 禁用持续自动对焦 (默认：false) */
	@Override
	public void setDisableContinueFoucs(boolean disableContinueFoucs)
	{
		this.mDisableContinueFoucs = disableContinueFoucs;
	}

	/** 设置辅助线显示状态 */
	@Override
	public void setGuideLineViewState(boolean mDisplayGuideLine)
	{
		if (this.getGuideRegionView() != null)
		{
			this.getGuideRegionView().setGuideLineViewState(mDisplayGuideLine);
		}
	}

	/** 开启滤镜配置选项 */
	public boolean isEnableFilterConfig()
	{
		return mEnableFilterConfig;
	}

	/** 开启滤镜配置选项 */
	@Override
	public void setEnableFilterConfig(boolean mEnableFilterConfig)
	{
		this.mEnableFilterConfig = mEnableFilterConfig;
	}

	/** 聚焦区域视图 */
	@SuppressWarnings("unchecked")
	public <T extends View & TuFocusRangeViewInterface> T getFocusRangeView()
	{
		if (mRangeView == null)
		{
			View view = this.getViewById("lsq_focus_range_view");
			if (view == null || !(view instanceof TuFocusRangeViewInterface)) return null;

			mRangeView = (TuFocusRangeViewInterface) view;
		}
		return (T) mRangeView;
	}

	/** 聚焦区域视图 */
	public void setFocusRangeView(TuFocusRangeViewInterface rangeView)
	{
		this.mRangeView = rangeView;
	}

	/** 视频显示区域 */
	public TuGuideRegionView getGuideRegionView()
	{
		if (mGuideRegionView == null)
		{
			mGuideRegionView = this.getViewById("lsq_guideRegionView");
		}
		return mGuideRegionView;
	}

	/** 滤镜配置视图 */
	public FilterConfigView getFilterConfigView()
	{
		if (mFilterConfigView == null)
		{
			mFilterConfigView = this.getViewById("lsq_filter_config_view");
		}
		return mFilterConfigView;
	}

	/** 脸部定位视图布局ID */
	public int getFaceDetectionLayoutID()
	{
		if (mFaceDetectionLayoutID < 1)
		{
			mFaceDetectionLayoutID = TuSdkContext.getLayoutResId("tusdk_impl_component_camera_face_detection_view");
		}
		return mFaceDetectionLayoutID;
	}

	/** 设置脸部定位视图布局ID */
	public void setFaceDetectionLayoutID(int layout)
	{
		mFaceDetectionLayoutID = layout;
	}

	/** 加载视图 */
	@Override
	public void loadView()
	{
		super.loadView();

		// 聚焦区域视图
		this.showViewIn(this.getFocusRangeView(), false);

		if (this.getFilterConfigView() != null)
		{
			this.getFilterConfigView().hiddenDefault();
		}
	}

	/**
	 * 通知聚焦
	 * 
	 * @param lastPoint
	 *            最后的聚焦点
	 * @param capture
	 *            是否拍摄
	 * @return 是否允许聚焦
	 */
	@Override
	protected boolean notifyFoucs(PointF lastPoint, final boolean capture)
	{
		boolean result = super.notifyFoucs(lastPoint, capture);

		if (result && this.getFocusRangeView() != null)
		{
			this.getFocusRangeView().setPosition(lastPoint);
		}
		return result;
	}

	/** 显示选区焦点视图 */
	@Override
	public void showRangeView()
	{
		if (this.getFocusRangeView() != null) this.getFocusRangeView().setPosition(this.getLastPoint());
	}

	/** 设置选区焦点视图状态 */
	@Override
	public void setRangeViewFoucsState(boolean success)
	{
		if (this.getFocusRangeView() != null) this.getFocusRangeView().setFoucsState(success);
	}

	/** 显示区域百分比 */
	@Override
	public void setRegionPercent(RectF regionPercent)
	{
		super.setRegionPercent(regionPercent);

		this.getGuideRegionView().setRegionPercent(regionPercent);
	}

	/** 相机状态改变 */
	@Override
	public void cameraStateChanged(TuSdkStillCameraInterface camera, CameraState state)
	{
		super.cameraStateChanged(camera, state);
		if (state == CameraState.StateStarted)
		{
			// 防止准心因为意外关闭相机无法关闭
			this.showViewIn(this.getFocusRangeView(), false);
		}
	}

	/** 创建脸部定位视图 */
	@Override
	public View buildFaceDetectionView()
	{
		View view = this.buildView(this.getFaceDetectionLayoutID(), this);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		return view;
	}

	/*************************** FilterConfigView *************************/
	/** 通知滤镜配置视图 */
	@Override
	public void notifyFilterConfigView(SelesOutInput filter)
	{
		FilterConfigView configView = this.getFilterConfigView();
		if (configView == null) return;

		if (!this.isEnableFilterConfig())
		{
			this.showViewIn(configView, false);
			return;
		}

		configView.setSelesFilter(filter);
	}
}