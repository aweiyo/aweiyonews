package com.aweiyo.newmobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.aweiyo.newmobilesafe.bean.AppInfo;

/**
 * ��������ҵ���߼���
 * @author aweiyoo
 *
 */
public class AppInfos {

	/**
	 * ����ǻ�ȡ���е���������е�û���еĶ�������
	 * @param context
	 * @return
	 */
	public  static List<AppInfo> getAppInfos(Context context){
		
		List<AppInfo> packagesAppInfo=new ArrayList<AppInfo>();
		
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(1);
		for (PackageInfo packageInfo : installedPackages) {
			AppInfo appInfo=new AppInfo();
			//��ȡӦ�ó����ͼ��
			Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
			appInfo.setIcon(icon);
			//��ȡӦ�ó��������
			String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
			appInfo.setApkName(apkName);
			//��ȡӦ�ó���İ���
			String packageName = packageInfo.packageName;
			appInfo.setPackages(packageName);
			//��ȡӦ�ó���Ĵ�С
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			File file=new File(sourceDir);
			long apkSize=file.length();
			appInfo.setApkSize(apkSize);
			
			//��ȡӦ�ó���ı�ǣ��ñ�����ж����û�app����ϵͳapp������˵��rom�ڴ滹��sd�ڴ�
			int flags = packageInfo.applicationInfo.flags;
			if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				//��ϵͳapp
				appInfo.setUserApp(false);
			}else{
				appInfo.setUserApp(true);
			}
			
			//FLAG_EXTERNAL_STORAGE���ж�sd����rom�ڴ�
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				//��sd�ڴ�
				appInfo.setRom(false);
			}else{
				appInfo.setRom(true);
				
			}
			
			packagesAppInfo.add(appInfo);
			
//			System.out.println("��ȡӦ�ó����ͼ��"+apkName);
			
		}
		return packagesAppInfo;
	}
}
