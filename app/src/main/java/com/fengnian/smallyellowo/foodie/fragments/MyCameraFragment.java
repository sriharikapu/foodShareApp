/**
 * TuSDKCore
 * TuCameraFragment.java
 *
 * @author Clear
 * @Date 2014-11-29 下午5:46:47
 * @Copyright (c) 2014 tusdk.com. All rights reserved.
 */
package com.fengnian.smallyellowo.foodie.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.FastEditActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.ReviewImagesActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.ReviewImagesIntent;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFacing;
import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCamera;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.core.utils.hardware.TuSdkVideoCameraExtendViewInterface;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.core.view.TuSdkImageView;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;
import org.lasque.tusdk.impl.components.filter.TuFilterOnlineFragment;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.camera.TuCameraFragmentBase;
import org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * 相机控制器
 *
 * @author Clear
 */
public class MyCameraFragment extends TuCameraFragmentBase implements TuSdkFragmentActivity.TuSdkFragmentActivityEventListener,OnClickListener {
    private RelativeLayout lsqCameraview;
    private TextView tvFilterName;
    private LinearLayout lsqConfigbar;
    private TuSdkImageButton lsqClosebutton;
    private TuSdkImageButton lsqGuidelinebutton;
    private TuSdkImageButton lsqFlashbutton;
    private TuSdkImageButton lsqSwitchbutton;
    private Button lsqNextbutton;
    private RelativeLayout lsqBottombar;
    private TuSdkImageView lsqAlbumposterview;
    private TuSdkImageButton lsqCapturebutton;
    private TuSdkImageButton lsqFilterbutton;
    private TuSdkImageButton lsqRatiobutton;
    private ImageView lsqTokenPicture;
    private TextView tvTokenPictureNum;
    private LinearLayout llFiltersContainer;
    private ImageView ivNone;
    private FrameLayout ivNoneCover;
    private ImageView ivXian;
    private FrameLayout ivXianCover;
    private ImageView ivSu;
    private FrameLayout ivSuCover;
    private ImageView ivNuan;
    private FrameLayout ivNuanCover;
    private ImageView ivLiang;
    private FrameLayout ivLiangCover;
    private ImageView ivRuan;
    private FrameLayout ivRuanCover;
    private ImageView ivCloseFilters;

    /**
     * 布局ID
     */
    public static int getLayoutId() {
        return R.layout.fragment_my_camera;
    }

    @OnClick({R.id.iv_none, R.id.iv_xian, R.id.iv_su, R.id.iv_nuan, R.id.iv_liang, R.id.iv_ruan, R.id.iv_close_filters, R.id.lsq_nextButton, R.id.lsq_token_picture})
    public void onClick(View view) {
        if (R.id.iv_close_filters == view.getId()) {
            llFiltersContainer.setVisibility(View.GONE);
            return;
        }
        if (R.id.lsq_nextButton == view.getId()) {
            FastEditActivity.onImagePiced(tokenPicturePath);
            this.dismissActivityWithAnim();
            return;
        }

        if (R.id.lsq_token_picture == view.getId()) {
            ReviewImagesIntent intent = new ReviewImagesIntent();
            intent.setRequestCode(1);
            intent.setIndex(0);
//            intent.setImages(tokenPicturePath);
            startActivity(ReviewImagesActivity.class, intent);
            return;
        }
        ivNoneCover.setVisibility(View.GONE);
        ivXianCover.setVisibility(View.GONE);
        ivSuCover.setVisibility(View.GONE);
        ivNuanCover.setVisibility(View.GONE);
        ivLiangCover.setVisibility(View.GONE);
        ivRuanCover.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.iv_none:
                TLog.i("无");
                getCamera().switchFilter("");
                ivNoneCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters);
                onSwitchFilter("无");
                break;
            case R.id.iv_xian:
                TLog.i("鲜");
                onSwitchFilter("鲜");
                getCamera().switchFilter("Lightup");
                ivXianCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters_selected);
                break;
            case R.id.iv_su:
                TLog.i("素");
                onSwitchFilter("素");
                getCamera().switchFilter("Olympus");
                ivSuCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters_selected);
                break;
            case R.id.iv_nuan:
                TLog.i("暖");
                onSwitchFilter("暖");
                getCamera().switchFilter("Nan");
                ivNuanCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters_selected);
                break;
            case R.id.iv_liang:
                TLog.i("凉");
                onSwitchFilter("凉");
                getCamera().switchFilter("Yajing");
                ivLiangCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters_selected);
                break;
            case R.id.iv_ruan:
                TLog.i("软");
                onSwitchFilter("软");
                getCamera().switchFilter("Summer");
                ivRuanCover.setVisibility(View.VISIBLE);
                getFilterButton().setImageResource(R.mipmap.ic_camera_filters_selected);
                break;
        }
    }

    private void onSwitchFilter(String name) {
        tvFilterName.setText(name);
        tvFilterName.setVisibility(View.VISIBLE);
        tvFilterName.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFilterName.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public <G extends IntentData> void startActivity(@NonNull Class<? extends BaseActivity<G>> clazz, G data) {
        startActivityForResult(new Intent(getActivity(), clazz).putExtra("IntentData", data), data.getRequestCode());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            return;
        }
        intent.putExtra("origin_baseActivity", getActivity().getClass().getName());
        intent.putExtra("childTags_baseActivity", new ArrayList<>());
        super.startActivityForResult(intent, requestCode);
    }

    public int getCurrentRationType() {
        return currentRationType;
    }

    public static class TempPic implements Parcelable {
        public SYRichTextPhotoModel getModel() {
            return model;
        }

        public void setModel(SYRichTextPhotoModel model) {
            this.model = model;
        }

        private SYRichTextPhotoModel model;

        public TempPic(String[] paths, String filterCode) {
            this.paths = paths;
            this.filterCode = filterCode;
        }

        public TempPic(SYRichTextPhotoModel model) {
            this.paths = new String[]{model.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl(),
                    model.getPhoto().getImageAsset().getProcessedImage() != null ?
                            model.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl() : null};
            this.filterCode = model.getPhoto().getImageFilterName() != null &&
                    model.getPhoto().getImageFilterName().toLowerCase().contains("gehuang")
                    ? null : model.getPhoto().getImageFilterName();
            this.model = model;
        }

        public boolean hasFilter() {
            return !FFUtils.isStringEmpty(filterCode) && !filterCode.equals("Normal");
        }

        public String[] getPaths() {
            return paths;
        }

        public void setPaths(String[] paths) {
            this.paths = paths;
        }

        public String getFilterCode() {
            return filterCode;
        }

        public void setFilterCode(String filterCode) {
            this.filterCode = filterCode;
        }

        private String[] paths;
        private String filterCode;

        public String getPath() {
            if (hasFilter()) {
                return paths[1];
            } else {
                return paths[0];
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.model, flags);
            dest.writeStringArray(this.paths);
            dest.writeString(this.filterCode);
        }

        protected TempPic(Parcel in) {
            this.model = in.readParcelable(SYRichTextPhotoModel.class.getClassLoader());
            this.paths = in.createStringArray();
            this.filterCode = in.readString();
        }

        public static final Creator<TempPic> CREATOR = new Creator<TempPic>() {
            @Override
            public TempPic createFromParcel(Parcel source) {
                return new TempPic(source);
            }

            @Override
            public TempPic[] newArray(int size) {
                return new TempPic[size];
            }
        };
    }


    public static final ArrayList<TempPic> tokenPicturePath = new ArrayList<>();

    public void addImage(Bitmap[] bitmaps, String[] paths, String filterCode) {
        TempPic pic = new TempPic(paths, filterCode);
        tokenPicturePath.add(0, pic);
        lsqTokenPicture.setVisibility(View.VISIBLE);
        tvTokenPictureNum.setVisibility(View.VISIBLE);
        tvTokenPictureNum.setText((tokenPicturePath.size() + FastEditActivity.draft.getFeed().getFood().getRichTextLists().size()) + "");
        if (pic.hasFilter()) {
            lsqTokenPicture.setImageBitmap(bitmaps[1]);
        } else {
            lsqTokenPicture.setImageBitmap(bitmaps[0]);
        }
        lsqNextbutton.setEnabled(true);
    }

    @Override
    public void onResumeFragment() {
        super.onResumeFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshTokenPictureNum();
    }

    public void refreshTokenPictureNum() {
        if (tokenPicturePath.isEmpty() && FastEditActivity.draft.getFeed().getFood().getRichTextLists().isEmpty()) {
            lsqTokenPicture.setVisibility(View.GONE);
            tvTokenPictureNum.setVisibility(View.GONE);
            tvTokenPictureNum.setText("0");
            lsqNextbutton.setEnabled(false);
        } else {
            lsqTokenPicture.setVisibility(View.VISIBLE);
            tvTokenPictureNum.setVisibility(View.VISIBLE);
            tvTokenPictureNum.setText((tokenPicturePath.size() + FastEditActivity.draft.getFeed().getFood().getRichTextLists().size()) + "");
            if (tokenPicturePath.isEmpty()) {
                FFImageLoader.loadSmallImage(null, FastEditActivity.draft.getFeed().getFood().getRichTextLists().get(0).getPhoto().getImageAsset().pullProcessedImageUrl(), lsqTokenPicture);
            } else {
                FFImageLoader.loadSmallImage(null, tokenPicturePath.get(tokenPicturePath.size() - 1).getPath(), lsqTokenPicture);
            }
            lsqNextbutton.setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing()) {
            tokenPicturePath.clear();
        }
    }

    /**
     * 相机控制器委托
     */
    public interface TuCameraFragmentDelegate extends TuSdkComponentErrorListener {
        /**
         * 获取一个拍摄结果
         *
         * @param fragment 默认相机视图控制器
         * @param result   拍摄结果
         */
        void onTuCameraFragmentCaptured(MyCameraFragment fragment, TuSdkResult result);

        /**
         * 获取一个拍摄结果 (异步方法)
         *
         * @param fragment 默认相机视图控制器
         * @param result   拍摄结果
         * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
         */
        boolean onTuCameraFragmentCapturedAsync(MyCameraFragment fragment, TuSdkResult result);

        /**
         * 请求从相机界面跳转到相册界面。只有 设置mDisplayAlbumPoster为true (默认:false) 才会发生该事件
         *
         * @param fragment 系统相册控制器
         */
        void onTuAlbumDemand(MyCameraFragment fragment);
    }

    /**
     * 相册照片列表控制器委托
     */
    private TuCameraFragmentDelegate mDelegate;

    /**
     * 相机控制器委托
     */
    public TuCameraFragmentDelegate getDelegate() {
        return mDelegate;
    }

    /**
     * 相机控制器委托
     */
    public void setDelegate(TuCameraFragmentDelegate mDelegate) {
        this.mDelegate = mDelegate;
        this.setErrorListener(mDelegate);
    }

    /**
     * 相机控制器
     */
    public MyCameraFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TLog.i("filter init:%s", FilterManager.shared().isInited());
//		if (this.getRootViewLayoutId() == 0)
//		{
        this.setRootViewLayoutId(getLayoutId());
//		}
        View view = super.onCreateView(inflater, container, savedInstanceState);
        lsqCameraview = (RelativeLayout) view.findViewById(R.id.lsq_cameraView);
        tvFilterName = (TextView) view.findViewById(R.id.tv_filter_name);
        lsqConfigbar = (LinearLayout) view.findViewById(R.id.lsq_configBar);
        lsqClosebutton = (TuSdkImageButton) view.findViewById(R.id.lsq_closeButton);
        lsqGuidelinebutton = (TuSdkImageButton) view.findViewById(R.id.lsq_guideLineButton);
        lsqFlashbutton = (TuSdkImageButton) view.findViewById(R.id.lsq_flashButton);
        lsqSwitchbutton = (TuSdkImageButton) view.findViewById(R.id.lsq_switchButton);
        lsqNextbutton = (Button) view.findViewById(R.id.lsq_nextButton);
        lsqBottombar = (RelativeLayout) view.findViewById(R.id.lsq_bottomBar);
        lsqAlbumposterview = (TuSdkImageView) view.findViewById(R.id.lsq_albumPosterView);
        lsqCapturebutton = (TuSdkImageButton) view.findViewById(R.id.lsq_captureButton);
        lsqFilterbutton = (TuSdkImageButton) view.findViewById(R.id.lsq_filterButton);
        lsqRatiobutton = (TuSdkImageButton) view.findViewById(R.id.lsq_ratioButton);
        lsqTokenPicture = (ImageView) view.findViewById(R.id.lsq_token_picture);
        tvTokenPictureNum = (TextView) view.findViewById(R.id.tv_token_picture_num);
        llFiltersContainer = (LinearLayout) view.findViewById(R.id.ll_filters_container);
        llFiltersContainer.setVisibility(View.VISIBLE);
        ivNone = (ImageView) view.findViewById(R.id.iv_none);
        ivNoneCover = (FrameLayout) view.findViewById(R.id.iv_none_cover);
        ivXian = (ImageView) view.findViewById(R.id.iv_xian);
        ivXianCover = (FrameLayout) view.findViewById(R.id.iv_xian_cover);
        ivSu = (ImageView) view.findViewById(R.id.iv_su);
        ivSuCover = (FrameLayout) view.findViewById(R.id.iv_su_cover);
        ivNuan = (ImageView) view.findViewById(R.id.iv_nuan);
        ivNuanCover = (FrameLayout) view.findViewById(R.id.iv_nuan_cover);
        ivLiang = (ImageView) view.findViewById(R.id.iv_liang);
        ivLiangCover = (FrameLayout) view.findViewById(R.id.iv_liang_cover);
        ivRuan = (ImageView) view.findViewById(R.id.iv_ruan);
        ivRuanCover = (FrameLayout) view.findViewById(R.id.iv_ruan_cover);
        ivCloseFilters = (ImageView) view.findViewById(R.id.iv_close_filters);
        int[] ids = {R.id.iv_none, R.id.iv_xian, R.id.iv_su, R.id.iv_nuan, R.id.iv_liang, R.id.iv_ruan, R.id.iv_close_filters, R.id.lsq_nextButton, R.id.lsq_token_picture};
        for (int id:ids){
            view.findViewById(id).setOnClickListener(this);
        }
        refreshTokenPictureNum();
        return view;
    }

    /******************************* Config ********************************/

    /**
     * 相机方向
     * {@link CameraFacing}
     */
    private CameraFacing mAvPostion = CameraFacing.Back;
    /**
     * 照片输出分辨率 (默认：1440 * 1920)
     */
    private TuSdkSize mOutputSize;

    /**
     * 闪关灯模式
     *
     * @see # {@link CameraFlash}
     */
    private CameraFlash mDefaultFlashMode;
    /**
     * 触摸聚焦视图ID
     */
    private int mFocusTouchViewId;
    /**
     * 视频视图显示比例
     */
    private float mCameraViewRatio;
    /**
     * 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
     */
    private boolean mOutputImageData;
    /**
     * 禁用系统拍照声音
     */
    private boolean mDisableCaptureSound;
    /**
     * 自定义拍照声音RAW ID，默认关闭系统发声
     */
    private int mCaptureSoundRawId;
    /**
     * 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
     */
    private boolean mAutoReleaseAfterCaptured;
    /**
     * 开启长按拍摄
     */
    private boolean mEnableLongTouchCapture;
    /**
     * 禁用聚焦声音
     */
    private boolean mDisableFocusBeep;
    /**
     * 禁用持续自动对焦
     */
    private boolean mDisableContinueFoucs;
    /**
     * 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
     */
    private boolean mUnifiedParameters = false;
    /**
     * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
     */
    private float mPreviewEffectScale = 0.75f;
    /**
     * 视频覆盖区域颜色 (默认：0xFF000000)
     */
    private int mRegionViewColor = 0xFF000000;
    /**
     * 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
     */
    private boolean mDisableMirrorFrontFacing;
    /**
     * 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册)
     */
    private boolean mDisplayAlbumPoster;
    /**
     * 是否显示辅助线 (默认: false)
     */
    private boolean mDisplayGuideLine;
    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
     * TuFilterOnlineFragmentInterface接口)
     */
    private Class<?> mOnlineFragmentClazz;
    /**
     * 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
     */
    private boolean mEnableFaceDetection;
    /**
     * 是否允许音量键拍照 (默认关闭)
     */
    private boolean mEnableCaptureWithVolumeKeys;

    /**
     * 相机方向 (默认:CameraInfo.CAMERA_FACING_BACK)
     * {@link Camera.CameraInfo}
     */
    @Override
    public CameraFacing getAvPostion() {
        if (mAvPostion == null) {
            mAvPostion = CameraFacing.Back;
        }
        return mAvPostion;
    }

    /**
     * 相机方向 (默认:CameraFacing.Back)
     * {@link CameraFacing}
     */
    public void setAvPostion(CameraFacing mAvPostion) {
        this.mAvPostion = mAvPostion;
    }

    /**
     * 照片输出图片长宽 (默认：全屏)
     */
    public TuSdkSize getOutputSize() {
        return mOutputSize;
    }

    /**
     * 照片输出分辨率
     */
    public void setOutputSize(TuSdkSize mOutputSize) {
        this.mOutputSize = mOutputSize;
    }

    /**
     * 闪关灯模式
     *
     * @return 闪关灯模式 (默认：Camera.Parameters.FLASH_MODE_OFF)
     * @see #
     * {@link CameraFlash}
     */
    public CameraFlash getDefaultFlashMode() {
        if (mDefaultFlashMode == null) {
            mDefaultFlashMode = CameraFlash.Off;
        }
        return mDefaultFlashMode;
    }

    /**
     * 闪关灯模式
     *
     * @param mDefaultFlashMode 闪关灯模式
     * @see #
     * {@link CameraFlash}
     */
    public void setDefaultFlashMode(CameraFlash mDefaultFlashMode) {
        this.mDefaultFlashMode = mDefaultFlashMode;
    }

    /**
     * 触摸聚焦视图ID
     *
     * @return 触摸聚焦视图ID (默认: _impl_component_camera_focus_touch_view)
     * @see #
     * {@link TuSdkVideoCameraExtendViewInterface}
     */
    public int getFocusTouchViewId() {
        if (mFocusTouchViewId == 0) {
            mFocusTouchViewId = TuFocusTouchView.getLayoutId();
        }
        return mFocusTouchViewId;
    }

    /**
     * 触摸聚焦视图ID
     *
     * @param mFocusTouchViewId 触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
     * @see #
     * {@link TuSdkVideoCameraExtendViewInterface}
     */
    public void setFocusTouchViewId(int mFocusTouchViewId) {
        this.mFocusTouchViewId = mFocusTouchViewId;
    }

    /**
     * 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示)
     */
    @Override
    public float getCameraViewRatio() {
        if (mCameraViewRatio < 0) {
            mCameraViewRatio = 0;
        }
        return mCameraViewRatio;
    }

    /**
     * 视频视图显示比例 (默认：0， 0 <= mRegionRatio, 当设置为0时全屏显示)
     */
    public void setCameraViewRatio(float mCameraViewRatio) {
        this.mCameraViewRatio = mCameraViewRatio;
    }

    /**
     * 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
     */
    public boolean isOutputImageData() {
        return mOutputImageData;
    }

    /**
     * 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
     */
    public void setOutputImageData(boolean outputImageData) {
        this.mOutputImageData = outputImageData;
    }

    /**
     * 禁用系统拍照声音 (默认:false)
     */
    public boolean isDisableCaptureSound() {
        return mDisableCaptureSound;
    }

    /**
     * 禁用系统拍照声音 (默认:false)
     */
    public void setDisableCaptureSound(boolean disbleCaptureSound) {
        this.mDisableCaptureSound = disbleCaptureSound;
    }

    /**
     * 自定义拍照声音RAW ID，默认关闭系统发声
     */
    public int getCaptureSoundRawId() {
        return mCaptureSoundRawId;
    }

    /**
     * 自定义拍照声音RAW ID，默认关闭系统发声
     */
    public void setCaptureSoundRawId(int captureSoundRawId) {
        this.mCaptureSoundRawId = captureSoundRawId;
    }

    /**
     * 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
     */
    public boolean isAutoReleaseAfterCaptured() {
        return mAutoReleaseAfterCaptured;
    }

    /**
     * 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
     */
    public void setAutoReleaseAfterCaptured(boolean autoReleaseAfterCaptured) {
        this.mAutoReleaseAfterCaptured = autoReleaseAfterCaptured;
    }

    /**
     * 开启长按拍摄 (默认：false)
     */
    public boolean isEnableLongTouchCapture() {
        return mEnableLongTouchCapture;
    }

    /**
     * 开启长按拍摄 (默认：false)
     */
    public void setEnableLongTouchCapture(boolean enableLongTouchCapture) {
        this.mEnableLongTouchCapture = enableLongTouchCapture;
    }

    /**
     * 禁用聚焦声音 (默认：false)
     */
    public boolean isDisableFocusBeep() {
        return mDisableFocusBeep;
    }

    /**
     * 禁用聚焦声音 (默认：false)
     */
    public void setDisableFocusBeep(boolean disableFocusBeep) {
        this.mDisableFocusBeep = disableFocusBeep;
    }

    /**
     * 禁用持续自动对焦 (默认：false)
     */
    public boolean isDisableContinueFoucs() {
        return mDisableContinueFoucs;
    }

    /**
     * 禁用持续自动对焦 (默认：false)
     */
    public void setDisableContinueFoucs(boolean disableContinueFoucs) {
        this.mDisableContinueFoucs = disableContinueFoucs;
    }

    /**
     * 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
     */
    public boolean isUnifiedParameters() {
        return mUnifiedParameters;
    }

    /**
     * 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
     */
    public void setUnifiedParameters(boolean unifiedParameters) {
        this.mUnifiedParameters = unifiedParameters;
    }

    /**
     * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
     */
    public float getPreviewEffectScale() {
        if (mPreviewEffectScale <= 0) mPreviewEffectScale = 0.75f;
        else if (mPreviewEffectScale > 1) mPreviewEffectScale = 1;
        return mPreviewEffectScale;
    }

    /**
     * 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale <= 1)
     */
    public void setPreviewEffectScale(float mPreviewEffectScale) {
        this.mPreviewEffectScale = mPreviewEffectScale;
    }

    /**
     * 视频覆盖区域颜色 (默认：0xFF000000)
     */
    public int getRegionViewColor() {
        return mRegionViewColor;
    }

    /**
     * 视频覆盖区域颜色 (默认：0xFF000000)
     */
    public void setRegionViewColor(int mRegionViewColor) {
        this.mRegionViewColor = mRegionViewColor;
    }

    /**
     * 是否显示辅助线
     */
    public void setDisplayGuideLine(boolean mDisplayGuideLine) {
        this.mDisplayGuideLine = mDisplayGuideLine;
    }

    /**
     * 是否显示辅助线
     */
    public boolean isDisplayGuideLine() {
        return mDisplayGuideLine;
    }

    /**
     * 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
     */
    public boolean isDisableMirrorFrontFacing() {
        return mDisableMirrorFrontFacing;
    }

    /**
     * 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
     */
    public void setDisableMirrorFrontFacing(boolean mDisableMirrorFrontFacing) {
        this.mDisableMirrorFrontFacing = mDisableMirrorFrontFacing;
    }

    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
     *
     * @see {@link TuFilterOnlineFragmentInterface}
     */
    public Class<?> getOnlineFragmentClazz() {
        if (mOnlineFragmentClazz == null) {
            mOnlineFragmentClazz = TuFilterOnlineFragment.class;
        }
        return mOnlineFragmentClazz;
    }

    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
     *
     * @see {@link TuFilterOnlineFragmentInterface}
     */
    public void setOnlineFragmentClazz(Class<?> mOnlineFragmentClazz) {
        this.mOnlineFragmentClazz = mOnlineFragmentClazz;
    }

    /**
     * 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册)
     */
    public boolean isDisplayAlbumPoster() {
        return mDisplayAlbumPoster;
    }

    /**
     * 是否显示相册照片 (默认: false，如显示，点击照片跳转到相册)
     */
    public void setDisplayAlbumPoster(boolean mDisplayAlbumPoster) {
        this.mDisplayAlbumPoster = mDisplayAlbumPoster;
    }

    /**
     * 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
     */
    public boolean isEnableFaceDetection() {
        return mEnableFaceDetection;
    }

    /**
     * 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
     */
    public void setEnableFaceDetection(boolean enableFaceDetection) {
        this.mEnableFaceDetection = enableFaceDetection;
    }

    /**
     * 是否允许音量键拍照 (默认关闭)
     *
     * @param mEnableCaptureWithVolumeKeys
     */
    public void setEnableCaptureWithVolumeKeys(Boolean mEnableCaptureWithVolumeKeys) {
        this.mEnableCaptureWithVolumeKeys = mEnableCaptureWithVolumeKeys;
    }

    /**
     * 是否允许音量键拍照 (默认关闭)
     *
     * @return
     */
    public boolean isEnableCaptureWithVolumeKeys() {
        return mEnableCaptureWithVolumeKeys;
    }

    /******************************* View ********************************/
    /**
     * 相机视图
     */
    private RelativeLayout mCameraView;
    /**
     * 顶部配置栏
     */
    private ViewGroup mConfigBar;
    /**
     * 关闭按钮
     */
    private TuSdkImageButton mCloseButton;
    /**
     * 闪光灯按钮
     */
    private TuSdkImageButton mFlashButton;
    /**
     * 切换镜头按钮
     */
    private TuSdkImageButton mSwitchButton;
    /**
     * 相机比例切换按钮
     */
    private TuSdkImageButton mRatioButton;
    /**
     * 相机辅助线切换按钮
     */
    private TuSdkImageButton mGuideLineButton;
    /**
     * 滤镜分组视图
     */
    private TuCameraFilterView mGroupFilterView;
    /**
     * 底部栏目
     */
    private RelativeLayout mBottomBar;
    /**
     * 拍摄按钮
     */
    private TuSdkImageButton mCaptureButton;
    /**
     * 滤镜开关按钮
     */
    private TuSdkImageButton mFilterButton;
    /**
     * 滤镜选项视图
     */
    private RelativeLayout mFlashView;
//    /** 自动闪光灯选项 */
//    private TextView mFlashModelAuto;
//    /** 开启闪光灯选项 */
//    private TextView mFlashModelOpen;
//    /** 关闭闪光灯选项 */
//    private TextView mFlashModelOff;
    /**
     * 启动视图
     */
    private ImageView mStartingView;

    /**
     * 相机视图
     */
    @Override
    public RelativeLayout getCameraView() {
        if (mCameraView == null) {
            mCameraView = this.getViewById("lsq_cameraView");
        }
        return mCameraView;
    }

    /**
     * 顶部配置栏
     */
    public ViewGroup getConfigBar() {
        if (mConfigBar == null) {
            mConfigBar = this.getViewById("lsq_configBar");
        }
        return mConfigBar;
    }

    /**
     * 关闭按钮
     */
    public TuSdkImageButton getCloseButton() {
        if (mCloseButton == null) {
            mCloseButton = this.getViewById("lsq_closeButton");
            if (mCloseButton != null) {
                mCloseButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mCloseButton;
    }

    /**
     * 闪光灯按钮
     */
    public TuSdkImageButton getFlashButton() {
        if (mFlashButton == null) {
            mFlashButton = this.getViewById("lsq_flashButton");
            if (mFlashButton != null) {
                mFlashButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mFlashButton;
    }

    /**
     * 切换镜头按钮
     */
    public TuSdkImageButton getSwitchButton() {
        if (mSwitchButton == null) {
            mSwitchButton = this.getViewById("lsq_switchButton");
            if (mSwitchButton != null) {
                mSwitchButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mSwitchButton;
    }

    /**
     * 相机比例切换按钮
     */
    public TuSdkImageButton getRatioButton() {
        if (mRatioButton == null) {
            mRatioButton = this.getViewById("lsq_ratioButton");
            if (mRatioButton != null) {
                mRatioButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mRatioButton;
    }

    /**
     * 相机比例切换按钮
     */
    public TuSdkImageButton getGuideLineButton() {
        if (mGuideLineButton == null) {
            mGuideLineButton = this.getViewById("lsq_guideLineButton");
            if (mGuideLineButton != null) {
                mGuideLineButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mGuideLineButton;
    }

    /**
     * 滤镜分组视图
     */
    public TuCameraFilterView getGroupFilterView() {
        if (mGroupFilterView == null) {
            mGroupFilterView = this.getViewById("lsq_group_filter_view");
            if (mGroupFilterView != null) {
                this.configGroupFilterView(mGroupFilterView);
                // 绑定选择委托
                mGroupFilterView.setDelegate(mFilterBarDelegate);
            }
        }
        return mGroupFilterView;
    }

    /**
     * 配置滤镜栏视图
     */
    protected void configGroupFilterView(GroupFilterBaseView view) {
        if (view == null) return;
        // 设置控制器
        view.setActivity(this.getActivity());
        // 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
        view.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
    }

    /**
     * 底部栏目
     */
    public RelativeLayout getBottomBar() {
        if (mBottomBar == null) {
            mBottomBar = this.getViewById("lsq_bottomBar");
        }
        return mBottomBar;
    }

    /**
     * 拍摄按钮
     */
    public TuSdkImageButton getCaptureButton() {
        if (mCaptureButton == null) {
            mCaptureButton = this.getViewById("lsq_captureButton");
            if (mCaptureButton != null) {
                mCaptureButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mCaptureButton;
    }

    /**
     * 滤镜开关按钮
     */
    public TuSdkImageButton getFilterButton() {
        if (mFilterButton == null) {
            mFilterButton = this.getViewById("lsq_filterButton");
            if (mFilterButton != null) {
                mFilterButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mFilterButton;
    }

    /**
     * 滤镜选项视图
     */
    public RelativeLayout getFlashView() {
        if (mFlashView == null) {
            mFlashView = this.getViewById("lsq_flashView");
            if (mFlashView != null) {
                mFlashView.setOnClickListener(mButtonClickListener);
            }
        }
        return mFlashView;
    }

    /**
     * 启动视图
     */
    public ImageView getStartingView() {
        if (mStartingView == null) {
            mStartingView = this.getViewById("lsq_startingView");
            if (mStartingView != null) {
                mStartingView.setOnClickListener(mButtonClickListener);
            }
        }
        return mStartingView;
    }

    /**
     * 滤镜选择栏委托
     */
    private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate() {
        /**
         * @param view
         *            滤镜分组视图
         * @param itemData
         *            滤镜分组元素
         * @param canCapture
         *            是否允许拍摄
         * @return 是否允许继续执行
         */
        @Override
        public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture) {
            // 直接拍照
            if (canCapture) {
                handleCaptureButton();
                return true;
            }

            switch (itemData.type) {
                case TypeFilter:
                    // 设置滤镜
                    return handleSwitchFilter(itemData.getFilterCode());
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow) {
            if (isShow) return;
            onGroupFilterHidden(view);
        }
    };

    /**
     * 按钮点击事件
     */
    private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener() {
        @Override
        public void onSafeClick(View v) {
            // 分发视图点击事件
            dispatcherViewClick(v);
        }
    };

    /**
     * 分发视图点击事件
     */
    protected void dispatcherViewClick(View v) {
        if (this.equalViewIds(v, this.getCloseButton())) {
            this.handleCloseButton();
        } else if (this.equalViewIds(v, this.getFlashButton())) {
            this.handleFlashButton();
        } else if (this.equalViewIds(v, this.getGuideLineButton())) {
            this.handleGuideLineButton();
        } else if (this.equalViewIds(v, this.getSwitchButton())) {
            this.handleSwitchButton();
        } else if (this.equalViewIds(v, this.getCaptureButton())) {
            Log.i("tag","handleCaptureButton--"+System.currentTimeMillis());
            this.handleCaptureButton();
        }else if (this.equalViewIds(v, this.getFilterButton())) {
             this.handleFilterButton();

        } else if (this.equalViewIds(v, this.getFlashView())) {
            this.handleFlashView();
        } else if (this.equalViewIds(v, this.getRatioButton())) {
            this.handleCameraRatio();
        }
    }

    @Override
    protected void handleCameraRatio() {
        setCurrentRatioType(getNextRatioType());
        getCamera().adapter().changeRegionRatio(RatioType.ratio(currentRationType));
    }

    /**************************
     * loadView
     *****************************/

    @Override
    protected void loadView(ViewGroup view) {
        super.loadView(view);

        this.getCameraView();
        this.getConfigBar();
        this.getCloseButton();
        this.handleFlashModel(this.getDefaultFlashMode());
        this.initDefaultRatio(this.getRatioButton());
        this.showViewIn(this.getSwitchButton(), CameraHelper.cameraCounts() > 1);
        this.getBottomBar();
        this.getCaptureButton();
        // 滤镜分组视图
        this.getGroupFilterView();

        this.showViewIn(this.getFlashView(), false);
        this.getStartingView();

        // 先隐藏滤镜栏，稍后再处理
        if (this.getGroupFilterView() != null) ViewCompat.setAlpha(this.getGroupFilterView(), 0);

        this.setGuideLineButtonState();
    }

    @Override
    protected void viewDidLoad(ViewGroup view) {
        super.viewDidLoad(view);

        if (hasRequiredPermission()) {
            // 延迟加载视图
            ThreadHelper.postDelayed(new Runnable() {
                public void run() {
                    initCameraView();
                }
            }, 0);
        } else {
            requestRequiredPermissions();
        }
    }

    /**
     * 是否已被授予权限
     *
     * @param permissionGranted
     */
    protected void onPermissionGrantedResult(boolean permissionGranted) {
        if (permissionGranted) {
            ThreadHelper.post(new Runnable() {
                @Override
                public void run() {
                    initCameraView();
                }
            });

        } else {

            String msg = TuSdkContext.getString("lsq_carema_no_access", ContextUtils.getAppName(getContext()));

            TuSdkViewHelper.alert(permissionAlertDelegate, this.getContext(), TuSdkContext.getString("lsq_carema_alert_title"),
                    msg, TuSdkContext.getString("lsq_button_close"), TuSdkContext.getString("lsq_button_setting")
            );
        }
    }

    /**
     * 初始化相机及控件, 在 viewDidLoad 之后调用
     */
    @Override
    protected void initCameraView() {
        super.initCameraView();
        lsqNextbutton.setEnabled(FastEditActivity.draft.getFeed().getFood().getRichTextLists().size() != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.isFragmentPause() && this.getCamera() != null) {
            this.getCamera().startCameraCapture();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.getCamera() != null) {
            this.showViewIn(this.getStartingView(), true);
            this.getCamera().stopCameraCapture();
        }
    }

    /**
     * 配置相机参数
     */
    @Override
    protected void configCamera(TuSdkStillCameraInterface camera) {
        // 可选：设置输出图片分辨率
        // 注意：因为移动设备内存问题，可能会限制部分机型使用最高分辨率
        // 请使用 TuSdkGPU.getGpuType().getSize() 查看当前设备所能够进行处理的图片尺寸
        // 默认使用 1920 * 1080分辨率
        camera.setOutputSize(this.getOutputSize());
        // 视频预览显示比例 (默认：0， 0 <= RegionRatio, 当设置为0时全屏显示)
        camera.adapter().setRegionRatio(this.getCurrentRatio());
        // 可选，设置相机手动聚焦
        camera.adapter().setFocusTouchView(this.getFocusTouchViewId());
        // 可选，开启长按拍摄
        camera.adapter().setEnableLongTouchCapture(this.isEnableLongTouchCapture());
        // 禁用聚焦声音 (默认：false)
        camera.adapter().setDisableFocusBeep(this.isDisableFocusBeep());
        // 禁用持续自动对焦 (默认：false)
        camera.adapter().setDisableContinueFoucs(this.isDisableContinueFoucs());
        // 可选，是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
        camera.adapter().setOutputImageData(this.isOutputImageData());
        // 可选，禁用系统拍照声音 (默认:false)
        camera.adapter().setDisableCaptureSound(this.isDisableCaptureSound());
        // 可选，自定义拍照声音RAW ID，默认关闭系统发声
        camera.adapter().setCaptureSoundRawId(this.getCaptureSoundRawId());
        // 可选，自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
        camera.setAutoReleaseAfterCaptured(this.isAutoReleaseAfterCaptured());
        // 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
        camera.setUnifiedParameters(this.isUnifiedParameters());
        // 预览视图实时缩放比例 (默认:0.75, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
        // <= 1)
        camera.setPreviewEffectScale(this.getPreviewEffectScale());
        // 视频覆盖区域颜色 (默认：0xFF000000)
        camera.adapter().setRegionViewColor(this.getRegionViewColor());
        // 是否显示辅助线 (默认: false)
        camera.adapter().setDisplayGuideLine(this.isDisplayGuideLine());
        // 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
        camera.setDisableMirrorFrontFacing(this.isDisableMirrorFrontFacing());
        // 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
        camera.setEnableFaceDetection(this.isEnableFaceDetection());
    }

    /**
     * 设备旋转通知
     */
    @Override
    public void onOrientationChanged(InterfaceOrientation orien) {
        AnimHelper.rotateAnimation(this.getFilterButton(), orien, 200);
        AnimHelper.rotateAnimation(this.getRatioButton(), orien, 200);
        if (this.getGuideLineButton() != null) {
            AnimHelper.rotateAnimation(this.getGuideLineButton(), orien, 200);
        }
        AnimHelper.rotateAnimation(this.getFlashButton(), orien, 200);
        AnimHelper.rotateAnimation(this.getSwitchButton(), orien, 200);
    }

    /************************** camera action *****************************/

    /**
     * 相机状态改变
     */
    protected void onCameraStateChangedImpl(TuSdkStillCameraInterface camera, CameraState state) {
        if (state == CameraState.StateCaptured) {
            this.hubStatus(this.getResString("lsq_carema_image_process"));
            return;
        }

        if (state != CameraState.StateStarted) return;

        this.showViewIn(this.getStartingView(), false);
        this.showViewIn(this.getFlashView(), false);


        if (camera.canSupportFlash())
            camera.setFlashMode(this.getDefaultFlashMode());

        this.getFlashButton().setEnabled(camera.canSupportFlash());


        // 是否允许存储文件
        this.canSaveFile();
        // CameraHelper.logParameters(mCamera.inputCameraParameters());
    }

    /**
     * 获取拍摄图片
     */
    protected void onCameraTakedPictureImpl(TuSdkStillCameraInterface camera, final TuSdkResult result) {
        // 异步处理如果需要保存文件 (默认完成后执行:notifyProcessing(TuSdkResult result))
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                try {
                    asyncProcessingIfNeedSave(result);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 通知处理结果
     *
     * @param result SDK处理结果
     */
    @Override
    protected void notifyProcessing(TuSdkResult result) {
//        this.hubSuccess(this.getResString("lsq_carema_image_process_completed"));
        TuSdk.messageHub().dismiss();
        if (this.mDelegate == null) return;
        this.mDelegate.onTuCameraFragmentCaptured(this, result);
    }

    /**
     * 异步通知处理结果
     *
     * @param result SDK处理结果
     * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
     */
    @Override
    protected boolean asyncNotifyProcessing(TuSdkResult result) {
        if (this.mDelegate == null) return false;
        return this.mDelegate.onTuCameraFragmentCapturedAsync(this, result);
    }

    /**
     * 切换闪光灯模式
     */
    @Override
    protected void handleFlashModel(CameraFlash flashMode) {
        this.handleFlashView();

        this.setDefaultFlashMode(flashMode);
        if (flashMode == null) return;

        int flashIcon = R.mipmap.ic_camera_flash_auto;

        if (flashMode == CameraFlash.On) {
            flashIcon = R.mipmap.ic_camera_flash_open;
        } else if (flashMode == CameraFlash.Off) {
            flashIcon = R.mipmap.ic_camera_flash_close;
        }
        if (this.getFlashButton() != null) {
            this.getFlashButton().setImageResource(flashIcon);
        }

        super.handleFlashModel(flashMode);
    }

    /**
     * 关闭闪光灯选项视图
     */
    protected void handleFlashView() {
        AnimHelper.alphaHidden(this.getFlashView());
    }

    /**
     * 开启闪光灯选项视图
     */
    protected void handleFlashButton() {
        CameraFlash model = null;
        if (getDefaultFlashMode() == CameraFlash.Off) {
            model = CameraFlash.On;
        } else if (getDefaultFlashMode() == CameraFlash.Auto) {
            model = CameraFlash.Off;
        } else {
            model = CameraFlash.Auto;
        }
        handleFlashModel(model);
//        AnimHelper.alphaShow(this.getFlashView());
    }

    /**
     * 切换前后摄像头
     */
    @Override
    protected void handleSwitchButton() {
        this.showViewIn(this.getStartingView(), true);
        super.handleSwitchButton();
    }

    /**
     * 切换辅助线显示
     */
    @Override
    protected void handleGuideLineButton() {
        super.handleGuideLineButton();

        this.setDisplayGuideLine(!this.isDisplayGuideLine());

        setGuideLineButtonState();
    }

    /**
     * 切换滤镜视图显示
     */
    protected void handleFilterButton() {
        llFiltersContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 滤镜组隐藏
     */
    protected void onGroupFilterHidden(TuCameraFilterView view) {
        ViewCompat.animate(this.getBottomBar()).alpha(1).setDuration(80);
    }

    /****************************** CameraRatio ***********************************/

    /**
     * 初始化默认相机显示比例
     */
    private void initDefaultRatio(TuSdkImageButton btn) {
        if (btn == null) return;

        // 设置了固定比例
        if (this.getCameraViewRatio() > 0) {
            this.showViewIn(btn, false);
            return;
        }

        // 设置了固定比例，或者仅有一种比例可选时，不显示比例开关
        if (RatioType.ratioCount(this.getRatioType()) == 1) this.showViewIn(btn, false);

        this.setCurrentRatioType(RatioType.firstRatioType(this.getRatioType()));
    }

    private void setGuideLineButtonState() {
        if (this.getGuideLineButton() == null) return;

        int icon = 0;
        if (this.isDisplayGuideLine()) {
            icon = R.mipmap.ic_nine_open;
        } else {
            icon = R.mipmap.ic_nine_close;
        }

        this.getGuideLineButton().setImageResource(icon);
    }

    private int currentRationType = RatioType.ratio_orgin;


    /**
     * 设置当前比例类型
     */
    @Override
    protected void setCurrentRatioType(int ratioType) {
        super.setCurrentRatioType(ratioType);
        currentRationType = ratioType;
        int ratioIcon;
        switch (ratioType) {
            case RatioType.ratio_3_4:
                ratioIcon = R.mipmap.ic_scale_3_4;
                break;
            case RatioType.ratio_2_3:
                ratioIcon = R.mipmap.ic_scale_2_3;
                break;
            case RatioType.ratio_1_1:
                ratioIcon = R.mipmap.ic_scale_1_1;
                break;
            default:
                ratioIcon = R.mipmap.ic_scale_full;
                break;
        }
        this.getRatioButton().setImageResource(ratioIcon);
    }

    protected int getNextRatioType() {
        switch (currentRationType) {
            case RatioType.ratio_3_4:
                return RatioType.ratio_2_3;
            case RatioType.ratio_2_3:
                return RatioType.ratio_orgin;
            case RatioType.ratio_1_1:
                return RatioType.ratio_3_4;
            default:
                return RatioType.ratio_1_1;
        }
    }

    /**
     * 获取音量键触发事件
     */
    @Override
    public boolean onActivityKeyDispatcher(TuSdkFragmentActivity activity,
                                           int keyCode) {
        if (!this.isEnableCaptureWithVolumeKeys())
            return false;

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                this.handleCaptureWithVolume();
                return true;
            default:
                break;
        }

        return false;
    }
    /**
     * 获取手势放大和缩小操作
     */
    @Override
    public boolean onActivityTouchMotionDispatcher(TuSdkFragmentActivity activity, boolean isZoomIn)
    {
        if(isZoomIn)
        {
            // 放大手势操作
            handleFocalDistance(-1, true);
        }
        else
        {
            // 缩小手势操作
            handleFocalDistance(-1, false);
        }

        return true;
    }
    /**
     * 处理相机焦距变化
     *
     * @param isZoomIn 是否放大焦距
     */
    private void handleFocalDistance(int scale, boolean isZoomIn)
    {
        Camera.Parameters params = ((TuSdkStillCamera)getCamera()).inputCameraParameters();
        if(params == null) return;

        int result = 0;
        if (params.isZoomSupported())
        {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();

            if(scale < 0){
                if (isZoomIn && zoom < maxZoom){
                    zoom++;
                }
                else if (zoom > 0){
                    zoom--;
                }

                result = zoom;
            }else if(scale >= 0 && scale <= params.getMaxZoom()){
                result = scale;
            }
            params.setZoom(result);

            ((TuSdkStillCamera)getCamera()).inputCamera().setParameters(params);
        }
        else
        {
            TLog.e("Device not support Zoom");
        }
    }
}