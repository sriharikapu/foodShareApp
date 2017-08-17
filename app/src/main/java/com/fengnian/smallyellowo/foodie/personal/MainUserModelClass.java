package com.fengnian.smallyellowo.foodie.personal;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

/**
 * Created by chenglin on 2017-3-24.
 */

public class MainUserModelClass {
    /**
     * 用户中心头部的数据结构
     */
    public static class UserInfoResult extends BaseResult {
        public int wantEatState;
        public int recordState;
        public int noteState;
        public int scoreState;
        public int foodCourse;
        public SYUser user;
    }

    /**
     * 个人中心的美食记录过滤的请求参数
     * 文档地址:http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915259
     */
    public static class ProfileCenterLeftRequestParams {
        public String ptype = "-1";//品类（汉字）
        public String streetId = "";//商圈id
        public String isElite = "-1";//是否精选（1只精选,-1全选或者全不选）
        public String pubType = "-1";//类型（1速记、2编辑,-1全选或者全不选）
        public String status = "-1";//1分享、0未分享、-1全选
        public final String pageSize = "15";//请求的条数
        public String lastFoodNoteId = "";//之前最后一条

        public String keyWord = "";//搜索关键字
        public boolean isUnSaved = false;//只选中了未保存

        @Override
        public String toString() {
            return "ptype:" + ptype
                    + ";streetId:" + streetId
                    + ";isElite:" + isElite
                    + ";pubType:" + pubType
                    + ";status:" + status
                    + ";isUnSaved:" + isUnSaved
                    + ";";
        }
    }

    /**
     * 个人中心的想吃清单过滤的请求参数
     * 文档地址:http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915341
     */
    public static class ProfileCenterRightRequestParams {
        public String ptype = "-1";//品类（汉字）
        public String streetId = "";//商圈id
        public int eatType = 1;//想吃类型（1未吃、2吃过 , 0全选；默认0）
        public int eatSort = 0;//排序（0按时间、1按评分 , 2按距离， 默认按时间0）
        public String eatSource = "1,2,3";//来源（1达人荐，2动态， 3其他 默认空）传来源字符串，逗号分割(1,2--按达人荐和动态搜)
        public final String pageSize = "15";//请求的条数
//        public String longitude = "";//用户位置，经度
//        public String latitude = "";//用户位置，纬度
//        public String lastFoodNoteId = "0";//之前最后一条

        public String keyWord = "";//搜索关键字
        public String lastFoodEatId = "";//想吃清单的最后一条ID

        @Override
        public String toString() {
            return "ptype:" + ptype
                    + ";streetId:" + streetId
                    + ";eatType:" + eatType
                    + ";eatSort:" + eatSort
                    + ";eatSource:" + eatSource
                    + ";pageSize:" + pageSize
                    + ";";
        }
    }
}
