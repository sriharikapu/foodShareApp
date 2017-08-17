package com.fengnian.smallyellowo.foodie.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.fengnian.smallyellowo.foodie.MemoReultActivty;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.R.id.ed_food_name;

/**
 * Created by Administrator on 2016-10-9.
 */

public class MemoResultaAdapter extends BaseAdapter {


    private List<String>  mlist;
    private Context mcontext;
    private DraftModel model1;

    private Activity  activty;
    public MemoResultaAdapter(Context context,List<String>list,DraftModel model,Activity activity) {
         this.mlist=list;
         this.mcontext=context;
        this.model1=model;
        this.activty=activity;

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
    public View getView(final  int i, View view, ViewGroup viewGroup) {
        viewholder holder=null;
       if(view==null)   {
           holder=new viewholder();
           view =View.inflate(mcontext, R.layout.item_memo_result,null);
           holder.ed_food_name= (EditText) view.findViewById(ed_food_name);
           holder.iv_delete_img= (ImageView) view.findViewById(R.id.iv_delete_img);
           wacther_text(holder.ed_food_name,holder.iv_delete_img,holder, i);
           view.setTag(holder);
        }else{
           holder= (viewholder) view.getTag();
       }
         holder.ed_food_name.setText(mlist.get(i));

        holder.iv_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlist.size()!=0)
                mlist.remove(i);
//                model1.getFeed().getFood().setDishesNameList(mlist);
//                SYDataManager.shareDataManager().updateDraft(model1);
                if(mlist.size()==0){
                    MemoReultActivty.iv_memo_point.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        MemoReultActivty.tv_right_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                model1.getFeed().getFood().setDishesNameList(mlist);
                SYDataManager.shareDataManager().updateDraft(model1);
                activty.finish();
            }
        });
        return view;
    }

    private void  wacther_text(EditText editText, final  ImageView iv, viewholder holder, final int pos){
        TextWatcher watcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
             if(editable.length()>0){
                 iv.setVisibility(View.VISIBLE);
             }else{
                 iv.setVisibility(View.GONE);
             }
               mlist.add(pos,editable.toString());
                mlist.remove(pos+1);

            }
        };
        editText.addTextChangedListener(watcher);
    }

    private class   viewholder{
        private EditText ed_food_name;
        private ImageView iv_delete_img;

    }

}
