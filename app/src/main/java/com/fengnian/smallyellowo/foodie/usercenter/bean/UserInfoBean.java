package com.fengnian.smallyellowo.foodie.usercenter.bean;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.food_history.MultiClassParser;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYCommentGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYConcernedFansGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFoodGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFulfillGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYGoodChoiceGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYWantEatGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.Parser;

import java.util.List;

/**
 * Created by elaine on 2017/6/13.
 */

public class UserInfoBean extends BaseResult {

    public List<UserStatEntity> userStat;

    public SYUser user;
    public List<SyFoodNotesClassificationsBean> syFoodNotesClassifications;

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public List<SyFoodNotesClassificationsBean> getSyFoodNotesClassifications() {
        return syFoodNotesClassifications;
    }

    public void setSyFoodNotesClassifications(List<SyFoodNotesClassificationsBean> syFoodNotesClassifications) {
        this.syFoodNotesClassifications = syFoodNotesClassifications;
    }

    public List<UserStatEntity> getUserStat() {
        return userStat;
    }

    public void setUserStat(List<UserStatEntity> userStat) {
        this.userStat = userStat;
    }


    public static class SyFoodNotesClassificationsBean {
        public String imgUrl;
        public String type;
        public int counts;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }
    }

    public static class UserStatEntity {

        public String show;
        public int type;
        public String date;
        public Object data;

        public String getShareInfo() {

//            show = "美食编辑";
            StringBuilder sb = new StringBuilder();
            sb.append(date).append("  分享了");
            if (show.contains("美食")) {
                sb.append("<font color='#F9A825'>").append(show).append("</font>");
            } else {
                sb.append(show);
            }
            return sb.toString();
        }

        public String getAdmireInfo() {
            StringBuilder sb = new StringBuilder();
//            sb.append(date)
            sb.append("30天")
                    .append("  内收到")
                    .append("<font color='#F9A825'>")
                    .append(FFUtils.getMore999(show))
                    .append("</font>")
                    .append("个赞");
            return sb.toString();
        }

        public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = MultiClassParser.parse(data, new Class[]{SYFoodGrowthRecord.class,
                    SYFulfillGrowthRecord.class,
                    SYCommentGrowthRecord.class,
                    SYWantEatGrowthRecord.class,
                    SYGoodChoiceGrowthRecord.class,
                    SYConcernedFansGrowthRecord.class});
        }

    }

}
