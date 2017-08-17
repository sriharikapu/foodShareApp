package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.CheckScoreActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.JifenBean;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.tv_item_score;
import static com.fengnian.smallyellowo.foodie.R.id.tv_name;

/**
 * Created by Administrator on 2016-10-18.
 */

public class CheckScoreAdapter extends BaseAdapter {

    private Context mcontext;
    private List<CheckScoreActivity.Sum_Target_bean> mlist;

    private List<JifenBean.ItemListBean5> list_bean5;

    public CheckScoreAdapter(Context context, List<CheckScoreActivity.Sum_Target_bean> list, List<JifenBean.ItemListBean5> ll_list) {
        this.mcontext=context;
        this.mlist=list;
        this.list_bean5=ll_list;
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
        if(view==null){
            holder=new viewholder();
            view=View.inflate(mcontext, R.layout.item_check_score,null);

            holder.tv_name = (TextView) view.findViewById(tv_name);
            holder.tv_item_score=(TextView)view.findViewById(tv_item_score);
            view.setTag(holder);
        }else{
            holder= (viewholder) view.getTag();
        }
        CheckScoreActivity.Sum_Target_bean  bean=mlist.get(i);
        String score=bean.getScore()+"";
        List<CheckScoreActivity.Target_bean>   bean2_list=bean.getMmmmmlist();
        int  lengeth=bean2_list.size();
        String name="";
        for(int k=0;k<lengeth;k++){
                    name+=bean2_list.get(k).getContent();
        }
        String target_name=name.trim();
        holder.tv_name.setText(target_name);
        holder.tv_item_score.setText("+"+score);
        return view;
    }
    private class  viewholder{
        private TextView tv_name,tv_item_score;
    }
}
