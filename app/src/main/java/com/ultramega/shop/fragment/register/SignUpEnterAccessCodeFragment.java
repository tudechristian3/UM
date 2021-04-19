package com.ultramega.shop.fragment.register;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.responses.GetBranchesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.PartialRegistrationEnterAccessCodeResponse;
import com.ultramega.shop.responses.PartialRegistrationEnterMobileResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpEnterAccessCodeFragment extends BaseFragment implements View.OnClickListener {

    private String authenticationid = "";
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String mobilenumber = "";
    private String countrycode = "";
    private String country = "";
    private String accesscode = "";
    private String processtype = "";

    private boolean isViewShown = false;
    private MaterialEditText edt_enter_access_code;
    private SignUpEnterMobileFragment mobile;
    private String usertype = "";

    private TextView txv_access_code_label;
    private TextView txv_countdown_timer;
    private TextView txv_resend;

    private UltramegaShopUtilities mdb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_enter_access_code, container, false);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            authenticationid = getArguments().getString("AuthenticationID");
            mobilenumber = getArguments().getString("MobileNumber");
            countrycode = getArguments().getString("CountryCode");
            country = getArguments().getString("Country");
            processtype = getArguments().getString("processtype");
        }
        //setup title
        setTitle();
        countdown();
        init(view);
        if (processtype.equals("FORGETPASSWORD")) {
            UserPreferenceManager.saveBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE", true);
        }
        return view;
    }

    private void init(View view) {
        edt_enter_access_code = (MaterialEditText) view.findViewById(R.id.edt_enter_access_code);
        Button button_access_code = (Button) view.findViewById(R.id.btnContinueAccessCode);
        button_access_code.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Continue"));
        button_access_code.setOnClickListener(this);

        txv_resend = (TextView) view.findViewById(R.id.txv_resend);
        txv_resend.setOnClickListener(this);

        txv_access_code_label = (TextView) view.findViewById(R.id.txv_access_code_label);
        txv_countdown_timer = (TextView) view.findViewById(R.id.txv_countdown_timer);

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
    }

    private void setTitle() {
        if (getArguments() != null) {
            processtype = getArguments().getString("processtype");
            if (processtype.equals(".")) {
                ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Sign Up");
            } else if (processtype.equals("FORGETPASSWORD")) {
                ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Forgot Password");
            }
        } else {
            ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Sign Up");
        }
    }

    private void partialRegistrationEnterAccessCode(Callback<PartialRegistrationEnterAccessCodeResponse> partialRegistrationEnterAccessCodeCallback, String mobilenumber, String accesscode, String authcode, String authenticationid, String imei) {
        Call<PartialRegistrationEnterAccessCodeResponse> registrationresponse = RetrofitBuild.partialRegistrationEnterAccessCodeService(getViewContext())
                .partialRegistrationEnterAccessCodeCall(mobilenumber,
                        accesscode,
                        authcode,
                        authenticationid,
                        imei,
                        sessionid);
        registrationresponse.enqueue(partialRegistrationEnterAccessCodeCallback);
    }

    private void partialRegistrationWholeSalerAccessCode(Callback<PartialRegistrationEnterAccessCodeResponse> partialRegistrationWholeSalerAccessCodeCallback) {
        Call<PartialRegistrationEnterAccessCodeResponse> registrationresponse = RetrofitBuild.partialRegistrationEnterAccessCodeService(getViewContext())
                .partialRegistrationWholeSalerAccessCodeCall(countrycode.concat(mobilenumber),
                        accesscode,
                        authcode,
                        authenticationid,
                        imei,
                        sessionid);
        registrationresponse.enqueue(partialRegistrationWholeSalerAccessCodeCallback);
    }

    private final Callback<GetSessionResponse> callsessionAccessCode = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    if (usertype.equals("CONSUMER")) {
                        partialRegistrationEnterAccessCode(partialRegistrationEnterAccessCodeSession, countrycode.concat(mobilenumber), accesscode, authcode, authenticationid, imei);
                    } else if (usertype.equals("WHOLESALER")) {
                        partialRegistrationWholeSalerAccessCode(partialRegistrationWholeSalerAccessCodeSession);
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

    private final Callback<PartialRegistrationEnterAccessCodeResponse> partialRegistrationWholeSalerAccessCodeSession = new Callback<PartialRegistrationEnterAccessCodeResponse>() {

        @Override
        public void onResponse(Call<PartialRegistrationEnterAccessCodeResponse> call, Response<PartialRegistrationEnterAccessCodeResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response.body().getStatus(), response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<PartialRegistrationEnterAccessCodeResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private final Callback<PartialRegistrationEnterAccessCodeResponse> partialRegistrationEnterAccessCodeSession = new Callback<PartialRegistrationEnterAccessCodeResponse>() {

        @Override
        public void onResponse(Call<PartialRegistrationEnterAccessCodeResponse> call, Response<PartialRegistrationEnterAccessCodeResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response.body().getStatus(), response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<PartialRegistrationEnterAccessCodeResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void evaluateResponse(String statuscode, String message) {
        switch (statuscode) {
            case "000": {
                if (processtype.equals(".")) {
                    if (usertype.equals("CONSUMER")) {
                        hideProgressDialog();
                        hideKeyboard(getViewContext());
                        Bundle args = new Bundle();
                        args.putString("AuthenticationID", authenticationid);
                        args.putString("processtype", processtype);
                        args.putString("MobileNumber", mobilenumber);
                        args.putString("CountryCode", countrycode);
                        args.putString("Country", country);
                        ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(4, args);
//                        ((ConsumerWholesalerSignUpActivity) getViewContext()).setFinalRegistration(4, mobilenumber, countrycode, country);
                    } else if (usertype.equals("WHOLESALER")) {
                            //get list branches
                            getBranchesSession();

                    }
                } else if (processtype.equals("FORGETPASSWORD")) {
                    UserPreferenceManager.saveBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE", false);
                    hideProgressDialog();
                    hideKeyboard(getViewContext());
                    Bundle args = new Bundle();
                    args.putString("AuthenticationID", authenticationid);
                    args.putString("processtype", processtype);
                    args.putString("MobileNumber", mobilenumber);
                    args.putString("CountryCode", countrycode);
                    args.putString("Country", country);
                    ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(7, args);
                }
                break;
            }
            default: {
                hideProgressDialog();
                openErrorDialog(message);
                break;
            }
        }
    }

    private void getBranchesSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //showProgressDialog();
            createSession(callbranchessession);
        } else {
            hideProgressDialog();
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callbranchessession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    getBranches(getBranchesSession);
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

    private void getBranches(Callback<GetBranchesResponse> getBranchesCallback) {
        Call<GetBranchesResponse> getbranches = RetrofitBuild.getBranchesService(getViewContext())
                .getBranchesCall(authcode,
                        imei,
                        sessionid);
        getbranches.enqueue(getBranchesCallback);
    }

    private final Callback<GetBranchesResponse> getBranchesSession = new Callback<GetBranchesResponse>() {

        @Override
        public void onResponse(Call<GetBranchesResponse> call, Response<GetBranchesResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_BRANCHES);
                    List<Branches> listbranches = response.body().getGetBranches();
                    for (Branches branches : listbranches) {
                        mdb.insertAllBranches(branches, 0);
                    }
                    hideKeyboard(getViewContext());
                    Bundle args = new Bundle();
                    args.putString("AuthenticationID", authenticationid);
                    args.putString("processtype", processtype);
                    args.putString("MobileNumber", mobilenumber);
                    args.putString("CountryCode", countrycode);
                    args.putString("Country", country);
                    ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(5, args);
                    //((ConsumerWholesalerSignUpActivity) getViewContext()).setFinalRegistration(5, mobilenumber, countrycode, country);
                } else {
                    openErrorDialog(response.body().getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<GetBranchesResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                new MaterialDialog.Builder(getViewContext())
                        .content("You are about to leave this page.")
                        .cancelable(false)
                        .negativeText("Close")
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                Bundle args = new Bundle();
                                args.putString("processtype", processtype);

                                if (processtype.equals("FORGETPASSWORD")) {
                                    UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_AUTHENTICATION");
                                    UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_MOBILE_NUMBER");
                                    UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY_CODE");
                                    UserPreferenceManager.removePreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY");
                                    UserPreferenceManager.saveBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE", false);
                                    UserPreferenceManager.saveBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE_CANCELLED", true);
                                }

                                ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(2, args);

                            }
                        })
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinueAccessCode: {
                if (edt_enter_access_code.getText().toString().trim().length() > 0) {
                    accesscode = edt_enter_access_code.getText().toString();

                    if (isAirplaneModeOn(getViewContext())) {
                        showError("Airplane mode is enabled. Please disable Airplane mode and enable GPS or Internet to proceed.");
                    } else {
                        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
                            if (isGPSModeOn()) {
                                getSession();
                            } else {
                                showGPSDisabledAlertToUser();
                            }
                        } else {
                            showError(getString(R.string.generic_internet_error_message));
                        }
                    }
                }
                break;
            }
            case R.id.txv_resend: {
                createSession(callsession);
                countdown();
                txv_access_code_label.setText(CommonFunctions.setTypeFace(getViewContext(),
                        "fonts/ElliotSans-Medium.ttf", "An access code was sent to you via SMS which can take a while. Thank you for your patience."));
                txv_countdown_timer.setVisibility(View.VISIBLE);
                txv_resend.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            createSession(callsessionAccessCode);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private void partialRegistrationEnterMobile(Callback<PartialRegistrationEnterMobileResponse> partialRegistrationEnterMobileCallback, String mobilenumber, String imei, String sessionid, String authcode) {
        Call<PartialRegistrationEnterMobileResponse> registrationresponse = RetrofitBuild.partialRegistrationEnterMobileService(getViewContext())
                .partialRegistrationEnterMobileCall(mobilenumber,
                        imei,
                        sessionid,
                        authcode,
                        processtype);
        registrationresponse.enqueue(partialRegistrationEnterMobileCallback);
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);

                    partialRegistrationEnterMobile(partialRegistrationEnterMobileSession, countrycode + mobilenumber, imei, sessionid, authcode);
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

    private final Callback<PartialRegistrationEnterMobileResponse> partialRegistrationEnterMobileSession = new Callback<PartialRegistrationEnterMobileResponse>() {

        @Override
        public void onResponse(Call<PartialRegistrationEnterMobileResponse> call, Response<PartialRegistrationEnterMobileResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    authenticationid = response.body().getAuthenticationCode();

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                openErrorDialog(response.body().getMessage());
            }
        }

        @Override
        public void onFailure(Call<PartialRegistrationEnterMobileResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    public void countdown() {
        new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txv_access_code_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "An access code was sent to you via SMS which can take a while. Thank you for your patience."));
                txv_countdown_timer.setText("Resend in: " + String.format("%d min & %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                txv_access_code_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "The SMS with the code did not arrive?"));
                txv_resend.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Resend"));
                txv_resend.setVisibility(View.VISIBLE);
                txv_countdown_timer.setVisibility(View.GONE);

            }
        }.start();
    }
}
