package com.fengnian.smallyellowo.foodie.emoji;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;

import com.fengnian.smallyellowo.foodie.appbase.BaseTextView;

/**
 * Created by chenglin on 2017-2-21.
 */

public class CustomEmojiTextView extends BaseTextView {
    public CustomEmojiTextView(Context context) {
        super(context);
    }

    public CustomEmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置自定义的emoji文本
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text == null || !EmojiUtils.isContainsEmoji(text)) {
            super.setText(text, type);
            return;
        }

        Spannable span = EaseSmileUtils.getSmiledText(getContext(), text);
        super.setText(span, type);
    }
}
