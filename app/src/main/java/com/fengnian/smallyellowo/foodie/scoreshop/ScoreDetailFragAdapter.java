package com.fengnian.smallyellowo.foodie.scoreshop;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.scoreshop.ScoreDetailActivity.MyViewHolder;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-5-4.
 */
public class ScoreDetailFragAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ScoreDetailFragment mFragment;
    private List<SYNewPerPointsListModel> mAllList = new ArrayList<>();
    private int mLoadState;

    public ScoreDetailFragAdapter(ScoreDetailFragment fragment) {
        mFragment = fragment;
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
        this.notifyDataSetChanged();
    }

    public void setDataList(List<SYNewPerPointsListModel> list) {
        //显示空页面
        if (list == null) {
            mAllList.clear();
            SYNewPerPointsListModel emptyFoodListModel = new SYNewPerPointsListModel();
            emptyFoodListModel.type = Constants.EMPTY;
            mAllList.add(emptyFoodListModel);
            this.notifyDataSetChanged();
            return;
        }

        mAllList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<SYNewPerPointsListModel> list) {
        if (list == null) {
            return;
        }
        mAllList.addAll(list);
        this.notifyDataSetChanged();
    }

    public List<SYNewPerPointsListModel> getDataList() {
        return mAllList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mFragment.getActivity(), R.layout.score_detail_item, null);

        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SYNewPerPointsListModel item = mAllList.get(position);
        if (item == null) {
            return;
        }
        if (getItemCount() == 1 && item.type == Constants.EMPTY) {
            setEmptyView(true, holder);
            return;
        } else {
            setEmptyView(false, holder);
        }

        setInfo(holder, item, position);
        setLoadMoreState(holder, position);
    }

    private void setEmptyView(boolean isEmpty, MyViewHolder holder) {
        if (holder.emptyImageView != null) {
            holder.convertView.removeView(holder.emptyImageView);
            holder.emptyImageView = null;
        }

        if (isEmpty) {
            setChildVisibility(holder.convertView, View.GONE);

            holder.emptyImageView = new ImageView(mFragment.getActivity());
            holder.emptyImageView.setImageResource(R.drawable.score_detail_empty_icon);
            LinearLayout.LayoutParams emptyLayoutParams = new LinearLayout.LayoutParams(DisplayUtil.screenWidth, -2);
            emptyLayoutParams.topMargin = DisplayUtil.dip2px(90f);
            holder.convertView.addView(holder.emptyImageView, 0, emptyLayoutParams);
        } else {
            setChildVisibility(holder.convertView, View.VISIBLE);
        }
    }

    private void setChildVisibility(ViewGroup viewGroup, int visibility) {
        if (viewGroup != null && viewGroup.getChildCount() > 0) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setVisibility(visibility);
            }
        }
    }

    private void setInfo(MyViewHolder holder, SYNewPerPointsListModel item, int position) {
        if (!TextUtils.isEmpty(item.scoreDetailsContent)) {
            holder.tv_title.setText(item.scoreDetailsContent);
        } else {
            holder.tv_title.setText("");
        }

        if (!TextUtils.isEmpty(item.scoreDetailsPoints)) {
            holder.tv_score.setText(item.scoreDetailsPoints);
        } else {
            holder.tv_score.setText("");
        }

        if (!TextUtils.isEmpty(item.scoreDetailsTime)) {
            holder.tv_time.setText(item.scoreDetailsTime);
        } else {
            holder.tv_time.setText("");
        }

        if (position == 0) {
            holder.line_top.setVisibility(View.VISIBLE);
            holder.line_top_2.setVisibility(View.VISIBLE);
        } else {
            holder.line_top.setVisibility(View.GONE);
            holder.line_top_2.setVisibility(View.GONE);
        }
    }

    private void setLoadMoreState(MyViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.footerView.setVisibility(View.VISIBLE);
            if (mLoadState == RecyclerLoadMoreFooterView.LOADING) {
                holder.footerView.setLoading();
            } else {
                holder.footerView.setLoadFinish();
            }
        } else {
            holder.footerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAllList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
