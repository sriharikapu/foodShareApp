package com.fengnian.smallyellowo.foodie.personal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.Adapter.FoodListDetailAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.FoodListBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.results.InviteFoodListDetailResult;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FoodListIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-6.
 */

public class FoodListDetailActivty extends BaseActivity<FoodListIntent> {

    private ListView lv_listview;
    private  List<SYFeed> list;
    private FoodListDetailAdapter adapter;
    private PullToRefreshLayout prl;
    private FoodListBean  foodbean;
    String lastFoodNoteId="0";
    String pageSize="15";
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodbean=getIntentData().getFoodbean();
        if(foodbean==null) foodbean=new FoodListBean();
        setContentView(R.layout.activty_food_list_detail);
        setTitle(foodbean.getType());
        lv_listview=findView(R.id.lv_listview);
        list=new ArrayList<>();
        adapter =new FoodListDetailAdapter(viewholder.class,R.layout.item_food_list_detail,list,this);
        lv_listview.setAdapter(adapter);

        type=getIntentData().getType();

        initclick();
        initrefresh_view();

    }
    private void  initclick(){
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                DynamicDetailIntent intent = new DynamicDetailIntent(list.get(i), true, false);
                startActivity(DynamicDetailActivity.class, intent);
            }
        });
    }

    private void  initrefresh_view(){


        prl = PullToRefreshLayout.supportPull(lv_listview, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                lastFoodNoteId="0";
                getFoodListDetail(lastFoodNoteId,pageSize,type);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(list.size()-1>0)
                lastFoodNoteId=list.get(list.size()-1).getFoodNoteId();
                getFoodListDetail(lastFoodNoteId,pageSize,type);
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




    private void  getFoodListDetail(final String lastFoodNoteId, String pageSize, String type){

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/queryFoodNotesListByTypeV250.do", "", extra, new FFNetWorkCallBack<InviteFoodListDetailResult>() {
        post(IUrlUtils.Search.queryFoodNotesListByTypeV250, "", extra, new FFNetWorkCallBack<InviteFoodListDetailResult>() {
            @Override
            public void onSuccess(InviteFoodListDetailResult response, FFExtraParams extra) {
                if(response.getErrorCode()==0){

                    if("0".equals(lastFoodNoteId))
                        list.clear();
                    list.addAll(response.getFeed());


                    prl.refreshFinish(prl.SUCCEED);
                    prl.loadmoreFinish(prl.SUCCEED);
                    if (response.getFeed().size() < 15) {
                        prl.setDoPullUp(false);
                    } else {
                        prl.setDoPullUp(true);

                    }
                    adapter.notifyDataSetChanged();
                }else   showToast(response.getErrorMessage());
            }
            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"lastFoodNoteId",lastFoodNoteId,"pageSize",pageSize,"type",type,"typeName",foodbean.getType(),"userId", SP.getUser().getId());
    }

  public  static class  viewholder{
      public ImageView  iv_img;
      public TextView   tv_count;
      public TextView   tv_time;
      public TextView   tv_business_name;
      public TextView   tv_date;
  }

}
