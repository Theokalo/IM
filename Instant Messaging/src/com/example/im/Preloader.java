package com.example.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Preloader extends Activity {

	TextView tload;
	ImageView iimage;
	ProgressBar pprogressbar1;
	private int splashTime = 5000;
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_preloader);
		tload = (TextView) findViewById(R.id.load);
		iimage = (ImageView) findViewById(R.id.image);
		pprogressbar1 = (ProgressBar) findViewById(R.id.progressBar1);
		thread = new Thread(runable);
	    thread.start(); 
	      
	}
	
	public Runnable runable = new Runnable() {
	    public void run() {
	    	int jumpTime = 0;
            while(jumpTime < splashTime){
	        	
	               try {
	            	   Thread.sleep(100);
	                  jumpTime += 100;
	            
	            pprogressbar1.setProgress(jumpTime);


	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
            }
	        try {
	            startActivity(new Intent(Preloader.this,MainActivity.class));
	            finish();
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	    }
	};
		
		
	}


