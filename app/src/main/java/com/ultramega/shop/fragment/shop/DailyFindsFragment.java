package com.ultramega.shop.fragment.shop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.SearchActivity;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.adapter.shop.ShopItemRecyclerViewAdapter;
import com.ultramega.shop.adapter.shop.TopPicksGridViewAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.responses.GetDailyFindsResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 10/12/2017.
 */


public class DailyFindsFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ShopItemRecyclerViewAdapter mDealsAdapter;

    private String imei = "";
    private String usertype = "";
    private String itemtype = "";
    private String authcode = "";
    private String sessionid = "";
    private String categoryid = "";
    private String categoryname = "";
    private String pricegroup = "";
    private int limit = 0;
    private String customerid = "";

    private HashMap<String, String> url_maps;
    private RecyclerView recyclerView;

    private List<CategoryItems> mGridData = new ArrayList<>();

    private TextView text_empty;
    private TextView tab_item;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private String categorytype = "";

    //    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;

    private LinearLayout mSmoothProgressBar;

    private LinearLayoutManager linearManager;
    private GridLayoutManager gridManager;

    public static final int PAGE_SIZE = 30;
    private boolean isLoading = false;
    private boolean isfirstload = true;

    private static final String KEY_SHOP_FRAGMENT = "title";

    //FAB
    private ImageView central_background;
    private List<Central> centralList;
    private FrameLayout layout_menu_central;
    private FloatingActionMenu fab;
    private boolean isCentralOpen = false;
    private RecyclerView recyclerViewCentral;
    private CentralRecyclerViewAdapter mCentralAdapter;

    private List<CategoryItems> dailyFindsList;

    private boolean isScroll = true;

    private ProgressBar progressBar;

    //    private GridView gridView;
    private TopPicksGridViewAdapter gridViewAdapter;

    private ShimmerFrameLayout shimmerLayout;

    public static DailyFindsFragment newInstance(String value) {
        DailyFindsFragment fragment = new DailyFindsFragment();
        Bundle b = new Bundle();
        b.putString(KEY_SHOP_FRAGMENT, value);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!UserPreferenceManager.getBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN)) {
            menu.findItem(R.id.action_notification).setVisible(false);
        }

        menu.findItem(R.id.action_scan_code).setVisible(true);
        menu.findItem(R.id.action_my_shopping_cart).setVisible(true);
        showSearchBar();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        LinearLayout searchlayout = toolbar.findViewById(R.id.searchbox);
        searchlayout.setVisibility(View.VISIBLE);
        searchlayout.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_finds, container, false);

        try {

            init(view);

            initData();

            //central layout
            setUpCentralLayout(view);

            if (getArguments().getBoolean("isShow", true)) {
                showCentral();
                fab.setVisibility(View.GONE);
            }else{
                fab.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void init(View view) {
//        nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);
        mSmoothProgressBar = view.findViewById(R.id.mSmoothProgressBar);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        layout_empty = view.findViewById(R.id.layout_empty);
        text_empty = view.findViewById(R.id.text_empty);
        image_empty = view.findViewById(R.id.image_empty);
        tab_item = view.findViewById(R.id.tab_item);
        tab_item.setVisibility(View.VISIBLE);
        progressBar = view.findViewById(R.id.progress);
        recyclerView = view.findViewById(R.id.recycler_view_deals);
        shimmerLayout = view.findViewById(R.id.shimmer_view_container);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        //Initialize with empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        mGridData = new ArrayList<>();
        dailyFindsList = new ArrayList<>();

        isbottomscroll = false;
        isloadmore = true;

        usertype = getCurrentUserType(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        categoryid = "DAILYFINDS";
        categoryname = "DAILYFINDS";

        if (usertype.equals("CONSUMER")) {
            itemtype = "RETAIL";
            pricegroup = ".";
            customerid = mdb.getConsumerID() == null ? "." : mdb.getConsumerID();
        } else if (usertype.equals("WHOLESALER")) {
            itemtype = "WHOLESALE";
            pricegroup = mdb.getWholesalerProfile().getPriceGroup();
            customerid = mdb.getWholesalerID() == null ? "." : mdb.getWholesalerID();
        }

        //recyclerView.setVisibility(View.VISIBLE);
        categorytype = "DAILYFINDS";
        setTextBackgroundView(UserPreferenceManager.getStringPreference(getViewContext(), BaseFragment.Theme_Current), tab_item, "PICK & SHOP", "fonts/ElliotSans-Medium.ttf");

        gridManager = new GridLayoutManager(getViewContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        mDealsAdapter = new ShopItemRecyclerViewAdapter(getViewContext(), false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(mDealsAdapter);

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

        new LongFirstOperation().execute();

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

    private void updateFirstLoadDailyFindsList(final List<CategoryItems> data) {

        if (data.size() > 0) {
            tab_item.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);

            if (mDealsAdapter != null) {
                mDealsAdapter.clear();
                mDealsAdapter.addAll(data);
            }

            isLoading = false;
            isScroll = false;
        } else {
            tab_item.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
        }

    }

    private void updateList(final List<CategoryItems> data) {

        if (isAdded()) {

            if (data.size() > 0) {

                layout_empty.setVisibility(View.GONE);

                if (mdb != null) {
                    //recyclerView.setVisibility(View.VISIBLE);
                    tab_item.setVisibility(View.VISIBLE);

                    if (mDealsAdapter != null) {
                        if (!isLoading) {
                            mDealsAdapter.addAll(data);
                        }
                    }

//                    if (gridViewAdapter != null) {
//                        if (!isLoading) {
//                            gridViewAdapter.addAll(data);
//                        }
//                    }

                }

            } else {

                //recyclerView.setVisibility(View.GONE);
                tab_item.setVisibility(View.GONE);
                layout_empty.setVisibility(View.VISIBLE);
                text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));

            }

        }

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
            progressBar.setVisibility(View.GONE);
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
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
                    getDailyFinds(getDailyFindsSession);
                } else {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                if (isAdded()) {
                    showError(getString(R.string.generic_internet_error_message));
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (isAdded())
                showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getDailyFinds(Callback<GetDailyFindsResponse> getDailyFindsCallback) {
        Call<GetDailyFindsResponse> getdailyfinds = RetrofitBuild.getDailyFindsService(getViewContext())
                .getDailyFindsCall(imei,
                        usertype,
                        authcode,
                        sessionid,
                        String.valueOf(limit),
                        pricegroup,
                        customerid);
        getdailyfinds.enqueue(getDailyFindsCallback);
    }

    private final Callback<GetDailyFindsResponse> getDailyFindsSession = new Callback<GetDailyFindsResponse>() {

        @Override
        public void onResponse(Call<GetDailyFindsResponse> call, Response<GetDailyFindsResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            ResponseBody errorBody = response.errorBody();

            isLoading = false;
            isfirstload = false;

            if (errorBody == null) {

                if (response.body().getStatus().equals("000")) {

                    isloadmore = response.body().getCategoryItems().size() > 0;

                    List<CategoryItems> categoryItems = response.body().getCategoryItems();

                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                    }

                    new LongInsertOperation().execute(categoryItems);

                    if (categoryItems.size() > 0) {
                        new LongOperation().execute(categoryItems);
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
            }
        }

        @Override
        public void onFailure(Call<GetDailyFindsResponse> call, Throwable t) {
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
        }
    };

    private class LongInsertOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {
            try {
                Thread.sleep(1000);

                if (mdb != null) {

                    for (CategoryItems category : lists[0]) {
                        mdb.insertDailyFinds(category, usertype);
                    }

                }

            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CategoryItems> categoryItems) {
            super.onPostExecute(categoryItems);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class LongOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {
//            for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.interrupted();
            }
//            }
            return lists[0];
        }

        @Override
        protected void onPostExecute(List<CategoryItems> categoryItems) {
            if (categoryItems.size() > 0) {
                dailyFindsList.addAll(categoryItems);
                updateList(categoryItems);
            }

            isScroll = false;
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
            isScroll = true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class LongFirstOperation extends AsyncTask<List<CategoryItems>, Void, List<CategoryItems>> {

        @Override
        protected List<CategoryItems> doInBackground(List<CategoryItems>... lists) {
//            for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.interrupted();
            }
//           }
            return mdb.getDailyFinds();
        }

        @Override
        protected void onPostExecute(List<CategoryItems> categoryItems) {


            mGridData = categoryItems;
            dailyFindsList = categoryItems;

            limit = getLimit(mGridData.size(), 30);

            if (mGridData.size() == 0) {
                getSession();
            }
            updateFirstLoadDailyFindsList(dailyFindsList);
        }

        @Override
        protected void onPreExecute() {
            mGridData.clear();
            dailyFindsList.clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbox: {
                SearchActivity.start(getViewContext(), "DAILY FINDS");
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        if (mdb != null) {
            if (categorytype.equals("DAILYFINDS")) {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);

                mDealsAdapter.clear();

                mGridData.clear();

                dailyFindsList.clear();
            }
        }

        //refresh badge count icon
        MainActivity shopItem = MainActivity.getInstance();
        shopItem.setUpBadge();

        isScroll = true;
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSmoothProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tab_item != null) {
            tab_item.setVisibility(View.VISIBLE);
        }
    }

    public void setUpCentralLayout(View view) {
        centralList = new ArrayList<>();
        centralList = getCentralList(usertype);
        layout_menu_central = view.findViewById(R.id.layout_menu_central);
        fab = view.findViewById(R.id.fabMenu);
        recyclerViewCentral = view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), DailyFindsFragment.this);
        recyclerViewCentral.addItemDecoration(new SpacesItemDecoration(10));
        recyclerViewCentral.setAdapter(mCentralAdapter);
        central_background = view.findViewById(R.id.central_background);
        Glide.with(getViewContext())
                .asBitmap()
                .load(R.drawable.central_background)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        central_background.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }
                });

        fab.getMenuIconView().setImageResource(R.drawable.ic_ultramega_central_icon);
        fab.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCentralOpen) {
                    showCentral();
                } else {
                    hideCentral();
                }
            }
        });

    }

    public void hideCentral() {
        if (isAdded()) {
            if (mCentralAdapter != null) {
                mCentralAdapter.clear();

                layout_menu_central.setVisibility(View.GONE);
                isCentralOpen = false;
                fab.getMenuIconView().setImageResource(R.drawable.ic_ultramega_central_icon);

                //show toolbar
                ((MainActivity) getViewContext()).hideToolBar(false);

                mSwipeRefreshLayout.setEnabled(true);

            }
        }
    }

    private void showCentral() {
        if (isAdded()) {
            if (mCentralAdapter != null) {
                layout_menu_central.setVisibility(View.VISIBLE);

                isCentralOpen = true;
                fab.getMenuIconView().setImageResource(R.drawable.ic_fab_x);
                mCentralAdapter.setGridData(centralList);

                //hide toolbar
                ((MainActivity) getViewContext()).hideToolBar(true);

                mSwipeRefreshLayout.setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    }
}
