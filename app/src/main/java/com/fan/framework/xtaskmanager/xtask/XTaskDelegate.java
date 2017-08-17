package com.fan.framework.xtaskmanager.xtask;

/**
 * 任务回调代理接口
 */
public interface XTaskDelegate {

    /**
     * 即将新添加的任务
     * @param task 待添加的任务对象
     */
    void willAddTask(XTask task);

    /**
     * 即将移除旧的任务
     * @param task 待移除的任务对象
     */
    void willRemoveTask(XTask task);

    /**
     * 即将开始执行任务
     * @param task 待执行的任务对象
     */
    void willStartTask(XTask task);

    /**
     * 任务请求失败，即将重试
     * @param task 待重试的任务对象
     */
    void willRetryTask(XTask task);

    /**
     * 任务请求进度
     * @param task  任务对象
     * @param progress 当前请求进度
     * @param totalProgress 总的进度大小
     */
    void execWithTask(XTask task, long progress, long totalProgress);

    /**
     * 任务完成回调
     * @param task 任务对象
     * @param responseString 回调元数据
     * @param bError true任务出错 否则正常
     */
    void completeDidTask(XTask task,String responseString,boolean bError);
}
