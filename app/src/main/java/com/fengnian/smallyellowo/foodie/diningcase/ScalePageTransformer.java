package com.fengnian.smallyellowo.foodie.diningcase;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.fan.framework.utils.FFLogUtil;

/**
 * Created by chenglin on 2017-6-8.
 */

public class ScalePageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.9f;
    private static final float MIN_ALPHA = 0.45f;

    @Override
    public void transformPage(View view, float position) {

        if (position < -1 || position > 1) {
            view.setAlpha(MIN_ALPHA);
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else if (position <= 1) { // [-1,1]
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            if (position < 0) {
                float scaleX = 1 + 0.1f * position;
                view.setScaleX(scaleX);
                view.setScaleY(scaleX);
            } else {
                float scaleX = 1 - 0.1f * position;
                view.setScaleX(scaleX);
                view.setScaleY(scaleX);
            }
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}