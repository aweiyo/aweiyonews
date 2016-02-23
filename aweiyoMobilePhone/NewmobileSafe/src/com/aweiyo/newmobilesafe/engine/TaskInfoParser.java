package com.aweiyo.newmobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.util.Log;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.bean.TaskInfo;

public class TaskInfoParser {
	
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager activityManager=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		
		PackageManager packageManager = context.getPackageManager();
		List<TaskInfo> list=new ArrayList<TaskInfo>();
		
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			
			TaskInfo taskInfo=new TaskInfo();
			
			//����������еĳ���İ���<������>
			String processName = runningAppProcessInfo.processName;
			taskInfo.setPackageName(processName);
			
			try {
				//�õ������õ�ǰ���г���Ĵ�С
				MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
				int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty();
				taskInfo.setMemorySize(totalPrivateDirty*1024);
				
				//�õ��������еĳ���İ���Ϣ
				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				
				String label = packageInfo.applicationInfo.loadLabel(packageManager).toString();
				taskInfo.setAppName(label);
				
				int flags = packageInfo.applicationInfo.flags;
				if((flags & ApplicationInfo.FLAG_SYSTEM)!=0){
					taskInfo.setUserApp(false);
				}else{
					taskInfo.setUserApp(true);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				//ϵͳ���Ŀ�����û��ͼ������֣������Լ�����Ĭ�ϵ�����
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				taskInfo.setAppName(processName);
			}
			
			list.add(taskInfo);
		}
		return list;
	}
}
