package com.aweiyo.newmobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

public class MD5Utils {

	/**
	 * md5����
	 * 
	 * @param password
	 * @return
	 */
	public static String encode(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");// ��ȡMD5�㷨����
			byte[] digest = instance.digest(password.getBytes());// ���ַ�������,�����ֽ�����

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff;// ��ȡ�ֽڵĵͰ�λ��Чֵ
				String hexString = Integer.toHexString(i);// ������תΪ16����

				if (hexString.length() < 2) {
					hexString = "0" + hexString;// �����1λ�Ļ�,��0
				}

				sb.append(hexString);
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// û�и��㷨ʱ,�׳��쳣, �����ߵ�����
		}

		return "";
	}

	public static String getFileMd5(String sourceDir) {
		
		File file=new File(sourceDir);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] buffer=new byte[1024];
			int len=0;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			while((len=fis.read(buffer))!=0){
				digest.update(buffer, 0, len);
			}
			byte[] bs = digest.digest();//����ժҪ
			StringBuffer sb = new StringBuffer();
			for (byte b : bs) {
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";
	}
}

