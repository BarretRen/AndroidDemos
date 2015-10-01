需要事先制作
	 *target：image——识别图标；cylinder——识别立体图型；Multi——识别多个立体图形；Frame Markers——识别多个图案；Object——对象识别
不需要事先制作
     * User Defined——当用户拍摄一幅图像时，可以实时将这幅图作为Target使用，然后就会显示Augmented（一个茶壶）。仅用于2G
     * Text——识别文字，仅限英文
     * Virtual Buttons——虚拟按钮，可以动态控制虚拟图形
     * Cloud——云存储识别，Target和其他数据放在云端
基本使用流程：
	1，初始化Vuforia
	2，初始化Tracker
	3，加载Tracker所需数据——可以开始使用了
	4，设置摄像头识别区域（尺寸），打开摄像头，校准摄像头
	5，启动tracker，设置摄像头聚焦模式
	