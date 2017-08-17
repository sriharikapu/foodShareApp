package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantTag;

import java.util.List;

public class RestInfoResult extends BaseResult {
    private SYFindMerchantInfo buinessDetail;
    private List<RestInfoActivity.RestInfoFeed> shareFeedsList;
    private int currentStartLevelType;
    private AttentionInfoBean attentionInfo;
    private int currentCategoryDataType;
    private AttentionInfoBean daRenJianInfo;

    public List<SYFindMerchantTag> getFindMerchantTags() {
        return findMerchantTags;
    }

    public void setFindMerchantTags(List<SYFindMerchantTag> findMerchantTags) {
        this.findMerchantTags = findMerchantTags;
    }

    private List<SYFindMerchantTag> findMerchantTags;
    public int getCurrentStartLevelType() {
        return currentStartLevelType;
    }

    public void setCurrentStartLevelType(int currentStartLevelType) {
        this.currentStartLevelType = currentStartLevelType;
    }

    public AttentionInfoBean getAttentionInfo() {
        return attentionInfo;
    }

    public void setAttentionInfo(AttentionInfoBean attentionInfo) {
        this.attentionInfo = attentionInfo;
    }

    public int getCurrentCategoryDataType() {
        return currentCategoryDataType;
    }

    public void setCurrentCategoryDataType(int currentCategoryDataType) {
        this.currentCategoryDataType = currentCategoryDataType;
    }

    public AttentionInfoBean getDaRenJianInfo() {
        return daRenJianInfo;
    }

    public void setDaRenJianInfo(AttentionInfoBean daRenJianInfo) {
        this.daRenJianInfo = daRenJianInfo;
    }

    public SYFindMerchantInfo getBuinessDetail() {
        return buinessDetail;
    }

    public void setBuinessDetail(SYFindMerchantInfo buinessDetail) {
        this.buinessDetail = buinessDetail;
    }

    public List<RestInfoActivity.RestInfoFeed> getShareFeedsList() {
        return shareFeedsList;
    }

    public void setShareFeedsList(List<RestInfoActivity.RestInfoFeed> shareFeedsList) {
        this.shareFeedsList = shareFeedsList;
    }

    public static class AttentionInfoBean {
        /**
         * startLevelList : [0,0,0,0]
         * score : 0.0
         * totalNumOfFeeds : 1
         */

        private double score;
        private int totalNumOfFeeds;
        private List<Integer> startLevelList;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getTotalNumOfFeeds() {
            return totalNumOfFeeds;
        }

        public void setTotalNumOfFeeds(int totalNumOfFeeds) {
            this.totalNumOfFeeds = totalNumOfFeeds;
        }

        public List<Integer> getStartLevelList() {
            return startLevelList;
        }

        public void setStartLevelList(List<Integer> startLevelList) {
            this.startLevelList = startLevelList;
        }
    }
}
