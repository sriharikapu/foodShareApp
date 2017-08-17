package com.fan.framework.base.Test;

import com.fan.framework.base.XData;

/**
 * Created by lanbiao on 2017/6/7.
 */

public class TestObject extends XData {
    private int a;
    private BaseData<BaseTestModel_TestModel> model;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public BaseData<BaseTestModel_TestModel> getModel() {
        return model;
    }

    public void setModel(BaseData<BaseTestModel_TestModel> model) {
        this.model = model;
    }
}
