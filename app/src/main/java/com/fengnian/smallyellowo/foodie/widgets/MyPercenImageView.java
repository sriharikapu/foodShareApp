package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.fan.framework.imageloader.PercentImageView;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by Administrator on 2017-6-21.
 */

public class MyPercenImageView extends PercentImageView {
    private Paint paint;

    public MyPercenImageView(Context context) {
        super(context);
        paint = new Paint();
    }

    public MyPercenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public MyPercenImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    Bitmap dishName;


    public void setDishName(String dishName) {
        if (this.dishName != null && this.dishName.isRecycled()) {
            this.dishName.recycle();
            this.dishName = null;
        }
        if(TextUtils.isEmpty(dishName)){
            return;
        }
        TextView tv = new TextView(getContext());
        tv.setBackgroundResource(R.drawable.bg_dish_bg);
        tv.setGravity(Gravity.CENTER);
        int px = FFUtils.getPx(8);
        tv.setPadding(px, 0, px, 0);
        tv.setTextColor(0xffffffff);
        tv.setText(dishName);
        tv.setTextSize(13);
        int textViewLength = (int) FFUtils.getTextViewLength(tv, dishName);
        tv.setWidth(textViewLength+px+px);
        tv.setHeight(FFUtils.getPx(25));
        this.dishName = FFUtils.getBitmapViewByMeasure(tv, textViewLength+px+px, FFUtils.getPx(25));
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = getImage();
        if (bitmap == null || null == dishName) {
            return;
        }
        int thisWidth = getWidth();
        int thisHeight = getHeight();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        boolean isMoreWidth = thisWidth * 1f / thisHeight < bitmapWidth * 1f / bitmapHeight;
        int bitmapDrawWidth = bitmapWidth*thisHeight/bitmapHeight;
        int bitmapDrawHeight = bitmapHeight*thisWidth/bitmapWidth;
        int x = isMoreWidth?(//宽图
                thisWidth
                ):(//高图
                (thisWidth- bitmapDrawWidth)/2+ bitmapDrawWidth
                );
        int y = isMoreWidth?(//宽图
                (thisHeight- bitmapDrawHeight)/2+ bitmapDrawHeight
        ):(//高图
               thisHeight
        );

        canvas.drawBitmap(dishName, x-dishName.getWidth()-FFUtils.getPx(10), y-dishName.getHeight()-FFUtils.getPx(10), paint);
    }
}
