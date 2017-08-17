package com.fengnian.smallyellowo.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.select_picture.AllFolderImagesActivity;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publish.AliOssUploadUtil;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.R.id.et_word;

public class SuggestActivity extends BaseActivity<IntentData> {

    @Bind(et_word)
    EditText etWord;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.iv_4)
    ImageView iv4;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_big)
    ImageView ivBig;
    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.ll_big)
    LinearLayout llBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        setNotitle(true);
        ButterKnife.bind(this);
        tvNum.setText("");
        findViewById(R.id.btn_commit).setClickable(false);
        findViewById(R.id.btn_commit).setBackgroundResource(R.drawable.logout_press);
        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    tvNum.setText("");
                    findViewById(R.id.btn_commit).setClickable(false);
                    findViewById(R.id.btn_commit).setBackgroundResource(R.drawable.logout_press);
                } else {
                    tvNum.setText(editable.length() + "/500");
                    findViewById(R.id.btn_commit).setClickable(true);
                    findViewById(R.id.btn_commit).setBackgroundResource(R.drawable.login_selector);
                }
            }
        });
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }

    int current = 0;

    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.btn_commit, R.id.iv_back, R.id.iv_del, R.id.ll_big})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
                if (picturePathList.size() == 0) {
                    startActivity(AllFolderImagesActivity.class, new IntentData().setRequestCode(3));
                } else if (picturePathList.size() > 0) {
                    llBig.setVisibility(View.VISIBLE);
                    FFImageLoader.loadBigImage(this, picturePathList.get(current = 0), ivBig);
                    FFUtils.setSoftInputInvis(view.getWindowToken());
                }
                break;
            case R.id.iv_2:
                if (picturePathList.size() == 1) {
                    startActivity(SingleCheckImageActivity.class, new IntentData().setRequestCode(3));
                } else if (picturePathList.size() > 1) {
                    llBig.setVisibility(View.VISIBLE);
                    FFImageLoader.loadBigImage(this, picturePathList.get(current = 1), ivBig);
                    FFUtils.setSoftInputInvis(view.getWindowToken());
                }
                break;
            case R.id.iv_3:
                if (picturePathList.size() == 2) {
                    startActivity(SingleCheckImageActivity.class, new IntentData().setRequestCode(3));
                } else if (picturePathList.size() > 2) {
                    llBig.setVisibility(View.VISIBLE);
                    FFImageLoader.loadBigImage(this, picturePathList.get(current = 2), ivBig);
                    FFUtils.setSoftInputInvis(view.getWindowToken());
                }
                break;
            case R.id.iv_4:
                if (picturePathList.size() == 3) {
                    startActivity(SingleCheckImageActivity.class, new IntentData().setRequestCode(3));
                } else if (picturePathList.size() > 3) {
                    llBig.setVisibility(View.VISIBLE);
                    FFImageLoader.loadBigImage(this, picturePathList.get(current = 3), ivBig);
                    FFUtils.setSoftInputInvis(view.getWindowToken());
                }
                break;
            case R.id.btn_commit:
                commit();
                break;
            case R.id.iv_back:
                llBig.setVisibility(View.GONE);
                ivBig.setImageBitmap(null);
                break;
            case R.id.iv_del:
                llBig.setVisibility(View.GONE);
                picturePathList.remove(current);
                ImageView ivs[] = {iv1, iv2, iv3, iv4};
                for (int i = 0; i < 4; i++) {
                    ivs[i].setVisibility(View.GONE);
                }
                int i = 0;
                for (; i < picturePathList.size(); i++) {
                    ivs[i].setVisibility(View.VISIBLE);
                    FFImageLoader.loadMiddleImage(this, picturePathList.get(i), ivs[i]);
                }
                if (i < 4) {
                    ivs[i].setVisibility(View.VISIBLE);
                    ivs[i].setImageResource(R.mipmap.ic_suggest_add_img);
                }
                break;
        }
    }

    private void commit() {
        if (etWord.getText().toString().trim().length() == 0 && picturePathList.size() == 0) {
            return;
        }
        if (picturePathList.size() == 0) {
//            post(Constants.shareConstants().getNetHeaderAdress() + "/questionFeedback.do", "", null, new FFNetWorkCallBack<BaseResult>() {
            post(IUrlUtils.Search.questionFeedback, "", null, new FFNetWorkCallBack<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response, FFExtraParams extra) {
                    showToast("提交成功");
                    finish();
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "content", etWord.getText().toString().trim(), "picUrls", "");
        } else {
            final int id = showProgressDialog("", false, Integer.MAX_VALUE);
            new Thread() {
                int backCount = 0;
                String url = "";

                @Override
                public void run() {
                    for (int i = 0; i < picturePathList.size(); i++) {
                        SYUploadImage img = new SYUploadImage(){

                            @Override
                            public void uploadFail() {
                                backCount++;
                                dis();
                                showToast("提交失败！");
                            }

                            private void dis() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissProgressDialog(id);
                                    }
                                });
                            }

                            @Override
                            public void uploadSuccess() {
                                backCount++;
                                if (backCount == picturePathList.size()) {
                                    url = url + getImage().getUrl();
//                                    post(Constants.shareConstants().getNetHeaderAdress() + "/questionFeedback.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                                    post(IUrlUtils.Search.questionFeedback, null, null, new FFNetWorkCallBack<BaseResult>() {
                                        @Override
                                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                                            showToast("提交成功");
                                            finish();
                                        }

                                        @Override
                                        public boolean onFail(FFExtraParams extra) {
                                            return false;
                                        }

                                        @Override
                                        public void onBack(FFExtraParams extra) {
                                            dis();
                                        }
                                    }, "content", etWord.getText().toString().trim(), "picUrls", url);
                                } else {
                                    url = url + getImage().getUrl() + ",";
                                }
                            }
                        };
                        SYImage image = new SYImage();
                        image.setUrl(picturePathList.get(i));
                        img.setImage(image);
                        AliOssUploadUtil.getInstance().ossUpload(img);
                    }


                }
            }.start();

        }
    }

    ArrayList<String> picturePathList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 3) {
            picturePathList.add(data.getStringExtra("path"));

            if(picturePathList.size()==0){
                findViewById(R.id.btn_commit).setClickable(false);
                findViewById(R.id.btn_commit).setBackgroundResource(R.drawable.logout_press);

            }else{
                findViewById(R.id.btn_commit).setClickable(true);
                findViewById(R.id.btn_commit).setBackgroundResource(R.drawable.login_selector);
            }
            switch (picturePathList.size()) {
                case 1:
                    FFImageLoader.loadMiddleImage(this, data.getStringExtra("path"), iv1);
                    iv2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    FFImageLoader.loadMiddleImage(this, data.getStringExtra("path"), iv2);
                    iv3.setVisibility(View.VISIBLE);
                    break;
                case 3:

                    FFImageLoader.loadMiddleImage(this, data.getStringExtra("path"), iv3);
                    iv4.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    FFImageLoader.loadMiddleImage(this, data.getStringExtra("path"), iv4);
                    break;
            }
        }
    }

    private void getPopWindow(){
        new EnsureDialog.Builder(SuggestActivity.this)
                .setTitle("提示")//设置对话框标题
                .setMessage("是否放弃本次编辑!")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                        dialog.dismiss();
                        SuggestActivity.this.finish();
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                //nothing
            }

        }).show();//在按键响应事件中显示此对话框
    }

    private boolean isEditing() {
        if (picturePathList.size() > 0 || etWord.length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(View v) {
        if (isEditing()) {
            getPopWindow();
            return;
        }
        super.onBackPressed(v);
    }

    @Override
    public void onBackPressed() {
        if (llBig.getVisibility() == View.VISIBLE) {
            llBig.setVisibility(View.GONE);
            return;
        } else if (isEditing()) {
            getPopWindow();
            return;
        }
        super.onBackPressed();
    }


}
