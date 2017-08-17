package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.ScoreDetailBean;

import java.util.List;

/**
 * Created by Administrator on 2016-9-8.
 */

public class MyChildpointAdapter extends BaseAdapter {

    private List<ScoreDetailBean> mlist;
    private Context context;

    public MyChildpointAdapter(List<ScoreDetailBean> list, Context context) {
        this.mlist=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewholder holder=null;
    if(holder==null){
        holder=new viewholder();

      view =View.inflate(context, R.layout.item_point_child,null);

        holder.tv_time=(TextView) view.findViewById(R.id.tv_time);
        holder.tv_content=(TextView)view.findViewById(R.id.tv_content);
        holder.tv_point=(TextView)view.findViewById(R.id.tv_point);
        view.setTag(holder);
      }else{
        holder=(viewholder)view.getTag();
      }

        ScoreDetailBean  bean=mlist.get(i);
        holder.tv_time.setText(bean.getScoreDetailsTime());
        holder.tv_content.setText(bean.getScoreDetailsContent());
        holder.tv_point.setText(bean.getScoreDetailsPoints());
      return view;
    }

    private class viewholder{
        private TextView  tv_time;
        private  TextView tv_content;
        private TextView  tv_point;
    }
}
