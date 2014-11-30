package com.example.note1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;

public class Changenote extends Activity {

	private EditText etitle;
	private EditText econtent;
	private String title;
	private String content;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtext);
		
		etitle = (EditText) findViewById(R.id.edit1);
		econtent = (EditText) findViewById(R.id.edit2);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		
		etitle.setText(title);
		econtent.setText(content);
		
		actionBar = getActionBar();
		actionBar.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem menuItem = menu.add(0, 0, 0, "sava");
		MenuItem menuItem2 = menu.add(0, 0, 0, "image");
		
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		//Ìí¼ÓÍ¼Æ¬µÄ²Ëµ¥
		menuItem2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
		

}
