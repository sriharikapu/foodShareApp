package com.fengnian.smallyellowo.foodie.receivers;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.DynamicMessagesActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.NoticeActivity;
import com.fengnian.smallyellowo.foodie.NoticeDetailActivity;
import com.fengnian.smallyellowo.foodie.PGCDetailActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.results.NoticeResult;
import com.fengnian.smallyellowo.foodie.fragments.IterationSocialFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.NoticeDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.personal.MyActions;

/**
 * Created by Administrator on 2016-11-8.
 */

public class PushManager {

    private static PushData data;

    public static void onPushCame(PushData data, MainActivity activity) {
        PushManager.data = data;
        if (!SP.isLogin()) {
            PushManager.data = new PushData();
        }
        activity.refreshPointsAndNums(data);
        refreshDynamic();
        refreshFriend();
        refreshMessage();
        refreshNotice();
        refreshGrowth();
    }

    public static void onPushCame(SYBaseUserInfoData data, MainActivity activity) {
        if (data != null && activity != null) {
            if (data instanceof SYInternalPushUserInfoData) {
                SYInternalPushUserInfoData internalData = (SYInternalPushUserInfoData) data;
                PushData pushData = new PushData();
                pushData.getUserinfo().setHaveNewMessage(internalData.getHaveNewMessage());
                pushData.getUserinfo().setNewAddFriendCount(internalData.getNewAddFriendCount());
                pushData.getUserinfo().setNoticeCount(internalData.getNoticeCount());
                pushData.getUserinfo().setHasNewProgress(internalData.getHasNewProgress());
                pushData.getUserinfo().setUnReadDynamicCount(internalData.getUnReadDynamicCount());
                PushManager.onPushCame(pushData, activity);
            } else if (data instanceof SYAppInSideNotificationUserInfoData) {
                SYAppInSideNotificationUserInfoData appInSideNotificationUserInfoData = (SYAppInSideNotificationUserInfoData) data;
                NoticeDetailIntent intent = new NoticeDetailIntent();
                NoticeResult.NotificationMessagesBean messagesBean = new NoticeResult.NotificationMessagesBean();
                messagesBean.setMessageHtmlUrl(appInSideNotificationUserInfoData.getMessageHtmlUrl());
                messagesBean.setNoticeid(Integer.parseInt(appInSideNotificationUserInfoData.getNoticeId()));
                messagesBean.setMessageType(Integer.parseInt(appInSideNotificationUserInfoData.getMessageType()));
                intent.setBean(messagesBean);
                // 最后一种类型 跳转到    发现的消息通知页面   DynamicMessagesActivity
                if (messagesBean.getMessageType() == 1 || messagesBean.getMessageType() == 2) {
                    activity.startActivity(NoticeDetailActivity.class, intent);

                } else {
                    //通知中心
                    activity.startActivity(NoticeActivity.class, intent);
                }

            } else if (data instanceof SYCommentUserInfoData) {
                activity.startActivity(DynamicMessagesActivity.class, new IntentData());
            } else if (data instanceof SYPGCUserInfoData) {
                SYPGCUserInfoData pgcUserInfoData = (SYPGCUserInfoData) data;
                PGCDetailIntent dataIntent = new PGCDetailIntent();
                dataIntent.setUrl(pgcUserInfoData.getTarget());
                dataIntent.setId(pgcUserInfoData.getPgcId());
                dataIntent.setAccount(SP.getUid());
                dataIntent.setToken(SP.getToken());
                dataIntent.setVersion(FFUtils.getVerName());
                dataIntent.setIsAppns("Yes");
                activity.startActivity(PGCDetailActivity.class, dataIntent);
            }
        }
    }

    public static void order_onPushCame(SYBaseUserInfoData data, BaseActivity activity) {
        if (data != null && activity != null) {
//            if(data instanceof SYInternalPushUserInfoData) {
//                SYInternalPushUserInfoData internalData = (SYInternalPushUserInfoData)data;
//                PushData pushData = new PushData();
//                pushData.getUserinfo().setHaveNewMessage(internalData.getHaveNewMessage());
//                pushData.getUserinfo().setNewAddFriendCount(internalData.getNewAddFriendCount());
//                pushData.getUserinfo().setNoticeCount(internalData.getNoticeCount());
//                pushData.getUserinfo().setUnReadDynamicCount(internalData.getUnReadDynamicCount());
//                PushManager.onPushCame(pushData,activity);
//            }else
            if (data instanceof SYAppInSideNotificationUserInfoData) {
                SYAppInSideNotificationUserInfoData appInSideNotificationUserInfoData = (SYAppInSideNotificationUserInfoData) data;
                NoticeDetailIntent intent = new NoticeDetailIntent();
                NoticeResult.NotificationMessagesBean messagesBean = new NoticeResult.NotificationMessagesBean();
                messagesBean.setMessageHtmlUrl(appInSideNotificationUserInfoData.getMessageHtmlUrl());
                messagesBean.setNoticeid(Integer.parseInt(appInSideNotificationUserInfoData.getNoticeId()));
                messagesBean.setMessageType(Integer.parseInt(appInSideNotificationUserInfoData.getMessageType()));
                intent.setBean(messagesBean);
                // 最后一种类型 跳转到    发现的消息通知页面   DynamicMessagesActivity
                if (messagesBean.getMessageType() == 1 || messagesBean.getMessageType() == 2) {
                    activity.startActivity(NoticeDetailActivity.class, intent);

                } else {
                    //通知中心
                    activity.startActivity(NoticeActivity.class, intent);
                }

            } else if (data instanceof SYCommentUserInfoData) {
                activity.startActivity(DynamicMessagesActivity.class, new IntentData());
            } else if (data instanceof SYPGCUserInfoData) {
                SYPGCUserInfoData pgcUserInfoData = (SYPGCUserInfoData) data;
                PGCDetailIntent dataIntent = new PGCDetailIntent();
                dataIntent.setUrl(pgcUserInfoData.getTarget());
                dataIntent.setId(pgcUserInfoData.getPgcId());
                dataIntent.setAccount(SP.getUid());
                dataIntent.setToken(SP.getToken());
                dataIntent.setVersion(FFUtils.getVerName());
                activity.startActivity(PGCDetailActivity.class, dataIntent);
            }
        }
    }


    public static void onNewFrientClick() {
        if (data != null) {
            data.getUserinfo().setNewAddFriendCount("0");
            refreshFriend();
        }
    }

    public static void onDynamicClick() {
        if (data != null) {
            data.getUserinfo().setUnReadDynamicCount("0");
            refreshDynamic();
        }
    }

    public static void onNoticeClick() {
        if (data != null) {
            data.getUserinfo().setNoticeCount("0");
            refreshNotice();
        }
    }

    public static void onMessageClick() {
        if (data != null) {
            data.getUserinfo().setHaveNewMessage("0");
            refreshMessage();
        }
    }

    private static void refreshFriend() {
        if (MainActivity.instance != null) {
            IterationSocialFragment fragment = MainActivity.instance.tabAdapter.mIterationSocialFragment;
            if (fragment != null) {
                fragment.setNewFrientNum(getNewFrientNum());
            }
            MainActivity.instance.refreshPointsAndNums(data);
        }
    }

    public static int getNewFrientNum() {
        if (data == null) {
            return 0;
        }
        if (data.getUserinfo() == null) {
            return 0;
        }
        return data.getUserinfo().getNewFrientNum();
    }


    private static void refreshDynamic() {
        refreshMessage();
    }


    private static void refreshNotice() {
        if (MainActivity.instance != null) {
            if (data != null && data.getUserinfo() != null) {
                Intent intent = new Intent(MyActions.HomeFragAction.ACTION_HOME_FRAG);
                intent.putExtra("type", "type_notice_num");
                intent.putExtra(MyActions.HomeFragAction.KEY_NOTICE_NUM, data.getUserinfo().getNoticeNum());
                LocalBroadcastManager.getInstance(MainActivity.instance).sendBroadcast(intent);
            }
            MainActivity.instance.refreshPointsAndNums(data);
        }
    }


    public static void refreshMessage() {
        if (MainActivity.instance != null) {
            int dynamicNum = 0, messageNum = 0;
            if (data != null && data.getUserinfo() != null) {
                dynamicNum = data.getUserinfo().getDynamicNum();
                messageNum = data.getUserinfo().getMessageNum();
            }

            Intent intent = new Intent(MyActions.HomeFragAction.ACTION_HOME_FRAG);
            intent.putExtra("type", "type_dynamic_num");
            intent.putExtra(MyActions.HomeFragAction.KEY_DYNAMIC_NUM, dynamicNum);
            intent.putExtra(MyActions.HomeFragAction.KEY_MSG_NUM, messageNum);
            LocalBroadcastManager.getInstance(MainActivity.instance).sendBroadcast(intent);

            MainActivity.instance.refreshPointsAndNums(data);
        }
    }

    public static void refreshGrowth() {
        if (MainActivity.instance != null) {
            String hasNew = getHasNewProgress();
            Intent intent = new Intent(MyActions.ACTION_PROFILE);
            intent.putExtra("type", "type_has_new_growth");
            intent.putExtra("has_new", hasNew);
            LocalBroadcastManager.getInstance(MainActivity.instance).sendBroadcast(intent);
            MainActivity.instance.refreshPointsAndNums(data);
        }
    }

    public static String getHasNewProgress() {
        if (data != null && data.getUserinfo() != null) {
            return data.getUserinfo().getHasNewProgress();
        }
        return "";
    }

    public static void logout() {
        data = new PushData();
        if (MainActivity.instance != null) {
            onPushCame(data, MainActivity.instance);
        }
    }
}
