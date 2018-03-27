package com.androidsrc.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

        private Socket mSocket;
        private Marker marker;

    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    {
        try {
            //mSocket = IO.socket("http://192.168.1.101:3000");//old port
            mSocket = IO.socket("http://192.168.1.101:3002");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private LatLng previous;
    private LatLng current;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carmarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16.0f));

        previous = sydney;
        current = sydney;

        mSocket.connect();
        //mSocket.emit("user_connect", 119);
        mSocket.on("get_geolocation_17", onNewMessage);


    }



    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Gson gson = new Gson();
                    try {
                        GeoLocation response = gson.fromJson(data.toString(), GeoLocation.class);
                        Log.e("Locations : ", "Latitude : " + response.getGeoLat() + "\n" + "Longitude : " + response.getGeoLon());
                        if (marker != null) {
                            previous = current;
                            current = new LatLng(Double.parseDouble(response.getGeoLat()), Double.parseDouble(response.getGeoLon()));
                            marker.setPosition(new LatLng(Double.parseDouble(response.getGeoLat()), Double.parseDouble(response.getGeoLon())));
                            marker.setRotation(Float.parseFloat(response.getGeoHeading()));
                            //rotateMarker(marker,Float.parseFloat(response.getGeoHeading()));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(response.getGeoLat()), Double.parseDouble(response.getGeoLon()))));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(Double.parseDouble(response.getGeoLat()), Double.parseDouble(response.getGeoLon())))             // Sets the center of the map to current location
                                    .zoom(15)                   // Sets the zoom
                                    //.bearing(bearingBetweenLocations(previous,current)) // Sets the orientation of the camera to east
                                    .bearing(Float.parseFloat(response.getGeoHeading()))
                                    .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
                        }
                        if (response != null) {
                        }
                    } catch (Exception e) {
                        return;
                    }

                    // add the message to view

                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }


    private float bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return (float)brng;
    }

private boolean isMarkerRotating;
    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
}
