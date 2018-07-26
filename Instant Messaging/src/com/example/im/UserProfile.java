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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity implements OnClickListener {
	
	TextView tname,tuname,treq;
	Button bback,bdelete,bsend,baccept,bcansel,bignore;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	private static final String REQ_URL = "http://87.203.112.158:5050/IM/add.php";
	private static final String SEND_URL = "http://87.203.112.158:5050/IM/send.php";
	private static final String CANSEL_URL = "http://87.203.112.158:5050/IM/cansel.php";
	private static final String ACCEPT_URL = "http://87.203.112.158:5050/IM/accept.php";
	private static final String IGNORE_URL = "http://87.203.112.158:5050/IM/ignore.php";
	private static final String DELETE_URL = "http://87.203.112.158:5050/IM/delete.php";
	private static final String LISTCHAT_URL = "http://87.203.112.158:5050/IM/list_chat.php";
	private static final String READED_URL = "http://87.203.112.158:5050/IM/readed.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_userprofile);
		tname = (TextView) findViewById(R.id.name);
		tuname = (TextView) findViewById(R.id.uname);
		treq = (TextView) findViewById(R.id.req);
		bback = (Button) findViewById(R.id.back);
		bdelete = (Button) findViewById(R.id.delete);
	    bsend = (Button) findViewById(R.id.send);
	    baccept = (Button) findViewById(R.id.accept);
	    bcansel = (Button) findViewById(R.id.cansel);
	    bignore = (Button) findViewById(R.id.ignore);
	    
	    
	   
	    bsend.setVisibility(View.GONE);
	    bdelete.setVisibility(View.GONE);
	    baccept.setVisibility(View.GONE);
	    bcansel.setVisibility(View.GONE);
	    bignore.setVisibility(View.GONE);
		
		
		
        
        bback.setOnClickListener(this);
        bsend.setOnClickListener(this);
        bdelete.setOnClickListener(this);
        baccept.setOnClickListener(this);
        bignore.setOnClickListener(this);
        bcansel.setOnClickListener(this);
		
		SharedPreferences us = PreferenceManager.getDefaultSharedPreferences(UserProfile.this);
        String username = us.getString("user", "name");
        tname.setText(username);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UserProfile.this);
        String uname = sp.getString("username", "anon");
        tuname.setText(uname);
        tuname.setVisibility(View.GONE);
        treq.setText("Hello " +uname+ " i want to be friends with you.Please accept my friend request!!!");
	    treq.setVisibility(View.GONE);
        new addreq().execute();
	}
	
	public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.back:
        	Intent i = new Intent(this, Tab.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	 Bundle b=new Bundle();
             b.putString("condition","Second Tab");
             i.putExtras(b);
             finish();
             startActivity(i);
            break;
        case R.id.send:
        	new send().execute();
            break;
        case R.id.delete:
        	new delete().execute();
            break;
        case R.id.accept:
        	new accept().execute();
            break;
        case R.id.ignore:
        	new ignore().execute();
            break;
        case R.id.cansel:
        	new cansel().execute();
            break;
        default:
            break;
        }
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the Friends via AsyncTask
		new listchat().execute();
		
	}
	class listchat extends AsyncTask<String, String, String>{
		
		@Override
		   protected String doInBackground(String... args) {
		       // TODO Auto-generated method stub
		        // Check for success tag
		       int success;
		       String ufrom =tuname.getText().toString();
		       String uto =tname.getText().toString();
		       try {
		           // Building Parameters
		           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		               params.add(new BasicNameValuePair("ufrom", ufrom));
		               params.add(new BasicNameValuePair("uto", uto));
		               Log.d("request!", "starting");
		               // getting product details by making HTTP request
		              
		              JSONObject json = jsonParser.makeHttpRequest(
		                      LISTCHAT_URL, "GET", params);
		              JSONObject jsson = jsonParser.makeHttpRequest(
				                READED_URL, "GET", params);

		               // check your log for json response
		                   Log.d("Login attempt", json.toString());
		                   success = json.getInt(TAG_SUCCESS);
		                   if (success == 1) {
		                       Log.d("Logout Successful!", json.toString());
		                      
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
	}
	
	
	class addreq extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Loading...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  REQ_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Friends", json.toString());
                   
                   return json.getString(TAG_MESSAGE);  
                   
           }  else if(success == 2){
        	   Log.d("Accept-Ignore", json.toString());
               
               return json.getString(TAG_MESSAGE);
           		}
           	else if(success == 3){
        	   Log.d("Cansel", json.toString());
               
               return json.getString(TAG_MESSAGE);
           		}
               
               else{
        	   	   
                   Log.d("No Friends", json.getString(TAG_MESSAGE));
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
           if (file_url != null && file_url.equals("Friends")){
        	   Log.e("file", "["+file_url+"]");
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               showdelete();
              
           }
           else if(file_url != null && file_url.equals("accept")){
        	   
        	   showaccept_ignore();
           }
           else if(file_url != null && file_url.equals("cansel")){
        	   showcansel();
           }
           else if(file_url != null && file_url.equals("No Friends")){
        	   Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
        	   showsend();
           }
           

       }
       public void showsend(){
    	   bsend.setVisibility(View.VISIBLE);   	    
       }
       public void showdelete(){
    	   bdelete.setVisibility(View.VISIBLE);
    	   
       }
       public void showaccept_ignore(){
    	   baccept.setVisibility(View.VISIBLE);
    	   bignore.setVisibility(View.VISIBLE);
    	   treq.setVisibility(View.VISIBLE);
       }
       public void showcansel(){
    	   bcansel.setVisibility(View.VISIBLE);
    	   
       }

   }
	class send extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Sending...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  SEND_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Send", json.toString());
                   finish();
                   startActivity(getIntent());
                   
                   return json.getString(TAG_MESSAGE);  
                   
           }                
               else{
        	   	   
                   Log.d("No send", json.getString(TAG_MESSAGE));
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
           if (file_url != null ){
        	   
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               
              
           }
}
}
	class cansel extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Canseling...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  CANSEL_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Cansel", json.toString());
                   finish();
                   startActivity(getIntent());
                   return json.getString(TAG_MESSAGE);  
                   
           }                
               else{
        	   	   
                   Log.d("No cansel", json.getString(TAG_MESSAGE));
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
           if (file_url != null ){
        	   
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               
              
           }
}
}
	
	class accept extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Accepting...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  ACCEPT_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Accept", json.toString());
                   finish();
                   startActivity(getIntent());
                   return json.getString(TAG_MESSAGE);  
                   
           }                
               else{
        	   	   
                   Log.d("No accept", json.getString(TAG_MESSAGE));
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
           if (file_url != null ){
        	   
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               
              
           }
}
}
	class ignore extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Ignoring...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  IGNORE_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Ignore", json.toString());
                   finish();
                   startActivity(getIntent());
                   return json.getString(TAG_MESSAGE);  
                   
           }                
               else{
        	   	   
                   Log.d("No ignore", json.getString(TAG_MESSAGE));
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
           if (file_url != null ){
        	   
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               
              
           }
}
}
	class delete extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(UserProfile.this);
       pDialog.setMessage("Deleting...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String u_name =tname.getText().toString();
       String username =tuname.getText().toString();
       
     
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("u_name", u_name));
               params.add(new BasicNameValuePair("username", username));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  DELETE_URL, "GET", params);

           // check your log for json response
               Log.d("user attempt", json.toString());

               // json success tag
               
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
           
                   Log.d("Delete", json.toString());
                   finish();
                   startActivity(getIntent());
                   return json.getString(TAG_MESSAGE);  
                   
           }                
               else{
        	   	   
                   Log.d("No delete", json.getString(TAG_MESSAGE));
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
           if (file_url != null ){
        	   
               Toast.makeText(UserProfile.this, file_url, Toast.LENGTH_LONG).show();
               
              
           }
}
}
}
	
