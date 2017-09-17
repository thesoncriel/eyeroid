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

public class ServiceInfo extends Activity {
	private TextView tvName = null;
	private TextView tvPrice = null;
	private TextView tvStk1 = null;
	private TextView tvStk2 = null;
	private TextView tvStk3 = null;
	private TextView tvStk4 = null;
	private String key = null;
//	srv_name varchar(30) primary key,			/*서비스명*/
//	price int,				/*가격*/
//	stk1 varchar(30),			/*사용 재고품 1*/
//	stk2 varchar(30),			/*사용 재고품 2*/
//	stk3 varchar(30),			/*사용 재고품 3*/
//	stk4 varchar(30)			/*사용 재고품 4*/
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.srv_info);
		
		key = getIntent().getStringExtra("key");
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.srv_info)
		);
		init();
		data();
		event();
	}
	private void init(){
		tvName = (TextView)findViewById(R.id.srvInfo_tvNameValue);
		tvPrice = (TextView)findViewById(R.id.srvInfo_tvPriceValue);
		tvStk1 = (TextView)findViewById(R.id.srvInfo_tvStk1);
		tvStk2 = (TextView)findViewById(R.id.srvInfo_tvStk2);
		tvStk3 = (TextView)findViewById(R.id.srvInfo_tvStk3);
		tvStk4 = (TextView)findViewById(R.id.srvInfo_tvStk4);
	}
	private void data(){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("srv", "srv_name", key);
			if(c.moveToNext()){
				tvName.setText(c.getString(0));
				tvPrice.setText(c.getString(1));
				tvStk1.setText(c.getString(2));
				tvStk2.setText(c.getString(3));
				tvStk3.setText(c.getString(4));
				tvStk4.setText(c.getString(5));
			}
			c.close();
			dbm.close();
	}
	
	private void event(){
		Button btnMod = (Button)findViewById(R.id.srvInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.srvInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.srvInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ServiceInfo.this, ServiceRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", key);
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				msg.show(ServiceInfo.this, getResources().getString(R.string.delete_question), "예", "아니오 ", false, delEvent);
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
			DBManager dbm = new DBManager(ServiceInfo.this);
			dbm.delete("cou", "acc_date", key);
			msg.show(ServiceInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
