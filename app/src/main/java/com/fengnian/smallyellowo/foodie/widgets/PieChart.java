package com.fengnian.smallyellowo.foodie.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * 圆形饼图控件,title为多少条就显示多少条，无视value
 *
 * @author HuangYuGuang Create on 2015年12月18日 File Name PieChart.java
 */
public class PieChart extends View {
    public static final int StorkWidth = 500;

    private final float density = getResources().getDisplayMetrics().density;
    /**
     * 饼图之间间隔的角度
     */
    private final float ANGLE_DIS = 0;
    /**
     * 左右两边空间距离
     */
    private final float LR_PADDING = 25 * density;
    /**
     * 顶部空间距离
     */
    private final float TOP_PADDING = 27 * density;
    /**
     * 饼图和下边文字的距离
     */
    private final float PIE_TEXT_DIS = 22 * density;
    /**
     * 上下行文字的距离
     */
    private final float TEXT_TEXT_DIS = 15 * density;
    /**
     * 底部和文字的距离,实际为BOTTOM_DIS+TEXT_TEXT_DIS
     */
    private final float BOTTOM_DIS = 27 * density;
    /**
     * 标题和值的最小距离
     */
    private final float TITLE_VALUE_DIS = 18 * density;

    /**
     * 每部分的颜色值,默认有10个颜色
     */
    private int[] mColors = new int[]{0xfff5a002, 0xfffb5a2f, 0xff36bc99, 0xff43F90C, 0xff181A18, 0xffF802F6,
            0xff022DF8, 0xffECF802, 0xff02F8E8, 0xffEA0F8E};
    /**
     * 值
     */
    private double[] mValues;
    /**
     * 值转换成角度
     */
    private float[] mAngles;

    public View getCenterTexts() {
        return centerTexts;
    }

    public void setCenterTexts(View centerTexts) {
        this.centerTexts = centerTexts;
    }

    private View centerTexts;
    /**
     * 饼图直径
     */
    private float pieR;
    /**
     * 饼图所占总的角度
     */
    private float pieAngle;

    private String[] mTitles; // 每部分的内容
    private String mEmptyMsg = "暂无数据"; // 无数据提示的内容

    private float mTitleSize;
    private float mValueSize;
    /**
     * 饼图里面文字的大小
     */
    private float mPieTextSize;

    private int mDefaultPointColor = 0xfff5a002; // 无数据时提示文字的颜色

    private Rect mTextBound;
    private TextPaint mTextPaint;
    private Paint mPiePaint;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTitleSize = sp2px(14);
        mPieTextSize = sp2px(12);
        mValueSize = sp2px(16);

        mPiePaint = new Paint();
        mTextPaint = new TextPaint();
        mTextBound = new Rect();
        mTextPaint.setColor(0xff595959);
        mTextPaint.setTextSize(mTitleSize);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPiePaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        /** ------------------------------无数据-------------------------------- */
        if (mTitles == null || mTitles.length == 0 || isValueEmpty()) {
//			mPiePaint.setColor(mDefaultPointColor);
//			mTextPaint.setColor(0xffffffff);
//			float cr = (getWidth() < getHeight() ? getWidth() / 2 : getHeight() / 2) - LR_PADDING;
//			canvas.drawCircle(getWidth() / 2, getHeight() / 2, cr, mPiePaint);
//			mTextPaint.getTextBounds(mEmptyMsg, 0, mEmptyMsg.length(), mTextBound);
//			canvas.drawText(mEmptyMsg, (getWidth() - mTextBound.width()) / 2, (getHeight() + mTextBound.height()) / 2,
//					mTextPaint);
            return;
        }
        /** ------------------------------无数据-------------------------------- */

        /** ------------------------------画饼图-------------------------------- */
        int textHeight = getTextHeight("00", Math.max(mTitleSize, mValueSize));
        float r1 = getWidth() - LR_PADDING * 2;
        float r2 = getHeight() - TOP_PADDING - BOTTOM_DIS;
        pieR = Math.min(r1, r2);// 为了防止饼图越界，饼图直径选取最小值
        if(centerTexts != null) {
            int yyyy = (int) (TOP_PADDING + pieR * 1.0f / 2 - centerTexts.getHeight() / 2);
            centerTexts.setPadding(0, yyyy, 0, 0);
//            ((FrameLayout.LayoutParams) centerTexts.getLayoutParams()).setMargins(0, yyyy, 0, 0);
            centerTexts.setVisibility(View.VISIBLE);
        }
        RectF oval = new RectF((getWidth() - pieR) / 2, TOP_PADDING, (getWidth() + pieR) / 2, TOP_PADDING + pieR);
        mPiePaint.setStyle(Paint.Style.FILL);
        mAngles = getAngles();
        float[] centerAngle = new float[mAngles.length];
        float pre = 0;
        for (int i = 0; i < mAngles.length; i++) {
            if (mAngles[i] == 0)
                continue;
            centerAngle[i] = ANGLE_DIS + mAngles[i] / 2 + pre;
            pre += mAngles[i];
        }
        for (int i = 0; i < mAngles.length; i++) {
            mAngles[i] = mAngles[i];
        }
        float startAngle = -90;
        int halfFengxi = 5;
        float centerX = getWidth() * 1.0f / 2;
        float centerY = TOP_PADDING + pieR * 1.0f / 2;
        for (int i = 0; i < mAngles.length; i++) {
            mPiePaint.setColor(mColors[i]);
            if (mAngles[i] == 0)
                continue;
            // if(i == 0){
            // canvas.drawArc(oval.left, oval.top, oval.right, oval.bottom,
            // startAngle, mAngles[i], true,
            // mPiePaint);
            // startAngle += (mAngles[i] + ANGLE_DIS);
            // }
            // canvas.d
            double tan = Math.abs(Math.sin(((mAngles[i]) / 2) * (Math.PI / 180)));
            int neiR = (int) (halfFengxi * 1f / tan);
            float offsetX = (float) (neiR * Math.cos((centerAngle[i] - 90) * (Math.PI / 180)));
            float offsetY = (float) (neiR * Math.sin((centerAngle[i] - 90) * (Math.PI / 180)));

            float arcCenterX = 0;
            float arcCenterY = 0;

            arcCenterX = centerX + offsetX;
            arcCenterY = centerY + offsetY;

            float x = arcCenterX - 1.0f / 2;
            float y = arcCenterY + 1.0f / 2 / 2;
            x -= oval.width() / 2 + oval.left;
            y -= oval.height() / 2 + oval.top;
            oval.offset(x, y);
            canvas.drawArc(oval, startAngle, mAngles[i], true, mPiePaint);
            oval.offset(-x, -y);
            startAngle += (mAngles[i] + ANGLE_DIS);
        }
        /** ------------------------------画饼图-------------------------------- */

        /** ------------------------------最下面的文字，title和value-------------------------------- */
        // float cr = 5 * density; // 圆点半径
        // float ctd = 8 * density; // 圆点和右边文字距离
        // float titleLen = getMaxTextWidth(mTitles, mTitleSize) +
        // TITLE_VALUE_DIS;
        // float len = 2 * cr + ctd + titleLen + getMaxTextWidth(mValues,
        // mValueSize);
        // float topDis = TOP_PADDING + PIE_TEXT_DIS + pieR + textHeight; //
        // 第一行文字底部和控件顶部距离
        // float cX = (getWidth() - len) / 2 + cr;
        // float titleX = cX + cr + ctd;
        // float valueX = titleX + titleLen;
        // int valueLen = mValues.length - 1;
        // for (int i = 0; i < mTitles.length; i++) {
        // mPiePaint.setColor(mColors[i]);
        // float yDis = topDis + (textHeight + TEXT_TEXT_DIS) * i;
        // canvas.drawCircle(cX, yDis - textHeight / 2, cr, mPiePaint);
        // mTextPaint.setTextSize(mTitleSize);
        // mTextPaint.setColor(mTitleColor);
        // canvas.drawText(mTitles[i], titleX, yDis, mTextPaint);
        // mTextPaint.setColor(mValueColor);
        // mTextPaint.setTextSize(mValueSize);
        // canvas.drawText("gggggg", valueX, yDis, mTextPaint);
        // // canvas.drawText(AmountUtils.moneyFormat(i > valueLen ? 0 :
        // // mValues[i]), valueX, yDis, mTextPaint);
        // }
        /** ------------------------------最下面的文字，title和value-------------------------------- */

        // 饼图上的文字

        pre = 0;
        // centerAngle = new float[mAngles.length];
        // for (int i = 0; i < mAngles.length; i++) {
        // if (mAngles[i] == 0)
        // continue;
        // centerAngle[i] = ANGLE_DIS + mAngles[i] / 2 + pre;
        // pre += mAngles[i];
        // }
        float cenR = pieR * 1.0f / 2 * 25 / 30;
        mTextPaint.setColor(0xFFFFFFFF);
        float cenX = 0;
        float cenY = 0;
        for (int i = 0; i < centerAngle.length; i++) {
            if (centerAngle[i] == 0)
                continue;
            float xa = (float) (cenR * Math.cos((centerAngle[i] - 90) * (Math.PI / 180)));
            float ya = (float) (cenR * Math.sin((centerAngle[i] - 90) * (Math.PI / 180)));
            cenX = centerX + xa;
            cenY = centerY + ya;
            mTextPaint.setTextSize(mPieTextSize);
            String perMsg = mTitles[i];
            // perMsg = "中华儿女";
            float x = cenX - (getTextWidth(perMsg, mPieTextSize) * 1.0f / 2);
            float y = cenY - (getTextHeight(perMsg, mPieTextSize) * 1.0f / 2) + getTextHeight("中", mPieTextSize) * 0.8f;
            youhuaString(perMsg, canvas, x, y);
//			mPiePaint.setColor(0xff000000);
//			canvas.drawCircle(cenX, cenY, 5, mPiePaint);
        }
        // 内圆
        mPiePaint.setColor(0xffffffff);
        canvas.drawCircle(centerX, centerY, pieR * 1.0f / 2 * 4 / 6, mPiePaint);
        // 外圆
        mPiePaint.setStyle(Paint.Style.STROKE);
        mPiePaint.setStrokeWidth(StorkWidth);
        canvas.drawCircle(centerX, centerY, pieR * 1.0f / 2 + StorkWidth / 2, mPiePaint);
        // canvas.draw:

    }

    /**
     * 设置每个饼图代表的名字
     *
     * @param titles
     */
    public void setTitles(List<String> titles) {
        mTitles = (String[]) titles.toArray();
    }

    /**
     * 设置每个饼图代表的名字
     *
     * @param titles
     * @author HuangYuGuang Create on 2015年12月30日
     */
    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    /**
     * 设置值
     *
     * @param values
     */
    public void setValues(List<Double> values) {
        mValues = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            mValues[i] = values.get(i);
        }
    }

    /**
     * 设置值
     *
     * @param values
     */
    public void setValues(double[] values) {
        mValues = values;
    }

    /**
     * 设置每块饼图的颜色
     *
     * @param colors
     */
    public void setColors(List<Integer> colors) {
        mColors = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            mColors[i] = colors.get(i);
        }
    }

    /**
     * 设置每块饼图的颜色
     *
     * @param colors
     */
    public void setColors(int[] colors) {
        mColors = colors;
    }

    /**
     * 设置名字的字体大小
     *
     * @param size
     */
    public void setTitleSize(float size) {
        mTitleSize = sp2px(size);
    }

    /**
     * 设置数值的大小
     *
     * @param size
     */
    public void setValueSize(float size) {
        mValueSize = sp2px(size);
    }

    /**
     * 设置饼图上文字的大小
     *
     * @param size
     */
    public void setPieTextSize(float size) {
        mPieTextSize = sp2px(size);
    }

    /**
     * 设置无数据时提示文字的颜色 Create on 2015年12月31日
     *
     * @param color
     */
    public void setDefaultPointColor(int color) {
        mDefaultPointColor = color;
    }

    /**
     * 无数据的提示内容
     *
     * @param msg
     */
    public void setEmptyMsg(String msg) {
        mEmptyMsg = msg;
    }

    private boolean isValueEmpty() {
        if (mValues == null || mValues.length == 0)
            return true;
        for (double va : mValues) {
            if (va > 0)
                return false;
        }
        mEmptyMsg = "暂无数据";
        return true;
    }

    /**
     * 获得每个值所占的角度
     *
     * @return
     */
    private float[] getAngles() {
        if (mValues == null || mValues.length == 0)
            return null;
        double sum = 0;
        int len = mTitles.length;
        float[] angles = new float[len];
        int gapCount = 0;// 饼图间隙条数
        for (int i = 0; i < len; i++) {
            sum += mValues[i];
            if (mValues[i] > 0)
                gapCount++;
        }
        float angle = 0;
        pieAngle = 360 - gapCount * ANGLE_DIS;
        for (int i = 0; i < len - 1; i++) {
            angles[i] = (float) (pieAngle * mValues[i] / sum);
            angle += angles[i];
        }
        if (mValues[len - 1] > 0)
            angles[len - 1] = pieAngle - angle;

        return angles;
    }

    int maxEms = 2;
    /**
     * 行间距 dp
     */
    private float lineSpace = 2;

    private String youhuaString(String str, Canvas canvas, float x, float y) {
        str = str.replaceAll("\n", "").replaceAll("\r", "");
        StringBuilder sb = new StringBuilder();
        boolean has = false;
        while (str.length() > maxEms) {
            String substring = str.substring(0, maxEms);
            canvas.drawText(substring, x, y, mTextPaint);
            str = str.substring(maxEms);
            y += getTextHeight(substring, mPieTextSize) + getResources().getDisplayMetrics().density * lineSpace;
            has = true;
        }
        if (!has) {
            canvas.drawText(str, x, y, mTextPaint);
        } else {
            canvas.drawText(str, str.length() == maxEms ? x : x + getTextWidth("重", mPieTextSize) / 2, y, mTextPaint);
        }
        return sb.toString();
    }

    private int getTextWidth(String str, float size) {
        int width = getTextRect(str.length() > 2 ? "中国" : str, size).width();
        return width;
    }

    private int getTextHeight(String str, float size) {
        int line = (str.length() / 2) + (str.length() % 2);
        int i = (int) (getTextRect("总", size).height() * line + ((line - 1) * getResources().getDisplayMetrics().density * lineSpace));
        return i;
    }

    private Rect getTextRect(String str, float size) {
        // str = youString(str);
        Rect textBound = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.getTextBounds(str, 0, str.length(), textBound);
        return textBound;
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 保留四位小数
     *
     * @param num
     * @return
     */
    public static double numDecimals(double num) {
        return ((int) (num * 10000)) * 1.0d / 10000;
    }
}