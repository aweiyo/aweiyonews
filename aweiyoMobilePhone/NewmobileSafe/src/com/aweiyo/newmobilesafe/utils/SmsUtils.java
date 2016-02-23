package com.aweiyo.newmobilesafe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * 短信备份的工具类
 * @author aweiyoo
 *
 */
public class SmsUtils {

	/**
	 * 1.判断用户是否有sdcard   
	 * 2.判断权限是否可以 
	 * 3.写短信
	 * @param context  
	 * @return
	 */
	public static boolean backUp(Context context,ProgressDialog pd){
		
		 
		//判断sd是否挂载上
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);
			int count=cursor.getCount();//获得短信的数量
			pd.setMax(count);
			int number=0;//记录备份短信的数量
			
			try {
				//第一个参数是获得当前的目录 第二个是备份后的文件名
				File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
	System.out.println(Environment.getExternalStorageDirectory());
	System.out.println("进来了。。。。");
				FileOutputStream fos=new FileOutputStream(file);
				
				XmlSerializer serializer = Xml.newSerializer();
				//位序列化器设置输出流，说白了就是写的流，写到什么地方，用什么编码
				serializer.setOutput(fos, "utf-8");
				
				serializer.startDocument("utf-8", true);
			
				serializer.startTag(null, "smss");
				serializer.attribute(null, "smsCount", count+"");//将短短信的个数写入备份的信息中
				
				
				
			while(cursor.moveToNext()){
				System.out.println(cursor.getString(0));
				System.out.println(cursor.getString(1));
				
				serializer.startTag(null, "sms");
				
				serializer.startTag(null, "address");
				serializer.text(cursor.getString(0));
				serializer.endTag(null, "address");
				
				serializer.startTag(null, "date");
				serializer.text(cursor.getString(1));
				serializer.endTag(null, "date");
				
				serializer.startTag(null, "type");
				serializer.text(cursor.getString(2));
				serializer.endTag(null, "type");
				
				serializer.startTag(null, "body");
				serializer.text(cursor.getString(3));
				serializer.endTag(null, "body");
				
				serializer.endTag(null, "sms");
				
				number++;
				pd.setProgress(number);
			}
			
			cursor.close();
			serializer.endTag(null, "smss");
			
			serializer.endDocument();
			fos.flush();
			fos.close();
			return true;
		}
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return false;
	}
}
