package com.ultramega.shop.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionMenu;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.adapter.viewpayments.ViewPaymentRecylerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.BankReference;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.WalletReload;
import com.ultramega.shop.responses.GetBankReferenceResponse;
import com.ultramega.shop.responses.GetConsumerWalletResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.ViewPaymentWalletReloadResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;

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
 * Created by Tony on 31/08/2017.
 */

public class WalletReloadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

//    private static final String KEY_WALLET_RELOAD = "title";
//    public static final int mNofItems = 8;

    public static final String STATUS_APPROVED = "COMPLETED";
    public static final String STATUS_FOR_APPROVAL = "FORAPPROVAL";
    public static final String STATUS_DECLINED = "DECLINED";
    public static final String STATUS_EXPIRES = "EXPIRED";

    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewPaymentRecylerAdapter mViewPaymentWalletAdapter;
    private List<WalletReload> mWalletData;

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String userid = "";
    private int year = 0;
    private int month = 0;
    private int limit;
    private int filterLimit = 0;

    private ImageView img_no_wallet;
    private LinearLayout layout_wallet;

    private boolean isloadmore = true;
    private boolean isbottomscroll = false;
    private RecyclerView recyclerView;
    private NestedScrollView nested_wallet;

    private TextView total_wallet;
    private TextView noBalance;
    private TextView YCB;
    private TextView PHP;
    private TextView transaction;
    private TextView waiting;
    private TextView approved;
    private TextView decline;
    private TextView expired;
    private ImageView imagePlus;
    private MaterialDialog mReloadDialog;

    private LinearLayout mSmoothProgressBar;
    private boolean isfirstload = true;

    //VIEW ARCHIVE
    private LinearLayout main_layout;
    private LinearLayout viewarchivelayoutv2;
    private TextView viewarchivev2;
    private LinearLayout viewarchivelayout;
    private TextView viewarchive;
    private MaterialDialog mViewArchiveDialog;
    private MaterialDialog mFilterOptionDialog;
    private MaterialEditText edtMonth;
    private MaterialEditText edtYear;
    private int MIN_MONTH = 1;
    private int MIN_YEAR = 2017;
    private List<String> MONTHS = new ArrayList<>();
    private List<String> YEARS = new ArrayList<>();
    private boolean isFilter = false;

    //EMPTY LAYOUT
    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    //FAB
    private ImageView central_background;
    private List<Central> centralList;
    private FrameLayout layout_menu_central;
    private FloatingActionMenu fab;
    private boolean isCentralOpen = false;
    private RecyclerView recyclerViewCentral;
    private CentralRecyclerViewAdapter mCentralAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        hideSearchBar();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_reload, container, false);

        try {
            //initialize empty data
            mdb = new UltramegaShopUtilities(getViewContext());
            imei = CommonFunctions.getIMEI(getViewContext());
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            usertype = getCurrentUserType(getViewContext());
            imei = CommonFunctions.getIMEI(getViewContext());
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            mWalletData = new ArrayList<>();
            mWalletData = mdb.getWalletReload();
            limit = getLimit(mWalletData.size(), 30);
            month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            year = Calendar.getInstance().get(Calendar.YEAR);
            MIN_MONTH = Integer.valueOf(CommonFunctions.getMonthFromDate(itemProf.getDateTimeRegistered()));
            MIN_YEAR = Integer.valueOf(CommonFunctions.getYearFromDate(itemProf.getDateTimeRegistered()));

            //EMPTY LAYOUT
            layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
            text_empty = (TextView) view.findViewById(R.id.text_empty);
            image_empty = (ImageView) view.findViewById(R.id.image_empty);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.empty_list));

            //VIEW ARCHIVE
            viewarchivelayoutv2 = (LinearLayout) view.findViewById(R.id.viewarchivelayoutv22);
            viewarchivev2 = (TextView) view.findViewById(R.id.viewarchivev22);
            viewarchivev2.setOnClickListener(this);
            viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
            viewarchivelayout = (LinearLayout) view.findViewById(R.id.viewarchivelayout);
            viewarchivelayout.setVisibility(View.GONE);
            viewarchive = (TextView) view.findViewById(R.id.viewarchive);
            viewarchive.setOnClickListener(this);
            viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));

            main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
            mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
            imagePlus = (ImageView) view.findViewById(R.id.img_plus_wallet);
            YCB = (TextView) view.findViewById(R.id.txv_wallet_YCB);
            PHP = (TextView) view.findViewById(R.id.txv_PHP);
            transaction = (TextView) view.findViewById(R.id.txv_transaction);
            waiting = (TextView) view.findViewById(R.id.txv_waiting);
            approved = (TextView) view.findViewById(R.id.txv_approved);
            decline = (TextView) view.findViewById(R.id.txv_decline);
            expired = (TextView) view.findViewById(R.id.txv_expired);
            YCB.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Your current balance"));
            PHP.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "  PHP"));
            transaction.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "TRANSACTIONS"));
            waiting.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Waiting"));
            approved.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Approved"));
            decline.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Declined"));
            expired.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Expired"));

            total_wallet = (TextView) view.findViewById(R.id.wallet_total_balance);
            total_wallet.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", (CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())))));
            img_no_wallet = (ImageView) view.findViewById(R.id.img_no_wallet);
            layout_wallet = (LinearLayout) view.findViewById(R.id.layout_wallet);
            text_empty = (TextView) view.findViewById(R.id.text_empty);
            image_empty = (ImageView) view.findViewById(R.id.image_empty);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_wallet_reload);

            nested_wallet = (NestedScrollView) view.findViewById(R.id.nested_wallet);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeWalletReaload);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            imagePlus.setOnClickListener(this);

            //SET UP ARCHIVE DIALOG
            setUpViewArchiveDialog();
            setUpFilterOptions();

            //SETUP CENTRAL LAYOUT
            setUpCentralLayout(view);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setItemAnimator(new SlideInUpAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                    mViewPaymentWalletAdapter = new ViewPaymentRecylerAdapter(getViewContext(), mWalletData);
                    getSession();
                    recyclerView.setAdapter(mViewPaymentWalletAdapter);
                    updateFirstLoadListWalletReaload(mdb.getWalletReload());
                }
            }, 400);

            nested_wallet.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        //Log.d("antonhttp", "Scroll DOWN");
                        mSwipeRefreshLayout.setEnabled(true);
                    }

                    if (scrollY < oldScrollY) {
                        //Log.d("antonhttp", "Scroll UP");
                        mSwipeRefreshLayout.setEnabled(true);
                    }

                    if (scrollY == 0) {
                        //Log.d("antonhttp", "TOP SCROLL");
                        mSwipeRefreshLayout.setEnabled(true);
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        //Log.d("antonhttp", "======BOTTOM SCROLL=======");
                        mSwipeRefreshLayout.setEnabled(false);
                        isbottomscroll = true;
                        if (isloadmore && !mWalletData.isEmpty()) {
                            if (!isfirstload) {
                                limit = limit + 30;
                            }
                            getSession();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setUpCentralLayout(View view) {
        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), WalletReloadFragment.this);
        recyclerViewCentral.addItemDecoration(new SpacesItemDecoration(10));
        recyclerViewCentral.setAdapter(mCentralAdapter);
        central_background = (ImageView) view.findViewById(R.id.central_background);
        Glide.with(getViewContext())
                .asBitmap()
                .load(R.drawable.central_background)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        central_background.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }
                });

        centralList = new ArrayList<>();
        centralList = getCentralList(usertype);

        fab.getMenuIconView().setImageResource(R.drawable.ic_ultramega_central_icon);
        fab.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCentralOpen) {
                    showCentral();
                } else {
                    hideCentral();
                }
            }
        });

    }

    public void hideCentral() {
        if (isAdded()) {
            if (mCentralAdapter != null) {
                mCentralAdapter.clear();

                Animation move = AnimationUtils.loadAnimation(getViewContext(), R.anim.gk_services_anim_out);
                move.setInterpolator((new AccelerateDecelerateInterpolator()));
                move.setFillAfter(false);
                //layout_menu_central.startAnimation(move);
                layout_menu_central.setVisibility(View.GONE);
                isCentralOpen = false;
                fab.getMenuIconView().setImageResource(R.drawable.ic_ultramega_central_icon);

                //show toolbar
                ((MainActivity) getViewContext()).hideToolBar(false);

                //hide layout
                main_layout.setVisibility(View.VISIBLE);
                viewarchivelayoutv2.setVisibility(View.VISIBLE);

                mSwipeRefreshLayout.setEnabled(true);

            }
        }
    }

    private void showCentral() {
        if (isAdded()) {
            if (mCentralAdapter != null) {
                layout_menu_central.setVisibility(View.VISIBLE);
                Animation move = AnimationUtils.loadAnimation(getViewContext(), R.anim.gk_services_anim);
                move.setInterpolator((new AccelerateDecelerateInterpolator()));
                move.setFillAfter(true);
                //layout_menu_central.startAnimation(move);
                isCentralOpen = true;
                fab.getMenuIconView().setImageResource(R.drawable.ic_fab_x);

                //mCentralAdapter.clear();
                mCentralAdapter.setGridData(centralList);

                //hide toolbar
                ((MainActivity) getViewContext()).hideToolBar(true);

                //hide layout
                main_layout.setVisibility(View.GONE);
                viewarchivelayoutv2.setVisibility(View.GONE);

                mSwipeRefreshLayout.setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        viewarchivelayoutv2.setVisibility(View.GONE);
        viewarchivelayout.setVisibility(View.GONE);

        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        viewarchivelayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        img_no_wallet.setVisibility(View.GONE);
        //mSwipeRefreshLayout.setRefreshing(true);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        getSession();
    }

    @Override
    public void onRefresh() {

        viewarchivelayoutv2.setVisibility(View.GONE);
        viewarchivelayout.setVisibility(View.GONE);

        if (mdb != null) {
            mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
        }

        if (mViewPaymentWalletAdapter != null) {
            mViewPaymentWalletAdapter.clear();
        }

        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        viewarchivelayoutv2.setVisibility(View.GONE);
        layout_empty.setVisibility(View.GONE);
        viewarchivelayout.setVisibility(View.GONE);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        layout_wallet.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        img_no_wallet.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();

        viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
        viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //API CALL
            //showProgressDialog();
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(callsession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
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
                    //get payments
                    getViewPayments(getViewPaymentSession);
                    //get wallet balance
                    if (usertype.equals("CONSUMER")) {
                        getWallet();
                        getBankReference();
                    }
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                if (isAdded()) {
                    showError("Something went wrong with the server.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError("Something went wrong with the server.");
            }
        }
    };

    private void getViewPayments(Callback<ViewPaymentWalletReloadResponse> getViewPaymentWalletCallBack) {
        int mLimit = 0;

        if (isFilter) {
            mLimit = 0;
        } else {
            mLimit = limit;
        }

        Call<ViewPaymentWalletReloadResponse> list = RetrofitBuild.getViewPaymentService(getViewContext())
                .getWalletReloadHistoryCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        String.valueOf(mLimit),
                        userid,
                        year,
                        month);
        list.enqueue(getViewPaymentWalletCallBack);
    }

    private final Callback<ViewPaymentWalletReloadResponse> getViewPaymentSession = new Callback<ViewPaymentWalletReloadResponse>() {

        @Override
        public void onResponse(Call<ViewPaymentWalletReloadResponse> call, Response<ViewPaymentWalletReloadResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            ResponseBody errorBody = response.errorBody();

            isfirstload = false;
            isFilter = false;

            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = (response.body().getDataArr().size() > 0) || (response.body().getHistArr().size() > 0);
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
                    }

                    List<WalletReload> walletReloads = response.body().getDataArr();
                    for (WalletReload list : walletReloads) {
                        mdb.insertWalletReload(list);
                    }

                    List<WalletReload> walletHistory = response.body().getHistArr();
                    for (WalletReload hist : walletHistory) {
                        mdb.insertWalletReload(hist);
                    }

                    updateListWalletReload(mdb.getWalletReload());

                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                if (isAdded()) {
                    showError("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<ViewPaymentWalletReloadResponse> call, Throwable t) {
            isFilter = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private void updateFirstLoadListWalletReaload(List<WalletReload> data) {

        if (data.size() > 0) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            layout_wallet.setVisibility(View.VISIBLE);
            if (mViewPaymentWalletAdapter != null) {
                mViewPaymentWalletAdapter.setListData(data);
            }
        } else {
            viewarchivelayoutv2.setVisibility(View.VISIBLE);
            viewarchivelayout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            layout_wallet.setVisibility(View.VISIBLE);
            if (mViewPaymentWalletAdapter != null) {
                mViewPaymentWalletAdapter.clear();
            }
        }

        if (mdb != null) {
            showImageNoWallet(mdb.getConsumerTotalWallet() == 0 && mdb.getWalletReload().size() == 0);
        }
    }

    public void updateListWalletReload(List<WalletReload> data) {

        if (data.size() > 0) {
            viewarchivelayoutv2.setVisibility(View.GONE);
            viewarchivelayout.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            layout_wallet.setVisibility(View.VISIBLE);
            if (mViewPaymentWalletAdapter != null) {
                mViewPaymentWalletAdapter.setListData(data);
            }
        } else {
            viewarchivelayoutv2.setVisibility(View.VISIBLE);
            viewarchivelayout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            recyclerView.setVisibility(View.GONE);
            layout_wallet.setVisibility(View.VISIBLE);
            if (mViewPaymentWalletAdapter != null) {
                mViewPaymentWalletAdapter.clear();
            }
        }

        if (mdb != null) {
            showImageNoWallet(mdb.getConsumerTotalWallet() == 0 && mdb.getWalletReload().size() == 0);
        }
    }

    public void showImageNoWallet(boolean isShow) {
        if (isShow) {
            if (mdb.getConsumerTotalWallet() == 0) {
                img_no_wallet.setVisibility(View.VISIBLE);
            }
            layout_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layout_wallet.setVisibility(View.GONE);
        } else {
            if (mdb.getConsumerTotalWallet() == 0) {
                img_no_wallet.setVisibility(View.GONE);
            }
            //updateListWalletReload(mdb.getWalletReload());
        }
    }

    private void getWallet() {
        Call<GetConsumerWalletResponse> call = RetrofitBuild.getConsumerWalletService(getViewContext())
                .getConsumerWalletCall(
                        customerid,
                        userid,
                        CommonFunctions.getSha1Hex(imei + userid + sessionid),
                        imei,
                        sessionid);
        call.enqueue(getWallet);
    }

    //ok na ni
    private Callback<GetConsumerWalletResponse> getWallet = new Callback<GetConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<GetConsumerWalletResponse> call, Response<GetConsumerWalletResponse> response) {
            ResponseBody errorBody = response.errorBody();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                    List<ConsumerWallet> consumerwallet = response.body().getConsumerWallet();
                    for (ConsumerWallet wallet : consumerwallet) {
                        mdb.insertConsumerWallet(wallet);
                    }

                    if (mdb != null) {
                        showImageNoWallet(mdb.getConsumerTotalWallet() == 0 && mdb.getWalletReload().size() == 0);
                    }

//                    if (mdb.getConsumerTotalWallet() == 0) {
//                        showImageNoWallet(true);
//                    }

                    total_wallet.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", (CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())))));
                }
            }
        }

        @Override
        public void onFailure(Call<GetConsumerWalletResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private void getBankReference() {
        Call<GetBankReferenceResponse> call = RetrofitBuild.getBankReferenceService(getViewContext())
                .getBankReferenceCall(
                        imei,
                        CommonFunctions.getSha1Hex(imei + sessionid),
                        sessionid);
        call.enqueue(getBankReference);
    }

    private Callback<GetBankReferenceResponse> getBankReference = new Callback<GetBankReferenceResponse>() {

        @Override
        public void onResponse(Call<GetBankReferenceResponse> call, Response<GetBankReferenceResponse> response) {
            ResponseBody errorBody = response.errorBody();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ADMIN_BANK_DETAILS);
                    List<BankReference> bankReferences = response.body().getBankReference();
                    for (BankReference bankreference : bankReferences) {
                        mdb.insertBankReference(bankreference);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GetBankReferenceResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_plus_wallet: {
                openWalletDialog();
                break;
            }
            case R.id.viewarchivev22: {
                if (viewarchivev2.getText().toString().equals(getString(R.string.string_view_archive))) {
                    mViewArchiveDialog.show();
                } else if (viewarchivev2.getText().toString().equals(getString(R.string.string_filter_options))) {
                    mFilterOptionDialog.show();
                }
                break;
            }
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
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
                    }

                    if (mViewPaymentWalletAdapter != null) {
                        mViewPaymentWalletAdapter.clear();
                    }

                    isFilter = true;
                    limit = 0;
                    isbottomscroll = false;
                    isloadmore = true;
                    viewarchivelayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    img_no_wallet.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(true);

                    getSession();
                } else {

                    if (edtMonth.getText().toString().trim().length() == 0) {
                        edtMonth.setError("Invalid! Month is required.");
                        edtMonth.requestFocus();
                    }

                    if (edtYear.getText().toString().trim().length() == 0) {
                        edtYear.setError("Invalid! Year is required.");
                        edtYear.requestFocus();
                    }

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
            case R.id.txvEdtSearches: {
                mFilterOptionDialog.dismiss();
                mViewArchiveDialog.show();
                break;
            }
            case R.id.txvClearSearches: {

                edtMonth.setText("");
                edtYear.setText("");

                if (mdb != null) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
                }

                if (mViewPaymentWalletAdapter != null) {
                    mViewPaymentWalletAdapter.clear();
                }

                mFilterOptionDialog.dismiss();

                limit = 0;
                isbottomscroll = false;
                isloadmore = true;
                viewarchivelayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                img_no_wallet.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(true);
                month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                year = Calendar.getInstance().get(Calendar.YEAR);
                getSession();

                viewarchive.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));
                viewarchivev2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_view_archive)));

                break;
            }
        }

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
