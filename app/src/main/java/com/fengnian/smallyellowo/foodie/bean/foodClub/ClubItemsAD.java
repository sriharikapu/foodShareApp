package com.fengnian.smallyellowo.foodie.bean.foodClub;

import com.fan.framework.base.XData;

import java.io.Serializable;
import java.util.List;

public class ClubItemsAD extends XData implements Serializable {

    private static final long serialVersionUID = -4661531959356710426L;

    private String image;
    private String bgImageUrl;
    private String targetUrl;
    private String title;
    private String valueDesc;
    private String extra1;
    private String extra2;
    private String extra3;
    private List<ClubTrackParam> trackParam;

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    public List<ClubTrackParam> getTrackParam() {
        return trackParam;
    }

    public void setTrackParam(List<ClubTrackParam> trackParam) {
        this.trackParam = trackParam;
    }


}
