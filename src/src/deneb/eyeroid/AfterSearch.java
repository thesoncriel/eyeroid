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

public class AfterSearch extends Activity {
	private EditText etSearch = null;
	private ImageButton ibDate = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvSearch = null;
	private DBManager dbm = null;
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_sch);
		
		key = this.getIntent().getStringExtra("cusPhon");
		
		dbm = new DBManager(this);
		Cursor c = dbm.search("select acc_date as _id, acc_cont as cont, fixprice as price, pro_date as date from aft where cus_phon like '" + key + "' order by date desc");
		String[] from = {"_id", "cont", "price", "date"};
		int[] to = {R.id.aftSch_tvAccDate, R.id.aftSch_tvAccCont, R.id.aftSch_tvFixPrice, R.id.aftSch_tvProDate};
		adapter = new SimpleCursorAdapter(this, R.layout.aft_sch_entry, c, from, to);
	
		lvSearch = (ListView)this.findViewById(R.id.couSch_list);
		lvSearch.setAdapter(adapter);
	
		lvSearch.setTextFilterEnabled(true);
	
		etSearch = (EditText)this.findViewById(R.id.couSch_etSearch);
		ibDate = (ImageButton)findViewById(R.id.couSch_ibDate);
		
		dbm.close();
		etSearch.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable arg0) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				DBManager dbmr = new DBManager(AfterSearch.this);
				Cursor cr = dbmr.search(
						"select acc_date as _id, acc_cont as cont, fixprice as price, pro_date as date from aft " + 
						"where cus_phon like '" + key + "' and (cont like '%" + s.toString() + "%' or date like '%" + s.toString() + "%' or cont like '%" + s.toString() + "%') " + 
						"order by date desc;"
					);
				if(cr.getCount() != 0)
					adapter.changeCursor(cr);
				
				//cr.close();
				dbmr.close();
			}
		});
		ibDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				DateTimePicker dtp = new DateTimePicker(AfterSearch.this, etSearch, DateTimePicker.DATE_ONLY);
			}
		});
		lvSearch.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent(AfterSearch.this, AfterInfo.class);
				
				intent.putExtra("key", ((TextView)arg1.findViewById(R.id.aftSch_tvAccDate)).getText().toString());
				startActivity(intent);
				
				//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
			}
		});
	}
}
