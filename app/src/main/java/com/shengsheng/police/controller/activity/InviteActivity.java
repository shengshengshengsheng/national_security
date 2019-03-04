package com.shengsheng.police.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.adapter.InviteAdapter;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.InvitationInfo;
import com.shengsheng.police.utils.Constant;

import java.util.List;

//邀请信息列表页面
public class InviteActivity extends AppCompatActivity {
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager mLBM;
    private ListView lv_invite;
    private InviteAdapter.OnInviteListener mOnInviteListener=new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(final InvitationInfo invitationInfo) {
            //通知环信服务器，点击了接受按钮
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUserInfo().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDao().updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT,invitationInfo.getUserInfo().getHxid());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面发生变化
                                Toast.makeText(InviteActivity.this, "接受了邀请", Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }

        @Override
        public void onReject(final InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUserInfo().getHxid());
                        //数据库变化
                        Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(invitationInfo.getUserInfo().getHxid());
                        //页面变化
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝成功了", Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝失败了", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });

        }
    };
    private BroadcastReceiver ContactChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initView();
        initData();
    }


    private void initData() {
        //初始化listView
         inviteAdapter = new InviteAdapter(this,mOnInviteListener);
        lv_invite.setAdapter(inviteAdapter);
        //刷新方法
        refresh();
        //注册邀请信息变化的广播

        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(ContactChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));

    }

    private void refresh() {
        //获取数据库中的所有邀请信息
        List<InvitationInfo> invitations = Model.getInstance().getDbManager().getInviteTableDao().getInvitations();
        //刷新
        inviteAdapter.refresh(invitations);
    }

    private void initView() {

        lv_invite=findViewById(R.id.lv_invite);
    }
    @Override
   protected void onDestroy()
    {
        super.onDestroy();
        mLBM.unregisterReceiver(ContactChangedReceiver);
    }
}
