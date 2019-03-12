package com.megabit.jotter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.Toast;

public class Abb {
	
	
	static public void println(String msg, Object... args){
		System.out.println(String.format(msg, args.toString()));
	}
	
	static public void hideTitleBar(Activity act){
		act.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	static SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	static public String getDate_yyyMMdd(){
		return sdf_yyyyMMdd.format(new Date());
	}
	
	static public String getDate_yyyyMMdd(Date date){
		return sdf_yyyyMMdd.format(date);
	}

	static SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	static public String getDate_yyyyMMddHHmmss(){
		return sdf_yyyyMMddHHmmss.format(new Date());
	}
	
	static public Toast shortToast(Context context, String msg){
		return Toast.makeText(context, msg, Toast.LENGTH_SHORT);
	}
	
	static public Toast longToast(Context context, String msg){
		return Toast.makeText(context, msg, Toast.LENGTH_LONG);
	}
	
}
