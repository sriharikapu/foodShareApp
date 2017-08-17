package com.fengnian.smallyellowo.foodie.emoji;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.PreferencesData;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.ViewPageNavigationDotLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-2-20.
 */

public class EmojiPanelLayout extends LinearLayout implements View.OnLayoutChangeListener {
    public static final int EMOJI_CUSTOM = 1;
    public static final int EMOJI_SYSTEM = 2;

    //这种模式键盘不闪烁，但是外部的内容View 要做相应的处理 具体问 by chenglin
    public static final int MODE_LINEAR_WEIGHT = 10;
    //普通模式
    public static final int MODE_NORMAL = 20;

    private View emojiPanelView;
    private LinearLayout emojiShowView;
    private ImageView customEmojiBtn, sysEmojiBtn;
    private EmojiViewPager customEmojiViewPager;
    private EmojiViewPager sysEmojiViewPager;
    private ViewPageNavigationDotLayout navigationDot;
    private EmojiPanelLayout.onEmojiItemListener mItemClickListener;
    private CustomEmojiEditText mAttachedEdit;
    private View activityRootView;
    private ImageView mAttachedEmojiBtn;
    private onKeyboardStateListener mOnKeyboardStateListener;
    private int mEmojiMode;

    public void setNoShowThis(boolean noShowThis) {
        this.noShowThis = noShowThis;
    }

    private boolean noShowThis;

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    private View mSendBtn;

    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            final View decorView = ((Activity) getContext()).getWindow().getDecorView();
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            int displayHeight = rect.bottom;
            int height = decorView.getHeight();
            int keyboardHeight = height - displayHeight;
            if (keyboardHeight > DisplayUtil.dip2px(150f)) {//假设键盘最小高度
                PreferencesData.setEmojiKeyboardHeight(keyboardHeight);
                FFLogUtil.d("keyboardHeight", "keyboardHeight = " + keyboardHeight);
            }
        }
    };


    public EmojiPanelLayout(Context context) {
        super(context);
        init();
    }

    public EmojiPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setEmojiItemClickListener(EmojiPanelLayout.onEmojiItemListener listener) {
        mItemClickListener = listener;

        if (customEmojiViewPager != null) {
            List<EmojiGridView> list = customEmojiViewPager.getEmojiGridViewList();
            if (list != null && list.size() > 0) {
                for (EmojiGridView emojiGridView : list) {
                    emojiGridView.setEmojiItemClickListener(mItemClickListener);
                }
            }
        }
        if (sysEmojiViewPager != null) {
            List<EmojiGridView> list = sysEmojiViewPager.getEmojiGridViewList();
            if (list != null && list.size() > 0) {
                for (EmojiGridView emojiGridView : list) {
                    emojiGridView.setEmojiItemClickListener(mItemClickListener);
                }
            }
        }
    }

    /**
     * 设置发送按钮的点击监听
     */
    public void setOnSendClickListener(View.OnClickListener listener) {
        mSendBtn.setVisibility(View.VISIBLE);
        mSendBtn.setOnClickListener(listener);
    }

    public void setOnKeyboardStateListener(onKeyboardStateListener listener) {
        this.mOnKeyboardStateListener = listener;
        mEmojiMode = MODE_LINEAR_WEIGHT;
    }

    public void setEmojiLayoutMode(int mode) {
        mEmojiMode = mode;
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setVisibility(View.GONE);//默认是隐藏

        //监听键盘的高度，记得一定要remove监听
        getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        //嵌套入整个输入法面板
        emojiPanelView = View.inflate(getContext(), R.layout.emoji_input_panel, null);
        addView(emojiPanelView);

        //初始化显示emoji
        emojiShowView = (LinearLayout) findViewById(R.id.emoji_show_view);
        customEmojiBtn = (ImageView) findViewById(R.id.custom_emoji);
        sysEmojiBtn = (ImageView) findViewById(R.id.sys_emoji);
        navigationDot = (ViewPageNavigationDotLayout) findViewById(R.id.navigation_dot);
        activityRootView = ((Activity) getContext()).findViewById(android.R.id.content);
        mSendBtn = findViewById(R.id.send_btn);

        initOnClickListener();
        customEmojiBtn.performClick();
        activityRootView.addOnLayoutChangeListener(this);
        mSendBtn.setVisibility(View.GONE);//发送按钮默认隐藏

        setEmojiItemClickListener(new EmojiPanelLayout.onEmojiItemListener() {
            @Override
            public void onClick(EmojiGridView.EmojiGridItem emojiGridItem) {
                if (mAttachedEdit != null) {
                    mAttachedEdit.setCustomText(emojiGridItem.emojiString);
                }
            }
        });
    }

    private void initOnClickListener() {
        //自定义emoji点击按钮
        customEmojiBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnSelected(EMOJI_CUSTOM);
                final int pageCount = EaseDefaultEmojiconDatas.getCustomEmojiShowPage();
                if (customEmojiViewPager == null) {
                    customEmojiViewPager = new EmojiViewPager(getContext());

                    List<EmojiGridView> list = new ArrayList<EmojiGridView>();
                    for (int index = 0; index < pageCount; index++) {
                        EmojiGridView emojiGridView = new EmojiGridView(getContext(), index, EMOJI_CUSTOM);
                        if (mItemClickListener != null) {
                            emojiGridView.setEmojiItemClickListener(mItemClickListener);
                        }
                        if (mAttachedEdit != null) {
                            emojiGridView.setAttachedEditText(mAttachedEdit);
                        }
                        list.add(emojiGridView);
                    }
                    customEmojiViewPager.setEmojiGridViewList(list);

                    int height = customEmojiViewPager.getEmojiShowViewHeight();
                    emojiShowView.addView(customEmojiViewPager, new ViewGroup.LayoutParams(-1, height));
                }

                navigationDot.setDotCount(pageCount);
                navigationDot.setSelected(customEmojiViewPager.getCurrentItem());

                if (sysEmojiViewPager != null) {
                    sysEmojiViewPager.setVisibility(View.GONE);
                }
                if (customEmojiViewPager != null) {
                    customEmojiViewPager.setVisibility(View.VISIBLE);
                }
            }
        });

        //系统emoji点击按钮
        sysEmojiBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnSelected(EMOJI_SYSTEM);
                final int pageCount = EmojiSysList.getSyeEmojiShowPage();

                if (sysEmojiViewPager == null) {
                    sysEmojiViewPager = new EmojiViewPager(getContext());

                    List<EmojiGridView> list = new ArrayList<EmojiGridView>();
                    for (int index = 0; index < pageCount; index++) {
                        EmojiGridView emojiGridView = new EmojiGridView(getContext(), index, EMOJI_SYSTEM);
                        if (mItemClickListener != null) {
                            emojiGridView.setEmojiItemClickListener(mItemClickListener);
                        }
                        if (mAttachedEdit != null) {
                            emojiGridView.setAttachedEditText(mAttachedEdit);
                        }
                        list.add(emojiGridView);
                    }
                    sysEmojiViewPager.setEmojiGridViewList(list);
                    navigationDot.setViewPager(sysEmojiViewPager);

                    int height = sysEmojiViewPager.getEmojiShowViewHeight();
                    emojiShowView.addView(sysEmojiViewPager, new ViewGroup.LayoutParams(-1, height));
                }

                navigationDot.setDotCount(pageCount);
                navigationDot.setSelected(sysEmojiViewPager.getCurrentItem());

                if (sysEmojiViewPager != null) {
                    sysEmojiViewPager.setVisibility(View.VISIBLE);

                    sysEmojiViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            navigationDot.setSelected(position);
                        }
                    });
                }
                if (customEmojiViewPager != null) {
                    customEmojiViewPager.setVisibility(View.GONE);
                }
            }
        });


    }

    private void setBtnSelected(int emojiType) {
        if (emojiType == EMOJI_CUSTOM) {
            customEmojiBtn.setBackgroundResource(R.color.gray_bg_new);
            sysEmojiBtn.setBackgroundResource(0);
        } else if (emojiType == EMOJI_SYSTEM) {
            customEmojiBtn.setBackgroundResource(0);
            sysEmojiBtn.setBackgroundResource(R.color.gray_bg_new);
        }
    }

    /**
     * 设置emoji面板附属的EditText，用来删除上面的字符
     */
    public void setAttachedEditText(CustomEmojiEditText editText) {
        if (editText == null) {
            return;
        }
        mAttachedEdit = editText;
        if (customEmojiViewPager != null) {
            List<EmojiGridView> list = customEmojiViewPager.getEmojiGridViewList();
            if (list != null && list.size() > 0) {
                for (EmojiGridView emojiGridView : list) {
                    emojiGridView.setAttachedEditText(editText);
                }
            }
        }
        if (sysEmojiViewPager != null) {
            List<EmojiGridView> list = sysEmojiViewPager.getEmojiGridViewList();
            if (list != null && list.size() > 0) {
                for (EmojiGridView emojiGridView : list) {
                    emojiGridView.setAttachedEditText(editText);
                }
            }
        }
    }

    /**
     * 设置emoji面板附属的emoji显示的按钮
     */
    public void setAttachedEmojiBtn(ImageView imageView) {
        if (imageView == null) {
            return;
        }

        mAttachedEmojiBtn = imageView;
        int resId = R.drawable.emoji_panel_show;
        mAttachedEmojiBtn.setTag(resId);
        mAttachedEmojiBtn.setImageResource(resId);

        mAttachedEmojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int panelDrawableId = 0;
                final int drawableId = (int) mAttachedEmojiBtn.getTag();

                if (drawableId == R.drawable.emoji_panel_show) {
                    if (mOnKeyboardStateListener != null) {
                        mOnKeyboardStateListener.onFixLayoutParam(true);
                    }

                    panelDrawableId = R.drawable.emoji_panel_keyboard;
                    mAttachedEmojiBtn.setImageResource(panelDrawableId);
                    mAttachedEmojiBtn.setTag(panelDrawableId);

                    //操作emoji面板
                    EmojiUtils.hideKeyboard(v.getContext(), mAttachedEdit);

                    if (mEmojiMode == MODE_LINEAR_WEIGHT) {
                        showEmojiPanel(true);
                    } else {
                        FFUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmojiPanel(true);
                            }
                        }, 180);
                    }
                } else if (drawableId == R.drawable.emoji_panel_keyboard) {
                    panelDrawableId = R.drawable.emoji_panel_show;
                    mAttachedEmojiBtn.setImageResource(panelDrawableId);
                    mAttachedEmojiBtn.setTag(panelDrawableId);
                    if (mOnKeyboardStateListener != null) {
                        mOnKeyboardStateListener.onRequestSoftVis();
                    }
                    EmojiUtils.openKeyboard(v.getContext(), mAttachedEdit);
                }
            }
        });
    }

    //记得要remove ，要不内存泄漏
    private void onDestroy() {
        if (mGlobalLayoutListener != null) {
            getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
            mGlobalLayoutListener = null;
        }
        activityRootView.removeOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        int keyHeight = DisplayUtil.screenHeight / 3;

        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            onKeyboardState(true);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            onKeyboardState(false);
        }
    }

    public void showEmojiPanel(boolean isShow) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (isShow) {
            params.height = EmojiUtils.getEmojiKeyboardHeight();
        } else {
            params.height = 0;
        }
        setLayoutParams(params);
        setVisibility(View.VISIBLE);
    }

    public interface onEmojiItemListener {
        void onClick(EmojiGridView.EmojiGridItem emojiGridItem);
    }

    //键盘是隐藏还是显示
    public void onKeyboardState(boolean isShow) {
        if(!isShow){
            noShowThis = false;
        }
        if(noShowThis){
            return;
        }
        if (mOnKeyboardStateListener != null) {
            mOnKeyboardStateListener.onKeyboardShow(isShow);
        }

        if (isShow) {
            showEmojiPanel(false);
            int panelDrawableId = R.drawable.emoji_panel_show;
            mAttachedEmojiBtn.setImageResource(panelDrawableId);
            mAttachedEmojiBtn.setTag(panelDrawableId);
        } else {
            if (mOnKeyboardStateListener != null) {
                mOnKeyboardStateListener.onFixLayoutParam(false);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    public static abstract class onKeyboardStateListener {

        /**
         * 键盘是显示还是隐藏
         */
        public abstract void onKeyboardShow(boolean isShow);

        /**
         * 在这里固定键盘弹出时的layout布局；
         * 如果isFix = true 就是需要固定住键盘上方的布局；
         * 否则就是释放键盘上方的布局
         */
        public abstract void onFixLayoutParam(boolean isFix);

        public void onRequestSoftVis() {
        }

    }
}

