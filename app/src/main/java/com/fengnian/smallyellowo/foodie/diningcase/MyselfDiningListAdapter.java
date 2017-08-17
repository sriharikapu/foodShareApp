package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.HashMap;

/**
 * Created by chenglin on 2017-4-12.
 */

public class MyselfDiningListAdapter extends MyBaseAdapter<MyselfDiningListFragment.ItemModel> {
    private BaseActivity mActivity;
    private MyselfDiningListFragment mFragment;

    public MyselfDiningListAdapter(MyselfDiningListFragment fragment) {
        super(fragment.getActivity());
        mFragment = fragment;
        mActivity = (BaseActivity) fragment.getActivity();
    }

    public void showDeleteDialog(final MyselfDiningListFragment.ItemModel model) {
        EnsureDialog.Builder dialog = new EnsureDialog.Builder(mActivity);
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(model);

            }
        }).create().show();
        dialog.setCancelable(true);
    }

    private void delete(final MyselfDiningListFragment.ItemModel model) {
//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/discover/onePersonFood/delOneFoodTjById.do",
        mActivity.post(IUrlUtils.WorkDining.delOneFoodTjById,
                "正在删除", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if (response != null && response.getErrorCode() == 0) {
                            mActivity.showToast("删除成功");
                            MyselfDiningListAdapter.this.remove(model);
                            if (getCount() <= 0) {
                                mFragment.showEmpty();
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "id", model.id);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.myself_dining_adapter_item, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_title_2 = (TextView) convertView.findViewById(R.id.tv_title_2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyselfDiningListFragment.ItemModel item = getItem(position);
        holder.tv_title.setText("目的地：" + item.position);
        holder.tv_title_2.setText("分类：" + item.ptype);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (FFUtils.checkNet()) {
                    showDeleteDialog(item);
                } else {
                    mActivity.showToast(mActivity.getString(R.string.lsq_network_connection_interruption));
                }
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mActivity.showToast(mActivity.getString(R.string.lsq_network_connection_interruption));
                    return;
                }
                getRecommendList(item);
            }
        });

        return convertView;
    }

    /**
     * 得到推荐列表数据
     */
    private void getRecommendList(final MyselfDiningListFragment.ItemModel item) {
        if (!FFUtils.checkNet()) {
            mActivity.showToast(mActivity.getString(R.string.lsq_network_connection_interruption));
            return;
        }

        final RecomRequestModel mParams = new RecomRequestModel();
        mParams.ptypes = item.ptype;
        mParams.tjID = item.id;
        mParams.latitude = item.latitude;
        mParams.longitude = item.longitude;
        pushEvent180(item.ptype);
//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/discover/onePersonFood/onePersonFoodList.do",
        mActivity.post(IUrlUtils.WorkDining.onePersonFoodList,
                "", null, new FFNetWorkCallBack<RecomResultModel>() {
                    @Override
                    public void onSuccess(RecomResultModel response, FFExtraParams extra) {
                        if (response != null && response.syFindMerchantInfos != null) {
                            if (response.syFindMerchantInfos.size() > 0) {
                                DiningRecommendActivity.startFromSingle(mActivity, response.syFindMerchantInfos, mParams);
                            } else {
                                new EnsureDialog.Builder(mActivity).setMessage(R.string.dining_recommend_result_empty)
                                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "pageSize", 15
                , "shopId", "0"
                , "longitude", item.longitude
                , "latitude", item.latitude
                , "ptypes", item.ptype
                , "city", "北京"
                , "tjID", mParams.tjID
                , "openGPS", "0"
        );

    }


    private static class ViewHolder {
        public TextView tv_title;
        public TextView tv_title_2;
    }

    private void pushEvent180(String type){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("food_type", type);
        mActivity.pushEventAction("Yellow_180", event);
    }
}
