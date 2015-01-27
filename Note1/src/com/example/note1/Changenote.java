package com.example.note1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.note1.db.DatabaseManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
	private Button buttonsave;
	private Button buttonback;
	private ImageButton buttonrecord;
	//��¼editText�е�ͼƬ�����ڵ���ʱ�жϵ���������һ��ͼƬ
	private List<Map<String,String>> imgList = new ArrayList<Map<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addtext);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_add);
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dManager = new DatabaseManager(this);

		buttonsave = (Button) findViewById(R.id.save);
		buttonsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
				
			}
		});
		
		
		buttonback = (Button) findViewById(R.id.back);
		buttonback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Changenote.this.finish();
			}
		});
		
		buttonrecord = (ImageButton) findViewById(R.id.voice_input);
		buttonrecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Changenote.this, Addvoice.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				startActivityForResult(intent, 2);
			}
		});
		
		
		etitle = (EditText) findViewById(R.id.edit1);
		econtent = (EditText) findViewById(R.id.edit2);
		econtent.setOnClickListener(new TextClickEvent());
		button = (Button) findViewById(R.id.addphoto);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		content = bundle.getString("content");
		id = bundle.getString("id");

		etitle.setText(title);
//		econtent.setText(content);

		actionBar = getActionBar();
//		actionBar.show();

		int startIndex = 0;
		Pattern p = Pattern.compile("/([^\\.]*)\\.\\w{3}");
		Matcher matcher = p.matcher(content);
		while (matcher.find()) {
			if (matcher.start() > 0) {
				String content1 = content.substring(startIndex, matcher.start());
				econtent.append(content1);
				Log.i("", content1);
			}
			//ȡ��ͼƬ���������textView��
	        SpannableString ss = new SpannableString(matcher.group().toString());
	        //ȡ��·��
	        String path = matcher.group().toString();
//	        ȡ��·���еĺ�׺
	        String type = path.substring(path.length() - 3, path.length());
	        Bitmap rbm = null;
	        if (type.equals("jpg")) {
	        	Bitmap bitmap = null;
		        bitmap = BitmapFactory.decodeFile(matcher.group());
		        rbm = resize(bitmap,480);
		       
			}
	        else {
	        	rbm = BitmapFactory.decodeResource(getResources(), R.drawable.record_icon);
	    		//����ͼƬ
		        rbm = resize(rbm,200);
			}
	        
	      //ΪͼƬ��ӱ߿�Ч��
	        rbm = getBitmapHuaSeBianKuang(rbm);
	       
	        ImageSpan span = new ImageSpan(this, rbm);
	        ss.setSpan(span,0, matcher.end() - matcher.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        System.out.println(matcher.start()+"-------"+matcher.end());
	        econtent.append(ss);
	        startIndex = matcher.end();
	        
	      //��List��¼��ͼƬ��λ�ü�����·�������ڵ����¼�
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("location", matcher.start()+"-"+matcher.end());
	        map.put("path", path);
	        imgList.add(map);

	    }
	    //�����һ��ͼƬ֮������������TextView�� 
		econtent.append(content.substring(startIndex,content.length()));
		
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
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			//ȡ������
			Uri uri = data.getData();
			ContentResolver cr = Changenote.this.getContentResolver();
			Bitmap bitmap = null;
			Bundle extras = null;
			//�����ѡ����Ƭ
			if(requestCode == 1){
				//TODO
				//ȡ��ѡ����Ƭ��·��
				
				String[] proj = { MediaStore.Images.Media.DATA };   
	            Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);   
				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
				actualimagecursor.moveToFirst();
	            String path = actualimagecursor.getString(actual_image_column_index);  
				try {
					//���������Bitmap�� 
					bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//����ͼƬ 
				//TODO
				InsertBitmap(bitmap,480,path);
			}
			
			
			
		}
	}

	private void InsertBitmap(Bitmap bitmap, int S, String imgPath) {
		// TODO Auto-generated method stub
		bitmap = resize(bitmap,S);
		//��ӱ߿�Ч��
		bitmap = getBitmapHuaSeBianKuang(bitmap);
		//bitmap = addBigFrame(bitmap,R.drawable.line_age);
		final ImageSpan imageSpan = new ImageSpan(this,bitmap);
		SpannableString spannableString = new SpannableString(imgPath);
		spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
		//����Ƶ���һ��
		econtent.append("\n");
		Editable editable = econtent.getEditableText();
		int selectionIndex = econtent.getSelectionStart();
		spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
		
		//��ͼƬ��ӽ�EditText��
		editable.insert(selectionIndex, spannableString);
		//���ͼƬ���Զ��ճ����� 
		econtent.append("\n");
		
//		��List��¼��¼����λ�ü�����·�������ڵ����¼�
        Map<String,String> map = new HashMap<String,String>();
        map.put("location", selectionIndex+"-"+(selectionIndex+spannableString.length()));
        map.put("path", imgPath);
//        imgList.add(map);
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
	
	
	class TextClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Spanned s = (Spanned) econtent.getText();
			ImageSpan[] imageSpans;
			imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
			
			int selectionStart = econtent.getSelectionStart();
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
						Intent intent = new Intent(Changenote.this,Showrecord.class);
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
						Intent intent = new Intent(Changenote.this,Showpicture.class);
						intent.putExtra("imgPath", path);
						startActivity(intent);
					}
				}
			}	
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		MenuItem menuItem = menu.add(0, 0, 0, "sava");
//		MenuItem menuItem2 = menu.add(0, 0, 0, "image");
//
//		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				// TODO Auto-generated method stub
//				String titlenew = etitle.getText().toString();
//				String contentnew = econtent.getText().toString();
//
//				dManager.open();
//				dManager.update(id, titlenew, contentnew);
//
//				Intent intent = new Intent(Changenote.this, MainActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(intent);
//
//				dManager.close();
//
//				return false;
//			}
//		});
//
//		// ���ͼƬ�Ĳ˵�
//		menuItem2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});

		return super.onCreateOptionsMenu(menu);
	}

}
