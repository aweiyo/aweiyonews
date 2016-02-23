package com.aweiyo.newmobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.EventLogTags.Description;

public class AnivirusDao {
	public static String checkFileVirus(String md5){
		String desc=null;
		SQLiteDatabase database=SQLiteDatabase.openDatabase("/data/data/com.aweiyo.newmobilesafe/files/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.rawQuery("select desc from datable where md5=?", new String[]{md5});
		while (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor=null;
		return desc.toString();
	}
}
