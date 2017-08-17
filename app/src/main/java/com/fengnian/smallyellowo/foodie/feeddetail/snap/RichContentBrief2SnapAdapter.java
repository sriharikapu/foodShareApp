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
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.LinkedHashSet;

/**
 * Created by chenglin on 2017-3-3.
 */

public class RichContentBrief2SnapAdapter extends SnapBaseAdapter {

    public RichContentBrief2SnapAdapter(Activity activity, SYFeed data, String url) {
        super(activity, data, url);
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
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_style_title, null);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                final View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_brief2_style_image, null, false);

                DynamicImageView iv_img = (DynamicImageView) convertView.findViewById(R.id.iv_img);
                TextView tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                ImageView iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
                View itemFoot = convertView.findViewById(R.id.item_foot);
                TextView publicTime = (TextView) convertView.findViewById(R.id.public_time);
                View line_1 = convertView.findViewById(R.id.line_1);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                int level = getItem(position).getPhoto().getImageComment();
                BaseDetailAdapter.setCommentData(level, iv_comment, BaseDetailAdapter.CommentLevel.TYPE_STANDARD);

                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                int imgWidth = FFUtils.getDisWidth();
                iv_img.setRectAdius(5f);

                RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
                if (img.getWidth() == 0) {
                    imageParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    iv_img.setMinimumHeight((int) (imgWidth * 0.6));
                } else {
                    imageParams.height = (int) (imgWidth * img.getHeight() / img.getWidth());
                    iv_img.setMinimumHeight(0);
                }
                iv_img.setLayoutParams(imageParams);

                FFUtils.setText(tv_dish_name, getItem(position).getDishesName());
                if (tv_dish_name.getVisibility() == View.VISIBLE) {
                    line_1.setVisibility(View.VISIBLE);
                } else {
                    line_1.setVisibility(View.GONE);
                }

                FFUtils.setText(tv_content, getItem(position).getContent());

                //当前viewType的最后一项
                if (position == data.getFood().getRichTextLists().size()) {
                    itemFoot.setVisibility(View.VISIBLE);
                    publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", data.getTimeStamp()));
                } else {
                    itemFoot.setVisibility(View.GONE);
                    publicTime.setText("");
                }

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
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_text, null, false);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                ImageView left_comma = (ImageView) convertView.findViewById(R.id.left_comma);
                ImageView right_comma = (ImageView) convertView.findViewById(R.id.right_comma);

                String contentStr = getItem(position).getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content.setText(contentStr);
                    tv_content.setVisibility(View.VISIBLE);
                    left_comma.setVisibility(View.VISIBLE);
                    right_comma.setVisibility(View.VISIBLE);
                } else {
                    tv_content.setText("");
                    tv_content.setVisibility(View.GONE);
                    left_comma.setVisibility(View.GONE);
                    right_comma.setVisibility(View.GONE);
                }

                listener.OnSnapViewCreated(convertView);
                return;
            }
        }
    }

    public SYRichTextPhotoModel getItem(int position) {
        return data.getFood().getRichTextLists().get(position - 1);
    }

    private class HeadViewHolder {
        private ImageView iv_cover;
        private ImageView iv_avator;
        private TextView tv_name;
        private TextView tv_title;
        private View ll_level_container;
        private RatingBar rb_level;
        private TextView tv_level;
        private TextView tv_total_price;
        private TextView tv_poeple_num;
        private TextView tv_rest_name;
        private TextView tv_class;
        private View headView;
        private View tv_attention;
        private ImageView iv_is_jing;
        private View eat_info;
        private TextView tv_people_average;

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
            ll_level_container = findViewById(R.id.ll_level_container);
            rb_level = (RatingBar) findViewById(R.id.rb_level);
            tv_level = (TextView) findViewById(R.id.tv_level);
            tv_attention = findViewById(R.id.tv_attention);
            iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
            eat_info = findViewById(R.id.eat_info);
            tv_people_average = (TextView) findViewById(R.id.tv_people_average);
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
                    tv_title.setText(title);
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            activity.getResources().getDimensionPixelSize(R.dimen.font_size_4_2));
                }
            } else {
                tv_title.setVisibility(View.GONE);
            }

            //设置ratingBar高度
            int ratingBarHeight = activity.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rb_level.getLayoutParams();
            params.height = ratingBarHeight;
            rb_level.setLayoutParams(params);

            boolean isShow = false;
            //总价
            if (data.getFood().getTotalPrice() == 0) {
                tv_total_price.setVisibility(View.GONE);
            } else {
                isShow = true;
                tv_total_price.setVisibility(View.VISIBLE);
                FFUtils.setText(tv_total_price, "总价：¥", FFUtils.getSubFloat(data.getFood().getTotalPrice()));
            }

            //餐类
            tv_class.setVisibility(View.GONE);
            if (data.getFood() != null) {
                String foodTypeStr = data.getFood().getFoodTypeString();
                if (!TextUtils.isEmpty(foodTypeStr)) {
                    FFUtils.setText(tv_class, "餐类：", foodTypeStr);
                    tv_class.setVisibility(View.VISIBLE);
                    isShow = true;
                }
            }

            //人数
            tv_poeple_num.setVisibility(View.GONE);
            if (data != null && data.getFood() != null) {
                int foodNumber = data.getFood().getNumberOfPeople();
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
                double total = data.getFood().getTotalPrice();
                int foodNumber = data.getFood().getNumberOfPeople();
                tv_people_average.setText("人均：¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
            }

            //商户
            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name.setText("餐厅：" + data.getFood().getPoi().getTitle());
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            if (isShow) {
                eat_info.setVisibility(View.VISIBLE);
            } else {
                eat_info.setVisibility(View.GONE);
            }

            //是否精华内容
            if (data.isHandPick()) {
                iv_is_jing.setVisibility(View.VISIBLE);
            } else {
                iv_is_jing.setVisibility(View.GONE);
            }
            setKingCrownInfo(headView, data);

            FFImageLoader.load_base((FFContext) activity, data.getFood().pullHeadImage(), iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);
        }

        public View findViewById(int id) {
            return headView.findViewById(id);
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

}
