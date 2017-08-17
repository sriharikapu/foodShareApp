package com.fengnian.smallyellowo.foodie.Adapter;

import android.view.View;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.AddFriends;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.contact.ContactActivty;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

/**
 * 通讯录
 */

public class ContactAdapter extends FFBaseAdapter {

  private  FFNetWork  mNet;
    public ContactAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
        super(viewHolderClass, layoutId, dataList, context);
        mNet= new FFNetWork(mcontext);
    }
    public ContactAdapter(Class viewHolderClass, int layoutId, FFContext context ) {
        super(viewHolderClass, layoutId, context);
        mNet= new FFNetWork(mcontext);
    }

    @Override
    public void setViewData(Object viewHolder, int position, Object model) {
        if (viewHolder != null && viewHolder instanceof ContactActivty.viewHolder) {
            ContactActivty.viewHolder  holder=(ContactActivty.viewHolder)viewHolder;

            if(model!=null&&model instanceof AddFriends){
                AddFriends addFriends = (AddFriends) model;
                IsAddCrownUtils.checkIsAddCrow(addFriends.getUser(),holder.iv_add_crown);
                FFImageLoader.loadAvatar(mcontext.context(), addFriends.getUser().getHeadImage().getUrl(), holder.iv_header);
                holder.tv_name.setText(addFriends.getUser().getNickName());
                holder.tv_contanct_name.setText("通讯录联系人:"+addFriends.getName());
                boolean  is_follow_me=addFriends.getUser().isByFollowMe();
                holder.tv_is_attion.setText(is_follow_me?"已关注":"关注");
                holder.tv_is_attion.setBackgroundResource(is_follow_me? R.drawable.is_attion_bg:R.drawable.no_attion_bg);
                holder.tv_know.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void click(View convertView, final Object modle, final int positon ) {
        super.click(convertView,modle,positon);
        //点击关注按钮的事件
        convertView.findViewById(R.id.tv_is_attion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modle!=null&&modle instanceof AddFriends){
                    AddFriends addFriends = (AddFriends) modle;
                    String  userid=addFriends.getUser().getId();
                    String  followId=addFriends.getUser().isByFollowMe()?"0":"1";
                    checkIsAttion(userid,followId,positon,addFriends.getUser());
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击进入到 对外的个人信息界面
                if(modle!=null&&modle instanceof AddFriends){
                AddFriends addFriends = (AddFriends) modle;

                    UserInfoIntent intent = new UserInfoIntent();
                    intent.setUser(addFriends.getUser());
//                    ((ContactActivty)mcontext).startActivity(UserInfoActivity.class, intent);
                    IsAddCrownUtils.ActivtyStartAct(addFriends.getUser(),intent,mcontext);
                }
            }
        });

    }

    /**
     *
     * @param followId (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private   void checkIsAttion(String followId,String followState ,int postion,final SYUser user){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
        mNet.post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if(response==null) return;
                if("success".equals(response.getResult())){
                    String AttionState=response.getAttentionState();
                    if("01".equals(AttionState)){//wo关注
                        user.setByFollowMe(true);
                    }else if("00".equals(AttionState)||"10".equals(AttionState)){//00取消关注   10关注wo
                        user.setByFollowMe(false);
                    }else if("11".equals(AttionState)){ //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                    }
                   notifyDataSetChanged();
                }else  {
                  mcontext.context().showToast(response.getReturnmessage());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"followId",followId,"followState",followState);
    }
}
