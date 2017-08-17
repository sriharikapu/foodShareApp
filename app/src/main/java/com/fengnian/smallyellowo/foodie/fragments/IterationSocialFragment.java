package com.fengnian.smallyellowo.foodie.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.Adapter.Contact_Scoial__Recomd_GridAdapter;
import com.fengnian.smallyellowo.foodie.Adapter.Social_Friends_Command_GridAdapter;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFriendRecommendModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRelationTalentModel;
import com.fengnian.smallyellowo.foodie.bean.results.SYFriendRecommendResult;
import com.fengnian.smallyellowo.foodie.bean.results.SYRelationTalentResult;
import com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty;
import com.fengnian.smallyellowo.foodie.contact.MyAttionFragmentActivty;
import com.fengnian.smallyellowo.foodie.contact.MyFansActivty;
import com.fengnian.smallyellowo.foodie.contact.RecommendActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.NoSlideGridView;

import java.util.ArrayList;
import java.util.List;

//import com.fengnian.smallyellowo.foodie.contact.MyAttionFragmentActivty;


/**
 * Created by Administrator on 2017-2-20.
 * 迭代 2.70的社交 新页面
 */

public class IterationSocialFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_social_attion, rl_social_fans, rl_mor_people;
    private NoSlideGridView grid_view;//recommended_gridview,
    public static RelativeLayout no_darentuijian;
    public static RecyclerView scro_1;
    private LinearLayout goAnswerView;

    private TextView tv_remind_num, tv_other_friends, tv_1, tv_3, tv_6;

    private Contact_Scoial__Recomd_GridAdapter adapter;

    private Social_Friends_Command_GridAdapter gridAdapter;

    private List<SYRelationTalentModel> recomd_lits, templist;
    private List<SYFriendRecommendModel> command_frends_list;
    private ImageView iv_social_add_friend;

    private View view4, view_xian_6, view5;


    public IterationSocialFragment() {
        this(null);
    }


    @SuppressLint("ValidFragment")
    public IterationSocialFragment(BaseActivity context) {
        super(context);
    }

    private RelativeLayout rv_fail_one, no_intnet;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_socia_relation, container, false);
        return rootView;
    }

    @Override
    public void onFindView() {

        rv_fail_one = findView(R.id.inc_1);
        iv_social_add_friend = findView(R.id.iv_social_add_friend);
        iv_social_add_friend.setOnClickListener(this);

        no_darentuijian = findView(R.id.no_darentuijian);
        rl_social_attion = findView(R.id.rl_social_attion);
        tv_1 = findView(R.id.tv_1);
        rl_social_attion.setOnClickListener(this);
        rl_social_fans = findView(R.id.rl_social_fans);
        tv_3 = findView(R.id.tv_3);
        rl_social_fans.setOnClickListener(this);
        view5 = findView(R.id.view5);
        tv_remind_num = findView(R.id.tv_remind_num);

        goAnswerView = findView(R.id.go_html_ll);
        goAnswerView.setOnClickListener(this);

        no_intnet = findView(R.id.no_intnet);
        no_intnet.setOnClickListener(this);

        scro_1 = findView(R.id.scro_1);
        grid_view = findView(R.id.grid_view);

        rl_mor_people = findView(R.id.rl_mor_people);
        rl_mor_people.setOnClickListener(this);
        tv_other_friends = findView(R.id.tv_other_friends);

        tv_6 = findView(R.id.tv_6);
        view4 = findView(R.id.view4);
        view_xian_6 = findView(R.id.view_xian_6);
        recomd_lits = new ArrayList<>();
        templist = new ArrayList<>();
        command_frends_list = new ArrayList<>();


        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scro_1.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        scro_1.setHasFixedSize(true);


        gridAdapter = new Social_Friends_Command_GridAdapter(getBaseActivity(), command_frends_list);
        grid_view.setAdapter(gridAdapter);
        registerBroadcast();
    }

    /**
     * 根据新好友数量刷新页面
     *
     * @param newFrientNum
     */
    public void setNewFrientNum(int newFrientNum) {
        //TODO 铭泽 根据新好友数量刷新页面

//        old_message_num=newFrientNum;
        if (tv_remind_num == null) return;
        if (newFrientNum > 0) {

            tv_remind_num.setVisibility(View.VISIBLE);
            tv_remind_num.setText(newFrientNum + "");
        } else {
            tv_remind_num.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }
        switch (v.getId()) {
            case R.id.iv_social_add_friend:

                startActivity(AddFriendsActivty.class, new IntentData());
                break;
            case R.id.rl_social_attion:
//                UserInfoIntent userInfoIntent=  new UserInfoIntent();
//                userInfoIntent.setUser(SP.getUser());
//                startActivity(MyAllAttionActivity.class,userInfoIntent);
                startActivity(MyAttionFragmentActivty.class, new IntentData());
                break;
            case R.id.rl_social_fans:
                startActivity(MyFansActivty.class, new IntentData());
                break;
            case R.id.rl_mor_people:
                startActivity(RecommendActivity.class, new IntentData());
                break;

            case R.id.go_html_ll:
                WebInfo data = new WebInfo();
                data.setTitle("美食问答");
                data.setMenuVisible(false);
                data.setUrl(IUrlUtils.HtmlUrl.cateQuestionAnswer);
                startActivity(CommonWebviewUtilActivity.class, data);
                break;
        }
    }

    int flag1, flag2;

    @Override
    public void onResume() {
        super.onResume();
//        if (!isHidden()){
        if (flag1 == 1) getSocialCommand(null);
        else getSocialCommand("");
        getSocialCommandFrend(null);

//        }
        FFLogUtil.e("onResume           ", "123456789");
        if (FFUtils.checkNet()) {
            rv_fail_one.setVisibility(View.GONE);
            no_intnet.setVisibility(View.GONE);
        } else {
            no_intnet.setVisibility(View.VISIBLE);

            rv_fail_one.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 达人推荐
     */
    private void getSocialCommand(String str) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/querySocialPeopleRecommenders.do", str, extra, new FFNetWorkCallBack<SYRelationTalentResult>() {
        post(IUrlUtils.Search.querySocialPeopleRecommenders, str, extra, new FFNetWorkCallBack<SYRelationTalentResult>() {
            @Override
            public void onSuccess(SYRelationTalentResult response, FFExtraParams extra) {
                flag1 = 1;
                if (response.judge()) {
                    recomd_lits.clear();
                    recomd_lits.addAll(response.getTalent());
                    templist.clear();
                    adapter = new Contact_Scoial__Recomd_GridAdapter(getBaseActivity(), recomd_lits, templist);
                    scro_1.setAdapter(adapter);
//                    dataManager.setDataList((ArrayList<SYRelationTalentModel>) recomd_lits);
                    //在主线程刷新区域
//                    dataManager.getNextList();

                    FFLogUtil.e("返回数据的集合个数...", recomd_lits.size() + "");
                    if (recomd_lits.size() > 0 && recomd_lits.size() <= 4) {
                        for (int i = 0; i < recomd_lits.size(); i++) {
                            templist.add(recomd_lits.get(i));
                        }
                    } else if (recomd_lits.size() > 4) {
                        for (int i = 0; i < 4; i++) {
                            templist.add(recomd_lits.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (recomd_lits.size() > 0) {
                        no_darentuijian.setVisibility(View.GONE);
                    } else {
                        no_darentuijian.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    private void getSocialCommandFrend(String string) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/querySocialIndex.do", string, extra, new FFNetWorkCallBack<SYFriendRecommendResult>() {
        post(IUrlUtils.Search.querySocialIndex, string, extra, new FFNetWorkCallBack<SYFriendRecommendResult>() {
            @Override
            public void onSuccess(SYFriendRecommendResult response, FFExtraParams extra) {
                flag2 = 2;
                if (response.judge()) {
                    command_frends_list.clear();
                    command_frends_list.addAll(response.getCommon());
                    gridAdapter.notifyDataSetChanged();
                    tv_1.setText(response.getUserAttentionCount());
                    tv_3.setText(response.getUserFansCount());

                    if (!response.isHasMoreFriends()) {
                        view_xian_6.setVisibility(View.GONE);
                        rl_mor_people.setVisibility(View.GONE);
                    } else {
                        rl_mor_people.setVisibility(View.VISIBLE);
                        view_xian_6.setVisibility(View.VISIBLE);
                    }
                    if (command_frends_list.size() == 0) {
                        grid_view.setVisibility(View.GONE);
                        tv_6.setVisibility(View.GONE);
                        view4.setVisibility(View.GONE);
                        view5.setVisibility(View.GONE);
                        rl_mor_people.setVisibility(View.GONE);
                        view_xian_6.setVisibility(View.GONE);
                    } else {
                        grid_view.setVisibility(View.VISIBLE);
                        tv_6.setVisibility(View.VISIBLE);
                        view4.setVisibility(View.VISIBLE);
                        view5.setVisibility(View.VISIBLE);
                        rl_mor_people.setVisibility(View.VISIBLE);
                        view_xian_6.setVisibility(View.VISIBLE);
                    }

                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver;

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_NO_NET)) {
                    if ("type_net".equals(intent.getStringExtra("type"))) {
                        boolean isConnect = intent.getBooleanExtra("isConnect", false);
                        onNetStatusChanged(isConnect);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_NO_NET);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void onNetStatusChanged(boolean isConnect) {
        if (isConnect && rv_fail_one != null) {
            rv_fail_one.setVisibility(View.GONE);
        } else if (rv_fail_one != null) {
            rv_fail_one.setVisibility(View.VISIBLE);
        }
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }
}
