package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.List;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-6-19.
 * 用的高德地图的周边数据
 */

public class NearbyGdDiningRoomActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private ListView mListView;
    private NearbyGdDiningRoomAdapter mAdapter;
    private PullToRefreshLayout prl;
    private EditText mEditSearch;
    private int mPage = 1;
    private ReplaceViewHelper mReplaceViewHelper;
    private GDPoiModel mGDPoiModel;

    public static void start(Context context) {
        Intent intent = new Intent(context, NearbyGdDiningRoomActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, GDPoiModel model) {
        Intent intent = new Intent(context, NearbyGdDiningRoomActivity.class);
        intent.putExtra("item", model);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGDPoiModel = (GDPoiModel) getIntent().getSerializableExtra("item");
        setNotitle(true);
        setContentView(R.layout.nearby_dining_room_layout);
        mListView = findView(R.id.list_view);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEditSearch = (EditText) findViewById(R.id.et_input);

        mAdapter = new NearbyGdDiningRoomAdapter(this);
        mAdapter.setGDPoiModel(mGDPoiModel);
        mListView.setAdapter(mAdapter);
        mReplaceViewHelper = new ReplaceViewHelper(this);

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (TextUtils.isEmpty(getKeywords())) {
                    getNearbyData(true);
                } else {
                    search(true, getKeywords());
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (TextUtils.isEmpty(getKeywords())) {
                    getNearbyData(false);
                } else {
                    search(false, getKeywords());
                }
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(true);

        mEditSearch.setHint("搜索附近位置");
        mEditSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
                    getNearbyData(true);
                } else {
                    search(true, getKeywords());
                }

            }
        });

        getNearbyData(true);
    }

    private void search(final boolean isRefresh, final String keywords) {
        if (isRefresh) {
            mPage = 1;
        }

        get(Value.GD_AROUND_SEARCH, null, null, new FFNetWorkCallBack<GDPoiList>() {
                    @Override
                    public void onSuccess(GDPoiList response, FFExtraParams extra) {
                        if (!keywords.equals(getKeywords())) {
                            return;
                        }
                        if (response != null && response.pois != null) {
                            mPage = mPage + 1;

                            if (isRefresh) {
                                mAdapter.setDataList(response.pois);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.pois);
                            }

                            if (response.pois.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }
                        }

                        if (mAdapter.getCount() <= 0) {
                            mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
                            TextView textView = (TextView) mReplaceViewHelper.getView().findViewById(R.id.text);
                            textView.setText(R.string.gd_keyword_search_empty);
                            prl.setDoPullUp(false);
                        } else {
                            mReplaceViewHelper.removeView();
                            prl.setDoPullUp(true);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "key", Value.GDkey
                , "keywords", keywords
                , "offset", 20
                , "page", mPage
                , "city", "beijing"
                , "citylimit", true
                , "sortrule", "distance");
    }


    /**
     * 进行加工处理，去除传进来的GDPoiModel item
     */
    private GDPoiModel handlingList(List<GDPoiModel> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }

        //把list的第一条置为当前位置
        list.get(0).isCurrentLocation = true;

        if (mGDPoiModel != null && !TextUtils.isEmpty(mGDPoiModel.id)) {
            for (GDPoiModel model : list) {
                if (model.id.equals(mGDPoiModel.id)) {
                    list.remove(model);
                    return model;
                }
            }
        }
        return null;
    }

    public void getNearbyData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        }

        get(Value.GD_AROUND, "", null, new FFNetWorkCallBack<GDPoiList>() {
                    @Override
                    public void onSuccess(GDPoiList response, FFExtraParams extra) {
                        if (response != null) {
                            GDPoiModel gdPoiModel = handlingList(response.pois);
                            mPage = mPage + 1;

                            if (isRefresh) {
                                //对传入的item进行处理，放在第一个
                                if (gdPoiModel != null) {
                                    response.pois.add(0, gdPoiModel);
                                }
                                mAdapter.setDataList(response.pois);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.pois);
                            }

                            if (response.pois.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }
                        }

                        if (mAdapter.getCount() <= 0) {
                            mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
                            TextView textView = (TextView) mReplaceViewHelper.getView().findViewById(R.id.text);
                            textView.setText(R.string.gd_keyword_search_empty);
                            prl.setDoPullUp(false);
                        } else {
                            mReplaceViewHelper.removeView();
                            prl.setDoPullUp(true);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "key", Value.GDkey
                , "location", Value.mLongitude + "," + Value.mLatitude
                , "offset", 20
                , "page", mPage
                , "radius", Value.GD_AROUND_RADIUS
                , "sortrule", "distance");
    }

    private String getKeywords() {
        return mEditSearch.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back || v.getId() == R.id.tv_cancel) {
            finish();
        }
    }

    public static final class GDPoiList extends BaseResult {
        public String status;
        public String count;
        public String info;
        public String infocode;
        public List<GDPoiModel> pois;
    }

}
