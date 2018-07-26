package com.example.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.im.Chat.notifications;



import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Profile extends ListActivity implements OnClickListener {
	
	TextView tname,tpass,trequests;
	EditText esearchtext;
	Button blogout,bsearchbutton;
	
	private ProgressDialog pDialog;
	final Handler handler = new Handler();
	Runnable getnotify;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://87.203.112.158:5050/IM/log-out.php";
	private static final String SEARCH_URL = "http://87.203.112.158:5050/IM/search.php";
	private static final String FRIENDS_URL = "http://87.203.112.158:5050/IM/friends.php";
	private static final String REQUEST_URL = "http://87.203.112.158:5050/IM/requests.php";
	private static final String GETNOTIFICATIONS_URL = "http://87.203.112.158:5050/IM/getnotifications.php";
	
	
	private static final String TAG_USERNAME = "uname";
	private static final String TAG_NOTIFYUSERS = "notify_users";
	private static final String TAG_NOTIFY = "notify";
	private static final String TAG_POSTS = "posts";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	private JSONArray mComments = null;
	private JSONArray notify = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;
	private ArrayList<HashMap<String, String>> notifyList;
	private NotificationManager mNM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_profile);
		tname = (TextView) findViewById(R.id.name);
		tpass =  (TextView) findViewById(R.id.pass);
		trequests = (TextView) findViewById(R.id.requests);
		esearchtext= (EditText) findViewById(R.id.searchtext);
		blogout = (Button) findViewById(R.id.logout);
		bsearchbutton=(Button) findViewById(R.id.searchbutton);
		
		blogout.setOnClickListener(this);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Profile.this);
			            String username = sp.getString("username", "anon");
			            String password = sp.getString("password", "anon");
			            tname.setText(username);
			            tpass.setText(password);
			            tpass.setVisibility(View.GONE);
			            
	    bsearchbutton.setOnClickListener(this);
	}
		
	public void onClick(View v) {
        // TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.logout:
                new logout().execute();
                break;
                case R.id.searchbutton:
                	new search().execute();
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
		//new Friends().execute();
		new requests().execute();
		handler.postDelayed(getnotify = new Runnable() {
			  @Override
			  public void run() {
			      // TODO Auto-generated method stub
				  
					
					new notifications().execute();
					handler.postDelayed( this,  5000 );
			          }
					
					},5000 );
	}
	
	@Override
	public void onStop(){
		super.onStop();
		handler.removeCallbacks(getnotify);
	}
	
	class notifications extends AsyncTask<String, String, String> {
		 
	     

		   @SuppressWarnings("deprecation")
		@Override
		   protected String doInBackground(String... args) {
		       // TODO Auto-generated method stub
		        // Check for success tag
		      
		       String ufrom = tname.getText().toString();
		       notifyList = new ArrayList<HashMap<String, String>>();
		      
		       try {
		           // Building Parameters
		           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		               params.add(new BasicNameValuePair("ufrom", ufrom));
		              
		               
		               Log.d("request!", "starting");
		           // getting product details by making HTTP request
		          JSONObject json = jsonParser.makeHttpRequest(
		                  GETNOTIFICATIONS_URL, "POST", params);
		          
		           // check your log for json response
		               Log.d("Send attempt", json.toString());

		               // json success tag
		               
		               notify = json.getJSONArray(TAG_NOTIFY);
		               
		                  
		            	   for (int i = 0; i < notify.length(); i++) {
		            		   JSONObject c = notify.getJSONObject(i);
		       				String notifyUsers = c.getString(TAG_NOTIFYUSERS);
		       				HashMap<String, String> map = new HashMap<String, String>();

		    				map.put(TAG_NOTIFYUSERS, notifyUsers);
		    				notifyList.add(map);
		            		   
		            	   mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		           		Notification n = new Notification();
		           		n.icon = R.drawable.im1;
		           		n.tickerText="New Message from " + notifyUsers;
		           		n.when=System.currentTimeMillis();
		           		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Profile.this);
		                Editor edit = sp.edit();
		                edit.putString("username", ufrom);
		                edit.commit();
		           		Intent notificationIntent = new Intent(Profile.this,Tab.class);
		           		Bundle b=new Bundle();
		           		b.putString("condition","Third Tab");
		           		notificationIntent.putExtras(b);
		           		PendingIntent contentIntent = PendingIntent.getActivity(Profile.this, i,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		           		//n.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
		           			    //+ "://" + getPackageName() + "/raw/new_message");
		           		n.flags |= Notification.FLAG_AUTO_CANCEL;
		           		CharSequence contentText = "New message from " +notifyUsers;
		           		CharSequence contentTitle = notifyUsers;
		           		n.setLatestEventInfo(Profile.this, contentTitle, contentText, contentIntent);
		           		mNM.notify(i, n);
		           		

		            	   }
		         
		          } catch (JSONException e) {
		               e.printStackTrace();
		       }

		           return null;

		       }
			}
			 /**
			    * After completing background task Dismiss the progress dialog
			        * **/
			       protected void onPostExecute(String file_url) {
			           // dismiss the dialog once product deleted
			       pDialog.dismiss();
			           if (file_url != null){
			               Toast.makeText(Profile.this, file_url, Toast.LENGTH_LONG).show();
			           }

			       }
	
	public void updateJSONdata() {
		
		 String uname = tname.getText().toString();
	     
	           // Building Parameters
	           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
	               params.add(new BasicNameValuePair("uname", uname));
	              
	              
	               
	               Log.d("request!", "starting");
	           // getting product details by making HTTP request
	              
	          JSONObject jsson = jsonParser.makeHttpRequest(
	                  REQUEST_URL, "GET", params);
		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();
		

		// Bro, it's time to power up the J parser
		//JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jsonParser.getJSONFromUrl(REQUEST_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);
			

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);
			
				// gets the content of each tag
				//String title = c.getString(TAG_TITLE);
				//String content = c.getString(TAG_MESSAGE);
				String username = c.getString(TAG_USERNAME);
				//String req = c.getString(TAG_REQNUM);
				//tnumber_req.setText(req); 
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				//map.put(TAG_TITLE, title);
				//map.put(TAG_REQNUM, reqnum);
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
				R.layout.single_post, new String[] {TAG_USERNAME }, new int[] {R.id.username });

		// I shouldn't have to comment on this one:
		setListAdapter(adapter);
		
		// Optional: when the user clicks a list item we 
		//could do something.  However, we will choose
		//to do nothing...
		ListView lv = getListView();	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// This method is triggered if an item is click within our
				// list. For our example we won't be using this, but
				// it is useful to know in real life applications.
				SharedPreferences us = PreferenceManager.getDefaultSharedPreferences(Profile.this);
				String username  = (String) ((TextView) view.findViewById(R.id.username)).getText();
				Editor edit = us.edit();
                edit.putString("user",username);
                edit.commit();
                Intent i = new Intent(Profile.this,Tabprofile.class);
                finish();
                startActivity(i);
				//new add_req().execute();
			}

			
		});
	}

	public class requests extends AsyncTask<Void, Void, Boolean> {

		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Profile.this);
			pDialog.setMessage("Loading Users...");
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
	/*class Friends extends AsyncTask<String, String, String>{
		
		@Override
		   protected String doInBackground(String... args) {
		       // TODO Auto-generated method stub
		        // Check for success tag
		       int success;
		       String username =tname.getText().toString();
		       try {
		           // Building Parameters
		           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		               params.add(new BasicNameValuePair("username", username));
		               Log.d("request!", "starting");
		               // getting product details by making HTTP request
		              
		              JSONObject json = jsonParser.makeHttpRequest(
		                      FRIENDS_URL, "GET", params);

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
	}*/

	class logout extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(Profile.this);
       pDialog.setMessage("Attempting logout...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String username =tname.getText().toString();
       String password =tpass.getText().toString();
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
               params.add(new BasicNameValuePair("username", username));
               params.add(new BasicNameValuePair("password", password));
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  LOGIN_URL, "POST", params);
          JSONObject jsson = jsonParser.makeHttpRequest(
                  FRIENDS_URL, "GET", params);

           // check your log for json response
               Log.d("Login attempt", json.toString());

               // json success tag
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
                   Log.d("Logout Successful!", json.toString());
                   Intent i = new Intent(Profile.this,MainActivity.class);
                   startActivity(i);
                   finish();
                   return json.getString(TAG_MESSAGE);
           }else{
                   Log.d("Logout Failure!", json.getString(TAG_MESSAGE));
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
               Toast.makeText(Profile.this, file_url, Toast.LENGTH_LONG).show();
           }

       }

   }
	
	class search extends AsyncTask<String, String, String> {
		 
        /**
    * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;

       @Override
       protected void onPreExecute() {
       super.onPreExecute();
       pDialog = new ProgressDialog(Profile.this);
       pDialog.setMessage("Searching...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       pDialog.show();
       }

   @Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
       int success;
       String search =esearchtext.getText().toString();
       
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
               params.add(new BasicNameValuePair("search", search));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  SEARCH_URL, "GET", params);

           // check your log for json response
               Log.d("Search attempt", json.toString());

               // json success tag
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
                   Log.d("Search Successful!", json.toString());
                   Intent i = new Intent(Profile.this,Searchview.class);
                   finish();
               startActivity(i);
                   return json.getString(TAG_MESSAGE);
           }else{
                   Log.d("Search Failure!", json.getString(TAG_MESSAGE));
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
               Toast.makeText(Profile.this, file_url, Toast.LENGTH_LONG).show();
           }

       }

	}

}

	


	
