package com.fengnian.smallyellowo.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiEditText;
import com.fengnian.smallyellowo.foodie.emoji.EmojiPanelLayout;
import com.fengnian.smallyellowo.foodie.emoji.EmojiUtils;
import com.fengnian.smallyellowo.foodie.intentdatas.CommentIntent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.fengnian.smallyellowo.foodie.R.id.et_comment;

public class CommentActivity extends BaseActivity<CommentIntent> {

    @Bind(et_comment)
    CustomEmojiEditText etComment;
    @Bind(R.id.rl_comment)
    RelativeLayout rlComment;
    @Bind(R.id.view)
    View view;
    EmojiPanelLayout emojiPanelLayout;
    @Bind(R.id.emoji_panel_show)
    ImageView emojiPanel;
    @Bind(R.id.content_view)
    View mContentView;
    @Bind(R.id.btn_comment)
    TextView mBtnComment;

    private final static HashMap<String, Object> drafts = new HashMap<String, Object>() {
        private void save() {
            getCommentDraftsSP().edit().putString("draft", JSON.toJSONString(this)).commit();
        }

        @Override
        public Object remove(Object key) {
            Object remove = super.remove(key);
            save();
            return remove;
        }

        @Override
        public Object put(String key, Object value) {
            Object put = super.put(key, value);
            save();
            return put;
        }
    };

    static {
        SharedPreferences sp = getCommentDraftsSP();
        String str = sp.getString("draft", null);
        if (str != null) {
            drafts.putAll(JSON.parseObject(str, HashMap.class));
        }
    }


    private static SharedPreferences getCommentDraftsSP() {
        return APP.app.getSharedPreferences("CommentDrafts", Context.MODE_PRIVATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setNotitle(true);
        getContainer().setBackgroundColor(0);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    mBtnComment.setBackgroundResource(R.drawable.login_selector);
                    mBtnComment.setTextColor(getResources().getColor(R.color.title_text_color));
                } else {
                    mBtnComment.setBackgroundResource(R.drawable.shape_input);
                    mBtnComment.setTextColor(getResources().getColor(R.color.gray_bg));
                }
            }
        };
        LinearLayout parent = (LinearLayout) getContainer().getParent().getParent();
        emojiPanelLayout = (EmojiPanelLayout) getLayoutInflater().inflate(R.layout.view_input_emoji, parent, false);
        emojiPanelLayout.setAttachedEmojiBtn(emojiPanel);
        etComment.addTextChangedListener(watcher);

        CommentIntent intentData = getIntentData();
        if (intentData != null) {
            SYComment comment = intentData.getComment();
            if (comment != null) {
                String hintText = "回复" + comment.getCommentUser().getNickName();
                etComment.setHint(hintText);
                Object remove = drafts.remove(comment.getId() + "feedId:" + (intentData.getFeed().getId()));
                etComment.setText((String) remove);
                if (remove != null)
                    etComment.setSelection(((String) remove).length());

            } else {
//                etComment.setText(drafts.remove("feedId:" + intentData.getFeed().getId()).toString());
                Object remove = drafts.remove("feedId:" + (intentData.getFeed().getId()));
                etComment.setText((String) remove);
                if (remove != null)
                    etComment.setSelection(((String) remove).length());
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        emojiPanelLayout.setOnSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        emojiPanelLayout.setOnKeyboardStateListener(new EmojiPanelLayout.onKeyboardStateListener() {
            @Override
            public void onRequestSoftVis() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) getContainer().getParent()).getLayoutParams();
                params.height = FFUtils.getDisHight() - FFUtils.getStatusbarHight(context()) - EmojiUtils.getEmojiKeyboardHeight();
                params.weight = 0;
            }

            @Override
            public void onKeyboardShow(boolean isShow) {
                if (!isShow && emojiPanelLayout.getLayoutParams().height == 0) {
                    ((View) getContainer().getParent()).invalidate();
                    ((View) getContainer().getParent()).requestLayout();
                    finish();
                }
            }

            @Override
            public void onFixLayoutParam(boolean isFix) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((View) getContainer().getParent()).getLayoutParams();
                if (isFix) {
                    params.height = FFUtils.getDisHight() - FFUtils.getStatusbarHight(context()) - EmojiUtils.getEmojiKeyboardHeight();
                    params.weight = 0;
                } else {
                    params.height = 0;
                    params.weight = 1;
                }
            }
        });

        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        emojiPanelLayout.setAttachedEditText(etComment);
        emojiPanelLayout.setAttachedEmojiBtn(emojiPanel);
        parent.addView(emojiPanelLayout, 1);
        etComment.setWhenBackKeyboardIsFinishActivity(true);
    }

    @Override
    public void finish() {
        super.finish();

        if (etComment.getText().length() != 0) {
            CommentIntent intentData = getIntentData();
            if (intentData != null) {
                SYComment comment = intentData.getComment();
                if (comment != null) {
                    drafts.put(comment.getId() + "feedId:" + intentData.getFeed().getId(), etComment.getText().toString());
                } else {
                    drafts.put("feedId:" + intentData.getFeed().getId(), etComment.getText().toString());
                }
            }
        }
    }

    private void sendMessage() {
        if (etComment.getText().toString().trim().length() == 0) {
            showToast("请输入评论内容！");
            return;
        }
        FFUtils.setSoftInputVis(etComment, false);
        setResult(RESULT_OK, new Intent().putExtra("comment", etComment.getText().toString()));
        etComment.setText("");
        finish();
    }

    @Override
    public void onSoftInputMethVis(int softInputHight) {
        super.onSoftInputMethVis(softInputHight);
        etComment.setSelection(etComment.getText().length());
    }

//
//    @Override
//    public void onSoftInputMethInvis(int softInputHight) {
//        finish();
//    }
}
