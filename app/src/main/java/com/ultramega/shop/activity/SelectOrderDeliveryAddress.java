package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.myshoppingcart.SelectOrderDeliveryAddressRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SelectOrderDeliveryAddress extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_order_delivery_address);

        setUpToolBar();
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Address"));

        Button btn_add_address = (Button) findViewById(R.id.btnAddAddress);
        btn_add_address.setOnClickListener(this);

        btn_add_address.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf","Add a new address"));

        //initialize empty data
        List<ConsumerAddress> listAddressData = new ArrayList<>();
        UltramegaShopUtilities mdb = new UltramegaShopUtilities(getViewContext());
        
        mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
        ConsumerAddress consumerAddress = new ConsumerAddress();
        consumerAddress.setConsumerID(mdb.getConsumerID());
        consumerAddress.setAddressID("CAD00001");
        consumerAddress.setStreetAddress("724 M.Street");
        consumerAddress.setCity("Compostela");
        consumerAddress.setProvince("Cebu");
        consumerAddress.setZip(6003);
        consumerAddress.setCountry("Philippines");
        consumerAddress.setLongitude("10.000981231");
        consumerAddress.setLatitude("101.12098123");
        consumerAddress.setDateTimeAdded(".");
        consumerAddress.setDateTimeUpdated(".");
        mdb.insertConsumerAddress(consumerAddress);

        listAddressData = mdb.getConsumerAddress();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));

        SelectOrderDeliveryAddressRecyclerAdapter addressAdapter = new SelectOrderDeliveryAddressRecyclerAdapter(getViewContext(), listAddressData);

        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
        recyclerView.setAdapter(addressAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.skip: {
                OrderDeliverySummary.start(getViewContext());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SelectOrderDeliveryAddress.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
