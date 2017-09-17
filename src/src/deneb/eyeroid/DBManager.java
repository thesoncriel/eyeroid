package deneb.eyeroid;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.widget.SimpleCursorAdapter;

public class DBManager extends SQLiteOpenHelper{

	public static String DB_FILE_NAME = "eyeroid.db";
	public static int DB_VERSION = 3;
	private Activity act = null;
	private SQLiteDatabase db = null;
	private String previousSelect = null;
	private SimpleCursorAdapter adapter = null;
	private StringBuilder sb = null;
	//private boolean autoClose;
	
	public DBManager(Context context) {
		super(context, DB_FILE_NAME, null, DB_VERSION);
		sb = new StringBuilder();
		act = (Activity)context;
		db = this.getWritableDatabase();
	}
//	public DBManager(Context context, boolean autoClose){
//		super(context, DB_FILE_NAME, null, DB_VERSION);
//		sb = new StringBuilder();
//		act = (Activity)context;
//		this.autoClose = autoClose;
//		
//		//db = this.getWritableDatabase();
//		db = act.openOrCreateDatabase(DB_FILE_NAME, Context.MODE_WORLD_WRITEABLE, null);
//	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		onCreate();
	}
	public void onCreate(){
		db.execSQL(act.getResources().getString(R.string.create_table_cus));
		db.execSQL(act.getResources().getString(R.string.create_table_aft));
		db.execSQL(act.getResources().getString(R.string.create_table_cou));
		db.execSQL(act.getResources().getString(R.string.create_table_sal));
		db.execSQL(act.getResources().getString(R.string.create_table_srv));
		db.execSQL(act.getResources().getString(R.string.create_table_stk));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	public void onDrop(){
		db.execSQL(act.getResources().getString(R.string.drop_table_cus));
		db.execSQL(act.getResources().getString(R.string.drop_table_aft));
		db.execSQL(act.getResources().getString(R.string.drop_table_cou));
		db.execSQL(act.getResources().getString(R.string.drop_table_sal));
		db.execSQL(act.getResources().getString(R.string.drop_table_srv));
		db.execSQL(act.getResources().getString(R.string.drop_table_stk));
	}
	
	
	public Cursor search(String table, String field, String value){
		previousSelect = "select * from " + table + " where " + field + " like '%" + value + "%'";
		Cursor c = db.rawQuery(previousSelect, null);
		//if(autoClose) db.close();
		return c;
	}
	public Cursor search(String sql){
		previousSelect = sql;
		//if(autoClose) db.close();
		Cursor c = db.rawQuery(sql, null); 
		return c;
	}
	public Cursor searchAll(String table){
		previousSelect = "select * from " + table;
		//if(autoClose) db.close();
		Cursor c = db.rawQuery(previousSelect, null); 
		return c;
	}
	public void insert(String table, Editable... values) throws SQLiteConstraintException{
		insert(table, (Editable[])values);
	}
	public void insert(String table, String... values) throws SQLiteConstraintException{
		sb = new StringBuilder();
		sb.append(values[0]);
		for(int i = 1; i < values.length; i++){
			sb.append(", " + values[i]);
		}
		db.execSQL("insert into " + table + " values(" + sb.toString() + ");");
	}
	public boolean update(String table, String keyField, String... values){
		if(delete(table, keyField, values[0]) == false) return false;
		insert(table, values);
		return true;
	}
	public boolean delete(String table, String field, String... values){
		sb = new StringBuilder();
		;
		try{
			for(int i = 0; i < values.length; i++){
				sb.append("delete from " + table + " where " + field + " = '" + values[i] + "';");
			}
			db.execSQL(sb.toString());
		}
		catch(Exception ex){
			new msg(act,ex.toString());
			return false;
		}
		return true;
	}
	public boolean deleteAll(String table){
		try{
			db.execSQL("delete from " + table + ";");
		}
		catch(Exception ex){
			new msg(act,ex.toString());
			return false;
		}
		return true;
	}
	public boolean execSQL(String sql){
		try{
			db.execSQL(sql);
		}catch(Exception ex){
			new msg(act,ex.toString());
			return false;
		}
		return true;
	}
	public void open(){
		db = SQLiteDatabase.openOrCreateDatabase(DB_FILE_NAME, null);
	}
	public void close(){
		db.close();
	}
	public String sm(String str){
		if(str == null || str.equals("")){
			return "null";
		}
		return "'" + str + "'";
	}
	public String nm(String str){
		if(str == null || str.equals("")){
			return "0";
		}
		return str;
	}
//	public String dm(String str){
//		return "'" + str.replace("-", "").replace(":", "") + "'";
//	}
}
