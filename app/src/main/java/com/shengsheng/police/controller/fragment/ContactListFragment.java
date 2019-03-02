package com.shengsheng.police.controller.fragment;

import android.content.Intent;
import android.view.View;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.shengsheng.police.R;
import com.shengsheng.police.controller.activity.AddContactActivity;

//联系人列表页面
public class ContactListFragment extends EaseContactListFragment {
    @Override
    protected void initView() {
        super.initView();
        //布局显示加号
        titleBar.setRightImageResource(R.drawable.em_add);
        //头布局添加
        View headerView = View.inflate(getActivity(), R.layout.header_fragment_contact, null);
        listView.addHeaderView(headerView);
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
    }
}
