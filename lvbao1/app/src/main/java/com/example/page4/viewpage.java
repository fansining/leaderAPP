package com.example.page4;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.wangdan.lvbao.R;

import java.util.ArrayList;

/**
 * Created by wu on 2016/12/11.
 */
public class viewpage extends Activity {
    private ArrayList<View> pageview;
    private ImageView[] imageViews;
    int image[] = {R.drawable.img10, R.drawable.img9, R.drawable.img8};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popviewpage);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        ViewGroup vg = (ViewGroup) findViewById(R.id.viewGroup);
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new RadioGroup.LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageView.setImageResource(image[i]);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            pageview.add(imageViews[i]);
            vp.addView(imageViews[i]);
        }

        vp.setAdapter(mViewAdapter);
        vp.setOnPageChangeListener(new GuidePageChangeListener());

    }


    PagerAdapter mViewAdapter = new PagerAdapter() {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(pageview.get(position));
            return pageview.get(position);
        }


        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(pageview.get(position));
        }

        @Override
        public int getCount() {
            return pageview.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        //如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }

        }
    }
}
