package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;

/**
 * Created by chenglin on 2017-2-27.
 */

public class Brief2StyleHeadViewHolder extends BaseHeadViewHolder {
    private CircleImageView iv_cover;
    private TextView tv_total_price;
    private TextView tv_poeple_num;
    private TextView tv_class;
    private TextView tv_rest_name;
    private View eat_info;
    private TextView tv_attention;
    private TextView tv_people_average;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public Brief2StyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (CircleImageView) findViewById(R.id.iv_cover);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        eat_info = findViewById(R.id.eat_info);
        tv_people_average = (TextView) findViewById(R.id.tv_people_average);
    }


    @Override
    protected void refresh1() {
        iv_cover.setRectAdius(5f);
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);

        //字数大于18个字的时候，把标题的文字变小一些
        String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
        tv_title.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            if (title.length() >= 18) {
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_4_2));
            }
        }else {
            tv_title.setVisibility(View.GONE);
        }

        //设置ratingBar高度
        int ratingBarHeight = getActivity().getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rb_level.getLayoutParams();
        params.height = ratingBarHeight;
        rb_level.setLayoutParams(params);

        boolean isShow = false;

        //总价
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price.setVisibility(View.GONE);
        } else {
            isShow = true;
            tv_total_price.setVisibility(View.VISIBLE);
            FFUtils.setText(tv_total_price, "总价：¥", FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
        }

        //餐类
        tv_class.setVisibility(View.GONE);
        if (getActivity().data.getFood() != null) {
            String foodTypeStr = getActivity().data.getFood().getFoodTypeString();
            if (!TextUtils.isEmpty(foodTypeStr)) {
                FFUtils.setText(tv_class, "餐类：", foodTypeStr);
                tv_class.setVisibility(View.VISIBLE);
                isShow = true;
            }
        }

        //人数
        tv_poeple_num.setVisibility(View.GONE);
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                FFUtils.setText(tv_poeple_num, "人数：", foodNumber + "人");
                tv_poeple_num.setVisibility(View.VISIBLE);
                isShow = true;
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
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {
            tv_rest_name.setText("餐厅：" + getActivity().data.getFood().getPoi().getTitle());
            isShow = true;
        }

        if (isShow) {
            eat_info.setVisibility(View.VISIBLE);
        } else {
            eat_info.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initAttention(int status) {
        switch (status) {
            case 0:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.black_ic_attention_eachother, 0, 0, 0);
                tv_attention.setText("互相关注");
                break;
            case 1:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.black_ic_attentioned, 0, 0, 0);
                tv_attention.setText("已关注");
                break;
            case 2:
                tv_attention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.black_ic_add_attention, 0, 0, 0);
                tv_attention.setText("关注");
                break;
        }
    }
}
