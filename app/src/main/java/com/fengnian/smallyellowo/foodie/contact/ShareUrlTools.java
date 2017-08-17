package com.fengnian.smallyellowo.foodie.contact;

import com.fengnian.smallyellowo.foodie.appbase.BaseWebViewActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

/**
 * Created by Administrator on 2017-1-20.
 */

public class ShareUrlTools {
    private ShareUrlTools() {
    }

    /**
     * 动态富文本详情
     * detailInfoTypeCode 1
     *
     * @param feed
     * @return
     */
    public static String getDynamicRichUrl(SYFeed feed) {
        return getHtml5Url(BaseWebViewActivity.initParams(Constants.shareConstants().getShareUrlProfix() + "/record/"
                + feed.getFood().getId()
                + ".html?detailInfoTypeCode=" + 1 + "&detailInfoId=" + feed.getId(), false, null));
    }

    /**
     * 个人中心速记
     * detailInfoTypeCode 7
     *
     * @return
     */
    public static String getDynamicFastUrl(SYFeed feed) {
        return getHtml5Url(BaseWebViewActivity.initParams(Constants.shareConstants().getShareUrlProfix() + "/record/"
                + feed.getFood().getId()
                + ".html?detailInfoTypeCode=" + 7 + "&detailInfoId=" + feed.getFoodNoteId(), false, null));
    }

    /**
     * 商户详情
     * detailInfoTypeCode 3
     *
     * @return
     */
    public static String getRestInfoUrl(String id, String time) {
        return getHtml5Url(BaseWebViewActivity.initParams(Constants.shareConstants().getShareUrlProfix() + "/dis_shop_details/"
                + SP.getUid() + "_" + id + "_" + time
                + ".html?detailInfoTypeCode=" + 3 + "&detailInfoId=" + id, false, null));
    }

//    /**
//     * 对外个人页面
//     * detailInfoTypeCode 5
//     *
//     * @return
//     */
//    public static String getUserUrl() {
//        return "";
//    }

    /**
     * 个人中心富文本
     * detailInfoTypeCode 6
     *
     * @return
     */
    public static String getRichNoteUrl(SYFeed feed) {
        return getHtml5Url(BaseWebViewActivity.initParams(Constants.shareConstants().getShareUrlProfix() + "/record/"
                + feed.getFood().getId()
                + ".html?detailInfoTypeCode=" + 6 + "&detailInfoId=" + feed.getFoodNoteId(), false, null));
    }

    /**
     * 个人中心速记
     * detailInfoTypeCode 7
     *
     * @return
     */
    public static String getFastNoteUrl(SYFeed feed) {
        return getHtml5Url(BaseWebViewActivity.initParams(Constants.shareConstants().getShareUrlProfix() + "/record/"
                + feed.getFood().getId()
                + ".html?detailInfoTypeCode=" + 7 + "&detailInfoId=" + feed.getFoodNoteId(), false, null));
    }

    public static String getHtml5Url(String link) {
        while (link != null && link.endsWith("&")){//
            link = link.substring(0,link.length()-1);
        }
        return link;
    }
}
