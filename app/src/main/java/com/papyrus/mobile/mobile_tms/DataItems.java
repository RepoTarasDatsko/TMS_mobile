package com.papyrus.mobile.mobile_tms;

import android.os.Parcel;
import android.os.Parcelable;


public class DataItems implements Parcelable {
    private String mOrderData;
    private String mTimeDelivery;
    private String mOrderDesc;

    public DataItems(String sOrderData, String sOrderDesc, String sTimeDelivery) {
        this.mOrderData = sOrderData;
        this.mOrderDesc = sOrderDesc;
        this.mTimeDelivery = sTimeDelivery;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOrderData);
        dest.writeString(this.mOrderDesc);
        dest.writeString(this.mTimeDelivery);
    }

    protected DataItems(Parcel in) {
        this.mOrderData = in.readString();
        this.mOrderDesc = in.readString();
        this.mTimeDelivery = in.readString();
    }

    public static final Creator<DataItems> CREATOR = new Creator<DataItems>() {
        public DataItems createFromParcel(Parcel source) {
            return new DataItems(source);
        }
        public DataItems[] newArray(int size) {
            return new DataItems[size];
        }
    };


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //The following are just setter and getter methods
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getOrderData() {
        return mOrderData;
    }

    public void setOrderData(String sOrderData) {
        this.mOrderData = sOrderData;
    }

    public String getTimeDelivery() {
        return mTimeDelivery;
    }

    public void setTimeDelivery(String sTimeDelivery) {
        this.mTimeDelivery = sTimeDelivery;
    }

    public String getOrderDesc() {
        return mOrderDesc;
    }

    public void setOrderDesc(String sOrderDesc) {
        this.mOrderDesc = sOrderDesc;
    }

}
