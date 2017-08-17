package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;
import com.fengnian.smallyellowo.foodie.widgets.VerticalTextView;

/**
 * Created by chenglin on 2017-2-27.
 */

public class Brief2_2_StyleHeadViewHolder extends BaseHeadViewHolder {
    private CircleImageView iv_cover;
    private VerticalTextView tv_total_price;
    private VerticalTextView tv_poeple_num;
    private VerticalTextView tv_class;
    private VerticalTextView tv_rest_name;
    private VerticalTextView tv_title_2;
    private VerticalTextView tv_level_2;
    private TextView tv_attention;
    private View title_line;

    @Override
    public View getTv_attention() {
        return tv_attention;
    }

    public Brief2_2_StyleHeadViewHolder(DynamicDetailActivity activity, View headView) {
        super(activity, headView);
    }

    protected void onSetHeadView() {
        iv_cover = (CircleImageView) findViewById(R.id.iv_cover);
        tv_total_price = (VerticalTextView) findViewById(R.id.tv_total_price_2);
        tv_poeple_num = (VerticalTextView) findViewById(R.id.tv_poeple_num_2);
        tv_class = (VerticalTextView) findViewById(R.id.tv_class_2);
        tv_attention = (TextView) findViewById(R.id.tv_attention);
        tv_rest_name = (VerticalTextView) findViewById(R.id.tv_rest_name_2);
        tv_title_2 = (VerticalTextView) findViewById(R.id.tv_title_2);
        tv_level_2 = (VerticalTextView) findViewById(R.id.tv_level_2);
        title_line = findViewById(R.id.title_line);
    }


    @Override
    protected void refresh1() {
        iv_cover.setRectAdius(5f);
        FFImageLoader.loadBigImage(getActivity(), getActivity().getHeadImage(), iv_cover);

        //字数大于18个字的时候，把标题的文字变小一些
        tv_title_2.setTextColor(getActivity().getResources().getColor(R.color.title_text_color));
        tv_title_2.setText(titleStr);
        tv_title_2.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(titleStr)) {
            if (titleStr.length() >= 18) {
                tv_title_2.setLineWidth(DisplayUtil.dip2px(14f));
                tv_title_2.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_2));
            } else {
                tv_title_2.setLineWidth(DisplayUtil.dip2px(17f));
                tv_title_2.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_5));
            }
        } else {
            tv_title_2.setVisibility(View.INVISIBLE);
        }

        //设置ratingBar
        VerticalTextView ratingTitle = (VerticalTextView) findViewById(R.id.vertical_rating_bar_title);
        CustomRatingBar verticalRatingBar = (CustomRatingBar) findViewById(R.id.vertical_rating_bar);
        int starLevel = getActivity().data.getStarLevel();
        if (starLevel > 0) {
            initVerticalTextViewParams(ratingTitle);
            ratingTitle.setText("评分：");
            verticalRatingBar.setLevel(starLevel);
            initVerticalTextViewParams(tv_level_2);

            tv_level_2.setTextColor(getActivity().getResources().getColor(R.color.brief2_2_style_level_desc));
            tv_level_2.setText(getActivity().data.pullStartLevelString());

            ratingTitle.setVisibility(View.VISIBLE);
            verticalRatingBar.setVisibility(View.VISIBLE);
            tv_level_2.setVisibility(View.VISIBLE);
        } else {
            ratingTitle.setVisibility(View.GONE);
            verticalRatingBar.setVisibility(View.GONE);
            tv_level_2.setVisibility(View.GONE);
        }

        //总价
        initVerticalTextViewParams(tv_total_price);
        if (getActivity().data.getFood().getTotalPrice() == 0) {
            tv_total_price.setVisibility(View.GONE);
        } else {
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price.setText("总价：¥" + FFUtils.getSubFloat(getActivity().data.getFood().getTotalPrice()));
        }

        //餐类
        initVerticalTextViewParams(tv_class);
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
        initVerticalTextViewParams(tv_poeple_num);
        if (getActivity().data != null && getActivity().data.getFood() != null) {
            int foodNumber = getActivity().data.getFood().getNumberOfPeople();
            if (foodNumber > 0) {
                tv_poeple_num.setText("人数：" + foodNumber + "人");
                tv_poeple_num.setVisibility(View.VISIBLE);
            }
        }

        //餐厅
        initRest(tv_rest_name);
        initVerticalTextViewParams(tv_rest_name);
        if (getActivity().data.getFood().getPoi() != null && !TextUtils.isEmpty(getActivity().data.getFood().getPoi().getTitle())) {
            tv_rest_name.setText("餐厅：" + getActivity().data.getFood().getPoi().getTitle());
        }

        //设置精选ICON的位置
        if (getActivity().data.isHandPick()) {
            iv_is_jing.setVisibility(View.VISIBLE);

            int coverWidth = iv_cover.getWidth();
            if (coverWidth > 0) {
                int jingIconWidth = getActivity().getResources().getDrawable(R.mipmap.dynamic_detail_jing).getIntrinsicWidth() / 2;
                int margin = iv_cover.getLeft() + coverWidth / 2 - jingIconWidth;
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_is_jing.getLayoutParams();
                params1.leftMargin = margin;
                iv_is_jing.setLayoutParams(params1);
            }
        } else {
            iv_is_jing.setVisibility(View.GONE);
        }

        //标题旁边的那条线处理
        if (tv_title_2.getVisibility() == View.VISIBLE
                && (tv_class.getVisibility() == View.VISIBLE
                || tv_rest_name.getVisibility() == View.VISIBLE
                || tv_poeple_num.getVisibility() == View.VISIBLE
                || tv_level_2.getVisibility() == View.VISIBLE)) {
            title_line.setVisibility(View.VISIBLE);
        } else {
            title_line.setVisibility(View.INVISIBLE);
        }

    }

    private void initVerticalTextViewParams(VerticalTextView textView) {
        textView.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_2));
        textView.setTextColor(getActivity().getResources().getColor(R.color.title_text_color));
        textView.setLineWidth(getActivity().getResources().getDimensionPixelSize(R.dimen.vertical_textview_width));
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
