package com.ultramega.shop.fragment.myorders;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.myorders.MyOrdersRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetOrdersQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 7/18/2017.
 */

public class ItemOrdersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String KEY_ORDER_FRAGMENT = "title";
    //    public static final int mNofItems = 9;
    public static final int mNofItems = 2;

    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyOrdersRecyclerAdapter mPendingAdapter;
    private RecyclerView recyclerView;

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String userid = "";
    private String status = "";
    private int limit;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private List<OrdersQueue> mGridData;

    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;
    private boolean isfirstload = true;
    private boolean isLoading = false;

    private LinearLayout mSmoothProgressBar;

    private boolean isRefreshAll;

    //VIEW ARCHIVE
    private LinearLayout viewarchivelayoutv2;
    private TextView viewarchivev2;
    private LinearLayout viewarchivelayout;
    private TextView viewarchive;
    private MaterialDialog mViewArchiveDialog;
    private MaterialDialog mFilterOptionDialog;
    private List<String> MONTHS = new ArrayList<>();
    private List<String> YEARS = new ArrayList<>();
    private int MIN_MONTH = 1;
    private int MIN_YEAR = 2017;
    private MaterialEditText edtMonth;
    private MaterialEditText edtYear;
    private int year = 0;
    private int month = 0;

    private boolean isSearch = false;

    public static ItemOrdersFragment newInstance(String value) {
        ItemOrdersFragment fragment = new ItemOrdersFragment();
        Bundle b = new Bundle();
        b.putString(KEY_ORDER_FRAGMENT, value);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        try {

            status = getArguments().getString(KEY_ORDER_FRAGMENT);

            init(view);

            initData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void init(View view) {
        mSmoothProgressBar = view.findViewById(R.id.mSmoothProgressBar);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        layout_empty = view.findViewById(R.id.layout_empty);
        text_empty = view.findViewById(R.id.text_empty);
        image_empty = view.findViewById(R.id.image_empty);
        recyclerView = view.findViewById(R.id.recycler_view_order);

        //Log.d("antonhttp", "IS COMPLETED? " + status.toUpperCase());
        if (status.toUpperCase().equals("COMPLETED")) {
            //VIEW ARCHIVE
            viewarchivelayoutv2 = view.findViewById(R.id.viewarchivelayoutv2);
            viewarchivev2 = view.findViewById(R.id.viewarchivev2);
            viewarchivev2.setOnClickListener(this);
            viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
            viewarchivelayout = view.findViewById(R.id.viewarchivelayout);
            viewarchive = view.findViewById(R.id.viewarchive);
            viewarchive.setOnClickListener(this);
            viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
        }
    }

    private void initData() {
        // initialize with empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        mGridData = new ArrayList<>();
        mGridData = mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase());
        isbottomscroll = false;
        isloadmore = true;
        limit = getLimit(mGridData.size(), 30);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);

        isRefreshAll = false;
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            MIN_MONTH = Integer.parseInt(CommonFunctions.getMonthFromDate(itemProf.getDateTimeRegistered()));
            MIN_YEAR = Integer.parseInt(CommonFunctions.getYearFromDate(itemProf.getDateTimeRegistered()));
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            MIN_MONTH = Integer.parseInt(CommonFunctions.getMonthFromDate(wholesalerProfile.getDateTimeAdded()));
            MIN_YEAR = Integer.parseInt(CommonFunctions.getYearFromDate(wholesalerProfile.getDateTimeAdded()));
        }

        //SET UP ARCHIVE DIALOG
        setUpViewArchiveDialog();
        setUpFilterOptions();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setItemAnimator(new SlideInUpAnimator());
                mPendingAdapter = new MyOrdersRecyclerAdapter(getViewContext(), mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()), status.replaceAll("\\s+", "").toUpperCase(), usertype);

                if (mGridData.size() == 0) {
                    getSession();
                }

                recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                recyclerView.setAdapter(mPendingAdapter);

                updateFirstLoadList(mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()));
            }
        }, 400);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    isbottomscroll = true;
                    if (isloadmore) {
                        if (!isfirstload) {
                            limit = limit + 30;
                        }
                        getSession();
                    }
                }
            }
        });
    }

    private void updateFirstLoadList(List<OrdersQueue> data) {
        if (data.size() > 0) {
            if (status.toUpperCase().equals("COMPLETED")) {
                viewarchivelayoutv2.setVisibility(View.GONE);
                viewarchivelayout.setVisibility(View.VISIBLE);
            }
            layout_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (mPendingAdapter != null) {
                mPendingAdapter.setPendingData(data);
            }
        } else {
            if (status.toUpperCase().equals("COMPLETED")) {
                viewarchivelayoutv2.setVisibility(View.VISIBLE);
                viewarchivelayout.setVisibility(View.GONE);
            }
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updateList(List<OrdersQueue> data) {
        if (data.size() > 0) {
            if (status.toUpperCase().equals("COMPLETED")) {
                viewarchivelayoutv2.setVisibility(View.GONE);
                viewarchivelayout.setVisibility(View.VISIBLE);
            }
            layout_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (mPendingAdapter != null) {
                mPendingAdapter.setPendingData(data);
            }
        } else {
            if (status.toUpperCase().equals("COMPLETED")) {
                viewarchivelayoutv2.setVisibility(View.VISIBLE);
                viewarchivelayout.setVisibility(View.GONE);
            }
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    if (isRefreshAll) {
                        getOrdersQueue(getOrdersQueueSession);
                        getOrdersHistory(getOrdersHistorySession);
                    } else {
                        if (status.replaceAll("\\s+", "").toUpperCase().equals("PENDING")) {
                            getOrdersQueue(getOrdersQueueSession);
                        } else {
                            getOrdersHistory(getOrdersHistorySession);
                        }
                    }
                } else {
                    isLoading = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                isLoading = false;
                mSmoothProgressBar.setVisibility(View.GONE);
                if (isAdded()) {
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            Log.d("neil","SESSOIN SHIT ERROR : "+t.getMessage());
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private void getOrdersHistory(Callback<GetOrdersQueueResponse> getOrdersHistoryCallback) {
        Call<GetOrdersQueueResponse> fetchitems = RetrofitBuild.getOrdersQueueService(getViewContext())
                .getOrdersHistoryCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        String.valueOf(limit),
                        userid,
                        year,
                        month,
                        String.valueOf(isSearch));
        fetchitems.enqueue(getOrdersHistoryCallback);
    }

    private final Callback<GetOrdersQueueResponse> getOrdersHistorySession = new Callback<GetOrdersQueueResponse>() {

        @Override
        public void onResponse(Call<GetOrdersQueueResponse> call, Response<GetOrdersQueueResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            ResponseBody errorBody = response.errorBody();

            isLoading = false;
            isfirstload = false;
            isSearch = false;

            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getOrdersQueue().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                    }
                    //List<OrdersQueue> orderitems = response.body().getOrdersQueue();
                    List<OrdersQueue> orderitems = response.body().getData();
                    for (OrdersQueue order : orderitems) {
                        mdb.insertOrdersHistory(order, usertype);
                    }

                    if (mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()).size() > 0) {
                        layout_empty.setVisibility(View.GONE);
                    } else {
                        layout_empty.setVisibility(View.VISIBLE);
                        text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
                    }
                    if (isAdded()) {
                        updateList(mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()));
                    }
                } else {
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                if (isAdded()) {
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetOrdersQueueResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            t.printStackTrace();
            Log.d("neil","GET ORDERS HISTORY ERROR : "+t.getMessage());
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private void getOrdersQueue(Callback<GetOrdersQueueResponse> getOrdersQueueCallback) {
        Log.d("roneldayanan","GetORDERS QUEUE 3");
        Call<GetOrdersQueueResponse> fetchitems = RetrofitBuild.getOrdersQueueService(getViewContext())
                .getOrdersQueueCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        String.valueOf(limit),
                        userid,
                        year,
                        month);
        fetchitems.enqueue(getOrdersQueueCallback);
    }

    private final Callback<GetOrdersQueueResponse> getOrdersQueueSession = new Callback<GetOrdersQueueResponse>() {

        @Override
        public void onResponse(Call<GetOrdersQueueResponse> call, Response<GetOrdersQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isLoading = false;
            isfirstload = false;

            if (errorBody == null) {

                isloadmore = Objects.requireNonNull(response.body()).getOrdersQueue().size() > 0;

                if (!isbottomscroll) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                }

                //List<OrdersQueue> orderitems = response.body().getOrdersQueue();
                List<OrdersQueue> orderitems = response.body().getData();
                for (OrdersQueue order : orderitems) {
                    Log.d("neil","ORDER : "+new Gson().toJson(order));
                    //======================================
                    // VALIDATE DATA PASSED FROM THE SERVER
                    //======================================
                    try {
                        if (!(order.getStatus().matches(".*\\d.*") || order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED"))) {
                            mdb.insertOrdersQueue(order, usertype);
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        Log.d("neil","NULL SHIT ERROR : "+e.getMessage());
                    }
                }

                if (mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()).size() > 0) {
                    layout_empty.setVisibility(View.GONE);
                } else {
                    layout_empty.setVisibility(View.VISIBLE);
                    text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
                }
                if (isAdded()) {
                    updateList(mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()));
                }


            }
        }

        @Override
        public void onFailure(Call<GetOrdersQueueResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            t.printStackTrace();
            Log.d("neil","GET ORDERS QUEUE ERROR : "+t.getMessage());
            openErrorDialog("Something went wrong with the server.");
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (status.toUpperCase().equals("COMPLETED")) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.VISIBLE);
        }

        if (CommonFunctions.hasNewOrderUpdate) {
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.GONE);
            limit = 0;
            isbottomscroll = false;
            isloadmore = true;
            isRefreshAll = true;
            isfirstload = false;
            isSearch = false;
            month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            year = Calendar.getInstance().get(Calendar.YEAR);

            if (mdb != null) {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
            }

            if (mPendingAdapter != null) {
                mPendingAdapter.clear();
            }
            getSession();

        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mPendingAdapter != null) {
                        mPendingAdapter.clear();
                    }

                    updateList(mdb.getOrders(status.replaceAll("\\s+", "").toUpperCase()));
                }
            }, 200);
        }

        CommonFunctions.hasNewOrderUpdate = false;
    }

    @Override
    public void onRefresh() {

        if (status.toUpperCase().equals("COMPLETED")) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.GONE);
        }

        if (mdb != null) {
            mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDER_PAYMENTS);
        }
        if (mPendingAdapter != null) {
            mPendingAdapter.clear();
        }
        recyclerView.setVisibility(View.GONE);
        layout_empty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        isRefreshAll = false;
        isfirstload = false;
        isSearch = false;
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        getSession();
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //API CALL
            isLoading = true;
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(callsession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewarchive: {
                if (viewarchive.getText().toString().equals(getString(R.string.string_view_archive))) {
                    mViewArchiveDialog.show();
                } else if (viewarchive.getText().toString().equals(getString(R.string.string_filter_options))) {
                    mFilterOptionDialog.show();
                }
                break;
            }
            case R.id.txvDateClose: {
                mViewArchiveDialog.dismiss();
                break;
            }
            case R.id.txvFilter: {
                if (!edtMonth.getText().toString().isEmpty() &&
                        !edtYear.getText().toString().isEmpty()) {
                    mViewArchiveDialog.dismiss();

                    if (viewarchivev2.getText().toString().equals(getString(R.string.string_view_archive))) {
                        viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_filter_options)));
                    }

                    if (viewarchive.getText().toString().equals(getString(R.string.string_view_archive))) {
                        viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_filter_options)));
                    }

                    if (mdb != null) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                    }

                    if (mPendingAdapter != null) {
                        mPendingAdapter.clear();
                    }

                    recyclerView.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.GONE);
                    limit = 0;
                    isbottomscroll = false;
                    isloadmore = true;
                    isRefreshAll = false;
                    isfirstload = false;
                    isSearch = true;
                    getSession();
                }
                break;
            }
            case R.id.edtMonth: {
                new MaterialDialog.Builder(getViewContext())
                        .title("Select Month")
                        .items(MONTHS)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                month = Integer.parseInt(CommonFunctions.getMonthNumber(text.toString()));
                                edtMonth.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", text.toString()));
                            }
                        })
                        .show();
                break;
            }
            case R.id.edtYear: {
                new MaterialDialog.Builder(getViewContext())
                        .title("Select Year")
                        .items(YEARS)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                year = Integer.parseInt(text.toString());
                                MONTHS.clear();
                                int minMonth = year == MIN_YEAR ? MIN_MONTH : 1;
                                MONTHS = setUpMonth(minMonth);
                                edtYear.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", text.toString()));
                            }
                        })
                        .show();
                break;
            }
            case R.id.txvEdtSearches: {
                mFilterOptionDialog.dismiss();
                mViewArchiveDialog.show();
                break;
            }
            case R.id.txvClearSearches: {

                edtMonth.setText("");
                edtYear.setText("");

                if (mdb != null) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                }

                if (mPendingAdapter != null) {
                    mPendingAdapter.clear();
                }

                mFilterOptionDialog.dismiss();

                recyclerView.setVisibility(View.GONE);
                layout_empty.setVisibility(View.GONE);
                limit = 0;
                isbottomscroll = false;
                isloadmore = true;
                isRefreshAll = false;
                isfirstload = false;
                isSearch = false;
                month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                year = Calendar.getInstance().get(Calendar.YEAR);
                getSession();

                viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
                viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));

                break;
            }
            case R.id.viewarchivev2: {
                if (viewarchivev2.getText().toString().equals(getString(R.string.string_view_archive))) {
                    mViewArchiveDialog.show();
                } else if (viewarchivev2.getText().toString().equals(getString(R.string.string_filter_options))) {
                    mFilterOptionDialog.show();
                }
                break;
            }
        }
    }

    private void setUpViewArchiveDialog() {
        mViewArchiveDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_date_view_archive, false)
                .build();

        //SET UP MONTH
        MONTHS = setUpMonth(MIN_MONTH);

        //SET UP YEAR
        YEARS = setUpYear(MIN_YEAR);

        View view = mViewArchiveDialog.getCustomView();
        TextView txvDateClose = view.findViewById(R.id.txvDateClose);
        txvDateClose.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "CANCEL"));
        txvDateClose.setOnClickListener(this);
        TextView txvFilter = view.findViewById(R.id.txvFilter);
        txvFilter.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "FILTER"));
        txvFilter.setOnClickListener(this);

        edtMonth = view.findViewById(R.id.edtMonth);
        edtMonth.setOnClickListener(this);
        edtYear = view.findViewById(R.id.edtYear);
        edtYear.setOnClickListener(this);
    }

    private void setUpFilterOptions() {
        mFilterOptionDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_date_filter_options, false)
                .build();
        View view = mFilterOptionDialog.getCustomView();
        TextView txvEdtSearches = view.findViewById(R.id.txvEdtSearches);
        txvEdtSearches.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "EDIT SEARCHES"));
        txvEdtSearches.setOnClickListener(this);
        TextView txvClearSearches = view.findViewById(R.id.txvClearSearches);
        txvClearSearches.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "CLEAR SEARCHES"));
        txvClearSearches.setOnClickListener(this);
    }

    private List<String> setUpMonth(int minMonth) {
        //==================================
        // DateFormatSymbols().getMonths()
        // - Gets months strings.
        // e.g ( ["January","February","March","April","May","June","July","August","September","October","November","December"] )
        //==================================

        String[] months = new DateFormatSymbols().getMonths();
        List<String> mMonths = new ArrayList<>();
        mMonths.addAll(Arrays.asList(months).subList(minMonth - 1, months.length));

        return mMonths;
    }

    private List<String> setUpYear(int minYear) {
        List<String> mYears = new ArrayList<>();
        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        int numYears = currYear - minYear;

        for (int i = 0; i <= numYears; i++) {
            mYears.add(String.valueOf(minYear));
            minYear = minYear + 1;
        }

        return mYears;
    }

}
