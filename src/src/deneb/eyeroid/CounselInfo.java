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

public class CounselInfo extends Activity {
	
	private TextView tvPhon = null;
	private TextView tvName = null;
	private TextView tvCont = null;
	private TextView tvAccDate = null;
	private TextView tvResDate = null;
	private String key = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cou_info);
		
		key = getIntent().getStringExtra("key");
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cou_info)
		);
		init();
		data();
		event();
	}
//	cus_phon varchar(13),			/*연락처*/
//	cus_name varchar(20),			/*고객명*/
//	cus_cont text,				/*상담내용*/
//	acc_date char(19) primary key,		/*접수일시*/
//	res_date char(19)			/*예약일시*/
	private void init(){
		tvPhon = (TextView)findViewById(R.id.couInfo_tvPhonValue);
		tvName = (TextView)findViewById(R.id.couInfo_tvNameValue);
		tvCont = (TextView)findViewById(R.id.couInfo_tvContValue);
		tvAccDate = (TextView)findViewById(R.id.couInfo_tvAccDateValue);
		tvResDate = (TextView)findViewById(R.id.couInfo_tvResDateValue);
	}
	private void data(){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("cou", "acc_date", key);
			if(c.moveToNext()){
				tvPhon.setText(c.getString(0));
				tvName.setText(c.getString(1));
				tvCont.setText(c.getString(2));
				tvAccDate.setText(c.getString(3));
				tvResDate.setText(c.getString(4));
			}
			c.close();
			dbm.close();
	}
	
	private void event(){
		Button btnMod = (Button)findViewById(R.id.couInfo_btnMod);
		Button btnDel = (Button)findViewById(R.id.couInfo_btnDel);
		Button btnOK = (Button)findViewById(R.id.couInfo_btnOK);
		
		btnMod.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CounselInfo.this, CounselRegist.class);
				intent.putExtra("isMod", true);
				intent.putExtra("key", tvAccDate.getText().toString());
				startActivity(intent);
				finish();
			}
		});
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				msg.show(CounselInfo.this, getResources().getString(R.string.delete_question), "예", "아니오 ", false, delEvent);
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
			DBManager dbm = new DBManager(CounselInfo.this);
			dbm.delete("cou", "acc_date", tvAccDate.getText().toString());
			msg.show(CounselInfo.this, getResources().getString(R.string.delete_complete), true);
		}	
	};
}
