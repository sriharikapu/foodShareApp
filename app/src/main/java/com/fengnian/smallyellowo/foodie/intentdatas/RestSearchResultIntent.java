package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.results.HotSearchWordResult;

/**
 * Created by Administrator on 2016-9-6.
 */
public class RestSearchResultIntent extends IntentData {
    public static final int TYPE_CLASS = 0;
    public static final int TYPE_AREA = 1;
    public static final int TYPE_HOT_WORD = 2;
    public static final int TYPE_Key_WORD = 3;
    public static final int TYPE_NEARBY = 4;
    /**
     * 类型
     */
    private int type;
    /**
     * 商圈或者分类
     */
    private String content;
    private HotSearchWordResult.HotWord word;
    private String keyWord;
    private SiftBean.BusinessListBean.BusinessGroup.Business area;

    public HotSearchWordResult.HotWord getWord() {
        return word;
    }

    public void setWord(HotSearchWordResult.HotWord word) {
        this.word = word;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RestSearchResultIntent() {
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        type = TYPE_Key_WORD;
        this.keyWord = keyWord;
    }

    public SiftBean.BusinessListBean.BusinessGroup.Business getArea() {
        return area;
    }

    public void setArea(SiftBean.BusinessListBean.BusinessGroup.Business area) {
        this.area = area;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeParcelable(this.word, flags);
        dest.writeString(this.keyWord);
        dest.writeParcelable(this.area, flags);
    }

    protected RestSearchResultIntent(Parcel in) {
        super(in);
        this.type = in.readInt();
        this.content = in.readString();
        this.word = in.readParcelable(HotSearchWordResult.HotWord.class.getClassLoader());
        this.keyWord = in.readString();
        this.area = in.readParcelable(SiftBean.BusinessListBean.BusinessGroup.Business.class.getClassLoader());
    }

    public static final Creator<RestSearchResultIntent> CREATOR = new Creator<RestSearchResultIntent>() {
        @Override
        public RestSearchResultIntent createFromParcel(Parcel source) {
            return new RestSearchResultIntent(source);
        }

        @Override
        public RestSearchResultIntent[] newArray(int size) {
            return new RestSearchResultIntent[size];
        }
    };
}
