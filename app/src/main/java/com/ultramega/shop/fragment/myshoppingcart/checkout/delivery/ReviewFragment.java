package com.ultramega.shop.fragment.myshoppingcart.checkout.delivery;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.adapter.myshoppingcart.ReviewOrderRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.OrderPayments;
import com.ultramega.shop.pojo.OrderSkus;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.AddOrdersResponse;
import com.ultramega.shop.responses.GetConsumerWalletResponse;
import com.ultramega.shop.responses.GetOrdersQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends BaseFragment implements View.OnClickListener {

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String userid = "";
    private String customerid = "";
    private String usertype = "";
    private String ordertype = "";
    private String customername = "";
    private String customermobile = "";
    private String customeremail = "";
    private String customerdeliveryaddress = "";
    private String latitude;
    private String longitude;
    private String customerremarks;
    private String orderskus = "";
    private String branchid = "";
    private String delivereefee = "";
    private String vehicleid = "";
    private UltramegaShopUtilities mdb;

    private MaterialDialog mConfirmationDialog;
    private MaterialDialog mDialog;

    private double totalOrderAmount;
    private double deliverCharge;

    private TextView txvMessage;

    private Branches branches;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        setHasOptionsMenu(true);

        //set up title
        ((CheckoutProductsActivity) getViewContext()).setActionBarTitle("Checkout");

        TextView totalquantity = (TextView) view.findViewById(R.id.my_shopping_cart_product_quantity);
        TextView totalamount = (TextView) view.findViewById(R.id.my_shopping_cart_product_total);

        //initialize empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        totalOrderAmount = 0;
        deliverCharge = 0;

        String firstname = "";
        String lastname = "";

        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            ordertype = "DELIVERY";
            customerdeliveryaddress = getArguments().getString("address");
            latitude = getArguments().getString("latitude");
            longitude = getArguments().getString("longitude");
            customerremarks = getArguments().getString("instructions");
            delivereefee = UserPreferenceManager.getDeliveryFee(getViewContext());
            vehicleid = UserPreferenceManager.getVehicleID(getViewContext());

            if(getArguments().getString("branches") != null){
                branches = new Gson().fromJson(getArguments().getString("branches"),Branches.class);
                branchid = branches.getBranchID();
            }else{
                branchid = ".";
            }

            firstname = itemProf.getFirstName() == null ? "." : itemProf.getFirstName();
            lastname = itemProf.getLastName() == null ? "." : itemProf.getLastName();
            customermobile = itemProf.getUserID() == null ? "." : itemProf.getUserID();
            customeremail = itemProf.getEmailAddress() == null ? "." : itemProf.getEmailAddress();

        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            branchid = wholesalerProfile.getBranchID();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            firstname = wholesalerProfile.getFirstName() == null ? "." : wholesalerProfile.getFirstName();
            lastname = wholesalerProfile.getLastName() == null ? "." : wholesalerProfile.getLastName();
            customermobile = wholesalerProfile.getUserID() == null ? "." : wholesalerProfile.getUserID();
            customeremail = wholesalerProfile.getEmailAddress() == null ? "." : wholesalerProfile.getEmailAddress();

            ordertype = "PICKUP";
            customerdeliveryaddress = ".";
            latitude = ".";
            longitude = ".";
            customerremarks = ".";
        }

        customername = firstname + " " + lastname;

        List<OrderSkus> listorders = new ArrayList<>();
        List<ShoppingCartsQueue> listqueue = mdb.getAllShoppingCartsQueue();
        for (int i = 0; i < listqueue.size(); i++) {
            listorders.add(new OrderSkus(
                    listqueue.get(i).getItemCode(),
                    listqueue.get(i).getCatClass(),
                    listqueue.get(i).getQuantity(),
                    listqueue.get(i).getPackageCode(),
                    listqueue.get(i).getItemPicUrl()));
        }
        orderskus = new Gson().toJson(listorders);

        if (mdb.getShoppingCartSummary(usertype) != null) {

            ShoppingCartSummary item = mdb.getShoppingCartSummary(usertype);

            if (usertype.equals("CONSUMER")) {

                view.findViewById(R.id.totalBulkLayout).setVisibility(View.GONE);
                view.findViewById(R.id.totalNonBulkLayout).setVisibility(View.GONE);
                view.findViewById(R.id.totalQuantityLayout).setVisibility(View.VISIBLE);

                totalquantity.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(item.getQuantity()))));

            } else {

                view.findViewById(R.id.totalBulkLayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.totalNonBulkLayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.totalQuantityLayout).setVisibility(View.GONE);

                ((TextView) view.findViewById(R.id.txvTotalBulk)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(item.getTotalBulk()))));
                ((TextView) view.findViewById(R.id.txvTotalNonBulk)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(item.getTotalNonBulk()))));

            }

            totalamount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.currencyFormatter(String.valueOf(item.getTotalAmount()))));

            totalOrderAmount = item.getTotalAmount();

        }

        //SETUP DIALOGS
        if (usertype.equals("CONSUMER")) {
            setUpConfirmationDialog();
            setUpSuccessDialog();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_shopping_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        ReviewOrderRecyclerAdapter reviewAdapter = new ReviewOrderRecyclerAdapter(getViewContext(), mdb.getAllShoppingCartsQueue());
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(reviewAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvClose: {
                mConfirmationDialog.dismiss();
                break;
            }
            case R.id.txvProceed: {
                mConfirmationDialog.dismiss();
                getSession();
                break;
            }
            case R.id.txvshopagain: {
                mDialog.dismiss();

                Intent intent = new Intent(getViewContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;
            }
            case R.id.txvgotoorder: {
                mDialog.dismiss();

                Intent intent = new Intent(getViewContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("index", 3);
                startActivity(intent);
                break;
            }
        }
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //PAY ORDER
            showProgressDialog();
            //api session
            createSession(callsession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private void openPayOrderDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Ok")
                .negativeText("Close")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (usertype.equals("CONSUMER")) {
                            mConfirmationDialog.show();

//                            txvwalletbalance.setText(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())));
//                            txvorderamount.setText(CommonFunctions.currencyFormatter(String.valueOf(totalOrderAmount)));
//                            txvdeliverycharge.setText(CommonFunctions.currencyFormatter(String.valueOf(deliverCharge)));
//                            txvremainingbalance.setText(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet() - (totalOrderAmount + deliverCharge))));
                        } else {
                            getSession();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void openPlaceOrderDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Ok")
                .negativeText("Close")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgressDialog();
                        //api session
                        createSession(callsession);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);

                    addOrders(addOrdersSession);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog(response.body().getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
            hideProgressDialog();
        }
    };

    private void addOrders(Callback<AddOrdersResponse> addOrdersCallback) {
        String paymenttype = UserPreferenceManager.getStringPreference(getViewContext(),UserPreferenceManager.KEY_TEMP_PAYMENTOPTION);
        Call<AddOrdersResponse> addorders = RetrofitBuild.addOrdersService(getViewContext())
                .addOrdersCall(imei,
                        authcode,
                        sessionid,
                        userid,
                        customerid,
                        usertype,
                        ordertype,
                        customername.toUpperCase(),
                        customermobile,
                        customeremail,
                        customerdeliveryaddress.toUpperCase(),
                        longitude,
                        latitude,
                        delivereefee,
                        vehicleid,
                        orderskus,
                        customerremarks.toUpperCase(),
                        ".",
                        branchid,
                        (paymenttype.isEmpty() || paymenttype == null) ? "CASH": paymenttype);
        addorders.enqueue(addOrdersCallback);
    }

    private final Callback<AddOrdersResponse> addOrdersSession = new Callback<AddOrdersResponse>() {

        @Override
        public void onResponse(Call<AddOrdersResponse> call, Response<AddOrdersResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog(response.body().getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<AddOrdersResponse> call, Throwable t) {
            hideProgressDialog();
            t.printStackTrace();
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
        }
    };

    private void evaluateResponse(Response<AddOrdersResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {
                if (usertype.equals("CONSUMER")) {
                    OrderPayments orderPayments = response.body().getOrderPayments();

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    createSession(getordersqueuesession);

                    mDialog.show();

                    txvMessage.setText("Your order is successfully sent with transaction # " + orderPayments.getOrderTxnID() + ". Please monitor your transaction under orders.");

                } else if (usertype.equals("WHOLESALER")) {
                    OrderPayments orderPayments = response.body().getOrderPayments();

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    openSuccessfullCheckout(orderPayments.getOrderTxnID());
                    createSession(getordersqueuesession);
                }
                break;
            }
            case "004": {

                forceLogoutDialog("Invalid User");

                break;
            }
            default: {

                hideProgressDialog();

                if (isAdded()) {
                    openErrorDialog(response.body().getMessage());
                }

                break;
            }
        }
    }

    private final Callback<GetSessionResponse> getordersqueuesession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    getOrdersQueue(getOrdersQueueSession);
                } else {
                    hideProgressDialog();
                }
            } else {
                hideProgressDialog();
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            Log.d("antonhttp", t.toString());
            hideProgressDialog();
        }
    };

    private void getOrdersQueue(Callback<GetOrdersQueueResponse> getOrdersQueueCallback) {
        Log.d("roneldayanan","GetORDERS QUEUE 4");
        Call<GetOrdersQueueResponse> fetchitems = RetrofitBuild.getOrdersQueueService(getViewContext())
                .getOrdersQueueCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        "0",
                        userid,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH) + 1);
        fetchitems.enqueue(getOrdersQueueCallback);
    }

    private final Callback<GetOrdersQueueResponse> getOrdersQueueSession = new Callback<GetOrdersQueueResponse>() {

        @Override
        public void onResponse(Call<GetOrdersQueueResponse> call, Response<GetOrdersQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                    List<OrdersQueue> orderitems = response.body().getOrdersQueue();
                    for (OrdersQueue order : orderitems) {
                        //======================================
                        // VALIDATE DATA PASSED FROM THE SERVER
                        //======================================
                        try{
                            if (!(order.getStatus().matches(".*\\d.*") || order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED"))) {
                                mdb.insertOrdersQueue(order, usertype);
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GetOrdersQueueResponse> call, Throwable t) {
            hideProgressDialog();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_confirm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (usertype.equals("CONSUMER")) {
                    ((CheckoutProductsActivity) getViewContext()).setAddress(1, customerdeliveryaddress, latitude, longitude);
                } else {
                    getActivity().finish();
                }
                return true;
            }
            case R.id.confirm: {
                if (usertype.equals("CONSUMER")) {
//                    if (mdb.getConsumerTotalWallet() < totalOrderAmount) {
//                        openReloadWalletDialog("You don't have enough funds in your wallet");
//                    } else {
//                        //SETUP DELIVER CHARGE
//                        deliverCharge = setUpDeliveryCharge(totalOrderAmount);
//                        mConfirmationDialog.show();
//                    }

                    //SETUP DELIVER CHARGE
                    deliverCharge = setUpDeliveryCharge(totalOrderAmount);
                    mConfirmationDialog.show();

                } else {
                    openPlaceOrderDialog("Are you sure you want to place this order?");
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //================================================================================
    //WALLET
    //================================================================================
    private void getWalletSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            createSession(getWalletSession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
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

    private Callback<GetConsumerWalletResponse> getWallet = new Callback<GetConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<GetConsumerWalletResponse> call, Response<GetConsumerWalletResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
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

                    //SETUP DELIVER CHARGE
                    deliverCharge = setUpDeliveryCharge(totalOrderAmount);

                    if (mdb.getConsumerTotalWallet() < totalOrderAmount) {
                        openReloadWalletDialog("You don't have enough funds in your wallet");
                    } else {
                        openPayOrderDialog("Are you sure you want to pay this order?");
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<GetConsumerWalletResponse> call, Throwable t) {
            hideProgressDialog();
        }
    };

    private double setUpDeliveryCharge(double amount) {
        double charge = 0;

        //==========================================================================================
        //CALCULATE SERVICE CHARGE
        //==========================================================================================

        List<ConsumerCharge> listcharge = mdb.getDeliveryCharges();
        for (int i = 0; i < listcharge.size(); i++) {

            if (amount >= listcharge.get(i).getAmountFrom() && amount <= listcharge.get(i).getAmountTo()) {
                charge = ((amount * listcharge.get(i).getVariableFee()) / 100) + listcharge.get(i).getBaseFee();
                return charge;
            } else {
                charge = 0;
            }

        }

        return charge;
    }

    //================================================================================
    //SETUP DIALOGS
    //================================================================================
    private void setUpConfirmationDialog() {
        mConfirmationDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(false)
                .customView(R.layout.dialog_confirmation_order_payment, false)
                .build();
        View view = mConfirmationDialog.getCustomView();

        TextView txvClose = (TextView) view.findViewById(R.id.txvClose);
        txvClose.setOnClickListener(this);
        TextView txvProceed = (TextView) view.findViewById(R.id.txvProceed);
        txvProceed.setOnClickListener(this);

    }

    private void setUpSuccessDialog() {
        mDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(false)
                .customView(R.layout.dialog_success_order_payment, false)
                .build();
        View view = mDialog.getCustomView();

        TextView txvshopagain = (TextView) view.findViewById(R.id.txvshopagain);
        txvshopagain.setOnClickListener(this);
        TextView txvgotoorder = (TextView) view.findViewById(R.id.txvgotoorder);
        txvgotoorder.setOnClickListener(this);

        txvMessage = (TextView) view.findViewById(R.id.message);

    }
}
