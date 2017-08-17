package com.fengnian.smallyellowo.foodie.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.Adapter.ClubMyselfIntroductionAdapter;
import com.fengnian.smallyellowo.foodie.Adapter.Club_User_Jing_Adapter;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserDynamicActivity;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.View.DialogView;
import com.fengnian.smallyellowo.foodie.View.UpDownListView;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.Logindate;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SYClubVipUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SYVipUserRecommend;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.bean.results.ClubUserinfoResult;
import com.fengnian.smallyellowo.foodie.bean.results.FoodsClassResult;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserDynamicIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.personal.FansActivty;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.personal.MyAllAttionActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-5-26.
 */

public class ClubUserInfoFragment extends BaseFragment implements View.OnClickListener {

    ClubUserinfoResult response;

    public void setUser(ClubUserinfoResult response) {
        if (iv_back == null) {
            this.response = response;
            return;
        }
        SYClubVipUser vipsuer = response.getClubUser();
        this.user = vipsuer.getUser();
        setvalue(vipsuer);

        jingxuan_list.addAll(response.getVipUserRecommend());

        iv_club_dongtai_fail.setVisibility(View.GONE);
        if (jingxuan_list.size() > 0) {
            rl_1_1.setVisibility(View.VISIBLE);
        } else {
            rl_1_1.setVisibility(View.GONE);
        }
        jingxuan_adapter.notifyDataSetChanged();
        set_other_userinfo();
    }

    private ImageView iv_back, iv_sex, iv_nodata, head_tv_edit, iv_close, iv_img_bg;
    private TextView head_tv_nickname, tv_qianming, club_my_attion, my_fans,
            tv_huiyuan_bianhao, tv_is_attion, tv_set_nickname;
    private RelativeLayout rl_5, rl_one_no_intnet;

    private ListView ry_recycle_view, list_view;

    private List<SYVipUserRecommend> jingxuan_list;

    private Club_User_Jing_Adapter jingxuan_adapter;

    ClubMyselfIntroductionAdapter adapter1;
    Drawable img_xinghu_guanzhu;
    SYUser user;
    private RelativeLayout rv_fail_one;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                onNetStatusChanged(FFUtils.checkNet());
            }
        }
    };

    @Override
    protected UserInfoActivity getBaseActivity() {
        return (UserInfoActivity) super.getBaseActivity();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.club_user_info_activity, container, false);
    }

    @Override
    public void onFindView() {
        getBaseActivity().setContentView(R.layout.club_user_info_activity);

        getBaseActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        iv_back = findView(R.id.iv_back);
        iv_back.setOnClickListener(this);

        rv_fail_one = findView(R.id.inc_1);

        jingxuan_list = new ArrayList<>();
        ry_recycle_view = findView(R.id.ry_recycle_view);


        View head_view = View.inflate(getBaseActivity(), R.layout.club_user_info_headview, null);

        iv_img_bg = (ImageView) head_view.findViewById(R.id.iv_img_bg);
        tv_is_attion = (TextView) head_view.findViewById(R.id.tv_is_attion);

        tv_is_attion.setOnClickListener(this);
//        iv_avator = (ImageView) head_view.findViewById(R.id.iv_avator);

        head_tv_edit = (ImageView) head_view.findViewById(R.id.head_tv_edit);
        head_tv_edit.setOnClickListener(this);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        head_tv_nickname = (TextView) head_view.findViewById(R.id.head_tv_nickname);
        tv_set_nickname = (TextView) head_view.findViewById(R.id.tv_set_nickname);
        iv_sex = (ImageView) head_view.findViewById(R.id.iv_sex);
        tv_qianming = (TextView) head_view.findViewById(R.id.tv_qianming);
        club_my_attion = (TextView) head_view.findViewById(R.id.club_my_attion);
        club_my_attion.setOnClickListener(this);
        my_fans = (TextView) head_view.findViewById(R.id.my_fans);
        my_fans.setOnClickListener(this);

        tv_huiyuan_bianhao = (TextView) head_view.findViewById(R.id.tv_huiyuan_bianhao);
        tv_huiyuan_bianhao.setOnClickListener(this);

        rl_5 = (RelativeLayout) head_view.findViewById(R.id.rl_5);
        list_view = (ListView) head_view.findViewById(R.id.list_view);
        iv_nodata = (ImageView) head_view.findViewById(R.id.iv_nodata);

        user = getBaseActivity().getIntentData().getUser();

        adapter1 = new ClubMyselfIntroductionAdapter(getBaseActivity());
        list_view.setAdapter(adapter1);

        jingxuan_adapter = new Club_User_Jing_Adapter(getBaseActivity(), jingxuan_list);

        ry_recycle_view.setAdapter(jingxuan_adapter);

        View bottom_view = View.inflate(getBaseActivity(), R.layout.club_footview, null);

        rl_one_no_intnet = (RelativeLayout) bottom_view.findViewById(R.id.rl_one_no_intnet);
        rl_one_no_intnet.setOnClickListener(this);
        setbotomview(bottom_view);
        ry_recycle_view.addHeaderView(head_view);
        ry_recycle_view.addFooterView(bottom_view);
        if (getBaseActivity().uid.equals(SP.getUid())) {
            head_tv_edit.setVisibility(View.GONE);
            tv_set_nickname.setVisibility(View.GONE);
        } else {
            head_tv_edit.setVisibility(View.VISIBLE);
            tv_set_nickname.setVisibility(View.VISIBLE);
        }
        setScrolltitle();

        setUser(response);
    }

    ImageView iv_club_dongtai_fail;
    RelativeLayout rl_1_1;

    private RadioGroup rg_tab;
    private LinearLayout fl_area;
    private RelativeLayout fl_class, rl_no;
    private com.fengnian.smallyellowo.foodie.widgets.PieChart pc_class;
    private com.fengnian.smallyellowo.foodie.widgets.ArcVisView avv_meng;
    private LinearLayout ll_class_info;
    private ImageView iv_top;

    void setbotomview(View Bottom_view) {
        iv_club_dongtai_fail = (ImageView) Bottom_view.findViewById(R.id.iv_club_dongtai_fail);
        iv_club_dongtai_fail.setOnClickListener(this);
        rl_1_1 = (RelativeLayout) Bottom_view.findViewById(R.id.rl_1_1);
        rl_1_1.setOnClickListener(this);
        rg_tab = (RadioGroup) Bottom_view.findViewById(R.id.rg_tab);

        fl_area = (LinearLayout) Bottom_view.findViewById(R.id.fl_area);

        fl_class = (RelativeLayout) Bottom_view.findViewById(R.id.fl_class);
        rl_no = (RelativeLayout) Bottom_view.findViewById(R.id.rl_no);

        pc_class = (com.fengnian.smallyellowo.foodie.widgets.PieChart) Bottom_view.findViewById(R.id.pc_class);
        avv_meng = (com.fengnian.smallyellowo.foodie.widgets.ArcVisView) Bottom_view.findViewById(R.id.avv_meng);
        ll_class_info = (LinearLayout) Bottom_view.findViewById(R.id.ll_class_info);
        iv_top = (ImageView) Bottom_view.findViewById(R.id.iv_top);


        pc_class.setCenterTexts(ll_class_info);

        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class) {
                    fl_area.setVisibility(View.GONE);
                    fl_class.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rb_area) {
                    fl_area.setVisibility(View.VISIBLE);
                    fl_class.setVisibility(View.GONE);
                }
            }
        });

    }

    private static final int[] AllColors = new int[]{0xffFF9933, 0xffFFCC44, 0xffFF8800, 0xffFFD426, 0xffF87400, 0xffFF9900, 0xffFFBB00, 0xffFF9911, 0xffEB4E2F, 0xffCC3300};

    /**
     * 品类
     */
    private void set_other_userinfo() {
        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/queryOthersFoodNotesClassificationV250.do", "", null, new FFNetWorkCallBack<FoodsClassResult>() {
            @Override
            public void onSuccess(FoodsClassResult response, FFExtraParams extra) {
                if (response.getSyFoodNotesClassifications() == null || response.getSyFoodNotesClassifications().size() == 0) {
                    rl_no.setVisibility(View.VISIBLE);
                    iv_top.setVisibility(View.GONE);
                    ll_class_info.setVisibility(View.GONE);
                    return;
                } else {
                    String[] titles = new String[response.getSyFoodNotesClassifications().size()];
                    double[] percent = new double[titles.length];
                    int[] colors = new int[titles.length];
                    int i = 0;
                    for (FoodsClassResult.SyFoodNotesClassificationsBean clazz : response.getSyFoodNotesClassifications()) {
                        titles[i] = clazz.getType();
                        percent[i] = clazz.getCounts();
                        colors[i] = AllColors[i % AllColors.length];
                        i++;
                    }
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pc_class.getLayoutParams();
                    layoutParams.width = FFUtils.getScreenWidth();
                    layoutParams.height = FFUtils.getScreenWidth();
                    pc_class.setLayoutParams(layoutParams);
                    pc_class.setTitles(titles);
                    pc_class.setValues(percent);
                    pc_class.setColors(colors);
                    pc_class.invalidate();
                    avv_meng.start();
                    int sum = titles.length;
                    if (sum > 10) {
                        sum = 10;
                    }
                    rl_no.setVisibility(View.GONE);
                    if (sum > 0) {
                        iv_top.setVisibility(View.VISIBLE);
                        iv_top.setImageResource(new int[]{R.mipmap.top1, R.mipmap.top2, R.mipmap.top3, R.mipmap.top4, R.mipmap.top5, R.mipmap.top6, R.mipmap.top7, R.mipmap.top8, R.mipmap.top9, R.mipmap.top10}[sum - 1]);
                        ll_class_info.setVisibility(View.VISIBLE);
                    } else {
                        iv_top.setVisibility(View.GONE);
                        ll_class_info.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "userId", getBaseActivity().uid, "type", "1");
        /**
         * 区域
         */
        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/queryOthersFoodNotesClassificationV250.do", "", null, new FFNetWorkCallBack<FoodsClassResult>() {
            @Override
            public void onSuccess(FoodsClassResult response, FFExtraParams extra) {
                if (response.getSyFoodNotesClassifications() != null) {
                    int i = 0;
                    int[] imgs = new int[]{R.mipmap.top1, R.mipmap.top2, R.mipmap.top3, R.mipmap.top4, R.mipmap.top5};
                    int[] bgs = new int[]{R.drawable.top5_1, R.drawable.top5_2, R.drawable.top5_3, R.drawable.top5_4, R.drawable.top5_5};
                    int first = 0;
                    int maxLength = 0;
                    for (FoodsClassResult.SyFoodNotesClassificationsBean clazz : response.getSyFoodNotesClassifications()) {
                        if (i >= imgs.length) {
                            break;
                        }
                        View convertView = getBaseActivity().getLayoutInflater().inflate(R.layout.item_userinfo_topten, fl_area, false);
                        ImageView iv_top_number = (ImageView) convertView.findViewById(R.id.iv_top_number);
                        TextView tv_top_count = (TextView) convertView.findViewById(R.id.tv_top_count);
                        TextView tv_top_address = (TextView) convertView.findViewById(R.id.tv_top_address);
                        fl_area.addView(convertView);
                        if (i == 0) {
                            first = clazz.getCounts();
                            maxLength = FFUtils.getDisWidth() - FFUtils.getPx(20 + 16 + 42) - FFUtils.getTextLength(12) * 5;
//                            maxLength = FFUtils.getDisWidth() - FFUtils.getPx(20 + 16 + 42) - FFUtils.getTextLength(14) * 5;
                            tv_top_count.getLayoutParams().width = maxLength;
                        } else {
                            tv_top_count.getLayoutParams().width = clazz.getCounts() * maxLength / first;
                        }
                        iv_top_number.setImageResource(imgs[i]);
                        tv_top_count.setText(String.valueOf(clazz.getCounts()));
                        tv_top_address.setText(clazz.getType());
                        tv_top_count.setBackgroundResource(bgs[i]);
                        if ((maxLength - tv_top_count.getLayoutParams().width) / FFUtils.getTextLength(14) > (clazz.getType().length() - 5)) {
                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else {
                            String type = clazz.getType().substring(0, clazz.getType().length() / 2 + clazz.getType().length() % 2) + "\r\n" + clazz.getType().substring(clazz.getType().length() / 2 + clazz.getType().length() % 2);
                            tv_top_address.setText(type);
                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                        }
                        i++;
                    }
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "userId", getBaseActivity().uid, "type", "2");

    }

    private void getuserinfo() {
        post(Constants.shareConstants().getNetHeaderAdress() + "/user/getIndexPersonalInfoV250.do", "", null, new FFNetWorkCallBack<ClubUserinfoResult>() {
            @Override
            public void onSuccess(ClubUserinfoResult response, FFExtraParams extra) {
                setUser(response);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "targetAccount", getBaseActivity().uid, "type", "2");
    }


    void setvalue(SYClubVipUser clubVipUser) {
        img_xinghu_guanzhu = this.getResources().getDrawable(R.mipmap.ic_attention_eachother);
        img_xinghu_guanzhu.setBounds(0, 0, img_xinghu_guanzhu.getMinimumWidth(), img_xinghu_guanzhu.getMinimumHeight());
        if (SP.getUid() != null && getBaseActivity().uid != null) {
            if (SP.getUid().equals(getBaseActivity().uid)) {
                tv_is_attion.setVisibility(View.GONE);
            } else {
                tv_is_attion.setVisibility(View.VISIBLE);
            }
        }
        if (user.getBgImage() != null)
            FFImageLoader.loadBigImage(this, user.getBgImage().getUrl(), iv_img_bg);
        head_tv_nickname.setText(user.getNickName());

        setIsattion();


        if (user.getSex() == 0) {
            iv_sex.setImageResource(R.mipmap._one);
        } else if (user.getSex() == 1) {
            iv_sex.setImageResource(R.mipmap._one);
        } else if (user.getSex() == -1) {
        }
        if (user.getPersonalDeclaration() != null) {

            if (user.getPersonalDeclaration().length() > 0)
                tv_qianming.setText("个签：" + user.getPersonalDeclaration());
            else tv_qianming.setText("");
        } else {
            tv_qianming.setText("");
        }

        club_my_attion.setText("关注" + user.getFollowCount() + "");
        my_fans.setText("粉丝" + user.getFansCount() + "");

        tv_huiyuan_bianhao.setText("俱乐部" + clubVipUser.getClubNum());


        if (clubVipUser != null && clubVipUser.getVipUserImage() != null) {
            adapter1.setDataList(clubVipUser.getVipUserImage());
        }

        if (adapter1.getCount() == 0) {
            iv_nodata.setVisibility(View.VISIBLE);
        } else {
            iv_nodata.setVisibility(View.GONE);
        }
    }

    void setIsattion() {
        if (user.isByFollowMe() && user.isFollowMe()) {
            tv_is_attion.setCompoundDrawables(img_xinghu_guanzhu, null, null, null);
            tv_is_attion.setText("相互关注");
            head_tv_edit.setVisibility(View.VISIBLE);
            setIsShowRemarkname();
        } else if (!user.isByFollowMe()) {
            tv_is_attion.setText("+ 关注");
            tv_is_attion.setCompoundDrawables(null, null, null, null);
            head_tv_edit.setVisibility(View.GONE);
            head_tv_nickname.setText(user.getTmpNickname());
            tv_set_nickname.setVisibility(View.GONE);
        } else if (user.isByFollowMe()) {
            tv_is_attion.setText("√ 已关注");
            tv_is_attion.setCompoundDrawables(null, null, null, null);
            head_tv_edit.setVisibility(View.VISIBLE);
            setIsShowRemarkname();
        }
    }

    void setIsShowRemarkname() {
        if (TextUtils.isEmpty(user.getRemarkName())) {
            head_tv_nickname.setText(user.getTmpNickname());
            tv_set_nickname.setVisibility(View.GONE);
        } else {
            tv_set_nickname.setVisibility(View.VISIBLE);
            head_tv_nickname.setText(user.getNickName());
            tv_set_nickname.setText("昵称：" + user.getTmpNickname());
        }
    }


    DialogView dialog_view;

    private DialogView.MyCallOnclick mback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (!FFUtils.checkNet()) {
            showToast("暂无网络");
            return;
        }
        switch (v.getId()) {
            case R.id.rl_one_no_intnet:
                getuserinfo();
                break;
            case R.id.head_tv_edit:
//                if(dialog_view==null)
                dialog_view = new DialogView(getBaseActivity(), getBaseActivity(), user == null ? user : user);
                dialog_view.setMyCallBack(new DialogView.MyCallOnclick() {
                    @Override
                    public void myBack(String str) {
                        setUserInfo(str);
                    }
                });
                dialog_view.create();
                dialog_view.show();
                Window window = dialog_view.getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//                show();
                break;
            case R.id.iv_back:
                getBaseActivity().finish();
                break;
            case R.id.tv_is_attion:
                if (FFUtils.isStringEmpty(SP.getUid())) {
                    LoginIntentData loginIntentData = new LoginIntentData();
                    loginIntentData.setCode("");
                    startActivity(LoginOneActivity.class, loginIntentData);
                    return;
                }
                if (user != null) {
                    String stat = user.isByFollowMe() ? "0" : "1";
                    checkIsAttion(user.getId(), stat);
                } else {
                    String stat = user.isByFollowMe() ? "0" : "1";
                    checkIsAttion(user.getId(), stat);
                }

                break;
            case R.id.club_my_attion:
                UserInfoIntent info = new UserInfoIntent();
                info.setUser(user);
                startActivity(MyAllAttionActivity.class, info);
                break;
            case R.id.my_fans:
                UserInfoIntent userinfo = new UserInfoIntent();
                userinfo.setUser(user);
                startActivity(FansActivty.class, userinfo);
                break;
            case R.id.tv_huiyuan_bianhao: //会员编号
                WebInfo webInfo = new WebInfo();
                String url = "http://static.tinydonuts.cn/N-Club2.5.0.html?会员俱乐部";
                String title = "会员俱乐部";
                webInfo.setUrl(url);
                webInfo.setTitle(title);
                startActivity(CommonWebviewUtilActivity.class, webInfo);
                break;
            case R.id.iv_club_dongtai_fail:
                getuserinfo();
                break;
            case R.id.rl_1_1:
                UserDynamicIntent intent = new UserDynamicIntent();
                intent.setUserId(user.getId()).setUserName(user.getNickName());
                startActivity(UserDynamicActivity.class, intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FFUtils.checkNet()) {
            rv_fail_one.setVisibility(View.GONE);
        } else {
            rv_fail_one.setVisibility(View.VISIBLE);
        }


    }

    RelativeLayout rl_title;

    void setScrolltitle() {

        if (ry_recycle_view instanceof UpDownListView) {
            UpDownListView upDownListView = (UpDownListView) ry_recycle_view;
            upDownListView.setOnListViewUpDownListener(new UpDownListView.onListViewUpDownListener() {
                @Override
                public void onDownUp(boolean isUp) {
                    if (isUp) {//"上滑"
                        rl_title.animate().setDuration(300).translationY(-rl_title.getHeight());
                    } else {//"下滑"
                        rl_title.animate().setDuration(300).translationY(0);
                    }

                }
            });
        }


        rl_title = findView(R.id.rl_title);
        ry_recycle_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            int scrollOffset = FFUtils.getPx(48);

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 && view.getChildCount() > 0) {

                    if (scrollOffset > Math.abs(view.getChildAt(0).getTop())) {
                        int alpha = 0xff * Math.abs(view.getChildAt(0).getTop()) / scrollOffset;
                        int color = alpha << 24 | 0x00ffffff;
                        rl_title.setBackgroundColor(getResources().getColor(R.color.transparent));
                        iv_back.setImageResource(R.mipmap.ff_ic_back_normal);
                        iv_close.setImageResource(R.mipmap.ic_close_three_level_page_white);
                    } else {//100 %
                        rl_title.setBackgroundColor(0xfff3f3f3);
                        iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                        iv_close.setImageResource(R.mipmap.ic_close_three_level_page_black);
                    }

                }
            }
        });
    }

    /**
     * @param followId    (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private void checkIsAttion(String followId, String followState) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
        post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if (response == null) return;
                if ("success".equals(response.getResult())) {
                    String AttionState = response.getAttentionState();
                    if ("01".equals(AttionState)) {//wo关注
                        user.setByFollowMe(true);
                    } else if ("00".equals(AttionState)) {//取消关注
                        user.setByFollowMe(false);
                        user.setRemarkName("");
                    } else if ("10".equals(AttionState)) {//关注我
                        user.setByFollowMe(false);
                        user.setRemarkName("");
                    } else if ("11".equals(AttionState)) { //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                    }
                    setIsattion();
                } else {
                    showToast(response.getReturnmessage());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "followId", followId, "followState", followState);
    }

    void show() {
        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private void setUserInfo(final String remarkname) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.setTargetUserRemarkName, "", extra, new FFNetWorkCallBack<Logindate>() {
            @Override
            public void onSuccess(Logindate response, FFExtraParams extra) {
                user = response.getUser();
                if (dialog_view != null)
                    dialog_view.dismiss();
                if (!TextUtils.isEmpty(user.getRemarkName())) {
                    head_tv_nickname.setText(user.getRemarkName());
                    tv_set_nickname.setVisibility(View.VISIBLE);
                    tv_set_nickname.setText("昵称:" + user.getTmpNickname());
                } else {
                    head_tv_nickname.setText(user.getTmpNickname());
                    tv_set_nickname.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "targetAccount", getBaseActivity().uid, "remarkName", remarkname);
    }


    private BroadcastReceiver mBroadcastReceiver;

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_NO_NET)) {
                    if ("type_net".equals(intent.getStringExtra("type"))) {
                        boolean isConnect = intent.getBooleanExtra("isConnect", false);
                        onNetStatusChanged(isConnect);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_NO_NET);
        LocalBroadcastManager.getInstance(getBaseActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void onNetStatusChanged(boolean isConnect) {
        if (isConnect && rv_fail_one != null) {
            rv_fail_one.setVisibility(View.GONE);
        } else if (rv_fail_one != null) {
            rv_fail_one.setVisibility(View.VISIBLE);
        }
    }

    private void unregisterReceiver() {
        if (receiver != null)
            getBaseActivity().unregisterReceiver(receiver);
    }

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();
        getuserinfo();
    }
}
