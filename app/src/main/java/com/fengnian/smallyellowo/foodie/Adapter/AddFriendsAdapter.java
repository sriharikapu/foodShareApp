package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.tv_know;

/**
 * 添加朋友.
 */

public class AddFriendsAdapter extends BaseAdapter {

    private FFNetWork mNet;
    private FFContext mcontext;
    private List<AddFriends> addtolist;
    private List<SYUser> needtolist;
     public  AddFriendsAdapter(FFContext context, List<AddFriends> addFriendsList, List<SYUser> needToFriendseslist){
         this.mNet= new FFNetWork(mcontext);
         this.mcontext=context;
         this.addtolist=addFriendsList;
         this.needtolist=needToFriendseslist;
     }
    @Override
    public int getCount() {
        return addtolist.size()+needtolist.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        viewHolder  holder=null;
        if(view==null){
            holder=new viewHolder();
            view=View.inflate((Context)mcontext,R.layout.item_add_friends,null);
            holder.tv_know= (TextView) view.findViewById(tv_know);
            holder.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holder.iv_header= (ImageView) view.findViewById(R.id.iv_header);
            holder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            holder.tv_contanct_name= (TextView) view.findViewById(R.id.tv_contanct_name);
            holder.tv_is_attion= (TextView) view.findViewById(R.id.tv_is_attion);

            holder.view_1=view.findViewById(R.id.view_1);
            view.setTag(holder);
        }else{
            holder= (viewHolder) view.getTag();
        }
              if(pos< addtolist.size()&&addtolist.size()>0){
                  if(pos==0){
                      holder.tv_know.setVisibility(View.VISIBLE);
                      holder.tv_know.setText("认识的人");
                  }else{
                      holder.tv_know.setVisibility(View.GONE);
                  }
                  if(pos==addtolist.size()-1){
                      holder.view_1.setVisibility(View.GONE);
                  }else{
                      holder.view_1.setVisibility(View.VISIBLE);
                  }
                  AddFriends  add= addtolist.get(pos);
                  IsAddCrownUtils.checkIsAddCrow(add.getUser(),holder.iv_add_crown);
                  FFImageLoader.loadAvatar(mcontext.context(), add.getUser().getHeadImage().getUrl(), holder.iv_header);
                holder.tv_name.setText(add.getUser().getNickName());
                holder.tv_contanct_name.setText("通讯录联系人:"+add.getName());
                boolean  is_follow_me=add.getUser().isByFollowMe();
                holder.tv_is_attion.setText(is_follow_me?"已关注":"关注");
                holder.tv_is_attion.setBackgroundResource(is_follow_me? R.drawable.is_attion_bg:R.drawable.no_attion_bg);
              }

        if(pos>=addtolist.size()&&needtolist.size()!=0){
             if(pos==addtolist.size()){
                 holder.tv_know.setText("成功邀请的好友");
                 holder.tv_know.setVisibility(View.VISIBLE);
             }else{
                 holder.tv_know.setVisibility(View.GONE);
             }
            holder.view_1.setVisibility(View.VISIBLE);
            int new_pos=pos-(addtolist.size());
            SYUser user=needtolist.get(new_pos);
            IsAddCrownUtils.checkIsAddCrow(user,holder.iv_add_crown);
            FFImageLoader.loadAvatar(mcontext.context(), user.getHeadImage().getUrl(), holder.iv_header);
            holder.tv_name.setText(user.getNickName());
            if(!"null".equals(user.getRegistTime())){
                holder.tv_contanct_name.setText(user.getRegistTime()+"加入小黄圈");
            }else{
                holder.tv_contanct_name.setText("");
            }
            boolean  is_follow_me=user.isByFollowMe();
            holder.tv_is_attion.setText(is_follow_me?"已关注":"关注");
            holder.tv_is_attion.setBackgroundResource(is_follow_me? R.drawable.is_attion_bg:R.drawable.no_attion_bg);
        }

        view.findViewById(R.id.tv_is_attion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos< addtolist.size()){
                    AddFriends  add=  addtolist.get(pos);
                    String  userid=add.getUser().getId();
                    String  followId=add.getUser().isByFollowMe()?"0":"1";
                    checkIsAttion(userid,followId,pos,add.getUser());
                }

                if(pos>=addtolist.size()&&needtolist.size()!=0){
                    int new_pos=pos-(addtolist.size());
                    SYUser us=needtolist.get(new_pos);
                    String userid=us.getId();
                    String followid=us.isByFollowMe()?"0":"1";
                    checkIsAttion(userid,followid,pos,us);
                }


                }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击进入到 对外的个人信息界面
                SYUser  user=null;
                if(pos< addtolist.size()){
                    user=addtolist.get(pos).getUser();
                 }
                else if(pos>=addtolist.size()&&needtolist.size()!=0){
                    int new_pos=pos-(addtolist.size());
                    user=needtolist.get(new_pos);

                }
                if(user==null){
                    mcontext.showToast("~~~~~");
                    return;
                }
                    UserInfoIntent intent = new UserInfoIntent();
                    intent.setUser(user);
//                    ((AddFriendsActivty)mcontext).startActivity(UserInfoActivity.class, intent);
                     IsAddCrownUtils.ActivtyStartAct(user,intent,mcontext);
                }
        });
        return view;
    }
    public   class  viewHolder{
        public ImageView iv_header,iv_add_crown;
        public TextView tv_name,tv_know;
        public TextView tv_contanct_name ;
        public TextView tv_is_attion ;
        private View view_1;

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
                    }else if("00".equals(AttionState)||"10".equals(AttionState)){//关注wo
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
