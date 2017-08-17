package com.fengnian.smallyellowo.foodie.bean.results;

import java.util.List;

/**
 * 检查 版本号
 */

public class CheckVersionResult extends  BaseResult{
    private String  haveNewVersion;// 是否有新的版本(1有 0当前已是最新版本)
    private String  versionNew;//	最新的版本号
    private List<String> content;//新版本更新的内容的列表数组
    private String url ;//	新版本的下载地址

    public String getHaveNewVersion() {
        return haveNewVersion;
    }

    public void setHaveNewVersion(String haveNewVersion) {
        this.haveNewVersion = haveNewVersion;
    }

    public String getVersionNew() {
        return versionNew;
    }

    public void setVersionNew(String versionNew) {
        this.versionNew = versionNew;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
