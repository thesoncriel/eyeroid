package deneb.eyeroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class msg {
	private static boolean close = false;
	private static Activity a = null;
	public msg(Context act, String msg, String btn1, String btn2, boolean close, DialogInterface.OnClickListener event){
		this.close = close;
		a = (Activity)act;
		if(btn2 == null){
			new AlertDialog.Builder(act)
			.setMessage(msg)
			.setNegativeButton(btn1, event)
			.show();
		}
		else{
			new AlertDialog.Builder(act)
			.setMessage(msg)
			.setPositiveButton(btn1, event)
			.setNegativeButton(btn2, this.event)
			.show();
		}
	}
	public msg(Context act, String msg){
		this(act, msg, "확인", null, false, event);
	}
	public static msg show(Activity act, String msg, String btn1, String btn2, boolean close, DialogInterface.OnClickListener event){
		return new msg(act, msg, btn1, btn2, close, event);
	}
//	public static msg show(Context act, String msg, String btn1, String btn2, DialogInterface.OnClickListener event){
//		return new msg(act, msg, btn1, btn2, false, event);
//	}
	public static msg show(Context act, String msg, boolean close){
		return new msg(act, msg, "확인", null, close, event);
	}
	public static msg show(Context act, String msg){
		return new msg(act, msg);
	}
	private static DialogInterface.OnClickListener event = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if(close) a.finish();
		}
	};
}
