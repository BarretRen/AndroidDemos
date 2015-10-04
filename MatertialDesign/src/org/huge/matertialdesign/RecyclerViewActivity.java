package org.huge.matertialdesign;

import java.util.ArrayList;

import org.huge.adapter.MyDefineDivider;
import org.huge.adapter.SampleRecyclerAdapter;
import org.huge.matertialdesign.R.id;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.view.ViewOutlineProvider;

public class RecyclerViewActivity extends Activity {
	private RecyclerView recyclerView;//新版Listview
	private View deleteBar;
	private ImageButton fabView;
	private ArrayList<String> data;//要显示的数据源
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData(20);
		initView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//通过改变layoutmanager修改布局样式
		switch (item.getItemId()) {
		case R.id.action_gridview:
			recyclerView.setLayoutManager(new GridLayoutManager(this, 3));//3列
			break;
		case R.id.action_listview:
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			break;
		case R.id.action_hor_gridview:
			//3行
			recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
			break;
		case R.id.action_staggered:
			//进入瀑布流界面
			startActivity(new Intent(RecyclerViewActivity.this,StraggerListviewActivity.class));
			break;
		default:
			break;
		}
		return true;
	}
	
	private void initView() {
		deleteBar=(View)findViewById(R.id.deletebar);
		//创建右下角圆形按钮,将其他形状剪切成圆形
		ViewOutlineProvider provider=new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				//获取按钮尺寸
				int fabSize=getResources().getDimensionPixelSize(R.dimen.fab_size);
				//设置轮廓的尺寸
				outline.setOval(-4,-4,fabSize+2,fabSize+2);
			}
		};
		fabView=(ImageButton)findViewById(R.id.fab_add);//右下角添加按钮
		fabView.setOutlineProvider(provider);//绑定view和轮廓提供者，自动裁剪为圆形
		
		//获取RecyclerView,使用布局管理器设置布局方向
		recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
		final LinearLayoutManager manager=new LinearLayoutManager(this);
		recyclerView.setLayoutManager(manager);
		//为RecyclerView设置分隔条
		ItemDecoration itemDecoration=new MyDefineDivider(this);
		recyclerView.addItemDecoration(itemDecoration);
		//设置adapter
		final SampleRecyclerAdapter adapter=new SampleRecyclerAdapter(this,data);
		recyclerView.setAdapter(adapter);
		//设置列表项动画效果
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		//设置按钮点击
		fabView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取第一个可见列表项的位置
				int position=manager.findFirstCompletelyVisibleItemPosition();
				adapter.addItem(1);
			}
		});
		deleteBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position=manager.findFirstCompletelyVisibleItemPosition();
				adapter.removeData(1);
			}
		});
	}

	//初始化数据填充
	public void initData(int size){
		data=new ArrayList<String>();
		for(int i=0;i<size;i++){
			data.add("新的列表项<"+i+">");
		}
	}
}
