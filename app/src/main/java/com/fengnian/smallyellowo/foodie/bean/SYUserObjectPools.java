package com.fengnian.smallyellowo.foodie.bean;

import com.alibaba.fastjson.JSONObject;
import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lanbiao on 2017/5/3.
 */

public class SYUserObjectPools extends XData {
    /**
     * user用户对象的时间戳顺序
     */
    private ArrayList<String> userIDTimeSpace;

    /**
     * user用户对象集合
     */
    private HashMap<String,SYUser> userObjectPools;

    /**
     * user用户对象更新锁定对象
     */
    private final Object userLock = new Object();

    /**
     * 最大同时存在对象的个数
     */
    private final int max_pools_count = 50;

    /**
     * 全局唯一单例对象
     */
    public static SYUserObjectPools shareGlobalInstance = new SYUserObjectPools();

    private SYUserObjectPools(){
        userIDTimeSpace = new ArrayList<String>();
        userObjectPools = new HashMap<String,SYUser>();
    }

    /**
     * 清除对象池溢出的数据
     */
    private void clearGarbageCacheData(){
        if(userIDTimeSpace.size() >= max_pools_count){
            synchronized (userLock){
                for(int index = userIDTimeSpace.size() -1; index >= max_pools_count - 1;index--){
                    String key = userIDTimeSpace.get(index);
                    if(key != null){
                        userObjectPools.remove(key);
                        userIDTimeSpace.remove(key);
                    }
                }
            }
        }
    }

    /**
     * 增加新用户对象到对象池
     * @param user
     */
    private void addNewUserToPools(SYUser user){
        if(user == null || user.getId() == null || user.getId().length() <= 0)
            return;

        synchronized (userLock){
            userObjectPools.put(user.getId(),user);
            userIDTimeSpace.remove(user.getId());
            userIDTimeSpace.add(0,user.getId());
        }
    }

    /**
     * 从对象池移除指定的对象
     * @param user
     */
    private void removeUserToPools(SYUser user){
        if(user == null || user.getId() == null || user.getId().length() <= 0)
            return;
        removeUserToPools(user.getId());
    }

    /**
     * 从对象池移除指定key的对象
     * @param key 对象id
     */
    private void removeUserToPools(String key){
        if(key == null || key.length() <= 0)
            return;
        synchronized (userLock){
            userObjectPools.remove(key);
            userIDTimeSpace.remove(key);
        }
    }

    /**
     * 移除对象池所有的对象
     */
    private void removeAllPools(){
        synchronized (userLock){
            userObjectPools.clear();
            userIDTimeSpace.clear();
        }
    }

    private void updateUserToPools(SYUser user){
        if(user == null || user.getId() == null)
            return;
        synchronized (userLock){
            userObjectPools.remove(user.getId());
            userIDTimeSpace.remove(user.getId());
            userObjectPools.put(user.getId(),user);
            userIDTimeSpace.add(0,user.getId());
        }
    }

    /**
     * 根据指定的userID从对象池中查找对象，存在就返回对象，否则返回null
     * @param userID
     * @return
     */
    public SYUser userWithID(String userID){
        if(userID == null || userID.length() <= 0)
            return null;

        SYUser oldUser = null;
        synchronized (userLock){
            oldUser = userObjectPools.get(userID);
        }

        if(!(oldUser instanceof SYUser))
            return null;

        return oldUser;
    }

    /**
     * 根据指定User对象更新对象池User相应的对象,一般是手工调用更新对象池使用
     * @param user 新对象
     * @return 更新后的User对象
     */
    public SYUser updateUser(SYUser user){
        if(user == null || user.getId() == null || user.getId().length() <= 0){
            return null;
        }

        if(!(user instanceof SYUser)) {
            return null;
        }

        SYUser oldUser = userWithID(user.getId());
        if(oldUser == null){
            //插入新数据到map
            addNewUserToPools(user);
            clearGarbageCacheData();
        }else {
            if(oldUser.getId() == null || oldUser.getId().length() <= 0){
                //先移除old数据，再加入新数据
                removeUserToPools(oldUser);
                addNewUserToPools(user);
            }else {
                //更新新数据到老对象
                //oldUser.updateDataWithUser(user);
                updateUserToPools(user);
            }
        }
        return oldUser;
    }

    /**
     * 根据指定的json对象更新对象池对象
     * @param jsonObject 新json对象
     * @return 返回更新后的对象
     */
    public SYUser updateUserWithJSONObject(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }

        if(!jsonObject.containsKey("id")){
            return null;
        }

        String userID = jsonObject.getString("id");
        if(userID == null || userID.length() <= 0){
            return null;
        }

        SYUser oldUser = userWithID(userID);
        if(oldUser == null){
            //对象池中不存在，那就利用jsonObject解析出User并更新对象池
            SYUser newUser = SYUser.createOrUpdateWithJsonObject(null,jsonObject);
            addNewUserToPools(newUser);
            clearGarbageCacheData();
            oldUser = newUser;
        }else {
            //对象池中存在，利用jsonObject更新对象池对象
            oldUser = SYUser.createOrUpdateWithJsonObject(oldUser,jsonObject);
        }
        return oldUser;
    }
}
