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
import com.shengsheng.police.R;
import com.shengsheng.police.model.Model;

//工作台页面
public class WorkingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_working, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        //跳转到一键增援页面
//        bt_working_oneKey.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
//            }
//        });
        //出勤打卡
        initData();
    }

        private void initData() {

    }
}
