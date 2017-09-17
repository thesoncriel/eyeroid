package deneb.eyeroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AfterRegist extends Activity implements OnGestureListener{
	
	private EditText etAccNum = null;
	private Button btnAccDate = null;
	private EditText etCusName = null;
	private EditText etAddr = null;
	private AutoCompleteTextView aetCusPhon = null;
	private AutoCompleteTextView aetModel = null;
	private EditText etMakeNum = null;
	private Button btnSalDate = null;
	private Spinner spWarranty = null;
	private EditText etAccCont = null;
	private String key = null;

	
	//private TableLayout tbl = null;
	private GestureDetector gd = null;
	//private Intent aft2 = null;
	private static AfterService as;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aft_reg);
		
		super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_main_aft_reg) +
				" 1/2"
		); 
		
		init();
		auto();
		data();
	}
	private void init(){
		as = new AfterService();
		
		etAccNum = (EditText)findViewById(R.id.aftReg_etAccNum);
		btnAccDate = (Button)findViewById(R.id.aftReg_btnAccDate);
		etCusName = (EditText)findViewById(R.id.aftReg_etCusName);
		etAddr = (EditText)findViewById(R.id.aftReg_etAddr);
		aetCusPhon = (AutoCompleteTextView)findViewById(R.id.aftReg_aetCusPhon);
		aetModel = (AutoCompleteTextView)findViewById(R.id.aftReg_aetModel);
		etMakeNum = (EditText)findViewById(R.id.aftReg_etMakeNum);
		btnSalDate = (Button)findViewById(R.id.aftReg_btnSalDate);
		spWarranty = (Spinner)findViewById(R.id.aftReg_spWarranty);
		etAccCont = (EditText)findViewById(R.id.aftReg_etAccCont);
		
		btnAccDate.setOnClickListener(btnAccDate_OnClick);
		btnSalDate.setOnClickListener(btnSalDate_OnClick);
		
		String[] items = this.getResources().getStringArray(R.array.aft_reg_warranty_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWarranty.setPrompt(items[0]);
		spWarranty.setAdapter(adapter);
		//aft = this.getIntent();
		//aft2 = new Intent(this, AfterRegist2.class);
		//aft2.putExtras("", this.getIntent());
		gd = new GestureDetector(this);
		//tbl = (TableLayout)findViewById(R.id.aftReg_tbl);
	}
	private void auto(){
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetCusPhon)
		.setSql("cus", "cus_phon")
		.setEvent(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				String key = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
				DBManager dbm = new DBManager(AfterRegist.this);
				Cursor c = dbm.search("select cus_name, car_num from cus where cus_phon = '" + key + "'");
				if(c.moveToNext()){
					etCusName.setText(c.getString(0));
					as.setCarNum(c.getString(1));
				}
				dbm.close();
			}
		});
		
		new AutoCompleteManager()
		.setActivity(this)
		.setTextView(aetModel)
		.setSql("stk", "model");
	}
	private void data(){
		if(this.getIntent().getBooleanExtra("isMod", false)){
			key = getIntent().getStringExtra("key");
			DBManager dbm = new DBManager(this);
			Cursor c = dbm.search("aft", "acc_date", key);
			if(c.moveToNext()){
				etAccNum.setText(c.getString(0));
				btnAccDate.setText(c.getString(1));
				etCusName.setText(c.getString(2));
				etAddr.setText(c.getString(3));
				aetCusPhon.setText(c.getString(4));
				aetModel.setText(c.getString(5));
				etMakeNum.setText(c.getString(6));
				btnSalDate.setText(c.getString(7));
				spWarranty.setSelection((c.getString(8).equals("무상"))? 0 : 1);
				etAccCont.setText(c.getString(9));
				
				AfterRegist.getAs().setValues2(c.getString(10), 
						c.getString(11), c.getString(12), 
						c.getString(13), c.getString(14),
						c.getString(15)
						);
			}
			dbm.close();
		}
		else{
			DateTimeManager dtm = new DateTimeManager();
			dtm.setToday();
			dtm.setNow();
			btnAccDate.setText(dtm.getFullDateTime());
			btnSalDate.setText(dtm.getDate());
			AfterRegist.getAs().setProDate(dtm.getDate());
		}
	}
	protected OnClickListener btnAccDate_OnClick = new OnClickListener(){
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(AfterRegist.this, (Button)v);
			dtp.show();
		}
	};
	protected OnClickListener btnSalDate_OnClick = new OnClickListener(){
		public void onClick(View v) {
			DateTimePicker dtp = new DateTimePicker(AfterRegist.this, (Button)v,
					DateTimePicker.DATE_ONLY);
			dtp.show();
		}
	};
	
	public void onWindowFocusChanged(boolean hasFocus){
		if(hasFocus && AfterRegist.getAs() == null){
			finish();
		}
	}
	public static AfterService getAs(){
		return as;
	}
	public static void clearAs(){
		as = null;
	}
	
	public boolean onTouchEvent(MotionEvent me){
		return gd.onTouchEvent(me);
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vx,
			float vy) {
		//new msg(this, e1.getX() + " - " + e2.getX() + " : " + vx);
		if(e1.getX() - e2.getX() > 120 && Math.abs(vx) > 200){
			//new msg(this, "오 된다");
			
			AfterRegist.getAs().setValues(etAccNum, btnAccDate, etCusName, 
					etAddr, aetCusPhon, aetModel, etMakeNum, btnSalDate,
					spWarranty, etAccCont);
			Intent intent = new Intent(this, AfterRegist2.class);
			intent.putExtra("isMod", getIntent().getBooleanExtra("isMod", false));
			intent.putExtra("key", key);
			startActivity(intent);
			//new msg(this, spWarranty.getPrompt().toString());
			return true;
			
		}
		return false;
	}
	

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
