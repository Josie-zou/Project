package com.note1.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyactivityManager extends Application {
	
	private List<Activity> activityList = new LinkedList<Activity>();
	//ȫ��Ψһʵ��
	private static MyactivityManager instance;
	
	//������õ���ģʽ������ʵ����
	private MyactivityManager (){
		
	}
	
	private MyactivityManager getinstance() {
		if (instance == null) {
			instance = new MyactivityManager();
		}
		return instance;
	}
	
	//����activity�������б���
	public void addactivity(Activity activity) {
		activityList.add(activity);
	}
	
	//�رձ����activity
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
