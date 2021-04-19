package com.ultramega.shop.fragment.myshoppingcart.checkout.delivery;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.AddAddressActivity;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.activity.SelectedAddress;
import com.ultramega.shop.adapter.myshoppingcart.SelectOrderDeliveryAddressRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.GetConsumerAddressResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddressFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UltramegaShopUtilities mdb;
    private List<ConsumerAddress> listAddressData;
    private RecyclerView recyclerView;
    private SelectOrderDeliveryAddressRecyclerAdapter addressAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String consumerid = "";
    private String imei = "";
    private String userid = "";

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private NestedScrollView nested_address_scroll;

    private LinearLayout mSmoothProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        setHasOptionsMenu(true);

        mSmoothProgressBar = view.findViewById(R.id.mSmoothProgressBar);
        nested_address_scroll = view.findViewById(R.id.nested_address_scroll);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        layout_empty = view.findViewById(R.id.layout_empty);
        text_empty = view.findViewById(R.id.text_empty);
        image_empty = view.findViewById(R.id.image_empty);

        //set up title
        ((CheckoutProductsActivity) getViewContext()).setActionBarTitle("Checkout");

        //initialize empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        ConsumerProfile itemProf = null;
        itemProf = mdb.getConsumerProfile();
        consumerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
//        sessionid = UserPreferenceManager.getSession(getViewContext());
//        Toast.makeText(getViewContext(), sessionid, Toast.LENGTH_SHORT).show();
        listAddressData = new ArrayList<>();

        Button btn_add_address = view.findViewById(R.id.btnAddAddress);
        btn_add_address.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Add a new address"));
        btn_add_address.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        listAddressData = mdb.getConsumerAddress();
        if (listAddressData.size() == 0) {
            //api session
            getSession();
        }
        addressAdapter = new SelectOrderDeliveryAddressRecyclerAdapter(getViewContext(), listAddressData);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
        recyclerView.setAdapter(addressAdapter);

        updateFirstLoadList(listAddressData);

        nested_address_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //Log.d("antonhttp", "Scroll DOWN");
                    mSwipeRefreshLayout.setEnabled(false);
                }
                if (scrollY < oldScrollY) {
                    //Log.d("antonhttp", "Scroll UP");
                    mSwipeRefreshLayout.setEnabled(false);
                }

                if (scrollY == 0) {
                    //Log.d("antonhttp", "TOP SCROLL");
                    mSwipeRefreshLayout.setEnabled(true);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //Log.d("antonhttp", "======BOTTOM SCROLL=======");
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        return view;
    }

    private void updateFirstLoadList(List<ConsumerAddress> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            addressAdapter.setSelectOrderDeliveryAddressData(data);
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.address_arrow));
        }
    }

    private void updateList(List<ConsumerAddress> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            addressAdapter.setSelectOrderDeliveryAddressData(data);
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us where you want us to deliver your items."));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.address_arrow));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    String sessionid = response.body().getSession();
                    String authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getConsumerAddress(getConsumerAddressSession, consumerid, userid, authcode, imei, sessionid);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    //hideProgressDialog();
                    showError(response.body().getMessage());
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                //hideProgressDialog();
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            //hideProgressDialog();
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            //showProgressDialog();
            //api session
            createSession(callsession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private void getConsumerAddress(Callback<GetConsumerAddressResponse> getConsumerAddressCallback, String consumerid, String userid, String authcode, String imei, String sessionid) {
        Call<GetConsumerAddressResponse> getconsumeraddress = RetrofitBuild.getConsumerAddressService(getViewContext()).getConsumerAddressCall(consumerid, userid, authcode, imei, sessionid);
        getconsumeraddress.enqueue(getConsumerAddressCallback);
    }

    private final Callback<GetConsumerAddressResponse> getConsumerAddressSession = new Callback<GetConsumerAddressResponse>() {

        @Override
        public void onResponse(Call<GetConsumerAddressResponse> call, Response<GetConsumerAddressResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            //hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                    List<ConsumerAddress> address = response.body().getConsumerAddress();
                    for (ConsumerAddress consumeraddress : address) {
                        mdb.insertConsumerAddress(consumeraddress);
                    }
                    updateList(mdb.getConsumerAddress());
                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetConsumerAddressResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            //hideProgressDialog();
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_skip, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.skip: {
//                sessionid = UserPreferenceManager.getSession(getViewContext());
//                Toast.makeText(getViewContext(), "sessionid" + sessionid, Toast.LENGTH_SHORT).show();
                String detailedaddress = UserPreferenceManager.getStringPreference(getViewContext(), "SelectedAddress");
                SelectedAddress selected = new Gson().fromJson(detailedaddress, SelectedAddress.class);

                if (detailedaddress.equals("")) {
                    openErrorDialog("Address is required");
                } else {
                    ((CheckoutProductsActivity) getViewContext()).setAddress(1, selected.getDetailedAddress(), selected.getLatitude(), selected.getLongitude());
                }

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddAddress: {

                if (isAirplaneModeOn(getViewContext())) {
                    showError("Airplane mode is enabled. Please disable Airplane mode and enable GPS or Internet to proceed.");
                } else {
                    if(CommonFunctions.getConnectivityStatus(getViewContext()) > 0){
                        if (isGPSModeOn()) {
                            AddAddressActivity.start(getViewContext(), 1, "CHECKOUT");
                        } else {
                            showGPSDisabledAlertToUser();
                        }
                    } else {
                        showError(getString(R.string.generic_internet_error_message));
                    }
                }

                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mdb != null) {
            updateList(mdb.getConsumerAddress());
        }
    }
}
