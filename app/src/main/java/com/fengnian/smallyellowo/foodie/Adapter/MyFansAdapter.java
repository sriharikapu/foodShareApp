package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.DialogInterface;
import android.view.View;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYMyFansModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.contact.MyFansActivty;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.List;

/**
 *  我的粉丝
 */

public class MyFansAdapter extends FFBaseAdapter {



    public MyFansAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
        super(viewHolderClass, layoutId, dataList, context);
    }

    @Override
    public void setViewData(Object viewHolder, final int position, Object model) {
        if (viewHolder != null && viewHolder instanceof MyFansActivty.viewHolder) {
            MyFansActivty.viewHolder  holder=(MyFansActivty.viewHolder)viewHolder;
            if(model!=null&&model instanceof SYMyFansModel){
                final SYMyFansModel fans = (SYMyFansModel) model;

                IsAddCrownUtils.checkIsAddCrow(fans.getUser(),holder.iv_add_crown);
                FFImageLoader.loadAvatar(mcontext.context(), fans.getUser().getHeadImage().getUrl(), holder.iv_header);

                holder.tv_name.setText(fans.getUser().getNickName());
                holder.tv_sign.setText(fans.getUser().getPersonalDeclaration());
                holder.tv_date.setText(fans.getAttentionTimeStr());
                if("1".equals( fans.getStatus())){
                    holder.rl_1.setBackgroundResource(R.color.myfans_now_attion);
                }else{
                    holder.rl_1.setBackgroundResource(R.color.white_bg);
                }
                final boolean is_by_follow_me = fans.getUser().isByFollowMe();

                final  boolean  is_follow_me=fans.getUser().isFollowMe();
                if ( is_by_follow_me&&is_follow_me) {

                    holder.iv_is_attion.setImageResource(R.mipmap.my_eacher_attion);
                } else if(is_by_follow_me&&!is_follow_me){
                    holder.iv_is_attion.setImageResource(R.mipmap.my_yi_ation_img);
                }
                else  {
                    holder.iv_is_attion.setImageResource(R.mipmap.myfan_need_attion);
                }
                if(position==(getDataList().size()-1)){
                    holder.view_bottom_xian.setVisibility(View.GONE);
                }else{
                    holder.view_bottom_xian.setVisibility(View.VISIBLE);
                }
              final String   followState=is_by_follow_me?"0":"1";
              holder.iv_is_attion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg="";
                    if(is_by_follow_me){
                        msg="确定要取消关注该用户!";
                    }else{
                        msg="确定要关注该用户!";
                    }

                    if(is_by_follow_me){
                    new EnsureDialog.Builder((BaseActivity)mcontext)
//                        .setTitle("系统提示")//设置对话框标题

                            .setMessage(msg)//设置显示的内容

                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    checkIsAttion(fans.getUser().getId(),followState,position,fans.getUser());
                                }

                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//响应事件

                        }

                    }).show();//在按键响应事件中显示此对话框
                   }else{
                        checkIsAttion(fans.getUser().getId(),followState,position,fans.getUser());
                    }
                }
            });
        }
        }
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
                    String AttionState=response.getAttentionState();
                    if("01".equals(AttionState)){//wo关注
                        user.setByFollowMe(true);
                    }else if("00".equals(AttionState)||"10".equals(AttionState)){//   取消关注  关注wo
                        user.setByFollowMe(false);
                    }else if("11".equals(AttionState)){ //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                    }

                     refresh(user);
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
    void      refresh(SYUser us){

        for(int i=0;i<getDataList().size();i++){

            SYMyFansModel mod=(SYMyFansModel)(getDataList().get(i));
             SYUser user=mod.getUser();
          if(us.getId().equals(user.getId())){
                 user.setByFollowMe(us.isByFollowMe());
                 user.setFollowMe(us.isFollowMe());
            }
        }
    }
}
