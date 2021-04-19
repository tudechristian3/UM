package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.WalletReloadFragment;
import com.ultramega.shop.pojo.WalletReload;

/**
 * Created by User on 08/09/2017.
 */

public class ViewWalletReloadDetailsActivity extends BaseActivity {
    public static final String KEY_WALLET_RELOAD = "Wallet_Reload";

    private UltramegaShopUtilities mDb;

    private ImageView img_status;
    private TextView tnx_no;
    private TextView date;
    private TextView withdarawal_amount;
    private TextView bank;
    private TextView bank_account_no;
    private TextView bank_account_name;
    private TextView bank_transaction_number;
    private ImageView img_deposit_slip;

    private WalletReload mWalletReload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_reload_view_payments_details);
        init();
    }

    private void init() {

        setUpToolBar();

        mDb = new UltramegaShopUtilities(getViewContext());

        mWalletReload = getIntent().getParcelableExtra(KEY_WALLET_RELOAD);

      //  img_status = (ImageView) findViewById(R.id.img_payment_status);
        tnx_no = (TextView) findViewById(R.id.txv_payment_txn_number);
     //   date = (TextView) findViewById(R.id.txv_payment_reload_date);
     //   withdarawal_amount = (TextView) findViewById(R.id.txv_payment_reload_withdrawal_amount);
     ///   bank = (TextView) findViewById(R.id.txv_payment_reload_bank);
     //   bank_account_no = (TextView) findViewById(R.id.txv_payment_reload_bank_account_number);
     //   bank_account_name = (TextView) findViewById(R.id.txv_payment_reload_bank_account_name);
     //   img_deposit_slip = (ImageView) findViewById(R.id.payment_deposit_slip);
    //    bank_transaction_number = (TextView) findViewById(R.id.txv_payment_reload_bank_transaction_number);

        setWalletDetails(mWalletReload);

    }

    public static void start(Context context, WalletReload walletReload) {
        Intent intent = new Intent(context, ViewWalletReloadDetailsActivity.class);
        intent.putExtra(KEY_WALLET_RELOAD, walletReload);
        context.startActivity(intent);
    }

    private void setWalletDetails(WalletReload walletReload) {
        tnx_no.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf",  "TXN NO " + walletReload.getPaymentTxnNo()));
        bank.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankcode")));
        date.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf",CommonFunctions.convertDateWalletReloadPayment(walletReload.getDateTimeCompleted())));
        withdarawal_amount.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf",CommonFunctions.currencyFormatter(walletReload.getAmountPaid())));
        bank_account_no.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf",CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankaccountnumber")));
        bank_account_name.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf",CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankaccountname")));
        bank_transaction_number.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf",CommonFunctions.parseXML(walletReload.getPaymentDetails(), "banktxnnumber")));

        int imageRes = 0;
        switch (walletReload.getStatus()) {
            case WalletReloadFragment.STATUS_APPROVED: {
                imageRes = R.drawable.ic_wallet_reload_approved;
                break;
            }
            case WalletReloadFragment.STATUS_FOR_APPROVAL: {
                imageRes = R.drawable.ic_wallet_reload_on_process;
                break;
            }
            case WalletReloadFragment.STATUS_DECLINED: {
                imageRes = R.drawable.ic_wallet_reload_cancelled;
                break;
            }
        }
        Glide.with(getViewContext())
                .load(imageRes)
                .into(img_status);

        Glide.with(getViewContext())
                .load(CommonFunctions.parseXML(walletReload.getPaymentDetails(), "depositslipurl"))
                .apply(RequestOptions.centerInsideTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.profile_image_empty))
                .into(img_deposit_slip);
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
}
