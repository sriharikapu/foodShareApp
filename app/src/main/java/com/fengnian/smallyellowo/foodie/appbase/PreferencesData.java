package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesData {

    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences("preference_1", Context.MODE_PRIVATE);
    }

    //记录开发环境的切换
    private static final String develop_environment = "develop_environment";
    //记录键盘高度
    private static final String emoji_keyboard_height = "emoji_keyboard_height";


    public static void setEmojiKeyboardHeight(int height) {
        SharedPreferences prefs = getPreferences(APP.app);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(emoji_keyboard_height, height);
        editor.commit();
    }

    public static int getEmojiKeyboardHeight() {
        return getPreferences(APP.app).getInt(emoji_keyboard_height, 0);
    }


    public static void setDevelopEnvironmentId(int id) {
        SharedPreferences prefs = getPreferences(APP.app);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(develop_environment, id);
        editor.commit();
    }

    public static int getDevelopEnvironmentId() {
        return getPreferences(APP.app).getInt(develop_environment, -10);
    }
}
