package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PGCResult extends BaseResult {
    private List<SYChoiceModel> pgcs;

    public List<SYChoiceModel> getPgcs() {
        return pgcs;
    }

    public void setPgcs(List<SYChoiceModel> pgcs) {
        this.pgcs = pgcs;
    }
}
