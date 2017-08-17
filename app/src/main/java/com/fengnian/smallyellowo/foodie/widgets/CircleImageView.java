package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。
 * 
 */
public class CircleImageView extends ImageView {

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleImageView(Context context) {
		super(context);
		init();
	}

	private final RectF roundRect = new RectF();
	private float rect_adius = 3;
	private final Paint maskPaint = new Paint();
	private final Paint zonePaint = new Paint();
	private Paint paint;

	private void init() {
		maskPaint.setAntiAlias(true);
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		//
		zonePaint.setAntiAlias(true);
		zonePaint.setColor(Color.BLACK);
		//
		float density = getResources().getDisplayMetrics().density;
		rect_adius = rect_adius * density;
		paint = new Paint();
	}

	public void setRectAdius(float adius) {
		rect_adius = DisplayUtil.dip2px(adius);
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int w = getWidth();
		int h = getHeight();
		roundRect.set(0, 0, w, h);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
		canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
		//
		canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
		super.draw(canvas);
		canvas.restore();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setAntiAlias(true);
		// 设置画笔为无锯齿
		paint.setColor(0);
		// 设置画笔颜色
		canvas.drawColor(Color.alpha(0));
		// 白色背景
		paint.setStrokeWidth((float) 1.0);
		// 线宽
		paint.setStyle(Style.STROKE);
		// 绘制矩形
		// 下边
		canvas.drawRoundRect(roundRect, rect_adius, rect_adius, paint);
	}

}