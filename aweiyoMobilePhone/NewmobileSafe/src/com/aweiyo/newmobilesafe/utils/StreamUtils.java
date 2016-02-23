package com.aweiyo.newmobilesafe.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	/**
	 * 读取文件流转换成string的工具类
	 * @return
	 * @throws IOException 
	 */
	public static String  readFromStream(InputStream is) throws IOException{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		byte[] bus=new byte[1024];
		int len=0;
		while((len=(is.read(bus)))!=-1){
			bos.write(bus, 0, len);
		}
		bos.close();
		is.close();
		return bos.toString();
	}
}
