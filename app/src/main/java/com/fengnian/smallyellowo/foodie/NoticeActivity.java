package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.NoticeResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.NoticeDetailIntent;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoticeActivity extends BaseActivity<IntentData> {

    @Bind(R.id.listView2)
    ListView listView2;

    public static class Holder {
        ImageView iv_img;
        TextView tv_title;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_over;
        View v;
        TextView tv_watch_detail;
        TextView tv_vis_all_content;
    }

    MyBaseAdapter<Holder, NoticeResult.NotificationMessagesBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        setTitle("通知中心");
//        post(Constants.shareConstants().getNetHeaderAdress() + "/notice/queryNoticeList.do", "", null, new FFNetWorkCallBack<NoticeResult>() {
        post(IUrlUtils.Search.queryNoticeList, "", null, new FFNetWorkCallBack<NoticeResult>() {
            @Override
            public void onSuccess(NoticeResult response, FFExtraParams extra) {
                adapter.setData(response.getNotificationMessages());
                adapter.notifyDataSetChanged();
                PushManager.onNoticeClick();
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "lastNoticeId", 0, "pageSize", 15);

        adapter = new MyBaseAdapter<Holder, NoticeResult.NotificationMessagesBean>(Holder.class, context(), R.layout.item_notice) {

            @Override
            public void initView(View convertView, Holder holder, int position, final NoticeResult.NotificationMessagesBean item) {
                if (holder.iv_img != null)
                    switch (item.getMessageType()) {
                        case 1://活动通知
                            if (item.getMessageStatus() != 1) {
                                if (item.getMessageStatus() == 2) {
                                    holder.iv_over.setImageResource(R.mipmap.notice_canceled);
                                } else {//3
                                    holder.iv_over.setImageResource(R.mipmap.ic_time_out);
                                }
                            }
                            holder.iv_img.setImageResource(R.mipmap.notice_action);
                            break;
                        case 2://xhp通知
                            if (item.getMessageStatus() != 1) {
                                if (item.getMessageStatus() == 2) {
                                    holder.iv_over.setImageResource(R.mipmap.notice_canceled);
                                } else {//3
                                    holder.iv_over.setImageResource(R.mipmap.ic_time_out);
                                }
                                holder.iv_img.setImageResource(R.mipmap.notice_xhq_canceled);
                            } else {
                                holder.iv_img.setImageResource(R.mipmap.notice_xhq);
                            }
                            break;
                        default://系统通知
                            holder.iv_img.setImageResource(R.mipmap.notice_system);
                            break;
                    }
                holder.tv_title.setText(item.getMessageTitle());
                holder.tv_content.setText(item.getMessageSubContent());
                holder.tv_time.setText(item.getMessageTime());
                if (holder.tv_vis_all_content != null) {
                    int textLine = FFUtils.getTextLine(holder.tv_content, item.getMessageSubContent(), FFUtils.getDisWidth() - FFUtils.getPx(58 + 15));
                    if (textLine > 2) {
                        holder.tv_vis_all_content.setVisibility(View.VISIBLE);
                        holder.tv_vis_all_content.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                item.isExpand = !item.isExpand;
                                notifyDataSetChanged();
                            }
                        });
                        if (item.isExpand) {
                            holder.tv_vis_all_content.setVisibility(View.GONE);
                            holder.tv_content.setMaxLines(Integer.MAX_VALUE);
                        } else {
                            holder.tv_content.setMaxLines(2);
                        }
                    } else {
                        holder.tv_vis_all_content.setVisibility(View.GONE);
                    }
                }
                if (holder.tv_watch_detail != null && !FFUtils.isStringEmpty(item.getMessageHtmlUrl()) && item.getMessageStatus() == 1) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NoticeDetailIntent intent = new NoticeDetailIntent();
                            intent.setBean(item);
                            startActivity(NoticeDetailActivity.class, intent);
                        }
                    });
                    holder.tv_watch_detail.setVisibility(View.VISIBLE);
                    holder.v.setVisibility(View.VISIBLE);
                } else {
                    if (holder.tv_watch_detail != null && FFUtils.isStringEmpty(item.getMessageHtmlUrl())) {
                        holder.tv_watch_detail.setVisibility(View.GONE);
                        holder.v.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public int getItemViewType1(int position) {
                NoticeResult.NotificationMessagesBean item = getItem(position);
                switch (item.getMessageType()) {
                    case 1://活动通知
                    case 2://xhp通知
                        if (item.getMessageStatus() != 1) {
                            return 0;
                        } else {
                            return 1;
                        }
                    default://系统通知
                        return 2;
                }
            }

            @Override
            public int getItemViewId(int position) {
                NoticeResult.NotificationMessagesBean item = getItem(position);
                switch (item.getMessageType()) {
                    case 1://活动通知
                    case 2://xhp通知
                        if (item.getMessageStatus() != 1) {
                            return R.layout.item_notice_canceled;
                        } else {
                            return R.layout.item_notice;
                        }
                    default:
                        return R.layout.item_notice_sys;
                }
            }

            @Override
            public int getViewTypeCount1() {
                return 3;
            }
        };
        listView2.setAdapter(adapter);
    }

}
