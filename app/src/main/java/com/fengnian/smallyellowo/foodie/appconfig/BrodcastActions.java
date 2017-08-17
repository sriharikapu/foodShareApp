package com.fengnian.smallyellowo.foodie.appconfig;

import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-10-9.
 */
public class BrodcastActions {

    private static final ArrayList<FeedChangeListener> list = new ArrayList<>();

    public static void addListener(FeedChangeListener listener) {
        list.add(listener);
    }

    public static void removeListtener(FeedChangeListener listener) {
        list.remove(listener);
    }


    public static void publishFailed(PublishModel task) {
        FFLogUtil.e("发布", "发布失败 广播" + task.getId());
        for (FeedChangeListener listener : list) {
            listener.publishFailed(task);
        }
    }

    public static void publishSuccessed(PublishModel task, SYFeed feed) {
        FFLogUtil.e("发布", "发布成功 广播" + task.getId());
        for (FeedChangeListener listener : list) {
            listener.publishSuccessed(task, feed);
        }
    }

    public static void startTask(PublishModel task) {
        FFLogUtil.e("发布", "发布开始 广播" + task.getId());
        for (FeedChangeListener listener : list) {
            listener.startTask(task);
        }
    }


    public static void foodDeleted(SYFeed task, boolean isUserCenter) {
        FFLogUtil.e("发布", "删除feed广播" + task.getId());
        for (FeedChangeListener listener : list) {
            listener.foodDeleted(task, isUserCenter);
        }
    }

    public static void netDataPublishSuccess(SYFeed feed) {
        FFLogUtil.e("发布", "重新发布广播" + feed.getId());
        for (FeedChangeListener listener : list) {
            listener.newPubSuccess(feed);
        }
    }

    public interface FeedChangeListener {

        void publishFailed(PublishModel task);

        void publishSuccessed(PublishModel task, SYFeed feed);

        void startTask(PublishModel task);

        void foodDeleted(SYFeed task, boolean isUserCenter);

        void newPubSuccess(SYFeed feed);
    }


}