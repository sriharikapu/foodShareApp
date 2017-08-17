package com.fengnian.smallyellowo.foodie.utils;

import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-10-10.
 */

public class Config {


    //控制选择图片的
    public static List<ImageItem>  list=new ArrayList<>();

    public static   List<String>  getStringlist(){

        List<String>  mlist=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            ImageItem item=list.get(i);
            mlist.add(item.getImagePath());
        }

        return mlist;
    }
   //识别水单的菜品的名字
    public static List<String>  memo_list=new ArrayList<>();

    public static String   is_add_memo_pic_flag="no";//yes  是添加水单  no 是不是

    /**
     * 判断 正则 是否正确
     * @param str
     * @param str_rggular
     * @return
     */
    public static boolean isRightRegular(String str,String str_rggular) {
        Pattern p = Pattern
                .compile(str_rggular);
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }
}
