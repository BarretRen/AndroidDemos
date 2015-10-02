本项目模拟QQ左右滑动切换出菜单界面，并且抽屉式侧滑和缩放式侧滑动画效果

基本思路是继承HorizontalScrollView然后自定义一个可以左右滑动的View，用于左侧Menu和主界面的滑动切换

自定义viewgroup关键
	1，onMeasure：决定内部view和子view的宽高，和自身的宽高
	2，onLayout：决定子view的放置位置
	3，onTouchEvent
	
自定义的View的设置属性：
	1，编写values/attr.xml
	2，使用view或view的3个参数值的构造函数(Context context, AttributeSet attrs, int defStyleAttr)
	3，通过TypedArray获取到包含属性值的数组
	4，遍历数组中的自定义属性，当找到和attr中定义的属性名一致的属性时通过TypedArray的getDimension方法获取属性值
	5，获取值之后可以作为变量使用
	6，回收TypedArray的空间	

动画效果1.0——抽屉式侧滑：
	左侧菜单仿佛在主界面下面
	思路：默认情况下随着滑动MenuView会逐步显示出来，如果在滑动的同时设置MenuView的偏移量，就好像MenuView一直在底层一样
		使用属性动画控制translationX偏移量

动画效果2.0——抽屉侧滑+缩放:
	主界面滑动时的缩放效果，向右滑动缩小，向左滑动增大
	MenuView的缩放效果，向左滑动缩小，向右滑动增大
	