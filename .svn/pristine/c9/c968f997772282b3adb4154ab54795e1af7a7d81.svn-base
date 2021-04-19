package com.ultramega.shop.activity.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.NotificationsRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.Notification;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetNotificationResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tony on 15/09/2017.
 */

public class NotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String usertype = "";
    private String customerid = "";
    private String userid = "";
    private int year = 0;
    private int month = 0;
    private int limit;

    private UltramegaShopUtilities mDb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private NotificationsRecyclerAdapter mNotificationAdapter;

    private List<Notification> mNotification;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;
    private boolean isloadmore = false;

    private LinearLayout mSmoothProgressBar;

    private NestedScrollView nested_scroll;
    private boolean isbottomscroll;
    private boolean isfirstload = true;
    private boolean isLoading = false;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_notification);
        init();
    }

    public void init() {
        setUpToolBar();

        mDb = new UltramegaShopUtilities(getViewContext());
        mNotification = new ArrayList<>();
        mNotification = mDb.getNotificationList();
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        limit = getLimit(mNotification.size(), 30);
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mDb.getConsumerProfile();
            customerid = consumerProfile.getConsumerID();
            userid = consumerProfile.getUserID();

            String mMonth = CommonFunctions.getMonthFromDate(consumerProfile.getDateTimeRegistered());
            String mYear = CommonFunctions.getYearFromDate(consumerProfile.getDateTimeRegistered());

            MIN_MONTH = Integer.valueOf(mMonth);
            MIN_YEAR = Integer.valueOf(mYear);
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mDb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            MIN_MONTH = Integer.valueOf(CommonFunctions.getMonthFromDate(wholesalerProfile.getDateTimeAdded()));
            MIN_YEAR = Integer.valueOf(CommonFunctions.getYearFromDate(wholesalerProfile.getDateTimeAdded()));
        }
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);

        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        mSmoothProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        text_empty = (TextView) findViewById(R.id.text_empty);
        image_empty = (ImageView) findViewById(R.id.image_empty);
        image_empty.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_action_notification);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notification_SwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        viewarchivelayoutv2 = (LinearLayout) findViewById(R.id.viewarchivelayoutv2);
        viewarchivev2 = (TextView) findViewById(R.id.viewarchivev2);
        viewarchivev2.setOnClickListener(this);
        viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
        viewarchivelayout = (LinearLayout) findViewById(R.id.viewarchivelayout);
        viewarchive = (TextView) findViewById(R.id.viewarchive);
        viewarchive.setOnClickListener(this);
        viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));

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
                mNotificationAdapter = new NotificationsRecyclerAdapter(getViewContext(), mNotification);

                if (mNotification.isEmpty()) {
                    getSession();
                }

                recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                recyclerView.setAdapter(mNotificationAdapter);
                updateFirstListNotification(mDb.getNotificationList());
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

    public static void start(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSession() {

        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //API CALL
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            createSession(callsession);

        } else {
            mSwipeRefreshLayout.setRefreshing(false);
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
                    getNotication(getNotificationCallBackSession);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    isLoading = false;
                    showError(response.body().getMessage());
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                isLoading = false;
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getNotication(Callback<GetNotificationResponse> getNotifacationCallBack) {

        String str_month = "";

        if (month <= 9) {
            str_month = "0".concat(String.valueOf(month));
        } else {
            str_month = String.valueOf(month);
        }

        Call<GetNotificationResponse> notif = RetrofitBuild.getNotificationsService(getViewContext())
                .getNotificationsCall(imei,
                        authcode,
                        sessionid,
                        usertype,
                        customerid,
                        userid,
                        String.valueOf(limit),
                        String.valueOf(year),
                        str_month);
        notif.enqueue(getNotifacationCallBack);
    }

    private final Callback<GetNotificationResponse> getNotificationCallBackSession = new Callback<GetNotificationResponse>() {

        @Override
        public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
            isLoading = false;
            isfirstload = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getData().size() > 0;
                    if (!isbottomscroll) {
                        mDb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
                    }
                    List<Notification> notif = response.body().getData();
                    for (Notification list : notif) {
                        insertNotification(list);
                    }
                    updateListNotification(mDb.getNotificationList());
                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                showError("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    @Override
    public void onRefresh() {

        viewarchivelayoutv2.setVisibility(View.GONE);
        viewarchivelayout.setVisibility(View.GONE);

        if (mDb != null) {
            mDb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
            recyclerView.setVisibility(View.GONE);
            mNotificationAdapter.clear();
        }

        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSwipeRefreshLayout.setRefreshing(true);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        getSession();
        CommonFunctions.hasNewNotification = false;

        viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
        viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewarchivelayoutv2.setVisibility(View.GONE);
        viewarchivelayout.setVisibility(View.GONE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //set notifications
                UserPreferenceManager.saveIntegerPreference(getViewContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);

                if (mDb != null) {
                    if (mDb.getNotificationList().size() == 0 || CommonFunctions.hasNewNotification) {
                        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                        year = Calendar.getInstance().get(Calendar.YEAR);
                        if (mDb != null) {
                            mDb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
                            recyclerView.setVisibility(View.GONE);
                            if (mNotificationAdapter != null) {
                                mNotificationAdapter.clear();
                            }
                        }

                        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                        year = Calendar.getInstance().get(Calendar.YEAR);
                        isfirstload = false;
                        limit = 0;
                        isbottomscroll = false;
                        isloadmore = true;

                        getSession();
                    } else {

                        if (mNotificationAdapter != null) {
                            mNotificationAdapter.clear();
                        }

                        if (mNotificationAdapter != null) {
                            mNotificationAdapter.setNotificationsData(mDb.getNotificationList());
                        }

                    }

                    CommonFunctions.hasNewNotification = false;
                }

            }
        }, 200);

    }

    public void updateFirstListNotification(List<Notification> data) {

        CommonFunctions.hasNewNotification = false;

        if (data.size() > 0) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            image_empty.setVisibility(View.GONE);
            if (mNotificationAdapter != null) {
                mNotificationAdapter.setNotificationsData(data);
            }
        } else {
            viewarchivelayoutv2.setVisibility(View.VISIBLE);
            viewarchivelayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    public void updateListNotification(List<Notification> data) {

        CommonFunctions.hasNewNotification = false;

        if (data.size() > 0) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            image_empty.setVisibility(View.GONE);
            if (mNotificationAdapter != null) {
                if (!isLoading) {
                    mNotificationAdapter.setNotificationsData(data);
                }
            }
        } else {
            viewarchivelayoutv2.setVisibility(View.VISIBLE);
            viewarchivelayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    public void insertNotification(Notification notification) {
        mDb.insertNotification(notification);
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
            case R.id.edtMonth: {
                new MaterialDialog.Builder(getViewContext())
                        .title("Select Month")
                        .items(MONTHS)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                month = Integer.valueOf(CommonFunctions.getMonthNumber(text.toString()));
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

                    if (mDb != null) {
                        mDb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
                    }

                    if (mNotificationAdapter != null) {
                        mNotificationAdapter.clear();
                    }

                    isfirstload = false;
                    limit = 0;
                    isbottomscroll = false;
                    isloadmore = true;
                    mSwipeRefreshLayout.setRefreshing(true);
                    getSession();
                }
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

                if (mDb != null) {
                    mDb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
                }

                if (mNotificationAdapter != null) {
                    mNotificationAdapter.clear();
                }

                mFilterOptionDialog.dismiss();

                isfirstload = false;
                limit = 0;
                isbottomscroll = false;
                isloadmore = true;
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
        TextView txvDateClose = (TextView) view.findViewById(R.id.txvDateClose);
        txvDateClose.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "CANCEL"));
        txvDateClose.setOnClickListener(this);
        TextView txvFilter = (TextView) view.findViewById(R.id.txvFilter);
        txvFilter.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "FILTER"));
        txvFilter.setOnClickListener(this);

        edtMonth = (MaterialEditText) view.findViewById(R.id.edtMonth);
        edtMonth.setOnClickListener(this);
        edtYear = (MaterialEditText) view.findViewById(R.id.edtYear);
        edtYear.setOnClickListener(this);
    }

    private void setUpFilterOptions() {
        mFilterOptionDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_date_filter_options, false)
                .build();
        View view = mFilterOptionDialog.getCustomView();
        TextView txvEdtSearches = (TextView) view.findViewById(R.id.txvEdtSearches);
        txvEdtSearches.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "EDIT SEARCHES"));
        txvEdtSearches.setOnClickListener(this);
        TextView txvClearSearches = (TextView) view.findViewById(R.id.txvClearSearches);
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
