package com.fengnian.smallyellowo.foodie.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by chenglin on 2017-4-5.
 */

public class RecyclerLoadMoreFooterView extends LinearLayout {
    public static final int LOADING = 1;
    public static final int LOAD_FINISH = 2;

    public RecyclerLoadMoreFooterView(Context context) {
        super(context);
        init();
    }

    public RecyclerLoadMoreFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOrientation(LinearLayout.VERTICAL);
        View footer = View.inflate(getContext(), R.layout.recycler_load_footer, null);
        addView(footer);
    }

    public void setLoadText(final String text) {
        TextView load_text = (TextView) findViewById(R.id.load_text);
        load_text.setText(text);
    }

    public void setLoadText(final int textId) {
        setLoadText(getContext().getString(textId));
    }

    public void setLoading() {
        setLoadText(R.string.load_ing);
    }

    public void setLoadFinish() {
        setLoadText(R.string.load_no_more);
    }
}
