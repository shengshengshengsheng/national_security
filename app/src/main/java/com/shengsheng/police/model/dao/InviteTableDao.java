package com.shengsheng.police.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.shengsheng.police.model.bean.GroupInfo;
import com.shengsheng.police.model.bean.InvitationInfo;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//邀请信息表的操作类
public class InviteTableDao {
    private DBHelper mHelpter;

    public InviteTableDao(DBHelper dbHelpter) {
        this.mHelpter = dbHelpter;
    }

    // 添加邀请
    public void addInvitation(InvitationInfo invitationInfo) {
        //校验
        if (invitationInfo == null) {
            return;
        }
        //获取连接
        SQLiteDatabase db = mHelpter.getReadableDatabase();
        //执行添加语句
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(InviteTable.COL_REASON,invitationInfo.getReason());//原因
        contentvalues.put(InviteTable.COL_STATUS,invitationInfo.getStatus().ordinal());//状态

        UserInfo user = invitationInfo.getUserInfo();
        if (user == null) {
            //群组邀请
            contentvalues.put(InviteTable.COL_GROUP_HXID, invitationInfo.getGroupInfo().getGroupId());
            contentvalues.put(InviteTable.COL_GROUP_NAME, invitationInfo.getGroupInfo().getGroupName());
            contentvalues.put(InviteTable.COL_USER_HXID, invitationInfo.getGroupInfo().getInvitePerson());

        } else {
            //联系人邀请
            contentvalues.put(InviteTable.COL_USER_HXID, invitationInfo.getUserInfo().getHxid());
            contentvalues.put(InviteTable.COL_USER_NAME, invitationInfo.getUserInfo().getName());
        }
        contentvalues.put(InviteTable.COL_REASON, invitationInfo.getReason());
        contentvalues.put(InviteTable.COL_STATUS, invitationInfo.getStatus().ordinal());
        db.replace(InviteTable.TABLE_NAME, null, contentvalues);
    }

    // 获取所有邀请信息
    public List<InvitationInfo> getInvitations(){
        //获取连接
        SQLiteDatabase database = mHelpter.getReadableDatabase();
        //执行查询语句
        String sql = "select * from "+InviteTable.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        List<InvitationInfo> invitationInfos = new ArrayList<>();

        while (cursor.moveToNext()){
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            String groupid = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));

            if (groupid==null){
                //联系人的邀请信息
                UserInfo userInfo = new UserInfo();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));

                invitationInfo.setUserInfo(userInfo);
            }else{
                //群组的邀请人信息
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                invitationInfo.setGroupInfo(groupInfo);
            }
            //添加本次循环的邀请信息到总的几何
            invitationInfos.add(invitationInfo);
        }
        cursor.close();
        return invitationInfos;
    }
    // 将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;

    }

    // 删除邀请
    public void removeInvitation(String hxId){
        //校验
        if (TextUtils.isEmpty(hxId)){
            return;
        }
        SQLiteDatabase database = mHelpter.getReadableDatabase();

        database.delete(InviteTable.TABLE_NAME,
                InviteTable.COL_USER_HXID+"=?",new String[]{hxId});
    }

    // 更新邀请状态
    public void updateInvitationStatus(
            InvitationInfo.InvitationStatus invitationStatus, String hxId){

        if (TextUtils.isEmpty(hxId)){
            return;
        }
        //获取数据库连接
        SQLiteDatabase database = mHelpter.getReadableDatabase();
        //执行更新操作
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(InviteTable.COL_STATUS,invitationStatus.ordinal());

        //第一个参数表名，第二个参修改的字段和值 第三个参数条件选择 第四个参数条件选择的值
        database.update(InviteTable.TABLE_NAME,contentvalues,
                InviteTable.COL_USER_HXID+"=?",new String[]{hxId});
    }
}
