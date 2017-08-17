package com.fengnian.smallyellowo.foodie.personal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;

/**
 * Created by chenglin on 2017-5-4.
 */

public class WantFoodViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title, tv_business_name;
    TextView tv_location, tv_level, tv_avarge_money;
    View line_bottom, item_root, lin_1, shop_linear;
    ImageView image;
    RatingBar rating_bar;
    RecyclerLoadMoreFooterView footerView;
    View tv_eated, emptyView;

    public WantFoodViewHolder(View view) {
        super(view);

        item_root = view.findViewById(R.id.item_root);
        rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
        line_bottom = view.findViewById(R.id.line_bottom);
        image = (ImageView) view.findViewById(R.id.image);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_avarge_money = (TextView) view.findViewById(R.id.tv_avarge_money);
        tv_business_name = (TextView) view.findViewById(R.id.tv_business_name);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_level = (TextView) view.findViewById(R.id.tv_level);
        lin_1 = view.findViewById(R.id.lin_1);
        shop_linear = view.findViewById(R.id.shop_linear);
        footerView = (RecyclerLoadMoreFooterView) view.findViewById(R.id.recycler_load_footer);
        tv_eated = view.findViewById(R.id.tv_eated);
    }
}