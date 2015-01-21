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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
//		Log.i("", content);
		System.out.println(content);
		id = bundle.getString("id");
		
		textView1 = (TextView) findViewById(R.id.titletext1);
		textView1.setText(title);
	//	Log.i("Correct","No error");
		
		textView2 = (TextView) findViewById(R.id.contenttext1);
		textView2.setMovementMethod(ScrollingMovementMethod.getInstance());
//		textView2.setText("");
		
		actionBar = getActionBar();
		actionBar.show();
		
		int startIndex = 0;
		Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
			if (matcher.start() > 0) {
				String content1 = content.substring(startIndex, matcher.start());
				textView2.append(content1);
				Log.i("", content1);
			}
			//ȡ��ͼƬ���������textView��
	        SpannableString ss = new SpannableString(matcher.group().toString());
	        //ȡ��·��
	        String path = matcher.group().toString();
//	        ȡ��·���еĺ�׺
	        String type = path.substring(path.length() - 3, path.length());
	        Bitmap rbm = null;
	        if(type.equals("amr")){
	    		rbm = BitmapFactory.decodeResource(getResources(), R.drawable.record_icon);
	    		//����ͼƬ
		        rbm = resize(rbm,200);
	    	}
	        else {
	        	Bitmap bitmap = null;
		        bitmap = BitmapFactory.decodeFile(matcher.group());
		        rbm = resize(bitmap,480);
			}
	        
	      //ΪͼƬ��ӱ߿�Ч��
	        rbm = getBitmapHuaSeBianKuang(rbm);
	       
	        ImageSpan span = new ImageSpan(this, rbm);
	        ss.setSpan(span,0, matcher.end() - matcher.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        System.out.println(matcher.start()+"-------"+matcher.end());
	        textView2.append(ss);
	        startIndex = matcher.end();
	        
//	      ��List��¼��ͼƬ��λ�ü�����·�������ڵ����¼�
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("location", matcher.start()+"-"+matcher.end());
	        map.put("path", path);
	        imgList.add(map);

	    }
	    //�����һ��ͼƬ֮������������TextView�� 
	    textView2.append(content.substring(startIndex,content.length()));
		}
	
	
	
	
	
	private Bitmap resize(Bitmap bitmap,int S){
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
	
	
	//��ͼƬ�ӱ߿򣬲����ر߿���ͼƬ
	public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();
 
        // ��������ͼ
        Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
 
        // ���õ�ͼΪ����
        Canvas canvas = new Canvas(bitmapbg);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
 
        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
                / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
                / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);
 
        // ����Ƭ��С����(��ȥ�߿�Ĵ�С)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
 
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);
 
        // ���Ƶ�ͼ�߿�
        canvas.drawRect(
                new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
                paint);
        // ���ƻ�ɫ�߿�
        paint.setColor(Color.BLUE);
        canvas.drawRect(
                new Rect((int) (frameSize), (int) (frameSize), bitmapbg
                        .getWidth() - (int) (frameSize), bitmapbg.getHeight()
                        - (int) (frameSize)), paint);
 
        canvas.drawBitmap(bitmap, frameSize + 1, frameSize + 1, paint);
 
        return bitmapbg;
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

class TextClickEvent implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Spanned s = (Spanned) textView2.getText();
		ImageSpan[] imageSpans;
		imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		
		int selectionStart = textView2.getSelectionStart();
		for(ImageSpan span : imageSpans){
			
			int start = s.getSpanStart(span);
			int end = s.getSpanEnd(span);
			//�ҵ�ͼƬ
			if(selectionStart >= start && selectionStart < end){
				//Bitmap bitmap = ((BitmapDrawable)span.getDrawable()).getBitmap();
				//���ҵ�ǰ������ͼƬ����һ��ͼƬ
				//System.out.println(start+"-----------"+end);
				String path = null;
				for(int i = 0;i < imgList.size();i++){
					Map map = imgList.get(i);
					//�ҵ���
					if(map.get("location").equals(start+"-"+end)){
						path = imgList.get(i).get("path");
						break;
					}
				}
				//�����жϵ�ǰͼƬ�Ƿ���¼�������Ϊ¼��������ת������¼����Activity��������ǣ�����ת���鿴ͼƬ�Ľ���
				//¼��������ת������¼����Activity
				if(path.substring(path.length()-3, path.length()).equals("amr")){
					Intent intent = new Intent(Shownote.this,Showrecord.class);
					intent.putExtra("audioPath", path);
					startActivity(intent);
				}
				//ͼƬ������ת���鿴ͼƬ�Ľ���
				else{
					//�����ַ������鿴ͼƬ����һ�־���ֱ�ӵ���ϵͳ��ͼ��鿴ͼƬ���ڶ������Զ���Activity
					//����ϵͳͼ��鿴ͼƬ
					/*Intent intent = new Intent(Intent.ACTION_VIEW);
					File file = new File(path);
					Uri uri = Uri.fromFile(file);
					intent.setDataAndType(uri, "image/*");*/
					//ʹ���Զ���Activity
					Intent intent = new Intent(Shownote.this,Showpicture.class);
					intent.putExtra("imgPath", path);
					startActivity(intent);
				}
			}
			
		
	}

}
}
}
