package com.fengnian.smallyellowo.foodie.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.PGCActivity;
import com.fengnian.smallyellowo.foodie.PGCDetailActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.PgcModelsList;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016-7-19
 */
public class PGCFragment extends BaseFragment {
    private MyBaseAdapter<PGCHolder, PgcModelsList.ItemPGC> mAdapter;
    private List<PgcModelsList.ItemPGC> mPgcList;
    private PullToRefreshLayout prl;
    private ListView lv_pgc;
    private PGCActivity pgcActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pgcActivity = (PGCActivity) getActivity();
    }

    @Override
    public void onFindView() {
        lv_pgc = (ListView) findViewById(R.id.lv_pgc);
        prl = PullToRefreshLayout.supportPull(lv_pgc, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pgcActivity.getData(new OnFinishListener() {
                    @Override
                    public void onFinish(Object object) {
                        prl.refreshFinish(prl.SUCCEED);
                    }
                });
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        mAdapter = new MyAdapter(PGCHolder.class, activity, R.layout.item_main_home_pgc);
        if (mPgcList != null) {
            mAdapter.setData(mPgcList);
        }
        lv_pgc.setAdapter(mAdapter);

        prl.setDoPullDown(true);
        prl.setDoPullUp(false);

        lv_pgc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setItemClick(position);
            }
        });
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home_pgc, container, false);
    }

    public void setData(List<PgcModelsList.ItemPGC> list) {
        if (mAdapter == null) {
            mPgcList = list;
        } else {
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setItemClick(int position) {
        PGCDetailIntent data = new PGCDetailIntent();
        data.setUrl(mAdapter.getItem(position).htmlUrl);
        data.setTitle(mAdapter.getItem(position).title);
        data.setId(mAdapter.getItem(position).id);
        data.setImgUrl(mAdapter.getItem(position).cover);
        data.setAccount(SP.getUid());
        data.setToken(SP.getToken());
        data.setVersion(FFUtils.getVerName());
        mAdapter.getItem(position).views++;
        mAdapter.notifyDataSetChanged();
        startActivity(PGCDetailActivity.class, data);
    }

    private static final class MyAdapter extends MyBaseAdapter<PGCHolder, PgcModelsList.ItemPGC> {
        private BaseActivity mActivity;

        public MyAdapter(Class<? extends PGCHolder> clazz, Activity activity, int layoutId) {
            super(clazz, activity, layoutId);
            mActivity = (BaseActivity) activity;
        }


        @Override
        public void initView(View convertView, PGCHolder holder, int position, PgcModelsList.ItemPGC item) {
            FFImageLoader.loadBigImage(mActivity, item.cover, holder.iv_show);

            if (!TextUtils.isEmpty(item.description)) {
                holder.tv_content.setVisibility(View.VISIBLE);
                holder.tv_content.setText(item.description);
            } else {
                holder.tv_content.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.createTime)) {
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(item.createTime);
            } else {
                holder.tv_time.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.title)) {
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.tv_title.setText(item.title);
            } else {
                holder.tv_title.setVisibility(View.GONE);
            }

            holder.tv_view_times.setText("浏览" + item.views + "次");
        }

        @Override
        public void onGetView(PGCHolder holder) {
            holder.iv_show.getLayoutParams().height = (int) (FFUtils.getDisWidth() * 0.8);
        }
    }

    public static class PGCHolder {
        private ImageView iv_show;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        private TextView tv_view_times;
    }

}
