package com.fan.framework.xtaskmanager;

import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fan.framework.xtaskmanager.xthreadpool.XThreadPool;
import com.fengnian.smallyellowo.foodie.appbase.APP;

/**
 * Created by lanbiao on 16/10/9.
 */

public class XTaskManager extends XData {
    private XThreadPool threadPool;

    private static XTaskManager taskManager = null;

    private XTaskManager() {

    }

    public synchronized static XTaskManager taskManagerWithTask(XTask task) {
        if (taskManager == null) {
            taskManager = new XTaskManager();
        }

        if (task == null)
            return taskManager;

        if (task.getTaskExecutState() == XTask.XTaskExecutStateReady ||
                task.getTaskExecutState() == XTask.XTaskExecutStateFail) {
            task.setTaskExecutState(XTask.XTaskExecutStateExecing);
            taskManager.startTaskService();
            taskManager.addTask(task);
        } else {
        }

        return taskManager;
    }

    private void addTask(XTask task) {
        threadPool.addTask(task);
    }

    public void startTaskService() {
        if (threadPool == null) {
            threadPool = new XThreadPool();
            threadPool.startThreadPool();
        }
    }

    public void stopTaskService() {
        if (threadPool != null) {
            threadPool.stopThreadPool();
            threadPool = null;
        }
    }

    public void executeTaskService() {
        if (threadPool != null) {
            threadPool.executeThreadPool();
        }
    }

    public void pauseTaskService() {
        if (threadPool != null) {
            threadPool.pauseThreadPool();
        }
    }
}
