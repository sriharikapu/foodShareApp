package com.fengnian.smallyellowo.foodie.utils;

import android.view.View;

import com.fan.framework.base.FFContext;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;

/**
 * Created by Administrator on 2017-3-8.
 */

public class IsAddCrownUtils {

    public  static  boolean   checkIsAddCrow(SYUser user, View v){
        if (user == null){
            v.setVisibility(View.GONE);
            return false;
        }else if (v == null){
            return false;
        }

        if(user.getUserType()==1){//
             v.setVisibility(View.VISIBLE);
            return true;
        }else{
            v.setVisibility(View.GONE);
            return false;
        }
    }

    public  static  void     FragmentActivtyStartAct(SYUser user, UserInfoIntent info, FFContext mcontext){
        if(user.getUserType()==0){
            ((BaseActivity)mcontext).startActivity(UserInfoActivity.class,info);
        }else{
            ((BaseActivity)mcontext).startActivity(ClubUserInfoActivity.class,info);
        }
    }

    public  static  void     ActivtyStartAct(SYUser user, UserInfoIntent info, FFContext mcontext){
        if(user.getUserType()==0){
            ((BaseActivity)mcontext).startActivity(UserInfoActivity.class,info);
        }else{
            ((BaseActivity)mcontext).startActivity(ClubUserInfoActivity.class,info);
        }
    }

    public  static  void     ActivtyStartActTwo(SYUser user, UserInfoIntent info, FFContext mcontext){
        if(user.getUserType()==0){
            ((BaseActivity)mcontext).startActivity(UserInfoActivity.class,info);
        }else{
            ((BaseActivity)mcontext).startActivity(ClubUserInfoActivity.class,info);
        }
    }
}
