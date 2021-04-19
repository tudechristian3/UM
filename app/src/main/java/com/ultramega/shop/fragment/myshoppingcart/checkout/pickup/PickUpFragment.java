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

import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 8/18/2017.
 */

public class PickUpFragment extends BaseFragment implements CustomMapFragment.OnTouchListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private GPSTracker gpsTracker;
    private Geocoder geocoder;
    private GoogleMap mMap;

    private final int marker_height = 60;
    private final int marker_width = 60;

    private Marker globalMarker;
    private Address markerAddress;

    private List<Address> addressList;
    private List<Address> currentAddresslist;

    private UltramegaShopUtilities mdb;
    private List<Branches> mGridData;

    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView draggableImage;
    private RecyclerView recyclerView;

    private String imei = "";
    private String usertype = "";
    private String itemtype = "";
    private String authcode = "";
    private String sessionid = "";
    private String userid = "";
    private String processoption = "";
    private String consumerid = "";

    private double current_latitude;
    private double current_longitude;

    private BranchesRecyclerAdapter mAdapter;
    private HashMap<Marker, Branches> branchMarker;

    private Branches selectedItem;
    MapFragment mMapFragment ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_up, container, false);
        setHasOptionsMenu(true);

        //set up title
        ((PickUpActivity) getViewContext()).setActionBarTitle("Checkout");

        //initialize empty data
        selectedItem = null;
        branchMarker = new HashMap<>();
        currentAddresslist = new ArrayList<>();
        addressList = new ArrayList<>();
        mGridData = new ArrayList<>();
        mdb = new UltramegaShopUtilities(getViewContext());
        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        userid = itemProf.getUserID();
        consumerid = itemProf.getConsumerID();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_add_address);
        draggableImage = (ImageView) view.findViewById(R.id.draggableImage);
        draggableImage.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.ic_keyboard_arrow_up));
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.pick_up_content);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        setupBottomSheet();

        gpsTracker = new GPSTracker(getViewContext());
        geocoder = new Geocoder(getViewContext());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            CustomMapFragment mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.setListener(this);
            mapFragment.getMapAsync(this);
            //relocate my location button position at the bottom right
            relocateButtonPosition(mapFragment);
        }

        return view;
    }

    private void relocateButtonPosition(CustomMapFragment mapFragment) {
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 100, 30, 0);
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
                    ((PickUpActivity) getViewContext()).setBranch(2, selectedItem);
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
        current_latitude = gpsTracker.getLatitude();
        current_longitude = gpsTracker.getLongitude();

        //sets the marker address
        setMarkerAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        globalMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_consumer, marker_height, marker_width))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));

        mGridData = mdb.getBranches();
        if (mGridData.size() == 0) {
            getSession();
        } else {
            for (int i = 0; i < mGridData.size(); i++) {
                branchMarker.put(placeMarker(mGridData.get(i), 60, 60), mGridData.get(i));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        //mAdapter = new BranchesRecyclerAdapter(getViewContext(), mGridData, gpsTracker.getLatitude(), gpsTracker.getLongitude(), PickUpFragment.this);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mAdapter);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(this);
//        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) getViewContext());
    }

    private Marker placeMarker(Branches item, int height, int width) {
        Marker m = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude())))
                .title(item.getBranchName())
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmapImage(R.drawable.map_branch, height, width))));
        return m;
    }

    private Bitmap resizeBitmapImage(int drawable, int height, int width) {

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(drawable);
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
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

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
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
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getBranches(getBranchesSession);
                } else {
                    hideProgressDialog();
                    showError(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getBranches(Callback<GetBranchesResponse> getBranchesCallback) {

        //assign this @processoption variable to its corresponding value

        Call<GetBranchesResponse> getbranches = RetrofitBuild.getBranchesService(getViewContext())
                .getBranchesForRetailer(
                        authcode,
                        imei,
                        sessionid,
                        "DELIVERY");
        getbranches.enqueue(getBranchesCallback);
    }

    private final Callback<GetBranchesResponse> getBranchesSession = new Callback<GetBranchesResponse>() {

        @Override
        public void onResponse(Call<GetBranchesResponse> call, Response<GetBranchesResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_BRANCHES);
                    List<Branches> listbranches = response.body().getGetBranches();
                    for (Branches branches : listbranches) {
                        mdb.insertAllBranches(branches, getDistance(current_latitude, current_longitude, branches));
                    }
                    mGridData = mdb.getBranches();
                    for (int i = 0; i < mGridData.size(); i++) {
                        branchMarker.put(placeMarker(mGridData.get(i), 60, 60), mGridData.get(i));
                    }
                    updateList(mGridData);
                }
            }
        }

        @Override
        public void onFailure(Call<GetBranchesResponse> call, Throwable t) {
            hideProgressDialog();
        }
    };

    private void updateList(List<Branches> data) {
        if (data.size() > 0) {
            mAdapter.setPickUpBranchesData(data);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Branches item = branchMarker.get(marker);
        selectedItem = item;
        return false;
    }

    public void selectBranchMarker(final Branches item) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        selectedItem = item;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getViewContext(), "You clicked: " + item.getBranchName(), Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude())), 15));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(item.getLatitude()), Double.valueOf(item.getLongitude()))));
            }
        }, 400);
    }

}
