/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fengnian.smallyellowo.foodie.emoji;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String ee_1 = "[馋]";
    public static final String ee_2 = "[赞]";
    public static final String ee_3 = "[喜欢]";
    public static final String ee_4 = "[嘚瑟]";
    public static final String ee_5 = "[哭]";
    public static final String ee_6 = "[害羞]";
    public static final String ee_7 = "[生气]";
    public static final String ee_8 = "[大笑]";
    public static final String ee_9 = "[难吃]";
    public static final String ee_10 = "[笑哭]";
    public static final String ee_11 = "[汗]";
    public static final String ee_12 = "[鄙视]";
    public static final String ee_13 = "[委屈]";
    public static final String ee_14 = "[羡慕]";
    public static final String ee_15 = "[吃]";
    public static final String ee_16 = "[好饱]";

    private static final Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
        for (EaseEmojicon emojicon : emojicons) {
            addPattern(emojicon.getEmojiText(), emojicon.getIcon());
        }
//	    EaseUI.EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
//	    if(emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null){
//	        for(Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()){
//	            addPattern(entry.getKey(), entry.getValue());
//	        }
//	    }

    }

    /**
     * add text and icon to the map
     * @param emojiText-- text of emoji
     * @param icon -- resource id or local path
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            if (activity != null && activity.isFinishing()) {
                return false;
            }
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                if (activity != null && activity.isFinishing()) {
                    return false;
                }
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)){
                    if (activity != null && activity.isFinishing()) {
                        return false;
                    }
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end()) {
                        spannable.removeSpan(span);
                    } else {
                        set = false;
                        break;
                    }
                }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
