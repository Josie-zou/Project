package com.example.note1;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.note1.db.DatabaseHelper;
import com.note1.db.DatabaseManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

//Ҳ������һ����¼�����������Ҫ����������ҳ�����Ƶ�
public class Addtext extends Activity {
	private EditText editText2;
	private EditText editText1;
	private Button button;
	private ListView listView;

	private String titleText = "";
	private String contentText = "";

	private ActionBar actionBar;

	private DatabaseManager databaseManager = null;
	private DatabaseHelper databaseHelper = null;
	// ��¼editText�е�ͼƬ�����ڵ���ʱ�жϵ���������һ��ͼƬ
	private List<Map<String, String>> imgList = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtext);
		button = (Button) findViewById(R.id.addphoto);
		editText1 = (EditText) findViewById(R.id.edit1);
		editText2 = (EditText) findViewById(R.id.edit2);
		actionBar = getActionBar();
		actionBar.show();
		databaseManager = new DatabaseManager(this);// �ǵñ�©�����
		// databaseHelper = new DatabaseHelper(this, "1.db3", null, 1);
		// ��ť�ĵ���¼�
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// �趨����Ϊimage
				intent.setType("image/*");
				// ����action
				intent.setAction(intent.ACTION_GET_CONTENT);
				// ѡ��ͼƬ�󷵻ر�activity
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
			String path = null;// ͼƬ��·��
			if (requestCode == 1) {
				try {
					bitmap = BitmapFactory.decodeStream(contentResolver
							.openInputStream(uri));
//					��ͼƬ��·���ҳ���
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					path = cursor.getString(column_index);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int imgWidth = bitmap.getWidth();
			int imgHeight = bitmap.getHeight();
			double partion = imgWidth * 1.0 / imgHeight;
			double sqrtLength = Math.sqrt(partion * partion + 1);
			// �µ�����ͼ��С
			double newImgW = 480 * (partion / sqrtLength);
			double newImgH = 480 * (1 / sqrtLength);
			float scaleW = (float) (newImgW / imgWidth);
			float scaleH = (float) (newImgH / imgHeight);

			Matrix matrix = new Matrix();

			matrix.postScale(scaleW, scaleH);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight,
					matrix, true);
			final ImageSpan imageSpan = new ImageSpan(this, bitmap);
			SpannableString spannableString = new SpannableString("test");
			spannableString.setSpan(imageSpan, 0, spannableString.length(),
					SpannableString.SPAN_MARK_MARK);
			// ����Ƶ���һ��
			editText2.append("\n");
			Editable editable = editText2.getEditableText();
			int selectionIndex = editText2.getSelectionStart();
			spannableString.getSpans(0, spannableString.length(),
					ImageSpan.class);

			// ��ͼƬ��ӽ�EditText��
			editable.insert(selectionIndex, spannableString);
			// ���ͼƬ���Զ��ճ�����
			editText2.append("\n\n");

			Map<String, String> map = new HashMap<String, String>();
			map.put("location", selectionIndex + "-"
					+ (selectionIndex + spannableString.length()));
			map.put("path", path);
			imgList.add(map);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuItem menuItem = menu.add(0, 0, 0, "save");
		MenuItem menuItem2 = menu.add(0, 0, 0, "image");

		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		// ��������Ӽ��µİ�ť
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			// TODO
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				titleText = editText1.getText().toString();
				contentText = editText2.getText().toString();

				// databaseManager.open();
				try {
					databaseManager.open();
					databaseManager.insert(titleText, contentText);
					// databaseManager = databaseHelper.getReadableDatabase();
					// databaseManager.execSQL("insert into 1 values(null,?,?,null)",new
					// String[]{titleText, contentText});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				databaseManager.close();
				// �������ص���ҳ
				Intent intent = new Intent(Addtext.this, MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
				// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent);

				return false;
			}
		});

		// ѡ������ͼƬ�Ĳ˵���ť
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
