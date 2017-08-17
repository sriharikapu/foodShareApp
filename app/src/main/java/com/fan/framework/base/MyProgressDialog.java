/*
 * Copyright © 1999-2014 maidoumi, Inc. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fan.framework.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 类描述： 自定义对话框 类名称：MyDialog
 */
public class MyProgressDialog extends Dialog {

    private Animation mAnimation;
    private Animation mReverseAnimation;
    private static final HashMap<Activity, MyProgressDialog> map = new HashMap<>();

    private final ArrayList<DialogMessage> list = new ArrayList<DialogMessage>();
    private final Context context;
    private static int id = 0;

    private static class DialogMessage {
        CharSequence word;
        boolean cancelAble;
        int id;
        public long showTime;
        public int timeOutTime;
    }


    private MyProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        initDialog(context);
    }

    private MyProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initDialog(context);
    }

    private MyProgressDialog(Context context) {
        super(context, R.style.FFProgressDialogStyle);
        this.context = context;
        initDialog(context);
    }

    private void initDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ff_dialog_progress, null);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.ff_pop_menu_animation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 0.6f;
        window.setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public synchronized static int show(Activity activity, boolean cancelAble, CharSequence word, int timeOut) {
        MyProgressDialog dialog = map.get(activity);
        if (dialog == null) {
            try {
                dialog = new MyProgressDialog(activity);
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

            map.put(activity, dialog);
        }
        dialog.setMessage(word);
        DialogMessage msg = new DialogMessage();
        msg.cancelAble = cancelAble;
        msg.showTime = System.currentTimeMillis();
        msg.timeOutTime = timeOut;
        msg.id = id;
        id++;
        msg.word = word;
        dialog.list.add(msg);
        Log.e("FF", "id=" + id);
        return msg.id;
    }


    @Override
    public void onBackPressed() {
        onBackPressed(-1);
    }

    public synchronized void onBackPressed(final int id) {
        if (!list.isEmpty()) {
            int index = list.size() - 1;
            if (id != -1) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).id == id) {
                        index = i;
                        break;
                    }
                }
            }
            if (list.get(index).cancelAble || id != -1) {
                list.remove(index);
            }
        }
        if (list.isEmpty()) {
            try {
                dismiss();
            } catch (Exception e) {

            }
            timer.cancel();
            timer1.cancel();
            map.remove(context);
        } else {
            setMessage(list.get(list.size() - 1).word);
        }
    }

    Timer timer = new Timer();
    Timer timer1 = new Timer();

    public void show() {
        final ImageView iv = (ImageView) findViewById(R.id.iv_progress);
        iv.setImageResource(R.mipmap.progress1);
        timer.schedule(new TimerTask() {
            int[] res = new int[]{R.mipmap.progress1, R.mipmap.progress3, R.mipmap.progress5, R.mipmap.progress7, R.mipmap.progress9, R.mipmap.progress10};
            int i = 0;

            public void run() {
                APP.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(res[i]);
                        i++;
                        i %= res.length;
                    }
                });
            }
        }, 0, 150);
        timer1.schedule(new TimerTask() {
            public void run() {
                FFApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = list.size() - 1; i >= 0; i--) {
                            final long currentTime = System.currentTimeMillis();
                            if (currentTime - list.get(i).showTime > list.get(i).timeOutTime) {
                                onBackPressed(list.get(i).id);
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);
        super.show();

    }

    public void setMessage(CharSequence message) {
        FFApplication.showToast(null, message.toString());
        ((TextView) findViewById(R.id.tv_message)).setText("");
    }

    public synchronized static final void Dismiss(Activity activity, int id) {
        MyProgressDialog dialog = map.get(activity);
        if (dialog != null) {
            dialog.onBackPressed(id);
        }
    }
}
