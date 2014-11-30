package com.example.note1;

import java.sql.SQLException;

import com.note1.db.DatabaseHelper;
import com.note1.db.DatabaseManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

//也是增加一条记录，不过这个主要是用来处理页面的设计的
public class Addtext extends Activity {
	private EditText editText2;
	private EditText editText1;
	private ListView listView;
	
	private String titleText = "";
	private String contentText = "";
	
	private ActionBar actionBar;
	
	private DatabaseManager databaseManager = null;
	private DatabaseHelper databaseHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtext);
		editText1= (EditText) findViewById(R.id.edit1);
		editText2 = (EditText) findViewById(R.id.edit2);
		
		actionBar = getActionBar();
		actionBar.show();
		databaseManager = new DatabaseManager(this);//记得别漏了这个
		//databaseHelper = new DatabaseHelper(this, "1.db3", null, 1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuItem menuItem = menu.add(0,0,0,"save");
		MenuItem menuItem2 = menu.add(0,0,0,"image");
		
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		//这里是添加记事的按钮
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			//TODO
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				titleText = editText1.getText().toString();
				contentText = editText2.getText().toString();
				
			//	databaseManager.open();
				try {
					databaseManager.open();
					databaseManager.insert(titleText, contentText);
				//	databaseManager = databaseHelper.getReadableDatabase();
			//		databaseManager.execSQL("insert into 1 values(null,?,?,null)",new String[]{titleText, contentText});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				databaseManager.close();
				//让它跳回到主页
				Intent intent = new Intent(Addtext.this, MainActivity.class);
				startActivity(intent);
				
				return false;
			}
		});
		
		//选择增加图片的菜单按钮
		menuItem2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return true;
	}
	

}
