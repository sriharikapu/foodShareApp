package com.fengnian.smallyellowo.foodie.personal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.fengnian.smallyellowo.foodie.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * weichenglin create in 15/5/12
 */
public class ChooseAdapter extends BaseAdapter {
    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SECOND = 1;
    private Context mContext;
    private ChooseAdapter mChildAdapter;
    private PopupWindow pop_window;
    private ChooseListener mListener;
    private FilterItem mSelectedItem;
    private List<FilterItem> mDataList = new ArrayList<>();

    public ChooseAdapter(Context context, ChooseListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setChildAdapter(ChooseAdapter adapter) {
        mChildAdapter = adapter;
    }

    public ChooseAdapter getChildAdapter() {
        return mChildAdapter;
    }

    public void setPopWindow(PopupWindow pop) {
        pop_window = pop;
    }

    public void setCurrentItem(FilterItem item) {
        mSelectedItem = item;
    }

    public FilterItem getCurrentItem() {
        return mSelectedItem;
    }

    public void setData(List<FilterItem> list) {
        if (null != list) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public List<FilterItem> getData() {
        return mDataList;
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    private void dismissPop() {
        if (pop_window != null) {
            pop_window.dismiss();
            pop_window = null;
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public FilterItem getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.choose_item_layout, null);
            holder.parent_text = (TextView) convertView.findViewById(R.id.parent_text);
            holder.parent_item = convertView.findViewById(R.id.parent_item);
            holder.line = convertView.findViewById(R.id.line);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FilterItem item = getItem(position);
        holder.parent_text.setText(item.title);

//        if (item_image_gallery.type == AppConstants.TYPE_CATEGORY_PARENT) {
//
//        } else if (item_image_gallery.type == AppConstants.TYPE_CATEGORY_CHILD) {
//
//        } else if (item_image_gallery.type == AppConstants.TYPE_AREA_PARENT) {
//
//        } else if (item_image_gallery.type == AppConstants.TYPE_AREA_CHILD) {
//
//        }

        if (mChildAdapter == null) {
            holder.parent_item.setBackgroundColor(getColor(R.color.white_bg));
            if (item.id != null && mSelectedItem != null && item.id.equals(mSelectedItem.id)) {
                holder.parent_text.setTextColor(getColor(R.color.colorPrimary));
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.parent_text.setTextColor(getColor(R.color.title_text_color));
                holder.imageView.setVisibility(View.GONE);
            }

            if (position == getCount() - 1) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
        } else {
            holder.line.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            if (item.id != null && mSelectedItem != null && item.id.equals(mSelectedItem.id)) {
                holder.parent_item.setBackgroundColor(getColor(R.color.color_5));
            } else {
                holder.parent_item.setBackgroundColor(getColor(R.color.color_4));
            }
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = item;
                if (item.level == LEVEL_FIRST) {
                    if (mChildAdapter != null) {
                        mChildAdapter.setData(item.childList);
                    }

                    if (!isOpen(item)) {
                        if (mListener != null) {
                            mListener.onFinish(item);
                        }
                        dismissPop();
                    }
                } else if (item.level == LEVEL_SECOND) {
                    if (mListener != null) {
                        mListener.onFinish(item);
                    }
                    dismissPop();
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private int getColor(int color_id) {
        return mContext.getResources().getColor(color_id);
    }

    private boolean isOpen(FilterItem item) {
        if (item.childList != null && item.childList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private static class ViewHolder {
        public TextView parent_text;
        public View parent_item, line, imageView;
    }

}
