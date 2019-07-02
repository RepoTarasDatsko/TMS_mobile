package com.papyrus.mobile.mobile_tms;

import android.os.Parcel;
import android.os.Parcelable;


public class OrdersNumber implements Parcelable {

        private String mNumber;
        private String mDate;
        private String mNumberOperation;
        private String mNotation;
        private String mManager;
        private String mTypeOfPayment;
        private String mContactPerson;
        private String mКindOf;
        private String mWarehouse;


        public OrdersNumber(String number, String date, String numberOperation, String notation, String manager, String typeOfPayment, String contactPerson, String kindOf, String warehouse) {
            this.mNumber = number;
            this.mDate = date;
            this.mNumberOperation = numberOperation;
            this.mNotation = notation;
            this.mManager = manager;
            this.mTypeOfPayment = typeOfPayment;
            this.mContactPerson = contactPerson;
            this.mКindOf = kindOf;
            this.mWarehouse = warehouse;

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mNumber);
            dest.writeString(this.mDate);
            dest.writeString(this.mNumberOperation);
            dest.writeString(this.mNotation);
            dest.writeString(this.mManager);
            dest.writeString(this.mTypeOfPayment);
            dest.writeString(this.mContactPerson);
            dest.writeString(this.mКindOf);
            dest.writeString(this.mWarehouse);
        }

        protected OrdersNumber(Parcel in) {
            this.mNumber = in.readString();
            this.mDate = in.readString();
            this.mNumberOperation = in.readString();
            this.mNotation = in.readString();
            this.mManager = in.readString();
            this.mTypeOfPayment = in.readString();
            this.mContactPerson = in.readString();
            this.mКindOf = in.readString();
            this.mWarehouse = in.readString();
        }

        public static final Creator<OrdersNumber> CREATOR = new Creator<OrdersNumber>() {
            public OrdersNumber createFromParcel(Parcel source) {
                return new OrdersNumber(source);
            }
            public OrdersNumber[] newArray(int size) {
                return new OrdersNumber[size];
            }
        };

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //The following are just setter and getter methods
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        public String getmNumber() {
            return mNumber;
        }

        public void setmNumber(String mNumber) {
            this.mNumber = mNumber;
        }

        public String getmDate() {
            return mDate;
        }

        public void setmDate(String mDate) {
            this.mDate = mDate;
        }

        public String getmNumberOperation() {
            return mNumberOperation;
        }

        public void setmNumberOperation(String mNumberOperation) {
            this.mNumberOperation = mNumberOperation;
        }

        public String getmNotation() {
            return mNotation;
        }

        public void setmNotation(String notation) {
            this.mNotation = notation;
        }

        public String getmManager() {
        return mManager;
    }

        public void setmManager(String manager) {
        this.mManager = manager;
    }

        public String getmTypeOfPayment() {
        return mTypeOfPayment;
    }

        public void setmTypeOfPayment(String typeOfPayment) {
            this.mTypeOfPayment = typeOfPayment;
       }

        public String getmContactPerson() {
        return mContactPerson;
    }

        public void setmContactPerson(String contactPerson) {
        this.mContactPerson = contactPerson;
    }

        public String getmКindOf() {
        return mКindOf;
    }

        public void setmКindOf(String kindOf) {
            this.mКindOf = kindOf;
        }

        public String getmWarehouse() {
        return mWarehouse;
    }

        public void setmWarehouse(String warehouse) {
        this.mWarehouse = warehouse;
    }

}
