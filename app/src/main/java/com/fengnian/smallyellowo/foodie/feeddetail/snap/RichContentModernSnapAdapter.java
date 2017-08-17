package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.fengnian.smallyellowo.foodie.feeddetail.detail.DetailAdapterUtils;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.ModernStyleAdapter;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglin on 2017-3-3.
 */

public class RichContentModernSnapAdapter extends SnapBaseAdapter {
    public static final int VIEW_TYPE_HEAD = 0;
    public static final int VIEW_TYPE_IMAGE = 1;
    public static final int VIEW_TYPE_TEXT = 2;
    private final int ITEM_VIEW_IMAGE_LEFT_RIGHT = 1;
    private final int ITEM_VIEW_IMAGE_TOP = 2;
    private Map<String, ModernStyleAdapter.ItemViewParams> sortIdMap = new HashMap<String, ModernStyleAdapter.ItemViewParams>();
    private boolean isLeft = true;

    public RichContentModernSnapAdapter(Activity activity, SYFeed data, String url) {
        super(activity, data, url);

        sortIdMap.clear();
        isLeft = true;
        final int count = data.getFood().getRichTextLists().size();
        int photoIndex = 0;
        for (int index = 0; index < count; index++) {
            SYRichTextPhotoModel photoModel = data.getFood().getRichTextLists().get(index);
            if (photoModel.isTextPhotoModel()) {
                photoIndex++;
                ModernStyleAdapter.ItemViewParams itemViewParams = new ModernStyleAdapter.ItemViewParams();
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
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0://头
            {
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_modern_style_title, null);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                final View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_modern_style_image, null, false);
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

                ModernStyleAdapter.ItemViewParams itemViewParams = sortIdMap.get(photoModel.getId());
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
                BaseDetailAdapter.setCommentData(commentLevel, iv_comment, BaseDetailAdapter.CommentLevel.TYPE_STANDARD);

                //食物图片展示
                final SYImage img = photoModel.getPhoto().getImageAsset().pullProcessedImage().getImage();

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

                //食物描述
                String contentStr = photoModel.getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content.setText(contentStr);
                } else {
                    tv_content.setText("");
                }

                //当前viewType的最后一项的时间展示
                setFootTime(position, itemFoot, getItemViewType(position));

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
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_modern_text, null, false);
                View itemFoot = convertView.findViewById(R.id.item_foot);

                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());

                //当前viewType的最后一项的时间展示
                setFootTime(position, itemFoot, viewType);
                listener.OnSnapViewCreated(convertView);
                return;
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

        if (position == data.getFood().getRichTextLists().size()) {
            itemFoot.setVisibility(View.VISIBLE);
            itemFoot.setPadding(DisplayUtil.dip2px(20f), timeMargeTop, DisplayUtil.dip2px(20f), DisplayUtil.dip2px(12f));
            publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", data.getTimeStamp()));
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

    public SYRichTextPhotoModel getItem(int position) {
        return data.getFood().getRichTextLists().get(position - 1);
    }

    private class HeadViewHolder {
        private ImageView iv_cover;
        private ImageView iv_avator;
        private TextView tv_name;
        private TextView tv_title;
        private View ll_level_container;
        private CustomRatingBar rb_level_custom;
        private TextView tv_level;
        private TextView tv_total_price;
        private TextView tv_poeple_num;
        private TextView tv_rest_name;
        private TextView tv_class;
        private View headView;
        private View tv_attention;
        private ImageView iv_is_jing;
        private View cover_frame;

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
            rb_level_custom = (CustomRatingBar) findViewById(R.id.rb_level_custom);
            tv_level = (TextView) findViewById(R.id.tv_level);
            tv_attention = findViewById(R.id.tv_attention);
            iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
            cover_frame = findViewById(R.id.cover_frame);
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

            if (data.getUser() != null)
                tv_name.setText(data.getUser().getNickName());
            String title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();

            if (title == null) try {
                title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
            } catch (Exception e) {

            }

            //字数大于18个字的时候，把标题的文字变小一些
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_level_container.getLayoutParams();
            tv_title.setText(title);
            if (!TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.VISIBLE);
                params.topMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_small);
                if (title.length() >= 12 && title.length() <= 15) {
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            activity.getResources().getDimensionPixelSize(R.dimen.font_size_6_1));
                }else if (title.length() > 15){
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            activity.getResources().getDimensionPixelSize(R.dimen.font_size_5));
                }
            } else {
                tv_title.setVisibility(View.GONE);
                params.topMargin = DisplayUtil.dip2px(38f);
            }
            ll_level_container.setLayoutParams(params);

            //设置ratingBar评分
            int ratingLevel = data.getStarLevel();
            rb_level_custom.setLevel(ratingLevel);
            rb_level_custom.setStarIcon(R.drawable.star_modern_rating_solid, R.drawable.star_modern_rating_empty);

            //总价
            if (data.getFood().getTotalPrice() == 0) {
                tv_total_price.setVisibility(View.GONE);
            } else {
                tv_total_price.setVisibility(View.VISIBLE);
                String totalPriceStr = FFUtils.getSubFloat(data.getFood().getTotalPrice());
                tv_total_price.setText("总价：¥" + totalPriceStr);
            }

            //餐类
            tv_class.setVisibility(View.GONE);
            if (data.getFood() != null) {
                String foodTypeStr = data.getFood().getFoodTypeString();
                if (!TextUtils.isEmpty(foodTypeStr)) {
                    tv_class.setText("餐类：" + foodTypeStr);
                    tv_class.setVisibility(View.VISIBLE);
                }
            }

            //人数
            tv_poeple_num.setVisibility(View.GONE);
            if (data != null && data.getFood() != null) {
                int foodNumber = data.getFood().getNumberOfPeople();
                if (foodNumber > 0) {
                    tv_poeple_num.setText("人数：" + foodNumber + "人");
                    tv_poeple_num.setVisibility(View.VISIBLE);
                }
            }

            //商户
            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {//商户名称
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name.setText("餐厅：" + data.getFood().getPoi().getTitle());
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            //如果没有商户，那么距离下方的间距需要发生变化
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) cover_frame.getLayoutParams();
            if (tv_rest_name.getVisibility() == View.VISIBLE) {
                params1.bottomMargin = 0;
            } else {
                params1.bottomMargin = DisplayUtil.dip2px(15f);
            }
            cover_frame.setLayoutParams(params1);

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

    }

}
