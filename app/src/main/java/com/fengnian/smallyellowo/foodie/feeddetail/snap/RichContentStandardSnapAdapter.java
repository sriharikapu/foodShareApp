package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

/**
 * Created by Administrator on 2017-1-9.
 */

public class RichContentStandardSnapAdapter extends SnapBaseAdapter {

    public RichContentStandardSnapAdapter(Activity activity, SYFeed data, String url) {
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
                View convertView = activity.getLayoutInflater().inflate(R.layout.snap_dynamic_detial_title, null);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                final View convertView = activity.getLayoutInflater().inflate(R.layout.snap_dynamic_detial_image, null, false);
                DynamicImageView iv_img;
                TextView tv_content;
                TextView tv_dish_name;
                ImageView iv_comment;
                iv_img = (DynamicImageView) convertView.findViewById(R.id.iv_img);
                tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

                if (position == data.getFood().getRichTextLists().size()) {
                    View cover = convertView.findViewById(R.id.fl_cover);
                    cover.setVisibility(View.VISIBLE);
                    cover.setBackgroundResource(R.drawable.snap_lower);
                } else if (position == 1) {
                    View cover = convertView.findViewById(R.id.fl_cover);
                    cover.setVisibility(View.VISIBLE);
                    cover.setBackgroundResource(R.drawable.snap_upper);
                }

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
                int imgWidth = FFUtils.getDisWidth();
                if (img.getWidth() == 0) {
                    iv_img.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    iv_img.setMinimumHeight((int) (imgWidth * 0.6));
//                            iv_img.setWidth(imgWidth);
                } else {
                    iv_img.getLayoutParams().height = (int) (imgWidth * img.getHeight() / img.getWidth());
                    iv_img.setMinimumHeight(0);
//                            iv_img.setWidth(imgWidth);
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
//                        return load_base(activity, imageUrl, imageView, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, null);
                FFUtils.setText(tv_content, getItem(position).getContent());
                FFUtils.setText(tv_dish_name, getItem(position).getDishesName());
                FFImageLoader.load_base((FFContext) activity, img.getUrl(), iv_img, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);

                return;
            }
            case 2: {//文字
                View convertView = activity.getLayoutInflater().inflate(R.layout.snap_dynamic_detial_text, null, false);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                if (position == data.getFood().getRichTextLists().size()) {
                    View cover = convertView.findViewById(R.id.fl_cover);
                    cover.setVisibility(View.VISIBLE);
                    cover.setBackgroundResource(R.drawable.snap_lower);
                } else if (position == 1) {
                    View cover = convertView.findViewById(R.id.fl_cover);
                    cover.setVisibility(View.VISIBLE);
                    cover.setBackgroundResource(R.drawable.snap_upper);
                }
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
        private TextView tv_total_price;
        private TextView tv_poeple_num;
        private TextView tv_rest_name;
        private TextView tv_class;
        private View headView;
        private View line_price;
        private View line_num;
        private View line_class;
        private TextView tv_people_average;

        public void setHeadView(View headView) {
            this.headView = headView;
            iv_cover = (ImageView) findViewById(R.id.iv_cover);
            iv_avator = (ImageView) findViewById(R.id.iv_avator);
            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_rest_name = (TextView) findViewById(R.id.tv_rest_name);
            tv_total_price = (TextView) findViewById(R.id.tv_total_price);
            tv_poeple_num = (TextView) findViewById(R.id.tv_poeple_num);
            tv_class = (TextView) findViewById(R.id.tv_class);
            ll_level_container = findViewById(R.id.ll_level_container);
            rb_level = (RatingBar) findViewById(R.id.rb_level);
            tv_level = (TextView) findViewById(R.id.tv_level);
            tv_people_average = (TextView) findViewById(R.id.tv_people_average);

            line_price = findViewById(R.id.v_line_price);
            line_num = findViewById(R.id.v_line_price);
            line_class = findViewById(R.id.v_line_class);
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
            if (data.getFood().getTotalPrice() == 0) {
                ((View) tv_total_price.getParent()).setVisibility(View.GONE);
                line_price.setVisibility(View.GONE);
            } else {
                FFUtils.setText(tv_total_price, "总价:¥", FFUtils.getSubFloat(data.getFood().getTotalPrice()));
            }
            if (FFUtils.isStringEmpty(data.getFood().getFoodTypeString())) {
                ((View) tv_class.getParent()).setVisibility(View.GONE);
                line_class.setVisibility(View.GONE);
            } else {
                FFUtils.setText(tv_class, "餐类:", data.getFood().getFoodTypeString());
            }
            if (data.getFood().getNumberOfPeople() == 0) {
                ((View) tv_poeple_num.getParent()).setVisibility(View.GONE);
                line_num.setVisibility(View.GONE);
            } else {
                FFUtils.setText(tv_poeple_num, "人数:", data.getFood().getNumberOfPeople());
            }
            if (data.getFood().getTotalPrice() == 0 && (data.getFood().getFoodType() > 6 || data.getFood().getFoodType() < 1) && data.getFood().getNumberOfPeople() == 0) {
                ((View) tv_class.getParent().getParent()).setVisibility(View.GONE);
            } else {
                ((View) tv_class.getParent().getParent()).setVisibility(View.VISIBLE);
            }

            if (null != data.getFood().getPoi() && !FFUtils.isStringEmpty(data.getFood().getPoi().getTitle())) {
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name.setText(data.getFood().getPoi().getTitle());
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            //人均
            tv_people_average.setVisibility(View.GONE);
            if (tv_total_price.getVisibility() == View.VISIBLE && tv_poeple_num.getVisibility() == View.VISIBLE) {
                tv_people_average.setVisibility(View.VISIBLE);
                double total = data.getFood().getTotalPrice();
                int foodNumber = data.getFood().getNumberOfPeople();
                tv_people_average.setText("人均:¥" + ((int) FFUtils.divide(total, foodNumber, 0)));
            }

            setKingCrownInfo(headView,data);
            FFImageLoader.load_base((FFContext) activity, data.getFood().pullHeadImage(), iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);
        }

        public View findViewById(int id) {
            return headView.findViewById(id);
        }
    }

}
