package com.fengnian.smallyellowo.foodie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.Adapter.CityAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.CityListBean.CityBean;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/7/17.
 */

public class SelectCityActivity extends BaseActivity<IntentData> {

    @Bind(R.id.city_list_view)
    RecyclerView cityListView;
    @Bind(R.id.error_layout)
    LinearLayout errorLayout;
    private CityAdapter mAdapter;
    private SelectCityReceiver mReceiver;
    private TextView cityName;
    private List<CityBean> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_city);
        setTitle("选择城市");
        if (getBackView() instanceof ImageView) {
            ((ImageView) getBackView()).setImageResource(R.mipmap.ic_close_three_level_page_black);
        }

        ButterKnife.bind(this);

        getBackView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

        initView();

        register();
    }

    @Override
    public boolean showClose() {
        return false;
    }

    private void register() {
        mReceiver = new SelectCityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IUrlUtils.Constans.ACTIION_SELECT_CITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    private void initView() {

        cityListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 20);
            }
        });
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityListView.setLayoutManager(llManager);
        mAdapter = new CityAdapter(this);
        cityList = CityPref.getCityList();
        if (!FFUtils.isListEmpty(cityList)){
            mAdapter.addDataList(cityList);
        }
        HeaderAndFooterRecyclerViewAdapter header = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayout headerView = (LinearLayout) View.inflate(this, R.layout.item_select_city_header_view, null);
        cityName = (TextView) headerView.findViewById(R.id.city_name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = FFUtils.getScreenWidth();
        headerView.setLayoutParams(params);
        header.addHeaderView(headerView);
        cityListView.setAdapter(header);

        mAdapter.setOnItemClick(new CityAdapter.OnItemClick() {
            @Override
            public void itemClickListener(CityBean city) {
                if (city == null){
                    return;
                }
                currentCity = city;
                bindCity(city.getId());
            }
        });

        getCityList();

        updateCity();
    }

    private CityListBean.CityBean currentCity;

    private void getCityList() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.list, null, extra, new FFNetWorkCallBack<CityListBean>() {
            @Override
            public void onSuccess(CityListBean response, FFExtraParams extra) {
                /*if (response == null) {
                    cityList = CityPref.getCityList();
                } else {
                    cityList = response.getCitys();
                    CityPref.saveCityList(cityList);
                }*/

                if (response == null){
                    return;
                }

                if (FFUtils.isListEmpty(response.getCitys())){
                    return;
                }

                cityList = response.getCitys();

                if (FFUtils.isListEmpty(cityList)) {
                    showErrorLayout(true);
                    return;
                }
                CityPref.saveCityList(cityList);
                showErrorLayout(false);
                updateCity();
                mAdapter.addDataList(cityList);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                cityList = CityPref.getCityList();
                if (!FFUtils.isListEmpty(cityList)) {
                    updateCity();
                    mAdapter.notifyDataSetChanged();
                } else {
//                    test();
                    showErrorLayout(true);
                }
                return false;
            }
        });
    }

    private void test() {
        cityList = new ArrayList<>();
        CityBean c1 = new CityBean();
        c1.setId("01");
        c1.setAreaName("北京");
        cityList.add(c1);

        CityBean c2 = new CityBean();
        c2.setId("28");
        c2.setAreaName("成都");
        cityList.add(c2);
        mAdapter.addDataList(cityList);
        showErrorLayout(false);
    }

    private void showErrorLayout(boolean show) {
        errorLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        cityListView.setVisibility(show ? View.GONE : View.VISIBLE);

        errorLayout.setOnClickListener(show ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityList();
            }
        } : null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goToMain();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        CityPref.setFirstStartApp(false);
        ButterKnife.unbind(this);
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    private void goToMain() {
        if (CityPref.isFirstStartApp()) {
            CityPref.setFirstStartApp(false);
            if (FFUtils.isStringEmpty(SP.getYoukeToken())) {
                startActivity(Main2Activity.class, new IntentData());
            } else {
                startActivity(MainActivity.class, new IntentData());
            }
        }
        finish();
    }

    private class SelectCityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*switch (intent.getAction()) {
                case IUrlUtils.Constans.ACTIION_SELECT_CITY:
                    CityBean cityBean = (CityBean) intent.getSerializableExtra("city");

                    bindCity(cityBean.getId());
                    break;
            }*/
        }
    }

    private void updateCity() {
        CityBean city = CityPref.getSelectedCity();
        String name = city.isChecked() ? city.getAreaName() : "未选择";
        cityName.setText("当前城市-" + name);
    }

    private void bindCity(String cityId) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.switchCity, null, extra, new FFNetWorkCallBack<CityAreaBean>() {
            @Override
            public void onSuccess(CityAreaBean response, FFExtraParams extra) {
                if (response == null) {
                    return;
                }

                if (response.getArea() == null) {
                    return;
                }

                List<CityAreaBean.AreaEntity.ListEntity> list = response.getArea().getList();
                if (FFUtils.isListEmpty(response.getArea().getList())) {
                    return;
                }
                currentCity.setChecked(true);
                CityPref.saveSelectedCity(currentCity);
                CityPref.saveCityAreaList(list);
                CityPref.saveCityFoodTypes(response.getCategories());
                CityPref.saveCityConfigInfo(response.getCityInfo());
                updateCity();
                mAdapter.notifyDataSetChanged();

                Intent selectIntent = new Intent();
                selectIntent.putExtra("city", currentCity);
                selectIntent.setAction(IUrlUtils.Constans.ACTIION_SELECT_CITY);
                LocalBroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(selectIntent);

                goToMain();

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "cityId", cityId);
    }

}
