package com.shengsheng.police.caseManage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.shengsheng.police.R;

import java.util.ArrayList;
import java.util.List;

public class CaseCenterActivity extends AppCompatActivity implements MyAdapter.OnGroupExpanded {

    private ExpandableListView mElistView;
    public String[] titleStrings = {"民事案件", "刑事案件", "行政案件"};

    public String[][] nameStrings = {
            {"离婚纠纷", "劳动合同纠纷", "房屋买卖合同纠纷"},
            {"危害国家安全罪", "危害公共安全罪", "破坏社会主义市场经济秩序罪", "侵犯公民人身权利"},
            {"侵犯财产罪", "危害国防利益罪", "贪污贿赂罪", "渎职罪", "军人违反职责罪"}
    };

    private MyAdapter mAdapter;
    private List<TitleInfo> mList = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_center);
        mElistView = findViewById(R.id.mElistview);
        initList();
        initAdapter();
        initListener();
    }

    /**
     * ExpandableListView条目点击事件
     */
    private void initListener() {
        //子对象点击监听事件
        mElistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(CaseCenterActivity.this,nameStrings[groupPosition][childPosition]+"",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CaseCenterActivity.this,UploadCaseInformtionActivity.class);
                startActivity(intent);
                return false;
            }
        });
        //组对象点击监听事件
        mElistView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;//请务必返回false，否则分组不会展开
            }
        });
        //组对象判断分组监听事件
        mAdapter.setOnGroupExPanded(this);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter = new MyAdapter(mList, this);
        mElistView.setAdapter(mAdapter);
    }

    /**
     * 初始化数据源
     */
    private void initList() {

        for (int i = 0; i < titleStrings.length; i++) {
            //创建组对象
            TitleInfo info= new TitleInfo();
            //循环添加组的标题名
            info.setTitle(titleStrings[i]);
            //创建子对象数据源
            List<ContentInfo> list = new ArrayList<>();
            for (int j = 0; j < nameStrings.length; j++) {
                //创建子对象
                ContentInfo info2 = new ContentInfo();
                //添加用户名或者头像
                info2.setName(nameStrings[i][j]);
                //将子对象添加到数据源
                list.add(info2);
            }
            //将子对象数据源复制给组对象
            info.setInfo(list);
            //将组对象添加到总数据源中
            mList.add(info);

        }

    }


    /**
     * 监听是否关闭其他的组对象
     * @param groupPostion
     */
    @Override
    public void onGroupExpanded(int groupPostion) {
        EListViewUtils utils=new EListViewUtils();
        utils.expandOnlyOne(groupPostion,mElistView);
    }
}
