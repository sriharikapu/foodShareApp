package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
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
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;
import com.fengnian.smallyellowo.foodie.zxing.CreateQRImage;

/**
 * Created by Administrator on 2017-1-9.
 *
 */
public class FastContentSnapAdapter extends SnapBaseAdapter {
    public FastContentSnapAdapter(Activity activity, SYFeed data, String url) {
        super(activity, data, url);
    }

    @Override
    public int getCount() {
        return data.getFood().getRichTextLists().size() + 3;
    }


    @Override
    public void getView(int position, final OnSnapViewCreatedListener listener) {
        if (position == 0) {
            initHead(listener);
            return;
        } else if (position == getCount() - 1) {
            final View convertView = activity.getLayoutInflater().inflate(R.layout.snap_content_qrcode, null, false);
            final ImageView iv_qr_code = (ImageView) convertView.findViewById(R.id.iv_qr_code);
            new Thread() {
                @Override
                public void run() {
                    final Bitmap bitmap = CreateQRImage.createQRImage(url);
                    APP.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_qr_code.setImageBitmap(bitmap);
                            listener.OnSnapViewCreated(convertView);
                        }
                    });
                }
            }.start();
            return;
        } else if (position == getCount() - 2) {
            View convertView = activity.getLayoutInflater().inflate(R.layout.snap_dynamic_detial_text, null, false);
            TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            View cover = convertView.findViewById(R.id.fl_cover);
            cover.setVisibility(View.VISIBLE);
            cover.setBackgroundResource(R.drawable.snap_lower);
            tv_content.setText(data.getFood().getContent());
            listener.OnSnapViewCreated(convertView);
            return;
        } else {//图片
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
    }

    public SYRichTextPhotoModel getItem(int position) {
        return data.getFood().getRichTextLists().get(position - 1);
    }

    private void initHead(final OnSnapViewCreatedListener listener) {
        final View view = activity.getLayoutInflater().inflate(R.layout.snap_fast_title, null);

        ImageView iv_avator = (ImageView) view.findViewById(R.id.iv_avator);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        View ll_level_container = view.findViewById(R.id.ll_level_container);
        RatingBar rb_level = (RatingBar) view.findViewById(R.id.rb_level);
        TextView tv_level = (TextView) view.findViewById(R.id.tv_level);
        TextView tv_rest_name = (TextView) view.findViewById(R.id.tv_rest_name);

        if (data.getUser() != null) {
            tv_name.setText(data.getUser().getNickName());
        }
        tv_time.setText("时间:" + TimeUtils.getTime("yyyy-MM-dd    HH:mm", data.getTimeStamp()));

        if (data.getFood().getPoi() == null || TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setVisibility(View.GONE);
        } else {
            tv_rest_name.setVisibility(View.VISIBLE);
            tv_rest_name.setText(data.getFood().getPoi().getTitle());
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
                listener.OnSnapViewCreated(view);
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        };
        FFImageLoader.load_base((FFContext) activity, data.getUser().getHeadImage().getUrl(), iv_avator, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, FFImageLoader.TYPE_ROUND, lis);

    }
}
