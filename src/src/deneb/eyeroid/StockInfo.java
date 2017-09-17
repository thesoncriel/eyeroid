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

public class StockInfo extends Activity {
	private TextView tvName = null;
	private TextView tvCate = null;
	private TextView tvModel = null;
	private TextView tvCode = null;
	private TextView tvEa = null;
	private TextView tvAlertEa = null;
	private TextView tvMaker = null;
	private TextView tvDate = null;
	private String key = null;
//	stk_name varchar(30),			/*��� ��ǰ��*/
//	cate varchar(20),			/*�з�*/
//	model varchar(30),			/*�𵨸�*/
//	code varchar(30) primary key,	/*���ڵ� ��ȣ*/
//	ea int,					/*����*/
//	alert_ea int,				/*��� ����*/
//	maker varchar(20),			/*������*/
//	lastdate char(10)			/*�ֱ� �԰� ����*/
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stk_info);
		
		key = getIntent().getStringExtra("key");
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.stk_info)
		);
		init();
		data();
		event();
	}
	private void init(){
		tvName = (TextView)findViewById(R.id.stkInfo_tvNameValue);
		tvCate = (TextView)findViewById(R.id.stkInfo_tvCateValue);
		tvModel = (TextView)findViewById(R.id.stkInfo_tvModelValue);
		tvCode = (TextView)findViewById(R.id.stkInfo_tvCodeValue);
		tvEa = (TextView)findViewById(R.id.stkInfo_tvEaValue);
		tvAlertEa = (TextView)findViewById(R.id.stkInfo_tvAlertEaValue);
		tvMaker = (TextView)findViewById(R.id.stkInfo_tvMakerValue);
		tvDate = (TextView)findViewById(R.id.stkInfo_tvDateValue);
	}
	private void data(){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("stk", "code", key);
			if(c.moveToNext()){
				tvName.setText(c.getString(0));
				tvCate.setText(c.getString(1));
				tvModel.setText(c.getString(2));
				tvCode.setText(c.getString(3));
				tvEa.setText(c.getString(4));
				tvAlertEa.setText(c.getString(5));
				tvMaker.setText(c.getString(6));
				tvDate.setText(c.getString(7));
			}
			c.close();
			dbm.close();
	}
	
	private void event(){
		Button btnMod = (Button)findViewById(R.id.stkInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.stkInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.stkInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StockInfo.this, StockRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", key);
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				msg.show(StockInfo.this, getResources().getString(R.string.delete_question), "��", "�ƴϿ� ", false, delEvent);
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
			DBManager dbm = new DBManager(StockInfo.this);
			dbm.delete("stk", "code", key);
			msg.show(StockInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
