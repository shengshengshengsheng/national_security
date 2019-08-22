package com.shengsheng.police.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.shengsheng.police.R;
import com.shengsheng.police.model.Model;

public class RegisterActivity extends Activity {
    private EditText et_user_name;
    private EditText et_password;
    private EditText et_password_again;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化控件
        initView();
        //初始化监听
        initListener();
    }

    private void initListener() {
        //注册按钮的点击事件处理
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册的业务逻辑处理
                register();
            }
        });
    }

    //注册的业务逻辑处理
    private void register() {
        //1.获取用户输入的用户名和密码
        final String registerName = et_user_name.getText().toString();
        final String registerPwd = et_password.getText().toString();
        final String registerPwdAgain = et_password_again.getText().toString();
        //2.校验用户输入的用户名和密码是否为空，判断两次输入的密码是否相同
        if (TextUtils.isEmpty(registerName) || TextUtils.isEmpty(registerPwd) || TextUtils.isEmpty(registerPwdAgain)) {
            Toast.makeText(RegisterActivity.this, "输入的用户名或密码不能为空，请重新输入", Toast.LENGTH_SHORT).show();
            return;

        } else if (!registerPwd.equals(registerPwdAgain)) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不同，请确认后输入", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //3.去服务器注册账号
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //去环信服务器注册账号
                        EMClient.getInstance().createAccount(registerName, registerPwd);
                        //更新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册成功,去登录", Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();
                                //跳转到登录界面
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        //注册失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

    }

    private void initView() {
        et_user_name = findViewById(R.id.et_user_name);
        et_password = findViewById(R.id.et_password);
        et_password_again = findViewById(R.id.et_password_again);
        btn_register = findViewById(R.id.btn_register);
    }
}
