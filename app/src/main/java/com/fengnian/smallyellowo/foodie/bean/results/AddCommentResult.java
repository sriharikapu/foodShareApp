package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYSecondLevelcomment;

import java.util.List;

public class AddCommentResult extends BaseResult {
    private String result;
    private int totalCommentCount;
    private List<SYSecondLevelcomment> sySecondLevelComments;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<SYSecondLevelcomment> getSySecondLevelComments() {
        return sySecondLevelComments;
    }

    public void setSySecondLevelComments(List<SYSecondLevelcomment> sySecondLevelComments) {
        this.sySecondLevelComments = sySecondLevelComments;
    }

    public int getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }
}
