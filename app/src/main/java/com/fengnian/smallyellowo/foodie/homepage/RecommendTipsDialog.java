package com.fengnian.smallyellowo.foodie.homepage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;

/**
 * Created by chenglin on 2017-7-19.
 */

public class RecommendTipsDialog extends Dialog {
    private BaseActivity mActivity;

    public RecommendTipsDialog(Context context) {
        this(context, R.style.dialog);
    }

    public RecommendTipsDialog(Context context, int themeResId) {
        super(context, themeResId);
        mActivity = (BaseActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);

        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.dialog_theme_animation_top);
        WindowManager.LayoutParams param = window.getAttributes();
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(param);
    }

    /**
     * 第一次推荐对话框
     */
    public void addFirstRecommendDialog(View.OnClickListener listener) {
        setContentView(R.layout.home_recommend_first_dialog_layout);
        findViewById(R.id.btn_sure).setOnClickListener(listener);
        show();
    }

    /**
     * 不是第一次推荐对话框
     */
    public void addRecommendDialog(final OnFinishListener listener) {
        setCancelable(false);
        setContentView(R.layout.home_recommend_dialog_layout);
        show();

        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null && !mActivity.isFinishing()) {
                    if (isShowing()) {
                        dismiss();
                        listener.onFinish(null);
                    }
                }
            }
        }, 1200);
    }
}
