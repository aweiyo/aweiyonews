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
	private List<NewsMenuData> list;//侧栏date的集合
	/**
	 * 记录listview条目点击的第几个
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
		 * 监听listview的item的点击
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				clickItem=position;
				adapter.notifyDataSetChanged();//adapter的刷新
				//item一点击就设置当前的详情页面
				setCurrentDetailMenuPager(position);
				toggleSlingMenu();
			}
		});// 监听listview的item点击事件
		}
	
	/**
	 * 切换slingmenu的状态
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
	 * 从服务器拿侧屏的数据
	 * @param data
	 */
	public void setMenuData(NewsData data){
		list = data.data;	
		adapter = new MenuAdapter();
		listview.setAdapter(adapter);//这儿不能使用 new MenuAdapter(),因为adapter的刷新就是这一个，这个是配置了的
		
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
