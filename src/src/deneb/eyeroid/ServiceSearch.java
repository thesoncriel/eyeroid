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

public class ServiceSearch extends Activity {
	
	private final String searchSql = "select count(sal.srv_name) as cnt, srv.srv_name as _id, srv.price as price "+
									"from srv "+
									"left join sal "+
									"on srv.srv_name = sal.srv_name "+
									"group by _id "+
									"order by cnt desc";
	private final String autoSql = "select count(sal.srv_name) as cnt, srv.srv_name as _id, srv.price as price "+
									"from srv "+
									"left join sal "+
									"on srv.srv_name = sal.srv_name "+
									"group by _id "+
									"having _id like '%#k%' "+
									"order by cnt desc";
	
	private EditText etSearch = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvSearch = null;
	private DBManager dbm = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.srv_sch);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.srv_main_sch)
		);
		
		init();
		
	}
	
	private void init(){
		dbm = new DBManager(this);
		Cursor c = dbm.search(searchSql);
		String[] from = {"cnt", "_id", "price"};
		int[] to = {R.id.srvSch_tvCnt, R.id.srvSch_tvName, R.id.srvSch_tvPrice};
		adapter = new SimpleCursorAdapter(this, R.layout.srv_sch_entry, c, from, to);
	
		lvSearch = (ListView)this.findViewById(R.id.srvSch_lvList);
		lvSearch.setAdapter(adapter);
		lvSearch.setTextFilterEnabled(true);
		etSearch = (EditText)this.findViewById(R.id.srvSch_etSearch);
	
		etSearch.addTextChangedListener(onAutoComplete);
		lvSearch.setOnItemClickListener(onListItemClick);
		dbm.close();
	}
	
	private TextWatcher onAutoComplete = new TextWatcher(){
		public void afterTextChanged(Editable arg0) {}
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			DBManager dbmr = new DBManager(ServiceSearch.this);
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
			
			Intent intent = new Intent(ServiceSearch.this, ServiceInfo.class);
			
			intent.putExtra("key", ((TextView)arg1.findViewById(R.id.srvSch_tvName)).getText().toString());
			startActivity(intent);
			
			//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
		}
	};
}
