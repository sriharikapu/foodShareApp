package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publish.SYExploitValueRank;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.tv_bianhao;
import static com.fengnian.smallyellowo.foodie.R.id.tv_gongxunzhi;
import static com.fengnian.smallyellowo.foodie.R.id.tv_nickname;
import static com.fengnian.smallyellowo.foodie.R.id.tv_number;

/**
 * Created by Administrator on 2017-2-28.
 */

public class ClubPaiHangAdapter extends BaseAdapter {
    private FFContext mcontext;
    private List<SYExploitValueRank> mlist;
    private SYExploitValueRank temp_user;
    public ClubPaiHangAdapter(FFContext context,List<SYExploitValueRank> list,SYExploitValueRank temp_user_rank) {

        this.mcontext=context;
        this.mlist=list;
        this.temp_user=temp_user_rank;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewholder holder=null;
        if(convertView==null){
            holder=new viewholder();
            convertView=View.inflate((Context) mcontext, R.layout.item_club_paihang,null);
            holder.tv_number= (TextView) convertView.findViewById(tv_number);
            holder.tv_nickname= (TextView) convertView.findViewById(tv_nickname);
            holder.tv_bianhao= (TextView) convertView.findViewById(tv_bianhao);
            holder.tv_gongxunzhi= (TextView) convertView.findViewById(tv_gongxunzhi);
            holder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        }else{
            holder= (viewholder) convertView.getTag();
        }
        SYExploitValueRank user_Rank=mlist.get(position);
        holder.tv_number.setText(user_Rank.getRank()+"");
       if(temp_user!=null){
       if(temp_user.getAccount().equals(user_Rank.getAccount())){
           holder.iv_head.setPadding(FFUtils.getPx(3),FFUtils.getPx(3),FFUtils.getPx(3),FFUtils.getPx(3));
       }else{

           holder.iv_head.setPadding(0,0,0,0);
       }
       }

        FFImageLoader.loadAvatar(mcontext,user_Rank.getHeadImage(),holder.iv_head);




        holder.tv_bianhao.setText(user_Rank.getClubNum());
        holder.tv_nickname.setText(user_Rank.getNickname());
        holder.tv_gongxunzhi.setText(user_Rank.getExploitValue()+"");

        return convertView;
    }

    private class viewholder{
        private TextView  tv_number,tv_nickname,tv_bianhao,tv_gongxunzhi;
        private ImageView iv_head  ;

    }
}
