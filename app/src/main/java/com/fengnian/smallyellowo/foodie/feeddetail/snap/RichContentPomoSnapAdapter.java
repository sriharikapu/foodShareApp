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
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.FlowLayout;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.LinkedHashSet;

/**
 * Created by chenglin on 2017-3-3.
 */

public class RichContentPomoSnapAdapter extends SnapBaseAdapter {

    public RichContentPomoSnapAdapter(Activity activity, SYFeed data, String url) {
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
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_style_title, null);
                ((ImageView) convertView.findViewById(R.id.iv_head_bg)).setImageResource(R.drawable.pomo_head);
                convertView.setBackgroundColor(0xFFFCF2E6);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                int j = 0;
                for (int i = 0; i < position; i++) {
                    if (data.getFood().getRichTextLists().get(j).isTextPhotoModel()) {
                        j++;
                        j %= 2;
                    }
                }
                final View convertView = activity.getLayoutInflater().inflate(j == 1 ? R.layout.item_dynamic_detail_pomo_left_image : R.layout.item_dynamic_detail_pomo_right_image, null, false);
                ImageView iv_img;
                TextView tv_content;
                TextView tv_dish_name;
                ImageView iv_comment;
                iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

                iv_img.getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                convertView.findViewById(R.id.iv_img_cover).getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                switch (getItem(position).getPhoto().getImageComment()) {
                    case 0:
                        iv_comment.setVisibility(View.GONE);
                        break;
                    case 1:
                        iv_comment.setVisibility(View.VISIBLE);
                        iv_comment.setImageResource(R.drawable.ei_ic_good);
                        break;
                    case 2:
                        iv_comment.setVisibility(View.VISIBLE);
                        iv_comment.setImageResource(R.drawable.ei_ic_normal);
                        break;
                    case 3:
                        iv_comment.setVisibility(View.VISIBLE);
                        iv_comment.setImageResource(R.drawable.ei_ic_bad);
                        break;
                }

                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                FFImageCallBack lis = new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        listener.OnSnapViewCreated(convertView);
                    }

                    @Override
                    public void onDownLoadProgress(int downloaded, int contentLength) {

                    }
                };
//                        return load_base(activity, imageUrl, imageView, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, null);
                FFUtils.setText(tv_content, getItem(position).getContent());
                FFUtils.setText(tv_dish_name, getItem(position).getDishesName());
                FFImageLoader.load_base((FFContext) activity, img.getUrl(), iv_img, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);

                return;
            }
            case 2: {//文字
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_pomo_text, null, false);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                tv_content.setText(getItem(position).getContent());
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
        private TextView tv_time;
        private View ll_level_container;
        private RatingBar rb_level;
        private TextView tv_level;
        private TextView tv_rest_name;
        private View headView;
        private View tv_attention;
        private ImageView iv_is_jing;
        private FlowLayout eat_info_layout;

        public void setHeadView(View headView) {
            this.headView = headView;
            iv_cover = (ImageView) findViewById(R.id.iv_cover);
            iv_avator = (ImageView) findViewById(R.id.iv_avator);
            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
            ll_level_container = findViewById(R.id.ll_level_container);
            rb_level = (RatingBar) findViewById(R.id.rb_level);
            tv_level = (TextView) findViewById(R.id.tv_level);
            tv_attention = findViewById(R.id.tv_attention);
            iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
            eat_info_layout = (FlowLayout) findViewById(R.id.eat_info_layout);
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
            //TODO zhangfan

            if (data.getUser() != null)
                tv_name.setText(data.getUser().getNickName());
            String title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();

            if (title == null) try {
                title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
            } catch (Exception e) {

            }
            tv_title.setText(title);
            tv_time.setText("时间:" + TimeUtils.getTime("yyyy-MM-dd    HH:mm", data.getTimeStamp()));

            eat_info_layout.removeAllViews();
            int index = 0;
            //餐类
            if (data.getFood() != null) {
                String foodTypeStr = data.getFood().getFoodTypeString();
                if (!TextUtils.isEmpty(foodTypeStr)) {
                    View itemView = View.inflate(activity, R.layout.china_style_title_item, null);
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
            if (data.getFood().getTotalPrice() > 0 && data.getFood() != null) {
                double total = data.getFood().getTotalPrice();
                int foodNumber = data.getFood().getNumberOfPeople();

                View itemView = View.inflate(activity, R.layout.china_style_title_item, null);
                TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                tv_show_title.setText("人均");

                tv_show_content.setText("：¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                eat_info_layout.addView(itemView, params1);
                index++;
            }

            //总价
            if (data.getFood().getTotalPrice() > 0) {
                View itemView = View.inflate(activity, R.layout.china_style_title_item, null);
                TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                tv_show_title.setText("总价");
                FFUtils.setText(tv_show_content, "：¥", FFUtils.getSubFloat(data.getFood().getTotalPrice()));
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                eat_info_layout.addView(itemView, params1);
                index++;
            }

            //人数
            if (data != null && data.getFood() != null) {
                int foodNumber = data.getFood().getNumberOfPeople();
                if (foodNumber > 0) {
                    View itemView = View.inflate(activity, R.layout.china_style_title_item, null);
                    TextView tv_show_title = (TextView) itemView.findViewById(R.id.tv_show_title);
                    TextView tv_show_content = (TextView) itemView.findViewById(R.id.tv_show_content);
                    tv_show_title.setText("人数");
                    FFUtils.setText(tv_show_content, "：", foodNumber + "人");
                    ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(getItemWidth(index), -2);
                    eat_info_layout.addView(itemView, params1);
                    index++;
                }
            }


            if (null != data.getFood().getPoi() && !FFUtils.isStringEmpty(data.getFood().getPoi().getTitle())) {
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name.setText(":" + data.getFood().getPoi().getTitle());
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            //商户
            LinearLayout ll_rest_container = (LinearLayout) findViewById(R.id.ll_rest_container);
            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {//商户名称
                tv_rest_name.setText(":" + data.getFood().getPoi().getTitle());
            } else {
                ll_rest_container.setVisibility(View.GONE);
            }

            //用餐的一些信息
            if (data.getFood().getTotalPrice() == 0 && (data.getFood().getFoodType() > 6
                    || data.getFood().getFoodType() < 1) && data.getFood().getNumberOfPeople() == 0) {
                eat_info_layout.setVisibility(View.GONE);
            } else {
                eat_info_layout.setVisibility(View.VISIBLE);
            }

            //水单
            LinearLayout ll_menu_container = (LinearLayout) findViewById(R.id.ll_menu_container);
            TextView tv_foods = (TextView) findViewById(R.id.tv_foods);
            String dishString = getDishString();
            if (FFUtils.isStringEmpty(dishString)) {
                ll_menu_container.setVisibility(View.GONE);
            } else {
                ll_menu_container.setVisibility(View.VISIBLE);
                tv_foods.setText(":" + dishString);
            }

            //是否精华内容
            if (data.isHandPick()) {
                iv_is_jing.setVisibility(View.VISIBLE);
            } else {
                iv_is_jing.setVisibility(View.GONE);
            }
            setKingCrownInfo(headView, data);
            FFImageLoader.load_base((FFContext) activity, data.getFood().pullHeadImage(), iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);


            if (!TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                int size = 30;
                float textViewLength = FFUtils.getTextViewLength(tv_title, title);
                while (size > 19 && textViewLength > FFUtils.getPx(300)) {
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
                    size--;
                }
            }

            if (tv_time != null) {
                tv_time.setText(TimeUtils.getTime("yyyy-MM-dd    HH:mm", data.getTimeStamp()));
            }
            tv_time.setTextColor(APP.app.getResources().getColor(R.color.china_style_font_color));
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


    private int getItemWidth(final int index) {
        int eatLayoutWidth = DisplayUtil.screenWidth
                - getDimension(R.dimen.padding_middle) * 2
                - getDimension(R.dimen.padding_small) * 3
                - activity.getResources().getDrawable(R.mipmap.detail_eat_icon).getIntrinsicWidth();

        int itemWidth;
        if (DisplayUtil.screenWidth <= 720) {
            itemWidth = DisplayUtil.dip2px(110f);
        } else {
            itemWidth = DisplayUtil.dip2px(120f);
        }

        if (index % 2 == 1) {
            itemWidth = eatLayoutWidth - itemWidth - DisplayUtil.dip2px(8f);
        }
        return itemWidth;
    }

    private int getDimension(int dimension) {
        return activity.getResources().getDimensionPixelSize(dimension);
    }
}
