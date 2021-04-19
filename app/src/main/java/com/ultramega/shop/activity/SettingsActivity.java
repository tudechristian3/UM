package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.SettingsRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;

public class SettingsActivity extends BaseActivity {

    private String customerid = "";

    private UltramegaShopUtilities mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpToolBar();

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            customerid = mdb.getConsumerID();
            sessionid = UserPreferenceManager.getSession(getViewContext());
        } else if (usertype.equals("WHOLESALER")) {
            customerid = mdb.getWholesalerID();
        }

        ArrayList<String> mGridData = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_settings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        SettingsRecyclerAdapter mSettingsAdapter = new SettingsRecyclerAdapter(getViewContext(), mGridData);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mSettingsAdapter);

        mGridData.add("Switch Shopping Mode");
        if (UserPreferenceManager.getBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN)) {
            if (usertype.equals("CONSUMER")) {
                if (customerid != null) {
                    //mGridData.add("Switch Shopping Mode");
                    mGridData.add("My Profile");
                    mGridData.add("My Addresses");
                    mGridData.add("Change Password");

                }
                //mGridData.add("Notifications");
                //mGridData.add("Support");
            } else {
                if (customerid != null) {
                    mGridData.add("My Profile");
                    mGridData.add("Change Password");
                }
                //mGridData.add("Notifications");
                //mGridData.add("Support");
            }
        }
        mGridData.add("Terms and Conditions");
        //mGridData.add("FAQs");
        mGridData.add("Quick Tour");
        mSettingsAdapter.setSettingsData(mGridData);

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Settings"));
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
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
