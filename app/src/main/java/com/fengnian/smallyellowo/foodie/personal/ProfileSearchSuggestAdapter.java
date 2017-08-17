package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lasque.tusdk.core.TuSdkContext.getString;

/**
 * Created by chenglin on 2017-3-31.
 */

public class ProfileSearchSuggestAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private List<ProfileSearchModel> mDataList = new ArrayList<>();

    private ProfileSearchSuggestAdapter() {
    }

    public ProfileSearchSuggestAdapter(BaseActivity context) {
        mContext = context;
    }

    public void setData(List<ProfileSearchModel> list) {
        if (null != list) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ProfileSearchModel getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.profile_search_item_layout, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.right_text = (TextView) convertView.findViewById(R.id.right_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProfileSearchModel item = getItem(position);

        //标题文字
        if (item != null && !TextUtils.isEmpty(item.title)) {
            holder.text.setText(item.title);
        } else {
            holder.text.setText("");
        }

        //右侧文字
        String rightText = "";
        if (item != null) {
            if (item.dataCount > 0 && item.wantEatCount > 0) {
                rightText = "美食记录" + item.dataCount + "，想吃清单" + item.wantEatCount;
            } else if (item.dataCount > 0) {
                rightText = "美食记录" + item.dataCount;
            } else if (item.wantEatCount > 0) {
                rightText = "想吃清单" + item.wantEatCount;
            }
        }
        holder.right_text.setText(rightText);

        //品类、区域
        holder.imageView.setImageResource(0);
        if (item != null) {
            if (item.isCategory == 0) {
                holder.imageView.setImageResource(R.drawable.profile_search_food_icon);
            } else if (item.isCategory == 1) {
                holder.imageView.setImageResource(R.drawable.profile_search_area_icon);
            }
        }

        //处理最后那根线
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.line.getLayoutParams();
        if (getCount() - 1 == position) {
            params.leftMargin = 0;
            params.bottomMargin = 0;
        } else {
            params.leftMargin = DisplayUtil.dip2px(30f);
            params.bottomMargin = 1;
        }
        holder.line.setLayoutParams(params);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mContext.showToast(getString(R.string.lsq_network_connection_interruption));
                    return;
                }
                if (item == null) {
                    return;
                }
                Intent intent = new Intent(mContext, ProfileSearchResultAct.class);
                intent.putExtra("keyWord", item.title);
                intent.putExtra("isCategory", item.isCategory);
                intent.putExtra("key", item.key);
                mContext.startActivity(intent);
                mContext.finish();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView text, right_text;
        public View line;
    }

    public static final class ProfileSearchList extends BaseResult {
        public List<ProfileSearchModel> resultData;
    }

    public static final class ProfileSearchModel {
        public int isCategory;
        public String title;
        public int dataCount;
        public int wantEatCount;
        public String key;
    }
}
