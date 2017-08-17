package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.VerticalTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglin on 2017-2-27.
 */

public class China2StyleAdapter extends BaseDetailAdapter {
    private China2StyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;
    private Map<String, Boolean> sortIdMap = new HashMap<String, Boolean>();
    private boolean isLeft = true;

    public China2StyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new China2StyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_2_style_title, null));

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
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_china_2_style_image, parent, false);
                }

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
                setCommentData(commentLevel, iv_comment, CommentLevel.TYPE_STANDARD);

                //食物图片展示
                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                int imageSize = activity.getResources().getDimensionPixelSize(R.dimen.china2_Image_size);
                fixedImageViewSize(iv_img, position);
                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);

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

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toPicturePreview(img);
                    }
                });

                //食物描述
                if (DisplayUtil.screenWidth >= 720) {
                    tv_content.setLineWidth(DisplayUtil.dip2px(12));
                    tv_content.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_2));
                } else {
                    tv_content.setLineWidth(activity.getResources().getDimensionPixelSize(R.dimen.vertical_china2_textview_foodname_width));
                    tv_content.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_1_1));
                }

                LinearLayout.LayoutParams tvContentParams = (LinearLayout.LayoutParams) tv_content.getLayoutParams();
                tvContentParams.height = imageSize - DisplayUtil.dip2px(28f);
//                tvContentParams.width = getDimension(R.dimen.detail_china_2_left_margin);
                tv_content.setLayoutParams(tvContentParams);
                tv_content.setTextHeight(tvContentParams.height);
                tv_content.setTextColor(activity.getResources().getColor(R.color.china_style_font_color_gray));

                String contentStr = getItem(position).getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content_linear.setVisibility(View.VISIBLE);
                    tv_content.setText(contentStr.replace("\n", ""));
                } else {
                    tv_content.setText("");
                    tv_content_linear.setVisibility(View.INVISIBLE);
                }

                //右边的那个食物的文字描述不可见时，要做的一些操作
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tv_content_linear.getLayoutParams();
//                int imageWidth = getDimension(R.dimen.china2_Image_size);
                if (tv_content_linear.getVisibility() == View.VISIBLE) {
                    int padding = DisplayUtil.dip2px(10f);
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
                if (position == getMCount() - 1) {
                    convertView.setPadding(0, padding2, 0, DisplayUtil.dip2px(30f));
                } else {
                    if (position >= 1) {
                        if (getMItemViewType(position - 1) == VIEW_TYPE_TEXT) {
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

                return convertView;
            }
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_2_text, parent, false);
                }
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());

                return convertView;
            }
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

    private int getDimension(int dimen) {
        return activity.getResources().getDimensionPixelSize(dimen);
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
        int height = activity.getResources().getDimensionPixelSize(R.dimen.detail_cover_height) - DisplayUtil.dip2px(110);
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
