package com.shengsheng.police.controller.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.shengsheng.police.R;
import com.shengsheng.police.model.Model;

//创建新群
public class NewGroupActivity extends AppCompatActivity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_desc;
    private CheckBox cb_newgroup_public;
    private CheckBox cb_newgroup_invite;
    private Button bt_newgroup_create;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        initView();
        initListener();
    }

    private void initListener() {
        //创建按钮的点击事件处理
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到选择联系人的页面
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            //创建群
            createGroup(data.getStringArrayExtra("members"));
        }
    }
    //创建群
    private void createGroup(final String[] members) {
        //获取群名称
        final String groupName = et_newgroup_name.getText().toString();
        //群描述
        final String groupDesc = et_newgroup_desc.getText().toString();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器创建群
                //参数一：群名称 参数二：群描述 参数三：群成员 参数四：创建群的原因 参数五：参数设置
                EMGroupOptions options=new EMGroupOptions();
                options.maxUsers=200;//设置群成员上线为200人
                EMGroupManager.EMGroupStyle groupStyle=null;
                if(cb_newgroup_public.isChecked())
                {
                    //公开本群
                    if(cb_newgroup_invite.isChecked())
                    {
                        //开放了群邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;

                    }
                    else
                    {
                        //没有开放群邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;

                    }
                }
                else
                {
                    //不公开本群
                    if(cb_newgroup_invite.isChecked())
                    {
                        //开放了群邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

                    }
                    else
                    {
                        //没有开放群邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;

                    }
                }
                options.style=groupStyle;
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName,groupDesc,members,"申请加入群",options);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void initView() {
        View et_newgroup_name = findViewById(R.id.et_newgroup_name);
        View et_newgroup_desc = findViewById(R.id.et_newgroup_desc);
        View cb_newgroup_public = findViewById(R.id.cb_newgroup_public);
        View cb_newgroup_invite = findViewById(R.id.cb_newgroup_invite);
        View bt_newgroup_create = findViewById(R.id.bt_newgroup_create);
        
    }
}
