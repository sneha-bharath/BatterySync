package com.example.listview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {
	private static final String tag = "DeviceArrayAdapter";
	private static final String ASSETS_DIR = "images/";
	private static final String FILE_EXTENSION= ".png";
	private static final String FILE_EXT= ".jpg";
	private Context context;

	private ImageView Icon;
	private ImageView Bcon;
	private TextView  Name;
	private TextView  Battery;
	private List<Device> devices = new ArrayList<Device>();

	public DeviceArrayAdapter(Context context, int textViewResourceId,
			List<Device> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.devices = objects;
		// TODO Auto-generated constructor stub
	}
	public int getCount() {
		return this.devices.size();
	}

	public Device getItem(int index) {
		return this.devices.get(index);
	}
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			Log.d(tag, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.second, parent, false);
			Log.d(tag, "Successfully completed XML Row Inflation!");
		}

		// Get item
		Device currdevice = getItem(position);
		
		// Get reference to ImageView 
		Icon = (ImageView) row.findViewById(R.id.icon);
		Bcon = (ImageView) row.findViewById(R.id.icon2);
		
		// Get reference to TextView - country_name
		Name = (TextView) row.findViewById(R.id.name);
		
		// Get reference to TextView - country_abbrev
		Battery = (TextView) row.findViewById(R.id.level);

		//Set country name
		Name.setText(currdevice.name);
		String imgFilePath = ASSETS_DIR + currdevice.type + FILE_EXTENSION ;
		String imgFileP = null;
		Log.d("path",imgFilePath);
		int t = Integer.parseInt(currdevice.battery);
		if(t==100)
			imgFileP = ASSETS_DIR + "100%" + FILE_EXT ;
		else if(( t<100) && (t>=95) )
			imgFileP = ASSETS_DIR + "95%" + FILE_EXT;
		else if(( t<95) && (t>=90) )
			imgFileP = ASSETS_DIR + "90%" + FILE_EXT;
		else if(( t<90) && (t>=80) )
			imgFileP = ASSETS_DIR + "80%" + FILE_EXT;
		else if(( t<80) && (t>=70))
			imgFileP = ASSETS_DIR + "70%" + FILE_EXT;
		else if(( t<70) && (t>=60))
			imgFileP = ASSETS_DIR + "60%" + FILE_EXT;
		else if(( t<60) && (t>=50))
			imgFileP = ASSETS_DIR + "50%" + FILE_EXT;
		else if(( t<50) && (t>=40))
			imgFileP = ASSETS_DIR + "40%" + FILE_EXT;
		else if(( t<40) && (t>=30))
			imgFileP = ASSETS_DIR + "30%" + FILE_EXT;
		else if(( t<30) && (t>=25))
			imgFileP = ASSETS_DIR + "25%" + FILE_EXT;
		else if(( t<25) && (t>=20))
			imgFileP = ASSETS_DIR + "20%" + FILE_EXT;
		else if(( t<20) && (t>=10))
			imgFileP = ASSETS_DIR + "10%" + FILE_EXT;
		else if(( t<10) && (t>=5))
			imgFileP = ASSETS_DIR + "5%" + FILE_EXT;
		else
			imgFileP = ASSETS_DIR + "0%" + FILE_EXT;
		
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(this.context.getResources().getAssets()
					.open(imgFilePath));
			Icon.setImageBitmap(bitmap);
			bitmap = BitmapFactory.decodeStream(this.context.getResources().getAssets()
					.open(imgFileP));
			Bcon.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set country abbreviation
		Battery.setText(currdevice.battery +"%");
		return row;
		
	}
	
}
