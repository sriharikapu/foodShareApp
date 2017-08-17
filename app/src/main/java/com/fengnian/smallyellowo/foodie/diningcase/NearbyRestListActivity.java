package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.SYShopLocationResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;


public class NearbyRestListActivity extends BaseActivity<IntentData> {
    private ListView mListView;
    private PullToRefreshLayout prl;
    private MyAdapter mAdapter;
    private int mPageNum = 1;
    private ReplaceViewHelper mReplaceViewHelper;
    private EditText mEditSearch;
    private LocationHelper mLocationHelper;

    public static void start(Context context, double longitude, double latitude) {
        Intent intent = new Intent(context, NearbyRestListActivity.class);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, NearbyRestListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNotitle(true);
        setContentView(R.layout.nearby_rest_list_layout);
        mListView = findView(R.id.list_view);
        mReplaceViewHelper = new ReplaceViewHelper(this);
        mLocationHelper = new LocationHelper(this);

        View headerView = View.inflate(this, R.layout.nearby_rest_header, null);
        mListView.addHeaderView(headerView);
        mEditSearch = (EditText) findViewById(R.id.et_search);
        mAdapter = new MyAdapter(this);
        mListView.setAdapter(mAdapter);

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(true, getKeywords());
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false, getKeywords());
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(true);

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getData(true, getKeywords());
                }
                return true;
            }
        });

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(getKeywords())) {
                    getData(true, null);
                } else {
                    getData(true, getKeywords());
                }

            }
        });

        if (!FFUtils.checkNet()) {
            mReplaceViewHelper.toReplaceView(getContainer(), R.layout.ff_nonet_layout);
            mReplaceViewHelper.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDataList();
                }
            });
        } else {
            mReplaceViewHelper.removeView();
            initDataList();
        }
    }

    private void initDataList() {
        if (getIntent().hasExtra("longitude") && getIntent().hasExtra("latitude")) {
            Value.mLongitude = getIntent().getDoubleExtra("longitude", 0);
            Value.mLatitude = getIntent().getDoubleExtra("latitude", 0);
            getData(true, null);
        } else {
            startLocation();
        }
    }

    private void startLocation() {
        mLocationHelper.startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (isFinishing() || getDestroyed()){
                    mLocationHelper.stopLocation();
                    return;
                }
                if (location == null) {
                    return;
                }
                Value.mLatitude = location.getLatitude();
                Value.mLongitude = location.getLongitude();
                mLocationHelper.stopLocation();
                getData(true, null);
            }
        });
    }

    private String getKeywords() {
        String keyword = mEditSearch.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            return null;
        } else {
            return keyword;
        }
    }

    private void getData(final boolean isRefresh, final String keyWord) {
        String showText = null;
        if (isRefresh && TextUtils.isEmpty(keyWord)) {
            showText = "";
        }

        if (isRefresh) {
            mPageNum = 1;
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/searchShop.do",
        post(IUrlUtils.Search.search_shop,
                showText, null, new FFNetWorkCallBack<SYShopLocationResult>() {
                    @Override
                    public void onSuccess(SYShopLocationResult response, FFExtraParams extra) {
                        if (!TextUtils.isEmpty(keyWord) && !keyWord.equals(getKeywords())) {
                            return;
                        }

                        if (response != null && response.getList() != null) {
                            mPageNum++;
                            if (isRefresh) {
                                mAdapter.setDataList(response.getList());
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.getList());
                            }

                            if (response.getList().size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }
                        }

                        if (mAdapter.getCount() <= 0) {
                            mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
                        } else {
                            mReplaceViewHelper.removeView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "keyword", keyWord
                , "pageSize", 20
                , "pageNum", mPageNum
                , "longitude", Value.mLongitude
                , "latitude", Value.mLatitude);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class MyAdapter extends MyBaseAdapter<SYShopLocationResult.SYShopLocationModel> {
        private BaseActivity mActivity;

        public MyAdapter(BaseActivity activity) {
            super(activity);
            mActivity = activity;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.item_add_rest, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SYShopLocationResult.SYShopLocationModel item = getItem(position);
            holder.tv_name.setText(item.getTitle());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyActions.ACTION_NEARBY_BUSINESS_ITEM);
                    intent.putExtra("item", item);
                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    mActivity.finish();
                }
            });

            return convertView;
        }

        private static class ViewHolder {
            public TextView tv_name;
        }
    }

}
