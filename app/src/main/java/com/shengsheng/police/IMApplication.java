package com.shengsheng.police;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.util.EMLog;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.model.dao.ContactTable;
import com.shengsheng.police.model.dao.ContactTableDao;
import com.shengsheng.police.utils.SpUtils;

import java.util.List;

import static com.hyphenate.chat.EMClient.TAG;
import static com.hyphenate.easeui.EaseConstant.USER_NICK;
import static com.hyphenate.easeui.EaseConstant.USER_PHOTO;


public class IMApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后才能接受群邀请
        EaseUI.getInstance().init(this, options);

        //初始化数据模型层类
        Model.getInstance().init(this);
        //初始化全局上下文对象
        mContext = this;

        setEaseUIProviders();
        registerMessageListener();
    }

    //获取全局上下文对象
    public static Context getGlobalApplication() {
        return mContext;
    }

    protected void setEaseUIProviders() {
        EaseUI easeUI1 = EaseUI.getInstance();
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI1.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    public EaseUser getUserInfo(String username) {
        //获取 EaseUser实例, 这里从内存中读取
        //如果你是从服务器中读读取到的，最好在本地进行缓存
        EaseUser user = null;
        if (username == null) {
            Toast.makeText(mContext, username + "", Toast.LENGTH_SHORT).show();
            return null;
        }
//如果用户是本人，就设置自己的头像
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            user = new EaseUser(username);
            user.setAvatar((String) SpUtils.getInstance().getString("userAvatar", ""));
            user.setNickname((String) SpUtils.getInstance().getString("userName", ""));
        } else {//设置对方头像昵称
            user = new EaseUser(username);
            user.setAvatar((String) SpUtils.getInstance().getString("userAvatar", ""));
            user.setNickname((String) SpUtils.getInstance().getString("userName", ""));
        }
        return user;
    }

    protected void registerMessageListener() {
        EMMessageListener messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());

                    //接收并处理扩展消息
                    String userName = message.getStringAttribute(USER_NICK, "");
                    String userPic = message.getStringAttribute(USER_PHOTO, "");
                    String hxIdFrom = message.getFrom();
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNickname(userName);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
            }

        };
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

}