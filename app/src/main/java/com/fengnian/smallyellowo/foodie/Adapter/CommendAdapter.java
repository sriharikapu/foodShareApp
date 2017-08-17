package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19.
 */
public class CommendAdapter extends BaseAdapter {


    public ArrayList<SYUser> commentuser;

    public CommendAdapter(ArrayList<SYUser> commentuser) {
        this.commentuser = commentuser;
    }

    @Override
    public int getCount() {
        return commentuser==null?0:commentuser.size();
    }

    @Override
    public Object getItem(int i) {
        return commentuser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder holder=null;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_recommend2, null);
            holder = new ViewHolder();
            holder.tv_nikename = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_commend = (TextView) view.findViewById(R.id.tv_time);
            holder.iv_icon = (ImageView) view.findViewById(R.id.iv_img);
            holder.iv_add_crown = (ImageView) view.findViewById(R.id.iv_add_crown);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Context context = view.getContext();
        SYUser syUser = commentuser.get(i);
        holder.tv_nikename.setText(syUser.getNickName());
        holder.tv_commend.setText(syUser.getPersonalDeclaration());

        IsAddCrownUtils.checkIsAddCrow(syUser,holder.iv_add_crown);
        FFImageLoader.loadAvatar((FFContext) context,syUser.getHeadImage().getUrl(), holder.iv_icon);

        return view;

    }

    public class ViewHolder{
        ImageView iv_icon,iv_add_crown;
        TextView tv_nikename;
        TextView tv_commend;


    }
}

