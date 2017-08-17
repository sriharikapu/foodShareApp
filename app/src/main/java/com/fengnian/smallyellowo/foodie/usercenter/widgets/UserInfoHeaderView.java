package com.fengnian.smallyellowo.foodie.usercenter.widgets;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.CheckBigimgActivty;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserDynamicActivity;
import com.fengnian.smallyellowo.foodie.View.DialogView;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYCommentGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYConcernedFansGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFoodGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFulfillGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYGoodChoiceGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYWantEatGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.publics.Logindate;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.intentdatas.UserDynamicIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.personal.FansActivty;
import com.fengnian.smallyellowo.foodie.personal.MyAllAttionActivity;
import com.fengnian.smallyellowo.foodie.usercenter.bean.UserInfoBean;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/12.
 */

public class UserInfoHeaderView extends RelativeLayout {
    /*@Bind(R.id.rv_fail_one)
    RelativeLayout rvFailOne;*/
    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.imageView7)
    ImageView imageView7;
    /*@Bind(R.id.iv_back)
    ImageView ivBack;*/
    /*@Bind(R.id.iv_close)
    public ImageView ivClose;*/
    /*@Bind(R.id.tv_add_attention)
    TextView tvAddAttention;*/
    @Bind(R.id.iv_avator)
    ImageView ivAvator;
    @Bind(R.id.tv_222)
    TextView tv222;
    @Bind(R.id.tv_name)
    public TextView tvName;
    @Bind(R.id.tv_set_nickname)
    public TextView tvSetNickname;
    @Bind(R.id.iv_sex)
    ImageView ivSex;
    @Bind(R.id.iv_edit)
    public ImageView ivEdit;
    @Bind(R.id.tv_dynamic)
    TextView tvDynamic;
    @Bind(R.id.tv_attention)
    TextView tvAttention;
    @Bind(R.id.tv_fans)
    TextView tvFans;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @Bind(R.id.text_share)
    TextView textShare;
    @Bind(R.id.text_milestone)
    TextView textMilestone;
    @Bind(R.id.text_admire)
    TextView textAdmire;
    @Bind(R.id.dynamic_layout)
    LinearLayout dynamicLayout;
    @Bind(R.id.food_type)
    public FoodTypeLayout foodType;
    @Bind(R.id.food_type_ll)
    public LinearLayout foodTypeLl;
    @Bind(R.id.no_data_ll)
    RelativeLayout noDataLl;
    @Bind(R.id.share_count)
    public TextView shareCount;
    @Bind(R.id.image)
    ImageView noDataImage;
    @Bind(R.id.text)
    TextView noDataText;
    @Bind(R.id.share_count_ll)
    public LinearLayout shareCountLl;

    private SYUser user;
    public String uid;
    private BaseActivity activity;
    private DialogView dialogView;

    public UserInfoHeaderView(Context context, String userId) {
        super(context);
        activity = (BaseActivity) context;
        this.uid = userId;
        initView();
        setClickListener();
    }

    public UserInfoHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (BaseActivity) context;
        initView();
        setClickListener();
    }

    public UserInfoHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (BaseActivity) context;
        initView();
        setClickListener();
    }

    private void initView() {
        View header = inflate(getContext(), R.layout.view_user_header, null);
        ButterKnife.bind(this, header);
        addView(header);
    }

    private void setClickListener() {
        ivAvator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FFUtils.checkNet()) {
                    Intent intent = new Intent(activity, CheckBigimgActivty.class);
                    String url = "";
                    if (user != null)
                        url = user.getHeadImage().getUrl();
                    intent.putExtra("url", url);
                    activity.startActivity(intent);
                } else {
                    activity.showToast(activity.getString(R.string.lsq_network_connection_interruption));
                }
            }
        });

        tvDynamic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid;
                if (FFUtils.checkNet()) {
                    if (user != null) {
                        uid = user.getId();
                    } else {
                        return;
                    }

                    activity.startActivity(UserDynamicActivity.class, new UserDynamicIntent().setUserId(uid).setUserName(user.getNickName()));
                } else {
                    activity.showToast(activity.getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        tvAttention.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FFUtils.checkNet()) {
                    UserInfoIntent userinfo = new UserInfoIntent();
                    userinfo.setUser(user);
                    activity.startActivity(MyAllAttionActivity.class, userinfo);
                } else {
                    activity.showToast(activity.getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        tvFans.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FFUtils.checkNet()) {
                    UserInfoIntent userinfo = new UserInfoIntent();
                    if (user == null) user = new SYUser();
                    userinfo.setUser(user);
                    activity.startActivity(FansActivty.class, userinfo);
                } else {
                    activity.showToast(activity.getString(R.string.lsq_network_connection_interruption));
                }
            }
        });

        ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = new DialogView(activity, activity, user);
                dialogView.setMyCallBack(new DialogView.MyCallOnclick() {
                    @Override
                    public void myBack(String str) {
                        setUserInfo(str);
                    }
                });
                dialogView.show();

                Window window = dialogView.getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            }
        });

    }

    public void setUserId(String userId) {
        this.uid = userId;
    }

    public void setUser(SYUser user, boolean needUser) {
        this.user = user;
        FFImageLoader.loadAvatar(activity, user.getHeadImage().getUrl(), ivAvator);
        tvName.setText(user.getNickName());
        if (user.getSex() == 0) {
            ivSex.setImageResource(R.mipmap.new_boy_one);
        } else {
            ivSex.setImageResource(R.mipmap._one);
        }

        if (user.getPersonalDeclaration() != null && user.getPersonalDeclaration().length() > 0)
            tvSign.setText("个签:  " + user.getPersonalDeclaration());
        else
            tvSign.setText("");
        tvDynamic.setText("动态 " + user.getDynamicNumber());
        tvAttention.setText("关注 " + user.getFollowCount());
        tvFans.setText("粉丝 " + user.getFansCount());
        FFImageLoader.loadBigImage((FFContext) getContext(), user.getBgImage().getUrl(), imageView3, R.mipmap.userinfo_defaultbg);


        if (user != null && user.isByFollowMe() && user.isFollowMe()) {
            ivEdit.setVisibility(View.VISIBLE);

            setIsShowRemarkname(user);

        } else if (user != null && !user.isByFollowMe()) {
            ivEdit.setVisibility(View.GONE);
            tvName.setText(user.getTmpNickname());
            tvSetNickname.setText("");

        } else if (user != null && user.isByFollowMe()) {
            ivEdit.setVisibility(View.VISIBLE);
            setIsShowRemarkname(user);
        }
    }

    void setIsShowRemarkname(SYUser us) {
        if (TextUtils.isEmpty(us.getRemarkName())) {
            tvName.setText(user.getTmpNickname());
            tvSetNickname.setText("");
        } else {
            tvName.setText(us.getNickName());
            tvSetNickname.setText("昵称：" + us.getTmpNickname());
        }
    }

    /**
     * @param followId    (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    public void checkIsAttion(String followId, String followState, final SYUser user) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
        activity.post(IUrlUtils.UserCenter.attentionOrNotV250, null, extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if (response == null) return;
                if ("success".equals(response.getResult())) {
                    String AttionState = response.getAttentionState();
                    if ("01".equals(AttionState)) {//wo关注
                        user.setByFollowMe(true);
                        ivEdit.setVisibility(View.VISIBLE);
                        if (watchListener != null) {
                            watchListener.addWatched();
                        }

                    } else if ("00".equals(AttionState)) {
                        user.setByFollowMe(false);
                        ivEdit.setVisibility(View.GONE);
                        user.setRemarkName("");
                        tvName.setText(user.getTmpNickname());
                        tvSetNickname.setText("");

                        if (watchListener != null) {
                            watchListener.unWatched();
                        }

                    } else if ("10".equals(AttionState)) {//关注wo
                        user.setByFollowMe(false);
                        ivEdit.setVisibility(View.GONE);
                        user.setRemarkName("");
                        tvName.setText(user.getTmpNickname());
                        tvSetNickname.setText("");

                        if (watchListener != null) {
                            watchListener.unWatched();
                        }

                    } else if ("11".equals(AttionState)) { //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                        ivEdit.setVisibility(View.VISIBLE);
                        if (watchListener != null) {
                            watchListener.eachWatched();
                        }
                    }
                    //Todo  将点击的变成改变的
                } else {
                    activity.showToast(response.getReturnmessage());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "followId", followId, "followState", followState);
    }

    private void setUserInfo(final String remarkname) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        activity.post(IUrlUtils.Search.setTargetUserRemarkName, "", extra, new FFNetWorkCallBack<Logindate>() {
            @Override
            public void onSuccess(Logindate response, FFExtraParams extra) {
                if (response.judge()) {
                    user = response.getUser();
                    if (dialogView != null)
                        dialogView.dismiss();
                    setIsShowRemarkname(user);

                } else {
                    activity.showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "targetAccount", user.getId(), "remarkName", remarkname);
    }

    public void setDynamicInfo(List<UserInfoBean.UserStatEntity> dynamicInfo) {
        dynamicLayout.setVisibility(VISIBLE);
        for (UserInfoBean.UserStatEntity dynamic : dynamicInfo) {
            if (dynamic.getType() == 1) {
                textShare.setVisibility(VISIBLE);
                textShare.setText(Html.fromHtml(dynamic.getShareInfo()));

            } else if (dynamic.getType() == 2) {
                String content = "";
                String day = dynamic.getDate();
                if (dynamic.getData() instanceof SYFoodGrowthRecord) {
                    content = ((SYFoodGrowthRecord) dynamic.getData()).getCompleteCountList().get(0).providerTypeString();

                } else if (dynamic.getData() instanceof SYFulfillGrowthRecord) {
                    content = ((SYFulfillGrowthRecord) dynamic.getData()).getContentString();

                } else if (dynamic.getData() instanceof SYCommentGrowthRecord) {
                    content = ((SYCommentGrowthRecord) dynamic.getData()).getContentString();

                } else if (dynamic.getData() instanceof SYWantEatGrowthRecord) {
                    content = ((SYWantEatGrowthRecord) dynamic.getData()).getCompleteCountList().get(0).providerTypeString();

                } else if (dynamic.getData() instanceof SYGoodChoiceGrowthRecord) {
                    content = ((SYGoodChoiceGrowthRecord) dynamic.getData()).getCompleteCountList().get(0).providerTypeString();

                } else if (dynamic.getData() instanceof SYConcernedFansGrowthRecord) {
                    content = ((SYConcernedFansGrowthRecord) dynamic.getData()).getContentString();
                }

                if (TextUtils.isEmpty(content)) {
                    textMilestone.setVisibility(GONE);
                    return;
                }
                textMilestone.setVisibility(VISIBLE);
                textMilestone.setText(Html.fromHtml(day + content));

            } else if (dynamic.getType() == 3) {
                textAdmire.setVisibility(VISIBLE);
                textAdmire.setText(Html.fromHtml(dynamic.getAdmireInfo()));
            }
        }
    }

    private WatchListener watchListener;

    public void setWatchListener(WatchListener watchListener) {
        this.watchListener = watchListener;
    }

    public void showNoDataLayout(boolean empty) {
        if (empty) {
            int h = relativeLayout1.getHeight();
            h += dynamicLayout.getHeight();
            h += foodTypeLl.getHeight();
            FFLogUtil.e("header", "height = " + h);
            int screenH = FFUtils.getScreenHeight();
            FFLogUtil.e("header", "screenH = " + screenH);
            int error = screenH - h;
            FFLogUtil.e("header", "error = " + error);
            noDataText.setText("好友还没有发布动态\n可以看看其他人的动态");
            noDataText.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) noDataText.getLayoutParams();
            textParams.setMargins(0, 20, 0, 0);
            noDataText.setLineSpacing(0, 1.2f);
            noDataText.setLayoutParams(textParams);

            noDataText.setTextColor(getResources().getColor(R.color.ff_text_gray));
            LayoutParams params = (LayoutParams) noDataLl.getLayoutParams();
            params.height = error;
            noDataLl.setLayoutParams(params);
            noDataLl.bringToFront();
        }
        noDataLl.setVisibility(empty ? VISIBLE : GONE);
    }

    public interface WatchListener {
        /**
         * 添加关注
         */
        void addWatched();

        /**
         * 取消关注
         */
        void unWatched();

        /**
         * 相互关注
         */
        void eachWatched();
    }
}
