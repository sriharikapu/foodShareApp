package com.fengnian.smallyellowo.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/29.
 */
public class NewsActivity extends BaseActivity {
    @Bind(R.id.wv_news)
    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_news);
        ButterKnife.bind(this);
        setTitle("最新活动");

        Intent intent = getIntent();
        String utl = intent.getStringExtra("utl");
        wvNews.loadUrl(utl);

    }
}
