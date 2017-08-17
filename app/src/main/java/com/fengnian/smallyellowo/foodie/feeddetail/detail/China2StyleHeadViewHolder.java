package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-3-6.
 */

public class China2StyleHeadViewHolder extends BaseHeadViewHolder {
    private ImageView iv_cover;
    private TextView tv_total_price;
    private TextView tv_poeple_num;
    private TextView tv_class;
    private TextView tv_foods;
    private TextView tv_rest_name;
    private TextView tv_people_average;
    private TextView tv_attention;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public China2StyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_foods = (TextView) findViewById(R.id.tv_foods);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        tv_people_average = (TextView) findViewById(R.id.tv_people_average);
    }


    @Override
    protected void refresh1() {
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);

        //字数大于18个字的时候，把标题的文字变小一些
        String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
        if (!TextUtils.isEmpty(title)) {
            if (title.length() >= 18) {
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_5));
            }
        }

        //设置ratingBar高度
        int ratingBarHeight = getActivity().getResources().getDrawable(R.drawable.china_style_rating_bar_star).getIntrinsicHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rb_level.getLayoutParams();
        params.height = ratingBarHeight;
        rb_level.setLayoutParams(params);

        //设置基准线的长度
        View base_line = findViewById(R.id.base_line);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) base_line.getLayoutParams();
        params1.width = DisplayUtil.screenWidth / 2 - getActivity().getResources().getDimensionPixelSize(R.dimen.padding_big);
        base_line.setLayoutParams(params1);

        //总价
        View tv_total_price_title = findViewById(R.id.tv_total_price_title);
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price_title.setVisibility(View.GONE);
            tv_total_price.setVisibility(View.GONE);
        } else {
            tv_total_price_title.setVisibility(View.VISIBLE);
            tv_total_price.setVisibility(View.VISIBLE);
            FFUtils.setText(tv_total_price, "¥", FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
        }

        //餐类
        View tv_class_title = findViewById(R.id.tv_class_title);
        tv_class_title.setVisibility(View.GONE);
        tv_class.setVisibility(View.GONE);
        if (getActivity().data.getFood() != null) {
            String foodTypeStr = getActivity().data.getFood().getFoodTypeString();
            if (!TextUtils.isEmpty(foodTypeStr)) {
                FFUtils.setText(tv_class, "", foodTypeStr);
                tv_class_title.setVisibility(View.VISIBLE);
                tv_class.setVisibility(View.VISIBLE);
            }
        }

        //人数
        View tv_poeple_num_title = findViewById(R.id.tv_poeple_num_title);
        tv_poeple_num_title.setVisibility(View.GONE);
        tv_poeple_num.setVisibility(View.GONE);
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                FFUtils.setText(tv_poeple_num, "", foodNumber + "人");
                tv_poeple_num_title.setVisibility(View.VISIBLE);
                tv_poeple_num.setVisibility(View.VISIBLE);
            }
        }

        //人均
        View tv_people_average_title = findViewById(R.id.tv_people_average_title);
        tv_people_average_title.setVisibility(View.GONE);
        tv_people_average.setVisibility(View.GONE);
        if (tv_total_price.getVisibility() == View.VISIBLE && tv_poeple_num.getVisibility() == View.VISIBLE) {
            tv_people_average_title.setVisibility(View.VISIBLE);
            tv_people_average.setVisibility(View.VISIBLE);
            double total = getActivity().data.getFood().getTotalPrice();
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            tv_people_average.setText("¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
        }

        //用餐的一些信息
        View detail_eat_icon = findViewById(R.id.detail_eat_icon);
        View line_1 = findViewById(R.id.line_1);
        if (getActivity().data.getFood().getTotalPrice() == 0 && (getActivity().data.getFood().getFoodType() > 6
                || getActivity().data.getFood().getFoodType() < 1) && getActivity().data.getFood().getNumberOfPeople() == 0) {
            detail_eat_icon.setVisibility(View.GONE);
            line_1.setVisibility(View.GONE);
        } else {
            detail_eat_icon.setVisibility(View.VISIBLE);
            line_1.setVisibility(View.VISIBLE);
        }

        //水单
        String dishString = getActivity().getDishString();
        View menu_image = findViewById(R.id.menu_image);
        View line_2 = findViewById(R.id.line_2);
        if (FFUtils.isStringEmpty(dishString)) {
            line_2.setVisibility(View.GONE);
            menu_image.setVisibility(View.GONE);
            tv_foods.setVisibility(View.GONE);
        } else {
            line_2.setVisibility(View.VISIBLE);
            menu_image.setVisibility(View.VISIBLE);
            tv_foods.setVisibility(View.VISIBLE);
            tv_foods.setText(dishString);
        }

        //商户
        View tv_rest_name_title = findViewById(R.id.tv_rest_name_title);
        tv_rest_name.setVisibility(View.GONE);
        tv_rest_name_title.setVisibility(View.GONE);
        initRest(tv_rest_name);
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setVisibility(View.VISIBLE);
            tv_rest_name_title.setVisibility(View.VISIBLE);
            tv_rest_name.setText(getActivity().data.getFood().getPoi().getTitle());
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
}
