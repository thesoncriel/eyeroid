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

public class CustomerInfo extends Activity{
	
	private TextView tvPhon = null;
	private TextView tvName = null;
	private TextView tvCarType = null;
	private TextView tvCarNum = null;
	private TextView tvRegDate = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cus_info);
	
		init();
		data();
		event();

		((TextView)findViewById(R.id.cusInfo_tvPhon)).getBackground().setAlpha(33);
		((TextView)findViewById(R.id.cusInfo_tvName)).getBackground().setAlpha(33);
		((TextView)findViewById(R.id.cusInfo_tvCarType)).getBackground().setAlpha(33);
		((TextView)findViewById(R.id.cusInfo_tvCarNum)).getBackground().setAlpha(33);
		((TextView)findViewById(R.id.cusInfo_tvRegDate)).getBackground().setAlpha(33);	
	}
	private void init(){
		tvPhon = (TextView)findViewById(R.id.cusInfo_tvPhonValue);
		tvName = (TextView)findViewById(R.id.cusInfo_tvNameValue);
		tvCarType = (TextView)findViewById(R.id.cusInfo_tvCarTypeValue);
		tvCarNum = (TextView)findViewById(R.id.cusInfo_tvCarNumValue);
		tvRegDate = (TextView)findViewById(R.id.cusInfo_tvRegDateValue);
	}
	private void data(){
		String sValue = getIntent().getStringExtra("cusPhon");
		DBManager dbm = new DBManager(this);
		Cursor c = dbm.search("select * from cus where cus_phon = '" + sValue + "';");
		
		if(c.moveToNext()){
			tvPhon.setText(c.getString(0));
			tvName.setText(c.getString(1));
			tvCarType.setText(c.getString(2));
			tvCarNum.setText(c.getString(3));
			tvRegDate.setText(c.getString(4));
			c.close();
		}
		dbm.close();
	}
	private void event(){
		Button btnMod = (Button)findViewById(R.id.cusInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.cusInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.cusInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CustomerInfo.this, CustomerRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", tvPhon.getText().toString());
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msg.show(CustomerInfo.this, getResources().getString(R.string.delete_question), "예", "아니오 ", false, delEvent);
			}
		});
		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private DialogInterface.OnClickListener delEvent = new DialogInterface.OnClickListener(){
		@Override//삭제시 -2 값
		public void onClick(DialogInterface dialog, int which) {
			DBManager dbm = new DBManager(CustomerInfo.this);
			dbm.delete("cus", "cus_phon", tvPhon.getText().toString());
			msg.show(CustomerInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
