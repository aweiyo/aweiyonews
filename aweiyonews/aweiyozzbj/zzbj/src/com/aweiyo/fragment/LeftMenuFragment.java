package com.aweiyo.fragment;

import java.util.List;

import com.aweiyo.domain.NewsData;
import com.aweiyo.domain.NewsData.NewsMenuData;
import com.aweiyo.pager.NewsCenterPager;
import com.aweiyo.zzbj.HomeActivity;
import com.aweiyo.zzbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment {
	
//	@ViewInject(R.id.listview)
	private ListView listview;
	private List<NewsMenuData> list;//����date�ļ���
	/**
	 * ��¼listview��Ŀ����ĵڼ���
	 */
	private  int clickItem;
	private MenuAdapter adapter;
	
	
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu	, null);
		listview=(ListView) view.findViewById(R.id.listview);
//		ViewUtils.inject(mActivity,view);
		return view;
	}
	
	
	@Override
		public void initDate() {
		/**
		 * ����listview��item�ĵ��
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				clickItem=position;
				adapter.notifyDataSetChanged();//adapter��ˢ��
				//itemһ��������õ�ǰ������ҳ��
				setCurrentDetailMenuPager(position);
				toggleSlingMenu();
			}
		});// ����listview��item����¼�
		}
	
	/**
	 * �л�slingmenu��״̬
	 */
	protected void toggleSlingMenu() {
		HomeActivity activity=(HomeActivity) mActivity;
		SlidingMenu menu = activity.getSlidingMenu();
		menu.toggle();
	}


	protected void setCurrentDetailMenuPager(int position) {
	HomeActivity mainUI= (HomeActivity) mActivity;
	HomeFragment homeMenuFragment = mainUI.getHomeMenuFragment();
	NewsCenterPager newsCenterPager = homeMenuFragment.getNewsCenterPager();
	newsCenterPager.setCurrentDetailMenuPager(position);
	}


	/**
	 * �ӷ������ò���������
	 * @param data
	 */
	public void setMenuData(NewsData data){
		list = data.data;	
		adapter = new MenuAdapter();
		listview.setAdapter(adapter);//�������ʹ�� new MenuAdapter(),��Ϊadapter��ˢ�¾�����һ��������������˵�
		
	}
	
	
	class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(mActivity, R.layout.list_menu_left_item, null);
			TextView tvTile=(TextView) view.findViewById(R.id.tv_title);	
			tvTile.setText(list.get(position).title);
			if(clickItem==position){
				tvTile.setEnabled(true);
			}else {
				tvTile.setEnabled(false);
			}
			
			return view;
		}
		
	}
	
}
