package com.example.user.simpleui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Location;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, RoutingListener {

    final  static int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;

    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    Marker marker;
    LatLng storeLocation;

    List<Polyline> polylines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        final String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        TextView noteTextView = (TextView) findViewById(R.id.noteTextView);
        TextView storeInfoTextView = (TextView) findViewById(R.id.storeInfoTextView);
        TextView menuResultsTextView = (TextView) findViewById(R.id.menuResultsTextView);
        ImageView staticMapImageView = (ImageView) findViewById(R.id.googleMapImageView);

        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);

        String text = "";

        if (menuResultList != null) {
            for (String menuResult : menuResultList) {
                text += menuResult + "\n";
            }
        }
        menuResultsTextView.setText(text);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                (new GeoCodingTask(OrderDetailActivity.this)).execute("台北市大安區羅斯福路四段一號");
            }
        });


    }

    @Override
    public void responseWithGeoCondingResults(LatLng latlng) {
        if (googleMap != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17);
            //         googleMap.animateCamera(cameraUpdate);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("台灣大學").snippet("Hello Google Map");
            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    CameraUpdate cp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 21);//點擊放大成21
                    googleMap.moveCamera(cp);
                    return false;
                }
            });

            storeLocation = latlng;

//            googleMap.moveCamera(cameraUpdate);
            CreateGoogleAPIClient();

        }
    }
    public  void CreateGoogleAPIClient(){
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION_REQUEST_CODE);
                }
            return;
        }

        createLocationRequest();
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        LatLng start = new LatLng(25.0186348,121.5398379);

        if(location != null){
            start = new LatLng(location.getLatitude(),location.getLongitude());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,17));

        Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.WALKING)
                                .waypoints(start,storeLocation)
                                .withListener(this)
                                .build();

        routing.execute();
    }

    public void createLocationRequest(){
        if (locationRequest == null){
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//高精準度
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ACCESS_FINE_LOCATION_REQUEST_CODE == requestCode)
        {
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                onConnected(null);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,17));

        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title("台灣大學").snippet("Hello Google Map");
            marker = googleMap.addMarker(markerOptions);
        }
        else
        {
            marker.setPosition(currentLatLng);
        }

        if (polylines.size()>0){
            for(Polyline polyline:polylines)
            {
                List<LatLng> points = polyline.getPoints();
                int index = -1;
                for (int i = 0;i<points.size();i++)
                {
                    if(i!=points.size()-1)
                    {

                        LatLng point1 = points.get(i);
                        LatLng point2 = points.get(i+1);

                        Double maxLat = Math.max(point1.latitude, point2.latitude);
                        Double minLat = Math.min(point1.latitude, point2.latitude);
                        Double maxLng = Math.max(point1.latitude, point2.latitude);
                        Double minLng = Math.min(point1.latitude, point2.latitude);
                        if (currentLatLng.latitude >= minLat && currentLatLng.latitude < maxLat && currentLatLng.latitude>=minLng && currentLatLng.latitude< maxLng)
                        {
                            index = i;
                            break;
                        }
                    }

                }
                if (index != -1){
                    for (int i = index-1;i>=0;i--){
                        points.remove(0);
                    }
                    points.set(0,currentLatLng);
                    polyline.setPoints(points);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null)
        {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int i) {

        if (polylines.size()>0){
            for(Polyline polyline :polylines){
                polyline.remove();
            }
        }

        for (int index = 0;index<routes.size();index++)
        {
            List<LatLng> points = routes.get(index).getPoints();
            PolylineOptions polylineOptions = new PolylineOptions();//如何化線段
            polylineOptions.addAll(points);
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(10);

            Polyline polyline = googleMap.addPolyline(polylineOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}