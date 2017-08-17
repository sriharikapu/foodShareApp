package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.SYObjectPools;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SerializeUtil;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.DiscoverResult;
import com.fengnian.smallyellowo.foodie.bean.results.HotSearchWordResult;
import com.fengnian.smallyellowo.foodie.utils.Parser;

import java.util.ArrayList;
import java.util.List;

public class SP {

    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditorElse;
    private static SharedPreferences mSharedPreferencesElse;
    private static Context context;

    static {
        context = APP.app;
        mSharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mSharedPreferencesElse = context.getSharedPreferences("else", Context.MODE_PRIVATE);
        mEditorElse = mSharedPreferencesElse.edit();
    }

    public static void setToken(String token) {
        mEditor.putString("token", token);
        mEditor.commit();
    }

    public static String getToken() {
        return mSharedPreferences.getString("token", null);
    }

    /**
     * 获取登录返回的user信息
     *
     * @return
     */
    public static SYUser getUser() {

        SYUser user = SYObjectPools.shareGlobalObject.userWithUserID(getUid());
        if (user == null) {
            String jsonUser = mSharedPreferences.getString("User", null);
            if (jsonUser == null) {
                user = new SYUser(true);
            } else {
                user = JSON.parseObject(jsonUser, SYUser.class);
            }
        }
        return user;
    }

    public static void setUser(SYUser user) {
        if (user == null)
            return;

        String infosss = JSON.toJSON(user) + "";
        if (user.getToken() != null && user.getToken().length() > 0) {
            setToken(user.getToken());
        }
        setUid(user.getId());
        mEditor.putString("User", infosss);
        mEditor.commit();
    }

    public static void setWeiChatCode(String weiChatCode) {
        mEditor.putString("setWeiChatCode", weiChatCode);
        mEditor.commit();
    }

    public static void saveKeyValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static String getValue(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public static String getWeiChatCode() {
        return mSharedPreferences.getString("setWeiChatCode", null);
    }

    static String youkeToken;

    public static String getYoukeToken() {
        if (youkeToken != null) {
            return youkeToken;
        }
        return youkeToken = mSharedPreferences.getString("youkeToken", null);
    }


    public static void setYoukeToken(String youkeToken) {
        mEditor.putString("youkeToken", youkeToken);
        mEditor.commit();
    }

    public static List<String> getHistory() {
        String str = mSharedPreferences.getString("getHistory", "[]");
        return JSON.parseArray(str, String.class);
    }

    public static void setHistory(List<String> history) {
        mEditor.putString("getHistory", JSON.toJSONString(history));
        mEditor.commit();
    }

    public static void ClearUserSp() {
        com.igexin.sdk.PushManager.getInstance().unBindAlias(APP.app, SP.getUid(), false);
        com.igexin.sdk.PushManager.getInstance().turnOffPush(APP.app);
//        MainActivity.checkItem = -1;
        String youkeToken = getYoukeToken();
        mEditor.clear();
        setYoukeToken(youkeToken);
        setLoginType("");
    }

    /**
     * 获取检查积分
     *
     * @return true kaiqi  可以弹出检查积分选项
     */
    public static boolean getCheckScore() {
        boolean flag = true;
        flag = mSharedPreferences.getBoolean("checkScore", true);
        return flag;
    }

    /**
     * 设置积分检查状态
     *
     * @param checkScore
     */
    public static void setCheckScore(boolean checkScore) {

        mEditor.putBoolean("checkScore", checkScore);
        mEditor.commit();
    }

    public static void setUid(String uid) {
        mEditor.putString("uid", uid);
        mEditor.commit();
    }

    public static String getUid() {
        return mSharedPreferences.getString("uid", "");
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getUid());
    }

    //------------------------------------------------------------------------------------------------------
    public static void setDiscoverData(String reponseString) {
        mEditorElse.putString("reponseString", reponseString);
        mEditorElse.commit();
    }

    public static void setLoginType(String platform) {
        mEditorElse.putString("platform", platform);
        mEditorElse.commit();
    }

    public static String getLoginType() {
        return mSharedPreferencesElse.getString("platform", "密码");
    }

    public static DiscoverResult getDiscoverData() {
        String str = mSharedPreferencesElse.getString("reponseString", "");
        return JSON.parseObject(str, DiscoverResult.class);
    }

    public static void setConfig(ConfigResult str) {
        config = str;
        mEditorElse.putString("setConfig1", SerializeUtil.serializeObject(str));
        mEditorElse.commit();
    }

    private static ConfigResult config;

    public static ConfigResult getConfig() {
        List<SiftBean.BusinessListBean.BusinessGroup> business = areaToBusiness();
        List<SiftBean.FoodKindListBean.FoodKindBean> foods = getFoodTypeList();
        if (config != null) {
            if (!FFUtils.isListEmpty(business)){
                config.getConfig().getSift().getBusiness().setList(business);
            }
            if (!FFUtils.isListEmpty(foods)){
                config.getConfig().getSift().getFoodKind().setFoodKind(foods);
            }
            return config;
        }
        String str = mSharedPreferencesElse.getString("setConfig1", null);
        if (str == null) {

            String result = FFUtils.readFileFromAssets("config.json");
            config = JSON.parseObject(result, ConfigResult.class);
            if (!FFUtils.isListEmpty(business)){
                config.getConfig().getSift().getBusiness().setList(business);
            }
            if (!FFUtils.isListEmpty(foods)){
                config.getConfig().getSift().getFoodKind().setFoodKind(foods);
            }
            return config;
        }

        config = (ConfigResult) SerializeUtil.readObject(str);
        if (!FFUtils.isListEmpty(business)){
            config.getConfig().getSift().getBusiness().setList(business);
        }
        if (!FFUtils.isListEmpty(foods)){
            config.getConfig().getSift().getFoodKind().setFoodKind(foods);
        }
        return config;
    }

    private static List<SiftBean.BusinessListBean.BusinessGroup> areaToBusiness(){
        List<SiftBean.BusinessListBean.BusinessGroup> businessList = new ArrayList<>();
        List<CityAreaBean.AreaEntity.ListEntity> areaList = CityPref.getCityAreaList();
        if (FFUtils.isListEmpty(areaList)){
            return null;
        }

        for (CityAreaBean.AreaEntity.ListEntity entity : areaList){
            SiftBean.BusinessListBean.BusinessGroup bean = new SiftBean.BusinessListBean.BusinessGroup();
            bean.setId(entity.getId());
            bean.setContent(entity.getAreaName());
            bean.setList(getEntity(entity.getAreaList()));
            businessList.add(bean);
        }

        return businessList;
    }

    private static List<SiftBean.FoodKindListBean.FoodKindBean> getFoodTypeList(){
        List<SiftBean.FoodKindListBean.FoodKindBean> foods = new ArrayList<>();
        List<CityAreaBean.FoodKindEntity> items = CityPref.getCityFoodList();
        if (FFUtils.isListEmpty(items)){
            return null;
        }

        for (CityAreaBean.FoodKindEntity food : items){
            SiftBean.FoodKindListBean.FoodKindBean bean = new SiftBean.FoodKindListBean.FoodKindBean();
            bean.setId(Parser.parseInt(food.getId()));
            bean.setContent(food.getTitle());
            foods.add(bean);
        }

        return foods;
    }

    private static List<SiftBean.BusinessListBean.BusinessGroup.Business> getEntity(List<CityAreaBean.AreaEntity.ListEntity.AreaListEntity> list){
        List<SiftBean.BusinessListBean.BusinessGroup.Business> business = new ArrayList<>();
        for (CityAreaBean.AreaEntity.ListEntity.AreaListEntity area : list){
            SiftBean.BusinessListBean.BusinessGroup.Business b = new SiftBean.BusinessListBean.BusinessGroup.Business();
            b.setId(area.getId());
            b.setContent(area.getAreaName());
            b.setImageUrl(area.getImgPath());

            business.add(b);
        }

        return business;
    }

    public static List<HotSearchWordResult.HotWord> getHotWord() {
        String str = mSharedPreferencesElse.getString("getHotWord", "[]");
        return JSON.parseArray(str, HotSearchWordResult.HotWord.class);
    }

    public static void setHotWord(List<HotSearchWordResult.HotWord> hotWord) {
        mEditorElse.putString("getHotWord", JSON.toJSONString(hotWord));
        mEditorElse.commit();
    }

    public static boolean getMainTip() {
        return mSharedPreferencesElse.getBoolean("getMainTip", false);
    }

    public static void setMainTip() {
        mEditorElse.putBoolean("getMainTip", true);
        mEditorElse.commit();
    }

    public static void setIgnore(String ignore) {
        mEditorElse.putString("ignoreVersion", ignore);
        mEditorElse.commit();
    }

    public static boolean isVersionIgnored(String version) {
        String ignoreVersion = mSharedPreferencesElse.getString("ignoreVersion", null);
        if (ignoreVersion == null) {
            return false;
        }
        if (ignoreVersion.equals(version)) {
            return true;
        }
        return false;
    }

    //是否是第一次首页计划性推荐
    public static void setFirstHomeRecommend(boolean isRecommend) {
        mEditor.putBoolean("isRecommend", isRecommend);
        mEditor.commit();
    }

    //是否是第一次首页计划性推荐
    public static boolean getFirstHomeRecommend() {
        return mSharedPreferences.getBoolean("isRecommend", true);
    }
}
