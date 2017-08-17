package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddyuluActivity extends BaseActivity<IntentData> {

    @Bind(R.id.lv_list)
    ListView lvList;
    private MyBaseAdapter<Holder, ConfigResult.HotWordBean.ShortContentsBean> adapter;

    public static class Holder {
        TextView text;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addyulu);
        ButterKnife.bind(this);
        adapter = new MyBaseAdapter<Holder, ConfigResult.HotWordBean.ShortContentsBean>(Holder.class,
                this, R.layout.item_yullu, SP.getConfig().getConfig().getHotword().getShortContents()) {
            @Override
            public void initView(View convertView, Holder holder, int position, ConfigResult.HotWordBean.ShortContentsBean item) {
                holder.text.setText(item.getContent());
            }
        };
        lvList.setAdapter(adapter);

        setTitle("美食语录");

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK, new Intent().putExtra("text", adapter.getItem(position).getContent()));
                finish();
            }
        });
    }
}
