package com.example.wangdan.lvbao;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.myCoord)
    CoordinatorLayout mycoord;

    private BottomBar mBottomBar;
    private List<Fragment> fragmentList;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPager();
        createBottomBar(savedInstanceState);
    }
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentOne());
        fragmentList.add(new FragmentTwo());
        fragmentList.add(new FragmentThree());
        fragmentList.add(new FragmentFoue());

        viewPager.setOffscreenPageLimit(4);//重点来了，加了这一行之后跳转页面再返回后还是保持原来的状态
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomBar.selectTabAtPosition(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
       private void createBottomBar(Bundle savedInstanceState)
    {
        mBottomBar = BottomBar.attachShy(mycoord,viewPager,savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(int menuItemId) {
                switch (menuItemId) {
                    case R.id.nearby:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.scenery:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.share:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.friends:
                        viewPager.setCurrentItem(3);
                        break;

                }
            }

            @Override
    public void onMenuTabReSelected(int menuItemId) {

    }
});
        mBottomBar.mapColorForTab(0, "#6699FF");
        mBottomBar.mapColorForTab(1, "#9966cc");
        mBottomBar.mapColorForTab(2, "#cc99cc");
        mBottomBar.mapColorForTab(3, "#00CC00");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.exit(0);
    }

    @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {

    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
        if((System.currentTimeMillis()-exitTime) > 2000){
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
        return false;
    }
    return super.onKeyDown(keyCode, event);
}
    }
