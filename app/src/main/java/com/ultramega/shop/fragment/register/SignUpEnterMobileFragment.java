package com.ultramega.shop.fragment.register;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.PartialRegisterWholeSalerResponse;
import com.ultramega.shop.responses.PartialRegistrationEnterMobileResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpEnterMobileFragment extends BaseFragment implements View.OnClickListener {

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String mobilenumber = "";
    private String countrycode = "";
    private String country = "";
    private String usertype = "";
    private String processtype = "";

    private boolean isViewShown = false;

    private TextView countryCode;
    private MaterialEditText txv_enter_mobile;
    private MaterialEditText edt_country;

    private List<String> mCountryCodes;
    private List<String> mCountryCodesFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_enter_mobile, container, false);

        setHasOptionsMenu(true);

        //default data
        countrycode = "63";
        country = "Philippines";
        usertype = getCurrentUserType(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        //setup title
        setTitle();

        init(view);

        return view;
    }

    private void init(View view) {
        Button button_continue = (Button) view.findViewById(R.id.btnContinue);
        countryCode = (TextView) view.findViewById(R.id.countryCode);
        countryCode.setText(getString(R.string.string_63));
        TextView txv_code_label = (TextView) view.findViewById(R.id.txv_code_label);
        txv_enter_mobile = (MaterialEditText) view.findViewById(R.id.txv_enter_mobile);
        button_continue.setOnClickListener(this);
        edt_country = (MaterialEditText) view.findViewById(R.id.edtCountry);
        edt_country.setOnClickListener(this);
        edt_country.setText(getString(R.string.string_philippines));

        mCountryCodes = new ArrayList<>();
        mCountryCodesFinal = new ArrayList<>();
        mCountryCodes = Arrays.asList(getResources().getStringArray(R.array.CountryCodes));
        for (String str : mCountryCodes) {
            String[] country = str.split(",");
            mCountryCodesFinal.add(country[2] + ", " + country[0]);
        }

        button_continue.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Continue"));
        txv_code_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "An access code will be sent to you via SMS"));
    }

    private void setTitle() {
        processtype = getArguments().getString("processtype");
        if (processtype.equals(".")) {
            ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Sign Up");
        } else if (processtype.equals("FORGETPASSWORD")) {
            ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Forgot Password");
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    if (usertype.equals("CONSUMER")) {
                        partialRegistrationEnterMobile(partialRegistrationEnterMobileSession,
                                countrycode.concat(mobilenumber),
                                imei,
                                sessionid,
                                authcode);
                    } else if (usertype.equals("WHOLESALER")) {
                        partialRegisterWholeSaler(partialRegisterWholeSalerSession);
                    }
                } else {
                    hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void partialRegistrationEnterMobile(Callback<PartialRegistrationEnterMobileResponse> partialRegistrationEnterMobileCallback, String mobilenumber, String imei, String sessionid, String authcode) {
        Call<PartialRegistrationEnterMobileResponse> registrationresponse = RetrofitBuild.partialRegistrationEnterMobileService(getViewContext())
                .partialRegistrationEnterMobileCall(mobilenumber,
                        imei,
                        sessionid,
                        authcode,
                        processtype);
        registrationresponse.enqueue(partialRegistrationEnterMobileCallback);
    }

    private final Callback<PartialRegistrationEnterMobileResponse> partialRegistrationEnterMobileSession = new Callback<PartialRegistrationEnterMobileResponse>() {

        @Override
        public void onResponse(Call<PartialRegistrationEnterMobileResponse> call, Response<PartialRegistrationEnterMobileResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response.body().getStatus(), response.body().getAuthenticationCode(), response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<PartialRegistrationEnterMobileResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void partialRegisterWholeSaler(Callback<PartialRegisterWholeSalerResponse> partialRegisterWholeSalerCallback) {
        Call<PartialRegisterWholeSalerResponse> partialregwholesaler = RetrofitBuild.partialRegisterWholeSalerService(getViewContext())
                .partialRegisterWholeSalerCall(countrycode.concat(mobilenumber),
                        imei,
                        sessionid,
                        authcode,
                        processtype);
        partialregwholesaler.enqueue(partialRegisterWholeSalerCallback);
    }

    private final Callback<PartialRegisterWholeSalerResponse> partialRegisterWholeSalerSession = new Callback<PartialRegisterWholeSalerResponse>() {

        @Override
        public void onResponse(Call<PartialRegisterWholeSalerResponse> call, Response<PartialRegisterWholeSalerResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response.body().getStatus(), response.body().getAuthenticationCode(), response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<PartialRegisterWholeSalerResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void evaluateResponse(String statuscode, String AuthenticationID, String message) {
        switch (statuscode) {
            case "000": {
                hideKeyboard(getViewContext());
                UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_AUTHENTICATION");
                UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_MOBILE_NUMBER");
                UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY_CODE");
                UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY");
                UserPreferenceManager.saveStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_AUTHENTICATION", AuthenticationID);
                UserPreferenceManager.saveStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_MOBILE_NUMBER", mobilenumber);
                UserPreferenceManager.saveStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY_CODE", countrycode);
                UserPreferenceManager.saveStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY", country);
                ((ConsumerWholesalerSignUpActivity) getViewContext()).setPartialRegistration(3, AuthenticationID, mobilenumber, countrycode, country, processtype);
                break;
            }
            default: {
                openErrorDialog(message);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (UserPreferenceManager.getBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE_CANCELLED")) {
                    UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_CANCELLED");
                    Intent i = new Intent(getViewContext(), ShoppingModeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue: {
                if (txv_enter_mobile.getText().toString().trim().length() > 0) {
                    String mobileno = getMobileNumber(txv_enter_mobile.getText().toString().trim());
                    if (mobileno.equals("INVALID")) {
                        openErrorDialog("Invalid Mobile Number");
                    } else {
                        country = edt_country.getText().toString().trim();
                        mobilenumber = mobileno.substring(2, mobileno.length());
                        getSession();
                    }
                } else {
                    openErrorDialog("Please enter your mobile number");
                }
                break;
            }
            case R.id.btnLogin: {
                //LoginActivity.start(getViewContext());
                break;
            }
            case R.id.edtCountry: {
                openCountryDialog();
                break;
            }
        }
    }

    private void openCountryDialog() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getViewContext());
        dialog.title("Country");
        dialog.items(mCountryCodesFinal);
        dialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                String[] country = text.toString().split(",");
                edt_country.setText(country[0].trim());
                countrycode = country[1].trim();
                countryCode.setText("+".concat(countrycode));
            }
        });
        dialog.show();
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

}
