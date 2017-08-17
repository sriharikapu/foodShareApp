package com.fan.framework.xtaskmanager.xthreadpool.xqueue;

import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xtask.XTask;

/**
 * 基础任务队列
 */
public class XTaskQueue<T> extends XQueue<T> {

    /**
     * 任务线程超时时间，单位秒
     */
    private final Integer thread_Wait_Message_TimeOut = 3;

    private XTaskQueue(){
        super();
    }

    public static XTaskQueue createTaskQueue(){
        XTaskQueue taskQueue = new XTaskQueue();
        return taskQueue;
    }

    @Override
    public T getMember() {
        T member = null;
        synchronized (this){
            if(getQueue().size() > 0){
                member = getQueue().remove(0);
            }else{
                try {
                    wait(thread_Wait_Message_TimeOut * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return member;
    }

    @Override
    public void addMember(T member) {
        if(member == null)
            return;

        synchronized (this){
            if (member instanceof XTask){
                XTask newTask = (XTask)member;
                if(newTask.taskPriority == XTask.XTaskPriorityLow){
                    getQueue().add((T) newTask);
                }else if(newTask.taskPriority == XTask.XTaskPriotytyHigh){
                    getQueue().add(0, (T) newTask);
                }else {
                    Integer count = getQueue().size();
                    Integer index = 0;
                    for( ; index < count; index ++){
                        T data = getQueue().get(index);
                        if(data instanceof XTask){
                            XTask task = (XTask)data;
                            if(task.taskPriority < newTask.taskPriority){
                                break;
                            }
                        }
                    }
                    getQueue().add(index,(T) newTask);
                }
            }else if(member instanceof XData){
                getQueue().add(member);
            }
            notify();
        }
    }

    @Override
    public void removeAllMember() {
        synchronized (this){
            getQueue().clear();
        }
    }

    @Override
    public void removeMember(T member) {
        if(member == null)
            return;
        synchronized (this){
            Integer size = getQueue().size();
            if(size > 0){
                for (Integer index = 0; index < size; index ++){
                    T oldMember = getQueue().get(index);
                    if(oldMember instanceof XData &&
                            member instanceof XData){
                        XData oldData = (XData) oldMember;
                        XData data = (XData) member;
                        if(oldData.isEqual(data)){
                            getQueue().remove(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Integer count() {
        synchronized (this){
            return getQueue().size();
        }
    }
}
