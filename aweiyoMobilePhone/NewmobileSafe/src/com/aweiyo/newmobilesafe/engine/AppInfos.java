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
 * 软件管理的业务逻辑类
 * @author aweiyoo
 *
 */
public class AppInfos {

	/**
	 * 这个是获取所有的软件，运行的没运行的都在这里
	 * @param context
	 * @return
	 */
	public  static List<AppInfo> getAppInfos(Context context){
		
		List<AppInfo> packagesAppInfo=new ArrayList<AppInfo>();
		
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(1);
		for (PackageInfo packageInfo : installedPackages) {
			AppInfo appInfo=new AppInfo();
			//获取应用程序的图标
			Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
			appInfo.setIcon(icon);
			//获取应用程序的名字
			String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
			appInfo.setApkName(apkName);
			//获取应用程序的包名
			String packageName = packageInfo.packageName;
			appInfo.setPackages(packageName);
			//获取应用程序的大小
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			File file=new File(sourceDir);
			long apkSize=file.length();
			appInfo.setApkSize(apkSize);
			
			//获取应用程序的标记，用标记来判断是用户app还是系统app，或者说是rom内存还是sd内存
			int flags = packageInfo.applicationInfo.flags;
			if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				//是系统app
				appInfo.setUserApp(false);
			}else{
				appInfo.setUserApp(true);
			}
			
			//FLAG_EXTERNAL_STORAGE是判断sd还是rom内存
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				//是sd内存
				appInfo.setRom(false);
			}else{
				appInfo.setRom(true);
				
			}
			
			packagesAppInfo.add(appInfo);
			
//			System.out.println("获取应用程序的图标"+apkName);
			
		}
		return packagesAppInfo;
	}
}
