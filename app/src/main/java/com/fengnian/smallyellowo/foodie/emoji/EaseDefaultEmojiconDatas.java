package com.fengnian.smallyellowo.foodie.emoji;


import com.fengnian.smallyellowo.foodie.R;

public class EaseDefaultEmojiconDatas {

    public final static String[] emojis = new String[]{
            EaseSmileUtils.ee_1,
            EaseSmileUtils.ee_2,
            EaseSmileUtils.ee_3,
            EaseSmileUtils.ee_4,
            EaseSmileUtils.ee_5,
            EaseSmileUtils.ee_6,
            EaseSmileUtils.ee_7,
            EaseSmileUtils.ee_8,
            EaseSmileUtils.ee_9,
            EaseSmileUtils.ee_10,
            EaseSmileUtils.ee_11,
            EaseSmileUtils.ee_12,
            EaseSmileUtils.ee_13,
            EaseSmileUtils.ee_14,
            EaseSmileUtils.ee_15,
            EaseSmileUtils.ee_16
    };

    public final static int[] icons = new int[]{
            R.drawable.ee_1,
            R.drawable.ee_2,
            R.drawable.ee_3,
            R.drawable.ee_4,
            R.drawable.ee_5,
            R.drawable.ee_6,
            R.drawable.ee_7,
            R.drawable.ee_8,
            R.drawable.ee_9,
            R.drawable.ee_10,
            R.drawable.ee_11,
            R.drawable.ee_12,
            R.drawable.ee_13,
            R.drawable.ee_14,
            R.drawable.ee_15,
            R.drawable.ee_16
    };

    public final static int[] custom_show_icons = new int[]{
            R.drawable.emoji_custom_1,
            R.drawable.emoji_custom_2,
            R.drawable.emoji_custom_3,
            R.drawable.emoji_custom_4,
            R.drawable.emoji_custom_5,
            R.drawable.emoji_custom_6,
            R.drawable.emoji_custom_7,
            R.drawable.emoji_custom_8,
            R.drawable.emoji_custom_9,
            R.drawable.emoji_custom_10,
            R.drawable.emoji_custom_11,
            R.drawable.emoji_custom_12,
            R.drawable.emoji_custom_13,
            R.drawable.emoji_custom_14,
            R.drawable.emoji_custom_15,
            R.drawable.emoji_custom_16
    };

    private static final EaseEmojicon[] DATA = createData();

    private static EaseEmojicon[] createData() {
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for (int i = 0; i < icons.length; i++) {
            datas[i] = new EaseEmojicon(icons[i], emojis[i], EaseEmojicon.Type.NORMAL);
        }
        return datas;
    }

    public static EaseEmojicon[] getData() {
        return DATA;
    }


    public static int getCustomEmojiShowPage() {
        int page = emojis.length / EmojiGridView.TOTAL_NUM;
        if (emojis.length % EmojiGridView.TOTAL_NUM > 0) {
            page = page + 1;
        }
        return page;
    }
}
