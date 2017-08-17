package com.fengnian.smallyellowo.foodie.taskmanager.task;

import android.util.Log;

import com.fan.framework.xtaskmanager.xtask.XTask;

/**
 * Created by lanbiao on 16/10/9.
 */

public class TestTask extends XTask {
    @Override
    public void taskExecutor() {
        //super.taskExecutor();
        Log.d("TestTask", "taskExecutor: 我开始运行了哦。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    taskExecytorEnd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.taskExecutor();
        Log.d("TestTask", "taskExecutor: 我执行完了。");
    }

    @Override
    public void taskResultCallBack() {
        super.taskResultCallBack();
        Log.d("TestTask", "taskExecutor: 我回调了。^_^");
    }
}
