package com.fengnian.smallyellowo.foodie.bean.publics;

/**
 * Created by Administrator on 2016-12-28.
 */

public class DeliciousFoodMapBean {

    private  SYPoi poi;

    private String xgd;
    private  String ptype;

    public SYPoi getPoi() {
        return poi;
    }

    public void setPoi(SYPoi poi) {
        this.poi = poi;
    }

    public String getXgd() {
        return xgd;
    }

    public void setXgd(String xgd) {
        this.xgd = xgd;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    private SYImage img;
    private String price;
    private  String shareNum;
    private   String starLevel;

    public void setImg(SYImage img) {
        this.img = img;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }
}
