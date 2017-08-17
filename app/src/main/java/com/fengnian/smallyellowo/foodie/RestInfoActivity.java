package com.fengnian.smallyellowo.foodie;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.RestInfoResult;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.dialogs.CallDialog;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.RemarkManager;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.shopcommiterror.ShopErrorTypeActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.R.id.tv_attention;

public class RestInfoActivity extends BaseActivity<RestInfoIntent> {

    private RestInfoResult response;

    class Holder7 {
        ImageView iv_avatar;
        TextView tv_name;
        TextView tv_time;
        ImageView iv_da;
        ImageView iv_guan;
        LinearLayout ll_level_container;
        RatingBar rb_level;
        TextView tv_level;
        LinearLayout ll_nine_pic_container;
        ImageView iv_dynamic_1;
        ImageView iv_dynamic_2;
        ImageView iv_dynamic_3;
        ImageView iv_dynamic_4;
        ImageView iv_dynamic_5;
        ImageView iv_dynamic_6;
        ImageView iv_dynamic_7;
        ImageView iv_dynamic_8;
        ImageView iv_dynamic_9;
        FrameLayout fl_dynamic_lastImage_container;
        TextView tv_dynamic_moreImage;
        ImageView iv_dynamic_msbj_only;
        ImageView iv_dynamic_mssj_only;
        final ImageView[] ivs = new ImageView[8];//前八张图片  第九张特殊处理
    }

    public static final int mssjImageWidth = FFUtils.getDisWidth() - FFUtils.getPx(15 + 15);
    public static final int onlyImageHight = mssjImageWidth * 536 / 660;
    public static final int msbjImageHight = FFUtils.getDisWidth() * 536 / 660;
    public static final int nineImageHight = (FFUtils.getDisWidth() - FFUtils.getPx(15 + 4 + 4 + 15)) / 3;
    @Bind(R.id.lv_restinfo)
    ListView lvRestinfo;
    @Bind(R.id.s_status_bar)
    View sStatusBar;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.v_title_bottom)
    View vTitleBottom;
    @Bind(R.id.iv_close)
    ImageView iv_close;
    private PullToRefreshLayout prl;
    private HeadHolder headHolder;
    ActionViewHolder actionViewHolder2;


    class RatingHolder {
        View ratingView;
        @Bind(R.id.iv1)
        ImageView iv1;
        @Bind(R.id.iv2)
        ImageView iv2;
        @Bind(R.id.iv3)
        ImageView iv3;
        @Bind(R.id.iv4)
        ImageView iv4;
        //        @Bind(R.id.tv_star_1)
        TextView tvStar1;
        //        @Bind(R.id.tv_pian_1)
        TextView tvPian1;
        private int rating;
        ImageView[] ivs;

        /**
         * rating 1-4
         *
         * @param rating
         */
        public void setView(int rating) {
            this.rating = rating;
            ratingView = getLayoutInflater().inflate(R.layout.item_rating_restinfo_top, null);
            ratingView.setId(R.drawable.alpha + rating - 1);
            ButterKnife.bind(this, ratingView);
            View viewById = ratingView.findViewById(R.id.tv_star_11);
            tvStar1 = (TextView) viewById;
            tvPian1 = (TextView) ratingView.findViewById(R.id.tv_pian_1);

            ivs = new ImageView[]{iv1, iv2, iv3, iv4};
        }

        public void initSelection(int selectedRating) {
            int i = 0;
            if (selectedRating == rating) {
                ratingView.setBackgroundResource(R.drawable.restinfo_rating_2);
                for (ImageView iv : ivs) {
                    if (i < rating) {
                        iv.setImageResource(R.mipmap.restinfo_small_rating_2);
                    } else {
                        iv.setImageResource(R.mipmap.restinfo_small_rating_1);
                    }
                    i++;
                }

                tvPian1.setTextColor(0xffffffff);
                tvStar1.setTextColor(0xffffffff);
            } else {
                ratingView.setBackgroundResource(R.drawable.restinfo_rating_1);
                for (ImageView iv : ivs) {
                    if (i < rating) {
                        iv.setImageResource(R.mipmap.restinfo_small_rating_4);
                    } else {
                        iv.setImageResource(R.mipmap.restinfo_small_rating_3);
                    }
                    i++;
                }
                tvPian1.setTextColor(getResources().getColor(R.color.brief_text_gray));
                tvStar1.setTextColor(getResources().getColor(R.color.brief_text_gray));
            }

        }

        public void initNum(int num) {
            tvStar1.setText(String.valueOf(num));
            if (num == 0) {
                ratingView.setVisibility(View.GONE);
            } else {
                ratingView.setVisibility(View.VISIBLE);
            }
        }
    }


    class ActionViewHolder implements View.OnClickListener {
        View actionView;
        @Bind(tv_attention)
        TextView tvAttention;
        @Bind(R.id.tv_attention_pian)
        TextView tvAttentionPian;
        @Bind(R.id.linearLayout1)
        LinearLayout linearLayout1;
        @Bind(R.id.tv_attention_text)
        TextView tvAttentionText;
        @Bind(R.id.line_attention)
        View lineAttention;
        @Bind(R.id.rl_attention)
        RelativeLayout rlAttention;
        @Bind(R.id.tv_da)
        TextView tvDa;
        @Bind(R.id.tv_da_pian)
        TextView tvDaPian;
        @Bind(R.id.linearLayout2)
        LinearLayout linearLayout2;
        @Bind(R.id.tv_da_text)
        TextView tvDaText;
        @Bind(R.id.line_da)
        View lineDa;
        @Bind(R.id.rl_da)
        RelativeLayout rlDa;
        @Bind(R.id.tv_score_title)
        TextView tvScoreTitle;
        @Bind(R.id.tv_score)
        TextView tvScore;
        @Bind(R.id.tv_all)
        TextView tvAll;
        @Bind(R.id.ll_ratings_container)
        LinearLayout llRatingsContainer;
        @Bind(R.id.ll_action_container)
        LinearLayout ll_action_container;

        RatingHolder[] ratingHolders = new RatingHolder[4];

        public void setView(View view) {
            if (view != null) {
                actionView = view;
            } else {
                actionView = getLayoutInflater().inflate(R.layout.item_restinfo_float_action, null);
            }
            ButterKnife.bind(this, actionView);
            for (int i = 0; i < 4; i++) {
                ratingHolders[i] = new RatingHolder();
                ratingHolders[i].setView(i + 1);
                ratingHolders[i].initSelection(0);
                ratingHolders[i].ratingView.setOnClickListener(this);
                llRatingsContainer.addView(ratingHolders[i].ratingView);
            }
        }

        public void refresh() {
            if (response != null) {
                tvAttention.setText(String.valueOf(response.getAttentionInfo().getTotalNumOfFeeds()));
                tvDa.setText(String.valueOf(response.getDaRenJianInfo().getTotalNumOfFeeds()));
            } else {
                tvDa.setText("0");
                tvAttention.setText("0");
            }
            refreshTabs();
            switchType();
        }

        public void onClick1(int id) {
            switch (id) {
                case R.id.rl_attention: {
                    if (response != null){
                        response.setCurrentCategoryDataType(0);
                    }
                    load(false);
                }
                break;
                case R.id.rl_da: {
                    if (response != null){
                        response.setCurrentCategoryDataType(1);
                    }
                    load(false);
                }
                break;
                case R.id.ll_score_container: {
                    if (llRatingsContainer.getVisibility() == View.GONE) {
                        llRatingsContainer.setVisibility(View.VISIBLE);
                        tvScore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.dynamic_detail_menu_down, 0);
                    } else {
//                        llRatingsContainer.setVisibility(View.GONE);
//                        tvScore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.dynamic_detail_menu_right, 0);
                    }
                }
                break;
                case R.id.tv_all:
                    if (response != null){
                        response.setCurrentStartLevelType(0);
                    }
                    load(false);
                    break;
                case R.drawable.alpha:
                    if (response != null){
                        response.setCurrentStartLevelType(1);
                    }
                    switchStar();
                    break;
                case R.drawable.alpha + 1:
                    if (response != null){
                        response.setCurrentStartLevelType(2);
                    }
                    switchStar();
                    break;
                case R.drawable.alpha + 2:
                    if (response != null){
                        response.setCurrentStartLevelType(3);
                    }
                    switchStar();
                    break;
                case R.drawable.alpha + 3:
                    if (response != null){
                        response.setCurrentStartLevelType(4);
                    }
                    switchStar();
                    break;
            }
        }

        private void refreshTabs() {
            boolean isAttention = response == null ? true : response.getCurrentCategoryDataType() == 0;
            int ff_text_black;
            int ff_text_gray;
            ff_text_black = R.color.ff_text_black;
            ff_text_gray = R.color.ff_text_gray;
            int color = getResources().getColor(isAttention ? ff_text_black : ff_text_gray);
            tvAttentionText.setTextColor(color);
            tvAttention.setTextColor(color);
            tvAttentionPian.setTextColor(color);

            color = getResources().getColor(!isAttention ? ff_text_black : ff_text_gray);
            tvDaText.setTextColor(color);
            tvDa.setTextColor(color);
            tvDaPian.setTextColor(color);

            lineAttention.setVisibility(isAttention ? View.VISIBLE : View.GONE);
            lineDa.setVisibility(isAttention ? View.GONE : View.VISIBLE);
            initstars(isAttention);
        }

        private void switchType() {
            if (response == null || response.getCurrentCategoryDataType() == 0) {
                tvScore.setText(response == null ? "0.0" : getFloat(response.getAttentionInfo().getScore()));
                tvScoreTitle.setText("关注的人综合评分");
                if (response == null || response.getAttentionInfo().getTotalNumOfFeeds() == 0 || response.getAttentionInfo().getScore() == 0) {
                    ll_action_container.setVisibility(View.GONE);
                } else {
                    ll_action_container.setVisibility(View.VISIBLE);
                }
            } else {
                tvScore.setText(response == null ? "0.0" : getFloat(response.getDaRenJianInfo().getScore()));
                tvScoreTitle.setText("达人荐综合评分");
                if (response == null || response.getDaRenJianInfo().getTotalNumOfFeeds() == 0 || response.getDaRenJianInfo().getScore() == 0) {
                    ll_action_container.setVisibility(View.GONE);
                } else {
                    ll_action_container.setVisibility(View.VISIBLE);
                }
            }
            int level = response == null ? 0 : response.getCurrentStartLevelType();
            if (level == 0) {
                tvAll.setTextColor(0xffffffff);
                tvAll.setBackgroundResource(R.drawable.restinfo_rating_2);
            } else {
                tvAll.setTextColor(getResources().getColor(R.color.ff_text_gray));
                tvAll.setBackgroundResource(R.drawable.restinfo_rating_1);
            }


            for (int i = 0; i < 4; i++) {
                ratingHolders[i].initSelection(level);
            }
        }

        private String getFloat(double f) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String string = fnum.format(f);
            return string;
        }

        private void initstars(boolean isAttention) {
            if (response == null) {
                return;
            }
            RestInfoResult.AttentionInfoBean bean;
            if (isAttention) {
                bean = response.getAttentionInfo();
            } else {
                bean = response.getDaRenJianInfo();
            }

            for (int i = 0; i < 4; i++) {
                ratingHolders[i].initNum(bean.getStartLevelList().get(i));
            }
        }


        @OnClick({R.id.rl_attention, R.id.rl_da, R.id.ll_score_container, R.id.tv_all})
        public void onClick(View view) {
            if (view.getId() == R.id.ll_score_container) {
                if (this == headHolder.actionViewHolder1) {
                    actionViewHolder2.onClick1(view.getId());
                } else {
                    headHolder.actionViewHolder1.onClick1(view.getId());
                }
            }
            onClick1(view.getId());
        }
    }

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();
        load(false);
    }

    class HeadHolder {
        RemarkManager rm;

        public HeadHolder() {
            headView = getLayoutInflater().inflate(R.layout.view_restinfo_head, lvRestinfo, false);
            iv_cover = (ImageView) headView.findViewById(R.id.iv_cover);
            tv_location = (TextView) headView.findViewById(R.id.tv_location);
            tv_class = (TextView) headView.findViewById(R.id.tv_class);
            tv_per_people = (TextView) headView.findViewById(R.id.tv_per_people);
            tv_name = (TextView) headView.findViewById(R.id.tv_name);
            line_phone = (View) headView.findViewById(R.id.line_phone);
            tv_address = (TextView) headView.findViewById(R.id.tv_address);
            line_address = (View) headView.findViewById(R.id.line_address);
            tv_phone = (TextView) headView.findViewById(R.id.tv_phone);
            btn_want_eat = (TextView) headView.findViewById(R.id.btn_want_eat);
            ratingBar = (RatingBar) headView.findViewById(R.id.ratingBar);
            tv_score = (TextView) headView.findViewById(R.id.tv_score);
            rm = new RemarkManager(headView);
            actionViewHolder1 = new ActionViewHolder();
            actionViewHolder1.setView(null);

            ((LinearLayout) headView).addView(actionViewHolder1.actionView);
        }


        ImageView iv_cover;
        TextView tv_location;
        RatingBar ratingBar;
        TextView tv_score;
        TextView tv_class;
        TextView tv_per_people;
        TextView tv_name;
        View line_phone;
        TextView tv_address;
        View line_address;
        TextView tv_phone;
        TextView btn_want_eat;
        ActionViewHolder actionViewHolder1;

        public void refresh() {
            actionViewHolder1.refresh();
            actionViewHolder2.refresh();
            rm.setData(response==null?null:response.getFindMerchantTags());
            FFImageLoader.loadBigImage(RestInfoActivity.this, info.getMerchantImage().getUrl(), iv_cover);
            FFUtils.setText(this.tv_location, info.getMerchantArea());
            FFUtils.setText(this.tv_address, info.getMerchantAddress());
            this.tv_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (info != null && info.getMerchantPoi() != null) {
                        double lat = info.getMerchantPoi().getLatitude();
                        double lng = info.getMerchantPoi().getLongitude();
                        String name = info.getMerchantName();
                        String address = info.getMerchantAddress();
                        startActivity(RestLocationActivity.class, new RestLocationIntent().
                                setAddress(address).
                                setName(name).
                                setLng(lng).
                                setLat(lat));
                    }
                }
            });
            FFUtils.setText(this.tv_class, info.getMerchantKind());
            if (FFUtils.getSubFloat(info.getMerchantPrice()).equals("0")) {
                this.tv_per_people.setVisibility(View.GONE);
            } else {
                this.tv_per_people.setVisibility(View.VISIBLE);
                this.tv_per_people.setText(FFUtils.getSubFloat(info.getMerchantPrice()) + "/人");
            }
            refreshWantEat();


            if (!FFUtils.isStringEmpty(info.getMerchantPhone())) {
                this.tv_phone.setText(info.getMerchantPhone());
                tv_phone.setVisibility(View.VISIBLE);
            } else {
                tv_phone.setVisibility(View.GONE);
            }
            this.tv_name.setText(info.getMerchantName());

            float startLevel = info.getStartLevel();
            if (startLevel > 0) {
                ((RelativeLayout.LayoutParams) tv_name.getLayoutParams()).setMargins(FFUtils.getPx(16), 0, 0, FFUtils.getPx(60));
                this.ratingBar.setVisibility(View.VISIBLE);
                this.tv_score.setVisibility(View.VISIBLE);
                this.ratingBar.setRating(startLevel);
                this.tv_score.setText(startLevel + "");
            } else {
                ((RelativeLayout.LayoutParams) tv_name.getLayoutParams()).setMargins(FFUtils.getPx(16), 0, 0, FFUtils.getPx(40));
                this.ratingBar.setVisibility(View.GONE);
                this.tv_score.setVisibility(View.GONE);
            }

            this.tv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CallDialog(RestInfoActivity.this, R.style.ActionSheetDialogStyle, info.getMerchantPhone()).show();
                }
            });
        }

        private void refreshWantEat() {
            if (info.isMerchantIsWant()) {
                this.btn_want_eat.setBackgroundResource(R.drawable.restinfo_want_true);
                this.btn_want_eat.setOnClickListener(null);
            } else {
                this.btn_want_eat.setBackgroundResource(R.drawable.restinfo_want_false);
                this.btn_want_eat.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             if (FFUtils.isStringEmpty(SP.getUid())) {
                                                                 LoginIntentData loginIntentData = new LoginIntentData();
                                                                 loginIntentData.setCode("");
                                                                 startActivity(LoginOneActivity.class, loginIntentData);
                                                                 return;
                                                             }
                                                             post(IUrlUtils.Search.addFoodEatV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                                                                         @Override
                                                                         public void onSuccess(BaseResult response, FFExtraParams extra) {
                                                                             showToast("已将该商户加入您的想吃清单中");
                                                                             info.setMerchantIsWant(true);
                                                                             refreshWantEat();
                                                                             HashMap<String, String> event = new HashMap<String, String>();
                                                                             event.put("account", SP.getUid());
                                                                             event.put("shop_id", info.getMerchantUid());
                                                                             event.put("record_id", ""); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                                                                             event.put("from", RestInfoActivity.class.getName());
                                                                             pushEventAction("Yellow_051", event);
                                                                         }

                                                                         @Override
                                                                         public boolean onFail(FFExtraParams extra) {
                                                                             return true;
                                                                         }
                                                                     }, "recordId", "",
                                                                     "id", info.getMerchantUid(),
                                                                     "isResource", "5",
                                                                     "shopImage", info.getMerchantImage().getUrl(),
                                                                     "shopType", 0);
                                                         }
                                                     }

                );
            }
        }
    }

    private BaseAdapter adapter;
    private SYFindMerchantInfo info;
    private List<RestInfoFeed> feeds = new ArrayList<>();
    private View headView;
    private ImageView iv_commit_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBottom();
        setContentView(R.layout.activity_rest_info);

        setNotitle(true);
        ButterKnife.bind(this);
        iv_commit_error = findView(R.id.iv_commit);
        actionViewHolder2 = new ActionViewHolder();
        actionViewHolder2.setView(findViewById(R.id.view_action));
        info = getIntentData().getInfo();
        load(false);
        if (info == null) {
            return;
        }
        init();
    }

    private void init() {
        headHolder = new HeadHolder();
        headHolder.refresh();

        lvRestinfo.setOnScrollListener(new AbsListView.OnScrollListener() {
            int scrollOffset = sStatusBar.getLayoutParams().height + FFUtils.getPx(48);

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean b1 = firstVisibleItem == 0 && view.getChildCount() > 0;
                if (b1 && (scrollOffset > Math.abs(view.getChildAt(0).getTop()))) {
                    int alpha = 0xff * Math.abs(view.getChildAt(0).getTop()) / scrollOffset;
                    int color = alpha << 24 | 0x00ffffff;
                    rlTitle.setBackgroundColor(color);
                    sStatusBar.setBackgroundColor(color);
                    ivBack.setImageResource(R.mipmap.ff_ic_back_normal);
                    iv_close.setImageResource(R.mipmap.ic_close_three_level_page_white);
                    tvTitle.setTextColor(0xffffffff);
                    vTitleBottom.setVisibility(View.INVISIBLE);
                    iv_commit_error.setImageResource(R.mipmap.iv_other_commit_error);
                    ivShare.setImageResource(R.mipmap.restinfo_share);//白色icon
                } else {//100 %
                    rlTitle.setBackgroundColor(0xffffffff);
                    sStatusBar.setBackgroundColor(0xff000000);
                    ivBack.setImageResource(R.mipmap.ff_ic_back_pressed);
                    iv_close.setImageResource(R.mipmap.ic_close_three_level_page_black);
                    tvTitle.setTextColor(0xff000000);
                    vTitleBottom.setVisibility(View.VISIBLE);
                    iv_commit_error.setImageResource(R.mipmap.commit_error_img);
                    ivShare.setImageResource(R.mipmap.want_eat_detail_share);
                }
                if (headHolder.actionViewHolder1.actionView == null) {
                    return;
                }
                if (view.getChildCount() > 0 && lvRestinfo.getFirstVisiblePosition() == 0) {
                    int top = lvRestinfo.getChildAt(0).getTop();
                    FFLogUtil.e("asdf", (headHolder.actionViewHolder1.actionView.getTop() + top) + "   " + vTitleBottom.getBottom());
                    if (headHolder.actionViewHolder1.actionView.getTop() + top <= vTitleBottom.getBottom()) {
                        actionViewHolder2.actionView.setVisibility(View.VISIBLE);
                    } else {
                        actionViewHolder2.actionView.setVisibility(View.GONE);
                    }

                } else {
                    actionViewHolder2.actionView.setVisibility(View.VISIBLE);
                }
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String feedId = "";
                if (feeds != null && !feeds.isEmpty()) {
                    feedId = feeds.get(0).getId();
                }
                final String clientTime = TimeUtils.getTime("yyyyMMddhhmmssSSS", System.currentTimeMillis());
//                post(Constants.shareConstants().getNetHeaderAdress() + "/discover/createDisShopDetailsHtmlV260.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                post(IUrlUtils.Search.createDisShopDetailsHtmlV260, "", null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {
                                final String url = ShareUrlTools.getRestInfoUrl(info.getMerchantUid(), clientTime);
                                View contentView = LayoutInflater.from(context()).inflate(
                                        R.layout.pop_share, null);
                                final PopupWindow popupWindow = new PopupWindow(contentView,
                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                                // 设置按钮的点击事件
                                TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
                                TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
                                TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);
                                final TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                                ((View) tv_del.getParent()).setVisibility(View.GONE);
                                contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                    }
                                });
                                final String title = info.getMerchantName();
                                StringBuilder textBuilder = new StringBuilder();
                                if (info.getMerchantPrice() != 0) {
                                    textBuilder.append("￥" + FFUtils.getSubFloat(info.getMerchantPrice()) + "/人 | ");
                                }
                                if (!FFUtils.isStringEmpty(info.getMerchantKind())) {
                                    textBuilder.append(info.getMerchantKind() + " | ");
                                }
                                textBuilder.append(info.getMerchantAddress());
                                final String text = textBuilder.toString();
                                tv_pyq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final int id = showProgressDialog("");
                                        FFImageLoader.load_base(context(), info.getMerchantImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                            @Override
                                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                                dismissProgressDialog(id);
                                                popupWindow.dismiss();
                                                if (bitmap == null) {
                                                    return;
                                                }
                                                WeixinOpen.getInstance().share2weixin_pyq(url, text, title, bitmap);
                                            }

                                            @Override
                                            public void onDownLoadProgress(int downloaded, int contentLength) {

                                            }
                                        });
                                    }
                                });

                                tv_py.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final int id = showProgressDialog("");
                                        FFImageLoader.load_base(context(), info.getMerchantImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                            @Override
                                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                                dismissProgressDialog(id);
                                                popupWindow.dismiss();
                                                if (bitmap == null) {
                                                    return;
                                                }
                                                WeixinOpen.getInstance().share2weixin(url, text, title, bitmap);
                                            }

                                            @Override
                                            public void onDownLoadProgress(int downloaded, int contentLength) {

                                            }
                                        });
                                    }
                                });
                                tv_sina.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final int id = showProgressDialog("");
                                        FFImageLoader.load_base(context(), info.getMerchantImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                            @Override
                                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                                dismissProgressDialog(id);
                                                popupWindow.dismiss();
                                                if (bitmap == null) {
                                                    return;
                                                }
                                                SinaOpen.share(context(), bitmap, title, text, url);
                                            }

                                            @Override
                                            public void onDownLoadProgress(int downloaded, int contentLength) {

                                            }
                                        });
                                    }
                                });
//                }


                                popupWindow.setTouchable(true);

                                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        Log.i("mengdd", "onTouch : ");

                                        return false;
                                    }
                                });
                                popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
                                popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "clientTime", clientTime
                        , "merchantId", info.getMerchantUid(),
                        "shopImg", info.getMerchantImage().getUrl()
                        , "pageSize", 3
                        , "recordId", feedId);
            }


        });
        //报错
        iv_commit_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FFUtils.isStringEmpty(SP.getUid())) {
                    LoginIntentData loginIntentData = new LoginIntentData();
                    loginIntentData.setCode("");
                    startActivity(LoginOneActivity.class, loginIntentData);
                    return;
                }
                ShopErrorInfoIntent infoIntent = new ShopErrorInfoIntent();
                infoIntent.setShopid(info.getMerchantUid());
                infoIntent.setName(info.getMerchantName());
                infoIntent.setPhone(info.getMerchantPhone());
                infoIntent.setLat(info.getMerchantPoi().getLongitude());
                infoIntent.setLng(info.getMerchantPoi().getLongitude());
                infoIntent.setAddress(info.getMerchantAddress());
                startActivity(ShopErrorTypeActivity.class, infoIntent);
            }
        });

        if (!SP.isLogin()) {
            iv_commit_error.setVisibility(View.GONE);
        }

        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                if (feeds == null || feeds.size() == 0) {
                    return 1;
                }
                return feeds.size() + 1;
            }

            @Override
            public Object getItem(int position) {
                if (position == 0) {//头
                    return null;
                }
                return feeds.get(position - 1);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 8;
            }


            /**
             * 0 商户信息view
             * 1 第一条图文的头部
             * 2 纯文本类型的富文本
             * 3 带图片的富文本
             * 4 展开全部按钮
             * 5 其他文本
             * @param position
             * @return
             */
            @Override
            public int getItemViewType(int position) {
                if (position == 0) {//头
                    return 0;
                }
                return feeds.get(position - 1).getFood().wasMeishiBianji() ? 5 : 7;
            }


            class Holder5 {
                ImageView iv_avatar;
                TextView tv_name;
                TextView tv_title;
                TextView tv_time;
                ImageView iv_da;
                ImageView iv_guan;
                DynamicImageView iv_cover;
                LinearLayout ll_level_container;
                RatingBar rb_level;
                TextView tv_level;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                switch (getItemViewType(position)) {
                    case 0:
                        return headView;
                    case 5:
                        return getType5(position, convertView, parent);
                    case 7:
                        return getType7(position, convertView, parent);
                }
                return null;
            }

            private View getType5(int position, View convertView, ViewGroup parent) {

                final Holder5 holder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_restinfo_dynamic, parent, false);
                    holder = new Holder5();
                    convertView.setTag(holder);
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.iv_da = (ImageView) convertView.findViewById(R.id.iv_da);
                    holder.iv_guan = (ImageView) convertView.findViewById(R.id.iv_guan);
                    holder.iv_cover = (DynamicImageView) convertView.findViewById(R.id.iv_cover);
                    holder.ll_level_container = (LinearLayout) convertView.findViewById(R.id.ll_level_container);
                    holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
                    holder.rb_level = (RatingBar) convertView.findViewById(R.id.rb_level);
                } else {
                    holder = (Holder5) convertView.getTag();
                }
                final RestInfoFeed feed = (RestInfoFeed) getItem(position);
                if (feed.getStarLevel() == 0) {
                    holder.ll_level_container.setVisibility(View.GONE);
                } else {
                    holder.ll_level_container.setVisibility(View.VISIBLE);
                    holder.rb_level.setRating(feed.getStarLevel());
                    holder.tv_level.setText(feed.pullStartLevelString());
                }
                //TODO zhangfan
                FFImageLoader.loadAvatar(context(), feed.getUser().getHeadImage().getUrl(), holder.iv_avatar);
                holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!feed.getUser().getId().equals(SP.getUid())) {
                            UserInfoIntent intent = new UserInfoIntent();
                            intent.setUser(feed.getUser());
                            startActivity(UserInfoActivity.class, intent);
                        } else {
                            MainActivity.toUser();
                            finishAllActivitysByTag(ActivityTags.main);
                        }
                    }
                });
                {
                    SYImage img = feed.getFood().pullCoverImg();
                    if (img == null || img.getWidth() == 0) {
                        holder.iv_cover.getLayoutParams().height = FFUtils.getPx(200);
                        holder.iv_cover.setWidth(FFUtils.getDisWidth());
                    } else {
                        holder.iv_cover.getLayoutParams().height = (int) (FFUtils.getDisWidth() * img.getHeight() / img.getWidth());
                        holder.iv_cover.getLayoutParams().width = FFUtils.getDisWidth();
                    }
                    FFImageLoader.load_base(context(), feed.getFood().pullCoverImage(), holder.iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                        @Override
                        public void imageLoaded(Bitmap bitmap, String imageUrl) {
                            if (bitmap == null) {
                                return;
                            }
                            holder.iv_cover.getLayoutParams().height = (FFUtils.getDisWidth() * bitmap.getHeight() / bitmap.getWidth());
                            holder.iv_cover.getLayoutParams().width = FFUtils.getDisWidth();
                        }

                        @Override
                        public void onDownLoadProgress(int downloaded, int contentLength) {

                        }
                    });

                }
                holder.tv_name.setText(feed.getUser().getNickName());
                holder.tv_time.setText(TimeUtils.getTime("yyyy-MM-dd", feed.getTimeStamp()));
                if (feed.isbAttention()) {
                    holder.iv_guan.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_guan.setVisibility(View.GONE);
                }
                if (feed.isbDaRRenRecommend()) {
                    holder.iv_da.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_da.setVisibility(View.GONE);
                }

                FFUtils.setText(holder.tv_title, feed.getFood().getFrontCoverModel().getFrontCoverContent().getContent());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(feed, false, false));
                    }
                });
                return convertView;
            }

            private View getType7(int position, View convertView, ViewGroup parent) {

                final Holder7 holder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_restinfo_type7_fast, parent, false);
                    holder = new Holder7();
                    convertView.setTag(holder);
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    holder.iv_da = (ImageView) convertView.findViewById(R.id.iv_da);
                    holder.iv_guan = (ImageView) convertView.findViewById(R.id.iv_guan);
                    holder.ll_level_container = (LinearLayout) convertView.findViewById(R.id.ll_level_container);
                    holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
                    holder.rb_level = (RatingBar) convertView.findViewById(R.id.rb_level);


                    holder.ll_nine_pic_container = (LinearLayout) convertView.findViewById(R.id.ll_nine_pic_container);
                    holder.iv_dynamic_1 = (ImageView) convertView.findViewById(R.id.iv_dynamic_1);
                    holder.iv_dynamic_2 = (ImageView) convertView.findViewById(R.id.iv_dynamic_2);
                    holder.iv_dynamic_3 = (ImageView) convertView.findViewById(R.id.iv_dynamic_3);
                    holder.iv_dynamic_4 = (ImageView) convertView.findViewById(R.id.iv_dynamic_4);
                    holder.iv_dynamic_5 = (ImageView) convertView.findViewById(R.id.iv_dynamic_5);
                    holder.iv_dynamic_6 = (ImageView) convertView.findViewById(R.id.iv_dynamic_6);
                    holder.iv_dynamic_7 = (ImageView) convertView.findViewById(R.id.iv_dynamic_7);
                    holder.iv_dynamic_8 = (ImageView) convertView.findViewById(R.id.iv_dynamic_8);
                    holder.iv_dynamic_9 = (ImageView) convertView.findViewById(R.id.iv_dynamic_9);
                    holder.fl_dynamic_lastImage_container = (FrameLayout) convertView.findViewById(R.id.fl_dynamic_lastImage_container);
                    holder.tv_dynamic_moreImage = (TextView) convertView.findViewById(R.id.tv_dynamic_moreImage);
                    holder.iv_dynamic_msbj_only = (ImageView) convertView.findViewById(R.id.iv_dynamic_msbj_only);
                    holder.iv_dynamic_mssj_only = (ImageView) convertView.findViewById(R.id.iv_dynamic_mssj_only);

                    holder.ivs[0] = holder.iv_dynamic_1;
                    holder.ivs[1] = holder.iv_dynamic_2;
                    holder.ivs[2] = holder.iv_dynamic_3;
                    holder.ivs[3] = holder.iv_dynamic_4;
                    holder.ivs[4] = holder.iv_dynamic_5;
                    holder.ivs[5] = holder.iv_dynamic_6;
                    holder.ivs[6] = holder.iv_dynamic_7;
                    holder.ivs[7] = holder.iv_dynamic_8;
                    holder.iv_dynamic_mssj_only.getLayoutParams().height = onlyImageHight;
                    holder.iv_dynamic_msbj_only.getLayoutParams().height = msbjImageHight;
                    holder.iv_dynamic_mssj_only.getLayoutParams().width = mssjImageWidth;
                    holder.iv_dynamic_msbj_only.getLayoutParams().width = FFUtils.getDisWidth();
                    for (ImageView iv : holder.ivs) {
                        iv.getLayoutParams().height = nineImageHight;
                        iv.getLayoutParams().width = nineImageHight;
                    }
                    holder.fl_dynamic_lastImage_container.getLayoutParams().height = nineImageHight;
                    holder.fl_dynamic_lastImage_container.getLayoutParams().width = nineImageHight;


                } else {
                    holder = (Holder7) convertView.getTag();
                }
                final RestInfoFeed feed = (RestInfoFeed) getItem(position);
                if (feed.getStarLevel() == 0) {
                    holder.ll_level_container.setVisibility(View.GONE);
                } else {
                    holder.ll_level_container.setVisibility(View.VISIBLE);
                    holder.rb_level.setRating(feed.getStarLevel());
                    holder.tv_level.setText(feed.pullStartLevelString());
                }

                initPics(holder, feed, context());

                //TODO zhangfan
                FFImageLoader.loadAvatar(context(), feed.getUser().getHeadImage().getUrl(), holder.iv_avatar);
                holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!feed.getUser().getId().equals(SP.getUid())) {
                            UserInfoIntent intent = new UserInfoIntent();
                            intent.setUser(feed.getUser());
                            startActivity(UserInfoActivity.class, intent);
                        } else {
                            MainActivity.toUser();
                            finishAllActivitysByTag(ActivityTags.main);
                        }
                    }
                });
                holder.tv_name.setText(feed.getUser().getNickName());
                holder.tv_time.setText(TimeUtils.getTime("yyyy-MM-dd", feed.getTimeStamp()));
                if (feed.isbAttention()) {
                    holder.iv_guan.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_guan.setVisibility(View.GONE);
                }
                if (feed.isbDaRRenRecommend()) {
                    holder.iv_da.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_da.setVisibility(View.GONE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(FastDetailActivity.class, new FastDetailIntent(feed.getId()));
                    }
                });
                return convertView;
            }
        };
        lvRestinfo.setAdapter(adapter);

        prl = PullToRefreshLayout.supportPull(lvRestinfo, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                load(true);
            }
        });
        prl.setDoPullUp(false);
        prl.setDoPullDown(false);
    }


    private void load(boolean isLoadMore) {
        String id;
        if (info == null) {
            id = getIntentData().getId();
        } else if (FFUtils.isStringEmpty(info.getMerchantUid())) {
            return;
        } else {
            id = info.getMerchantUid();
        }
//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/queryDiscoverShopDetailV260.do", "", null, new FFNetWorkCallBack<RestInfoResult>() {
        post(IUrlUtils.Search.queryDiscoverShopDetailV260, "", null, new FFNetWorkCallBack<RestInfoResult>() {
            @Override
            public void onSuccess(RestInfoResult response, FFExtraParams extra) {

                RestInfoActivity.this.response = response;
                SYFindMerchantInfo buinessDetail = response.getBuinessDetail();
                buinessDetail.setStartLevel(getIntentData().getStar());
                if (info == null) {
//                    if (feeds != null) {
//                        feeds.addAll(response.getShareFeedsList());
//                    } else {
                    feeds = response.getShareFeedsList();
//                    }
                    info = buinessDetail;
                    init();
                } else {
//                    if (feeds != null) {
//                        feeds.addAll(response.getShareFeedsList());
//                    } else {
                    feeds = response.getShareFeedsList();
//                    }
                    info = buinessDetail;
                }
//                prl.setDoPullUp(!FFUtils.isListEmpty(response.getShareFeedsList()));
                headHolder.refresh();
                adapter.notifyDataSetChanged();
                prl.loadmoreFinish(prl.SUCCEED);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(prl.FAIL);
                //TODO zhangfan
                return false;
            }
        }, "shopId", id, "currentCategoryDataType", response == null ? 0 : response.getCurrentCategoryDataType());
    }

    private void switchStar() {
        String id;
        if (info == null) {
            id = getIntentData().getId();
        } else if (FFUtils.isStringEmpty(info.getMerchantUid())) {
            return;
        } else {
            id = info.getMerchantUid();
        }
//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/querShopFeedsByStarLevel.do", "", null, new FFNetWorkCallBack<RestInfoResult>() {
        post(IUrlUtils.Search.querShopFeedsByStarLevel, "", null, new FFNetWorkCallBack<RestInfoResult>() {
            @Override
            public void onSuccess(RestInfoResult response, FFExtraParams extra) {
                response.setAttentionInfo(RestInfoActivity.this.response.getAttentionInfo());
                response.setDaRenJianInfo(RestInfoActivity.this.response.getDaRenJianInfo());
                RestInfoActivity.this.response = response;
                feeds = response.getShareFeedsList();
                headHolder.refresh();
                adapter.notifyDataSetChanged();
                prl.loadmoreFinish(prl.SUCCEED);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(prl.FAIL);
                //TODO zhangfan
                return false;
            }
        }, "shopId", id, "currentCategoryDataType", response == null ? 0 : response.getCurrentCategoryDataType(), "currentStartLevelType", response == null ? 0 : response.getCurrentStartLevelType());
    }

    public static class RestInfoFeed extends SYFeed {
        private boolean bDaRRenRecommend;
        private boolean bAttention;

        public int getStarLevel() {
            return starLevel;
        }

        public void setStarLevel(int starLevel) {
            this.starLevel = starLevel;
        }

        private int starLevel;

        public boolean isbAttention() {
            return bAttention;
        }

        public void setbAttention(boolean bAttention) {
            this.bAttention = bAttention;
        }

        public boolean isbDaRRenRecommend() {
            return bDaRRenRecommend;
        }

        public void setbDaRRenRecommend(boolean bDaRRenRecommend) {
            this.bDaRRenRecommend = bDaRRenRecommend;
        }
    }

    public static void initPics(Holder7 holder, SYFeed item, final FFContext context) {
        if (item.getFood().wasMeishiBianji()) {//美食编辑
            holder.iv_dynamic_msbj_only.setVisibility(View.VISIBLE);
            holder.ll_nine_pic_container.setVisibility(View.GONE);
            holder.iv_dynamic_mssj_only.setVisibility(View.GONE);
            String imgUrl = item.getFood().pullCoverImage();
            FFImageLoader.loadBigImage(context, imgUrl, holder.iv_dynamic_msbj_only);
        } else {//美食速记
            int i = 0;
            int j = 0;
            SYFoodPhotoModel photo1 = null;
            for (; (i < 3) && (j < item.getFood().getRichTextLists().size()); j++) {
                SYFoodPhotoModel photo = item.getFood().getRichTextLists().get(j).getPhoto();
                if (item.getFood().getRichTextLists().get(j).getPhoto() != null) {
                    i++;
                    photo1 = photo;
                } else {
                    continue;
                }
            }
            if (i == 1) {//只有一张图片
                holder.iv_dynamic_msbj_only.setVisibility(View.GONE);
                holder.ll_nine_pic_container.setVisibility(View.GONE);
                holder.iv_dynamic_mssj_only.setVisibility(View.VISIBLE);
                FFImageLoader.loadBigImage(context, photo1.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_mssj_only);
            } else {
                j = 0;
                holder.iv_dynamic_mssj_only.setVisibility(View.GONE);
                holder.iv_dynamic_msbj_only.setVisibility(View.GONE);
                holder.ll_nine_pic_container.setVisibility(View.VISIBLE);
                for (ImageView iv : holder.ivs) {
                    iv.setVisibility(View.GONE);
                }
                holder.fl_dynamic_lastImage_container.setVisibility(View.GONE);
                holder.tv_dynamic_moreImage.setVisibility(View.GONE);

                for (i = 0; (i < 3) && (j < item.getFood().getRichTextLists().size()); j++) {
                    if (item.getFood().getRichTextLists().get(j).getPhoto() != null) {
                        i++;
                        holder.ivs[i - 1].setVisibility(View.VISIBLE);
                        FFImageLoader.loadMiddleImage(context, item.getFood().getRichTextLists().get(j).getPhoto().getImageAsset().pullProcessedImageUrl(), holder.ivs[i - 1]);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

}
