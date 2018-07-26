package com.example.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;



public class Messages extends ListActivity {

TextView tname,tmessages;
	
private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
private static final String GETMESSAGES_URL = "http://87.203.112.158:5050/IM/getmessages.php";
private static final String READED_URL = "http://87.203.112.158:5050/IM/readed.php";	
	private static final String TAG_USERNAME = "uname";
	private static final String TAG_POSTS = "posts";
	
	
	
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_messages);
		tname = (TextView) findViewById(R.id.name);
		tmessages = (TextView) findViewById(R.id.messages);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Messages.this);
        String username = sp.getString("username", "anon");
        tname.setText(username);        
        tname.setVisibility(View.GONE);
       

	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the Friends via AsyncTask
		new Friends().execute();
	}
	
	public void updateJSONdata() {
		
		 String uname = tname.getText().toString();
	     
         // Building Parameters
         List<NameValuePair> params = new ArrayList<NameValuePair>(1);
             params.add(new BasicNameValuePair("uname", uname));
            
            
             
             Log.d("request!", "starting");
         // getting product details by making HTTP request
             JSONObject jsson = jsonParser.makeHttpRequest(
	                      GETMESSAGES_URL, "GET", params);
		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();
		
		//String uname = tname.getText().toString();
		//List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        //params.add(new BasicNameValuePair("uname", uname));
		// Bro, it's time to power up the J parser
		//JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jsonParser.getJSONFromUrl(GETMESSAGES_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {
			

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);
			
			Log.e("friends", "["+mComments+"]");
            

			

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);
				
				

				// gets the content of each tag
				//String img1 = c.getString(TAG_IMAGE1);
				//String img2 = c.getString(TAG_IMAGE2);
				//String content = c.getString(TAG_NEWMESSAGE);
				String username = c.getString(TAG_USERNAME);
				
				 
				 
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				//map.put(TAG_IMAGE1, img1);
				//map.put(TAG_IMAGE2, img2);
				//map.put(TAG_NEWMESSAGE, content);
				map.put(TAG_USERNAME, username);

				// adding HashList to ArrayList
				mCommentList.add(map);

				// annndddd, our JSON data is up to date same with our array
				// list
			}
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	  private void updateList() {
		// For a ListActivity we need to set the List Adapter, and in order to do
		//that, we need to create a ListAdapter.  This SimpleAdapter,
		//will utilize our updated Hashmapped ArrayList, 
		//use our single_post xml template for each item in our list,
		//and place the appropriate info from the list to the
		//correct GUI id.  Order is important here.		
		 
		
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_post, new String[] {TAG_USERNAME} , new int[] {R.id.username});
		
		
		// I shouldn't have to comment on this one:
		setListAdapter(adapter);
		
		// Optional: when the user clicks a list item we 
		//could do something.  
		ListView lv = getListView();	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// This method is triggered if an item is click within our
				// list.
				 
				SharedPreferences us = PreferenceManager.getDefaultSharedPreferences(Messages.this);
				String username  = (String) ((TextView) view.findViewById(R.id.username)).getText();
				Editor edit = us.edit();
                edit.putString("user",username);
                edit.commit();
                
                String uname = tname.getText().toString();
			     
		         // Building Parameters
		         List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		             params.add(new BasicNameValuePair("uname", uname));
		             params.add(new BasicNameValuePair("ufrom",username));
		            
		             
		             Log.d("request!", "starting");
		         // getting product details by making HTTP request
		             JSONObject jsson = jsonParser.makeHttpRequest(
			                      READED_URL, "GET", params);
                Intent i = new Intent(Messages.this,Tabprofile.class);
                finish();
                startActivity(i);
				
			}

			
		});
	}
	
	public class Friends extends AsyncTask<Void, Void, Boolean> {

			
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Messages.this);
				pDialog.setMessage("Loading Messages List...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			
			protected Boolean doInBackground(Void... arg0) {

				updateJSONdata();
			    
				return null;

			}

		
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pDialog.dismiss();
				updateList();
			}
		}
		
}


