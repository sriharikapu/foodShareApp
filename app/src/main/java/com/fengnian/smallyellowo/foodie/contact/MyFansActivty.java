package com.fengnian.smallyellowo.foodie.contact;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.UI.PullRefreshLoading.PullToRefreshListView;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.Adapter.MyFansAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYMyFansModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.MyfansAttionResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的粉丝
 */
public class MyFansActivty extends BaseActivity<IntentData> implements View.OnClickListener {

    private List<SYMyFansModel> mlist;
    private MyFansAdapter adapter;
    private ListView lv_listview;
    private ImageView no_people;
    private PullToRefreshListView mPullListView;

    private int  index=2;//頁碼

    private  String attentionRecordI="0";
    private PullToRefreshLayout prl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfans);
        lv_listview=findView(R.id.lv_listview);
        no_people= findView(R.id.no_people);
        mlist = new ArrayList<>();
        adapter = new MyFansAdapter(viewHolder.class, R.layout.item_myfans, mlist, this);
        lv_listview.setAdapter(adapter);
        initOnclick();
        addMenu("添加朋友", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddFriendsActivty.class,new IntentData() );
            }
        });


        initrefresh_view();
    }


  private void initOnclick(){

      lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              SYUser us=mlist.get(i).getUser();
              UserInfoIntent intent = new UserInfoIntent();
              intent.setUser(us);
              IsAddCrownUtils.ActivtyStartAct(us,intent,MyFansActivty.this);
          }
      });
  }

    private void  initrefresh_view(){

        prl = PullToRefreshLayout.supportPull(lv_listview, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //shang la
                isrefresh=true;
                index=2;
                String  sign="1";
                initdata(index+"",sign,"");
//                refresh(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isrefresh=false;
                String  sign="2";
                initdata(index+"",sign,attentionRecordI);
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



  private   boolean isrefresh;
    private void initdata(String   page, String sign, final String attentionRecordId) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() +"/relation/queryAttentionRecordListV250.do",null, extra, new FFNetWorkCallBack<MyfansAttionResult>() {
        post(IUrlUtils.Search.queryAttentionRecordListV250,null, extra, new FFNetWorkCallBack<MyfansAttionResult>() {
            @Override
            public void onSuccess(MyfansAttionResult response, FFExtraParams extra) {
                if (response.judge()) {
                    PushManager.onNewFrientClick();
                    if("0".equals(attentionRecordId)){
                        mlist.clear();
                    }
                    if(response.getUser().size()==0)
                    {
                        no_people.setVisibility(View.VISIBLE);
                        prl.refreshFinish(prl.SUCCEED);
                        prl.loadmoreFinish(prl.SUCCEED);
                        if (response.getUser().size() < 10) {
                            prl.setDoPullUp(false);
                        } else {
                            prl.setDoPullUp(true);

                        }
                    }
                    else {
                        index++;
                        no_people.setVisibility(View.GONE);
                        if(isrefresh){
                            mlist.clear();
                        }
                        mlist.addAll(response.getUser());
                        attentionRecordI=mlist.get(mlist.size()-1).getAttentionID();
                        prl.refreshFinish(prl.SUCCEED);
                        prl.loadmoreFinish(prl.SUCCEED);
                        if (response.getUser().size() < 10) {
                            prl.setDoPullUp(false);
                        } else {
                            prl.setDoPullUp(true);

                        }
                        adapter.notifyDataSetChanged();
                    }

                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.refreshFinish(prl.FAIL);
                prl.loadmoreFinish(prl.FAIL);
                if(mlist.size()==0)
                no_people.setVisibility(View.VISIBLE);
                return false;
            }
        }, "attentionRecordId", attentionRecordId,"pageSize","10");//
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_back:
//                onBackPressed();
//                break;
//            case R.id.tv_right_title:
//                //TODO 进入到我的朋友页面
//                break;
//
        }
    }

    public static class viewHolder {
        public RelativeLayout rl_1;
        public ImageView iv_header,iv_is_attion,iv_add_crown;

        public TextView tv_name;
        public TextView tv_sign;
        public TextView tv_date;

        public View  view_bottom_xian;
    }
}
