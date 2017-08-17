package com.fengnian.smallyellowo.foodie.usercenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.OtherAccount;
import com.fengnian.smallyellowo.foodie.bean.publics.OtherAccount.Account;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BangDingPhoneResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.usercenter.adapter.BindAccountAdapter;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.QQSdkUtil;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/6.
 * 第三方账号绑定页面
 */

public class BindAccountActivity extends BaseActivity<IntentData> {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

/*    @Bind(R.id.right_wechat_image)
    ImageView rightWechatImage;
    @Bind(R.id.account_wechat_name)
    CustomEmojiTextView accountWechatName;

    @Bind(R.id.right_qq_image)
    ImageView rightQqImage;
    @Bind(R.id.account_qq_name)
    CustomEmojiTextView accountQQName;

    @Bind(R.id.right_sina_image)
    ImageView rightSinaImage;
    @Bind(R.id.account_sina_name)
    CustomEmojiTextView accountSinaName;*/

    //    private SYUser currentUser;
    private QQSdkUtil QQSdk;
    private SsoHandler ssoHandler;
    private String bindCode = "";

    private ArrayList<Account> accounts;
    private BindAccountAdapter mAdapter;
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bind_account);
        ButterKnife.bind(this);

        registerLoginReceiver();
//        currentUser = SP.getUser();
//        updateName(currentUser, "");

        /*if (FFUtils.isListEmpty(accounts)) {
            accounts = new ArrayList<>();
            Account weixin = new Account();
            weixin.setType(1);
            weixin.setAccountName("蚂蚁");
            weixin.setBinded(true);
            accounts.add(weixin);

            Account qq = new Account();
            qq.setType(2);
            qq.setBinded(true);
            qq.setAccountName("像蚂蚁一样");
            accounts.add(qq);

            Account weibo = new Account();
            weibo.setType(3);
            weibo.setBinded(false);
            accounts.add(weibo);
        }*/

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        accounts = new ArrayList<>();
        mAdapter = new BindAccountAdapter(this, accounts);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setmItemClickListener(new BindAccountAdapter.ItemClick() {
            @Override
            public void onItemClickListener(int position, Account account) {

                pushEvent("THIRD_BIND_CLICK", account.getType() + "");
                type = account.getType();
                bindCode = account.getType()+"";
                switch (account.getType()) {

                    case 1:
                        if (account.isBinded()) {
                            /*pushEvent("THIRD_ONE_SHOW", "1");
                            showToast("该”微信号”已被绑定");*/
                            return;
                        }

                        WeixinOpen.getInstance().AuthLgin();
                        break;

                    case 2:
                        if (account.isBinded()) {
                            /*pushEvent("THIRD_ONE_SHOW", "2");
                            showToast("该”QQ号”已被绑定");*/
                            return;
                        }

                        QQSdk = new QQSdkUtil(BindAccountActivity.this);
                        QQSdk.bindAccount();
                        break;

                    case 3:
                        if (account.isBinded()) {
                            /*pushEvent("THIRD_ONE_SHOW", "3");
                            showToast("该”微博号”已被绑定");*/
                            return;
                        }
                        AuthInfo info = new AuthInfo(BindAccountActivity.this, SinaOpen.APP_KEY, SinaOpen.REDIRECT_URL, SinaOpen.SCOPE);
                        ssoHandler = new SsoHandler(BindAccountActivity.this, info);
                        SinaOpen.loginBySina(BindAccountActivity.this, ssoHandler);
                        break;
                }

            }
        });

        getBindInfo();

//        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*private void init() {

        accountQQName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bindCode = "2";
                pushEvent("THIRD_BIND_CLICK", bindCode);

                *//*if (currentUser.isBindQQ()) {
                    showToast("该”QQ号”已被绑定");
                    return;
                }*//*
                QQSdk = new QQSdkUtil(BindAccountActivity.this);
                QQSdk.bindAccount();
            }
        });

        accountSinaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bindCode = "3";
                pushEvent("THIRD_BIND_CLICK", bindCode);

                *//*if (currentUser.isBindSina()) {
                    showToast("该”微博号”已被绑定");
                    return;
                }*//*

                AuthInfo info = new AuthInfo(BindAccountActivity.this, SinaOpen.APP_KEY, SinaOpen.REDIRECT_URL, SinaOpen.SCOPE);
                ssoHandler = new SsoHandler(BindAccountActivity.this, info);
                SinaOpen.loginBySina(BindAccountActivity.this, ssoHandler);
            }
        });

        accountWechatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindCode = "1";
                pushEvent("THIRD_BIND_CLICK", bindCode);

                if (currentUser.isBindWechat()) {
                    showToast("该”微信号”已被绑定");
                    return;
                }
                WeixinOpen.getInstance().AuthLgin();
            }
        });
    }*/

    public class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra(IUrlUtils.Constans.CALL_TOKEN);

            if (IUrlUtils.Constans.LOAGIN_CODE_WECHAT.equals(intent.getAction())) { // 微信绑定回调
                bindAccount(IUrlUtils.UserCenter.bindingWeChat);
//                bangDingWeiXin(token);

            } else if (IUrlUtils.Constans.LOAGIN_CODE_QQ.equals(intent.getAction())) { // qq绑定回调
                String openId = intent.getStringExtra(IUrlUtils.Constans.CALL_OPEN_ID);
                bindAccount(IUrlUtils.UserCenter.bindingQq, "accessToken", token, "openId", openId);

            } else if (IUrlUtils.Constans.LOAGIN_CODE_SINA.equals(intent.getAction())) { // 新浪微博绑定回调
                String refreshToken = intent.getStringExtra(IUrlUtils.Constans.CALL_REFRESH_TOKEN);
                bindAccount(IUrlUtils.UserCenter.bindingWeibo, "accessToken", token, "refreshToken", refreshToken);
            }
        }
    }

    private void getBindInfo() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);

        post(IUrlUtils.UserCenter.bindInfo, "", extra, new FFNetWorkCallBack<OtherAccount>() {
            @Override
            public void onSuccess(OtherAccount response, FFExtraParams extra) {
                if (!response.judge()) {
                    showToast(response.getServerMsg());
                    return;
                }

                if (response == null) {
                    return;
                }
                accounts.clear();
                accounts.addAll(response.getAccountDataList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    /**
     * 绑定微信
     */
    /*private void bangDingWeiXin(String wechatCode) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.UserCenter.bindingWeChat, null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
            @Override
            public void onSuccess(BangDingPhoneResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser();
                    SP.setUser(us);
                    showToast("已成功绑定");
                    updateName(response.getThirdNickname());
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }*/
    private void updateName(String name) {
        for (Account account : accounts) {
            if (account.getType() == type) {
                account.setAccountName(name);
                account.setBinded(true);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 绑定微信
     */
    private void bindAccount(String url, Object... params) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(url, null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
            @Override
            public void onSuccess(BangDingPhoneResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser();
                    SP.setUser(us);
                    showToast("已成功绑定");
                    String nickName = response.getThirdNickname();
                    if (TextUtils.isEmpty(nickName)) {
                        nickName = "马上绑定";
                    }
                    updateName(nickName);

                    pushEvent("THIRD_BIND_SUCCESS", bindCode);

                } else if (response.getErrorCode() == 903) {
                    pushEvent("THIRD_ONE_SHOW", "2");
                    showToast("该”QQ号”已被绑定");

                } else if (response.getErrorCode() == 1003) {
                    pushEvent("THIRD_ONE_SHOW", "3");
                    showToast("该”微博号”已被绑定");

                } else if (response.getErrorCode() == 17) {
                    pushEvent("THIRD_ONE_SHOW", "1");
                    showToast("该”微信号”已被绑定");

                } else {
                    pushEvent("THIRD_BIND_FAIR", bindCode);
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public void onFailPublic(BangDingPhoneResult response, FFExtraParams extraParams) {
                super.onFailPublic(response, extraParams);
                pushEvent("THIRD_BIND_FAIR", bindCode);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }

        }, params);
    }

    // UMeng事件统计
    public void pushEvent(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        pushEventAction(key, map);
    }

    /*private void updateName(SYUser us, String nickName) {
        if (us == null) {
            us = SP.getUser();
        }
        if (TextUtils.isEmpty(nickName)) {
            nickName = us.getNickName();
        }

        if (TextUtils.isEmpty(nickName)) {
            nickName = "马上绑定";
        }

        if (us.isBindWechat()) {
            accountWechatName.setText(nickName);
            rightWechatImage.setVisibility(View.GONE);

            updatePosition(accountWechatName);

//            accountWechatName.setClickable(false);
        } else if (us.isBindQQ()) {
            accountQQName.setText(nickName);
            rightQqImage.setVisibility(View.GONE);

            updatePosition(accountQQName);

//            accountQQName.setClickable(false);
        } else if (us.isBindSina()) {
            accountSinaName.setText(nickName);
            rightSinaImage.setVisibility(View.GONE);

            updatePosition(accountSinaName);

//            accountSinaName.setClickable(false);
        }
    }*/

    /*private void updatePosition(View view) {

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.title_text_color));
        }

        if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) view.getLayoutParams()).setMargins(0, 0, 30, 0);
            ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            view.setLayoutParams(view.getLayoutParams());
        }
    }*/

    private LoginReceiver loginReceiver;

    public void registerLoginReceiver() {
        loginReceiver = new LoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_WECHAT);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_QQ);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_SINA);
        filter.addAction(MyActions.ACTION_UPDATE_ADDRESS);
        registerReceiver(loginReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN && QQSdk != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, QQSdk.getIUiListener());
        }

        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginReceiver != null) {
            unregisterReceiver(loginReceiver);
        }

        if (QQSdk != null) {
            QQSdk.onDestory();
        }
    }

}
