package com.fan.framework.xtaskmanager.xthreadpool;

import android.util.Log;

import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fan.framework.xtaskmanager.xthreadpool.xqueue.XQueue;
import com.fan.framework.xtaskmanager.xthreadpool.xqueue.XTaskQueue;
import com.fan.framework.xtaskmanager.xthreadpool.xqueue.XThreadQueue;
import com.fan.framework.xtaskmanager.xthreadpool.xthread.XManagerTaskThread;
import com.fan.framework.xtaskmanager.xthreadpool.xthread.XThread;

/**
 * Created by lanbiao on 16/10/9.
 */

public class XThreadPool extends XData {

    /**
     * 管理线程
     */
    private XThread managerThread;

    /**
     * 任务队列
     */
    private XQueue<XTask> taskQueue;

    /**
     * 线程队列
     */
    private XThreadQueue threadQueue;

    /**
     * 最大线程个数(注:任务线程数)
     */
    private Integer maxThreadNum;

    /**
     * 最小的线程个数(注:任务线程数)
     */
    private Integer minThreadNum;

    /**
     * 线程个数数据源锁对象
     */
    private final Object threadNumLock = new Object();

    /**
     * 默认的最大任务线程数
     */
    private final Integer Permanent_Thread_Number = 3;

    private final Integer InvalidManagerThreadRunCount = 3;

    private static Integer invalidRun = 0;

    public XThreadPool(){
        this.maxThreadNum = Permanent_Thread_Number;
        this.minThreadNum = 0;
        taskQueue = XTaskQueue.createTaskQueue();
        threadQueue = XThreadQueue.createThreadQueue();
    }

    public void addTask(XTask task){
        taskQueue.addMember(task);
        managerThread.setThreadExecuteState(XThread.X_Thread_Execute_State_Execute);
    }

    public void removeTask(XTask task){
        taskQueue.removeMember(task);
    }

    public void removeAllTask(){
        taskQueue.removeAllMember();
    }

    public XTask firstTask(){
        return taskQueue.getMember();
    }

    public Integer count(){
        return taskQueue.count();
    }



    public void setMaxThreadNum(Integer maxThreadNum1){
        if(maxThreadNum1 <= 0)
            return;

        synchronized (threadNumLock){
            if(maxThreadNum1 >= minThreadNum){
                maxThreadNum = maxThreadNum1;
            }
        }
    }

    public Integer getMaxThreadNum(){
        synchronized (threadNumLock){
            return maxThreadNum;
        }
    }

    public void setMinThreadNum(Integer minThreadNum1){
        if(minThreadNum1 <= 0)
            return;

        synchronized (threadNumLock){
            if(minThreadNum1 <= maxThreadNum){
                minThreadNum = minThreadNum1;
            }
        }
    }

    public Integer getMinThreadNum(){
        synchronized (threadNumLock){
            return minThreadNum;
        }
    }







    public void startThreadPool(){
        stopThreadPool();
        createManagerThread();
    }

    public void stopThreadPool(){
        if(managerThread != null)
            managerThread.setThreadExecuteState(XThread.X_Thread_Execute_State_Stop);
    }

    public void pauseThreadPool(){
        if(managerThread != null)
            managerThread.setThreadExecuteState(XThread.X_Thread_Execute_State_Pause);
    }

    public void executeThreadPool(){
        if(managerThread != null)
             managerThread.setThreadExecuteState(XThread.X_Thread_Execute_State_Execute);
    }





    private void stopTaskThread(boolean bStopAll){
        if(bStopAll){
            invalidRun = 0;
            threadQueue.removeAllTaskThread();
            Log.d("XThreadPool", "stopTaskThread: 管理线程奉命关闭所有任务线程");
        }else {
            if (threadQueue.taskThreadCount() > getMinThreadNum()) {
                invalidRun = 0;
                threadQueue.removeTaskThread();
                Log.d("XThreadPool", "stopTaskThread: 管理线程奉命关闭一个任务线程");
            }else {
                invalidRun++;
                if(invalidRun >= InvalidManagerThreadRunCount){
                    invalidRun = 0;
                    managerThread.setThreadExecuteState(XThread.X_Thread_Execute_State_Pause);
                    Log.d("XThreadPool", "stopTaskThread: 管理线程进入休眠状态");
                }
            }
        }
    }

    private void pauseTaskThread(boolean bPauseAll){
        if(bPauseAll){
            threadQueue.pauseAllTaskThread();
        }else {
            threadQueue.pauseTaskThread();
        }
    }

    private void executeTaskThread(boolean bExecuteAll){
        if(bExecuteAll){
            threadQueue.executeAllTaskThread();
        }else{
            threadQueue.executeTaskThread();
        }
    }

    private void addTaskThread(){
        if(getMaxThreadNum() > threadQueue.taskThreadCount()){
            XThread taskThread = new XThread() {
                @Override
                public void executeBlock() {
                    XTask task = taskQueue.getMember();
                    if(task != null){
                        task.taskExecutor();
                        task.taskResultCallBack();
                    }
                }
            };
            threadQueue.addThread(taskThread);
            Log.d("Task", "addTaskThread: 奉命增加一个任务线程");
        }else {
            Log.d("Task", "addTaskThread: 任务线程以达到最大峰值");
        }
    }

    private void managerThreadProcess(){
        Integer taskThreadNum = threadQueue.taskThreadCount();
        double runRate = threadQueue.threadRunRate();
        if(taskQueue.count() > 0){
            if(runRate >= 1.0){
                addTaskThread();
            }else if(taskThreadNum > 0){
                executeTaskThread(false);
            }else {
                addTaskThread();
            }
        }
        else {
            stopTaskThread(false);
        }
    }

    public void createManagerThread(){
        managerThread = new XManagerTaskThread() {
            @Override
            public void executeBlock() {
                if(getThreadExecuteState() == X_Thread_Execute_State_Stop){
                    stopTaskThread(true);
                }else if(getThreadExecuteState() == X_Thread_Execute_State_Pause){
                    pauseTaskThread(true);
                }else if(getThreadExecuteState() == X_Thread_Execute_State_Execute ||
                        getThreadExecuteState() == X_THREAD_EXECUTE_STATE_UNKOWN){
                    managerThreadProcess();
                }
            }
        };
        threadQueue.addThread(managerThread);
    }
}
