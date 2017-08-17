package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.List;

/**
 * Created by chenglin on 2017-3-2.
 */

public class BriefStyleHeadViewHolder extends BaseHeadViewHolder {
    public TextView view_index;
    private TextView tv_total_price;
    private TextView tv_poeple_num;
    private TextView tv_class;
    private TextView tv_rest_name;
    private TextView tv_attention;
    private View line_1, line_2, line_3;
    private BriefHeadViewPager briefHeadViewPager;
    private TextView tv_people_average;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public BriefStyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
        tv_people_average = (TextView) findViewById(R.id.tv_people_average);
        view_index = (TextView) findViewById(R.id.view_index);
        line_1 = findViewById(R.id.line_1);
        line_2 = findViewById(R.id.line_2);
        line_3 = findViewById(R.id.line_3);

        //第一次时初始化头部ViewPager
        RelativeLayout headViewPagerParent = (RelativeLayout) findViewById(R.id.head_view_pager_parent);
        if (hasPhoto()) {
            briefHeadViewPager = new BriefHeadViewPager(getActivity(), this, getActivity().data);
            briefHeadViewPager.setId(R.id.view_pager);
            int bitmapWidth = DetailAdapterUtils.getImageWidth(briefHeadViewPager.getSYRichTextPhotoModelList().get(0));
            int bitmapHeight = DetailAdapterUtils.getImageHeight(briefHeadViewPager.getSYRichTextPhotoModelList().get(0));

            int viewPagerHeight = (int) (1f * bitmapHeight * DisplayUtil.screenWidth / bitmapWidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.screenWidth, viewPagerHeight);
            headViewPagerParent.setVisibility(View.VISIBLE);
            headViewPagerParent.addView(briefHeadViewPager, 0, params);
        } else {
            headViewPagerParent.setVisibility(View.GONE);
        }
    }

    //根据图片的高度动态改变ViewPager的高度
    public void onViewPagerLayoutParamsChange(int BitmapWidth, int BitmapHeight) {
        if (briefHeadViewPager == null) {
            return;
        }
        final ViewGroup.LayoutParams params = briefHeadViewPager.getLayoutParams();
        int newHeight = BitmapHeight * params.width / BitmapWidth;

        //写个简单动画，让体验好一点，不忽高忽低 by chenglin
        ValueAnimator valueAnimator = ValueAnimator.ofInt(params.height, newHeight);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.height = (int) animation.getAnimatedValue();
                briefHeadViewPager.setLayoutParams(params);
            }
        });
        valueAnimator.start();
    }

    private boolean hasPhoto() {
        List<SYRichTextPhotoModel> list = getActivity().data.getFood().getRichTextLists();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isTextPhotoModel()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void refresh1() {
        //字数大于18个字的时候，把标题的文字变小一些
        String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();

        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }

        //设置ratingBar高度
        int ratingBarHeight = getActivity().getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rb_level.getLayoutParams();
        params.height = ratingBarHeight;
        rb_level.setLayoutParams(params);

        //总价
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price.setVisibility(View.GONE);
        } else {
            tv_total_price.setVisibility(View.VISIBLE);
            FFUtils.setText(tv_total_price, "总价：¥", FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
        }

        //餐类
        tv_class.setVisibility(View.GONE);
        if (getActivity().data.getFood() != null) {
            String foodTypeStr = getActivity().data.getFood().getFoodTypeString();
            if (!TextUtils.isEmpty(foodTypeStr)) {
                FFUtils.setText(tv_class, "餐类:", foodTypeStr);
                tv_class.setVisibility(View.VISIBLE);
            }
        }

        //人数
        tv_poeple_num.setVisibility(View.GONE);
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                FFUtils.setText(tv_poeple_num, "人数:", foodNumber + "人");
                tv_poeple_num.setVisibility(View.VISIBLE);
            }
        }

        //人均
        tv_people_average.setVisibility(View.GONE);
        if (tv_total_price.getVisibility() == View.VISIBLE && tv_poeple_num.getVisibility() == View.VISIBLE) {
            tv_people_average.setVisibility(View.VISIBLE);
            double total = getActivity().data.getFood().getTotalPrice();
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            tv_people_average.setText("人均:¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
        }

        //商户
        initRest(tv_rest_name);
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {
            tv_rest_name.setText("餐厅:" + getActivity().data.getFood().getPoi().getTitle() + "  ");//为了右边的箭头间距
        }

        //处理那两条线，要不会挨着显示
        if (tv_total_price.getVisibility() != View.VISIBLE || tv_poeple_num.getVisibility() != View.VISIBLE) {
            line_1.setVisibility(View.GONE);
            line_3.setVisibility(View.GONE);
        } else {
            line_1.setVisibility(View.VISIBLE);
            line_3.setVisibility(View.VISIBLE);
        }

        //时间
        tv_time.setText(TimeUtils.getTime("yyyy-MM-dd    HH:mm", getActivity().data.getTimeStamp()));

        if (tv_poeple_num.getVisibility() != View.VISIBLE || tv_class.getVisibility() != View.VISIBLE) {
            line_2.setVisibility(View.GONE);
        } else {
            line_2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initAttention(int status) {
        switch (status) {
            case 0:
                tv_attention.setBackgroundResource(R.mipmap.brief_attention_eachother);
//                tv_attention.setText("互相关注");
                break;
            case 1:
                tv_attention.setBackgroundResource(R.mipmap.brief_attentioned);
//                tv_attention.setText("已关注");
                break;
            case 2:
                tv_attention.setBackgroundResource(R.mipmap.brief_add_attention);
//                tv_attention.setText("关注");
                break;
        }
    }
}
