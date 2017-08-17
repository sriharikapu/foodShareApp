package com.fengnian.smallyellowo.foodie.personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import static com.fan.framework.xtaskmanager.xtask.XTask.XTaskExecutStateExecing;

/**
 * Created by chenglin on 2017-3-26.
 */

public class MyFoodListFragHelper {
    MyFoodListFragment mFragment;

    public MyFoodListFragHelper(MyFoodListFragment frag) {
        mFragment = frag;
    }

    public void getPopWindow(final DeliciousFoodModel.FoodListModel foodModel) {
        EnsureDialog.Builder dialog = new EnsureDialog.Builder(mFragment.getActivity());
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(foodModel);

            }
        }).create().show();
        dialog.setCancelable(true);
    }

    private void delete(DeliciousFoodModel.FoodListModel foodModel) {
        if (foodModel.type == DeliciousFoodModel.MODEL_NATIVE) { //本地数据
            SYFeed syFeed = foodModel.nativeRichTextFood;
            NativeRichTextFood localFood = (NativeRichTextFood) syFeed.getFood();

            if (localFood.getTask().taskExecutState == XTaskExecutStateExecing) {
                mFragment.showToast(mFragment.getString(R.string.food_publishing_disable_edit));
                return;
            }

            //syFeed.getFoodNoteId()不为空表示二次编辑的本地数据
            if (!TextUtils.isEmpty(syFeed.getFoodNoteId())) {
                mFragment.mAdapter.removeReEditNativeItem(syFeed.getFoodNoteId());
            }

            SYDataManager.shareDataManager().removeTask(localFood.getTask());
            mFragment.mAdapter.removeItem(foodModel);
            BrodcastActions.foodDeleted(syFeed, true);
            if (mFragment.mAdapter.getItemCount() <= 0) {
                mFragment.setEmptyView();
            }
            mFragment.showToast("删除成功");

            Intent intent = new Intent(MyActions.ACTION_PROFILE);
            intent.putExtra("type", "type_delete_item");
            LocalBroadcastManager.getInstance(mFragment.getActivity()).sendBroadcast(intent);
        } else if (foodModel.type == DeliciousFoodModel.MODEL_NET) {  //网络数据
            DeliciousFoodModel.SYSearchUserFoodModel sYSearchUserFoodModel = foodModel.sYSearchUserFoodModel;
            removeNetFoodNote(sYSearchUserFoodModel.foodNoteId);
        }
    }


    //删除网络的一条记录
    private void removeNetFoodNote(final String foodNoteId) {
//        mFragment.post(Constants.shareConstants().getNetHeaderAdress() + "/notes/removeFoodNoteV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
        mFragment.post(IUrlUtils.Search.removeFoodNoteV250, "", null, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    DeliciousFoodModel.FoodListModel foodModel = mFragment.mAdapter.removeItem(foodNoteId);
                    BrodcastActions.foodDeleted(createSYFeedByNetData(foodModel), true);
                    if (mFragment.mAdapter.getItemCount() <= 0) {
                        mFragment.setEmptyView();
                    }
                    mFragment.showToast("删除成功");

                    Intent intent = new Intent(MyActions.ACTION_PROFILE);
                    intent.putExtra("type", "type_delete_item");
                    LocalBroadcastManager.getInstance(mFragment.getActivity()).sendBroadcast(intent);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "cusId", foodNoteId, "recordId", foodNoteId);
    }

    //根据网络的数据创建一个本地的SYFeed
    public SYFeed createSYFeedByNetData(DeliciousFoodModel.FoodListModel foodModel) {
        if (foodModel == null || foodModel.sYSearchUserFoodModel == null) {
            return null;
        }
        SYFeed syFeed = new SYFeed();
        syFeed.setFoodNoteId(foodModel.sYSearchUserFoodModel.foodNoteId);
        return syFeed;
    }

    public String getRestName(SYFeed syFeed) {
        String rest_name = "";
        if (syFeed.getFood() == null || syFeed.getFood().getPoi() == null
                || FFUtils.isStringEmpty(syFeed.getFood().getPoi().getTitle())) {
            rest_name = "无商户";
        } else {
            rest_name = syFeed.getFood().getPoi().getTitle();
        }
        return rest_name;
    }
}
