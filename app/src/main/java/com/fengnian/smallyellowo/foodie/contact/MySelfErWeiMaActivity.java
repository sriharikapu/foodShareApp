package com.fengnian.smallyellowo.foodie.contact;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapBitmapCreatedListener;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.snap.MyselfErWeiMaSnapAdapter;
import com.fengnian.smallyellowo.foodie.utils.ProductImgUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.fengnian.smallyellowo.foodie.zxing.CreateQRImage;

import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.File;

import static com.fan.framework.config.FFConfig.IMAGE_QUALITY;
import static com.fan.framework.utils.FileUitl.scanFile;

/**
 * Created by Administrator on 2017-2-22.
 */

public class MySelfErWeiMaActivity extends BaseActivity<IntentData> {

    private ImageView iv_avator, iv_erweima;

    private TextView tv_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_erweima);
        iv_avator = findView(R.id.iv_avator);
        tv_name = findView(R.id.tv_name);
        iv_erweima = findView(R.id.iv_erweima);
        SYUser user = SP.getUser();
        FFImageLoader.loadAvatar(this, user.getHeadImage().getUrl(), iv_avator);
        tv_name.setText(user.getNickName());

        String url = getUrl1();
        iv_erweima.setImageBitmap(CreateQRImage.createQRImage(url));
        addMenu(R.mipmap.iv_erweima_fenxiang, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(context()).inflate(
                        R.layout.pop_share, null);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                // 设置按钮的点击事件
                TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
                TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
                TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);

                LinearLayout ll_else = (LinearLayout) contentView.findViewById(R.id.ll_else);
                ll_else.setVisibility(View.GONE);
                //封面
                TextView tv_cover = (TextView) contentView.findViewById(R.id.tv_cover);
                //内容
                LinearLayout ll_else1 = (LinearLayout) contentView.findViewById(R.id.ll_else1);
                ll_else1.setVisibility(View.VISIBLE);
                TextView product_tv_content = (TextView) contentView.findViewById(R.id.tv_content);
                //保存到相册
                TextView tv_save_garrly = (TextView) contentView.findViewById(R.id.tv_save_garrly);
                TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                tv_pyq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.createQrcodeSnap(new MyselfErWeiMaSnapAdapter(MySelfErWeiMaActivity.this, getUrl1()), 0, new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String bitmap) {
                                        if (bitmap != null) {
                                            dismissProgressDialog(id);
                                            WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                        } else {
                                            showToast("生成失败");
                                        }
                                    }
                                });
                            }
                        }.start();

                    }
                });

                tv_py.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.createQrcodeSnap(new MyselfErWeiMaSnapAdapter(MySelfErWeiMaActivity.this, getUrl1()), 0, new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String bitmap) {
                                        if (bitmap != null) {
                                            dismissProgressDialog(id);
                                            WeixinOpen.getInstance().share2weixinpy(context(), bitmap);
                                        } else {
                                            showToast("生成失败");
                                        }
                                    }
                                });
                            }
                        }.start();

                    }
                });

                tv_sina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.createQrcodeSnap(new MyselfErWeiMaSnapAdapter(MySelfErWeiMaActivity.this, getUrl1()), 0, new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String path) {
                                        if (path == null) {
                                            dismissProgressDialog(id);
                                            showToast("生成失败！");
                                            return;
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                final Bitmap bitmap = FFImageUtil.bitmapFromPath(path, Constants.BigImage,Constants.BigImage);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissProgressDialog(id);
                                                        SinaOpen.sharesingle(context(), bitmap, "", "", getUrl1(), true);
                                                    }
                                                });
                                            }
                                        }.start();


                                    }
                                });
                            }
                        }.start();

                    }
                });

                tv_save_garrly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int id = showProgressDialog("");
                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.createQrcodeSnap(new MyselfErWeiMaSnapAdapter(MySelfErWeiMaActivity.this, getUrl1()), 0, new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String bitmap) {
                                        dismissProgressDialog(id);
                                        if (bitmap!= null) {
                                            saveMyBitmap(bitmap);
                                        }else{
                                            showToast("快照生成失败");
                                        }
                                    }
                                });
                            }
                        }.start();

                    }
                });

//                tv_cover.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        if (data.getFood() instanceof NativeRichTextFood) {
////                            showToast("未保存成功的数据不能分享哦!");
////                            return;
////                        }
//
//                        final int id = showProgressDialog("");
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                ProductImgUtils.createQrcodeSnap(null, new DynamicDetailsCoverAdapter(new SYFeed(), MySelfErWeiMaActivity.this, ""), 0, new OnSnapBitmapCreatedListener() {
//                                    @Override
//                                    public void OnSnapBitmapCreatedListener(final Bitmap bitmap) {
//                                        if (bitmap == null) {
//                                            dismissProgressDialog(id);
//                                            showToast("生成失败！");
//                                            return;
//                                        }
//                                        new Thread() {
//                                            @Override
//                                            public void run() {
//                                                final String path = FileUitl.getCacheFileDir() + "/share";
////                                                    FFImageUtil.saveBitmap(path, bitmap);
//                                                BitmapHelper.saveBitmap(new File(path), bitmap, 90);
//                                                bitmap.recycle();
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        dismissProgressDialog(id);
//                                                        WeixinOpen.getInstance().share2weixin(context(), path);
//                                                    }
//                                                });
//                                            }
//                                        }.start();
//
//                                    }
//                                });
//                            }
//                        }.start();
//
//                    }
//                });


//                if (SP.getUser().getId().equals(SP.getUid())) {
//                    tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_del, 0, 0);
//                    tv_del.setText("删除");
//                    tv_del.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            popupWindow.dismiss();
////                            if (PublishModelManager.isNative(data)) {
////                                if (!PublishModelManager.isPublishFail(data)) {
////                                    showToast("发布中动态不可删除");
////                                    return;
////                                }
////                                SYDataManager.shareDataManager().removeTask(((NativeRichTextFood) data.getFood()).getTask());
////                                finish();
////                                return;
////                            }
//                            EnsureDialog.showEnsureDialog(context(), true, "确定删除这条记录?", "删除", "取消", new EnsureDialog.EnsureDialogListener() {
//                                @Override
//                                public void onOk(DialogInterface dialog) {
//                                    del();
//                                }
//
//                                @Override
//                                public void onCancel(DialogInterface dialog) {
//
//                                }
//                            });
//                        }
//
////                        private void del() {
////                            if (!getIntentData().isMineMode()) {
////                                post(Constants.shareConstants().getNetHeaderAdress() + "/food/removeFoodRecordByTypeV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
////                                    @Override
////                                    public void onSuccess(BaseResult response, FFExtraParams extra) {
////                                        BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
////                                        setResult(RESULT_OK);
////                                        finish();
////                                    }
////
////                                    @Override
////                                    public boolean onFail(FFExtraParams extra) {
////                                        return false;
////                                    }
////                                }, "recordId", data.getId(), "type", "2");
////                            } else {
////                                post(Constants.shareConstants().getNetHeaderAdress() + "/notes/removeFoodNoteV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
////                                    @Override
////                                    public void onSuccess(BaseResult response, FFExtraParams extra) {
////                                        BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
////                                        setResult(RESULT_OK);
////                                        finish();
////                                    }
////
////                                    @Override
////                                    public boolean onFail(FFExtraParams extra) {
////                                        return false;
////                                    }
////                                }, "cusId", data.getFoodNoteId(), "recordId", data.getFoodNoteId());
////                            }
////                        }
//                    });
//                } else {
//                    tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_jubao, 0, 0);
//                    tv_del.setText("举报");
//                    tv_del.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            popupWindow.dismiss();
//                            setDialog();
//                        }
//                    });
//                }


                popupWindow.setTouchable(true);

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.i("mengdd", "onTouch : ");

                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });

                // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                // 我觉得这里是API的一个bug
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
                popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
            }
        });
    }

    //保存文件到指定路径
    private void saveMyBitmap(String bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "small_yellow_circle");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "small_yellow_circle" + System.currentTimeMillis() + ".jpg";
        File f = new File(appDir, fileName);
        try {
            new File(bitmap).renameTo(f);
            scanFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrl1() {
        return Constants.shareConstants().getShareUrlProfix() + "/person/info.html?uid=" + SP.getUid();
    }
}
