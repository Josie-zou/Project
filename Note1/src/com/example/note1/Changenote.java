package com.example.note1;

import java.io.FileNotFoundException;

import com.note1.db.DatabaseManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class Changenote extends Activity {

	private Button button;
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
		button = (Button) findViewById(R.id.addphoto);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		id = bundle.getString("id");

		etitle.setText(title);
		econtent.setText(content);

		actionBar = getActionBar();
		actionBar.show();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);

			}
		});
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver contentResolver = this.getContentResolver();
			Bitmap bitmap = null;
			Bundle bundle = null;
			if (requestCode == 1) {
				try {
					bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int imgWidth = bitmap.getWidth();  
	        int imgHeight = bitmap.getHeight();  
	        double partion = imgWidth*1.0/imgHeight;  
	        double sqrtLength = Math.sqrt(partion*partion + 1);  
	        //新的缩略图大小  
	        double newImgW = 480*(partion / sqrtLength);  
	        double newImgH = 480*(1 / sqrtLength);  
	        float scaleW = (float) (newImgW/imgWidth);  
	        float scaleH = (float) (newImgH/imgHeight); 
	        
	        Matrix matrix = new Matrix();
	        
	        matrix.postScale(scaleW, scaleH);  
	        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, matrix, true); 
	        final ImageSpan imageSpan = new ImageSpan(this,bitmap);  
	        SpannableString spannableString = new SpannableString("test");  
	        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK); 
//	                     光标移到下一行
	        econtent.append("\n");  
	        Editable editable = econtent.getEditableText();  
	        int selectionIndex = econtent.getSelectionStart();  
//	        econtent.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
	        spannableString.getSpans(0, spannableString.length(), ImageSpan.class); 
	        
	      //将图片添加进EditText中  
	        editable.insert(selectionIndex, spannableString);  
	        //添加图片后自动空出两行   
	        econtent.append("\n\n");  
		}
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
				// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

				dManager.close();

				return false;
			}
		});

		// 添加图片的菜单
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
