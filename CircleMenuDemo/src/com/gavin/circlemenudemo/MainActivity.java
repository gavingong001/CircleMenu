package com.gavin.circlemenudemo;


import java.util.ArrayList;

import com.gavin.circlemenudemo.CircleMenuView.SelectListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements SelectListener {

	
	private CircleMenuView mCircleMenuView;// 圆形菜单
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		mCircleMenuView = (CircleMenuView) findViewById(R.id.circlemenuview);

		initCircleMenuView();
	}


	/**
	 * 初始化圆形菜单
	 */
	@SuppressLint("NewApi")
	private void initCircleMenuView() {
		ArrayList<Integer> imgs = new ArrayList<Integer>();
		ArrayList<Integer> imgs_fouse = new ArrayList<Integer>();

		Integer bg_circle = R.drawable.bg_inner;
		// Integer bg_circle = null;
		Integer bg_inner = null;
		Integer center = R.drawable.circle_all_item;

		imgs.add(R.drawable.home1_shzx);
		imgs.add(R.drawable.home2_bdxw);
		imgs.add(R.drawable.home3_bsdt);
		imgs.add(R.drawable.home4_ggfw);

		imgs_fouse.add(R.drawable.home1_shzx_active);
		imgs_fouse.add(R.drawable.home2_bdxw_active);
		imgs_fouse.add(R.drawable.home3_bsdt_active);
		imgs_fouse.add(R.drawable.home4_ggfw_active);

		mCircleMenuView.setItemsSrcs(imgs, imgs_fouse, bg_circle, bg_inner,
				center, (int) mCircleMenuView.getX());
		mCircleMenuView.setOnSelectListener(this);

	}


	@Override
	public void onSelect(int chooseItem) {

		switch (chooseItem) {
		case -1:// 中间
			Toast.makeText(getApplicationContext(), "中间", Toast.LENGTH_SHORT).show();
			break;
		case 0:
			Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	
		
	}
	

}
