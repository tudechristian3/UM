package com.ultramega.shop.fragment.shop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.ultramega.shop.adapter.shop.CategoriesRecyclerAdapter;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.FetchShopCategoriesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private UltramegaShopUtilities mdb;
    private CategoriesRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String pricegroup = "";
    private String customerid = "";
    private int limit;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    //    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;
    private boolean isfirstload = true;
    private boolean isLoading = false;

    private LinearLayout mSmoothProgressBar;

    private TextView tab_item;

    private static final String KEY_SHOP_FRAGMENT = "title";

    private List<Category> listCategory;

    //FAB
    private ImageView central_background;
    private List<Central> centralList;
    private FrameLayout layout_menu_central;
    private FloatingActionMenu fab;
    private boolean isCentralOpen = false;
    private RecyclerView recyclerViewCentral;
    private CentralRecyclerViewAdapter mCentralAdapter;

    private boolean isScroll = true;

    private ShimmerFrameLayout shimmerLayout;

    public static CategoriesListFragment newInstance(String value) {
        CategoriesListFragment fragment = new CategoriesListFragment();
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

        menu.findItem(R.id.action_notification).setVisible(true);
        menu.findItem(R.id.action_scan_code).setVisible(true);
        menu.findItem(R.id.action_my_shopping_cart).setVisible(true);
        if (!UserPreferenceManager.getBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN)) {
            menu.findItem(R.id.action_notification).setVisible(false);
        }

        showSearchBar();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        LinearLayout searchlayout = (LinearLayout) toolbar.findViewById(R.id.searchbox);
        searchlayout.setVisibility(View.VISIBLE);
        searchlayout.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        init(view);

        initData();

        //central layout
        setUpCentralLayout(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void init(View view) {
        shimmerLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
        layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
        text_empty = (TextView) view.findViewById(R.id.text_empty);
        image_empty = (ImageView) view.findViewById(R.id.image_empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        tab_item = (TextView) view.findViewById(R.id.tab_item);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    private void initData() {
        //Initialize with empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        listCategory = new ArrayList<>();
        isbottomscroll = false;
        isloadmore = true;

        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mdb.getConsumerProfile();
            pricegroup = ".";
            customerid = consumerProfile.getConsumerID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            pricegroup = wholesalerProfile.getPriceGroup();
            customerid = wholesalerProfile.getWholesalerID();
        }

        setTextBackgroundView(UserPreferenceManager.getStringPreference(getViewContext(), BaseFragment.Theme_Current), tab_item, "CATEGORIES", "fonts/ElliotSans-Medium.ttf");
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new CategoriesRecyclerAdapter(getViewContext());
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mAdapter);

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

    private class LongFirstOperation extends AsyncTask<List<Category>, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground(List<Category>... lists) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mdb.getCategories();
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            listCategory = categories;
            limit = getLimit(listCategory.size(), 30);
            updateFirstLoadList(listCategory);
            if (categories.size() == 0) {
                getSession();
            }
        }

        @Override
        protected void onPreExecute() {
            listCategory.clear();
        }
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
        mSwipeRefreshLayout.setEnabled(false);
    }

    private void onScrolledToTop() {
        Log.d("antonhttp", "=============== TOP SCROLL OI ATAY ==============");
        mSwipeRefreshLayout.setEnabled(true);
    }

    private void onScrolledToBottom() {
        Log.d("antonhttp", "=============== BOTTOM SCROLL OI ATAY ==============");
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

    private void updateFirstLoadList(List<Category> data) {
        if (data.size() > 0) {
//            tab_item.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            mAdapter.clear();
            mAdapter.addAll(data);
            isScroll = false;
        } else {
//            tab_item.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updateList(List<Category> data) {
        if (data.size() > 0) {
//            tab_item.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
//            mAdapter.setCategoriesData(data);
            mAdapter.addAll(data);
        } else {
//            tab_item.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
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
            hideProgressDialog();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    //api request
                    getCategoryList(getCategoryListSession);
                } else {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    isLoading = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                isLoading = false;
                mSmoothProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            isLoading = false;
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
        }
    };

    private void getCategoryList(Callback<FetchShopCategoriesResponse> getCategoryListCallback) {
        Call<FetchShopCategoriesResponse> callcategories = RetrofitBuild.fetchShopCategoriesService(getViewContext())
                .fetchShopCategoriesCall(imei,
                        usertype,
                        authcode,
                        String.valueOf(limit),
                        sessionid,
                        pricegroup);
        callcategories.enqueue(getCategoryListCallback);
    }

    private final Callback<FetchShopCategoriesResponse> getCategoryListSession = new Callback<FetchShopCategoriesResponse>() {

        @Override
        public void onResponse(Call<FetchShopCategoriesResponse> call, Response<FetchShopCategoriesResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isfirstload = false;
            isLoading = false;

            //hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    isloadmore = response.body().getCategories().size() > 0;

                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
                    }

                    List<Category> categories = response.body().getCategories();

                    new LongInsertOperation().execute(categories);

                    if (categories.size() > 0) {
                        new LongOperation().execute(categories);
                    } else {
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                    }
                } else {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                }
            } else {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<FetchShopCategoriesResponse> call, Throwable t) {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
        }
    };

    private class LongInsertOperation extends AsyncTask<List<Category>, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground(List<Category>... lists) {

            try {
                Thread.sleep(1000);

                if (mdb != null) {

                    for (Category category : lists[0]) {
                        mdb.insertAllCategories(category);
                    }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }


    }

    private class LongOperation extends AsyncTask<List<Category>, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground(List<Category>... lists) {

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.interrupted();
            }

            return lists[0];
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            if (categories.size() > 0) {
                updateList(categories);
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

    @Override
    public void onRefresh() {
        //refresh badge count icon
        MainActivity shopItem = MainActivity.getInstance();
        shopItem.setUpBadge();

        isScroll = true;
        recyclerView.setVisibility(View.GONE);
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSmoothProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);

        mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
        mAdapter.clear();

        getSession();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbox: {
                SearchActivity.start(getViewContext(), "CATEGORIES");
                break;
            }
        }
    }

    public void setUpCentralLayout(View view) {
        centralList = new ArrayList<>();
        centralList = getCentralList(usertype);
        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), CategoriesListFragment.this);
        recyclerViewCentral.addItemDecoration(new SpacesItemDecoration(10));
        recyclerViewCentral.setAdapter(mCentralAdapter);
        central_background = (ImageView) view.findViewById(R.id.central_background);
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
