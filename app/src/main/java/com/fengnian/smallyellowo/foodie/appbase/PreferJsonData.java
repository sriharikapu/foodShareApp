package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferJsonData {
    //工作日聚餐手动选择的地理位置
    private static final String dining_location = "dining_location";

    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences("preference_json", Context.MODE_PRIVATE);
    }

    //缓存朋友聚餐的用餐品类json
    private static final String together_type_json = "together_type_json";


    public static void setTogetherTypeJson(String json) {
        SharedPreferences prefs = getPreferences(APP.app);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(together_type_json, json);
        editor.commit();
    }

    public static String getTogetherTypeJson() {
        return getPreferences(APP.app).getString(together_type_json, "");
    }

    public static void setDiningLocation(String str) {
        SharedPreferences prefs = getPreferences(APP.app);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(dining_location, str);
        editor.commit();
    }

    public static String getDiningLocation() {
        return getPreferences(APP.app).getString(dining_location, "");
    }
}
