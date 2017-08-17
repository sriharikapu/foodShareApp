package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fan.framework.xtaskmanager.XTaskManager;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RotationLoadingView;
import com.fengnian.smallyellowo.foodie.bean.PersionSYfeedC;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;

import java.util.List;


/**
 * Created by Administrator on 2016-10-24.
 */

public class PersionalCenterAdapter extends BaseAdapter {

    private List<SYFeed> mlist;
    private FFContext mcontext;

    private Handler mhandler;

    public PersionalCenterAdapter(List<SYFeed> mlist, FFContext mcontext, Handler handler) {
        this.mlist = mlist;
        this.mcontext = mcontext;
        this.mhandler = handler;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        UserFeedHolder holder = null;
        if (holder == null) {
            holder = new UserFeedHolder();
            view = View.inflate((Context) mcontext, R.layout.item_main_user_feeds, null);
            holder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.iv_rest_name = (ImageView) view.findViewById(R.id.iv_rest_name);
            holder.tv_rest_name = (TextView) view.findViewById(R.id.tv_rest_name);
            holder.tv_publish_status = (TextView) view.findViewById(R.id.tv_publish_status);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_img_num = (TextView) view.findViewById(R.id.tv_img_num);
            holder.tv_share_status = (TextView) view.findViewById(R.id.tv_share_status);
            holder.rotation_loadingview = (RotationLoadingView) view.findViewById(R.id.rotation_loadingview);
        } else {
            holder = (UserFeedHolder) view.getTag();
        }


        SYFeed item = mlist.get(position);
        final SYFeed fed = item;

        if (!fed.getFood().wasMeishiBianji()) {
            holder.tv_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_fast, 0);
        } else {
            holder.tv_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        holder.tv_publish_status.setVisibility(View.GONE);
        holder.rotation_loadingview.setVisibility(View.GONE);
        holder.rotation_loadingview.stopRotationAnimation();
        if (fed.getFood() instanceof NativeRichTextFood) {
            if (((NativeRichTextFood) fed.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateFail) {
                holder.tv_publish_status.setVisibility(View.VISIBLE);

                holder.tv_publish_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XTaskManager.taskManagerWithTask(((NativeRichTextFood) fed.getFood()).getTask());
                    }
                });
            } else if (((NativeRichTextFood) fed.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateExecing) {
                holder.rotation_loadingview.setVisibility(View.VISIBLE);
                holder.rotation_loadingview.startRotationAnimation();
            }
        }

        holder.tv_title.setText(item.getFood().getFrontCoverModel().getFrontCoverContent().getContent());
        String rest_name = item.getFood() == null || item.getFood().getPoi() == null || FFUtils.isStringEmpty(item.getFood().getPoi().getTitle()) ? "无商户" : item.getFood().getPoi().getTitle();
        holder.tv_rest_name.setText(rest_name);
        if ("无商户".equals(rest_name)) {
            holder.iv_rest_name.setImageResource(R.mipmap.main_user_rest);
        } else {
            holder.iv_rest_name.setImageResource(R.mipmap.have_merchant_img);
        }
        holder.tv_time.setText(TimeUtils.getTime("MM-dd", item.getTimeStamp()));
        if (item.getFood() != null) {
            FFImageLoader.loadSmallImage(mcontext, item.getFood().pullCoverImage(), holder.iv_img);
            holder.tv_img_num.setText(item.getFood().allImageContent().size() + "");
            boolean bo1 = item instanceof PersionSYfeedC && !(((PersionSYfeedC) item).isSharedToAct());
            boolean bo2 = item.getFood() instanceof NativeRichTextFood && !((NativeRichTextFood) item.getFood()).getTask().isBshareToSmallYellowO();
            if (bo1 || bo2) {
                holder.tv_share_status.setVisibility(View.VISIBLE);
            } else {
                holder.tv_share_status.setVisibility(View.GONE);
            }
        }
        return view;
    }


    private class UserFeedHolder {
        private ImageView iv_img, iv_rest_name;
        private TextView tv_img_num;
        private TextView tv_title;
        private TextView tv_rest_name;
        private TextView tv_publish_status;
        private TextView tv_time;
        private TextView tv_share_status;
        private RotationLoadingView rotation_loadingview;
    }


}
