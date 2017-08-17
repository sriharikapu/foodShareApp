package com.fengnian.smallyellowo.foodie.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYSecondLevelcomment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.AddCommentResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.HomeChildDynamicFrag;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;

import java.util.ArrayList;
import java.util.HashMap;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by Administrator on 2016-11-15.
 */

public class CommentUtil {
    private static class Holder {
        View view;
        TextView tv_name;
        TextView tv_content;
        View line_v;

        public Holder(View item) {
            view = item;
            tv_name = (TextView) item.findViewById(R.id.tv_comment_first_name);
            tv_content = (TextView) item.findViewById(R.id.tv_comment_first_name_content);
            line_v = item.findViewById(R.id.line_v);
        }
    }


    private final static HashMap<String, SparseArray<ArrayList<View>>> list = new HashMap<>();

    private static final Holder getFirstText(Activity activity, SparseArray<ArrayList<View>> arr) {
        ArrayList<View> li = arr.get(R.id.comment_first);
        if (li.isEmpty()) {
            View item = activity.getLayoutInflater().inflate(R.layout.item_comment_first, null, false);
            Holder holder = new Holder(item);
            item.setTag(holder);
            return holder;
        }
        return (Holder) li.remove(0).getTag();
    }

    private static final TextView getSecondText(Activity activity, SparseArray<ArrayList<View>> arr) {
        ArrayList<View> li = arr.get(R.id.comment_second);
        if (li.isEmpty()) {
            return (TextView) activity.getLayoutInflater().inflate(R.layout.item_comment_second, null, false);
        }
        return (TextView) li.remove(0);
    }


    public static void dooComment(final SYFeed data, LinearLayout ll, final Runnable runn, final Activity activity, final BaseFragment fragment) {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        String key = activity.toString() + activity.hashCode() + (fragment == null ? "" : (fragment.toString() + fragment.hashCode()));
        SparseArray<ArrayList<View>> arr = list.get(key);
        if (arr == null) {
            arr = new SparseArray<>();
            list.put(key, arr);
            arr.put(R.id.comment_first, new ArrayList<View>());
            arr.put(R.id.comment_second, new ArrayList<View>());
        }

        while (ll.getChildCount() != 0) {
            View tv = ll.getChildAt(0);
            ArrayList<View> views = arr.get(tv.getId());
            if(views != null) {
                views.add(tv);
            }
            ll.removeViewAt(0);
        }

        for (int j = 0; j < data.getSecondLevelcomments().size(); j++) {
            if (data.getSecondLevelcomments().get(j).isDeleted() && FFUtils.isListEmpty(data.getSecondLevelcomments().get(j).getChildrenCommentsList())) {
                data.getSecondLevelcomments().remove(j);
                j--;
            }
        }
        int i = 0;
        for (final SYSecondLevelcomment comment : data.getSecondLevelcomments()) {
            final Holder firstText = getFirstText(activity, arr);
            if (i == 0) {
                firstText.line_v.setVisibility(View.GONE);
                i++;
            } else {
                firstText.line_v.setVisibility(View.VISIBLE);
            }
            ll.addView(firstText.view);
            firstText.tv_content.setText("");
            firstText.tv_name.setText(comment.getFirstLevelComment().getCommentUser().getNickName());
            firstText.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClick(comment.getFirstLevelComment().getCommentUser(), fragment, activity);
                }
            });
            if (!comment.isDeleted()) {
                firstText.tv_name.setVisibility(View.VISIBLE);
                firstText.tv_content.setText(comment.getFirstLevelComment().getCommentContent());
                firstText.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onContentClick(comment.getFirstLevelComment(), activity, firstText.tv_content, data, runn);
                    }
                });
            } else {
                firstText.tv_name.setVisibility(View.GONE);
                appendDel(firstText.tv_content);
            }

            if (!FFUtils.isListEmpty(comment.getChildrenCommentsList())) {
                for (SYComment comm : comment.getChildrenCommentsList()) {
                    TextView secondText = getSecondText(activity, arr);
                    secondText.setText("");
                    ll.addView(secondText);
                    appendUser(secondText, comm.getCommentUser(), activity, fragment);
                    if (comm.getReplyComment() != null) {
                        secondText.append(" 回复 ");
                        appendUser(secondText, comm.getReplyComment().getCommentUser(), activity, fragment);
                    }
                    appendContnet(secondText, comm, data, activity, runn);
                }
            }
        }
    }

    private static void appendDel(TextView tv) {
        String str = "该评论已删除";
        SpannableString spStr = new SpannableString(str);
        spStr.setSpan(new ForegroundColorSpan(tv.getResources().getColor(R.color.ff_text_gray)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spStr.setSpan(new BackgroundColorSpan(0xffdddddd), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spStr);
    }

    private static void appendUser(final TextView tv, final SYUser user, final Context context, final BaseFragment fragment) {
        SpannableString spStr = new SpannableString(user.getNickName());
        spStr.setSpan(new HomeChildDynamicFrag.TouchableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(tv.getResources().getColor(R.color.comment_name)); // 设置文件颜色
                ds.bgColor = 0;
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                onUserClick(user, fragment, context);
            }
        }, 0, user.getNickName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spStr);
        tv.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件
    }

    private static void onUserClick(SYUser user, BaseFragment fragment, Context context) {
        if (user.getId().equals(SP.getUid())) {
            if (fragment != null && fragment.getActivity() instanceof MainActivity) {
                ((MainActivity) fragment.getActivity()).rb_user.setChecked(true);
                return;
            }
            ((BaseActivity) context).finishAllActivitysByTag(ActivityTags.main);

            MainActivity.toUser();
            return;
        }
        UserInfoIntent intent = new UserInfoIntent();
        intent.setUser(user);
        if (fragment != null) {
            if (user.getUserType() == 0) {
                fragment.startActivity(UserInfoActivity.class, intent);
            } else {
                fragment.startActivity(ClubUserInfoActivity.class, intent);
            }
        } else {
            if (user.getUserType() == 0) {
                ((BaseActivity) context).startActivity(UserInfoActivity.class, intent);
            } else {
                ((BaseActivity) context).startActivity(ClubUserInfoActivity.class, intent);
            }
        }
    }

    private static void appendContnet(final TextView tv, final SYComment comment, final SYFeed feed, final Context context, final Runnable refresh) {
        String userName = ":" + comment.getCommentContent();
        SpannableString spStr = new SpannableString(userName);
        spStr.setSpan(new ForegroundColorSpan(tv.getContext().getResources().getColor(R.color.ff_text_black)), 0, userName.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        spStr.setSpan(new HomeChildDynamicFrag.TouchableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(tv.getContext().getResources().getColor(R.color.ff_text_black)); // 设置文件颜色
                ds.bgColor = 0;
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                onContentClick(comment, context, tv, feed, refresh);
            }
        }, 0, userName.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spStr);
        tv.setMovementMethod(LinkMovementMethod.getInstance());// 开始响应点击事件
    }

    private static void onContentClick(final SYComment comment, Context context, final TextView tv, final SYFeed feed, final Runnable refresh) {
        if (comment.getCommentUser().getId().equals(SP.getUid())) {
            EnsureDialog.showEnsureDialog(context, true, "确定删除该条评论？", "确认", null, "取消", new EnsureDialog.EnsureDialogListener() {
                @Override
                public void onOk(DialogInterface dialog) {
                    dialog.dismiss();
//                    ((FFContext) tv.getContext()).post(Constants.shareConstants().getNetHeaderAdress() + "/comment/delCommentV210.do", null, null, new FFNetWorkCallBack<AddCommentResult>() {
                    ((FFContext) tv.getContext()).post(IUrlUtils.Search.delCommentV210, null, null, new FFNetWorkCallBack<AddCommentResult>() {
                        @Override
                        public void onSuccess(AddCommentResult response, FFExtraParams extra) {
                            feed.setSecondLevelcomments(response.getSySecondLevelComments());
                            refresh.run();
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, "commentId", comment.getId());
                }

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            return;
        } else if (context instanceof MainActivity) {
            ((MainActivity) context).comment(feed, comment, refresh);
        } else if (context instanceof DynamicDetailActivity) {
            ((DynamicDetailActivity) context).comment(comment);
        } else if (context instanceof FastDetailActivity) {
            ((FastDetailActivity) context).comment(comment);
        }
    }
}
