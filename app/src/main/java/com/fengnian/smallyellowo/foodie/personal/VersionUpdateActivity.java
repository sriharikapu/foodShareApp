package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLoger;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.BuildConfig;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.CheckVersionResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-2.
 */

public class VersionUpdateActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private TextView tv_version, tv_2, tv_genxin;
    private ImageView iv_network_exception;
    private ListView lv_up_content;
    private ContentAdapter adapter;
    private RelativeLayout rl_1,rl_4;
    private  ImageView iv_have_new_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_version_updptae);
        rl_1 = findView(R.id.rl_1);
        tv_version = findView(R.id.tv_version);
        iv_network_exception = findView(R.id.iv_network_exception);
        tv_2 = findView(R.id.tv_2);
        lv_up_content = findView(R.id.lv_up_content);
        adapter = new ContentAdapter(VersionUpdateActivity.viewholder.class, R.layout.item_update_content, list, this);
        tv_version.setText(FFUtils.getVerName());
        tv_genxin = findView(R.id.tv_genxin);
        iv_have_new_version=findView(R.id.iv_have_new_version);

        rl_4=findView(R.id.rl_4);
        tv_genxin.setOnClickListener(this);
        findView(R.id.tv_version);
        if (FFUtils.getCurrentNetType() == -1) {
            rl_1.setVisibility(View.GONE);
            iv_network_exception.setVisibility(View.VISIBLE);
            return;
        }
        check_app_version();

        //显示debug build 信息
        if (!Constants.shareConstants().isPublishEnvironment()) {
            tv_2.setText("当前已是最新版本\r\n" +
                    "打包时间" + FFUtils.getAppBuildTime() + "\r\n" +
                    "BuildConfig.BUILD_TYPE_DEFINE=" + BuildConfig.BUILD_TYPE_DEFINE + "\r\n" +
                    "URL=" + Constants.shareConstants().getNetHeaderAdress() + "\r\n" +
                    "SHARE_URL=" + Constants.shareConstants().getShareUrlProfix() + "\r\n" +
                    "BuildConfig.DEBUG=" + BuildConfig.DEBUG + "\r\n" +
                    "\r\n以上信息不会出现在正式版本\r\n\r\n点击可清空日志");
            tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FFLoger.clean();
                }
            });
        }
    }

    private void check_app_version() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/version/queryNewVersonInformation.do", "", extra, new FFNetWorkCallBack<CheckVersionResult>() {
        post(IUrlUtils.Search.queryNewVersonInformation, "", extra, new FFNetWorkCallBack<CheckVersionResult>() {
            @Override
            public void onSuccess(CheckVersionResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    SetUpdateVersion(response);
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "os", "2");
    }

    private String url;//去下载的链接地址
    List<String> list = new ArrayList<String>();

    private void SetUpdateVersion(CheckVersionResult response) {

        if ("1".equals(response.getHaveNewVersion())) {
            //有新版本
            iv_have_new_version.setVisibility(View.VISIBLE);
            tv_2.setVisibility(View.GONE);
            rl_4.setVisibility(View.VISIBLE);
            List<String> mlist = new ArrayList<String>();
            mlist.addAll(response.getContent());
            int length = mlist.size();
            for (int i = 0; i < mlist.size() ; i++) {
                list.add(mlist.get(i) + "\n");
            }
            lv_up_content.setVisibility(View.VISIBLE);
            lv_up_content.setAdapter(adapter);
            url = response.getUrl();
            tv_genxin.setVisibility(View.VISIBLE);


        }
        if ("0".equals(response.getHaveNewVersion())) {
            tv_2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_genxin:
                //TODO  跳转到下载的页面

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    public class ContentAdapter extends FFBaseAdapter {

        public ContentAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
            super(viewHolderClass, layoutId, dataList, context);
        }

        public ContentAdapter(Class viewHolderClass, int layoutId, FFContext context) {
            super(viewHolderClass, layoutId, context);
        }

        @Override
        public void setViewData(Object viewHolder, int position, Object model) {

            VersionUpdateActivity.viewholder holder = (VersionUpdateActivity.viewholder) viewHolder;
            holder.tv_content.setText(list.get(position));

        }


    }

    public static class viewholder {
        public TextView tv_content;
    }
}
