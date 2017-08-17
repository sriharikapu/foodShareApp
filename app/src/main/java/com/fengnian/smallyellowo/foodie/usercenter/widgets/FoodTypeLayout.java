package com.fengnian.smallyellowo.foodie.usercenter.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.usercenter.bean.UserInfoBean.SyFoodNotesClassificationsBean;
//import com.fengnian.smallyellowo.foodie.bean.results.FoodsClassResult.SyFoodNotesClassificationsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/12.
 */

public class FoodTypeLayout extends RecyclerView {
    private FoodAdapter mAdapter;
    private List<SyFoodNotesClassificationsBean> items;

    public FoodTypeLayout(Context context) {
        super(context);
        initView();
    }

    public FoodTypeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FoodTypeLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        setLayoutManager(gridLayoutManager);
        addItemDecoration(new ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 8);
            }
        });
        mAdapter = new FoodAdapter();
        items = new ArrayList<>();
        setAdapter(mAdapter);
    }

    public void addItems(List<SyFoodNotesClassificationsBean> list) {
        if (items != null && FFUtils.isListEmpty(items)) {
            items.clear();
        }

        if (list.size() > 4) {
            list = list.subList(0, 4);
        }
        items.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    public class FoodAdapter extends Adapter<FoodAdapter.FoodHolder> {

        @Override
        public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FoodHolder(View.inflate(getContext(), R.layout.item_food_type, null));
        }

        @Override
        public void onBindViewHolder(FoodHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            if (FFUtils.isListEmpty(items)) {
                return 0;
            }
            return items.size();
        }

        public class FoodHolder extends MyBaseHolder {

            @Bind(R.id.food_type_name)
            TextView foodTypeName;
            @Bind(R.id.food_count)
            TextView foodCount;

            public FoodHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onBind(int position) {
                /*if (position == 1) {
                    items.get(position).setType("小吃快餐鲁菜");
                }*/
                foodTypeName.setText(items.get(position).getType() + ":");
                foodCount.setText(items.get(position).getCounts() + "个");
            }
        }
    }
}
