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
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;
import com.fengnian.smallyellowo.foodie.widgets.VerticalTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglin on 2017-2-27.
 */

public class Brief2_2_StyleAdapter extends BaseDetailAdapter {
    private Brief2_2_StyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;
    private Map<String, Boolean> sortIdMap = new HashMap<String, Boolean>();
    private boolean isLeft = true;

    public Brief2_2_StyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new Brief2_2_StyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_2_style_title, null));
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
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_brief2_2_style_image, parent, false);
                }

                ViewGroup layout_1 = (ViewGroup) convertView.findViewById(R.id.layout_1);
                CircleImageView iv_img_1 = (CircleImageView) convertView.findViewById(R.id.iv_img_1);
                VerticalTextView tv_dish_name_1 = (VerticalTextView) convertView.findViewById(R.id.tv_dish_name_1);
                ImageView iv_comment_1 = (ImageView) convertView.findViewById(R.id.iv_comment_1);
                View line_1 = convertView.findViewById(R.id.line_1);
                TextView tv_content_1 = (TextView) convertView.findViewById(R.id.tv_content_1);

                ViewGroup layout_2 = (ViewGroup) convertView.findViewById(R.id.layout_2);
                CircleImageView iv_img_2 = (CircleImageView) convertView.findViewById(R.id.iv_img_2);
                VerticalTextView tv_dish_name_2 = (VerticalTextView) convertView.findViewById(R.id.tv_dish_name_2);
                ImageView iv_comment_2 = (ImageView) convertView.findViewById(R.id.iv_comment_2);
                View line_2 = convertView.findViewById(R.id.line_2);
                TextView tv_content_2 = (TextView) convertView.findViewById(R.id.tv_content_2);

                CircleImageView iv_img;
                VerticalTextView tv_dish_name;
                ImageView iv_comment;
                View line;
                TextView tv_content;

                boolean isShowLeft = sortIdMap.get(getItem(position).getId());
                if (isShowLeft) {
                    layout_1.setVisibility(View.VISIBLE);
                    layout_2.setVisibility(View.GONE);

                    iv_img = iv_img_1;
                    tv_dish_name = tv_dish_name_1;
                    iv_comment = iv_comment_1;
                    line = line_1;
                    tv_content = tv_content_1;
                } else {
                    layout_1.setVisibility(View.GONE);
                    layout_2.setVisibility(View.VISIBLE);

                    iv_img = iv_img_2;
                    tv_dish_name = tv_dish_name_2;
                    iv_comment = iv_comment_2;
                    line = line_2;
                    tv_content = tv_content_2;
                }

                //评价展示
                int level = getItem(position).getPhoto().getImageComment();
                setCommentData(level, iv_comment, CommentLevel.TYPE_BRIEF_2_2);

                //食物图片展示
                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                iv_img.setRectAdius(5f);

                if (iv_img.getWidth() > 0) {
                    RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
                    imageParams.width = iv_img.getWidth();
                    imageParams.height = iv_img.getWidth();
                    iv_img.setLayoutParams(imageParams);
                }

                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);

                //食物名称
                tv_dish_name.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_4));
                tv_dish_name.setTextColor(activity.getResources().getColor(R.color.title_text_color));
                tv_dish_name.setLineWidth(DisplayUtil.dip2px(16f));
                String dishNameStr = getItem(position).getDishesName();
                if (!TextUtils.isEmpty(dishNameStr)) {
                    tv_dish_name.setVisibility(View.VISIBLE);
                    tv_dish_name.setText(dishNameStr.replace("\n", ""));
                } else {
                    tv_dish_name.setVisibility(View.INVISIBLE);
                }

                RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
                if (!TextUtils.isEmpty(getItem(position).getContent())) {
                    tv_content.setText(getItem(position).getContent());
                    tv_content.setVisibility(View.VISIBLE);
                    imageParams.bottomMargin = 0;
                } else {
                    tv_content.setText("");
                    tv_content.setVisibility(View.GONE);
                    if (position == getMCount() - 1) {
                        imageParams.bottomMargin = 0;
                    } else {
                        imageParams.bottomMargin = DisplayUtil.dip2px(10f);
                    }
                }
                iv_img.setLayoutParams(imageParams);

                //当前viewType的最后一项的时间展示
                ViewGroup itemFoot = (ViewGroup) convertView.findViewById(R.id.item_foot);
                TextView publicTime = (TextView) convertView.findViewById(R.id.public_time);
                int paddingLeft = activity.getResources().getDimensionPixelSize(R.dimen.padding_big);
                itemFoot.setPadding(paddingLeft, DisplayUtil.dip2px(25f), paddingLeft, DisplayUtil.dip2px(40f));

                if (position == getMCount() - 1) {
                    itemFoot.setVisibility(View.VISIBLE);
                    publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", activity.data.getTimeStamp()));
                } else {
                    itemFoot.setVisibility(View.GONE);
                    publicTime.setText("");
                }

                //处理那条线
                if (tv_dish_name.getVisibility() == View.VISIBLE && iv_comment.getVisibility() == View.VISIBLE) {
                    line.setVisibility(View.VISIBLE);
                } else {
                    line.setVisibility(View.INVISIBLE);
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
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_2_text, parent, false);
                }
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                String contentStr = getItem(position).getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content.setText(contentStr);
                    tv_content.setVisibility(View.VISIBLE);
                } else {
                    tv_content.setText("");
                    tv_content.setVisibility(View.GONE);
                }
                return convertView;
            }
        }

    }

    @Override
    public void notifyDataSetChanged() {
        // 这段代码必须要在这里写，因为本地数据和网络数据切换时，拿到的getId是会变的，
        //所以每次都要重新生成一遍 by chenglin 2017年3月7日
        sortIdMap.clear();
        isLeft = true;
        final int count = activity.data.getFood().getRichTextLists().size();
        for (int i = 0; i < count; i++) {
            SYRichTextPhotoModel photoModel = activity.data.getFood().getRichTextLists().get(i);
            if (photoModel.isTextPhotoModel()) {
                sortIdMap.put(photoModel.getId(), isLeft);
                isLeft = !isLeft;
            }
        }

        super.notifyDataSetChanged();
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
