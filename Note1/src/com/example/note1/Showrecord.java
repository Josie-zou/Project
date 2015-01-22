package com.example.note1;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;



import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Showrecord extends Activity {
	private String audiopath;
	private int isplaying = 0;
	private Timer timer;
	private MediaPlayer mediaPlayer;
	private ImageView imageView;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.showrecord);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_add);
		
//		Button back = (Button) findViewById(R.id.back);
//		back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if(isplaying == 1){
//					mediaPlayer.stop();
//					mediaPlayer.release();
//				}
//				Showrecord.this.finish();
//			}
//		});
		
//		Button delete = (Button) findViewById(R.id.save);
//		delete.setBackgroundResource(R.drawable.delete);
		
		textView = (TextView) findViewById(R.id.time);
		imageView = (ImageView) findViewById(R.id.player);
		imageView.setOnClickListener(new ClickEvent());
		
		Intent intent = this.getIntent();
		audiopath = intent.getStringExtra("audioPath");
	}
	final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String time[] = textView.getText().toString().split(":");
				int hour = Integer.parseInt(time[0]);
				int minute = Integer.parseInt(time[1]);
				int second = Integer.parseInt(time[2]);
				
				if (second < 59) {
					second ++;
				}
				else if (second == 59 && minute < 59) {
					second = 0;
					minute ++;
				}
				else if (second == 59 && minute == 59 && hour < 24) {
					second = 0;
					minute = 0;
					hour ++;
				}
				
				time[0] =hour + "";
				time[1] = minute + "";
				time[2] = second + "";
				if (hour < 10) {
					time[0] = "0" + hour;
				}
				if (second < 10) {
					time[2] = "0" + second;
				}
				if (minute < 10) {
					time[1] = "0" + minute;
				}
				textView.setText(time[0] + ":" + time[1] + ":" + time[2]);
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
			if (isplaying == 0) {
				isplaying = 1;
				mediaPlayer = new MediaPlayer();
				timer = new Timer();
				textView.setText("00:00:00");
				
				try {
					mediaPlayer.setDataSource(audiopath);
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timer.schedule(new TimerTask() {
					
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
				isplaying = 0;
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
				timer.cancel();
				timer = null;
			}
		}
		
	}

}
