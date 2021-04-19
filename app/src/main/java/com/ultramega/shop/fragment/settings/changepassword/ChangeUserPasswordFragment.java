package com.ultramega.shop.fragment.settings.changepassword;

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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.UpdatePasswordResponse;
import com.ultramega.shop.responses.UpdatePasswordWholeSalerResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 10/19/2017.
 */

public class ChangeUserPasswordFragment extends BaseFragment implements View.OnClickListener {
    private MaterialEditText edtnewpassword;
    private MaterialEditText edtpassword;
    private MaterialEditText edtconfirmpassword;
    private Button savepassword;

    private UltramegaShopUtilities mdb;
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String password = "";
    private String userid = "";
    private String oldpassword = "";
    private String usertype = "";

    private MaterialDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        setHasOptionsMenu(true);

        init(view);

        initData();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do s.th.

                getActivity().finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savepassword: {

                if (evaluateForm()) {

                    if (evaluatePassword()) {

                        oldpassword = edtpassword.getText().toString().trim();
                        password = edtnewpassword.getText().toString().trim();
                        getSession();

                    } else {

                        openErrorDialog("Passwords are mismatch.");

                    }

                } else {

                    openErrorDialog("Please input all fields.");

                }
                break;
            }
            case R.id.closeDialog: {


                mDialog.dismiss();

                getActivity().finish();


                break;
            }
        }

    }

    private void init(View view) {
        TextView profileLabel = (TextView) view.findViewById(R.id.profileLabel);
        profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Set up new password"));
        edtnewpassword = (MaterialEditText) view.findViewById(R.id.edtnewpassword);
        edtnewpassword.setVisibility(View.VISIBLE);
        edtpassword = (MaterialEditText) view.findViewById(R.id.edtpassword);
        edtconfirmpassword = (MaterialEditText) view.findViewById(R.id.edtconfirmpassword);
        savepassword = (Button) view.findViewById(R.id.savepassword);
        savepassword.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", getString(R.string.string_save_password)));
        savepassword.setOnClickListener(this);
    }

    private void initData() {
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mdb.getConsumerProfile();
            userid = consumerProfile.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            userid = wholesalerProfile.getUserID();
        }
    }

    private boolean evaluateCount() {
        return edtpassword.isCharactersCountValid() &&
                edtnewpassword.isCharactersCountValid() &&
                edtconfirmpassword.isCharactersCountValid();
    }

    private boolean evaluateForm() {
        return edtnewpassword.getText().toString().trim().length() > 0 &&
                edtpassword.getText().toString().trim().length() > 0 &&
                edtconfirmpassword.getText().toString().trim().length() > 0;
    }

    private boolean evaluatePassword() {
        return edtnewpassword.getText().toString().trim().equals(edtconfirmpassword.getText().toString().trim());
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            //api session
            createSession(callsession);
        } else {
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
                    if (usertype.equals("CONSUMER")) {
                        updatePassword(updatePasswordSession);
                    } else if (usertype.equals("WHOLESALER")) {
                        updatePasswordWholeSaler(updatePasswordWholeSalerSession);
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

    private void updatePassword(Callback<UpdatePasswordResponse> updatePasswordCallback) {
        Call<UpdatePasswordResponse> updatePassword = RetrofitBuild.updatePasswordService(getViewContext())
                .updatePasswordCall(imei,
                        authcode,
                        sessionid,
                        password,
                        userid,
                        oldpassword,
                        usertype);
        updatePassword.enqueue(updatePasswordCallback);
    }

    private final Callback<UpdatePasswordResponse> updatePasswordSession = new Callback<UpdatePasswordResponse>() {

        @Override
        public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong with the server.");
        }
    };

    private void updatePasswordWholeSaler(Callback<UpdatePasswordWholeSalerResponse> updatePasswordCallback) {
        Call<UpdatePasswordWholeSalerResponse> updatePassword = RetrofitBuild.updatePasswordService(getViewContext())
                .updatePasswordWholeSalerCall(imei,
                        authcode,
                        sessionid,
                        password,
                        userid,
                        oldpassword,
                        usertype);
        updatePassword.enqueue(updatePasswordCallback);
    }

    private final Callback<UpdatePasswordWholeSalerResponse> updatePasswordWholeSalerSession = new Callback<UpdatePasswordWholeSalerResponse>() {

        @Override
        public void onResponse(Call<UpdatePasswordWholeSalerResponse> call, Response<UpdatePasswordWholeSalerResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
            if (errorBody == null) {
                evaluateWholesalerResponse(response);
            }
        }

        @Override
        public void onFailure(Call<UpdatePasswordWholeSalerResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong with the server.");
        }
    };

    private void evaluateResponse(Response<UpdatePasswordResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                mDialog = new MaterialDialog.Builder(getViewContext())
                        .cancelable(false)
                        .customView(R.layout.dialog_wholesaler_application_in_process, false)
                        .show();

                View view = mDialog.getCustomView();
                ImageView closeDialog = (ImageView) view.findViewById(R.id.closeDialog);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("Request successful");
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

    private void evaluateWholesalerResponse(Response<UpdatePasswordWholeSalerResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                mDialog = new MaterialDialog.Builder(getViewContext())
                        .cancelable(false)
                        .customView(R.layout.dialog_wholesaler_application_in_process, false)
                        .show();

                View view = mDialog.getCustomView();
                ImageView closeDialog = (ImageView) view.findViewById(R.id.closeDialog);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("Request successful");
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

}
