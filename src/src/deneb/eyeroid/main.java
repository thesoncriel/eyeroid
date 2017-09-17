package deneb.eyeroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class main extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	private TextView tvReserv = null;
    private TextView tvStock = null;
    private Button btnMain0 = null;
    private Button btnMain1 = null;
    private Button btnMain2 = null;
    private Button btnMain3 = null;
    private TextView tvCopyleft = null;
    private DBManager dbm = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tvReserv = (TextView)findViewById(R.id.tvReserv);
        tvStock = (TextView)findViewById(R.id.tvStock);
        btnMain0 = (Button)findViewById(R.id.btnMain0);
        btnMain1 = (Button)findViewById(R.id.btnMain1);
        btnMain2 = (Button)findViewById(R.id.btnMain2);
        btnMain3 = (Button)findViewById(R.id.btnMain3);
        
        btnMain0.getBackground().setAlpha(75);
        btnMain1.getBackground().setAlpha(75);
        btnMain2.getBackground().setAlpha(75);
        btnMain3.getBackground().setAlpha(75);
        
        tvReserv.setOnClickListener(this);
        tvStock.setOnClickListener(this);
        btnMain0.setOnClickListener(this);
        btnMain1.setOnClickListener(this);
        btnMain2.setOnClickListener(this);
        btnMain3.setOnClickListener(this);
        
        tvCopyleft = (TextView)findViewById(R.id.main_tvCopyleft);
        //tvCopyleft.setOnClickListener(this);
        
        //StringBuilder sb = new StringBuilder();

        

        /*
        try{
        	//dbm.onCreate(sqlite);
        	//
        	//dbm.execSQL("drop table customer;");
        	//dbm.onCreate();
        	//String str[] = {"123", "'호호맨'"};
        	//dbm.insert("haha", str);
        	//dbm.insert("haha", "120", "'슈퍼맨'");
        	//dbm.insert("haha", "121", "'원더맨'");
        	//dbm.insert("haha", "122", "'쬬꼬바'");
        	dbm.insert("cus", "'01074476100'", "'손준현'", "'토렌토'", "'1234'", "'20100601'");
        	//dbm.delete("haha", "num", "120");
        	//dbm.close();
        	//sb.append("일단 성공?");
        	Cursor c = dbm.searchAll("cus");
            while(c.moveToNext()){
            	sb.append(c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2) + " - " + c.getString(3) + " - " + c.getString(4) + "\n");
            }
            //c.close();
            
        }
        catch(Exception ex){
        	sb.append(ex);
        }
        
        //TextView tv = this.get
        //tv.setText(sb.toString());
        setContentView(R.layout.test);
        */
    }

	public void onClick(View v) {
		//msg(v.getId() + "\n" + R.id.btnMain0);
		
		if(v.getId() == R.id.tvReserv){
			Intent intent = new Intent(this, ReservToday.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.tvStock){
			Intent intent = new Intent(this, StockSearch.class);
			intent.putExtra("less", true);
			startActivity(intent);
		}
		else if(v.getId() == R.id.btnMain0){
			popup0();
		}
		else if(v.getId() == R.id.btnMain1){
			popup1();
		}
		else if(v.getId() == R.id.btnMain2){
			popup2();
		}
		else if(v.getId() == R.id.btnMain3){
			popup3();
		}
		else if(v.getId() == R.id.main_tvCopyleft){
			temp();
		}
		/*
		else if(v.getId() == R.id.btnReg){
			Intent intent = new Intent(main.this, CustomerRegist.class);
			intent.putExtra("뭥미:", "뭘깡 ㅡ.ㅡ");
			startActivity(intent);
		}
		*/
		
	}
	/*
	 * 고객 관리 하위 메뉴
	 */
	protected void popup0(){
		PopupSubMenu popup = new PopupSubMenu(this)
		.setTitle(R.string.main_menu_0)
		.setIcon(R.drawable.icon_main_0)
		.addSubMenu(R.string.cus_main_cus_sch, CustomerSearch.class)
		.addSubMenu(R.string.cus_main_cus_reg, CustomerRegist.class)
		.addSubMenu(R.string.cus_main_cou_reg, CounselRegist.class)
		.addSubMenu(R.string.cus_main_aft_reg, AfterRegist.class)
		.show();
	}
	/*
	 * 입고 관리 하위 메뉴
	 */
	protected void popup1(){
		PopupSubMenu popup = new PopupSubMenu(this)
		.setTitle(R.string.main_menu_1)
		.setIcon(R.drawable.icon_main_1)
		.showCamera(2)
		.addSubMenu(R.string.stkin_main_reg, StockInRegist.class)
		.addSubMenu(R.string.stkin_main_camera, StockInRegist.class)
		.show();
	}
	/*
	 * 매출 관리 하위 메뉴
	 */
	protected void popup2(){
		PopupSubMenu popup = new PopupSubMenu(this)
		.setTitle(R.string.main_menu_2)
		.setIcon(R.drawable.icon_main_2)
		.addSubMenu(R.string.sal_main_sch, SalesSearch.class)
		.addSubMenu(R.string.sal_main_reg, SalesRegist.class)
		.show();
	}
	protected void popup3(){
		PopupSubMenu popup = new PopupSubMenu(this)
		.setTitle(R.string.main_menu_3)
		.setIcon(R.drawable.icon_main_3)
		.addSubMenu(R.string.srv_main_sch, ServiceSearch.class)
		.addSubMenu(R.string.srv_main_reg, ServiceRegist.class)
		.addSubMenu(R.string.srv_main_stk_sch, StockSearch.class)
		.addSubMenu(R.string.srv_main_stk_reg, StockRegist.class)
		.show();
	}
	protected void temp(){
		msg.show(this, "임시 명령을 수행 했다능");
		DBManager dbm = new DBManager(this);
		dbm.execSQL("drop table srv;");
		dbm.execSQL(getResources().getString(R.string.create_table_srv));
	}
	
	
	public void onWindowFocusChanged(boolean hasFocus){
		if(hasFocus){
			dbm = new DBManager(this);
			try{
				String t = new DateTimeManager().getToday();
				Cursor c = dbm.search("select acc_date from cou where res_date like '" + t + "%';");
				//msg.show(this, (c == null) + "읭?");
				
				tvReserv.setText(getResources().getString(R.string.main_top_reserv).replace("#n", c.getCount()+""));
				c = dbm.search("select count(*) as cnt from stk where ea <= alert_ea;");
				if(c.moveToNext()){
					tvStock.setText(getResources().getString(R.string.main_top_stock).replace("#n", c.getString(0)));
				}
				c.close();
			}
			catch(Exception ex){
				dbm.onCreate();
				new msg(this, "반갑습니다~!\n당신은 이 프로그램을 처음 사용하고 계십니다.\nDB생성에 성공 하였습니다.\n이제 사용하셔도 좋습니다.");
			}
		}
	}
}