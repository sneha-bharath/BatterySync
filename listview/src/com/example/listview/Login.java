package com.example.listview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener
{
	public static final String PREFS_NAME = "batterysync";
	public EditText t3;
	public EditText t4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		t3 = (EditText) findViewById(R.id.editText15);
		t4 = (EditText) findViewById(R.id.editText16);
		Button b1 = (Button) findViewById(R.id.button15);
		b1.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		
		// TODO Auto-generated method stub
		if(t3.getText()!=null && t4.getText()!=null)
		{
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("login", true);
			 editor.putString("username", t3.getText().toString());
			 editor.putString("devicename", t4.getText().toString());
			 editor.commit();
			 Intent data = new Intent();
			 data.putExtra("usernam",t3.getText().toString() );
			 data.putExtra("devicenam",t4.getText().toString());
			 setResult(Activity.RESULT_OK,data);
			 this.finish();
		}
		
	}
}
