package com.ultramega.shop.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionMenu;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.adapter.LoyaltyRewardsRecyclerAdapter;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.PromoPts;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetPromoPointsResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoyaltyRewardsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private UltramegaShopUtilities mdb;
    private RecyclerView recyclerView;
    private LoyaltyRewardsRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String authcode = "";
    private String sessionid = "";
    private String imei = "";
    private String customerid = "";
    private String usertype = "";
    private String userid = "";
    private int limit;

//    private TextView loyalty_amount;
//    private TextView loyalty_amount_label;

    private LinearLayout mSmoothProgressBar;

    //empty layout
    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private RelativeLayout main_layout;

    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;

    private boolean isLoading = false;
    private boolean isfirstload = true;

    private List<PromoPts> mGridData;

    //FAB
    private ImageView central_background;
    private List<Central> centralList;
    private FrameLayout layout_menu_central;
    private FloatingActionMenu fab;
    private boolean isCentralOpen = false;
    private RecyclerView recyclerViewCentral;
    private CentralRecyclerViewAdapter mCentralAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        hideSearchBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loyalty_rewards, container, false);
        setHasOptionsMenu(true);

        try {

            mdb = new UltramegaShopUtilities(getViewContext());
            imei = CommonFunctions.getIMEI(getViewContext());
            usertype = getCurrentUserType(getViewContext());
            if (usertype.equals("CONSUMER")) {
                ConsumerProfile consumerProfile = mdb.getConsumerProfile();
                userid = consumerProfile.getUserID();
                customerid = consumerProfile.getConsumerID();
            } else if (usertype.equals("WHOLESALER")) {
                WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
                userid = wholesalerProfile.getUserID();
                customerid = wholesalerProfile.getWholesalerID();
            }
            mGridData = new ArrayList<>();
            mGridData = mdb.getPromoPts(customerid);
            limit = getLimit(mGridData.size(), 30);
            isbottomscroll = false;
            isloadmore = true;

            main_layout = (RelativeLayout) view.findViewById(R.id.main_layout);
            nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);
            layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
            layout_empty.setVisibility(View.GONE);
            text_empty = (TextView) view.findViewById(R.id.text_empty);
            image_empty = (ImageView) view.findViewById(R.id.image_empty);

            mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_loyalty_rewards);
            recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
            mAdapter = new LoyaltyRewardsRecyclerAdapter(getViewContext());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setItemAnimator(new SlideInUpAnimator());
            recyclerView.setAdapter(mAdapter);

            getSession();

            //SETUP CENTRAL LAYOUT
            setUpCentralLayout(view);

            nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        isbottomscroll = true;
                        if (isloadmore) {
                            if (!isfirstload) {
                                limit = limit + 30;
                            }
                            getSession();
                        }
                    }
                }
            });

            updatePromoList(mGridData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setUpCentralLayout(View view){
        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), LoyaltyRewardsFragment.this);
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

        centralList = new ArrayList<>();
        centralList = getCentralList(usertype);

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

                Animation move = AnimationUtils.loadAnimation(getViewContext(), R.anim.gk_services_anim_out);
                move.setInterpolator((new AccelerateDecelerateInterpolator()));
                move.setFillAfter(false);
                //layout_menu_central.startAnimation(move);
                layout_menu_central.setVisibility(View.GONE);
                isCentralOpen = false;
                fab.getMenuIconView().setImageResource(R.drawable.ic_ultramega_central_icon);

                //show toolbar
                ((MainActivity) getViewContext()).hideToolBar(false);

                //hide layout
                main_layout.setVisibility(View.VISIBLE);

                mSwipeRefreshLayout.setEnabled(true);

            }
        }
    }

    private void showCentral() {
        if (isAdded()) {
            if (mCentralAdapter != null) {
                layout_menu_central.setVisibility(View.VISIBLE);
                Animation move = AnimationUtils.loadAnimation(getViewContext(), R.anim.gk_services_anim);
                move.setInterpolator((new AccelerateDecelerateInterpolator()));
                move.setFillAfter(true);
                //layout_menu_central.startAnimation(move);
                isCentralOpen = true;
                fab.getMenuIconView().setImageResource(R.drawable.ic_fab_x);

                //mCentralAdapter.clear();
                mCentralAdapter.setGridData(centralList);

                //hide toolbar
                ((MainActivity) getViewContext()).hideToolBar(true);

                //hide layout
                main_layout.setVisibility(View.GONE);

                mSwipeRefreshLayout.setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    }

    private void updatePromoList(List<PromoPts> data) {

        if (data.size() > 0) {

            if (mAdapter != null) {

                main_layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                layout_empty.setVisibility(View.GONE);

                mAdapter.setPromoPtsData(data);
            }

        } else {

            main_layout.setVisibility(View.GONE);
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
            createSession(callsession);
        } else {
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
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getPromoPoints(getPromoPointsSession);
                } else {
                    isLoading = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                    if (isAdded()) {
                        showError(response.body().getMessage());
                    }
                }
            } else {
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
                if (isAdded()) {
                    showError("Something went wrong with the server.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError("Something went wrong with the server.");
            }
        }
    };

    private void getPromoPoints(Callback<GetPromoPointsResponse> getPromoPointsCallback) {
        Call<GetPromoPointsResponse> getpromopoints = RetrofitBuild.getPromoPointsService(getViewContext())
                .getPromoPointsCall(imei,
                        authcode,
                        sessionid,
                        usertype,
                        userid,
                        customerid,
                        String.valueOf(limit));
        getpromopoints.enqueue(getPromoPointsCallback);
    }

    private final Callback<GetPromoPointsResponse> getPromoPointsSession = new Callback<GetPromoPointsResponse>() {

        @Override
        public void onResponse(Call<GetPromoPointsResponse> call, Response<GetPromoPointsResponse> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);

            isLoading = false;
            isfirstload = false;

            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {

                if (response.body().getStatus().equals("000")) {

                    List<PromoPts> promoPoints = response.body().getPromoPoints();

                    isloadmore = promoPoints.size() > 0;

                    if (isloadmore) {

                        if (mdb != null) {

                            if (!isbottomscroll) {
                                mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
                            }

                            for (PromoPts pts : promoPoints) {
                                mdb.insertPromoPts(pts, usertype);
                            }

                            updatePromoList(mdb.getPromoPts(customerid));

                        }

                    }

                } else {

                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }

                }

            }
        }

        @Override
        public void onFailure(Call<GetPromoPointsResponse> call, Throwable t) {
            isLoading = false;
            isfirstload = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                openErrorDialog("Something went wrong with the server.");
            }
        }
    };

    @Override
    public void onRefresh() {
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        mSwipeRefreshLayout.setRefreshing(true);

        main_layout.setVisibility(View.VISIBLE);
        layout_empty.setVisibility(View.GONE);

        if (mdb != null) {

            mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
            mAdapter.clear();

        }

        getSession();
    }
}
