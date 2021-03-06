package com.ultramega.shop.fragment.myshoppingcart.checkout.pickup;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.PickUpActivity;
import com.ultramega.shop.adapter.myshoppingcart.BranchesRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.GetBranchesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.CustomMapFragment;
import com.ultramega.shop.util.GPSTracker;
import com.ultramega.shop.util.UserPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 10/2/2017.
 */

public class SetUpPickUpBranchFragment extends BaseFragment implements CustomMapFragment.OnTouchListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener, View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private GPSTracker gpsTracker;
    private Geocoder geocoder;
    private GoogleMap mMap;

    private final int marker_height = 60;
    private final int marker_width = 60;
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    //private String processoption = "";
    private String userid = "";
    private String consumerid = "";

    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView draggableImage;
    private RecyclerView recyclerView;

    private UltramegaShopUtilities mdb;
    private List<Branches> mGridData = new ArrayList<>();
    private List<Address> currentAddresslist;
    private double current_latitude;
    private double current_longitude;
    private BranchesRecyclerAdapter mAdapter;

    private Marker globalMarker;
    private Address markerAddress;
    private HashMap<Marker, Branches> branchMarker;
    private Branches selectedItem;

    private LinearLayout mSmoothProgressBar;
    private TextView txvhintinstruction;
    private TextView txvbracheslabel;

    private View view;

    private ImageView refresh;
    //private ImageView search;
    private SearchView searchbox;

    private String processoption = "PICKUP";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments().getString("processoption") != null){
            processoption = getArguments().getString("processoption");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        try {
            view = inflater.inflate(R.layout.fragment_setup_pickup_address, container, false);

            init(view);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //===========================================
                    //check & execute if activity is still active
                    //===========================================
                    if (isAdded()) {
                        setUpMapView();
                    }
                }
            }, 400);

        } catch (Exception e) {
            e.printStackTrace();
        /* map is already there, just return view as it is */
        }

        validateBranches();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_skip, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.skip: {
                if (selectedItem != null) {
                    UserPreferenceManager.saveBranchID(getContext(), selectedItem.getBranchID());
                    ((PickUpActivity) getViewContext()).setBranch(2, selectedItem);
//                    Toast.makeText(getContext(), "Select" + selectedItem, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Select" + selectedItem.getBranchID(), Toast.LENGTH_SHORT).show();
                } else {
                    openErrorDialog("Please select a branch");
                }
//                ((PickUpActivity) getViewContext()).setAddress(2, markerAddress);
                return true;
            }
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpMapView() {
        //=============================================================
        // SET UP MAPS
        //
        //=============================================================
        gpsTracker = new GPSTracker(getViewContext());
        geocoder = new Geocoder(getViewContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            CustomMapFragment mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.setListener(this);
            mapFragment.getMapAsync(this);
        }
    }

    private void init(View view) {
        mdb = new UltramegaShopUtilities(getViewContext());
        searchbox = (SearchView) view.findViewById(R.id.searchbox);
        searchbox.setOnQueryTextListener(this);
        searchbox.setOnSearchClickListener(this);
        searchbox.setOnCloseListener(this);
        //search = (ImageView) view.findViewById(R.id.search);
        //search.setOnClickListener(this);
        refresh = (ImageView) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
        selectedItem = null;
        imei = CommonFunctions.getIMEI(getViewContext());
        branchMarker = new HashMap<>();
        mGridData = new ArrayList<>();
        currentAddresslist = new ArrayList<>();
        ConsumerProfile consumerProfile = mdb.getConsumerProfile();
        userid = consumerProfile.getUserID();
        consumerid = consumerProfile.getConsumerID();

        txvbracheslabel = (TextView) view.findViewById(R.id.txvbracheslabel);
        txvbracheslabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "ULTRAMEGA BRANCHES"));
        txvhintinstruction = (TextView) view.findViewById(R.id.txvhintinstruction);
        txvhintinstruction.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Select what branch you want to pick up your items."));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_add_address);
        draggableImage = (ImageView) view.findViewById(R.id.draggableImage);
        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_up));

        //setup bottom sheet
        View bview = view.findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bview);
        setupBottomSheet();
    }

    private void setupBottomSheet() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_up));
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_down));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_down));
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_down));
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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

        //GET CURRENT LATITUDE AND LONGITUDE
        current_latitude = gpsTracker.getLatitude();
        current_longitude = gpsTracker.getLongitude();

        //sets the marker address
        setMarkerAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        //SET MARKER CURRENT LOCATION
        globalMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12.0f));

        //SETUP RECYCLERVIEW LIST
        mGridData = mdb.getBranches();
        getSession();
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BranchesRecyclerAdapter(getViewContext(), mGridData, gpsTracker.getLatitude(), gpsTracker.getLongitude(), SetUpPickUpBranchFragment.this);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mAdapter);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
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

    private Bitmap resizeBitmapImage(int drawable, int height, int width) {

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(drawable);
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Branches item = branchMarker.get(marker);
        selectedItem = item;
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    //API CALLS
    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(callsession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }



    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);

                    getBranches(getBranchesCallback);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void validateBranches() {
        if (CommonFunctions.getConnectivityStatus(getActivity()) > 0) {
            getBranches(getBranchesCallback);
        } else {
            Toast.makeText(getContext(), "qwe", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBranches(Callback<GetBranchesResponse> getBranchesCallback) {
        Call<GetBranchesResponse> getbranches = RetrofitBuild.getBranchesService(getViewContext())
                .getBranchesForRetailer(authcode,
                        imei,
                        sessionid,
                         processoption);
        getbranches.enqueue(getBranchesCallback);
    }

    private final Callback<GetBranchesResponse> getBranchesCallback = new Callback<GetBranchesResponse>() {

        @Override
        public void onResponse(Call<GetBranchesResponse> call, Response<GetBranchesResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_BRANCHES);

                    List<Branches> listbranches = response.body().getGetBranches();
                    for (Branches branches : listbranches) {
                        mdb.insertAllBranches(branches, getDistance(current_latitude, current_longitude, branches));
                    }

                    mGridData = new ArrayList<>();
                    branchMarker = new HashMap<>();
                    mGridData = mdb.getBranches();
                    for (int i = 0; i < mGridData.size(); i++) {
                        Log.d("neil","BRANCHES: "+i+" : "+ new Gson().toJson(mGridData.get(i)));
                        branchMarker.put(placeMarker(mGridData.get(i), 60, 60), mGridData.get(i));
                    }
                    updateList(mGridData);
                }
            }
        }

        @Override
        public void onFailure(Call<GetBranchesResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    };

    private Marker placeMarker(Branches item, int height, int width) {
        Marker m = null;
        try {
            m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude())))
                    .title(item.getBranchName())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_branch, height, width))));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return m;
    }

    private void updateList(List<Branches> data) {
        if (data.size() > 0) {
            mAdapter.setPickUpBranchesData(data);
        }
    }

    public void selectBranchMarker(final Branches item) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        selectedItem = item;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //Toast.makeText(getViewContext(), "You clicked: " + item.getBranchName(), Toast.LENGTH_SHORT).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude())), 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude()))));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }, 400);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh: {
                mSmoothProgressBar.setVisibility(View.VISIBLE);
                getSession();
                break;
            }
//            case R.id.search: {
//
//                break;
//            }
            case R.id.searchbox: {
                txvbracheslabel.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        return true;
    }

    @Override
    public boolean onClose() {
        txvbracheslabel.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        return false;
    }
}
