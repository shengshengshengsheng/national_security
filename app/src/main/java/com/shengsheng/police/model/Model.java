package com.shengsheng.police.model;

import android.content.Context;

import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.model.dao.UserAccountDao;
import com.shengsheng.police.model.db.DBManager;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层全局类
public class Model {
    private Context mContext;
    private ExecutorService executor= Executors.newCachedThreadPool();
    //创建对象
    private static Model model = new Model();
    private UserAccountDao userAccountDao;
    private DBManager dbManager;
    //私有化构造
    private Model() {

    }

    //获取单例对象
    public static Model getInstance() {
        return model;
    }

    //创建一个初始化的方法
    public void init(Context context)
    {
        mContext=context;
        //创建用户账号数据库的操作类对象
        userAccountDao = new UserAccountDao(mContext);
        //开启全局监听
        EventListener eventListener=new EventListener(mContext);
    }
    //获取全局线程池对象
    public ExecutorService getGlobalThreadPool()
    {
        return executor;
    }
    //用户登录成功的处理方法
    public void loginSuccess(UserInfo account) {
        if (account ==null)
        {
           return;
        }
        if(dbManager!=null)
        {
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getName());
    }
    public DBManager getDbManager(){
        return dbManager;
    }
    //获取用户账号数据库的操作类
    public UserAccountDao getUserAccountDao()
    {
        return userAccountDao;
    }
}
