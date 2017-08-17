package com.fengnian.smallyellowo.foodie.bean.food_history;

import android.text.TextUtils;
import android.view.View;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 美食分享被加入想吃清单或被践行历程对象
 */

public class SYFulfillGrowthRecord extends BaseGrowthHistoryItem {
    public boolean bFulfillGrowthRecordModel;//	是否是FulfillGrowthRecordModel	是

    public int type;//	*  1所有的美食分享总共被XX次加入想吃清单	是
    //	 *  2某条美食被加入想吃清单的总数XX次
    //	 *  3所有的美食分享总共被XX次践行
    //	 *  4某条美食被践行总数达到XX次
    //	 *  5我加入的想吃清单次数达到XX次
    //   *  6已完成XX篇想吃记录


    public int sourceType;//	*  1从达人荐加入想吃清单	否
    //	 *  2从动态加入想吃清单的总数
    //	 *  3其他地方 暂时未知

    public int count;//	次数	是
    public int foodtype;
    public SYUser user;//	用户对象	是
    public String title;//	美食标题	是

    public String getBuinessName() {
        return buinessName;
    }

    public void setBuinessName(String buinessName) {
        this.buinessName = buinessName;
    }

    public String buinessName;//	商家名称	是
    public String businessID;//	商户id	是
    public SYImage coverImage;//	封面图	是

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String feedID;

    public int getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(int foodtype) {
        this.foodtype = foodtype;
    }

    public boolean isbFulfillGrowthRecordModel() {
        return bFulfillGrowthRecordModel;
    }

    public void setbFulfillGrowthRecordModel(boolean bFulfillGrowthRecordModel) {
        this.bFulfillGrowthRecordModel = bFulfillGrowthRecordModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public SYImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(SYImage coverImage) {
        this.coverImage = coverImage;
    }

    public String getContentString() {
        StringBuilder content = new StringBuilder();
        content.append("  ");
        switch (getType()) {
            case 1://所有的美食分享总共被XX次加入想吃清单
                if (getUser() != null && !FFUtils.isStringEmpty(getUser().getNickName()) && !FFUtils.isStringEmpty(getBuinessName())) {
                    content.append("美食分享被第" );
                    content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                    content.append("次加入想吃清单");
                    
                } else {
                    content.append("被加入想吃清单总数达到" );
                    content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                }
                break;
            case 2://某条美食被加入想吃清单的总数XX次
                if (getUser() != null) {
                    if (!FFUtils.isStringEmpty(getTitle())) {
                        content.append("《" + FFUtils.subStr10(getTitle()) + "》");
                    }
                    content.append("被加入想吃清单总数达到");
                    content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                } else {
                    content.append("被加入想吃清单总数达到");
                    content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                }
                break;
            case 3://所有的美食分享总共被XX次践行
                content.append("美食分享被践行总数达到");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 4://某条美食被践行总数达到XX次

                if (!FFUtils.isStringEmpty(getTitle())) {
                    content.append("《" + FFUtils.subStr10(getTitle()) + "》");
                }
                content.append("被践行总数达到");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 5://我加入的想吃清单次数达到XX次
                switch (getSourceType()) {
                    case 1://从达人荐加入想吃清单
                        content.append("从  达人荐  ");
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                    case 2://从动态加入想吃清单的总数
                        content.append("从  动态  ");
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                    case 3://其他地方 暂时未知
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                }

                break;
            case 6:
                switch (getSourceType()) {
                    case 1://从达人荐加入想吃清单
                        content.append("从  达人荐  ");
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                    case 2://从动态加入想吃清单的总数
                        content.append("从  动态  ");
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                    case 3://其他地方 暂时未知
                        content.append("加入想吃清单总数达到");
                        content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                        break;
                }
                break;
        }
        return content.toString();
    }

}
