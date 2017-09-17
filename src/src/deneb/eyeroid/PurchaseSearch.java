package deneb.eyeroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PurchaseSearch extends Activity {
	private final String searchSql = "select sal_date as _id, srv_name as name, price from sal where cus_phon like '%#p%' order by _id desc";
	
	private EditText etSearch = null;
	private ImageButton ibDate = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvSearch = null;
	private DBManager dbm = null;
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_sch);

		init();
	}
	private void init(){
		key = this.getIntent().getStringExtra("cusPhon");
		
		dbm = new DBManager(this);
		Cursor c = dbm.search(searchSql.replace("#p", key));
		String[] from = {"_id", "name", "price"};
		int[] to = {R.id.salSch_tvDate, R.id.salSch_tvName, R.id.salSch_tvPrice};
		adapter = new SimpleCursorAdapter(this, R.layout.sal_sch_entry, c, from, to);
	
		lvSearch = (ListView)this.findViewById(R.id.couSch_list);
		lvSearch.setAdapter(adapter);
	
		lvSearch.setTextFilterEnabled(true);
	
		etSearch = (EditText)this.findViewById(R.id.couSch_etSearch);
		ibDate = (ImageButton)findViewById(R.id.couSch_ibDate);
		dbm.close();
		
		//msg.show(this, searchSql.replace("#p", key) + c.getCount() + "°³ Çà");
		
		etSearch.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable arg0) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				DBManager dbmr = new DBManager(PurchaseSearch.this);
				Cursor cr = dbmr.search(
						"select sal_date as _id, srv_name as name, price from sal where " + 
						"(_id like '%" + s + "%' or name like '%" + s + "%') and cus_phon = '" + key + "' order by _id desc;"
					);
				if(cr.getCount() != 0)
					adapter.changeCursor(cr);
				
				//cr.close();
				dbmr.close();
			}
		});
		ibDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				DateTimePicker dtp = new DateTimePicker(PurchaseSearch.this, etSearch, DateTimePicker.DATE_ONLY);
			}
		});
		lvSearch.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent(PurchaseSearch.this, SalesInfo.class);
				
				intent.putExtra("key", ((TextView)arg1.findViewById(R.id.salSch_tvDate)).getText().toString());
				startActivity(intent);
				
				//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
			}
		});
	}
}
