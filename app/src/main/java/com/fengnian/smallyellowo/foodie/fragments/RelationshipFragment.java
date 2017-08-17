package com.fengnian.smallyellowo.foodie.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RelationshipListAdapter;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.RelationShipSortUser;
import com.fengnian.smallyellowo.foodie.bean.publics.RelationShipSorts;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.RelationShipListResult;
import com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty;
import com.fengnian.smallyellowo.foodie.contact.RelationSearchActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.SideBar;

import java.util.ArrayList;

/**
 * Created by lanbiao on 16/8/2.
 */
@SuppressLint("ValidFragment")
public class RelationshipFragment extends BaseFragment implements View.OnClickListener {
    private ListView listView;
//    private FastSectionBar fastSectionBar;
    private SideBar sideBar;
    private RelationshipListAdapter relationshipAdapter;
    private RelativeLayout rl_no_contact, contact_content;
    private ImageView add_new_relation_id;
    public static int is_xianshi_soucuo = 1;

    public  static  int  message_num=0;
//    public  static  int  old_message_num=0;

    private ScrollView sc_view;

    private TextView tv_center;


    public RelationshipFragment() {
        super(null);
    }

    public RelationshipFragment(FFBaseActivity context) {
        super(context);
    }


    Handler mandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 100086:
                    initData(null);
                    break;
            }
        }
    };
    /**
     * 根据新好友数量刷新页面
     *
     * @param newFrientNum
     */
    public void setNewFrientNum(int newFrientNum) {
        //TODO 铭泽 根据新好友数量刷新页面
//             old_message_num=newFrientNum;
             if(relationshipAdapter!=null){
                 message_num=newFrientNum;
                 relationshipAdapter.notifyDataSetChanged();
         }

    }
    @Override
    public void onFindView() {

        tv_center= (TextView) findViewById(R.id.tv_center);
        listView = (ListView) findViewById(R.id.contact_list);
//        fastSectionBar = (FastSectionBar) findViewById(R.id.fast_scroller);
        sideBar = (SideBar) findViewById(R.id.fast_scroller);


        contact_content = (RelativeLayout) findViewById(R.id.contact_content);
        rl_no_contact = (RelativeLayout) findViewById(R.id.rl_no_contact);
        add_new_relation_id = (ImageView) findViewById(R.id.add_new_relation_id);
        add_new_relation_id.setOnClickListener(this);
        userList = new ArrayList<>();
        relationshipAdapter = new RelationshipListAdapter(RelationItemViewHolder.class, R.layout.relationship_list_item, userList, getBaseActivity(),mandle);
        listView.setAdapter(relationshipAdapter);

//        setNewFrientNum(old_message_num);

//        fastSectionBar.setListView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(RelationSearchActivity.class, new IntentData());
                }
//                if (position == 1)
//                    startActivity(MyFansActivty.class, new IntentData());

                if (position >= 1) {

                    UserInfoIntent intent = new UserInfoIntent();
                    SYUser us=userList.get(position - 1).getUser();
                    intent.setUser(us);

                    if(us.getUserType()==1){
                        startActivity(ClubUserInfoActivity.class,intent);
                    }
                     else startActivity(UserInfoActivity.class, intent);
                }

            }
        });



        sideBar.setTextView(tv_center);
        // 设置字母导航触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // TODO Auto-generated method stub
                // 该字母首次出现的位置
                int position = relationshipAdapter.getPositionForSelection(s.charAt(0));

                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });

    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.relationship_fragment, container, false);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userList.size() == 0) {
            initData("");
        } else {
            initData(null);
        }

    }

    ArrayList<RelationShipSortUser> userList;

    public    void initData(String isshow) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/getRelationShipList.do", isshow, extra, new FFNetWorkCallBack<RelationShipListResult>() {
        post(IUrlUtils.Search.getRelationShipList, isshow, extra, new FFNetWorkCallBack<RelationShipListResult>() {
            @Override
            public void onSuccess(RelationShipListResult response, FFExtraParams extra) {
                if (response.judge()) {
                     userList.clear();
                    ArrayList<RelationShipSorts> list = response.getRelationships();

                    for (Integer xIndex = 0; xIndex < list.size(); xIndex++) {
                        RelationShipSorts relation = list.get(xIndex);
                        ArrayList<SYUser> users = relation.getUsers();

                        for (Integer yIndex = 0; yIndex < users.size(); yIndex++) {
                            RelationShipSortUser relationUser = new RelationShipSortUser();
                            relationUser.setKey(relation.getKey());
                            relationUser.setUser(users.get(yIndex));
                            userList.add(relationUser);
                        }
                    }
//                    Collections.sort(userList);
                    relationshipAdapter.notifyDataSetChanged();
//                    setAdapter();
                    if (list != null && list.size() == 0 && userList != null && userList.size() == 0) {
//                        fastSectionBar.setVisibility(View.GONE);
                        sideBar.setVisibility(View.GONE);
                        rl_no_contact.setVisibility(View.VISIBLE);
                    } else {
                        rl_no_contact.setVisibility(View.GONE);
//                        fastSectionBar.setVisibility(View.VISIBLE);
                        sideBar.setVisibility(View.VISIBLE);
                    }
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_relation_id:
                startActivity(AddFriendsActivty.class, new IntentData());
                break;
        }

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

    public static class SearchItemViewHolder {
        public SearchView topId;
    }

    public static class MyFansItemViewHolder {
        public TextView myFansView;
        public   TextView tv_num;


    }



}
