package deneb.eyeroid;

import android.app.Activity;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;

public class AutoCompleteManager {
	
	private DBManager dbm = null;
	private Cursor c = null;
	private AutoCompleteTextView actv = null;
	//private SimpleCursorAdapter adapter = null;
	private ArrayAdapter adapter = null;
	private Activity act = null;
	private int[] view = null;
	
	public AutoCompleteManager(){
		//view = new int[]{R.id.auto_tv0, R.id.auto_tv1, R.id.auto_tv2, R.id.auto_tv3};
	}
	public AutoCompleteManager(Activity act, AutoCompleteTextView actv, String table, String field){
		//view = new int[]{R.id.auto_tv0, R.id.auto_tv1, R.id.auto_tv2, R.id.auto_tv3};
		setActivity(act);
		setTextView(actv);
		setSql(table, field);
	}
	public AutoCompleteManager setActivity(Activity act){
		this.act = act;
		dbm = new DBManager(act);
		return this;
	}
	public AutoCompleteManager setTextView(AutoCompleteTextView actv){
		this.actv = actv;
		return this;
	}
	public AutoCompleteManager setSql(String table, String field){
		return setSql("select distinct " + field + " from " + table);
	}
	public AutoCompleteManager setSql(String sql){
		c = dbm.search(sql);
		String[] data = new String[c.getCount()];
		for(int i = 0; c.moveToNext(); i++){
			data[i] = c.getString(0);
		}
		adapter = new ArrayAdapter(act, android.R.layout.simple_dropdown_item_1line, data);
		actv.setAdapter(adapter);
		dbm.close();
		return this;
	}
	public AutoCompleteManager setEvent(OnItemClickListener event){
		actv.setOnItemClickListener(event);
		return this;
	}
//	public AutoCompleteManager setSql(String table, String field){
//		return setSql("select distinct " + field + " from " + table);
//	}
//	public AutoCompleteManager setSql(String sql, String... fields){
//		c = dbm.search(sql);
//		String[] from = fields;
//		int[] to = new int[from.length];
//		for(int i = 0; i < from.length; i++){
//			to[i] = view[i];
//		}
//		adapter = new SimpleCursorAdapter(act, R.layout.auto_entry, c, from, to);
//		actv.setAdapter(adapter);
//		return this;
//	}
	private void setSqlCommon(){
		
	}
}
