package com.example.page2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangdan.lvbao.FragmentTwo;
import com.example.wangdan.lvbao.R;

/**
 * Created by wangdan on 2016/8/5.
 */
public class tour4 extends Activity {
    private ImageView back;
    private ImageView save;
    private ImageView share;
    private boolean flag;
    private ImageView top;
    private GoTopScrollView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour4);
        back = (ImageView)findViewById(R.id.back);
        save = (ImageView)findViewById(R.id.save);
        share = (ImageView)findViewById(R.id.share);
        top = (ImageView)findViewById(R.id.top);
        sc = (GoTopScrollView)findViewById(R.id.myScrollView);
        //4.0获取屏幕高度
        sc.setImgeViewOnClickGoToFirst(top);
        //4.0获取屏幕高度
        int screenHeight=ScreenUtil.getScreenSize(getApplicationContext())[1];
        //设置ScrollView滑动多少距离就显示，低于多少就显示 如果没有设置就默认为500;(这里我们设置屏幕高度)
        sc.setScreenHeight(screenHeight);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.post(new Runnable() {
                    @Override
                    public void run() {
                        sc.fullScroll(NestedScrollView.FOCUS_UP);
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    save.setImageResource(R.drawable.on);
                    flag = false;
                } else if (flag == false) {
                    save.setImageResource(R.drawable.off);
                    flag = true;
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
