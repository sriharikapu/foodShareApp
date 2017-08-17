package com.fengnian.smallyellowo.foodie.bean.food_history;

import android.text.TextUtils;
import android.view.View;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 赞或评论的历程对象
 */

public class SYCommentGrowthRecord extends BaseGrowthHistoryItem {
    public int type;//1收到的赞总数 2某条文章被赞总数  3自己点赞的总数 4收到的总评论数 5某条文字被评论总数 6自己发评论的总数	是
    public int count;//赞数或评论数	是
    public SYImage coverImage;//封面图	是
    public SYUser user;//用户对象	是
    public String title;//美食标题	是
    public String buinessName;//商户名	是
    public String feedID;//动态id	是
    public String foodNoteID;//个人中心id	否
    public String commentID;//评论id	是
    public String commentContent;//评论内容	是
    public int commentGrowthReleaseTemplateType;//评论内容	是
    public int commentGrowthFoodtype;//评论内容	是

    public int getCommentGrowthReleaseTemplateType() {
        return commentGrowthReleaseTemplateType;
    }

    public void setCommentGrowthReleaseTemplateType(int commentGrowthReleaseTemplateType) {
        this.commentGrowthReleaseTemplateType = commentGrowthReleaseTemplateType;
    }

    public int getCommentGrowthFoodtype() {
        return commentGrowthFoodtype;
    }

    public void setCommentGrowthFoodtype(int commentGrowthFoodtype) {
        this.commentGrowthFoodtype = commentGrowthFoodtype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SYImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(SYImage coverImage) {
        this.coverImage = coverImage;
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

    public String getBuinessName() {
        return buinessName;
    }

    public void setBuinessName(String buinessName) {
        this.buinessName = buinessName;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getFoodNoteID() {
        return foodNoteID;
    }

    public void setFoodNoteID(String foodNoteID) {
        this.foodNoteID = foodNoteID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getContentString() {
        StringBuilder content = new StringBuilder();
        content.append("  ");
        switch (getType()) {//
            case 1://收到的赞总数
                content.append("收到的赞总数达到  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                break;
            case 2://某条文章被赞总数

                if (!TextUtils.isEmpty(getTitle())) {
                    content.append("《" + FFUtils.subStr10(getTitle()) + "》");

                } else if (!TextUtils.isEmpty(getBuinessName())) {
                    content.append("《" + FFUtils.subStr10(getBuinessName()) + "》");
                }
                content.append("被赞总数达到  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 3://自己点赞的总数
                content.append("点赞的总数达到  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 4://收到的总评论数
                content.append("你收到的评论总数达到");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 5://某条文字被评论总数
                if (!TextUtils.isEmpty(getTitle())) {
                    content.append("《" + FFUtils.subStr10(getTitle()) + "》");

                } else if (!TextUtils.isEmpty(getBuinessName())) {
                    content.append("《" + FFUtils.subStr10(getBuinessName()) + "》");
                }
                content.append("被评论总数达到  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
            case 6://评论总数达到
                content.append("你评论的总数达到");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");

                break;
        }

        return content.toString();
    }
}
