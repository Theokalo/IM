package com.example.im;


import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tab extends TabActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tab);
            /*Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
	`		
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            */
            Bundle b=getIntent().getExtras();
            String con=b.getString("condition");
            // create the TabHost that will contain the Tabs
            TabHost tabHost = getTabHost();


            TabSpec tab1 = tabHost.newTabSpec("First Tab");
            TabSpec tab2 = tabHost.newTabSpec("Second Tab");
            TabSpec tab3 = tabHost.newTabSpec("Third Tab");
            
            
            
           // Set the Tab name and Activity
           // that will be opened when particular Tab will be selected
            tab1.setIndicator("",getResources().getDrawable(R.drawable.home));
            tab1.setContent(new Intent(this,Profile.class));
           
            tab2.setIndicator("",getResources().getDrawable(R.drawable.epafes));
            tab2.setContent(new Intent(this,Contacts.class));
            
            tab3.setIndicator("",getResources().getDrawable(R.drawable.blue_cloud));
            tab3.setContent(new Intent(this,Messages.class));
           
            /** Add the tabs  to the TabHost to display. */
            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
            tabHost.addTab(tab3);
            tabHost.setCurrentTab(0);
            
           /*tabHost.getTabWidget().getChildAt(0).setLayoutParams(new
              LinearLayout.LayoutParams((width/2)-2,50));
            tabHost.getTabWidget().getChildAt(1).setLayoutParams(new
                  LinearLayout.LayoutParams((width/2)-2,50));
           */
            
            if(con.equals("Third Tab"))
            	     {
            			tabHost.setCurrentTab(2);
            	     }
           

    }

	

}
