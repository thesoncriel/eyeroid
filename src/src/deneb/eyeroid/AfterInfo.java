package deneb.eyeroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AfterInfo extends Activity {
	private TextView tvAccNum = null;
	private TextView tvAccDate = null;
	private TextView tvCusName = null;
	private TextView tvCusAddr = null;
	private TextView tvCusPhon = null;
	private TextView tvModel = null;
	private TextView tvMakeNum = null;
	private TextView tvSalDate = null;
	private TextView tvWarranty = null;
	private TextView tvAccCont = null;
//	acc_num varchar(20),			/*접수 번호*/
//	acc_date char(14) primary key,		/*접수 일시*/
//	cus_name varchar(20),			/*고객명*/
//	cus_addr text,				/*주소*/
//	cus_phon varchar(13),			/*연락처*/
//	model varchar(30),			/*모델명*/
//	make_num varchar(20),			/*제조번호(품번)*/
//	sal_date char(10),			/*구입일자*/
//	warranty char(2),			/*보증구분*/
//	acc_cont text,				/*접수 내용*/
	private TextView tvAccName = null;
	private TextView tvProDate = null;
	private TextView tvFixPrice = null;
	private TextView tvCarNum = null;
	private TextView tvRunDist = null;
	private TextView tvCarBody = null;
//	acc_name varchar(20),			/*접수자*/
//	pro_date char(8),			/*처리 일자*/
//	fixprice int,				/*수리금액*/
//	car_num varchar(20),			/*차번호*/
//	run_dist int,				/*주행거리*/
//	car_body varchar(20)			/*차대번호*/
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aft_info3);
		
		key = getIntent().getStringExtra("key");
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.aft_info)
		);
		init();
		data();
		event();
	}
	private void init(){
		tvAccNum = (TextView)findViewById(R.id.aftInfo_tvAccNumValue);
		tvAccDate = (TextView)findViewById(R.id.aftInfo_tvAccDateValue);
		tvCusName = (TextView)findViewById(R.id.aftInfo_tvCusNameValue);
		tvCusAddr = (TextView)findViewById(R.id.aftInfo_tvCusAddrValue);
		tvCusPhon = (TextView)findViewById(R.id.aftInfo_tvCusPhonValue);
		tvModel = (TextView)findViewById(R.id.aftInfo_tvModelValue);
		tvMakeNum = (TextView)findViewById(R.id.aftInfo_tvMakeNumValue);
		tvSalDate = (TextView)findViewById(R.id.aftInfo_tvSalDateValue);
		tvWarranty = (TextView)findViewById(R.id.aftInfo_tvWarrantyValue);
		tvAccCont = (TextView)findViewById(R.id.aftInfo_tvAccContValue);
		
		tvAccName = (TextView)findViewById(R.id.aftInfo_tvAccNameValue);
		tvProDate = (TextView)findViewById(R.id.aftInfo_tvProDateValue);
		tvFixPrice = (TextView)findViewById(R.id.aftInfo_tvFixPriceValue);
		tvCarNum = (TextView)findViewById(R.id.aftInfo_tvCarNumValue);
		tvRunDist = (TextView)findViewById(R.id.aftInfo_tvRunDistValue);
		tvCarBody = (TextView)findViewById(R.id.aftInfo_tvCarBodyValue);
	}
	private void data(){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("aft", "acc_date", key);
			if(c.moveToNext()){
				tvAccNum.setText(c.getString(0));
				tvAccDate.setText(c.getString(1).substring(0, 16));
				tvCusName.setText(c.getString(2));
				tvCusAddr.setText(c.getString(3));
				tvCusPhon.setText(c.getString(4));
				tvModel.setText(c.getString(5));
				tvMakeNum.setText(c.getString(6));
				tvSalDate.setText(c.getString(7));
				tvWarranty.setText(c.getString(8));
				tvAccCont.setText(c.getString(9));
				
				tvAccName.setText(c.getString(10));
				tvProDate.setText(c.getString(11));
				tvFixPrice.setText(c.getString(12));
				tvCarNum.setText(c.getString(13));
				tvRunDist.setText(c.getString(14));
				tvCarBody.setText(c.getString(15));
			}
			c.close();
			dbm.close();
	}
	
	private void event(){
		Button btnMod = (Button)findViewById(R.id.aftInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.aftInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.aftInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AfterInfo.this, AfterRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", key);
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				msg.show(AfterInfo.this, getResources().getString(R.string.delete_question), "예", "아니오 ", false, delEvent);
			}
		});
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private DialogInterface.OnClickListener delEvent = new DialogInterface.OnClickListener(){
		@Override//삭제시 -2 값
		public void onClick(DialogInterface dialog, int which) {
			DBManager dbm = new DBManager(AfterInfo.this);
			dbm.delete("aft", "code", key);
			msg.show(AfterInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
