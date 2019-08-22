package com.shengsheng.police.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.shengsheng.police.model.bean.UserAccountDB;
import com.shengsheng.police.model.bean.UserInfo;

//用户账号数据库的操作类
public class UserAccountDao {
    public final UserAccountDB mHelper;
    public UserAccountDao(Context context) {
         mHelper =new UserAccountDB(context);
    }
    //添加用户到数据库的方法
    public void addAccount(UserInfo user)
    {
        //验证
        if (user==null){
            return;
        }
        //获取数据库对象
       SQLiteDatabase db= mHelper.getReadableDatabase();
        //执行添加操作
        ContentValues values=new ContentValues();
        values.put(UserAccountTable.COL_HXID,user.getHxid());
        values.put(UserAccountTable.COL_NAME,user.getName());
        values.put(UserAccountTable.COL_NICK,user.getNick());
        values.put(UserAccountTable.COL_PHOTO,user.getPhoto());
//        public long replace(String table, String nullColumnHack, ContentValues initialValues)
        db.replace(UserAccountTable.TAB_NAME,null,values);
    }
    //更新用户信息
    public void updateAccount(UserInfo user)
    {
        //验证
        if (user==null){
            return;
        }
        //获取数据库对象
        SQLiteDatabase db= mHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        String[] args={String.valueOf(user.getHxid())};
        values.put(UserAccountTable.COL_HXID,user.getHxid());
        values.put(UserAccountTable.COL_NAME,user.getName());
        values.put(UserAccountTable.COL_NICK,user.getNick());
        values.put(UserAccountTable.COL_PHOTO,user.getPhoto());
        db.update(UserAccountTable.TAB_NAME,values,"hxid=?",args);
    }
    //根据环信id获取所有用户信息
    public UserInfo getAccountByHxId(String hxId)
    {
        if (hxId == null || TextUtils.isEmpty(hxId)){
            return null;
        }
        //获取数据库对象
       SQLiteDatabase db= mHelper.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+UserAccountTable.TAB_NAME+" where "+UserAccountTable.COL_HXID+"=?";
       Cursor cursor=db.rawQuery(sql,new String[]{hxId});
        UserInfo userInfo=null;
       if(cursor.moveToNext())
       {
           userInfo=new UserInfo();
           //封装对象
           userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
           userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
           userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
           userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
       }
        //关闭资源
        cursor.close();
        //返回数据
        return userInfo;
    }
}
