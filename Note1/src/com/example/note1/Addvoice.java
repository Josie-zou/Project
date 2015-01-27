package com.example.note1;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Addvoice extends Activity {
	
	private ActionBar actionBar;
	private TextView textView;
	private ImageButton imageButton;
	private Button buttonsave;
	private int isRecording;
	private String filepath;
	private Timer mTimer;
	//语音操作对象
	private MediaPlayer mPlayer = null;
	private MediaRecorder mRecorder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.addvoice);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_add);
		
		actionBar = getActionBar();
//		actionBar.show();
		
		buttonsave = (Button) findViewById(R.id.save);
		buttonsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				Bundle b = new Bundle();
				b.putString("audio", filepath);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				
				Addvoice.this.finish();
				
			}
		});
		
		textView = (TextView) findViewById(R.id.tv_recordTime);
		
		imageButton = (ImageButton) findViewById(R.id.voice_input);
		imageButton.setOnClickListener(new ClickEvent());
	}
	
	
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
//	MenuItem menuItem1 = menu.add(0, 0, 0, "save");
//	menuItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//	menuItem1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//		
//		@Override
//		public boolean onMenuItemClick(MenuItem item) {
//			// TODO Auto-generated method stub
//			Intent intent = getIntent();
//			Bundle b = new Bundle();
//			b.putString("audio", filepath);
//			intent.putExtras(b);
//			setResult(RESULT_OK, intent);
//			
//			Addvoice.this.finish();
//			return false;
//		}
//	});
	return super.onCreateOptionsMenu(menu);

}

final Handler handler = new Handler(){
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			String time[] = textView.getText().toString().split(":");
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			int second = Integer.parseInt(time[2]);
			
			if (second < 59) {
				second ++;
			}
			else if (second >= 59 && minute < 59) {
				minute ++;
				second = 0;
			}
			else if (second >= 59 && minute >= 59 && hour < 24) {
				hour ++;
				second = 0;
				minute = 0;
			}
			
			time[0] = hour + "";
			time[1] = minute + "";
			time[2] = second + "";
			if (hour < 10) {
				time[0] = "0" + hour;
			}
			if (minute < 10) {
				time[1] = "0" + minute;
			}
			if (second < 10) {
				time[2] = "0" + second;
			}
			//显示在TextView中
			textView.setText(time[0]+":"+time[1]+":"+time[2]);
			break;

		default:
			break;
		}
		
	};
};


class ClickEvent implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.voice_input:
			if (isRecording == 0) {
				if (filepath != null) {
					File oFile = new File(filepath);
					oFile.delete();
				}
				//获得系统当前时间，并以该时间作为文件名
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date(System.currentTimeMillis());
				String str = simpleDateFormat.format(date);
				
				str = str + "record.amr";
				File dir = new File("/sdcard/note1/");
		        File file = new File("/sdcard/note1/",str);
		        if (!dir.exists()) {
					dir.mkdir();
				}
		        else if (file.exists()) {
					file.delete();
				}
		        
		        filepath = dir.getPath() + "/" + str;
		        
		        mTimer = new Timer();
		        textView.setText("00:00:00");
				//将按钮换成停止录音
		        isRecording = 1;
		        
		        mRecorder = new MediaRecorder();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				mRecorder.setOutputFile(filepath);
		        
		        try {
					mRecorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        mRecorder.start();
		        
		        mTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						
					}
				}, 1000, 1000);
			}
			else {
				isRecording = 0;
				mRecorder.stop();
				mTimer.cancel();
				mTimer = null;
				
				mRecorder.release();
				mRecorder = null;
			}
			break;

		default:
			break;
		}
		
	}
	
}


}


