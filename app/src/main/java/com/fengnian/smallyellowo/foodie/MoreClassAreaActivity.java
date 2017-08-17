package com.fengnian.smallyellowo.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean.AreaEntity;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean.AreaEntity.ListEntity;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean.AreaEntity.ListEntity.AreaListEntity;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.intentdatas.MoreClassAreaIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestSearchResultIntent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreClassAreaActivity extends BaseActivity<MoreClassAreaIntent> {

    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tab_bottom1)
    View tabBottom1;
    @Bind(R.id.tab_bottom2)
    View tabBottom2;
    @Bind(R.id.lv_class)
    ListView lvClass;
    @Bind(R.id.lv_area_right)
    ListView lvAreaRight;
    @Bind(R.id.lv_area_left)
    ListView lvAreaLeft;
    @Bind(R.id.ll_area)
    LinearLayout llArea;

    private SiftBean data;

    public static class SiftHolder {

        TextView textView;
        ImageView imageView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_class_area);
        data = SP.getConfig().getConfig().getSift();
        setTitle("更多");
        ButterKnife.bind(this);

        lvClass.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.FoodKindListBean.FoodKindBean>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getFoodKind().getFoodKind()) {
            @Override
            public void initView(View convertView, SiftHolder holder, final int position, final SiftBean.FoodKindListBean.FoodKindBean item) {
                holder.textView.setText(item.getContent());
                holder.textView.setGravity(Gravity.CENTER);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK, new Intent().putExtra("content", item.getContent()).putExtra("type", RestSearchResultIntent.TYPE_CLASS));
                        finish();
                    }
                });
            }
        });

        lvAreaLeft.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getBusiness().getList()) {

            private int areaParent = 0;

            @Override
            public void initView(View convertView, SiftHolder holder, final int position, final SiftBean.BusinessListBean.BusinessGroup item) {
                if (areaParent == position) {
                    convertView.setBackgroundColor(0xffffffff);
                } else {
                    convertView.setBackgroundColor(0xffeeeeee);
                }
                holder.textView.setText(item.getContent());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areaParent = position;
                        notifyDataSetChanged();
                        MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business> adapter = ((MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business>) lvAreaRight.getAdapter());
                        adapter.setData(item.getList());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        lvAreaRight.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getBusiness().getList().get(0).getList()) {

            @Override
            public void setData(List<SiftBean.BusinessListBean.BusinessGroup.Business> data) {
                super.setData(data);
                notifyDataSetChanged();
            }

            @Override
            public void initView(View convertView, SiftHolder holder, int position, final SiftBean.BusinessListBean.BusinessGroup.Business item) {
                holder.textView.setText(item.getContent());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK, new Intent().putExtra("item", (Parcelable) item).putExtra("type", RestSearchResultIntent.TYPE_AREA));
                        finish();
                    }
                });
            }
        });

        onClick(getIntentData().isArea() ? tvArea : tvClass);
    }

    @OnClick({R.id.tv_class, R.id.tv_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_class:
                tabBottom1.setVisibility(View.GONE);
                tabBottom2.setVisibility(View.VISIBLE);
                tvClass.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvArea.setTextColor(getResources().getColor(R.color.ff_text_black));
                lvClass.setVisibility(View.VISIBLE);
                llArea.setVisibility(View.GONE);
                break;
            case R.id.tv_area:
                tabBottom2.setVisibility(View.GONE);
                tabBottom1.setVisibility(View.VISIBLE);
                tvClass.setTextColor(getResources().getColor(R.color.ff_text_black));
                tvArea.setTextColor(getResources().getColor(R.color.colorPrimary));
                lvClass.setVisibility(View.GONE);
                llArea.setVisibility(View.VISIBLE);
                break;
        }
    }

}