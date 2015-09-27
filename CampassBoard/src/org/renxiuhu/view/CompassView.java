package org.renxiuhu.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import org.renxiuhu.smalldemo.R;

/**
 * Created by Tiger on 2015/9/26.
 */
public class CompassView extends View{
    private Paint makerPaint;
    private Paint textPaint;
    private Paint circlePaint;
    private String north,south,east,west;
    private int textHeight;
    private float bearing;
    //默认构造函数
    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }
    //初始化指南针表盘组件
    private void initCompassView(){
        setFocusable(true);//获取焦点
        Resources r=this.getResources();
        //初始化要写入的字符串
        north=r.getString(R.string.cardinal_north);
        south=r.getString(R.string.cardinal_south);
        east=r.getString(R.string.cardinal_east);
        west=r.getString(R.string.cardinal_west);
        //初始化画笔
        circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(r.getColor(R.color.background_color));
        circlePaint.setStrokeWidth(2);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(r.getColor(R.color.text_color));
        textPaint.setTextSize(30);
        textHeight=(int)textPaint.measureText("yY");

        makerPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        makerPaint.setColor(r.getColor(R.color.maker_color));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth=measure(widthMeasureSpec);
        int measureHeight=measure(heightMeasureSpec);
        int d=Math.min(measureHeight,measureWidth);
        /*
        如果是一个View，重写onMeasure时要注意：
            如果在使用自定义view时，用了wrap_content。那么在onMeasure中就要调用setMeasuredDimension来指定view的宽高。
            如果使用的fill_parent或者一个具体的dp值。那么直接使用super.onMeasure即可。
         */
        setMeasuredDimension(d,d);
    }
    /*
    通过MeasureSpec这个类可以获取父View传递过来的一些信息，包括MODE、SIZE属性。这里做一下说明
    MODE:分为一下三种类别,
        AT_MOST：子容器可以是声明大小内的任意大小
        EXACTLY:父容器已经为子容器确定的大小，子容器应该遵守
        UNSPECIFIED:父容器对子容器没有做任何限制，子容器可以任意大小
    SIZE是父容器为子容器提供的大小
        当MODE为AT_MOST时，SIZE大小为父容器所能提供的最大值。
        当MODE为EXACTLY时，SIZE为父容器提供的限制值。
        当MODE为UNSPECIFIED时，大小为0，SIZE完全由子容器的大小决定。
     */
    //根据上面的准则，返回组件大小
    private int measure(int measureSpec){
        int result=0;
        //对测量说明进行解码
        int speMode=MeasureSpec.getMode(measureSpec);
        int speSize=MeasureSpec.getSize(measureSpec);
        if (speMode==MeasureSpec.UNSPECIFIED){
            result=200;//如果没有指定界限，默认返回200
        }else{
            result=speSize;
        }
        return result;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
        //当用户在一个视图操作时调用此方法。事件是按照用户操作类型分类,如TYPE_VIEW_CLICKED
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int mMeasureWidth=getMeasuredWidth();
        int mMeasureHeight=getMeasuredHeight();
        int px=mMeasureWidth/2;
        int py=mMeasureHeight/2;
        int radius=Math.min(px,py);//取最小值作为半径
        //绘制背景
        canvas.drawCircle(px,py,radius,circlePaint);
        canvas.save();
        canvas.rotate(-bearing,px,py);//旋转-bearing角度
        //绘制标记
        int textWidth=(int)textPaint.measureText("W");
        int cardinalX=px-textWidth/2;
        int cardinalY=py-radius+textHeight;
        //每15度绘制一个标记,每45度绘制一个文本
        for (int i=0;i<24;i++){
            canvas.drawLine(px,py-radius,px,py-radius+10,makerPaint);
            canvas.save();
            canvas.translate(0,textHeight);
            //绘制基本方位
            if (i%6==0){
                String dirString="";
                switch (i){
                    case 0:
                        dirString=north;
                        int arrowY=2*textHeight;
                        canvas.drawLine(px,arrowY,px-5,3*textHeight,makerPaint);
                        canvas.drawLine(px,arrowY,px+5,3*textHeight,makerPaint);
                        break;
                    case 6:
                        dirString=east;
                        break;
                    case 12:
                        dirString=south;
                        break;
                    case 18:
                        dirString=west;
                        break;
                    default:break;
                }
                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);// 每45度绘制文本
            }else if(i%3==0){
                String angle=String.valueOf(i*15);
                float angleTextWidth=textPaint.measureText(angle);
                int angleTextX=(int)(px-angleTextWidth/2);
                int angleTextY=py-radius+textHeight;
                canvas.drawText(angle,angleTextX,angleTextY,textPaint);
            }
            canvas.restore();
            canvas.rotate(15,px,py);
        }
        canvas.restore();
    }
    /*
    提供无障碍服务与一个自定义视图的最低要求是实现dispatchPopulateAccessibilityEvent()。
    这个方法被系统调用，要求AccessibilityEvent在自定义视图兼容的辅助功能服务在Android 1.6(API级别4)以上
     */
    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.dispatchPopulateAccessibilityEvent(event);
        if (isShown()){
            String bearingStr=String.valueOf(bearing);
            if (bearingStr.length()>AccessibilityEvent.MAX_TEXT_LENGTH)
                bearingStr=bearingStr.substring(0,AccessibilityEvent.MAX_TEXT_LENGTH);
            event.getText().add(bearingStr);
            return true;
        }else{
            return false;
        }
    }
}

