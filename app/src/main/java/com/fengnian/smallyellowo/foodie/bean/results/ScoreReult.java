package com.fengnian.smallyellowo.foodie.bean.results;


import com.fengnian.smallyellowo.foodie.bean.publics.ScoreDayBean;

import java.util.List;

/**
 * Created by Administrator on 2016-9-8.
 */

public class ScoreReult extends BaseResult {
    private List<ScoreDayBean> scoreDays;
    private String totalPoints;

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<ScoreDayBean> getScoreDays() {
        return scoreDays;
    }

    public void setScoreDays(List<ScoreDayBean> scoreDays) {
        this.scoreDays = scoreDays;
    }


}
