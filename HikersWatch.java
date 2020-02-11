package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startListenting();
        }
    }
    public void startListenting(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("LocationInfo", location.toString());

        TextView latTextView=(TextView) findViewById(R.id.latitudeTextView);
        TextView lonTextView=(TextView)findViewById(R.id.longitudeTextView);
        TextView accTextView=(TextView)findViewById(R.id.accuracyTextView);
        TextView altTextView=(TextView)findViewById(R.id.altitudeTextView);


        latTextView.setText("Latitude: "+String.format("%.2f",location.getLatitude()));
        lonTextView.setText("Longitude: "+String.format("%.2f",location.getLongitude()));
        accTextView.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: "+Double.toString(location.getAltitude()));



        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address="Could Not Find Address :(";
            List <Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                Log.i("PlaceInfo", addressList.get(0).toString());
                address="Address: \n";

                if(addressList.get(0).getAddressLine(0)!=null){
                    address+=addressList.get(0).getAddressLine(0)+"\n";
                }
            }
            TextView addTextView=(TextView) findViewById(R.id.addressTextView);
            addTextView.setText(address);
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getActionBar().hide();
        //getSupportActionBar().hide();
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(Build.VERSION.SDK_INT<23){
            startListenting();
        }else {

            //If we dont have permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //If we have the permission
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Whether location is known or not
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }

}
