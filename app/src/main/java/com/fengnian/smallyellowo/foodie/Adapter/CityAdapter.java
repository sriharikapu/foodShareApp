package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.SelectCityActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/7/19.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {

    private Context mContext;
    private List<CityListBean.CityBean> dataList;

    public CityAdapter(Context context) {
        mContext = context;
    }

    public CityAdapter(Context context, List<CityListBean.CityBean> list) {
        mContext = context;
        dataList = list;
    }

    public void addDataList(List<CityListBean.CityBean> list) {
        dataList = list;
        notifyDataSetChanged();
    }

    @Override
    public CityAdapter.CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityHolder(View.inflate(mContext, R.layout.item_select_city, null));
    }

    @Override
    public void onBindViewHolder(CityAdapter.CityHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (FFUtils.isListEmpty(dataList)) {
            return 0;
        }

        return dataList.size() + 1;
    }

    public class CityHolder extends MyBaseHolder {
        @Bind(R.id.city_image)
        ImageView cityImage;
        @Bind(R.id.city_name)
        TextView cityName;
//        @Bind(R.id.city_background)
//        View cityBackground;
        @Bind(R.id.city_checkbox)
        TextView cityCheckbox;

        public CityHolder(View itemView) {
            super(itemView);
            int width = (int) (FFUtils.getScreenWidth() * 0.92);
            int height = (int) (width * 0.45);
                int space = (FFUtils.getScreenWidth() - width) / 2;
                FFLogUtil.e("select_activity", "width = " + width + ";   height = " + height + " ;   space = " + space);
            ButterKnife.bind(this, itemView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cityImage.getLayoutParams();
            params.width = width;
            params.height = height;
            cityImage.setLayoutParams(params);

            /*RelativeLayout.LayoutParams paramsBg = (RelativeLayout.LayoutParams) cityBackground.getLayoutParams();
            paramsBg.width = width;
            paramsBg.height = height;
            cityBackground.setLayoutParams(paramsBg);*/

            RelativeLayout.LayoutParams paramsBox = (RelativeLayout.LayoutParams) cityCheckbox.getLayoutParams();
//                FFLogUtil.e("select_activity", "check box height = " + paramsBox.height);

            int cw = mContext.getResources().getDrawable(R.drawable.item_city_checkbox).getIntrinsicWidth() / 2;
            int ch = mContext.getResources().getDrawable(R.drawable.item_city_checkbox).getIntrinsicHeight() / 2;
            FFLogUtil.e("select_activity", "check box height = " + ch + " ;  width = " + cw);
            paramsBox.setMargins((int) (width - (ch/2)), (int) (height - (ch*1.5)), 0, 0);
            cityCheckbox.setLayoutParams(paramsBox);
        }

        @Override
        public void onBind(final int position) {
//                FFLogUtil.e("select_city", "position = " + position);
            if (position == getItemCount() - 1) {
//                cityBackground.setVisibility(View.GONE);
                cityImage.setImageResource(R.drawable.ic_watch_this_space);
                cityImage.setBackground(null);
                cityImage.setPadding(0, 0, 0, 0);
                cityName.setVisibility(View.GONE);
                cityCheckbox.setVisibility(View.GONE);
                return;
            }

            final CityListBean.CityBean currentCity = dataList.get(position);
            if (currentCity.getId().equals(CityPref.getSelectedCity().getId()) && CityPref.getSelectedCity().isChecked()) {
                currentCity.setChecked(true);
                cityImage.setBackgroundResource(R.drawable.shape_select_city_bg);
                cityCheckbox.setBackgroundResource(R.drawable.item_city_checkbox);
            } else {
                currentCity.setChecked(false);
                cityImage.setBackgroundResource(R.drawable.shape_select_city_normal_bg);
                cityCheckbox.setBackgroundResource(R.drawable.item_city_checkbox_normal);
            }

            cityImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        CityBean cityBean = dataList.get(position);
                    if (currentCity.isChecked()) {
                        ((SelectCityActivity)mContext).finish();
                        return;
                    }
                    for (CityListBean.CityBean city : dataList) {
                        if (city.isChecked()) {
                            city.setChecked(false);
                            break;
                        }
                    }

                    if (onItemClick != null){
                        onItemClick.itemClickListener(currentCity);
                    }

                    /*currentCity.setChecked(true);
                    CityPref.saveSelectedCity(currentCity);
                    Intent selectIntent = new Intent();
                    selectIntent.putExtra("city", currentCity);
                    selectIntent.setAction(IUrlUtils.Constans.ACTIION_SELECT_CITY);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(selectIntent);*/

                }
            });

            /*if (!TextUtils.isEmpty(currentCity.getImgPath())){
                return;
            }*/
            // 产品的逻辑为，  优先本地图片，再网络图片（替换本地图片）
            switch (currentCity.getId()) {
                case "3":
                    cityImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.item_city_beijing));
                    break;

                case "242":
                    cityImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.item_city_chengdu));
                    break;

                default:
                    cityImage.setImageResource(R.drawable.ic_watch_this_space);
                    break;
            }

            FFImageLoader.loadBigImage((FFContext) mContext, currentCity.getImgPath(), cityImage, R.drawable.ic_watch_this_space);

        }
    }

    private OnItemClick onItemClick;
    public interface OnItemClick{
        public void itemClickListener(CityListBean.CityBean city);
    }

    public void setOnItemClick(OnItemClick click){
        onItemClick = click;
    }
}
