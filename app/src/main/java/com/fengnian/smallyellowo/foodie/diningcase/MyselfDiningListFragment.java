package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by chenglin on 2017-6-16.
 */

public class MyselfDiningListFragment extends BaseFragment implements View.OnClickListener {
    private ListView mListView;
    private MyselfDiningListAdapter mAdapter;
    private View mTitle;
    private ReplaceViewHelper mReplaceViewHelper;
    private MyselfDiningActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFindView() {
        mListView = findView(R.id.list_view);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myself_dining_list_layout, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof MyselfDiningActivity) {
            mActivity = (MyselfDiningActivity) getActivity();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReplaceViewHelper = new ReplaceViewHelper(getActivity());
        view.findViewById(R.id.new_build).setOnClickListener(this);
        view.findViewById(R.id.my_back_view).setOnClickListener(this);
        mTitle = findViewById(R.id.title_1);
        mTitle.setVisibility(View.INVISIBLE);

        View s_status_bar = view.findViewById(R.id.s_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) s_status_bar.getLayoutParams();
        params.height = FFUtils.getStatusbarHight(getActivity());
        s_status_bar.setLayoutParams(params);

        View headerLine = new View(getActivity());
        headerLine.setBackgroundColor(getResources().getColor(R.color.normal_bg));
        headerLine.setLayoutParams(new AbsListView.LayoutParams(-1, DisplayUtil.dip2px(10f)));
        mListView.addHeaderView(headerLine);

        mAdapter = new MyselfDiningListAdapter(this);
        mListView.setAdapter(mAdapter);

        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showEmpty() {
        mReplaceViewHelper.toReplaceView(mListView, R.layout.ff_empty_layout);
        mActivity.onNewBuild();
    }

    public void getData() {
        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/onePersonFood/queryOneFoodTjList.do",
                "", null, new FFNetWorkCallBack<ListModel>() {
                    @Override
                    public void onSuccess(ListModel response, FFExtraParams extra) {
                        if (response != null && response.list != null) {
                            mAdapter.setDataList(response.list);
                        }

                        if (mAdapter.getCount() > 0) {
                            mTitle.setVisibility(View.VISIBLE);
                            mReplaceViewHelper.removeView();
                        } else {
                            mActivity.onHasDiningList(false);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_build) {
            HashMap<String, String> event = new HashMap<String, String>();
            event.put("account", SP.getUid());
            event.put("city", CityPref.getSelectedCity().getAreaName());
            mActivity.pushEventAction("Yellow_181", event);

            mActivity.onNewBuild();
        } else if (v.getId() == R.id.my_back_view) {
            getActivity().finish();
        }
    }

    public static final class ListModel extends BaseResult {
        public List<ItemModel> list;
    }

    public static final class ItemModel {
        public String id;
        public String account;
        public String position;
        public String ptype;
        public String createTime;
        public String city;
        public String longitude;
        public String latitude;
    }
}
