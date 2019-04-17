package com.shengsheng.police.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.shengsheng.police.model.bean.GroupInfo;
import com.shengsheng.police.model.bean.InvitationInfo;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.utils.Constant;
import com.shengsheng.police.utils.SpUtils;

import java.util.List;

public class EventListener {
    private Context mContext;
    private final LocalBroadcastManager mLBM;
    public EventListener(Context context) {
        mContext = context;
        //本地广播
        mLBM = LocalBroadcastManager.getInstance(mContext);
        //        注册联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactlistener);
        //注册一个群信息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(eMGroupChangeListener);
    }

    //群信息变化的监听
    private final EMGroupChangeListener eMGroupChangeListener = new EMGroupChangeListener() {
        //收到群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //添加邀请到数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_INVITE);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,inviter));
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));
        }


        //收到群申请通知
        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {
            InvitationInfo invitation = new InvitationInfo();
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION);
            invitation.setGroupInfo(new GroupInfo(groupName,groupId,applicant));

            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));
        }
        //收到群申请被接受
        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setReason("");
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            invitation.setGroupInfo(new GroupInfo(groupName,groupId,accepter));
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));

        }
        //收到群申请被拒绝
        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
            InvitationInfo invitation = new InvitationInfo();
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
            invitation.setGroupInfo(new GroupInfo(groupName,groupId,decliner));
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));

        }
        //收到群邀请被同意
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));

        }

        //收到群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,invitee));
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));
        }
        //群成员的删除
        @Override
        public void onUserRemoved(String s, String s1) {
        }
        //群解散
        @Override
        public void onGroupDestroyed(String s, String s1) {
        }
        //收到群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setReason(inviteMessage);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            invitation.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHAGED));

        }
        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {
        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {
        }

        @Override
        public void onAdminAdded(String s, String s1) {

        }

        @Override
        public void onAdminRemoved(String s, String s1) {

        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {

        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    };
    private final EMContactListener emContactlistener = new EMContactListener() {
        //增加了联系人时回调此方法  当你同意添加好友
        @Override
        public void onContactAdded(String hxid) {
            //保存联系人
            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(hxid), true);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //被删除时回调此方法
        @Override
        public void onContactDeleted(String hxid) {

            //删除邀请信息
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);
            //删除联系人
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //收到好友邀请  别人加你
        @Override
        public void onContactInvited(String hxid, String reason) {

            //加到邀请信息表
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUserInfo(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);//新邀请
            //数据库更新
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //好友请求被同意  你加别人的时候 别人同意了
        @Override
        public void onFriendRequestAccepted(String hxid) {
            //添加到邀请信息表
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUserInfo(new UserInfo(hxid));
            invitationInfo.setReason("邀请被接受");
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//别人同意了你的邀请
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //好友请求被拒绝  你加别人 别人拒绝了
        @Override
        public void onFriendRequestDeclined(String s) {

            //保存小红点的状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
