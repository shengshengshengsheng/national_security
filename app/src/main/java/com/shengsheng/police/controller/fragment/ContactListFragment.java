package com.shengsheng.police.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.exceptions.HyphenateException;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.activity.AddContactActivity;
import com.shengsheng.police.controller.activity.ChatActivity;
import com.shengsheng.police.controller.activity.GroupListActivity;
import com.shengsheng.police.controller.activity.InviteActivity;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.utils.Constant;
import com.shengsheng.police.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//联系人列表页面
public class ContactListFragment extends EaseContactListFragment {
    private ImageView iv_contact_red;
    private LocalBroadcastManager mLBM;
    private String mHxid;
    private BroadcastReceiver ContactInviteChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };
    private LinearLayout ll_contact_invite;
    private BroadcastReceiver ContactChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refreshContact();
        }
    };
    private BroadcastReceiver GroupChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //显示红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        //布局显示加号
        titleBar.setRightImageResource(R.drawable.em_add);
        //头布局添加
        View headerView = View.inflate(getActivity(), R.layout.header_fragment_contact, null);
        listView.addHeaderView(headerView);

        //获取红点对象
        iv_contact_red = headerView.findViewById(R.id.iv_contact_red);
        //获取邀请信息条目点击事件
        ll_contact_invite = headerView.findViewById(R.id.ll_contact_invite);
        //设置联系人条目的点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                if(user==null)
                {
                    return;
                }
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);
            }
        });
        //跳转到群组列表页面
        LinearLayout ll_contact_group = headerView.findViewById(R.id.ll_contact_group);
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //添加按钮的点击事件处理
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });
        //初始化红点显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_contact_red.setVisibility(isNewInvite?View.VISIBLE:View.GONE);

        //邀请信息条目的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //红点处理
                iv_contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,false);
                //跳转到邀请信息界面
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactInviteChangeReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(ContactChangeReceiver,new IntentFilter(Constant.CONTACT_CHANGED));
        mLBM.registerReceiver(GroupChangeReceiver,new IntentFilter(Constant.GROUP_INVITE_CHAGED));
        //从环信服务器获取所有的联系人信息
        getContactFromHxServer();
        //绑定listview和contextmenu
        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取环信ID
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
         mHxid = easeUser.getUsername();
        //添加删除页面布局
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.contact_delete)
        {
            //执行删除选中的联系人操作
            deleteContact();
            return true;

        }
        return super.onContextItemSelected(item);
    }
    //执行删除选中的联系人操作
    private void deleteContact() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //从环信服务器中删除联系人
                try {
                    EMClient.getInstance().contactManager().deleteContact(mHxid);
                    //本地数据库的更新
                    Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(mHxid);
                    if(getActivity()==null)
                    {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast提示
                            Toast.makeText(getActivity(), "删除"+mHxid+"成功", Toast.LENGTH_SHORT).show();

                            //刷新页面
                            refreshContact();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if(getActivity()==null)
                    {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "删除"+mHxid+"失败", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }

    //从环信服务器获取所有的联系人信息
    private void getContactFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取到所有的好友的环信id
                    List<String> hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //校验
                    if(hxids!=null&&hxids.size()>=0)
                    {
                        //转换
                        List<UserInfo>contacts=new ArrayList<UserInfo>();
                        for (String hxid : hxids)
                        {
                            UserInfo userInfo = new UserInfo(hxid);
                            contacts.add(userInfo);
                        }
                        //保存好友信息到本地数据库
                        Model.getInstance().getDbManager().getContactTableDao().saveContacts(contacts,true);
                        if(getActivity()==null)
                        {
                            return ;
                        }
                        //刷新页面
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新页面的方法
                                refreshContact();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void refreshContact() {//刷新联系人列表
        //获取数据
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();
        //校验
        if(contacts!=null&&contacts.size()>=0)
        {
            //设置数据
            Map<String, EaseUser> contactsMap=new HashMap<>();
            //数据转换
            for(UserInfo contact:contacts)
            {
                EaseUser easeUser = new EaseUser(contact.getHxid());
                contactsMap.put(contact.getHxid(),easeUser);
            }
            setContactsMap(contactsMap);
            //刷新页面
            refresh();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mLBM.unregisterReceiver(ContactInviteChangeReceiver);
        mLBM.unregisterReceiver(ContactChangeReceiver);
        mLBM.unregisterReceiver(GroupChangeReceiver);
    }
}
