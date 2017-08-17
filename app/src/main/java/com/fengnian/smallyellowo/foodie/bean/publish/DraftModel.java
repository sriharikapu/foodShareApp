package com.fengnian.smallyellowo.foodie.bean.publish;

import android.os.Parcel;

import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * 草稿modle
 * Created by Administrator on 2016-9-29.
 */
@DatabaseTable
public class DraftModel extends XData implements Serializable {

    public List<String> hotDishList;
    /**
     * 速记
     */
    public static int TYPE_FAST = 0X1;
    /**
     * 富文本
     */
    public static int TYPE_RICH = 0X2;

    /**
     * 源：速记
     */
    public static int SOURCE_TYPE_FAST = 0X10;
    /**
     * 源：富文本
     */
    public static int SOURCE_TYPE_RICH = 0X20;

    /**
     * 来源：网络
     */
    public static int SOURCE_DATA_NET = 0X100;
    /**
     * 来源：任务
     */
    public static int SOURCE_DATA_TASK = 0X200;
    /**
     * 来源：草稿
     */
    public static int SOURCE_DATA_DRFT = 0x300;
    SYFeed feed;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    //    public ArrayList<String> getMenuList() {
//        return menuList;
//    }
//
//    public void setMenuList(ArrayList<String> menuList) {
//        this.menuList = menuList;
//    }
//
//    ArrayList<String> menuList = new ArrayList<>();
    @DatabaseField(id = true)
    public String id;


    /**
     * 源
     */
    @DatabaseField
    private int source;

    public String getFirstComtomId() {
        return firstComtomId;
    }

    public void setFirstComtomId(String firstComtomId) {
        this.firstComtomId = firstComtomId;
    }

    /**
     * 源
     */
    @DatabaseField
    private String firstComtomId;

    /**
     * 来源
     */
    @DatabaseField
    private int source_data;
    /**
     * 类型预留以后有可能会有速记的草稿
     */
    @DatabaseField
    private int type;
    /**
     * 生成时间
     */
    @DatabaseField
    private long createTime;
    /**
     * 最后编辑时间
     */
    @DatabaseField
    private long lastEditTime;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 当前用户
     */
    @DatabaseField
    private String createUser;
    /**
     * 序列化的SYFeed字符串
     */
    @DatabaseField
    private String feedString;

    /**
     * 预留以后新增功能后标记modle的不同版本
     */
    @DatabaseField
    private int version;

    /**
     * 预留以后新增功能后标记modle的不同版本
     */
    @DatabaseField
    private boolean isShareToSmallYellowO;

    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    public boolean isShareToSmallYellowO() {
        return isShareToSmallYellowO;
    }

    public void setShareToSmallYellowO(boolean shareToSmallYellowO) {
        isShareToSmallYellowO = shareToSmallYellowO;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSource_data() {
        return source_data;
    }

    public void setSource_data(int source_data) {
        this.source_data = source_data;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getFeedString() {
        return feedString;
    }

    public void setFeedString(String feedString) {
        this.feedString = feedString;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public DraftModel() {
    }


    /**
     * 全部的图片张数
     *
     * @return 图片张数
     */
    public Integer allImageCount() {
        return this.feed.getFood().allImageContent().size();
    }

    /**
     * 是否存在文字描述 (富文本文字)
     *
     * @return true存在 否则不存在
     */
    public boolean haveWordsDescription() {
        return this.feed.getFood().haveWordsDescription();
    }

    public boolean fasthaveWordsDescription() {
        return this.feed.getFood().getContent().length() >= 2 ? true : false;
    }

    /**
     * 是否存在餐厅名称
     *
     * @return true存在 否则不存在
     */
    public boolean haveRestaurantName() {
        return this.feed.getFood().haveRestaurantName();
    }

    /**
     * 是否存在就餐人数
     *
     * @return true存在 否则不存在
     */
    public boolean foodPeopleCount() {
        return this.feed.getFood().getNumberOfPeople() > 0;
    }

    /**
     * 是否存在总价
     *
     * @return true存在 否则不存在
     */
    public boolean totalPrice() {
        return this.feed.getFood().getTotalPrice() > 0.0;
    }

    /**
     * 是否存在就餐类型
     *
     * @return true存在 否则不存在
     */
    public boolean haveFoodStyle() {
        return this.feed.getFood().getFoodType() > 0;
    }

    /**
     * 是否存在评价标签和菜品
     *
     * @return true存在 否则不存在
     */
    public boolean haveCommentTagAndDishes() {
        return this.feed.getFood().haveCommentTagAndDishes();
    }


    /**
     * 是否存在封面
     *
     * @return true存在 否则不存在
     */
    public boolean haveCoverImage() {
        return this.feed.getFood().haveCoverImage();
    }

    /**
     * 是否存在封面标题
     *
     * @return true存在 否则不存在
     */
    public boolean haveCoverTitle() {
        return this.feed.getFood().haveCoverTitle();
    }

    /**
     * 是否存在美食评分
     *
     * @return true存在 否则不存在
     */
    public boolean haveCommentScorce() {
        return this.getFeed().haveCommentScorce();
    }

    public void onUnSerializable() {
        setFeed(((SYFeed) SerializeUtil.readObject(getFeedString())));
        getFeed().onUnSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.feed, flags);
        dest.writeString(this.id);
        dest.writeInt(this.source);
        dest.writeString(this.firstComtomId);
        dest.writeInt(this.source_data);
        dest.writeInt(this.type);
        dest.writeLong(this.createTime);
        dest.writeLong(this.lastEditTime);
        dest.writeString(this.createUser);
        dest.writeString(this.feedString);
        dest.writeInt(this.version);
        dest.writeByte(this.isShareToSmallYellowO ? (byte) 1 : (byte) 0);
    }

    protected DraftModel(Parcel in) {
        super(in);
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
        this.id = in.readString();
        this.source = in.readInt();
        this.firstComtomId = in.readString();
        this.source_data = in.readInt();
        this.type = in.readInt();
        this.createTime = in.readLong();
        this.lastEditTime = in.readLong();
        this.createUser = in.readString();
        this.feedString = in.readString();
        this.version = in.readInt();
        this.isShareToSmallYellowO = in.readByte() != 0;
    }

    public static final Creator<DraftModel> CREATOR = new Creator<DraftModel>() {
        @Override
        public DraftModel createFromParcel(Parcel source) {
            return new DraftModel(source);
        }

        @Override
        public DraftModel[] newArray(int size) {
            return new DraftModel[size];
        }
    };
}
