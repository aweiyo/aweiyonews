package com.aweiyo.domain;

import java.util.ArrayList;

public class PhotosData {
	public int retcode;
	public PhotoInfos data;
	
	public class PhotoInfos{
		public String title;
		public ArrayList<PhotoInfo> news;
	}
	
	public class PhotoInfo{
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}
}
