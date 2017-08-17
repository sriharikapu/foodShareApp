package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;

public class StandardHeadViewHolder extends BaseHeadViewHolder {

    private ImageView iv_cover;
    private TextView tv_total_price;
    private TextView tv_poeple_num;
    private TextView tv_class;
    private LinearLayout ll_menu_container;
    private TextView tv_foods;
    private ImageView iv_vis_all;
    private LinearLayout ll_rest_container;
    private TextView tv_rest_name;
    private TextView tv_attention;
    private TextView tv_people_average;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public StandardHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        ll_menu_container = (LinearLayout) findViewById(R.id.ll_menu_container);
        tv_foods = (TextView) findViewById(R.id.tv_foods);
        iv_vis_all = (ImageView) findViewById(R.id.iv_vis_all);
        ll_rest_container = (LinearLayout) findViewById(R.id.ll_rest_container);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        tv_people_average = (TextView) findViewById(R.id.tv_people_average);
    }


    @Override
    protected void refresh1() {
        //TODO zhangfan
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price.setVisibility(View.GONE);
        } else {
            FFUtils.setText(tv_total_price, "总价:¥", FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
        }
        FFUtils.setText(tv_class, "分类:", getActivity().data.getFood().getFoodTypeString());
        FFUtils.setText(tv_poeple_num, "人数:", getActivity().data.getFood().getNumberOfPeople());

        if (getActivity().data.getFood().getTotalPrice() == 0 && (getActivity().data.getFood().getFoodType() > 6 || getActivity().data.getFood().getFoodType() < 1) && getActivity().data.getFood().getNumberOfPeople() == 0) {
            ((View) tv_class.getParent()).setVisibility(View.GONE);
        } else {
            ((View) tv_class.getParent()).setVisibility(View.VISIBLE);
        }

        //人均
        tv_people_average.setVisibility(View.GONE);
        if (tv_total_price.getVisibility() == View.VISIBLE && tv_poeple_num.getVisibility() == View.VISIBLE) {
            tv_people_average.setVisibility(View.VISIBLE);
            double total = getActivity().data.getFood().getTotalPrice();
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            tv_people_average.setText("人均:¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
        }

        //水单
        String dishString = getActivity().getDishString();
        if (FFUtils.isStringEmpty(dishString)) {
            ll_menu_container.setVisibility(View.GONE);
        } else {
            ll_menu_container.setVisibility(View.VISIBLE);
            tv_foods.setText(dishString);
            if (FFUtils.getTextViewLength(tv_foods, dishString) > FFUtils.getDisWidth() - FFUtils.getPx(60)) {
                iv_vis_all.setVisibility(View.VISIBLE);
                iv_vis_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean vis = v.getTag() == null ? false : !((boolean) (v.getTag()));
                        tv_foods.setSingleLine(vis);
                        if (vis) {
                            iv_vis_all.setImageResource(R.mipmap.dynamic_detail_menu_right);
                        } else {
                            iv_vis_all.setImageResource(R.mipmap.dynamic_detail_menu_down);
                        }
                        v.setTag(vis);
                    }
                });
            } else {
                iv_vis_all.setVisibility(View.GONE);
            }
        }

        initRest(ll_rest_container);

        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setText("商户:" + getActivity().data.getFood().getPoi().getTitle() + " ");
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
