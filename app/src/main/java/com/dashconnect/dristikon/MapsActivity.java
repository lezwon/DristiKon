package com.dashconnect.dristikon;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMarkerClickListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng, crimeLatLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker, crimeMarker,clueMarker;
    float distance;

    ListView list;
    String[] itemname ={
            "Knife",
            "Finger print",

    };
    List<Integer> num = new ArrayList<>();

    Integer[] imgid= new Integer[]{
            R.drawable.knife,
            R.drawable.finger_print
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        num.add(R.drawable.knife);
        num.add(R.drawable.finger_print);


        distance=10.0f;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab.hide();

                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, null);

                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AppBarLayout.LayoutParams.WRAP_CONTENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        fab.show();
                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            }
        });


        final FloatingActionButton clue = (FloatingActionButton) findViewById(R.id.clue);
        clue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clue.hide();

                Integer[] finalResult = num.toArray(new Integer[num.size()]);
                final CustomListAdapter adapter=new CustomListAdapter(MapsActivity.this, itemname,finalResult);
                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setBackgroundColor(Color.GRAY);
                list.setVisibility(View.VISIBLE);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        String Slecteditem= itemname[+position];
                        Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                        list.setVisibility(View.INVISIBLE);
                        clue.show();
                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        mGoogleMap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mGoogleMap.setMapStyle(style);

    }

    protected synchronized void buildGoogleApiClient() {
        //  Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000); //5 seconds
        mLocationRequest.setFastestInterval(1000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        try {
            crimeLatLng = new LatLng(mLastLocation.getLatitude() + 0.0001, mLastLocation.getLongitude() + 0.0001);

        } catch (NullPointerException e) {
            Log.e("error", "null");
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(crimeLatLng);
        markerOptions.title("Crime");

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.crime_scene_taped));
        crimeMarker = mGoogleMap.addMarker(markerOptions);


        try {
            crimeLatLng = new LatLng(mLastLocation.getLatitude() + 0.0002, mLastLocation.getLongitude() - 0.0002);

        } catch (NullPointerException e) {
            Log.e("error", "null");
        }
        markerOptions = new MarkerOptions();
        markerOptions.position(crimeLatLng);
        markerOptions.title("Crime");

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.scan_footsteps));
        clueMarker = mGoogleMap.addMarker(markerOptions);
        }

    @Override
    public void onConnectionSuspended(int i) {
        // Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //  Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        distance = distance(latLng.latitude, latLng.longitude, crimeLatLng.latitude, crimeLatLng.longitude);


        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        currLocationMarker = mGoogleMap.addMarker(markerOptions);


        // Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .tilt(60)
                .target(latLng).zoom(20).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    public float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getId().equals(crimeMarker.getId()) || marker.getId().equals(clueMarker.getId())) {
            if (distance < 50) {
                Toast.makeText(this, "Reached crime location", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MapsActivity.this, ArchitectActivity.class);
                startActivity(intent);

            }
            else{
                Toast.makeText(this, "crime location is too far", Toast.LENGTH_SHORT).show();

            }
        }
        return true;
    }
}