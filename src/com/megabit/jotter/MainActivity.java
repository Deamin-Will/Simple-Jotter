package com.megabit.jotter;

import com.megabit.jotter.utils.Abb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static final int REQUESTCODE_ADD = 110;
	public static final int REQUESTCODE_EDIT = 120;
	MemoBaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Abb.hideTitleBar(MainActivity.this);
		requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		setContentView(R.layout.activity_main);
		
		ListView listview = (ListView) findViewById(R.id.listview);
		adapter = new MemoBaseAdapter(this);
		listview.setAdapter(adapter);
		
		Button btn_Add = (Button) findViewById(R.id.btn_Add);
		btn_Add.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				// Abb.shortToast(MainActivity.this, "Add 버튼을 눌렀습니다.").show();
				Intent addMemo = new Intent(MainActivity.this, AddMemoActivity.class);
				startActivityForResult(addMemo, REQUESTCODE_ADD);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("로그", "onActivityResult 호출");
		if(requestCode == REQUESTCODE_ADD && resultCode == Activity.RESULT_OK){
			adapter.refresh();
		}
		
		if(requestCode == REQUESTCODE_EDIT && resultCode == Activity.RESULT_OK) {
			adapter.refresh();
		}
		
	}
	
}
