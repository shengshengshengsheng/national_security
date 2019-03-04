package com.shengsheng.police.model.db;

import android.content.Context;

import com.shengsheng.police.model.dao.ContactTableDao;
import com.shengsheng.police.model.dao.InviteTableDao;
//联系人和邀请信息表的操作类的管理类
public class DBManager {
    private final ContactTableDao contactTableDao;
    private final InviteTableDao invitableDao;
    private final DBHelper dbHelper;

    public DBManager(Context context, String name){
        //创建数据库
        dbHelper = new DBHelper(context, name);
        //创建联系人操作类
        contactTableDao = new ContactTableDao(dbHelper);
        //创建邀请信息操作类
        invitableDao = new InviteTableDao(dbHelper);
    }
//获取联系人表的操作类对象
    public ContactTableDao getContactTableDao(){
        return contactTableDao;
    }
//获取邀请人表的操作类对象
    public InviteTableDao getInviteTableDao(){
        return invitableDao;
    }
//关闭数据库的方法
    public void close(){
        dbHelper.close();
    }
}
