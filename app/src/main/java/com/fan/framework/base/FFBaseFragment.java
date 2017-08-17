package com.fan.framework.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.http.FFNetWorkRequest;
import com.fengnian.smallyellowo.foodie.R;

import java.util.ArrayList;

public abstract class FFBaseFragment extends Fragment implements FFContext {

    public final String TAG;
    public FFNetWork mNet;
    private ViewGroup view;
    public FFBaseActivity activity;
    boolean isDestoryed = false;
    public TextView failView;


    public View getContainr(){
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = (FFBaseActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public ArrayList<String> getChildTags() {
        return null;
    }

    @Override
    public ArrayList<String> getTags() {
        return null;
    }

    public FFBaseFragment() {
        this(null);
    }

    public View findViewById(int id) {
        return view.findViewById(id);

    }

    public FFBaseFragment(FFBaseActivity activity) {
        TAG = getClass().getSimpleName();
        mNet = new FFNetWork(this);
        this.activity = activity;
    }

    protected FFBaseActivity getBaseActivity() {
        return activity == null ? (FFBaseActivity) getActivity() : activity;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = (ViewGroup) inflater.inflate(R.layout.fragment_base, container,false);
        failView = (TextView) view.findViewById(R.id.base_tv_noNet);
        view.addView(getView(inflater, view, savedInstanceState),0);
        onFindView();
        return view;
    }

    /**
     * 初始化控件
     */
    public abstract void onFindView();

    public boolean getIsNeedLoadData() {
        return true;
    }

    public abstract View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    @Override
    public void onDestroy() {
        isDestoryed = true;
        super.onDestroy();
    }

    public boolean isActivityDestroyed() {
        if (getActivity() == null) {
            return false;
        }
        return ((FFBaseActivity) getActivity()).getDestroyed();
    }

    @Override
    public boolean getDestroyed() {
        return isDestoryed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void showToast(CharSequence text) {
        showToast(text, null);
    }

    @Override
    public void showToast(int resId, CharSequence text, CharSequence debugMsg) {
        if (FFConfig.SHOW_DEBUG_TOAST && debugMsg != null) {
            SpannableString ss = new SpannableString(debugMsg + "\n" + text);
            ss.setSpan(new ForegroundColorSpan(0xffff8888), 0, debugMsg != null ? debugMsg.toString().length() : 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            showToast(resId, ss);
        } else {
            if (text != null) {
                showToast(resId, text);
                return;
            }
        }
    }

    @Override
    public void showToast(CharSequence msg, CharSequence debugMsg) {
        FFApplication.showToast(msg, debugMsg);
    }

    @Override
    public void showToast(int resId, CharSequence text) {
        FFApplication.showToast(resId, text);
    }

    @Override
    public int showProgressDialog(CharSequence word) {
        if (getActivity() != null) {
            return ((FFBaseActivity) getActivity()).showProgressDialog(word);
        }
        return 0;
    }

    @Override
    public int showProgressDialog(CharSequence word, boolean cancelAble) {
        if (getActivity() != null) {
            return ((FFBaseActivity) getActivity()).showProgressDialog(word, cancelAble);
        }
        return 0;
    }

    @Deprecated
    @Override
    public void startActivity(Intent intent) {
        if (intent == null) {
            return;
        }
        intent.putExtra("origin_baseActivity", activity.getClass().getName());
        intent.putExtra("origin_fragment_baseActivity", this.getClass().getName());
        super.startActivity(intent);
    }

    boolean skiped = false;

    @Deprecated
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            return;
        }
        intent.putExtra("origin_baseActivity", activity.getClass().getName());
        intent.putExtra("origin_fragment_baseActivity", this.getClass().getName());
        if (intent == null) {
            return;
        }
        synchronized (this) {
            if (skiped) {
                return;
            }
            skiped = true;
        }
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        skiped = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        skiped = false;
    }

    @Override
    public void dismissProgressDialog(int id) {
        if (getActivity() != null) {
            ((FFBaseActivity) getActivity()).dismissProgressDialog(id);
        }
    }

    public <T extends FFBaseBean> void post(String url, String words, FFExtraParams extra, FFNetWorkCallBack<T> callBack, Object... params) {
        mNet.post(url, words, extra, callBack, params);
    }

    public <T extends View> T getView(int id) {
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(id);
    }

    public <T extends FFBaseBean> void get(String url, String words, FFExtraParams extra, FFNetWorkCallBack<T> callBack, Object... params) {
        mNet.get(url, words, extra, callBack, params);
    }
    @Override
    public void onPageInitFail(final FFNetWorkRequest request) {
        failView.setVisibility(View.VISIBLE);
        failView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pic_fail_no_net,0,0);
        failView.setText("加载失败了,点击重试一下");
        failView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failView.setVisibility(View.GONE);
                onPageInitRetry(request);
            }
        });
    }

    @Override
    public void onPageInitNoNet(final FFNetWorkRequest request) {
        failView.setVisibility(View.VISIBLE);
        failView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pic_fail_no_net,0,0);
        failView.setText("网络异常，加载失败了\r\n点击重试一下");
        failView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failView.setVisibility(View.GONE);
                onPageInitRetry(request);
            }
        });
    }

    @Override
    public void onPageInitNoData(FFNetWorkRequest request) {
        failView.setVisibility(View.VISIBLE);
        failView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pic_fail_nodata,0,0);
        failView.setText("没有相关记录");
        failView.setOnClickListener(null);
    }

    @Override
    public void onPageInitHasData(FFNetWorkRequest request) {
        failView.setVisibility(View.GONE);
        failView.setOnClickListener(null);
    }

    @Override
    public void onPageInitRetry(FFNetWorkRequest request) {
        request.getNet().excute(request);
    }
}