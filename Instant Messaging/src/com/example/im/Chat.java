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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
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

public class Chat extends ListActivity implements OnClickListener{
	
	
	TextView tfrom_user,tto_user;
	EditText echat_mess;
	Button bsend;
	
	private ProgressDialog pDialog;
	final Handler handler = new Handler();
	Runnable getData,getnotify;
	
	JSONParser jsonParser = new JSONParser();
	private static final String CHAT_URL = "http://87.203.112.158:5050/IM/chat.php";
	private static final String LISTCHAT_URL = "http://87.203.112.158:5050/IM/list_chat.php";
	private static final String NOTIFICATIONS_URL = "http://87.203.112.158:5050/IM/notifications.php";
	private static final String GETNOTIFICATIONS_URL = "http://87.203.112.158:5050/IM/getnotifications.php";
	private static final String TAG_UFROM = "ufrom";
	private static final String TAG_TIME = "time";
	private static final String TAG_CHAT = "chat";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_NOTIFYUSERS = "notify_users";
	private static final String TAG_NOTIFY = "notify";
	
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
		setContentView(R.layout.chat);
		tfrom_user = (TextView) findViewById(R.id.from_user);
		tto_user = (TextView) findViewById(R.id.to_user);
		echat_mess = (EditText) findViewById(R.id.chat_mess);
		bsend = (Button) findViewById(R.id.send);
		
		
		bsend.setOnClickListener(this);
		
		SharedPreferences us = PreferenceManager.getDefaultSharedPreferences(Chat.this);
        String username = us.getString("user", "name");
        tto_user.setText(username);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Chat.this);
        String uname = sp.getString("username", "anon");
        tfrom_user.setText(uname);
        tfrom_user.setVisibility(View.GONE);
		

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// loading the Friends via AsyncTask
		
		handler.postDelayed(getData = new Runnable() {
  @Override
  public void run() {
      // TODO Auto-generated method stub
	  
		new listchat().execute();
		
		handler.postDelayed( this,  1000 );
          }
		
		},1000 );
		
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
		handler.removeCallbacks(getData);
		handler.removeCallbacks(getnotify);
	}
	
	public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.send:
        	new send().execute();
        	
            break;
        default:
            break;
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
       pDialog = new ProgressDialog(Chat.this);
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
       String ufrom = tfrom_user.getText().toString();
       String uto =  tto_user.getText().toString();
       String emess = echat_mess.getText().toString();
       
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
               params.add(new BasicNameValuePair("ufrom", ufrom));
               params.add(new BasicNameValuePair("uto", uto));
               params.add(new BasicNameValuePair("emess", emess));
               
               Log.d("request!", "starting");
           // getting product details by making HTTP request
          JSONObject json = jsonParser.makeHttpRequest(
                  CHAT_URL, "POST", params);
          JSONObject jsson = jsonParser.makeHttpRequest(
                  NOTIFICATIONS_URL, "POST", params);

           // check your log for json response
               Log.d("Send attempt", json.toString());

               // json success tag
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {    
            	   
                   Log.d("Send Successful!", json.toString());
                   return json.getString(TAG_MESSAGE);
           }else{
                   Log.d("Send Failure!", json.getString(TAG_MESSAGE));
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
               Toast.makeText(Chat.this, file_url, Toast.LENGTH_LONG).show();
               clear();
              new listchat().execute();
           }

       }


}
	public void clear(){
 	   echat_mess.setText("");
 	  
 	   
    }
	
	public void updateJSONdata() {
		
		
		       
		       String tfrom = tfrom_user.getText().toString();
		       String tto =  tto_user.getText().toString();
		       
		       
		       
		      
		           // Building Parameters
		           List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		               params.add(new BasicNameValuePair("ufrom", tfrom));
		               params.add(new BasicNameValuePair("uto", tto));
		              
		               
		               Log.d("request!", "starting");
		           // getting product details by making HTTP request
		         
		          JSONObject jsson = jsonParser.makeHttpRequest(
		                  LISTCHAT_URL, "GET", params);
		          

		           // check your log for json response
		              

		               // json success tag
		              
		              
		           

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();
		
		
		
		JSONParser jParser = new JSONParser();
		
		JSONObject json = jParser.getJSONFromUrl(LISTCHAT_URL);

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
				//String title = c.getString(TAG_TITLE);
				String ufrom = c.getString(TAG_UFROM);
				String chat = c.getString(TAG_CHAT);
				String time = c.getString(TAG_TIME);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				//map.put(TAG_TITLE, title);
				//map.put(TAG_MESSAGE, content);
				map.put(TAG_UFROM, ufrom);
				map.put(TAG_CHAT, chat);
				map.put(TAG_TIME, time);
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
		//use our chat_post xml template for each item in our list,
		//and place the appropriate info from the list to the
		//correct GUI id.  Order is important here.
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.chat_post, new String[] {TAG_UFROM,TAG_CHAT,TAG_TIME }, new int[] {R.id.ufrom,R.id.chat,R.id.time});

		
		setListAdapter(adapter);
		
		// Optional: when the user clicks a list item we 
		//could do something.  However, we will choose
		//to do nothing...
		ListView lv = getListView();
		
		lv.setSelection(lv.getAdapter().getCount()-1);
		lv.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
			
			
		});
	}
	
	
	class notifications extends AsyncTask<String, String, String> {
		 
     

   @SuppressWarnings("deprecation")
@Override
   protected String doInBackground(String... args) {
       // TODO Auto-generated method stub
        // Check for success tag
      
       String ufrom = tfrom_user.getText().toString();
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
           		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Chat.this);
                Editor edit = sp.edit();
                edit.putString("username", ufrom);
                edit.commit();
           		Intent notificationIntent = new Intent(Chat.this,Tab.class);
           		Bundle b=new Bundle();
           		b.putString("condition","Third Tab");
           		notificationIntent.putExtras(b);
           		PendingIntent contentIntent = PendingIntent.getActivity(Chat.this, i,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
           		//n.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
           			    //+ "://" + getPackageName() + "/raw/new_message");
           		n.flags |= Notification.FLAG_AUTO_CANCEL;
           		CharSequence contentText = "New message from " +notifyUsers;
           		CharSequence contentTitle = notifyUsers;
           		n.setLatestEventInfo(Chat.this, contentTitle, contentText, contentIntent);
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
	               Toast.makeText(Chat.this, file_url, Toast.LENGTH_LONG).show();
	           }

	       }

	   
	public class listchat extends AsyncTask<Void, Void, Boolean> {

			
			/*protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Chat.this);
				pDialog.setMessage("Loading chat...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}*/

			
			protected Boolean doInBackground(Void... arg0) {
			
                updateJSONdata();
				return null;

			}

		
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				
					//pDialog.dismiss();
				updateList();
         
			}
		}
}
