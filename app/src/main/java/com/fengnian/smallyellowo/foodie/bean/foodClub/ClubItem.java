package com.fengnian.smallyellowo.foodie.bean.foodClub;

import java.io.Serializable;
import java.util.List;

public class ClubItem implements Serializable {

    private static final long serialVersionUID = 4147825505656924918L;

    private long id;
    private String templateStr;
    private String subtemplate;
    private String templateUrl;
    private int type;
    private List<ClubItemsAD> itemsAD;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTemplateStr() {
        return templateStr;
    }

    public void setTemplateStr(String templateStr) {
        this.templateStr = templateStr;
    }

    public String getSubtemplate() {
        return subtemplate;
    }

    public void setSubtemplate(String subtemplate) {
        this.subtemplate = subtemplate;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ClubItemsAD> getItemsAD() {
        return itemsAD;
    }

    public void setItemsAD(List<ClubItemsAD> itemsAD) {
        this.itemsAD = itemsAD;
    }


}
