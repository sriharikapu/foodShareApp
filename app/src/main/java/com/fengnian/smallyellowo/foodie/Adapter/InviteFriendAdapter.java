package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.InviteUserList;

import java.util.List;

/**
 * Created by Administrator on 2016-9-13.
 */

public class InviteFriendAdapter extends BaseAdapter{


    private FFContext mcontext;
    private List<InviteUserList> mlist;

    public InviteFriendAdapter(FFContext mcontext, List<InviteUserList> mlist) {
        this.mcontext = mcontext;
        this.mlist = mlist;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewholder holder=null;
        if(view==null){
            holder=new viewholder();
            view=View.inflate((Context) mcontext, R.layout.item_invite_friend,null);
            holder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            holder.tv_time= (TextView) view.findViewById(R.id.tv_time);
            view.setTag(holder);
        }else{
            holder= (viewholder) view.getTag();
        }
        InviteUserList inviteUser= mlist.get(i);
        holder.tv_name.setText(inviteUser.getNickName());
        holder.tv_time.setText(inviteUser.getRegistTime());
        return view;

    }
    private   class viewholder {
        private TextView tv_name;
        private TextView tv_time;
    }

}
