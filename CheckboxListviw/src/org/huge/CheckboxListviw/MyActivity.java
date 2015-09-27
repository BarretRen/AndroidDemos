package org.huge.CheckboxListviw;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity {
    private ListView listView;
    private Button ok,select,all,cancel;
    private ArrayList<Food> foods = new ArrayList<Food>();
    private MyListViewAdapter adapter;
    private CheckBox checkBox;
    private boolean isShowed=false;
    //接受message消息，然后改变listview的adapter
    private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==1) {
				adapter=new MyListViewAdapter(foods, getApplicationContext(),true);
				listView.setAdapter(adapter);
				isShowed=true;
				cancel.setVisibility(View.VISIBLE);
				all.setVisibility(View.VISIBLE);
			}else if (msg.what==0) {
				adapter=new MyListViewAdapter(foods, getApplicationContext(),false);
				listView.setAdapter(adapter);
				isShowed=false;
				cancel.setVisibility(View.GONE);
				all.setVisibility(View.GONE);
			}
		}
    };
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        //��ʼ�����
        listView=(ListView)findViewById(R.id.drink_list);
        View view= LayoutInflater.from(this).inflate(R.layout.item,null);
        checkBox=(CheckBox)view.findViewById(R.id.check_box);
        initData();//��ʼ������
        adapter=new MyListViewAdapter(foods,getApplicationContext(),false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
                MyListViewAdapter.ViewHolder holder = (MyListViewAdapter.ViewHolder) view.getTag();
                // �ı�CheckBox��״̬
                holder.cb.toggle();
                // ��CheckBox��ѡ��״����¼����
                MyListViewAdapter.getIsSelected().put(position, holder.cb.isChecked());
            }
        });
        
        select=(Button)findViewById(R.id.btn_select);
        all=(Button)findViewById(R.id.btn_all);
        cancel=(Button)findViewById(R.id.btn_cancel);
        ok=(Button)findViewById(R.id.btn_commit);
        if (isShowed) {
			select.setText("取消");
			cancel.setVisibility(View.VISIBLE);
			all.setVisibility(View.VISIBLE);
		}else{
			select.setText("点餐");
			cancel.setVisibility(View.GONE);
			all.setVisibility(View.GONE);
		}
        //设置按钮的监听程序
        select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isShowed) {
					handler.sendEmptyMessage(0);
					select.setText("点餐");
				}else{
					handler.sendEmptyMessage(1);
					select.setText("取消");
				}
			}
		});
        all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//遍历checkbox的map，将所有值变为true
				for(int i=0;i<foods.size();i++){
					MyListViewAdapter.getIsSelected().put(i, true);
				}
				adapter.notifyDataSetChanged();//通知数据变化
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i=0;i<foods.size();i++){
					MyListViewAdapter.getIsSelected().put(i, false);
				}
				adapter.notifyDataSetChanged();
			}
		});
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                if (id==R.id.btn_commit)
                    myPrice();
            }
        });
        
    }

    public void initData(){
        Class cls = R.drawable.class;//反射
        try {
            foods.add(new Food(cls.getDeclaredField("d1").getInt(null), "猕猴桃汁",
                    "10"));
            foods.add(new Food(cls.getDeclaredField("d2").getInt(null), "橙汁",
                    "12"));
            foods.add(new Food(cls.getDeclaredField("d3").getInt(null), "啤酒",
                    "15"));
            foods.add(new Food(cls.getDeclaredField("d4").getInt(null), "葡萄汁",
                    "10"));
            foods.add(new Food(cls.getDeclaredField("d5").getInt(null), "纯麦奶茶",
                    "8"));
            foods.add(new Food(cls.getDeclaredField("d6").getInt(null), "薄荷汁",
                    "10"));
            foods.add(new Food(cls.getDeclaredField("d7").getInt(null), "柠檬薄荷",
                    "12"));
            foods.add(new Food(cls.getDeclaredField("d8").getInt(null), "椰子汁",
                    "10"));
            foods.add(new Food(cls.getDeclaredField("d9").getInt(null), "珍珠奶茶",
                    "9"));
            foods.add(new Food(cls.getDeclaredField("d10").getInt(null), "石榴汁",
                    "10"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �����ܼ۸�ķ���
     * */
    public void myPrice() {
        HashMap<Integer, Boolean> map = MyListViewAdapter.getIsSelected();
        String str = "";
        int money = 0;
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                str += (i + " ");
                money += Integer.parseInt(foods.get(i).food_price);
            }
        }
        MyListViewAdapter.getIsSelected().get("");
        Toast.makeText(getApplicationContext(),
                "已选中了" + str + "项,总价钱为:" + money, Toast.LENGTH_SHORT).show();
    }
}
