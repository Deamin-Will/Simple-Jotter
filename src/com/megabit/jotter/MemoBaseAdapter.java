package com.megabit.jotter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import com.megabit.jotter.utils.MemoDB;
import com.megabit.jotter.custom.Dialogs;
import com.megabit.jotter.type.MemoItem;
import java.util.ArrayList;

public class MemoBaseAdapter extends BaseAdapter {

	Context context;
	MemoDB memoDB;
	ArrayList<MemoItem> data;
	
	public MemoBaseAdapter(Context context){
		this.context = context;
		memoDB = new MemoDB(context);
		data = memoDB.readMemos();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
		}
		
		TextView tv_Title = (TextView) convertView.findViewById(R.id.tv_Title);
		TextView tv_Glance = (TextView) convertView.findViewById(R.id.tv_Glance);
		Button btn_Edit = (Button) convertView.findViewById(R.id.btn_Edit);
		
		MemoItem item = (MemoItem) getItem(position);
		tv_Title.setText(item.Title);
		tv_Glance.setText(item.Content);
		btn_Edit.setTag(R.dimen.TAG_POSITION, position);
		convertView.setTag(R.dimen.TAG_POSITION, position);
		btn_Edit.setOnClickListener(itemListener);
		
		
		
		convertView.setOnLongClickListener(optionListener);
		
		return convertView;
	}
	
	public void refresh(){
		this.data = memoDB.readMemos();
		notifyDataSetChanged();
	}
	
	private View.OnClickListener itemListener = new View.OnClickListener() {
		@Override
		public void onClick(View v){
			int position = (Integer) v.getTag(R.dimen.TAG_POSITION);
			MemoItem item = (MemoItem) getItem(position);
			
			Activity activity = (Activity) context;
			Intent editMemo = new Intent(activity, EditMemoActivity.class);
			editMemo.putExtra("R.string.TAG_ID", item.ID);
			activity.startActivityForResult(editMemo, MainActivity.REQUESTCODE_EDIT);
		}
	};
	
	private View.OnLongClickListener optionListener = new View.OnLongClickListener(){
		@Override
		public boolean onLongClick(View view){
			int position = (Integer) view.getTag(R.dimen.TAG_POSITION);
			MemoItem item = (MemoItem) getItem(position);
			Dialogs.optionDialog((Activity)context, item, MemoBaseAdapter.this);
			return false;
		}
	};
	

}
