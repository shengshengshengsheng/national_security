package com.shengsheng.police.controller.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shengsheng.police.R;
import com.shengsheng.police.utils.Constant;

//会话详情页面
public class ChatActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initData();
    }

    private void initData() {
        //创建一个聊天的fragment
        EaseChatFragment easeChatFragment = new EaseChatFragment();
        String mHxid = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        easeChatFragment.setArguments(getIntent().getExtras());
        //替换fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat,easeChatFragment).commit();

    }
}
