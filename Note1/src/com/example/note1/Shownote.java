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
	//记录editText中的图片，用于单击时判断单击的是那一个图片
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
			//取出图片，并添加在textView中
	        SpannableString ss = new SpannableString(matcher.group().toString());
	        //取出路径
	        String path = matcher.group().toString();
//	        取出路径中的后缀
	        String type = path.substring(path.length() - 3, path.length());
	        Bitmap rbm = null;
	        if(type.equals("amr")){
	        	Intent intent1 = new Intent(Shownote.this, Changenote.class);
//				intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				Bundle bundle1 = new Bundle();
				bundle1.putString("title", title);
				bundle1.putString("content", content);
				bundle1.putString("id", id);
				intent1.putExtras(bundle1);
				intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent1);
	    		rbm = BitmapFactory.decodeResource(getResources(), R.drawable.record_icon);
	    		//缩放图片
		        rbm = resize(rbm,200);
	    	}
	        else {
	        	Bitmap bitmap = null;
		        bitmap = BitmapFactory.decodeFile(matcher.group());
		        rbm = resize(bitmap,480);
			}
	        
	      //为图片添加边框效果
	        rbm = getBitmapHuaSeBianKuang(rbm);
	       
	        ImageSpan span = new ImageSpan(this, rbm);
	        ss.setSpan(span,0, matcher.end() - matcher.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        System.out.println(matcher.start()+"-------"+matcher.end());
	        textView2.append(ss);
	        startIndex = matcher.end();
	        
//	      用List记录该图片的位置及所在路径，用于单击事件
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("location", matcher.start()+"-"+matcher.end());
	        map.put("path", path);
	        imgList.add(map);

	    }
	    //将最后一个图片之后的文字添加在TextView中 
	    textView2.append(content.substring(startIndex,content.length()));
		}
	
	
	
	
	
	private Bitmap resize(Bitmap bitmap,int S){
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		double partion = imgWidth*1.0/imgHeight;
		double sqrtLength = Math.sqrt(partion*partion + 1);
		//新的缩略图大小
		double newImgW = S*(partion / sqrtLength);
		double newImgH = S*(1 / sqrtLength);
		float scaleW = (float) (newImgW/imgWidth);
		float scaleH = (float) (newImgH/imgHeight);
		
		Matrix mx = new Matrix();
		//对原图片进行缩放
		mx.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
		return bitmap;
	}
	
	
	//给图片加边框，并返回边框后的图片
	public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();
 
        // 用来做底图
        Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
 
        // 设置底图为画布
        Canvas canvas = new Canvas(bitmapbg);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
 
        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
                / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
                / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);
 
        // 对相片大小处理(减去边框的大小)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
 
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);
 
        // 绘制底图边框
        canvas.drawRect(
                new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
                paint);
        // 绘制灰色边框
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
	MenuItem menuItem = menu.add(0, 0, 0, "编辑");
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
			//找到图片
			if(selectionStart >= start && selectionStart < end){
				//Bitmap bitmap = ((BitmapDrawable)span.getDrawable()).getBitmap();
				//查找当前单击的图片是哪一个图片
				//System.out.println(start+"-----------"+end);
				String path = null;
				for(int i = 0;i < imgList.size();i++){
					Map map = imgList.get(i);
					//找到了
					if(map.get("location").equals(start+"-"+end)){
						path = imgList.get(i).get("path");
						break;
					}
				}
				//接着判断当前图片是否是录音，如果为录音，则跳转到试听录音的Activity，如果不是，则跳转到查看图片的界面
				//录音，则跳转到试听录音的Activity
				if(path.substring(path.length()-3, path.length()).equals("amr")){
					Intent intent = new Intent(Shownote.this,Showrecord.class);
					intent.putExtra("audioPath", path);
					startActivity(intent);
				}
				//图片，则跳转到查看图片的界面
				else{
					//有两种方法，查看图片，第一种就是直接调用系统的图库查看图片，第二种是自定义Activity
					//调用系统图库查看图片
					/*Intent intent = new Intent(Intent.ACTION_VIEW);
					File file = new File(path);
					Uri uri = Uri.fromFile(file);
					intent.setDataAndType(uri, "image/*");*/
					//使用自定义Activity
					Intent intent = new Intent(Shownote.this,Showpicture.class);
					intent.putExtra("imgPath", path);
					startActivity(intent);
				}
			}
			
		
	}

}
}
}
