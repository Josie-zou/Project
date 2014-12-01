package com.example.note1;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;

public class Shownote extends Activity {
	private TextView textView1;
	private TextView textView2;
	private ActionBar actionBar;
	private String title;
	private String content;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shownote);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		id = bundle.getString("id");
		
		textView1 = (TextView) findViewById(R.id.titletext1);
		textView1.setText(title);
	//	Log.i("Correct","No error");
		
		textView2 = (TextView) findViewById(R.id.contenttext1);
		textView2.setText(content);
		
		actionBar = getActionBar();
		actionBar.show();
	}
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	super.onCreateOptionsMenu(menu);
	MenuItem menuItem = menu.add(0, 0, 0, "±à¼­");
	menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	
	menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			Intent intent1 = new Intent(Shownote.this, Changenote.class);
//			intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putString("content", content);
			bundle.putString("id", id);
			intent1.putExtras(bundle);
			intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent1);
			return false;
		}
	});
	
	return true;
}

}
