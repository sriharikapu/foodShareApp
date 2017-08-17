package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-2-28.
 */

public class CustomRatingBar extends LinearLayout {
    private static final int RATING_MAX_LEVEL = 4;
    private static int mItemPadding;
    private static final int STAR_SOLID = 1;
    private static final int STAR_EMPTY = 2;

    public CustomRatingBar(Context context) {
        super(context);
        init();
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mItemPadding = DisplayUtil.dip2px(5f);

        for (int i = 0; i < RATING_MAX_LEVEL; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setTag(STAR_EMPTY);
            imageView.setImageResource(R.mipmap.vertical_rating_star_empty);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            if (i != 0) {
                if (getOrientation() == LinearLayout.VERTICAL) {
                    params.topMargin = mItemPadding;
                } else {
                    params.leftMargin = mItemPadding;
                }
            }
            addView(imageView, params);
        }
    }

    /**
     * 设置评分等级
     */
    public CustomRatingBar setLevel(final int level) {
        for (int i = 0; i < getChildCount(); i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            if (i < level) {
                imageView.setImageResource(R.mipmap.vertical_rating_star);
                imageView.setTag(STAR_SOLID);
            }else {
                imageView.setImageResource(R.mipmap.vertical_rating_star_empty);
                imageView.setTag(STAR_EMPTY);
            }
        }
        return this;
    }

    /**
     * 设置星星的图片，第一个参数是实心的星星，第二个是空心的星星
     */
    public CustomRatingBar setStarIcon(final int solidStarRes, final int emptyStarRes) {
        for (int i = 0; i < getChildCount(); i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            int starType = (int) imageView.getTag();
            if (starType == STAR_SOLID) {
                imageView.setImageResource(solidStarRes);
            } else if (starType == STAR_EMPTY) {
                imageView.setImageResource(emptyStarRes);
            }
        }
        return this;
    }

    /**
     * 设置星星的间距
     */
    public CustomRatingBar setStarPadding(final int padding) {
        mItemPadding = padding;
        for (int i = 0; i < getChildCount(); i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) imageView.getLayoutParams();
            if (i != 0) {
                if (getOrientation() == LinearLayout.VERTICAL) {
                    params.topMargin = mItemPadding;
                } else {
                    params.leftMargin = mItemPadding;
                }
            }
        }
        return this;
    }
}
