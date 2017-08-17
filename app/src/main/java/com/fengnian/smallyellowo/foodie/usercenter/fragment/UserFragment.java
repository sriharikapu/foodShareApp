package com.fengnian.smallyellowo.foodie.usercenter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.UserDynamicResult;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.fengnian.smallyellowo.foodie.recyclerview.RecyclerViewUtils;
import com.fengnian.smallyellowo.foodie.usercenter.adapter.UserAdapter;
import com.fengnian.smallyellowo.foodie.usercenter.bean.UserInfoBean;
import com.fengnian.smallyellowo.foodie.usercenter.widgets.UserInfoHeaderView;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/12.
 */

public class UserFragment extends BaseFragment {

    @Bind(R.id.list_view)
    RecyclerView listView;
    @Bind(R.id.tv_no_net)
    RelativeLayout rvFailOne;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    /*@Bind(R.id.iv_close)
    ImageView ivClose;*/
    @Bind(R.id.tv_add_attention)
    TextView tvAddAttention;
    @Bind(R.id.title_ll)
    RelativeLayout titleLl;
    @Bind(R.id.user_title)
    TextView userTitle;
    @Bind(R.id.s_status_bar)
    View sStatusBar;

    private UserInfoHeaderView headerView;
    private Drawable img_attented, img_add_attent, img_eachother, img_eachothered;
    private final ArrayList<SYFeed> list_net = new ArrayList<>();
    private UserAdapter mAdapter;
    private String userId;
    private SYUser user;
    private int flag = 1;
    private PullToRefreshLayout prl;
    private boolean isMe = false;

    @Override
    public void onFindView() {

    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_user, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sStatusBar.getLayoutParams();
        params.height = FFUtils.getStatusbarHight(getActivity());
        sStatusBar.setLayoutParams(params);

        user = getArguments().getParcelable("user");
        userId = getArguments().getString("user_id");
        isMe = SP.getUid().equalsIgnoreCase(userId);
        if (isMe){
            tvAddAttention.setVisibility(View.GONE);
        } else {
            tvAddAttention.setVisibility(View.VISIBLE);
        }
        initDrawable();
        headerView = new UserInfoHeaderView(getActivity(), userId);
//        headerView.setUser(user, false);
        mAdapter = new UserAdapter(getActivity(), list_net);
        HeaderAndFooterRecyclerViewAdapter header = new HeaderAndFooterRecyclerViewAdapter(mAdapter);

        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData();
            }
        });
        prl.setDoPullUp(true);

        LinearLayoutManager linear = new LinearLayoutManager(getActivity());
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linear);
        listView.setAdapter(header);

        RecyclerViewUtils.setHeaderView(listView, headerView);
        getUserInfo();
        getData();

        setClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FFUtils.checkNet()) {
            rvFailOne.setVisibility(View.GONE);
        } else {
            rvFailOne.setVisibility(View.VISIBLE);
        }
    }

    private void initDrawable() {
        img_eachother = this.getResources().getDrawable(R.mipmap.ic_attention_eachother);
        img_eachother.setBounds(0, 0, img_eachother.getMinimumWidth(), img_eachother.getMinimumHeight());

        img_add_attent = this.getResources().getDrawable(R.mipmap.ic_add_attent);
        img_add_attent.setBounds(0, 0, img_add_attent.getMinimumWidth(), img_add_attent.getMinimumHeight());

        img_attented = this.getResources().getDrawable(R.mipmap.ic_attented);
        img_attented.setBounds(0, 0, img_attented.getMinimumWidth(), img_attented.getMinimumHeight());

        img_eachothered = this.getResources().getDrawable(R.mipmap.ic_attented_eachother);
        img_eachothered.setBounds(0,0, img_eachothered.getMinimumWidth(), img_eachothered.getMinimumHeight());

    }

    private void setClick() {
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scroll = recyclerView.computeVerticalScrollOffset();

                if (scroll / 500 <= 0.5 && scroll / 500 >= 0) {
                    float scale = (float) scroll / 500;
                    float alpha = (255 * scale);
                    titleLl.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    sStatusBar.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
                    if (userTitle.getVisibility() == View.VISIBLE) {
                        userTitle.setVisibility(View.GONE);
                        updateTitle(user);
                    }

                } else if ((scroll / 500) > 0.5) {
                    titleLl.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    sStatusBar.setBackgroundColor(Color.argb(255, 0, 0, 0));
                    if (userTitle.getVisibility() != View.VISIBLE) {
                        userTitle.setVisibility(View.VISIBLE);
                        updateTitle(user);
                    }
                }

            }
        });
        //todo  在此处判断uid 是否是用户自己的 做处理
        tvAddAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FFUtils.checkNet()) {

                    if (!SP.isLogin()) {
                        LoginOneActivity.startLogin((BaseActivity) getActivity());
                        return;
                    }

                    if (user == null) user = new SYUser();
                    String followId = user.isByFollowMe() ? "0" : "1";
                    checkIsAttion(user.getId(), followId, user);
                } else {
                    activity.showToast(activity.getString(R.string.lsq_network_connection_interruption));
                }
            }
        });

        if (headerView != null) {
            headerView.setWatchListener(new UserInfoHeaderView.WatchListener() {
                @Override
                public void addWatched() {

                    updateTitle(user);
                }

                @Override
                public void unWatched() {
                    updateTitle(user);
                }

                @Override
                public void eachWatched() {
                    updateTitle(user);
                }
            });
        }
    }

    private void getUserInfo() {
        activity.post(IUrlUtils.UserCenter.getIndexPersonalInfoV310, "", null, new FFNetWorkCallBack<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response, FFExtraParams extra) {
                if (response == null) {
                    return;
                }

                if (response.getUser() != null) {
                    headerView.setUser(response.getUser(), false);
                    updateTitle(response.getUser());

                    if (response.getUser().getDynamicNumber() > 0) {
                        headerView.shareCountLl.setVisibility(View.VISIBLE);
                        StringBuilder text = new StringBuilder();
                        text.append("TA分享的美食");
                        text.append("<font color='#787878' size=28px'>").append("(").append("</font>");
                        text.append("<font color='#F9A825'>").append(response.getUser().getDynamicNumber()).append("</font>");
                        text.append("<font color='#787878' size=28px>条</font>");
                        text.append("<font color='#787878' size=28px'>)</font>");
                        headerView.shareCount.setText(Html.fromHtml(text.toString()));
                    }
                }

                if (response.getSyFoodNotesClassifications() != null) {
                    headerView.foodTypeLl.setVisibility(View.VISIBLE);
//                    headerView.shareCount.setText(Html.fromHtml("TA分享的美食"+"(<font color='#F9A825'>" + response.getUser().getDynamicNumber() + "</font>" +
//                    "<font color='#787878' size=28px>条</font>" + ")"));
                    headerView.foodType.addItems(response.getSyFoodNotesClassifications());
                } else {
                    headerView.foodTypeLl.setVisibility(View.GONE);
                }

                if (response.getUserStat() != null) {
                    headerView.setDynamicInfo(response.getUserStat());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "targetAccount", userId, "type", "2");
    }

    private void scrollUpdateTitle(boolean hide) {
        if (hide) {
            userTitle.setVisibility(View.GONE);
        } else {
            userTitle.setVisibility(View.VISIBLE);
            if (headerView != null) {
                userTitle.setText(headerView.tvName.getText().toString());
            }
        }

    }

    private void updateTitle(SYUser user) {
        boolean isScrolled = userTitle.getVisibility() == View.VISIBLE;
        if (headerView != null && isScrolled) {
            userTitle.setText(headerView.tvName.getText().toString());
        }

        if (isScrolled) {
            ivBack.setImageResource(R.mipmap.ff_ic_back_pressed);
        } else {
            ivBack.setImageResource(R.mipmap.ff_ic_back_normal);
        }

        if (user != null && user.isByFollowMe() && user.isFollowMe()) {
            if (isScrolled) {
                tvAddAttention.setText("");
                tvAddAttention.setCompoundDrawables(img_eachothered, null, null, null);
            } else {
                tvAddAttention.setText("相互关注");
                tvAddAttention.setCompoundDrawables(img_eachother, null, null, null);
            }

        } else if (user != null && !user.isByFollowMe()) {
            if (isScrolled) {
                tvAddAttention.setText("");
                tvAddAttention.setCompoundDrawables(img_add_attent, null, null, null);
            } else {
                tvAddAttention.setText("+ 关注");
                tvAddAttention.setCompoundDrawables(null, null, null, null);
            }

        } else if (user != null && user.isByFollowMe()) {
            if (isScrolled) {
                tvAddAttention.setText("");
                tvAddAttention.setCompoundDrawables(img_attented, null, null, null);
            } else {
                tvAddAttention.setText("√ 已关注");
                tvAddAttention.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    private void getData() {
        post(IUrlUtils.UserCenter.queryMyFoodRecordsV250, FFUtils.isListEmpty(list_net) ? "" : null, null, new FFNetWorkCallBack<UserDynamicResult>() {
                    @Override
                    public void onSuccess(UserDynamicResult response, FFExtraParams extra) {
//                        response = null;
                        prl.loadmoreFinish(prl.SUCCEED);
                        if (response == null) {
                            prl.setDoPullUp(false);
                            headerView.showNoDataLayout(true);
                            return;
                        }

                        if (FFUtils.isListEmpty(response.getFeeds())) {
                            prl.setDoPullUp(false);
                            if (mAdapter.getItemCount() <= 0 && headerView != null) {
                                headerView.showNoDataLayout(true);
                            }
//                            mAdapter.setHasMore(false);
//                            mAdapter.setLoadMore(true);
                        } else {
                            if (headerView != null) {
                                headerView.showNoDataLayout(false);
                            }
                            list_net.addAll(response.getFeeds());
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(prl.FAIL);
                        return false;
                    }
                }, "userId", userId, "lastFoodRecordId", FFUtils.isListEmpty(list_net) ? 0 : list_net.get(list_net.size() - 1).getId(),
                "lastTimeStamp", FFUtils.isListEmpty(list_net) ? 0 : list_net.get(list_net.size() - 1).getTimeStamp());

    }

    private void checkIsAttion(String followId, String followState, final SYUser user) {
        if (headerView != null) {
            headerView.checkIsAttion(followId, followState, user);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                onNetStatusChanged(FFUtils.checkNet());
            }
        }
    };

    public void onNetStatusChanged(boolean isConnect) {
        if (isConnect && rvFailOne != null) {
            rvFailOne.setVisibility(View.GONE);
        } else if (rvFailOne != null) {
            rvFailOne.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (receiver != null) {
            getBaseActivity().unregisterReceiver(receiver);
        }
    }

}
