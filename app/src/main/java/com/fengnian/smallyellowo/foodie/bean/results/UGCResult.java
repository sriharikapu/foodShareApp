package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class UGCResult extends BaseResult {
    List<UGCData> feeds;
    List<SYChoiceModel> pgcs;
    public List<UGCData> getFeeds() {
        return feeds;
    }
    public void setFeeds(List<UGCData> feeds) {
        this.feeds = feeds;
    }
    public List<SYChoiceModel> getPgcs() {
        return pgcs;
    }
    public void setPgcs(List<SYChoiceModel> pgcs) {
        this.pgcs = pgcs;
    }

    public static class UGCData {
        private String userId;
        private String headImg;
        private String nickName;
        private String isCustomMerchant;
        private double merchantLatitude;
        private double merchantLongitude;
        private String merchantAddress;
        private String merchantName;

        private int userType;//0是普通   1是会员

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getMerchantUid() {
            return merchantUid;
        }

        public void setMerchantUid(String merchantUid) {
            this.merchantUid = merchantUid;
        }

        private String merchantUid;

        public String getMerchantPhone() {
            return merchantPhone;
        }

        public void setMerchantPhone(String merchantPhone) {
            this.merchantPhone = merchantPhone;
        }

        private String merchantPhone;

        public boolean isbEat() {
            return bEat;
        }

        public void setbEat(boolean bEat) {
            this.bEat = bEat;
        }

        public String getMerchantAddress() {
            return merchantAddress;
        }

        public void setMerchantAddress(String merchantAddress) {
            this.merchantAddress = merchantAddress;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        private String feedId;
        private int starLevel;
        private String frontCoverImg;
        private String frontCoverContent;
        private int haveEat;
        private int wantEat;
        private boolean bEat;
        private String foodType;
        private boolean byFollowMe;
        private boolean followMe;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIsCustomMerchant() {
            return isCustomMerchant;
        }

        public void setIsCustomMerchant(String isCustomMerchant) {
            this.isCustomMerchant = isCustomMerchant;
        }

        public double getMerchantLatitude() {
            return merchantLatitude;
        }

        public void setMerchantLatitude(double merchantLatitude) {
            this.merchantLatitude = merchantLatitude;
        }

        public double getMerchantLongitude() {
            return merchantLongitude;
        }

        public void setMerchantLongitude(double merchantLongitude) {
            this.merchantLongitude = merchantLongitude;
        }

        public String getFeedId() {
            return feedId;
        }

        public void setFeedId(String feedId) {
            this.feedId = feedId;
        }

        public int getStarLevel() {
            return starLevel;
        }

        public void setStarLevel(int starLevel) {
            this.starLevel = starLevel;
        }

        public String getFrontCoverImg() {
            return frontCoverImg;
        }

        public void setFrontCoverImg(String frontCoverImg) {
            this.frontCoverImg = frontCoverImg;
        }

        public String getFrontCoverContent() {
            return frontCoverContent;
        }

        public void setFrontCoverContent(String frontCoverContent) {
            this.frontCoverContent = frontCoverContent;
        }

        public int getHaveEat() {
            return haveEat;
        }

        public void setHaveEat(int haveEat) {
            this.haveEat = haveEat;
        }

        public int getWantEat() {
            return wantEat;
        }

        public void setWantEat(int wantEat) {
            this.wantEat = wantEat;
        }

        public boolean isBEat() {
            return bEat;
        }

        public void setBEat(boolean bEat) {
            this.bEat = bEat;
        }

        public String getFoodType() {
            return foodType;
        }

        public void setFoodType(String foodType) {
            this.foodType = foodType;
        }

        public boolean isByFollowMe() {
            return byFollowMe;
        }

        public void setByFollowMe(boolean byFollowMe) {
            this.byFollowMe = byFollowMe;
        }

        public boolean isFollowMe() {
            return followMe;
        }

        public void setFollowMe(boolean followMe) {
            this.followMe = followMe;
        }
    }

}
