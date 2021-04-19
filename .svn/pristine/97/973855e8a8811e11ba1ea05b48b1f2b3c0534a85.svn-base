package com.ultramega.shop.activity.items;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.shop.ShopItemRecyclerViewAdapter;
import com.ultramega.shop.adapter.supplier.SupplierItemsRecyclerViewAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.Promos;
import com.ultramega.shop.pojo.Supplier;
import com.ultramega.shop.pojo.SupplierItems;
import com.ultramega.shop.responses.FetchShopCategoriesItemsResponse;
import com.ultramega.shop.responses.GetPromoItemsResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.GetSupplierItemsResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewItemsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView recyclerView;
    private NestedScrollView nested_scroll;
    private boolean isloadmore = true;
    private boolean isbottomscroll = false;

    private String itemtype = "";

    //category
    private String imei = "";
    private String categorytype = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String categoryid = "";
    private int limit = 0;
    private String pricegroup = "";

    //supplier
    private String supplierid = "";

    //promos
    private String promoid = "";

    //adapters
    private ShopItemRecyclerViewAdapter mCategoryAdapter;
    private SupplierItemsRecyclerViewAdapter mSupplierAdapter;
    private ShopItemRecyclerViewAdapter mPromosAdapter;

    //empty layout
    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private LinearLayout mSmoothProgressBar;

    private boolean isfirstload = true;
    private boolean isLoading = false;

    //array list
    private List<SupplierItems> mSupplierList;
    private List<CategoryItems> mCategoryList;

    private ShimmerFrameLayout shimmerLayout;
    private boolean isScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        setUpToolBar();

        try {
            init();

            initData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        mSmoothProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        text_empty = (TextView) findViewById(R.id.text_empty);
        image_empty = (ImageView) findViewById(R.id.image_empty);
        shimmerLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getViewContext(), 2));
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setItemAnimator(new SlideInUpAnimator());

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

//        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                if (scrollY > oldScrollY) {
////                    //Log.d("antonhttp", "Scroll DOWN");
//
////                }
////                if (scrollY < oldScrollY) {
////                    //Log.d("antonhttp", "Scroll UP");
//
////                }
////
////                if (scrollY == 0) {
////                    //Log.d("antonhttp", "TOP SCROLL");
//
////                }
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    isbottomscroll = true;
//                    if (isloadmore) {
//                        if (!isfirstload) {
//                            limit = limit + 30;
//                        }
//                        getSession();
//                    }
//                }
//            }
//        });
    }

    private void initData() {
        //initialize data
        mSupplierList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mdb = new UltramegaShopUtilities(getViewContext());
        itemtype = getIntent().getStringExtra("type");
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());

        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
        } else if (usertype.equals("WHOLESALER")) {
            pricegroup = mdb.getWholesalerProfile().getPriceGroup();
        }

        switch (itemtype) {
            case "CATEGORY": {
                Category item = getIntent().getParcelableExtra("item");
                setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", item.getDescription()));

                categoryid = item.getCategory();
                categorytype = "REGULAR";

                mCategoryAdapter = new ShopItemRecyclerViewAdapter(getViewContext(), false);
                recyclerView.setAdapter(mCategoryAdapter);
                break;
            }
            case "PROMO": {
                Promos item = getIntent().getParcelableExtra("item");
                setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", item.getName()));

                promoid = item.getPromoID();

                mPromosAdapter = new ShopItemRecyclerViewAdapter(getViewContext(), true);
                recyclerView.setAdapter(mPromosAdapter);
                break;
            }
            case "SUPPLIER": {
                Supplier item = getIntent().getParcelableExtra("item");
                setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", item.getSupplierName()));

                supplierid = item.getSupplierID();

                mSupplierAdapter = new SupplierItemsRecyclerViewAdapter(getViewContext());
                recyclerView.setAdapter(mSupplierAdapter);
                break;
            }
        }

        new LongFirstOperation().execute();

    }

    private class LongFirstOperation extends AsyncTask<List<String>, Void, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... lists) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (itemtype) {
                case "CATEGORY": {
                    mCategoryList = mdb.getCategoryItems(categoryid);
                }
                case "PROMO": {
                    mCategoryList = mdb.getPromoItems(promoid);
                    break;
                }
                case "SUPPLIER": {
                    mSupplierList = mdb.getSupplierItems(supplierid);
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {

            switch (itemtype) {
                case "CATEGORY": {
                    limit = getLimit(mCategoryList.size(), 30);
                    if (!mdb.isCategoryExist(categoryid)) {
                        getSession();
                    }
                    updateCategoryFirstLoadList(mCategoryList);
                    break;
                }
                case "PROMO": {
                    limit = getLimit(mCategoryList.size(), 30);
                    if (!mdb.isPromoExist(promoid)) {
                        getSession();
                    }
                    updatePromosFirstLoadList(mCategoryList);
                    break;
                }
                case "SUPPLIER": {
                    limit = getLimit(mSupplierList.size(), 30);
                    if (!mdb.isSupplierIdExist(supplierid)) {
                        getSession();
                    }
                    updateSupplierFirstLoadList(mSupplierList);
                    break;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            switch (itemtype) {
                case "CATEGORY": {
                    mCategoryList.clear();
                    break;
                }
                case "PROMO": {
                    mCategoryList.clear();
                    break;
                }
                case "SUPPLIER": {
                    mSupplierList.clear();
                    break;
                }
            }
        }
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
        mSwipeRefreshLayout.setEnabled(false);
    }

    private void onScrolledToTop() {
        mSwipeRefreshLayout.setEnabled(true);
    }

    private void onScrolledToBottom() {
        mSwipeRefreshLayout.setEnabled(false);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startCategoryItems(Context context, Category item) {
        Intent intent = new Intent(context, ViewItemsActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("type", "CATEGORY");
        context.startActivity(intent);
    }

    public static void startPromoItems(Context context, Promos item) {
        Intent intent = new Intent(context, ViewItemsActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("type", "PROMO");
        context.startActivity(intent);
    }

    public static void startSupplierItems(Context context, Supplier item) {
        Intent intent = new Intent(context, ViewItemsActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("type", "SUPPLIER");
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
        switch (itemtype) {
            case "CATEGORY": {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
                mCategoryAdapter.clear();
                break;
            }
            case "PROMO": {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_PROMOS);
                mPromosAdapter.clear();
                break;
            }
            case "SUPPLIER": {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER_ITEMS);
                mSupplierAdapter.clear();
                break;
            }
        }
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            //api session
            isLoading = true;
            createSession(callsession);
        } else {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            //hideProgressDialog();
            mSwipeRefreshLayout.setRefreshing(false);
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
                    switch (itemtype) {
                        case "CATEGORY": {
                            fetchCategoryItemsSales(fetchCategoryItemsSalesSession);
                            break;
                        }
                        case "PROMO": {
                            getPromoItems(getPromoItemsSession);
                            break;
                        }
                        case "SUPPLIER": {
                            getSupplierItems(getSupplierItemsSession);
                            break;
                        }
                    }
                } else {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    isLoading = false;
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                isLoading = false;
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            openErrorDialog("Something went wrong with the server.");
        }
    };

    //========================================================
    //CATEGORY
    //========================================================
    private void fetchCategoryItemsSales(Callback<FetchShopCategoriesItemsResponse> fetchCategoryItemsSalesCallback) {
        Call<FetchShopCategoriesItemsResponse> fetchitems = RetrofitBuild.fetchShopCategoriesItemsService(getViewContext())
                .fetchShopCategoriesItemsCall(imei,
                        categorytype,
                        usertype,
                        authcode,
                        sessionid,
                        categoryid,
                        String.valueOf(limit),
                        pricegroup);
        fetchitems.enqueue(fetchCategoryItemsSalesCallback);
    }

    private final Callback<FetchShopCategoriesItemsResponse> fetchCategoryItemsSalesSession = new Callback<FetchShopCategoriesItemsResponse>() {

        @Override
        public void onResponse(Call<FetchShopCategoriesItemsResponse> call, Response<FetchShopCategoriesItemsResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isLoading = false;
            isfirstload = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getCategoryItems().size() > 0;
                    List<CategoryItems> categoryItems = response.body().getCategoryItems();

                    new LongInsertCategoryItemsOperation().execute(categoryItems);

                    if (categoryItems.size() > 0) {
                        new LongCategoryItemsOperation().execute(categoryItems);
                    } else {
                        shimmerLayout.setVisibility(View.GONE);
                    }

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<FetchShopCategoriesItemsResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            isfirstload = false;
            t.printStackTrace();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void updateCategoryList(List<CategoryItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            if (mCategoryAdapter != null) {
                if (!isLoading) {
                    mCategoryAdapter.addAll(data);
                }
            }
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updateCategoryFirstLoadList(List<CategoryItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            mCategoryAdapter.clear();
            mCategoryAdapter.addAll(data);
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    //========================================================
    //SUPPLIER
    //========================================================
    private void getSupplierItems(Callback<GetSupplierItemsResponse> getSupplierItemsCallback) {
        Call<GetSupplierItemsResponse> getsupplieritems = RetrofitBuild.getSupplierItemsService(getViewContext())
                .getSupplierItemsCall(imei,
                        usertype,
                        authcode,
                        sessionid,
                        supplierid,
                        String.valueOf(limit),
                        pricegroup);
        getsupplieritems.enqueue(getSupplierItemsCallback);
    }

    private final Callback<GetSupplierItemsResponse> getSupplierItemsSession = new Callback<GetSupplierItemsResponse>() {

        @Override
        public void onResponse(Call<GetSupplierItemsResponse> call, Response<GetSupplierItemsResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isLoading = false;
            isfirstload = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getSupplieritems().size() > 0;

                    List<SupplierItems> supplieritems = response.body().getSupplieritems();

                    new LongInsertSupplierItemsOperation().execute(supplieritems);

                    if (supplieritems.size() > 0) {
                        new LongSupplierItemsOperation().execute(supplieritems);
                    } else {
                        shimmerLayout.setVisibility(View.GONE);
                    }

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetSupplierItemsResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            isfirstload = false;
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void updateSupplierList(List<SupplierItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            if (mSupplierAdapter != null) {
                if (!isLoading) {
                    mSupplierAdapter.addAll(data);
                }
            }
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updateSupplierFirstLoadList(List<SupplierItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            mSupplierAdapter.clear();
            mSupplierAdapter.addAll(data);
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    //========================================================
    //PROMOS
    //========================================================
    private void getPromoItems(Callback<GetPromoItemsResponse> getPromoItemsCallback) {
        Call<GetPromoItemsResponse> getpromoitems = RetrofitBuild.getPromoItemsService(getViewContext())
                .getPromoItemsCall(imei,
                        usertype,
                        authcode,
                        sessionid,
                        String.valueOf(limit),
                        pricegroup,
                        promoid);
        getpromoitems.enqueue(getPromoItemsCallback);
    }

    private final Callback<GetPromoItemsResponse> getPromoItemsSession = new Callback<GetPromoItemsResponse>() {

        @Override
        public void onResponse(Call<GetPromoItemsResponse> call, Response<GetPromoItemsResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isfirstload = false;
            isLoading = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getPromosList().size() > 0;
                    List<CategoryItems> promosList = response.body().getPromosList();

                    new LongInsertPromoItemsOperation().execute(promosList);

                    if (promosList.size() > 0) {
                        new LongPromoItemsOperation().execute(promosList);
                    } else {
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
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetPromoItemsResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            isfirstload = false;
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void updatePromosFirstLoadList(List<CategoryItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            mPromosAdapter.addAll(data);
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updatePromosList(List<CategoryItems> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            if (mPromosAdapter != null) {
                if (!isLoading) {
                    mPromosAdapter.addAll(data);
                }
            }
        } else {
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private class LongInsertCategoryItemsOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {

            try {
                Thread.sleep(1000);

                for (CategoryItems category : lists[0]) {
                    mdb.insertAllCategoryItemsRegular(category, usertype);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class LongCategoryItemsOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {

            try {
                Thread.sleep(1000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return lists[0];
        }

        @Override
        protected void onPostExecute(List<CategoryItems> categoryItems) {
            if (categoryItems.size() > 0) {
                updateCategoryList(categoryItems);
            }

            isScroll = false;
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            isScroll = true;
        }
    }

    private class LongInsertPromoItemsOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {

            try {
                Thread.sleep(1000);

                for (CategoryItems promo : lists[0]) {
                    mdb.insertPromoItems(promo, usertype);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class LongPromoItemsOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {

            try {
                Thread.sleep(1000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return lists[0];
        }

        @Override
        protected void onPostExecute(List<CategoryItems> categoryItems) {
            if (categoryItems.size() > 0) {
                updatePromosList(categoryItems);
            }

            isScroll = false;
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            isScroll = true;
        }
    }

    private class LongInsertSupplierItemsOperation extends AsyncTask<List<SupplierItems>, Void, List<SupplierItems>> {

        @Override
        protected List<SupplierItems> doInBackground(List<SupplierItems>... lists) {

            try {
                Thread.sleep(1000);

                for (SupplierItems items : lists[0]) {
                    mdb.insertAllSupplierItems(items, usertype);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class LongSupplierItemsOperation extends AsyncTask<List<SupplierItems>, Void, List<SupplierItems>> {

        @Override
        protected List<SupplierItems> doInBackground(List<SupplierItems>... lists) {

            try {
                Thread.sleep(1000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return lists[0];
        }

        @Override
        protected void onPostExecute(List<SupplierItems> supplierItems) {
            if (supplierItems.size() > 0) {
                updateSupplierList(supplierItems);
            }

            isScroll = false;
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            isScroll = true;
        }
    }


}
