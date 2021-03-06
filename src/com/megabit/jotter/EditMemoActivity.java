package com.megabit.jotter;

import com.megabit.jotter.type.MemoItem;
import com.megabit.jotter.utils.Abb;
import com.megabit.jotter.utils.MemoDB;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMemoActivity extends Activity {

	MemoDB memoDB;
	int memoID;
	
	EditText edt_Title;
	EditText edt_Content;
	Button btn_Edit;
	
	Toast alertToast = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		setContentView(R.layout.activity_addmemo);
		
		memoDB = new MemoDB(this);
		memoID = getIntent().getIntExtra("R.string.TAG_ID", -1);
		
		edt_Title = (EditText) findViewById(R.id.edt_Title);
		edt_Content = (EditText) findViewById(R.id.edt_Content);
		btn_Edit = (Button) findViewById(R.id.btn_Add);
		btn_Edit.setText("Edit");
		
		MemoItem item = memoDB.readMemo(memoID);
		edt_Title.setText(item.Title);
		edt_Content.setText(item.Content);
		
		btn_Edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String title = edt_Title.getText().toString();
				String content = edt_Content.getText().toString();
				
				if(title.length()==0 && content.length()==0){
					if(alertToast==null){
						alertToast = Abb.shortToast(EditMemoActivity.this, getResources().getString(R.string.Toast_Add_Alert));
					}
					alertToast.show();
					return;
				}else{
					memoDB.editMemo(memoID, title, content);
					// Intent add_complete_intent = new Intent();
					// TODO add_complete_intent.putExtra(R.string.TAG_ID, value);
					// setResult(Activity.RESULT_OK, add_complete_intent);
					setResult(Activity.RESULT_OK);
					finish();
				}
				
			}
		});
		
	}
	
}
