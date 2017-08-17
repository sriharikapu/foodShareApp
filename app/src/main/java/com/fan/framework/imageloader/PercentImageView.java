package com.fan.framework.imageloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.ZoomImageView;
import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by Administrator on 2016-9-28.
 */
public class PercentImageView extends ZoomImageView {

    Paint linePait;
    TextPaint textPaint;
    Paint frintPaint;

    int textSize = 42;

    public PercentImageView(Context context) {
        super(context);
        init();
    }

    @Override
    public void setTag(int key, Object tag) {
        super.setTag(key, tag);
        FFLogUtil.e("image", key + " " + tag);
    }

    public PercentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();


    }

    private void init() {
        linePait = new Paint();
        textPaint = new TextPaint();
        frintPaint = new Paint();

        linePait.setColor(0xffffffff);
        textPaint.setColor(0xffffffff);
        frintPaint.setColor(getResources().getColor(R.color.colorPrimary));

        linePait.setStrokeWidth(1);
        frintPaint.setStrokeWidth(3);
        textPaint.setTextSize(textSize);

    }

    int totailProgress = 100;
    int downloaded = 100;

    public void onProgress(int downloaded, int totailProgress) {
        this.totailProgress = totailProgress;
        this.downloaded = downloaded;
        postInvalidate();
    }

    public void onFaile() {
        this.totailProgress = 100;
        this.downloaded = -1;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (downloaded < 0) {
            String text = "加载失败！";
            canvas.drawText(text, (width / 2) - (textPaint.measureText(text) / 2), height / 2 - textSize / 2, textPaint);
        } else if (totailProgress > downloaded) {
            canvas.drawLine(FFUtils.getPx(60), height / 2, width - FFUtils.getPx(60), height / 2, linePait);

            int length = (downloaded * (width - FFUtils.getPx(120))) / totailProgress;
            canvas.drawLine(FFUtils.getPx(60), (height / 2) - 1, length + FFUtils.getPx(60), (height / 2) - 1, frintPaint);

            String text = (downloaded * 100 / totailProgress) + "%";
            canvas.drawText(text, (width / 2) - (textPaint.measureText(text) / 2), height / 2 - textSize - FFUtils.getPx(2), textPaint);
        }
    }


}
