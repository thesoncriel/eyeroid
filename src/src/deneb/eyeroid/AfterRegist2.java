package deneb.eyeroid;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class AfterRegist2 extends Activity implements OnGestureListener {
	
	private GestureDetector gd = null;
	private AutoCompleteTextView aetAccName = null;
	private Button btnProDate = null;
	private EditText etFixPrice = null;
	private EditText etCarNum = null;
	private EditText etRunDist = null;
	private AutoCompleteTextView aetCarBody = null;
	private Button btnSubmit = null;
	private String submitText = null;
	private boolean isMod;
	private String key = null;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aft_reg2);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_main_aft_reg) +
				" 2/2"
		);
		init();
		auto();
		data();
	}
	private void init(){
		aetAccName = (AutoCompleteTextView)findViewById(R.id.aftReg_aetAccName);
		btnProDate = (Button)findViewById(R.id.aftReg_btnProDate);
		etFixPrice = (EditText)findViewById(R.id.aftReg_etFixPrice);
		etCarNum = (EditText)findViewById(R.id.aftReg_etCarNum);
		etRunDist = (EditText)findViewById(R.id.aftReg_etRunDist);
		aetCarBody = (AutoCompleteTextView)findViewById(R.id.aftReg_aetCarBody);
		btnSubmit = (Button)findViewById(R.id.aftReg_btnSubmit);

		gd = new GestureDetector(this);
		
		btnProDate.setOnClickListener(btnProDate_OnClick);
		btnSubmit.setOnClickListener(btnSubmit_OnClick);

	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetAccName)
		.setSql("aft", "acc_name");
		
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetCarBody)
		.setSql("aft", "car_body");
	}
	private void data(){
		isMod = this.getIntent().getBooleanExtra("isMod", false);
		if(isMod){
			key = getIntent().getStringExtra("key");
			btnSubmit.setText(getResources().getString(R.string.btnModify));
			submitText = getResources().getString(R.string.modify_complete);
		}
		else{
			submitText = getResources().getString(R.string.submit_complete);
		}
		AfterRegist.getAs().getValues2(aetAccName, 
				btnProDate, etFixPrice, etCarNum,
				etRunDist, aetCarBody);
	}
	OnClickListener btnProDate_OnClick = new OnClickListener(){
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(AfterRegist2.this, (Button)v,
					DateTimePicker.DATE_ONLY);
			dtp.show();
		}
	};
	OnClickListener btnSubmit_OnClick = new OnClickListener(){
		public void onClick(View v) {
			try{
				DBManager dbm = new DBManager(AfterRegist2.this);
				AfterRegist.getAs().setValues2(aetAccName, btnProDate, etFixPrice, etCarNum,
						etRunDist, aetCarBody);
				if(key != null){
					dbm.delete("aft", "acc_date", key);
				}
				dbm.insert("aft", AfterRegist.getAs().getSqlValuesAll());
				//AfterRegist2.this.getPackageManager()
				AfterRegist.clearAs();
				dbm.close();
				msg.show(AfterRegist2.this, submitText, true);
			}catch(Exception ex){
				new msg(AfterRegist2.this, ex.toString());
			}
		}
	};
	public boolean onTouchEvent(MotionEvent me){
		return gd.onTouchEvent(me);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vx,
			float vy) {
		//new msg(this, e1.getX() + " - " + e2.getX() + " : " + vx);
		if(e2.getX() - e1.getX() > 120 && Math.abs(vx) > 200){
			//new msg(this, "¿À µÈ´Ù");
			try{
				AfterRegist.getAs().setValues2(aetAccName, btnProDate, etFixPrice, etCarNum,
						etRunDist, aetCarBody);
				this.finish();
			}
			catch(Exception ex){
				new msg(this, ex.toString());
			}
			return true;
		}
		return false;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
