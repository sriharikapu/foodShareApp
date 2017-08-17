package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * meilishuo -- weichenglin create in 15/9/17
 */
public class GridAdapter<T> extends MyBaseAdapter<T> {
    private Context mContext;
    private int mNumColumns = 2;
    private int mLineSpace;
    private ItemClickListener mItemClickListener;

    /**
     * @param context Context
     * @param columns gird一行要显示的列数
     */
    public GridAdapter(Context context, int columns) {
        super(context);
        mContext = context;
        mNumColumns = columns;
        init();
    }

    public GridAdapter(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        mLineSpace = 0;
    }

    @Override
    public final int getCount() {
        int remainder = super.getCount() % mNumColumns;
        if (remainder <= 0) {
            return super.getCount() / mNumColumns;
        } else {
            return super.getCount() / mNumColumns + 1;
        }
    }

    public int getItemCount() {
        return getData().size();
    }

    @Override
    public final View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout rootLinear = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_listview_item, null);
            rootLinear = (LinearLayout) convertView;
            rootLinear.setPadding(mLineSpace / 2, 0, mLineSpace / 2, 0);
        }

        rootLinear = (LinearLayout) convertView;
        final int childCount = rootLinear.getChildCount();

        if (childCount < mNumColumns) {
            for (int i = childCount; i < mNumColumns; i++) {
                int itemPos = mNumColumns * position + i;
                if (itemPos < getData().size()) {
                    View itemView = getItemView(itemPos, null);
                    rootLinear.addView(itemView);
                    setItemViewParams(itemView);
                    setOnItemClick(itemPos, itemView);
                }
            }
        } else {
            for (int i = 0; i < mNumColumns; i++) {
                int itemPos = mNumColumns * position + i;
                View itemView = rootLinear.getChildAt(i);
                boolean isVisibility = getItemViewVisibility(itemPos, itemView);
                if (isVisibility) {
                    itemView.setVisibility(View.VISIBLE);
                    getItemView(itemPos, itemView);
                    setOnItemClick(itemPos, itemView);
                } else {
                    itemView.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    /**
     * 此方法相当于getView，子类只需继承此方法当做getView来用就行了
     */
    protected View getItemView(final int position, final View convertView) {
        return convertView;
    }

    private void setOnItemClick(final int position, final View itemView) {
        if (mItemClickListener != null && itemView != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position, itemView);
                }
            });
        }
    }

    /**
     * 隐藏由于奇数时产生的不需要的ItemView
     */
    private boolean getItemViewVisibility(final int position, View convertView) {
        if (null != convertView) {
            if (position >= getData().size()) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public final int getNumColumns() {
        return mNumColumns;
    }

    private void setItemViewParams(View itemView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemView.getLayoutParams();
        params.width = getItemViewWidth();
        params.leftMargin = mLineSpace / 2;
        params.rightMargin = mLineSpace / 2;
        params.bottomMargin = mLineSpace / 2;
        params.topMargin = mLineSpace / 2;
    }

    /**
     * 在setAdapter之前调用这个方法
     */
    public final void setLineSpace(int lineSpace) {
        mLineSpace = lineSpace;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public final int getItemViewWidth() {
        int screenWidth = DisplayUtil.screenWidth;
        int itemWidth = (screenWidth - mLineSpace * (mNumColumns + 1)) / mNumColumns;
        return itemWidth;
    }

    public interface ItemClickListener {
        void onItemClick(int position, View itemView);
    }
}
