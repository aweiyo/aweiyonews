package com.aweiyo.pager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aweiyo.zzbj.R;

public class RefreshListView extends ListView implements OnScrollListener,OnItemClickListener{

	private TextView tvTime;
	private ImageView ivArr;
	private TextView tvTitle;
	private int startY=-1                                                   ;
	private int endY;
	private int mHeight;
	private View mHeaderView;
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新
	private int mCurrrentState = STATE_PULL_REFRESH;// 设置当前状态默认为下拉刷新状态
	private RotateAnimation animationDown;
	private RotateAnimation animationUp;
	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterHeight;
	private boolean isLoadingMore=false;//用来记录是否显示正在加载的控件，默认是false
	
	
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addHeaderView();
		addFooterView();
        }
	

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addHeaderView();
		addFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		addHeaderView();
		addFooterView();
	}

	/**
	 * 给listview添加刷新头
	 */
	private void addHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		
		
		//先测量
		mHeaderView.measure(0, 0);
		mHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeight, 0, 0);
		
		initArrowAnim();
		//
		tvTime.setText("最后刷新时间:" +getCurrentTime());
	}
	
	/**
	 * 初始化脚布局
	 */
	public void addFooterView(){
		mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		
		mFooterView.measure(0, 0);
		mFooterHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0,-mFooterHeight, 0, 0);//隐藏
		
		//监听listview的滑动
		this.setOnScrollListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			//正在刷新就直接跳出，不做任何操作
			if(mCurrrentState==STATE_REFRESHING){
				break;
			}
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}
			endY = (int) ev.getRawY();
			int dy=endY-startY;
			if(dy>0&&0==getFirstVisiblePosition()){
				int padding=-mHeight+dy;
				mHeaderView.setPadding(0, 0, 0, 0);
				//padding大于0就是松开状态，padding小于0就是下拉状态
				if(padding>0&&mCurrrentState!=STATE_RELEASE_REFRESH){
					mCurrrentState=STATE_RELEASE_REFRESH;
					refreshState();//改变状态后刷新显示的状态
				}else if(padding<0&&mCurrrentState!=STATE_PULL_REFRESH){
					mCurrrentState=STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY=-1;  
			//如果现在的状态是松开刷新，那么松开后就刷新状态。如果现在的状态还在下拉刷新，说明没有下拉的够厉害，直接隐藏不刷新
			if(mCurrrentState==STATE_RELEASE_REFRESH){
				mCurrrentState=STATE_REFRESHING;//把状态改变成刷新状态
				mHeaderView.setPadding(0, 0, 0, 0);//把刷新状态显示出来
				refreshState();
			}else if(mCurrrentState==STATE_PULL_REFRESH){
				mHeaderView.setPadding(0, -mHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		
		//return true,那么表示该方法消费了此次事件，
		//如果return false，那么表示该方法并未处理完全，该事件仍然需要以某种方式传递下去继续等待处理。
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 获得目前的时间
	 * @return
	 */
	private String getCurrentTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	/**
	 * 刷新下拉刷新的状态
	 */
	private void refreshState(){
		switch (mCurrrentState) {
		
		//下拉状态
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animationDown);
			break;
		
		//刷新状态
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新");
			ivArr.clearAnimation();
			ivArr.setVisibility(View.INVISIBLE);
			if(mListener!=null){
				//实现数据的刷新
				mListener.onRefresh();
			}
			break;
			
		//松开状态
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animationDown);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 初始化箭头动画，包括向上动画和想下动画
	 */
	private void initArrowAnim(){
		animationDown = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationDown.setDuration(200);
		animationDown.setFillAfter(true);
		
		animationUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationUp.setDuration(200);
		animationUp.setFillAfter(true);
	}
	
	
	public void setOnRefreshListener(OnRefreshListener listener){
		mListener=listener;
	}
	
	public interface OnRefreshListener{
		public void onRefresh();
		public void onLoadMore();
	}

	/*
	 * 收起下拉控件
	 */
	public  void onRefreshComplete(boolean success){
		if(isLoadingMore){
			
			//恢复默认的下拉刷新状态
			mCurrrentState=STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArr.setVisibility(View.INVISIBLE);
			
			mHeaderView.setPadding(0, -mHeight, 0, 0);//隐藏掉下拉刷新控件
			//只有刷新成功才更新時間,刷新失敗更新時間是沒有意義的
			if(success)
			{
				tvTime.setText("最后刷新时间:" +getCurrentTime());
			}
			
			//如果加载下一页的控件已经显示，那么这次刷新的时候就隐藏掉
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);
			isLoadingMore=false;//然后将记录下拉控件的值设置为默认值，下次再拉到底部的时候又进行判断
		}else{
			//恢复默认的下拉刷新状态
			mCurrrentState=STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArr.setVisibility(View.INVISIBLE);
			
			mHeaderView.setPadding(0, -mHeight, 0, 0);//隐藏掉下拉刷新控件
			//只有刷新成功才更新時間,刷新失敗更新時間是沒有意義的
			if(success)
			{
				tvTime.setText("最后刷新时间:" +getCurrentTime());
			}
		}
	}

	
	
	/**
	 * 第一是静止状态，SCROLL_STATE_IDLE
	 * 第二是手指滚动状态，SCROLL_STATE_TOUCH_SCROLL
	 * 第三是手指不动了，但是屏幕还在滚动状态。SCROLL_STATE_FLING
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
			//如果滑到底了,就显示出底部的加载控件
			if(getLastVisiblePosition()==getCount()-1&& !isLoadingMore){
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);
				isLoadingMore=true;//如果底部控件显示出来后，就将值设置为true，则下次
				
				if(mListener!=null){
					mListener.onLoadMore();//加载更多数据
				}
			}
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	//重写listview的setOnClickListener
	private OnItemClickListener itemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {//listener是用户传过来的
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		itemClickListener=listener;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		itemClickListener.onItemClick(parent, view, position-2, id);
	}
	
}
