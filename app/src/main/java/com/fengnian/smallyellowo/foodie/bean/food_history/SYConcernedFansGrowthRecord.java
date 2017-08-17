package com.fengnian.smallyellowo.foodie.bean.food_history;

import android.view.View;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 关注或粉丝历程对象
 */

public class SYConcernedFansGrowthRecord extends BaseGrowthHistoryItem {
    public int type;//	1已获得XX粉丝 2已关注XX位用户	是
    public SYUser user;//	用户对象	是
    public int count;//	粉丝数或者关注数	是

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContentString() {
        StringBuilder content = new StringBuilder();
        content.append("  ");
        switch (getType()) {
            case 1://已获得XX粉丝
                content.append("已获得  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                content.append("位粉丝");

                break;
            case 2://已关注XX位用户
                content.append("已关注  ");
                content.append("<font color='#F9A825'>").append(FFUtils.getMore999(getCount()+"")).append("</font>");
                content.append("位用户");
                break;
        }
        return content.toString();
    }
}
