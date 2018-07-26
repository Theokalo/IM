package com.example.im;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements OnClickListener {

	
	EditText eusername,epassword,eoutput;
	Button blogin,bregister,bforgotpass;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://87.203.112.158:5050/IM/login.php";
	
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setHomeButtonEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
        //getSupportActionBar().setIcon(R.color.transparent);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
         setContentView(R.layout.activity_main);
        eusername = (EditText) findViewById(R.id.username);
        epassword = (EditText) findViewById(R.id.password);
        checkInternetEnabled();
       
        blogin = (Button) findViewById(R.id.login);
        bregister = (Button) findViewById(R.id.register);
        bforgotpass = (Button) findViewById(R.id.forgotpass);
        
        bregister.setOnClickListener(this);
        
        bforgotpass.setOnClickListener(this);
        
        blogin.setOnClickListener(this);
    }
    private void checkInternetEnabled(){
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo ni = cm.getActiveNetworkInfo();
    	
    	if (ni == null){
    		
    		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("Το Internet είναι απενεργοποιημένο.Θέλετε να το ενεργοποιήσετε;")
    				.setCancelable(false)
    				.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
    					public void onClick(@SuppressWarnings("unused")
    					final DialogInterface dialog,@SuppressWarnings("unused") final int id){
    						startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    						finish();
    					}
    				})
    				.setNegativeButton("Οχι", new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
							dialog.cancel();
							
						}
					});
    		final AlertDialog alert = builder.create();
    		alert.show();
    	}
    }				
						
    public void onClick(View v) {
    		        // TODO Auto-generated method stub
    		        switch (v.getId()) {
    		        case R.id.login:
    		                new AttemptLogin().execute();
    		            break;
    		        case R.id.register:
    		                Intent i = new Intent(this, Register.class);
    		                startActivity(i);
    		            break;
    		        case R.id.forgotpass:
		                Intent x = new Intent(this, Forgotpass.class);
		                startActivity(x);
		            break;
    		 
    		        default:
    		            break;
    		        }
    		    }
        class AttemptLogin extends AsyncTask<String, String, String> {
        		 
        		         /**
        	         * Before starting background thread Show Progress Dialog
        		         * */
        		        boolean failure = false;
        		 
        		        @Override
        		        protected void onPreExecute() {
        	            super.onPreExecute();
        	            pDialog = new ProgressDialog(MainActivity.this);
        		        pDialog.setMessage("Attempting login...");
        	            pDialog.setIndeterminate(false);
        		        pDialog.setCancelable(true);
        		        pDialog.show();
        		        }
        	
        	        @Override
        	        protected String doInBackground(String... args) {
        	            // TODO Auto-generated method stub
        	             // Check for success tag
        	            int success;
        	            String username = eusername.getText().toString();
        	            String password = epassword.getText().toString();
        	            try {
        	                // Building Parameters
        	                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        		                params.add(new BasicNameValuePair("username", username));
        		                params.add(new BasicNameValuePair("password", password));
        		                Log.d("request!", "starting");
        	                // getting product details by making HTTP request
        	               JSONObject json = jsonParser.makeHttpRequest(
        	                       LOGIN_URL, "POST", params);
        	               
        	 
        	                // check your log for json response
        		                Log.d("Login attempt", json.toString());
        		 
        		                // json success tag
        		                success = json.getInt(TAG_SUCCESS);
        		                if (success == 1) {
        		                    Log.d("Login Successful!", json.toString());
        		                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        		                    Editor edit = sp.edit();
        		                    edit.putString("username", username);
        		                    edit.putString("password", password);
        		                    edit.commit();
        		                    Intent i = new Intent(MainActivity.this,Tab.class);
        		                    Bundle b=new Bundle();
        		                    b.putString("condition","First Tab");
        		                    i.putExtras(b);
        		               		finish();
        		               		startActivity(i);
        		                    
        	                    
        		                    return json.getString(TAG_MESSAGE);
        	                }else{
        		                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
        		                    return json.getString(TAG_MESSAGE);
        	 
        		                }
        		            } catch (JSONException e) {
        		                e.printStackTrace();
        	            }
        		 
        		            return null;
        		 
        		        }
        		        /**
        	         * After completing background task Dismiss the progress dialog
        		         * **/
        		        protected void onPostExecute(String file_url) {
        		            // dismiss the dialog once product deleted
        	            pDialog.dismiss();
        		            if (file_url != null){
        		                Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG).show();
        		            }
        		 
        		        }
        		 
        		    }
        
        @Override
        public void onBackPressed() {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }
}
 