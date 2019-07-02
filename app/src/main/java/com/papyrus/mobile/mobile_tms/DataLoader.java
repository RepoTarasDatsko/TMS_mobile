package com.papyrus.mobile.mobile_tms;

import android.os.AsyncTask;
import android.content.Context;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import android.content.SharedPreferences;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;


public class DataLoader extends AsyncTask<String, Void, SoapObject> {

    public SoapObject Result;
    private static final String NAMESPACE = "http://www.papyrus.tms_mobile-package.org";
    //private static final String URL = "http://212.90.177.62:88/TMS/ws/TMSMobile.1cws";
    private static final String URL = "http://93.170.114.42:99/TMS/ws/TMSMobile.1cws";
    //private static final String URL = "http://212.90.177.62:98/TMSTEST/ws/TMSMobile.1cws";
    private static final String WEB_SERVICE = "#TMSMobile:";
    private static final String SOAP_ACTION = NAMESPACE + WEB_SERVICE;

    private Context mContext;
    private LoginCompleted mCallback;

    public DataLoader(Context context) {
        this.mContext = context;
        this.mCallback = (LoginCompleted) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected SoapObject doInBackground(String... params) {
        try {
            SoapObjectCustom request = new SoapObjectCustom(NAMESPACE, params[0]);
            if (params[0] == "SaveAll") {

                SharedPreferences  mPrefs = mContext.getSharedPreferences("Global", 0);
                Gson gson = new Gson();
                String json = mPrefs.getString("flight", "");
                ArrayList flights =  gson.fromJson(json,  ArrayList.class);
                String currentFlight = mPrefs.getString("CurrentOrdersNumber", "");
                SoapObjectCustom mobileflights = new SoapObjectCustom(NAMESPACE, "mobileFlights");

                for (Object itemflight : flights) {
                    LinkedTreeMap item = (LinkedTreeMap) itemflight;
                    if (!item.get("mFlightsNumber").toString().equals(currentFlight)) {
                        continue;
                    }
                    SoapObjectCustom mobileflight = new SoapObjectCustom(NAMESPACE, "Flights");
                    String Items;
                    ArrayList orders = (ArrayList) item.get("mOrders");
                    for (Object it : orders) {
                        LinkedTreeMap itemOrder = (LinkedTreeMap) it;
                        SoapObjectCustom mobileOrder = new SoapObjectCustom(NAMESPACE,
                                "Orders");
                        mobileOrder.addProperty("Customer", itemOrder.get("mCustomer").toString());
                        SoapObjectCustom mobileTransactionNumbers = new SoapObjectCustom(NAMESPACE, "OrdersNumber");
                        ArrayList ordersNumber = (ArrayList) itemOrder.get("mOrdersNumber");
                        for (Object on : ordersNumber) {
                            LinkedTreeMap itemNumber = (LinkedTreeMap) on;
                            SoapObjectCustom mobileNumber = new SoapObjectCustom(NAMESPACE,
                                    "TransactionNumber");
                            mobileNumber.addProperty("Number", itemNumber.get("mNumber").toString());
                            mobileNumber.addProperty("Date", itemNumber.get("mDate").toString());
                            Items = itemNumber.get("mNotation").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("Notation", "");
                            } else {
                                mobileNumber.addProperty("Notation", itemNumber.get("mNotation").toString());
                            }
                            Items = itemNumber.get("mNumberOperation").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("NumberOperation", "1");
                            } else {
                                mobileNumber.addProperty("NumberOperation", itemNumber.get("mNumberOperation").toString());
                            }
                            Items = itemNumber.get("mManager").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("Manager", "");
                            } else {
                                mobileNumber.addProperty("Manager", itemNumber.get("mManager").toString());
                            }
                            Items = itemNumber.get("mTypeOfPayment").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("TypeOfPayment", "");
                            } else {
                                mobileNumber.addProperty("TypeOfPayment", itemNumber.get("mTypeOfPayment").toString());
                            }
                            Items = itemNumber.get("mContactPerson").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("ContactPerson", "");
                            } else {
                                mobileNumber.addProperty("ContactPerson", itemNumber.get("mContactPerson").toString());
                            }
                            Items = itemNumber.get("mКindOf").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("КindOf", "");
                            } else {
                                mobileNumber.addProperty("КindOf", itemNumber.get("mКindOf").toString());
                            }
                            Items = itemNumber.get("mWarehouse").toString();
                            if (Items.equals("anyType{}")) {
                                mobileNumber.addProperty("Warehouse", "");
                            } else {
                                mobileNumber.addProperty("Warehouse", itemNumber.get("mWarehouse").toString());
                            }

                            mobileTransactionNumbers.addSoapObject(mobileNumber);
                        }
                        mobileOrder.addSoapObject(mobileTransactionNumbers);
                        Items = itemOrder.get("mAdress").toString();
                        if (Items.equals("anyType{}")) {
                            mobileOrder.addProperty("Adress", "");
                        } else {
                            mobileOrder.addProperty("Adress", Items);
                        }
                        Items = itemOrder.get("mlongitude").toString();
                        if (Items.equals("anyType{}")) {
                            mobileOrder.addProperty("longitude", "0");
                        } else {
                            mobileOrder.addProperty("longitude", itemOrder.get("mlongitude").toString());
                        }
                        Items = itemOrder.get("mlatitude").toString();
                        if (Items.equals("anyType{}")) {
                            mobileOrder.addProperty("latitude", "0");
                        } else {
                            mobileOrder.addProperty("latitude", itemOrder.get("mlatitude").toString());
                        }
                        Items = itemOrder.get("mNumberOfSeats").toString();
                        if (Items.equals("anyType{}")) {
                            mobileOrder.addProperty("NumberOfSeats", 1);
                        } else {
                            mobileOrder.addProperty("NumberOfSeats", 1);
                        }
                        Double t = (Double) itemOrder.get("mNumberOfPosition");
                        int temp = t.intValue();
                        mobileOrder.addProperty("NumberOfPosition", temp);
                        mobileOrder.addProperty("Condition", itemOrder.get("mCondition").toString());
                        Items = itemOrder.get("mTimeCalculated").toString();
                        if (Items.equals("anyType{}")) {
                            mobileOrder.addProperty("TimeCalculated", "");
                        } else {
                            mobileOrder.addProperty("TimeCalculated", Items);
                        }
                        mobileOrder.addProperty("CheckOutTime", itemOrder.get("mCheckOutTime").toString());
                        mobileOrder.addProperty("Operation", Boolean.valueOf(itemOrder.get("mOperation").toString()));
                        mobileOrder.addProperty("Passed", Boolean.valueOf(itemOrder.get("mPassed").toString()));
                        mobileOrder.addProperty("TimeDelivery", itemOrder.get("mTimeDelivery").toString());
                        mobileflight.addSoapObject(mobileOrder);
                    }
                    mobileflight.addProperty("FlightsNumber", item.get("mFlightsNumber").toString());
                    mobileflight.addProperty("FlightsDate", item.get("mFlightsDate").toString());
                    mobileflight.addProperty("Swap", Boolean.valueOf(item.get("mSwap").toString()));
                    mobileflights.addSoapObject( mobileflight);
                }
                request.addSoapObject(mobileflights);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(URL, params[1], params[2]);
            androidHttpTransport.debug = true;
            try {
                androidHttpTransport.call(SOAP_ACTION + params[0], envelope);
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    Result = (SoapObject) result.getProperty("return");
            } catch (Exception e) {
                e.printStackTrace();
                SoapObject NO = new SoapObject(NAMESPACE, "mobileOK");
                NO.addProperty("OK", "NO");
                return NO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            SoapObject NO = new SoapObject(NAMESPACE, "mobileOK");
            NO.addProperty("OK", "NO");
            return NO;
        }
        return Result;
    }

    @Override
    protected void onPostExecute(SoapObject Result) {
            mCallback.onTaskComplete(Result);
    }
}
