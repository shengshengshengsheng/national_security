package com.shengsheng.police.model.dao;
//邀请人信息的表
public class InviteTable {
    public static final String TABLE_NAME = "tab_invite"; //表名
    public static final String COL_USER_NAME = "user_name"; //联系人名字
    public static final String COL_USER_HXID = "user_hxid"; //联系人环信ID
    public static final String COL_GROUP_NAME = "group_name"; //群组名称
    public static final String COL_GROUP_HXID = "group_hxid"; //群组ID
    public static final String COL_REASON = "reason"; //邀请理由
    public static final String COL_STATUS = "status"; //邀请状态
    public static final String CREATE_TAB = "create table "
            +TABLE_NAME + "("
            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_HXID + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " Integer);";
}
