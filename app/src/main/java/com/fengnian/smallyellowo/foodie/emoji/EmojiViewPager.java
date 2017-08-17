package com.fengnian.smallyellowo.foodie.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.fengnian.smallyellowo.foodie.R;

import java.util.List;

/**
 * Created by chenglin on 2017-2-21.
 */

public class EmojiViewPager extends ViewPager {
    private ViewPagerAdapter mViewPagerAdapter;

    public EmojiViewPager(Context context) {
        super(context);
        init();
    }

    public EmojiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public void setEmojiGridViewList(List<EmojiGridView> list) {
        mViewPagerAdapter = new ViewPagerAdapter(getContext(), list);
        this.setAdapter(mViewPagerAdapter);
    }

    public int getEmojiShowViewHeight() {
        int keyboardHeight = EmojiUtils.getEmojiKeyboardHeight();

        int showEmojiHeight;
        int bottomView = getContext().getResources().getDimensionPixelSize(R.dimen.emoji_panel_btn_size);
        showEmojiHeight = keyboardHeight - bottomView;
        return showEmojiHeight;
    }

    public List<EmojiGridView> getEmojiGridViewList() {
        if (mViewPagerAdapter != null) {
            return mViewPagerAdapter.list;
        }
        return null;
    }

    public static class ViewPagerAdapter extends PagerAdapter {
        public List<EmojiGridView> list;
        private Context mContext;

        public ViewPagerAdapter(Context context, List<EmojiGridView> list) {
            this.list = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

}
