package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.Parser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by elaine on 2017/7/17.
 */

public class CityListBean extends BaseResult implements Serializable {
    public transient static final String CITY_BJ = "3";//北京
    public transient static final String CITY_BJ_NAME = "北京";
    public transient static final String CITY_BJ_LAT = "39.980437";
    public transient static final String CITY_BJ_LON = "116.499471";
    public transient static final String CITY_CHEGNDU = "242";//成都

    private List<CityBean> citys;

    public List<CityBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CityBean> citys) {
        this.citys = citys;
    }

    public static class CityBean implements Serializable {
        private String id;
        private String superId;
        private String level;
        private String areaName;
        private String createTime;
        private String superAreas;
        private String imgPath;
        private String longitude;
        private String latitude;
        private boolean isChecked;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSuperId() {
            return superId;
        }

        public void setSuperId(String superId) {
            this.superId = superId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSuperAreas() {
            return superAreas;
        }

        public void setSuperAreas(String superAreas) {
            this.superAreas = superAreas;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public double getLongitude() {
            return Parser.parseDouble(longitude);
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return Parser.parseDouble(latitude);
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }


}
