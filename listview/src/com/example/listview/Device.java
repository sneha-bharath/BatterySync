package com.example.listview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Device implements Comparator<Device> {
	public String username;
	public String name;
	public String type;
	public String battery;
	public List<Actv> myList;

	public Device()
		{
			// TODO Auto-generated constructor stub
		}

	public Device(String name, String uname, String type, String blevel)
		{
			this.name = name;
			this.username = uname;
			this.type= type;
			this.battery = blevel;
			this.myList = new  ArrayList<Actv>();
		}
	/*public Device(String name, String uname, String type, String blevel)
	{
		this.name = name;
		this.username = uname;
		this.type= type;
		this.battery = blevel;
	}*/

	@Override
	public String toString()
		{
			return this.name;
		}


	@Override
	public int compare(Device arg0, Device arg1) {
		int i = Integer.parseInt(arg0.battery);
		int j = Integer.parseInt(arg1.battery);
		return i-j;
	}
	public boolean equals(Object o){
	    if(Integer.parseInt(this.battery)==Integer.parseInt(((Device)o).battery))
	        return true;
	    else 
	        return false;
	}
}

