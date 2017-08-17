package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView to display top-crop scale of an image view.
 *
 * @author Chris Arriola
 */
public class TopCropImageView extends ImageView {
    public TopCropImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TopCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        if(getDrawable() == null){
            return super.setFrame(l, t, r, b);
        }
        final Matrix matrix = getImageMatrix();

        float scale;
        final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        final int drawableWidth = getDrawable().getIntrinsicWidth();
        final int drawableHeight = getDrawable().getIntrinsicHeight();

        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = (float) viewHeight / (float) drawableHeight;
        } else {
            scale = (float) viewWidth / (float) drawableWidth;
        }

        matrix.setScale(scale, scale);
        setImageMatrix(matrix);

        return super.setFrame(l, t, r, b);
    }
}