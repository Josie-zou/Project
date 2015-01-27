package com.example.note1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.note1.db.DatabaseManager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity<ListViewAdapter> extends Activity {

	private Button button1;
	private Button button2;
	private Button button3;
	private ListView listView;
	private DatabaseManager databaseManager;
	private SimpleAdapter sAdapter;
	private ListAdapter1 listAdapter1;
	private Cursor cursor;
	private int count;
//	private ActionBar actionBar;
	private ArrayList<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
	private Button buttonadd;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_main);
		
		databaseManager = new DatabaseManager(this);//这句话，我漏了很多次。。。。。
//		actionBar = getActionBar();
//		actionBar.show();
		buttonadd = (Button) findViewById(R.id.add);
		buttonadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, Addtext.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}
		});
//		button1 = (Button)findViewById(R.id.bt1);
//		button1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, Addtext.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(intent);
//				
//				
//			}
//		});
//		
//		button2 = (Button) findViewById(R.id.bt2);
//		button2.setOnClickListener( new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, Addvoice.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
////				startActivityForResult(intent, 4);
//				
//			}
//		});
//		
//		button3 = (Button) findViewById(R.id.bt3);
//		button3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent( MainActivity.this, Addvideo.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				
//			}
//		});
		listView = (ListView) findViewById(R.id.listv);
		
	//	listView.setOnScrollListener((OnScrollListener) this);
		
		//TODO
		iniAdapter();
		//TODO
		listAdapter1 = new ListAdapter1(this, item);
		sAdapter = (SimpleAdapter) listAdapter1.retAdapter1();
		listView.setAdapter(sAdapter);
		//单击每一个选项时它的事件激发
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
		//		ListView lv = (ListView)parent;
//				HashMap<String, String> map = (HashMap<String, String>) lv.getItemAtPosition(position);
//				String title = map.get("listtitle");
//				String content = map.get("listcontent");
				//不能从控件上获取数据，只能从给控件的map上提取数据
				
				//TODO
				String id1 = item.get(position).get("id").toString();
				String title = item.get(position).get("title").toString();
				String content = item.get(position).get("content").toString();
				
				Intent intent = new Intent(MainActivity.this, Changenote.class);
				Bundle bundle = new Bundle();//该类用作携带数据
				bundle.putString("id", id1);
				bundle.putString("title", title);
				bundle.putString("content", content);
				intent.putExtras(bundle);//附带上额外的数据
				//通过意图传递数据，然后在修改时根据title寻找该条记录
			//	intent.putExtra("title", title);//把题目和内容传过去
			//	intent.putExtra("content", content);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	/*	listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				
			}
		});*/
		//TODO
	//	listView.setOnItemLongClickListener((OnItemLongClickListener) this);
		itemonLongclick();
		
		
	}
	
	

	public void iniAdapter() {
		databaseManager.open();
		cursor = databaseManager.selectAll();
		count = cursor.getCount();//获取个数
		cursor.moveToLast();//让游标移动到最后一条数据，为的是让我们新保存的的东西保存在最开始的选项。
	//	cursor.moveToFirst();//将游标移动到第一条数据，使用前必须调用
		
		for (int i = 0; i < count; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
	        data.put("id", cursor.getInt(cursor.getColumnIndex("id")));
			data.put("title", cursor.getString(cursor.getColumnIndex("title")));
			data.put("content", cursor.getString(cursor.getColumnIndex("content")));
			item.add(data);

		//	cursor.moveToNext();//让游标指到下一条位置
			cursor.moveToPrevious();//让游标指到上一条位置
		}
	//	cursor.close();
		
		databaseManager.close();
	}

	 public boolean itemonLongclick()
	 {
		 listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				
				menu.add("删除");
			}
		});
				return false;
		 
	 }
	 
	 
	 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo menuInfo = 
				(AdapterView.AdapterContextMenuInfo)item
				.getMenuInfo();
		databaseManager.open();
		switch (item.getItemId()) {
		case 0:
			//TODO
			cursor.moveToPosition(count - menuInfo.position - 1);//因为我是倒叙显示，所以。。。
			int i = databaseManager.delete(Long.parseLong(cursor.getString(cursor.getColumnIndex("id"))));
			listAdapter1.removeItem(menuInfo.position);//删除数据
			sAdapter.notifyDataSetChanged();//通知数据源，数据已经改变，刷新页面。
			//删除操作
			break;

		default:
			break;
		}
		databaseManager.close();
		return super.onContextItemSelected(item);
	}
	 


	 
	 

//菜单栏的设计
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
