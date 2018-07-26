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

public class Register extends Activity implements OnClickListener {
	
	EditText eusername,epassword1,epassword2,eemail1,eemail2,efirstname,elastname;
	Button bregister,bback;
	
private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://87.203.112.158:5050/IM/dbregister.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_register);
		
		 eusername = (EditText) findViewById(R.id.username);
	        epassword1 = (EditText) findViewById(R.id.password1);
	        epassword2 = (EditText) findViewById(R.id.password2);
	        eemail1 = (EditText) findViewById(R.id.email1);
	        eemail2 = (EditText) findViewById(R.id.email2);
	        efirstname = (EditText) findViewById(R.id.firstname);
	        elastname = (EditText) findViewById(R.id.lastname);
	        bregister = (Button) findViewById(R.id.register);
	        bback = (Button) findViewById(R.id.back);
	        
	        bback.setOnClickListener(this);
	        
	        bregister.setOnClickListener(this);
	}
	
	public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.register:
                new AttemptLogin().execute();
            break;
        case R.id.back:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
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
            pDialog = new ProgressDialog(Register.this);
	        pDialog.setMessage("Creating User...");
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
            String password1 = epassword1.getText().toString();
            String password2 = epassword2.getText().toString();
            String email1 = eemail1.getText().toString();
            String email2 = eemail2.getText().toString();
            String firstname = efirstname.getText().toString();
            String lastname = elastname.getText().toString();
            try {
                // Building Parameters
					
					List<NameValuePair> params = new ArrayList<NameValuePair>(1);
					params.add(new BasicNameValuePair("name",username));
					params.add(new BasicNameValuePair("pass1",password1));
					params.add(new BasicNameValuePair("pass2",password2));
					params.add(new BasicNameValuePair("email1",email1));
					params.add(new BasicNameValuePair("email2",email2));
					params.add(new BasicNameValuePair("firstname",firstname));
					params.add(new BasicNameValuePair("lastname",lastname));
	                Log.d("request!", "starting");
                // getting product details by making HTTP request
               JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
	                Log.d("Login attempt", json.toString());
	 
	                // json success tag
	                success = json.getInt(TAG_SUCCESS);
	                if (success == 1) {
	                    Log.d("Register Successful!", json.toString());
	                    Intent i = new Intent(Register.this,MainActivity.class);
	                    finish();
                    startActivity(i);
	                    return json.getString(TAG_MESSAGE);
                }else{
	                    Log.d("Register Failure!", json.getString(TAG_MESSAGE));
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
	                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
	            }
	 
	        }
	 
	    }
}
					
	