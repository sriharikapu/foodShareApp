package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.SYObjectPools;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SerializeUtil;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.DiscoverResult;
import com.fengnian.smallyellowo.foodie.bean.results.HotSearchWordResult;

import java.util.List;

/**
 * 持久化多城市相关信息
 */
public class CityPref {

    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mSharedPreferences;
    private static Context context;

    static {
        context = APP.app;
        mSharedPreferences = context.getSharedPreferences("yellowo", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    //判断是否为第一次安装app(用于判断进入欢迎页面后，是否需要进入选择城市页面)
    public static boolean isFirstStartApp() {
        return mSharedPreferences.getBoolean("install_app", true);
    }

    public static void setFirstStartApp(boolean isFirst) {
        mEditor.putBoolean("install_app", isFirst);
        mEditor.commit();
    }

    /**
     * 判断当前城市是不是北京
     */
    public static boolean isBeiJing() {
        CityListBean.CityBean cityBean = CityPref.getSelectedCity();
        return CityListBean.CITY_BJ.equals(cityBean.getId());
    }


    /**
     * 保存选择的城市
     *
     * @param city
     */
    public static void saveSelectedCity(CityListBean.CityBean city) {
        if (city == null)
            return;

        String cityInfo = JSON.toJSON(city) + "";
        mEditor.putString("city", cityInfo);
        mEditor.commit();
    }

    /**
     * 获取选择的城市
     *
     * @return
     */
    public static CityListBean.CityBean getSelectedCity() {
        String jsonCity = mSharedPreferences.getString("city", null);
        CityListBean.CityBean city = null;
        if (!TextUtils.isEmpty(jsonCity)) {
            city = JSON.parseObject(jsonCity, CityListBean.CityBean.class);
        }

        if (city == null) {
            city = getDefaultCity();
        }

        return city;
    }

    private static CityListBean.CityBean getDefaultCity() {
        CityListBean.CityBean city = new CityListBean.CityBean();
        city.setId(CityListBean.CITY_BJ);
        city.setAreaName(CityListBean.CITY_BJ_NAME);
        city.setLatitude(CityListBean.CITY_BJ_LAT);
        city.setLongitude(CityListBean.CITY_BJ_LON);
        return city;
    }

    /**
     * 获取城市列表
     *
     * @return
     */
    public static List<CityListBean.CityBean> getCityList() {
        String jsonCityList = mSharedPreferences.getString("city_list", null);
        if (TextUtils.isEmpty(jsonCityList)) {
            return null;
        }

        List<CityListBean.CityBean> cityList = JSON.parseArray(jsonCityList, CityListBean.CityBean.class);
        return cityList;
    }

    /**
     * 保存接口返回的城市列表
     *
     * @param cityList
     */
    public static void saveCityList(List<CityListBean.CityBean> cityList) {
        if (FFUtils.isListEmpty(cityList)) {
            return;
        }

        String jsonList = JSON.toJSONString(cityList);
        if (jsonList.isEmpty()) {
            return;
        }

        mEditor.putString("city_list", jsonList);
        mEditor.commit();
    }

    /**
     * 保存选中城市的商圈列表
     *
     * @param list
     */
    public static void saveCityAreaList(List<CityAreaBean.AreaEntity.ListEntity> list) {
        if (FFUtils.isListEmpty(list)) {
            return;
        }

        String jsonList = JSON.toJSONString(list);
        if (jsonList.isEmpty()) {
            return;
        }
        mEditor.putString("city_area_list", jsonList);
        mEditor.commit();
    }

    /**
     * 获取选中城市的商圈列表
     *
     * @return
     */
    public static List<CityAreaBean.AreaEntity.ListEntity> getCityAreaList() {
        String jsonCityList = mSharedPreferences.getString("city_area_list", null);
        if (TextUtils.isEmpty(jsonCityList)) {
            return null;
        }

        List<CityAreaBean.AreaEntity.ListEntity> cityList = JSON.parseArray(jsonCityList, CityAreaBean.AreaEntity.ListEntity.class);
        return cityList;
    }

    /**
     * 获取多城市配置信息的key（轮播图， 活动等配置信息的key）
     * @return
     */
    public static CityAreaBean.CityInfoEntity getCityConfigInfo(){
        CityAreaBean.CityInfoEntity configItem = null;
        String jsonCityConfig = mSharedPreferences.getString("city_config_info", null);
        if (!TextUtils.isEmpty(jsonCityConfig)) {
            configItem = JSON.parseObject(jsonCityConfig, CityAreaBean.CityInfoEntity.class);
        }

        if (configItem == null){
            //默认北京
            configItem = new CityAreaBean.CityInfoEntity();
            configItem.setCityId(CityListBean.CITY_BJ);
            configItem.setBanner("index_beijing");
            configItem.setPgc("pgc_beijing");
            configItem.setActivityUrl("activity_beijing");
        }

        return configItem;
    }

    /**
     * 保存多城市配置信息的key
     * @param cityConfig
     */
    public static void saveCityConfigInfo(CityAreaBean.CityInfoEntity cityConfig){
        if (cityConfig == null)
            return;

        String cityInfo = JSON.toJSON(cityConfig) + "";
        mEditor.putString("city_config_info", cityInfo);
        mEditor.commit();
    }

    public static void saveCityFoodTypes(List<CityAreaBean.FoodKindEntity> foodList){
        if (FFUtils.isListEmpty(foodList))
            return;

        String cityInfo = JSON.toJSONString(foodList);
        mEditor.putString("city_food_type_list", cityInfo);
        mEditor.commit();
    }

    public static List<CityAreaBean.FoodKindEntity> getCityFoodList(){
        List<CityAreaBean.FoodKindEntity> list = null;
        String jsonCityConfig = mSharedPreferences.getString("city_food_type_list", null);
        if (!TextUtils.isEmpty(jsonCityConfig)) {
            list = JSON.parseArray(jsonCityConfig, CityAreaBean.FoodKindEntity.class);
        }

        return list;
    }
}
