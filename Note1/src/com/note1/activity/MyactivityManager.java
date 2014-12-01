package com.note1.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyactivityManager extends Application {
	
	private List<Activity> activityList = new LinkedList<Activity>();
	//全局唯一实例
	private static MyactivityManager instance;
	
	//该类采用单例模式，不能实例化
	private MyactivityManager (){
		
	}
	
	private MyactivityManager getinstance() {
		if (instance == null) {
			instance = new MyactivityManager();
		}
		return instance;
	}
	
	//保存activity到现有列表中
	public void addactivity(Activity activity) {
		activityList.add(activity);
	}
	
	//关闭保存的activity
	public void exit() {
		if (activityList != null) {
			Activity activity;
			
			for (int i = 0; i < activityList.size(); i++) {
				activity = activityList.get(i);
				
				if (activity != null) {
					if (!activity.isFinishing()) {
						activity.finish();
					}
					activity = null;
				}
				activityList.remove(i);
				i--;
			}
		}
	}

}
