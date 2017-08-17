package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYBusinessCircleAreaModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodKindModel;

import java.util.List;

/**
 * Created by Administrator on 2016-9-6.
 */
public class DiscoverResult extends BaseResult {

    private DiscoverData findData;

    public DiscoverData getFindData() {
        return findData;
    }

    public void setFindData(DiscoverData findData) {
        this.findData = findData;
    }

    public static class DiscoverData {
        private List<SYBusinessCircleAreaModel> businessArea;
        private List<SYFoodKindModel> hotFood;

        public List<Carousel> getCarouselArray() {
            return carouselArray;
        }

        public void setCarouselArray(List<Carousel> carouselArray) {
            this.carouselArray = carouselArray;
        }

        private List<Carousel> carouselArray;



        public List<SYBusinessCircleAreaModel> getBusinessArea() {
            return businessArea;
        }

        public void setBusinessArea(List<SYBusinessCircleAreaModel> businessArea) {
            this.businessArea = businessArea;
        }

        public List<SYFoodKindModel> getHotFood() {
            return hotFood;
        }

        public void setHotFood(List<SYFoodKindModel> hotFood) {
            this.hotFood = hotFood;
        }


    }

    public static class Carousel {
        public int id;
        public int noticeId;
        public String picUrl;
        public String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(int noticeId) {
            this.noticeId = noticeId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        //        id=4
//        noticeId=26
//        picUrl=http://smallyellowotest.tinydonuts.cn/44b079447cbf4a1da10eda6a0bfbeb8f.png
//        url=http://static.tinydonuts.cn/notice/notice.html?noticeId=26
    }
}
