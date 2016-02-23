package com.aweiyo.domain;

import java.util.List;

/**
 * 从服务器接手过来的信息
 * @author aweiyoo
 *
 */
public class NewsData {
	
	public int retcode;
	public List<NewsMenuData> data;
	
	/**
	 * 侧屏栏的数据
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
	 * 新闻页面的数据
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
