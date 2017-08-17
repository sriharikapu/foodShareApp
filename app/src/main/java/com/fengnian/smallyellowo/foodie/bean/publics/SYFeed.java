package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.base.XData;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModel;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.SYObjectPools;
import com.fengnian.smallyellowo.foodie.bean.SYUserObjectPools;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager.getConfigByIndex;

/**
 * 一条发布
 *
 * @author Administrator
 */
public class SYFeed extends XData implements Parcelable, Serializable {
    //    private String title;// 纪录名称 是
    private String isAddScore = "0";//  是否加过分，1得过 0没得过
    private SYUser user;// 发布feed的用户对象 是
    private SYRichTextFood food;// 速记美食对象或者是富文本美食对象 是
    private boolean bFav;// 是否收藏过 是
    private transient boolean bEat;
    private boolean bThumbsUp;// 是否赞过 是
    private int starLevel;

    public int getThumbsUpCount() {
        return thumbsUpCount <= 0 ? 0 : thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

    private int thumbsUpCount;// 总赞数 是
    private int eatCount;// 多少人吃数 是
    private List<SYUser> thumbsUps;// 赞列表 否
    private List<SYSecondLevelcomment> secondLevelcomments;// 评论列表 否
    private Double distance;// 距离(单位:m) 否
    private long timeStamp;// 时间戳（分页用） 是
    private boolean handPick;//是否精选内容
    private int eatState;//1.4.0 新加字段 - (想吃)/是否吃过 0:在想吃清单中未吃、 1：在想吃清单中吃过 、2:不在想吃清单(已吃/未吃)中
    private String foodNoteId;//  个人中心表示的feed的 ID 是

    public ArrayList<SYFoodTagModel> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(ArrayList<SYFoodTagModel> tags) {
        this.tags = tags;
    }

    private ArrayList<SYFoodTagModel> tags;
    private RichTextModel releaseTemplate = getConfigByIndex(SYReleaseTemplateType_Standard);


    public int getEatCount(String eatCount1) {
        return eatCount;
    }

    public Double getDistance(String distance1) {
        return distance;
    }

    public long getTimeStamp(String timeStamp1) {
        return timeStamp;
    }

    public int getEatState(String eatState1) {
        return eatState;
    }

    public int getThumbsUpCount(String thumbsUpCount1) {
        return thumbsUpCount;
    }

    public int getStarLevel(String starLevel1) {
        return starLevel;
    }

    public int getStarLevel() {
        return starLevel <= 0 ? 0 : starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    @JSONField(serialize = false)
    private boolean isExpaned;

    public int getEatState() {
        return eatState <= 0 ? 0 : eatState;
    }

    public void setEatState(int eatState) {
        this.eatState = eatState;
    }


    public String getIsAddScore() {
        return isAddScore;
    }

    public void setIsAddScore(String isAddScore) {
        this.isAddScore = isAddScore;
    }

    public String getFoodNoteId() {
        return foodNoteId;
    }

    public void setFoodNoteId(String foodNoteId) {
        this.foodNoteId = foodNoteId;
    }

    public static Creator<SYFeed> getCREATOR() {
        return CREATOR;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public SYRichTextFood getFood() {
        return food;
    }

    public void setFood(SYRichTextFood food) {
        this.food = food;
    }

    public boolean isbFav() {
        return bFav;
    }

    public void setbFav(boolean bFav) {
        this.bFav = bFav;
    }

    public boolean isbThumbsUp() {
        return bThumbsUp;
    }

    public void setbThumbsUp(boolean bThumbsUp) {
        this.bThumbsUp = bThumbsUp;
    }

    public int getEatCount() {
        return eatCount <= 0 ? 0 : eatCount;
    }

    public void setEatCount(int eatCount) {
        this.eatCount = eatCount;
    }

    public List<SYUser> getThumbsUps() {
        return thumbsUps;
    }

    public void setThumbsUps(List<SYUser> thumbsUps) {
        this.thumbsUps = thumbsUps;
    }

    public Double getDistance() {
        return distance <= 0 ? 0 : distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public long getTimeStamp() {
        return timeStamp <= 0 ? 0 : timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isHandPick() {
        return handPick;
    }

    public void setHandPick(boolean handPick) {
        this.handPick = handPick;
    }

    @JSONField(serialize = false)
    public boolean isExpaned() {
        return isExpaned;
    }

    @JSONField(serialize = false)
    public void setExpaned(boolean expaned) {
        isExpaned = expaned;
    }

    public SYFeed() {
    }

    public void onUnSerializable() {

        List<SYRichTextPhotoModel> list = getFood().getRichTextLists();
        int status = 0;//默认全部成功
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isTextPhotoModel()) {
                if (list.get(i).getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                    list.get(i).getPhoto().getImageAsset().getOriginalImage().setUploadStatus(SYUploadImage.UPLOAD_STATUS_INIT);//有失败的
                } else if (list.get(i).getPhoto().getImageAsset().getProcessedImage() != null &&
                        list.get(i).getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                    list.get(i).getPhoto().getImageAsset().getProcessedImage().setUploadStatus(SYUploadImage.UPLOAD_STATUS_INIT);//有失败的
                }
            }
        }
    }

    public void removeThumbsUp(SYUser user) {
        if (thumbsUps == null) {
            thumbsUps = new ArrayList<>();
            return;
        }
        for (int i = 0; i < thumbsUps.size(); i++) {
            if (thumbsUps.get(i).getId().equals(SP.getUid())) {
                thumbsUps.remove(i);
                return;
            }
        }
    }

    public List<SYSecondLevelcomment> getSecondLevelcomments() {
        if (secondLevelcomments == null) {
            secondLevelcomments = new ArrayList<>();
        }
        return secondLevelcomments;
    }

    public void setSecondLevelcomments(List<SYSecondLevelcomment> secondLevelcomments) {
        this.secondLevelcomments = secondLevelcomments;
    }

    public void addThumbsUp(SYUser user) {
        if (thumbsUps == null) {
            thumbsUps = new ArrayList<>();
            thumbsUps.add(user);
            return;
        }
        boolean has = false;
        for (int i = 0; i < thumbsUps.size(); i++) {
            if (thumbsUps.get(i).getId().equals(SP.getUid())) {
                has = true;
                break;
            }
        }
        if (!has) {
            thumbsUps.add(user);
        }
    }

    public boolean isbEat() {
        return bEat;
    }

    public void setbEat(boolean bEat) {
        this.bEat = bEat;
    }

    /**
     * 判断本地数据是否分享到小黄圈
     */
    public boolean isShared() {
        if (getFood() == null || TextUtils.isEmpty(getFood().getId())) {
            return false;
        }
        XTask xTask = SYDataManager.shareDataManager().taskWithID(getFood().getId());
        if (xTask == null) {
            return false;
        }
        if (xTask instanceof PublishModel) {
            PublishModel publishModel = (PublishModel) xTask;
            if (publishModel != null) {
                return publishModel.isBshareToSmallYellowO();
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.isAddScore);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.food, flags);
        dest.writeByte(this.bFav ? (byte) 1 : (byte) 0);
        dest.writeByte(this.bThumbsUp ? (byte) 1 : (byte) 0);
        dest.writeInt(this.starLevel);
        dest.writeInt(this.thumbsUpCount);
        dest.writeInt(this.eatCount);
        dest.writeTypedList(this.thumbsUps);
        dest.writeTypedList(this.secondLevelcomments);
        dest.writeValue(this.distance);
        dest.writeLong(this.timeStamp);
        dest.writeByte(this.handPick ? (byte) 1 : (byte) 0);
        dest.writeInt(this.eatState);
        dest.writeString(this.foodNoteId);
        dest.writeByte(this.isExpaned ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.getReleaseTemplate(), flags);
        dest.writeTypedList(this.tags);
    }

    protected SYFeed(Parcel in) {
        super(in);
        this.isAddScore = in.readString();
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.food = in.readParcelable(SYRichTextFood.class.getClassLoader());
        this.bFav = in.readByte() != 0;
        this.bThumbsUp = in.readByte() != 0;
        this.starLevel = in.readInt();
        this.thumbsUpCount = in.readInt();
        this.eatCount = in.readInt();
        this.thumbsUps = in.createTypedArrayList(SYUser.CREATOR);
        this.secondLevelcomments = in.createTypedArrayList(SYSecondLevelcomment.CREATOR);
        this.distance = (Double) in.readValue(Double.class.getClassLoader());
        this.timeStamp = in.readLong();
        this.handPick = in.readByte() != 0;
        this.eatState = in.readInt();
        this.foodNoteId = in.readString();
        this.isExpaned = in.readByte() != 0;
        this.releaseTemplate = in.readParcelable(RichTextModel.class.getClassLoader());
        this.tags = in.createTypedArrayList(SYFoodTagModel.CREATOR);
    }

    public static final Creator<SYFeed> CREATOR = new Creator<SYFeed>() {
        @Override
        public SYFeed createFromParcel(Parcel source) {
            return new SYFeed(source);
        }

        @Override
        public SYFeed[] newArray(int size) {
            return new SYFeed[size];
        }
    };

    public String pullStartLevelString() {
        return pullStartLevelString(getStarLevel());
    }

    public static String pullStartLevelString(int level) {
        switch (level) {
            case 0:
                return "美食评分";
            case 1:
                return "差评";
            case 2:
                return "一般";
            case 3:
                return "推荐";
            case 4:
                return "极力推荐";
        }
        return "";
    }

    /**
     * 是否存在美食评分
     *
     * @return true存在 否则不存在
     */
    public boolean haveCommentScorce() {
        if (getStarLevel() >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public RichTextModel getReleaseTemplate() {
        if (releaseTemplate == null) {
            releaseTemplate = RichTextModelManager.getConfigByIndex(1);
        }
        return releaseTemplate;
    }

    public void setReleaseTemplate(RichTextModel releaseTemplate) {
        this.releaseTemplate = releaseTemplate;
    }

    public static SYFeed createOrUpdateWithJsonObject(SYFeed feed, JSONObject jsonObject) {

        if (jsonObject == null)
            return null;

        Boolean bUpdate = true;
        if (feed == null) {
            String feedJson = jsonObject.toJSONString();
            feed = JSON.parseObject(feedJson, SYFeedEx.class);
            bUpdate = false;
            return feed;
        }

        if (jsonObject.containsKey("id")) {
            feed.setId(jsonObject.getString("id"));
        } else {
            if (!bUpdate)
                feed.setId(null);
        }

        if (jsonObject.containsKey("isAddScore")) {
            feed.setIsAddScore(jsonObject.getString("isAddScore"));
        } else {
            if (!bUpdate)
                feed.setIsAddScore(null);
        }

        if (jsonObject.containsKey("user")) {
            SYUser user = null;
            JSONObject userJsonObject = jsonObject.getJSONObject("user");
            if (userJsonObject != null) {
                user = SYObjectPools.shareGlobalObject.processObject(userJsonObject, SYUser.class);
            }
            feed.setUser(user);
        } else {
            if (!bUpdate)
                feed.setUser(null);
        }

        if (jsonObject.containsKey("food")) {
            SYRichTextFood food = jsonObject.getObject("food", SYRichTextFood.class);
            feed.setFood(food);
        } else {
            if (!bUpdate)
                feed.setFood(null);
        }

        if (jsonObject.containsKey("bFav")) {
            Boolean isBFav = jsonObject.getBoolean("bFav");
            if (isBFav != null) {
                isBFav = jsonObject.getBooleanValue("bFav");
                feed.setbFav(isBFav);
            }
        } else {
//            feed.setbFav(false);
        }

        if (jsonObject.containsKey("bEat")) {
            Boolean isBEat = jsonObject.getBoolean("bEat");
            if (isBEat != null) {
                isBEat = jsonObject.getBooleanValue("bEat");
                feed.setbEat(isBEat);
            }
        } else {
//            feed.setbEat(false);
        }

        if (jsonObject.containsKey("bThumbsUp")) {
            Boolean bThumbsUp = jsonObject.getBoolean("bThumbsUp");
            if (bThumbsUp != null) {
                bThumbsUp = jsonObject.getBooleanValue("bThumbsUp");
                feed.setbThumbsUp(bThumbsUp);
            }
        } else {
//            feed.setbThumbsUp(false);
        }

        if (jsonObject.containsKey("starLevel")) {
            Integer starLevel = jsonObject.getInteger("starLevel");
            if (starLevel != null) {
                starLevel = jsonObject.getIntValue("starLevel");
                feed.setStarLevel(starLevel);
            }
        } else {
            if (!bUpdate)
                feed.setStarLevel(-1);
        }

        if (jsonObject.containsKey("thumbsUpCount")) {
            Integer thumbsUpCount = jsonObject.getInteger("thumbsUpCount");
            if (thumbsUpCount != null) {
                thumbsUpCount = jsonObject.getIntValue("thumbsUpCount");
                feed.setThumbsUpCount(thumbsUpCount);
            }
        } else {
            if (!bUpdate)
                feed.setThumbsUpCount(-1);
        }

        if (jsonObject.containsKey("eatCount")) {
            Integer eatCount = jsonObject.getInteger("eatCount");
            if (eatCount != null) {
                eatCount = jsonObject.getIntValue("eatCount");
                feed.setEatCount(eatCount);
            }
        } else {
            if (!bUpdate)
                feed.setEatCount(-1);
        }

        ArrayList<SYUser> thumbsUps = new ArrayList<SYUser>();
        if (jsonObject.containsKey("thumbsUps")) {
            JSONArray jsonArray = jsonObject.getJSONArray("thumbsUps");
            for (int index = 0; index < jsonArray.size(); index++) {
                JSONObject userJsonObject = jsonArray.getJSONObject(index);
                SYUser user = SYObjectPools.shareGlobalObject.processObject(userJsonObject, SYUser.class);
                if (user != null)
                    thumbsUps.add(user);
            }
            feed.setThumbsUps(thumbsUps);
        } else {
            if (!bUpdate) {
                feed.setThumbsUps(thumbsUps);
            }
        }


        ArrayList<SYSecondLevelcomment> secondLevelcomments = new ArrayList<>();
        if (jsonObject.containsKey("secondLevelcomments")) {
            JSONArray jsonArray = jsonObject.getJSONArray("secondLevelcomments");
            for (int index = 0; index < jsonArray.size(); index++) {
                SYSecondLevelcomment secondLevelcomment = jsonArray.getObject(index, SYSecondLevelcomment.class);
                if (secondLevelcomment != null)
                    secondLevelcomments.add(secondLevelcomment);
            }
            feed.setSecondLevelcomments(secondLevelcomments);
        } else {
            if (!bUpdate) {
                feed.setSecondLevelcomments(secondLevelcomments);
            }
        }

        if (jsonObject.containsKey("distance")) {
            Double distance = jsonObject.getDouble("distance");
            if (distance != null) {
                distance = jsonObject.getDoubleValue("distance");
                feed.setDistance(distance);
            }
        } else {
            if (!bUpdate)
                feed.setDistance(-1.0);
        }

        if (jsonObject.containsKey("timeStamp")) {
            Long timeStamp = jsonObject.getLong("timeStamp");
            if (timeStamp != null) {
                timeStamp = jsonObject.getLongValue("timeStamp");
                feed.setTimeStamp(timeStamp);
            }
        } else {
            if (!bUpdate)
                feed.setTimeStamp(-1);
        }

        if (jsonObject.containsKey("handPick")) {
            Boolean bHandPick = jsonObject.getBoolean("handPick");
            if (bHandPick != null) {
                bHandPick = jsonObject.getBooleanValue("handPick");
                feed.setHandPick(bHandPick);
            }
        } else {
//            feed.setHandPick(false);
        }

        if (jsonObject.containsKey("eatState")) {
            Integer eatState = jsonObject.getInteger("eatState");
            if (eatState != null) {
                eatState = jsonObject.getIntValue("eatState");
                feed.setEatState(eatState);
            }
        } else {
            if (!bUpdate)
                feed.setEatState(-1);
        }

        if (jsonObject.containsKey("foodNoteId")) {
            feed.setFoodNoteId(jsonObject.getString("foodNoteId"));
        } else {
            feed.setFoodNoteId(null);
        }

        if (jsonObject.containsKey("isExpaned")) {
            Boolean bIsExpaned = jsonObject.getBoolean("isExpaned");
            if (bIsExpaned != null) {
                bIsExpaned = jsonObject.getBooleanValue("isExpaned");
                feed.setExpaned(bIsExpaned);
            }
        } else {
//            feed.setExpaned(false);
        }
        return feed;
    }
}
