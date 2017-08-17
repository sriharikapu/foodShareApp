package com.fengnian.smallyellowo.foodie.bean.food_history;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 已完成记录Model
 */

public class SYCompleteCount {
    public int count;//个数  是
    public int type;// 已完成记录样式 1速记 2富文本 3总的美食记录 4精选内容 5想吃记录 6速记美食图片 7富文本美食图片 8获得总积分数 是

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String gotTypeString() {
        switch (type) {
            case 1:
                return "已完成  " + count + "篇  美食速记";
            case 2:
                return "已完成  " + count + "篇  美食编辑";
            case 3:
                return "已完成  " + count + "篇  美食记录";
            case 4:
                return "已完成  " + count + "篇  精选内容";
            case 5:
                return "已完成  " + count + "篇  想吃记录";
            case 6:
                return "已完成  " + count + "张  美食速记照片分享";
            case 7:
                return "已完成  " + count + "张  美食编辑照片分享";
            case 8:
                return "已获得积分总数达到" + count;
        }
        return "";
    }

    public String providerTypeString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        switch (type) {
            case 1:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("篇美食速记");

                return sb.toString();
            case 2:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("篇美食编辑");

                return sb.toString();
            case 3:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("篇美食记录");

                return sb.toString();
            case 4:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("篇精选内容");

                return sb.toString();
            case 5:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("篇想吃记录");

                return sb.toString();
            case 6:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("张美食速记照片分享");

                return sb.toString();
            case 7:
                sb.append("已完成");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                sb.append("张美食编辑照片分享");

                return sb.toString();
            case 8:
                sb.append("已获得积分总数达到");
                sb.append("<font color='#F9A825'>").append(FFUtils.getMore999(count+"")).append("</font>");
                return sb.toString();
        }
        return "";
    }

    public int gotIcon() {
        switch (type) {
            case 1:
                return R.mipmap.growth_complate_ic_fast;
            case 2:
                return R.mipmap.growth_complate_ic_rich;
            case 3:
                return R.mipmap.growth_complate_ic_record;
            case 4:
                return R.mipmap.growth_complate_ic_jing;
            case 5:
                return R.mipmap.growth_complate_ic_want_eat;
            case 6:
                return R.mipmap.growth_complate_ic_picture;
            case 7:
                return R.mipmap.growth_complate_ic_picture;
            case 8:
                return R.mipmap.growth_complate_ic_score;
        }
        return 0;
    }
}
