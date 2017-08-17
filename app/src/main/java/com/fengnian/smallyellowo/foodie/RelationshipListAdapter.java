package com.fengnian.smallyellowo.foodie;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.fan.framework.base.FFBaseAdapter;
import com.fan.framework.base.FFBaseAdapterDelegate;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.FastSection;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.RelationShipSortUser;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.fragments.RelationshipFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lanbiao on 16/8/4.
 */
public class RelationshipListAdapter extends FFBaseAdapter implements FastSection, FFBaseAdapterDelegate {

    private final int search_view_type = 0;
    private final int my_fans_view_type = 1;
    private final int relation_view_type = 2;
    private final int view_types[] = {search_view_type, my_fans_view_type, relation_view_type};

    private List<RelationShipSortUser> relationShipSortUserList;
    private HashMap<String, Integer> indexMap = new HashMap<>();
    private   Handler mhandle;
    public RelationshipListAdapter(Class clazz, int layoutId, FFContext context) {
        super(clazz, layoutId, context);
        this.delegate = this;
    }

    public RelationshipListAdapter(Class clazz, int layoutId, List data, FFContext context, Handler handle) {
        super(clazz, layoutId, data, context);
        readyData();
        this.relationShipSortUserList = data;
        this.delegate = this;
        this.mhandle=handle;
    }

    @Override
    public void addData(List dataList) {
        super.addData(dataList);
        readyData();
    }

    private void readyData() {
        relationShipSortUserList = getDataList();
        for (int i = 0; i < relationShipSortUserList.size(); i++) {
            RelationShipSortUser relation = relationShipSortUserList.get(i);
            String key = relation.getKey();
            if (key == null || "".equals(key))
                continue;
            String section = key.toUpperCase().substring(0, 1);
            if (!indexMap.containsKey(section))
                indexMap.put(section, i);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getStartPositionOfSection(String section) {
        if (indexMap.containsKey(section))
            return indexMap.get(section) + 1;
        return -1;
    }

    @Override
    public void setViewData(Object holder, final int position, Object item) {
//        if (position == 1) {
//            if (holder != null && holder instanceof RelationshipFragment.MyFansItemViewHolder) {
//                RelationshipFragment.MyFansItemViewHolder viewHolder = (RelationshipFragment.MyFansItemViewHolder) holder;
//                if (RelationshipFragment.message_num > 0) {
//                    viewHolder.tv_num.setVisibility(View.VISIBLE);
//                    viewHolder.tv_num.setText(RelationshipFragment.message_num + "");
//                } else {
//                    viewHolder.tv_num.setVisibility(View.GONE);
//                }
//            }
//        }
        if (position >= 1) {
            for (int i = 0; i < relationShipSortUserList.size(); i++) {
                RelationShipSortUser relation = relationShipSortUserList.get(i);
                String key = relation.getKey();
                if (key == null || "".equals(key))
                    continue;
                String section = key.toUpperCase().substring(0, 1);
                if (!indexMap.containsKey(section))
                    indexMap.put(section, i);
            }

            if (holder != null && holder instanceof RelationshipFragment.RelationItemViewHolder) {
                RelationshipFragment.RelationItemViewHolder viewHolder = (RelationshipFragment.RelationItemViewHolder) holder;
                if (item != null && item instanceof RelationShipSortUser) {
                    final RelationShipSortUser userRelation = (RelationShipSortUser) item;
                    viewHolder.sectionNameView.setText(userRelation.getKey());
                    viewHolder.tv_name.setText(userRelation.getUser().getSearchNickName());
//                    viewHolder.tv_name.setText(userRelation.getUser().getNickName());

                    if(userRelation.getUser().getUserType()==0){
                        viewHolder.iv_add_crown.setVisibility(View.GONE);
                    }else{
                        viewHolder.iv_add_crown.setVisibility(View.VISIBLE);
                    }
                    FFImageLoader.loadAvatar(mcontext.context(), userRelation.getUser().getHeadImage().getUrl(), viewHolder.iv_header);
                    viewHolder.tv_content.setText(userRelation.getUser().getPersonalDeclaration());
                    final boolean is__follow_me = userRelation.getUser().isFollowMe();
                    final boolean is_by_follow_me = userRelation.getUser().isByFollowMe();
                    if (is__follow_me && is_by_follow_me) {
                        viewHolder.rl_1.setVisibility(View.VISIBLE);
                        viewHolder.iv_is_attion.setImageResource(R.mipmap.my_eacher_attion);
                        viewHolder.tv_is_attion.setText("相互关注");
                    } else if (!is__follow_me && is_by_follow_me) {
                        viewHolder.rl_1.setVisibility(View.VISIBLE);
                        viewHolder.iv_is_attion.setImageResource(R.mipmap.my_yi_ation_img);
                        viewHolder.tv_is_attion.setText("已关注");
                    } else {
                        viewHolder.rl_1.setVisibility(View.INVISIBLE);
                    }


                    if (position == 1) {
                        viewHolder.sectionNameView.setVisibility(View.VISIBLE);
                    } else if (position > 1) {
                        RelationShipSortUser userItem = (RelationShipSortUser) getItem(position - 1);
                        if (userRelation.getKey().toUpperCase().charAt(0) != userItem.getKey().toUpperCase().charAt(0)) {
                            viewHolder.sectionNameView.setVisibility(View.VISIBLE);
                        } else
                            viewHolder.sectionNameView.setVisibility(View.GONE);
                    }
                    viewHolder.rl_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new EnsureDialog.Builder((BaseActivity)mcontext)
//                        .setTitle("系统提示")//设置对话框标题

                                    .setMessage("确定要取消关注该用户!")//设置显示的内容

                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                        @Override

                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            dialog.dismiss();
                                            checkIsAttion(userRelation.getUser().getId(),"0",position,userRelation.getUser());
                                        }

                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override

                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    dialog.dismiss();
                                }

                            }).show();//在按键响应事件中显示此对话框

                        }
                    });

                }


            }
        }
    }

    @Override
    public int getItemCount() {
        return 1 + getDataList().size();
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        int newPosition = position - 1;
        if (newPosition >= 0 && newPosition <= getDataList().size() - 1) {
            item = getDataList().get(newPosition);
        }
        return item;
    }

    @Override
    public int getViewTypeCount() {
        return view_types.length;
    }

    @Override
    public int getItemViewType(int position) {
        int view_type = -1;
        if (position == 0) {
            view_type = search_view_type;
        }
//        else if (position == 1) {
//            view_type = my_fans_view_type;
//        }
        else {
            view_type = relation_view_type;
        }
        return view_type;
    }

    @Override
    public Class getClass(int position) {
        Class clazz = null;
        if (position == 0) {
            clazz = RelationshipFragment.SearchItemViewHolder.class;
        }
//        else if (position == 1) {
//            clazz = RelationshipFragment.MyFansItemViewHolder.class;
//        }
        else if (position >= 1) {
            clazz = RelationshipFragment.RelationItemViewHolder.class;
        }
        return clazz;
    }

    @Override
    public int getItemLayoutId(int position) {
        int layoutId = -1;
        if (position == 0) {
            layoutId = R.layout.relationship_search_item_not_edtex;
        }
//        else if (position == 1) {
//            layoutId = R.layout.relationship_myfans_item;
//        } else
        if (position >= 1) {
            layoutId = R.layout.relationship_list_item;
        }
        return layoutId;
    }


    public int getPositionForSelection(int selection) {
        for (int i = 0; i < relationShipSortUserList.size(); i++) {
            String Fpinyin = relationShipSortUserList.get(i).getKey();
            char first = Fpinyin.toUpperCase().charAt(0);
            if (first == selection) {
                return i;
            }
        }
        return -1;

    }

    /**
     *
     * @param followId (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private   void checkIsAttion(String followId,String followState ,int postion,final SYUser user){
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
                    if("01".equals(AttionState)){//wo关注
                        user.setByFollowMe(true);
                    }else if("00".equals(AttionState)){ //取消关注
                        user.setByFollowMe(false);
                    }
                    else if("10".equals(AttionState)){//关注wo
                        user.setByFollowMe(false);
                    }else if("11".equals(AttionState)){ //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                    }
                    Message msg = new Message();
                    msg.arg1=100086;
                    mhandle.sendMessage(msg);
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


}
