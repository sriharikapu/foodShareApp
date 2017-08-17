package com.fengnian.smallyellowo.foodie.widgets;

/**
 * Created by Administrator on 2017-4-7.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;

/**
 * 验证码输入View
 * Created by fhp on 14/12/18.
 */
public class VerifyCodeView extends View {
    StringBuilder verifyCodeBuilder;
    //一个字符或横线占用的宽度
    private int characterWidth;
    //一个数字后中间的间隔
    private int centerSpacing;
    //两字符间隔
    private int characterSpacing;
    private int textSize;
    Paint textPaint;
    float textBaseY;
    public VerifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //能获取焦点才能弹出软键盘
        setFocusableInTouchMode(true);
        verifyCodeBuilder = new StringBuilder(6);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        centerSpacing=getResources().getDimensionPixelSize(R.dimen.padding_middle);
        characterSpacing=getResources().getDimensionPixelSize(R.dimen.padding_middle);
        textSize=getResources().getDimensionPixelSize(R.dimen.code_text_size);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在View上点击时弹出软键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.viewClicked(this);
        imm.showSoftInput(this, 0);
        return super.onTouchEvent(event);
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        //定义软键盘样式为数字键盘
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        return super.onCreateInputConnection(outAttrs);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //接收按键事件，67是删除键(backspace),7-16就是0-9
        if (keyCode == 67 && verifyCodeBuilder.length() > 0) {
            verifyCodeBuilder.deleteCharAt(verifyCodeBuilder.length() - 1);
            //重新绘图
            invalidate();
        } else if (keyCode >= 7 && keyCode <= 16 && verifyCodeBuilder.length() < 6) {
            verifyCodeBuilder.append(keyCode - 7);
            invalidate();
        }
        //到了6位自动隐藏软键盘
        if (verifyCodeBuilder.length() >= 6) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 获取输入的验证码
     * @return
     */
    public String getVerifyCodeStr() {
        return verifyCodeBuilder.toString();
    }
    /**
     * 设置显示的验证码
     * @param verifyCode
     */
    public void setVerifyCode(String verifyCode) {
        if(verifyCodeBuilder.length()>0) {
            verifyCodeBuilder.delete(0, verifyCodeBuilder.length());
        }
        verifyCodeBuilder.append(verifyCode);
        invalidate();
    }

    /**
     * 删除验证码
     */
    public void  deleVerifyCode(){
        verifyCodeBuilder.delete(0,verifyCodeBuilder.length());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算一个字符的宽度
        if(characterWidth==0) {
            int w = getWidth() - getPaddingLeft() - getPaddingRight();
            characterWidth=(w-centerSpacing-4*characterSpacing)/6;
        }
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        if(textBaseY==0)
            textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
        //写已输入的验证码
        if(verifyCodeBuilder.length()>0) {
            textPaint.setColor(getResources().getColor(R.color.title_text_color));
            String verifyCodeStr = getVerifyCodeStr();
            char[] chars = verifyCodeStr.toCharArray();
            int x,y=(int)textBaseY;
            for(int i=0;i<chars.length;i++) {
                //计算x,y
                if(i<=2) {
                    x=(characterWidth+characterSpacing)*i+characterWidth/2;
                }else{
                    x=(characterWidth+characterSpacing)*2+characterWidth+centerSpacing+
                            (characterWidth+characterSpacing)*(i-3)+characterWidth/2;
                }
                canvas.drawText(chars,i,1,x,y+FFUtils.getPx(2),textPaint);
            }
        }
        //画白线
        if(verifyCodeBuilder.length()<6) {
            for(int i=verifyCodeBuilder.length();i<6;i++) {
                textPaint.setColor(getResources().getColor(R.color.title_text_color));
                textPaint.setStrokeWidth(FFUtils.getPx(3));
                int x,y=(int)textBaseY;
                if(i<=2) {
                    x=(characterWidth+characterSpacing)*i;
                }else{
                    x=(characterWidth+characterSpacing)*2+characterWidth+centerSpacing+
                            (characterWidth+characterSpacing)*(i-3);
                }
                canvas.drawLine(x,y,x+characterWidth,y,textPaint);
            }
        }
    }
}
