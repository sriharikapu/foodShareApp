package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.BaseDetailAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.DetailAdapterUtils;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.VerticalTextView;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Created by chenglin on 2017-3-6.
 */

public class RichContentChina2_SnapAdapter extends SnapBaseAdapter {
    private Map<String, Boolean> sortIdMap = new HashMap<String, Boolean>();
    private boolean isLeft = true;

    public RichContentChina2_SnapAdapter(Activity activity, SYFeed data, String url) {
        super(activity, data, url);

        sortIdMap.clear();
        isLeft = true;
        final int count = data.getFood().getRichTextLists().size();
        for (int i = 0; i < count; i++) {
            SYRichTextPhotoModel photoModel = data.getFood().getRichTextLists().get(i);
            if (photoModel.isTextPhotoModel()) {
                sortIdMap.put(photoModel.getId(), isLeft);
                isLeft = !isLeft;
            }
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (data != null) {
            count = 1 + data.getFood().getRichTextLists().size();
        } else {
            count = 0;
        }
        return count;
    }


    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            if (data.getFood().getRichTextLists().get(position - 1).isTextPhotoModel()) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    @Override
    public void getView(int position, final OnSnapViewCreatedListener listener) {
        switch (getItemViewType(position)) {
            case 0://头
            {
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_2_style_title, null);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                final View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_china_2_style_image, null, false);
                ViewGroup layout_1 = (ViewGroup) convertView.findViewById(R.id.layout_1);
                ImageView iv_img_1 = (ImageView) convertView.findViewById(R.id.iv_img_1);
                TextView tv_dish_name_1 = (TextView) convertView.findViewById(R.id.tv_dish_name_1);
                VerticalTextView tv_content_1 = (VerticalTextView) convertView.findViewById(R.id.tv_content_1);
                ImageView iv_comment_1 = (ImageView) convertView.findViewById(R.id.iv_comment_1);
                View tv_content_1_linear = convertView.findViewById(R.id.tv_content_1_linear);
                View tv_dish_name_top_1 = convertView.findViewById(R.id.tv_dish_name_top_1);
                View tv_dish_name_bottom_1 = convertView.findViewById(R.id.tv_dish_name_bottom_1);
                View tv_dish_name_linear_1 = convertView.findViewById(R.id.tv_dish_name_linear_1);

                ViewGroup layout_2 = (ViewGroup) convertView.findViewById(R.id.layout_2);
                ImageView iv_img_2 = (ImageView) convertView.findViewById(R.id.iv_img_2);
                TextView tv_dish_name_2 = (TextView) convertView.findViewById(R.id.tv_dish_name_2);
                VerticalTextView tv_content_2 = (VerticalTextView) convertView.findViewById(R.id.tv_content_2);
                ImageView iv_comment_2 = (ImageView) convertView.findViewById(R.id.iv_comment_2);
                View tv_content_2_linear = convertView.findViewById(R.id.tv_content_2_linear);
                View tv_dish_name_top_2 = convertView.findViewById(R.id.tv_dish_name_top_2);
                View tv_dish_name_bottom_2 = convertView.findViewById(R.id.tv_dish_name_bottom_2);
                View tv_dish_name_linear_2 = convertView.findViewById(R.id.tv_dish_name_linear_2);

                ImageView iv_img;
                VerticalTextView tv_content;
                TextView tv_dish_name;
                ImageView iv_comment;
                View tv_content_linear;
                View tv_dish_name_linear, tv_dish_name_top, tv_dish_name_bottom;

                boolean isShowLeft = sortIdMap.get(getItem(position).getId());

                if (isShowLeft) {
                    layout_1.setVisibility(View.VISIBLE);
                    layout_2.setVisibility(View.GONE);

                    iv_img = iv_img_1;
                    tv_dish_name = tv_dish_name_1;
                    iv_comment = iv_comment_1;
                    tv_content = tv_content_1;
                    tv_content_linear = tv_content_1_linear;
                    tv_dish_name_top = tv_dish_name_top_1;
                    tv_dish_name_bottom = tv_dish_name_bottom_1;
                    tv_dish_name_linear = tv_dish_name_linear_1;
                } else {
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);

                    iv_img = iv_img_2;
                    tv_dish_name = tv_dish_name_2;
                    iv_comment = iv_comment_2;
                    tv_content = tv_content_2;
                    tv_content_linear = tv_content_2_linear;
                    tv_dish_name_top = tv_dish_name_top_2;
                    tv_dish_name_bottom = tv_dish_name_bottom_2;
                    tv_dish_name_linear = tv_dish_name_linear_2;
                }


                //评价展示
                int commentLevel = getItem(position).getPhoto().getImageComment();
                BaseDetailAdapter.setCommentData(commentLevel, iv_comment, BaseDetailAdapter.CommentLevel.TYPE_STANDARD);

                //食物图片展示
                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                int imageSize = activity.getResources().getDimensionPixelSize(R.dimen.china2_Image_size);
                fixedImageViewSize(iv_img, position);

                //食物名称
                tv_dish_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        activity.getResources().getDimensionPixelSize(R.dimen.font_size_3_1));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_dish_name.getLayoutParams();
                params.width = DisplayUtil.dip2px(23f);

                String dishNameStr = getItem(position).getDishesName();
                if (!TextUtils.isEmpty(dishNameStr)) {
                    tv_dish_name_linear.setVisibility(View.VISIBLE);
                    tv_dish_name.setVisibility(View.VISIBLE);
                    tv_dish_name.setText(dishNameStr);

                    if (dishNameStr.length() > 13) {
                        tv_dish_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                activity.getResources().getDimensionPixelSize(R.dimen.font_size_1_1));
                        params.width = DisplayUtil.dip2px(17f);
                    }
                } else {
                    tv_dish_name.setText("");
                    tv_dish_name.setVisibility(View.INVISIBLE);
                    tv_dish_name_linear.setVisibility(View.INVISIBLE);
                }
                tv_dish_name.setLayoutParams(params);


                //食物描述
                LinearLayout.LayoutParams tvContentParams = (LinearLayout.LayoutParams) tv_content.getLayoutParams();
                tvContentParams.height = imageSize - DisplayUtil.dip2px(28f);
                tvContentParams.width = getDimension(R.dimen.detail_china_2_left_margin);
                tv_content.setLayoutParams(tvContentParams);
                tv_content.setTextHeight(tvContentParams.height);
                tv_content.setTextColor(activity.getResources().getColor(R.color.china_style_font_color_gray));

                if (DisplayUtil.screenWidth >= 720) {
                    tv_content.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_2));
                    tv_content.setLineWidth(DisplayUtil.dip2px(12));
                } else {
                    tv_content.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_1_1));
                    tv_content.setLineWidth(activity.getResources().getDimensionPixelSize(R.dimen.vertical_china2_textview_foodname_width));
                }

                String contentStr = getItem(position).getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content_linear.setVisibility(View.VISIBLE);
                    tv_content.setText(contentStr.replace("\n",""));
                } else {
                    tv_content.setText("");
                    tv_content_linear.setVisibility(View.INVISIBLE);
                }

                //右边的那个食物的文字描述不可见时，要做的一些操作
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tv_content_linear.getLayoutParams();
                int imageWidth = getDimension(R.dimen.china2_Image_size);
                if (tv_content_linear.getVisibility() == View.VISIBLE) {
                    int padding = (DisplayUtil.screenWidth - imageWidth - getDimension(R.dimen.detail_china_2_left_margin)) / 2;
                    if (isShowLeft) {
                        params1.leftMargin = padding;
                    } else {
                        params1.rightMargin = padding;
                    }
                } else {
                    if (isShowLeft) {
                        int padding = (getDimension(R.dimen.detail_china_2_left_margin)) / 2 - DisplayUtil.dip2px(20f);
                        params1.leftMargin = -padding + DisplayUtil.dip2px(14f / 2);//看布局的数值
                    } else {
                        int padding = getDimension(R.dimen.detail_china_2_left_margin) / 2 + DisplayUtil.dip2px(20f);
                        params1.rightMargin = padding + DisplayUtil.dip2px(11f / 2);//看布局的数值
                    }
                }
                tv_content_linear.setLayoutParams(params1);

                //当前viewType的最后一项
                int padding2 = getDimension(R.dimen.padding_small);
                if (position == data.getFood().getRichTextLists().size()) {
                    convertView.setPadding(0, padding2, 0, DisplayUtil.dip2px(30f));
                } else {
                    if (position >= 1) {
                        if (getItemViewType(position - 1) == 2) {
                            convertView.setPadding(0, 0, 0, 0);
                        } else {
                            convertView.setPadding(0, padding2, 0, 0);
                        }
                    } else {
                        convertView.setPadding(0, padding2 - DisplayUtil.dip2px(5f), 0, 0);
                    }
                }

                LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) tv_dish_name_top.getLayoutParams();
                LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) tv_dish_name_bottom.getLayoutParams();
                params3.height = -2;
                params4.height = -2;
                if (!TextUtils.isEmpty(dishNameStr) && tv_content_linear.getVisibility() != View.VISIBLE) {
                    tv_dish_name_top.setVisibility(View.VISIBLE);
                    tv_dish_name_bottom.setVisibility(View.VISIBLE);
                    if (dishNameStr.length() > 11) {
                        params3.height = DisplayUtil.dip2px(35f);
                        params4.height = DisplayUtil.dip2px(35f);
                    } else if (dishNameStr.length() > 13) {
                        params3.height = DisplayUtil.dip2px(20f);
                        params4.height = DisplayUtil.dip2px(20f);
                    }
                } else {
                    tv_dish_name_top.setVisibility(View.GONE);
                    tv_dish_name_bottom.setVisibility(View.GONE);
                }
                tv_dish_name_top.setLayoutParams(params3);
                tv_dish_name_bottom.setLayoutParams(params4);

                FFImageCallBack lis = new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        listener.OnSnapViewCreated(convertView);
                    }

                    @Override
                    public void onDownLoadProgress(int downloaded, int contentLength) {

                    }
                };

                FFImageLoader.load_base((FFContext) activity, img.getUrl(), iv_img, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);

                return;
            }
            case 2: {//文字
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_2_text, null, false);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());
                listener.OnSnapViewCreated(convertView);
                return;
            }
        }
    }

    private int getDimension(int dimen) {
        return activity.getResources().getDimensionPixelSize(dimen);
    }

    public SYRichTextPhotoModel getItem(int position) {
        return data.getFood().getRichTextLists().get(position - 1);
    }

    private class HeadViewHolder {
        private ImageView iv_cover;
        private ImageView iv_avator;
        private TextView tv_name;
        private TextView tv_title;
        private TextView tv_total_price;
        private TextView tv_poeple_num;
        private TextView tv_rest_name;
        private TextView tv_class;
        private View headView;
        private View tv_attention;
        private ImageView iv_is_jing;
        protected RatingBar rb_level;
        private TextView tv_people_average;
        private TextView tv_foods;
        private TextView tv_time;
        private TextView tv_level;
        private View ll_level_container;


        public void setHeadView(View headView) {
            this.headView = headView;
            iv_cover = (ImageView) findViewById(R.id.iv_cover);
            iv_avator = (ImageView) findViewById(R.id.iv_avator);
            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
            tv_total_price = (TextView) findViewById(R.id.tv_total_price);
            tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
            tv_class = (TextView) findViewById(R.id.tv_class);
            rb_level = (RatingBar) findViewById(R.id.rb_level);
            tv_attention = findViewById(R.id.tv_attention);
            iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
            tv_people_average = (TextView) findViewById(R.id.tv_people_average);
            tv_foods = (TextView) findViewById(R.id.tv_foods);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_level = (TextView) findViewById(R.id.tv_level);
            ll_level_container = findViewById(R.id.ll_level_container);
        }


        public void refresh(final OnSnapViewCreatedListener listener) {

            final SnapItemInfo info = new SnapItemInfo();
            info.count = 2;
            if (data == null) {
                return;
            }

            if (data.getStarLevel() == 0) {
                ll_level_container.setVisibility(View.GONE);
            } else {
                ll_level_container.setVisibility(View.VISIBLE);
                rb_level.setRating(data.getStarLevel());
                tv_level.setText(data.pullStartLevelString());
            }
            tv_attention.setVisibility(View.GONE);

            info.count = 1;
            FFImageCallBack lis = new FFImageCallBack() {
                @Override
                public void imageLoaded(Bitmap bitmap, String imageUrl) {
                    info.sum++;
                    if (info.sum == info.count) {
                        listener.OnSnapViewCreated(headView);
                    }
                }

                @Override
                public void onDownLoadProgress(int downloaded, int contentLength) {

                }
            };
            if (data != null &&
                    data.getUser() != null &&
                    data.getUser().getHeadImage() != null &&
                    data.getUser().getHeadImage().getUrl() != null) {
                info.count = 2;
                FFImageLoader.load_base((FFContext) activity, data.getUser().getHeadImage().getUrl(), iv_avator, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, FFImageLoader.TYPE_ROUND, lis);
            }

            if (data.getUser() != null) {
                tv_name.setText(data.getUser().getNickName());
            }

            //字数大于18个字的时候，把标题的文字变小一些
            String title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
            tv_title.setText(title);
            if (!TextUtils.isEmpty(title)) {
                if (title.length() >= 18) {
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_5));
                }
            }

            tv_time.setText("时间:" + TimeUtils.getTime("yyyy-MM-dd    HH:mm", data.getTimeStamp()));
            tv_level.setText(data.pullStartLevelString());

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
            if (data.getFood().getTotalPrice() == 0) {
                tv_total_price_title.setVisibility(View.GONE);
                tv_total_price.setVisibility(View.GONE);
            } else {
                tv_total_price_title.setVisibility(View.VISIBLE);
                tv_total_price.setVisibility(View.VISIBLE);
                FFUtils.setText(tv_total_price, "¥", FFUtils.getSubFloat(data.getFood().getTotalPrice()));
            }

            //餐类
            View tv_class_title = findViewById(R.id.tv_class_title);
            tv_class_title.setVisibility(View.GONE);
            tv_class.setVisibility(View.GONE);
            if (data.getFood() != null) {
                String foodTypeStr = data.getFood().getFoodTypeString();
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
            if (data != null && data.getFood() != null) {
                int foodNumber = data.getFood().getNumberOfPeople();
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
                double total = data.getFood().getTotalPrice();
                int foodNumber = data.getFood().getNumberOfPeople();
                tv_people_average.setText("¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
            }


            //用餐的一些信息
            View detail_eat_icon = findViewById(R.id.detail_eat_icon);
            View line_1 = findViewById(R.id.line_1);
            if (data.getFood().getTotalPrice() == 0 && (data.getFood().getFoodType() > 6
                    || data.getFood().getFoodType() < 1) && data.getFood().getNumberOfPeople() == 0) {
                detail_eat_icon.setVisibility(View.GONE);
                line_1.setVisibility(View.GONE);
            } else {
                detail_eat_icon.setVisibility(View.VISIBLE);
                line_1.setVisibility(View.VISIBLE);
            }

            //水单
            String dishString = getDishString();
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

            //是否精华内容
            if (data.isHandPick()) {
                iv_is_jing.setVisibility(View.VISIBLE);
            } else {
                iv_is_jing.setVisibility(View.GONE);
            }

            //商户
            View tv_rest_name_title = findViewById(R.id.tv_rest_name_title);
            tv_rest_name.setVisibility(View.GONE);
            tv_rest_name_title.setVisibility(View.GONE);
            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {//商户名称
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name_title.setVisibility(View.VISIBLE);
                tv_rest_name.setText(data.getFood().getPoi().getTitle());
            }
            setKingCrownInfo(headView,data);

            FFImageLoader.load_base((FFContext) activity, data.getFood().pullHeadImage(), iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);
        }

        public View findViewById(int id) {
            return headView.findViewById(id);
        }


        private Activity getActivity() {
            return activity;
        }

        public String getDishString() {
            if (data == null) {
                return "";
            }
            LinkedHashSet<String> dishs = new LinkedHashSet<>();
            for (SYRichTextPhotoModel rt : data.getFood().getRichTextLists()) {
                if (!FFUtils.isStringEmpty(rt.getDishesName())) {
                    dishs.add(rt.getDishesName());
                }
            }
//        if (data.getFood().getDishesNameList() != null) {
//            for (String dishName : data.getFood().getDishesNameList()) {
//                if (!FFUtils.isStringEmpty(dishName)) {
//                    dishs.add(dishName);
//                }
//            }
//        }
            StringBuilder sb = new StringBuilder();
            for (String dishName : dishs) {
                sb.append(dishName);
                sb.append(",");
            }
            return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        }

    }

    private int fixedImageViewSize(ImageView iv_img, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
        int bitmapWidth = DetailAdapterUtils.getImageWidth(getItem(position));
        int bitmapHeight = DetailAdapterUtils.getImageHeight(getItem(position));

        params.width = activity.getResources().getDimensionPixelSize(R.dimen.china2_Image_size);
        if (bitmapWidth * 1f / bitmapHeight * 1f >= 1) {
            params.height = params.width;
        } else {
            params.height = (int) (params.width * 4f / 3f);
        }
        iv_img.setLayoutParams(params);
        return params.width;
    }
}
