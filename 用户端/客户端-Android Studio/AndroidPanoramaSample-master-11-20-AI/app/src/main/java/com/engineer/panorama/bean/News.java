package com.engineer.panorama.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:颉海鹏
 * @create:2022-06-22:20
 **/

public class News implements Serializable {
    private Long uid;
    private String newsTitle;
    private String newsContent;
    private String newsSource;
    private String createTime;
    private byte newsDel;

    public News() {
    }

    public News(Long uid, String newsTitle, String newsContent, String newsSource, String createTime, byte newsDel) {
        this.uid = uid;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsSource = newsSource;
        this.createTime = createTime;
        this.newsDel = newsDel;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public byte getNewsDel() {
        return newsDel;
    }

    public void setNewsDel(byte newsDel) {
        this.newsDel = newsDel;
    }

    @Override
    public String toString() {
        return "News{" +
                "uid=" + uid +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsContent='" + newsContent + '\'' +
                ", newsSource='" + newsSource + '\'' +
                ", createTime='" + createTime + '\'' +
                ", newsDel=" + newsDel +
                '}';
    }
}
