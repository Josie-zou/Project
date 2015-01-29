package com.example.note1;

import java.util.ArrayList;
import java.util.Map;

import android.R.integer;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.SimpleAdapter;
//注意不要把类名给重名了
public class ListAdapter1 extends ListActivity {
	SimpleAdapter adapter;
	ArrayList<Map<String, Object>> listitem1;
	
	public ListAdapter1(Context context, ArrayList<Map<String, Object>> item) {
		// TODO Auto-generated constructor stub
		this.listitem1 = item;
		adapter = new SimpleAdapter(context, item, R.layout.onlytext, new String[]{"id", "title", "content"}, new int[]{R.id.note_id,R.id.listtitle,R.id.listcontent});
	}
	
	//TODO
	public Adapter retAdapter1() {
		return adapter;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setListAdapter(adapter);
	}
	
	//删除指定位置的数据
	public void removeItem(int position) {
		listitem1.remove(position);
	}

}
