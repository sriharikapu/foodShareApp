package com.fengnian.smallyellowo.foodie.bean.foodClub;

import java.io.Serializable;

public class ClubTrackParam implements Serializable {
    private static final long serialVersionUID = -6795703431709439237L;
    private String keyStr;
    private String value;

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
