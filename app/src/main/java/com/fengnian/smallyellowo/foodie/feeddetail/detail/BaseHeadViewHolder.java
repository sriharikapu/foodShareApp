package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.MainHomeUGCFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

/**
 * Created by Administrator on 2017-2-23.
 */

public abstract class BaseHeadViewHolder {
    private View headView;
    private ImageView iv_avator,iv_add_crown;
    protected LinearLayout ll_level_container;
    protected RatingBar rb_level;
    private TextView tv_level;
    private TextView tv_name;
    protected TextView tv_title;
    protected TextView tv_time;
    protected ImageView iv_is_jing;
    protected String titleStr;

    public DynamicDetailActivity getActivity() {
        return activity;
    }

    private DynamicDetailActivity activity;

    public BaseHeadViewHolder(DynamicDetailActivity activity, View headView) {
        this.activity = activity;
        setHeadView(headView);
    }

    public void setHeadView(View headView) {
        this.headView = headView;
        iv_avator = (ImageView) findViewById(R.id.iv_avator);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_level_container = (LinearLayout) findViewById(R.id.ll_level_container);
        rb_level = (RatingBar) findViewById(R.id.rb_level);
        tv_level = (TextView) findViewById(R.id.tv_level);
        iv_add_crown = (ImageView) findViewById(R.id.iv_add_crown);
        onSetHeadView();
    }

    protected abstract void onSetHeadView();


    public View getHeadView() {
        return headView;
    }

    public View findViewById(int id) {
        return headView.findViewById(id);
    }

    public abstract View getTv_attention();

    protected abstract void refresh1();

    public void refresh() {
        if (!getActivity().getIntentData().isPreview()) {
            View.OnClickListener listener = new View.OnClickListener() {
                public void onClick(View v) {
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        getActivity().startActivity(LoginOneActivity.class, loginIntentData);
                        return;
                    }
                    if (getActivity().data == null) {
                        return;
                    }
                    getActivity().data.getUser().setByFollowMe(!getActivity().data.getUser().isByFollowMe());
//                    getActivity().post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                    getActivity().post(IUrlUtils.UserCenter.attentionOrNotV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, "followId", getActivity().data.getUser().getId(), "followState", getActivity().data.getUser().isByFollowMe() ? 1 : 0);
                    initAttention();
                }
            };
            initAttention();
            getTv_attention().setOnClickListener(listener);
            if (getActivity().iv_menu_attent != null)
                getActivity().iv_menu_attent.setOnClickListener(listener);
        } else {
            getTv_attention().setVisibility(View.GONE);
            if (getActivity().iv_menu_attent != null) {
                getTv_attention().setVisibility(View.GONE);
            }
        }

        if (getActivity().data == null) {
            return;
        }

        if (getActivity().data.getStarLevel() == 0) {
            if (ll_level_container != null){
                ll_level_container.setVisibility(View.GONE);
            }
        } else {
            if (ll_level_container != null){
                ll_level_container.setVisibility(View.VISIBLE);
            }

            if (rb_level != null){
                rb_level.setRating(getActivity().data.getStarLevel());
            }
            if (tv_level != null){
                tv_level.setText(getActivity().data.pullStartLevelString());
            }
        }

        if (iv_avator != null){
            if (getActivity().data != null &&
                    getActivity().data.getUser() != null &&
                    getActivity().data.getUser().getHeadImage() != null &&
                    getActivity().data.getUser().getHeadImage().getUrl() != null) {

                //是否显示头像皇冠
                if (iv_add_crown != null){
                    IsAddCrownUtils.checkIsAddCrow(getActivity().data.getUser(),iv_add_crown);
                }
                FFImageLoader.loadAvatar(getActivity(), getActivity().data.getUser().getHeadImage().getUrl(), iv_avator);
                iv_avator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!getActivity().data.getUser().getId().equals(SP.getUid())) {
                            UserInfoIntent intent = new UserInfoIntent();
                            intent.setUser(getActivity().data.getUser());
                            intent.setRequestCode(DynamicDetailActivity.INTENT_USER);
//                            getActivity().startActivity(UserInfoActivity.class, intent);

                            IsAddCrownUtils.ActivtyStartAct(getActivity().data.getUser(),intent,activity);
                        }
                    }
                });
            }
        }

        if (getActivity().data.getUser() != null) {
            if (tv_name != null){
                tv_name.setText(getActivity().data.getUser().getNickName());
            }
        }

        titleStr = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();

        if (tv_title != null) {
            if (!FFUtils.isStringEmpty(titleStr)) {
                tv_title.setText(titleStr);
            } else {
                tv_title.setText("");
            }
        }

        if (tv_time != null){
            tv_time.setText("时间:" + TimeUtils.getTime("yyyy-MM-dd    HH:mm", getActivity().data.getTimeStamp()));
        }

        //是否精华内容
        if (iv_is_jing != null){
            if (getActivity().data.isHandPick()) {
                iv_is_jing.setVisibility(View.VISIBLE);
            } else {
                iv_is_jing.setVisibility(View.GONE);
            }
        }

        refresh1();
    }


    public void initAttention() {
        if (getActivity().getIntentData().isPreview() || getActivity().getIntentData().isMineMode()) {
            if (getActivity().getIntentData().isMineMode()) {
                getTv_attention().setVisibility(View.GONE);
                if (getActivity().iv_menu_attent != null){
                    getActivity().iv_menu_attent.setVisibility(View.GONE);
                }
            }
            return;
        }

        if (!SP.isLogin()){
            initAttention(2);
            return;
        }

        if (getActivity().data.getUser() == null || getActivity().data.getUser().getId().equals(SP.getUid())) {
            getTv_attention().setVisibility(View.GONE);
            if (getActivity().iv_menu_attent != null){
                getActivity().iv_menu_attent.setVisibility(View.GONE);
            }
            return;
        }

        if (getActivity().data.getUser().isByFollowMe()) {
            if (getActivity().data.getUser().isFollowMe()) {
                initAttention(0);
                if (getActivity().iv_menu_attent != null) {
                    getActivity().iv_menu_attent.setImageResource(R.mipmap.ic_attented_eachother);
                }
            } else {
                initAttention(1);
                if (getActivity().iv_menu_attent != null) {
                    getActivity().iv_menu_attent.setImageResource(R.mipmap.ic_attented);
                }
            }
        } else {
            initAttention(2);
            if (getActivity().iv_menu_attent != null) {
                getActivity().iv_menu_attent.setImageResource(R.mipmap.ic_add_attent);
            }
        }

    }

    protected void initRest(View restEvent) {
        if (getActivity().data.getFood().getPoi() == null || TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {//商户名称
            restEvent.setVisibility(View.GONE);
        } else {
            restEvent.setVisibility(View.VISIBLE);
            restEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity().data.getFood().getPoi().getIsCustom() == 1) {
                        getActivity().showToast("该商户为自定义创建，暂无地址信息");
                        return;
                    }

//                    getActivity().post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                    getActivity().post(IUrlUtils.Search.queryShopDrawerInfoV250, "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                        @Override
                        public void onSuccess(MainHomeUGCFragment.RestInfoDrawerResult response, FFExtraParams extra) {
                            new PopRest(getActivity(), null, response.getBuinessDetail().getMerchantPhone(),
                                    getActivity().data.getFood().getPoi().getAddress(), getActivity().data.getFood().getPoi().getTitle(), getActivity().data.getFood().getPoi().getLatitude(), getActivity().data.getFood().getPoi().getLongitude(),getActivity().data.getFood().getPoi().getId()).showAtLocation((View) getActivity().getContainer().getParent(), Gravity.CENTER, 0, 0);
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, "merchantId", getActivity().data.getFood().getPoi().getId());
                }
            });
        }
    }


    protected abstract void initAttention(int status);
}
