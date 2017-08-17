package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.FlowLayout;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-2-23.
 */

public class ChinaStyleHeadViewHolder extends BaseHeadViewHolder {
    private ImageView iv_cover;
    private LinearLayout ll_menu_container;
    private TextView tv_foods;
    private LinearLayout ll_rest_container;
    private TextView tv_rest_name;
    private FlowLayout eat_info_layout;
    private TextView tv_attention;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public ChinaStyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        ll_menu_container = (LinearLayout) findViewById(R.id.ll_menu_container);
        tv_foods = (TextView) findViewById(R.id.tv_foods);
        ll_rest_container = (LinearLayout) findViewById(R.id.ll_rest_container);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        eat_info_layout = (FlowLayout) findViewById(R.id.eat_info_layout);
    }

    @Override
    protected void refresh1() {
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);

        //字数大于18个字的时候，把标题的文字变小一些
        String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            if (title.length() >= 18) {
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_6));
            }
        }else {
            tv_title.setVisibility(View.GONE);
        }

        //设置ratingBar高度
        int ratingBarHeight = getActivity().getResources().getDrawable(R.drawable.china_style_rating_bar_star).getIntrinsicHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rb_level.getLayoutParams();
        params.height = ratingBarHeight;
        rb_level.setLayoutParams(params);

        eat_info_layout.removeAllViews();
        int index = 0;
        //餐类
        if (getActivity().data.getFood() != null) {
            String foodTypeStr = getActivity().data.getFood().getFoodTypeString();
            if (!TextUtils.isEmpty(foodTypeStr)) {
                View itemView = View.inflate(getActivity(), R.layout.china_style_title_item, null);
                TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                tv_show_title.setText("餐类");
                FFUtils.setText(tv_show_content, "：", foodTypeStr);
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                eat_info_layout.addView(itemView, params1);
                index++;
            }
        }

        //人均
        if (getActivity().data.getFood() != null) {
            double total = getActivity().data.getFood().getTotalPrice();
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();

            if (total > 0 && foodNumber > 0){
                View itemView = View.inflate(getActivity(), R.layout.china_style_title_item, null);
                TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                tv_show_title.setText("人均");
                tv_show_content.setText("：¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                eat_info_layout.addView(itemView, params1);
                index++;
            }
        }

        //总价
        if (getActivity().data.getFood().getTotalPrice() > 0) {
            View itemView = View.inflate(getActivity(), R.layout.china_style_title_item, null);
            TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
            TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
            tv_show_title.setText("总价");
            FFUtils.setText(tv_show_content, "：¥", FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
            ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
            eat_info_layout.addView(itemView, params1);
            index++;
        }

        //人数
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                View itemView = View.inflate(getActivity(), R.layout.china_style_title_item, null);
                TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                tv_show_title.setText("人数");
                FFUtils.setText(tv_show_content, "：", foodNumber + "人");
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                eat_info_layout.addView(itemView, params1);
                index++;
            }
        }

        //用餐的一些信息
        if (getActivity().data.getFood().getTotalPrice() == 0 && (getActivity().data.getFood().getFoodType() > 6
                || getActivity().data.getFood().getFoodType() < 1) && getActivity().data.getFood().getNumberOfPeople() == 0) {
            eat_info_layout.setVisibility(View.GONE);
        } else {
            eat_info_layout.setVisibility(View.VISIBLE);
        }


        //水单
        String dishString = getActivity().getDishString();
        if (FFUtils.isStringEmpty(dishString)) {
            ll_menu_container.setVisibility(View.GONE);
        } else {
            ll_menu_container.setVisibility(View.VISIBLE);
            tv_foods.setText("：" + dishString);
        }

        //商户
        initRest(ll_rest_container);
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setText("：" + getActivity().data.getFood().getPoi().getTitle());
        }

    }

    @Override
    protected void initAttention(int status) {
        switch (status) {
            case 0:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_attention_eachother, 0, 0, 0);
                tv_attention.setText("互相关注");
                break;
            case 1:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_attentioned, 0, 0, 0);
                tv_attention.setText("已关注");
                break;
            case 2:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_add_attention, 0, 0, 0);
                tv_attention.setText("关注");
                break;
        }
    }


    private int getItemWidth(final int index) {
        int eatLayoutWidth = DisplayUtil.screenWidth
                - getDimension(R.dimen.padding_middle) * 2
                - getDimension(R.dimen.padding_small) * 3
                - getActivity().getResources().getDrawable(R.mipmap.detail_eat_icon).getIntrinsicWidth();

        int itemWidth;
        if (DisplayUtil.screenWidth <= 720) {
            itemWidth = DisplayUtil.dip2px(110f);
        } else {
            itemWidth = DisplayUtil.dip2px(120f);
        }

        if (index % 2 == 1) {
            itemWidth = eatLayoutWidth - itemWidth - DisplayUtil.dip2px(5f);
        }
        return itemWidth;
    }

    private int getDimension(int dimension) {
        return getActivity().getResources().getDimensionPixelSize(dimension);
    }
}
