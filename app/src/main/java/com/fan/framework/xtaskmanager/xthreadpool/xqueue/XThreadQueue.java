package com.fan.framework.xtaskmanager.xthreadpool.xqueue;

import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xthreadpool.xthread.XManagerTaskThread;
import com.fan.framework.xtaskmanager.xthreadpool.xthread.XThread;

/**
 * 线程队列
 */
public class XThreadQueue extends XData {

    /**
     * 线程集合
     */
    private XQueue<XThread> queue;

    /**
     * 构造方法私有，防止被实例化
     */
    private XThreadQueue(){
        queue = new XQueue<XThread>();
    }

    /**
     * 创建实例对象
     * @return 返回实例对象
     */
    public static XThreadQueue createThreadQueue(){
        XThreadQueue threadQueue = new XThreadQueue();
        return threadQueue;
    }

    /**
     * 返回线程队列中的总的成员个数
     * @return 线程总的成员个数
     */
    public Integer count() {
        synchronized (this){
            return queue.count();
        }
    }

    private boolean isTaskThread(XThread thread){
        if(thread == null)
            return false;

        boolean bTaskThread = true;
        if(thread instanceof XManagerTaskThread)
            bTaskThread = false;
        return bTaskThread;
    }

    /**
     * 返回线程队列中任务线程个数
     * @return  任务线程个数
     */
    public Integer taskThreadCount(){
        Integer count = 0,index = 0;
        synchronized (this){
            for (; index < queue.count();index++){
                XThread thread = queue.getQueue().get(index);
                if(isTaskThread(thread) &&
                        thread.getThreadExecuteState() != XThread.X_Thread_Execute_State_Stop){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     *  当前线程池的执行效率，可根据结果判断是否有线程处于偷懒的状态，若有的话，就其中一个睡眠线程并打开。否则就需要添加一个新任务线程.
     *
     *  @return 处于0~1之间，0表示都处于睡眠状态(如果有任务线程的话，没有另说)，1表示使用率很高.
     */
    public double threadRunRate(){
        double rate = 0.0f;
        synchronized (this){
            Integer maxTaskThreadNum = 0,runTaskThreadNum = 0,index = 0;
            for (;index < queue.count();index++){
                XThread thread = queue.getQueue().get(index);
                if(isTaskThread(thread)){
                    maxTaskThreadNum ++;
                    if(thread.getThreadExecuteState() == XThread.X_Thread_Execute_State_Execute)
                        runTaskThreadNum ++;
                }
            }

            if(maxTaskThreadNum > 0)
                rate = (runTaskThreadNum * 1.0) / maxTaskThreadNum;
        }
        return rate;
    }

    /**
     * 添加线程到线程队列
     * @param thread 待添加的线程对象
     */
    public void addThread(XThread thread){
        if(thread == null)
            return;

        synchronized (this){
            boolean bExist = false;
            for(Integer index = 0; index < queue.count(); index++){
                XThread oldThread = queue.getQueue().get(index);
                if(oldThread.isEqual(thread)){
                    bExist = true;
                    break;
                }
            }

            if(bExist == false) {
                queue.addMember(thread);
                new Thread(thread).start();
            }
        }
    }

    /**
     * 关闭线程队列中的所有任务线程，管理线程和处于停止状态的任务线程忽略该操作
     */
    public void removeAllTaskThread(){
        synchronized (this){
            for(Integer index = queue.count() - 1; index >= 0 ; index--){
                XThread thread = queue.getQueue().get(index);
                if(isTaskThread(thread)){
                    thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Stop);
                    queue.removeMember(thread);
                }
            }
        }
    }

    /**
     * 关闭一个任务线程，管理线程和处于停止状态的线程忽略该操作
     */
    public void removeTaskThread(){
        synchronized (this){
            XThread thread = null;
            for (Integer index = 0; index < queue.count(); index++){
                XThread trd = queue.getQueue().get(index);
                if(isTaskThread(trd)){
                    thread = trd;
                    if(trd.getThreadExecuteState() != XThread.X_Thread_Execute_State_Stop &&
                            trd.getThreadExecuteState() != XThread.X_Thread_Execute_State_Execute) {
                        break;
                    }
                }
            }

            if(thread != null) {
                thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Stop);
                queue.removeMember(thread);
            }
        }
    }

    /**
     * 暂停所有任务线程，管理线程和处于停止状态的线程忽略该操作
     */
    public void pauseAllTaskThread(){
        synchronized (this){
            for(Integer index = 0; index < queue.count(); index ++){
                XThread thread = queue.getQueue().get(index);
                if(isTaskThread(thread)){
                    thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Pause);
                }
            }
        }
    }

    /**
     * 暂停一个任务线程，管理线程和处于停止状态的线程忽略该操作
     */
    public void pauseTaskThread(){
        synchronized (this){
            XThread thread = null;
            for(Integer index = 0; index < queue.count(); index ++){
                XThread trd = queue.getQueue().get(index);
                if(isTaskThread(trd)){
                    if(trd.getThreadExecuteState() != XThread.X_Thread_Execute_State_Pause){
                        thread = trd;
                        break;
                    }
                }
            }

            if(thread != null){
                thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Pause);
            }
        }
    }

    /**
     * 唤醒正在休眠的所有任务线程，已是执行状态的任务线程忽略该操作
     */
    public void executeAllTaskThread(){
        synchronized (this){
            for(Integer index = 0; index < queue.count(); index ++){
                XThread thread = queue.getQueue().get(index);
                if(isTaskThread(thread)){
                    thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Execute);
                }
            }
        }
    }

    /**
     * 唤醒正在休眠的某个任务线程，已是执行状态的任务线程忽略该操作
     */
    public void executeTaskThread(){
        synchronized (this){
            XThread thread = null;
            for(Integer index = 0; index < queue.count(); index++){
                XThread trd = queue.getQueue().get(index);
                if(isTaskThread(trd)){
                    if(trd.getThreadExecuteState() != XThread.X_Thread_Execute_State_Execute){
                        thread = trd;
                        break;
                    }
                }
            }

            if(thread != null){
                thread.setThreadExecuteState(XThread.X_Thread_Execute_State_Execute);
            }
        }
    }
}
