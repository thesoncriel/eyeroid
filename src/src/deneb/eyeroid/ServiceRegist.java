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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ServiceRegist extends Activity {
	
	private EditText etName = null;
	private EditText etPrice = null;
	private AutoCompleteTextView aetStk1 = null;
	private AutoCompleteTextView aetStk2 = null;
	private AutoCompleteTextView aetStk3 = null;
	private AutoCompleteTextView aetStk4 = null;
	private ImageButton ibCamera1 = null;
	private ImageButton ibCamera2 = null;
	private ImageButton ibCamera3 = null;
	private ImageButton ibCamera4 = null;
	
	private Button btnSubmit = null;
	private String key = null;
	private String submitText = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.srv_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.srv_main_reg)
		);
		
		init();
		auto();
		data();
	}
	
	private void init(){
		etName = (EditText)findViewById(R.id.srvReg_etName);
		etPrice = (EditText)findViewById(R.id.srvReg_etPrice);
		aetStk1 = (AutoCompleteTextView)findViewById(R.id.srvReg_aetStk1);
		aetStk2 = (AutoCompleteTextView)findViewById(R.id.srvReg_aetStk2);
		aetStk3 = (AutoCompleteTextView)findViewById(R.id.srvReg_aetStk3);
		aetStk4 = (AutoCompleteTextView)findViewById(R.id.srvReg_aetStk4);
		btnSubmit = (Button)findViewById(R.id.srvReg_btnSubmit);
		
		btnSubmit.setOnClickListener(btnSubmit_OnClick);
		ibCamera1 = (ImageButton)findViewById(R.id.srvReg_ibCamera1);
		ibCamera2 = (ImageButton)findViewById(R.id.srvReg_ibCamera1);
		ibCamera3 = (ImageButton)findViewById(R.id.srvReg_ibCamera1);
		ibCamera4 = (ImageButton)findViewById(R.id.srvReg_ibCamera1);
		
		ibCamera1.setOnClickListener(ibCamera_OnClick);
		ibCamera2.setOnClickListener(ibCamera_OnClick);
		ibCamera3.setOnClickListener(ibCamera_OnClick);
		ibCamera4.setOnClickListener(ibCamera_OnClick);
		
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetStk1)
		.setSql("stk", "code")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(ServiceRegist.this);
				Cursor c = dbm.search("select srv_name from srv where srv_name = '" + 
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
		.setTextView(aetStk2)
		.setSql("stk", "code")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(ServiceRegist.this);
				Cursor c = dbm.search("select srv_name from srv where srv_name = '" + 
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
		.setTextView(aetStk3)
		.setSql("stk", "code")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(ServiceRegist.this);
				Cursor c = dbm.search("select srv_name from srv where srv_name = '" + 
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
		.setTextView(aetStk4)
		.setSql("stk", "code")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DBManager dbm = new DBManager(ServiceRegist.this);
				Cursor c = dbm.search("select srv_name from srv where srv_name = '" + 
						((TextView)arg1.findViewById(android.R.id.text1)).getText() + "';");
				if(c.moveToNext()){
					etPrice.setText(c.getString(0));
				}
				c.close();
				dbm.close();
			}
		});
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("srv", "srv_name", key);
			if(c.moveToNext()){
				etName.setText(c.getString(0));
				etPrice.setText(c.getString(1));
				aetStk1.setText(c.getString(2));
				aetStk2.setText(c.getString(3));
				aetStk3.setText(c.getString(4));
				aetStk4.setText(c.getString(5));
			}
			c.close();
			dbm.close();
		}
		else{
			submitText = getResources().getString(R.string.submit_complete);
		}
	}
	
	private OnClickListener btnSubmit_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			try{
				DBManager dbm = new DBManager(ServiceRegist.this);
				if(key != null){
					dbm.delete("srv", "srv_name", key);
				}
				dbm.insert("srv", dbm.sm(etName.getText().toString()),
						dbm.nm(etPrice.getText().toString()),
						dbm.sm(aetStk1.getText().toString()),
						dbm.sm(aetStk2.getText().toString()),
						dbm.sm(aetStk3.getText().toString()),
						dbm.sm(aetStk4.getText().toString())
				);
				msg.show(ServiceRegist.this, getResources().getString(R.string.submit_complete), true);
			}
			catch(android.database.sqlite.SQLiteConstraintException ex){
				msg.show(ServiceRegist.this, etName.getText().toString() + getResources().getString(R.string.srv_reg_error1));
			}
			catch(SQLiteException ex){
				msg.show(ServiceRegist.this, ex.toString());
			}
		}
	};
	
	private OnClickListener ibCamera_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.srvReg_ibCamera1){
				showCamera(0);
			}
			else if(v.getId() == R.id.srvReg_ibCamera2){
				showCamera(1);
			}
			else if(v.getId() == R.id.srvReg_ibCamera3){
				showCamera(2);
			}
			else if(v.getId() == R.id.srvReg_ibCamera4){
				showCamera(3);
			}
		}
	};
	public void showCamera(int requestCode){
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
        startActivityForResult(intent, requestCode);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
        	String result = intent.getStringExtra("SCAN_RESULT");
        	switch(requestCode){
        	case 0:
        		aetStk1.setText(result);
        		break;
        	case 1:
        		aetStk2.setText(result);
        		break;
        	case 2:
        		aetStk3.setText(result);
        		break;
        	case 3:
        		aetStk4.setText(result);
        		break;
        	}
            
            //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
        }
    }
}
