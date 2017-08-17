package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.WanDetailDyn;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.iv_lin_1;

/**
 * Created by Administrator on 2016-11-12.
 */

public class WantEatDetailAdapter extends BaseAdapter {

    private List<WanDetailDyn> mlist;
    private FFContext mcontext;

    public WantEatDetailAdapter(List<WanDetailDyn> mlist, FFContext mcontext) {
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

    final int height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8+25)) / 3);
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewholder holer = null;
        if (view == null) {
            holer = new viewholder();
            view = View.inflate((Context) mcontext, R.layout.item_want_eat_detail, null);
            holer.iv_head = (ImageView) view.findViewById(R.id.iv_head);
            holer.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holer.iv_img = (ImageView) view.findViewById(R.id.iv_avator);
            holer.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            holer.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holer.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holer.tv_level = (TextView) view.findViewById(R.id.tv_level);
            holer.rb_level = (RatingBar) view.findViewById(R.id.rb_level);
            holer.v_line_level = view.findViewById(R.id.v_line_level);

            holer.lin_1= (LinearLayout) view.findViewById(R.id.lin_1);
            holer.iv_lin_1= (ImageView) view.findViewById(iv_lin_1);
            holer.iv_lin_2= (ImageView) view.findViewById(R.id.iv_lin_2);
            holer.iv_lin_3= (ImageView) view.findViewById(R.id.iv_lin_3);

            view.setTag(holer);
        } else {
            holer = (viewholder) view.getTag();
        }
        WanDetailDyn detail = mlist.get(i);
        if (detail.getStarLevel() == 0) {
            holer.tv_level.setVisibility(View.INVISIBLE);
            holer.rb_level.setVisibility(View.INVISIBLE);
            holer.v_line_level.setVisibility(View.INVISIBLE);
        } else {
            holer.rb_level.setRating(detail.getStarLevel());
            holer.tv_level.setText(detail.pullStartLevelString());
            holer.tv_level.setVisibility(View.VISIBLE);
            holer.rb_level.setVisibility(View.VISIBLE);
            holer.v_line_level.setVisibility(View.VISIBLE);
        }
        IsAddCrownUtils.checkIsAddCrow(detail.getUser(),holer.iv_add_crown);
        FFImageLoader.loadAvatar(mcontext, detail.getUser().getHeadImage().getUrl(), holer.iv_head);
        if(detail.getUser()!=null)
           holer.tv_nickname.setText(detail.getUser().getNickName());
        else
            holer.tv_nickname.setText(detail.getUserName());
        holer.tv_time.setText(detail.getPublishTime());

        if(detail.getFoodImageArray()!=null&&detail.getFoodImageArray().size()>0){
            holer.iv_img.setVisibility(View.GONE);
            holer.lin_1.setVisibility(View.VISIBLE);
            setui(detail.getFoodImageArray(),holer.iv_lin_1,holer.iv_lin_2,holer.iv_lin_3);
        }else{
            holer.iv_img.setVisibility(View.VISIBLE);
            holer.lin_1.setVisibility(View.GONE);
            FFImageLoader.loadMiddleImage(mcontext, detail.getFoodImage(), holer.iv_img);
            holer.tv_title.setText(detail.getFoodContent());
        }


        return view;
    }


    private void   setui(List<String> list,ImageView iv0,ImageView iv1,ImageView iv3){
        int len=list.size();
        if(len==1){
            iv0.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(0), iv0, height, height, R.drawable.alpha);
            iv1.setVisibility(View.GONE);
            iv3.setVisibility(View.GONE);
        }

        if(len==2){
            iv0.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(0), iv0, height, height, R.drawable.alpha);
            iv1.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(1), iv1, height, height, R.drawable.alpha);
            iv3.setVisibility(View.GONE);
        }

        if(len==3){
            iv0.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(0), iv0, height, height, R.drawable.alpha);
            iv1.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(1), iv1, height, height, R.drawable.alpha);
            iv3.setVisibility(View.VISIBLE);
            FFImageLoader.loadNativeImage(mcontext, list.get(2), iv3, height, height, R.drawable.alpha);
        }
    }
    private class viewholder {
        private ImageView iv_head, iv_img,iv_add_crown;

        TextView tv_level;
        RatingBar rb_level;
        View v_line_level;
        private TextView tv_nickname, tv_time, tv_title;

        private LinearLayout lin_1;

        private ImageView  iv_lin_1,iv_lin_2,iv_lin_3;
    }
}
