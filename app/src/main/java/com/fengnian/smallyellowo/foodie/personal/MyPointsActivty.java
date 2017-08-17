package com.fengnian.smallyellowo.foodie.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.PointeAdapter;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.ScoreDayBean;
import com.fengnian.smallyellowo.foodie.bean.results.ScoreReult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-8.
 */

public class MyPointsActivty extends BaseActivity<IntentData> implements View.OnClickListener{

   private TextView tv_point_rule,tv_points,tv_member_benefits;
   private ListView lv_listview;
   private List<ScoreDayBean>  list;
    private  PointeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);
        View view=View.inflate(this,R.layout.new_point,null);
        tv_point_rule=(TextView) view.findViewById(R.id.tv_point_rule);
        tv_points=(TextView) view.findViewById(R.id.tv_points);
        tv_member_benefits=(TextView) view.findViewById(R.id.tv_member_benefits);

        tv_point_rule.setOnClickListener(this);
        tv_member_benefits.setOnClickListener(this);

        lv_listview=findView(R.id.lv_listview);
        lv_listview.addHeaderView(view);
        list=new ArrayList<>();
        adapter =new PointeAdapter(list,this);
        lv_listview.setAdapter(adapter);

         getPoints();


        }

    private void  getPoints(){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/score/queryScoreList.do", "", extra, new FFNetWorkCallBack<ScoreReult>() {
        post(IUrlUtils.Search.queryScoreList, "", extra, new FFNetWorkCallBack<ScoreReult>() {
            @Override
            public void onSuccess(ScoreReult response, FFExtraParams extra) {
                if(response.getErrorCode()==0){

                    tv_points.setText(response.getTotalPoints());
                    list.addAll(response.getScoreDays());
                    adapter.notifyDataSetChanged();
                }else   showToast("出现错误");
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"day","0","pageSize","20");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_point_rule:

                WebInfo info=new WebInfo();
                info.setUrl(" http://static.tinydonuts.cn/points240.html");//http://static.tinydonuts.cn/points.html

                info.setTitle("积分规则");
               startActivity(CommonWebviewUtilActivity.class,info);
                break;
            case R.id.tv_member_benefits:
                WebInfo info1=new WebInfo();
                info1.setUrl("http://static.tinydonuts.cn/boon.html");
                info1.setTitle("会员福利");
                startActivity(CommonWebviewUtilActivity.class,info1);
                break;
        }

    }

}
