package com.fengnian.smallyellowo.foodie.bean.publics;

import java.util.List;

/**
 * Created by Administrator on 2016-9-30.
 */

public class PicGalleryEntity {
    private String path;
    private String name;
    private List<String> paths;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
