package com.example.wangdan.lvbao;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.wangdan.lvbao.R;


public class WelcomeActivity extends Activity  {
	private ImageView  imageView = null;
	private Animation alphaAnimation = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		imageView = (ImageView)findViewById(R.id.welcome_image_view);
		alphaAnimation = AnimationUtils.loadAnimation(this, R.animator.welcome_alpha);
		alphaAnimation.setFillEnabled(true); //����Fill����
		alphaAnimation.setFillAfter(true);  //���ö��������һ֡�Ǳ�����View����
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
							
								Intent k = new Intent();
								k.setClass(WelcomeActivity.this,MainActivity.class);
								startActivity(k);
							}
						});
						
					}
			
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}
	
}
