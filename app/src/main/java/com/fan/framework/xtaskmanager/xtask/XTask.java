package com.fan.framework.xtaskmanager.xtask;

import android.util.Log;

import com.fan.framework.base.XData;

/**
 * 基础任务对象
 */
public abstract class XTask extends XData {

    /**
     * 默认优先级
     */
    public final static int XTaskPriorityNone = 0;

    /**
     * 最低优先级 默认
     */
    public final static int XTaskPriorityLow = XTaskPriorityNone;

    /**
     * 中优先级
     */
    public final static int XTaskPriotityMedium = XTaskPriorityLow + 1;

    /**
     * 高优先级
     */
    public final static int XTaskPriotytyHigh = XTaskPriotityMedium + 1;

    /**
     * 准备状态
     */
    public final static int XTaskExecutStateReady = 0;

    /**
     * 等待图片上传完成再执行状态
     */
    public final static int XTaskExecutStateWaitForImages = XTaskExecutStateReady + 1;

    /**
     * 执行中状态
     */
    public final static int XTaskExecutStateExecing = XTaskExecutStateWaitForImages + 1;

    /**
     * 执行完成状态
     */
    public final static int XTaskExecutStateComplete = XTaskExecutStateExecing + 1;

    /**
     * 取消执行状态
     */
    public final static int XTaskExecutStateCancel = XTaskExecutStateComplete + 1;

    /**
     * 执行失败状态
     */
    public final static int XTaskExecutStateFail = XTaskExecutStateCancel + 1;

    /**
     * 任务是否取消
     */
    public boolean bCanceled;

    /**
     * 任务进度
     */
    public float taskProgress;

//    /**
//     * 任务代理
//     */
//    @DatabaseField
//    public XTaskDelegate delegate;

    /**
     * 任务处理结果是否在主线程回调
     */
    public boolean bMainThreadCallBack;

    /**
     * 任务优先级
     */
    public int taskPriority;

    /**
     * 任务执行状态
     */
    public int taskExecutState;

    private final Object taskLockObj = new Object();

    public XTask() {
        bCanceled = false;
        taskProgress = 0;
        bMainThreadCallBack = false;
        taskPriority = XTaskPriotityMedium;
        taskExecutState = XTaskExecutStateReady;
    }

    public void setbCanceled(boolean canceled) {
        synchronized (taskLockObj) {
            bCanceled = canceled;
        }
    }

    public boolean isbCanceled() {
        synchronized (taskLockObj) {
            return bCanceled;
        }
    }

    public void setTaskExecutState(Integer executState) {
        synchronized (taskLockObj) {
            taskExecutState = executState;
        }
    }

    public int getTaskExecutState() {
        synchronized (taskLockObj) {
            return taskExecutState;
        }
    }

    public void setTaskPriority(Integer priority) {
        synchronized (taskLockObj) {
            taskPriority = priority;
        }
    }

    public float getTaskProgress() {
        synchronized (taskLockObj) {
            return taskProgress;
        }
    }

    public void setTaskProgress(float progress) {
        synchronized (taskLockObj) {
            taskProgress = progress;
        }
    }

    public int getTaskPriority() {
        synchronized (taskLockObj) {
            return taskPriority;
        }
    }

    /**
     * 任务执行器
     */
    public void taskExecutor() {
        synchronized (this) {
            setTaskExecutState(XTaskExecutStateExecing);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 任务执行器执行结束，程序员忽略该方法
     */
    public void taskExecytorEnd() {
        synchronized (this) {
            notify();
        }
    }

    /**
     * 任务请求结果回调
     */
    public void taskResultCallBack() {
        Log.d("XTask", "任务结果回调");
    }

    /**
     * 任务取消
     */
    public void taskCancel() {
        synchronized (this) {
            setbCanceled(true);
            setTaskExecutState(XTaskExecutStateCancel);
            notify();
            Log.d("XTask", String.format("任务%@取消执行", this.getId()));
        }
    }

    /**
     * 判断任务的合法性
     */
    public boolean isValidataTask() {
        return true;
    }

    public boolean isFinish() {
        return isbCanceled() == false &&
                getTaskExecutState() == XTaskExecutStateComplete;
    }
}
