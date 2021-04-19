package com.ultramega.shop.fragment.login;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.SetNewPasswordResponse;
import com.ultramega.shop.responses.SetNewPasswordWholeSalerResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 9/11/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private String mobilenumber = "";
    private String countrycode = "";
    private String country = "";
    private String processtype = "";
    private String sessionid = "";
    private String authcode = "";
    private String imei = "";
    private String password = "";
    private String usertype = "";
    private String customerid = "";
    private String authenticationid = "";

    private Button savepassword;
    private MaterialEditText edtpassword;
    private MaterialEditText edtconfirmpassword;

    private List<ConsumerProfile> listprofile;
    private UltramegaShopUtilities mdb;

    private MaterialDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        setHasOptionsMenu(true);

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        mobilenumber = getArguments().getString("MobileNumber");
        countrycode = getArguments().getString("CountryCode");
        country = getArguments().getString("Country");
        processtype = getArguments().getString("processtype");
        authenticationid = getArguments().getString("AuthenticationID");
        userid = countrycode.trim().concat(mobilenumber.trim());
        listprofile = new ArrayList<>();

        view.findViewById(R.id.edtnewpassword).setVisibility(View.GONE);

        edtpassword = (MaterialEditText) view.findViewById(R.id.edtpassword);
        edtpassword.setHint(getString(R.string.string_new_password));

        edtconfirmpassword = (MaterialEditText) view.findViewById(R.id.edtconfirmpassword);

        savepassword = (Button) view.findViewById(R.id.savepassword);
        savepassword.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_save_password)));
        savepassword.setOnClickListener(this);

        TextView profileLabel = (TextView) view.findViewById(R.id.profileLabel);
        profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Set up new password"));

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do s.th.
                Bundle args = new Bundle();
                args.putString("AuthenticationID", authenticationid);
                args.putString("processtype", processtype);
                args.putString("MobileNumber", mobilenumber);
                args.putString("CountryCode", countrycode);
                args.putString("Country", country);
                ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(3, args);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean evaluatePassword() {
        return edtpassword.getText().toString().trim().equals(edtconfirmpassword.getText().toString().trim());
    }

    private boolean evaluateForm() {
        return edtpassword.getText().toString().trim().length() > 0 &&
                edtconfirmpassword.getText().toString().trim().length() > 0;
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            //api session
            createSession(callsession);
        } else {
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
                    if (usertype.equals("CONSUMER")) {
                        setNewPassword(setNewPasswordSession);
                    } else if (usertype.equals("WHOLESALER")) {
                        setNewPasswordWholeSaler(setNewPasswordWholeSalerSession);
                    }
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private void setNewPasswordWholeSaler(Callback<SetNewPasswordWholeSalerResponse> setNewPasswordWholeSalerCallback) {
        Call<SetNewPasswordWholeSalerResponse> setnewpasswordwholesaler = RetrofitBuild.setNewPasswordWholeSalerService(getViewContext())
                .setNewPasswordWholeSalerCall(imei,
                        authcode,
                        sessionid,
                        password,
                        userid,
                        FirebaseInstanceId.getInstance().getToken());
        setnewpasswordwholesaler.enqueue(setNewPasswordWholeSalerCallback);
    }

    private final Callback<SetNewPasswordWholeSalerResponse> setNewPasswordWholeSalerSession = new Callback<SetNewPasswordWholeSalerResponse>() {

        @Override
        public void onResponse(Call<SetNewPasswordWholeSalerResponse> call, Response<SetNewPasswordWholeSalerResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateWholesalerResponse(response);
            } else {
                if (isAdded()) {
                    showError("Something went wrong with the server.");
                }
            }
        }

        @Override
        public void onFailure(Call<SetNewPasswordWholeSalerResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                showError("Something went wrong with the server.");
            }
        }
    };

    private void evaluateWholesalerResponse(Response<SetNewPasswordWholeSalerResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);

                List<WholesalerProfile> listwholesalerprofile = response.body().getWholesalerProfile();

                for (WholesalerProfile profile : listwholesalerprofile) {
                    mdb.insertWholesalerProfile(profile);
                }
                customerid = listwholesalerprofile.get(0).getWholesalerID();

                //get shopping carts queue
                createSession(cartsqueuesession);

                break;
            }
            default: {
                hideKeyboard(getViewContext());
                hideProgressDialog();
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    private void setNewPassword(Callback<SetNewPasswordResponse> setNewPasswordCallback) {
        Call<SetNewPasswordResponse> setnewpassword = RetrofitBuild.setNewPasswordService(getViewContext())
                .setNewPasswordCall(imei,
                        authcode,
                        sessionid,
                        password,
                        userid,
                        FirebaseInstanceId.getInstance().getToken());
        setnewpassword.enqueue(setNewPasswordCallback);
    }

    private final Callback<SetNewPasswordResponse> setNewPasswordSession = new Callback<SetNewPasswordResponse>() {

        @Override
        public void onResponse(Call<SetNewPasswordResponse> call, Response<SetNewPasswordResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);

                    listprofile = response.body().getConsumerProfile();
                    for (ConsumerProfile profile : listprofile) {
                        mdb.insertConsumerProfile(profile);
                    }
                    customerid = listprofile.get(0).getConsumerID();

                    //get shopping carts queue
                    createSession(cartsqueuesession);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<SetNewPasswordResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                showError("Something went wrong with the server.");
            }
        }
    };

    private final Callback<GetSessionResponse> cartsqueuesession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);
                    fetchShoppingCarts(callshoppingsession);
                } else {
                    hideProgressDialog();
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

    private void fetchShoppingCarts(Callback<FetchShoppingCartsQueueResponse> getshopqueueCallback) {
        Call<FetchShoppingCartsQueueResponse> shopqueue = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
                .fetchShoppingCartsQueueCall(imei,
                        authcode,
                        "0",
                        sessionid,
                        userid,
                        customerid,
                        getCurrentUserType(getViewContext()));
        shopqueue.enqueue(getshopqueueCallback);
    }

    private final Callback<FetchShoppingCartsQueueResponse> callshoppingsession = new Callback<FetchShoppingCartsQueueResponse>() {

        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void evaluateResponse(Response<FetchShoppingCartsQueueResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
                for (ShoppingCartsQueue queue : shoppingqueue) {
                    mdb.insertAllShoppingCartsQueue(queue);
                }

                mDialog = new MaterialDialog.Builder(getViewContext())
                        .cancelable(false)
                        .customView(R.layout.dialog_wholesaler_application_in_process, false)
                        .show();

                View view = mDialog.getCustomView();
                ImageView closeDialog = (ImageView) view.findViewById(R.id.closeDialog);
                // TextView title = (TextView) view.findViewById(R.id.title);
                // title.setText("Request successful");
                TextView message = (TextView) view.findViewById(R.id.message);
                message.setText("Your password has been changed successfully.");
                closeDialog.setOnClickListener(this);

                break;
            }
            default: {
                hideKeyboard(getViewContext());
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savepassword: {
                if (evaluateForm()) {
                    if (evaluatePassword()) {
                        password = edtpassword.getText().toString().trim();
                        getSession();
                    } else {
                        openErrorDialog("Passwords are mismatch.");
                    }
                } else {
                    openErrorDialog("Invalid Passwords.");
                }

                break;
            }
            case R.id.closeDialog: {
                //set up login to true
                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);

                mDialog.dismiss();

                MainActivity.start(getViewContext(), 0);

                break;
            }
        }
    }

}
