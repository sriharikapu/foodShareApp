package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.FragmentTabAdapter;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.UPushHelper;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.results.AddCommentResult;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.intentdatas.CommentIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.receivers.PushData;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.receivers.SYAppInSideNotificationUserInfoData;
import com.fengnian.smallyellowo.foodie.receivers.SYBaseUserInfoData;
import com.fengnian.smallyellowo.foodie.receivers.SYCommentUserInfoData;
import com.fengnian.smallyellowo.foodie.receivers.SYInternalPushUserInfoData;
import com.fengnian.smallyellowo.foodie.receivers.SYPGCUserInfoData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.PublishView;
import com.fengnian.smallyellowo.foodie.widgets.TopCropImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.fengnian.smallyellowo.foodie.R.id.rl_youke_dialog;

public class MainActivity extends BaseActivity<IntentData> {
    @Bind(R.id.iv_youke_social)
    TopCropImageView ivYoukeSocial;
    @Bind(R.id.iv_youke_user)
    TopCropImageView ivYoukeUser;
    @Bind(R.id.iv_youke_publish_upper)
    ImageView ivYoukePublishUpper;
    @Bind(R.id.iv_youke_publish_lower)
    ImageView ivYoukePublishLower;
    @Bind(R.id.ll_youke_publish)
    LinearLayout llYoukePublish;
    @Bind(R.id.tv_need_login)
    TextView tvNeedLogin;
    @Bind(R.id.btn_login)
    View btnLogin;
    @Bind(rl_youke_dialog)
    RelativeLayout rlYoukeDialog;
    private FrameLayout fl_main_container;
    private RelativeLayout rl_comment;
    private RadioGroup rg_main_bottom;
    private RadioButton rb_home;
    private RadioButton rb_dynamic;
    private TextView textView;
    private RadioButton rb_sns;
    public RadioButton rb_user;
    private TextView bottom_home_sum;
    private ImageView bottom_home_tips;
    private TextView bottom_discover_sum;
    private ImageView bottom_discover_tips;
    private TextView bottom_sns_sum;
    private ImageView bottom_sns_tips;
    private TextView bottom_user_sum;
    private ImageView bottom_user_tips;
    private PublishView mPublishView;
    public FragmentTabAdapter tabAdapter;
    private SYFeed feed;
    private SYComment comment;
    private FrameLayout fl_cur;
    private MessageReceiver mMessageReceiver;
    public static MainActivity instance = null;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.getuidemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static int fragment_is_change = 0;
    private long lastTimeMillis = 0;
    private static int TIME_LONG = 3 * 1000;//用户重复按返回键，检测是否真正要退出应用的时间间隔
    private long mLastTime = 0;//检测用户重复按返回键的辅助变量

    public static int checkItem = -1;
    private View gide;

    public static void toDynamic() {
        checkItem = 1;
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public static void toUser() {
        checkItem = 4;
    }

    public static void onNetStatusChanged(boolean isConnect) {
        if (instance != null) {
            Intent intent = new Intent(MyActions.ACTION_NO_NET);
            intent.putExtra("type", "type_net");
            intent.putExtra("isConnect", isConnect);
            LocalBroadcastManager.getInstance(instance).sendBroadcast(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SP.isLogin()) {
            initYouke();
        }
        instance = this;
        if (checkItem != -1) {
            if (checkItem == 1) {
                instance.rb_home.setChecked(true);
                FFUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyActions.HomeFragAction.ACTION_HOME_FRAG);
                        intent.putExtra("type", "type_to_dynamic_top");
                        LocalBroadcastManager.getInstance(instance).sendBroadcast(intent);
                    }
                }, 300);
            } else if (checkItem == 4) {
                rb_user.setChecked(true);
            }
        }
        checkItem = -1;

        if (System.currentTimeMillis() - lastTimeMillis > 60 * 30 * 1000) {
            getConfigs();
            lastTimeMillis = System.currentTimeMillis();
        }
    }

    public void refreshPointsAndNums(PushData data) {
        if (data == null) {
            int num = SYDataManager.shareDataManager().getAllFailDynamic().size();
            if (num > 0) {
                bottom_home_tips.setVisibility(View.VISIBLE);
            } else {
                bottom_home_tips.setVisibility(View.GONE);
            }
            return;
        }
        //TODO zhangfan  刷新红点和数字
        int dynamicNum = data.getUserinfo().getDynamicNum();
//        dynamicNum = dynamicNum + SYDataManager.shareDataManager().getAllFailDynamic().size();
        if (dynamicNum > 0) {
            bottom_home_sum.setVisibility(View.VISIBLE);
            bottom_home_tips.setVisibility(View.GONE);
            bottom_home_sum.setText(dynamicNum + "");
        } else if (data.getUserinfo().getMessageNum() > 0) {
            bottom_home_sum.setVisibility(View.GONE);
            bottom_home_tips.setVisibility(View.VISIBLE);
        } else {
            bottom_home_sum.setVisibility(View.GONE);
            bottom_home_tips.setVisibility(View.GONE);
        }

        if (data.getUserinfo().getNoticeNum() > 0) {
            bottom_discover_sum.setVisibility(View.GONE);
            bottom_discover_tips.setVisibility(View.GONE);

            bottom_discover_sum.setText(data.getUserinfo().getNoticeCount());
        } else {
            bottom_discover_sum.setVisibility(View.GONE);
            bottom_discover_tips.setVisibility(View.GONE);
        }

        if (data.getUserinfo().getNewFrientNum() > 0) {
            bottom_sns_sum.setVisibility(View.GONE);
            bottom_sns_tips.setVisibility(View.VISIBLE);
            bottom_sns_sum.setText(data.getUserinfo().getNewAddFriendCount());
        } else {
            bottom_sns_sum.setVisibility(View.GONE);
            bottom_sns_tips.setVisibility(View.GONE);
        }

        if (data.getUserinfo().getNewProgress() > 0) {
            bottom_user_sum.setVisibility(View.GONE);
            bottom_user_tips.setVisibility(View.VISIBLE);
            //bottom_user_sum.setText(data.getUserinfo().getHasNewProgress());
        } else {
            bottom_user_sum.setVisibility(View.GONE);
            bottom_user_tips.setVisibility(View.GONE);
        }
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_EXTRAS);
                if (messge == null) {
                    return;
                }
                FFLogUtil.d("tag", messge);
                if (messge != null) {
                    JSONObject jsonObject = JSONObject.parseObject(messge);
                    String userInfoObj = jsonObject.getString("userinfo");
                    if (FFUtils.isStringEmpty(userInfoObj)) {
                        userInfoObj = "{}";
                    }
                    SYBaseUserInfoData userInfoData = null;
                    String typeString = jsonObject.getString("pushType");
                    if (typeString == null) {
                        return;
                    }
                    int pushType = Integer.parseInt(typeString);
                    switch (pushType) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            userInfoData = JSON.parseObject(userInfoObj, SYInternalPushUserInfoData.class);
                            break;
                        case 5:
                            userInfoData = JSON.parseObject(userInfoObj, SYAppInSideNotificationUserInfoData.class);
                            break;
                        case 6:
                            userInfoData = JSON.parseObject(userInfoObj, SYCommentUserInfoData.class);
//                            userInfoData = new SYCommentUserInfoData();
                            break;
                        case 7:
                            userInfoData = JSON.parseObject(userInfoObj, SYPGCUserInfoData.class);
                            break;
                        case 8:
                            break;
                        case 99:
                            break;
                        case 100:
                            break;
                    }
                    if (userInfoData != null) {
                        PushManager.onPushCame(userInfoData, (MainActivity) context());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        if (isFinishing()) {
            instance = null;
            checkItem = -1;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.clear();
        }
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        getChildTags().clear();
        getTags().clear();
        noticeService();
        registerMessageReceiver();
        while (allActivis.size() > 1 && allActivis.contains(this)) {
            Activity activity = allActivis.get(0);
            if (activity == this) {
                if (allActivis.size() >= 2) {
                    activity = allActivis.get(1);
                } else {
                    activity = null;
                }
            }
            if (activity != null) {
                allActivis.remove(activity);
                activity.finish();
            }
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rlYoukeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        gide = findViewById(R.id.publish_gide);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginIntentData loginIntentData = new LoginIntentData();
                loginIntentData.setCode("");
                startActivity(LoginOneActivity.class, loginIntentData);
            }
        });
        setNotitle(true);
        View v_statusBar = findViewById(R.id.v_statusBar);
        initBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            v_statusBar.getLayoutParams().height = FFUtils.getStatusbarHight(this);
        } else {
            v_statusBar.getLayoutParams().height = 0;
        }
        com.igexin.sdk.PushManager.getInstance().initialize(this, null);
        if (SP.isLogin()) {
            com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
            com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
            UPushHelper.setPushAlias(getApplicationContext(), SP.getUid());
        } else {
            com.igexin.sdk.PushManager.getInstance().turnOffPush(getApplicationContext());
        }
//        if(instance != null && instance != this){
//            instance.finish();
//            instance = null;
//        }
        instance = this;
        fl_main_container = (FrameLayout) findViewById(R.id.fl_main_container);
        rg_main_bottom = (RadioGroup) findViewById(R.id.rg_main_bottom);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_dynamic = (RadioButton) findViewById(R.id.rb_dynamic);
        fl_cur = (FrameLayout) findViewById(R.id.fl_cur);
        textView = (TextView) findViewById(R.id.textView);
        rb_sns = (RadioButton) findViewById(R.id.rb_sns);
        rb_user = (RadioButton) findViewById(R.id.rb_user);
        bottom_home_sum = (TextView) findViewById(R.id.bottom_home_sum);
        bottom_home_tips = (ImageView) findViewById(R.id.bottom_home_tips);
        bottom_discover_sum = (TextView) findViewById(R.id.bottom_discover_sum);
        bottom_discover_tips = (ImageView) findViewById(R.id.bottom_discover_tips);
        bottom_sns_sum = (TextView) findViewById(R.id.bottom_sns_sum);
        bottom_sns_tips = (ImageView) findViewById(R.id.bottom_sns_tips);
        bottom_user_sum = (TextView) findViewById(R.id.bottom_user_sum);
        bottom_user_tips = (ImageView) findViewById(R.id.bottom_user_tips);

        tabAdapter = new FragmentTabAdapter(this, R.id.fl_main_container, new RadioButton[]{rb_home, rb_dynamic, rb_sns, rb_user}) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    return;
                }
                if (!SP.isLogin()) {//未登录
                    if (buttonView == rb_sns) {
                        checkSns();
                        checkChange(buttonView);
                        return;
                    } else if (buttonView == rb_user) {
                        checkUser();
                        checkChange(buttonView);
                        return;
                    } else {
                        setSelectedFragDislikeIconHide();
                        initYouke();
                    }
                }
                super.onCheckedChanged(buttonView, isChecked);
            }
        };
        tabAdapter.setOnTabCheckedListener(new FragmentTabAdapter.OnTabCheckedListener() {
            @Override
            public void OnChecked(CompoundButton radioGroup, boolean checkedId, int index) {
//                onCheckedChanged(index);
                fragment_is_change = index;
                Intent intent = new Intent(MyActions.ACTION_TAB_SELECTED);
                intent.putExtra("tab_index", index);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }
        });

        rg_main_bottom.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SP.getUid().length() == 0) {//未登录
                    rb_dynamic.setChecked(false);
                    rb_home.setChecked(false);
                    rb_sns.setChecked(false);
                    rb_user.setChecked(false);
                    checkPublish();
                    return;
                }

                //不要改变代码顺序
                if (mPublishView != null) {
                    fl_cur.removeView(mPublishView);
                    mPublishView = null;
                }
                if (mPublishView == null) {
                    mPublishView = new PublishView(MainActivity.this);
                    fl_cur.addView(mPublishView, fl_cur.getChildCount(), new ViewGroup.LayoutParams(-1, -1));
                    mPublishView.show();
                }

            }
        });
        if (!SP.getMainTip()) {
            SP.setMainTip();
            gide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            gide.setVisibility(View.VISIBLE);
            ((ImageView) ((ViewGroup) gide).getChildAt(0)).setImageResource(R.mipmap.main_gide);
        } else {
            fl_cur.removeView(gide);
            gide = null;
        }
    }

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();
        initYouke();
        final RadioButton[] cbs = {rb_home, rb_dynamic, rb_sns, rb_user};
        for (int i = 0; i < 4; i++) {
            if (cbs[i].isChecked()) {
//                tabAdapter.showTab(i);
                cbs[i].setChecked(false);
                final int j = i;
                FFUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cbs[j].setChecked(true);
                    }
                }, 1);
                return;
            }
        }

        textView.performClick();
//        tabAdapter.showTab(0);
        rb_home.setChecked(true);
    }


    private void initYouke() {
        ivYoukePublishLower.setImageResource(0);
        ivYoukePublishUpper.setImageResource(0);
        ivYoukeSocial.setImageResource(0);
        ivYoukeUser.setImageResource(0);
        rlYoukeDialog.setVisibility(View.GONE);
        llYoukePublish.setVisibility(View.GONE);
        ivYoukeSocial.setVisibility(View.INVISIBLE);
        ivYoukeUser.setVisibility(View.INVISIBLE);
    }

    private void checkPublish() {
        initYouke();

        rlYoukeDialog.setVisibility(View.VISIBLE);
        tvNeedLogin.setText("现在还不能  “发布”  美食\r\n需登录注册后才能使用哦");

        llYoukePublish.setVisibility(View.VISIBLE);
        ivYoukePublishLower.setImageResource(R.mipmap.food_edit_img);
        ivYoukePublishUpper.setImageResource(R.mipmap.food_shorthand);
    }

    private void checkSns() {
        initYouke();

        rlYoukeDialog.setVisibility(View.VISIBLE);
        tvNeedLogin.setText("更多  “社交”   功能\r\n需登录注册后才能使用哦");

        ivYoukeSocial.setImageResource(R.mipmap.youke_bg_social);
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !getDestroyed()){
                    ivYoukeSocial.setImageResource(R.mipmap.youke_bg_social);
                }else {
                    FFUtils.getHandler().removeCallbacks(this);
                }
            }
        }, 10);
        ivYoukeSocial.setVisibility(View.VISIBLE);
    }

    private void setSelectedFragDislikeIconHide(){
        if (tabAdapter.mMainMyHomeFragment != null
                && tabAdapter.mMainMyHomeFragment.mHomeChildSelectedFrag != null){
            tabAdapter.mMainMyHomeFragment.mHomeChildSelectedFrag.setDislikeIconHide();
        }
    }

    private void checkUser() {
        initYouke();

        rlYoukeDialog.setVisibility(View.VISIBLE);
        tvNeedLogin.setText("更多  “个人”   功能\r\n需登录注册后才能使用哦");

        ivYoukeUser.setImageResource(R.mipmap.youke_bg_user);
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !getDestroyed()){
                    ivYoukeUser.setImageResource(R.mipmap.youke_bg_user);
                }else {
                    FFUtils.getHandler().removeCallbacks(this);
                }
            }
        }, 10);
        ivYoukeUser.setVisibility(View.VISIBLE);

    }


    private void noticeService() {
//        APP.post(Constants.shareConstants().getNetHeaderAdress() + "/config/rptDeviceToken.do", null, new FFNetWorkCallBack<BaseResult>() {
        APP.post(IUrlUtils.Search.rptDeviceToken, null, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    Runnable runn;

    public void comment(SYFeed feed, SYComment comment, Runnable runn) {
        if (FFUtils.isStringEmpty(SP.getUid())) {
            LoginIntentData loginIntentData = new LoginIntentData();
            loginIntentData.setCode("");
            startActivity(LoginOneActivity.class, loginIntentData);
            return;
        }
        this.runn = runn;
        this.feed = feed;
        this.comment = comment;
        CommentIntent intent = (CommentIntent) new CommentIntent();
        intent.setFeed(feed);
        intent.setComment(comment);
        startActivity(CommentActivity.class, (CommentIntent) intent.setRequestCode(517));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 517 && resultCode == RESULT_OK) {
            if (runn != null) {
                final SYFeed fe = feed;
                final Runnable run = runn;
                final SYComment com = comment;
//                post(Constants.shareConstants().getNetHeaderAdress() + "/comment/addCommentV250.do", null, null, new FFNetWorkCallBack<AddCommentResult>() {
                post(IUrlUtils.Search.addCommentV250, null, null, new FFNetWorkCallBack<AddCommentResult>() {
                    @Override
                    public void onSuccess(AddCommentResult response, FFExtraParams extra) {
                        feed.setSecondLevelcomments(response.getSySecondLevelComments());
                        run.run();
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ugcId", feed.getId(), "content", data.getStringExtra("comment"), "toUserId", comment == null ? feed.getUser().getId() : comment.getCommentUser().getId(), "toCommentId", comment == null ? 0 : comment.getId());
                comment = null;
            }
        }
    }

    public void getConfigs() {
        long interfaceVersion = 0;
        if (SP.getConfig() != null) {
            if (SP.getConfig().getConfig() != null)
                interfaceVersion = SP.getConfig().getConfig().getInterfaceVersion();
        }

        if (interfaceVersion < 0)
            interfaceVersion = 0;
//        post(Constants.shareConstants().getNetHeaderAdress() + "/config/getConfigV220.do", null,
        post(IUrlUtils.Search.getConfigV220, null,
                new FFExtraParams().setKeepWhenActivityFinished(true).setQuiet(true),
                new FFNetWorkCallBack<ConfigResult>() {
                    @Override
                    public void onSuccess(ConfigResult response, FFExtraParams extra) {
                        if (response.getConfig() != null) {
                            SP.setConfig(response);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "interfaceVersion", String.valueOf(interfaceVersion));
    }

    @Override
    public void onBackPressed() {
        if (gide != null && gide.getVisibility() == View.VISIBLE) {
            gide.setVisibility(View.GONE);
            fl_cur.removeView(gide);
            gide = null;
            return;
        }
        if (mPublishView != null && mPublishView.isShow) {
            mPublishView.close();
            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fl_cur.removeView(mPublishView);
                    mPublishView = null;
                }
            }, PublishView.duration);
        } else {
            //添加用户按两次返回键才能退出的逻辑 by chenglin 2017-2-16
            long exitTime = System.currentTimeMillis();
            if (exitTime - mLastTime < TIME_LONG) {
                super.onBackPressed();
                System.exit(0);//退出虚拟机
            } else {
                showToast(getString(R.string.quit_alert));
                mLastTime = exitTime;
                return;
            }
        }
    }
}
