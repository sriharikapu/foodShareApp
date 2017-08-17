package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.GoodFriendWantEatAdapter;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publish.apiparams.GoodFriendWantListBean;
import com.fengnian.smallyellowo.foodie.bean.results.GoodFriendWantEatResult;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public class GoodFriendsWantEatActivity extends Activity implements  View.OnClickListener{
   private ListView lv_list;
    private TextView tv_have_num_people;
    private RelativeLayout rl_1,rl_2;

    private List<GoodFriendWantListBean> list;
    GoodFriendWantEatAdapter adapter;

    String merchantUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_good_friend_want_eat);
        merchantUid=getIntent().getStringExtra("merchantUid");
        lv_list= (ListView) findViewById(R.id.lv_list);
        rl_1= (RelativeLayout) findViewById(R.id.rl_1);
        rl_1.setOnClickListener(this);
        rl_2= (RelativeLayout) findViewById(R.id.rl_2);
        rl_2.setOnClickListener(this);

//        ViewGroup.LayoutParams Params = lv_list.getLayoutParams();
//        Params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        Params.width=ViewGroup.LayoutParams.MATCH_PARENT;
//        lv_list.setLayoutParams(Params);

        View view=View.inflate(this,R.layout.good_friends_title,null);
        tv_have_num_people= (TextView) view.findViewById(R.id.tv_have_num_people);

        list=new ArrayList<>();
        lv_list.addHeaderView(view);
        adapter=new GoodFriendWantEatAdapter(list,this);
        lv_list.setAdapter(adapter);
        initclcik();

        get_want_eat_friend_list();




    }
  private void initclcik(){
      lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              GoodFriendsWantEatActivity.this.finish();
          }
      });
  };

    private void  get_want_eat_friend_list(){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        APP.post(Constants.shareConstants().getNetHeaderAdress() + "/eat/queryCommonEatListV250.do",null,  new FFNetWorkCallBack<GoodFriendWantEatResult>() {
        APP.post(IUrlUtils.Search.queryCommonEatListV250,null,  new FFNetWorkCallBack<GoodFriendWantEatResult>() {
            @Override
            public void onSuccess(GoodFriendWantEatResult response, FFExtraParams extra) {
                if(response.getErrorCode()==0){

                    if(response.getList().size()>0){
                        list.addAll(response.getList());
                        adapter.notifyDataSetChanged();
                        tv_have_num_people.setText("有"+response.getList().size()+"位好友想吃");
                    }
                }else
                    showToas(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                showToas("网络不好");
                return false;
            }
        },"lastCreateTime","0","merchantUid",merchantUid,"pageSize","15");
    }

    private void  showToas(String msg){
        Toast toast=Toast.makeText(GoodFriendsWantEatActivity.this,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_1:

                finish();
                break;
            case R.id.rl_2:

                finish();
                break;

        }
    }
}
