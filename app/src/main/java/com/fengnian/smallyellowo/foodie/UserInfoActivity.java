package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ClubUserinfoResult;
import com.fengnian.smallyellowo.foodie.fragments.ClubUserInfoFragment;
import com.fengnian.smallyellowo.foodie.fragments.NormalUserInfoFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.usercenter.fragment.UserFragment;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

public class UserInfoActivity extends BaseActivity<UserInfoIntent> {
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        setTitle("");
        setNotitle(true);
        SYUser user = getIntentData().getUser();
        if (user != null) {
            uid = user.getId();
        } else {
            uid = getIntentData().getId();
        }
        getInfo();
    }

    public void getInfo() {
        post(IUrlUtils.UserCenter.getIndexPersonalInfoV250, "", new FFExtraParams().setInitPage(true), new FFNetWorkCallBack<ClubUserinfoResult>() {
            @Override
            public void onSuccess(ClubUserinfoResult response, FFExtraParams extra) {
                if (response.getClubUser() != null) {
                    ClubUserInfoFragment fragment = new ClubUserInfoFragment();
                    fragment.setUser(response);
                    getSupportFragmentManager().beginTransaction().add(getContainer().getId(), fragment).commit();
                } else if (response.getUser() != null) {
                    UserFragment fragment = new UserFragment();
                    Bundle b = new Bundle();
                    b.putParcelable("user", response.getUser());
                    b.putString("user_id", uid);
                    fragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().add(getContainer().getId(), fragment).commit();

                    /*NormalUserInfoFragment fragment = new NormalUserInfoFragment();
                    fragment.setUser(response.getUser(), false);
                    getSupportFragmentManager().beginTransaction().add(getContainer().getId(), fragment).commit();*/
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "targetAccount", uid, "type", 2);
    }
}
