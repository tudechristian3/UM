package com.ultramega.shop.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.claudiodegio.msv.OnSearchViewListener;
import com.claudiodegio.msv.SuggestionMaterialSearchView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.search.PopularSearchesAdapter;
import com.ultramega.shop.adapter.search.RecentSearchAdapter;
import com.ultramega.shop.adapter.search.SuggestiveSearchAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.PopularSearch;
import com.ultramega.shop.responses.GetPopularSearchesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {

    private UltramegaShopUtilities mDbUtils;
    private SuggestionMaterialSearchView searchView;

    private Context mContext;

    private RecyclerView rvRecentSearches;
    private RecyclerView rvPopularSearches;
    private RecyclerView rvSuggestiveSearches;

    private RecentSearchAdapter mRecentAdapter;
    private PopularSearchesAdapter mPopularAdapter;
    private SuggestiveSearchAdapter mSuggestiveAdapter;

    private List<PopularSearch> mPopularSearches = new ArrayList<>();

    private List<CategoryItems> mSuggestiveSearchesList = new ArrayList<>();

    private LinearLayout recentLayout;

    private LinearLayout seperatorview;

    private LinearLayout mProgressBar;

    private TextView clearRecent;

    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpToolBar();

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        searchType = getIntent().getStringExtra("searchType");

        mDbUtils = new UltramegaShopUtilities(getViewContext());
        recentLayout = (LinearLayout) findViewById(R.id.recentLayout);

        clearRecent = (TextView) findViewById(R.id.clearRecent);
        clearRecent.setOnClickListener(this);

        rvSuggestiveSearches = (RecyclerView) findViewById(R.id.rv_suggestive_searches);
        rvSuggestiveSearches.setLayoutManager(new LinearLayoutManager(getViewContext()));
        rvSuggestiveSearches.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider_search)));
        mSuggestiveAdapter = new SuggestiveSearchAdapter(getViewContext(), mSuggestiveSearchesList, searchType);
        rvSuggestiveSearches.setAdapter(mSuggestiveAdapter);

        LinearLayout suggestivelayout = (LinearLayout) findViewById(R.id.suggestivelayout);
        suggestivelayout.bringToFront();
        suggestivelayout.invalidate();

        LinearLayout popularLayout = (LinearLayout) findViewById(R.id.popularLayout);

        searchView = (SuggestionMaterialSearchView) findViewById(R.id.sv);
        searchView.setOnSearchViewListener(new OnSearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                onBackPressed();
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                mDbUtils.insertRecentSearches(s);
                SearchResultActivity.start(getViewContext(), s, searchType);
                return true;
            }

            @Override
            public void onQueryTextChange(String s) {
//                searchView.setSuggestion(mDbUtils.getSuggestiveSearchForKeyWord(s));
                mSuggestiveAdapter.updateData(mDbUtils.getSuggestiveForKeyWord(s, searchType));
            }
        });

        searchView.showSearch();


        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Search"));


        rvRecentSearches = (RecyclerView) findViewById(R.id.rv_recent_searches);
        rvRecentSearches.setLayoutManager(new LinearLayoutManager(getViewContext()));
        rvRecentSearches.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        mRecentAdapter = new RecentSearchAdapter(getViewContext(), mDbUtils.getRecentSearches(), searchType);
        rvRecentSearches.setAdapter(mRecentAdapter);


        seperatorview = (LinearLayout) findViewById(R.id.seperatorview);

        if (mDbUtils != null && mRecentAdapter != null) {
            if (mDbUtils.getRecentSearches().size() == 0) {
                Log.d("recenthttp", "Adapter exist and mDbUtils is null");
                recentLayout.setVisibility(View.GONE);
                seperatorview.setVisibility(View.GONE);
                mRecentAdapter.updateData(mDbUtils.getRecentSearches());
            }
        }

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//        rvPopularSearches.setLayoutManager(layoutManager);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        rvPopularSearches = (RecyclerView) findViewById(R.id.rv_popular_searches);
        rvPopularSearches.setLayoutManager(layoutManager);
        rvPopularSearches.addItemDecoration(new SpacesItemDecoration(10));
        mPopularAdapter = new PopularSearchesAdapter(getViewContext(), mPopularSearches, searchType);
        rvPopularSearches.setAdapter(mPopularAdapter);

        rvRecentSearches.setNestedScrollingEnabled(false);
        rvPopularSearches.setNestedScrollingEnabled(false);
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
                    getPopularSearch();
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

    private void getPopularSearch() {
        Call<GetPopularSearchesResponse> call = RetrofitBuild.getDailyFindsService(getViewContext())
                .getPopularSearches(CommonFunctions.getIMEI(getViewContext()),
                        getCurrentUserType(getViewContext()),
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + sessionid),
                        sessionid);
        call.enqueue(getPopularSearchesCallback);
    }

    private Callback<GetPopularSearchesResponse> getPopularSearchesCallback = new Callback<GetPopularSearchesResponse>() {
        @Override
        public void onResponse(Call<GetPopularSearchesResponse> call, Response<GetPopularSearchesResponse> response) {
//            hideProgressDialog();
            mProgressBar.setVisibility(View.GONE);
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    if (mPopularAdapter != null) {
                        mPopularAdapter.updateData(response.body().getPopularSearches());
                    }
                } else {
                    showError(response.body().getMessage());
                }
            } else {
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetPopularSearchesResponse> call, Throwable t) {
//            hideProgressDialog();
            mProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mDbUtils != null && mRecentAdapter != null) {
            mRecentAdapter.updateData(mDbUtils.getRecentSearches());
            Log.d("recenthttp", "mRecent adapter is true" + mRecentAdapter);
        } else {
            Log.d("recenthttp", "mRecent adapter is false" + mRecentAdapter);
        }
        if (searchView != null) {
            searchView.showSearch();
//            searchView.setQuery("",false);
//            searchView.clearFocus();
        }
//        Toast.makeText(this, "YES!", Toast.LENGTH_SHORT).show();
//        if (mPopularAdapter != null) {
//            progressDialog();
//            createSession(sessionCallback);
//        }
    }

    public static void start(Context context, String searchType) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("searchType", searchType);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
//                onBackPressed();
//                startActivity(new Intent(getViewContext(), MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(getViewContext(), MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearRecent: {
                if (mDbUtils != null) {
                    mDbUtils.truncateTable(mDbUtils.TABLE_RECENT_SEARCHES);
                    mRecentAdapter.clear();
                    recentLayout.setVisibility(View.GONE);
                    seperatorview.setVisibility(View.GONE);
                }
                break;
            }
        }
    }
}
