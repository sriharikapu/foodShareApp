package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by Administrator on 2016-11-25.
 */

public class DynamicImageView extends ImageView {
    private final RectF roundRect = new RectF();
    private float rectAdius = 0f;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();
    private Paint paint;
    private int wantWidth = 0;
    public int bitmapWidth,bitmapHeight;
    private onBitmapLoadedListener mOnBitmapLoadedListener;

    public DynamicImageView(Context context) {
        super(context);
    }

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRectAdius(float adius) {
        if (adius <= 0f) {
            return;
        }
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.BLACK);

        paint = new Paint();
        rectAdius = DisplayUtil.dip2px(adius);
        invalidate();
    }


    public void setWidth(int width) {
        wantWidth = width;
    }

    public void setOnBitmapLoadedListener(onBitmapLoadedListener listener){
        mOnBitmapLoadedListener = listener;
    }

    public void removeOnBitmapLoadedListener(){
        mOnBitmapLoadedListener = null;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            bitmapWidth = bm.getWidth();
            bitmapHeight = bm.getHeight();

            if (wantWidth > 0){
                int height = wantWidth * bitmapHeight / bitmapWidth;
                getLayoutParams().width = wantWidth;
                getLayoutParams().height = height;
            }

            if (mOnBitmapLoadedListener != null){
                mOnBitmapLoadedListener.onLoaded(bitmapWidth,bitmapHeight);
            }
        }
        super.setImageBitmap(bm);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        if (rectAdius > 0) {
            canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
            canvas.drawRoundRect(roundRect, rectAdius, rectAdius, zonePaint);
            canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        }

        super.draw(canvas);
        if (rectAdius > 0) {
            canvas.restore();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectAdius > 0) {
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(R.color.line));
            canvas.drawColor(Color.alpha(0));
            paint.setStrokeWidth((float) 1.0);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(roundRect, rectAdius, rectAdius, paint);
        }
    }

    public interface onBitmapLoadedListener {
        void onLoaded(int bitmapWidth, int bitmapHeight);
    }
}
