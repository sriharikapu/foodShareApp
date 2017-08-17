package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publish.SYVipUserRecommend;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;

import java.util.List;

/**
 * Created by Administrator on 2017-2-27.
 */

public class Club_User_Jing_Adapter  extends BaseAdapter{

    private FFContext  mcontext;
    private List<SYVipUserRecommend> mlist;

    public Club_User_Jing_Adapter(FFContext context,List<SYVipUserRecommend> list) {
        this.mlist=list;
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewhoder holder=null;
        if(convertView==null){

               holder=new viewhoder();
            convertView=View.inflate((Context) mcontext, R.layout.item_club_recommand,null);
            holder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else{
            holder= (viewhoder) convertView.getTag();
        }
        final SYVipUserRecommend  recommend=mlist.get(position);

        FFImageLoader.loadNativeImage(mcontext,recommend.getFoodImage(),holder.iv_head,-1, FFUtils.getPx(134), R.drawable.alpha);


        if(TextUtils.isEmpty(recommend.getFoodConment())){
            holder.tv_title.setVisibility(View.GONE);
        }else{
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(recommend.getFoodConment());
        }
        holder.tv_time.setText(recommend.getFoodTime());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!FFUtils.checkNet())
                {
                    mcontext.showToast("暂无网络");
                    return;
                }
                ((BaseActivity) mcontext).startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(recommend.getFoodRecordId()));
            }
        });

        return convertView;
    }




    public class  viewhoder {
     private ImageView iv_head;
     private TextView tv_title,tv_time;
    }
}
