package com.ultramega.shop.fragment.register;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.util.CustomMapFragment;
import com.ultramega.shop.util.GPSTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User-PC on 9/29/2017.
 */

public class SetUpWholesalerAddressFragment extends BaseFragment implements View.OnClickListener, CustomMapFragment.OnTouchListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapLongClickListener {

    private LinearLayout hintlayout;
    private LinearLayout hintsetlayout;
    private TextView txvaddress;
    private Button btnsetaddress;

    private GPSTracker gpsTracker;
    private Geocoder geocoder;
    private GoogleMap mMap;

    private final int marker_height = 60;
    private final int marker_width = 60;

    private Marker globalMarker;

    private List<Address> currentAddresslist;

    private Address markerAddress;

    private View view;

    //private String intentType = "";
    private String mobilenumber = "";
    private String countrycode = "";
    private String country = "";
    private String authenticationid = "";
    private String processtype = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null)
//                parent.removeView(view);
//        }
        try {
            view = inflater.inflate(R.layout.fragments_setup_address, container, false);
            //setHasOptionsMenu(true);

            init(view);

            //set title
            //((AddAddressActivity) getViewContext()).setActionBarTitle("Add Address");

            if (!Places.isInitialized()) {
                Places.initialize(getViewContext(), CommonFunctions.placesAPIKEY);
            }

            AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                    getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setCountry("PH");

            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,
                    Place.Field.ADDRESS));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    hintlayout.setVisibility(View.GONE);
                    hintsetlayout.setVisibility(View.VISIBLE);
                    txvaddress.setText(place.getAddress());
                    setPlaceMarker(place);
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.d("antonhttp", "An error occurred: " + status);
                    openErrorDialog("An error occurred: " + status);
                }
            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isAdded()) {
                        setUpMapView();
                    }

                }
            }, 400);


        } catch (InflateException e) {
            e.printStackTrace();
        /* map is already there, just return view as it is */
        }

        return view;
    }

    private void setUpMapView() {
        //=============================================================
        // SET UP MAPS
        // * if precise address, append code .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
        // * country to PH
        //=============================================================
        gpsTracker = new GPSTracker(getViewContext());
        geocoder = new Geocoder(getViewContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            CustomMapFragment mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.setListener(this);
            mapFragment.getMapAsync(this);
            //relocate my location button position at the bottom right
            //relocateButtonPosition(mapFragment);
        }
    }

    private void relocateButtonPosition(CustomMapFragment mapFragment) {
        try {
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 100, 30, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        mobilenumber = getArguments().getString("MobileNumber");
        countrycode = getArguments().getString("CountryCode");
        country = getArguments().getString("Country");
        authenticationid = getArguments().getString("AuthenticationID");
        processtype = getArguments().getString("processtype");
        //intentType = getArguments().getString("intenttype");
        currentAddresslist = new ArrayList<>();
        hintlayout = (LinearLayout) view.findViewById(R.id.hintlayout);
        hintlayout.setVisibility(View.VISIBLE);
        hintsetlayout = (LinearLayout) view.findViewById(R.id.hintsetlayout);
        txvaddress = (TextView) view.findViewById(R.id.txvaddress);
        btnsetaddress = (Button) view.findViewById(R.id.btnsetaddress);
        btnsetaddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsetaddress: {
                if (markerAddress != null) {
                    Bundle args = new Bundle();
                    args.putString("AuthenticationID", authenticationid);
                    args.putString("processtype", processtype);
                    args.putString("MobileNumber", mobilenumber);
                    args.putString("CountryCode", countrycode);
                    args.putString("Country", country);
                    args.putParcelable("address", markerAddress);
                    ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(6, args);

                    //((ConsumerWholesalerSignUpActivity) getViewContext()).setAddress(6, mobilenumber, countrycode, country, markerAddress);
                } else {
                    openErrorDialog("Address not found");
                }
                //((AddAddressActivity) getViewContext()).setAddress(2, markerAddress, intentType);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
//            case R.id.skip: {
//                ((AddAddressActivity) getViewContext()).setAddress(2, markerAddress);
//                return true;
//            }
            case android.R.id.home: {
                Bundle args = new Bundle();
                args.putString("AuthenticationID", authenticationid);
                args.putString("processtype", processtype);
                args.putString("MobileNumber", mobilenumber);
                args.putString("CountryCode", countrycode);
                args.putString("Country", country);
                ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(3, args);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTouch() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getViewContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getViewContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12.0f));

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

        setMarkerAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        if (globalMarker == null) {
            globalMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                    .title(getMarkerAddress(markerAddress))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        } else {
            globalMarker.setPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        }

        hintlayout.setVisibility(View.GONE);
        hintsetlayout.setVisibility(View.VISIBLE);
        txvaddress.setText(getMarkerAddress(markerAddress));

        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //sets the marker address
        setMarkerAddress(latLng.latitude, latLng.longitude);

        globalMarker.setTitle(getMarkerAddress(markerAddress));

        if (globalMarker == null) {
            // Marker was not set yet. Add marker:
            globalMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        } else {
            globalMarker.setPosition(latLng);
        }

        hintlayout.setVisibility(View.GONE);
        hintsetlayout.setVisibility(View.VISIBLE);
        txvaddress.setText(getMarkerAddress(markerAddress));
    }

    public void setPlaceMarker(Place place) {
        if (globalMarker == null) {
            // Marker was not set yet. Add marker:
            globalMarker = mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng())
                    .title(String.valueOf(place.getAddress()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
        } else {
            globalMarker.setTitle(String.valueOf(place.getAddress()));
            globalMarker.setPosition(place.getLatLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
        }

        LatLng latLng = place.getLatLng();
        try {
            currentAddresslist = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (currentAddresslist.size() > 0) {
            markerAddress = currentAddresslist.get(0);
        }

    }

    private void setMarkerAddress(double latitude, double longitude) {
        try {
            currentAddresslist = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (currentAddresslist != null) {
            if (currentAddresslist.size() > 0) {
                markerAddress = currentAddresslist.get(0);
            }
        }
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
}
