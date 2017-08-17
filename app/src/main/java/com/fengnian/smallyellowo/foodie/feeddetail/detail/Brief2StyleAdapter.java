package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bigpicture.BitPictureIntent;
import com.fengnian.smallyellowo.foodie.bigpicture.ChatBigPictureActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.ArrayList;

/**
 * Created by chenglin on 2017-2-27.
 */

public class Brief2StyleAdapter extends BaseDetailAdapter {
    private Brief2StyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;

    public Brief2StyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new Brief2StyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_style_title, null));
    }


    @Override
    public BaseHeadViewHolder getHeadView() {
        return headView;
    }


    @Override
    public SYRichTextPhotoModel getItem(int position) {
        if (position == 0) {
            return null;
        }
        return activity.data.getFood().getRichTextLists().get(position - 1);
    }

    @Override
    public View getMView(final int position, View convertView, ViewGroup parent) {
        switch (getMItemViewType(position)) {
            case VIEW_TYPE_HEAD://头
                return headView.getHeadView();
            case VIEW_TYPE_IMAGE: {//图片
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_brief2_style_image, parent, false);
                }

                DynamicImageView iv_img = (DynamicImageView) convertView.findViewById(R.id.iv_img);
                TextView tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                ImageView iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
                View itemFoot = convertView.findViewById(R.id.item_foot);
                TextView publicTime = (TextView) convertView.findViewById(R.id.public_time);
                View line_1 = convertView.findViewById(R.id.line_1);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                int level = getItem(position).getPhoto().getImageComment();
                setCommentData(level,iv_comment,CommentLevel.TYPE_STANDARD);

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
                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);

                FFUtils.setText(tv_dish_name, getItem(position).getDishesName());
                if (tv_dish_name.getVisibility() == View.VISIBLE){
                    line_1.setVisibility(View.VISIBLE);
                }else {
                    line_1.setVisibility(View.GONE);
                }

                FFUtils.setText(tv_content, getItem(position).getContent());

                //当前viewType的最后一项
                if (position == getMCount() - 1) {
                    itemFoot.setVisibility(View.VISIBLE);
                    publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", activity.data.getTimeStamp()));
                } else {
                    itemFoot.setVisibility(View.GONE);
                    publicTime.setText("");
                }

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
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_text, parent, false);
                }
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
                return convertView;
            }
        }

    }

    @Override
    public int getMAttentionBottomY() {
        int height = headView.getHeadView().getHeight() - DisplayUtil.dip2px(55);
        return height;
    }

    @Override
    public int getMCount() {
        return activity.data.getFood().getRichTextLists().size() + 1;
    }

    @Override
    public int getMViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getMItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEAD;
        }
        if (activity.data.getFood().getRichTextLists().get(position - 1).isTextPhotoModel()) {
            return VIEW_TYPE_IMAGE;
        }
        return VIEW_TYPE_TEXT;
    }
}
