package deneb.eyeroid;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SalesRegist extends Activity {
	private Button btnDate = null;
	private Button btnSubmit = null;
	private EditText etCarNum = null;
	private AutoCompleteTextView aetCarType = null;
	private AutoCompleteTextView aetCusPhon = null;
	private AutoCompleteTextView aetName = null;
	private EditText etPrice = null;
	private EditText etSerial = null;
	private String key = null;
	private String submitText = null;

	private String date = null;
	private String name = null;
	private String price = null;
	private String serial = null;
	private String phon = null;
	private String carType = null;
	private String carNum = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sal_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.sal_main_reg)
		);

		init();
		auto();
		data();
	}
	
	public void init(){
		btnDate = (Button)findViewById(R.id.salReg_btnDate);
		aetName = (AutoCompleteTextView)findViewById(R.id.salReg_aetName);
		etPrice = (EditText)findViewById(R.id.salReg_etPrice);
		etSerial = (EditText)findViewById(R.id.salReg_etSerial);
		aetCusPhon = (AutoCompleteTextView)findViewById(R.id.salReg_aetCusPhon);
		aetCarType = (AutoCompleteTextView)findViewById(R.id.salReg_aetCarType);
		etCarNum = (EditText)findViewById(R.id.salReg_etCarNum);
		
		btnSubmit = (Button)findViewById(R.id.salReg_btnSubmit);
		btnDate.setOnClickListener(btnDate_OnClick);
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetName)
		.setSql("srv", "srv_name")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(SalesRegist.this);
				Cursor c = dbm.search("select price from srv where srv_name = '" + 
						((TextView)arg1.findViewById(android.R.id.text1)).getText() + "';");
				if(c.moveToNext()){
					etPrice.setText(c.getString(0));
				}
				c.close();
				dbm.close();
			}
		});
		
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(this.aetCusPhon)
		.setSql("cus", "cus_phon")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(SalesRegist.this);
				Cursor c = dbm.search("select car_type, car_num from cus where cus_phon = '" + 
						((TextView)arg1.findViewById(android.R.id.text1)).getText() + "';");
				if(c.moveToNext()){
					aetCarType.setText(c.getString(0));
					etCarNum.setText(c.getString(1));
				}
				c.close();
				dbm.close();
			}
		});
		
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(this.aetCarType)
		.setSql("cus", "car_type")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//self completion
			}
		});
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("sal", "sal_date", key);
			if(c.moveToNext()){
				btnDate.setText(c.getString(0));
				aetName.setText(c.getString(1));
				etPrice.setText(c.getString(2));
				etSerial.setText(c.getString(3));
				aetCusPhon.setText(c.getString(4));
				aetCarType.setText(c.getString(5));
				etCarNum.setText(c.getString(6));
			}
			dbm.close();
		}
		else{
			btnDate.setText(new DateTimeManager().setTodayNow().getFullDateTime());
			submitText = getResources().getString(R.string.submit_complete);
		}
	}
	
	private OnClickListener btnDate_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(SalesRegist.this, btnDate);
		}
	};

	private OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			try{
				PhoneNumberFormat pnf = new PhoneNumberFormat();
				date = btnDate.getText().toString();
				name = aetName.getText().toString();
				price = etPrice.getText().toString();
				serial = etSerial.getText().toString(); 
				phon = aetCusPhon.getText().toString(); 
				carType = aetCarType.getText().toString();
				carNum = etCarNum.getText().toString();
				
				String[] stks = new String[4];
				//msg.show(SalesRegist.this, pnf.Convert(etCusPhon));
				DBManager dbm = new DBManager(SalesRegist.this);
				if(key != null){
					dbm.delete("sal", "sal_date", key);
				}
				dbm.insert("sal", dbm.sm(date),
						dbm.sm(name),
						dbm.nm(price),
						dbm.sm(serial),
						dbm.sm(phon),
						dbm.sm(carType),
						dbm.sm(carNum)
				);
				//상품 판매시 그에 따른 재고품 수량 감소
				Cursor c = dbm.search("srv", "srv_name", name);
				String updateSql = "update stk set ea = ea - 1 where code = '#k'";
				if(c.moveToNext()){
					for(int i = 0; i < 4; i++){
						stks[i] = c.getString(2+i);
						if(stks[i] != null && !stks[i].equals("") && !stks[i].toLowerCase().equals("null")){
							dbm.execSQL(updateSql.replace("#k", stks[i]));
						}
					}
				}
				msg.show(SalesRegist.this, submitText, true);
			}
			catch(android.database.sqlite.SQLiteConstraintException ex){
				msg.show(SalesRegist.this, "같은 일시의 데이터가 존재합니다. 잠시만 기다렸다가 다시 시도 해 주십시요.");
			}
			catch(SQLiteException ex){
				new msg(SalesRegist.this, ex.toString());
			}
		}
	};
}
