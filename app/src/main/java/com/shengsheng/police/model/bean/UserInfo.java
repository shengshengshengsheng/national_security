package com.shengsheng.police.model.bean;

import android.view.SurfaceView;

//用户账号的信息的bean类
public class UserInfo {
    public int uid;
    public SurfaceView view;
    private String name;//用户名称
    private String hxid;//环信id，用户在环信服务器中唯一的标识
    private String nick;//用户昵称
    private String photo;//用户头像
    public UserInfo()
    {

    }
    //设置用户名称、昵称、环信id为相同的name
    public UserInfo(String name)
    {
        this.name=name;
        this.hxid=name;
        this.nick=name;
    }
    public UserInfo(String name,String hxid,String nick)
    {
        this.name=name;
        this.hxid=hxid;
        this.nick=nick;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }
    public String getHxid() {
        return hxid;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getNick() {
        return nick;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
