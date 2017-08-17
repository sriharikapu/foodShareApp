package com.fengnian.smallyellowo.foodie.bean.results;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lanbiao on 2017/1/4.
 */

public class SYShopLocationResult extends BaseResult{

    public static class SYShopLocationPoiModel {
        private double lat=0;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public  static class SYShopLocationInfoModel{
        private String adcode="";
        private String province="";
        private String city="";
        private String district="";

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }

    public static class SYShopLocationModel implements Serializable {
        private String id="";
        private String title="";
        private String address="";
        private String distance="";
        private String category="";
        private  int merchantType; //网络处理  0腾讯数据   1：本地添加或者大众点评数据
        private String isCustom="";

        private SYShopLocationPoiModel location;
        private SYShopLocationInfoModel ad_info;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getMerchantType() {
            return merchantType;
        }

        public void setMerchantType(int merchantType) {
            this.merchantType = merchantType;
        }

        public String getIsCustom() {
            return isCustom;
        }

        public void setIsCustom(String isCustom) {
            this.isCustom = isCustom;
        }

        public SYShopLocationPoiModel getLocation() {
            return location;
        }

        public void setLocation(SYShopLocationPoiModel location) {
            this.location = location;
        }

        public SYShopLocationInfoModel getAd_info() {
            return ad_info;
        }

        public void setAd_info(SYShopLocationInfoModel ad_info) {
            this.ad_info = ad_info;
        }
    }

    private List<SYShopLocationModel> list;

    public List<SYShopLocationModel> getList() {
        return list;
    }

    public void setList(List<SYShopLocationModel> list) {
        this.list = list;
    }

}




