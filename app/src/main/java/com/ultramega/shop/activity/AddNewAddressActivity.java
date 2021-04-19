package com.ultramega.shop.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.util.CustomMapFragment;
import com.ultramega.shop.util.GPSTracker;

import java.io.IOException;
import java.util.List;

public class AddNewAddressActivity extends BaseActivity implements OnMapReadyCallback, CustomMapFragment.OnTouchListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private List<Address> addressList;
    private final int marker_height = 60;
    private final int marker_width = 60;
    // Declare marker globally
    private Marker globalMarker;
    //    private Circle globalCircle;
    private Geocoder geocoder;
    private Address markerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        setUpToolBar();

        gpsTracker = new GPSTracker(this);
        geocoder = new Geocoder(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            CustomMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.setListener(this);
            mapFragment.getMapAsync(this);
        }

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Add Address"));
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();

        if (location != null || !location.equals("")) {

            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.size() > 0) {
                markerAddress = addressList.get(0);
                LatLng latLng = new LatLng(markerAddress.getLatitude(), markerAddress.getLongitude());

                if (globalMarker == null) {
                    // Marker was not set yet. Add marker:
                    globalMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(getMarkerAddress(markerAddress))
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    globalMarker.setTitle(getMarkerAddress(markerAddress));
                    globalMarker.setPosition(latLng);
                }

            } else {
                openErrorDialog("No results for ".concat(location));
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AddNewAddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onTouch() {

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        //sets the marker address
        setMarkerAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        globalMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                .title(getMarkerAddress(markerAddress))
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));
        // Fill color of the circle
        // 0x represents, this is an hexadecimal code
        // 55 represents percentage of transparency. For 100% transparency, specify 00.
        // For 0% transparency ( ie, opaque ) , specify ff
        // The remaining 6 characters(00ff00) specify the fill color
//        globalCircle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
//                .radius(100)
//                .strokeColor(0xff000000)
//                .fillColor(0x00000000));


        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    private Bitmap resizeBitmapImage(int drawable, int height, int width) {

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(drawable);
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    @Override
    public boolean onMyLocationButtonClick() {

        //sets the marker address
        setMarkerAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        globalMarker.setTitle(getMarkerAddress(markerAddress));

        if (globalMarker == null) {
            // Marker was not set yet. Add marker:
            globalMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        } else {
            globalMarker.setPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        }
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //sets the marker address
        setMarkerAddress(latLng.latitude, latLng.longitude);

        globalMarker.setTitle(getMarkerAddress(markerAddress));

        // First check if myMarker is null
        if (globalMarker == null) {
            // Marker was not set yet. Add marker:
            globalMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(getMarkerAddress(markerAddress))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        } else {
            globalMarker.setPosition(latLng);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.skip: {
                Log.d("antonhttp", "halaaaaa");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMarkerAddress(double latitude, double longitude) {
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null) {
            if (addressList.size() > 0) {
                markerAddress = addressList.get(0);
            }
        }

        Log.d("antonhttp", "THIS IS MARKER JSON: " + new Gson().toJson(markerAddress));
        Log.d("antonhttp", "THIS IS YOUR MARKER ADDRESS: " + getMarkerAddress(markerAddress));
        Log.d("antonhttp", "THIS IS YOUR ADDITIONAL ADDRESS: " + getAdditionalAddress(markerAddress));
    }

    private String getMarkerAddress(Address address) {
        String display_address = "";
        if (address != null) {

            display_address += address.getAddressLine(0) + ", ";

            for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
                display_address += address.getAddressLine(i) + ", ";
            }

            display_address = display_address.substring(0, display_address.length() - 2);
        }

        return display_address;
    }

    private String getAdditionalAddress(Address address) {
        String display_address = "";

        if (address != null) {
            if (address.getFeatureName() != null) {
                display_address += address + ", ";
            }

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                display_address += address.getAddressLine(i);
            }
        }

        return display_address;
    }

}
