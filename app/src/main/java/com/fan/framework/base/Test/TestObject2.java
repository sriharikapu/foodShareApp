package com.fan.framework.base.Test;

import java.util.ArrayList;

/**
 * Created by lanbiao on 2017/6/8.
 */

public class TestObject2 {
    private int a;
    private ArrayList<BaseData<BaseTestModel_TestModel>> modellist;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public ArrayList<BaseData<BaseTestModel_TestModel>> getModellist() {
        return modellist;
    }

    public void setModellist(ArrayList<BaseData<BaseTestModel_TestModel>> modellist) {
        this.modellist = modellist;
    }
}
