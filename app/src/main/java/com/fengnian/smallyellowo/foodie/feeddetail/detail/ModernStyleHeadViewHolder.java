package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;

/**
 * Created by chenglin on 2017-3-6.
 */

public class ModernStyleHeadViewHolder extends BaseHeadViewHolder {
    private ImageView iv_cover;
    private TextView tv_total_price;
    private TextView tv_poeple_num;
    private TextView tv_class;
    private TextView tv_rest_name;
    private TextView tv_attention;
    private CustomRatingBar rb_level_custom;
    private View cover_frame;
    private TextView tv_people_average;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public ModernStyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        rb_level_custom = (CustomRatingBar) findViewById(R.id.rb_level_custom);
        cover_frame = findViewById(R.id.cover_frame);
        tv_people_average = (TextView) findViewById(R.id.tv_people_average);
    }

    @Override
    protected void refresh1() {
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);

        //字数大于18个字的时候，把标题的文字变小一些
        String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_level_container.getLayoutParams();

        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.padding_small);
            if (title.length() >= 12 && title.length() <= 15) {
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_6_1));
            }else if (title.length() > 15){
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_5));
            }
        } else {
            tv_title.setVisibility(View.GONE);
            params.topMargin = DisplayUtil.dip2px(38f);
        }
        ll_level_container.setLayoutParams(params);

        //设置ratingBar评分
        int ratingLevel = getActivity().data.getStarLevel();
        rb_level_custom.setLevel(ratingLevel);
        rb_level_custom.setStarIcon(R.drawable.star_modern_rating_solid, R.drawable.star_modern_rating_empty);

        //总价
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price.setVisibility(View.GONE);
        } else {
            tv_total_price.setVisibility(View.VISIBLE);
            String totalPriceStr = FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice());
            tv_total_price.setText("总价：¥" + totalPriceStr);
        }

        //餐类
        tv_class.setVisibility(View.GONE);
        if (getActivity().data.getFood() != null) {
            String foodTypeStr = getActivity().data.getFood().getFoodTypeString();
            if (!TextUtils.isEmpty(foodTypeStr)) {
                tv_class.setText("餐类：" + foodTypeStr);
                tv_class.setVisibility(View.VISIBLE);
            }
        }

        //人数
        tv_poeple_num.setVisibility(View.GONE);
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                tv_poeple_num.setText("人数：" + foodNumber + "人");
                tv_poeple_num.setVisibility(View.VISIBLE);
            }
        }

        //人均
        tv_people_average.setVisibility(View.GONE);
        if (tv_total_price.getVisibility() == View.VISIBLE && tv_poeple_num.getVisibility() == View.VISIBLE) {
            tv_people_average.setVisibility(View.VISIBLE);
            double total = getActivity().data.getFood().getTotalPrice();
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            tv_people_average.setText("人均：¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
        }

        //商户
        initRest(tv_rest_name);
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setText("餐厅：" + getActivity().data.getFood().getPoi().getTitle());
        }

        //如果没有商户，那么距离下方的间距需要发生变化
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) cover_frame.getLayoutParams();
        if (tv_rest_name.getVisibility() == View.VISIBLE){
            params1.bottomMargin = 0;
        }else {
            params1.bottomMargin = DisplayUtil.dip2px(15f);
        }
        cover_frame.setLayoutParams(params1);
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
