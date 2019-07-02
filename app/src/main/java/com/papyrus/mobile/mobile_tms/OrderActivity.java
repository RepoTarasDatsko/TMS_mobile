package com.papyrus.mobile.mobile_tms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    private  double TARGET_LATITUDE;
    private  double TARGET_LONGITUDE;
    private TextView Customer;
    private TextView Adress;
    private TextView Notation;
    private TextView Condition;
    private TextView ContactPerson;
    private TextView Manager;
    private TextView КindOf;
    private TextView Warehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        Customer = (TextView)findViewById(R.id.tvCustomer);
        Customer.setText(intent.getStringExtra("Customer"));
        Adress = (TextView)findViewById(R.id.tvAdress);
        Adress.setText(intent.getStringExtra("Adress"));
        Notation = (TextView)findViewById(R.id.tvNotation);
        Notation.setText(intent.getStringExtra("notation"));
        Condition = (TextView)findViewById(R.id.textView2);
        String condition = intent.getStringExtra("condition");
        ContactPerson = (TextView)findViewById(R.id.tvContactPerson);
        ContactPerson.setText(intent.getStringExtra("ContactPerson"));
        КindOf = (TextView)findViewById(R.id.textКindOf);
        КindOf.setText(intent.getStringExtra("КindOf"));
        Manager = (TextView)findViewById(R.id.tvManager);
        Manager.setText(intent.getStringExtra("Manager"));
        Warehouse = (TextView)findViewById(R.id.textWarehouse);
        Warehouse.setText(intent.getStringExtra("Warehouse"));

            switch (condition) {
                case "00011":
                    Condition.setText("Виконано");
                    break;
                case "00012":
                    Condition.setText("Відкладено");
                    break;
                case "00010":
                    Condition.setText("В дорозі");
                    break;
                default:
                    Condition.setText("В дорозі");
            }

        String latitude =  intent.getStringExtra("latitude");
        TARGET_LATITUDE = Double.parseDouble(latitude.replace(",",".") );
        String longitude =  intent.getStringExtra("longitude");
        TARGET_LONGITUDE = Double.parseDouble(longitude.replace(",",".") );
        createMapView();
    }

    private void createMapView() {
       // MapsInitializer.initialize(context);
        try {
            if (null == googleMap) {
                ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMapAsync(this);
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    private void addMarker() {
        double lat = TARGET_LATITUDE;
        double lng = TARGET_LONGITUDE;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);

        if (null != googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(Customer.getText().toString())
                    .draggable(false)
            );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap = googleMap;
        addMarker();
    }
}


