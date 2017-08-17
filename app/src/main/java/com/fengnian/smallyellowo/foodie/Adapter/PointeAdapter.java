package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.ScoreDayBean;
import com.fengnian.smallyellowo.foodie.widgets.MyListView;

import java.util.List;

/**
 * Created by Administrator on 2016-9-8.
 */

public class PointeAdapter extends BaseAdapter {

    private List<ScoreDayBean> mlist;
    private Context context;

    public PointeAdapter(List<ScoreDayBean> list, Context context) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        viewholder holder=null;
        if(holder==null){
            holder=new viewholder();

            view =View.inflate(context, R.layout.item_pointer,null);

            holder.tv_date=(TextView) view.findViewById(R.id.tv_date);
            holder.tv_point=(TextView)view.findViewById(R.id.tv_point);
            holder.iv_icon=(ImageView)view.findViewById(R.id.iv_icon);
            holder.child_listview=(MyListView)view.findViewById(R.id.child_listview);

            view.setTag(holder);
        }else{
            holder=(viewholder)view.getTag();
        }

                 ScoreDayBean dayBean = mlist.get(i);
                 holder.tv_date.setText(dayBean.getScoreDayTime());
                 holder.tv_point.setText(dayBean.getScoreDayPoints());

                MyChildpointAdapter adapter1=new MyChildpointAdapter(dayBean.getScoreDetails(),context);
                holder.child_listview.setAdapter(adapter1);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoreDayBean  dayBean=mlist.get(i);
                if(dayBean.getScoreDetails().size()>0&&!dayBean.isFlag()){
                    dayBean.setFlag(true);
                    MyListView lview= (MyListView)view.findViewById(R.id.child_listview);
                    lview.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.iv_icon).setBackgroundResource(R.mipmap.arrow_botton);
                } else if(dayBean.isFlag()){
                    dayBean.setFlag(false);
                    view.findViewById(R.id.iv_icon).setBackgroundResource(R.mipmap.arrow_top);
                    view.findViewById(R.id.child_listview).setVisibility(View.GONE);
                }
            }
        });
            return view;
    }

    private class viewholder{
        private TextView  tv_date;
        private TextView  tv_point;
        private ImageView iv_icon;
        private MyListView child_listview;
    }

//
//    public PointeAdapter(Class viewHolderClass, int layoutId, List dataList, FFContext context) {
//        super(viewHolderClass, layoutId, dataList, context);
//    }
//
//    public PointeAdapter(Class viewHolderClass, int layoutId, FFContext context) {
//        super(viewHolderClass, layoutId, context);
//    }
//
//    @Override
//    public void setViewData(Object viewHolder, int position, Object model) {
//        System.out.println("是否进来 了呢 .....");
//        if (viewHolder != null && viewHolder instanceof MyPointsActivty.viewholder) {
//            MyPointsActivty.viewholder  holder=(MyPointsActivty.viewholder)viewHolder;
//            if(model!=null&&model instanceof ScoreDayBean){
//                ScoreDayBean dayBean = (ScoreDayBean) model;
//                 holder.tv_date.setText(dayBean.getScoreDayTime());
//                 holder.tv_point.setText(dayBean.getScoreDayPoints());
//
//                MyChildpointAdapter adapter1=new MyChildpointAdapter(dayBean.getScoreDetails(),(Context)mcontext);
//                holder.child_listview.setAdapter(adapter1);
//            }
//        }
//    }



}
