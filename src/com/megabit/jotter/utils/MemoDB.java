package com.megabit.jotter.utils;

import java.util.ArrayList;
import java.util.Date;

import com.megabit.jotter.type.MemoItem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDB {
	
	static private DBHelper MemoDb = null;
	public Boolean logcat = false;
	
	public MemoDB(Context context){
		if(MemoDb==null){
			MemoDb = new DBHelper(context);
		}
	}
	
	public void addMemo(String title, String content){
		MemoDb.addMemo(title, content);
	}
	
	public void editMemo(int id, String title, String content){
		MemoDb.editMemo(id, title, content);
	}
	
	public void removeMomo(int id){
		MemoDb.removeMemo(id);
	}
	
	public MemoItem readMemo(int id){
		return MemoDb.readMemo(id);
	}
	
	public ArrayList<MemoItem> readMemos(){
		return MemoDb.readMemos();
	}
	
	public ArrayList<MemoItem> readMemos(int top){
		return MemoDb.readMemos(top);
	}
	
	/**
	 * 수정된 메모 날짜 기준으로 삭제해야 하므로 사용금지.
	 * */
	@Deprecated
	public void discardMemo(Date before){
		if(false){
			MemoDb.discardMemo(before);
		}else{
			
		}
	}
	
	public void discardMemo(int left){
		MemoDb.discardMemo(left);
	}
	
	public void print(int top){
		ArrayList<MemoItem> arrList = MemoDb.readMemos(top);
		for(MemoItem item : arrList){
			Log.d("로그", String.format("[%s] (%s) %s", item.ID, item.Date, item.Content));
		}
	}
	
//	static public Logger getInstance(Context context){
//		if(logger==null){
//			logger = new Logger(context.getApplicationContext());
//		}
//		return logger;
//	}
//	
//	static public Logger getInstance() throws NullPointerException{
//		if(logger==null){
//			throw new NullPointerException("You don't use context. We cannot provide instance.");
//		}
//		return logger;
//	}
	
	@Override
	protected void finalize() throws Throwable{
		MemoDb.close();
	}
	
	/****************************************************/
	
	class DBHelper extends SQLiteOpenHelper{
		
		private Context context;
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "MemoDB";
		
		private static final String TABLE_NAME = "Memos";
		private static final String KEY_ID = "_id";
		private static final String KEY_CONTENT = "content";
		private static final String KEY_TITLE = "title";
		private static final String KEY_Date = "date";
		
		public DBHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {

			// Create table if not exist table!
			String createTable = "CREATE TABLE IF NOT EXISTS Memos (_id INTEGER PRIMARY KEY AUTOINCREMENT, content text, title text, date text) ";
			db.execSQL(createTable);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			// method 1 : Drop older table if existed.
			// TODO LATER
			
			// method 2 : Rename older table
			String renameName = String.format("Memos_%s", Abb.getDate_yyyyMMddHHmmss());
			String renameTable = "ALTER TABLE Memos RENAME to " + renameName;
			db.execSQL(renameTable);
			
		}
		
		public void addMemo(String title, String content){
			SQLiteDatabase db = getWritableDatabase();

			String sql_WriteLog = "INSERT INTO Memos (title, content, date) Values( \"{title}\", \"{content}\", \"{date}\" ) ";
			sql_WriteLog = sql_WriteLog.replace("{title}", title);
			sql_WriteLog = sql_WriteLog.replace("{content}", content);
			sql_WriteLog = sql_WriteLog.replace("{date}", Abb.getDate_yyyMMdd());
			db.execSQL(sql_WriteLog);
			
		}
		
		public MemoItem readMemo(int id) throws NullPointerException {
			SQLiteDatabase db = getReadableDatabase();
			String sql_Select = "SELECT * FROM Memos WHERE _id = " + id;
			Cursor c = db.rawQuery(sql_Select, null);
			if(!c.moveToFirst()){
				throw new NullPointerException(id+ "-memo is not found.");
			}else{
				MemoItem item = new MemoItem(c.getInt(c.getColumnIndex(KEY_ID)));
				item.Title = c.getString(c.getColumnIndex(KEY_TITLE));
				item.Content = c.getString(c.getColumnIndex(KEY_CONTENT));
				return item;
			}
		}
		
		public ArrayList<MemoItem> readMemos(int top){
			
			// Get Data From DB
			SQLiteDatabase db = getReadableDatabase();
			String sql_ReadLog = "SELECT * FROM Memos ORDER BY _id desc LIMIT {top} ";
			sql_ReadLog = sql_ReadLog.replace("{top}", top+"");
			
			// Get Data From Cursor
			Cursor c = db.rawQuery(sql_ReadLog, null);
			ArrayList<MemoItem> arrList = new ArrayList<MemoItem>();
			if(!c.moveToFirst()){
				return arrList;
			}else{
				// Save To...
				do{
					MemoItem item = new MemoItem(c.getInt(c.getColumnIndex(KEY_ID)));
					item.Content = c.getString(c.getColumnIndex(KEY_CONTENT));
					item.Title = c.getString(c.getColumnIndex(KEY_TITLE));
					item.Date = c.getString(c.getColumnIndex(KEY_Date));
					arrList.add(item);
				}while(c.moveToNext());
			}
			return arrList;
		}
		
		public ArrayList<MemoItem> readMemos(){
			
			// Get Data From DB
			SQLiteDatabase db = getReadableDatabase();
			String sql_ReadLog = "SELECT * FROM Memos ORDER BY _id desc ";
			
			// Get Data From Cursor
			Cursor c = db.rawQuery(sql_ReadLog, null);
			ArrayList<MemoItem> arrList = new ArrayList<MemoItem>();
			if(!c.moveToFirst()){
				return arrList;
			}else{
				// Save To...
				do{
					MemoItem item = new MemoItem(c.getInt(c.getColumnIndex(KEY_ID)));
					item.Content = c.getString(c.getColumnIndex(KEY_CONTENT));
					item.Title = c.getString(c.getColumnIndex(KEY_TITLE));
					item.Date = c.getString(c.getColumnIndex(KEY_Date));
					arrList.add(item);
				}while(c.moveToNext());
			}
			return arrList;
		}
		
		public void editMemo(int id, String title, String content){
			SQLiteDatabase db = getWritableDatabase();
			String sql_UpdateMemo = "UPDATE Memos SET title = \"{title}\", content = \"{content}\" WHERE _id = {id}";
			sql_UpdateMemo = sql_UpdateMemo.replace("{title}", title);
			sql_UpdateMemo = sql_UpdateMemo.replace("{content}", content);
			sql_UpdateMemo = sql_UpdateMemo.replace("{id}", id+"");
			db.execSQL(sql_UpdateMemo);
		}
		
		public void removeMemo(int id){
			SQLiteDatabase db = getWritableDatabase();
			String sql_DeleteMemo = " DELETE FROM Memos WHERE _id = " + id ;
			db.execSQL(sql_DeleteMemo);
		}
		
		public void discardMemo(Date before){
			SQLiteDatabase db = getWritableDatabase();
			String sql_DeleteLog = " DELETE FROM Memos WHERE date <= '{before}' ; " ;
			sql_DeleteLog = sql_DeleteLog.replace("{before}", Abb.getDate_yyyyMMdd(before));
			db.execSQL(sql_DeleteLog);
		}
		
		public void discardMemo(int left){
			SQLiteDatabase db = getWritableDatabase();
			String sql_DeleteLog = " DELETE FROM Memos WHERE _id NOT IN (SELECT _id FROM Memos ORDER BY date desc LIMIT {left}) ; ";
			sql_DeleteLog = sql_DeleteLog.replace("{left}", left+"");
			db.execSQL(sql_DeleteLog);
		}	
	}
}
