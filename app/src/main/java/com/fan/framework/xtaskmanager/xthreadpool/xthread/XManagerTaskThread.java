package com.fan.framework.xtaskmanager.xthreadpool.xthread;

import android.util.Log;

/**
 * Created by lanbiao on 16/10/9.
 */

public abstract class XManagerTaskThread extends XThread {

    private final long managerThreadWaitExecTimeOut = 3;

    private boolean bWaitExecLock(){
        synchronized (this){
            try {
                wait(managerThreadWaitExecTimeOut * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void run() {
        do {
            pauseThread();
            executeBlock();
            if(getThreadExecuteState() == X_Thread_Execute_State_Stop)
                break;
        }
        while (!bWaitExecLock());
        Log.d("XManagerTaskThread", String.format("run: 管理线程%@运行结束了", getId()));
    }
}
