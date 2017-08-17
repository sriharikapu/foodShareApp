package com.fengnian.smallyellowo.foodie.personal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.Adapter.AllAttionAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.MyAllAttionResult;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-11.
 */

public class FansActivty extends BaseActivity<UserInfoIntent> {
    private ListView listView;
    private List<SYUser> list;
    private AllAttionAdapter adapter;
    SYUser user;
    String lastid="";
    private PullToRefreshLayout prl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allattion);
        listView = findView(R.id.lv_listview);
         user= getIntentData().getUser();
        list = new ArrayList<>();
        adapter = new AllAttionAdapter(MyAllAttionActivity.RelationItemViewHolder.class, R.layout.relationship_list_item, list, this);
        listView.setAdapter(adapter);


        initrefresh_view();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                UserInfoIntent userinfo = new UserInfoIntent();
//                userinfo.setUser(list.get(i));
////                startActivity(UserInfoActivity.class, userinfo);
//                IsAddCrownUtils.ActivtyStartAct(list.get(i),userinfo,FansActivty.this);
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfoIntent userinfo = new UserInfoIntent();
                userinfo.setUser(list.get(i));
//                startActivity(UserInfoActivity.class, userinfo);
                IsAddCrownUtils.ActivtyStartAct(list.get(i),userinfo,FansActivty.this);
            }
        });
    }


    private void  initrefresh_view(){

        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //shang la

                getMyAlltion(user.getId(),"0");
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getMyAlltion(user.getId(), lastid);
            }
        });
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 500);

        prl.setDoPullUp(true);
        prl.setDoPullDown(true);
    }

    private void getMyAlltion(String targetaccout,final  String lastId) {


        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/getIndexUserList.do", "", extra, new FFNetWorkCallBack<MyAllAttionResult>() {
        post(IUrlUtils.Search.getIndexUserList, "", extra, new FFNetWorkCallBack<MyAllAttionResult>() {
            @Override
            public void onSuccess(MyAllAttionResult response, FFExtraParams extra) {
                prl.refreshFinish(prl.SUCCEED);
                prl.loadmoreFinish(prl.SUCCEED);
                if(lastId.equals("0")){
                    list.clear();
                }
                if (response.getErrorCode() == 0) {
                    list.addAll(response.getUsers());
                    adapter.notifyDataSetChanged();
                    lastid=response.getLastId();
                    if (response.getUsers().size() < 15) {
                        prl.setDoPullUp(false);
                    } else {
                        prl.setDoPullUp(true);

                    }
                } else showToast(response.getErrorMessage());
            }
            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.refreshFinish(prl.FAIL);
                prl.loadmoreFinish(prl.FAIL);
                return false;
            }
        }, "isAttentionQuery", false, "targetAccount", targetaccout, "lastId", lastId, "count", "15");


    }

    public static class RelationItemViewHolder {
        public TextView sectionNameView;
        public RelativeLayout userView;
        public ImageView iv_header,iv_add_crown;
        public TextView tv_name;
        public TextView tv_content;
        public RelativeLayout rl_1;
        public ImageView iv_is_attion;
        public TextView tv_is_attion;

    }
}
