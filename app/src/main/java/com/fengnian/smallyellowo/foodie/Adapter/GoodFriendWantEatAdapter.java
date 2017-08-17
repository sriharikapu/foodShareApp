package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publish.GoodFriendWantEatBean;
import com.fengnian.smallyellowo.foodie.bean.publish.apiparams.GoodFriendWantListBean;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public class GoodFriendWantEatAdapter extends BaseAdapter {

    private List<GoodFriendWantListBean> mlist;
    private Context mcontext;

    public GoodFriendWantEatAdapter(List<GoodFriendWantListBean> mlist, Context mcontext) {
        this.mlist = mlist;
        this.mcontext = mcontext;
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
            view=View.inflate((Context) mcontext, R.layout.item_good_friends_want_eat_list,null);
            holder.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holder.iv_head= (ImageView) view.findViewById(R.id.iv_head);
            holder.tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
            holder.tv_content= (TextView) view.findViewById(R.id.tv_content);
            view.setTag(holder);
        }else{
           holder= (viewholder) view.getTag();
        }
        GoodFriendWantEatBean bean= mlist.get(i).getUser();
        String nickname=bean.getNickName();
        String url=bean.getHeadImage().getThumbUrl();
        String content=bean.getPersonalDeclaration();
//        IsAddCrownUtils.checkIsAddCrow(feed.getUser(),holder.iv_add_crown);
        FFImageLoader.loadAvatar(null,url,holder.iv_head);
        holder.tv_nickname.setText(nickname);
        holder.tv_content.setText(content);

        return view;
    }

    private class viewholder{

        private ImageView iv_head,iv_add_crown;
        private TextView tv_nickname,tv_content;

    }
}
