package com.fengnian.smallyellowo.foodie.bean;

import java.io.Serializable;
import java.util.List;

public class JifenBean implements Serializable {
    private int id;

    private RichFeedBean richFeed;

//    private ShortFeedBean shortFeed;
private RichFeedBean shortFeed;
    private List<ItemListBean5> itemList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RichFeedBean getRichFeed() {
        return richFeed;
    }

    public void setRichFeed(RichFeedBean richFeed) {
        this.richFeed = richFeed;
    }

//    public ShortFeedBean getShortFeed() {
//        return shortFeed;
//    }
//
//    public void setShortFeed(ShortFeedBean shortFeed) {
//        this.shortFeed = shortFeed;
//    }


    public RichFeedBean getShortFeed() {
        return shortFeed;
    }

    public void setShortFeed(RichFeedBean shortFeed) {
        this.shortFeed = shortFeed;
    }

    public List<ItemListBean5> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean5> itemList) {
        this.itemList = itemList;
    }

    public static class RichFeedBean implements Serializable {
        private int id;

        private List<ListBean1> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ListBean1> getList() {
            return list;
        }

        public void setList(List<ListBean1> list) {
            this.list = list;
        }

        public static class ListBean1 implements Serializable {
            private int id;
            private double score;
            /**
             * id : 1
             * itemCode : 1
             * content : null
             * minValue : null
             */

            private List<ListBean2> list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public List<ListBean2> getList() {
                return list;
            }

            public void setList(List<ListBean2> list) {
                this.list = list;
            }

            public static class ListBean2 implements Serializable {
                private int id;
                private int itemCode;
                private String content;
                private String minValue;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getItemCode() {
                    return itemCode;
                }

                public void setItemCode(int itemCode) {
                    this.itemCode = itemCode;
                }

                public String getMinValue() {
                    return minValue;
                }

                public void setMinValue(String minValue) {
                    this.minValue = minValue;
                }

                public String getContent() {

                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }
    }

    public static class ShortFeedBean implements Serializable {
        private int id;
        /**
         * id : 5
         * score : 0.5
         * list : [{"id":11,"itemCode":1,"content":null,"minValue":null},{"id":12,"itemCode":2,"content":null,"minValue":null},{"id":13,"itemCode":3,"content":null,"minValue":null}]
         */

        private List<ListBean3> list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ListBean3> getList() {
            return list;
        }

        public void setList(List<ListBean3> list) {
            this.list = list;
        }

        public static class ListBean3 implements Serializable {
            private int id;
            private double score;
            /**
             * id : 11
             * itemCode : 1
             * content : null
             * minValue : null
             */

            private List<ListBean4> list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public List<ListBean4> getList() {
                return list;
            }

            public void setList(List<ListBean4> list) {
                this.list = list;
            }

            public static class ListBean4 implements Serializable {
                private int id;
                private int itemCode;
                private String content;
                private String minValue;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getItemCode() {
                    return itemCode;
                }

                public void setItemCode(int itemCode) {
                    this.itemCode = itemCode;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getMinValue() {
                    return minValue;
                }

                public void setMinValue(String minValue) {
                    this.minValue = minValue;
                }
            }
        }
    }

    public static class ItemListBean5 implements Serializable {
        private int id;
        private int itemCode;
        private String content;
        private String minValue;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getItemCode() {
            return itemCode;
        }

        public void setItemCode(int itemCode) {
            this.itemCode = itemCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMinValue() {
            return minValue;
        }

        public void setMinValue(String minValue) {
            this.minValue = minValue;
        }
    }
}