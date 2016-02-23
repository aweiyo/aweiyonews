package com.aweiyo.newmobilesafe.broadcast;

import com.aweiyo.newmobilesafe.service.KillProcessWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord.OnRecordPositionUpdateListener;

/**
 * 桌面小部件的广播
 * @author aweiyoo
 *
 */
public class MyAppWidget extends AppWidgetProvider {

	/***
	 * 广播的生命方法，每次有接受到广播时运行这个方法
	 * 当前广播的生命周期很短，只有十多秒，接收到服务的时候，处理业务逻辑一定要在别的类里面
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
	//桌面小部件的生命周期
	/**
	 * 每次有新的桌面小部件添加进来的时候运行这个方法
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/**
	 * 每次删除时
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	/**
	 * 第一次添加桌面小部件时
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		//当第一个次有桌面小部件的时候，就开启服务，服务在后台更新数据
		Intent intent=new Intent(context,KillProcessWidgetService.class);
		context.startService(intent);
	}

	/**
	 * 最后一个桌面小部件都被删除时
	 */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		//桌面小部件全部删除了也就结束服务了
		Intent intent=new Intent(context,KillProcessWidgetService.class);
		context.stopService(intent);
	}

	
}
