package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.AddAddressActivity;
import com.ultramega.shop.adapter.settings.MyAddressesRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.GetConsumerAddressResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UltramegaShopUtilities mDb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ConsumerAddress> listAddressData;
    private MyAddressesRecyclerAdapter mAddressesAdapter;

    private String consumerid = "";
    private String userid = "";
    private String authcode = "";
    private String imei = "";
    private String sessionid = "";

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;
    private CoordinatorLayout recycler_layout;

    private NestedScrollView nested_address_scroll;

    private LinearLayout mSmoothProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_address, container, false);

        recycler_layout = (CoordinatorLayout) view.findViewById(R.id.recycler_layout);
        mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
        nested_address_scroll = (NestedScrollView) view.findViewById(R.id.nested_address_scroll);
        layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
        text_empty = (TextView) view.findViewById(R.id.text_empty);
        image_empty = (ImageView) view.findViewById(R.id.image_empty);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_address);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        ConsumerProfile itemProf = null;
        itemProf = mDb.getConsumerProfile();
        consumerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
        listAddressData = new ArrayList<>();

        Button btn_add_address = (Button) view.findViewById(R.id.btnAddAddress);
        btn_add_address.setOnClickListener(this);

        btn_add_address.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Add a new address"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_edit_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));

        listAddressData = mDb.getConsumerAddress();
        if (listAddressData.size() == 0) {
            //api session
            getSession();
        }

        mAddressesAdapter = new MyAddressesRecyclerAdapter(getViewContext(), listAddressData);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
        recyclerView.setAdapter(mAddressesAdapter);

        updateFirstLoadList(listAddressData);
        // prepareAddressData();

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
            recycler_layout.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            mAddressesAdapter.setAddressData(data);
        } else {
            recycler_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.address_arrow));
        }
    }

    private void updateList(List<ConsumerAddress> data) {
        if (data.size() > 0) {
            recycler_layout.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            mAddressesAdapter.setAddressData(data);
        } else {
            recycler_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us where you want us to deliver your items."));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.address_arrow));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDb != null) {
            updateList(mDb.getConsumerAddress());
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
                    mDb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                    List<ConsumerAddress> address = response.body().getConsumerAddress();
                    for (ConsumerAddress consumeraddress : address) {
                        mDb.insertConsumerAddress(consumeraddress);
                    }
                    updateList(mDb.getConsumerAddress());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddAddress: {
                AddAddressActivity.start(getViewContext(), 1, "SETTINGS");
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }

}

