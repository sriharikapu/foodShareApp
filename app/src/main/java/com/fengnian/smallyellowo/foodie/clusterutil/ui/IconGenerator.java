/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.fengnian.smallyellowo.foodie.clusterutil.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.DeliciousFoodMapActivity;
import com.fengnian.smallyellowo.foodie.R;


/**
 * IconGenerator generates icons that contain text (or custom content) within an info
 * window-like shape.
 * <p/>
 * The icon {@link Bitmap}s generated by the factory should be used in conjunction with a {@link
 * com.baidu.mapapi.map.BitmapDescriptorFactory}.
 * <p/>
 * This class is not thread safe.
 */
public class IconGenerator {
    private final Context mContext;

    private ViewGroup mContainer;
    private RotationLayout mRotationLayout;
    private TextView mTextView;
    private View mContentView;

    private int mRotation;

    private float mAnchorU = 0.5f;
    private float mAnchorV = 1f;

    /**
     * Creates a new IconGenerator with the default style.
     */
    public IconGenerator(Context context) {
        mContext = context;
        mContainer = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.text_bubble, null);
        mRotationLayout = (RotationLayout) mContainer.getChildAt(0);
        mContentView = mTextView = (TextView) mContainer.findViewById(R.id.text);
//        setStyle(STYLE_DEFAULT);
    }

    /**
     * Sets the text content, then creates an icon with the current style.
     *
     * @param text the text content to display inside the icon.
     */
    public Bitmap makeIcon(String text) {
        if (mTextView != null) {
            mTextView.setText(text);
        }

        return makeIcon(text,"");
//      return   BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.shanghu_zuobiao);
    }

    /**
     * Creates an icon with the current content and style.
     * <p/>
     * This method is useful if a custom view has previously been set, or if text content is not
     * applicable.
     */
    public Bitmap makeIcon(String text,String text1) {
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mContainer.measure(measureSpec, measureSpec);

        int measuredWidth = mContainer.getMeasuredWidth();
        int measuredHeight = mContainer.getMeasuredHeight();

//        mContainer.layout(0, 0, measuredWidth, measuredHeight);
        mContainer.layout(0, 0, 0, 0);

        if (mRotation == 1 || mRotation == 3) {
            measuredHeight = mContainer.getMeasuredWidth();
            measuredWidth = mContainer.getMeasuredHeight();
        }

//        Bitmap r = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
//        r.eraseColor(Color.TRANSPARENT);
//        Bitmap r=  BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.shanghu_zuobiao);
        Bitmap r=null;
        if(DeliciousFoodMapActivity.SetMarkIconStytle==1){
             r=    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.shanghu_zuobiao).copy(Bitmap.Config.ARGB_8888, true);
        }
        else if(DeliciousFoodMapActivity.SetMarkIconStytle==2){
             r=    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.jingmao_number_some).copy(Bitmap.Config.ARGB_8888, true);
        }else{
             r=    BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.shanghu_zuobiao).copy(Bitmap.Config.ARGB_8888, true);
        }

          Canvas canvas = new Canvas(r);

//        if (mRotation != 0) {
//            // do nothing
//            if (mRotation == 1) {
//                canvas.translate(measuredWidth, 0);
//                canvas.rotate(90);
//            } else if (mRotation == 2) {
//                canvas.rotate(180, measuredWidth / 2, measuredHeight / 2);
//            } else {
//                canvas.translate(0, measuredHeight);
//                canvas.rotate(270);
//            }
//
//        }

//        mContainer.draw(canvas);

        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(1);

        if(Integer.valueOf(text)>=1000)
            mPaint.setTextSize(FFUtils.getPx(10));
        else mPaint.setTextSize(FFUtils.getPx(12));
        if(Integer.valueOf(text)>=10000)
            text="1万+";
//        mPaint.setColor(Color.RED);
//        mPaint.setColor(mContext.getColor(R.color.index_word));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPaint.setColor(mContext.getColor(R.color.juehe_text_color));
        }else{
            mPaint.setColor(Color.rgb(110,110,110));
        }
        mPaint.setAntiAlias(true);//抗锯齿
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (r.getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top-FFUtils.getPx(5);
        canvas.drawText(text,r.getWidth() / 2 - bounds.width() / 2-5, baseline, mPaint);
        return r;
    }

    /**
     * Sets the child view for the icon.
     * <p/>
     * If the view contains a {@link TextView} with the id "text", operations such as {@link
     * #setTextAppearance} and {@link #makeIcon(String)} will operate upon that {@link TextView}.
     */
    public void setContentView(View contentView) {
        mRotationLayout.removeAllViews();
        mRotationLayout.addView(contentView);
        mContentView = contentView;
        final View view = mRotationLayout.findViewById(R.id.text);
        mTextView = view instanceof TextView ? (TextView) view : null;
    }

    /**
     * Rotates the contents of the icon.
     *
     * @param degrees the amount the contents should be rotated, as a multiple of 90 degrees.
     */
    public void setContentRotation(int degrees) {
        mRotationLayout.setViewRotation(degrees);
    }

    /**
     * Rotates the icon.
     *
     * @param degrees the amount the icon should be rotated, as a multiple of 90 degrees.
     */
    public void setRotation(int degrees) {
        mRotation = ((degrees + 360) % 360) / 90;
    }


    /**
     * @return u coordinate of the anchor, with rotation applied.
     */
    public float getAnchorU() {
        return rotateAnchor(mAnchorU, mAnchorV);
    }

    /**
     * @return v coordinate of the anchor, with rotation applied.
     */
    public float getAnchorV() {
        return rotateAnchor(mAnchorV, mAnchorU);
    }

    /**
     * Rotates the anchor around (u, v) = (0, 0).
     */
    private float rotateAnchor(float u, float v) {
        switch (mRotation) {
            case 0:
                return u;
            case 1:
                return 1 - v;
            case 2:
                return 1 - u;
            case 3:
                return v;
            default:
        }
        throw new IllegalStateException();
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color from the specified
     * <code>TextAppearance</code> resource.
     *
     * @param resid the identifier of the resource.
     */
    public void setTextAppearance(Context context, int resid) {
        if (mTextView != null) {
            mTextView.setTextAppearance(context, resid);
        }
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color from the specified
     * <code>TextAppearance</code> resource.
     *
     * @param resid the identifier of the resource.
     */
    public void setTextAppearance(int resid) {
        setTextAppearance(mContext, resid);
    }

    /**
     * Sets the style of the icon. The style consists of a background and text appearance.
     */
    public void setStyle(int style) {
        setTextAppearance(mContext, getTextStyle(style));
    }

    /**
     * Set the background to a given Drawable, or remove the background.
     *
     * @param background the Drawable to use as the background, or null to remove the background.
     */
    @SuppressWarnings("deprecation")
    // View#setBackgroundDrawable is compatible with pre-API level 16 (Jelly Bean).
    public void setBackground(Drawable background) {
        mContainer.setBackgroundDrawable(background);

        // Force setting of padding.
        // setBackgroundDrawable does not call setPadding if the background has 0 padding.
//        if (background != null) {
//            Rect rect = new Rect();
//            background.getPadding(rect);
//            mContainer.setPadding(rect.left, rect.top, rect.right, rect.bottom);
//        } else {
//            mContainer.setPadding(0, 0, 0, 0);
//        }
    }

    /**
     * Sets the padding of the content view. The default padding of the content view (i.e. text
     * view) is 5dp top/bottom and 10dp left/right.
     *
     * @param left   the left padding in pixels.
     * @param top    the top padding in pixels.
     * @param right  the right padding in pixels.
     * @param bottom the bottom padding in pixels.
     */
    public void setContentPadding(int left, int top, int right, int bottom) {
        mContentView.setPadding(left, top, right, bottom);
    }

    public static final int STYLE_DEFAULT = 1;
    public static final int STYLE_WHITE = 2;
    public static final int STYLE_RED = 3;
    public static final int STYLE_BLUE = 4;
    public static final int STYLE_GREEN = 5;
    public static final int STYLE_PURPLE = 6;
    public static final int STYLE_ORANGE = 7;

    private static int getStyleColor(int style) {
        switch (style) {
            default:
            case STYLE_DEFAULT:
            case STYLE_WHITE:
                return 0xffffffff;
            case STYLE_RED:
                return 0xffcc0000;
            case STYLE_BLUE:
                return 0xff0099cc;
            case STYLE_GREEN:
                return 0xff669900;
            case STYLE_PURPLE:
                return 0xff9933cc;
            case STYLE_ORANGE:
                return 0xffff8800;

        }
    }

    private static int getTextStyle(int style) {
        switch (style) {
            default:
            case STYLE_DEFAULT:
            case STYLE_WHITE:
                return R.style.Bubble_TextAppearance_Dark;
            case STYLE_RED:
            case STYLE_BLUE:
            case STYLE_GREEN:
            case STYLE_PURPLE:
            case STYLE_ORANGE:
                return R.style.Bubble_TextAppearance_Light;
        }
    }
}
