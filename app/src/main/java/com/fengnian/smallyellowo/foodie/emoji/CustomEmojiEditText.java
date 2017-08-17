package com.fengnian.smallyellowo.foodie.emoji;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.fengnian.smallyellowo.foodie.appbase.BaseEditText;

/**
 * Created by chenglin on 2017-2-21.
 */

public class CustomEmojiEditText extends BaseEditText {
    private boolean mIsFinishActivity = false;//按back键关闭输入法时是否关闭activity

    public CustomEmojiEditText(Context context) {
        super(context);
    }

    public CustomEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //处理手动输入Emoji表情时，可以自动转化的问题
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!TextUtils.isEmpty(text) && EmojiUtils.isContainsEmoji(text)) {//简单处理下效率问题
            EaseSmileUtils.addSmiles(getContext(), getText());
        }
    }

    /**
     * 设置自定义的emoji文本
     */
    public void setCustomText(final String text) {
        int index = getSelectionStart();
        if (index < 0 || index >= length()) {
            append(text);
        } else {
            getEditableText().insert(index, text);
        }
    }

    /**
     * 设置按back键关闭输入法时，是否也要关闭当前的Activity
     */
    public void setWhenBackKeyboardIsFinishActivity(boolean isFinishActivity) {
        mIsFinishActivity = isFinishActivity;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (mIsFinishActivity) {
            Activity activity = (Activity) getContext();
            if (activity != null && !activity.isFinishing() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                        state.startTracking(event, this);
                        return true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP
                            && !event.isCanceled() && state.isTracking(event)) {
                        EmojiUtils.hideKeyboard(activity, CustomEmojiEditText.this);
                        activity.finish();
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
