package deneb.eyeroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StockSearch extends Activity {
	
	private String searchSql = "select code as _id, stk_name as name, model, ea from stk;";
	private String autoSql = "select code as _id, stk_name as name, model, ea from stk"+
								"where name like '%#k%' or model like '%#k%';";
	
	private EditText etSearch = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvSearch = null;
	private DBManager dbm = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stk_sch);
		
		if(getIntent().getBooleanExtra("less", false)){
			searchSql = "select code as _id, stk_name as name, model, ea from stk where ea <= alert_ea;";
			autoSql = "select code as _id, stk_name as name, model, ea from stk"+
				"where (name like '%#k%' or model like '%#k%') and (ea <= alert_ea);";
			super.setTitle(
					getResources().getText(R.string.app_name) + " - " + 
					getResources().getText(R.string.less_stock)
			);
		}
		else{
			super.setTitle(
					getResources().getText(R.string.app_name) + " - " + 
					getResources().getText(R.string.srv_main_stk_sch)
			);
		}
		
		init();
	}
	
	private void init(){
		//findViewById(R.id.stkSch_btnAdd).setOnClickListener(btnAdd_OnClick);
		
		dbm = new DBManager(this);
		Cursor c = dbm.search(searchSql);
		String[] from = {"_id", "name", "model", "ea"};
		int[] to = {R.id.stkSch_tvCode, R.id.stkSch_tvName, R.id.stkSch_tvModel, R.id.stkSch_tvEa};
		adapter = new SimpleCursorAdapter(this, R.layout.stk_sch_entry, c, from, to);
	
		lvSearch = (ListView)this.findViewById(R.id.stkSch_lvList);
		lvSearch.setAdapter(adapter);
		lvSearch.setTextFilterEnabled(true);
		etSearch = (EditText)this.findViewById(R.id.stkSch_etSearch);
	
		etSearch.addTextChangedListener(onAutoComplete);
		lvSearch.setOnItemClickListener(onListItemClick);
		dbm.close();
	}
	
//	protected OnClickListener btnAdd_OnClick = new OnClickListener(){
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(StockSearch.this, StockRegist.class);
//			//intent.putExtra("key", value)
//			startActivity(intent);
//		}
//	};
	
	private TextWatcher onAutoComplete = new TextWatcher(){
		public void afterTextChanged(Editable arg0) {}
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			DBManager dbmr = new DBManager(StockSearch.this);
			Cursor cr = dbmr.search(autoSql.replace("#k", s));
			if(cr.getCount() != 0)
				adapter.changeCursor(cr);
			
			//cr.close();
			dbmr.close();
		}
	};
	private OnItemClickListener onListItemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			Intent intent = new Intent(StockSearch.this, StockInfo.class);
			
			intent.putExtra("key", ((TextView)arg1.findViewById(R.id.stkSch_tvCode)).getText().toString());
			startActivity(intent);
			
			//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
		}
	};
}
