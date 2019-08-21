package com.shengsheng.police.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//联系人表的操作类
public class ContactTableDao {
    private DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }

    // 获取所有联系人
    public List<UserInfo> getContacts() {
        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME
                + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfo> users = new ArrayList<>();
        //数据封装
        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            users.add(userInfo);
        }
        //关闭cursor
        cursor.close();
        return users;
    }

    // 通过环信id获取联系人单个信息
    public UserInfo getContactByHx(String hxId) {
        //校验
        if (TextUtils.isEmpty(hxId)) {
            return null;
        }

        //获取连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME
                + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
        }
        //关闭资源
        cursor.close();
        //返回数据
        return userInfo;
    }

    // 通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByHx(List<String> hxIds) {

        //校验
        if (hxIds == null || hxIds.size() == 0) {
            return null;
        }

        //封装数据
        List<UserInfo> contacts = new ArrayList<>();
        //遍历环信id来查找
        for (String hxid : hxIds) {
            UserInfo contact = getContactByHx(hxid);
            if (contact != null) {
                contacts.add(contact);
            }
        }
        return contacts;
    }

    // 保存单个联系人
    public void saveContact(UserInfo user, boolean isMyContact) {
        //校验
        if (user == null) {
            return;
        }
        //获取连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactTable.COL_HXID, user.getHxid());
        contentValues.put(ContactTable.COL_NAME, user.getName());
        contentValues.put(ContactTable.COL_NICK, user.getNick());
        contentValues.put(ContactTable.COL_PHOTO, user.getPhoto());
        contentValues.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);
        db.replace(ContactTable.TAB_NAME, null, contentValues);
    }


    // 保存联系人信息
    public void saveContacts(List<UserInfo> contacts, boolean isMyContact) {

        //判断
        if (contacts == null || contacts.size() <= 0) {
            return;
        }

        for (UserInfo contact : contacts) {
            saveContact(contact, isMyContact);
        }

    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId) {
        if (TextUtils.isEmpty(hxId)) {
            return;
        }
        //获取连接
        SQLiteDatabase database = mHelper.getReadableDatabase();

        database.delete(ContactTable.TAB_NAME, ContactTable.COL_HXID + "=?", new String[]{hxId});
    }
}
