package com.engineer.panorama.bean;

import java.io.Serializable;

public class IndoorPano implements Serializable {
    private Long id;
    private String addrname;
    private String pid;

    public IndoorPano() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddrname() {
        return addrname;
    }

    public void setAddrname(String addrname) {
        this.addrname = addrname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "IndoorPano{" +
                "id=" + id +
                ", addrname='" + addrname + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
