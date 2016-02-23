package com.aweiyo.domain;

import java.util.List;

/**
 * �ӷ��������ֹ�������Ϣ
 * @author aweiyoo
 *
 */
public class NewsData {
	
	public int retcode;
	public List<NewsMenuData> data;
	
	/**
	 * ������������
	 */
	public 	class NewsMenuData{
		public String id;
		public String title;
		public int type;
		public String url;
		public List<NewsCenterData> children;
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + "]";
		}
		
	}
	
	/**
	 * ����ҳ�������
	 * @author aweiyoo
	 *
	 */
	public class NewsCenterData{
		public String id;
		public String title;
		public String type;
		public String url;
		@Override
		public String toString() {
			return "NewsCenterData [title=" + title + "]";
		}
		
	}

	@Override
	public String toString() {
		return "NewsData [data=" + data + "]";
	}
	
	
}
