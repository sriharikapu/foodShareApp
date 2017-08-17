package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by chenglin on 2017-7-24.
 * 通用的页面管理的Model（类似蘑菇街的麦田系统，提供静态的数据）
 */

public class AdModelsList extends BaseResult {
    public List<ADModel> data;

    public static final class ADModel {
        public String id;
        public String templateStr;
        public String subtemplate;
        public String templateUrl;
        public String type;
        public List<ItemsAd> itemsAD;
    }

    public static final class ItemsAd {
        public String id;
        public String image;
        public String bgImageUrl;
        public String targetUrl;
        public String title;
        public String valueDesc;
        public String extra1;
        public String extra2;
        public String extra3;
        public String type;
        public String trackParam;
    }
}
