package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.NoSlideGridView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenglin on 2017-6-13.
 */

public class DiningGridView extends NoSlideGridView {
    public static final int TYPE_SINGLE = 1;//单选
    public static final int TYPE_MULTIPLE = 2;//多选
    public static final int MAX_COUNT = 3;

    private MyAdapter mAdapter;

    public DiningGridView(Context context) {
        super(context);
    }

    public DiningGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int type) {
        this.setNumColumns(3);
        int space = DisplayUtil.dip2px(7.5f);
        this.setPadding(space, 0, space * 2, 0);
        mAdapter = new MyAdapter((BaseActivity) getContext(), type);
        this.setAdapter(mAdapter);
    }

    public MyAdapter getAdapter() {
        return mAdapter;
    }

    public void setSelectedItem(DiningTypeItem item) {
        getAdapter().setSelectedItem(item);
    }

    public void setChild(DiningGridView child) {
        getAdapter().setChild(child);
    }

    public static class MyAdapter extends MyBaseAdapter<DiningTypeItem> {
        private BaseActivity mActivity;
        private DiningGridView mChild;
        private int mType = DiningGridView.TYPE_SINGLE;
        private List<DiningTypeItem> indexList = new LinkedList<>();

        public MyAdapter(BaseActivity context, int type) {
            super(context);
            mActivity = context;
            mType = type;
        }

        public List<DiningTypeItem> getIndexList() {
            return indexList;
        }

        public void setSelectedItem(DiningTypeItem item) {
            if (mType == TYPE_SINGLE) {
                indexList.clear();
                indexList.add(item);
                if (mChild != null) {
                    if (item.foodTypeList != null && item.foodTypeList.size() > 0) {
                        mChild.getAdapter().setDataList(item.foodTypeList);
                        mChild.getAdapter().getIndexList().clear();
                        mChild.setSelectedItem(item.foodTypeList.get(0));
                    }
                }
            } else if (mType == TYPE_MULTIPLE) {
                if (item.name.startsWith("全部")) {
                    indexList.clear();
                } else {
                    for (DiningTypeItem item1 : indexList) {
                        if (item1.name.startsWith("全部")) {
                            indexList.remove(item1);
                            break;
                        }
                    }
                }

                if (indexList.contains(item)) {
                    indexList.remove(item);
                    for (DiningTypeItem tempItem : indexList) {
                        if (tempItem.index > item.index) {
                            tempItem.index = tempItem.index - 1;
                        }
                    }
                } else {
                    if (indexList.size() == DiningGridView.MAX_COUNT) {
                        mActivity.showToast(mActivity.getString(R.string.dining_type_max_tips));
                        return;
                    }
                    item.index = indexList.size() + 1;
                    indexList.add(item);
                }
            }
            notifyDataSetChanged();
        }

        public List<DiningTypeItem> getSelectedList() {
            return indexList;
        }

        public void setChild(DiningGridView child) {
            mChild = child;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.dining_type_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.selectedIndex = (TextView) convertView.findViewById(R.id.selected_index);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final DiningTypeItem item = getItem(position);

            holder.textView.setText(item.name);

            if (indexList.contains(item)) {
                holder.textView.setBackgroundResource(R.drawable.dining_type_selected);
            } else {
                holder.textView.setBackgroundResource(R.drawable.dining_type_normal);
            }

            if (mType == TYPE_SINGLE) {
                holder.selectedIndex.setVisibility(View.INVISIBLE);
            } else {
                if (indexList.contains(item)) {
                    if (position == 0) {
                        holder.selectedIndex.setVisibility(View.INVISIBLE);
                    } else {
                        holder.selectedIndex.setVisibility(View.VISIBLE);
                        holder.selectedIndex.setText("" + item.index);
                    }
                } else {
                    holder.selectedIndex.setVisibility(View.INVISIBLE);
                }
            }

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedItem(item);
                }
            });
            return convertView;
        }

        static class ViewHolder {
            TextView textView;
            TextView selectedIndex;
        }
    }

}
