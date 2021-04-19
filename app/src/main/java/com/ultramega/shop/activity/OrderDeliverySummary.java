package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrderSkus;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.responses.AddOrdersResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDeliverySummary extends BaseActivity implements View.OnClickListener {

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String usertype = "";
    private String ordertype = "";
    private String customername = "";
    private String customermobile = "";
    private String customeremail = "";
    private String customerdeliveryaddress = "";
    private String longitude = "";
    private String latitude = "";
    private String delivereefee = "";
    private String vehicleid = "";

    private String orderskus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery_summary);

        setUpToolBar();
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Order Summary"));

        Button btnplaceorder = (Button) findViewById(R.id.btnplaceorder);
        btnplaceorder.setOnClickListener(this);

        btnplaceorder.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Place Order"));

//        initialize empty data
        List<OrderSkus> listorders = new ArrayList<>();
        List<ShoppingCartsQueue> listqueue = new ArrayList<>();
        ConsumerProfile itemProf = null;
        UltramegaShopUtilities mdb = new UltramegaShopUtilities(getViewContext());
        itemProf = mdb.getConsumerProfile();
        listqueue = mdb.getAllShoppingCartsQueue();

        imei = CommonFunctions.getIMEI(getViewContext());
        customerid = mdb.getConsumerID();
        usertype = getCurrentUserType(getViewContext());
        ordertype = "DELIVERY";
        customername = itemProf.getFirstName() + " " + itemProf.getLastName();
        customermobile = itemProf.getMobileNo();
        customeremail = itemProf.getEmailAddress();
        customerdeliveryaddress = "724 M.Street, Compostela, Cebu, 6003, Philippines";
        latitude = "100.23";
        longitude = "50.123";
        delivereefee = UserPreferenceManager.getDeliveryFee(getViewContext());
        vehicleid = UserPreferenceManager.getVehicleID(getViewContext());

        for (int i = 0; i < listqueue.size(); i++) {
            listorders.add(new OrderSkus(listqueue.get(i).getItemCode(),
                    listqueue.get(i).getCatClass(),
                    listqueue.get(i).getQuantity(),
                    listqueue.get(i).getPackageCode(),
                    listqueue.get(i).getItemPicUrl()));
        }

//        Gson gson = new Gson();
        orderskus = new Gson().toJson(listorders);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OrderDeliverySummary.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplaceorder: {
                //api session
                createSession(callsession);
                break;
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
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);

                    addOrders(addOrdersSession);
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {

        }
    };

    private void addOrders(Callback<AddOrdersResponse> addOrdersCallback) {

        String paymenttype =  UserPreferenceManager.getStringPreference(getViewContext(),UserPreferenceManager.KEY_TEMP_PAYMENTOPTION);
        Call<AddOrdersResponse> addorders = RetrofitBuild.addOrdersService(getViewContext())
                .addOrdersCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        ordertype,
                        customername,
                        customermobile,
                        customeremail,
                        customerdeliveryaddress,
                        longitude,
                        latitude,
                        ".",
                        delivereefee,
                        vehicleid,
                        orderskus,
                        "",
                        ".",
                        "",
                        (paymenttype.isEmpty() || paymenttype == null) ? "CASH": paymenttype);

        addorders.enqueue(addOrdersCallback);
    }

    private final Callback<AddOrdersResponse> addOrdersSession = new Callback<AddOrdersResponse>() {

        @Override
        public void onResponse(Call<AddOrdersResponse> call, Response<AddOrdersResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {

            }
        }

        @Override
        public void onFailure(Call<AddOrdersResponse> call, Throwable t) {

        }
    };
}
