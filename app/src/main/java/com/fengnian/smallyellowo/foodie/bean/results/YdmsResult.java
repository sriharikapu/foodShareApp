package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-5-18.
 */

public class YdmsResult extends BaseResult {
    ArrayList<SYDifferentPlaceFoodModel> data;

    @Override
    public boolean isNoData() {
        return FFUtils.isListEmpty(data);
    }

    public ArrayList<SYDifferentPlaceFoodModel> getData() {
        return data;
    }

    public void setData(ArrayList<SYDifferentPlaceFoodModel> data) {
        this.data = data;
    }


    public static class SYDifferentPlaceFoodModel {
        SYImage differentPlaceFoodImage;//	异地美食图片	是
        String differentPlaceFoodId;//	美食id	否
        String differentPlaceFoodHtmlUrl;//	链接htmlUrl	否
        int numOfDifferentPlaceFood;//	活动第几期	否
        String differentPlaceFoodTitle;//	标题	是

        public SYImage getDifferentPlaceFoodImage() {
            return differentPlaceFoodImage;
        }

        public void setDifferentPlaceFoodImage(SYImage differentPlaceFoodImage) {
            this.differentPlaceFoodImage = differentPlaceFoodImage;
        }

        public String getDifferentPlaceFoodId() {
            return differentPlaceFoodId;
        }

        public void setDifferentPlaceFoodId(String differentPlaceFoodId) {
            this.differentPlaceFoodId = differentPlaceFoodId;
        }

        public String getDifferentPlaceFoodHtmlUrl() {
            return differentPlaceFoodHtmlUrl;
        }

        public void setDifferentPlaceFoodHtmlUrl(String differentPlaceFoodHtmlUrl) {
            this.differentPlaceFoodHtmlUrl = differentPlaceFoodHtmlUrl;
        }

        public int getNumOfDifferentPlaceFood() {
            return numOfDifferentPlaceFood;
        }

        public void setNumOfDifferentPlaceFood(int numOfDifferentPlaceFood) {
            this.numOfDifferentPlaceFood = numOfDifferentPlaceFood;
        }

        public String getDifferentPlaceFoodTitle() {
            return differentPlaceFoodTitle;
        }

        public void setDifferentPlaceFoodTitle(String differentPlaceFoodTitle) {
            this.differentPlaceFoodTitle = differentPlaceFoodTitle;
        }
    }

}
