/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.fengnian.smallyellowo.foodie.widgets.wheelview;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * The simple Array wheel adapter
 *
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

    // items
    private T items[];

    /**
     * Constructor
     *
     * @param context the current context
     * @param items   the items
     */
    public ArrayWheelAdapter(Context context, T items[]) {
        super(context);

        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            T item = items[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        view.setTextColor(view.getResources().getColor(R.color.title_text_color));
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.getResources().getDimensionPixelSize(R.dimen.font_size_3_2));
        view.setPadding(0, DisplayUtil.dip2px(6f), 0, DisplayUtil.dip2px(6f));
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
}
