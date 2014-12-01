package com.example.note1;

import com.note1.db.DatabaseManager;

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
	private String id;
	private ActionBar actionBar;
	private DatabaseManager dManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtext);
		dManager = new DatabaseManager(this);

		etitle = (EditText) findViewById(R.id.edit1);
		econtent = (EditText) findViewById(R.id.edit2);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		id = bundle.getString("id");

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
				String titlenew = etitle.getText().toString();
				String contentnew = econtent.getText().toString();

				dManager.open();
				dManager.update(id, titlenew, contentnew);

				Intent intent = new Intent(Changenote.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

				dManager.close();

				return false;
			}
		});

		// Ìí¼ÓÍ¼Æ¬µÄ²Ëµ¥
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
