package com.fengnian.smallyellowo.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.Adapter.AddyuluActivity;
import com.fengnian.smallyellowo.foodie.appbase.AppHelper;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPhotoPrecisionModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.PublishLimitResult;
import com.fengnian.smallyellowo.foodie.bean.results.PublishResult;
import com.fengnian.smallyellowo.foodie.bean.results.RecognizeResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiEditText;
import com.fengnian.smallyellowo.foodie.emoji.EmojiPanelLayout;
import com.fengnian.smallyellowo.foodie.emoji.EmojiUtils;
import com.fengnian.smallyellowo.foodie.fragments.MyCameraFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.CheckScoreIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.PublishedSuccesskIndata;
import com.fengnian.smallyellowo.foodie.intentdatas.ReviewImagesIntent;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.CameraComponentSample;
import com.fengnian.smallyellowo.foodie.widgets.MyRecyclerView;
import com.fengnian.smallyellowo.foodie.widgets.MyRecyclerViewClub;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.utils.TLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.R.layout.view_input_emoji;

public class FastEditActivity extends BaseActivity<FastEditIntent> {

    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_publish)
    TextView tvPublish;
    @Bind(R.id.ll_bottomBar)
    View ll_bottomBar;
    @Bind(R.id.listView)
    MyRecyclerView listView;

    View headView;
    View footView;

    public static DraftModel draft;
    public static FastEditActivity instance;
    private HeaderHolder headHolder;
    private FootHolder footHolder;
    private RecyclerView.Adapter adapter;
    private int MAX_COUNT = 1000;

    private List<ConfigResult.HotWordBean.ShortContentsBean> yululist = SP.getConfig().getConfig().getHotword().getShortContents();
    private List<ConfigResult.HotWordBean.ShortContentsBean> yululist1 = new ArrayList<>();
    public static PublishModel task;

    public static void onImagePiced(ArrayList<MyCameraFragment.TempPic> list) {
        if (FFUtils.isListEmpty(list) || instance == null) {
            return;
        }
        Collections.reverse(list);
        DraftModelManager.onImagesPicked(instance, draft, list, -1);
        MyCameraFragment.tokenPicturePath.clear();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        if (isFinishing()) {
            draft = null;
        }
        task = null;
    }

    private EmojiPanelLayout emoji_panel;
    private View fl_emoji_btn_container;

    private void initYululist() {
        List<ConfigResult.HotWordBean.ShortContentsBean> list = new ArrayList<>();
        list.addAll(yululist);
        while (list.size() > 0) {
            int n = list.size();
            yululist1.add(list.remove(new Random().nextInt(n)));
        }
    }

    private void initTuSdk()
    {
        if (AppHelper.isAppMainProcess(this))
        {
            TLog.i("isAppMainProcess");

            TuSdk.init(this, "1faa2d433fef2f60-03-glpcq1");
            TuSdk.checkFilterManager(mFilterManagerDelegate);
        }
    }

    /** 滤镜管理器委托 */
    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate()
    {
        @Override
        public void onFilterManagerInited(FilterManager manager)
        {
            TLog.i("init success");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTuSdk();
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        initYululist();
        addChildTag(ActivityTags.fast_edit);
        instance = this;
        if (savedInstanceState == null) {
            draft = null;
        }
        initDraft();
        if (savedInstanceState == null && draft.getFeed().getFood().getRichTextLists().size() == 0) {
            addImage();
        }


        getIsScore();
        setContentView(R.layout.activity_fast_edit);
        ButterKnife.bind(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        setNotitle(true);

        listView.setOnOutFoucusViewActionDownListener(new MyRecyclerView.OnOutFoucusViewActionDownListener() {
            @Override
            public void OnOutFoucusViewActionDown() {
                adapter.notifyDataSetChanged();
                FFUtils.setSoftInputInvis(listView.getWindowToken());
                emoji_panel.setVisibility(View.GONE);
                fl_emoji_btn_container.setVisibility(View.GONE);
            }
        });
        LinearLayout parent = (LinearLayout) getContainer().getParent().getParent();
        emoji_panel = (EmojiPanelLayout) getLayoutInflater().inflate(view_input_emoji, parent, false);
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
                if (isSoftInputVis && isShow) {
                    ((LinearLayoutManager) listView.getLayoutManager()).scrollToPositionWithOffset(0, FFUtils.getPx(60));
                    FFUtils.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            headHolder.et_content.requestFocus();
                            FFUtils.setSoftInputVis(headHolder.et_content, true);
                        }
                    });
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
        initHeadView();
        initFootView();

        adapter = new RecyclerView.Adapter() {
            int width = (FFUtils.getDisWidth() - (FFUtils.getPx(44 + (14 * 3)))) / 3;

            class ImgsHolder extends RecyclerView.ViewHolder {
                RecyclerView rv;

                public ImgsHolder(View itemView) {
                    super(itemView);
                    rv = (RecyclerView) itemView;
                    rv.setLayoutManager(new LinearLayoutManager(context()));
                    rv.setAdapter(new RecyclerView.Adapter() {
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return new MyHolder(getLayoutInflater().inflate(R.layout.item_fast_imgs, parent, false));
                        }

                        @Override
                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                            ((MyHolder) holder).refresh(position);
                        }

                        @Override
                        public int getItemCount() {
                            int count = 0;
                            count += (draft.getFeed().getFood().getRichTextLists().size() + 1) % 3 == 0 ? 0 : 1;
                            count += (draft.getFeed().getFood().getRichTextLists().size() + 1) / 3;
                            return count;
                        }
                    });
                    initHeight();
                }

                public void refresh() {
                    rv.getAdapter().notifyDataSetChanged();
                    initHeight();
                }

                private void initHeight() {
                    int height = width + FFUtils.getPx(13);
                    if (rv.getAdapter().getItemCount() == 1) {
                        rv.getLayoutParams().height = height;
                    } else {
                        rv.getLayoutParams().height = height * 2;
                    }
                    if (rv.getAdapter().getItemCount() > 2) {
                        listView.setFView(rv);
                    } else {
                        listView.setFView(null);
                    }
                }

            }


            class MyHolder extends RecyclerView.ViewHolder {
                ImageView[] imageViews = new ImageView[3];
                TextView[] textViews = new TextView[3];

                public MyHolder(View view) {
                    super(view);
                    imageViews[0] = (ImageView) view.findViewById(R.id.iv1);
                    imageViews[1] = (ImageView) view.findViewById(R.id.iv2);
                    imageViews[2] = (ImageView) view.findViewById(R.id.iv3);
                    textViews[0] = (TextView) view.findViewById(R.id.tv1);
                    textViews[1] = (TextView) view.findViewById(R.id.tv2);
                    textViews[2] = (TextView) view.findViewById(R.id.tv3);
                    for (int ii = 0; ii < 3; ii++) {
                        ((View) imageViews[ii].getParent()).getLayoutParams().width = width;
                        ((View) imageViews[ii].getParent()).getLayoutParams().height = width;
                    }
                }

                public void refresh(int i) {
                    int j = 0;
                    if (i == 0) {//添加按钮
                        j = 1;
                        imageViews[0].setTag(R.id.ff_tag_imageLoader, "");
                        imageViews[0].setImageResource(R.mipmap.ic_fast_add);
                        textViews[0].setVisibility(View.GONE);
                        imageViews[0].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imageViews[0].setBackgroundResource(R.drawable.fast_add_img);
                        imageViews[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (footHolder.isUploading || isWaitingRecognize) {
                                    showToast("图片识别中请稍后！");
                                    return;
                                }
                                addImage();
                            }
                        });
                    } else {
                        textViews[0].setVisibility(View.GONE);
                        imageViews[0].setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViews[0].setBackgroundResource(R.color.image_bg);
                        imageViews[0].setOnClickListener(null);
                    }

                    int offset = i * 3 - 1;
                    int index = j + offset;
                    for (; j < 3; j++, index++) {
                        if (index < draft.getFeed().getFood().getRichTextLists().size()) {
                            final int dex = index;
                            SYRichTextPhotoModel richTextPhotoModel = draft.getFeed().getFood().getRichTextLists().get(index);
                            FFImageLoader.loadMiddleImage(context(), richTextPhotoModel.getPhoto().getImageAsset().pullProcessedImageUrl(), imageViews[j]);
                            imageViews[j].setVisibility(View.VISIBLE);
                            String pulledDishName = richTextPhotoModel.pullDishesName();
                            if (!FFUtils.isStringEmpty(pulledDishName)) {
                                textViews[j].setVisibility(View.VISIBLE);
                                textViews[j].setText(pulledDishName);
                            } else {
                                textViews[j].setVisibility(View.GONE);
                            }
                            imageViews[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (footHolder.isUploading || isWaitingRecognize) {
                                        return;
                                    }
                                    ReviewImagesIntent data = new ReviewImagesIntent();
                                    data.setRequestCode(4);
                                    data.setIndex(dex);
                                    startActivity(ReviewImagesActivity.class, data);
                                }
                            });
                        } else {
                            textViews[j].setVisibility(View.GONE);
                            imageViews[j].setVisibility(View.INVISIBLE);
                            imageViews[j].setOnClickListener(null);
                        }
                    }
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                switch (viewType) {
                    case 0: {
                        return new RecyclerView.ViewHolder(headView) {
                        };
                    }
                    case 2: {
                        return new RecyclerView.ViewHolder(footView) {
                        };
                    }
                    case 1: {
                        view = getLayoutInflater().inflate(R.layout.item_fast_imgage_rv, parent, false);
                        return new ImgsHolder(view);
                    }
                }
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder.itemView == headView) {
                    headHolder.refresh();
                } else if (holder instanceof ImgsHolder) {
                    ((ImgsHolder) holder).refresh();
                }
            }

            @Override
            public int getItemCount() {
                if (draft == null) {
                    return 0;
                }
                return 3;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 0;
                }
                if (position == getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    public void onOnePictureUploadStatusChange(SYUploadImage syUploadImage) {
        if (!getDestroyed() && isWaitingRecognize) {
            isWaitingRecognize = false;
            footHolder.btn_recognize.performClick();
        }
    }

    boolean isWaitingRecognize = false;

    public class FootHolder {
        @Bind(R.id.btn_recognize)
        ImageView btn_recognize;


        private boolean recognize() {
            for (final SYRichTextPhotoModel richTextPhotoModel : draft.getFeed().getFood().getRichTextLists()) {
                if (richTextPhotoModel.getPhoto().status == SYFoodPhotoModel.STATUS_WAITING) {
                    richTextPhotoModel.getPhoto().status = SYFoodPhotoModel.STATUS_RECOGINZEING;
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/activity/inferencePic.do", null, null, new FFNetWorkCallBack<RecognizeResult>() {
                    post(IUrlUtils.Search.inferencePic, null, null, new FFNetWorkCallBack<RecognizeResult>() {
                        @Override
                        public void onBack(FFExtraParams extra) {
                            if (getDestroyed()) {
                                return;
                            }
                            if (recognize()) {
//                                        showToast("所有图片已识别");
                            }
                        }

                        @Override
                        public void onSuccess(RecognizeResult response, FFExtraParams extra) {
                            ArrayList<SYPhotoPrecisionModel> dishesNameList = response.getList();
                            richTextPhotoModel.getPhoto().setDishesNameList(dishesNameList);
                            if (!FFUtils.isListEmpty(dishesNameList)) {
                                float max = 0;
                                String bestName = "";
                                for (int i = 0; i < dishesNameList.size(); i++) {
                                    SYPhotoPrecisionModel model = dishesNameList.get(i);
                                    if (model.getPrecision() < 0.5f) {
                                        dishesNameList.remove(i);
                                        i--;
                                        continue;
                                    }
                                    if (model.getPrecision() > max) {
                                        bestName = model.getContent();
                                        max = model.getPrecision();
                                    }
                                }
                                if (!FFUtils.isStringEmpty(bestName)) {
                                    richTextPhotoModel.setDishesName(bestName);
                                }
                            }
                            richTextPhotoModel.getPhoto().status = SYFoodPhotoModel.STATUS_SUCCESSED;
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            richTextPhotoModel.getPhoto().status = SYFoodPhotoModel.STATUS_FAIL;
                            adapter.notifyDataSetChanged();
                            return false;
                        }
                    }, "url", richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl());
                    adapter.notifyDataSetChanged();
                    return false;
                }
            }
            isUploading = false;
            adapter.notifyDataSetChanged();
            return true;
        }

        boolean isUploading = false;

        public FootHolder(View footView) {
            ButterKnife.bind(this, footView);

            btn_recognize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //识别菜
                    UploadResult ur = new UploadResult();
                    ur.inxit();
                    for (final SYRichTextPhotoModel richTextPhotoModel : draft.getFeed().getFood().getRichTextLists()) {
                        if (FFUtils.isStringEmpty(richTextPhotoModel.getDishesName()) && (richTextPhotoModel.getPhoto().status == SYFoodPhotoModel.STATUS_INIT || richTextPhotoModel.getPhoto().status == SYFoodPhotoModel.STATUS_FAIL)) {
                            richTextPhotoModel.getPhoto().status = SYFoodPhotoModel.STATUS_WAITING;
                        }
                    }
                    if (ur.uploadSuccessNum != ur.total) {
                        isWaitingRecognize = true;
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    if (isUploading) {
                        return;
                    }
                    isUploading = true;
                    if (recognize()) {
                        showToast("当前图片已全部识别！");
                    }
                }

            });
        }
    }

    class UploadResult {
        int total;
        int uploadingNum;
        int uploadSuccessNum;
        int uploadFailNum;
        int uploadInitNum;

        public void inxit() {
            for (SYRichTextPhotoModel richTextPhotoModel : draft.getFeed().getFood().getRichTextLists()) {
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

    @Override
    public void startActivity(Intent intent) {
        if (intent instanceof TuSdkIntent) {
            startActivityForResult(intent, 3);
            return;
        }
        super.startActivity(intent);
    }

    public class HeaderHolder {
        private LinearLayout ll_add_rest;
        private TextView tv_name;
        private ImageView iv_clear;
        private TextView tv_level;
        private RatingBar rb_level;
        private ImageView ic_help;
        private EditText et_content;
        private TextView tv_text_num;
        private TextView tv_add_yulu;
        private LinearLayout ll_yulu;
        private ImageView iv_arrow_left;
        private ViewPager rl_yulu;
        private View ll_input_content;
        private ImageView iv_arrow_right;
        private TextView tv_add;
        private TextView tv_more_yulu;

        public HeaderHolder(View view) {
            ll_add_rest = (LinearLayout) view.findViewById(R.id.ll_add_rest);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
            tv_level = (TextView) view.findViewById(R.id.tv_level);
            rb_level = (RatingBar) view.findViewById(R.id.rb_level);
            ic_help = (ImageView) view.findViewById(R.id.ic_help);
            et_content = (EditText) view.findViewById(R.id.et_content);
            tv_text_num = (TextView) view.findViewById(R.id.tv_text_num);
            tv_add_yulu = (TextView) view.findViewById(R.id.tv_add_yulu);
            ll_yulu = (LinearLayout) view.findViewById(R.id.ll_yulu);
            iv_arrow_left = (ImageView) view.findViewById(R.id.iv_arrow_left);
            rl_yulu = (ViewPager) view.findViewById(R.id.rl_yulu);
            iv_arrow_right = (ImageView) view.findViewById(R.id.iv_arrow_right);
            tv_add = (TextView) view.findViewById(R.id.tv_add);
            tv_more_yulu = (TextView) view.findViewById(R.id.tv_more_yulu);
            ll_input_content = view.findViewById(R.id.ll_input_content);

            et_content.setText(draft.getFeed().getFood().getContent());

            ll_yulu.setVisibility(View.GONE);
            tv_add_yulu.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_add_yulu_black,
                    0, R.mipmap.dish_down, 0);
            final View.OnClickListener addYuluListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ll_yulu.getVisibility() == View.GONE) {
                        ll_yulu.setVisibility(View.VISIBLE);
                        tv_add_yulu.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_add_yulu,
                                0, R.mipmap.dish_up, 0);
                    } else {
                        ll_yulu.setVisibility(View.GONE);
                        tv_add_yulu.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_add_yulu_black,
                                0, R.mipmap.dish_down, 0);
                    }
                }
            };
            tv_add_yulu.setOnClickListener(addYuluListener);

            initPager(this, 0);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = 1;
                    if (v == iv_arrow_left) {
                        i = -1;
                    }
                    rl_yulu.setCurrentItem(rl_yulu.getCurrentItem() + i);
                }
            };
//            iv_arrow_left.setOnClickListener(l);
            iv_arrow_right.setOnClickListener(l);

            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_content.append(yululist1.get(rl_yulu.getCurrentItem() % yululist.size()).getContent());
                    addYuluListener.onClick(null);
                }
            });

            tv_more_yulu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AddyuluActivity.class, new IntentData().setRequestCode(555));
//                    addYuluListener.onClick(null);
                }
            });

            et_content.setFilters(new InputFilter[]{new EnglishCharFilter(MAX_COUNT)});
            et_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                private long calculateLength(CharSequence c) {
                    double len = 0;
                    for (int i = 0; i < c.length(); i++) {
                        int tmp = (int) c.charAt(i);
                        if (tmp > 0 && tmp < 127) {
                            len += 0.5;
                        } else {
                            len++;
                        }
                    }
                    return Math.round(len);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    tv_text_num.setText(calculateLength(editable) + "/" + (MAX_COUNT / 2));
                    draft.getFeed().getFood().setContent(editable.toString());
                }
            });

            ll_add_rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(AddRestActivity.class, new IntentData());
                }
            });

            iv_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EnsureDialog.showEnsureDialog(context(), true, "确定要清空商户位置吗?", "确定", null, "取消", new EnsureDialog.EnsureDialogListener() {
                        @Override
                        public void onOk(DialogInterface dialog) {
                            dialog.dismiss();
                            draft.getFeed().getFood().setPoi(null);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            rb_level.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    draft.getFeed().setStarLevel((int) v);
                    switch ((int) v) {
                        case 0:
                            rb_level.setRating(1);
                            draft.getFeed().setStarLevel(1);
                            break;
                    }
                    tv_level.setText(draft.getFeed().pullStartLevelString());
                }
            });
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
        }

        public void refresh() {
            if (draft.getFeed().getFood().getPoi() == null) {
                tv_name.setText("添加餐厅");
                iv_clear.setVisibility(View.GONE);
            } else {
                iv_clear.setVisibility(View.VISIBLE);
                tv_name.setText(draft.getFeed().getFood().getPoi().getTitle());
            }
            RatingBar.OnRatingBarChangeListener listener = rb_level.getOnRatingBarChangeListener();
            rb_level.setOnRatingBarChangeListener(null);
            rb_level.setRating(draft.getFeed().getStarLevel());
            tv_level.setText(draft.getFeed().pullStartLevelString());
            rb_level.setOnRatingBarChangeListener(listener);
        }
    }


    private void initHeadView() {
        headView = getLayoutInflater().inflate(R.layout.header_fast_edit, listView, false);
        headHolder = new HeaderHolder(headView);
    }

    private void initFootView() {
        footView = getLayoutInflater().inflate(R.layout.footer_fast_edit, listView, false);
        footHolder = new FootHolder(footView);
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

    /**
     * 添加图片
     */
    private void addImage() {
        new CameraComponentSample().showSample(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (draft == null) {
            initDraft();
        }
        if (requestCode == 12634) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (data != null) {
                boolean bThisPrompt = data.getBooleanExtra("bThisPrompt", true);
                setbPromptError(bThisPrompt);
            }
            // 发布
            task = PublishModelManager.publish(context(), new FFNetWorkCallBack<PublishResult>() {
                @Override
                public void onSuccess(PublishResult response, FFExtraParams extra) {
                    PublishedSuccessActivity.model = (PublishModel) extra.getObj();
                    startActivity(PublishedSuccessActivity.class, new PublishedSuccesskIndata());
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, draft, true);

        }
        if (requestCode == 3) {
            if (draft.getFeed().getFood().getRichTextLists().size() == 0) {
                draft = null;
                finish();
            }
        }

        if (requestCode == 555 && resultCode == RESULT_OK) {
            String text = data.getStringExtra("text");
            headHolder.et_content.append(text);
            for (int i = 0; i < yululist1.size(); i++) {
                if (text.equals(yululist1.get(i).getContent())) {
                    initPager(headHolder, i);
                    break;
                }
            }
        }
    }

    private void initPager(final HeaderHolder headHolder, int i) {
        headHolder.rl_yulu.setAdapter(new PagerAdapter() {

            ArrayList<TextView> views = new ArrayList<>();
            SparseArray<TextView> views_used = new SparseArray<>();

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(views_used.get(position));
                views.add(views_used.get(position));
                views_used.remove(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TextView view;
                if (!views.isEmpty()) {
                    view = views.remove(0);
                } else {
                    view = new TextView(context());
                    ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                    params.height = FFUtils.getPx(82);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            headHolder.et_content.append(((TextView) v).getText());
                            headHolder.ll_yulu.setVisibility(View.GONE);
                            headHolder.tv_add_yulu.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_add_yulu_black,
                                    0, R.mipmap.dish_down, 0);
                        }
                    });
                    view.setLayoutParams(params);
                    int padding = FFUtils.getPx(4);
                    view.setPadding(padding, padding, padding, padding);
                    view.setLines(3);
                    view.setMaxLines(3);
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    view.setTextSize(13);
                    view.setTextColor(0xff333333);
                }
                view.setText(yululist1.get(position % yululist.size()).getContent());
                views_used.put(position, view);
                container.addView(view);
                return view;
            }
        });

        headHolder.rl_yulu.setCurrentItem(i, false);

    }

    private void initDraft() {
        if (draft == null) {//如果是回收后重新启动的情况不会重新创建model
            draft = getIntentData().getFeed();
        }
        if (draft == null) {
            draft = DraftModelManager.create(true);
        }
    }

    boolean isSoftInputVis = false;

    @Override
    public void onSoftInputMethInvis(int softInputHeight) {
        super.onSoftInputMethInvis(softInputHeight);
        isSoftInputVis = false;
        headHolder.tv_text_num.setVisibility(View.INVISIBLE);
        listView.setFocusView(null);
        emoji_panel.setVisibility(View.GONE);
        ll_bottomBar.setVisibility(View.VISIBLE);
        fl_emoji_btn_container.setVisibility(View.GONE);
        FFUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSoftInputMethVis(int softInputHight) {
        super.onSoftInputMethVis(softInputHight);
        emoji_panel.setAttachedEditText((CustomEmojiEditText) headHolder.et_content);
        isSoftInputVis = true;
        ll_bottomBar.setVisibility(View.GONE);
        fl_emoji_btn_container.setVisibility(View.VISIBLE);
        headHolder.tv_text_num.setVisibility(View.VISIBLE);
        listView.setFocusView(headHolder.ll_input_content);
        ((LinearLayoutManager) listView.getLayoutManager()).scrollToPositionWithOffset(0, FFUtils.getPx(60));
    }

    @OnClick({R.id.tv_cancel, R.id.tv_publish, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
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
            case R.id.tv_publish:
                if (draft == null || draft.getFeed().getFood().allImageContent().size() == 0) {
                    return;
                }
                SYPoi poi = draft.getFeed().getFood().getPoi();
                if (poi == null) {
                    showToast("您还没有添加餐厅哦，请添加餐厅~");
                    return;
                }
                SYRichTextFood food = draft.getFeed().getFood();
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
//                post(Constants.shareConstants().getNetHeaderAdress() + "/notes/detectSensitiveWord.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                post(IUrlUtils.Search.detectSensitiveWord, "", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if ("0".equals(draft.getFeed().getIsAddScore())) {
                            if (SP.getCheckScore()) {
                                //开关已打开，需要判断积分检查
                                if (isbPromptError()) {
                                    if (limitResult != null && limitResult.getPublishLimit() != null) {
                                        CheckScoreIntent data = new CheckScoreIntent(limitResult, "No");
                                        startActivity(CheckScoreActivity.class, (CheckScoreIntent) data.setRequestCode(12634));
                                        overridePendingTransition(0, 0);
                                        return;
                                    }
                                }
                            }
                        }
                        task = PublishModelManager.publish(context(), new FFNetWorkCallBack<PublishResult>() {
                            @Override
                            public void onSuccess(PublishResult response, FFExtraParams extra) {
                                PublishedSuccessActivity.model = (PublishModel) extra.getObj();
                                startActivity(PublishedSuccessActivity.class, new PublishedSuccesskIndata());
                                finish();
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, draft, true);

                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "content", content);

                break;
        }
    }


    @Override
    public void onBackPressed() {
        EnsureDialog.showEnsureDialog(this, true, "是否保存记录", "保存并退出", "不保存", "取消", new EnsureDialog.EnsureDialogListener1() {
            @Override
            public void onCenter(DialogInterface dialog) {
                dialog.dismiss();
                SYDataManager.shareDataManager().removeDraft(draft);
                finish();
                draft = null;
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

    private void save() {
        final boolean bShare = draft.isShareToSmallYellowO();
        draft.setShareToSmallYellowO(false);
        task = PublishModelManager.publish(context(), new FFNetWorkCallBack<PublishResult>() {
            @Override
            public void onSuccess(PublishResult response, FFExtraParams extra) {
//                        MainActivity.toUser();
                finish();
                draft = null;
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                draft.setShareToSmallYellowO(bShare);
                return false;
            }
        }, draft, true);
    }

    private void getIsScore() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdressrAdress() + "/notes/publishScoreCheck.do", null, extra, new FFNetWorkCallBack<PublishLimitResult>() {
        post(IUrlUtils.Search.publishScoreCheck, null, extra, new FFNetWorkCallBack<PublishLimitResult>() {
            @Override
            public void onSuccess(PublishLimitResult response, FFExtraParams extra) {

                if (response.judge()) {
                    limitResult = response;
                } else {

                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    private PublishLimitResult limitResult;
}
