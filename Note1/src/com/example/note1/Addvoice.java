package com.example.note1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class Addvoice extends Activity {
	
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addvoice);
		
		actionBar = getActionBar();
		actionBar.show();
	}
	
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	MenuItem menuItem1 = menu.add(0, 0, 0, "save");
	menuItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	menuItem1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}
	});
	return super.onCreateOptionsMenu(menu);

}
}
