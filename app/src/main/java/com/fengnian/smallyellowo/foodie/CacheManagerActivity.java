package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.imageloader.NativeAndNetImageMapManager;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CacheManagerActivity extends BaseActivity<IntentData> {

    @Bind(R.id.tv_total_memory)
    TextView tvTotalMemory;
    @Bind(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_manager);
        ButterKnife.bind(this);
        tvTotalMemory.setText(FFUtils.getSubFloat(FFImageLoader.getCacheMemory() * 1f / 1024 / 1024) + "M");
    }

    @OnClick(R.id.button)
    public void onClick() {
        final int id = showProgressDialog("", false);
        new Thread() {
            @Override
            public void run() {
                FFImageLoader.cleanCache();
                NativeAndNetImageMapManager.clear();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog(id);
                        showToast("已成功清空缓存");
                        tvTotalMemory.setText("0M");

                    }
                });
            }
        }.start();

    }
}
