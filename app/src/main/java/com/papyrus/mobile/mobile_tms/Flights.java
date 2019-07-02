package com.papyrus.mobile.mobile_tms;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Flights implements Parcelable {
    private String mFlightsNumber;
    private String mFlightsDate;
    private Boolean mSwap;
    private ArrayList<Order> mOrders = new ArrayList<>();

    public Flights(String sNumber, String sDate, Boolean swap, ArrayList<Order> orders) {
        this.mFlightsNumber = sNumber;
        this.mFlightsDate = sDate;
        this.mSwap = swap;
        this.mOrders = orders;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFlightsNumber);
        dest.writeString(this.mFlightsDate);
        dest.writeByte((byte) (this.mSwap ? 1 : 0));
        dest.writeTypedList(this.mOrders);
    }

    protected Flights(Parcel in) {
        this.mFlightsNumber = in.readString();
        this.mFlightsDate = in.readString();
        this.mSwap =  in.readByte() != 0;
        in.readTypedList(this.mOrders, Order.CREATOR);
    }

    public static final Creator<Flights> CREATOR = new Creator<Flights>() {
        public Flights createFromParcel(Parcel source) {
            return new Flights(source);
        }
        public Flights[] newArray(int size) {
            return new Flights[size];
        }
    };


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //The following are just setter and getter methods
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getmFlightsNumber() {
        return mFlightsNumber;
    }

    public void setFlightsNumber(String flightsNumber) {
        this.mFlightsNumber = flightsNumber;
    }

    public String getFlightsDate() {
        return mFlightsDate;
    }

    public void setFlightsDate(String flightsDate) {
        this.mFlightsDate = flightsDate;
    }

    public Boolean  getmSwap() {
        return mSwap;
    }

    public void setmSwap(Boolean swap) {
        this.mSwap = swap;
    }

    public ArrayList<Order> getOrders() {
        return mOrders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.mOrders = orders;
    }

}
