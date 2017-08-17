package com.fengnian.smallyellowo.foodie.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRelationTalentModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.fragments.IterationSocialFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lasque.tusdk.core.TuSdkContext.getString;

/**
 * Created by Administrator on 2017-2-20.
 */

public class Contact_Scoial__Recomd_GridAdapter extends RecyclerView.Adapter<Contact_Scoial__Recomd_GridAdapter.viewholder> {

    private List<SYRelationTalentModel> mlist;
    private FFContext mcontext;

    private  List<SYRelationTalentModel> templist;

    int pos_flag=0;
    private SocialDataManager<SYRelationTalentModel> dataManager;
    public Contact_Scoial__Recomd_GridAdapter(FFContext context, List<SYRelationTalentModel> list,List<SYRelationTalentModel> tlist) {
        this.mcontext=context;
        this.mlist=list;
        dataManager=new SocialDataManager();
        dataManager.setDataList((ArrayList<SYRelationTalentModel>) mlist);
        this.templist=dataManager.getNextList();


    }


    //创建新View，被LayoutManager所调用
    @Override
    public Contact_Scoial__Recomd_GridAdapter.viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iteration_social,parent,false);
        RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
//        //获取当前控件的布局对象
        params.height= FFUtils.getPx(209);//设置当前控件布局的高度
        int wid=(FFUtils.getDisWidth()*2/5);
        params.width=wid;
        view.setLayoutParams(params);//将设置好的布局参数应用到控件中
        viewholder vh = new viewholder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(Contact_Scoial__Recomd_GridAdapter.viewholder holder, final int position) {

        if(templist.size()>0){
            System.out.println("huoqu de pos..."+(templist.size()-1>=position));
            if(templist.size()-1>=position){
                final SYRelationTalentModel  result=templist.get(position);
                //设置数据
                final SYUser user=result.getUser();
                FFImageLoader.loadAvatar(mcontext,user.getHeadImage().getUrl(),holder.iv_head);
                holder.tv_name.setText(user.getNickName());
                holder.tv_jing_and_fans.setText("精选"+result.getJingXuanCount()+"篇"+"  "+"粉丝"+result.getFans()+"人");
                if(result.getMasteryFood()!=null)   {
                    String[] str=result.getMasteryFood().split(",");
                    if(str.length==0){
                        holder.tv_dish_type1.setVisibility(View.GONE);
                        holder.tv_dish_type2.setVisibility(View.GONE);
                    }else if(str.length==1){
                        holder.tv_dish_type1.setVisibility(View.VISIBLE);
                        holder.tv_dish_type2.setVisibility(View.GONE);
                        holder.tv_dish_type1.setText(str[0]);

                        setbag(holder.tv_dish_type1,type(str[0]));

                    }else{
                        holder.tv_dish_type1.setVisibility(View.VISIBLE);
                        holder.tv_dish_type2.setVisibility(View.VISIBLE);
                        holder.tv_dish_type1.setText(str[0]);
                        setbag(holder.tv_dish_type1,type(str[0]));
                        holder.tv_dish_type2.setText(str[1]);
                        setbag(holder.tv_dish_type2,type(str[1]));
                    }

                }else{
                    holder.tv_dish_type1.setVisibility(View.GONE);
                    holder.tv_dish_type1.setVisibility(View.GONE);
                }
                if(user.isByFollowMe()){
                    holder.tv_is_attion.setImageResource(R.mipmap.yiguanzhu_darenjian);
                }else{
                    holder.tv_is_attion.setImageResource(R.mipmap.daren_tuijian_attion);
                }
                IsAddCrownUtils.checkIsAddCrow(user,holder.iv_add_crown);

                holder.tv_is_attion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FFUtils.checkNet()) {
                            mcontext.showToast(getString(R.string.lsq_network_connection_interruption));
                            return;
                        }
                        String  followId=user.isByFollowMe()?"0":"1";

                        if("1".equals(followId)){ //guanzhu
                            result.getUser().setByFollowMe(true);
                            dataManager.deleteData(result);
                            result.getUser().setFollowMe(false);
                        }else{
                            result.getUser().setByFollowMe(false);
                            dataManager.addData(result);
                        }
                        notifyDataSetChanged();
                        checkIsAttion(user.getId(),followId,result);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FFUtils.checkNet()) {
                            mcontext.showToast(getString(R.string.lsq_network_connection_interruption));
                            return;
                        }
                        UserInfoIntent info=new UserInfoIntent();
                        info.setUser(user);
//                ((BaseFragmentActivity)mcontext).startActivity(UserInfoActivity.class,info);
//                ((BaseFragmentActivity)mcontext).startActivity(ClubUserInfoActivity.class,info);
                        IsAddCrownUtils.FragmentActivtyStartAct(user,info,mcontext);

                    }
                });

            }

            if (templist.size()>0&&position==getItemCount()-1){
                holder.rl_1.setVisibility(View.GONE);
                holder.rl_2.setVisibility(View.VISIBLE);

            }else{
                holder.rl_1.setVisibility(View.VISIBLE);
                holder.rl_2.setVisibility(View.GONE);
            }

            holder.rl_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



//                int len=4*(pos_flag+1);
//                if(mlist.size()-len==0){
//                    pos_flag=0;
//                    List<SYRelationTalentModel> list1=new ArrayList<>();
//                    list1.addAll(mlist);
//                    for(int i=0;i<list1.size();i++){
//
//                        SYUser us=list1.get(i).getUser();
//                        if(us.isByFollowMe()){
//                            mlist.remove(i);
//                        }
//                    }
//
//                }
//                  if(is_restart){ //重新开始的时候   剔除一遍
//                    pos_flag=0;
//                    is_restart=false;
//                    List<SYRelationTalentModel> list1=new ArrayList<>();
//                    list1.addAll(mlist);
//                    for(int i=0;i<list1.size();i++){
//
//                        SYUser us=list1.get(i).getUser();
//                        if(us.isByFollowMe()){
//                            mlist.remove(us);
//                        }
//                    }
//                }
//                else {
//                    pos_flag++;
//                }
//                if(mlist.size()<=4&&pos_flag==0){
//                    mcontext.showToast("暂无更多推荐的好友");
//                    pos_flag=0;
//                    return;
//                }
//
//                setTemplist(pos_flag);
                    if(dataManager.dataList.size()<4){
                     mcontext.showToast("暂无更多推荐的好友");
                    return;
                }
                    templist.clear();
                    templist.addAll(dataManager.getNextList());
                    notifyDataSetChanged();
                    IterationSocialFragment.scro_1.scrollToPosition(0);
                }
            });



        }
    }
    boolean  is_restart=false;
    void  setTemplist(int flag){
        int len=4*(flag+1);
        templist.clear();
        System.out.println("...........0"+(mlist.size()-len+4));
        if(mlist.size()-len>=0){  //够这次的四个

            for(int x=flag*4;x<len;x++){
                templist.add(mlist.get(x));
            }
        }
        else if(mlist.size()-len<0&&(mlist.size()>0)&&(mlist.size()-len+4)>0) {  //不够这次的四个


            //不够四个
            for (int y = mlist.size()-(mlist.size()-len+4); y < mlist.size() ; y++) {//(mlist.size()-len+4)+mlist.size()-(mlist.size()-len+4)
                templist.add(mlist.get(y));
            }
            is_restart=true;
        }
        else if(mlist.size()-len+4==0){
            pos_flag=0;
            setTemplist(pos_flag);
            List<SYRelationTalentModel> list1=new ArrayList<>();
            list1.addAll(mlist);
            for(int i=0;i<list1.size();i++){

                SYUser us=list1.get(i).getUser();
                if(us.isByFollowMe()){
                    mlist.remove(us);
                }
            }
        }
//
//                //下次 重新循环之前   去掉   关注了的
//
////               List<SYRelationTalentModel> list1=new ArrayList<>();
////                list1.addAll(mlist);
////                for(int i=0;i<list1.size();i++){
////
////                    SYUser us=list1.get(i).getUser();
////                    if(us.isByFollowMe()){
////                        if(i>=(mlist.size()-1))
////                        mlist.remove(i);
////                    }
////                }
////                 pos_flag=0;
////                setTemplist(pos_flag);
//            }
        FFLogUtil.e("mlist的集合中的数据还剩下多少呢  ........",mlist.size()+"");

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return templist.size()==0?0: (templist.size()+1);
    }



    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class viewholder extends RecyclerView.ViewHolder {
        public RelativeLayout rl_1,rl_2;
        public ImageView iv_head,tv_is_attion,iv_add_crown;
        public TextView tv_name,tv_jing_and_fans,tv_dish_type1,tv_dish_type2;
        public viewholder(View view){
            super(view);
            rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
            rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
            iv_add_crown = (ImageView) view.findViewById(R.id.iv_add_crown);
            iv_head = (ImageView) view.findViewById(R.id.iv_head);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_jing_and_fans = (TextView) view.findViewById(R.id.tv_jing_and_fans);
            tv_dish_type1 = (TextView) view.findViewById(R.id.tv_dish_type1);
            tv_dish_type2 = (TextView) view.findViewById(R.id.tv_dish_type2);
            tv_is_attion = (ImageView) view.findViewById(R.id.tv_is_attion);
        }
    }


    private  void setbag(View v,int i){

        if( i==1){
            v.setBackgroundResource(R.drawable.food_type1);
        }else if(i==2){
            v.setBackgroundResource(R.drawable.food_type2);
        }
        else if(i==3){
            v.setBackgroundResource(R.drawable.food_type3);
        }
        else if( i==4){
            v.setBackgroundResource(R.drawable.food_type4);
        }
        else if( i==5){
            v.setBackgroundResource(R.drawable.food_type5);
        }
        else if(i==6){
            v.setBackgroundResource(R.drawable.food_type6);
        }
    }
    String[] str1=new String[]{"川菜","北京菜","江浙菜","粤菜","湘菜","新疆菜","西北菜","云南菜","贵州菜","鲁菜","湖北菜","东北菜"};
    String[] str2=new String[]{"日本菜","海鲜"};
    String[] str3=new String[]{"烧烤","火锅","韩国料理","东南亚菜"};
    String[] str4=new String[]{"西餐","咖啡","面包甜点"};
    String[] str5=new String[]{"素菜","清真菜","家常菜"};
    String[] str6=new String[]{"自助餐","小吃快餐"};

    private   int   type(String foodname){
        if(checkIsHave(foodname,str1)) return 1 ;
        else if(checkIsHave(foodname,str2)) return 2 ;
        else if(checkIsHave(foodname,str3)) return 3 ;
        else if(checkIsHave(foodname,str4)) return 4 ;
        else if(checkIsHave(foodname,str5)) return 5 ;
        else if(checkIsHave(foodname,str6)) return 6 ;
        return 0;

    }
    private boolean   checkIsHave(String temp,String[] str){
        for (int i=0;i<str.length;i++){
            if(str[i].equals(temp))
                return true;
        }
        return  false;
    }


    /**
     *
     * @param followId (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private   void checkIsAttion(String followId, String followState , final SYRelationTalentModel model){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
        mcontext.post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if(response==null) return;
                if("success".equals(response.getResult())){
                    String AttionState=response.getAttentionState();
//                    if("01".equals(AttionState)){//我关注
//                        model.getUser().setByFollowMe(true);
//                        dataManager.deleteData(model);
//                        model.getUser().setFollowMe(false);
//                    }else if("00".equals(AttionState)){ //取消关注
//                        model.getUser().setByFollowMe(false);
//                        dataManager.addData(model);
//                    }
//                    notifyDataSetChanged();
                }else  {
                    mcontext.context().showToast(response.getReturnmessage());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"followId",followId,"followState",followState);
    }


    class SocialDataManager<T> extends Object{
        private class SYTempRelationTalentModel extends Object{
            public int index = -1;
            public SYRelationTalentModel model;
        }
        private  int position = -1;

        /**
         * 网络数据集合
         */
        private ArrayList<SYTempRelationTalentModel> dataList;

        /**
         * 已关注的集合
         */
        private ArrayList<SYTempRelationTalentModel> deleteList;

        public SocialDataManager(){
            position = -1;
            dataList = new ArrayList<SYTempRelationTalentModel>();
            deleteList = new ArrayList<SYTempRelationTalentModel>();
        }

        public void setDataList(ArrayList<SYRelationTalentModel> list){
            if(list == null){
                return;
            }

            for (int index = 0; index < list.size(); index++){
                SYRelationTalentModel talentModel = list.get(index);
                SYTempRelationTalentModel tempModel = new SYTempRelationTalentModel();
                tempModel.index = index;
                tempModel.model = talentModel;
                dataList.add(tempModel);
            }
        }

        /**
         * 点击关注调用
         * @param model
         */
        public void deleteData(SYRelationTalentModel model){
            if(model == null)
                return;
            for (int index = 0; index < dataList.size(); index++){
                SYTempRelationTalentModel talentModel = dataList.get(index);
                if(talentModel.model.getUser().getId().equals(model.getUser().getId())){
                    dataList.remove(talentModel);
                    deleteList.add(talentModel);
                    if(index <= position)
                        position --;
                    break;
                }
            }
        }

        /**
         * 取消关注调用
         * @param model
         */
        public void addData(SYRelationTalentModel model){
            if(model == null)
                return;
            for (int index = 0; index < deleteList.size(); index++){
                SYTempRelationTalentModel delTalentModel = deleteList.get(index);
                if(delTalentModel.model.getUser().getId().equals(model.getUser().getId())){
                    deleteList.remove(delTalentModel);

                    int jIndex = 0;
                    for(; jIndex < dataList.size();jIndex++){
                        SYTempRelationTalentModel talentModel = dataList.get(jIndex);
                        if(talentModel.index > delTalentModel.index){
                            dataList.add(jIndex,delTalentModel);
                            if(jIndex - 1 <= position)
                                position ++;
                            break;
                        }
                    }
                    if(jIndex >= dataList.size()){
                        dataList.add(delTalentModel);
                        if(jIndex <= position)
                            position ++;
                    }
                    break;
                }
            }
        }


        public ArrayList<SYRelationTalentModel> getNextList() {
            ArrayList<SYRelationTalentModel> list = new ArrayList<SYRelationTalentModel>();
            if(position + 1 >= dataList.size())
                position = -1;
            for(int index = 1; index <= 4; index++){
                if(position >= dataList.size()) {
                    break;
                }
                position ++;
                if(position < dataList.size()) {
                    SYTempRelationTalentModel model = dataList.get(position);
                    list.add(model.model);
                }
            }
            return list;
        }
    }

}
