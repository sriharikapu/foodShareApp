package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-5-18.
 */

public class BwtdResult extends BasePullToRefreshResult<BwtdResult.SYMillionFindMerchantsGroupModel> {

    public ArrayList<SYMillionFindMerchantsGroupModel> getData() {
        return data;
    }

    public void setData(ArrayList<SYMillionFindMerchantsGroupModel> data) {
        this.data = data;
    }

    private ArrayList<SYMillionFindMerchantsGroupModel> data;

    public static class SYMillionFindMerchantsGroupModel extends XData {
        String numOfMillionFindMerchantsGroup;//	期号（第几期）	否
        String millionFindMerchantsGrouptitle;//	title（例：	 第243期 汕头美食活动
        String activityHtmlUrl;//
        String activityTitle;//
        ArrayList<SYMillionFindMerchantModel> millionFindMerchantsModelList;//

        public String getNumOfMillionFindMerchantsGroup() {
            return numOfMillionFindMerchantsGroup;
        }

        public void setNumOfMillionFindMerchantsGroup(String numOfMillionFindMerchantsGroup) {
            this.numOfMillionFindMerchantsGroup = numOfMillionFindMerchantsGroup;
        }

        public String getMillionFindMerchantsGrouptitle() {
            return millionFindMerchantsGrouptitle;
        }

        public void setMillionFindMerchantsGrouptitle(String millionFindMerchantsGrouptitle) {
            this.millionFindMerchantsGrouptitle = millionFindMerchantsGrouptitle;
        }

        public String getActivityHtmlUrl() {
            return activityHtmlUrl;
        }

        public void setActivityHtmlUrl(String activityHtmlUrl) {
            this.activityHtmlUrl = activityHtmlUrl;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public ArrayList<SYMillionFindMerchantModel> getMillionFindMerchantsModelList() {
            return millionFindMerchantsModelList;
        }

        public void setMillionFindMerchantsModelList(ArrayList<SYMillionFindMerchantModel> millionFindMerchantsModelList) {
            this.millionFindMerchantsModelList = millionFindMerchantsModelList;
        }
    }

    public static class SYMillionFindMerchantModel {
        SYImage foodImage;//	美食image	是
        String feedId;//	feedId	是
        String title;//	美食标题	是
        SYPoi poi;//	商户poi	是
        SYUser user;//	用户	是

        public SYImage getFoodImage() {
            if (foodImage == null) {
                foodImage = new SYImage();
            }
            return foodImage;
        }

        public void setFoodImage(SYImage foodImage) {
            this.foodImage = foodImage;
        }

        public String getFeedId() {
            return feedId;
        }

        public void setFeedId(String feedId) {
            this.feedId = feedId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public SYPoi getPoi() {
            if (poi == null) {
                poi = new SYPoi();
            }
            return poi;
        }

        public void setPoi(SYPoi poi) {
            this.poi = poi;
        }

        public SYUser getUser() {
            if (user == null) {
                return new SYUser();
            }
            return user;
        }

        public void setUser(SYUser user) {
            this.user = user;
        }
    }

}
