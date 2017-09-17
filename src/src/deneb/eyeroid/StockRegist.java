package deneb.eyeroid;

import android.app.Activity;
import android.content.Intent;
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

public class StockRegist extends Activity {

	private EditText etName = null;
	private AutoCompleteTextView aetCate = null;
	private EditText etModel = null;
	private EditText etCode = null;
	private EditText etEa = null;
	private EditText etAlertEa = null;
	private AutoCompleteTextView aetMaker = null;
	private Button btnDate = null;
	private Button btnSubmit = null;
	private String key = null;
	private String submitText = null;
	
	String cate = null;
	String code = null;
	String name = null;
	String model = null;
	String ea = null;
	String maker = null;
	String alert_ea = null;
	String lastdate = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stk_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.srv_main_stk_reg)
		);
		
		init();
		auto();
		data();
	}
	private void init(){
		findViewById(R.id.stkReg_ibCamera).setOnClickListener(ibCamera_OnClick);
		
		etName = (EditText)findViewById(R.id.stkReg_etName);
		aetCate = (AutoCompleteTextView)findViewById(R.id.stkReg_aetCate);
		etModel = (EditText)findViewById(R.id.stkReg_etModel);
		etCode = (EditText)findViewById(R.id.stkReg_etCode);
		etEa = (EditText)findViewById(R.id.stkReg_etEa);
		etAlertEa = (EditText)findViewById(R.id.stkReg_etAlertEa);
		aetMaker = (AutoCompleteTextView)findViewById(R.id.stkReg_aetMaker);
		btnDate = (Button)findViewById(R.id.stkReg_btnDate);
		btnSubmit = (Button)findViewById(R.id.stkReg_btnSubmit);
		
		btnDate.setOnClickListener(btnDate_OnClick);
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetCate)
		.setSql("stk", "cate");
		
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(this.aetMaker)
		.setSql("stk", "maker");
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("stk", "code", key);
			if(c.moveToNext()){
				etName.setText(c.getString(0));
				aetCate.setText(c.getString(1));
				etModel.setText(c.getString(2));
				etCode.setText(c.getString(3));
				etEa.setText(c.getString(4));
				etAlertEa.setText(c.getString(5));
				aetMaker.setText(c.getString(6));
				btnDate.setText(c.getString(7));
			}
			dbm.close();
		}
		else{
			btnDate.setText(new DateTimeManager().getToday());
			submitText = getResources().getString(R.string.submit_complete);
		}
	}
	
	
	protected OnClickListener ibCamera_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			showCamera();
		}
	};
	protected OnClickListener btnDate_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(StockRegist.this, btnDate, DateTimePicker.DATE_ONLY);
		}
	};
	protected OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
			name = etName.getText().toString();
			cate = aetCate.getText().toString();
			model = etModel.getText().toString();
			code = etCode.getText().toString();
			ea = etEa.getText().toString();
			alert_ea = etAlertEa.getText().toString();
			maker = aetMaker.getText().toString();
			lastdate = btnDate.getText().toString();
			//String sql1 = "update stk set ea = ea + " + ea + ", lastdate = '" + date + "' where code = '" + code + "';";

			try{
				DBManager dbm = new DBManager(StockRegist.this);
				if(key != null){
					dbm.delete("stk", "code", key);
				}
				if(code != null && !code.equals("")){
					dbm.insert("stk",
							dbm.sm(name),
							dbm.sm(cate),
							dbm.sm(model),
							dbm.sm(code),
							dbm.nm(ea),
							dbm.nm(alert_ea),
							dbm.sm(maker),
							dbm.sm(lastdate)
					);
					msg.show(StockRegist.this, code + getResources().getString(R.string.submit_complete), true);
					return;
				}
			}
			catch(android.database.sqlite.SQLiteConstraintException ex){
				msg.show(StockRegist.this, "바코드값 " + code + "을 가진 재고가 이미 있습니다. 다른 제품의 값으로 넣어 주세요~");
			}
			catch(SQLiteException ex){
				ex.printStackTrace();
			}
			finally{
				//new msg(StockInRegist.this, code + "를 가진 재고 정보가 없습니다.\n먼저 재고 정보 등록 부터 해 주세요 ^^");
			}
		}
	};
	public void showCamera(){
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
        startActivityForResult(intent, 0);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            code = intent.getStringExtra("SCAN_RESULT");
            //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
        	etCode.setText(code);
        }
    }
}
