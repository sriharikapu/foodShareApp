package com.fengnian.smallyellowo.foodie.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.InviteFriendAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.InviteUserList;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.InviteStusResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MD5;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-11.
 */

public class InviteFriendActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private TextView tv_count, tv_sucess_invite, tv_invite_wechat;
    private ListView lv_listview;

    private InviteFriendAdapter adapter;

    private List<InviteUserList> list;

    private String cusId;
    BroadcasrReciver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        setContentView(R.layout.activity_invite_friend);
        tv_count = findView(R.id.tv_count);
        tv_sucess_invite = findView(R.id.tv_sucess_invite);
        lv_listview = findView(R.id.lv_listview);
        tv_invite_wechat = findView(R.id.tv_invite_wechat);
        tv_invite_wechat.setOnClickListener(this);
        adapter = new InviteFriendAdapter(this ,list);
        lv_listview.setAdapter(adapter);
        checkInviteSTUS();
        IntentFilter filter = new IntentFilter("share_sucessful");
        receiver = new BroadcasrReciver();
        registerReceiver(receiver, filter);
    }

    String count;
    private void checkInviteSTUS() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(false);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/invitation/getInvitationInfo.do", "", extra, new FFNetWorkCallBack<InviteStusResult>() {
        post(IUrlUtils.Search.getInvitationInfo, "", extra, new FFNetWorkCallBack<InviteStusResult>() {
            @Override
            public void onSuccess(InviteStusResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    count = response.getNumOfRemainderInvite();
                    tv_count.setText(count);
                    if ("0".equals(count)) {
                        tv_invite_wechat.setBackgroundResource(R.drawable.logout_press);
                        tv_invite_wechat.setTextColor(getResources().getColor(R.color.white_bg));
                        tv_invite_wechat.setClickable(false);
                    }
                    if (response.getInviteUsers() != null) {
                        list.addAll(response.getInviteUsers());
                        adapter.notifyDataSetChanged();
                        tv_sucess_invite.setText("您成功邀请"+list.size()+"位好友加入小黄圈");
                    }
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    /**
     * 生成标识 cusId 发给后台
     */
    private void sendCusId() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(false);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/invitation/createInvitationHtml.do", null, extra, new FFNetWorkCallBack<BaseResult>() {
        post(IUrlUtils.Search.createInvitationHtml, null, extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    String title = "【邀请码】恭喜加入小黄圈美食俱乐部,探寻路上就差你了";
                    String descriptionStr = "免费下载小黄圈,注册时输入我的邀请码,带你发现不一样的美食";
                    WeixinOpen.getInstance().share2weixin(Constants.shareConstants().getShareUrlProfix() + "/invitation_code/" + cusId + ".html", descriptionStr, title, null);

                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "cusId", cusId);
    }

    private String getMd5CusId() {
        SYUser info = SP.getUser();
        String valu = info.getId() + SP.getToken() + System.currentTimeMillis();
        try {
            return MD5.md5s(valu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    private void IsshareSucess() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(false);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/invitation/getInvitationCode.do", null, extra, new FFNetWorkCallBack<BaseResult>() {
        post(IUrlUtils.Search.getInvitationCode, null, extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    showToast("分享成功");
                    sharesucessfullUi();

                } else showToast("出现错误");
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"cusId", cusId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_invite_wechat:
                //// TODO 邀请微信好友
                cusId = getMd5CusId();
                sendCusId();
                break;
        }
    }

    public class BroadcasrReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("share_sucessful".equals(intent.getAction())) {
                IsshareSucess();

            }
        }

    }


    private  void  sharesucessfullUi(){
        if("1".equals(count)){

            tv_invite_wechat.setBackgroundResource(R.drawable.logout_press);
            tv_invite_wechat.setTextColor(getResources().getColor(R.color.white_bg));
            tv_invite_wechat.setClickable(false);
            tv_count.setText("0");
        }
        if(Integer.valueOf(count)>1)
            tv_count.setText((Integer.valueOf(count)-1)+"");
    }

    public static class viewholder {
        public TextView tv_name;
        public TextView tv_time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
    }
}
