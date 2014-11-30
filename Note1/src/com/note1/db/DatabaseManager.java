package com.note1.db;

import java.sql.SQLException;


import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.MenuItem.OnMenuItemClickListener;

public class DatabaseManager {
	
    private Context mContext = null;
	
	private SQLiteDatabase mSQLiteDatabase = null;//���ڲ������ݿ�Ķ���
	private DatabaseHelper dh = null;//���ڴ������ݿ�Ķ���
	
	private String dbName = "note.db";
	private int dbVersion = 1;

	public DatabaseManager(Context context){
		mContext = context;
	}

	
	/**
	 * �����ݿ�
	 */
	public void open(){
		
		try{
			dh = new DatabaseHelper(mContext, dbName, null, dbVersion);
			if(dh == null){
				Log.v("msg", "is null");
				return ;
			}
			mSQLiteDatabase = dh.getWritableDatabase();
			//dh.onOpen(mSQLiteDatabase);
			
		}catch(SQLiteException se){
			se.printStackTrace();
		}
	}
	
	

	//��ȡ�б�
	public Cursor selectAll(){
		Cursor cursor = null;
		try{
			String sql = "select * from record";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		return cursor;
	}

	//��������
	public long insert(String title, String content){
		
		long datetime = System.currentTimeMillis();
		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("content", content);
			cv.put("time", datetime);
			l = mSQLiteDatabase.insert("record", null, cv);
		//	Log.v("datetime", datetime+""+l);
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;
		
	}
	
	
	//ɾ������
	public int delete(long id) {
		int affect = 0;
		try {
			affect = mSQLiteDatabase.delete("record", "id=?", new String[]{""+id});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			affect = -1;
		}
		return affect;
	}
	
	
	
	

	/**
	 * �ر����ݿ�
	 */
	public void close(){
		
		mSQLiteDatabase.close();
		dh.close();
		
	}
}
