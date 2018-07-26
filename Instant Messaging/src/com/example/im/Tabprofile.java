package com.example.im;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tabprofile extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tab);

            // create the TabHost that will contain the Tabs
            TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


            TabSpec tab1 = tabHost.newTabSpec("First Tab");
            TabSpec tab2 = tabHost.newTabSpec("Second Tab");
            

           // Set the Tab name and Activity
           // that will be opened when particular Tab will be selected
            tab1.setIndicator("",getResources().getDrawable(R.drawable.home));
            tab1.setContent(new Intent(this,UserProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
           
            tab2.setIndicator("",getResources().getDrawable(R.drawable.blue_cloud));
            tab2.setContent(new Intent(this,Chat.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            
           
            /** Add the tabs  to the TabHost to display. */
            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
           

		}
}
