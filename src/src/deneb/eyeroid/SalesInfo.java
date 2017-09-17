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
	
//	sal_date char(14) primary key,		/*�Ǹ� �Ͻ�*/
//	srv_name varchar(30),			/*���񽺸�*/
//	price int,				/*�Ǹ� ����*/
//	serial varchar(20),			/*�ø��� ��ȣ*/
//	cus_phon varchar(13),			/*�� ����ó*/
//	car_type varchar(20),			/*����*/
//	car_num varchar(20)			/*�� ��ȣ*/
	
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
				msg.show(SalesInfo.this, getResources().getString(R.string.delete_question), "��", "�ƴϿ� ", false, delEvent);
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
		@Override//������ -2 ��
		public void onClick(DialogInterface dialog, int which) {
			DBManager dbm = new DBManager(SalesInfo.this);
			dbm.delete("sal", "sal_date", tvDate.getText().toString());
			msg.show(SalesInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
