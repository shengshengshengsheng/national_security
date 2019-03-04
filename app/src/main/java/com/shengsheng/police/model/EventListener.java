package com.shengsheng.police.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.shengsheng.police.model.bean.GroupInfo;
import com.shengsheng.police.model.bean.InvitationInfo;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.utils.Constant;
import com.shengsheng.police.utils.SpUtils;

public class EventListener {
    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context) {
        mContext = context;
//        注册联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactlistener);
        //本地广播
        mLBM = LocalBroadcastManager.getInstance(mContext);
    }

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
