package deneb.eyeroid;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


public class PopupSubMenu {
	
//	public static class Builder{
//		private static PopupSubMenu psm;
//
//		public Builder(Activity parent){
//			psm = new PopupSubMenu(parent);
//		}
//		public Builder setIcon(int iconId){
//			psm.setIcon(iconId);
//			return this;
//		}
//		public Builder setTitle(int titleId){
//			psm.setTitle(titleId);
//			return this;
//		}
//		public Builder addSubMenu(int menuStrId, Class activity){
//			psm.addSubMenu(menuStrId, activity);
//			return this;
//		}
//		public Builder enableToday(boolean enable){
//			psm.enableToday(enable);
//			return this;
//		}
//		public PopupSubMenu show(){
//			return psm.show();
//		}
//	}
	
	
	private int showCamera;
	private boolean enableToday;
	private List<String> menuStr;
	private List<Class> acts;
	private Activity pAct;
	private String title;
	private int icon;
	
	public PopupSubMenu(Activity parent){
		pAct = parent;
		enableToday = true;
		showCamera = 0;
		menuStr = new ArrayList<String>();
		acts = new ArrayList<Class>();
	}
	
	protected PopupSubMenu show(){
		AlertDialog ad = new AlertDialog.Builder(pAct)
		.setIcon(icon)
		.setTitle(title)
		.setSingleChoiceItems(menuStr.toArray(new String[menuStr.size()]), -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int re = 1 << which;
				
				Intent intent = new Intent(pAct, acts.get(which));
				intent.putExtra("setToday", enableToday);
				intent.putExtra("showCamera", (re & showCamera) == re);
				pAct.startActivity(intent);
//				new msg(pAct, "re = " + Integer.toBinaryString(re) + "\nshowCamera = " + Integer.toBinaryString(showCamera) + 
//						"\nAnd = " + (re & showCamera) );
				dialog.dismiss();
			}
		})
		
		.setPositiveButton("Ãë¼Ò", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
			}
		})
		.show();
		return this;
	}
	
	public PopupSubMenu setIcon(int iconId){
		icon = iconId;
		return this;
	}
	public PopupSubMenu setTitle(int titleId){
		title = pAct.getResources().getString(titleId);
		return this;
	}
	public PopupSubMenu addSubMenu(int menuStrId, Class activity){
		menuStr.add(pAct.getResources().getString(menuStrId));
		acts.add(activity);
		return this;
	}
	public PopupSubMenu enableToday(boolean enable){
		enableToday = enable;
		return this;
	}
	public PopupSubMenu showCamera(int target){
		showCamera = target;
		return this;
	}
}

