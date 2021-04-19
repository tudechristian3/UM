package com.ultramega.shop.activity.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.PesoPay;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.PesoPayDataResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.NumberTextWatcherForThousand;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ban_Lenovo on 9/15/2017.
 */

public class ReloadWalletThruCardActivity extends BaseActivity implements View.OnClickListener {

    private UltramegaShopUtilities mDbUtils;

    private EditText mEdtRemarks;
    private EditText mEdtAmount;
    private Button mBtnReload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload_thru_card);
        init();
    }

    private void init() {
        setUpToolBar();

        mDbUtils = new UltramegaShopUtilities(getViewContext());

        mEdtRemarks = (EditText) findViewById(R.id.reload_thru_card_remarks);
        mEdtAmount = (EditText) findViewById(R.id.reload_thru_card_amount);
        mEdtAmount.addTextChangedListener(new NumberTextWatcherForThousand(mEdtAmount));
        mBtnReload = (Button) findViewById(R.id.reload_thru_card_reload);

        mBtnReload.setOnClickListener(this);

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ReloadWalletThruCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_thru_card_reload: {
                if (evaluateForm()) {
                    progressDialog();
                    createSession(sessionCallback);
                } else {
                    openErrorDialog("Please enter amount.");
                }
                break;
            }
        }
    }

    private boolean evaluateForm() {
        return mEdtAmount.getText().toString().trim().length() > 0;
    }

    private Callback<GetSessionResponse> sessionCallback = new Callback<GetSessionResponse>() {
        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    reloadConsumerWalletViaPesoPay();
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

    private void reloadConsumerWalletViaPesoPay() {
        Call<PesoPayDataResponse> call = RetrofitBuild.reloadConsumerWalletService(getViewContext())
                .reloadConsumerWalletViaPesoPay(
                        mDbUtils.getConsumerID(),
                        mDbUtils.getConsumerProfile().getUserID(),
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + mDbUtils.getConsumerProfile().getUserID() + sessionid),
                        CommonFunctions.getIMEI(getViewContext()),
                        sessionid,
                        mDbUtils.getConsumerProfile().getFirstName() + " " + mDbUtils.getConsumerProfile().getLastName(),
                        mEdtRemarks.getText().toString().trim().length() > 0 ? mEdtRemarks.getText().toString() : ".",
                        "CARD",
                        mEdtAmount.getText().toString().trim().replace(",", ""));

        call.enqueue(reloadConsumerWalletViaPesoPayCallback);
    }

    private Callback<PesoPayDataResponse> reloadConsumerWalletViaPesoPayCallback = new Callback<PesoPayDataResponse>() {
        @Override
        public void onResponse(Call<PesoPayDataResponse> call, Response<PesoPayDataResponse> response) {
            hideProgressDialog();
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    PesoPay pesoPay = response.body().getPesoPay();
                    String paymentTxnNo = response.body().getPaymenttxnno();
                    String url = response.body().getUrl();
                    finish();
                    PesoPayWebViewActivity.start(getViewContext(), pesoPay, paymentTxnNo, url, mEdtAmount.getText().toString().trim().replace(",", ""), false);
                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                } else {
                    showError(response.body().getMessage());
                }
            } else {
                showError(getString(R.string.generic_internet_error_message));
            }

        }

        @Override
        public void onFailure(Call<PesoPayDataResponse> call, Throwable t) {
            hideProgressDialog();
            showError(getString(R.string.generic_internet_error_message));
        }
    };
}
