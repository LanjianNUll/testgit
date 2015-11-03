package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * 好友列表的数据库操作*/
public class MyFriendGroupDB extends SQLiteOpenHelper {

	private static final String DBName = "ourapp.db";
	public MyFriendGroupDB(Context context) {
		super(context, DBName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//建表语句
		db.execSQL("CREATE table IF NOT EXISTS friendGroup " +
				"(_id INTEGER PRIMARY KEY," +
				"friendId TEXT, " +
				"friendName TEXT," +
				"friendHeadPicType INTEGER," +
				"friendSignal TEXT)");		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS friendGroup");  
	        onCreate(db);
	}
	/*查询好友*/
	public User getFriend(int friendId){
		User u = new User();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("select * from friendGroup where friendId=?",
				new String[] { friendId + "" });
		if (c.moveToNext()){
			u.setUsername(c.getString(c.getColumnIndex("friendName")));
			u.setMy_user_sign(c.getString(c.getColumnIndex("friendSignal")));
			
		} else{
			return null;
		}
		return u;
	}
	/*增加好友*/
	public void AddFriend(User friend){
		if (getFriend(friend.getUserId()) != null)
		{
			update(friend);
			return;
		}
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(
				"insert into friendGroup (friendId,friendName,friendSignal) values(?,?,?)",
				new Object[] { friend.getUserId(), friend.getUsername(), friend.getMy_user_sign()});
		db.close();
	}
	/*增加好友列表*/
	public void AddFriendGroup(List<User> friend){
		
		SQLiteDatabase db = getWritableDatabase();
		for (User user : friend) {
			db.execSQL(
				"insert into friendGroup (friendId,friendName,friendSignal) values(?,?,?)",
				new Object[] { user.getUserId(), user.getUsername(), user.getMy_user_sign()});
		}
		db.close();
	}
	/*更新好友*/
	public void update(User friend) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(
				"update friendGroup set friendName=?,friendSignal=? where friendId=?",
				new Object[] { friend.getUsername(), friend.getMy_user_sign(), friend.getUserId()});
		db.close();	
	}
		
	/*获取好友列表*/
	public ArrayList<User> getFriendGroup()
	{
		SQLiteDatabase db = getWritableDatabase();
		ArrayList<User> list = new ArrayList<User>();
		Cursor c = db.rawQuery("select * from friendGroup", null);
		while (c.moveToNext()){
			User u = new User();
			u.setUserId(c.getInt(c.getColumnIndex("friendId")));
			u.setUsername(c.getString(c.getColumnIndex("friendName")));
			u.setMy_user_sign(c.getString(c.getColumnIndex("friendSignal")));
			list.add(u);
		}
		c.close();
		db.close();
		return list;
	}
	
	/*删除*/
	public void delUser(User u){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where friendId=?",
				new Object[] { u.getUserId() });
		db.close();
	}
	//根据userID删除好友
	public void delUserRID(int frinedId){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where friendId=?",
				new Object[] {frinedId });
		db.close();
	}
	/*删除表中的数据*/
	public void delete(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where 1=1");
		db.close();
	}	
}
