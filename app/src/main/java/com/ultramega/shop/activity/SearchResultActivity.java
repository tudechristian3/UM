package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.shop.ShopItemRecyclerViewAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.responses.GetDailyFindsResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ban_Lenovo on 9/15/2017.
 */

public class SearchResultActivity extends BaseActivity {

    public static final String KEY_SEARCH_KEY = "";

    private UltramegaShopUtilities mDbUtils;

    private ShopItemRecyclerViewAdapter mAdapter;
    private RecyclerView rvSearchGrid;

    private List<CategoryItems> mResult = new ArrayList<>();

    private int limit = 0;
    private String pricegroup = "";
    private String customerid = "";
    private String searchvalue = "";
    private String userid = "";
    private String usertype = "";
    private int check = 1;
    private Intent search;

    private LinearLayout mProgressBar;

    private String searchtype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)

            search = new Intent(this, SearchActivity.class);
        search.putExtra("searchType", searchtype);
        search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(search);

//       super.onBackPressed();

//         super.onBackPressed();
//            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setData(List<CategoryItems> data) {
        if (data.size() > 0) {
            findViewById(R.id.search_result_empty).setVisibility(View.GONE);
            rvSearchGrid.setVisibility(View.VISIBLE);
            mAdapter.setGridData(data);
        } else {
            rvSearchGrid.setVisibility(View.GONE);
            findViewById(R.id.search_result_empty).setVisibility(View.VISIBLE);
        }
    }

    private void init() {

        setUpToolJustBar();

        searchtype = getIntent().getStringExtra("searchType");

        mDbUtils = new UltramegaShopUtilities(getViewContext());

        searchvalue = getIntent().getStringExtra(KEY_SEARCH_KEY);

        rvSearchGrid = (RecyclerView) findViewById(R.id.rv_search);
        rvSearchGrid.setLayoutManager(new GridLayoutManager(getViewContext(), 2));
        rvSearchGrid.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ShopItemRecyclerViewAdapter(getViewContext(), mResult, false);
        rvSearchGrid.setAdapter(mAdapter);

        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
            userid = ".";
            customerid = mDbUtils.getConsumerID() == null ? "." : mDbUtils.getConsumerID();
        } else {
            userid = mDbUtils.getWholesalerProfile().getUserID();
            pricegroup = mDbUtils.getWholesalerProfile().getPriceGroup();
            customerid = mDbUtils.getWholesalerID() == null ? "." : mDbUtils.getWholesalerID();
        }

//        progressDialog();
        mProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        createSession(sessionCallback);
    }


    private Callback<GetSessionResponse> sessionCallback = new Callback<GetSessionResponse>() {
        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    search();
                } else {
//                    hideProgressDialog();
                    mProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
//                hideProgressDialog();
                mProgressBar.setVisibility(View.GONE);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
//            hideProgressDialog();
            mProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void search() {
        Call<GetDailyFindsResponse> call = RetrofitBuild.getDailyFindsService(getViewContext())
                .search(CommonFunctions.getIMEI(getViewContext()),
                        getCurrentUserType(getViewContext()),
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + sessionid),
                        sessionid,
                        limit,
                        pricegroup,
                        customerid,
                        userid,
                        searchvalue,
                        searchtype);
        call.enqueue(getSearchResult);
    }

    private Callback<GetDailyFindsResponse> getSearchResult = new Callback<GetDailyFindsResponse>() {
        @Override
        public void onResponse(Call<GetDailyFindsResponse> call, Response<GetDailyFindsResponse> response) {
//            hideProgressDialog();
            mProgressBar.setVisibility(View.GONE);
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    List<CategoryItems> items = response.body().getCategoryItems();
                    for (CategoryItems item : items) {
                        if (usertype.equals("CONSUMER")) {
                            item.setItemGroupPicURL(RetrofitBuild.S3_URL_RETAILER + (item.getCatClass().replaceAll("\\s+", "") + ".png").toLowerCase());
                        } else if (usertype.equals("WHOLESALER")) {
                            item.setItemGroupPicURL(RetrofitBuild.S3_URL_WHOLESALER + (item.getCatClass().replaceAll("\\s+", "") + ".png").toLowerCase());
                        }
                    }
                    setData(items);
                } else {
                    showError(response.body().getMessage());
                }
            } else {
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetDailyFindsResponse> call, Throwable t) {
//            hideProgressDialog();
            mProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    public static void start(Context context, String searchKey, String searchType) {
        Intent intent = new Intent(context, SearchResultActivity.class);

        if (searchKey.contains("'")) {
            searchKey = searchKey.replace("'", "''");
        }

        searchKey = CommonFunctions.replaceEscapeData(searchKey);

        intent.putExtra(KEY_SEARCH_KEY, searchKey);
        intent.putExtra("searchType", searchType);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        search = new Intent(this, SearchActivity.class);
        search.putExtra("searchType", searchtype);
        search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(search);
    }
}
