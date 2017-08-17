package com.fengnian.smallyellowo.foodie.emoji;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.NoSlideGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-2-20.
 */

public class EmojiGridView extends NoSlideGridView {
    public final static int COLUMNS_NUM = 7;
    public final static int ROMS_NUM = 3;
    public final static int TOTAL_NUM = COLUMNS_NUM * ROMS_NUM - 1;//为了显示最后一个删除按钮，所以这里减去1
    private EmojiGridAdapter mEmojiGridAdapter;
    private int mPosition;//当前在ViewPager显示的第几页，为了计算当前要显示的emoji个数
    private int mEmojiType;
    private EditText mAttachedEdit;

    public EmojiGridView(Context context, int position, int emojiType) {
        super(context);
        mPosition = position;
        mEmojiType = emojiType;
        init();
    }

//    public EmojiGridView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }

    private void init() {
        mEmojiGridAdapter = new EmojiGridAdapter(getContext());
        setNumColumns(COLUMNS_NUM);
        setAdapter(mEmojiGridAdapter);
        setVerticalSpacing(0);
        setHorizontalSpacing(0);
        int padding = DisplayUtil.dip2px(12f);
        setPadding(padding,0,padding,0);
    }

    /**
     * 设置emoji面板附属的EditText，用来删除上面的字符
     */
    public void setAttachedEditText(EditText editText) {
        mAttachedEdit = editText;
    }

    public void setEmojiItemClickListener(EmojiPanelLayout.onEmojiItemListener listener) {
        mEmojiGridAdapter.mItemClickListener = listener;
    }

    public class EmojiGridAdapter extends BaseAdapter {
        private List<EmojiGridItem> mEmojiList = new ArrayList<EmojiGridItem>();
        public EmojiPanelLayout.onEmojiItemListener mItemClickListener;


        public EmojiGridAdapter(Context context) {
            super();

            if (mEmojiType == EmojiPanelLayout.EMOJI_CUSTOM) {
                int emojiLength = EaseDefaultEmojiconDatas.custom_show_icons.length;

                int start = TOTAL_NUM * mPosition;
                int end;

                //计算每一页要显示的emoji数
                if (emojiLength - TOTAL_NUM * mPosition > TOTAL_NUM) {
                    end = TOTAL_NUM * mPosition + TOTAL_NUM;
                } else {
                    end = emojiLength;
                }

                for (int i = start; i < end; i++) {
                    EmojiGridItem emojiGridItem = new EmojiGridItem();
                    emojiGridItem.emojiDrawableId = EaseDefaultEmojiconDatas.custom_show_icons[i];
                    emojiGridItem.emojiString = EaseDefaultEmojiconDatas.emojis[i];
                    emojiGridItem.emojiType = mEmojiType;
                    mEmojiList.add(emojiGridItem);
                }

            } else if (mEmojiType == EmojiPanelLayout.EMOJI_SYSTEM) {
                int emojiLength = EmojiSysList.EMOJI_SYS_LIST.length;
                int start = TOTAL_NUM * mPosition;
                int end;

                //计算每一页要显示的emoji数
                if (emojiLength - TOTAL_NUM * mPosition > TOTAL_NUM) {
                    end = TOTAL_NUM * mPosition + TOTAL_NUM;
                } else {
                    end = emojiLength;
                }

                for (int i = start; i < end; i++) {
                    int unicode = EmojiSysList.EMOJI_SYS_LIST[i];
                    EmojiGridItem emojiGridItem = new EmojiGridItem();
                    emojiGridItem.emojiUnicode = unicode;
                    emojiGridItem.emojiString = EmojiUtils.getEmojiStringByUnicode(unicode);
                    emojiGridItem.emojiType = mEmojiType;
                    mEmojiList.add(emojiGridItem);
                }
            }

            //处理最后一个显示删除按钮的逻辑
            if (mEmojiList.size() < ROMS_NUM * COLUMNS_NUM) {
                int emptyCount = (ROMS_NUM * COLUMNS_NUM) - mEmojiList.size();
                for (int i = 0; i < emptyCount; i++) {
                    EmojiGridItem emptyEmojiGridItem = new EmojiGridItem();
                    emptyEmojiGridItem.isEmpty = true;
                    mEmojiList.add(emptyEmojiGridItem);
                }
            }
        }


        @Override
        public int getCount() {
            return mEmojiList.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final EmojiGridItem emojiItem = mEmojiList.get(position);

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.emoji_grid_item_layout, null);
            }
            TextView emojiTextView = (TextView) convertView.findViewById(R.id.emoji_text_item);
            ImageView emojiImage = (ImageView) convertView.findViewById(R.id.emoji_image_item);
            ImageView emojiDelete = (ImageView) convertView.findViewById(R.id.emoji_delete);

            emojiTextView.setVisibility(View.GONE);
            emojiImage.setVisibility(View.GONE);

            setItemLayoutParams(emojiTextView);
            setItemLayoutParams(emojiImage);
            setItemLayoutParams(emojiDelete);

            if (emojiItem.emojiType == EmojiPanelLayout.EMOJI_SYSTEM) {
                emojiTextView.setVisibility(View.VISIBLE);
                emojiTextView.setText(emojiItem.emojiString);
            } else {
                emojiImage.setVisibility(View.VISIBLE);
                emojiImage.setImageResource(emojiItem.emojiDrawableId);
            }

            if (position == mEmojiList.size() - 1) {
                emojiDelete.setVisibility(View.VISIBLE);
                emojiDelete.setImageResource(R.drawable.emoji_delete);
            } else {
                emojiDelete.setVisibility(View.GONE);
                emojiDelete.setImageResource(0);
            }

            emojiTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emojiItem.isEmpty) {
                        return;
                    }

                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(emojiItem);
                    }
                }
            });

            emojiImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emojiItem.isEmpty) {
                        return;
                    }

                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(emojiItem);
                    }
                }
            });

            emojiDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAttachedEdit != null && !TextUtils.isEmpty(mAttachedEdit.getText())) {
                        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                        mAttachedEdit.dispatchKeyEvent(event);
                    }
                }
            });

            return convertView;
        }


        private void setItemLayoutParams(View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = DisplayUtil.screenWidth / COLUMNS_NUM;
            params.width = params.height;
            view.setLayoutParams(params);
        }
    }

    public static final class EmojiGridItem {
        public int emojiType;//emoji的类型，为了区分是系统的，还是自定义的
        public int emojiUnicode;
        public String emojiString;
        public int emojiDrawableId;
        public boolean isEmpty = false;//用来标记空的item，目的是为了把那个删除箭头显示在最后一个
    }
}
