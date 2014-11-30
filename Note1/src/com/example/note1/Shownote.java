package com.example.note1;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Shownote extends Activity {
	private TextView textView1;
	private TextView textView2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shownote);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		String content = bundle.getString("content");
		
		textView1 = (TextView) findViewById(R.id.titletext1);
		textView1.setText(title);
	//	Log.i("Correct","No error");
		
		textView2 = (TextView) findViewById(R.id.contenttext1);
		textView2.setText(content);
	}

}
