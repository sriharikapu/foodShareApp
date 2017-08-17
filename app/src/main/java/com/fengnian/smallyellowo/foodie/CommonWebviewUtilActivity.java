package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;

import com.fengnian.smallyellowo.foodie.appbase.BaseWebViewActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;


public class CommonWebviewUtilActivity extends BaseWebViewActivity<WebInfo> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntentData().isMenuVisible()) {
            setMenuView(View.GONE);
        }
    }

    @Override
    public String getTitleString() {
        return getIntentData().getTitle();
    }

    @Override
    public String getUrl() {
        return getIntentData().getUrl();
    }

    @Override
    public String[] getParams() {
        return new String[0];
    }
}
