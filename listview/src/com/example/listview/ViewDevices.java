package com.example.listview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ViewDevices extends Activity 
{
	private List<Device> deviceList= new ArrayList<Device>();
	public static final String PREFS_NAME = "batterysync";
	HttpClient http = null;
	private ProgressDialog pDialog;
	public String user_name;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		user_name = settings.getString("username", "");
		//user_name = getIntent().getExtras().getString("user-name");
		
	    
		new LoadDevices().execute();
	}
	public void sendrequest()
	{
		InputStream is = null;
		String json = "";
		JSONObject jarray = null;
		try{
			http = AndroidHttpClient.newInstance("Battery");
			HttpGet request = new HttpGet();
			request.setURI(new URI("http://128.61.127.150/android_connect/view_all.php?username="+user_name));
			HttpResponse response = http.execute(request);
			HttpEntity httpEntity = response.getEntity();

			is = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					System.out.println("Inside the while loop");
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				jarray = new JSONObject(json);
				int entries = jarray.getInt("entries");
				String const_str = "details.";
				String const_str1 = "activities.";
				

				for(int i=0; i<entries; i++)
				{
					String record = const_str+i;
					//String detail = jarray.getString(record);
					JSONArray detailabc = jarray.getJSONArray(record);
					JSONObject detail = detailabc.getJSONObject(0);

					String user = detail.getString("user");
					String d_name = detail.getString("device");
					String type = detail.getString("type");
					String level = detail.getString("battery");
					int entr = detail.getInt("numofact");
					String a_names[] = new String[entr];
					System.out.println(user + " " + d_name + " " + level+ " "+ type);
					Device d1 = new Device(d_name,user,type,level);
					for(int j=0; j<entr; j++)
					{
					String rec = const_str1+j;
					JSONArray activabc = detail.getJSONArray(rec);
					JSONObject activ = activabc.getJSONObject(0);
					String act_name = activ.getString("name");
					String fl = activ.getString("flag");
					//a_names[j] = act_name;
					System.out.println(act_name + "  and ");
					if(Integer.parseInt(fl) == 0)
					{
						Actv a = new Actv(act_name,fl);
					    d1.myList.add(a);
					}
					
					}
					if(d1.myList.isEmpty())
					{
						System.out.println("Oops, your device has no activities");
					}
					deviceList.add(d1);
					//HashMap<String,String> hash = new HashMap<String, String>();
					//hash.put("device",d_name);
					//hash.put("battery",level);
					//deviceList.add(hash);
				}

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

		}

		catch(Exception e)
		{
			Log.i("info", "exception");
			Log.e("ERROR TAG", "Here is the error", e);
		}
	}

	private class LoadDevices extends AsyncTask<URL, Integer, Long> {
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewDevices.this);
            pDialog.setMessage("Loading your devices. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		protected Long doInBackground(URL... urls)	{
			System.out.println("Here in new thread");
			long dummy = 0;
			sendrequest();

			return dummy;
		}
	
		@Override
        protected void onPostExecute(Long dummy) {
        	 
        	System.out.println("In post execute");
        	System.out.print(dummy);
            // dismiss the dialog after getting all products
        	 pDialog.dismiss();
        	 Device d = deviceList.get(deviceList.size()-1);
        	 Collections.sort(deviceList,(Comparator <Device>)d);
        	 if (http != null && http.getConnectionManager() != null) 
        	 {
        		// request.abort();
                 http.getConnectionManager().shutdown();
                 ((AndroidHttpClient) http).close();
                 http = null;
                 System.out.println("Shutting off http GET");
        	 }
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	// Create a customized ArrayAdapter
            		DeviceArrayAdapter adapter = new DeviceArrayAdapter(
            				getApplicationContext(), R.layout.second, deviceList);
            		
            		// Get reference to ListView holder
            		final ListView lv = (ListView) ViewDevices.this.findViewById(R.id.countryLV);
            		
            		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
            		TextView tv = (TextView) header.findViewById(R.id.txtHeader);
            		 tv.setText("Welcome "+ user_name + " ! Your devices: ");
                    lv.addHeaderView(header);
            		// Set the ListView adapter
            		lv.setAdapter(adapter);
            		lv.setClickable(true);
                    lv.setOnItemClickListener(new OnItemClickListener()
                    {
                    	
                    
						@Override
						public void onItemClick(AdapterView<?> arg0, View v,
								int position, long arg3) {
							    Intent myIntent = new Intent(v.getContext(), ViewActv.class);
					            //startActivityForResult(myIntent, 0);
					            Device selDev= (Device) lv.getItemAtPosition(position);
					            System.out.println("Got device at "+ position +" which has name = "+ selDev.name);
					            for(Actv a:selDev.myList)
					            {
					            System.out.print(a.act_name+"\n");	
					            }
					            myIntent.putParcelableArrayListExtra("listofactv",(ArrayList<? extends Parcelable>) selDev.myList);
					            myIntent.putExtra("device_name", selDev.name);
					            startActivity(myIntent);
						}
                    	
					
                
            });
 
        }

            });
		}
	}
}