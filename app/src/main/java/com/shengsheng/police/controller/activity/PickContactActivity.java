package com.shengsheng.police.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.shengsheng.police.R;
//选择联系人的页面
public class PickContactActivity extends AppCompatActivity {
    private TextView tv_pick_save;
    private ListView lv_pick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        
        initView();
    }

    private void initView() {
        TextView tv_pick_save = (TextView) findViewById(R.id.tv_pick_save);
        ListView lv_pick = (ListView) findViewById(R.id.lv_pick);

    }
}
