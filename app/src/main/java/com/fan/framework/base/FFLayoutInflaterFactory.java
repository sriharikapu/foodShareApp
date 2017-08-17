package com.fan.framework.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiEditText;
import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiTextView;

/**
 * Created by Administrator on 2017-3-10.
 */

public class FFLayoutInflaterFactory implements LayoutInflater.Factory {
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        boolean isEmojiEnable = false;
        if (context instanceof FFBaseActivity){
            FFBaseActivity activity = (FFBaseActivity) context;
            isEmojiEnable = activity.isEmojiEnable();
        }

        if (isEmojiEnable){
            if (name.equals("TextView")) {
                return new CustomEmojiTextView(context, attrs);
            }
            if (name.equals("EditText")) {
                return new CustomEmojiEditText(context, attrs);
            }
        }

        return null;
    }
}
