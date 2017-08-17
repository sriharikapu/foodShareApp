package com.fengnian.smallyellowo.foodie.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.RecommendAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.RecommendPeople2;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecommendActivity extends BaseActivity<IntentData> {


    @Bind(R.id.recommend_list)
    ListView recommendList;
    @Bind(R.id.activity_recommend)
    RelativeLayout activityRecommend;
    //@Bind(R.id.ll_no_recommend)
   // LinearLayout llNoRecommend;
    public List<RecommendPeople2.RecommendUser> responseList;
    @Bind(R.id.iv_no_result)
    ImageView ivNoResult;
    private List<RecommendPeople2.RecommendUser> responseList2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        ivNoResult.setVisibility(View.INVISIBLE);
        setTitle("好友推荐");
        getAddRecommendList();
    }


    public void getAddRecommendList() {

//        post(Constants.shareConstants().getNetHeaderAdress() + "/recommend/friendUserList.do", "", null, new FFNetWorkCallBack<RecommendPeople2>() {
        post(IUrlUtils.Search.friendUserList, "", null, new FFNetWorkCallBack<RecommendPeople2>() {

            @Override
            public void onSuccess(RecommendPeople2 response, FFExtraParams extra) {
                if (response.getList().size() == 0) {
                    //llNoRecommend.setVisibility(View.VISIBLE);
                    ivNoResult.setVisibility(View.VISIBLE);
                } else {
                    ivNoResult.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "asaj", Toast.LENGTH_SHORT).show();
                    responseList = response.getList();
                    List<RecommendPeople2.RecommendUser> responseListno = new ArrayList<RecommendPeople2.RecommendUser>();
                    List<RecommendPeople2.RecommendUser> responseListyes = new ArrayList<RecommendPeople2.RecommendUser>();
                    responseList2 = new ArrayList<RecommendPeople2.RecommendUser>();

                    for (int j = 0; j < responseList.size(); j++) {
                        int readStatu = responseList.get(j).getReadStatu();
                        if (readStatu == 0) {
                            responseListno.add(responseList.get(j));
                        } else {
                            responseListyes.add(responseList.get(j));
                        }

                    }
                    responseList2.addAll(responseListno);
                    responseList2.addAll(responseListyes);
                    recommendList.setAdapter(new RecommendAdapter(responseList2, RecommendActivity.this));
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "startFlag", "0", "startUserId", "");


        recommendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    UserInfoIntent intent = new UserInfoIntent();
                    intent.setUser(responseList2.get(position).getUser());

//                    startActivity(UserInfoActivity.class, intent);
                IsAddCrownUtils.ActivtyStartAct(responseList2.get(position).getUser(),intent,RecommendActivity.this);
                }


        });
    }


}
