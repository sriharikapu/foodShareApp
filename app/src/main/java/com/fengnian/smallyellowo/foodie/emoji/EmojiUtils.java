package com.fengnian.smallyellowo.foodie.emoji;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fengnian.smallyellowo.foodie.appbase.PreferencesData;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-2-20.
 */

public class EmojiUtils {
    /**
     * 根据unicode 编码返回emoji字符
     */
    public static String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context, View v) {
        if (v == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 打开虚拟键盘
     */
    public static void openKeyboard(Context context, View v) {
        if (v == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(v, 0);
    }

    /**
     * 得到当前的键盘高度，如果为获取到，给一个默认值
     */
    public static int getEmojiKeyboardHeight() {
        int height = PreferencesData.getEmojiKeyboardHeight();
        if (height > 0) {
            return height;
        } else {
            return DisplayUtil.dip2px(230f);
        }
    }

    /**
     * 是否包含eomoji 表情符
     */
    public static boolean isContainsEmoji(CharSequence text) {
        if (text != null && text.toString().contains("[") && text.toString().contains("]")) {
            return true;
        } else {
            return false;
        }
    }
}
