package com.fengnian.smallyellowo.foodie.bean;

import com.alibaba.fastjson.JSONObject;
import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lanbiao on 2017/5/3.
 */

public class SYFeedObjectPools extends XData {
    /**
     * feed对象的时间戳顺序
     */
    private ArrayList<String> feedIDTimeSpace;

    /**
     * feed对象集合
     */
    private HashMap<String,SYFeed> feedObjectPools;

    /**
     * feed对象更新锁定对象
     */
    private final Object feedLock = new Object();

    /**
     * 最大同时存在对象的个数
     */
    private final int max_pools_count = 50;

    /**
     * 单例对象
     */
    public static SYFeedObjectPools shareGlobalInstance = new SYFeedObjectPools();

    private SYFeedObjectPools(){
        feedIDTimeSpace = new ArrayList<String>();
        feedObjectPools = new HashMap<String,SYFeed>();
    }

    /**
     * 根据指定的feedID从对象池中查找对象，存在就返回对象，否则返回null
     * @param feedID
     * @return
     */
    public SYFeed feedWithID(String feedID){
        if(feedID == null || feedID.length() <= 0)
            return null;

        SYFeed oldFeed = null;
        synchronized (feedLock){
            oldFeed = feedObjectPools.get(feedID);
        }

        if(!(oldFeed instanceof SYFeed))
            return null;

        return oldFeed;
    }

    /**
     * 增加新用户对象到对象池
     * @param feed
     */
    private void addNewFeedToPools(SYFeed feed){
        if(feed == null || feed.getId() == null || feed.getId().length() <= 0)
            return;

        synchronized (feedLock){
            feedObjectPools.put(feed.getId(),feed);
            feedIDTimeSpace.remove(feed.getId());
            feedIDTimeSpace.add(0,feed.getId());
        }
    }

    /**
     * 清除对象池溢出的数据
     */
    private void clearGarbageCacheData(){
        if(feedIDTimeSpace.size() >= max_pools_count){
            synchronized (feedLock){
                for(int index = feedIDTimeSpace.size() -1; index >= max_pools_count - 1;index--){
                    String key = feedIDTimeSpace.get(index);
                    if(key != null){
                        feedObjectPools.remove(key);
                        feedIDTimeSpace.remove(key);
                    }
                }
            }
        }
    }

    /**
     * 从对象池移除指定的对象
     * @param feed
     */
    private void removeFeedToPools(SYFeed feed){
        if(feed == null || feed.getId() == null || feed.getId().length() <= 0)
            return;
        removeFeedToPools(feed.getId());
    }

    /**
     * 从对象池移除指定key的对象
     * @param key 对象id
     */
    private void removeFeedToPools(String key){
        if(key == null || key.length() <= 0)
            return;
        synchronized (feedLock){
            feedObjectPools.remove(key);
            feedIDTimeSpace.remove(key);
        }
    }

    /**
     * 移除对象池所有的对象
     */
    private void removeAllPools(){
        synchronized (feedLock){
            feedObjectPools.clear();
            feedIDTimeSpace.clear();
        }
    }

    /**
     * 根据jsonObject对象更新对象池中相应的对象
     * @param jsonObject
     * @return
     */
    public SYFeed updateFeedWithJSONObject(JSONObject jsonObject){

        if(jsonObject == null){
            return null;
        }

        if(!jsonObject.containsKey("id")){
            return null;
        }

        String feedID = jsonObject.getString("id");
        if(feedID == null || feedID.length() <= 0){
            return null;
        }

        SYFeed oldFeed = feedWithID(feedID);
        if(oldFeed == null){
            //对象池中不存在，那就利用jsonObject解析出Feed并更新对象池
            SYFeed newFeed = SYFeed.createOrUpdateWithJsonObject(null,jsonObject);
            addNewFeedToPools(newFeed);
            clearGarbageCacheData();
            oldFeed = newFeed;
        }else {
            //对象池中存在，利用jsonObject更新对象池对象
            oldFeed = SYFeed.createOrUpdateWithJsonObject(oldFeed,jsonObject);
        }
        return oldFeed;
    }

    /**
     * 根据指定Feed对象更新对象池Feed相应的对象,一般是手工调用更新对象池使用
     * @param feed 新对象
     * @return 更新后的Feed对象
     */
    public SYFeed updateFeed(SYFeed feed){
        if(feed == null || feed.getId() == null || feed.getId().length() <= 0){
            return null;
        }

        if(!(feed instanceof SYFeed)) {
            return null;
        }

        SYFeed oldFeed = feedWithID(feed.getId());
        if(oldFeed == null){
            //插入新数据到map
            addNewFeedToPools(feed);
            clearGarbageCacheData();
        }else {
            if(oldFeed.getId() == null || oldFeed.getId().length() <= 0){
                //先移除old数据，再加入新数据
                removeFeedToPools(oldFeed);
                addNewFeedToPools(feed);
            }else {
                //更新新数据到老对象
                //oldUser.updateDataWithUser(user);
                updateFeedToPools(feed);
            }
        }
        return oldFeed;
    }

    private void updateFeedToPools(SYFeed feed){
        if(feed == null || feed.getId() == null)
            return;
        synchronized (feedLock){
            feedObjectPools.remove(feed.getId());
            feedIDTimeSpace.remove(feed.getId());
            feedObjectPools.put(feed.getId(),feed);
            feedIDTimeSpace.add(0,feed.getId());
        }
    }
}

