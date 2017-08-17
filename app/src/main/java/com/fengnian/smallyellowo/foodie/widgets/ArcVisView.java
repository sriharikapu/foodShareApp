package com.fengnian.smallyellowo.foodie.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class ArcVisView extends View {

    private Paint paint;
    private RectF rec;
    private Handler handler;

    @SuppressLint("NewApi")
    public ArcVisView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ArcVisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ArcVisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcVisView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xffffffff);
        rec = new RectF();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                invalidate();
            }
        };
    }

    private final int max = 1500;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = Math.min(getWidth(), getHeight());
        int xOffset = 0;
        int yOffset = 0;
        if (getWidth() > getHeight()) {
            xOffset = (getWidth() - getHeight())/2;
        } else {
            yOffset = (getHeight() - getWidth())/2;
        }
        rec.set(xOffset, yOffset, xOffset+width, yOffset+width);
        if (!playing) {
            if (isOriginal) {
                paint.setAntiAlias(true);
                canvas.drawArc(rec, 0, 360, true, paint);
            }
            return;
        }
//        rec.set(xOffset, yOffset, width, width);
        paint.setAntiAlias(true);
        int period = (int) (System.currentTimeMillis() - startTime);
        if (period < max) {
            final int per = 360 * period / max;
            int angle = -90 + per;
            canvas.drawArc(rec, angle, 270 - angle, true, paint);
            handler.sendEmptyMessage(0);
        } else {
            playing = false;
        }
    }

    public void setOriginal(boolean isOriginal) {
        this.isOriginal = isOriginal;
        playing = false;
        invalidate();
    }

    boolean playing = false;
    long startTime = 0;

    boolean isOriginal = true;

    public void start() {
        playing = true;
        startTime = System.currentTimeMillis();
        isOriginal = false;
        invalidate();
    }

}
