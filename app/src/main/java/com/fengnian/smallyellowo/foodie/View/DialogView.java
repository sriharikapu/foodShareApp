package com.fengnian.smallyellowo.foodie.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.Logindate;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.UIUtil;


/**
 * Created by Administrator on 2017-4-12.
 */

public class DialogView extends Dialog implements View.OnClickListener{
    private Context mcontext;

    private int  themeid;

    private BaseActivity mactivity;
   private SYUser  m_user;
    public DialogView(Context context, Activity activity, SYUser us)
    {
        this(context, R.style.dialog);
        this.mcontext=context;
        this.mactivity= (BaseActivity) activity;
        this.m_user=us;
    }

    public DialogView( Context mcontext,int themeResId) {
        super(mcontext, themeResId);
    }


    private ImageView iv_dismiss,iv_delete;
    private EditText  ed_remarkname;
    private TextView tv_commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_set_remarkname);

        iv_dismiss= (ImageView) findViewById(R.id.iv_dismiss);
        iv_dismiss.setOnClickListener(this);

        iv_delete= (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        ed_remarkname= (EditText) findViewById(R.id.ed_remarkname);


        ed_remarkname.setText(m_user.getRemarkName());
        ed_remarkname.setSelection(ed_remarkname.getText().length()); //光标移动到最后
        tv_commit= (TextView) findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_dismiss:
                this.dismiss();
                break;
            case R.id.tv_commit:
//                setUserInfo(ed_remarkname.getText().toString().trim());
                mBack.myBack(ed_remarkname.getText().toString().trim());
                break;
            case R.id.iv_delete:
                ed_remarkname.setText("");
                break;
        }
    }


    private void setUserInfo(final String remarkname) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        mactivity.post(IUrlUtils.Search.setTargetUserRemarkName, "", extra, new FFNetWorkCallBack<Logindate>() {
            @Override
            public void onSuccess(Logindate response, FFExtraParams extra) {
                if (response.judge()) {
                    m_user=response.getUser();
                    DialogView.this.dismiss();

                } else {
                   UIUtil.showToast(mcontext,response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "targetAccount",m_user.getId(), "remarkName",remarkname);
    }
    private MyCallOnclick mBack;

    public void setMyCallBack(MyCallOnclick mBack){
        this.mBack=mBack;
    }
    public interface MyCallOnclick{
        public void myBack( String str);
    }
}
