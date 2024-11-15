package com.engineer.panorama.bean;

import java.io.Serializable;

public class Pano implements Serializable {
    private Long uid;
    private String panoName;
    private String panoAddr;
    private Long panoX;
    private Long panoY;
    private String panoUid;
    private String createTime;
    private byte newsDel;

    public Pano() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPanoName() {
        return panoName;
    }

    public void setPanoName(String panoName) {
        this.panoName = panoName;
    }

    public String getPanoAddr() {
        return panoAddr;
    }

    public void setPanoAddr(String panoAddr) {
        this.panoAddr = panoAddr;
    }

    public Long getPanoX() {
        return panoX;
    }

    public void setPanoX(Long panoX) {
        this.panoX = panoX;
    }

    public Long getPanoY() {
        return panoY;
    }

    public void setPanoY(Long panoY) {
        this.panoY = panoY;
    }

    public String getPanoUid() {
        return panoUid;
    }

    public void setPanoUid(String panoUid) {
        this.panoUid = panoUid;
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
        return "Pano{" +
                "uid=" + uid +
                ", panoName='" + panoName + '\'' +
                ", panoAddr='" + panoAddr + '\'' +
                ", panoX=" + panoX +
                ", panoY=" + panoY +
                ", panoUid='" + panoUid + '\'' +
                ", createTime='" + createTime + '\'' +
                ", newsDel=" + newsDel +
                '}';
    }
}
