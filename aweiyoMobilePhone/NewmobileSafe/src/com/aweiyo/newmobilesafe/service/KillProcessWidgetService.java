package com.aweiyo.newmobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.broadcast.MyAppWidget;

public class KillProcessWidgetService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		final AppWidgetManager widgetManager = AppWidgetManager.getInstance(KillProcessWidgetService.this);
		//������ʱ����ÿ�������������
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				//������xml�ļ�
				RemoteViews view=new RemoteViews(getPackageName(), R.layout.process_widget);
				view.setTextViewText(R.id.process_count, "....");
				view.setTextViewText(R.id.process_memory, "iuiu");
				ComponentName componentName=new ComponentName(getApplicationContext(),MyAppWidget.class);//������  ����
				
				Intent intent = new Intent();
				intent.setAction("com.aweiyo.newmobilesafe");
				//����ȥһ������intent�Ĺ㲥
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				view.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				widgetManager.updateAppWidget(componentName, view);
			}
		};
		//timertask,when,peroid
		timer.schedule(task, 0, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
}
