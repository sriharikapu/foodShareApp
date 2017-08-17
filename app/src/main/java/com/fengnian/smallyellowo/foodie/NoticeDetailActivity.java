package com.fengnian.smallyellowo.foodie;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseWebViewActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.NoticeDetailIntent;

public class NoticeDetailActivity extends BaseWebViewActivity<NoticeDetailIntent> {

    @Override
    public String[] getParams() {
        return new String[]{"noticeId",getIntentData().getBean().getMessageId()+""};
    }
    @Override
    public String getTitleString() {
        if(FFUtils.isStringEmpty(webView.getTitle())){
            return "详情";
        }else{
            return webView.getTitle();
        }
    }

    @Override
    public String getUrl() {
        return getIntentData().getBean().getMessageHtmlUrl();
    }
}
