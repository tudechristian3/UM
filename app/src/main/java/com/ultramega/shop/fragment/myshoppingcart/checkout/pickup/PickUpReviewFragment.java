package com.ultramega.shop.fragment.myshoppingcart.checkout.pickup;

import android.content.Intent;
import android.os.Build;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.PickUpActivity;
import com.ultramega.shop.adapter.myshoppingcart.ReviewOrderRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrderPayments;
import com.ultramega.shop.pojo.OrderSkus;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.responses.AddOrdersResponse;
import com.ultramega.shop.responses.GetOrdersQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import org.json.JSONException;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import kotlinx.serialization.json.Json;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 8/18/2017.
 */

public class PickUpReviewFragment extends BaseFragment implements View.OnClickListener {
    private Branches selectedBranch;
    private String instructions = "";

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
    private String customerremarks;
    private String orderskus = "";
    private String customerdeliveryaddress = "";
    private String latitude = "";
    private String longitude = "";
    private String branchid = "";
    private String pickupschedule = "";
    private String delivereefee = "";
    private String vehicleid = "";
    private String paymenttype = "";
    private String order = "";

    private UltramegaShopUtilities mdb;

    private MaterialDialog mConfirmationDialog;
    private MaterialDialog mDialog;

    private TextView txvMessage;

    private double totalOrderAmount;
    private double deliverCharge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_up_review, container, false);

        getValues();

        setHasOptionsMenu(true);

        //set up title
        ((PickUpActivity) getViewContext()).setActionBarTitle("Checkout");

        TextView totalquantity = view.findViewById(R.id.my_shopping_cart_product_quantity);
        TextView totalamount = view.findViewById(R.id.my_shopping_cart_product_total);

        //initialize empty data
        totalOrderAmount = 0;
        deliverCharge = 0;

//        if (getArguments() != null) {
//            if (getArguments().getString("address").trim().length() > 0) {
//                latitude = getArguments().getString("latitude");
//                longitude = getArguments().getString("longitude");
//            }
//        }

        List<OrderSkus> listorders = new ArrayList<>();
        List<ShoppingCartsQueue> listqueue = new ArrayList<>();
        ConsumerProfile itemProf = null;
        mdb = new UltramegaShopUtilities(getViewContext());
        itemProf = mdb.getConsumerProfile();
        selectedBranch = null;
        selectedBranch = getArguments().getParcelable("branches");

        usertype = getCurrentUserType(getViewContext());

        ShoppingCartSummary item = null;
        if (mdb.getShoppingCartSummary(usertype) != null) {
            item = mdb.getShoppingCartSummary(usertype);
            totalquantity.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", String.valueOf(item.getQuantity())));
            totalamount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.currencyFormatter(String.valueOf(item.getTotalAmount()))));
            totalOrderAmount = item.getTotalAmount();
        }
        listqueue = mdb.getAllShoppingCartsQueue();

        imei = CommonFunctions.getIMEI(getViewContext());
        customerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
        ordertype = "PICKUP";
        String firstname = itemProf.getFirstName() == null ? "." : itemProf.getFirstName();
        String lastname = itemProf.getLastName() == null ? "." : itemProf.getLastName();
        customername = firstname + " " + lastname;
        customermobile = itemProf.getUserID() == null ? "." : itemProf.getUserID();
        customeremail = itemProf.getEmailAddress() == null ? "." : itemProf.getEmailAddress();
        branchid = selectedBranch.getBranchID();



        //customerdeliveryaddress = "724 M.Street, Compostela, Cebu, 6003, Philippines";
        //customerremarks = "sample";
        //latitude = "100.23";
        //longitude = "50.123";
        //sessionid = "de8b58c0-77cc-11eb-b8d6-2d4c6790dcd1";


        for (int i = 0; i < listqueue.size(); i++) {
            listorders.add(new OrderSkus(listqueue.get(i).getItemCode(),
                    listqueue.get(i).getCatClass(),
                    listqueue.get(i).getQuantity(),
                    listqueue.get(i).getPackageCode(),
                    listqueue.get(i).getItemPicUrl()));
        }
        orderskus = new Gson().toJson(listorders);
        //SETUP DIALOGS
        setUpConfirmationDialog();
        setUpSuccessDialog();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_my_shopping_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        ReviewOrderRecyclerAdapter reviewAdapter = new ReviewOrderRecyclerAdapter(getViewContext(), mdb.getAllShoppingCartsQueue());
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(reviewAdapter);



        //validateQRPartialV2();



        return view;
    }

    private void getValues() {
        mdb = new UltramegaShopUtilities(getViewContext());
        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        userid = itemProf.getUserID();
        customerid = mdb.getConsumerID();
        userid = itemProf.getUserID();
        branchid = UserPreferenceManager.getBranchID(getActivity());
        sessionid = UserPreferenceManager.getSession(getViewContext());
        customerremarks = getArguments().getString("instructions");
        pickupschedule = UserPreferenceManager.getPickupSchedule(getViewContext());
        delivereefee = ".";
        vehicleid = ".";
        customerdeliveryaddress = "sample";
        latitude = "100.23";
        longitude = "50.123";
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
                        getSession();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void validateQRPartialV2() {

        if (CommonFunctions.getConnectivityStatus(requireActivity()) > 0) {
            LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
            parameters.put("imei", imei);
            parameters.put("authcode",CommonFunctions.getSha1Hex(imei + sessionid + customerid));
            parameters.put("sessionid", sessionid);
            parameters.put("userid,", userid);
            parameters.put("customerid,", customerid);
            parameters.put("usertype", usertype);
            parameters.put("ordertype", "PICKUP");
            parameters.put("customername", customername);
            parameters.put("customermobile", customermobile);
            parameters.put("customeremail", customeremail);
            parameters.put("customerdeliveryaddress", customerdeliveryaddress);
            parameters.put("longitude", longitude);
            parameters.put("latitude", latitude);
            parameters.put("delivereefee", delivereefee);
            parameters.put("vehicleid", vehicleid);
            parameters.put("orderskus", orderskus);
            parameters.put("customerremarks", customerremarks);
            parameters.put("pickupschedule", pickupschedule);
            parameters.put("branchid", branchid);
            parameters.put("paymenttype", "CASH");


            String checker = new Gson().toJson(parameters);
            Log.d("SPecial", "JSON orders: " + checker);

            addOrders(addOrdersSession);


        } else {
            Toast.makeText(getContext(), "qwe", Toast.LENGTH_SHORT).show();
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

                    addOrders(addOrdersSession);
                } else {
                    hideProgressDialog();
                    openErrorDialog("Something went wrong with the server.");
                }
            } else {
                hideProgressDialog();
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
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
                        ".",
                        ".",
                        orderskus,
                        customerremarks.toUpperCase(),
                        pickupschedule,
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
                openErrorDialog(response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<AddOrdersResponse> call, Throwable t) {
            t.printStackTrace();
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void evaluateResponse(Response<AddOrdersResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {
                UserPreferenceManager.removePreference(getViewContext(),UserPreferenceManager.KEY_TEMP_PAYMENTOPTION);
                if (usertype.equals("CONSUMER")) {
                    OrderPayments orderPayments = response.body().getOrderPayments();

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    createSession(getordersqueuesession);

                    mDialog.show();

                    txvMessage.setText("Your order is successfully sent with transaction # " + orderPayments.getOrderTxnID() + ". Please monitor your transaction under orders.");

                } else if (usertype.equals("WHOLESALER")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    openSuccessfullCheckout("Your order has been placed. An Ultra Mega representative will get in touch with you shortly.");
                    createSession(getordersqueuesession);

                }
                break;
            }
            default: {
                hideProgressDialog();
                openErrorDialog(response.body().getMessage());
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
                    openErrorDialog(response.body().getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void getOrdersQueue(Callback<GetOrdersQueueResponse> getOrdersQueueCallback) {
        Log.d("roneldayanan","GetORDERS QUEUE 5");
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
                        if (!(order.getStatus().matches(".*\\d.*") || order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED"))) {
                            mdb.insertOrdersQueue(order, usertype);
                        }
                    }
                    if (usertype.equals("WHOLESALER")) {
                        openSuccessfullCheckout("Your order has been placed. An Ultra Mega representative will get in touch with you shortly.");
                    }
                } else {
                    hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<GetOrdersQueueResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((PickUpActivity) getViewContext()).setBranch(2, selectedBranch);
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
//
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

    private double setUpDeliveryCharge(double amount) {
        double charge = 0;

        //==========================================================================================
        //CALCULATE SERVICE CHARGE
        //==========================================================================================

        if (!ordertype.equals("PICKUP")) {
            List<ConsumerCharge> listcharge = mdb.getDeliveryCharges();
            for (int i = 0; i < listcharge.size(); i++) {

                if (amount >= listcharge.get(i).getAmountFrom() && amount <= listcharge.get(i).getAmountTo()) {
                    charge = ((amount * listcharge.get(i).getVariableFee()) / 100) + listcharge.get(i).getBaseFee();
                    return charge;
                } else {
                    charge = 0;
                }

            }
        } else {
            charge = 0;
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

        TextView txvClose = view.findViewById(R.id.txvClose);
        txvClose.setOnClickListener(this);
        TextView txvProceed = view.findViewById(R.id.txvProceed);
        txvProceed.setOnClickListener(this);

    }

    private void setUpSuccessDialog() {
        mDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(false)
                .customView(R.layout.dialog_success_order_payment, false)
                .build();
        View view = mDialog.getCustomView();

        txvMessage = view.findViewById(R.id.message);

        TextView txvshopagain = view.findViewById(R.id.txvshopagain);
        txvshopagain.setOnClickListener(this);
        TextView txvgotoorder = view.findViewById(R.id.txvgotoorder);
        txvgotoorder.setOnClickListener(this);

    }
}
