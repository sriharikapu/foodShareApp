package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bigpicture.BitPictureIntent;
import com.fengnian.smallyellowo.foodie.bigpicture.ChatBigPictureActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-2-21.
 */

public class StandardAdapter extends BaseDetailAdapter {

    private StandardHeadViewHolder headView;

    public StandardAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new StandardHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_title, null));
    }

    @Override
    public BaseHeadViewHolder getHeadView() {
        return headView;
    }
//
//    @Override
//    public StandardHeadViewHolder getHeadView() {
//        return headView;
//    }

    @Override
    public int getMCount() {
        return activity.data.getFood().getRichTextLists().size() + 1;
    }

    @Override
    public int getMViewTypeCount() {
        return 3;
    }

    @Override
    public int getMItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (activity.data.getFood().getRichTextLists().get(position - 1).isTextPhotoModel()) {
            return 1;
        }
        return 2;
    }

    @Override
    public SYRichTextPhotoModel getItem(int position) {
        if (position == 0) {
            return null;
        }
        return activity.data.getFood().getRichTextLists().get(position - 1);
    }

    @Override
    public View getMView(int position, View convertView, ViewGroup parent) {
        switch (getMItemViewType(position)) {
            case 0://头
                return headView.getHeadView();
            case 1: {//图片
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_image, parent, false);
                }
                DynamicImageView iv_img;
                TextView tv_content;
                TextView tv_dish_name;
                ImageView iv_comment;
                iv_img = (DynamicImageView) convertView.findViewById(R.id.iv_img);
                tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

                int level = getItem(position).getPhoto().getImageComment();
                setCommentData(level,iv_comment,CommentLevel.TYPE_STANDARD);

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
                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);
//                        return load_base(activity, imageUrl, imageView, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, null);
                FFUtils.setText(tv_content, getItem(position).getContent());
                FFUtils.setText(tv_dish_name, getItem(position).getDishesName());

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toPicturePreview(img);
                    }
                });
                return convertView;
            }
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_text, parent, false);
                }
                TextView tv_content;
                tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());
                return convertView;
            }
        }
    }

}
