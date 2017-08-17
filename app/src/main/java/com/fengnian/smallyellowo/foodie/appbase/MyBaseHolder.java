package com.fengnian.smallyellowo.foodie.appbase;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fengnian.smallyellowo.foodie.R;

public abstract class MyBaseHolder extends RecyclerView.ViewHolder {
    public MyBaseHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T findViewById(int id) {
        return (T) itemView.findViewById(id);
    }

    public abstract void onBind(int position);

}