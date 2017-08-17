package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
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
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;
import com.fengnian.smallyellowo.foodie.widgets.VerticalTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglin on 2017-3-6.
 */

public class RichContentBrief2_2_SnapAdapter extends SnapBaseAdapter {
    private Map<String, Boolean> sortIdMap = new HashMap<String, Boolean>();
    private boolean isLeft = true;

    public RichContentBrief2_2_SnapAdapter(Activity activity, SYFeed data, String url) {
        super(activity, data, url);

        sortIdMap.clear();
        isLeft = true;
        final int count = data.getFood().getRichTextLists().size();
        for (int i = 0; i < count; i++) {
            SYRichTextPhotoModel photoModel = data.getFood().getRichTextLists().get(i);
            if (photoModel.isTextPhotoModel()) {
                sortIdMap.put(photoModel.getId(), isLeft);
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
        switch (getItemViewType(position)) {
            case 0://头
            {
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_2_style_title, null);
                HeadViewHolder headView = new HeadViewHolder();
                headView.setHeadView(convertView);
                headView.refresh(listener);
            }
            return;
            case 1: {//图片
                final View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detial_brief2_2_style_image, null, false);
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
                BaseDetailAdapter.setCommentData(level, iv_comment, BaseDetailAdapter.CommentLevel.TYPE_BRIEF_2_2);

                //食物图片展示
                final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
                iv_img.setRectAdius(5f);

                if (iv_img.getWidth() > 0) {
                    RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
                    imageParams.width = iv_img.getWidth();
                    imageParams.height = iv_img.getWidth();
                    iv_img.setLayoutParams(imageParams);
                }

                //食物名称
                tv_dish_name.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_4));
                tv_dish_name.setTextColor(activity.getResources().getColor(R.color.title_text_color));
                tv_dish_name.setLineWidth(DisplayUtil.dip2px(16f));
                String dishNameStr = getItem(position).getDishesName();
                if (!TextUtils.isEmpty(dishNameStr)) {
                    tv_dish_name.setVisibility(View.VISIBLE);
                    tv_dish_name.setText(dishNameStr.replace("\n",""));
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
                    if (position == data.getFood().getRichTextLists().size()) {
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

                if (position == data.getFood().getRichTextLists().size()) {
                    itemFoot.setVisibility(View.VISIBLE);
                    publicTime.setText(TimeUtils.getTime("yyyy/MM/dd  HH:mm", data.getTimeStamp()));
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
                View convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief2_2_text, null, false);
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);

                String contentStr = getItem(position).getContent();
                if (!TextUtils.isEmpty(contentStr)) {
                    tv_content.setText(contentStr);
                    tv_content.setVisibility(View.VISIBLE);
                } else {
                    tv_content.setText("");
                    tv_content.setVisibility(View.GONE);
                }
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
        private VerticalTextView tv_title;
        private VerticalTextView tv_level;
        private VerticalTextView tv_total_price;
        private VerticalTextView tv_poeple_num;
        private VerticalTextView tv_rest_name;
        private VerticalTextView tv_class;
        private View headView;
        private View tv_attention;
        private ImageView iv_is_jing;
        private View title_line;

        public void setHeadView(View headView) {
            this.headView = headView;
            iv_cover = (ImageView) findViewById(R.id.iv_cover);
            iv_avator = (ImageView) findViewById(R.id.iv_avator);
            tv_name = (TextView) findViewById(R.id.tv_name);
            tv_title = (VerticalTextView) findViewById(R.id.tv_title_2);
            tv_rest_name = (VerticalTextView) findViewById(R.id.tv_rest_name_2);
            tv_total_price = (VerticalTextView) findViewById(R.id.tv_total_price_2);
            tv_poeple_num = (VerticalTextView) findViewById(R.id.tv_poeple_num_2);
            tv_class = (VerticalTextView) findViewById(R.id.tv_class_2);
            tv_level = (VerticalTextView) findViewById(R.id.tv_level_2);
            tv_attention = findViewById(R.id.tv_attention);
            iv_is_jing = (ImageView) findViewById(R.id.iv_is_jing);
            title_line = findViewById(R.id.title_line);
        }


        public void refresh(final OnSnapViewCreatedListener listener) {

            final SnapItemInfo info = new SnapItemInfo();
            info.count = 2;
            if (data == null) {
                return;
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

            if (data.getUser() != null) {
                tv_name.setText(data.getUser().getNickName());
            }

            //字数大于18个字的时候，把标题的文字变小一些
            String titleStr = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
            tv_title.setTextColor(getActivity().getResources().getColor(R.color.title_text_color));
            tv_title.setText(titleStr);
            tv_title.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(titleStr)) {
                if (titleStr.length() >= 18) {
                    tv_title.setLineWidth(DisplayUtil.dip2px(14f));
                    tv_title.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_2));
                } else {
                    tv_title.setLineWidth(DisplayUtil.dip2px(17f));
                    tv_title.setTextSize(getActivity().getResources().getDimensionPixelSize(R.dimen.font_size_5));
                }
            } else {
                tv_title.setVisibility(View.INVISIBLE);
            }

            //设置ratingBar
            VerticalTextView ratingTitle = (VerticalTextView) findViewById(R.id.vertical_rating_bar_title);
            CustomRatingBar verticalRatingBar = (CustomRatingBar) findViewById(R.id.vertical_rating_bar);
            int starLevel = data.getStarLevel();
            if (starLevel > 0) {
                initVerticalTextViewParams(ratingTitle);
                ratingTitle.setText("评分：");
                verticalRatingBar.setLevel(starLevel);
                initVerticalTextViewParams(tv_level);

                tv_level.setTextColor(getActivity().getResources().getColor(R.color.brief2_2_style_level_desc));
                tv_level.setText(data.pullStartLevelString());

                ratingTitle.setVisibility(View.VISIBLE);
                verticalRatingBar.setVisibility(View.VISIBLE);
                tv_level.setVisibility(View.VISIBLE);
            } else {
                ratingTitle.setVisibility(View.GONE);
                verticalRatingBar.setVisibility(View.GONE);
                tv_level.setVisibility(View.GONE);
            }

            //总价
            initVerticalTextViewParams(tv_total_price);
            if (data.getFood().getTotalPrice() == 0) {
                tv_total_price.setVisibility(View.GONE);
            } else {
                tv_total_price.setVisibility(View.VISIBLE);
                tv_total_price.setText("总价：¥" + FFUtils.getSubFloat(data.getFood().getTotalPrice()));
            }

            //餐类
            initVerticalTextViewParams(tv_class);
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
            initVerticalTextViewParams(tv_poeple_num);
            if (data != null && data.getFood() != null) {
                int foodNumber = data.getFood().getNumberOfPeople();
                if (foodNumber > 0) {
                    tv_poeple_num.setText("人数：" + foodNumber + "人");
                    tv_poeple_num.setVisibility(View.VISIBLE);
                }
            }

            //餐厅
            initVerticalTextViewParams(tv_rest_name);
            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {
                tv_rest_name.setVisibility(View.VISIBLE);
                tv_rest_name.setText("餐厅：" + data.getFood().getPoi().getTitle());
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            //设置精选ICON的位置
            if (data.isHandPick()) {
                iv_is_jing.setVisibility(View.VISIBLE);

                int coverWidth = iv_cover.getWidth();
                if (coverWidth > 0) {
                    int jingIconWidth = getActivity().getResources().getDrawable(R.mipmap.dynamic_detail_jing).getIntrinsicWidth() / 2;
                    int margin = iv_cover.getLeft() + coverWidth / 2 - jingIconWidth;
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_is_jing.getLayoutParams();
                    params1.leftMargin = margin;
                    iv_is_jing.setLayoutParams(params1);
                }
            } else {
                iv_is_jing.setVisibility(View.GONE);
            }

            //标题旁边的那条线处理
            if (tv_title.getVisibility() == View.VISIBLE
                    && (tv_class.getVisibility() == View.VISIBLE
                    || tv_rest_name.getVisibility() == View.VISIBLE
                    || tv_poeple_num.getVisibility() == View.VISIBLE
                    || tv_title.getVisibility() == View.VISIBLE)) {
                title_line.setVisibility(View.VISIBLE);
            } else {
                title_line.setVisibility(View.INVISIBLE);
            }

            setKingCrownInfo(headView,data);

            FFImageLoader.load_base((FFContext) activity, data.getFood().pullHeadImage(), iv_cover, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, FFImageLoader.TYPE_NONE, lis);
        }

        public View findViewById(int id) {
            return headView.findViewById(id);
        }

        private void initVerticalTextViewParams(VerticalTextView textView) {
            textView.setTextSize(activity.getResources().getDimensionPixelSize(R.dimen.font_size_2));
            textView.setTextColor(activity.getResources().getColor(R.color.title_text_color));
            textView.setLineWidth(activity.getResources().getDimensionPixelSize(R.dimen.vertical_textview_width));
        }

        private Activity getActivity() {
            return activity;
        }

    }

}
