package com.engineer.panorama.login;

import org.litepal.crud.LitePalSupport;

/**
 * 用户信息
 */
public class UserInfo extends LitePalSupport {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private long id;                       //用户ID号

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfo(String userName, String userPwd, long id) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.id = id;
    }

    public UserInfo() {
    }
}