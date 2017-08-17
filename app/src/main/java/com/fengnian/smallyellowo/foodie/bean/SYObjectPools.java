package com.fengnian.smallyellowo.foodie.bean;

import com.alibaba.fastjson.JSONObject;
import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.lang.reflect.Type;

/**
 * Created by lanbiao on 2017/5/3.
 * 项目对象池
 */
public class SYObjectPools extends XData {
    private SYUserObjectPools userObjectPools = SYUserObjectPools.shareGlobalInstance;
    private SYFeedObjectPools feedObjectPools = SYFeedObjectPools.shareGlobalInstance;

    /**
     * 全局唯一单例对象
     */
    public final static SYObjectPools shareGlobalObject = new SYObjectPools();

    private SYObjectPools(){
    }

    /**
     * 对外处理接口，主要用于外部模块与对象池的对接
     * @param jsonObject   json对象
     * @param type  被解析的数据类型
     * @param <T>   解析后的javaBean对象类型
     * @return  返回T对象
     */
    public <T> T processObject(JSONObject jsonObject, Type type){
        if(jsonObject == null)
            return null;

        if(type == SYFeed.class){
            return (T) feedObjectPools.updateFeedWithJSONObject(jsonObject);
        }else if(type == SYUser.class){
            return (T)userObjectPools.updateUserWithJSONObject(jsonObject);
        }
        return null;
    }

    public <T> T updateObject(T object){
        if(object == null)
            return null;

        if(object instanceof SYUser){
            return (T) userObjectPools.updateUser((SYUser) object);
        }else if(object instanceof SYFeed){
            return (T) feedObjectPools.updateFeed((SYFeed)object);
        }

        return null;
    }

    public <T> T userWithUserID(String userID){
        if(userID == null){
            return null;
        }

        return (T) userObjectPools.userWithID(userID);
    }

    public <T> T feedWithUserID(String feedID){
        if(feedID == null){
            return null;
        }

        return (T) feedObjectPools.feedWithID(feedID);
    }
}
