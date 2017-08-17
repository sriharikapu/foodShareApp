package com.fengnian.smallyellowo.foodie;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by chenglin on 2017-3-21.
 * 自定义的InputFilter ，用来限制英文中文输入字符限制。两个汉字代表一个英文。
 */

public class EnglishCharFilter implements InputFilter {
    int maxLen = 0;

    /**
     * 输入英文的最大长度 。比如你想要限制40个汉字，80个英文字符，传入的值就是80
     * 使用方式：mEdit.setFilters(new InputFilter[]{filter});
     */
    public EnglishCharFilter(int len) {
        maxLen = len;
    }


    @Override
    public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;

        while (count <= maxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= maxLen && sindex < src.length()) {
            char c = src.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            sindex--;
        }

        return src.subSequence(0, sindex);
    }

}
