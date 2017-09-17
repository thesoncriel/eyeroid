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

public class CounselSearch extends Activity {
	
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
		Cursor c = dbm.search("select cus_cont as cont, acc_date as _id, res_date as date from cou where cus_phon like '" + key + "' order by date desc, _id desc");
		String[] from = {"cont", "_id", "date"};
		int[] to = {R.id.couSch_tvCont, R.id.couSch_tvAccDate, R.id.couSch_tvResDate};
		adapter = new SimpleCursorAdapter(this, R.layout.cou_sch_entry, c, from, to);
	
		lvSearch = (ListView)this.findViewById(R.id.couSch_list);
		lvSearch.setAdapter(adapter);
	
		lvSearch.setTextFilterEnabled(true);
	
		etSearch = (EditText)this.findViewById(R.id.couSch_etSearch);
		ibDate = (ImageButton)findViewById(R.id.couSch_ibDate);
		
		//c.close();
		dbm.close();
		etSearch.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable arg0) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				DBManager dbmr = new DBManager(CounselSearch.this);
				Cursor cr = dbmr.search(
						"select cus_cont as cont, acc_date as _id, res_date as date from cou " + 
						"where cus_phon like '" + key + "' and (_id like '%" + s.toString() + "%' or date like '%" + s.toString() + "%' or cont like '%" + s.toString() + "%') " + 
						"order by _id desc;"
					);
				if(cr.getCount() != 0)
					adapter.changeCursor(cr);
				
				//cr.close();
				dbmr.close();
			}
		});
		ibDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				DateTimePicker dtp = new DateTimePicker(CounselSearch.this, etSearch, DateTimePicker.DATE_ONLY);
			}
		});
		lvSearch.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CounselSearch.this, CounselInfo.class);
				
				intent.putExtra("key", ((TextView)arg1.findViewById(R.id.couSch_tvAccDate)).getText().toString());
				startActivity(intent);
				
				//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
			}
		});
	}
}
