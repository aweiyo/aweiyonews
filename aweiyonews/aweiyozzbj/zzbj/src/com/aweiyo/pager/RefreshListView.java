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
	private static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	private static final int STATE_REFRESHING = 2;// ����ˢ��
	private int mCurrrentState = STATE_PULL_REFRESH;// ���õ�ǰ״̬Ĭ��Ϊ����ˢ��״̬
	private RotateAnimation animationDown;
	private RotateAnimation animationUp;
	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterHeight;
	private boolean isLoadingMore=false;//������¼�Ƿ���ʾ���ڼ��صĿؼ���Ĭ����false
	
	
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
	 * ��listview���ˢ��ͷ
	 */
	private void addHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		
		
		//�Ȳ���
		mHeaderView.measure(0, 0);
		mHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeight, 0, 0);
		
		initArrowAnim();
		//
		tvTime.setText("���ˢ��ʱ��:" +getCurrentTime());
	}
	
	/**
	 * ��ʼ���Ų���
	 */
	public void addFooterView(){
		mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		
		mFooterView.measure(0, 0);
		mFooterHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0,-mFooterHeight, 0, 0);//����
		
		//����listview�Ļ���
		this.setOnScrollListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			//����ˢ�¾�ֱ�������������κβ���
			if(mCurrrentState==STATE_REFRESHING){
				break;
			}
			if (startY == -1) {// ȷ��startY��Ч
				startY = (int) ev.getRawY();
			}
			endY = (int) ev.getRawY();
			int dy=endY-startY;
			if(dy>0&&0==getFirstVisiblePosition()){
				int padding=-mHeight+dy;
				mHeaderView.setPadding(0, 0, 0, 0);
				//padding����0�����ɿ�״̬��paddingС��0��������״̬
				if(padding>0&&mCurrrentState!=STATE_RELEASE_REFRESH){
					mCurrrentState=STATE_RELEASE_REFRESH;
					refreshState();//�ı�״̬��ˢ����ʾ��״̬
				}else if(padding<0&&mCurrrentState!=STATE_PULL_REFRESH){
					mCurrrentState=STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY=-1;  
			//������ڵ�״̬���ɿ�ˢ�£���ô�ɿ����ˢ��״̬��������ڵ�״̬��������ˢ�£�˵��û�������Ĺ�������ֱ�����ز�ˢ��
			if(mCurrrentState==STATE_RELEASE_REFRESH){
				mCurrrentState=STATE_REFRESHING;//��״̬�ı��ˢ��״̬
				mHeaderView.setPadding(0, 0, 0, 0);//��ˢ��״̬��ʾ����
				refreshState();
			}else if(mCurrrentState==STATE_PULL_REFRESH){
				mHeaderView.setPadding(0, -mHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		
		//return true,��ô��ʾ�÷��������˴˴��¼���
		//���return false����ô��ʾ�÷�����δ������ȫ�����¼���Ȼ��Ҫ��ĳ�ַ�ʽ������ȥ�����ȴ�����
		return super.onTouchEvent(ev);
	}
	
	/**
	 * ���Ŀǰ��ʱ��
	 * @return
	 */
	private String getCurrentTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	/**
	 * ˢ������ˢ�µ�״̬
	 */
	private void refreshState(){
		switch (mCurrrentState) {
		
		//����״̬
		case STATE_PULL_REFRESH:
			tvTitle.setText("����ˢ��");
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animationDown);
			break;
		
		//ˢ��״̬
		case STATE_REFRESHING:
			tvTitle.setText("����ˢ��");
			ivArr.clearAnimation();
			ivArr.setVisibility(View.INVISIBLE);
			if(mListener!=null){
				//ʵ�����ݵ�ˢ��
				mListener.onRefresh();
			}
			break;
			
		//�ɿ�״̬
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("�ɿ�ˢ��");
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animationDown);
			break;

		default:
			break;
		}
	}
	
	/**
	 * ��ʼ����ͷ�������������϶��������¶���
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
	 * ���������ؼ�
	 */
	public  void onRefreshComplete(boolean success){
		if(isLoadingMore){
			
			//�ָ�Ĭ�ϵ�����ˢ��״̬
			mCurrrentState=STATE_PULL_REFRESH;
			tvTitle.setText("����ˢ��");
			ivArr.setVisibility(View.INVISIBLE);
			
			mHeaderView.setPadding(0, -mHeight, 0, 0);//���ص�����ˢ�¿ؼ�
			//ֻ��ˢ�³ɹ��Ÿ��r�g,ˢ��ʧ�����r�g�Ǜ]�����x��
			if(success)
			{
				tvTime.setText("���ˢ��ʱ��:" +getCurrentTime());
			}
			
			//���������һҳ�Ŀؼ��Ѿ���ʾ����ô���ˢ�µ�ʱ������ص�
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);
			isLoadingMore=false;//Ȼ�󽫼�¼�����ؼ���ֵ����ΪĬ��ֵ���´��������ײ���ʱ���ֽ����ж�
		}else{
			//�ָ�Ĭ�ϵ�����ˢ��״̬
			mCurrrentState=STATE_PULL_REFRESH;
			tvTitle.setText("����ˢ��");
			ivArr.setVisibility(View.INVISIBLE);
			
			mHeaderView.setPadding(0, -mHeight, 0, 0);//���ص�����ˢ�¿ؼ�
			//ֻ��ˢ�³ɹ��Ÿ��r�g,ˢ��ʧ�����r�g�Ǜ]�����x��
			if(success)
			{
				tvTime.setText("���ˢ��ʱ��:" +getCurrentTime());
			}
		}
	}

	
	
	/**
	 * ��һ�Ǿ�ֹ״̬��SCROLL_STATE_IDLE
	 * �ڶ�����ָ����״̬��SCROLL_STATE_TOUCH_SCROLL
	 * ��������ָ�����ˣ�������Ļ���ڹ���״̬��SCROLL_STATE_FLING
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
			//�����������,����ʾ���ײ��ļ��ؿؼ�
			if(getLastVisiblePosition()==getCount()-1&& !isLoadingMore){
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);
				isLoadingMore=true;//����ײ��ؼ���ʾ�����󣬾ͽ�ֵ����Ϊtrue�����´�
				
				if(mListener!=null){
					mListener.onLoadMore();//���ظ�������
				}
			}
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	//��дlistview��setOnClickListener
	private OnItemClickListener itemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {//listener���û���������
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
