package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYThinkUser;
import com.fengnian.smallyellowo.foodie.contact.RelationSearchActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-9.
 */

public class RelationSearchAdapter extends BaseAdapter {

    private List<SYThinkUser> mlist;
    private FFContext mcontext;

    private int yello;
    public RelationSearchAdapter(  List dataList, FFContext context,int color) {
       this.mcontext=context;
        if(dataList!=null)
        this.mlist=dataList;
        else mlist=new ArrayList<>();
        this.yello=color;
    }



    @Override
    public int getCount() {
        int length=mlist.size();
        return length==0?0:length;
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
        final SYThinkUser user = mlist.get(i);
        viewholder holder=new viewholder();
        String  keyword= RelationSearchActivity.keyword;
        if("0".equals(user.getType())){//昵称
            view=View.inflate((Context) mcontext,R.layout.item_relation_search_nickname,null);
            holder.iv_header=(ImageView) view.findViewById(R.id.iv_header);
            holder.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holder.tv_name=(TextView)view.findViewById(R.id.tv_name);
            holder.tv_sigur=(TextView)view.findViewById(R.id.tv_sigur);

            IsAddCrownUtils.checkIsAddCrow(user.getUser(),holder.iv_add_crown);
            FFImageLoader.loadAvatar(mcontext, user.getUser().getHeadImage().getUrl(), holder.iv_header);

            String name=user.getUser().getSearchNickName();

            int index=name.indexOf(keyword,0);
            int  len=keyword.length();
            if(index!=-1){
            Spannable name_style=new SpannableString(name);
            name_style.setSpan(new ForegroundColorSpan(yello),index,index+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            holder.tv_name.setText(name_style);
            }else{
                holder.tv_name.setText(name);
            }
            String sigur=user.getUser().getPersonalDeclaration();
            int location=sigur.indexOf(keyword,0);
            if(location!=-1){
            Spannable sigur_style=new SpannableString(sigur);
            sigur_style.setSpan(new ForegroundColorSpan(yello),location,location+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            holder.tv_sigur.setText(sigur_style);
            }else{
                holder.tv_sigur.setText(sigur);
            }

        }else{
            view=View.inflate((Context)mcontext,R.layout.item_relation_search_number,null);
            holder.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holder.iv_header=(ImageView) view.findViewById(R.id.iv_header);
            holder.tv_name=(TextView)view.findViewById(R.id.tv_name);
            holder.tv_phone=(TextView)view.findViewById(R.id.tv_phone);
            IsAddCrownUtils.checkIsAddCrow(user.getUser(),holder.iv_add_crown);
            FFImageLoader.loadAvatar(mcontext, user.getUser().getHeadImage().getUrl(), holder.iv_header);
            String name=user.getUser().getSearchNickName();

            int index=name.indexOf(keyword,0);
            int  len=keyword.length();
            if(index!=-1){
                Spannable name_style=new SpannableString(name);
                name_style.setSpan(new ForegroundColorSpan(yello),index,index+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE );
                holder.tv_name.setText(name_style);
            }else{
                holder.tv_name.setText(name);
            }
            String phone=user.getUser().getTel();

            int location=phone.indexOf(keyword,0);
            if(location!=-1){
                Spannable phone_style=new SpannableString(phone);
                phone_style.setSpan(new ForegroundColorSpan(yello),location,location+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE );
                holder.tv_phone.setText(phone_style);
            }else{
                holder.tv_phone.setText(phone);
            }

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoIntent intent = new UserInfoIntent();
                intent.setUser(user.getUser());
//                ((BaseActivity)mcontext).startActivity(UserInfoActivity.class, intent);
                IsAddCrownUtils.ActivtyStartAct(user.getUser(),intent,mcontext);
            }
        });

        return view;
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }


    private class viewholder{
        private ImageView iv_header,iv_add_crown ;
        private TextView tv_name ;
        private TextView  tv_sigur ;
        public TextView  tv_phone ;
    }


}
