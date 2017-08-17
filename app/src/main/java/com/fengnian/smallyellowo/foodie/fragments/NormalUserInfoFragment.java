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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.CheckBigimgActivty;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserDynamicActivity;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.View.DialogView;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.Logindate;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.bean.results.FoodsClassResult;
import com.fengnian.smallyellowo.foodie.bean.results.GetUserInfoResult;
import com.fengnian.smallyellowo.foodie.intentdatas.UserDynamicIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.personal.FansActivty;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.personal.MyAllAttionActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2017-5-26.
 */

public class NormalUserInfoFragment extends BaseFragment implements View.OnClickListener {
    private static final int[] AllColors = new int[]{0xffFF9933, 0xffFFCC44, 0xffFF8800, 0xffFFD426, 0xffF87400, 0xffFF9900, 0xffFFBB00, 0xffFF9911, 0xffEB4E2F, 0xffCC3300};
    private RelativeLayout relativeLayout1;
    private ImageView imageView3;
    private View s_status_bar;
    private ImageView iv_back;
    private ImageView iv_close;
    private TextView tv_add_attention;
    private ImageView iv_avator, iv_sex;
    private TextView tv_name;
    private LinearLayout linearLayout;
    private TextView tv_dynamic;
    private TextView tv_attention;
    private TextView tv_fans, tv_set_nickname;
    private TextView tv_sign;
    private RadioGroup rg_tab;
    private LinearLayout fl_area;
    private RelativeLayout fl_class, rl_no;
    private com.fengnian.smallyellowo.foodie.widgets.PieChart pc_class;
    private com.fengnian.smallyellowo.foodie.widgets.ArcVisView avv_meng;
    private LinearLayout ll_class_info;
    private ImageView iv_top, iv_edit;


    private SYUser user;
    Drawable img_xinghu_guanzhu;
    private RelativeLayout rv_fail_one;

    public void setUser(SYUser user,boolean needUser) {
        if (iv_back == null) {
            this.user = user;
            return;
        }
        this.user = user;
        FFImageLoader.loadAvatar(context(), user.getHeadImage().getUrl(), iv_avator);
        tv_name.setText(user.getNickName());
        if (user.getSex() == 0) {
            iv_sex.setImageResource(R.mipmap.new_boy_one);
        } else {
            iv_sex.setImageResource(R.mipmap._one);
        }

        if (user.getPersonalDeclaration() != null && user.getPersonalDeclaration().length() > 0)
            tv_sign.setText("个签:  " + user.getPersonalDeclaration());
        else
            tv_sign.setText("");
        tv_dynamic.setText("动态 " + user.getDynamicNumber());
        tv_attention.setText("关注 " + user.getFollowCount());
        tv_fans.setText("粉丝 " + user.getFansCount());
        FFImageLoader.loadBigImage(context(), user.getBgImage().getUrl(), imageView3, R.mipmap.userinfo_defaultbg);


        if (user != null && user.isByFollowMe() && user.isFollowMe()) {
            tv_add_attention.setCompoundDrawables(img_xinghu_guanzhu, null, null, null);
            tv_add_attention.setText("相互关注");
            iv_edit.setVisibility(View.VISIBLE);

            setIsShowRemarkname(user);

        } else if (user != null && !user.isByFollowMe()) {
            tv_add_attention.setText("+ 关注");
            tv_add_attention.setCompoundDrawables(null, null, null, null);
            iv_edit.setVisibility(View.GONE);
            tv_name.setText(user.getTmpNickname());
            tv_set_nickname.setText("");

        } else if (user != null && user.isByFollowMe()) {
            tv_add_attention.setText("√ 已关注");
            iv_edit.setVisibility(View.VISIBLE);
            tv_add_attention.setCompoundDrawables(null, null, null, null);
            setIsShowRemarkname(user);
        }
        getInfo(needUser);
    }
    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_info, container, false);
    }

    @Override
    protected UserInfoActivity getBaseActivity() {
        return (UserInfoActivity) super.getBaseActivity();
    }


    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();
        getInfo(true);
    }

    @Override
    public void onFindView() {
        rv_fail_one = findView(R.id.rv_fail_one);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        s_status_bar = findViewById(R.id.s_status_bar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_add_attention = (TextView) findViewById(R.id.tv_add_attention);


        iv_edit = findView(R.id.iv_edit);
        if (iv_edit != null)
            iv_edit.setOnClickListener(this);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        if (getBaseActivity().showClose()) {
            iv_close.setVisibility(View.VISIBLE);
        } else {
            iv_close.setVisibility(View.GONE);
        }

        tv_set_nickname = findView(R.id.tv_set_nickname);
        rl_no = findView(R.id.rl_no);

        iv_avator = (ImageView) findViewById(R.id.iv_avator);
        iv_avator.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_sex = findView(R.id.iv_sex);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_dynamic = (TextView) findViewById(R.id.tv_dynamic);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        fl_area = (LinearLayout) findViewById(R.id.fl_area);
        fl_class = (RelativeLayout) findViewById(R.id.fl_class);
        pc_class = (com.fengnian.smallyellowo.foodie.widgets.PieChart) findViewById(R.id.pc_class);
        avv_meng = (com.fengnian.smallyellowo.foodie.widgets.ArcVisView) findViewById(R.id.avv_meng);
        ll_class_info = (LinearLayout) findViewById(R.id.ll_class_info);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        pc_class.setCenterTexts(ll_class_info);

        img_xinghu_guanzhu = this.getResources().getDrawable(R.mipmap.ic_attention_eachother);
        img_xinghu_guanzhu.setBounds(0, 0, img_xinghu_guanzhu.getMinimumWidth(), img_xinghu_guanzhu.getMinimumHeight());

        //用户自己
        if (getBaseActivity().uid.equals(SP.getUid())) {
            tv_add_attention.setVisibility(View.GONE);
        }
        if (user != null && user.isByFollowMe() && user.isFollowMe()) {
            tv_add_attention.setCompoundDrawables(img_xinghu_guanzhu, null, null, null);
            tv_add_attention.setText("相互关注");
            iv_edit.setVisibility(View.VISIBLE);

            setIsShowRemarkname(user);

        } else if (user != null && !user.isByFollowMe()) {
            tv_add_attention.setText("+ 关注");
            iv_edit.setVisibility(View.GONE);
            tv_add_attention.setCompoundDrawables(null, null, null, null);

            tv_name.setText(user.getTmpNickname());
            tv_set_nickname.setText("");
        } else if (user != null && user.isByFollowMe()) {
            tv_add_attention.setText("√ 已关注");
            iv_edit.setVisibility(View.VISIBLE);
            tv_add_attention.setCompoundDrawables(null, null, null, null);

            setIsShowRemarkname(user);
        }
        iv_avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FFUtils.checkNet()) {
                    Intent intent = new Intent(getBaseActivity(), CheckBigimgActivty.class);
                    String url = "";
                    if (user != null)
                        url = user.getHeadImage().getUrl();
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        //todo  在此处判断uid 是否是用户自己的 做处理
        tv_add_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FFUtils.checkNet()) {

                    if (!SP.isLogin()) {
                        LoginOneActivity.startLogin(getBaseActivity());
                        return;
                    }

                    if (user == null) user = new SYUser();
                    String followId = user.isByFollowMe() ? "0" : "1";
                    checkIsAttion(user.getId(), followId, user);
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        tv_dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid;
                if (FFUtils.checkNet()) {
                    if (user != null) {
                        uid = user.getId();
                    } else {
                        return;
                    }

                    startActivity(UserDynamicActivity.class, new UserDynamicIntent().setUserId(uid).setUserName(user.getNickName()));
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        tv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FFUtils.checkNet()) {
                    UserInfoIntent userinfo = new UserInfoIntent();
                    userinfo.setUser(user);
                    startActivity(MyAllAttionActivity.class, userinfo);
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        tv_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FFUtils.checkNet()) {
                    UserInfoIntent userinfo = new UserInfoIntent();
                    if (user == null) user = new SYUser();
                    userinfo.setUser(user);
                    startActivity(FansActivty.class, userinfo);
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (FFUtils.checkNet()) {
                    if (checkedId == R.id.rb_class) {
                        fl_area.setVisibility(View.GONE);
                        fl_class.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.rb_area) {
                        fl_area.setVisibility(View.VISIBLE);
                        fl_class.setVisibility(View.GONE);
                    }
                } else {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                }
            }
        });
        getBaseActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        setUser(user,false);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                onNetStatusChanged(FFUtils.checkNet());
            }
        }
    };


    void setIsShowRemarkname(SYUser us) {
        if (TextUtils.isEmpty(us.getRemarkName())) {
            tv_name.setText(user.getTmpNickname());
            tv_set_nickname.setText("");
        } else {
            tv_name.setText(us.getNickName());
            tv_set_nickname.setText("昵称：" + us.getTmpNickname());
        }
    }

    void getInfo(boolean needUser) {
        if(needUser) {
            post(Constants.shareConstants().getNetHeaderAdress() + "/user/getIndexPersonalInfoV250.do", "", null, new FFNetWorkCallBack<GetUserInfoResult>() {
                @Override
                public void onSuccess(GetUserInfoResult response, FFExtraParams extra) {
                    setUser(response.getUser(), false);
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "targetAccount", getBaseActivity().uid, "type", 2);
        }
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

    /**
     * @param followId    (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private void checkIsAttion(String followId, String followState, final SYUser user) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
        post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", null, extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if (response == null) return;
                if ("success".equals(response.getResult())) {
                    String AttionState = response.getAttentionState();
                    if ("01".equals(AttionState)) {//wo关注
                        user.setByFollowMe(true);
                        tv_add_attention.setVisibility(View.VISIBLE);
                        tv_add_attention.setText("√ 已关注");
                        tv_add_attention.setCompoundDrawables(null, null, null, null);
                        iv_edit.setVisibility(View.VISIBLE);
                    } else if ("00".equals(AttionState)) {
                        tv_add_attention.setVisibility(View.VISIBLE);
                        user.setByFollowMe(false);
                        tv_add_attention.setText("+ 关注");
                        tv_add_attention.setCompoundDrawables(null, null, null, null);
                        iv_edit.setVisibility(View.GONE);
                        user.setRemarkName("");
                        tv_name.setText(user.getTmpNickname());
                        tv_set_nickname.setText("");
                    } else if ("10".equals(AttionState)) {//关注wo
                        tv_add_attention.setVisibility(View.VISIBLE);
                        user.setByFollowMe(false);
                        tv_add_attention.setText("+ 关注");
                        tv_add_attention.setCompoundDrawables(null, null, null, null);
                        iv_edit.setVisibility(View.GONE);
                        user.setRemarkName("");
                        tv_name.setText(user.getTmpNickname());
                        tv_set_nickname.setText("");
                    } else if ("11".equals(AttionState)) { //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                        tv_add_attention.setVisibility(View.VISIBLE);
                        tv_add_attention.setText("相互关注");
                        iv_edit.setVisibility(View.VISIBLE);
                        tv_add_attention.setCompoundDrawables(img_xinghu_guanzhu, null, null, null);
//                        R.mipmap.ic_attention_eachother
                    }
                    //Todo  将点击的变成改变的
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

    @Override
    public void onResume() {
        super.onResume();
        if (FFUtils.checkNet()) {
            rv_fail_one.setVisibility(View.GONE);
        } else {
            rv_fail_one.setVisibility(View.VISIBLE);
        }
    }


    DialogView dialogView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        if (!FFUtils.checkNet()) {
            showToast("暂无网络");
            return;
        }
        switch (v.getId()) {
            case R.id.iv_edit:
                dialogView = new DialogView(getBaseActivity(), getBaseActivity(), user);
                dialogView.setMyCallBack(new DialogView.MyCallOnclick() {
                    @Override
                    public void myBack(String str) {
                        setUserInfo(str);
                    }
                });
                dialogView.show();

                Window window = dialogView.getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                break;
        }
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
                if (response.judge()) {
                    user = response.getUser();
                    if (dialogView != null)
                        dialogView.dismiss();
//                    if (user.getRemarkName() != null && user.getRemarkName().length() > 0) {
//
//                        tv_name.setText(user.getNickName());
//                        tv_set_nickname.setText("昵称:"+user.getTmpNickname());
//                    } else {
//                        tv_name.setText(user.getNickName());
//                        tv_set_nickname.setText("昵称:"+user.getTmpNickname());
//                    }

                    setIsShowRemarkname(user);

                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "targetAccount", user.getId(), "remarkName", remarkname);
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
        LocalBroadcastManager.getInstance(getBaseActivity().getApplicationContext()).registerReceiver(mBroadcastReceiver, intentFilter);

//        MainActivity.onNetStatusChanged(FFUtils.checkNet());
    }


    @Override
    public void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    public void onNetStatusChanged(boolean isConnect) {
        if (isConnect && rv_fail_one != null) {
            rv_fail_one.setVisibility(View.GONE);
        } else if (rv_fail_one != null) {
            rv_fail_one.setVisibility(View.VISIBLE);
        }
    }

    private void unregisterReceiver() {
//        if (mBroadcastReceiver != null) {
//            LocalBroadcastManager.getInstance(UserInfoActivity.this).unregisterReceiver(mBroadcastReceiver);
//        }
        if (receiver != null)
            getBaseActivity().unregisterReceiver(receiver);
    }



}
