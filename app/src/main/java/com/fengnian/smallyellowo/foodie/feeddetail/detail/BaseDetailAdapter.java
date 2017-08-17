package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bigpicture.BitPictureIntent;
import com.fengnian.smallyellowo.foodie.bigpicture.ChatBigPictureActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.CommentUtil;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-2-21.
 */

public abstract class BaseDetailAdapter extends BaseAdapter {
    public static final int VIEW_TYPE_HEAD = 0;
    public static final int VIEW_TYPE_IMAGE = 1;
    public static final int VIEW_TYPE_TEXT = 2;
    protected final DynamicDetailActivity activity;
    private BaseDetailAdapterHelper mHelper;

    public BaseDetailAdapter(DynamicDetailActivity activity) {
        this.activity = activity;
        mHelper = new BaseDetailAdapterHelper(this);
    }

    public abstract BaseHeadViewHolder getHeadView();

    boolean isSupportClick() {
        return !activity.getIntentData().isPreview();
    }

    @Override
    public int getCount() {
        int count;
        if (activity.data != null) {
            int commentAndPrises = 0;
            if (!activity.getIntentData().isPreview()) {
                commentAndPrises = (FFUtils.isListEmpty(activity.data.getThumbsUps()) ? 0 : 1) +
                        (FFUtils.isListEmpty(activity.data.getSecondLevelcomments()) ? 0 : (!isUGC() ? 1 : activity.data.getSecondLevelcomments().size())) + 1;
            }
            count = getMCount() + commentAndPrises;
        } else {
            count = 0;
        }
        return count;
    }


    @Override
    public int getViewTypeCount() {
        return 4 + getMViewTypeCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= getMCount()) {
            if (position == getMCount() && !activity.getIntentData().isPreview()) {
                return 0;
            } else {//评论和赞
                if (position == getMCount() + 1 && !FFUtils.isListEmpty(activity.data.getThumbsUps())) {
                    return 1;
                }
                if (!isUGC()) {
                    return 3;
                }
                return 2;
            }
        } else {
            return 4 + getMItemViewType(position);
        }
    }

    private boolean isUGC() {
//                return isFrom(MainActivity.class, MainHomeUGCFragment.class);
        return false;
    }

    /**
     * 跳转到图片预览
     */
    public void toPicturePreview(SYImage currentSYImage) {
        ArrayList<BitPictureIntent.ImageMap> list = new ArrayList<>();

        int index = 0;
        int currentIndex = 0;
        for (SYRichTextPhotoModel txt : activity.data.getFood().getRichTextLists()) {
            if (!txt.isTextPhotoModel()) {
                continue;
            }
            SYImage imgg = txt.getPhoto().getImageAsset().pullProcessedImage().getImage();
            BitPictureIntent.ImageMap map = new BitPictureIntent.ImageMap();
            map.setPath(imgg.getUrl());
            map.setDishName(txt.getDishesName());
            list.add(map);
            if (currentSYImage != null) {
                if (imgg == currentSYImage) {
                    currentIndex = index;
                } else if (imgg.getUrl().equals(currentSYImage.getUrl())) {
                    currentIndex = index;
                }
            }
            index++;
        }
        BitPictureIntent intent = new BitPictureIntent();
        intent.setImages(list);
        intent.setIndex(currentIndex);
        activity.startActivity(ChatBigPictureActivity.class, intent);
    }

    /**
     * 设置评价的图片
     */
    public static void setCommentData(int commentLevel, ImageView imageView, final int type) {
        int[] iconArray = CommentLevel.ICON_STANDARD;
        if (type == CommentLevel.TYPE_BRIEF_2_2) {
            iconArray = CommentLevel.ICON_BRIEF_2_2;
        }

        switch (commentLevel) {
            case 0:
                imageView.setVisibility(View.INVISIBLE);
                imageView.setImageResource(iconArray[0]);
                break;
            case 1:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(iconArray[1]);
                break;
            case 2:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(iconArray[2]);
                break;
            case 3:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(iconArray[3]);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position < getMCount()) {
            return getMView(position, convertView, parent);
        }
        switch (getItemViewType(position)) {
            case 0: {//
                return activity.actionViewHolder1.getActionView();
            }
            case 1: {//赞列表
                LinearLayout ll;
                if (convertView == null) {
                    ll = (LinearLayout) (convertView = activity.getLayoutInflater().inflate(R.layout.view_dynamic_detail_prise_list, parent, false));
                } else {
                    ll = (LinearLayout) convertView;
                }
                int maxWidth = FFUtils.getDisWidth() - FFUtils.getPx(12 + 9 + 30);
                int perWidth = FFUtils.getPx(34);

                int count = activity.data.getThumbsUps().size();
                int maxCount = maxWidth / perWidth;
                if (count > maxCount){
                    count = maxCount;
                }
                while (ll.getChildCount() > 1) {
                    ll.removeViewAt(1);
                }

                for (int index = 0; index < count; index++) {
                    final SYUser prise = activity.data.getThumbsUps().get(index);
                    View userView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_prise, ll, false);
                    ImageView iv_img = (ImageView) userView.findViewById(R.id.iv_avator);
                    TextView tv_num = (TextView) userView.findViewById(R.id.tv_num);
                    ImageView ivCrown = (ImageView) userView.findViewById(R.id.iv_add_crown);
                    ivCrown.setVisibility(View.GONE);
                    mHelper.setPaiseUserImageLayoutParams(prise, iv_img, tv_num, ivCrown);

                    FFImageLoader.loadAvatar(activity, prise.getHeadImage().getUrl(), iv_img);
                    iv_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!activity.data.getUser().getId().equals(SP.getUid())) {
                                if (view.getTag(R.id.iv_img) != null){
                                    PriseUserListDialog dialog = new PriseUserListDialog(activity,
                                            activity.data.getId());
                                    dialog.show();
                                    return;
                                }

                                UserInfoIntent intent = new UserInfoIntent();
                                intent.setUser(prise);
//                                activity.startActivity(UserInfoActivity.class, intent);
                                IsAddCrownUtils.ActivtyStartAct(prise, intent, activity);
                            }
                        }
                    });

                    tv_num.setVisibility(View.GONE);
                    if (index == count - 1) {//最后一个了
                        if (count < activity.data.getThumbsUps().size() - 1) {//还有更多
                            iv_img.setTag(R.id.iv_img, "last");
                            int moreNum = activity.data.getThumbsUps().size() - 1 - index;
                            tv_num.setText(String.valueOf(moreNum));
                            tv_num.setVisibility(View.VISIBLE);
                        }
                    }
                    ll.addView(userView);
                }
                return convertView;
            }
            case 2: {//回复
//                        CommentsHolder holder;
//                        if (convertView == null) {
//                            convertView = getLayoutInflater().inflate(R.layout.item_dynamic_detail_comment, parent, false);
//                            holder = new CommentsHolder();
//                            holder.setView(convertView);
//                        } else {
//                            holder = (CommentsHolder) convertView.getTag();
//                        }
//                        final SYSecondLevelcomment item_image_gallery = data.getSecondLevelcomments().get(position - data.getFood().getRichTextLists().size() - 2 - (FFUtils.isListEmpty(data.getThumbsUps()) ? 0 : 1));
////                        FFImageLoader.loadAvatar(context(), item_image_gallery.getCommentUser().getHeadImage().getUrl(), holder.iv_avatar);
//                        holder.tv_content.setText(item_image_gallery.getFirstLevelComment().getCommentContent());
//                        if (item_image_gallery.getChildrenCommentsList() != null) {
//
//                            for (SYComment comm : item_image_gallery.getChildrenCommentsList()) {
//                                holder.tv_content.append("\n");
//                                HomeChildDynamicFrag.appendUser(holder.tv_content, comm.getCommentUser(), context(), null);
//                                if (comm.getCommentUser() != null) {
//                                    holder.tv_content.append(" 回复 ");
//                                    HomeChildDynamicFrag.appendUser(holder.tv_content, comm.getCommentUser(), context(), null);
//                                }
//                                holder.tv_content.append(" ");
//                                HomeChildDynamicFrag.appendContnet(holder.tv_content, comm, data, context(), new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                            }
//                        }
//                        holder.tv_name.setText(item_image_gallery.getFirstLevelComment().getCommentUser().getNickName());
//
//                        View.OnClickListener on = new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                SYUser user = item_image_gallery.getFirstLevelComment().getCommentUser();
//                                if (!user.getId().equals(SP.getUid())) {
//                                    UserInfoIntent userInfoIntent = new UserInfoIntent();
//                                    userInfoIntent.setUser(user);
//                                    startActivity(UserInfoActivity.class, userInfoIntent);
//                                } else {
//                                    finishAllActivitysByTag(ActivityTags.main);
//                                    MainActivity.toUser();
//                                }
//                            }
//                        };
//
//                        holder.tv_name.setOnClickListener(on);
//                        holder.iv_avatar.setOnClickListener(on);
//
//                        holder.tv_time.setText(ContextUtils.getFriendlyTime(item_image_gallery.getFirstLevelComment().getCreateTime(), false));
//                        holder.iv_comment.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                startActivity(CommentActivity.class, (CommentIntent) new CommentIntent().setFeed(data).setRequestCode(517));
//                                comment = item_image_gallery.getFirstLevelComment();
//                            }
//                        });
//
//                        convertView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (item_image_gallery.getFirstLevelComment().getCommentUser().getId().equals(SP.getUid())) {
//                                    EnsureDialog.showEnsureDialog(context(), true, "确定删除该条评论？", "确认", "取消", new EnsureDialog.EnsureDialogListener() {
//                                        @Override
//                                        public void onOk(DialogInterface dialog) {
//                                            dialog.dismiss();
//                                            post(Constants.shareConstants().getNetHeaderAdress() + "/comment/delCommentV210.do", null, null, new FFNetWorkCallBack<AddCommentResult>() {
//                                                @Override
//                                                public void onSuccess(AddCommentResult response, FFExtraParams extra) {
//                                                    data.setSecondLevelcomments(response.getSySecondLevelComments());
//                                                    notifyDataSetChanged();
//                                                }
//
//                                                @Override
//                                                public boolean onFail(FFExtraParams extra) {
//                                                    return false;
//                                                }
//                                            }, "commentId", item_image_gallery.getFirstLevelComment().getId());
//                                        }
//
//                                        @Override
//                                        public void onCancel(DialogInterface dialog) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    return;
//                                }
//                            }
//                        });
                return convertView;
            }
            case 3: {
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_comment_detail, parent, false);
                }
                final Runnable runn = new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                };

                CommentUtil.dooComment(activity.data, (LinearLayout) convertView, runn, activity, null);

                return convertView;
            }
        }
        return convertView;
    }

    /**
     * 获取关注/取关按钮底部的高度
     *
     * @return
     */
    public int getMAttentionBottomY() {
        return activity.s_status_bar.getLayoutParams().height + FFUtils.getPx(48);
    }

    @Override
    public abstract SYRichTextPhotoModel getItem(int position);

    public abstract View getMView(int position, View convertView, ViewGroup parent);

//    public abstract StandardHeadViewHolder getHeadView();

    public abstract int getMCount();

    public abstract int getMViewTypeCount();

    public abstract int getMItemViewType(int position);

    public int getActionIndex() {
        return getMCount();
    }

    public static class CommentLevel {
        public static int[] ICON_STANDARD = {0, R.drawable.ei_ic_good, R.drawable.ei_ic_normal, R.drawable.ei_ic_bad};
        public static int[] ICON_BRIEF_2_2 = {0, R.drawable.brief_ei_ic_good, R.drawable.brief_ei_ic_normal, R.drawable.brief_ei_ic_bad};
        public static int[] ICON_BRIEF = {0, R.drawable._brief_ei_ic_good, R.drawable._brief_ei_ic_normal, R.drawable._brief_ei_ic_bad};

        public static final int TYPE_STANDARD = 1;
        public static final int TYPE_BRIEF_2_2 = 2;
        public static final int TYPE_BRIEF = 3;
    }
}
