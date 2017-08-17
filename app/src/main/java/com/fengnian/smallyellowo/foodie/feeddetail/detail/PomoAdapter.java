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
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by Administrator on 2017-3-8.
 */

public class PomoAdapter extends BaseDetailAdapter {
    private final ChinaStyleHeadViewHolder headView;
//    private final PomoHeadViewHolder headView;

    public PomoAdapter(DynamicDetailActivity activity) {
        super(activity);
//        headView = new PomoHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_pomo_title, null));

        View view = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_china_style_title, null);
        view.setPadding(0,0,0,FFUtils.getPx(44));
        headView = new ChinaStyleHeadViewHolder(activity, view) {
            @Override
            protected void refresh1() {
                super.refresh1();
                String title = getActivity().data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
                if (!TextUtils.isEmpty(title)) {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(title);
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    int size = 30;
                    float textViewLength = FFUtils.getTextViewLength(tv_title, title);
                    while (size > 19 && textViewLength > FFUtils.getPx(300)) {
                        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
                        size--;
                    }
                }

                if (tv_time != null) {
                    tv_time.setText(TimeUtils.getTime("yyyy-MM-dd    HH:mm", getActivity().data.getTimeStamp()));
                }
                tv_time.setTextColor(APP.app.getResources().getColor(R.color.china_style_font_color));
            }
        };
        ((ImageView) view.findViewById(R.id.iv_head_bg)).setImageResource(R.drawable.pomo_head);
        view.setBackgroundColor(0xFFFCF2E6);
        activity.listView1.setBackgroundColor(0xFFFCF2E6);
        activity.getContainer().setBackgroundColor(0xFFFCF2E6);
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
            case 0://头
                return headView.getHeadView();
            case 1: {//图片1
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_pomo_right_image, parent, false);
                    convertView.findViewById(R.id.iv_img).getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                    convertView.findViewById(R.id.iv_img_cover).getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                }
                initPic(position, convertView);
                setImagePadding(false, (ImageView) convertView.findViewById(R.id.iv_img));
                return convertView;
            }
            case 2: {//图片2
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_pomo_left_image, parent, false);
                    convertView.findViewById(R.id.iv_img).getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                    convertView.findViewById(R.id.iv_img_cover).getLayoutParams().height = FFUtils.getDisWidth() * 1182 / 1125;
                }
                initPic(position, convertView);
                setImagePadding(true,(ImageView) convertView.findViewById(R.id.iv_img));
                return convertView;
            }
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_pomo_text, parent, false);
                }
                TextView tv_content;
                tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());
                return convertView;
            }
        }
    }

    //计算遮盖的那个图片的空白处间距 by chenglin version:2.8.0
    private void setImagePadding(boolean isLeft, ImageView iv_img) {
        float scale = 0.09f;//这个值是测量图片得到的
        float padding = DisplayUtil.screenWidth * scale;

        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
        if (isLeft) {
            imageParams.rightMargin = (int) padding;
            imageParams.leftMargin = 0;
        } else {
            imageParams.leftMargin = (int) padding;
            imageParams.rightMargin = 0;
        }
        iv_img.setLayoutParams(imageParams);
    }

    private void initPic(int position, View convertView) {
        ImageView iv_img;
        TextView tv_content;
        TextView tv_dish_name;
        ImageView iv_comment;
        iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
        tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
        iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

        int level = getItem(position).getPhoto().getImageComment();
        setCommentData(level, iv_comment, CommentLevel.TYPE_STANDARD);

        final SYImage img = getItem(position).getPhoto().getImageAsset().pullProcessedImage().getImage();
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
    }

    @Override
    public int getMCount() {
        return activity.data.getFood().getRichTextLists().size() + 1;
    }


    @Override
    public int getMViewTypeCount() {
        return 4;
    }

    @Override
    public int getMItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (!activity.data.getFood().getRichTextLists().get(position - 1).isTextPhotoModel()) {
            return 3;
        }
        int j = 0;
        for (int i = 0; i < position; i++) {
            if (activity.data.getFood().getRichTextLists().get(i).isTextPhotoModel()) {
                j++;
                j %= 2;
            }
        }
        return 1 + j;
    }
}
