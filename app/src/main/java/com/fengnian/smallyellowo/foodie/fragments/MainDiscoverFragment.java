package com.fengnian.smallyellowo.foodie.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fan.framework.base.Test.StringUtils;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.ClubActivity;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.DeliciousFoodMapActivity;
import com.fengnian.smallyellowo.foodie.PGCActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.RestSearchResultActivity;
import com.fengnian.smallyellowo.foodie.SearchRestActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYBusinessCircleAreaModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodKindModel;
import com.fengnian.smallyellowo.foodie.bean.results.DiscoverResult;
import com.fengnian.smallyellowo.foodie.diningcase.MyselfDiningActivity;
import com.fengnian.smallyellowo.foodie.diningcase.TogetherDiningActivity;
import com.fengnian.smallyellowo.foodie.diningcase.WorkDiningActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestSearchResultIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SearchRestIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainDiscoverFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_nearby)
    TextView tvNearby;
    @Bind(R.id.fl_search)
    FrameLayout flSearch;
    @Bind(R.id.iv_club_entrance)
    ImageView iv_club_entrance;
    @Bind(R.id.iv_pgc_entrance)
    ImageView iv_pgc_entrance;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    private ImageView iv_lunch_working_day;
    private ImageView iv_friends_dinner;
    private ImageView iv_ones_dinner;
    private ImageView iv_aftea;
    private BroadcastReceiver mBroadcastReceiver;

    public MainDiscoverFragment() {
        this(null);
    }

    @SuppressLint("ValidFragment")
    public MainDiscoverFragment(BaseActivity context) {
        super(context);
    }

    @Override
    public void onFindView() {
        iv_lunch_working_day = (ImageView) findViewById(R.id.iv_lunch_working_day);
        iv_friends_dinner = (ImageView) findViewById(R.id.iv_friends_dinner);
        iv_ones_dinner = (ImageView) findViewById(R.id.iv_ones_dinner);
        iv_aftea = (ImageView) findViewById(R.id.iv_aftea);
        iv_club_entrance.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(16)) * 452 / 1023;
        iv_pgc_entrance.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(16)) * 452 / 1023;
        int width = (FFUtils.getDisWidth() - FFUtils.getPx(30)) / 2;
        int height = width * 273 / 519;

        iv_lunch_working_day.getLayoutParams().width = width;
        iv_lunch_working_day.getLayoutParams().height = height;
        iv_friends_dinner.getLayoutParams().width = width;
        iv_friends_dinner.getLayoutParams().height = height;
        iv_ones_dinner.getLayoutParams().width = width;
        iv_ones_dinner.getLayoutParams().height = height;
        iv_aftea.getLayoutParams().width = width;
        iv_aftea.getLayoutParams().height = height;

        iv_lunch_working_day.setOnClickListener(this);
        iv_friends_dinner.setOnClickListener(this);
        iv_ones_dinner.setOnClickListener(this);
        iv_aftea.setOnClickListener(this);
        setDiningCaseView();

        DiscoverResult data = SP.getDiscoverData();
        if (data == null) {
            scrollView.setVisibility(View.GONE);
        } else {
            initData(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/homePage.do", null, null, new FFNetWorkCallBack<DiscoverResult>() {
        post(IUrlUtils.Search.homePage, null, null, new FFNetWorkCallBack<DiscoverResult>() {
            @Override
            public void onSuccess(DiscoverResult response, FFExtraParams extra) {
                SP.setDiscoverData(extra.getReponseString());
                initData(response);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    private void initData(DiscoverResult response) {


        scrollView.setVisibility(View.VISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    showToast("网络连接失败，请检查网络设置");
                    return;
                }
                SYBusinessCircleAreaModel tag = (SYBusinessCircleAreaModel) v.getTag();
                RestSearchResultIntent intent = new RestSearchResultIntent();
                if (tag instanceof SYFoodKindModel) {
                    intent.setContent(tag.getContent());
                    intent.setType(RestSearchResultIntent.TYPE_CLASS);
                } else {
                    SiftBean.BusinessListBean.BusinessGroup.Business tag1 = new SiftBean.BusinessListBean.BusinessGroup.Business();
                    tag1.setId(tag.getAreaId());
                    tag1.setContent(tag.getContent());
                    intent.setArea(tag1);
                    intent.setType(RestSearchResultIntent.TYPE_AREA);
                }
                startActivity(RestSearchResultActivity.class, intent);
            }
        };
    }

    /**
     * 切换城市时隐藏就餐场景，只有北京才有就餐场景
     */
    private void setDiningCaseView(){
        View dining_case_1 = findViewById(R.id.dining_case_1);
        View dining_case_2 = findViewById(R.id.dining_case_2);
        if (CityPref.isBeiJing()) {
            dining_case_1.setVisibility(View.VISIBLE);
            dining_case_2.setVisibility(View.VISIBLE);
        } else {
            dining_case_1.setVisibility(View.GONE);
            dining_case_2.setVisibility(View.GONE);
        }
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(IUrlUtils.Constans.ACTIION_SELECT_CITY)) {
                    setDiningCaseView();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IUrlUtils.Constans.ACTIION_SELECT_CITY);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return super.getLayoutInflater(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_discover, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_nearby, R.id.fl_search, R.id.iv_pgc_entrance, R.id.iv_club_entrance, R.id.iv_map_enter})
    public void onClick(View view) {
        if (!FFUtils.checkNet()) {
            showToast("网络连接失败，请检查网络设置");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_nearby:
                RestSearchResultIntent intent = new RestSearchResultIntent();
                intent.setType(RestSearchResultIntent.TYPE_NEARBY);
                startActivity(RestSearchResultActivity.class, intent);
                break;
            case R.id.fl_search:
                startActivity(SearchRestActivity.class, new SearchRestIntent(2, ""));
                break;
            case R.id.iv_pgc_entrance:
                startActivity(PGCActivity.class, new IntentData());
                break;
            case R.id.iv_club_entrance:
                if (!SP.isLogin()) {
                    LoginOneActivity.startLogin(getBaseActivity());
                    return;
                }

                if (CityPref.getSelectedCity() != null && !CityPref.isBeiJing()) {
                    WebInfo data = new WebInfo();
                    data.setTitle("俱乐部专栏");
                    if (CityPref.getCityConfigInfo() == null){
                        return;
                    }

                    String url = CityPref.getCityConfigInfo().getClubUrl();
                    if (TextUtils.isEmpty(url)){
                        return;
                    }
                    data.setUrl(url); // 多城市俱乐部专栏html5链接
                    startActivity(CommonWebviewUtilActivity.class, data);
                    return;
                }

                startActivity(ClubActivity.class, new IntentData());
                break;

            case R.id.iv_map_enter:
                startActivity(DeliciousFoodMapActivity.class, new IntentData());
                break;
            case R.id.iv_lunch_working_day://工作日
                if (!FFUtils.checkNet()) {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                } else if (!SP.isLogin()) {
                    LoginOneActivity.startLogin(getBaseActivity());
                } else {

                    pushEvent("Yellow_151");

                    startActivity(WorkDiningActivity.class, new IntentData());
                }
                break;
            case R.id.iv_friends_dinner://朋友聚餐
                if (!FFUtils.checkNet()) {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                } else if (!SP.isLogin()) {
                    LoginOneActivity.startLogin(getBaseActivity());
                } else {

                    pushEvent("Yellow_152");

                    startActivity(TogetherDiningActivity.class, new IntentData());
                }
                break;
            case R.id.iv_ones_dinner://吃独食
                if (!FFUtils.checkNet()) {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                } else if (!SP.isLogin()) {
                    LoginOneActivity.startLogin(getBaseActivity());
                } else {

                    pushEvent("Yellow_153");

                    startActivity(MyselfDiningActivity.class, new IntentData());
                }
                break;
            case R.id.iv_aftea://敬请期待
//                startActivity(DeliciousFoodMapActivity.class, new IntentData());
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void pushEvent(String eventId){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("city", CityPref.getSelectedCity().getAreaName());
        getBaseActivity().pushEventAction(eventId, event);
    }
}
