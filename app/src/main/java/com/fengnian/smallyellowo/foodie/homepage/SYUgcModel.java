package com.fengnian.smallyellowo.foodie.homepage;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.io.Serializable;

/**
 * Created by chenglin on 2017-4-10.
 * 文档地址：http://tools.tinydonuts.cn:8090/display/yellowCircle/SYUgcModel
 */
public class SYUgcModel implements Serializable {
    public transient int itemType = -1;//私有字段，标识类型
    public SYUser user;
    public String merchantUid; //店铺id
    public String merchantName;//店铺名称
    public String isCustomMerchant;//0-腾讯地图数据 1-自定义数据
    public String feedId;//美食纪录ID
    public int releaseTemplateType;//模版类型 0：标准 1：现代 2:泼墨 3:中式 4:中式2 5:简短 6:简短2 7：简短2-2
    public int starLevel;//推荐星级
    public String frontCoverImg;//封面图片
    public String frontCoverContent;//封面标题
    public int haveEat;//多少人吃过
    public int wantEat;//多少人想吃
    public String foodType;//餐品种类
    public String distance; //距离 （附近接口用）
    public double imageHeight;//图片高度 （附近接口用）
    public double imageWidth;//图片宽度 （附近接口用）
    public String merchantStreet;//区域名称

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getIsCustomMerchant() {
        return isCustomMerchant;
    }

    public void setIsCustomMerchant(String isCustomMerchant) {
        this.isCustomMerchant = isCustomMerchant;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getReleaseTemplateType() {
        return releaseTemplateType;
    }

    public void setReleaseTemplateType(int releaseTemplateType) {
        this.releaseTemplateType = releaseTemplateType;
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

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight = imageHeight;
    }

    public double getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getMerchantStreet() {
        return merchantStreet;
    }

    public void setMerchantStreet(String street) {
        this.merchantStreet = street;
    }
}
