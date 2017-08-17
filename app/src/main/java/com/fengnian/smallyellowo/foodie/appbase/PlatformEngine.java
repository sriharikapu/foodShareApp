package com.fengnian.smallyellowo.foodie.appbase;

/**
 * Created by elaine on 2017/6/15.
 */

public class PlatformEngine {
    private static PlatformEngine instance;
    private TencentManager tencentManager;
    private SinaManager sinaManager;

    private PlatformEngine() {

    }

    public static PlatformEngine getInstance() {
        if (instance == null) {
            synchronized (PlatformEngine.class) {
                if (instance == null) {
                    instance = new PlatformEngine();
                }
            }
        }

        return instance;
    }

    public TencentManager getTencentManager() {
        if (tencentManager == null) {
            tencentManager = new TencentManager();
        }
        return tencentManager;
    }

    public SinaManager getSinaManager() {
        if (sinaManager == null) {
            sinaManager = new SinaManager();
        }
        return sinaManager;
    }
}
