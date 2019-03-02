package com.shengsheng.police.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.shengsheng.police.R;
import com.shengsheng.police.controller.fragment.ChatFragment;
import com.shengsheng.police.controller.fragment.ContactListFragment;
import com.shengsheng.police.controller.fragment.SettingFragment;
import com.shengsheng.police.controller.fragment.WorkingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private  ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;
    private WorkingFragment workingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment=null;
                switch (checkedId)
                {
                    //会话列表页面
                    case R.id.rb_main_chat:
                        fragment=chatFragment;
                    break;
                    //联系人列表页面
                    case R.id.rb_main_contact:
                        fragment= contactListFragment;
                        break;
                        //工作台页面
                    case R.id.rb_main_working:
                        fragment=workingFragment;
                        break;
                        //设置页面
                    case R.id.rb_main_setting:
                        fragment=settingFragment;
                        break;
                }
                //实现fragment的切换的方法
                switchFragment(fragment);
            }
        });
    }
    //实现fragment的切换的方法
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();


    }

    private void initData() {
        //创建四个fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
        workingFragment = new WorkingFragment();
        
    }
    private void initView() {
        rg_main=findViewById(R.id.rg_main);
    }
}
