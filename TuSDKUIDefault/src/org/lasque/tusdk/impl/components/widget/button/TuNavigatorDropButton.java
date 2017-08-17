/** 
 * TuSDK
 * TuNavigatorDropButton.java
 *
 * @author 		Clear
 * @Date 		2015-8-31 下午6:29:26 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.button;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class TuNavigatorDropButton extends Button {
	
	/**
	 * 图标尺寸
	 */
    protected int drawableWidth;
    
    /**
     * 图标位置
     */
    protected DrawablePositions drawablePosition;
    
    /**
     * 图文间距
     */
    protected int iconPadding = 5;

    // Cached to prevent allocation during onLayout
    Rect bounds;

    public static enum DrawablePositions {
        NONE,
        LEFT_AND_RIGHT,
        START,
        END
    }

    public TuNavigatorDropButton(Context context) 
    {
        super(context);
        bounds = new Rect();
    }

    public TuNavigatorDropButton(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
    	
        bounds = new Rect();
    }

    public TuNavigatorDropButton(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
    	
        bounds = new Rect();
    }
    
    /**
     * 图标和文字之间的间距
     * @param padding
     */
    public void setIconPadding(int padding) 
    {
        iconPadding = padding;
        requestLayout();
    }
    
    /**
     * 图标位置
     * @param position
     */
    public void setIconPosition(DrawablePositions position) 
    {
        this.drawablePosition = position;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) 
    {
        super.onLayout(changed, left, top, right, bottom);

        Paint textPaint = getPaint();
        String text = getText().toString();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        int textWidth = bounds.width();
        int factor = (drawablePosition == DrawablePositions.LEFT_AND_RIGHT) ? 2 : 1;
        int contentWidth = drawableWidth + iconPadding * factor + textWidth;
        int horizontalPadding = (int) ((getWidth() / 2.0) - (contentWidth / 2.0));

        setCompoundDrawablePadding(-horizontalPadding + iconPadding);

        switch (drawablePosition) {
            case START:
                setPadding(horizontalPadding, getPaddingTop(), 0, getPaddingBottom());
                break;

            case END:
                setPadding(0, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;

            case LEFT_AND_RIGHT:
                setPadding(horizontalPadding, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;

            default:
                setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) 
    {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

        if (left != null && right != null) {
            drawableWidth = left.getIntrinsicWidth() + right.getIntrinsicWidth();
            drawablePosition = DrawablePositions.LEFT_AND_RIGHT;
        } else if (left != null) {
            drawableWidth = left.getIntrinsicWidth();
            drawablePosition = DrawablePositions.START;
        } else if (right != null) {
            drawableWidth = right.getIntrinsicWidth();
            drawablePosition = DrawablePositions.END;
        } else {
            drawablePosition = DrawablePositions.NONE;
        }

        requestLayout();
    }
}