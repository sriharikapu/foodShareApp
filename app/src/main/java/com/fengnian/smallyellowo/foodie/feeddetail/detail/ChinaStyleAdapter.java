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

/**
 * Created by chenglin on 2017-2-24.
 */

public class ChinaStyleAdapter extends BaseDetailAdapter {
    private ChinaStyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;

    public ChinaStyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new ChinaStyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_style_title, null));
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
    public View getMView(int position, View convertView, ViewGroup parent) {
        switch (getMItemViewType(position)) {
            case VIEW_TYPE_HEAD://头
                return headView.getHeadView();
            case VIEW_TYPE_IMAGE: {//图片
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_china_style_image, parent, false);
                }

                ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                TextView tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
                ImageView iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

                int level = getItem(position).getPhoto().getImageComment();
                setCommentData(level, iv_comment, CommentLevel.TYPE_STANDARD);

                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();

//                iv_img.setPadding(DisplayUtil.dip2px(20),0,DisplayUtil.dip2px(20),0);
                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);
                FFUtils.setText(tv_content, getItem(position).getContent());

                tv_dish_name.getPaint().setFakeBoldText(true);
                String dishNameStr = getItem(position).getDishesName();
                if (!TextUtils.isEmpty(dishNameStr)) {
                    tv_dish_name.setText(dishNameStr);
                    tv_dish_name.setVisibility(View.VISIBLE);
                    if (dishNameStr.length() > 17) {
                        tv_dish_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                activity.getResources().getDimensionPixelSize(R.dimen.font_size_3));
                    } else {
                        tv_dish_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                activity.getResources().getDimensionPixelSize(R.dimen.font_size_3_2));
                    }

                    if (dishNameStr.length() <= 2) {
                        tv_dish_name.setBackgroundResource(R.drawable.detail_eat_title_small);
                    } else if (dishNameStr.length() > 3 && dishNameStr.length() < 10) {
                        tv_dish_name.setBackgroundResource(R.drawable.detail_eat_title_normal);
                    } else {
                        tv_dish_name.setBackgroundResource(R.drawable.detail_eat_title_big);
                    }
                } else {
                    tv_dish_name.setVisibility(View.GONE);
                }

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toPicturePreview(img);
                    }
                });

                //判断viewType的不同position的项，要不会出现位置很丑的情况
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
                int bitmapWidth = DetailAdapterUtils.getImageWidth(getItem(position));
                int bitmapHeight = DetailAdapterUtils.getImageHeight(getItem(position));

                int imgWidth = FFUtils.getDisWidth() - activity.getResources().getDimensionPixelSize(R.dimen.padding_middle) * 2;
                params.height = (int) (1f * imgWidth * bitmapHeight / bitmapWidth);

                if (position == getMCount() - 1) {
                    params.bottomMargin = DisplayUtil.dip2px(20f);
                } else {
                    params.bottomMargin = 0;
                }
                iv_img.setLayoutParams(params);

                return convertView;
            }
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_china_text, parent, false);
                }
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());

                //判断viewType的不同position的项，要不会出现位置很丑的情况
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_content.getLayoutParams();
                if (position == getMCount() - 1) {
                    params.bottomMargin = DisplayUtil.dip2px(20f);
                } else {
                    params.bottomMargin = 0;
                }
                tv_content.setLayoutParams(params);
                return convertView;
            }
        }

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
