package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.base.FFBaseFragment;
import com.fan.framework.http.FFNetWorkRequest;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author
 * @version 1.0
 * @date 2016-7-19
 */
public abstract class BaseFragment extends FFBaseFragment {

    public BaseFragment(FFBaseActivity context) {
        this.activity = context;
    }

    public BaseFragment() {
    }

    @Override
    protected BaseActivity getBaseActivity() {
        return (BaseActivity) super.getBaseActivity();
    }

    protected void refreshAfterLogin() {
    }

    private boolean isYouke = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isYouke = TextUtils.isEmpty(SP.getUid()) ? true : false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        boolean isYouke = TextUtils.isEmpty(SP.getUid()) ? true : false;
        if (isYouke != this.isYouke) {
            refreshAfterLogin();
        }
        this.isYouke = isYouke;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public FFBaseActivity context() {
        return getActivity() == null ? activity : (FFBaseActivity) getActivity();
    }


    public <G extends IntentData> void startActivity(@NonNull Class<? extends BaseActivity<G>> clazz, G data) {
        startActivityForResult(new Intent(getActivity(), clazz).putExtra("IntentData", data), data.getRequestCode());
        if(clazz.getName().equals(LoginOneActivity.class.getName())){
            getActivity().overridePendingTransition(R.anim.switch_muban_activity_in, 0);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        intent.putExtra("childTags_baseActivity", activity.getChildTags());
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * 省掉强制转换
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void onPageInitFail(FFNetWorkRequest request) {
        super.onPageInitFail(request);
    }

    @Override
    public void onPageInitNoNet(FFNetWorkRequest request) {
        super.onPageInitNoNet(request);
    }

    @Override
    public void onPageInitNoData(FFNetWorkRequest request) {
        super.onPageInitNoData(request);
    }

    @Override
    public void onPageInitHasData(FFNetWorkRequest request) {
        super.onPageInitHasData(request);
    }
}
