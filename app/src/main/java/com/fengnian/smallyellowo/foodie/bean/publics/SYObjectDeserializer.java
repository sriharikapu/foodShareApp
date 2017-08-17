package com.fengnian.smallyellowo.foodie.bean.publics;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.fan.framework.base.Test.RunTimeCompare;
import com.fan.framework.base.Test.StringUtils;
import com.fengnian.smallyellowo.foodie.bean.SYObjectPools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanbiao on 2017/4/24.
 * 自动解析扩展
 */
public class SYObjectDeserializer extends JavaBeanDeserializer {

    public SYObjectDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return super.deserialze(parser, type, fieldName);
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, int features) {
        return super.deserialze(parser, type, fieldName, features);
    }

    @Override
    protected <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, Object object, int features) {
        if (type instanceof Class<?>) {
            T obj = null;
            JSONObject jsonObject = MapDeserializer.instance.deserialze(parser, JSONObject.class, fieldName);
            obj = (T) SYObjectPools.shareGlobalObject.processObject(jsonObject,type);
            return obj;
        }else if(type instanceof ParameterizedType){
            Type basicType = ((ParameterizedType) type).getRawType();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            String name = types[0].toString();
            int index = name.indexOf(" ");
            if(index >= 0){
                index += 1;
                name = name.substring(index);
            }

            index = name.lastIndexOf(".");
            List<String> pack_class_name_list = new ArrayList<String>();
            if(index >= 0){
                index += 1;
                String class_Names = name.substring(index);
                String packName = name.substring(0,index - 1);
                List<String> class_name_list = StringUtils.split_String(class_Names);
                for(String classname : class_name_list){
                    pack_class_name_list.add(packName + "." + classname);
                }
            }


            if(basicType instanceof Class<?>){
                T obj = null;
                JSONObject jsonObject = MapDeserializer.instance.deserialze(parser, JSONObject.class, fieldName);

                obj = RunTimeCompare.getClassCompareWeight(pack_class_name_list,jsonObject);
                return obj;
            }
        }

        return super.deserialze(parser, type, fieldName, object, features);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
