package com.fan.framework.xtaskmanager.xthreadpool.xqueue;

import android.os.Parcel;

import com.fan.framework.base.XData;
import com.fan.framework.utils.FFUtils;

import java.util.ArrayList;

/**
 * 基础队列
 */
public class XQueue<T> extends XData {
    private ArrayList<T> queue;
    public final Object queueLock = new Object();

    public ArrayList<T> getOriginalQueue(){
        return queue;
    }


    public static <T> XQueue createQueue() {
        XQueue<T> queueObj = new XQueue<>();
        return queueObj;
    }

    public XQueue() {
        queue = new ArrayList<T>();
    }

    public ArrayList<T> getQueue() {
        synchronized (queueLock) {
            return queue;
        }
    }

    public void setQueue(ArrayList<T> q) {
        synchronized (queueLock) {
            queue = q;
        }
    }

    public Integer count() {
        synchronized (this) {
            return getQueue().size();
        }
    }

    public T getMember() {
        T member = null;
        synchronized (this) {
            if (getQueue().size() > 0) {
                {
//                member = (T) getQueue().remove(0);
                    member = (T) getQueue().get(0);
                }
            }
        }
        return member;
    }

    public T memberWithID(String ID) {
        if (ID == null) {
            return null;
        }

        synchronized (queueLock) {
            T data = null;
            for (int index = queue.size() - 1; index >= 0; index--) {
                T oldData = queue.get(index);
                if (oldData instanceof XData) {
                    XData oldDataMember = (XData) oldData;
                    if (oldDataMember.getId().equals(ID)) {
                        data = (T) oldDataMember;
                        break;
                    }
                }
            }
            return data;
        }
    }

    public T memberOfFirst() {
        synchronized (queueLock) {
            T data = null;
            for (int index = queue.size() - 1; index >= 0; index--) {
                data = queue.get(index);
            }
            return data;
        }
    }

    public void updateMember(T member) {
        if (member == null)
            return;

        synchronized (this) {
            for (Integer index = queue.size() - 1; index >= 0; index--) {
                T oldMember = queue.get(index);
                if (oldMember instanceof XData &&
                        member instanceof XData) {
                    XData oldData = (XData) oldMember;
                    XData data = (XData) member;
                    if (oldData.isEqual(data)) {
                        queue.remove(index.intValue());
                    }
                }
            }
            queue.add(member);
        }
    }

    public void removeAllMember() {
        synchronized (this) {
            queue.clear();
        }
    }

    public void removeMember(T member) {
        if (member == null)
            return;

        synchronized (this) {
            for (Integer index = 0; index < queue.size(); index++) {
                T oldMember = queue.get(index);
                if (oldMember instanceof XData &&
                        member instanceof XData) {
                    XData oldData = (XData) oldMember;
                    XData data = (XData) member;
                    if (oldData.isEqual(data)) {
                        queue.remove(index.intValue());
                        break;
                    }
                }
            }
        }
    }

    public void addMember(T member) {
        if (member == null)
            return;
        synchronized (this) {
            boolean bExist = false;
            for (Integer index = 0; index < queue.size(); index++) {
                T oldMember = queue.get(index);
                if (oldMember instanceof XData &&
                        member instanceof XData) {
                    if (((XData) member).isEqual((XData) oldMember)) {
                        bExist = true;
                        break;
                    }
                }
            }

            if (!bExist)
                queue.add(member);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.queue);
    }

    protected XQueue(Parcel in) {
        super(in);
        this.queue = new ArrayList<T>();
        in.readList(this.queue, FFUtils.getTClass(this).getClassLoader());
    }

    public static final Creator<XQueue> CREATOR = new Creator<XQueue>() {
        @Override
        public XQueue createFromParcel(Parcel source) {
            return new XQueue(source);
        }

        @Override
        public XQueue[] newArray(int size) {
            return new XQueue[size];
        }
    };
}
