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
 * ���ű��ݵĹ�����
 * @author aweiyoo
 *
 */
public class SmsUtils {

	/**
	 * 1.�ж��û��Ƿ���sdcard   
	 * 2.�ж�Ȩ���Ƿ���� 
	 * 3.д����
	 * @param context  
	 * @return
	 */
	public static boolean backUp(Context context,ProgressDialog pd){
		
		 
		//�ж�sd�Ƿ������
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);
			int count=cursor.getCount();//��ö��ŵ�����
			pd.setMax(count);
			int number=0;//��¼���ݶ��ŵ�����
			
			try {
				//��һ�������ǻ�õ�ǰ��Ŀ¼ �ڶ����Ǳ��ݺ���ļ���
				File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
	System.out.println(Environment.getExternalStorageDirectory());
	System.out.println("�����ˡ�������");
				FileOutputStream fos=new FileOutputStream(file);
				
				XmlSerializer serializer = Xml.newSerializer();
				//λ���л��������������˵���˾���д������д��ʲô�ط�����ʲô����
				serializer.setOutput(fos, "utf-8");
				
				serializer.startDocument("utf-8", true);
			
				serializer.startTag(null, "smss");
				serializer.attribute(null, "smsCount", count+"");//���̶��ŵĸ���д�뱸�ݵ���Ϣ��
				
				
				
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
