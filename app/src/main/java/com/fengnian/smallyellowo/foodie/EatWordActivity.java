package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.YdmsResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EatWordActivity extends BaseActivity<IntentData> {

    @Bind(R.id.listView)
    ListView listView;
    private MyBaseAdapter<HolderActionHistory, YdmsResult.SYDifferentPlaceFoodModel> adapter;
    private PullToRefreshLayout prl;

    public static class HolderActionHistory {
        public CircleImageView iv_img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_word);
        ButterKnife.bind(this);
        setTitle("异地美食");
        adapter = new MyBaseAdapter<HolderActionHistory, YdmsResult.SYDifferentPlaceFoodModel>(HolderActionHistory.class, this, R.layout.item_eat_word) {
            @Override
            public void initView(View convertView, HolderActionHistory holder, int position, final YdmsResult.SYDifferentPlaceFoodModel item) {
                FFImageLoader.loadBigImage(context(), item.getDifferentPlaceFoodImage().getUrl(), holder.iv_img);
                holder.iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebInfo data = new WebInfo();
                        data.setUrl(item.getDifferentPlaceFoodHtmlUrl());
//                        data.setTitle(item_image_gallery.getd);
                        startActivity(CommonWebviewUtilActivity.class, data);
                    }
                });
            }

            @Override
            public void onGetView(HolderActionHistory holder) {
                holder.iv_img.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(18)) * 306 / 708;
            }
        };
        listView.setAdapter(adapter);
        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullDown(true);
        prl.setDoPullUp(true);
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 500);
    }

    private void getData(final boolean isRefresh) {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/page/getDiffPlaceFood.do", isRefresh ? null : "", new FFExtraParams().setInitPage(isRefresh && adapter.getCount() == 0), new FFNetWorkCallBack<YdmsResult>() {
        post(IUrlUtils.Search.getDiffPlaceFood, isRefresh ? null : "", new FFExtraParams().setInitPage(isRefresh && adapter.getCount() == 0), new FFNetWorkCallBack<YdmsResult>() {
            @Override
            public void onSuccess(YdmsResult response, FFExtraParams extra) {
                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (FFUtils.isListEmpty(response.getData())) {
                    prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                } else {
                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                if (isRefresh) {
                    adapter.setData(response.getData());
                } else {
                    adapter.addData(response.getData());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                prl.refreshFinish(PullToRefreshLayout.FAIL);
                return false;
            }
        }, "pageSize", isRefresh ? 15 : 8, "lastId", isRefresh ? 0 : (adapter.isEmpty() ? "0":adapter.getItem(adapter.getCount()-1).getDifferentPlaceFoodId()));
    }
}
