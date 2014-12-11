package com.example.note1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.note1.db.DatabaseManager;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
	//��¼editText�е�ͼƬ�����ڵ���ʱ�жϵ���������һ��ͼƬ
		private List<Map<String,String>> imgList = new ArrayList<Map<String,String>>();
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
//		textView2.setText(content);
		
		actionBar = getActionBar();
		actionBar.show();
		
		int startIndex = 0;
		Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
			if (matcher.start() > 0) {
				textView2.append(content.substring(startIndex, matcher.start()));
			}
			//ȡ��ͼƬ���������textView��
	        SpannableString ss = new SpannableString(matcher.group().toString());
	        //ȡ��·��
	        String path = matcher.group().toString();
//	        ȡ��·���еĺ�׺
	        String type = path.substring(path.length() - 3, path.length());
	        Bitmap bitmap = null;
	        bitmap = BitmapFactory.decodeFile(matcher.group());
	        
	        ImageSpan imageSpan = new ImageSpan(this,bitmap);
	        ss.setSpan(imageSpan,0, matcher.end() - matcher.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        System.out.println(matcher.start()+"-------"+matcher.end());
	        textView2.append(ss);
	        startIndex = matcher.end();
	        
	      //��List��¼��ͼƬ��λ�ü�����·�������ڵ����¼�
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("location", matcher.start()+"-"+matcher.end());
	        map.put("path", path);
	        imgList.add(map);

	    }
	    //�����һ��ͼƬ֮������������TextView�� 
	    textView2.append(content.substring(startIndex,content.length()));
		}
	
	
	
	
	
	private Bitmap resizeImage(Bitmap bitmap,int S) {
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		double partion = imgWidth*1.0/imgHeight;
		double sqrtLength = Math.sqrt(partion*partion + 1);
		//�µ�����ͼ��С
		double newImgW = S*(partion / sqrtLength);
		double newImgH = S*(1 / sqrtLength);
		float scaleW = (float) (newImgW/imgWidth);
		float scaleH = (float) (newImgH/imgHeight);
		
		Matrix mx = new Matrix();
		//��ԭͼƬ��������
		mx.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
		return bitmap;
	}
	
	
	
	
	
	
	
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	super.onCreateOptionsMenu(menu);
	MenuItem menuItem = menu.add(0, 0, 0, "�༭");
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
