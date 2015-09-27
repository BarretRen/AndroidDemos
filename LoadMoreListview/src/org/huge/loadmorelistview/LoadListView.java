<<<<<<< HEAD
package org.huge.loadmorelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener; 

public class LoadListView extends ListView implements OnScrollListener{
	private int lastVisibleItem;//最后一个可见项
	private int totalItems;//所有项
	private View footer;//底部布局
	private boolean isLoading=false;
	private ILoadListener loadListener;
	
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
	
	private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		footer=inflater.inflate(R.layout.footer, null);
		footer.findViewById(R.id.footer).setVisibility(View.GONE);
		this.addFooterView(footer);//添加自定义布局到底部
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem=firstVisibleItem+visibleItemCount;
		this.totalItems=totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
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
}
=======
package org.huge.loadmorelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener; 

public class LoadListView extends ListView implements OnScrollListener{
	private int lastVisibleItem;//最后一个可见项
	private int totalItems;//所有项
	private View footer;//底部布局
	private boolean isLoading=false;
	private ILoadListener loadListener;
	
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
	
	private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		footer=inflater.inflate(R.layout.footer, null);
		footer.findViewById(R.id.footer).setVisibility(View.GONE);
		this.addFooterView(footer);//添加自定义布局到底部
		this.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem=firstVisibleItem+visibleItemCount;
		this.totalItems=totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
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
}
>>>>>>> origin/master
