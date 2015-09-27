package org.huge.loadmorelistview;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener; 

public class LoadListView extends ListView implements OnScrollListener{
	//底部加载声明
	private int lastVisibleItem;//最后一个可见项
	private int totalItems;//所有项
	private View footer;//底部布局
	private boolean isLoading=false;
	private ILoadListener loadListener;
	//顶部下拉刷新声明
	private int firstVisiableItem;//第一可见项
	private View header;//顶部布局
	private boolean isRemark=false;//判断在当前也没的最顶端并下滑
	private int startY;//Y坐标值
	private RLoadListener rListener;
	private int scrollState;//当前滚动状态
	private int headerHeight;//顶部布局文件的高度
	private int state;//
	//当前滚动状态常量
	final int NONE=0;//正常状态
	final int PULL=1;//提示下拉状态
	final int RELEASE=2;//提示释放状态
	final int REFLASHING=3;//正在刷新状态
	
	public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LoadListView(Context context) {
		super(context);
		initView(context);
	}
	//初始化布局组件
	private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		footer=inflater.inflate(R.layout.footer, null);
		header=inflater.inflate(R.layout.header, null);
		footer.findViewById(R.id.footer).setVisibility(View.GONE);
		
		measureView(header);
		headerHeight=header.getMeasuredHeight();
		topPadding(-headerHeight);//设置-headerHeight，保证启动时看不到顶部的刷新提示
		this.addFooterView(footer);//添加自定义布局到底部
		this.addHeaderView(header);//添加自定义布局到顶部
		this.setOnScrollListener(this);
	}
	
	//通知父组件占用的宽高
	private void measureView(View view){
		ViewGroup.LayoutParams params=view.getLayoutParams();
		if (params==null) {
			params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		int width=ViewGroup.getChildMeasureSpec(0, 0, params.width);
		int height;
		int tempHeight=params.height;
		if (tempHeight>0) {
			height=MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		}else{
			height=MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
	//=====================footer底部加载更多功能实现
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem=firstVisibleItem+visibleItemCount;
		this.totalItems=totalItemCount;
		this.firstVisiableItem=firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState=scrollState;
		//判断到达底部的方法：lastVisibleItem == totalItems；即最后一个可见项的标值等于总Item数量的时候
		if (lastVisibleItem==totalItems && scrollState==SCROLL_STATE_IDLE) {
			if (!isLoading) {//判断是不是正在加载
				footer.findViewById(R.id.footer).setVisibility(View.VISIBLE);
				loadListener.onLoad();
				isLoading=true;
			}
		}
	}
	
	public void loadCompleted(){
		isLoading=false;
		footer.findViewById(R.id.footer).setVisibility(View.GONE);
	}
	/*
	 * 加载更多数据的回调接口
	 */
	public interface ILoadListener{
		public void onLoad();
	}
	
	public void setInterface(ILoadListener listener){
		this.loadListener=listener;
	}
	
	//======================header下拉刷新实现
	//设置header布局的上边距
		private void topPadding(int topPadding){
			header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
			header.invalidate();
		}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://下拉
			if (firstVisiableItem==0) {
				isRemark=true;
				startY=(int)ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE://移动
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP://上拉
			if (state==RELEASE) {//如果当前处于释放状态，则刷新数据
				state=REFLASHING;
				//加载最新数据
				reflashViewByState();
				rListener.onRefresh();
			}else if (state==PULL) {//如果当前处于下拉状态，则返回正常状态
				state=NONE;
				isRemark=false;
				reflashViewByState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	//判读移动过程操作
	private void onMove(MotionEvent ev){
		if (!isRemark) {
			return;
		}
		int tempY=(int)ev.getY();
		int space=tempY-startY;
		int topPadding=space-headerHeight;
		switch (state) {
		case NONE:
			if (space>0) {//大于0，表示向下滑动，变为下拉状态
				state=PULL;
				reflashViewByState();
			}
			break;
		case PULL://如果正在拉下滚动
			topPadding(topPadding);//保持header同步滑动显示
			if (space>headerHeight+30 && scrollState==SCROLL_STATE_TOUCH_SCROLL) {//下拉到移动距离变为释放状态，组件不再下滑
				state=RELEASE;
				reflashViewByState();
			}
			break;
		case RELEASE://如果处于释放状态，则根据下滑距离设置当前当前状态
			topPadding(topPadding);
			if (space<headerHeight+30) {
				state=PULL;
				reflashViewByState();
			}else if (space<=0) {
				state=NONE;
				isRemark=false;
				reflashViewByState();
			}
			break;
		}
	}
	
	//根据当前状态，改变界面显示
	private void reflashViewByState(){
		TextView tip=(TextView)header.findViewById(R.id.tip);
		ImageView arrow=(ImageView)header.findViewById(R.id.arrow);
		ProgressBar progress=(ProgressBar)header.findViewById(R.id.progress);
		//设置左右旋转的两种动画
		RotateAnimation animation0=new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
		animation0.setDuration(500);
		animation0.setFillAfter(true);
		RotateAnimation animation1=new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
		animation1.setDuration(500);
		animation1.setFillAfter(true);
		//更具四种状态设置界面
		switch (state) {
		case NONE:
			arrow.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("下拉可以刷新!");
			arrow.clearAnimation();
			arrow.setAnimation(animation1);
			break;
		case RELEASE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("松开可以刷新!");
			arrow.clearAnimation();
			arrow.setAnimation(animation0);
			break;
		case REFLASHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("正在刷新...");
			arrow.clearAnimation();
			break;
		}
	}
	
	//获取完数据之后
	public void reflashComplete(){
		state=NONE;
		isRemark=false;
		reflashViewByState();
		TextView lastupdatetime=(TextView)header.findViewById(R.id.lastupdate_time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
		Date date=new Date(System.currentTimeMillis());
		lastupdatetime.setText(format.format(date));
	}
	/*
	 * 下拉刷新的回调接口
	 */
	public interface RLoadListener{
		public void onRefresh();
	}
	
	public void setRefreshInterface(RLoadListener listener){
		this.rListener=listener;
	}
}