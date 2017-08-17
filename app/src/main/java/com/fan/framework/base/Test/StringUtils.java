package com.fan.framework.base.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lanbiao on 2017/6/7.
 */

public class StringUtils {
    public static List<String> split_String(String classNames){
        List<String> stringsList = null;
        if(classNames != null){
            String[] className = classNames.split("_");
            stringsList = Arrays.asList(className);
        }
        return stringsList;
    }
}
