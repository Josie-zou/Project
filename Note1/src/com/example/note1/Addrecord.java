package com.example.note1;

import com.note1.db.DatabaseManager;

//����һ����¼
public class Addrecord {
	private String date;
	private String title;
	private String content;
	
	private DatabaseManager databaseManager;
	
	public Addrecord(String title, String content){
		this.title = title;
		this.content = content;
		
		databaseManager.open();//�����ݿ�
	}

}
