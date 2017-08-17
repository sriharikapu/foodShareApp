package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by chenglin on 2017-7-27.
 */

public class PgcModelsList extends BaseResult {
    public List<PGCModel> data;

    public static final class PGCModel {
        public String id;
        public String templateStr;
        public String subtemplate;
        public String templateUrl;
        public String type;
        public List<ItemPGC> itemsPGC;
    }

    public static final class ItemPGC {
        public String id;
        public String pgcNum;
        public String title;
        public String createTime;
        public String cover;
        public String officalCover;
        public String merchantName;
        public String merchantAddress;
        public String merchantId;
        public String htmlUrl;
        public String elite;
        public String eliteTime;
        public String authorId;
        public String isDelete;
        public String accountId;
        public String description;
        public String shareImg;
        public String shareDescription;
        public String street;
        public String shareTitle;
        public String longitude;
        public String latitude;
        public int views;
        public String htmlClickCount;
        public String shopId;
    }
}
