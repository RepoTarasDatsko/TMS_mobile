package com.papyrus.mobile.mobile_tms;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;


public class Order implements Parcelable {

        private String mCustomer;
        private ArrayList<OrdersNumber> mOrdersNumber = new ArrayList<>();
        private String mAdress;
        private String mlongitude;
        private String mlatitude;
        private int mNumberOfPosition;
        private String mNumberOfSeats;
        private String mTimeCalculated;
        private String mCheckOutTime;
        private Boolean mPassed;
        private Boolean mOperation;
        private String mCondition;
        private String mTimeDelivery;


        public Order(String customer,  ArrayList<OrdersNumber> ordersNumber, String adress, String longitude, String latitud, int numberOfPosition, String numberOfSeats,  String timeCalculated, String checkOutTime, Boolean passed, Boolean operation,  String condition, String timeDelivery) {
            this.mCustomer = customer;
            this.mOrdersNumber = ordersNumber;
            this.mAdress = adress;
            this.mlongitude = longitude;
            this.mlatitude = latitud;
            this.mNumberOfPosition = numberOfPosition;
            this.mNumberOfSeats = numberOfSeats;
            this.mTimeCalculated = timeCalculated;
            this.mCheckOutTime = checkOutTime;
            this.mPassed = passed;
            this.mOperation = operation;
            this.mCondition = condition;
            this.mTimeDelivery = timeDelivery;

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mCustomer);
            dest.writeTypedList(this.mOrdersNumber);
            dest.writeString(this.mAdress);
            dest.writeString(this.mlongitude);
            dest.writeString(this.mlatitude);
            dest.writeInt(this.mNumberOfPosition);
            dest.writeString(this.mNumberOfSeats);
            dest.writeString(this.mTimeCalculated);
            dest.writeString(this.mCheckOutTime);
            dest.writeByte((byte) (this.mPassed ? 1 : 0));
            dest.writeByte((byte) (this.mOperation ? 1 : 0));
            dest.writeString(this.mCondition);
            dest.writeString(this.mTimeDelivery);

        }

        protected Order(Parcel in) {
            this.mCustomer = in.readString();
            in.readTypedList(this.mOrdersNumber, OrdersNumber.CREATOR);
            this.mAdress = in.readString();
            this.mlongitude = in.readString();
            this.mlatitude = in.readString();
            this.mNumberOfPosition = in.readInt();
            this.mNumberOfSeats = in.readString();
            this.mTimeCalculated = in.readString();
            this.mCheckOutTime = in.readString();
            this.mPassed =  in.readByte() != 0;
            this.mOperation =  in.readByte() != 0;
            this.mCondition = in.readString();
            this.mTimeDelivery = in.readString();

        }

        public static final Creator<Order> CREATOR = new Creator<Order>() {
            public Order createFromParcel(Parcel source) {
                return new Order(source);
            }
            public Order[] newArray(int size) {
                return new Order[size];
            }
        };

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //The following are just setter and getter methods
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        public String getmCustomer() {
            return mCustomer;
        }

        public void setmCustomer(String mCustomer) {
            this.mCustomer = mCustomer;
        }

        public ArrayList<OrdersNumber> getOrders() {
        return mOrdersNumber;
    }

        public void setOrders(ArrayList<OrdersNumber> ordersNumber) {
        this.mOrdersNumber = ordersNumber;
        }

        public String getmAdress() {
            return mAdress;
        }

        public void setmAdress(String mAdress) {
            this.mAdress = mAdress;
        }

        public String getmlongitude() {
            return mlongitude;
        }

        public void setmlongitude(String mlongitude) {
            this.mlongitude = mlongitude;
        }

        public String getmlatitude() {
            return mlatitude;
        }

        public void setmlatitude(String mlatitude) {
            this.mlatitude = mlatitude;
        }

        public int getmNumberOfPosition() {
        return mNumberOfPosition;
    }

        public void setmNumberOfPosition(int mNumberOfPosition) {
            this.mNumberOfPosition = mNumberOfPosition;
        }

        public Boolean getmOperation() {
        return mOperation;
    }

        public void setmOperation(Boolean operation) {
        this.mOperation = operation;
    }

        public Boolean  getmPassed() {
            return mPassed;
        }

        public void setmPassed(Boolean passed) {
        this.mPassed = passed;
    }

        public String getmCondition() {
        return mCondition;
        }

        public void setmCondition(String mCondition) {
            this.mCondition = mCondition;
        }

        public String getmTimeCalculated() {
        return mTimeCalculated;
        }

        public void setmTimeCalculated(String mTimeCalculated) {
            this.mTimeCalculated = mTimeCalculated;
        }

        public String getmCheckOutTime() {
            return mCheckOutTime;
        }

        public void setmCheckOutTime(String checkOutTime) {
            this.mCheckOutTime = checkOutTime;
        }

        public String getmNumberOfSeats() {
            return mNumberOfSeats;
        }

        public void setmNumberOfSeats(String mNumberOfSeats) {
            this.mNumberOfSeats = mNumberOfSeats;
        }

        public String getmTimeDelivery() {
        return mTimeDelivery;
    }

        public void setmTimeDelivery(String timeDelivery) {
        this.mTimeDelivery = timeDelivery;
    }

}
