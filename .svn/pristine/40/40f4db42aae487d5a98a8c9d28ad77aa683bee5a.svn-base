package com.ultramega.shop.fragment.myshoppingcart.checkout.delivery;

import android.location.Address;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.AddAddressActivity;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.AddConsumerAddressResponse;
import com.ultramega.shop.responses.GetConsumerAddressResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 8/9/2017.
 */

public class ConfirmAddressFragment extends BaseFragment implements View.OnClickListener {

    private UltramegaShopUtilities mdb;

    private TextView addresslabel;
    //    private TextView instructionsLabel;
    private TextView addresshint;
    private EditText locationAddress;
    //    private EditText instructions;
    private Button saveAddress;

    private MaterialEditText edt_country;
    private MaterialEditText edt_province;
    private MaterialEditText edt_city;
    private MaterialEditText edt_streetaddress;
    private MaterialEditText edt_zip;

    private String imei = "";
    private String sessionid = "";
    private String authcode = "";
    private String userid = "";
    private String consumerid = "";
    private String streetaddress = "";
    private String city = "";
    private String province = "";
    private String zip = "";
    private String country = "";
    private String longitude = "";
    private String latitude = "";
    private String isdefault = "1";
    private Address address;

    private String intentType = "";

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_address, container, false);

        setHasOptionsMenu(true);

        //setup title
        ((AddAddressActivity) getViewContext()).setActionBarTitle("Add Address");

        mdb = new UltramegaShopUtilities(getViewContext());

        if (mdb.getConsumerAddress().size() == 0) {
            isdefault = "1";
        } else {
            isdefault = "0";
        }

        intentType = getArguments().getString("intenttype");
        address = getArguments().getParcelable("address");

        Log.d("antonhttp", "address: " + new Gson().toJson(address));

        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        consumerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();

        if (address == null) {
            streetaddress = "";
            city = "";
            province = "";
            zip = "";
            country = "";
            longitude = "";
            latitude = "";
        } else {
            streetaddress = address.getThoroughfare();
            city = address.getLocality();
            province = address.getSubAdminArea();
            zip = address.getPostalCode();
            country = address.getCountryName();
            longitude = String.valueOf(address.getLongitude());
            latitude = String.valueOf(address.getLatitude());
        }

        edt_country = (MaterialEditText) view.findViewById(R.id.country);
        edt_province = (MaterialEditText) view.findViewById(R.id.province);
        edt_city = (MaterialEditText) view.findViewById(R.id.city);
        edt_streetaddress = (MaterialEditText) view.findViewById(R.id.streetaddress);
        edt_zip = (MaterialEditText) view.findViewById(R.id.zip);

        edt_country.setText(country);
        edt_province.setText(province);
        edt_city.setText(city);
        edt_streetaddress.setText(streetaddress);
        edt_zip.setText(zip);

        addresshint = (TextView) view.findViewById(R.id.addresshint);
        addresslabel = (TextView) view.findViewById(R.id.addresslabel);
        locationAddress = (EditText) view.findViewById(R.id.locationAddress);
        saveAddress = (Button) view.findViewById(R.id.saveAddress);
        saveAddress.setOnClickListener(this);
        addresslabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "DETAILED ADDRESS"));
        addresshint.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "(Unit number, house number, buildings, street name)"));
        locationAddress.setText(getMarkerAddress(address));

        return view;
    }

    private String getMarkerAddress(Address address) {
        String display_address = "";
        if (address != null) {

            display_address += address.getAddressLine(0) + ", ";

            for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
                display_address += address.getAddressLine(i) + ", ";
            }

            display_address = display_address.substring(0, display_address.length() - 2);
        }

        return display_address;
    }

    private String getAdditionalAddress(Address address) {
        String display_address = "";

        if (address != null) {
            if (address.getFeatureName() != null) {
                display_address += address + ", ";
            }

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                display_address += address.getAddressLine(i);
            }
        }

        return display_address;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
//            case R.id.skip: {
//                ((AddAddressActivity) getViewContext()).setAddress(2, markerAddress);
//                return true;
//            }
            case android.R.id.home: {
                ((AddAddressActivity) getViewContext()).setAddress(1, null, intentType);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveAddress: {
                if (evaluateFields()) {
                    openConfirmAddressDialog("Are you sure you want to save this address?");
                } else {
                    if (isAdded()) {
                        openErrorDialog("All fields are required");
                    }
                }
                break;
            }
        }
    }

    public boolean evaluateFields() {
//        instructions.getText().toString().length() > 0 &&
        return locationAddress.getText().toString().length() > 0 &&
                edt_country.getText().toString().length() > 0 &&
                edt_province.getText().toString().length() > 0 &&
                edt_city.getText().toString().length() > 0 &&
                edt_streetaddress.getText().toString().length() > 0 &&
                edt_zip.getText().toString().length() > 0;
    }

    public void openConfirmAddressDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("PROCEED")
                .negativeText("CANCEL")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgressDialog();
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
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    addConsumerAddress(addConsumerAddressSession);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };

    private void addConsumerAddress(Callback<AddConsumerAddressResponse> addConsumerAddressCallback) {
        Call<AddConsumerAddressResponse> addaddress = RetrofitBuild.addConsumerAddressService(getViewContext())
                .addConsumerAddressCall(
                        consumerid,
                        userid,
                        authcode,
                        imei,
                        sessionid,
                        edt_streetaddress.getText().toString().trim(),
                        edt_city.getText().toString().trim(),
                        edt_province.getText().toString().trim(),
                        edt_zip.getText().toString().trim(),
                        edt_country.getText().toString().trim(),
                        isdefault,
                        longitude,
                        latitude);
        addaddress.enqueue(addConsumerAddressCallback);
    }

    private final Callback<AddConsumerAddressResponse> addConsumerAddressSession = new Callback<AddConsumerAddressResponse>() {

        @Override
        public void onResponse(Call<AddConsumerAddressResponse> call, Response<AddConsumerAddressResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    createSession(calladdresssession);
                } else if (response.body().getStatus().equals("004")) {

                    hideProgressDialog();
                    forceLogoutDialog("Invalid User");

                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong with the server. Please contact support.");
                }
            }
        }

        @Override
        public void onFailure(Call<AddConsumerAddressResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server. Please contact support.");
            }
        }
    };

    private void openSuccessfullDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Ok")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                        if (mdb.getConsumerAddress().size() == 1) {
                            ConsumerAddress item = mdb.getConsumerAddress().get(0);
                            String detailedaddress = item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry();
                            CheckoutProductsActivity.setFirstAddress(getViewContext(), 1, detailedaddress, item.getLatitude(), item.getLongitude());
                        }
                    }
                })
                .show();
    }

    private final Callback<GetSessionResponse> calladdresssession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    String sessionid = response.body().getSession();
                    String authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getConsumerAddress(getConsumerAddressSession, consumerid, userid, authcode, imei, sessionid);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };

    private void getConsumerAddress(Callback<GetConsumerAddressResponse> getConsumerAddressCallback, String consumerid, String userid, String authcode, String imei, String sessionid) {
        Call<GetConsumerAddressResponse> getconsumeraddress = RetrofitBuild.getConsumerAddressService(getViewContext()).getConsumerAddressCall(consumerid, userid, authcode, imei, sessionid);
        getconsumeraddress.enqueue(getConsumerAddressCallback);
    }

    private final Callback<GetConsumerAddressResponse> getConsumerAddressSession = new Callback<GetConsumerAddressResponse>() {

        @Override
        public void onResponse(Call<GetConsumerAddressResponse> call, Response<GetConsumerAddressResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                    List<ConsumerAddress> address = response.body().getConsumerAddress();
                    for (ConsumerAddress consumeraddress : address) {
                        mdb.insertConsumerAddress(consumeraddress);
                    }

                    if (intentType.equals("CHECKOUT")) {
                        getActivity().finish();
                        if (mdb.getConsumerAddress().size() == 1) {
                            ConsumerAddress item = mdb.getConsumerAddress().get(0);
                            String detailedaddress = item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry();
                            CheckoutProductsActivity.setFirstAddress(getViewContext(), 1, detailedaddress, item.getLatitude(), item.getLongitude());
                        }
                    } else if (intentType.equals("SETTINGS")) {
                        getActivity().finish();
                    }

                    //openSuccessfullDialog("Address has been added successfully");

                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                } else {
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetConsumerAddressResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };
}
