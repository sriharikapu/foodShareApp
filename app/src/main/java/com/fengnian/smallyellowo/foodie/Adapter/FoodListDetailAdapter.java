package com.fengnian.smallyellowo.foodie.Adapter;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.personal.FoodListDetailActivty;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016-9-13.
 */

public class FoodListDetailAdapter extends FFBaseAdapter {
    public FoodListDetailAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
        super(viewHolderClass, layoutId, dataList, context);
    }

    public FoodListDetailAdapter(Class viewHolderClass, int layoutId, FFContext context) {
        super(viewHolderClass, layoutId, context);
    }

    @Override
    public void setViewData(Object viewHolder, int position, Object model) {

        if (viewHolder != null && viewHolder instanceof FoodListDetailActivty.viewholder) {
            FoodListDetailActivty.viewholder  holder=(FoodListDetailActivty.viewholder)viewHolder;
            if(model!=null&&model instanceof SYFeed){
                SYFeed feed = (SYFeed) model;
               if( feed.getFood().getFrontCoverModel()==null){
                   if(feed.getFood().allImageContent()!=null&&feed.getFood().allImageContent().size()>0) {
                      String url= feed.getFood().allImageContent().get(0).getPhoto().getImageAsset().pullProcessedImageUrl();
                       FFImageLoader.loadSmallImage(mcontext.context(), url, holder.iv_img);
                   }
               }
                else{
                   String url= feed.getFood().getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().pullProcessedImageUrl();
                   FFImageLoader.loadSmallImage(mcontext.context(),url,holder.iv_img);
               }
                holder.tv_count.setText("1");//feed.getFood().getRichTextLists().size()+""
                holder.tv_time.setText(feed.getFood().getFrontCoverModel().getFrontCoverContent().getContent());
                String businessname=feed.getFood().getPoi().getTitle();
                holder.tv_business_name.setText("".equals(businessname)?"无商户":businessname);
                holder.tv_date.setText( new SimpleDateFormat("MM-dd").format(Long.valueOf(feed.getFood().getCreateTime())*1000));

            }
        }
    }
}
