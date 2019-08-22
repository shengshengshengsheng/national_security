package com.shengsheng.police.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;

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
        et_info_account.setText(EMClient.getInstance().getCurrentUser());
        et_info_account.setClickable(false);
        et_info_account.setFocusable(false);
        et_info_account.setFocusableInTouchMode(false);
        et_info_name.setText(EMClient.getInstance().getCurrentUser());
        et_info_nick.setText(EMClient.getInstance().getCurrentUser());
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
    }

    private void initView() {
        iv_head=findViewById(R.id.iv_head);
        et_info_account=findViewById(R.id.et_info_account);
        et_info_name=findViewById(R.id.et_info_name);
        et_info_nick=findViewById(R.id.et_info_nick);
        bt_save_change=findViewById(R.id.bt_save_change);
    }
}
