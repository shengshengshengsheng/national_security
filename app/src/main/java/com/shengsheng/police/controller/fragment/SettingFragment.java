package com.shengsheng.police.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.activity.ChangeInfoActivity;
import com.shengsheng.police.controller.activity.LoginActivity;
import com.shengsheng.police.model.Model;

//设置页面
public class SettingFragment extends Fragment {
    private Button bt_out;
    private Button bt_change_info;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bt_out=(Button) view.findViewById(R.id.bt_out);
        bt_change_info=view.findViewById(R.id.bt_change_info);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    private void initData() {
        //在button上显示当前用户名称
        bt_out.setText("退出登录("+ EMClient.getInstance().getCurrentUser()+")");
        //退出登录的逻辑显示
        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器退出
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            //退出成功
                            public void onSuccess() {
                                //关闭DBHelper
                                Model.getInstance().getDbManager().close();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新ui显示
                                        Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                                        //回到登录页面
                                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });

                            }
                            //退出失败
                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出失败"+s,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //正在退出
                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    }
                });
            }
        });
        //修改信息的点击事件处理
        bt_change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChangeInfoActivity.class);
                startActivity(intent);
            }
        });



    }
}
