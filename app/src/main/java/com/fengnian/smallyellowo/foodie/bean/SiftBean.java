package com.fengnian.smallyellowo.foodie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SiftBean implements Serializable, Parcelable {
    private int id;
    private BusinessListBean business;
    private FilterListBean filter;
    private SortListBean sort;
    private FoodKindListBean foodKind;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BusinessListBean getBusiness() {
        return business;
    }

    public void setBusiness(BusinessListBean business) {
        this.business = business;
    }

    public FilterListBean getFilter() {
        return filter;
    }

    public void setFilter(FilterListBean filter) {
        this.filter = filter;
    }

    public SortListBean getSort() {
        return sort;
    }

    public void setSort(SortListBean sort) {
        this.sort = sort;
    }

    public FoodKindListBean getFoodKind() {
        return foodKind;
    }

    public void setFoodKind(FoodKindListBean foodKind) {
        this.foodKind = foodKind;
    }

    public static class BusinessListBean implements Serializable, Parcelable {
        private int id;
        private List<BusinessGroup> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<BusinessGroup> getList() {
            return list;
        }

        public void setList(List<BusinessGroup> list) {
            this.list = list;
        }

        public static class BusinessGroup implements Serializable, Parcelable {
            private int id;
            private String content;
            private String regionCode;
            private String parentCode;
            private String imageUrl;
            private SYImage image;
            private List<Business> list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getRegionCode() {
                return regionCode;
            }

            public void setRegionCode(String regionCode) {
                this.regionCode = regionCode;
            }

            public String getParentCode() {
                return parentCode;
            }

            public void setParentCode(String parentCode) {
                this.parentCode = parentCode;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public SYImage getImage() {
                return image;
            }

            public void setImage(SYImage image) {
                this.image = image;
            }

            public List<Business> getList() {
                return list;
            }

            public void setList(List<Business> list) {
                this.list = list;
            }


            public static class Business implements Serializable, Parcelable {
                private String id;
                private String content;
                private String regionCode;
                private String parentCode;
                private String imageUrl;
                private SYImage image;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getRegionCode() {
                    return regionCode;
                }

                public void setRegionCode(String regionCode) {
                    this.regionCode = regionCode;
                }

                public String getParentCode() {
                    return parentCode;
                }

                public void setParentCode(String parentCode) {
                    this.parentCode = parentCode;
                }

                public Object getImageUrl() {
                    return imageUrl;
                }

                public static Creator<Business> getCREATOR() {
                    return CREATOR;
                }

                public SYImage getImage() {
                    return image;
                }

                public void setImage(SYImage image) {
                    this.image = image;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public Business() {
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                    dest.writeString(this.content);
                    dest.writeString(this.regionCode);
                    dest.writeString(this.parentCode);
                    dest.writeString(this.imageUrl);
                    dest.writeParcelable(this.image, flags);
                }

                protected Business(Parcel in) {
                    this.id = in.readString();
                    this.content = in.readString();
                    this.regionCode = in.readString();
                    this.parentCode = in.readString();
                    this.imageUrl = in.readString();
                    this.image = in.readParcelable(SYImage.class.getClassLoader());
                }

                public static final Creator<Business> CREATOR = new Creator<Business>() {
                    @Override
                    public Business createFromParcel(Parcel source) {
                        return new Business(source);
                    }

                    @Override
                    public Business[] newArray(int size) {
                        return new Business[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeString(this.regionCode);
                dest.writeString(this.parentCode);
                dest.writeString(this.imageUrl);
                dest.writeParcelable(this.image, flags);
                dest.writeTypedList(this.list);
            }

            public BusinessGroup() {
            }

            protected BusinessGroup(Parcel in) {
                this.id = in.readInt();
                this.content = in.readString();
                this.regionCode = in.readString();
                this.parentCode = in.readString();
                this.imageUrl = in.readString();
                this.image = in.readParcelable(SYImage.class.getClassLoader());
                this.list = in.createTypedArrayList(Business.CREATOR);
            }

            public static final Creator<BusinessGroup> CREATOR = new Creator<BusinessGroup>() {
                @Override
                public BusinessGroup createFromParcel(Parcel source) {
                    return new BusinessGroup(source);
                }

                @Override
                public BusinessGroup[] newArray(int size) {
                    return new BusinessGroup[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeTypedList(this.list);
        }

        public BusinessListBean() {
        }

        protected BusinessListBean(Parcel in) {
            this.id = in.readInt();
            this.list = in.createTypedArrayList(BusinessGroup.CREATOR);
        }

        public static final Creator<BusinessListBean> CREATOR = new Creator<BusinessListBean>() {
            @Override
            public BusinessListBean createFromParcel(Parcel source) {
                return new BusinessListBean(source);
            }

            @Override
            public BusinessListBean[] newArray(int size) {
                return new BusinessListBean[size];
            }
        };
    }

    public static class FilterListBean implements Serializable, Parcelable {
        private int id;

        private List<FilterBean> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<FilterBean> getList() {
            return list;
        }

        public void setList(List<FilterBean> list) {
            this.list = list;
        }

        public static class FilterBean implements Serializable, Parcelable {
            private int id;
            private String content;
            @JSONField(serialize = false)
            private String code;
            private String imageUrl;
            private boolean checked = false;

            public boolean isRealyChecked() {
                return realyChecked;
            }

            public void setRealyChecked(boolean realyChecked) {
                this.realyChecked = realyChecked;
            }

            private boolean realyChecked = false;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            @JSONField(serialize = false)
            public String getCode() {
                return code;
            }

            @JSONField(serialize = false)
            public void setCode(String code) {
                this.code = code;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public FilterBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeString(this.code);
                dest.writeString(this.imageUrl);
                dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
            }

            protected FilterBean(Parcel in) {
                this.id = in.readInt();
                this.content = in.readString();
                this.code = in.readString();
                this.imageUrl = in.readString();
                this.checked = in.readByte() != 0;
            }

            public static final Creator<FilterBean> CREATOR = new Creator<FilterBean>() {
                @Override
                public FilterBean createFromParcel(Parcel source) {
                    return new FilterBean(source);
                }

                @Override
                public FilterBean[] newArray(int size) {
                    return new FilterBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeList(this.list);
        }

        public FilterListBean() {
        }

        protected FilterListBean(Parcel in) {
            this.id = in.readInt();
            this.list = new ArrayList<FilterBean>();
            in.readList(this.list, FilterBean.class.getClassLoader());
        }

        public static final Creator<FilterListBean> CREATOR = new Creator<FilterListBean>() {
            @Override
            public FilterListBean createFromParcel(Parcel source) {
                return new FilterListBean(source);
            }

            @Override
            public FilterListBean[] newArray(int size) {
                return new FilterListBean[size];
            }
        };
    }

    public static class SortListBean implements Serializable, Parcelable {
        private int id;
        private List<SortBean> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<SortBean> getList() {
            return list;
        }

        public void setList(List<SortBean> list) {
            this.list = list;
        }

        public static class SortBean implements Serializable, Parcelable {
            private int id;
            private String content;
            private int code;
            private String imageUrl;
            private SYImage image;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public SYImage getImage() {
                return image;
            }

            public void setImage(SYImage image) {
                this.image = image;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeInt(this.code);
                dest.writeString(this.imageUrl);
                dest.writeParcelable(this.image, flags);
                dest.writeInt(this.status);
            }

            public SortBean() {
            }

            protected SortBean(Parcel in) {
                this.id = in.readInt();
                this.content = in.readString();
                this.code = in.readInt();
                this.imageUrl = in.readString();
                this.image = in.readParcelable(SYImage.class.getClassLoader());
                this.status = in.readInt();
            }

            public static final Creator<SortBean> CREATOR = new Creator<SortBean>() {
                @Override
                public SortBean createFromParcel(Parcel source) {
                    return new SortBean(source);
                }

                @Override
                public SortBean[] newArray(int size) {
                    return new SortBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeList(this.list);
        }

        public SortListBean() {
        }

        protected SortListBean(Parcel in) {
            this.id = in.readInt();
            this.list = new ArrayList<SortBean>();
            in.readList(this.list, SortBean.class.getClassLoader());
        }

        public static final Creator<SortListBean> CREATOR = new Creator<SortListBean>() {
            @Override
            public SortListBean createFromParcel(Parcel source) {
                return new SortListBean(source);
            }

            @Override
            public SortListBean[] newArray(int size) {
                return new SortListBean[size];
            }
        };
    }

    public static class FoodKindListBean implements Serializable, Parcelable {
        private int id;
        private List<FoodKindBean> foodKind;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<FoodKindBean> getFoodKind() {
            return foodKind;
        }

        public void setFoodKind(List<FoodKindBean> foodKind) {
            this.foodKind = foodKind;
        }

        public static class FoodKindBean implements Serializable, Parcelable {
            private int id;
            private String content;
            private SYImage image;
            private String imageUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public SYImage getImage() {
                return image;
            }

            public void setImage(SYImage image) {
                this.image = image;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeParcelable(this.image, flags);
                dest.writeString(this.imageUrl);
            }

            public FoodKindBean() {
            }

            protected FoodKindBean(Parcel in) {
                this.id = in.readInt();
                this.content = in.readString();
                this.image = in.readParcelable(SYImage.class.getClassLoader());
                this.imageUrl = in.readString();
            }

            public static final Creator<FoodKindBean> CREATOR = new Creator<FoodKindBean>() {
                @Override
                public FoodKindBean createFromParcel(Parcel source) {
                    return new FoodKindBean(source);
                }

                @Override
                public FoodKindBean[] newArray(int size) {
                    return new FoodKindBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeList(this.foodKind);
        }

        public FoodKindListBean() {
        }

        protected FoodKindListBean(Parcel in) {
            this.id = in.readInt();
            this.foodKind = new ArrayList<FoodKindBean>();
            in.readList(this.foodKind, FoodKindBean.class.getClassLoader());
        }

        public static final Creator<FoodKindListBean> CREATOR = new Creator<FoodKindListBean>() {
            @Override
            public FoodKindListBean createFromParcel(Parcel source) {
                return new FoodKindListBean(source);
            }

            @Override
            public FoodKindListBean[] newArray(int size) {
                return new FoodKindListBean[size];
            }
        };
    }

    public SiftBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.business, flags);
        dest.writeParcelable(this.filter, flags);
        dest.writeParcelable(this.sort, flags);
        dest.writeParcelable(this.foodKind, flags);
    }

    protected SiftBean(Parcel in) {
        this.id = in.readInt();
        this.business = in.readParcelable(BusinessListBean.class.getClassLoader());
        this.filter = in.readParcelable(FilterListBean.class.getClassLoader());
        this.sort = in.readParcelable(SortListBean.class.getClassLoader());
        this.foodKind = in.readParcelable(FoodKindListBean.class.getClassLoader());
    }

    public static final Creator<SiftBean> CREATOR = new Creator<SiftBean>() {
        @Override
        public SiftBean createFromParcel(Parcel source) {
            return new SiftBean(source);
        }

        @Override
        public SiftBean[] newArray(int size) {
            return new SiftBean[size];
        }
    };
}