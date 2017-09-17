package deneb.eyeroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SalesInfo extends Activity {
	private TextView tvDate = null;
	private TextView tvName = null;
	private TextView tvPrice = null;
	private TextView tvSerial = null;
	private TextView tvPhon = null;
	private TextView tvCarType = null;
	private TextView tvCarNum = null;
	private String key = null;
	
//	sal_date char(14) primary key,		/*판매 일시*/
//	srv_name varchar(30),			/*서비스명*/
//	price int,				/*판매 가격*/
//	serial varchar(20),			/*시리얼 번호*/
//	cus_phon varchar(13),			/*고객 연락처*/
//	car_type varchar(20),			/*차종*/
//	car_num varchar(20)			/*차 번호*/
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sal_info);
		
		key = getIntent().getStringExtra("key");
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.sal_info)
		);
		init();
		data();
		event();
	}
	private void init(){
		tvDate = (TextView)findViewById(R.id.salInfo_tvDateValue);
		tvName = (TextView)findViewById(R.id.salInfo_tvNameValue);
		tvPrice = (TextView)findViewById(R.id.salInfo_tvPriceValue);
		tvSerial = (TextView)findViewById(R.id.salInfo_tvSerialValue);
		tvPhon = (TextView)findViewById(R.id.salInfo_tvCusPhonValue);
		tvCarType = (TextView)findViewById(R.id.salInfo_tvCarTypeValue);
		tvCarNum = (TextView)findViewById(R.id.salInfo_tvCarNumValue);
	}
	private void data(){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("sal", "sal_date", key);
			if(c.moveToNext()){
				tvDate.setText(c.getString(0));
				tvName.setText(c.getString(1));
				tvPrice.setText(c.getString(2));
				tvSerial.setText(c.getString(3));
				tvPhon.setText(c.getString(4));
				tvCarType.setText(c.getString(5));
				tvCarNum.setText(c.getString(6));
			}
			c.close();
			dbm.close();
	}
	
	private void event(){
		Button btnMod = (Button)findViewById(R.id.salInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.salInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.salInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SalesInfo.this, SalesRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", tvDate.getText().toString());
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				msg.show(SalesInfo.this, getResources().getString(R.string.delete_question), "예", "아니오 ", false, delEvent);
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
			DBManager dbm = new DBManager(SalesInfo.this);
			dbm.delete("sal", "sal_date", tvDate.getText().toString());
			msg.show(SalesInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
