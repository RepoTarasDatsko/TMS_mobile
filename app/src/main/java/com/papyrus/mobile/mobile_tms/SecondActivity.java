package com.papyrus.mobile.mobile_tms;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import org.ksoap2.serialization.SoapObject;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;



public class SecondActivity extends AppCompatActivity implements LoginCompleted{
    private ArrayList<DataItems> data;
    private RecyclerView myrecyclerview;
    private MyAdapter adapter;
    private Spinner spinnerWithBorder;
    String[] spinnerItems;
    ArrayList flights;
    private Context context;
    SharedPreferences mPrefs;

    private ArrayList<String> Numbers;
    private ArrayList<String> Date;
    int currentPosition = 1000;
    Button btnUpdate, btnSave, btnRoute;
    private static final int JOB_ID = 1002;
    private static final long REFRESH_INTERVAL = 15  * 1000;

    @Override protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_second);
        mPrefs =  context.getSharedPreferences("Global", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("flight", "");
        flights = gson.fromJson(json,  ArrayList.class);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnRoute = (Button)findViewById(R.id.btnRoute);

        spinnerItems = new String[flights.size()];
        for (int i = 0; i < flights.size(); i++) {
            LinkedTreeMap fl = ( LinkedTreeMap )flights.get(i);
            String item = "Рейс " + fl.get("mFlightsNumber")  + " от " + fl.get("mFlightsDate") ;
            spinnerItems[i] = item;
        }
        spinnerWithBorder = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_textview, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spinnerWithBorder.setAdapter(spinnerArrayAdapter);
        myrecyclerview = (RecyclerView) findViewById(R.id.rv_fruits);
        myrecyclerview.hasFixedSize();
        myrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<DataItems>();
        LinkedTreeMap fl = ( LinkedTreeMap )flights.get(0);
        ArrayList orders = (ArrayList)fl.get("mOrders");
        for (int i = 0; i < orders.size(); i++) {
           LinkedTreeMap order = (LinkedTreeMap)orders.get(i);
           ArrayList ordersNumber = (ArrayList) order.get("mOrdersNumber");
           String notation = "";
            for (int y = 0; y < ordersNumber.size(); y++) {
                LinkedTreeMap num = (LinkedTreeMap)ordersNumber.get(y);
                if(!notation.equals(num.get("mNotation").toString())){
                    String n = num.get("mNotation").toString();
                    if(!n.equals("anyType{}")) {
                        notation +=  n;
                    }
                }
            }
            if(i == 0) {
                data.add(new DataItems(order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation, order.get("mTimeDelivery").toString()));
            }else{
                data.add(new DataItems(String.valueOf(i) +  ") " + order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation, order.get("mTimeDelivery").toString()));
            }
        }
        adapter = new MyAdapter(data);
        myrecyclerview.setAdapter(adapter);

        ItemTouchHelper.Callback call = new RVHItemTouchHelperCallback(adapter, true, false, false);
        ItemTouchHelper helper = new ItemTouchHelper(call);
        helper.attachToRecyclerView(myrecyclerview);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSave.isEnabled()) {
                    String[] myTaskParams = {"GetFlights", mPrefs.getString("NameD", ""), mPrefs.getString("Password", "")};
                    new DataLoader(SecondActivity.this).execute(myTaskParams);
                    btnUpdate.setEnabled(false);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnUpdate.isEnabled()) {
                    String[] myTaskParams = {"SaveAll", mPrefs.getString("NameD", ""), mPrefs.getString("Password", "")};
                    new DataLoader(SecondActivity.this).execute(myTaskParams);
                    btnSave.setEnabled(false);
                }
            }
        });
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /*String json = mPrefs.getString("flight", "");
                    Gson gson = new Gson();
                    flights = gson.fromJson(json,  ArrayList.class);
                    LinkedTreeMap fl = (LinkedTreeMap )flights.get(0);
                    ArrayList orders = (ArrayList)fl.get("mOrders");
                    for (int i = 0; i < orders.size(); i++) {
                       LinkedTreeMap order = (LinkedTreeMap)orders.get(i);
                       ArrayList ordersNumber = (ArrayList) order.get("mOrdersNumber");
                       String notation = "";
                       for (int y = 0; y < ordersNumber.size(); y++) {
                           LinkedTreeMap num = (LinkedTreeMap)ordersNumber.get(y);
                           if(!notation.equals(num.get("mNotation").toString())){
                               String n = num.get("mNotation").toString();
                               if(!n.equals("anyType{}")) {
                                   notation +=  n;
                               }
                           }
                       }
                        if(i == 0) {
                            data.add(new DataItems(order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation));
                        }else{
                            data.add(new DataItems(String.valueOf(i) +  ") " + order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation));
                        }
                }*/
                    Intent intent = new Intent(SecondActivity.this, MapViewActivity.class);
                startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Error " + e.getMessage());
                }
            }
        });
        spinnerWithBorder.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (currentPosition != position) {
                            adapter.Clear();
                            Gson gson = new Gson();
                            String json = mPrefs.getString("flight", "");
                            flights = gson.fromJson(json,  ArrayList.class);
                            LinkedTreeMap fl = (LinkedTreeMap)flights.get(position);
                            ArrayList orders = (ArrayList)fl.get("mOrders");
                            json = gson.toJson(orders);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            prefsEditor.putString("orders", json);
                            prefsEditor.putString("CurrentOrdersNumber",  fl.get("mFlightsNumber").toString());
                            prefsEditor.apply();
                            for (int i = 0; i < orders.size(); i++) {
                                LinkedTreeMap order = ( LinkedTreeMap )orders.get(i);
                                ArrayList ordersNumber = (ArrayList) order.get("mOrdersNumber");
                                String notation = "";
                                for (int y = 0; y < ordersNumber.size(); y++) {
                                    LinkedTreeMap num = (LinkedTreeMap)ordersNumber.get(y);
                                    if(!notation.equals(num.get("mNotation").toString())){
                                        String n = num.get("mNotation").toString();
                                        if(!n.equals("anyType{}")) {
                                            notation +=  n;
                                        }
                                    }
                                }
                                if(i == 0) {
                                    data.add(new DataItems(order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation, order.get("mTimeDelivery").toString()));
                                }else{
                                    data.add(new DataItems(String.valueOf(i) + ") " + order.get("mCustomer").toString() + " " + order.get("mAdress").toString(), notation, order.get("mTimeDelivery").toString()));
                                }
                            }
                            currentPosition = position;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(SecondActivity.this, "Spinner2: unselected",
                                Toast.LENGTH_LONG).show();
                    }
                });

        // Set the divider
       myrecyclerview.addItemDecoration(
              new RVHItemDividerDecoration(this, LinearLayoutManager.VERTICAL));

    }

    @Override
    public void onTaskComplete(SoapObject Result) {
        if(Result != null) {
            if (!btnSave.isEnabled()) {
                String ok = Result.getProperty(0).toString();
                if (ok.equals("OK")) {
                    Toast.makeText(SecondActivity.this, "Saved successfully", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putBoolean("SaveAllCompleted", true);
                    prefsEditor.apply();
                } else {
                    Toast.makeText(SecondActivity.this, "Service is not available",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putBoolean("SaveAllCompleted", false);
                    prefsEditor.apply();
                    scheduleJob();
                }
                btnSave.setEnabled(true);
            }

            if (!btnUpdate.isEnabled()) {
                String ok = Result.getProperty(0).toString();
                if (!ok.equals("NO")) {
                ArrayList<Flights> flight = new ArrayList<>();
                SoapObject fl;
                SoapObject OrderSoap;
                SoapObject OrdersNumbers;
                SoapObject TrNumber;
                for (int i = 0; i < Result.getPropertyCount(); i++) {
                        fl = (SoapObject) Result.getProperty(i);
                        ArrayList<Order> orders = new ArrayList<>();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        String curData = dateformat.format(new Date());
                        for (int y = 0; y < (fl.getPropertyCount() - 2); y++) {
                            OrderSoap = (SoapObject) fl.getProperty(y);
                            OrdersNumbers =   (SoapObject)OrderSoap.getProperty("OrdersNumber");
                            ArrayList<OrdersNumber> ordersNumber = new ArrayList<>();
                            for (int z = 0; z < OrdersNumbers.getPropertyCount(); z++) {
                                TrNumber =  (SoapObject)OrdersNumbers.getProperty(z);
                                ordersNumber.add(new OrdersNumber(TrNumber.getProperty("Number").toString()
                                        , TrNumber.getProperty("Date").toString()
                                        , TrNumber.getProperty("NumberOperation").toString()
                                        , TrNumber.getProperty("Notation").toString()
                                        , TrNumber.getProperty("Manager").toString()
                                        , TrNumber.getProperty("TypeOfPayment").toString()
                                        , TrNumber.getProperty("ContactPerson").toString()
                                        , TrNumber.getProperty("КindOf").toString()
                                        , TrNumber.getProperty("Warehouse").toString()));
                            }
                            orders.add(new Order(OrderSoap.getProperty("Customer").toString()
                                    , ordersNumber
                                    , OrderSoap.getProperty("Adress").toString()
                                    , OrderSoap.getProperty("longitude").toString()
                                    , OrderSoap.getProperty("latitude").toString()
                                    , Integer.parseInt(OrderSoap.getProperty("NumberOfPosition").toString())
                                    , OrderSoap.getProperty("NumberOfSeats").toString()
                                    , OrderSoap.getProperty("TimeCalculated").toString()
                                    , curData
                                    , Boolean.valueOf(OrderSoap.getProperty("Passed").toString())
                                    , Boolean.valueOf(OrderSoap.getProperty("Operation").toString())
                                    , OrderSoap.getProperty("Condition").toString()
                                    , OrderSoap.getProperty("TimeDelivery").toString()));
                        }
                        flight.add(new Flights(fl.getProperty("FlightsNumber").toString(), fl.getProperty("FlightsDate").toString(),false, orders));
                    }
                    SharedPreferences mPrefs =  context.getSharedPreferences("Global", 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String jsonflight = gson.toJson(flight);
                    prefsEditor.putString("flight", jsonflight);
                    adapter.Clear();
                    flights = flight;
                    Flights f = (Flights)flights.get(currentPosition);
                    ArrayList gsonOrder = (ArrayList)f.getOrders();
                    String jsonOrder  = gson.toJson(gsonOrder);
                    prefsEditor.putString("orders", jsonOrder);
                    prefsEditor.putString("CurrentOrdersNumber", f.getmFlightsNumber());
                    prefsEditor.apply();
                    for (int i = 0; i < gsonOrder.size(); i++) {
                        Order or = (Order)gsonOrder.get(i);
                        ArrayList <OrdersNumber> ordersNumber = or.getOrders();
                        String notation = "";
                        for (int y = 0; y < ordersNumber.size(); y++) {
                            OrdersNumber num = (OrdersNumber)ordersNumber.get(y);
                            if(!notation.equals(num.getmNotation())){
                                String n = num.getmNotation();
                                if(!n.equals("anyType{}")) {
                                    notation +=  n;
                                }
                            }
                        }
                        if(i == 0) {
                            data.add(new DataItems( or.getmCustomer() + " " + or.getmAdress(), notation, or.getmTimeDelivery()));
                        }else{
                            data.add(new DataItems(String.valueOf(i) +  ") " + or.getmCustomer() + " " + or.getmAdress(), notation, or.getmTimeDelivery()));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SecondActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SecondActivity.this, "Service is not available",
                            Toast.LENGTH_LONG).show();
                }

                btnUpdate.setEnabled(true);
            }
        }else {
            if (!btnSave.isEnabled()) {
                Toast.makeText(SecondActivity.this, "Service is not available",
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putBoolean("SaveAllCompleted", false);
                prefsEditor.apply();
                scheduleJob();
            }
            btnUpdate.setEnabled(true);
            btnSave.setEnabled(true);
        }
    }

    private void scheduleJob() {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

        // The JobService that we want to run
        final ComponentName name = new ComponentName(this, MJobScheduler.class);

        // Schedule the job
        assert jobScheduler != null;
        final int result = jobScheduler.schedule(getJobInfo(JOB_ID, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(SecondActivity.this, "Scheduled job successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private JobInfo getJobInfo(final int id, final ComponentName name) {
        final long interval = REFRESH_INTERVAL; // run every hour
        final boolean isPersistent = true; // persist through boot
        final int networkType = JobInfo.NETWORK_TYPE_ANY; // Requires some sort of connectivity

        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }
        return jobInfo;
    }

}
