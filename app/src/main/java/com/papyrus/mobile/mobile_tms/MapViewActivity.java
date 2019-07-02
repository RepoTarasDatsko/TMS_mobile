package com.papyrus.mobile.mobile_tms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.maps.android.ui.IconGenerator;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Bitmap;
import java.util.ArrayList;
import android.widget.Button;
import android.graphics.Color;


public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private GoogleMap gmap;
    ArrayList flights;
    SharedPreferences mPrefs;
    Button toolBarRoute;
    private Context context;

    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyCkWTss7HhcxJVDl1VCIkN8YHDojYHvo2Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        context = getApplicationContext();
        mPrefs =  context.getSharedPreferences("Global", 0);
        String ordersNumber = mPrefs.getString("CurrentOrdersNumber", "");
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setSubtitle(ordersNumber);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        gmap.setMinZoomPreference(10);
        Gson gson = new Gson();
        String json = mPrefs.getString("orders", "");
        ArrayList orders = gson.fromJson(json,  ArrayList.class);
        LatLng ny = new LatLng(50.4017832, 30.3926093);
        for (int i = 0; i < orders.size(); i++) {
            LinkedTreeMap order = (LinkedTreeMap)orders.get(i);
        String latitude =  order.get("mlatitude").toString();
        double TARGET_LATITUDE = Double.parseDouble(latitude.replace(",",".") );
        String longitude =  order.get("mlongitude").toString();
        double TARGET_LONGITUDE = Double.parseDouble(longitude.replace(",",".") );
        LatLng point = new LatLng( TARGET_LATITUDE, TARGET_LONGITUDE);

        String text = String.valueOf(i + 1);

       IconGenerator tc = new IconGenerator(this);
         String notation =  order.get("mCondition").toString();
            switch (notation) {
                case "00011":
                  tc.setColor(Color.GREEN);
       tc.setTextAppearance(R.style.iconGenText);
                    break;
                case "00012":
                   tc.setColor(Color.GRAY);
       tc.setTextAppearance(R.style.iconGenText);
                    break;
                case "00010":
                   tc.setColor(Color.CYAN);
                    tc.setTextAppearance(R.style.iconGenText);
                    break;
                default:
                   tc.setColor(Color.CYAN);
                    tc.setTextAppearance(R.style.iconGenText);
            }

       Bitmap bmp = tc.makeIcon(text);
       gmap.addMarker(new MarkerOptions().position(point)
               .title(order.get("mCustomer").toString())
               .icon(BitmapDescriptorFactory.fromBitmap(bmp))
           );
       }

        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));

    }
}
