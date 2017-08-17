package com.fengnian.smallyellowo.foodie.bean.results;

import com.alibaba.fastjson.JSON;
import com.fengnian.smallyellowo.foodie.bean.JifenBean;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodTagModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-6.
 */
public class ConfigResult extends BaseResult implements Serializable {
    private ConfigData config;

    public ConfigData getConfig() {
        return config;
    }

    public void setConfig(ConfigData config) {
        this.config = config;
    }

    public static class ConfigData implements Serializable {
        private long interfaceVersion;
        private JifenBean jifen;
        private SiftBean sift;
        private HotWordBean hotword;

        private MubanImages moban;

        public ArrayList<SYFoodTagModel> getTags() {
            return tags;
        }

        public void setTags(ArrayList<SYFoodTagModel> tags) {
            this.tags = tags;
        }

        private ArrayList<SYFoodTagModel> tags;

        public MubanImages getMoban() {
            return moban;
        }

        public void setMoban(String moban) {
            this.moban = JSON.parseObject(moban, MubanImages.class);
        }

//        public void setMoban(MubanImages moban) {
//            this.moban = moban;
//        }


        public HotWordBean getHotword() {
            return hotword;
        }

        public void setHotword(HotWordBean hotword) {
            this.hotword = hotword;
        }


        public long getInterfaceVersion() {
            return interfaceVersion;
        }

        public void setInterfaceVersion(long interfaceVersion) {
            this.interfaceVersion = interfaceVersion;
        }

        public JifenBean getJifen() {
            return jifen;
        }

        public void setJifen(JifenBean jifen) {
            this.jifen = jifen;
        }

        public SiftBean getSift() {
            return sift;
        }

        public void setSift(SiftBean sift) {
            this.sift = sift;
        }
    }

    public static class HotWordBean implements Serializable {

        private List<ShortContentsBean> shortContents;

        public List<ShortContentsBean> getShortContents() {
            return shortContents;
        }

        public void setShortContents(List<ShortContentsBean> shortContents) {
            this.shortContents = shortContents;
        }

        public static class ShortContentsBean implements Serializable {
            private int id;
            private String content;

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
        }
    }

    public static class MubanImages implements Serializable{

        private List<ResListBean> resList;

        public List<ResListBean> getResList() {
            return resList;
        }

        public void setResList(List<ResListBean> resList) {
            this.resList = resList;
        }
            public static class ResListBean  implements Serializable{

            private String id;
            private int type;
            private int startIndex;
            private List<ItemsBean> items;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public void setStartIndex(int startIndex) {
                this.startIndex = startIndex;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean  implements Serializable{
                /**
                 * image : {"fileNmae":"sy_moban_items_biaozhun_1","height":1407,"uploading":true,"id":"","width":1053,"key":"6979687df56e68810474be3610a35cd1.jpg","url":"http: //smallyellowotest.tinydonuts.cn/6979687df56e68810474be3610a35cd1.jpg"}
                 * id :
                 */

                private MuBanImage image;
                private String id;

                public MuBanImage getImage() {
                    return image;
                }

                public void setImage(MuBanImage image) {
                    this.image = image;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public static class MuBanImage  implements Serializable{
                    /**
                     * fileNmae : sy_moban_items_biaozhun_1
                     * height : 1407
                     * uploading : true
                     * id :
                     * width : 1053
                     * key : 6979687df56e68810474be3610a35cd1.jpg
                     * url : http: //smallyellowotest.tinydonuts.cn/6979687df56e68810474be3610a35cd1.jpg
                     */

                    private String fileNmae;
                    private int height;
                    private boolean uploading;
                    private String id;
                    private int width;
                    private String key;
                    private String url;

                    public String getFileNmae() {
                        return fileNmae;
                    }

                    public void setFileNmae(String fileNmae) {
                        this.fileNmae = fileNmae;
                    }

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public boolean isUploading() {
                        return uploading;
                    }

                    public void setUploading(boolean uploading) {
                        this.uploading = uploading;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }
        }
    }

}
