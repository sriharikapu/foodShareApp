package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.NoticeResult;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.tv_content;

/**
 * Created by Administrator on 2016-11-9.
 */

public class MessageAdapter extends BaseAdapter {
    private List<NoticeResult.MessageListBean> mlist;

    private FFContext mcontext;

    public MessageAdapter(FFContext context, List<NoticeResult.MessageListBean> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.isEmpty() ? 0 : mlist.size() + 1;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewholder holder = null;
        if (view == null) {
            holder = new viewholder();
            view = View.inflate((Context) mcontext, R.layout.items_message_center, null);
            holder.rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
            holder.iv_header = (ImageView) view.findViewById(R.id.iv_header);
            holder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
            holder.iv_zan_or_remark_delete = (ImageView) view.findViewById(R.id.iv_zan_or_remark_delete);
            holder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            holder.tv_content = (TextView) view.findViewById(tv_content);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            holder.rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
            view.setTag(holder);
        } else {
            holder = (viewholder) view.getTag();
        }
        if (mlist.size() >= 0 && mlist.size() == i) {
            holder.rl_1.setVisibility(View.GONE);
            holder.rl_2.setVisibility(View.VISIBLE);
        } else {
            holder.rl_1.setVisibility(View.VISIBLE);
            holder.rl_2.setVisibility(View.GONE);
        }
        if (mlist.size() > 0 && (mlist.size() != i)) {
            NoticeResult.MessageListBean bean = mlist.get(i);
            setUI(holder, bean);
        }

        return view;
    }


    private void setUI(viewholder holder, NoticeResult.MessageListBean bean) {
        String is_command_delete = bean.getCommentIsDelete();
        String type = bean.getInteractType();//0评论1回复2赞 3验证消息提醒4 精选

        String head_url = bean.getInteractHeadImage();
        String content = bean.getContent();
        String img = bean.getFoodRecordImage();
        String location = bean.getFoodRecordPosition();


        if ("true".equals(is_command_delete)) {
            //删除了该评论
            holder.tv_content.setVisibility(View.INVISIBLE);
            holder.tv_content.setText("");
            holder.iv_zan_or_remark_delete.setVisibility(View.VISIBLE);
            holder.iv_zan_or_remark_delete.setBackgroundResource(R.mipmap.comand_is_detle);
        } else if ("4".equals(type)) {
            //精选
            holder.iv_header.setBackgroundResource(R.mipmap.jingxuan_laba);
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.iv_zan_or_remark_delete.setVisibility(View.GONE);
            holder.tv_content.setText("已将您的内容选为精选");
        } else if ("2".equals(type)) {
            //2 zan
            holder.iv_zan_or_remark_delete.setVisibility(View.VISIBLE);
            holder.tv_content.setVisibility(View.INVISIBLE);
            holder.tv_content.setText("");
            holder.iv_zan_or_remark_delete.setBackgroundResource(R.mipmap.message_dianzan_img);
        } else {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.iv_zan_or_remark_delete.setVisibility(View.GONE);
            holder.tv_content.setText(content);
        }
        if (!"4".equals(type)) {
            FFImageLoader.loadAvatar((BaseActivity) mcontext, head_url, holder.iv_header);
        }
        holder.tv_nickname.setText(bean.getInteractNickname());
        holder.tv_time.setText(bean.getInteractTime());

        if (img.length() > 0) {
            holder.iv_img.setVisibility(View.VISIBLE);
            holder.tv_location.setVisibility(View.GONE);
            FFImageLoader.loadSmallImage((BaseActivity) mcontext, img, holder.iv_img);
        } else {
            holder.iv_img.setVisibility(View.GONE);
            holder.tv_location.setVisibility(View.VISIBLE);
            holder.tv_location.setText(location);
        }

        //动态改变对齐规则
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tv_content.getLayoutParams();
        if (holder.iv_img.getVisibility() == View.VISIBLE) {
            params.addRule(RelativeLayout.LEFT_OF, R.id.iv_img);
        } else {
            params.addRule(RelativeLayout.LEFT_OF, R.id.tv_location);
        }
        holder.tv_content.setLayoutParams(params);
    }

    private class viewholder {
        private ImageView iv_header, iv_zan_or_remark_delete, iv_img, is_add_crown;
        private TextView tv_nickname, tv_content, tv_time, tv_location;
        private RelativeLayout rl_1, rl_2;
    }
}
