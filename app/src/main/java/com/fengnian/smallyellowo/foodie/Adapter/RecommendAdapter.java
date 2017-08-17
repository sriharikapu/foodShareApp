package com.fengnian.smallyellowo.foodie.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.RecommendComment;
import com.fengnian.smallyellowo.foodie.bean.publics.RecommendPeople2;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.contact.RecommendActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/19.
 */
public class RecommendAdapter extends BaseAdapter {

   public  List<RecommendPeople2.RecommendUser> list;
    public RecommendActivity context2;

    public ArrayList<SYUser> commentuser;
    private ListView rl_commend_select;

    public RecommendAdapter(List<RecommendPeople2.RecommendUser> list,RecommendActivity activity) {
        this.list = list;
        this.context2=activity;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.item_recommend, null);
            holder = new ViewHolder();
            holder.tv_nikename = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_commend = (TextView) view.findViewById(R.id.tv_time);
            holder.img_publish_status = (ImageView) view.findViewById(R.id.img_publish_status);
            holder.rl_recommend = (RelativeLayout) view.findViewById(R.id.rl_recommend);
            holder.iv_add_crown= (ImageView) view.findViewById(R.id.iv_add_crown);
            holder.iv_icon = (ImageView) view.findViewById(R.id.iv_img);
            holder.rl_5 = (RelativeLayout) view.findViewById(R.id.rl_5);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Context context = view.getContext();
        RecommendPeople2.RecommendUser recommendUser=  list.get(i);
        int readStatu = recommendUser.getReadStatu();
        if (readStatu==0){
            holder.rl_recommend.setBackgroundResource(R.color.myfans_now_attion);
        }else{
            holder.rl_recommend.setBackgroundResource(R.color.white_bg);
        }
        final SYUser user = recommendUser.getUser();
        holder.tv_nikename.setText(user.getNickName());
        holder.tv_commend.setText(recommendUser.getCommonFriends()+"");
        IsAddCrownUtils.checkIsAddCrow(user,holder.iv_add_crown);
        FFImageLoader.loadAvatar((FFContext) context,user.getHeadImage().getUrl(), holder.iv_icon);
        holder.img_publish_status.setBackgroundResource(user.isByFollowMe()?R.mipmap.norecommend_follow:R.mipmap.recommend_follow);
      /*  holder.rl_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(context2,"ninini",Toast.LENGTH_SHORT).show();
               UserInfoIntent intent = new UserInfoIntent();
            }
        });*/
        holder.img_publish_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String followId = user.isByFollowMe() ? "0" : "1";
                String followId = user.getId();
                String  followState = user.isByFollowMe()?"0":"1";
                //Toast.makeText(context2,"ninini",Toast.LENGTH_SHORT).show();

                if(followState.equals("0"))  return;
                FFExtraParams extra = new FFExtraParams();
                extra.setDoCache(true);
                extra.setSynchronized(false);
                extra.setKeepWhenActivityFinished(false);
                extra.setProgressDialogcancelAble(true);
                //?flag=2
//                context2.post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNot.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
                context2.post(IUrlUtils.UserCenter.attentionOrNot, "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
                    @Override
                    public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                        if(response==null) return;
                        if("success".equals(response.getResult())){
                            String AttionState=response.getAttentionState();
                            if("01".equals(AttionState)){//wo关注
                                user.setByFollowMe(true);

                            }else if("00".equals(AttionState)||"10".equals(AttionState)){//关注wo
                                user.setByFollowMe(false);

                            }else if("11".equals(AttionState)){ //相互关注
                                user.setByFollowMe(true);
                                user.setFollowMe(true);
                            }
                            notifyDataSetChanged();
                        }else  {
                            context2.context().showToast(response.getReturnmessage());
                        }
                    }
                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                },"followId",followId,"followState",followState);



                //Toast.makeText(context2,"wowowowow",Toast.LENGTH_SHORT).show();


            }
        });
        holder.rl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context2.post(Constants.shareConstants().getNetHeaderAdress() + "/recommend/commonUserList.do", "", null, new FFNetWorkCallBack<RecommendComment>() {
                context2.post(IUrlUtils.Search.commonUserList, "", null, new FFNetWorkCallBack<RecommendComment>() {
                    @Override
                    public void onSuccess(RecommendComment response, FFExtraParams extra) {
                        commentuser = response.getCommentuser();
//                        createWifiSelectDialog(commentuser);
                         showDialog1(commentuser);


                    }
                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                },"recommendUserId",user.getId());
            }
        });
        return view;
    }
   static class ViewHolder {
        ImageView iv_icon,iv_add_crown;
        TextView tv_nikename;
        TextView tv_commend;
        ImageView img_publish_status;
         RelativeLayout rl_recommend;
         public RelativeLayout rl_5;

    }





    private void createWifiSelectDialog(ArrayList<SYUser> data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context2,R.style.dialog);
        final AlertDialog ad = builder.create();
        View view = View.inflate(context2, R.layout.recomment_comment_user, null);
        view.setBackgroundColor(context2.getResources().getColor(R.color.logout_bg));
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        ListView rl_commend_select = (ListView) view.findViewById(R.id.rl_commend_select);
        ImageButton tv_select_exit = (ImageButton) view.findViewById(R.id.tv_select_exit);
        rl_commend_select.setAdapter(new CommendAdapter(data));
        tv_select_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });
        ad.setView(view, 0, 0, 0, 0);
        ad.show();
    }

    private void showDialog1(final ArrayList<SYUser> data) {
        final Dialog dialog = new Dialog(context2, R.style.dialog);
        View view = View.inflate(context2, R.layout.recomment_comment_user, null);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        TextView txt_cancel = (TextView) dialogWindow.findViewById(R.id.tv_title);
        ImageButton txt_sure = (ImageButton) dialogWindow.findViewById(R.id.tv_select_exit);
        rl_commend_select = (ListView) dialogWindow.findViewById(R.id.rl_commend_select);
        rl_commend_select.setAdapter(new CommendAdapter(data));
        txt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

        rl_commend_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoIntent intent = new UserInfoIntent();
                intent.setUser(data.get(position));
//                context2.startActivity(UserInfoActivity.class, intent);
                IsAddCrownUtils.ActivtyStartAct(data.get(position),intent,context2);
            }
        });
    }






}

