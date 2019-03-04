package com.shengsheng.police.controller.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.UserInfo;
public class SplashActivity extends Activity {
    private Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
            //如果当前activity已经退出，那么就不处理handler中的消息
            if(isFinishing())
            {
                return;
            }
            //判断进入主页面还是登陆页面
            toMainOrLogin();
        }
    };
    //判断进入主页面还是登陆页面
    private void toMainOrLogin() {
//        new Thread()
//        {
//            public void run()
//            {
//
//            }
//        }.start();
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断当前账号是否曾经登陆过
                if(EMClient.getInstance().isLoggedInBefore())//该账号以前登陆过
                {
                    //获取到当前登陆用户的信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                    if(account==null)
                    {
                        //当前账号为空
                        //跳转到登陆页面
                        Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        //登陆成功后的方法
                        Model.getInstance().loginSuccess(account);

                        //跳转到主页面
                        Intent intent= new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
                else//没有登陆过
                {
                    //跳转到登陆页面
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                //结束当前页面
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //发送2秒钟的延时消息
        handler.sendMessageDelayed(Message.obtain(),2000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}
