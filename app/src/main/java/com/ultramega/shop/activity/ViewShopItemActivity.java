package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.shoppingcart.ViewShoppingCartsActivity;
import com.ultramega.shop.adapter.VariationsRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ImageSlider;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.SupplierItems;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.AddToMyListResponse;
import com.ultramega.shop.responses.FetchShopCategoriesItemSKUResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewShopItemActivity extends BaseActivity implements View.OnClickListener {

    private UltramegaShopUtilities mdb;
    private List<ItemSKU> mGridData;
    private VariationsRecyclerAdapter mVariationsAdapter;

    private String usertype = "";
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String catclass = "";
    private String itemname = "";
    private String itemdescription = "";
    private String itemgrouppicurl = "";
    private String customerid = "";
    private String promoid = "";
    private String userid = "";
    private String wholesalerid = "";
    private String wholesalerpricegroup = "";
    private String filtergroup = "";
    private String filtervalue = "";
    private String supplierid = "";

    private boolean isViewShown = false;

//    private SliderLayout mDemoSlider;

    private LinearLayout mSmoothProgressBar;

    //CART BADGE
    private ImageView shopping_cart_icon;
    private TextView txvCartItemCount;
    private String mCartItemCount = "";

    private static ViewShopItemActivity instance;

    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;
    private boolean isLoading = false;
    private boolean isfirstload = true;
    private int limit;

    private ItemSKU itemSKU;
    private List<ItemSKU> mPackageData;
    private String itemcode = "";

    private boolean ispromo = false;

    private List<ItemSKU> itemSkuList;

    private ShimmerFrameLayout shimmerLayout;

    private boolean isScroll = true;

    private ItemSKU scannedItemSKU;
    private String barcodefrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_item);
        setUpToolBar();

        try {
            init();

            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {

        shimmerLayout = findViewById(R.id.shimmer_view_container);
        mSmoothProgressBar = findViewById(R.id.mSmoothProgressBar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_shop_items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mVariationsAdapter = new VariationsRecyclerAdapter(getViewContext(), usertype, customerid, userid);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mVariationsAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    onScrolledToTop();
                } else if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                }
                if (dy < 0) {
                    onScrolledUp();
                } else {
                    onScrolledDown();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {

    }

    private void onScrolledToTop() {

    }

    private void onScrolledToBottom() {
        isbottomscroll = true;
        if (!isScroll) {
            if (isloadmore) {
                if (!isfirstload) {
                    limit = limit + 30;
                }

                shimmerLayout.startShimmer();
                shimmerLayout.setVisibility(View.VISIBLE);
                shimmerLayout.requestFocus();

                getSession();
            }
        }
    }

    private void initData() {
        //initialize data
        ispromo = getIntent().getBooleanExtra("ispromo", false);

        if (getIntent().getBooleanExtra("issupplier", false)) {

            SupplierItems item = getIntent().getParcelableExtra("item");

            if (item.getCatClass() != null) {
                catclass = item.getCatClass().trim();
            }

            if(item.getSupplierID() != null) {
                filtergroup = "SUPPLIER";
                filtervalue = item.getSupplierID().trim();
                supplierid = item.getSupplierID().trim();
            }

            itemname = item.getDescription();
            itemgrouppicurl = item.getPhotoURL();
            itemdescription = CommonFunctions.replaceEscapeData(item.getDescription());

        } else {

            CategoryItems item = getIntent().getParcelableExtra("item");

            scannedItemSKU = getIntent().getParcelableExtra("scanneditem");
            barcodefrom = getIntent().getStringExtra("barcodefrom");

            Log.d("antonhttp", "scannedItemSKU: " + new Gson().toJson(scannedItemSKU));

            if (item.getCatClass() != null) {
                catclass = item.getCatClass().trim();
            }

            if(item.getCategory() != null) {
                filtergroup = "CATEGORY";
                filtervalue = item.getCategory().trim();
                supplierid = ".";
            }

            itemname = item.getDescription();
            itemgrouppicurl = item.getItemGroupPicURL();
            itemdescription = CommonFunctions.replaceEscapeData(item.getDescription());

            if(ispromo) {
                promoid = item.getPromoID();
            } else {
                promoid = ".";
            }
        }
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", itemname));

        instance = this;
        mdb = new UltramegaShopUtilities(getViewContext());
        mGridData = new ArrayList<>();
        itemSkuList = new ArrayList<>();
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mdb.getConsumerProfile();
            customerid = consumerProfile.getConsumerID();
            userid = consumerProfile.getUserID();
            wholesalerpricegroup = ".";
            wholesalerid = ".";
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            wholesalerpricegroup = wholesalerProfile.getPriceGroup();
            wholesalerid = wholesalerProfile.getWholesalerID();
        }

        new LongFirstOperation().execute();

    }

    private class LongFirstOperation extends AsyncTask<List<ItemSKU>, Void, List<ItemSKU>> {

        @Override
        protected List<ItemSKU> doInBackground(List<ItemSKU>... lists) {
            return mdb.getAllItemSKU(catclass, filtergroup, filtervalue, ispromo);
        }

        @Override
        protected void onPostExecute(List<ItemSKU> itemSKUS) {

            mGridData.addAll(itemSKUS);
            limit = getLimit(mGridData.size(), 30);

            if (!mdb.isItemSKUExist(catclass)) {
                getSession();
            }

            updateList(mGridData);

        }

        @Override
        protected void onPreExecute() {
            mGridData.clear();
        }
    }

    private void updateList(final List<ItemSKU> data) {

        if (data.size() > 0) {

            if (mVariationsAdapter != null) {

                if (!isLoading) {

                    mVariationsAdapter.clear();
                    mVariationsAdapter.addAll(data);

                    if (isScroll) {
//                        Thread t = new Thread() {
//                            public void run() {
                        //
                        //==================================
                        //SETS ONLY IMAGES FOR SLIDER TO 3.
                        //==================================
                        int minSize = 3;
                        if (data.size() <= 3) {
                            minSize = data.size();
                        }

                        List<ImageSlider> imageSliderList = new ArrayList<>();
                        imageSliderList.clear();
                        imageSliderList.add(new ImageSlider(itemname, itemgrouppicurl));
                        for (int i = 0; i < minSize; i++) {
                            imageSliderList.add(new ImageSlider(data.get(i).getItemCode(), data.get(i).getItemPicURL()));
                        }

                        mVariationsAdapter.setHeaderData(imageSliderList, itemname, itemgrouppicurl, catclass, usertype, customerid, userid);

                    }

                    Log.d("antonhttp", "CHECK SCAN: " + (scannedItemSKU != null));

                    if (barcodefrom != null) {
                        if (!barcodefrom.equals("CatClass")) {
                            SelectVariationActivity.start(getViewContext(), scannedItemSKU, false);
                        }
                    }

                }

            }

        }
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            //api session
            createSession(callsession);
        } else {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
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
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    fetchShopCategoriesItemSKU(fetchShopCategoriesItemSKUSession);
                } else {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    isLoading = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                isLoading = false;
                mSmoothProgressBar.setVisibility(View.GONE);
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void fetchShopCategoriesItemSKU(Callback<FetchShopCategoriesItemSKUResponse> fetchShopCategoriesItemSKUCallback) {
        Call<FetchShopCategoriesItemSKUResponse> callcategoriesitemsku = RetrofitBuild.fetchShopCategoriesItemSKUService(getViewContext())
                .fetchShopCategoriesItemSKUCall(imei,
                        authcode,
                        sessionid,
                        catclass,
                        usertype,
                        customerid == null ? "." : customerid,
                        promoid,
                        wholesalerpricegroup,
                        supplierid,
                        String.valueOf(limit));
        callcategoriesitemsku.enqueue(fetchShopCategoriesItemSKUCallback);
    }

    private final Callback<FetchShopCategoriesItemSKUResponse> fetchShopCategoriesItemSKUSession = new Callback<FetchShopCategoriesItemSKUResponse>() {
        @Override
        public void onResponse(Call<FetchShopCategoriesItemSKUResponse> call, Response<FetchShopCategoriesItemSKUResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);

            isLoading = false;
            isfirstload = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {

                if (response.body().getStatus().equals("000")) {

                    isloadmore = response.body().getCategoryItemSKU().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
                    }

                    List<ItemSKU> categoryItemSKU = response.body().getCategoryItemSKU();

                    new LongInsertOperation().execute(categoryItemSKU);

                    if (categoryItemSKU.size() > 0) {

//                        Thread t = new Thread() {
//                            public void run() {


                        new LongOperation().execute(categoryItemSKU);


//                            }
//                        };
//                        t.start();

                    } else {
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                    }

                } else {

                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    openErrorDialog(response.body().getMessage());

                }

            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                openErrorDialog("Something went wrong. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<FetchShopCategoriesItemSKUResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private class LongInsertOperation extends AsyncTask<List<ItemSKU>, Void, List<ItemSKU>> {

        @Override
        protected List<ItemSKU> doInBackground(List<ItemSKU>... lists) {

            try {
//                Thread.sleep(1000);

                if (mdb != null) {

                    for (ItemSKU itemSKU : lists[0]) {
                        mdb.insertAllItemSKUS(itemSKU, usertype);
                    }

                }

            } catch (Exception e) {
//                Thread.interrupted();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ItemSKU> itemSKUS) {
            super.onPostExecute(itemSKUS);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class LongOperation extends AsyncTask<List<ItemSKU>, Void, List<ItemSKU>> {

        @Override
        protected List<ItemSKU> doInBackground(List<ItemSKU>... lists) {

            List<ItemSKU> itemSKUS = new ArrayList<>();

            try {
//                Thread.sleep(1000);
                
                itemSKUS = mdb.getAllItemSKU(catclass, filtergroup, filtervalue, ispromo);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemSKUS;
        }

        @Override
        protected void onPostExecute(List<ItemSKU> itemSKUS) {
            updateList(itemSKUS);
            isScroll = false;
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            isScroll = true;
            itemSkuList.clear();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //=============================
        //REFRESH BADGE CART ICON COUNT
        //=============================
        setUpBadge(false);
    }

    public static void start(Context context, CategoryItems item) {
        Intent intent = new Intent(context, ViewShopItemActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    public static void start(Context context, CategoryItems item, boolean ispromo) {
        Intent intent = new Intent(context, ViewShopItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("ispromo", ispromo);
        context.startActivity(intent);
    }

    public static void start(Context context, CategoryItems item, ItemSKU itemSKU, String barcodeFrom) {
        Intent intent = new Intent(context, ViewShopItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("scanneditem", itemSKU);
        intent.putExtra("barcodefrom", barcodeFrom);
        context.startActivity(intent);
    }

    public static void startSupplier(Context context, SupplierItems item) {
        Intent intent = new Intent(context, ViewShopItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("issupplier", true);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddToList: {
                if (getCurrentUserType(getViewContext()).equals("CONSUMER")) {
                    if (mdb.getConsumerID() == null) {
                        openLoginDialog("You are required to login to proceed with adding to list");
                    } else {
                        progressDialog();
                        createSession(calladdtolistsession);
                    }
                } else if (getCurrentUserType(getViewContext()).equals("WHOLESALER")) {
                    if (mdb.getWholesalerID() == null) {
                        openLoginDialog("You are required to login to proceed with adding to list");
                    } else {
                        progressDialog();
                        createSession(calladdtolistsession);
                    }
                }
                break;
            }
        }
    }

    private final Callback<GetSessionResponse> calladdtolistsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    addToMyList(addToMyListSession);
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

    private void addToMyList(Callback<AddToMyListResponse> addToMyListCallback) {
        Call<AddToMyListResponse> addtomylist = RetrofitBuild.addToMyListService(getViewContext())
                .addToMyListCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        catclass,
                        itemgrouppicurl,
                        userid,
                        "CATCLASS");
        addtomylist.enqueue(addToMyListCallback);
    }

    private final Callback<AddToMyListResponse> addToMyListSession = new Callback<AddToMyListResponse>() {

        @Override
        public void onResponse(Call<AddToMyListResponse> call, Response<AddToMyListResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    openListDialog();

                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                } else {

                    openErrorDialog(response.body().getMessage());

                }
            } else {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<AddToMyListResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);

        //==================
        //setup badge layout
        //==================
        final MenuItem menuItem = menu.findItem(R.id.action_my_shopping_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        txvCartItemCount = actionView.findViewById(R.id.cart_badge);
        shopping_cart_icon = actionView.findViewById(R.id.shopping_cart_icon);

        //==============================
        //apply badge count on cart icon
        //==============================
        setUpBadge(false);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_my_shopping_cart: {
                ViewShoppingCartsActivity.start(getViewContext());
//                MainActivity.start(getViewContext(), 2);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //=========================================
    // APPLY BADGE COUNT ON CART ICON
    //=========================================
    public void setUpBadge(boolean isShake) {

        if (mdb != null) {
            if (txvCartItemCount != null) {
                if (mdb.getAllShoppingCartsQueue().size() == 0) {
                    if (txvCartItemCount.getVisibility() != View.GONE) {
                        txvCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    //======================================
                    //APPLY TRANSLATE ANIMATION TO CART ICON
                    //======================================
                    if (isShake) {
                        final Animation shake = AnimationUtils.loadAnimation(getViewContext(), R.anim.shake);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                shopping_cart_icon.startAnimation(shake);
                                txvCartItemCount.startAnimation(shake);
                            }
                        }, 670);

                    }

                    mCartItemCount = String.valueOf(mdb.getAllShoppingCartsQueue().size());
                    txvCartItemCount.setText(mCartItemCount);
                    if (txvCartItemCount.getVisibility() != View.VISIBLE) {
                        txvCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public static ViewShopItemActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStop() {

//        if (mDemoSlider != null) {
//
//            mDemoSlider.stopAutoCycle();
//
//        }

        super.onStop();
    }
}
