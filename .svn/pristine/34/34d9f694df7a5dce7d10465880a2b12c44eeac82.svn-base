package com.ultramega.shop.fragment.shop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.adapter.shop.PromoRecyclerViewAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.pojo.Promos;
import com.ultramega.shop.responses.GetPromosResponse;
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

public class PromosFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PromoRecyclerViewAdapter mPromoAdapter;

    private String imei = "";
    private String usertype = "";
    private String itemtype = "";
    private String authcode = "";
    private String sessionid = "";
    private String categoryid = "";
    private String categoryname = "";
    private String pricegroup = "";
    private int limit;
    private String customerid = "";

    private HashMap<String, String> url_maps;

    private RecyclerView recyclerView;

    private List<Promos> mPromoData;

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

    private GridLayoutManager gridLayoutManager;

    //FAB
    private ImageView central_background;
    private List<Central> centralList;
    private FrameLayout layout_menu_central;
    private FloatingActionMenu fab;
    private boolean isCentralOpen = false;
    private RecyclerView recyclerViewCentral;
    private CentralRecyclerViewAdapter mCentralAdapter;

    private CoordinatorLayout coordinatorLayout;

    private ShimmerFrameLayout shimmerLayout;

    private boolean isScroll = true;

    public static PromosFragment newInstance(String value) {
        PromosFragment fragment = new PromosFragment();
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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        LinearLayout searchlayout = (LinearLayout) toolbar.findViewById(R.id.searchbox);
        searchlayout.setVisibility(View.VISIBLE);
        searchlayout.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promos, container, false);

        init(view);

        initData();

        //central layout
        setUpCentralLayout(view);

        return view;
    }

    private void init(View view) {
//        nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);
        mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
        text_empty = (TextView) view.findViewById(R.id.text_empty);
        image_empty = (ImageView) view.findViewById(R.id.image_empty);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_promos);
        tab_item = (TextView) view.findViewById(R.id.tab_item);
        recyclerView.setVisibility(View.VISIBLE);
        categorytype = "PROMOS";
        tab_item.setVisibility(View.VISIBLE);
        setTextBackgroundView(UserPreferenceManager.getStringPreference(getViewContext(), BaseFragment.Theme_Current), tab_item, "PROMOS", "fonts/ElliotSans-Medium.ttf");
        gridLayoutManager = new GridLayoutManager(getViewContext(), 2, GridLayoutManager.VERTICAL, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        shimmerLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
    }

    private void initData() {
        //Initialize with empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        isbottomscroll = false;
        isloadmore = true;
        mPromoData = new ArrayList<>();
        usertype = getCurrentUserType(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        categoryid = "PROMOS";
        categoryname = "PROMOS";

        if (usertype.equals("CONSUMER")) {
            itemtype = "RETAIL";
            pricegroup = ".";
            customerid = mdb.getConsumerID() == null ? "." : mdb.getConsumerID();
            coordinatorLayout.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_E4E4E4));
        } else if (usertype.equals("WHOLESALER")) {
            itemtype = "WHOLESALE";
            pricegroup = mdb.getWholesalerProfile().getPriceGroup();
            customerid = mdb.getWholesalerID() == null ? "." : mdb.getWholesalerID();
            coordinatorLayout.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_E4E4E4));
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        mPromoAdapter = new PromoRecyclerViewAdapter(getViewContext());
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(mPromoAdapter);

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

    private class LongFirstOperation extends AsyncTask<List<Promos>, Void, List<Promos>> {

        @Override
        protected List<Promos> doInBackground(List<Promos>... lists) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mdb.getPromos();
        }

        @Override
        protected void onPostExecute(List<Promos> promos) {
            mPromoData = promos;
            limit = getLimit(promos.size(), 30);
            updateFirstLoadPromoList(promos);

            if (promos.size() == 0) {
                getSession();
            }
        }
    }

    private void updateFirstLoadPromoList(List<Promos> data) {
        if (data.size() > 0) {
            if (mPromoAdapter != null) {
                if (!isLoading) {
                    mPromoAdapter.clear();
                    mPromoAdapter.addAll(data);
                    setSpanSize(data);
                }
            }
            recyclerView.setVisibility(View.VISIBLE);
            //tab_item.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            isScroll = false;
        } else {
            recyclerView.setVisibility(View.GONE);
            //tab_item.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void updatePromoList(List<Promos> data) {
        if (data.size() > 0) {
            if (mPromoAdapter != null) {
                if (!isLoading) {
                    mPromoAdapter.addAll(data);
                    setSpanSize(data);
                }
            }
            recyclerView.setVisibility(View.VISIBLE);
            //tab_item.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            //tab_item.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Nothing in here"));
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    private void setSpanSize(List<Promos> data) {
        final List<Promos> promData = data;

        if (promData.size() > 0) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if (mPromoData.size() > 0) {

                        try {

                            if (mPromoData.size() <= position) {

                                switch (mPromoData.get(position).getRank()) {
                                    case 1: {
                                        return 2;
                                    }
                                    case 2: {
                                        return 1;
                                    }
                                    case 3: {
                                        return 1;
                                    }
                                    default: {
                                        return 1;
                                    }
                                }

                            } else {
                                return 1;
                            }

                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            return 1;
                        }

                    } else {
                        return 1;
                    }

                }
            });

            recyclerView.setLayoutManager(gridLayoutManager);
        }

    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            //api session
            isLoading = true;
            createSession(callsession);
        } else {
            mSmoothProgressBar.setVisibility(View.GONE);
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
                    getPromos(getPromosSession);
                } else {
                    isLoading = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getPromos(Callback<GetPromosResponse> getPromosCallback) {
        Call<GetPromosResponse> getpromos = RetrofitBuild.getPromosService(getViewContext())
                .getPromosCall(imei,
                        usertype,
                        authcode,
                        sessionid,
                        String.valueOf(limit),
                        pricegroup,
                        customerid);
        getpromos.enqueue(getPromosCallback);
    }

    private final Callback<GetPromosResponse> getPromosSession = new Callback<GetPromosResponse>() {

        @Override
        public void onResponse(Call<GetPromosResponse> call, Response<GetPromosResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);

            isLoading = false;
            isfirstload = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getPromosList().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
                    }

                    List<Promos> promosList = response.body().getPromosList();
                    mPromoData.addAll(promosList);

                    new LongInsertOperation().execute(promosList);

                    if (promosList.size() > 0) {
                        new LongOperation().execute(promosList);
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
        public void onFailure(Call<GetPromosResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private class LongOperation extends AsyncTask<List<Promos>, Void, List<Promos>> {

        @Override
        protected List<Promos> doInBackground(List<Promos>... lists) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return lists[0];
        }

        @Override
        protected void onPostExecute(List<Promos> promos) {
            if (promos.size() > 0) {
                updatePromoList(promos);
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

    private class LongInsertOperation extends AsyncTask<List<Promos>, Void, List<Promos>> {

        @Override
        protected List<Promos> doInBackground(List<Promos>... lists) {

            try {
                Thread.sleep(1000);

                for (Promos promo : lists[0]) {
                    mdb.insertPromos(promo, usertype);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbox: {
                SearchActivity.start(getViewContext(), "PROMOS");
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        //refresh badge count icon
        MainActivity shopItem = MainActivity.getInstance();
        shopItem.setUpBadge();

        if (mdb != null) {
            mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
            recyclerView.setVisibility(View.GONE);
            mPromoAdapter.clear();
            mPromoData.clear();
        }

        isScroll = true;
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSmoothProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }

    public void setUpCentralLayout(View view) {
        centralList = new ArrayList<>();
        centralList = getCentralList(usertype);
        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), PromosFragment.this);
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
