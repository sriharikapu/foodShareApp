package com.fan.framework.base.Test;

/**
 * Created by lanbiao on 2017/6/7.
 */

public class BaseTestModel<T> extends BaseData {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name1){
        this.name = name1;
    }
}