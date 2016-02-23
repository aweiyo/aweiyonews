package com.aweiyo.newmobilesafe.broadcast;

import com.aweiyo.newmobilesafe.service.KillProcessWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord.OnRecordPositionUpdateListener;

/**
 * ����С�����Ĺ㲥
 * @author aweiyoo
 *
 */
public class MyAppWidget extends AppWidgetProvider {

	/***
	 * �㲥������������ÿ���н��ܵ��㲥ʱ�����������
	 * ��ǰ�㲥���������ں̣ܶ�ֻ��ʮ���룬���յ������ʱ�򣬴���ҵ���߼�һ��Ҫ�ڱ��������
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
	//����С��������������
	/**
	 * ÿ�����µ�����С������ӽ�����ʱ�������������
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/**
	 * ÿ��ɾ��ʱ
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	/**
	 * ��һ���������С����ʱ
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		//����һ����������С������ʱ�򣬾Ϳ������񣬷����ں�̨��������
		Intent intent=new Intent(context,KillProcessWidgetService.class);
		context.startService(intent);
	}

	/**
	 * ���һ������С��������ɾ��ʱ
	 */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		//����С����ȫ��ɾ����Ҳ�ͽ���������
		Intent intent=new Intent(context,KillProcessWidgetService.class);
		context.stopService(intent);
	}

	
}
