package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFriendRecommendModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

import static org.lasque.tusdk.core.TuSdkContext.getString;

/**
 * Created by Administrator on 2017-2-21.
 */

public class Social_Friends_Command_GridAdapter extends BaseAdapter {

    private FFContext mcontext;
    private List<SYFriendRecommendModel> mlist;
    public Social_Friends_Command_GridAdapter(FFContext context,List<SYFriendRecommendModel> list) {
        this.mcontext=context;
        this.mlist=list;
    }

    @Override
    public int getCount() {
        return mlist.size()>=3?3:mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewholder holder=null;
        if(convertView==null){
            holder=new viewholder();
            convertView=View.inflate((Context) mcontext, R.layout.item_iteration_social_good_friend_command,null);
            holder.iv_add_crown= (ImageView) convertView.findViewById(R.id.iv_add_crown);
            holder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_is_attion= (ImageView) convertView.findViewById(R.id.tv_is_attion);
            holder.tv_common_good_friend= (TextView) convertView.findViewById(R.id.tv_common_good_friend);
            convertView.setTag(holder);
        }else{
            holder= (viewholder) convertView.getTag();
        }
        SYFriendRecommendModel model=mlist.get(position);
        final SYUser user=model.getUser();

        FFImageLoader.loadAvatar(mcontext,user.getHeadImage().getUrl(),holder.iv_head);
        holder.tv_name.setText(user.getNickName());
        if(user.isByFollowMe()){
            holder.tv_is_attion.setImageResource(R.mipmap.yiguanzhu_img);
        }else{
            holder.tv_is_attion.setImageResource(R.mipmap.weiguanzhu_img);
        }
        if(model.getCommonFriends()>0)
         holder.tv_common_good_friend.setText(model.getCommonFriends()+"共同好友");
        else holder.tv_common_good_friend.setText("");

        holder.tv_is_attion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mcontext.showToast(getString(R.string.lsq_network_connection_interruption));
                    return;
                }
                String  followId=user.isByFollowMe()?"0":"1";
                if("1".equals(followId)){ //guanzhu
                    user.setByFollowMe(true);
                    user.setFollowMe(false);
                }else{
                    user.setByFollowMe(false);
                }
                notifyDataSetChanged();
                checkIsAttion(user.getId(),followId,position,user);
            }
        });

        IsAddCrownUtils.checkIsAddCrow(user,holder.iv_add_crown);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mcontext.showToast(getString(R.string.lsq_network_connection_interruption));
                    return;
                }
                UserInfoIntent info=new UserInfoIntent();
                info.setUser(user);
//                if()
//                ((BaseFragmentActivity)mcontext).startActivity(UserInfoActivity.class,info);
                IsAddCrownUtils.FragmentActivtyStartAct(user,info,mcontext);

            }
        });
        return convertView;
    }

      private class viewholder{

          private ImageView iv_head,tv_is_attion,iv_add_crown;
          private TextView tv_name,tv_common_good_friend;

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
        mcontext.post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if(response==null) return;
                if("success".equals(response.getResult())){
//                    String AttionState=response.getAttentionState();
//                    if("01".equals(AttionState)){//wo关注
//                        user.setByFollowMe(true);
//                    }else if("00".equals(AttionState)||"10".equals(AttionState)){//关注wo
//                        user.setByFollowMe(false);
//                    }else if("11".equals(AttionState)){ //相互关注
//                        user.setByFollowMe(true);
//                        user.setFollowMe(true);
//                    }
//                    notifyDataSetChanged();
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
