package com.shengsheng.police.model.bean;

import android.view.SurfaceView;

//用户账号的信息的bean类
public class UserInfo {
    public int uid;
    public SurfaceView view;
    private String name="name";//用户名称
    private String hxid="hxid";//环信id，用户在环信服务器中唯一的标识
    private String nick="nick";//用户昵称
    private String photo="http://b-ssl.duitang.com/uploads/item/201702/22/20170222200954_zeJWu.thumb.700_0.jpeg";//用户头像
    public UserInfo()
    {
    }
    //此处这样赋值仅为演示效果
    public UserInfo(String name)
    {
        this.name=name;
        this.hxid=name;
    }
    public UserInfo(String name,String hxid,String nick)
    {
        this.name=name;
        this.hxid=hxid;
        this.nick=nick;

    }
    public UserInfo(String name,String hxid,String nick,String pic_url)
    {
        this.name=name;
        this.hxid=hxid;
        this.nick=nick;
        this.photo=pic_url;
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
