package com.aweiyo.newmobilesafe.bean;

import android.R.string;
import android.graphics.drawable.Drawable;

public class AppInfo {
	
	private Drawable icon;
	
	private String apkName;
	
	private long apkSize;
	
	/**
	 * true：用户app
	 * false：系统app
	 */
	private boolean userApp;
	
	private boolean isRom;
	
	private String packages;

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public long getApkSize() {
		return apkSize;
	}

	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	public boolean isRom() {
		return isRom;
	}

	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}
	
	
	
	
}
