package com.fengnian.smallyellowo.foodie.personal;

import java.util.ArrayList;

/**
 * weichenglin create in 15/5/12
 */
public class FilterItem {
    public String title;
    public String id;
    public int type;
    public int level;
    public ArrayList<FilterItem> childList;

    /**
     * 当第二级当显示全部的时候，
     * 这个字段对应的就是它的父亲的名字
     */
    public String parentTitle;
    /**
     * 当第二级当显示全部的时候，
     * 这个字段对应的就是它的父亲的ID
     */
    public String parentId;
}
