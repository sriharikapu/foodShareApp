package com.fengnian.smallyellowo.foodie.Adapter;

import android.view.View;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.personal.MyAllAttionActivity;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

/**
 * Created by Administrator on 2016-9-11.
 */

public class AllAttionAdapter extends FFBaseAdapter {


    public AllAttionAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
        super(viewHolderClass, layoutId, dataList, context);
    }

    public AllAttionAdapter(Class viewHolderClass, int layoutId, FFContext context) {
        super(viewHolderClass, layoutId, context);
    }

    @Override
    public void setViewData(Object viewholder, int position, Object model) {
        if (viewholder != null && viewholder instanceof  MyAllAttionActivity.RelationItemViewHolder) {
            MyAllAttionActivity.RelationItemViewHolder  viewHolder = ( MyAllAttionActivity.RelationItemViewHolder) viewholder;
            if (model != null && model instanceof SYUser) {
                SYUser userRelation = (SYUser) model;
                viewHolder.tv_name.setText(userRelation.getNickName());
                IsAddCrownUtils.checkIsAddCrow(userRelation,viewHolder.iv_add_crown);
                FFImageLoader.loadAvatar(mcontext.context(), userRelation.getHeadImage().getUrl(), viewHolder.iv_header);
                viewHolder.tv_content.setText(userRelation.getPersonalDeclaration());
                boolean  is__follow_me=userRelation.isFollowMe();
                boolean  is_by_follow_me=userRelation.isByFollowMe();
                viewHolder.sectionNameView.setVisibility(View.GONE);
                if(is__follow_me&&is_by_follow_me){
                    viewHolder.rl_1.setVisibility(View.VISIBLE);
                    viewHolder.iv_is_attion.setBackgroundResource(R.mipmap.each_attion);
                    viewHolder.tv_is_attion.setText("相互关注");
                }else if(!is__follow_me&&is_by_follow_me){
                    viewHolder.rl_1.setVisibility(View.VISIBLE);
                    viewHolder.iv_is_attion.setBackgroundResource(R.mipmap.already_attion);
                    viewHolder.tv_is_attion.setText("已关注");
                }else{
                    viewHolder.rl_1.setVisibility(View.INVISIBLE);
                }
               viewHolder.view_1.setVisibility(View.VISIBLE);
            }
        }

    }
}
