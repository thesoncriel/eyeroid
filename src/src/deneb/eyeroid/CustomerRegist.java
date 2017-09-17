package deneb.eyeroid;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CustomerRegist extends Activity{
	
	private EditText etPhon = null;
	private EditText etName = null;
	private AutoCompleteTextView etCarType = null;
	private EditText etCarNum = null;
	private TextView tvDate = null;
	private Button btnSubmit = null;
	private String submitText = null;
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cus_reg);
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_main_cus_reg)
		);
		init();
		auto();
		data();
	}
	private void init(){
		btnSubmit = (Button)findViewById(R.id.cusReg_btnSubmit);
		etPhon = (EditText)findViewById(R.id.cusReg_etPhon);
		etName = (EditText)findViewById(R.id.cusReg_etName);
		etCarType = (AutoCompleteTextView)findViewById(R.id.cusReg_aetCarType);
		etCarNum = (EditText)findViewById(R.id.cusReg_etCarNum);
		tvDate = (TextView)findViewById(R.id.cusReg_tvDate);
		
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
		etPhon.setOnKeyListener(new PhoneNumberFormat());

		try{
			String[] items = this.getResources().getStringArray(R.array.srv_list);
			//msg(items[0]);
			Spinner spCusService = (Spinner)this.findViewById(R.id.cusReg_spService);
			//SpinnerAdapter sa = new SpinnerAdapter();
			//spCusService.set
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spCusService.setPrompt(getResources().getText(R.string.cus_reg_service));
			spCusService.setAdapter(adapter);
		}
		catch(Exception ex){
			//msg(ex.toString(), false);
		}
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(etCarType)
		.setSql("cus", "car_type");
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("cus", "cus_phon", key);
			if(c.moveToNext()){
				etPhon.setText(c.getString(0));
				etName.setText(c.getString(1));
				etCarType.setText(c.getString(2));
				etCarNum.setText(c.getString(3));
				tvDate.setText(c.getString(4));
			}
			dbm.close();
		}
		else{
			tvDate.setText(new DateTimeManager().getToday());
			submitText = getResources().getString(R.string.submit_complete);
		}
	}
	
	protected OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			String phone = etPhon.getText().toString();
			try{
				DBManager dbm = new DBManager(CustomerRegist.this);
				if(phone == null || phone.equals("") || !(phone.length() >= 9 && phone.length() <= 11) ){
					new msg(CustomerRegist.this, "연락처는 숫자 9~11자리어야 합니다.");
					return;
				}
				else{
					if(key != null){
						dbm.delete("cus", "cus_phon", key);
					}
					dbm.insert("cus", dbm.sm(etPhon.getText().toString()), 
							dbm.sm(etName.getText().toString()),
							dbm.sm(etCarType.getText().toString()), 
							dbm.sm(etCarNum.getText().toString()), 
							dbm.sm(tvDate.getText().toString())
					);
					msg.show(CustomerRegist.this, submitText, true);
				}
				dbm.close();
			}
			catch(SQLiteConstraintException ex){
				new msg(CustomerRegist.this, "이미 등록된 연락처 입니다.\n다른 번호로 등록 하여 주십시요.");
				return;
			}
			catch(Exception ex){
				new msg(CustomerRegist.this, "다소 황당한 오류네요 -_-\n" + ex.toString());
				return;
			}
		}
	};
}
