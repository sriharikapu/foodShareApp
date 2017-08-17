package com.fengnian.smallyellowo.foodie.bean.publics;

import java.util.List;

/**
 * Created by Administrator on 2016-9-8.
 */

public class ScoreDayBean {

    private List<ScoreDetailBean> scoreDetails;
    private String  scoreDayTime;
    private String  scoreDayPoints;

    private boolean flag;

    public List<ScoreDetailBean> getScoreDetails() {
        return scoreDetails;
    }

    public void setScoreDetails(List<ScoreDetailBean> scoreDetails) {
        this.scoreDetails = scoreDetails;
    }

    public String getScoreDayTime() {
        return scoreDayTime;
    }

    public void setScoreDayTime(String scoreDayTime) {
        this.scoreDayTime = scoreDayTime;
    }

    public String getScoreDayPoints() {
        return scoreDayPoints;
    }

    public void setScoreDayPoints(String scoreDayPoints) {
        this.scoreDayPoints = scoreDayPoints;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
