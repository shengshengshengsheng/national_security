package com.shengsheng.police.controller.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.fragment.SettingFragment;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.UserAccountDB;
import com.shengsheng.police.model.bean.UserInfo;

public class ChangeInfoActivity extends AppCompatActivity {
    private ImageView iv_head;//头像
    private EditText et_info_account;//账号
    private EditText et_info_name;//姓名
    private EditText et_info_nick;//昵称
    private Button bt_save_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        UserInfo userInfo= Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
        et_info_account.setText(userInfo.getHxid());
        et_info_account.setClickable(false);
        et_info_account.setFocusable(false);
        et_info_account.setFocusableInTouchMode(false);
        et_info_name.setText(userInfo.getName());
        et_info_nick.setText(userInfo.getNick());

    }

    private void initListener() {
        //头像的点击事件处理
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        //保存按钮的点击事件处理
        bt_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }
//上传头像
    private void updateImage() {

    }

    //保存用户相关信息
    private void saveInfo() {
        final String new_name=et_info_name.getText().toString();
        final String account=et_info_account.getText().toString();
        final String new_nick=et_info_nick.getText().toString();
        Model.getInstance().getUserAccountDao().updateAccount(new UserInfo(new_name,account,new_nick));
        Toast.makeText(ChangeInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
        //跳转到主界面
//        Intent intent = new Intent(ChangeInfoActivity.this, SettingFragment.class);
//        startActivity(intent);
        finish();
    }

    private void initView() {
        iv_head=findViewById(R.id.iv_head);
        et_info_account=findViewById(R.id.et_info_account);
        et_info_name=findViewById(R.id.et_info_name);
        et_info_nick=findViewById(R.id.et_info_nick);
        bt_save_change=findViewById(R.id.bt_save_change);
    }
}
