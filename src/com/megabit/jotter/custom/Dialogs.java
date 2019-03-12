package com.megabit.jotter.custom;

import java.lang.annotation.Target;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.megabit.jotter.EditMemoActivity;
import com.megabit.jotter.MainActivity;
import com.megabit.jotter.MemoBaseAdapter;
import com.megabit.jotter.R;
import com.megabit.jotter.type.MemoItem;
import com.megabit.jotter.utils.MemoDB;
import com.megabit.jotter.utils.Abb;

public class Dialogs {
	
	static public void optionDialog(final Activity activity, final MemoItem item, final MemoBaseAdapter adapter){
		final Dialog dialog = createDialog(activity, R.layout.dialog_listitem_longclick);;
		
		final Button btn_Edit = (Button) dialog.findViewById(R.id.btn_Edit);
		final Button btn_Remove = (Button) dialog.findViewById(R.id.btn_Remove);
		final Button btn_Clipboard = (Button) dialog.findViewById(R.id.btn_Clipboard);
		final Button btn_Close = (Button) dialog.findViewById(R.id.btn_Close);
		
		btn_Remove.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View view){
				new MemoDB(activity).removeMomo(item.ID);
				adapter.refresh();
				dialog.dismiss();
				return false;
			}
		});
		
		View.OnClickListener optionListener = new View.OnClickListener() {
			
			int count = 0;
			Toast deleteBeforeToast = null;
			@TargetApi(11)
			@Override
			public void onClick(View v){
				switch(v.getId()){
				case R.id.btn_Edit:
					Intent editMemo = new Intent(activity, EditMemoActivity.class);
					editMemo.putExtra("R.string.TAG_ID", item.ID);
					activity.startActivityForResult(editMemo, MainActivity.REQUESTCODE_EDIT);
					break;
				case R.id.btn_Remove:
					if(deleteBeforeToast==null && count <3){
						deleteBeforeToast = Abb.shortToast(activity, activity.getResources().getString(R.string.Toast_Delete_Before));
					}
					deleteBeforeToast.show();
					return;
				case R.id.btn_Clipboard:
//					ClipboardManager clipManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
//					ClipData clip = ClipData.newPlainText(item.Title, item.Content);
					String text = String.format("Title: %s\r\nMemo: %s", item.Title, item.Content);
					
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB){
						android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(text);
					}else{
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData.newPlainText("Simple Memo", text);
						clipboard.setPrimaryClip(clip);
					}
					
					
					Abb.shortToast(activity, activity.getResources().getString(R.string.Toast_Clipboard_Copy)).show();
					break;
				case R.id.btn_Close:
					break;
				}
				dialog.dismiss();
			}
		};
		
		btn_Edit.setOnClickListener(optionListener);
		btn_Remove.setOnClickListener(optionListener);
		btn_Clipboard.setOnClickListener(optionListener);
		btn_Close.setOnClickListener(optionListener);
		
		dialog.show();
	}
	
	static private Dialog createDialog(Activity activity, int resourceId){
		Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(resourceId);
		
		return dialog;
	}
	
	
}
