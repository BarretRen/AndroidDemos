package org.huge.slideviewqq;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class LeftSlideMenu extends HorizontalScrollView {
	private LinearLayout mWapper;//水平滚动view里第一级LinearLayout
	private ViewGroup mMenu;//左侧的Menu布局
	private ViewGroup mContent;//主界面布局
	
	private int mScreenWidth;//屏幕宽度
	private int mMenuWidth;//menuView的宽度
	private int mMenuRightPadding;//左侧Menu与屏幕右边距的距离
	
	private boolean once=false;//防止onMeasure多次调用
	private boolean changed=false;//防止onLayout多次调用
	
	/*
	 * 未自定义组件属性时使用
	 */
	public LeftSlideMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	/*
	 * 使用了自定义组件属性时使用
	 */
	public LeftSlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//获取自定义的属性
		TypedArray array=context.getTheme().obtainStyledAttributes(attrs, R.styleable.LeftSlideMenu, defStyleAttr, 0);
		int n=array.getIndexCount();//获取自定义属性数量
		for(int i=0;i<n;i++){
			int attr=array.getIndex(i);
			switch (attr) {
			case R.styleable.LeftSlideMenu_rightPadding://如果获取到的是attr中定义的属性，获取它的值(指定默认值为50dp)
				mMenuRightPadding=array.getDimensionPixelSize(attr, 
						(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
				break;
			}
		}
		array.recycle();//释放TypedArray
		//获取屏幕宽度
		WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics=new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		mScreenWidth=metrics.widthPixels;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!changed) {
			//将scrollview的起始位置滑动到menuWidth，正好可以使menuview无法显示
			this.scrollTo(mMenuWidth, 0);
			changed=true;
		}
	}
	
	/*
	 * 设置子View和自己的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mWapper=(LinearLayout)getChildAt(0);//获取第一级LinearLayout
			mMenu=(ViewGroup)mWapper.getChildAt(0);//获取MenuView
			mContent=(ViewGroup)mWapper.getChildAt(1);//主界面
			//设置左侧Menu和主界面的宽度
			mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
			mContent.getLayoutParams().width=mScreenWidth;
			once=true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/*
	 * 通过设置偏移量，将menuView隐藏
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			//判断当隐藏在左边的宽度小于menuView的一半时，将menuview显示出来
			int scrollX=getScrollX();//隐藏在左边的宽度
			if (scrollX >=mMenuWidth/2) {
				this.smoothScrollTo(mMenuWidth, 0);//隐藏
			}else {
				this.smoothScrollTo(0, 0);//显示
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	/*
	 * 滚动触发时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		//形成左侧菜单仿佛在主界面下面效果
		//调用属性动画，设置TranslationX,即X方向上偏移量
		mMenu.setTranslationX(l*0.8f);//0.8-0
		
		//缩放
		float scale=l*1.0f/mMenuWidth;//1-0
		//主界面滑动时的缩放效果，向右滑动缩小，向左滑动增大
		float rightScale=0.7f+0.3f*scale;//1-0.7
		//设置缩放中心点
		mContent.setPivotX(0);
		mContent.setPivotY(mContent.getHeight()/2);
		//设置缩放比率
		mContent.setScaleX(rightScale);
		mContent.setScaleY(rightScale);
		
		//MenuView的缩放效果，向左滑动缩小，向右滑动增大
		float leftScale=1-scale*0.3f;//0.7-1
		mMenu.setPivotX(0);
		mMenu.setPivotY(mMenuWidth/2);
		mMenu.setScaleX(leftScale);
		mMenu.setScaleY(leftScale);
		
	}
}
