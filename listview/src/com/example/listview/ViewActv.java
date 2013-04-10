package com.example.listview;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ViewActv extends Activity {
	 //private ArrayAdapter<String> listAdapter ; 
	public static final String PREFS_NAME = "batterysync";
	@Override
	
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_list);
		
		String device_name = getIntent().getExtras().getString("device_name");
		
		ListView l2 = (ListView) findViewById(R.id.listView2);
		ArrayList<Actv> actList = new ArrayList<Actv>(); 
		AlertDialog a1 = new AlertDialog.Builder(this).create();
		actList = getIntent().getExtras().getParcelableArrayList("listofactv");
		ActvAdapter adapter1 = new ActvAdapter(
				getApplicationContext(), R.layout.simplerow, actList, device_name,a1);
		if(actList!=null)
		{
			System.out.println("Hello, there are items \n");
			for(Actv a:actList)
			{
				System.out.print(a.act_name+" ");
			}
			
		}
		
		//listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, actList);  
		View header1 = (View)getLayoutInflater().inflate(R.layout.listview_header, null);
		TextView t1 = (TextView) header1.findViewById(R.id.txtheader);
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		String user_name1 = prefs.getString("username", "something");
		t1.setText(user_name1+", your activities for "+device_name+ " are");
		t1.setText("Your activities for "+device_name+ " are");
		l2.addHeaderView(header1);
		l2.setAdapter(adapter1);
		//tgbttn1 = findViewById(R.id.) 
}
	
}
