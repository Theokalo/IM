package com.example.im;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Forgotpass extends Activity implements OnClickListener {

	EditText eusername,eemail,enpassword;
	Button bchangepass,bback;
	
private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://87.203.112.158:5050/IM/forgotpass.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_forgotpass);
		 eusername = (EditText) findViewById(R.id.username);
	     enpassword = (EditText) findViewById(R.id.npassword);
	     eemail = (EditText) findViewById(R.id.email);
	     bchangepass = (Button) findViewById(R.id.changepass);
	     bback= (Button) findViewById(R.id.back);
	     
	     bback.setOnClickListener(this);
	     bchangepass.setOnClickListener(this);
	}
	public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.changepass:
                new changepassword().execute();
            break;
        case R.id.back:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            break;
        
        default:
            break;
        }
    }
class changepassword extends AsyncTask<String, String, String> {
	 
	         /**
         * Before starting background thread Show Progress Dialog
	         * */
	        boolean failure = false;
	 
	        @Override
	        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Forgotpass.this);
	        pDialog.setMessage("Changing password...");
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
            String email = eemail.getText().toString();
            String password = enpassword.getText().toString();
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
	                params.add(new BasicNameValuePair("username", username));
	                params.add(new BasicNameValuePair("email", email));
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
	                    Intent i = new Intent(Forgotpass.this,MainActivity.class);
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
	                Toast.makeText(Forgotpass.this, file_url, Toast.LENGTH_LONG).show();
	            }
	 
	        }
	 
	    }
}