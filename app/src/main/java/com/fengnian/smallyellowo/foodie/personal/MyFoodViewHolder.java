package com.fengnian.smallyellowo.foodie.personal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.View.RotationLoadingView;

/**
 * Created by chenglin on 2017-5-4.
 */

public class MyFoodViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title, tv_business_name, tv_location, tv_time;
    TextView tv_pic_count, tv_share_status, tv_publish_status;
    ImageView image, profile_camera;
    RatingBar rating_bar;
    View line_bottom, item_root;
    RotationLoadingView rotation_loadingview;
    RecyclerLoadMoreFooterView footerView;
    ImageView emptyImageView;
    LinearLayout convertView;

    public MyFoodViewHolder(View view) {
        super(view);
        convertView = (LinearLayout) view;
        item_root = view.findViewById(R.id.item_root);
        rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
        line_bottom = view.findViewById(R.id.line_bottom);
        image = (ImageView) view.findViewById(R.id.image);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_business_name = (TextView) view.findViewById(R.id.tv_business_name);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        profile_camera = (ImageView) view.findViewById(R.id.profile_camera);
        tv_pic_count = (TextView) view.findViewById(R.id.tv_pic_count);
        tv_share_status = (TextView) view.findViewById(R.id.tv_share_status);
        tv_publish_status = (TextView) view.findViewById(R.id.profile_un_save_btn);
        rotation_loadingview = (RotationLoadingView) view.findViewById(R.id.rotation_loadingview);
        footerView = (RecyclerLoadMoreFooterView) view.findViewById(R.id.recycler_load_footer);
    }
}