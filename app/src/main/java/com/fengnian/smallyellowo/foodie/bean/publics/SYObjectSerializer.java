package com.fengnian.smallyellowo.foodie.bean.publics;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by lanbiao on 2017/4/24.
 * 自动反序列化扩产，待扩展
 */
public class SYObjectSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

    }
}
