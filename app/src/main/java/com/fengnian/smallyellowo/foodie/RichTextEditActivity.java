package com.fengnian.smallyellowo.foodie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodTagModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publish.AliOssUploadUtil;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.PublishLimitResult;
import com.fengnian.smallyellowo.foodie.bean.results.PublishResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiEditText;
import com.fengnian.smallyellowo.foodie.emoji.EmojiPanelLayout;
import com.fengnian.smallyellowo.foodie.emoji.EmojiUtils;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.CheckScoreIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.CoverIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditImageIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.release.GalleryAndPhotoActvity;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;
import com.fengnian.smallyellowo.foodie.widgets.MyRecyclerView;
import com.fengnian.smallyellowo.foodie.widgets.SlidingButtonView;
import com.fengnian.smallyellowo.foodie.widgets.XCFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.R.id.tv_name;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_HalfAllow;

public class RichTextEditActivity extends BaseActivity<RichTextEditIntent> implements View.OnClickListener {
    final int INTENT_GET_IMG = 112;
    final int INTENT_CHANGE_IMG = 113;
    public static RichTextEditActivity instance;

    boolean shouldScrollToEnd = false;
    int lastNum = 0;
    //    Object pop;
    DraftModel model;
    @Bind(R.id.tv_actionbar_title)
    TextView tvActionbarTitle;
    @Bind(R.id.tv_review)
    TextView tvReview;
    @Bind(R.id.ll_images)
    MyRecyclerView lvImages;
    @Bind(R.id.s_status_bar)
    View s_status_bar;
    @Bind(R.id.tv_upload_fail)
    TextView tvUploadFail;
    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.rl_upload_fail)
    RelativeLayout rlUploadFail;
    @Bind(R.id.v_uploading_progress)
    View vUploadingProgress;
    @Bind(R.id.tv_loading)
    TextView tvLoading;
    @Bind(R.id.rl_uploading)
    RelativeLayout rlUploading;
    @Bind(R.id.rl_no_net)
    RelativeLayout rlNoNet;
    @Bind(R.id.rl_upload_success)
    RelativeLayout rlUploadSuccess;
    @Bind(R.id.ll_bottomBar)
    View ll_bottomBar;
    private View v_more;
    private ImageView iv_type;
    private CheckBox cb_zc;
    private CheckBox cb_zwc;
    private CheckBox cb_wuc;
    private CheckBox cb_xwc;
    private CheckBox cb_wanc;
    private CheckBox cb_yx;
    private TextView tv_people_num;
    private TextView tv_total_money;
    private RelativeLayout rl_title;
    private ImageView iv_title;
    private ImageView iv_title_arrow;
    private XCFlowLayout et_cjbq;
    private View ll_bg;
    private EditText et_remark;
    private ImageView iv_remark;
    private Button btn_ok_remark;
    private CheckBox cb_gzrwc;
    private CheckBox cb_zmjc;
    private CheckBox cb_gmxwc;
    private CheckBox cb_yrs;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                uploadAll();
            }
        }
    };
    private EmojiPanelLayout emoji_panel;
    private View fl_emoji_btn_container;
    public static PublishModel task;
    private CompoundButton.OnCheckedChangeListener checkChangeListener;

    class UploadResult {
        int total;
        int uploadingNum;
        int uploadSuccessNum;
        int uploadFailNum;
        int uploadInitNum;

        public void inxit() {
            for (SYRichTextPhotoModel richTextPhotoModel : model.getFeed().getFood().getRichTextLists()) {
                if (!richTextPhotoModel.isTextPhotoModel()) {
                    continue;
                }
                total++;
                if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                        !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())) {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                        uploadingNum++;
                    } else if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                        uploadFailNum++;
                    } else if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        uploadSuccessNum++;
                    } else if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_INIT) {
                        uploadInitNum++;
                    }
                }
            }
        }
    }


    private void onNetStatusChange(boolean justUploadFinish) {
        boolean isConnect = FFUtils.checkNet();
        rlNoNet.setVisibility(View.GONE);
        rlUploadFail.setVisibility(View.GONE);
        rlUploading.setVisibility(View.GONE);
        s_status_bar.setVisibility(View.VISIBLE);
        UploadResult result = new UploadResult();

        result.inxit();
//        if (result.uploadSuccessNum == result.total) {
//            tvReview.setEnabled(true);
//        } else {
//            tvReview.setEnabled(false);
//        }
        if (isConnect) {
            if (result.uploadingNum > 0) {
                rlUploading.setVisibility(View.VISIBLE);
                tvLoading.setText("正在上传图片    " + result.uploadSuccessNum + "  /  " + result.total);
                vUploadingProgress.getLayoutParams().width = FFUtils.getDisWidth() * result.uploadSuccessNum / result.total;
            } else if (result.uploadFailNum > 0) {
                rlUploadFail.setVisibility(View.VISIBLE);
                tvUploadFail.setText(result.uploadFailNum + "  张图片上传失败");
                tvRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadAll();
                    }
                });
            } else if (justUploadFinish) {
                FFUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onNetStatusChange(false);
                    }
                }, 500);
                rlUploadSuccess.setVisibility(View.VISIBLE);
            } else {
                s_status_bar.setVisibility(View.GONE);
            }
        } else {
            rlUploadFail.setVisibility(View.VISIBLE);
            rlNoNet.setVisibility(View.VISIBLE);
        }
    }

    private void uploadAll() {
        if (FFUtils.checkNet()) {
            boolean has = false;
            for (SYRichTextPhotoModel richTextPhotoModel : model.getFeed().getFood().getRichTextLists()) {
                if (!richTextPhotoModel.isTextPhotoModel()) {
                    continue;
                }
                if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                        !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                        (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL
                                || richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_INIT)) {
                    AliOssUploadUtil.getInstance().ossUpload(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage());
                    has = true;
                }
            }
            if (has) {
                adapter.notifyDataSetChanged();
            }
        }
        onNetStatusChange(false);
    }

    public void onOnePictureUploadStatusChange(SYUploadImage img) {
        if (model == null) {
            return;
        }
        try {
            boolean has = false;
            for (SYRichTextPhotoModel richTextPhotoModel : model.getFeed().getFood().getRichTextLists()) {
                if (!richTextPhotoModel.isTextPhotoModel()) {
                    continue;
                }
                try {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() == img) {
                        has = true;
                        break;
                    }
                } catch (Exception e) {
                    FFLogUtil.e("上传图片进度展示", e);
                }
            }
            if (has && adapter != null) {
                adapter.notifyDataSetChanged();
                onNetStatusChange(true);
            }
        } catch (Exception e) {
            FFLogUtil.e("上传图片进度展示", e);
        }
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    /**
     * 本次是否提示，需要结合积分检查状态
     */
    private boolean bPromptError = true;

    private void setbPromptError(boolean bPromptError1) {
        bPromptError = bPromptError1;
    }

    private boolean isbPromptError() {
        return bPromptError;
    }


    private RichTextEditAdapter adapter;
    private TextWatcher watcher_money = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            double num = 0;
            if (s != null && s.length() > 0) {
                num = Double.parseDouble(s.toString());
            }
            model.getFeed().getFood().setTotalPrice(num);
            refreshMoney(false);
        }
    };

    @SuppressWarnings("ResourceType")
    public void enter() {//cap5
        String str = et_remark.getText().toString().trim();
        if (str.length() != 0) {
            model.getFeed().getTags().add(new SYFoodTagModel(str));
        }

        et_remark.setText("");
        et_cjbq.removeAllViews();
        if (model.getFeed().getTags().isEmpty()) {
            iv_remark.setImageResource(R.mipmap.ic_remark_normal);
            TextView cb = new TextView(this);
            cb.setGravity(Gravity.CENTER);
            cb.setText("场景标签");
            cb.setTextColor(getResources().getColor(R.color.ff_text_gray));
            cb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, FFUtils.getPx(23));
            params.setMargins(FFUtils.getPx(3.5f), FFUtils.getPx(3.5f), FFUtils.getPx(3.5f), FFUtils.getPx(3.5f));
            cb.setLayoutParams(params);
            et_cjbq.addView(cb);
            return;
        }
        iv_remark.setImageResource(R.mipmap.ic_remark_selected);

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new EnsureDialog.Builder(context()).setMessage("确定删除此标签吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.getFeed().getTags().remove((int) v.getTag());
                        cb_gzrwc.setOnCheckedChangeListener(null);
                        cb_gmxwc.setOnCheckedChangeListener(null);
                        cb_zmjc.setOnCheckedChangeListener(null);
                        cb_yrs.setOnCheckedChangeListener(null);
                        cb_gzrwc.setChecked(false);
                        cb_zmjc.setChecked(false);
                        cb_gmxwc.setChecked(false);
                        cb_yrs.setChecked(false);
                        for (SYFoodTagModel tagModel : model.getFeed().getTags()) {
                            if (tagModel.getId().equals("1")) {
                                cb_gzrwc.setChecked(true);
                            } else if (tagModel.getId().equals("2")) {
                                cb_zmjc.setChecked(true);
                            } else if (tagModel.getId().equals("3")) {
                                cb_gmxwc.setChecked(true);
                            } else if (tagModel.getId().equals("4")) {
                                cb_yrs.setChecked(true);
                            }
                        }
                        cb_gzrwc.setOnCheckedChangeListener(checkChangeListener);
                        cb_gmxwc.setOnCheckedChangeListener(checkChangeListener);
                        cb_zmjc.setOnCheckedChangeListener(checkChangeListener);
                        cb_yrs.setOnCheckedChangeListener(checkChangeListener);
                        dialog.dismiss();
                        enter();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            }
        };
        int i = 0;
        for (SYFoodTagModel string : model.getFeed().getTags()) {
            TextView cb = new TextView(this);
            cb.setBackgroundResource(R.drawable.food_type_bg_checked);
            cb.setText(string.getContent());
            cb.setPadding(FFUtils.getPx(8), 0, FFUtils.getPx(8), 0);
            cb.setGravity(Gravity.CENTER);
            cb.setTag(i);
            cb.setTextColor(getResources().getColor(R.color.ff_text_black));
            cb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            cb.setOnLongClickListener(listener);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, FFUtils.getPx(23));
            params.setMargins(FFUtils.getPx(3.5f), FFUtils.getPx(3.5f), FFUtils.getPx(3.5f), FFUtils.getPx(3.5f));
            cb.setLayoutParams(params);
            et_cjbq.addView(cb);
            i++;
        }
    }


    public void findViews() {
        v_more = findViewById(R.id.v_more);
        iv_type = (ImageView) findViewById(R.id.iv_type);
        cb_zc = (CheckBox) findViewById(R.id.cb_zc);
        cb_zwc = (CheckBox) findViewById(R.id.cb_zwc);
        cb_wuc = (CheckBox) findViewById(R.id.cb_wuc);
        cb_xwc = (CheckBox) findViewById(R.id.cb_xwc);
        cb_wanc = (CheckBox) findViewById(R.id.cb_wanc);
        cb_yx = (CheckBox) findViewById(R.id.cb_yx);
        tv_people_num = (TextView) findViewById(R.id.tv_people_num);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title_arrow = (ImageView) findViewById(R.id.iv_title_arrow);
        et_cjbq = (XCFlowLayout) findViewById(R.id.et_cjbq);
        btn_ok_remark = (Button) findViewById(R.id.btn_ok_remark);
        ll_bg = findViewById(R.id.ll_bg);
        iv_remark = (ImageView) findViewById(R.id.iv_remark);
        et_remark = (EditText) findViewById(R.id.et_remark);
        cb_gzrwc = (CheckBox) findViewById(R.id.cb_gzrwc);
        cb_zmjc = (CheckBox) findViewById(R.id.cb_zmjc);
        cb_gmxwc = (CheckBox) findViewById(R.id.cb_gmxwc);
        cb_yrs = (CheckBox) findViewById(R.id.cb_yrs);
        for (SYFoodTagModel tagModel : model.getFeed().getTags()) {
            if (tagModel.getId().equals("1")) {
                cb_gzrwc.setChecked(true);
            } else if (tagModel.getId().equals("2")) {
                cb_zmjc.setChecked(true);
            } else if (tagModel.getId().equals("3")) {
                cb_gmxwc.setChecked(true);
            } else if (tagModel.getId().equals("4")) {
                cb_yrs.setChecked(true);
            }
        }
        checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int index = 0;
                switch (buttonView.getId()) {
                    case R.id.cb_gzrwc:
                        index = 0;
                        break;
                    case R.id.cb_zmjc:
                        index = 1;
                        break;
                    case R.id.cb_gmxwc:
                        index = 2;
                        break;
                    case R.id.cb_yrs:
                        index = 3;
                        break;
                }
                SYFoodTagModel e = SP.getConfig().getConfig().getTags().get(index);
                if (isChecked) {
                    model.getFeed().getTags().add(e);
                } else {
                    for (SYFoodTagModel tagModel : model.getFeed().getTags()) {
                        if (tagModel.getId().equals(e.getId())) {
                            model.getFeed().getTags().remove(tagModel);
                            break;
                        }
                    }
                }
                enter();
            }
        };
        cb_gzrwc.setOnCheckedChangeListener(checkChangeListener);
        cb_gmxwc.setOnCheckedChangeListener(checkChangeListener);
        cb_zmjc.setOnCheckedChangeListener(checkChangeListener);
        cb_yrs.setOnCheckedChangeListener(checkChangeListener);
        et_remark.setFilters(new InputFilter[]{new EnglishCharFilter(8 * 2)});
        ll_bg.setVisibility(View.GONE);
        btn_ok_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_remark.getText().toString();
                if (FFUtils.containsEmoji(str)) {
                    showToast("禁止输入表情！");
                    return;
                }
//                ll_bg.setVisibility(View.GONE);
                enter();
            }
        });
        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btn_ok_remark.setEnabled(false);
                } else {
                    btn_ok_remark.setEnabled(true);
                }
            }
        });
        et_cjbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bg.setVisibility(View.VISIBLE);
                et_remark.requestFocus();
                FFUtils.setSoftInputVis(et_remark, true);
            }
        });

        et_remark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ll_bg.setVisibility(View.GONE);
                } else {
                    emoji_panel.setNoShowThis(true);
                }
            }
        });

        View.OnFocusChangeListener l = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText et = (EditText) view;
                et.setSelection(et.length());
                if (b) {
                    emoji_panel.setNoShowThis(true);
                }
                if (view == et_remark) {
                    if (b) {
                        ll_bg.setVisibility(View.VISIBLE);
                    } else {
                        enter();
                        ll_bg.setVisibility(View.GONE);
                    }
                } else {
                    if (b) {
                        setFocusView(view);
                    }
                }
            }
        };

        enter();

        tv_people_num.setOnFocusChangeListener(l);
        tv_total_money.setOnFocusChangeListener(l);
        et_remark.setOnFocusChangeListener(l);

        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            CompoundButton lastView;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_NONE);
                if (isChecked) {
                    if (lastView != null && lastView != buttonView) {
                        lastView.setChecked(false);
                    }
                    lastView = buttonView;

                    switch (lastView.getId()) {
                        case R.id.cb_zc:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_ZC);
                            break;
                        case R.id.cb_zwc:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_ZWC);
                            break;
                        case R.id.cb_wuc:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_WUC);
                            break;
                        case R.id.cb_xwc:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_XWC);
                            break;
                        case R.id.cb_wanc:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_WANC);
                            break;
                        case R.id.cb_yx:
                            model.getFeed().getFood().setFoodType(SYRichTextFood.TYPE_YX);
                            break;
                    }
                    if (model.getFeed().getFood().getFoodType() == 0) {
                        iv_type.setImageResource(R.mipmap.food_type_disable);
                    } else {
                        iv_type.setImageResource(R.mipmap.food_type_enable);
                    }
                } else {
                    if (lastView == buttonView) {
                        model.getFeed().getFood().setFoodType(0);
                        iv_type.setImageResource(R.mipmap.food_type_disable);
                    }
                }


            }
        };
        cb_zc.setOnCheckedChangeListener(changeListener);
        cb_zwc.setOnCheckedChangeListener(changeListener);
        cb_wuc.setOnCheckedChangeListener(changeListener);
        cb_xwc.setOnCheckedChangeListener(changeListener);
        cb_wanc.setOnCheckedChangeListener(changeListener);
        cb_yx.setOnCheckedChangeListener(changeListener);

        rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//去设置封面和标题
//                v_more.setVisibility(View.GONE);
                startActivity(CoverActivity.class, new CoverIntent());
            }
        });

        tv_people_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                if (s != null && s.length() > 0) {
                    num = Integer.parseInt(s.toString());
                }
                model.getFeed().getFood().setNumberOfPeople(num);
                refreshPeopleNum(false);
            }
        });
        tv_total_money.addTextChangedListener(watcher_money);
    }

    //    private View editingTextView;
    private Timer time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        instance = this;
        getIsScore();
        addChildTag(ActivityTags.rich_edit);
        setNotitle(true);

        if (getIntentData().getType() == RichTextEditIntent.TYPE_EDIT_NEW) {
            SYDataManager.shareDataManager().removeAllDrafts();
        }
        model = DraftModelManager.getCurrentDraft();
        model.setShareToSmallYellowO(true);
        if (getIntentData().getModelIndex() > 0) {
            model.getFeed().setReleaseTemplate(RichTextModelManager.getConfigByIndex(getIntentData().getModelIndex()));
        }
        if (getIntentData().getType() == RichTextEditIntent.TYPE_EDIT_NEW) {
            GalleryInfo info = new GalleryInfo();
            info.setRequestCode(INTENT_GET_IMG);
            info.setIsmemo(GalleryInfo.Gallery_pic);
            info.setNum(model.getFeed().getFood().allImageContent().size());
            info.setTemplate_type(model.getFeed().getReleaseTemplate().indexCode);
            startActivity(GalleryAndPhotoActvity.class, info);
        }
        if (model.hotDishList == null && model.getFeed().getFood().getPoi() != null && !FFUtils.isStringEmpty(model.getFeed().getFood().getPoi().getId())) {
            post(IUrlUtils.Search.queryShopDishesV250, null, null, new FFNetWorkCallBack<AddRestActivity.ShopDishResult>() {
                @Override
                public void onSuccess(AddRestActivity.ShopDishResult response, FFExtraParams extra) {
                    DraftModelManager.getCurrentDraft().hotDishList = response.getList();
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "id", model.getFeed().getFood().getPoi().getId());
        }

        setContentView(R.layout.activity_rich_text_edit);
        ButterKnife.bind(this);
        LinearLayout parent = (LinearLayout) getContainer().getParent().getParent();
        emoji_panel = (EmojiPanelLayout) getLayoutInflater().inflate(R.layout.view_input_emoji, parent, false);
        fl_emoji_btn_container = findViewById(R.id.fl_emoji_btn_container);
        ImageView emoji_panel_show = (ImageView) findViewById(R.id.emoji_panel_show);
        emoji_panel.setAttachedEmojiBtn(emoji_panel_show);

        emoji_panel.setOnKeyboardStateListener(new EmojiPanelLayout.onKeyboardStateListener() {

            @Override
            public void onRequestSoftVis() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) getContainer().getParent()).getLayoutParams();
                params.height = FFUtils.getDisHight() - FFUtils.getStatusbarHight(context()) - EmojiUtils.getEmojiKeyboardHeight();
                params.weight = 0;
            }

            @Override
            public void onKeyboardShow(boolean isShow) {
                if (fl_emoji_btn_container.getVisibility() != View.VISIBLE) {
                    return;
                }
                if (isSoftInputVis) {
                    scrollToTop();
                }
            }

            @Override
            public void onFixLayoutParam(boolean isFix) {
                if (isFix) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) getContainer().getParent()).getLayoutParams();
                    params.height = FFUtils.getDisHight() - FFUtils.getStatusbarHight(context()) - EmojiUtils.getEmojiKeyboardHeight();
                    params.weight = 0;
                } else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) getContainer().getParent()).getLayoutParams();
                    params.height = 0;
                    params.weight = 1;
                }
            }
        });
        parent.addView(emoji_panel, 1);
        findViews();
        s_status_bar.getLayoutParams().height = FFUtils.getStatusbarHight(this);
        adapter = new RichTextEditAdapter();
        lvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvImages.setAdapter(adapter);
        lvImages.setItemAnimator(new DefaultItemAnimator());
        lvImages.setOnOutFoucusViewActionDownListener(new MyRecyclerView.OnOutFoucusViewActionDownListener() {
            @Override
            public void OnOutFoucusViewActionDown() {
                if (lvImages.getFocusView() != null && lvImages.getFocusView() instanceof SlidingButtonView && ((SlidingButtonView) lvImages.getFocusView()).getOpen()) {
                    ((SlidingButtonView) lvImages.getFocusView()).closeMenu();
                } else {
                    adapter.notifyDataSetChanged();
                    FFUtils.setSoftInputInvis(lvImages.getWindowToken());
                    emoji_panel.setVisibility(View.GONE);
                    fl_emoji_btn_container.setVisibility(View.GONE);
                }
            }
        });

        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (model.getFeed() != null && model.getFeed().getFood().allImageContent().size() > 0)
                    SYDataManager.shareDataManager().addDraft(model);
            }
        }, 0, 3000);

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void OnOutFoucusViewActionDown() {
        if (lvImages.getFocusView() != null && lvImages.getFocusView() instanceof SlidingButtonView) {
            ((SlidingButtonView) lvImages.getFocusView()).closeMenu();
            return;
        }
        FFUtils.setSoftInputInvis(lvImages.getWindowToken());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        time.cancel();
        unregisterReceiver(receiver);
        instance = null;
        task = null;
        super.onDestroy();
    }


    class ImgHolder extends TextHolder {
        @Bind(R.id.iv_img)
        CircleImageView ivImg;
        @Bind(R.id.tv_dish_name)
        TextView tvDishName;
        @Bind(R.id.rl_text_container)
        RelativeLayout rlTextContainer;
        @Bind(R.id.et_dish)
        EditText etDish;
        @Bind(R.id.iv_clear_dish)
        ImageView ivClearDish;
        @Bind(R.id.rv_hot_dish)
        RecyclerView rvHotDish;
        @Bind(R.id.rl_dish_container)
        RelativeLayout rlDishContainer;
        @Bind(R.id.et_dish1)
        EditText etDish1;
        @Bind(R.id.iv_clear_dish1)
        ImageView ivClearDish1;
        @Bind(R.id.rl_dish_container1)
        RelativeLayout rlDishContainer1;
        @Bind(R.id.iv_good)
        TextView ivGood;
        @Bind(R.id.iv_normal)
        TextView ivNormal;
        @Bind(R.id.iv_bad)
        TextView ivBad;
        @Bind(R.id.rl_comment_container)
        LinearLayout rlCommentContainer;
        @Bind(R.id.iv_comment)
        ImageView ivComment;
        @Bind(R.id.iv_text)
        ImageView ivText;
        @Bind(R.id.iv_dish)
        ImageView ivDish;
        @Bind(R.id.line_text)
        View lineText;
        @Bind(R.id.rl_upload_button)
        View rl_upload_button;
        @Bind(R.id.pb_uploading)
        ProgressBar pb_uploading;
        @Bind(R.id.iv_retry)
        ImageView iv_retry;
        @Bind(R.id.tv_upload_status)
        TextView tv_upload_status;
        @Bind(R.id.iv_dish_ok1)
        ImageView iv_dish_ok1;
        @Bind(R.id.iv_dish_ok)
        ImageView iv_dish_ok;


        @Bind(R.id.rl_img)
        View rl_img;
        @Bind(R.id.tv_process_img)
        TextView tv_process_img;
        @Bind(R.id.tv_reselect_img)
        TextView tv_reselect_img;

        ImgHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            rlDishContainer.setOnClickListener(l);
            rlDishContainer1.setOnClickListener(l);
            rlCommentContainer.setOnClickListener(l);

            View.OnClickListener l1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FFUtils.setSoftInputInvis(v.getWindowToken());
                }
            };
            iv_dish_ok1.setOnClickListener(l1);
            iv_dish_ok.setOnClickListener(l1);

            etDish1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    RecyclerView.Adapter adapter = rvHotDish.getAdapter();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    class TextHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_del)
        ImageView ivDel;
        @Bind(R.id.et_content)
        EditText etContent;
        @Bind(R.id.tv_text_num)
        TextView tvTextNum;

        public TextHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (this instanceof ImgHolder) {
                view.findViewById(R.id.rl_content).getLayoutParams().width = FFUtils.getDisWidth() - FFUtils.getPx(18);
            } else {
                etContent.getLayoutParams().width = FFUtils.getDisWidth() - FFUtils.getPx(18);
            }
            final SlidingButtonView sbv = (SlidingButtonView) itemView.findViewById(R.id.rl_ugc_edit_item);
            sbv.setParent(lvImages);
            sbv.setSlidingButtonListener(new SlidingButtonView.IonSlidingButtonListener() {
                @Override
                public void onMenuIsOpen(View view) {
                    sbv.openMenu();
                }

                @Override
                public void onDownOrMove(SlidingButtonView slidingButtonView) {
                    sbv.closeMenu();
                }
            });


        }
    }

    class RestHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView tv_name;


        public RestHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }


    private class RichTextEditAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
//            boolean canAdd = isCanAdd();
//            return model.getFeed().getFood().getRichTextLists().size() + 3 + (canAdd ? model.getFeed().getFood().getRichTextLists().size() : 0);
            return model.getFeed().getFood().getRichTextLists().size() + 3;
        }
//
//        private boolean isCanAdd() {
//
//            boolean canAddText = isCanAddText();
//
//            boolean canAddImg = isCanAddImg();
//
//            return canAddImg || canAddText;
//        }
//
//        private boolean isCanAddImg() {
//            boolean canAddImg;
//            int maxPictureCount = model.getFeed().getReleaseTemplate().picture.pictureCount;
//            int pictureCount = model.getFeed().getFood().allImageContent().size();
//            if (maxPictureCount != -1 && pictureCount >= maxPictureCount) {
//                canAddImg = false;
//            } else {
//                canAddImg = true;
//            }
//            return canAddImg;
//        }
//
//        private boolean isCanAddText() {
//            boolean canAddText;
//            if (model.getFeed().getReleaseTemplate().textWindow.textWindowCount == 1) {
//                canAddText = false;
//            } else {
//                canAddText = true;
//            }
//            return canAddText;
//        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView;
            switch (viewType) {
                case 0: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_add_rest, parent, false);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(AddRestActivity.class, new IntentData());
                        }
                    });
                    return new RecyclerView.ViewHolder(convertView) {
                    };
                }
                case 1: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_rest, parent, false);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(AddRestActivity.class, new IntentData());
                        }
                    };

                    convertView.findViewById(tv_name).setOnClickListener(listener);
                    convertView.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//删除

                            EnsureDialog.showEnsureDialog(context(), true, "确定要清空商户位置吗?", "确定", null, "取消", new EnsureDialog.EnsureDialogListener() {
                                @Override
                                public void onOk(DialogInterface dialog) {
                                    dialog.dismiss();
                                    model.hotDishList = null;
                                    model.getFeed().getFood().setPoi(null);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    return new RestHolder(convertView);
                }
                case 2: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_img, parent, false);
                    return new ImgHolder(convertView);
                }
                case 3: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_text, parent, false);
                    return new TextHolder(convertView);
                }
                case 4: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_rating, parent, false);
                    final TextView tv_level = (TextView) convertView.findViewById(R.id.tv_level);
                    final RatingBar rb_level = (RatingBar) convertView.findViewById(R.id.rb_level);
                    final ImageView ic_help = (ImageView) convertView.findViewById(R.id.ic_help);
                    final View rl_more = convertView.findViewById(R.id.rl_more);
                    rl_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v_more.setVisibility(View.VISIBLE);
                            iv_remark.post(new Runnable() {
                                @Override
                                public void run() {
                                    et_cjbq.getLayoutParams().width = FFUtils.getDisWidth() - FFUtils.getPx(30) - iv_remark.getWidth();
                                    enter();
                                }
                            });
                        }
                    });
                    rb_level.setRating(model.getFeed().getStarLevel());
                    if (model.getFeed().getStarLevel() == 0) {
                        tv_level.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_edit_ragine_disable, 0, 0, 0);
                        tv_level.setTextColor(getResources().getColor(R.color.ff_text_gray));
                    } else {
                        tv_level.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_edit_ragine_enable, 0, 0, 0);
                        tv_level.setTextColor(getResources().getColor(R.color.ff_text_black));
                    }
                    rb_level.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            model.getFeed().setStarLevel((int) v);
                            switch ((int) v) {
                                case 0:
                                    rb_level.setRating(1);
                                    model.getFeed().setStarLevel(1);
                                    break;
                            }
                            tv_level.setText(model.getFeed().pullStartLevelString());
                            tv_level.setTextColor(getResources().getColor(R.color.ff_text_black));
                            tv_level.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_edit_ragine_enable, 0, 0, 0);
                        }
                    });
                    tv_level.setText(model.getFeed().pullStartLevelString());
                    ic_help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View contentView = getLayoutInflater().inflate(
                                    R.layout.dialog_rating_help, null);
                            // 设置按钮的点击事件
                            final PopupWindow popupWindow = new PopupWindow(contentView,
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                            contentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });

                            contentView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                            popupWindow.setTouchable(true);

                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    return false;
                                    // 这里如果返回true的话，touch事件将被拦截
                                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                                }
                            });

                            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                            // 我觉得这里是API的一个bug
                            popupWindow.setBackgroundDrawable(new ColorDrawable(0));

                            // 设置好参数之后再show
                            popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
                        }
                    });
                    return new RecyclerView.ViewHolder(convertView) {
                    };
                }
                case 5: {
                    convertView = getLayoutInflater().inflate(R.layout.item_ugc_edit_title, parent, false);

                    return new TitleHolder(convertView);
                }
//                case 6: {
//                    convertView = getLayoutInflater().inflate(R.layout.item_add_img_and_text, parent, false);
//                    return new AddHolder(convertView);
//                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, final int position) {
            if (h instanceof TitleHolder) {
                ((TitleHolder) h).onBind();
            } else if (h instanceof TextHolder) {
                TextHolder holder = (TextHolder) h;
//                if (isCanAdd()) {
//                    if (position == 3) {
//                        holder.itemView.setPadding(0, FFUtils.getPx(6), 0, 0);
//                    } else {
//                        holder.itemView.setPadding(0, 0, 0, 0);
//                    }
//                } else {
                holder.itemView.setPadding(0, FFUtils.getPx(6), 0, FFUtils.getPx(6));
//                }
                final SYRichTextPhotoModel item = model.getFeed().getFood().getRichTextLists().get((position - 3) / getChushu());
                refreshContentText(holder, item, position);
                holder.ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (h instanceof ImgHolder && model.getFeed().getFood().allImageContent().size() == 1) {
                            showToast("请至少保留一张图片");
                            return;
                        }
                        model.getFeed().getFood().getRichTextLists().remove(item);
                        notifyItemRemoved(h.getAdapterPosition());
                    }
                });
                if (h instanceof ImgHolder) {
                    final ImgHolder holderImg = (ImgHolder) h;
                    holderImg.ivGood.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_good_unselected, 0, 0);
                    holderImg.ivNormal.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_normal_unselected, 0, 0);
                    holderImg.ivBad.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_bad_unselected, 0, 0);
                    switch (item.getPhoto().getImageComment()) {
                        case 0:
                            holderImg.ivComment.setImageResource(R.mipmap.ugc_edit_item_commend);
                            break;
                        case 1:
                            holderImg.ivComment.setImageResource(R.mipmap.ic_commend_good_small_selected);
                            holderImg.ivGood.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_good_selected, 0, 0);
                            break;
                        case 2:
                            holderImg.ivComment.setImageResource(R.mipmap.ic_commend_normal_small_selected);
                            holderImg.ivNormal.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_normal_selected, 0, 0);
                            break;
                        case 3:
                            holderImg.ivComment.setImageResource(R.mipmap.ic_commend_bad_small_selected);
                            holderImg.ivBad.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_commend_bad_selected, 0, 0);
                            break;
                    }

                    int uploadStatus = item.getPhoto().getImageAsset().getOriginalImage().getUploadStatus();
                    if (uploadStatus == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL || uploadStatus == SYUploadImage.UPLOAD_STATUS_INIT) {
                        holderImg.rl_upload_button.setVisibility(View.VISIBLE);
                        holderImg.iv_retry.setVisibility(View.VISIBLE);
                        holderImg.pb_uploading.setVisibility(View.GONE);
                        holderImg.tv_upload_status.setText("点击重新上传");
                        holderImg.rl_upload_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadAll();
                                onNetStatusChange(false);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else if (item.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                        holderImg.rl_upload_button.setVisibility(View.VISIBLE);
                        holderImg.iv_retry.setVisibility(View.GONE);
                        holderImg.pb_uploading.setVisibility(View.VISIBLE);
                        holderImg.tv_upload_status.setText("上传中...");
                        holderImg.rl_upload_button.setOnClickListener(null);
                    } else {
                        holderImg.rl_upload_button.setVisibility(View.GONE);
                    }

                    if (model.getFeed().getReleaseTemplate().pictureText.bHaveText) {
                        holderImg.ivText.setVisibility(View.VISIBLE);
                        holderImg.lineText.setVisibility(View.VISIBLE);
                    } else {
                        holderImg.ivText.setVisibility(View.GONE);
                        holderImg.lineText.setVisibility(View.GONE);

                        if (FFUtils.isStringEmpty(item.getContent())) {
                            holderImg.ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc);
                        } else {
                            holderImg.ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc_has);
                        }
                    }
                    if (FFUtils.isStringEmpty(item.getDishesName())) {
                        holderImg.tvDishName.setVisibility(View.GONE);
                        holderImg.ivDish.setImageResource(R.mipmap.ic_dish_item_ugc);
                    } else {
                        holderImg.tvDishName.setVisibility(View.VISIBLE);
                        holderImg.tvDishName.setText(item.getDishesName());
                        holderImg.ivDish.setImageResource(R.mipmap.ic_dish_item_ugc_has);
                    }
                    holderImg.ivDish.setOnClickListener(new View.OnClickListener() {

                        class Holder extends RecyclerView.ViewHolder {
                            TextView tv_name;

                            public Holder(View itemView) {
                                super(itemView);
                                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                            }
                        }

                        @Override
                        public void onClick(View v) {
                            holderImg.ivDel.setVisibility(View.GONE);
                            holderImg.rlTextContainer.setVisibility(View.GONE);
                            holderImg.rlDishContainer.setVisibility(View.GONE);
                            holderImg.rlDishContainer1.setVisibility(View.GONE);
                            holderImg.rlCommentContainer.setVisibility(View.GONE);
                            if (FFUtils.isListEmpty(model.hotDishList)) {
                                holderImg.rlDishContainer.setVisibility(View.VISIBLE);
                                lvImages.setFocusView(holderImg.rlDishContainer);
                                initDishText(holderImg.etDish, holderImg.ivClearDish, item);
                                holderImg.etDish.requestFocus();
                                FFUtils.setSoftInputVis(holderImg.etDish, true);
                                holderImg.etDish.setSelection(item.getDishesName() == null ? 0 : item.getDishesName().length());
                            } else {
                                holderImg.rlDishContainer1.setVisibility(View.VISIBLE);
                                lvImages.setFocusView(holderImg.rlDishContainer1);
                                initDishText(holderImg.etDish1, holderImg.ivClearDish1, item);
                                holderImg.etDish1.requestFocus();
                                FFUtils.setSoftInputVis(holderImg.etDish1, true);
                                holderImg.etDish1.setSelection(item.getDishesName() == null ? 0 : item.getDishesName().length());
                                holderImg.rvHotDish.setLayoutManager(new LinearLayoutManager(context(), LinearLayoutManager.VERTICAL, false));
                                holderImg.rvHotDish.setAdapter(new RecyclerView.Adapter<Holder>() {

                                    @Override
                                    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                                        return new Holder(getLayoutInflater().inflate(R.layout.item_select_dish, parent, false));
                                    }

                                    @Override
                                    public void onBindViewHolder(final Holder holder1, final int position1) {
                                        holder1.tv_name.setText(model.hotDishList.get(position1));
                                        if (model.hotDishList.get(position1).equals(holderImg.etDish1.getText().toString())) {
                                            holder1.tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            holder1.tv_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.restrestult_checked1, 0, R.mipmap.restrestult_checked, 0);
                                        } else {
                                            holder1.tv_name.setTextColor(getResources().getColor(R.color.ff_text_black));
                                            holder1.tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        }
                                        holder1.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                holderImg.etDish1.setText(model.hotDishList.get(position1));
                                                item.setDishesName(model.hotDishList.get(position1));
                                                holderImg.tvDishName.setText(model.hotDishList.get(position1));
                                                FFUtils.setSoftInputInvis(v.getWindowToken());
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    @Override
                                    public int getItemCount() {
                                        return model.hotDishList.size();
                                    }
                                });
                            }
                        }
                    });

                    holderImg.ivDel.setVisibility(View.VISIBLE);

                    if (!isSoftInputVis) {
                        holderImg.rlTextContainer.setVisibility(View.GONE);
                        holderImg.rlDishContainer.setVisibility(View.GONE);
                        holderImg.rlDishContainer1.setVisibility(View.GONE);
                        holderImg.rlCommentContainer.setVisibility(View.GONE);
                    }

                    FFImageLoader.loadBigImage(context(), item.getPhoto().getImageAsset().pullProcessedImageUrl(), holderImg.ivImg);

                    holderImg.ivComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holderImg.rlTextContainer.setVisibility(View.GONE);
                            holderImg.rlDishContainer.setVisibility(View.GONE);
                            holderImg.rlDishContainer1.setVisibility(View.GONE);
                            holderImg.rlCommentContainer.setVisibility(View.VISIBLE);
                            lvImages.setFocusView(holderImg.rlCommentContainer);
                            FFUtils.setSoftInputInvis(v.getWindowToken());
                        }
                    });
                    holderImg.ivText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holderImg.rlTextContainer.setVisibility(View.VISIBLE);
                            holderImg.rlDishContainer.setVisibility(View.GONE);
                            holderImg.rlDishContainer1.setVisibility(View.GONE);
                            holderImg.rlCommentContainer.setVisibility(View.GONE);
                            holderImg.etContent.requestFocus();
                            lvImages.setFocusView(holderImg.etContent);
                            FFUtils.setSoftInputVis(holderImg.etContent, true);
                            holderImg.etContent.setSelection(item.getContent() == null ? 0 : item.getContent().length());
                        }
                    });

                    View.OnClickListener l = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int check = 0;
                            switch (v.getId()) {
                                case R.id.iv_good:
                                    check = 1;
                                    break;
                                case R.id.iv_normal:
                                    check = 2;
                                    break;
                                case R.id.iv_bad:
                                    check = 3;
                                    break;
                            }
                            if (check == item.getPhoto().getImageComment()) {
                                check = 0;
                            }
                            item.getPhoto().setImageComment(check);
                            notifyDataSetChanged();
                        }
                    };
                    holderImg.ivGood.setOnClickListener(l);
                    holderImg.ivNormal.setOnClickListener(l);
                    holderImg.ivBad.setOnClickListener(l);
                    holderImg.rl_img.setVisibility(View.INVISIBLE);
                    holderImg.tv_process_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holderImg.rl_img.setVisibility(View.INVISIBLE);
                            lvImages.setFocusView(null);
                            startActivity(RichTextEditImageActivity.class, new RichTextEditImageIntent(model.getFeed().getFood().getRichTextLists().indexOf(item)));
                        }
                    });
                    holderImg.tv_reselect_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holderImg.rl_img.setVisibility(View.INVISIBLE);
                            lvImages.setFocusView(null);

                            RichTextEditActivity.this.item = item;
                            startActivity(SingleCheckImageActivity.class, new IntentData(INTENT_CHANGE_IMG));
                        }
                    });

                    holderImg.ivImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lvImages.setFocusView(holderImg.rl_img);
                            holderImg.rl_img.setVisibility(View.VISIBLE);
                        }
                    });
                } else {//文本
                    if (model.getFeed().getReleaseTemplate().sort == SYReleaseTemplateSort_HalfAllow && 0 == (position - 3) / getChushu()) {
                        holder.ivDel.setVisibility(View.GONE);

                    }
                }
            } else if (h instanceof RestHolder) {
                ((RestHolder) h).tv_name.setText(model.getFeed().getFood().getPoi().getTitle());
            }


        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 5;
            }
            if (position == 1) {
                return model.getFeed().getFood().getPoi() == null ? 0 : 1;
            }
            if (position == 2) {
                return 4;
            }

            return (model.getFeed().getFood().getRichTextLists().get(position - 3).isTextPhotoModel() ? 2 : 3);
        }

        private void refreshContentText(final TextHolder holder, final SYRichTextPhotoModel item, final int position) {
            TextWatcher tag = (TextWatcher) holder.etContent.getTag();
            holder.etContent.removeTextChangedListener(tag);
            holder.etContent.setText(item.getContent());
            holder.tvTextNum.setVisibility(View.GONE);
            holder.tvTextNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FFUtils.setSoftInputInvis(lvImages.getWindowToken());
                }
            });
            holder.etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        holder.etContent.setSelection(holder.etContent.length());
                        holder.tvTextNum.setVisibility(View.VISIBLE);
                        holder.ivDel.setVisibility(View.GONE);
                        lvImages.setFocusView(holder.etContent);
                        fl_emoji_btn_container.setVisibility(View.VISIBLE);
                        emoji_panel.setAttachedEditText((CustomEmojiEditText) holder.etContent);
                        if (isSoftInputVis) {
                            scrollToTop();
                        }
                    } else {
                        fl_emoji_btn_container.setVisibility(View.GONE);
                        emoji_panel.setVisibility(View.GONE);
                        holder.tvTextNum.setVisibility(View.GONE);
                        holder.ivDel.setVisibility(View.VISIBLE);
                        if (model.getFeed().getReleaseTemplate().sort == SYReleaseTemplateSort_HalfAllow && 0 == (position - 3) / getChushu()) {
                            holder.ivDel.setVisibility(View.GONE);
                        }
                        lvImages.setFocusView(null);
                    }
                }
            });
            int count;
            if (holder instanceof ImgHolder) {
                count = model.getFeed().getReleaseTemplate().pictureText.textWordCount;
                if (FFUtils.isStringEmpty(item.getContent())) {
                    ((ImgHolder) holder).ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc);
                } else {
                    ((ImgHolder) holder).ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc_has);
                }
            } else {
                count = model.getFeed().getReleaseTemplate().textWindow.textWindowWordCount;
            }
            final int MAX = count;
            holder.tvTextNum.setText(calculateLength(item.getContent().toString()) + "/" + MAX);
            tag = new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    holder.tvTextNum.setText(calculateLength(s.toString()) + "/" + MAX);
                    item.setContent(s.toString());
                    if (holder instanceof ImgHolder) {
                        if (s.length() == 0) {
                            ((ImgHolder) holder).ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc);
                        } else {
                            ((ImgHolder) holder).ivText.setImageResource(R.mipmap.ic_edit_text_item_ugc_has);
                        }
                    }


                }
            };
            holder.etContent.setFilters(new InputFilter[]{new EnglishCharFilter(MAX * 2)});
            holder.etContent.setTag(tag);
            holder.etContent.addTextChangedListener((TextWatcher) holder.etContent.getTag());
        }

        private void initDishText(final EditText etDish, final ImageView ivClearDish, final SYRichTextPhotoModel item) {
            if (etDish.getTag() != null) {
                etDish.removeTextChangedListener((TextWatcher) etDish.getTag());
            }

            etDish.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        EditText et = etDish;
                        et.setSelection(et.length());
                        emoji_panel.setNoShowThis(true);
                    }
                    if (hasFocus && isSoftInputVis) {
                        if (isSoftInputVis) {
                            scrollToTop();
                        }
                    }
                }
            });

            etDish.setText(item.getDishesName());
            if (FFUtils.isStringEmpty(item.getDishesName())) {
                ivClearDish.setVisibility(View.INVISIBLE);
            } else {
                ivClearDish.setVisibility(View.VISIBLE);
            }
            ivClearDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etDish.setText("");
                }
            });
            final int MAX = model.getFeed().getReleaseTemplate().dishes.dishesNameWordCount;
            etDish.setTag(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.setDishesName(s.toString());
                    if (FFUtils.isStringEmpty(item.getDishesName())) {
                        ivClearDish.setVisibility(View.INVISIBLE);
                    } else {
                        ivClearDish.setVisibility(View.VISIBLE);
                    }
                }
            });
            etDish.setFilters(new InputFilter[]{new EnglishCharFilter(MAX * 2)});
            etDish.addTextChangedListener((TextWatcher) etDish.getTag());
        }

        public int getChushu() {
//            return isCanAdd() ? 2 : 1;
            return 1;
        }
    }


    @Override
    public void onBackPressed() {
        if (emoji_panel.getVisibility() == View.VISIBLE) {
            emoji_panel.setVisibility(View.GONE);
            fl_emoji_btn_container.setVisibility(View.GONE);
            return;
        }

        if (v_more.getVisibility() == View.VISIBLE) {
            if (ll_bg.getVisibility() != View.VISIBLE) {
                FFUtils.setSoftInputInvis(v_more.getWindowToken());
                v_more.setVisibility(View.GONE);
            } else {
                ll_bg.setVisibility(View.GONE);
                FFUtils.setSoftInputInvis(v_more.getWindowToken());
            }
        } else {
            EnsureDialog.showEnsureDialog(this, true, "是否保存记录", "保存并退出", "不保存", "取消", new EnsureDialog.EnsureDialogListener1() {
                @Override
                public void onCenter(DialogInterface dialog) {
                    dialog.dismiss();
                    time.cancel();
                    SYDataManager.shareDataManager().removeDraft(model);
                    finishAllActivitysByTag(ActivityTags.main);
                }

                @Override
                public void onOk(DialogInterface dialog) {
                    dialog.dismiss();
                    //编辑
                    save();
                }

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void save() {
        time.cancel();
        final boolean bShare = model.isShareToSmallYellowO();
        model.setShareToSmallYellowO(false);
        task = PublishModelManager.publish(context(), new FFNetWorkCallBack<PublishResult>() {
            @Override
            public void onSuccess(PublishResult response, FFExtraParams extra) {
                MainActivity.toUser();
                finishAllActivitysByTag(ActivityTags.main);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                model.setShareToSmallYellowO(bShare);
                return false;
            }
        }, model, false);
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        refreshFoodType();
        refreshPeopleNum(true);
        refreshMoney(true);
        refreshTitle();

        model.getFeed().getFood().getFoodTypeString();
    }

    private void refreshMoney(boolean resetTextView) {
        if (resetTextView) {
            String subFloat = FFUtils.getSubFloat(model.getFeed().getFood().getTotalPrice());
            if ("0".equals(subFloat)) {
                tv_total_money.setText("");
            } else {
                tv_total_money.setText(subFloat);
            }
        }
        if (tv_total_money.length() == 0 || tv_total_money.getText().toString().equals("0")) {
            tv_total_money.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.food_money_disable, 0, 0, 0);
        } else {
            tv_total_money.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.food_money_enable, 0, 0, 0);
        }
    }


    private void refreshPeopleNum(boolean resetTextView) {
        if (resetTextView) {
            String subFloat = FFUtils.getSubFloat(model.getFeed().getFood().getNumberOfPeople());
            if ("0".equals(subFloat)) {
                tv_people_num.setText("");
            } else {
                tv_people_num.setText(subFloat);
            }
        }
        if (tv_people_num.length() == 0 || tv_people_num.getText().toString().equals("0")) {
            tv_people_num.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.food_people_num_disable, 0, 0, 0);
        } else {
            tv_people_num.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.food_people_num_enable, 0, 0, 0);
        }
    }

    private void refreshFoodType() {
        switch (model.getFeed().getFood().getFoodType()) {
            case SYRichTextFood.TYPE_ZC:
                cb_zc.setChecked(true);
                break;
            case SYRichTextFood.TYPE_ZWC:
                cb_zwc.setChecked(true);
                break;
            case SYRichTextFood.TYPE_WUC:
                cb_wuc.setChecked(true);
                break;
            case SYRichTextFood.TYPE_XWC:
                cb_xwc.setChecked(true);
                break;
            case SYRichTextFood.TYPE_WANC:
                cb_wanc.setChecked(true);
                break;
            case SYRichTextFood.TYPE_YX:
                cb_yx.setChecked(true);
                break;
        }
    }

    private void refreshTitle() {
        if (model.getFeed().getFood().getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl() == null) {
            iv_title.setImageResource(R.mipmap.food_cover_disable);
        } else {
            iv_title.setImageResource(R.mipmap.food_cover_enable);
        }
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }

    @OnClick({R.id.tv_review, R.id.tv_cancel, R.id.iv_add_text, R.id.iv_add_img, R.id.iv_sort, R.id.iv_share, R.id.ll_bg})
    public void onClick(final View v) {
        if (v.getId() == R.id.ll_bg) {
            return;
        }
        if (v_more.getVisibility() == View.VISIBLE || isSoftInputVis) {
            FFUtils.setSoftInputInvis(lvImages.getWindowToken());
            v_more.setVisibility(View.GONE);
            emoji_panel.setVisibility(View.GONE);
            fl_emoji_btn_container.setVisibility(View.GONE);
            return;
        }
        switch (v.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.iv_add_text:
                model.getFeed().getFood().getRichTextLists().add(new SYRichTextPhotoModel());
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                lvImages.smoothScrollToPosition(adapter.getItemCount() - 1);
                break;
            case R.id.iv_add_img:
                final int index = model.getFeed().getFood().getRichTextLists().size();
                GalleryInfo data = new GalleryInfo();
                data.setIndex(index);
                data.setTemplate_type(model.getFeed().getReleaseTemplate().indexCode);
                data.setNum(model.getFeed().getFood().allImageContent().size());
                startActivity(GalleryAndPhotoActvity.class, data);
                shouldScrollToEnd = true;
                lastNum = adapter.getItemCount();
                break;
            case R.id.iv_share:
                EnsureDialog.showEnsureDialog(this, true, "是否保存记录", "保存并退出", null, "取消", new EnsureDialog.EnsureDialogListener1() {
                    @Override
                    public void onCenter(DialogInterface dialog) {
                    }

                    @Override
                    public void onOk(DialogInterface dialog) {
                        dialog.dismiss();
                        //编辑
                        save();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.tv_review:
                if (model == null || model.getFeed().getFood().allImageContent().size() == 0) {
                    return;
                }
                SYPoi poi = model.getFeed().getFood().getPoi();
                if (poi == null) {
                    showToast("您还没有添加餐厅哦，请添加餐厅~");
                    return;
                }
                SYRichTextFood food = model.getFeed().getFood();
                String content = food.getTitle();
                if (food.getPoi() != null) {
                    content += "&&##" + food.getPoi().getTitle();
                }
                if (!FFUtils.isStringEmpty(food.getContent())) {
                    content += "&&##" + food.getContent();
                }
                for (SYRichTextPhotoModel text : food.getRichTextLists()) {
                    if (!FFUtils.isStringEmpty(text.getContent())) {
                        content += "&&##";
                        content += text.getContent();
                    }
                    if (!FFUtils.isStringEmpty(text.getDishesName())) {
                        content += "&&##";
                        content += text.getDishesName();
                    }
                }
                if (model.getFeed().getTags() != null)
                    for (SYFoodTagModel tag : model.getFeed().getTags()) {
                        if (!FFUtils.isStringEmpty(tag.getContent())) {
                            content += "&&##";
                            content += tag.getContent();
                        }
                    }
//                post(Constants.shareConstants().getNetHeaderAdress() + "/notes/detectSensitiveWord.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                post(IUrlUtils.Search.detectSensitiveWord, "", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if ("0".equals(model.getFeed().getIsAddScore())) {
                            if (SP.getCheckScore()) {
                                //开关已打开，需要判断积分检查
                                if (isbPromptError()) {
                                    if (limitResult != null && limitResult.getPublishLimit() != null) {
                                        CheckScoreIntent data = new CheckScoreIntent(limitResult, "Yes");
                                        startActivity(CheckScoreActivity.class, (CheckScoreIntent) data.setRequestCode(12634));
                                        overridePendingTransition(0, 0);
                                        return;
                                    } else {
                                        getIsScore(new Runnable() {
                                            @Override
                                            public void run() {
                                                onClick(v);
                                            }
                                        });
                                        return;
                                    }
                                }
                            }
                        }
                        //直接预览页
                        startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(null, false, true));

                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "content", content);
                break;
            case R.id.iv_sort:
                startActivity(SortActivity.class, new IntentData());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 12634:
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data != null) {
                    boolean bThisPrompt = data.getBooleanExtra("bThisPrompt", true);
                    setbPromptError(bThisPrompt);
                }
                startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(null, false, true));
                return;
            case INTENT_GET_IMG:
                if (model.getFeed().getFood().allImageContent().size() == 0) {
                    SYDataManager.shareDataManager().removeDraft(model);
                    finish();
                }
                return;
            case INTENT_CHANGE_IMG:
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (item != null) {
                    item.getPhoto().getImageAsset().setProcessedImage(null);
                    item.getPhoto().getImageAsset().setSYImage(data.getStringExtra("path"), true);
                    new File(item.getPhoto().getImageAsset().pullOriginalCutImagePath()).delete();
                    uploadAll();
                    adapter.notifyDataSetChanged();
                }
                return;
        }
    }

    SYRichTextPhotoModel item;

    @Override
    protected void onResume() {
        super.onResume();
        //添加图片后滚动到最下面
        if (shouldScrollToEnd && lastNum < adapter.getItemCount()) {
            lvImages.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
        shouldScrollToEnd = false;

        instance = this;
        if (model.getFeed().getFood().getRichTextLists().size() == 0) {
            if (getIntentData().getType() == RichTextEditIntent.TYPE_EDIT_NEW) {
                SYDataManager.shareDataManager().removeDraft(model);
                finish();
                return;
            }
        } else {
            new Thread() {
                @Override
                public void run() {
                    if (model.getFeed() != null && model.getFeed().getFood().allImageContent().size() > 0)
                        SYDataManager.shareDataManager().addDraft(model);
                }
            }.start();
        }

        FFUtils.setSoftInputInvis(lvImages.getWindowToken());

        refresh();
        onNetStatusChange(false);
    }

    @Override
    public void onSoftInputMethInvis(int softInputHight) {
        super.onSoftInputMethInvis(softInputHight);
        FFLogUtil.e("asd", "invis");
        setSoftInputVis(false);
        setFocusView(null);
        lvImages.setFocusView(null);
        refreshMoney(true);
        refreshPeopleNum(true);
        ll_bottomBar.setVisibility(View.VISIBLE);
        emoji_panel.setVisibility(View.GONE);
        fl_emoji_btn_container.setVisibility(View.GONE);
        if (v_more.getVisibility() == View.VISIBLE && ll_bg.getVisibility() == View.VISIBLE) {
            v_more.setVisibility(View.GONE);
            ll_bg.setVisibility(View.GONE);
        }
        FFUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    boolean isSoftInputVis = false;

    public void setSoftInputVis(boolean softInputVis) {
        isSoftInputVis = softInputVis;
    }

    @Override
    public void onSoftInputMethVis(int softInputHight) {
        super.onSoftInputMethVis(softInputHight);
        ll_bottomBar.setVisibility(View.GONE);
        setSoftInputVis(true);
        scrollToTop();
        FFUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (adapter.getItemCount() > 1)
                    adapter.notifyItemChanged(1);
            }
        });
    }

    private void scrollToTop() {
        View focus = getContainer().findFocus();
        if (focus != null) {
            switch (getContainer().findFocus().getId()) {
                case R.id.et_dish:
                case R.id.et_dish1:
                case R.id.et_content:
                    View parent = focus;
                    while (parent.getId() != R.id.rl_ugc_edit_item) {
                        parent = (View) parent.getParent();
                    }

                    int position = lvImages.getChildAdapterPosition(parent);
                    ((LinearLayoutManager) lvImages.getLayoutManager()).scrollToPositionWithOffset(position, FFUtils.getPx(60));
                    break;


            }
        }
    }

    private void getIsScore() {
        getIsScore(null);
    }

    boolean isUploading = false;
    Runnable runn;
    ArrayList<Integer> ids = new ArrayList<>();

    private synchronized void getIsScore(Runnable run) {
        this.runn = run;
        if (isUploading) {
            if (run != null) {
                ids.add(showProgressDialog("正在获取积分信息"));
            }
            return;
        }
        isUploading = true;

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);

//        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/publishScoreCheck.do", runn != null ? "正在获取积分信息" : null, extra, new FFNetWorkCallBack<PublishLimitResult>() {
        post(IUrlUtils.Search.publishScoreCheck, runn != null ? "正在获取积分信息" : null, extra, new FFNetWorkCallBack<PublishLimitResult>() {

            @Override
            public void onBack(FFExtraParams extra) {
                for (int i : ids) {
                    dismissProgressDialog(i);
                }
                ids.clear();
                super.onBack(extra);
            }

            @Override
            public void onSuccess(PublishLimitResult response, FFExtraParams extra) {
                limitResult = response;
                isUploading = false;
                if (RichTextEditActivity.this.runn != null && limitResult.getPublishLimit() != null) {
                    RichTextEditActivity.this.runn.run();
                } else if (limitResult.getPublishLimit() == null) {
                    showToast("获取积分信息失败!");
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                isUploading = false;
                return false;
            }
        });
    }

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            String tmp = new String(new char[]{c.charAt(i)});
            if (tmp.getBytes().length == 1) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private class TitleHolder extends RecyclerView.ViewHolder {

        private final EditText tvName;

        public TitleHolder(View convertView) {
            super(convertView);
            tvName = (EditText) convertView.findViewById(tv_name);
            final View ivClear = convertView.findViewById(R.id.iv_clear);
//                    NicknameActivty.setEtWatcher(tvName, 18);
            tvName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    model.getFeed().getFood().getFrontCoverModel().getFrontCoverContent().setContent(s.toString());
                    if (s.length() == 0) {
                        tvName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_title_edit, 0, 0, 0);
                        ivClear.setVisibility(View.GONE);
                    } else {
                        tvName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_title_edit_enable, 0, 0, 0);
                        ivClear.setVisibility(View.VISIBLE);
                    }
                }
            });
            tvName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (tvName.length() == 0) {
                            tvName.setText(tvName.getHint());
                            tvName.setSelection(tvName.length());
                        }
                        lvImages.setFocusView(v);
                    } else {
                        lvImages.setFocusView(null);
                    }
                }
            });
            tvName.setHint(TimeUtils.getTime("yyyyMMdd", System.currentTimeMillis()));
            ivClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//删除

                    EnsureDialog.showEnsureDialog(context(), true, "确定要清空标题吗?", "确定", null, "取消", new EnsureDialog.EnsureDialogListener() {
                        @Override
                        public void onOk(DialogInterface dialog) {
                            dialog.dismiss();
//                                    model.getFeed().setTitle(null);
                            tvName.setText("");
                        }

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        public void onBind() {
            tvName.setText(model.getFeed().getFood().getFrontCoverModel().getFrontCoverContent().getContent());
        }
    }

    private PublishLimitResult limitResult;
}