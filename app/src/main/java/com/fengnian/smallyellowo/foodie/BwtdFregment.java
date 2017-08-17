package com.fengnian.smallyellowo.foodie;

import android.view.View;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.appbase.PullToRefreshFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.BwtdResult;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

public class BwtdFregment extends PullToRefreshFragment<BwtdActivity.HolderBwtd, BwtdResult.SYMillionFindMerchantsGroupModel, BwtdResult> {
    @Override
    protected Object[] getParams(int currentPage) {
        return new Object[]{"pageSize", 20, "lastId", currentPage == 0 ? 0 : (getAdapter().isEmpty() ? "0" : getAdapter().getItem(getAdapter().getCount() - 1).getId())};
    }

    @Override
    protected String getUrl() {
        return IUrlUtils.Search.getMillionMerchant;
//        return Constants.shareConstants().getNetHeaderAdress() + "/page/getMillionMerchant.do";
    }

    @Override
    protected void refreshItem(View convertView, BwtdActivity.HolderBwtd holder, int position, final BwtdResult.SYMillionFindMerchantsGroupModel item) {

        holder.tv_title.setText(item.getMillionFindMerchantsGrouptitle());
        holder.tv_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebInfo data = new WebInfo();
                data.setTitle(item.getActivityTitle());
                data.setUrl(item.getActivityHtmlUrl());
                startActivity(CommonWebviewUtilActivity.class, data);
            }
        });
        while (holder.ll_item_container.getChildCount() > 0) {
            BwtdActivity.HolderBwtd.items.add((BwtdActivity.HolderBwtd.HolderBwtdItem) holder.ll_item_container.getChildAt(0).getTag());
            holder.ll_item_container.removeViewAt(0);
        }
        if (item.getMillionFindMerchantsModelList() != null) {
            for (int i = 0; i < item.getMillionFindMerchantsModelList().size(); i++) {
                BwtdActivity.HolderBwtd.HolderBwtdItem holder1;
                if (BwtdActivity.HolderBwtd.items.isEmpty()) {
                    holder1 = new BwtdActivity.HolderBwtd.HolderBwtdItem(getBaseActivity(), holder.ll_item_container);
                } else {
                    holder1 = BwtdActivity.HolderBwtd.items.remove(0);
                }
                holder1.onBind(item.getMillionFindMerchantsModelList().get(i));
                holder.ll_item_container.addView(holder1.itemView);
            }
        }
    }

    @Override
    protected void onGetView(BwtdActivity.HolderBwtd holder) {
    }

    @Override
    protected int getItemViewId(int position) {
        return R.layout.item_bwtd;
    }
}