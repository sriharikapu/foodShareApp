package com.fengnian.smallyellowo.foodie.emoji;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-2-20.
 */

public class TestEmojiAct extends Activity {
    CustomEmojiEditText mEdit;
    EmojiPanelLayout emojiPanelLayout;
    ImageView emojiPanel;
    View mContentView;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_emoji_layout);
        mEdit = (CustomEmojiEditText) findViewById(R.id.edit);
        emojiPanelLayout = (EmojiPanelLayout) findViewById(R.id.emoji_panel);
        emojiPanel = (ImageView) findViewById(R.id.emoji_panel_show);
        mContentView = findViewById(R.id.content_view);

        emojiPanelLayout.setAttachedEditText(mEdit);
        emojiPanelLayout.setAttachedEmojiBtn(emojiPanel);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.content_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

        emojiPanelLayout.setOnKeyboardStateListener(new EmojiPanelLayout.onKeyboardStateListener() {

            @Override
            public void onKeyboardShow(boolean isShow) {

            }

            @Override
            public void onFixLayoutParam(boolean isFix) {
                Log.v("tag_2", "isFix = " + isFix);

                if (isFix) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
                    params.height = 1008;
                    params.weight = 0;
                    mContentView.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
                    params.height = 0;
                    params.weight = 1;
                    mContentView.setLayoutParams(params);
                }
            }
        });
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("" + i);
        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            EditText editText = new EditText(parent.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.height = DisplayUtil.dip2px(50f);
            params.width = DisplayUtil.dip2px(300f);
            params.topMargin = DisplayUtil.dip2px(5f);
            linearLayout.addView(editText, params);

            MyViewHolder holder = new MyViewHolder(linearLayout);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends ViewHolder {
            public MyViewHolder(View view) {
                super(view);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}