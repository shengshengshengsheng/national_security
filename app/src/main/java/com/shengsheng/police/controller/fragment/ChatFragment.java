package com.shengsheng.police.controller.fragment;
import android.content.Intent;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.shengsheng.police.controller.activity.ChatActivity;
import java.util.List;

import static com.hyphenate.easeui.EaseConstant.USER_NICK;
import static com.hyphenate.easeui.EaseConstant.USER_PHOTO;

//会话列表页面
public class ChatFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();
        //跳转到会话详情页面
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                //传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                //是否是群聊
                if(conversation.getType()== EMConversation.EMConversationType.GroupChat)
                {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                }
                startActivity(intent);
            }
        });
        //清空集合数据
        conversationList.clear();
        //监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }
    private EMMessageListener emMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {

                //接收并处理扩展消息
                String userNick = message.getStringAttribute(USER_NICK, "");
                String userPic = message.getStringAttribute(USER_PHOTO, "");
                String hxIdFrom = message.getFrom();
                EaseUser easeUser = new EaseUser(hxIdFrom);
                easeUser.setAvatar(userPic);
                easeUser.setNickname(userNick);
            }
                //设置数据
                EaseUI.getInstance().getNotifier().notify(list);
                //刷新页面
                refresh();
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
}
