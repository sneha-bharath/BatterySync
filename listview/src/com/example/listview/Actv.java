package com.example.listview;

import android.os.Parcel;
import android.os.Parcelable;

public class Actv implements Parcelable{
	public String act_name;
	public int flag;

	public Actv(){}
	
	public Actv(String s,String f)
	{
		this.act_name = s;
		this.flag = Integer.parseInt(f);
	}

    public Actv(Parcel in)
    {
        readFromParcel(in);
    }

    public String getStrValue()
    {
        return this.act_name;
    }

    /**
     * Standard setter
     *
     * @param strValue
     */

    public void setStrValue(String strValue)
    {
        this.act_name = strValue;
    }


    /**
     * Standard getter
     *
     * @return intValue
     */
    public Integer getIntValue()
    {
        return this.flag;
    }

    /**
     * Standard setter
     *
     * @param strValue
     */
    public void setIntValue(Integer intValue)
    {
        this.flag = intValue;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		  dest.writeString(this.act_name);
	        dest.writeInt(this.flag);
		
	}
	  public void readFromParcel(Parcel in)
	    {
	        // We just need to read back each
	        // field in the order that it was
	        // written to the parcel

	        this.act_name = in.readString();
	        this.flag = in.readInt();
	    }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	    {
	        @Override
	        public Actv createFromParcel(Parcel in)
	        {
	            return new Actv(in);
	        }

	        @Override
	        public Actv[] newArray(int size)
	        {
	            return new Actv[size];
	        }
	    };
}
