package com.example.listview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	static final int REQUEST_CODE = 1;
	private ActivityManager myActivityManager;
	public String battery_lvl = "0";
	private Handler handler = new Handler();
	private static  String username;
	private static String devicename;
	IntentFilter batterylevelfilter = null;
	BluetoothAdapter mBluetoothAdapter;
	public SharedPreferences settings;
	public TextView et7,et5;
	public static final String PREFS_NAME = "batterysync";
	int flag = 0;
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			int level = intent.getIntExtra("level", 0);
			battery_lvl = String.valueOf(level);
			handler.post( new Runnable() {

	            public void run() {
	                Toast.makeText(arg0,"Battery Level Changed to "+battery_lvl + "% ", Toast.LENGTH_SHORT).show();
	            }
	        });
		}
	};
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Looper.;
		
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREFS_NAME, 0);
		//SharedPreferences.Editor editor = settings.edit();
		//editor.clear();
		//editor.commit();
		Boolean val = settings.getBoolean("login",false);
	    if(val==false) 
	    {
	    	Intent intent1 = new Intent(MainActivity.this,Login.class);
			startActivityForResult(intent1,REQUEST_CODE);
	    }
		//SharedPreferences.Editor editor = settings.edit();
		// editor.putString("username", "poojan123");
		// editor.commit();
		batterylevelfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent curr = registerReceiver(null,batterylevelfilter);
		int currlevel = curr.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		battery_lvl = String.valueOf(currlevel);
		registerReceiver(this.mBatInfoReceiver, batterylevelfilter);
		System.out.println("This is first battery level "+ battery_lvl +"\n");
		
		// Set the View layer
		setContentView(R.layout.activity_main);
		et7 =  (TextView) findViewById(R.id.editText11);
		et5 =  (TextView) findViewById(R.id.editText12);
		TextView tv23 = (TextView) findViewById(R.id.textView22);
		tv23.setText("Hi,"+ "You have registered this device as");
		//et7.setText(s.getString("username", "poojan123"));
		//et5.setText(s.getString("devicename", "emulator"));
		Button b1 = (Button) findViewById(R.id.button1);
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//if (et7.getText() != null )
				//{
					Intent intent = new Intent(MainActivity.this,ViewDevices.class);
					intent.putExtra("user-name", et7.getText().toString());
					startActivity(intent);
				//}	
			}
		});
	    //tv.setText("Welcome poojan123. These are your devices");
		myActivityManager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);
		
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	username = data.getExtras().getString("usernam");
	        	et7.setText(data.getExtras().getString("usernam"));
	        	devicename = data.getExtras().getString("devicenam");
	        	et5.setText(data.getExtras().getString("devicenam")); 
	        	Timer timer = new Timer();
	    		TimerTask update = new UpdateBattery(this.getBaseContext());
	    		timer.scheduleAtFixedRate(update, 0, 30000);//1000/FPS);
	    		new setBluetooth().execute();
	        }
		}
	}
	public void onResume()
	{
		super.onResume();
		et7.setText(username);
		et5.setText(devicename);
	}
	
	public void  PostRequest(int t, Context ctxt)
	{
		while(flag == 0);
		
		InputStream is = null;
		String json = "";
		JSONObject jarray = null;
		HttpClient http1 = null;
		HttpPost method = null;
		String tmp ="";
		String usernm = settings.getString("username", username);
		String devicenm = settings.getString("devicename", devicename);
		tmp = String.valueOf(battery_lvl);
		try{
			myActivityManager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);

			String services = "";
			//ArrayList<String> listEntries = new ArrayList<String>();
			myActivityManager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);

			/* String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			 if(provider.contains("gps"))
				 System.out.println("gps enabled");
			 else if(!provider.contains("gps"))
				 System.out.println("gps disabled");
			 
			final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            sendBroadcast(poke);
			*/
			
			WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			if(wifi.isWifiEnabled())
				services = services + "Wi-Fi,";
			
			// Check for GPS
			LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			System.out.println("Gps enalbed = " + lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER));
			if(lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
				services = services + "GPS,";
			
			// Check for Bluetooth
			// BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			//mBluetoothAdapter.disable();
			if(mBluetoothAdapter == null)
				System.out.println("Bluetooth not supported");
			else if ((mBluetoothAdapter.isEnabled()))
				services = services + "Bluetooth,";
				
			
			List<ActivityManager.RunningAppProcessInfo> allTasks = myActivityManager.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo aTask : allTasks) {
				//listEntries.add(aTask.baseActivity.getClassName() + " ID="+ aTask.id);
				services = services + aTask.processName + ",";// + "-" + aTask.pid + ",";
			}

			System.out.println("Before --- Services " + services);
			
			/*Intent intent2 = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent2.putExtra("enabled", true);
			sendBroadcast(intent2);
			*/
			
            
			/*String services2 = "";
			List<ActivityManager.RunningServiceInfo> allTasks2 = myActivityManager.getRunningServices(100);
			for (ActivityManager.RunningServiceInfo aTask : allTasks2) {
				//listEntries.add(aTask.baseActivity.getClassName() + " ID="+ aTask.id);
				services2 = services2 + aTask.service.getClassName() + ",";
			}
			System.out.println("After --- Services " + services2);
			*/
			
			http1 = AndroidHttpClient.newInstance("Battery1");
			method = new HttpPost("http://128.61.127.150/android_connect/add_device.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("username", usernm));
			nameValuePairs.add(new BasicNameValuePair("device_id", devicenm));
			nameValuePairs.add(new BasicNameValuePair("type", "phone"));
			nameValuePairs.add(new BasicNameValuePair("battery_level", tmp));
			nameValuePairs.add(new BasicNameValuePair("services", services));
			method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			

			HttpResponse response = http1.execute(method);
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
				int entries = jarray.getInt("entries");
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
					
					if(Integer.parseInt(level) < 50)
					{
						System.out.println("====Below Threshold====");
						/*
						Intent resultIntent = new Intent(ctxt, MainActivity.class);
						
						PendingIntent resultPendingIntent =
						        stackBuilder.getPendingIntent(
						            0,
						            PendingIntent.FLAG_UPDATE_CURRENT
						        );
						mBuilder.setContentIntent(resultPendingIntent);

						NotificationManager mNotificationManager =
							    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							// mId allows you to update the notification later on.
							int mId=0;
							mNotificationManager.notify(mId, mBuilder.build());
					}*/
						Notification noti = new Notification(R.drawable.ic_launcher,"Battery Low Notification",System.currentTimeMillis());//"Battery of " + d_name + " is low!");
						   
						/*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
						// Adds the back stack for the Intent (but not the Intent itself)
						//stackBuilder.addParentStack(ResultActivity.class);
						// Adds the Intent that starts the Activity to the top of the stack
						//stackBuilder.addNextIntent(resultIntent);
						PendingIntent resultPendingIntent =
						        stackBuilder.getPendingIntent(
						            0,
						            PendingIntent.FLAG_UPDATE_CURRENT
						        );
						noti.setContentIntent(resultPendingIntent);
						*/
						Intent resultIntent = new Intent(Intent.ACTION_MAIN);
						resultIntent.setClass(getApplicationContext(), ViewDevices.class);
						
//						TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
						//stackBuilder.addParentStack(ResultActivity.class);
	//					stackBuilder.addNextIntent(resultIntent);
						PendingIntent intent =
						          PendingIntent.getActivity(ctxt, 0,
						          resultIntent,  PendingIntent.FLAG_UPDATE_CURRENT | Notification.FLAG_AUTO_CANCEL);
						
						 NotificationManager notificationManager = 
						   (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

						 noti.setLatestEventInfo(ctxt, "Battery Low Notification", "Battery of one/more devices is quite low!", intent);
						 // Hide the notification after its selected
						 noti.flags |= Notification.FLAG_AUTO_CANCEL;

						 notificationManager.notify("Notification", 123, noti);//.getNotification());
					}

				}	
				String dummy_str = jarray.getString("activities");
				System.out.println("Response " + dummy_str);
				//String dummy_str = "com.android.calendar,android.com.browse";
				String[] close_app = dummy_str.split(",");
				System.out.println("Length is " + close_app.length);
				for(int j=0; j<(close_app.length); j = j+1)
				{
					System.out.println(close_app[j]);
					
					if(close_app[j].equals("Wi-Fi"))
					{
						wifi.setWifiEnabled(false);
					}
					
					else if(close_app[j].equals("Bluetooth"))
					{
						mBluetoothAdapter.disable();
					}
					
					else if (close_app[j].equals("GPS"))
					{
						System.out.println("GPS turning off------");
						Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
						intent.putExtra("enabled", false);
						sendBroadcast(intent);
					}
					
					else
					{
						int pid = android.os.Process.getUidForName(close_app[j]);
						android.os.Process.killProcess(pid);
						myActivityManager.killBackgroundProcesses(close_app[j]);
					}
				}
                
			} catch (Exception e) {
				Log.e("Buffer Error", "In PostRequest Error converting result " + e.toString());
			}
		}

		catch(Exception e)
		{
			Log.i("info", "exception");
			Log.e("ERROR TAG", "Here is the error", e);
		}
		 
		if (http1 != null && http1.getConnectionManager() != null) 
    	 {
		     method.abort();
             //http1.getConnectionManager().shutdown();
             ((AndroidHttpClient) http1).close();
             http1 = null;
             System.out.println("Shutting off http");
    	 }
	

	}
	
	private class setBluetooth extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls)	{
			Looper.prepare();
			System.out.println("Here in new thread for bluetooth");
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			flag = 1;
			
			long dummy = 0;
			return dummy;
		}
	}

	private class UpdateBattery extends TimerTask {
		int temp=10;
		public Context ctxt;
		
		public UpdateBattery(Context contextin)
		{
			ctxt = contextin;
			//notificationManager = 
				//	   (NotificationManager) ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		public void run() {
			//calculate the new position of myBall
			//System.out.print(temp);
			System.out.println("Timer!!!!");
			PostRequest(temp, ctxt); 
			
			//PostRequest();
		}
	}
	

}