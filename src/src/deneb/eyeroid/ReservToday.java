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

public class ReservToday extends Activity {
	private EditText etSearch = null;
	private ImageButton ibDate = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvSearch = null;
	private DBManager dbm = null;
	private String key = null;
	private String today = null;
	private String fdt = null;
	//
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_sch);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.reserv_today)
		);
		
		key = this.getIntent().getStringExtra("cusPhon");
		
		dbm = new DBManager(this);
		DateTimeManager dtm = new DateTimeManager();
		dtm.setToday();
		dtm.setNow();
		today = dtm.getToday();
		fdt = dtm.getFullDateTime();
		Cursor c = dbm.search("select acc_date as _id, cus_phon as phon, cus_name as name, " + 
				"cus_cont as cont, res_date as date from cou " +
				"where date like '" + today + "%' order by date desc;"
				);
		String[] from = {"_id", "phon", "name", "cont", "date"};
		int[] to = {R.id.resToday_tvAccDate, R.id.resToday_tvPhon, R.id.resToday_tvName, R.id.resToday_tvCont, R.id.resToday_tvDate};
		adapter = new SimpleCursorAdapter(this, R.layout.reserv_today_entry, c, from, to);
	
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
				DBManager dbmr = new DBManager(ReservToday.this);
				Cursor cr = dbmr.search(
						"select acc_date as _id, cus_phon as phon, cus_name as name, " + 
						"cus_cont as cont, res_date as date from cou " +
						"where (date like '" + today + "%') and (name like '%" + s.toString() + 
						"%' or phon like '%" + s.toString() + "%' or cont like '%" + 
						s.toString() + "%') order by date desc;"
					);
				if(cr.getCount() != 0)
					adapter.changeCursor(cr);
				
				//cr.close();
				dbmr.close();
			}
		});
		ibDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				DateTimePicker dtp = new DateTimePicker(ReservToday.this, etSearch, DateTimePicker.DATE_ONLY);
			}
		});
		lvSearch.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent(ReservToday.this, CounselInfo.class);
				
				intent.putExtra("key", ((TextView)arg1.findViewById(R.id.resToday_tvAccDate)).getText().toString());
				startActivity(intent);
				
				//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
			}
		});
		/*
		TextView tvDate = (TextView)findViewById(R.id.resToday_tvDate);
		tvDate.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				String[] str = s.toString().split(" ");
				s.clear();
				s.append(str[1]);
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
			
		});
		*/
	}
}
