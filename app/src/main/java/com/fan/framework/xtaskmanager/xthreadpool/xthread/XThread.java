package com.fan.framework.xtaskmanager.xthreadpool.xthread;

import android.util.Log;

import com.fan.framework.base.XData;

/**
 * 任务调度线程池中的线程抽象对象
 */
public abstract class XThread extends XData implements Runnable {

    /**
     * 默认线程工作状态
     */
    public static final Integer X_Thread_Execute_State_None = 0;
    /**
     * 正在执行
     */
    public static final Integer X_Thread_Execute_State_Execute = X_Thread_Execute_State_None;

    /**
     * 暂停状态
     */
    public static final Integer X_Thread_Execute_State_Pause = X_Thread_Execute_State_Execute + 1;

    /**
     * 停止状态
     */
    public static final Integer X_Thread_Execute_State_Stop = X_Thread_Execute_State_Pause + 1;

    /**
     * 未知的状态
     */
    public static final Integer X_THREAD_EXECUTE_STATE_UNKOWN = X_Thread_Execute_State_Stop + 1;

    /**
     * 线程当前执行状态
     */
    private Integer threadExecuteState;

    /**
     * 线程执行状态同步锁
     */
    private final Object threadStateLock = new Object();

    public XThread(){
        super();
        setThreadExecuteState(X_Thread_Execute_State_None);
    }

    public abstract void executeBlock();

    public boolean pauseThread(){
        if(getThreadExecuteState() == X_Thread_Execute_State_Pause){
            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (getThreadExecuteState() != X_Thread_Execute_State_Stop &&
                !pauseThread() &&
                getThreadExecuteState() != X_Thread_Execute_State_Stop){
            executeBlock();
        }
        Log.d("XThread", "run: 任务线程执行结束生命周期");
    }

    public boolean isEqual(XThread otherThread){
        if(getId().equals(otherThread.getId()))
            return true;
        else
            return false;
    }

    public void setThreadExecuteState(Integer threadState){
        synchronized (threadStateLock) {
            threadExecuteState = threadState;
            if(threadExecuteState != X_Thread_Execute_State_Pause){
                synchronized (this){
                    notify();
                }
            }
        }
    }

    public Integer getThreadExecuteState(){
        synchronized (threadStateLock) {
            return threadExecuteState;
        }
    }
}
