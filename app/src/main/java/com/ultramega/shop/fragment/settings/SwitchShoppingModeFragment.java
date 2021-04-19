package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.LoginActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.util.UserPreferenceManager;

public class SwitchShoppingModeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static final String Theme_Current = "AppliedTheme";
    private UltramegaShopUtilities mdb;
    private RadioGroup radioGroup;

    private MaterialDialog mLogoutConsumerDialog;
    private MaterialDialog mLogoutWholesalerDialog;

    private String usertype = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch_shopping_mode, container, false);
        setHasOptionsMenu(true);

        try {

            usertype = getCurrentUserType(getViewContext());

            radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupShoppingMode);
            TextView radio_button_consumer = (TextView) view.findViewById(R.id.radioButtonConsumer);
            TextView radio_button_wholesaler = (TextView) view.findViewById(R.id.radioButtonWholesaler);
            radio_button_consumer.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Consumer"));
            radio_button_wholesaler.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Wholesaler"));

            switch (UserPreferenceManager.getStringPreference(getViewContext(), Theme_Current)) {
                case "Consumer_Theme": {
                    radioGroup.check(R.id.radioButtonConsumer);
                    break;
                }
                case "Wholesaler_Theme": {
                    radioGroup.check(R.id.radioButtonWholesaler);
                    break;
                }
                default: {
                    radioGroup.check(R.id.radioButtonConsumer);
                    break;
                }
            }
            radioGroup.setOnCheckedChangeListener(this);

            view.findViewById(R.id.radioButtonWholesaler).setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radioButtonConsumer: {

                if (UserPreferenceManager.getStringPreference(getViewContext(), Theme_Current).equals("Wholesaler_Theme")) {

                    openLogoutWholesalerDialog("You're about to switch into Consumer");

                }

                break;
            }
            case R.id.radioButtonWholesaler: {


                if (UserPreferenceManager.getStringPreference(getViewContext(), Theme_Current).equals("Consumer_Theme")) {

                    openLogoutConsumerDialog("You're about to switch into Wholesaler");

                }

                break;
            }
        }
    }

    private void openLogoutConsumerDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());

        mLogoutConsumerDialog = new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Proceed")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);

                        UserPreferenceManager.clearPreference(getViewContext());
                        UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");

                        if (mdb != null) {
                            if (mdb.getWholesalerID() != null && !mdb.getWholesalerID().equals(".")) {
                                //set up login to true
                                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
                            } else {
                                //set up login to true
                                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
                            }
                        }

                        MainActivity.switchMode(getViewContext(), 0);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        radioGroup.check(R.id.radioButtonConsumer);
                        mLogoutConsumerDialog.dismiss();
                    }
                })
                .show();
    }

    private void openLogoutWholesalerDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());

        mLogoutWholesalerDialog = new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Proceed")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        //truncate tables
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);

                        UserPreferenceManager.clearPreference(getViewContext());
                        UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");

                        if (mdb != null) {
                            if (mdb.getConsumerID() != null) {
                                //set up login to true
                                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
                            } else {
                                //set up login to true
                                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
                            }
                        }

                        MainActivity.start(getViewContext(), 1);

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        radioGroup.check(R.id.radioButtonWholesaler);
                        mLogoutWholesalerDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLogoutConsumerDialog != null) {
            mLogoutConsumerDialog.dismiss();
        }

        if (mLogoutWholesalerDialog != null) {
            mLogoutWholesalerDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.radioButtonWholesaler: {

                if (usertype.equals("WHOLESALER")) {

                    LoginActivity.start(getViewContext());

                }

                break;
            }

        }

    }
}
