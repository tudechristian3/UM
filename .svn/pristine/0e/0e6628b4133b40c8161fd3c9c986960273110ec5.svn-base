package com.ultramega.shop.fragment.myshoppingcart.checkout.delivery;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.Order;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.Points;
import com.ultramega.shop.pojo.SpeedyData;
import com.ultramega.shop.responses.CalculateOrderChargeResponse;
import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetOrdersQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;
import static java.util.Calendar.DAY_OF_MONTH;


public class SpecialInstructionsFragment extends BaseFragment implements View.OnClickListener {

    private UltramegaShopUtilities mdb;
    private Branches selectedBranch;

    private EditText instructions;

    private String address;
    private String latitude;
    private String longitude;

    private String usertype = "";

    Date dates;

    private MaterialEditText scheduledate;
    private Spinner vehicle_type;

    public String imei = "";
    public String userid = "";
    public String devicetype = "";
    public String index = "";
    public String sessionid = "";
    public String authcode = "";
    public String mobilenumber = "";
    public String vehicleid = "";
    public String vehicletype = "";
    public String branchid = "";
    public String vehiclena = "";

    TextView delivery_charge;

    Order order;

    private MaterialDialog mPickUpDialog;

    //private Branches branches;
    private List<OrdersQueue> orderDetails = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_instructions, container, false);

        setHasOptionsMenu(true);
        getValues();



        //set up title
        ((CheckoutProductsActivity) getViewContext()).setActionBarTitle("Checkout");

        if (getArguments() != null) {
            if (getArguments().getString("address").trim().length() > 0) {
                address = getArguments().getString("address");
                latitude = getArguments().getString("latitude");
                longitude = getArguments().getString("longitude");
            }
        }



        usertype = getCurrentUserType(getViewContext());

        instructions = (EditText) view.findViewById(R.id.instructions);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView label2 = (TextView) view.findViewById(R.id.scheduleDeliver);
        TextView label3 = (TextView) view.findViewById(R.id.schedulelabel);

        delivery_charge = (TextView) view.findViewById(R.id.labeldelivery);

        TextView label4 = (TextView) view.findViewById(R.id.labeldeliverycharge);
        vehicle_type = view.findViewById(R.id.vehicle_type);


        scheduledate = (MaterialEditText) view.findViewById(R.id.scheduledate);
        scheduledate.setOnClickListener(this);

        label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "SPECIAL INSTRUCTIONS"));
        label2.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "VEHICLE TYPE"));
        label3.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "SCHEDULE OF DELIVERY"));
        //label3.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "SCHEDULE OF DELIVERY"));
        label4.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "DELIVERY CHARGE"));




        List<String> vehicle = new ArrayList<>();
        vehicle.add(0,"Select Vehicle Type");
        vehicle.add("Motorbike");
        vehicle.add("Car");

        ArrayAdapter<String> vehicleAdapter;
        vehicleAdapter = new ArrayAdapter<String>(getViewContext(), android.R.layout.simple_spinner_item, vehicle);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicle_type.setAdapter(vehicleAdapter);
        sessionid = UserPreferenceManager.getSession(getActivity());
        vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (adapterView.getItemAtPosition(position).equals("Motorbike")) {
                        sessionid = UserPreferenceManager.getSession(getActivity());
                        //sessionid = "d11b6b60-85fc-11eb-b71c-5718cfb5f362";
                        String items = adapterView.getItemAtPosition(position).toString();
                        vehicletype = items;
                        vehicleid = String.valueOf(8);
                        //                    Toast.makeText(getViewContext(), vehicleid, Toast.LENGTH_SHORT).show();
                        //validateQRPartialV2();
                        vehicle(getVehicleCallback);
                    }

                    if (adapterView.getItemAtPosition(position).equals("Car")) {
                        sessionid = UserPreferenceManager.getSession(getActivity());
                        //sessionid = "d11b6b60-85fc-11eb-b71c-5718cfb5f362";
                        String items = adapterView.getItemAtPosition(position).toString();
                        vehicletype = items;
                        vehicleid = String.valueOf(7);
                        UserPreferenceManager.saveVehicleID(getViewContext(), vehicleid);
                        Log.d("Master Tude", "onCreate: " + vehicleid);
                        //validateQRPartialV2();
                        vehicle(getVehicleCallback);
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



//        vehicletype = vehicle_type.getSelectedItem().toString();
//        Toast.makeText(getViewContext(), "vehicle_type" + vehicletype, Toast.LENGTH_SHORT).show();


        return view;
    }

    private void getValues() {
        mdb = new UltramegaShopUtilities(getViewContext());
        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        userid = itemProf.getUserID();
        customerid = mdb.getConsumerID();
        userid = itemProf.getUserID();
        mobilenumber = itemProf.getMobileNo();
        branchid = UserPreferenceManager.getBranchID(getActivity());
        sessionid = UserPreferenceManager.getSession(getActivity());
        //sessionid = UserPreferenceManager.getSession(getViewContext());
    }







//    private void validateQRPartialV2() {
//
//        if(CommonFunctions.getConnectivityStatus(requireActivity()) > 0) {
//            LinkedHashMap<String,String> parameters = new LinkedHashMap<>();
//            parameters.put("usertype",usertype);
//            parameters.put("sessionid",sessionid);
//            parameters.put("imei",imei);
//            parameters.put("authcode",CommonFunctions.getSha1Hex(imei + sessionid + customerid));
//            parameters.put("customerid",customerid);
//            parameters.put("userid", userid);
//            parameters.put("mobilenumber", mobilenumber);
//            parameters.put("vehicleid", vehicleid);
//            parameters.put("vehicletype", vehicletype);
//            parameters.put("longitude", longitude);
//            parameters.put("latitude", latitude);
//            parameters.put("branchid", branchid);
//            parameters.put("address", address);
//
//            String checker = new Gson().toJson(parameters);
//            Log.d("SPecial", "JSON ipada: "+checker);
//
//            vehicle(getVehicleCallback);
//
//        } else {
//            Toast.makeText(getContext(), "qwe", Toast.LENGTH_SHORT).show();
//        }
//    }

        private void vehicle(Callback<CalculateOrderChargeResponse> getVehicleCallback) {
            Call<CalculateOrderChargeResponse> call = RetrofitBuild.getOrdersQueueService(getContext())
                    .calculateOrderCharge(
                            usertype,
                            sessionid,
                            imei,
                            authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid),
                            customerid,
                            userid,
                            mobilenumber,
                            vehicleid,
                            vehicletype,
                            longitude,
                            latitude,
                            branchid,
                            address
                    );
            call.enqueue(getVehicleCallback);
        }

    private Callback<CalculateOrderChargeResponse> getVehicleCallback = new Callback<CalculateOrderChargeResponse>() {

        @Override
        public void onResponse(@NotNull Call<CalculateOrderChargeResponse> call, Response<CalculateOrderChargeResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                switch (response.body().getStatus()){
                    case "000":
                        if(response.body().getData() != null ){
                            if(response.body().getData().getParameter_warnings() == null){
                                //sessionid = UserPreferenceManager.getSession(getActivity());
                                sessionid = response.body().getSession();
                                authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                                Order order = response.body().getData().getOrder();
                                Order test = response.body().getData().getParameter_warnings();
                                String delivery_fee = order.getDelivery_fee_amount();
                                //String parameter_warnings = order.getParameter_warnings();
                                Log.d("neil","DELIVERY FEE : "+sessionid);
                                //Log.d("neil","parameter_warnings : "+parameter_warnings);
                                delivery_charge.setText(delivery_fee);
                                UserPreferenceManager.saveDeliveryFee(getViewContext(), delivery_fee);
                            }
                            else{
                                showError("Please select other address");
                            }

//                            Log.d("Master Tude", "response: " + delivery_fee);
//                            Log.d("Special", "response: " + response.body().getData());
//                            Log.d("Special", "responseGetOrder: " + response.body().getData().getOrder());
//                            Log.d("Special", "responseOrder: " + order);
//                            Log.d("Special", "responseData: " + response.body().getData());
//                            Log.d("Special", "responseStatus: " + response.body().getStatus());
//                            Log.d("Special", "responseMessage: " + response.body().getMessage());
                        }
                        break;
                    case "001":
                            forceLogoutDialog("Session expired...");
                        break;
                    default:
                         showError(response.body().getMessage());
                        break;

                }

            } else {
                showError("Something went wrong. Please try again.");
            }
        }


        @Override
        public void onFailure(Call<CalculateOrderChargeResponse> call, Throwable t) {
            showError(t.getMessage());
            t.printStackTrace();
        }
    };


    public void sesssionExpired(){
        mPickUpDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_session, false)
                .build();

        View view = mPickUpDialog.getCustomView();

        view.findViewById(R.id.txvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getViewContext(), "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        mPickUpDialog.show();
    }

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

//                String specialinstructions;
//                if (evaluateForm()) {
//                    specialinstructions = instructions.getText().toString().trim();
//                } else {
//                    specialinstructions = ".";
//                }
//
//                ((CheckoutProductsActivity) getViewContext()).setInstructions(2, address, latitude, longitude, specialinstructions);
//
//                hideKeyboard(getViewContext());

                if(delivery_charge.getText().toString().equals("")) {
                    showError("Please select other address");
                }
                else{
                        if (scheduledate.getText().toString().trim().length() != 0) {
                            hideKeyboard(getViewContext());
                            String specialinstructions;
                            if (evaluateForm()) {
                                specialinstructions = instructions.getText().toString().trim();
                            } else {
                                specialinstructions = ".";
                            }

                            ((CheckoutProductsActivity) getViewContext()).setInstructions(2, address, latitude, longitude, specialinstructions);
                        }
                        else {
                            scheduledate.setError("Invalid! Schedule Date is required.");
                            scheduledate.requestFocus();
                        }
                }



//                else if(Calendar.DAY_OF_MONTH == Calendar.DAY_OF_MONTH){
//                    scheduledate.setError("Invalid! Current Date.");
//                    scheduledate.requestFocus();
//                }




                return true;
            }
            case android.R.id.home: {
                hideKeyboard(getViewContext());
                if (usertype.equals("CONSUMER")) {
                    ((CheckoutProductsActivity) getViewContext()).displayView(0, null);
                } else if (usertype.equals("WHOLESALER")) {
                    getActivity().finish();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean evaluateForm() {
        return instructions.getText().toString().trim().length() > 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scheduledate: {
                SimpleDateFormat fmt = new SimpleDateFormat("d/M/yyyy");
                dates = new Date();
                java.util.Calendar now = java.util.Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//                                    if(date.compareTo(String.valueOf(DAY_OF_MONTH)) <= 1){
//                                        Toast.makeText(getViewContext(), "Current date is not allowed", Toast.LENGTH_SHORT).show();
//                                    }
                                if(date.equals(fmt.format(dates))){
                                    Toast.makeText(getViewContext(), "Current date is not allowed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    scheduledate.setText(date);
                                }
                            }
                        },
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(DAY_OF_MONTH) + 1
                );
                dpd.setMinDate(now);
                dpd.show(getActivity().getFragmentManager(), "dialog");
                break;
            }
        }
    }
}

