package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.text.TextUtils;
import android.util.TypedValue;
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
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglin on 2017-2-27.
 */

public class ModernStyleAdapter extends BaseDetailAdapter {
    private ModernStyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;
    private final int ITEM_VIEW_IMAGE_LEFT_RIGHT = 1;
    private final int ITEM_VIEW_IMAGE_TOP = 2;
    private Map<String, ItemViewParams> sortIdMap = new HashMap<String, ItemViewParams>();
    private boolean isLeft = true;

    public ModernStyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new ModernStyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_modern_style_title, null));

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
        int viewType = getMItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_HEAD://头
                return headView.getHeadView();
            case VIEW_TYPE_IMAGE: {//图片
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_modern_style_image, parent, false);
                }

                View itemFoot = convertView.findViewById(R.id.item_foot);

                ViewGroup layout_1 = (ViewGroup) convertView.findViewById(R.id.layout_1);
                DynamicImageView iv_img_1 = (DynamicImageView) convertView.findViewById(R.id.iv_img_1);
                TextView tv_dish_name_1 = (TextView) convertView.findViewById(R.id.tv_dish_name_1);
                TextView tv_content_1 = (TextView) convertView.findViewById(R.id.tv_content_1);
                ImageView iv_comment_1 = (ImageView) convertView.findViewById(R.id.iv_comment_1);
                TextView image_number_1 = (TextView) convertView.findViewById(R.id.image_number_1);

                ViewGroup layout_2 = (ViewGroup) convertView.findViewById(R.id.layout_2);
                DynamicImageView iv_img_2 = (DynamicImageView) convertView.findViewById(R.id.iv_img_2);
                TextView tv_dish_name_2 = (TextView) convertView.findViewById(R.id.tv_dish_name_2);
                TextView tv_content_2 = (TextView) convertView.findViewById(R.id.tv_content_2);
                ImageView iv_comment_2 = (ImageView) convertView.findViewById(R.id.iv_comment_2);
                TextView image_number_2 = (TextView) convertView.findViewById(R.id.image_number_2);

                ViewGroup layout_3 = (ViewGroup) convertView.findViewById(R.id.layout_3);
                DynamicImageView iv_img_3 = (DynamicImageView) convertView.findViewById(R.id.iv_img_3);
                TextView tv_dish_name_3 = (TextView) convertView.findViewById(R.id.tv_dish_name_3);
                TextView tv_content_3 = (TextView) convertView.findViewById(R.id.tv_content_3);
                ImageView iv_comment_3 = (ImageView) convertView.findViewById(R.id.iv_comment_3);
                TextView image_number_3 = (TextView) convertView.findViewById(R.id.image_number_3);

                ViewGroup layout_4 = (ViewGroup) convertView.findViewById(R.id.layout_4);
                DynamicImageView iv_img_4 = (DynamicImageView) convertView.findViewById(R.id.iv_img_4);
                TextView tv_dish_name_4 = (TextView) convertView.findViewById(R.id.tv_dish_name_4);
                TextView tv_content_4 = (TextView) convertView.findViewById(R.id.tv_content_4);
                ImageView iv_comment_4 = (ImageView) convertView.findViewById(R.id.iv_comment_4);
                TextView image_number_4 = (TextView) convertView.findViewById(R.id.image_number_4);

                DynamicImageView iv_img;
                TextView tv_content;
                TextView tv_dish_name;
                ImageView iv_comment;
                TextView image_number;

                final SYRichTextPhotoModel photoModel = getItem(position);

                //这块逻辑略复杂，不写注释了，因为注释已经不足以解释现代模板的千变万化，
                //真是佩服设计师和产品的脑洞大开  by chenglin 2017年3月8日21:32:05
                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.GONE);
                layout_3.setVisibility(View.GONE);
                layout_4.setVisibility(View.GONE);

                ItemViewParams itemViewParams = sortIdMap.get(photoModel.getId());
                if (itemViewParams.isLeft) {
                    if (getItemViewType(photoModel) == ITEM_VIEW_IMAGE_LEFT_RIGHT) {
                        layout_1.setVisibility(View.VISIBLE);
                        iv_img = iv_img_1;
                        tv_dish_name = tv_dish_name_1;
                        iv_comment = iv_comment_1;
                        tv_content = tv_content_1;
                        image_number = image_number_1;
                        setLeftRightTypeImageHeight(iv_img);
                    } else {
                        layout_3.setVisibility(View.VISIBLE);
                        iv_img = iv_img_3;
                        tv_dish_name = tv_dish_name_3;
                        iv_comment = iv_comment_3;
                        tv_content = tv_content_3;
                        image_number = image_number_3;
                        setTopTypeImageHeight(iv_img);
                    }
                } else {
                    if (getItemViewType(photoModel) == ITEM_VIEW_IMAGE_LEFT_RIGHT) {
                        layout_2.setVisibility(View.VISIBLE);
                        iv_img = iv_img_2;
                        tv_dish_name = tv_dish_name_2;
                        iv_comment = iv_comment_2;
                        tv_content = tv_content_2;
                        image_number = image_number_2;
                        setLeftRightTypeImageHeight(iv_img);
                    } else {
                        layout_4.setVisibility(View.VISIBLE);
                        iv_img = iv_img_4;
                        tv_dish_name = tv_dish_name_4;
                        iv_comment = iv_comment_4;
                        tv_content = tv_content_4;
                        image_number = image_number_4;
                        setTopTypeImageHeight(iv_img);
                    }
                }

                //序号
                image_number.setText(itemViewParams.index);

                //评价展示
                int commentLevel = photoModel.getPhoto().getImageComment();
                setCommentData(commentLevel, iv_comment, CommentLevel.TYPE_STANDARD);

                //食物图片展示
                final SYImage img = photoModel.getPhoto().getImageAsset().pullProcessedImage().getImage();
                FFImageLoader.loadBigImage(activity, img.getUrl(), iv_img);

                //食物名称
//                tv_dish_name.setTextColor(activity.getResources().getColor(R.color.title_text_color));
                tv_dish_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        activity.getResources().getDimensionPixelSize(R.dimen.font_size_2));
                String dishNameStr = photoModel.getDishesName();
                if (!TextUtils.isEmpty(dishNameStr)) {
                    tv_dish_name.setVisibility(View.VISIBLE);
                    tv_dish_name.setText(dishNameStr);
                    if (DisplayUtil.screenWidth <= 720 && dishNameStr.length() > 18) {
                        tv_dish_name.setTextSize(10);
                    }
                } else {
                    tv_dish_name.setVisibility(View.INVISIBLE);
                }

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toPicturePreview(img);
                    }
                });

                //食物描述
                String contentStr = photoModel.getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content.setText(contentStr);
                } else {
                    tv_content.setText("");
                }

                //当前viewType的最后一项的时间展示
                setFootTime(position, itemFoot, viewType);

                return convertView;
            }
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_modern_text, parent, false);
                }

                View itemFoot = convertView.findViewById(R.id.item_foot);

                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());

                //当前viewType的最后一项的时间展示
                setFootTime(position, itemFoot, viewType);

                return convertView;
            }
        }

    }

    //当前viewType的最后一项的时间展示
    private void setFootTime(int position, View itemFoot, int type) {
        TextView publicTime = (TextView) itemFoot.findViewById(R.id.public_time);

        int timeMargeTop = DisplayUtil.dip2px(16f);
        if (type == VIEW_TYPE_IMAGE) {
            timeMargeTop = DisplayUtil.dip2px(25f);
        }

        if (position == getMCount() - 1) {
            itemFoot.setVisibility(View.VISIBLE);
            itemFoot.setPadding(DisplayUtil.dip2px(20f), timeMargeTop, DisplayUtil.dip2px(20f), DisplayUtil.dip2px(12f));
            publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", activity.data.getTimeStamp()));
        } else {
            itemFoot.setVisibility(View.GONE);
            publicTime.setText("");
        }
    }

    /**
     * 1、若图⽚宽⾼⽐⼩于等于1（宽⽐⾼⼩或⽅形），则图靠左时⽂在右，图靠右时⽂在左；
     * 2、若图⽚宽⾼⽐⼤于1（宽⽐⾼⼤），则图在上，⽂在下排布
     */
    private int getItemViewType(SYRichTextPhotoModel photoModel) {
        int bitmapWidth = DetailAdapterUtils.getImageWidth(photoModel);
        int bitmapHeight = DetailAdapterUtils.getImageHeight(photoModel);

        if (bitmapWidth * 1f / bitmapHeight * 1f <= 1) {
            return ITEM_VIEW_IMAGE_LEFT_RIGHT;
        } else {
            return ITEM_VIEW_IMAGE_TOP;
        }
    }

    /**
     * 当itemView的类型为ITEM_VIEW_IMAGE_LEFT_RIGHT 时，动态设置高度
     */
    private void setLeftRightTypeImageHeight(DynamicImageView imageView) {
        int imageViewWidth = DisplayUtil.screenWidth - activity.getResources().getDimensionPixelSize(R.dimen.detail_modern_text_width)
                - activity.getResources().getDimensionPixelSize(R.dimen.padding_18) * 2;
        imageView.setWidth(imageViewWidth);
    }

    /**
     * 当itemView的类型为ITEM_VIEW_IMAGE_TOP 时，动态设置高度
     */
    private void setTopTypeImageHeight(DynamicImageView imageView) {
        int imageViewWidth = DisplayUtil.screenWidth - activity.getResources().getDimensionPixelSize(R.dimen.detail_modern_top_type_image_margin);
        imageView.setWidth(imageViewWidth);
    }

    @Override
    public void notifyDataSetChanged() {
        // 这段代码必须要在这里写，因为本地数据和网络数据切换时，拿到的getId是会变的，
        //所以每次都要重新生成一遍 by chenglin 2017年3月7日
        sortIdMap.clear();
        isLeft = true;
        final int count = activity.data.getFood().getRichTextLists().size();
        int photoIndex = 0;
        for (int index = 0; index < count; index++) {
            SYRichTextPhotoModel photoModel = activity.data.getFood().getRichTextLists().get(index);
            if (photoModel.isTextPhotoModel()) {
                photoIndex++;
                ItemViewParams itemViewParams = new ItemViewParams();
                itemViewParams.isLeft = isLeft;
                String str = photoIndex + "";
                if (photoIndex < 10) {
                    str = "0" + photoIndex;
                }
                itemViewParams.index = str;
                sortIdMap.put(photoModel.getId(), itemViewParams);
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

    public static final class ItemViewParams {
        public boolean isLeft;
        public String index;
    }
}
