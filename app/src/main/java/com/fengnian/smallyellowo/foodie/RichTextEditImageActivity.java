package com.fengnian.smallyellowo.foodie;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.fragments.MyEditCuterFragment;
import com.fengnian.smallyellowo.foodie.fragments.MyEditCuterOption;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditImageIntent;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichTextEditImageActivity extends BaseActivity<RichTextEditImageIntent> implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.fl_crop)
    FrameLayout flCrop;
    private String[] filterCode = {"",
            "Lightup", "Olympus", "Yajing",
            "Nan", "Summer"
    };
    @Bind(R.id.rb_filter)
    RadioButton rbFilter;
    @Bind(R.id.rb_crop)
    RadioButton rbCrop;
    @Bind(R.id.rg_tabs)
    RadioGroup rgTabs;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.tv_filter_name)
    TextView tvFilterName;
    @Bind(R.id.activity_rech_text_edit_image)
    RelativeLayout activityRechTextEditImage;
    @Bind(R.id.img_container)
    RelativeLayout imgContainer;
    @Bind(R.id.v_statusBar)
    View vStatusBar;
    @Bind(R.id.iv_none)
    ImageView ivNone;
    @Bind(R.id.iv_none_cover)
    View ivNoneCover;
    @Bind(R.id.iv_xian)
    ImageView ivXian;
    @Bind(R.id.iv_xian_cover)
    View ivXianCover;
    @Bind(R.id.iv_su)
    ImageView ivSu;
    @Bind(R.id.iv_su_cover)
    View ivSuCover;
    @Bind(R.id.iv_nuan)
    ImageView ivNuan;
    @Bind(R.id.iv_nuan_cover)
    View ivNuanCover;
    @Bind(R.id.iv_liang)
    ImageView ivLiang;
    @Bind(R.id.iv_liang_cover)
    View ivLiangCover;
    @Bind(R.id.iv_ruan)
    ImageView ivRuan;
    @Bind(R.id.iv_ruan_cover)
    View ivRuanCover;
    //    @Bind(R.id.tv_filter1)
//    TextView tvFilter1;
//    @Bind(R.id.tv_filter2)
//    TextView tvFilter2;
//    @Bind(R.id.tv_filter3)
//    TextView tvFilter3;
//    @Bind(R.id.tv_filter4)
//    TextView tvFilter4;
//    @Bind(R.id.tv_filter5)
//    TextView tvFilter5;
//    @Bind(R.id.tv_filter6)
//    TextView tvFilter6;
    @Bind(R.id.ll_filter)
    LinearLayout llFilter;
    @Bind(R.id.ll_crop)
    LinearLayout llCrop;
    private SYRichTextPhotoModel imageData;

    private void onSwitchFilter(String name) {
        if (!has) {
            has = true;
            return;
        }
        tvFilterName.setText(name);
        tvFilterName.setVisibility(View.VISIBLE);
        tvFilterName.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFilterName.setVisibility(View.GONE);
            }
        }, 1000);
    }

    /**
     * 没有滤镜的剪切过的原图
     */
    Bitmap mOriginalImage;

    /**
     * 带滤镜的切过的图
     */
    Bitmap mProcessedImage;

    String filterName;

    private MyEditCuterFragment cropFragment;

    private String getOriginalImage() {
        return imageData.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl();
    }

    private String getOriginalCutImage() {
        String path = getOriginalCutImagePath();
        if (new File(path).exists()) {
            return path;
        }
        return getOriginalImage();
    }

    private String getOriginalCutImagePath() {
        return imageData.getPhoto().getImageAsset().pullOriginalCutImagePath();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FFImageLoader.map.trimToSize(0);
        setNotitle(true);
        try {
            imageData = SYDataManager.shareDataManager().draftsOfFirst().getFeed().getFood().getRichTextLists().get(getIntentData().getIndex());
        } catch (Exception e) {
        }
        if (imageData == null || imageData.getPhoto() == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_rich_text_edit_image);
        ButterKnife.bind(this);
        vStatusBar.getLayoutParams().height = FFUtils.getStatusbarHight(this);
        initBottom();

        rgTabs.setOnCheckedChangeListener(this);

        handleFilterItemClick(imageData.getPhoto().getImageFilterName(), false);
        initOriginalCutImage(null);
        String url = imageData.getPhoto().getImageAsset().pullProcessedImageUrl();
        FFImageLoader.loadBigImage(this, url, ivImg, 0);
//        TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
        final int id = showProgressDialog("");
        TuSdk.checkFilterManager(new FilterManager.FilterManagerDelegate() {
            @Override
            public void onFilterManagerInited(FilterManager manager) {
//                TuSdk.messageHub().showSuccess(context(), R.string.lsq_inited);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog(id);
                    }
                });
            }
        });
    }

    boolean has = false;

    private void initOriginalCutImage(final Runnable runn) {
        FFImageLoader.load_base(this, getOriginalCutImage(), null, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                if (bitmap == null) {
                    return;
                }

                mOriginalImage = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                FFImageLoader.map.trimToSize(0);
                if (runn != null) {
                    runn.run();
                }
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        });
    }

    @OnClick({R.id.tv_submit, R.id.iv_none, R.id.iv_xian, R.id.iv_su, R.id.iv_nuan, R.id.iv_liang, R.id.iv_ruan, R.id.iv_rotate, R.id.iv_reset, R.id.iv_scale})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:

                if (cropFragment != null && flCrop.getVisibility() == View.VISIBLE) {
                    final int idd = showProgressDialog("");
                    cropFragment.handleCompleteButton(new MyEditCuterFragment.EditCuterCallback() {
                        @Override
                        public void notifyProcessing(final TuSdkResult result) {
                            new Thread() {
                                @Override
                                public void run() {
                                    if (mOriginalImage != null && !mOriginalImage.isRecycled()) {
                                        mOriginalImage.recycle();
                                    }
                                    if (mProcessedImage != null && !mProcessedImage.isRecycled()) {
                                        mProcessedImage.recycle();
                                    }
                                    mOriginalImage = BitmapHelper.getBitmap(new File(result.imageFile.getPath()));//解析图片
                                    if (!FFUtils.isStringEmpty(filterName)) {
                                        mProcessedImage = FilterManager.shared().process(mOriginalImage, filterName);
                                    } else {
                                        mProcessedImage = mOriginalImage;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissProgressDialog(idd);
                                            ok();
                                        }
                                    });
                                }
                            }.start();
                        }

                    });

                } else {
                    ok();
                }
                break;
            case R.id.iv_none:
            case R.id.iv_none_cover:
                handleFilterItemClick("", true);
                break;
            case R.id.iv_xian:
            case R.id.iv_xian_cover:
                handleFilterItemClick("Lightup", true);
                break;
            case R.id.iv_su:
            case R.id.iv_su_cover:
                handleFilterItemClick("Olympus", true);
                break;
            case R.id.iv_nuan:
            case R.id.iv_nuan_cover:
                handleFilterItemClick("Nan", true);
                break;
            case R.id.iv_liang:
            case R.id.iv_liang_cover:
                handleFilterItemClick("Yajing", true);
                break;
            case R.id.iv_ruan:
            case R.id.iv_ruan_cover:
                handleFilterItemClick("Summer", true);
                break;
            case R.id.iv_rotate:
                if (cropFragment != null) {
                    cropFragment.handleTrunButton();
                }
                break;
            case R.id.iv_reset:
                if (cropFragment != null) {
                    new File(getOriginalCutImagePath()).delete();
                    initOriginalCutImage(new Runnable() {//重新加载图片
                        @Override
                        public void run() {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.remove(cropFragment);
                            cropFragment = MyEditCuterOption.editCuterOption();
                            cropFragment.setImage(mOriginalImage);

                            cropFragment.setCurrentRatioType(RatioType.ratio_orgin);
                            flCrop.setVisibility(View.VISIBLE);
                            ft.add(R.id.fl_crop, cropFragment);
                            ft.commit();
                        }
                    });

                }
                break;
            case R.id.iv_scale:
                if (cropFragment != null) {
                    cropFragment.handleRatioButton();
                }
                break;
        }
    }


    private void ok() {
        if (mProcessedImage != null) {
            String pa = imageData.getPhoto().getImageAsset().pullPrecessedImagePath();
            FFImageUtil.saveBitmap(pa, mProcessedImage);
            FFImageUtil.saveBitmap(imageData.getPhoto().getImageAsset().pullOriginalCutImagePath(), mOriginalImage);
            imageData.getPhoto().getImageAsset().setSYImage(pa, false);
        }

        if (cropFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            cropFragment.hubDismissRightNow();
            ft.remove(cropFragment);
            ft.commit();
        }

        imageData.getPhoto().setImageFilterName(filterName);
        finish();
    }

    @Override
    public void onBackPressed() {
        new EnsureDialog.Builder(this).setMessage("是否放弃编辑？").setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mOriginalImage != null && !mOriginalImage.isRecycled()) {
                    mOriginalImage.recycle();
                    mOriginalImage = null;
                }
                if (mProcessedImage != null && !mProcessedImage.isRecycled()) {
                    mProcessedImage.recycle();
                    mProcessedImage = null;
                }
                RichTextEditImageActivity.super.onBackPressed();
            }
        }).create().show();
    }

    /**
     * 处理点击滤镜事件
     */
    private void handleFilterItemClick(String filterCode, boolean doFilter) {
        View iv;
        if (filterCode == null || filterCode.length() == 0) {
            iv = ivNoneCover;
            filterCode = "";
            onSwitchFilter("无");
        } else if (filterCode.equals("Lightup")) {//iv_xian
            iv = ivXianCover;
            onSwitchFilter("鲜");
        } else if (filterCode.equals("Olympus")) {//su
            iv = ivSuCover;
            onSwitchFilter("素");
        } else if (filterCode.equals("Nan")) {//nuan
            iv = ivNuanCover;
            onSwitchFilter("暖");
        } else if (filterCode.equals("Yajing")) {//liang
            iv = ivLiangCover;
            onSwitchFilter("凉");
        } else if (filterCode.equals("Summer")) {//ruan
            iv = ivRuanCover;
            onSwitchFilter("软");
        } else {
            iv = ivNoneCover;
            filterCode = "";
            onSwitchFilter("无");
        }

        ivNoneCover.setVisibility(View.GONE);
        ivXianCover.setVisibility(View.GONE);
        ivSuCover.setVisibility(View.GONE);
        ivNuanCover.setVisibility(View.GONE);
        ivLiangCover.setVisibility(View.GONE);
        ivRuanCover.setVisibility(View.GONE);

        filterName = filterCode;
        iv.setVisibility(View.VISIBLE);

        if (doFilter) {
            new Thread() {
                @Override
                public void run() {
                            mProcessedImage = FilterManager.shared().process(mOriginalImage, filterName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivImg.setImageBitmap(mProcessedImage);
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        llFilter.setVisibility(View.GONE);
        llCrop.setVisibility(View.GONE);


        if (id != R.id.rb_crop) {
            if (cropFragment != null && flCrop.getVisibility() == View.VISIBLE) {
                final int idd = showProgressDialog("");
                cropFragment.handleCompleteButton(new MyEditCuterFragment.EditCuterCallback() {
                    @Override
                    public void notifyProcessing(final TuSdkResult result) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (mProcessedImage != null && !mProcessedImage.isRecycled()) {
                                    mProcessedImage.recycle();
                                }
                                mOriginalImage = BitmapHelper.getBitmap(new File(result.imageFile.getPath()));//解析图片
                                if (!FFUtils.isStringEmpty(filterName)) {
                                    mProcessedImage = FilterManager.shared().process(mOriginalImage, filterName);
                                } else {
                                    mProcessedImage = mOriginalImage;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivImg.setImageBitmap(mProcessedImage);
                                        dismissProgressDialog(idd);
                                        flCrop.setVisibility(View.GONE);
                                        TuSdk.messageHub().dismiss();
                                    }
                                });
                            }
                        }.start();
                    }
                });

            }
        } else {
            flCrop.setVisibility(View.VISIBLE);
            if (cropFragment == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                cropFragment = MyEditCuterOption.editCuterOption();
                cropFragment.setImage(mOriginalImage);
                cropFragment.handleRatioButton();
                ft.add(R.id.fl_crop, cropFragment);
                ft.commit();
            }
        }
        switch (id) {
            case R.id.rb_filter:
                llFilter.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_crop:
                llCrop.setVisibility(View.VISIBLE);
                break;
        }
    }
}
