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

import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.activity.CaseCenterActivity;
import com.shengsheng.police.controller.activity.VideoActivity;
import com.shengsheng.police.daka.PunchCardActivity;
import com.shengsheng.police.model.Model;

//工作台页面
public class WorkingFragment extends Fragment {
    private Button bt_working_oneKey;
    private Button bt_working_punchCard;
    private Button bt_working_Case;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_working, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bt_working_oneKey=(Button) view.findViewById(R.id.bt_working_oneKey);
        bt_working_punchCard=(Button) view.findViewById(R.id.bt_working_punchCard);
        bt_working_Case=(Button) view.findViewById(R.id.bt_working_Case);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt_working_oneKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //跳转到一键增援页面
                        Intent intent=new Intent(getActivity(), VideoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        //出勤打卡
        bt_working_punchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //跳转到出勤打卡页面
                        Intent intent=new Intent(getActivity(), PunchCardActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        //任务中心
        bt_working_Case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //跳转到任务中心页面
                        Intent intent=new Intent(getActivity(), CaseCenterActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        initData();
    }

    private void initData() {

    }
}
