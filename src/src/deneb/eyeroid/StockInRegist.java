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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import deneb.camera.SocketCameraView;

public class StockInRegist extends Activity {
	private Button btnDate = null;
	private EditText etName = null;
	private EditText etModel = null;
	private AutoCompleteTextView aetCode = null;
	private EditText etEa = null;
	private Button btnSubmit = null;
	//private static String code = null;
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
		setContentView(R.layout.stkin_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.stkin_main_reg)
		);
		
		init();
		auto();
		//data();

		if(this.getIntent().getBooleanExtra("setToday", true)){
			DateTimeManager dtm = new DateTimeManager();
			btnDate.setText(dtm.getToday());
		}
		if(this.getIntent().getBooleanExtra("showCamera", true) ){
			showCamera();
		}
	}
	private void init(){
		findViewById(R.id.stkinReg_ibCamera).setOnClickListener(ibCamera_OnClick);
		btnSubmit = (Button)findViewById(R.id.stkin_reg_btnSubmit);
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
		
		etName = (EditText)findViewById(R.id.stkin_reg_etName);
		etModel = (EditText)findViewById(R.id.stkin_reg_etModel);
		aetCode = (AutoCompleteTextView)findViewById(R.id.stkin_reg_aetCode);
		etEa = (EditText)findViewById(R.id.stkin_reg_etEa);
		btnDate = (Button)findViewById(R.id.stkin_reg_btnDate);
		btnDate.setOnClickListener(btnDate_OnClick);
		btnDate.setText(new DateTimeManager().setTodayNow().getFullDateTime());
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetCode)
		.setSql("stk", "code")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				data( ((TextView)arg1.findViewById(android.R.id.text1)).getText().toString() );
			}
		});
	}
	private void data(String key){
		DBManager dbm = new DBManager(this);
		Cursor c = dbm.search("stk", "code", key);
    	aetCode.setText(key);
        if(c.moveToNext()){
        	etName.setText(c.getString(0));
        	//cate = c.getString(1);
        	
        	etModel.setText(c.getString(2));
        	//etEa.setText(c.getString(4));
        	//maker = c.getString(5);
        	//alert_ea = c.getString(6);
        	//btnDate.setText(c.getString(7));
        }
		dbm.close();
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
			DateTimePicker dtp = new DateTimePicker(StockInRegist.this, btnDate, DateTimePicker.DATE_ONLY);
			dtp.show();
		}
	};
//	stk_name varchar(30),			/*재고 상품명*/
//	cate varchar(20),			/*분류*/
//	model varchar(30),			/*모델명*/
//	code varchar(30) primary key,	/*바코드 번호*/
//	ea int,					/*개수*/
//	alert_ea int,				/*경고 개수*/
//	maker varchar(20),			/*제조사*/
//	lastdate char(10)			/*최근 입고 일자*/
	protected OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			code = aetCode.getText().toString();
			name = etName.getText().toString();
			model = etModel.getText().toString();
			ea = etEa.getText().toString();
			lastdate = btnDate.getText().toString();
			
			String date = btnDate.getText().toString();
			String check = "select code from stk where code = '" + code + "';";
			String sql1 = "update stk set ea = ea + " + ea + ", lastdate = '" + date + "' where code = '" + code + "';";
			//String sql2 = "update stk set lastdate = '" + date + "' where code = '" + code + "';";
			try{
				DBManager dbm = new DBManager(StockInRegist.this);
				Cursor c = dbm.search(check);
				if(c.getCount() < 1){
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
					msg.show(StockInRegist.this, code + getResources().getString(R.string.stkin_reg_new_data), true);
					return;
				}
				else if(dbm.execSQL(sql1)){
					msg.show(StockInRegist.this, getResources().getString(R.string.submit_complete), true);
					return;
				}
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
		Intent intent = null;
		if( ((CheckBox)findViewById(R.id.stkin_reg_chkSocket)).isChecked() ){
			intent = new Intent(StockInRegist.this, SocketCameraView.class);
			startActivityForResult(intent, 1);
		}
		else{
			intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
	        startActivityForResult(intent, 0);
		}
		
	}
//	stk_name varchar(30),			/*재고 상품명*/
//	cate varchar(20),			/*분류*/
//	model varchar(30),			/*모델명*/
//	code varchar(30) primary key,	/*바코드 번호*/
//	ea int,					/*개수*/
//	alert_ea int,				/*경고 개수*/
//	maker varchar(20),			/*제조사*/
//	lastdate char(10)			/*최근 입고 일자*/
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		String contents = intent.getStringExtra("SCAN_RESULT");
//        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
//        new msg(this, contents + "\n" + format);
        if (resultCode == RESULT_OK) {
            code = intent.getStringExtra("SCAN_RESULT");
            //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            data(code);
            etEa.requestFocus();
            etEa.selectAll();
        }
//        else if(requestCode == 1 && resultCode == RESULT_CANCELED && intent != null){
//        	//웹캠 성능때매 임시로 찍은 것 처럼 보이게 하는 코드 ㅡ_ㅡ;;
//        	String state = intent.getStringExtra("STATE");
//        	if(state != null && state.equals("NotFound")){
//        		code = "8809265760023";
//        		DBManager dbm = new DBManager(this);
//                Cursor c = dbm.search("select * from stk where code = '" + code + "'");
//            	etCode.setText(code);
//            	//msg.show(this, c.getCount() + "");
//                while(c.moveToNext()){
//                	cate = c.getString(0);
//                	etName.setText(c.getString(2));
//                	etModel.setText(c.getString(3));
//                	//etEa.setText(c.getString(4));
//                	maker = c.getString(5);
//                	//alert_ea = c.getString(6);
//                	//btnDate.setText(c.getString(7));
//                }
//        	}
//        }
    }
}
