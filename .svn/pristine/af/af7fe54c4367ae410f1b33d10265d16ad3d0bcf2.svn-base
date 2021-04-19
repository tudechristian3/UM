package com.ultramega.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.notification.NotificationActivity;
import com.ultramega.shop.adapter.myshoppingcart.MyShoppingCartRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.BankReference;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.responses.GetBankReferenceResponse;
import com.ultramega.shop.responses.GetConsumerWalletResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShoppingCartFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyShoppingCartRecyclerAdapter mShoppingCartAdapter;

    private TextView txv_my_shopping_cart_product_quantity, txv_my_shopping_cart_product_total;
    private TextView txvconsumerwallet;
    private ImageView depositwallet;

    private LinearLayout confirm_layout;
    private LinearLayout walletLayout;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private String usertype = "";

    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;
    private int limit;

    private ShoppingCartSummary shoppingCartSummary;

    private LinearLayout mSmoothProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mdb != null)
            if (mdb.getConsumerID() != null)
                menu.findItem(R.id.action_notification).setVisible(true);

        hideSearchBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getOrder() == R.id.action_notification)
            NotificationActivity.start(getViewContext());
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shopping_cart, container, false);

        mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
        nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
        text_empty = (TextView) view.findViewById(R.id.text_empty);
        image_empty = (ImageView) view.findViewById(R.id.image_empty);

        walletLayout = (LinearLayout) view.findViewById(R.id.walletLayout);
        txvconsumerwallet = (TextView) view.findViewById(R.id.consumerwallet);
        txvconsumerwallet.setOnClickListener(this);
        confirm_layout = (LinearLayout) view.findViewById(R.id.confirm_layout);
        confirm_layout.setVisibility(View.GONE);
        depositwallet = (ImageView) view.findViewById(R.id.depositwallet);
        txv_my_shopping_cart_product_quantity = (TextView) view.findViewById(R.id.my_shopping_cart_product_quantity);
        txv_my_shopping_cart_product_total = (TextView) view.findViewById(R.id.my_shopping_cart_product_total);
        Button btn_checkout = (Button) view.findViewById(R.id.btnCheckout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_shopping_cart);

        btn_checkout.setOnClickListener(this);
        depositwallet.setOnClickListener(this);

        // this is to set up menu
        setHasOptionsMenu(true);

        mdb = new UltramegaShopUtilities(getViewContext());
        isbottomscroll = false;
        isloadmore = true;
        limit = 0;

        // initialize with empty data
        txvconsumerwallet.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "BAL: PHP ".concat(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())))));
        ConsumerProfile itemProf = mdb.getConsumerProfile();

        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());

        if (usertype.equals("CONSUMER")) {
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            walletLayout.setBackgroundResource(R.color.color_2196F3);
        } else if (usertype.equals("WHOLESALER")) {
            customerid = mdb.getWholesalerID();
            userid = mdb.getWholesalerProfile().getUserID();
            walletLayout.setVisibility(View.GONE);
        }

        if (customerid == null) {
            walletLayout.setVisibility(View.GONE);
        }

        image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.empty_cart));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customerid != null) {
                    getSession();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
                recyclerView.setNestedScrollingEnabled(false);
                //mShoppingCartAdapter = new MyShoppingCartRecyclerAdapter(getViewContext(), mdb.getAllShoppingCartsQueue(), MyShoppingCartFragment.this);
                recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
                recyclerView.setAdapter(mShoppingCartAdapter);
                setShoppingSummaryUI();
                updateFirsLoadList(mdb.getAllShoppingCartsQueue());
            }
        }, 400);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                    mSwipeRefreshLayout.setEnabled(false);
                    isbottomscroll = true;
                    if (isloadmore) {
                        limit = limit + 30;
                        getSession();
                    }
                }
            }
        });


        return view;
    }

    public void updateFirsLoadList(List<ShoppingCartsQueue> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            confirm_layout.setVisibility(View.VISIBLE);
            mShoppingCartAdapter.setCartData(data);
        } else {
            mShoppingCartAdapter.clearCartData();
            confirm_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
        }
    }

    public void updateList(List<ShoppingCartsQueue> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            confirm_layout.setVisibility(View.VISIBLE);
            mShoppingCartAdapter.setCartData(data);
        } else {
            mShoppingCartAdapter.clearCartData();
            confirm_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
        }
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(getSession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            if(isAdded()){
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    }

    private void getWalletSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            createSession(getWalletSession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            if(isAdded()){
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    }

    public void setShoppingSummaryUI() {
        if (mdb.getShoppingCartSummary(usertype) != null) {
            shoppingCartSummary = mdb.getShoppingCartSummary(usertype);

            txv_my_shopping_cart_product_quantity.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", String.valueOf(shoppingCartSummary.getQuantity())));
            txv_my_shopping_cart_product_total.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", CommonFunctions.currencyFormatter(String.valueOf(shoppingCartSummary.getTotalAmount()))));
        }
    }

    private final Callback<GetSessionResponse> getWalletSession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    if (usertype.equals("CONSUMER")) {
                        getWallet();
                    }
                } else {
                    hideProgressDialog();
                    mSwipeRefreshLayout.setRefreshing(false);
                    if(isAdded()){
                        showError(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                mSwipeRefreshLayout.setRefreshing(false);
                if(isAdded()){
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            mSwipeRefreshLayout.setRefreshing(false);
            if(isAdded()){
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private final Callback<GetSessionResponse> getSession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    if (usertype.equals("CONSUMER")) {
                        getWallet();
                        getBankReference();
                    }
                    getShoppingCart();
                } else {
                    //hideProgressDialog();
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    if(isAdded()){
                        showError(response.body().getMessage());
                    }
                }
            } else {
                //hideProgressDialog();
                mSmoothProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                if(isAdded()){
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if(isAdded()){
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mdb != null)
            txvconsumerwallet.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "BAL: PHP ".concat(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())))));
    }

    private void getBankReference() {
        Call<GetBankReferenceResponse> call = RetrofitBuild.getBankReferenceService(getViewContext())
                .getBankReferenceCall(
                        imei,
                        CommonFunctions.getSha1Hex(imei + sessionid),
                        sessionid);
        call.enqueue(getBankReference);
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

    private void getShoppingCart() {
        Call<FetchShoppingCartsQueueResponse> call = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
                .fetchShoppingCartsQueueCall(
                        imei,
                        CommonFunctions.getSha1Hex(imei + userid + sessionid),
                        String.valueOf(limit),
                        sessionid,
                        userid,
                        customerid,
                        getCurrentUserType(getViewContext()));
        call.enqueue(fetchShoppingCartsQueueResponseCall);
    }

    //ok na ni
    private Callback<GetConsumerWalletResponse> getWallet = new Callback<GetConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<GetConsumerWalletResponse> call, Response<GetConsumerWalletResponse> response) {
            ResponseBody errorBody = response.errorBody();
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_DELIVERY_CHARGE);
                    List<ConsumerWallet> consumerwallet = response.body().getConsumerWallet();
                    for (ConsumerWallet wallet : consumerwallet) {
                        mdb.insertConsumerWallet(wallet);
                    }
                    List<ConsumerCharge> consumerCharge = response.body().getConsumerCharge();
                    for (ConsumerCharge charge : consumerCharge) {
                        mdb.insertConsumerCharge(charge);
                    }
                    txvconsumerwallet.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "BAL: PHP ".concat(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())))));
                } else {
                    //hideProgressDialog();
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }

        @Override
        public void onFailure(Call<GetConsumerWalletResponse> call, Throwable t) {
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private Callback<GetBankReferenceResponse> getBankReference = new Callback<GetBankReferenceResponse>() {

        @Override
        public void onResponse(Call<GetBankReferenceResponse> call, Response<GetBankReferenceResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ADMIN_BANK_DETAILS);
                    List<BankReference> bankReferences = response.body().getBankReference();
                    for (BankReference bankreference : bankReferences) {
                        mdb.insertBankReference(bankreference);
                    }
                } else {
                    //hideProgressDialog();
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }

        @Override
        public void onFailure(Call<GetBankReferenceResponse> call, Throwable t) {
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private Callback<FetchShoppingCartsQueueResponse> fetchShoppingCartsQueueResponseCall = new Callback<FetchShoppingCartsQueueResponse>() {
        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getShoppingCartsQueues().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    }
                    List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
                    for (ShoppingCartsQueue queue : shoppingqueue) {
                        mdb.insertAllShoppingCartsQueue(queue);
                    }
                    updateList(mdb.getAllShoppingCartsQueue());
                } else {
                    if(isAdded()){
                        showError(response.body().getMessage());
                    }
                }
            } else {
                if(isAdded()){
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }


        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            //hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if(isAdded()){
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckout: {
                try {
                    if (customerid == null) {
                        openLoginDialog("You are required to login to proceed with your orders");
                    } else {
                        if (mdb.getAllShoppingCartsQueue().size() > 0) {

                            if (usertype.equals("CONSUMER")) {

                                if (mdb.getConsumerTotalWallet() < shoppingCartSummary.getTotalAmount()) {
                                    openReloadWalletDialog("You don't have enough funds in your wallet");
                                } else {
                                    openDeliveryOptionDialog();
                                }

                            } else if (usertype.equals("WHOLESALER")) {
                                CheckoutProductsActivity.start(getViewContext(), 1);
                            }

                        } else {
                            openErrorDialog("Please fill your cart to checkout");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.depositwallet: {
                getWalletSession();
                break;
            }
            case R.id.consumerwallet: {
                MainActivity.start(getViewContext(), 5);
                break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        boolean isViewShown = false;
        if (getView() != null) {
            isViewShown = true;
            if (customerid == null) {
                updateList(mdb.getAllShoppingCartsQueue());
            } else {
                //api session
                getSession();
            }
        } else {
            isViewShown = false;
        }
    }

    @Override
    public void onRefresh() {
        //api session
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        if (mdb != null) {
            if (customerid != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                getSession();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                updateList(mdb.getAllShoppingCartsQueue());
            }
        } else {
            updateList(mdb.getAllShoppingCartsQueue());
        }

    }
}
