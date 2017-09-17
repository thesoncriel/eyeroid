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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SalesSearch extends Activity {
	
	private final String searchSql = "select sal_date as _id, srv_name as name, price from sal where _id like '#d%' order by _id desc";
	private final String autoSql = "select sal_date as _id, srv_name as name, price from sal where _id like '%#s%' or name like '%#s%' order by _id desc";
	
	private CheckBox chkToday = null;
	private EditText etSearch = null;
	private ImageButton ibDate = null;
	private ListView lvSearch = null;
	private TextView tvToday = null;
	private TextView tvYester = null;
	
	private DBManager dbm = null;
	private SimpleCursorAdapter adapter = null;
	
//	sal_date char(14) primary key,		/*판매 일시*/
//	srv_name varchar(30),			/*서비스명*/
//	price int,				/*판매 가격*/
//	serial varchar(20),			/*시리얼 번호*/
//	cus_phon varchar(13),			/*고객 연락처*/
//	car_type varchar(20),			/*차종*/
//	car_num varchar(20)			/*차 번호*/
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sal_sch);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.sal_main_sch)
		);
		init();
	}
	private void init(){
		dbm = new DBManager(this);
		Cursor c = dbm.search(searchSql.replace("#d", new DateTimeManager().getToday()));
		String[] from = {"_id", "name", "price"};
		int[] to = {R.id.salSch_tvDate, R.id.salSch_tvName, R.id.salSch_tvPrice};
		adapter = new SimpleCursorAdapter(this, R.layout.sal_sch_entry, c, from, to);
		
		chkToday = (CheckBox)findViewById(R.id.salSch_chkToday);
		lvSearch = (ListView)findViewById(R.id.salSch_lvSearch);
		etSearch = (EditText)findViewById(R.id.salSch_etSearch);
		ibDate = (ImageButton)findViewById(R.id.salSch_ibDate);
		tvToday = (TextView)findViewById(R.id.salSch_tvTodayValue);
		tvYester = (TextView)findViewById(R.id.salSch_tvYesterValue);
		
		lvSearch.setAdapter(adapter);
		lvSearch.setTextFilterEnabled(true);
		
		etSearch.addTextChangedListener(onAutoCompletion);
		lvSearch.setOnItemClickListener(lvSearch_OnItemClick);
		ibDate.setOnClickListener(ibDate_OnClick);
		
		chkToday.setOnCheckedChangeListener(chkToday_OnCheckedChange);
		calculateSales();
		//msg.show(this, searchSql.replace("#d", new DateTimeManager().getToday()));
	}
	protected void calculateSales(){
		String sqlToday = "select sum(price) as today from sal where sal_date like '" + new DateTimeManager().getToday() + "%';";
		String sqlYester = "select sum(price) as yester from sal where sal_date like '" + new DateTimeManager().setTodayNow().getYesterday() + "%';";
		Cursor c = null;
		String value = null;
		
		c = dbm.search(sqlToday);
		c.moveToNext();
		value = c.getString(0);
		value = (value == null)? "0" : value;
		tvToday.setText(value);
		
		c = dbm.search(sqlYester);
		c.moveToNext();
		value = c.getString(0);
		value = (value == null)? "0" : value;
		tvYester.setText(value);
		//msg.show(this, sqlYester);
	}
	
	protected OnCheckedChangeListener chkToday_OnCheckedChange = new OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			String s = etSearch.getText().toString();
			Cursor c = null;
			//"select sal_date as _id, srv_name as name, price from sal where _id like '#d' order by _id desc"
			if(isChecked){
				String t = new DateTimeManager().getToday();
				c = dbm.search("select sal_date as _id, srv_name as name, price from sal where " + 
						"(_id like '%" + s + "%' or name like '%" + s + "%') and _id like '" + t + "%' order by _id desc;"
					);
			}
			else{
				c = dbm.search(autoSql.replace("#s", s));
			}
			adapter.changeCursor(c);
		}
	};
	protected TextWatcher onAutoCompletion = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable arg0) {}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Cursor c = dbm.search(autoSql.replace("#s", s));
			if(c.getCount() != 0)
				adapter.changeCursor(c);
		}
	};
	protected OnItemClickListener lvSearch_OnItemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//TODO : 인텐트 클래스 바꾸기 'ㅅ'
			Intent intent = new Intent(SalesSearch.this, SalesInfo.class);
			
			intent.putExtra("key", ((TextView)arg1.findViewById(R.id.salSch_tvDate)).getText().toString());
			startActivity(intent);
		}
	};
	protected OnClickListener ibDate_OnClick = new OnClickListener(){
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(SalesSearch.this, etSearch, DateTimePicker.DATE_ONLY);
			dtp.show();
		}
	};
}
