package com.fengnian.smallyellowo.foodie.bean.publics;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016-8-30.
 */

public class AttionReddata  implements  Serializable{
    private String  lastPage;
    private List<AttentionRecord>  attentionRecordlist;

    public List<AttentionRecord> getAttentionRecordlist() {
        return attentionRecordlist;
    }

    public void setAttentionRecordlist(List<AttentionRecord> attentionRecordlist) {
        this.attentionRecordlist = attentionRecordlist;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }
}
