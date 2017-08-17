package com.fengnian.smallyellowo.foodie.datamanager;

import android.os.Parcel;

import com.fan.framework.base.FFDao;
import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fan.framework.xtaskmanager.xthreadpool.xqueue.XQueue;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-10-12.
 */

public class SYDataManager extends XData {

    /**
     * 单例对象
     */
    private static SYDataManager dataManager = null;

    /**
     * 本地任务队列,完全与数据表映射
     */
    private XQueue<PublishModel> taskQueues;

    /**
     * 任务同步锁对象
     */
    private final Object taskLock = new Object();

    /**
     * 草稿箱队列,完全与数据表映射
     */
    private XQueue<DraftModel> draftsQueues;

    /**
     * 草稿箱同步锁对象
     */
    private final Object draftsLock = new Object();

    /**
     * 不支持直接创建对象
     */
    private SYDataManager() {
        super();
        taskQueues = new XQueue<PublishModel>(){

            @Override
            public ArrayList<PublishModel> getQueue() {
                synchronized (queueLock) {
                    ArrayList<PublishModel> list = new ArrayList<>();
                    for (int i = 0; i < getOriginalQueue().size(); i++) {
                        if (getOriginalQueue().get(i).getCreateUser().equals(SP.getUid())) {
                            list.add(getOriginalQueue().get(i));
                        }
                    }
                    return list;
                }
            }

            public void removeAllMember() {
                synchronized (queueLock) {
                    for (int i = 0; i < getOriginalQueue().size(); i++) {
                        if (getOriginalQueue().get(i).getCreateUser().equals(SP.getUid())) {
                            getOriginalQueue().remove(i);
                        }
                    }
                }
            }
        };
        draftsQueues = new XQueue<DraftModel>(){

            @Override
            public ArrayList<DraftModel> getQueue() {
                synchronized (queueLock) {
                    ArrayList<DraftModel> list = new ArrayList<>();
                    for (int i = 0; i < getOriginalQueue().size(); i++) {
                        if (getOriginalQueue().get(i).getCreateUser().equals(SP.getUid())) {
                            list.add(getOriginalQueue().get(i));
                        }
                    }
                    return list;
                }
            }

            public void removeAllMember() {
                synchronized (queueLock) {
                    for (int i = 0; i < getOriginalQueue().size(); i++) {
                        if (getOriginalQueue().get(i).getCreateUser().equals(SP.getUid())) {
                            getOriginalQueue().remove(i);
                        }
                    }
                }
            }
        };
    }

    /**
     * 直接访问单例对象
     */
    public static SYDataManager shareDataManager() {

        if (dataManager == null) {
            synchronized (SYDataManager.class) {
                dataManager = new SYDataManager();
                try {
                    dataManager.initData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataManager;
    }

    private void initData() throws SQLException {
        {
            List<PublishModel> allLists = PublishModelManager.dbQueryForAll();
            for (PublishModel task : allLists) {
                task.onUnSerializable();
                if (task.getFeed() == null) {
                    continue;
                }
                ((NativeRichTextFood) task.getFeed().getFood()).setTask(task);
                if (task.getTaskExecutState() != XTask.XTaskExecutStateComplete) {
                    task.setTaskExecutState(XTask.XTaskExecutStateFail);
                    FFDao.saveOrUpdate(task);
                }
                if (taskQueues != null) {
                    taskQueues.addMember(task);
                }
            }
        }


        List<DraftModel> allLists = DraftModelManager.dbQueryForAll();
        for (DraftModel draft : allLists) {
            if (draftsQueues != null) {
                try {
                    draft.onUnSerializable();
                    draftsQueues.addMember(draft);
                } catch (Exception e) {
                    // TODO 删除旧版本
                }

            }
        }

    }

    /**
     * 获取当前所有的任务集合
     *
     * @return 任务对象集合
     */
    public ArrayList<PublishModel> allTasks() {
        if (taskQueues != null) {
            synchronized (taskLock) {
                return taskQueues.getQueue();
            }
        } else {
            return new ArrayList();
        }
    }

    /**
     * 查找对应ID下的草稿箱对象
     * @param ID 对象ID
     * @return 草稿箱对象
     */
    public DraftModel draftsWithID(String ID) {
        if (ID == null) {
            return null;
        }

        synchronized (draftsLock) {
            DraftModel draftModel = draftsQueues.memberWithID(ID);
            return draftModel;
        }
    }

    /**
     * 查找对应ID下的草稿箱对象
     *
     * @return 草稿箱对象
     */
    public DraftModel draftsOfFirst() {
        synchronized (draftsLock) {
            DraftModel draftModel = draftsQueues.getMember();
            return draftModel;
        }
    }

    /**
     * 分享到动态列表的失败列表
     *
     * @return
     */
    public List<PublishModel> getAllFailDynamic() {
        List<PublishModel> lpist = allTasks();
        List<PublishModel> list = new ArrayList<>();
        for (PublishModel pm : lpist) {
            if (pm.getTaskExecutState() == PublishModel.XTaskExecutStateFail && pm.isBshareToSmallYellowO()) {
                list.add(pm);
            }
        }
        return list;
    }


    /**
     * 添加草稿箱到内存草稿队列,并且加入表结构持久化缓存
     *
     * @param draftModel 草稿对象
     * @return true成功 否则失败
     */
    public boolean addDraft(DraftModel draftModel) {
        if (draftModel == null)
            return false;
        if (!draftModel.validateID())
            return false;

        synchronized (draftsLock) {
            boolean bSuccess = DraftModelManager.dbSaveOrUpDate(draftModel);
            if (bSuccess)
                draftsQueues.addMember(draftModel);
            return bSuccess;
        }
    }

    /**
     * 添加草稿箱到内存草稿箱队列,但不加入数据表持久化缓存
     *
     * @param draftModel 草稿对象
     * @return true成功      否则失败
     */
    public boolean addNonInsertDraftDataBase(DraftModel draftModel) {
        if (draftModel == null)
            return false;
        if (!draftModel.validateID())
            return false;

        synchronized (draftsLock) {
            draftsQueues.addMember(draftModel);
            return true;
        }
    }


    /**
     * 更新草稿到草稿队列,并且更新到数据表
     * @param draftModel
     * @return
     */
    public boolean updateDraft(DraftModel draftModel) {
        if (draftModel == null)
            return false;
        if (!draftModel.validateID())
            return false;

        synchronized (draftsLock) {
            boolean bSuccess = FFDao.update(draftModel);
            if (bSuccess)
                draftsQueues.updateMember(draftModel);
            return bSuccess;
        }
    }

    /**
     * 移除草稿
     * @param draftModel 任务对象
     * @return true成功 false失败
     */
    public boolean removeDraft(DraftModel draftModel) {
        if (draftModel == null)
            return false;
        if (!draftModel.validateID())
            return false;

        synchronized (draftsLock) {
            boolean bSuccess = FFDao.delete(draftModel);
            if (bSuccess)
                draftsQueues.removeMember(draftModel);
            return bSuccess;
        }
    }

    /**
     * 移除所有草稿
     * @return true成功 false失败
     */
    public boolean removeAllDrafts() {
        synchronized (draftsLock) {
            DraftModelManager.deleteCurrentDraftModel();
            boolean bSuccess = DraftModelManager.dbDeleteAll();
            if (bSuccess)
                draftsQueues.removeAllMember();
            return bSuccess;
        }
    }


    /**
     * 获取当前所有的草稿集合
     *
     * @return 草稿对象集合
     */
    public ArrayList allDrafts() {
        if (draftsQueues != null) {
            synchronized (draftsLock) {
                return draftsQueues.getQueue();
            }
        } else {
            return new ArrayList();
        }
    }

    /**
     * 查找对应ID下的任务对象
     *
     * @param ID 对象ID
     * @return 任务对象
     */
    public XTask taskWithID(String ID) {
        if (ID == null) {
            return null;
        }

        synchronized (taskLock) {
            XTask task = taskQueues.memberWithID(ID);
            return task;
        }
    }

    /**
     * 添加任务到内存任务队列,并且加入表结构持久化缓存
     *
     * @param task 任务对象
     * @return true成功 否则失败
     */
    public boolean addTask(PublishModel task) {
        if (task == null)
            return false;
        if (!task.validateID())
            return false;

        synchronized (taskLock) {
            boolean bSuccess = FFDao.saveOrUpdate(task);
            if (bSuccess)
                taskQueues.addMember(task);
            return bSuccess;
        }
    }

    /**
     * 添加任务到内存任务队列,但不加入数据表持久化缓存
     *
     * @param task 任务对象
     * @return true成功 否则失败
     */
    public boolean addNonInsertTaskDataBase(PublishModel task) {
        if (task == null)
            return false;
        if (!task.validateID())
            return false;

        synchronized (taskLock) {
            taskQueues.addMember(task);
            return true;
        }
    }

    /**
     * 更新任务到任务队列,并且更新到数据表
     *
     * @param task
     * @return
     */
    public boolean updateTask(PublishModel task) {
        if (task == null)
            return false;
        if (!task.validateID())
            return false;

        synchronized (taskLock) {
            boolean bSuccess = FFDao.update(task);
            if (bSuccess)
                taskQueues.updateMember(task);
            return bSuccess;
        }
    }

    /**
     * 移除任务
     *
     * @param task 任务对象
     * @return true成功 false失败
     */
    public boolean removeTask(PublishModel task) {
        if (task == null)
            return false;
        if (!task.validateID())
            return false;

        synchronized (taskLock) {
            boolean bSuccess = FFDao.delete(task);
            if (bSuccess)
                taskQueues.removeMember(task);
            return bSuccess;
        }
    }

    /**
     * 移除所有任务
     *
     * @return true成功 false失败
     */
    public boolean removeAllTask() {
        synchronized (taskLock) {
            boolean bSuccess = FFDao.deleteAll(PublishModel.class);
            if (bSuccess)
                taskQueues.removeAllMember();
            return bSuccess;
        }
    }


    public void addRichImage(SYRichTextPhotoModel model) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected SYDataManager(Parcel in) {
        super(in);
    }

    public static final Creator<SYDataManager> CREATOR = new Creator<SYDataManager>() {
        @Override
        public SYDataManager createFromParcel(Parcel source) {
            return new SYDataManager(source);
        }

        @Override
        public SYDataManager[] newArray(int size) {
            return new SYDataManager[size];
        }
    };
}