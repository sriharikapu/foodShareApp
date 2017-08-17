/*
 * @author http://blog.csdn.net/singwhatiwanna
 */
package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.homepage.TabInfo;

import java.util.List;


/**
 * 这是个选项卡式的控件，会随着viewpager的滑动而滑动
 */
public class TitleIndicator extends LinearLayout implements View.OnClickListener,
        OnFocusChangeListener {

    private static final float FOOTER_LINE_HEIGHT = 4.0f;

    private static final int FOOTER_COLOR = 0xFFFFC445;

    private static final float FOOTER_TRIANGLE_HEIGHT = 10;
    public static ImageView tips;
    private final int BSSEEID = 0xffff00;
    private int mCurrentScroll = 0;
    //选项卡列表
    private List<TabInfo> mTabs;
    //选项卡所依赖的viewpager
    private ViewPager mViewPager;
    //选项卡普通状态下的字体颜色
    private ColorStateList mTextColor;
    private ColorStateList mTextColorSelected;
    //普通状态和选中状态下的字体大小
    private float mTextSizeNormal;
    private float mTextSizeSelected;
    private Path mPath = new Path();
    private Paint mPaintFooterLine;
    private Paint mPaintFooterTriangle;
    private float mFooterTriangleHeight;
    //滚动条的高度
    private float mFooterLineHeight;
    //当前选项卡的下标，从0开始
    private int mSelectedTab = 0;
    ;
    private Context mContext;
    private boolean mChangeOnClick = true;
    private int mCurrID = 0;
    //单个选项卡的宽度
    private int mPerItemWidth = 0;
    //表示选项卡总共有几个
    private int mTotal = 0;
    private LayoutInflater mInflater;

//	private static TitleIndicator instance;


    /**
     * Default constructor
     */
    public TitleIndicator(Context context) {
        super(context);
        initDraw(FOOTER_LINE_HEIGHT, FOOTER_COLOR);
    }

    /**
     * The contructor used with an inflater
     *
     * @param context
     * @param attrs
     */
    public TitleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setOnFocusChangeListener(this);
        mContext = context;
        // Retrieve styles attributs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleIndicator);
        // Retrieve the colors to be used for this view and apply them.
        int footerColor = a.getColor(R.styleable.TitleIndicator_footerColor, FOOTER_COLOR);
        mTextColor = a.getColorStateList(R.styleable.TitleIndicator_textColor);
        mTextColorSelected = a.getColorStateList(R.styleable.TitleIndicator_textColorSelected);
        mTextSizeNormal = a.getDimension(R.styleable.TitleIndicator_textSizeNormal, 0);
        mTextSizeSelected = a.getDimension(R.styleable.TitleIndicator_textSizeSelected, mTextSizeNormal);
        mFooterLineHeight = a.getDimension(R.styleable.TitleIndicator_footerLineHeight,
                FOOTER_LINE_HEIGHT);
        mFooterTriangleHeight = a.getDimension(R.styleable.TitleIndicator_footerTriangleHeight,
                FOOTER_TRIANGLE_HEIGHT);

        initDraw(mFooterLineHeight, footerColor);
        a.recycle();
    }

    /**
     * Initialize draw objects
     */
    private void initDraw(float footerLineHeight, int footerColor) {
//    	instance = this;
        mPaintFooterLine = new Paint();
        mPaintFooterLine.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterLine.setStrokeWidth(footerLineHeight);
        mPaintFooterLine.setColor(footerColor);
        mPaintFooterTriangle = new Paint();
        mPaintFooterTriangle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterTriangle.setColor(footerColor);
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * @这个是核心函数，选项卡是用canvas画出来的。所有的invalidate方法均会触发onDraw
     * 大意是这样的：当页面滚动的时候，会有一个滚动距离，然后onDraw被触发后，
     * 就会在新位置重新画上滚动条（其实就是画线）
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //下面是计算本次滑动的距离
        float scroll_x = 0;
        if (mTotal != 0) {
            mPerItemWidth = getWidth() / mTotal;
            int tabID = mSelectedTab;
            scroll_x = (mCurrentScroll - ((tabID) * (FFUtils.getDisWidth() + mViewPager.getPageMargin()))) * getWidth() / FFUtils.getDisWidth() / mTotal;
        } else {
            mPerItemWidth = getWidth();
            scroll_x = mCurrentScroll;
        }
        //下面就是如何画线了
        Path path = mPath;
        path.rewind();
        float offset = FFUtils.getPx(10);
        float left_x = mSelectedTab * mPerItemWidth + offset + scroll_x;
        float right_x = (mSelectedTab + 1) * mPerItemWidth - offset + scroll_x;
        float top_y = getHeight() - mFooterLineHeight - mFooterTriangleHeight;
        float bottom_y = getHeight() - mFooterLineHeight;

//        path.moveTo(left_x, top_y + 1f);
//        path.lineTo(right_x, top_y + 1f);
//        path.lineTo(right_x, bottom_y + 1f);
//        path.lineTo(left_x, bottom_y + 1f);
//        path.close();
        RectF rec = new RectF();
        rec.set((int) left_x, (int) top_y, (int) right_x, (int) bottom_y);
        canvas.drawRoundRect(rec, FFUtils.getPx(2), FFUtils.getPx(2), mPaintFooterTriangle);
    }

    /**
     * 获取指定下标的选项卡的标题
     */
    private String getTitle(int pos) {
        // Set the default title
        String title = "title " + pos;
        // If the TitleProvider exist
        if (mTabs != null && mTabs.size() > pos) {
            title = mTabs.get(pos).getName();
        }
        return title;
    }

    /**
     * 获取指定下标的选项卡的图标资源id（如果设置了图标的话）
     */
    private int getIcon(int pos) {
        int ret = 0;
        if (mTabs != null && mTabs.size() > pos) {
            ret = mTabs.get(pos).getIcon();
        }
        return ret;
    }

    private boolean hasTips(int pos) {
        boolean ret = false;
        if (mTabs != null && mTabs.size() > pos) {
            ret = mTabs.get(pos).hasTips;
        }
        return ret;
    }

    //当页面滚动的时候，重新绘制滚动条
    public void onScrolled(int h) {
        mCurrentScroll = h;
        invalidate();
    }

    //当页面切换的时候，重新绘制滚动条
    public synchronized void onSwitched(int position) {
        if (mSelectedTab == position) {
            return;
        }
        setCurrentTab(position);
        invalidate();
    }

    //初始化选项卡
    public void init(int startPos, List<TabInfo> tabs, ViewPager mViewPager) {
        mCurrID = 0;
        removeAllViews();
        this.mViewPager = mViewPager;
        this.mTabs = tabs;
        this.mTotal = tabs.size();
        for (int i = 0; i < mTotal; i++) {
            add(i, getTitle(i), getIcon(i), hasTips(i));
        }
        setCurrentTab(startPos);
        invalidate();
    }

    public void updateChildTips(int postion, boolean showTips) {
        View child = getChildAt(postion);
        if (child == null){
            return;
        }
        final ImageView tips = (ImageView) child.findViewById(R.id.tab_title_tips);
        if (showTips) {
            tips.setVisibility(View.VISIBLE);
        } else {
            tips.setVisibility(View.GONE);
        }
        child.findViewById(R.id.tab_title_sum).setVisibility(View.GONE);
    }

//    public void updateChildNotifys(int postion, int notifys, boolean hasPoint) {
//        View child = getChildAt(postion);
//        final TextView tip_sum = (TextView) child.findViewById(R.id.tab_title_sum);
//        final ImageView tip_point = (ImageView) child.findViewById(R.id.tab_title_tips);
//        if (notifys > 0) {
//            tip_sum.setVisibility(View.VISIBLE);
//            tip_point.setVisibility(View.GONE);
//            tip_sum.setText(notifys < 100 ? String.valueOf(notifys) : "99+");
//        } else {
//            if (hasPoint) {
//                tip_point.setVisibility(View.VISIBLE);
//            } else {
//                tip_point.setVisibility(View.GONE);
//            }
//            tip_sum.setText(String.valueOf(notifys));
//            tip_sum.setVisibility(View.GONE);
//        }
//    }

    protected void add(int index, String label, int icon, boolean hasTips) {
        View tabIndicator = mInflater.inflate(R.layout.title_flow_indicator, this, false);
        TextView tv = (TextView) tabIndicator.findViewById(R.id.tab_title1);
        if (mTextColor != null&&tv!=null) {
            tv.setTextColor(mTextColor);
        }
        if (mTextSizeNormal > 0&&tv!=null) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal);
        }
        if(tv!=null)
        tv.setText(label);
        if (icon > 0&&tv!=null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        }
        if (index == 0) {
            tips = (ImageView) tabIndicator.findViewById(R.id.tab_title_tips);
        }
        tabIndicator.setId(BSSEEID + (mCurrID++));
        tabIndicator.setOnClickListener(this);
        LayoutParams lP = (LayoutParams) tabIndicator.getLayoutParams();
        lP.gravity = Gravity.CENTER_VERTICAL;
        addView(tabIndicator);
    }

    public void setDisplayedPage(int index) {
        mSelectedTab = index;
    }

    public boolean getChangeOnClick() {
        return mChangeOnClick;
    }

    public void setChangeOnClick(boolean changeOnClick) {
        mChangeOnClick = changeOnClick;
    }

    public static interface OnCurrentTagCheckedListener {
        void onCheck(int position, boolean isShowProgressDialog);
    }

    public void setCurrentTagCheckedListener(OnCurrentTagCheckedListener currentTagCheckedListener) {
        this.currentTagCheckedListener = currentTagCheckedListener;
    }

    OnCurrentTagCheckedListener currentTagCheckedListener;

    @Override
    public void onClick(View v) {
        int position = v.getId() - BSSEEID;
        if (currentTagCheckedListener != null) {
            currentTagCheckedListener.onCheck(position,position != mSelectedTab);
        }
        if (position != mSelectedTab) {
            setCurrentTab(position);
        }
    }

    public int getTabCount() {
        int children = getChildCount();
        return children;
    }

    //设置当前选项卡
    public synchronized void setCurrentTab(int index) {
        if (index < 0 || index >= getTabCount()) {
            return;
        }
        View oldTab = getChildAt(mSelectedTab);
        if (oldTab == null){
            return;
        }
        oldTab.setSelected(false);
        setTabTextSize(oldTab, false);

        mSelectedTab = index;
        View newTab = getChildAt(mSelectedTab);
        newTab.setSelected(true);
        setTabTextSize(newTab, true);

        mViewPager.setCurrentItem(mSelectedTab);
        invalidate();
    }

    private void setTabTextSize(View tab, boolean selected) {
        TextView tv = (TextView) tab.findViewById(R.id.tab_title1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                selected ? mTextSizeSelected : mTextSizeNormal);
        tv.setTextColor(
                selected ? mTextColorSelected : mTextColor);
    }

    public View getTabViewByIndex(int index) {
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (i == index) {
                    return getChildAt(i);
                }
            }
        }
        return null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus && getTabCount() > 0) {
            getChildAt(mSelectedTab).requestFocus();
            return;
        }

        if (hasFocus) {
            int i = 0;
            int numTabs = getTabCount();
            while (i < numTabs) {
                if (getChildAt(i) == v) {
                    setCurrentTab(i);
                    break;
                }
                i++;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mCurrentScroll == 0 && mSelectedTab != 0) {
            mCurrentScroll = (getWidth() + mViewPager.getPageMargin()) * mSelectedTab;
        }
    }
}
