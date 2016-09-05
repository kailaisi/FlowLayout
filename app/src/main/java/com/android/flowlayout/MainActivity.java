package com.android.flowlayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
        FlowLayout flowLayout = new FlowLayout(this);
        int padding=UiUtils.dp2px(this,13);
        flowLayout.setPadding(padding,padding,padding,padding);
        Drawable pressDrawable=DrawableUtils.createShape(this,0xffcecece);
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = new TextView(this);
            //设置textview未点击时的背景，圆角+随机颜色，通过xml设置+代码实现
            textView.setBackgroundResource(R.drawable.text_bg);
            //生成随机颜色，为了防止产生黑色或者白色，设定一定的范围
            int color= Color.rgb(new Random().nextInt(200) + 20, new Random().nextInt(200) + 20, new Random().nextInt(200) + 20);
            GradientDrawable drawable= (GradientDrawable) textView.getBackground();
            //将生成的随机色赋值给背景色
            drawable.setColor(color);
            //设置背景为状态选择器
            textView.setBackgroundDrawable(new DrawableUtils().creatStateListDrawable(pressDrawable, drawable));
            textView.setText(datas.get(i));
            textView.setGravity(Gravity.CENTER);
            Log.d("MainActivity", datas.get(i));
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, datas.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            flowLayout.addView(textView);
        }
        scrollView.addView(flowLayout);
    }

    /**
     * 生成要显示的数据
     */
    private void initDatas() {
        String[] strs=new String[]{"QQ","视频","放开那三国","电子书","酒店","单机","小说","斗地主","优酷",
                "网游","WIFI万能钥匙","播放器","捕鱼达人2","机票","游戏","熊出没之熊大快跑","美图秀秀","浏览器",
                "单机游戏","我的世界","电影电视","QQ空间","旅游","免费游戏","2048","刀塔传奇","壁纸","节奏大师",
                "锁屏","装机必备","天天动听","备份","网盘","海淘网","大众点评","爱奇艺视频","腾讯手机管家",
                "百度地图","猎豹清理大师","谷歌地图","hao123上网导航","京东","youni有你","万年历-农历黄历","支付宝钱包"};
        datas=new ArrayList<>(Arrays.asList(strs));
    }

    private void initViews() {
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
    }
}
