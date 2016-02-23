package com.aweiyo.detail;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.domain.PhotosData;
import com.aweiyo.domain.PhotosData.PhotoInfo;
import com.aweiyo.global.GlobalContants;
import com.aweiyo.utils.CacheUtils;
import com.aweiyo.zzbj.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * ��ͼ�˵�����ҳ
 * @author aweiyoo
 *
 */
public class PhotoMenuDetailFragment extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private ArrayList<PhotoInfo> imageList;
	private ImageButton mBtnButton;
	
	public PhotoMenuDetailFragment(Activity activity,ImageButton btnPhoto) {
		super(activity);
		this.mBtnButton=btnPhoto;
		//����mBtnButton�ĵ���¼�
		mBtnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeDisplay();//�ı�ͼƬ����ͼ�б�
			}
		});
	}

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.menu_photo_pager, null);
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		return view;
	}

	@Override
	public void initData() {
		String cache=CacheUtils.getCache(mActivity, GlobalContants.PHOTOS_URL);
		if(!TextUtils.isEmpty(cache)){
			parseData(cache);
		}else{
			getDateFromServer();
		}
		
	
	}
	
	private boolean isListDisplay=true;
	private PhotoAdapter    adapter;
	protected void changeDisplay() {
		if(isListDisplay){
			isListDisplay=false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			mBtnButton.setImageResource(R.drawable.icon_pic_list_type);
		}else{
			isListDisplay=true;
			lvPhoto.setVisibility(View.VISIBLE);
			//���ؼ�visibility����ΪINVISIBLEʱ��
			//���汣����view�ؼ���ռ�еĿռ䣻���ؼ�����ΪGONEʱ�������򲻱���view�ؼ���ռ�еĿռ䡣
			gvPhoto.setVisibility(View.GONE); 
			mBtnButton.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}

	private void getDateFromServer(){
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
					String resultl=responseInfo.result;
					parseData(resultl);
					CacheUtils.setCache(mActivity, GlobalContants.PHOTOS_URL, resultl);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, 0).show();
			}
		});
	}

	protected void parseData(String resultl) {
		Gson gson=new Gson();
		PhotosData data = gson.fromJson(resultl, PhotosData.class);
		imageList = data.data.news;
		
		//����ͼ����adapter
		 if (imageList!=null) {
			    adapter = new PhotoAdapter();
				lvPhoto.setAdapter(adapter);
				gvPhoto.setAdapter(adapter);
			}
		
	}
	
	class PhotoAdapter extends BaseAdapter{

		
		private BitmapUtils utils;

		public PhotoAdapter(){
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageList.size();
		}

		@Override
		public PhotoInfo getItem(int position) {
			// TODO Auto-generated method stub
			return imageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=View.inflate(mActivity, R.layout.list_photo_item, null);
				viewHolder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
				viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			
//			PhotoInfo info = imageList.get(position);
			PhotoInfo info=getItem(position);
//			viewHolder.ivPic.setBackground(info.)
			utils.display(viewHolder.ivPic, info.listimage);//��һ��������ͼƬ�ؼ����ڶ���������ͼƬ�ڷ������ϵ�url
			viewHolder.tvTitle.setText(info.title);
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public TextView tvTitle;
		public ImageView ivPic;
	}
}
