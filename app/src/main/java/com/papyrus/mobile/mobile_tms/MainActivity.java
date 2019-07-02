package com.papyrus.mobile.mobile_tms;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.ksoap2.serialization.SoapObject;
import java.text.SimpleDateFormat;
import java.util.*;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import com.google.gson.Gson;
import android.content.Context;


public class MainActivity extends AppCompatActivity implements LoginCompleted {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    public Boolean inProc = false;
    CheckBox chkRememberMe;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        chkRememberMe = (CheckBox) findViewById(R.id.rempasswordcheckbox);
        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        String PREFRENCES_NAME = "Login Data";
        SharedPreferences settings = context.getSharedPreferences(PREFRENCES_NAME, 0);
        Name.setText(settings.getString("name", ""));
        Password.setText(settings.getString("pwd", ""));
       if( settings.getString("chk", "").equals( "chk")){
           chkRememberMe.setChecked(true);
       }
        Login = (Button) findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!inProc){ validate(Name.getText().toString(), Password.getText().toString());}
            }
        });
    }

    private void validate(String userName, String userPassword)
    {
        inProc = true;
        Login.setEnabled(false);
        String[] myTaskParams = {"GetFlights", userName, userPassword };
        new DataLoader(MainActivity.this).execute(myTaskParams);
    }

    @Override
    public void onTaskComplete(SoapObject Result) {
        if(Result != null)
        {
            Login.setEnabled(true);
            String ok = Result.getProperty(0).toString();
            if (!ok.equals("NO")) {
            SharedPreferences mPrefs =  context.getSharedPreferences("Global", 0);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("NameD", Name.getText().toString());
            prefsEditor.putString("Password", Password.getText().toString());
            String PREFRENCES_NAME = "Login Data";
            if (chkRememberMe.isChecked())
            {
                SharedPreferences settings =  context.getSharedPreferences(PREFRENCES_NAME, 0);
                settings.edit().putString("name", Name.getText().toString()).putString("pwd", Password.getText().toString()).putString("chk", "chk").apply();
            } else
            {
                SharedPreferences settings =  context.getSharedPreferences(
                        PREFRENCES_NAME, this.MODE_PRIVATE);
                settings.edit().clear().apply();
            }

            ArrayList<Flights> flight = new ArrayList<>();
            SoapObject fl;
            SoapObject OrderSoap;
            SoapObject OrdersNumbers;
            SoapObject TrNumber;
            for (int i = 0; i < Result.getPropertyCount(); i++) {
                fl = (SoapObject)Result.getProperty(i);
                ArrayList<Order> orders = new ArrayList<>();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                String curData = dateformat.format(new Date());
                for (int y = 0; y < (fl.getPropertyCount() - 2); y++) {
                    OrderSoap =  (SoapObject)fl.getProperty(y);
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
                flight.add(new Flights(fl.getProperty("FlightsNumber").toString()
                        , fl.getProperty("FlightsDate").toString(),false,  orders));
            }
            Gson gson = new Gson();
            String json = gson.toJson(flight);
            prefsEditor.putString("flight", json);
            prefsEditor.apply();
            MainActivity.this.finish();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            }
        }
        inProc = false;
    }

}


