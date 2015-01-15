package com.example.note1;

import com.note1.db.DatabaseManager;

//增加一条记录
public class Addrecord {
	private String date;
	private String title;
	private String content;
	
	private DatabaseManager databaseManager;
	
	public Addrecord(String title, String content){
		this.title = title;
		this.content = content;
		
		databaseManager.open();//打开数据库
	}

}
