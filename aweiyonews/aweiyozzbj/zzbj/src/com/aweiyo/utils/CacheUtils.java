package com.aweiyo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
	
	private static SharedPreferences sp;

	public static void setCache(Context context,String key,String value){
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String  getCache(Context context,String key){
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
}
