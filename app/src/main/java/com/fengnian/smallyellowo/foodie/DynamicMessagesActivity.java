package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.MessageAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.NoticeResult;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

public class DynamicMessagesActivity extends BaseActivity<IntentData> {

    private ListView lv_messages;

    private List<NoticeResult.MessageListBean> list;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        setContentView(R.layout.activity_dynamic_messages);
        lv_messages = findView(R.id.lv_messages);
        adapter = new MessageAdapter(context(), list);
        lv_messages.setAdapter(adapter);
        getmessagelist();

        initclick();

    }

    private void initclick() {
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if (list.size() > 0 && list.size() == pos) {
                    getmessagelist(list.get(pos - 1).getMessageId());
                } else if (list.size() > 0) {
                    //todo  详情页面进入
                    DynamicDetailIntent detail = new DynamicDetailIntent();
                    detail.setId(list.get(pos).getFoodRecordId());
                    startActivity(DynamicDetailActivity.class, detail);
                }

            }
        });
    }


    private void getmessagelist() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/message/listInteractMessageV250.do", "", extra, new FFNetWorkCallBack<NoticeResult>() {
        post(IUrlUtils.Search.listInteractMessageV250, "", extra, new FFNetWorkCallBack<NoticeResult>() {
            @Override
            public void onSuccess(NoticeResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    list.addAll(response.getMessageList());
                    if (response.getMessageList().size() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                    PushManager.onDynamicClick();
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }

        });
    }


    private void getmessagelist(String messageId) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/message/listInteractMessageV250.do", "", extra, new FFNetWorkCallBack<NoticeResult>() {
        post(IUrlUtils.Search.listInteractMessageV250, "", extra, new FFNetWorkCallBack<NoticeResult>() {
            @Override
            public void onSuccess(NoticeResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    list.addAll(response.getMessageList());
                    if (response.getMessageList().size() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                    PushManager.onDynamicClick();
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }

        }, "messageId", messageId);
    }

}
