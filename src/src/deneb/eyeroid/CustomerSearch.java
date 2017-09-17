package deneb.eyeroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;

public class CustomerSearch extends Activity{
	
	private CheckBox chkCou = null;
	private EditText etSearch = null;
	private SimpleCursorAdapter adapter = null;
	private ListView lvList = null;
	private DBManager dbm = null;
	private Cursor c = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cus_sch);
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_main_cus_sch)
		);
		//acCusSearch = (AutoCompleteTextView)findViewById(R.id.acCusSearch);
		dbm = new DBManager(this);
		c = dbm.search("select cus_phon as _id, cus_name as name from cus");
		String[] from = {"_id", "name"};
		int[] to = {R.id.tvCusSchPhon, R.id.tvCusSchName};
		adapter = new SimpleCursorAdapter(this, R.layout.cus_sch_entry, c, from, to);

		lvList = (ListView)this.findViewById(R.id.cusSch_lvList);
		lvList.setAdapter(adapter);

		lvList.setTextFilterEnabled(true);

		chkCou = (CheckBox)findViewById(R.id.cusSch_chkReserv);
		etSearch = (EditText)this.findViewById(R.id.cusSch_etSearch);
		
		dbm.close();
		
		chkCou.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String s = etSearch.getText().toString();
				c.close();
				dbm = new DBManager(CustomerSearch.this);
				if(isChecked){
					String t = new DateTimeManager().getToday();
					c = dbm.search("select cus_phon as _id, cus_name as name from cus where " + 
							"(_id like '%" + s + "%' or name like '%" + s + "%') and _id in " +
							"(select cus_phon from cou where res_date >= '" + t + "');");
				}
				else{
					c = dbm.search("select cus_phon as _id, cus_name as name from cus where _id like '%" + s + "%' or name like '%" + s + "%'");
				}
				adapter.changeCursor(c);
				dbm.close();
			}
		});
		//etSearch.addTextChangedListener(new PhoneNumberFormat(etSearch));
		etSearch.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				dbm = new DBManager(CustomerSearch.this);
				c.close();
				c = dbm.search("select cus_phon as _id, cus_name as name from cus where _id like '%" + s + "%' or name like '%" + s + "%'");
				if(c.getCount() != 0 || CustomerSearch.this.chkCou.isChecked())
					adapter.changeCursor(c);
				dbm.close();
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CustomerSearch.this, CustomerTabView.class);
				
				intent.putExtra("cusPhon", ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText().toString());
				startActivity(intent);
				
				//new msg(CustomerSearch.this, ((TextView)arg1.findViewById(R.id.tvCusSchPhon)).getText() + "");
			}
		});
	}
}
