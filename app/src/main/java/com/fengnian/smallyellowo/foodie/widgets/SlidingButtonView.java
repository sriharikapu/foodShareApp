package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.fan.framework.utils.FFUtils;

public class SlidingButtonView extends HorizontalScrollView {
//    private View mTextView_Delete;
    private int mScrollWidth;
    private IonSlidingButtonListener mIonSlidingButtonListener;

    public Boolean getOpen() {
        return isOpen;
    }

    private Boolean isOpen = false;
    private Boolean once = false;



    MyRecyclerView recycelView;

    public void setParent(MyRecyclerView recycelView){
        this.recycelView = recycelView;
    }


    public SlidingButtonView(Context context) {
        this(context, null);
    }

    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!once) {
//            mTextView_Delete = findViewById(R.id.iv_del);
            once = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            mScrollWidth = FFUtils.getPx(45);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIonSlidingButtonListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 按滚动条被拖动距离判断关闭或打开菜单
     */
    public void changeScrollx() {
        if (getScrollX() >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            mIonSlidingButtonListener.onMenuIsOpen(this);
        } else {
            this.smoothScrollTo(0, 0);
            isOpen = false;
        }
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        recycelView.setFocusView(this);
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(mScrollWidth, 0);
        isOpen = true;
        mIonSlidingButtonListener.onMenuIsOpen(this);
    }

    /**
     * 关闭菜单
     * compile 'com.jakewharton:butterknife:7.0.1'
     */
    public void closeMenu() {
        if (!isOpen) {
            return;
        }
        recycelView.setFocusView(null);
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    public void setSlidingButtonListener(IonSlidingButtonListener listener) {
        mIonSlidingButtonListener = listener;
    }

    public interface IonSlidingButtonListener {
        void onMenuIsOpen(View view);

        void onDownOrMove(SlidingButtonView slidingButtonView);
    }
}