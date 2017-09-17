package deneb.eyeroid;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;

public class CustomerTabView extends TabActivity {
	public void onCreate(Bundle savedInstanceState) 
    { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.cus_tab);

        String cusPhon = this.getIntent().getStringExtra("cusPhon");
        super.setTitle(
				getResources().getText(R.string.app_name) + " - " + 
				getResources().getText(R.string.cus_info) + " : " +
				cusPhon
		);
        
        TabWidget tabs = (TabWidget)findViewById(android.R.id.tabs);
        //tabs.getBackground().setAlpha(75);
        final TabHost th = getTabHost();
        Intent int0 = new Intent(this, CustomerInfo.class);
        Intent int1 = new Intent(this, CounselSearch.class);
        Intent int2 = new Intent(this, PurchaseSearch.class);
        Intent int3 = new Intent(this, AfterSearch.class);
        //th.getBackground().setAlpha(75);
        int0.putExtra("cusPhon", cusPhon);
        int1.putExtra("cusPhon", cusPhon);
        int2.putExtra("cusPhon", cusPhon);
        int3.putExtra("cusPhon", cusPhon);
        //msg.show(this, (th == null) + "??");
        th.addTab(th.newTabSpec("tabCusInfo")
        	.setIndicator(getResources().getString(R.string.cus_tab_0),
        			getResources().getDrawable(R.drawable.icon_cus_tab0))
        	.setContent(int0)
        	);
        th.addTab(th.newTabSpec("tabCusCou")
        	.setIndicator(getResources().getString(R.string.cus_tab_1),
        			getResources().getDrawable(R.drawable.icon_cus_tab1))
        	.setContent(int1)
        	);
        th.addTab(th.newTabSpec("tabCusSal")
        	.setIndicator(getResources().getString(R.string.cus_tab_2),
        			getResources().getDrawable(R.drawable.icon_cus_tab2))
        	.setContent(int2)
        	);
        th.addTab(th.newTabSpec("tabCusAft")
        	.setIndicator(getResources().getString(R.string.cus_tab_3),
        			getResources().getDrawable(R.drawable.icon_cus_tab3))
        	.setContent(int3)
        	);
            
        
        /*
        TabHost tabHost = getTabHost(); 
        
        TabHost.TabSpec spec;
        
        // 첫 번째 탭
        spec = tabHost.newTabSpec( "Tab 011" );
        spec.setIndicator( "닝기리 냥냥", 
        		getResources().getDrawable( R.drawable.icon ) );
        spec.setContent( R.id.TabView1 );
        tabHost.addTab( spec );
        
        // 두 번째 탭
        spec = tabHost.newTabSpec( "Tab 02" );
        spec.setIndicator( "Tab 02" );
        spec.setContent( R.id.TabView2 );
        tabHost.addTab( spec );
        
        // 세 번째 탭
        spec = tabHost.newTabSpec( "Tab 03" );
        spec.setIndicator( "Tab 03" );
        spec.setContent( R.id.TabView3 );
        tabHost.addTab( spec );
        
        tabHost.setCurrentTab( 0 );//기본적으로 선택되는 탭 번호
        */
    } 
}
