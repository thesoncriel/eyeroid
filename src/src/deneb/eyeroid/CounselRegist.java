package deneb.eyeroid;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CounselRegist extends Activity{
	
	private AutoCompleteTextView etPhon = null;
	private EditText etName = null;
	private EditText etCont = null;
	private Button btnAccDate = null;
	private Button btnResDate = null;
	private Button btnSubmit = null;
	private DateTimePicker dtp = null;
	private String submitText = null;
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cou_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_main_cou_reg)
		);
		
		init();
		auto();
		data();
	}
	private void init(){
		etPhon = (AutoCompleteTextView)this.findViewById(R.id.couReg_aetPhon);
		etName = (EditText)this.findViewById(R.id.couReg_etName);
		etCont = (EditText)this.findViewById(R.id.couReg_etCont);
		btnAccDate = (Button)this.findViewById(R.id.couReg_btnAccDate);
		btnResDate = (Button)this.findViewById(R.id.couReg_btnResDate);
		btnSubmit = (Button)this.findViewById(R.id.couReg_btnSubmit);
		
		btnAccDate.setOnClickListener(btnAccDate_OnClick);
		btnResDate.setOnClickListener(btnResDate_OnClick);
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
		etPhon.setOnItemClickListener(aetPhon_OnItemClick);
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(etPhon)
		.setSql("cus", "cus_phon");
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("cou", "acc_date", key);
			if(c.moveToNext()){
				etPhon.setText(c.getString(0));
				etName.setText(c.getString(1));
				etCont.setText(c.getString(2));
				btnAccDate.setText(c.getString(3));
				btnResDate.setText(c.getString(4));
			}
			dbm.close();
		}
		else{
			btnAccDate.setText(new DateTimeManager().setTodayNow().getFullDateTime());
			submitText = getResources().getString(R.string.submit_complete);
		}
	}
	protected OnClickListener btnAccDate_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			dtp = new DateTimePicker(CounselRegist.this, btnAccDate);
		}
	};
	protected OnClickListener btnResDate_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			dtp = new DateTimePicker(CounselRegist.this, btnResDate);
		}
	};
	protected OnItemClickListener aetPhon_OnItemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			String key = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
			DBManager dbm = new DBManager(CounselRegist.this);
			Cursor c = dbm.search("select cus_name from cus where cus_phon = '" + key + "'");
			if(c.moveToNext()){
				etName.setText(c.getString(0));
			}
			dbm.close();
		}
	};
	protected OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			DBManager dbm = null;
			try{
				dbm = new DBManager(CounselRegist.this);
				String res = btnResDate.getText().toString();
				if(res == null || res.contains("¾øÀ½")){
					res = "null";
				}
				else{
					res = dbm.sm(res);
				}
				
				if(key != null){
					dbm.delete("cou", "acc_date", key);
				}
				dbm.insert("cou", 
					dbm.sm(etPhon.getText().toString()),
					dbm.sm(etName.getText().toString()),
					dbm.sm(etCont.getText().toString()),
					dbm.sm(btnAccDate.getText().toString()),
					res
				);
			}
			catch(Exception ex){
				msg.show(CounselRegist.this, ex.toString());
			}
			dbm.close();
			msg.show(CounselRegist.this, submitText, true);
		}
	};
}
