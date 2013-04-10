package com.example.listview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ActvAdapter extends ArrayAdapter<Actv> {
	private Context context;
	private String devicenm;
	public  ProgressDialog pDialog;
	HttpClient http2 = null;
	TextView tv1;
	ToggleButton tg1;
	public int s=0;
	public AlertDialog alertdialog;
	private List<Actv> activ = new ArrayList<Actv>();
	public ActvAdapter(Context context, int textViewResourceId, List<Actv> objects, String dname, AlertDialog a) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.activ = objects;
		this.devicenm = dname;
		this.alertdialog = a;
		// TODO Auto-generated constructor stub
	}
	public int getCount() {
		return this.activ.size();
	}

	public Actv getItem(int index){
		return this.activ.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		final Actv activty = getItem(position);
		if (row == null) {
			// ROW INFLATION
			
			String tag = "activiyt list adapter";
			Log.d(tag , "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.simplerow, parent, false);
			//Actv viewholder = new Actv();
			Log.d(tag, "Successfully completed XML Row Inflation!");
			if(activty.flag==1)
				row.setVisibility(View.GONE);
			else
				row.setVisibility(View.VISIBLE);
		}

		// Get item
		
		final String temp = activty.act_name;
		// Get reference to ImageView 
		tv1 = (TextView) row.findViewById(R.id.rowTextView);
		tg1 = (ToggleButton) row.findViewById(R.id.tgbutton1);
		tg1.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) 
			{
				
				
				String p = "Sorry, couldn't convey your choice. Check network connectivity"; 
				alertdialog.setTitle("Turning off this activity...");
				alertdialog.setMessage("Are you sure?");
				alertdialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String t= "Your selection has been saved. It will be performed shortly";
					new UpdateAct().execute(temp,"1");
					ActvAdapter.this.remove(activty);
					ActvAdapter.this.notifyDataSetChanged();
					Toast.makeText(getContext(), t, 6000).show();
					s=1;
				}
				});
				alertdialog.show();
				if(s==0)
					ActvAdapter.this.notifyDataSetChanged();				
				
			
			}
		});
		tg1.setEnabled(true);
	    tv1.setText(activty.act_name);
	    if(activty.flag==0)
	    tg1.setChecked(true);
	    else
	    tg1.setChecked(false);
		return row;
	}
	
	public void  PostRequest(String t,String k)
	{
		InputStream is = null;
		String json = "";
		JSONObject jarray = null;
		
		HttpPost method = null;
		String tmp ="";
		//tmp = String.valueOf(battery_lvl);
		try{
			
			
			http2 = AndroidHttpClient.newInstance("Battery2");
			method = new HttpPost("http://128.61.127.150/android_connect/update_act.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			//nameValuePairs.add(new BasicNameValuePair("username", "poojan123"));
			nameValuePairs.add(new BasicNameValuePair("device_id", this.devicenm));
		//	nameValuePairs.add(new BasicNameValuePair("type", "phone"));
			//nameValuePairs.add(new BasicNameValuePair("battery_level", tmp));
			nameValuePairs.add(new BasicNameValuePair("act_name", t));
			nameValuePairs.add(new BasicNameValuePair("flag", k));
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			

			HttpResponse response = http2.execute(method);
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					System.out.println("Inside the while loop - post");
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				System.out.print(json);
				jarray = new JSONObject(json);
				/*int entries = jarray.getInt("entries");
				String const_str = "details.";

				for(int i=0; i<entries; i++)
				{
					String record = const_str+i;
					//String detail = jarray.getString(record);
					JSONArray detailabc = jarray.getJSONArray(record);
					JSONObject detail = detailabc.getJSONObject(0);

					String d_name = detail.getString("device");
					String level = detail.getString("battery");
					System.out.println(d_name + " " + level);
					
					
					
				}	*/			
                
			} catch (Exception e) {
				Log.e("Buffer Error", "In PostRequest Error converting result " + e.toString());
			}
		}

		catch(Exception e)
		{
			Log.i("info", "exception");
			Log.e("ERROR TAG", "Here is the error", e);
		}
		if (http2 != null && http2.getConnectionManager() != null) 
   	 {
   		// request.abort();
            http2.getConnectionManager().shutdown();
            ((AndroidHttpClient) http2).close();
            http2 = null;
            System.out.println("Shutting off http Post update");
   	 }

	}
	private class UpdateAct extends AsyncTask<String, Integer, Long> {
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pDialog = new ProgressDialog(this.context);
            //pDialog.setMessage("Sending update request. Please wait...");
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(false);
            //pDialog.show();
        }
		@Override
		protected Long doInBackground(String... temp) {
			// TODO Auto-generated method stub
			System.out.println("Here in new thread for updating activity");
			String tm = temp[0];
			String p = temp[1];
			
			PostRequest(tm,p);
			Long dummy = (long) 0;
			return dummy;
		}

		
        protected void onPostExecute(Long dummy) {
        	 
        	System.out.println("In post execute");
        	System.out.print(dummy);
            // dismiss the dialog after getting all products
        	 //pDialog.dismiss();
        	 if (http2 != null && http2.getConnectionManager() != null) 
        	 {
        		// request.abort();
                 http2.getConnectionManager().shutdown();
                 ((AndroidHttpClient) http2).close();
                 http2 = null;
                 System.out.println("Shutting off http Post update");
        	 }
}
	}
}
		