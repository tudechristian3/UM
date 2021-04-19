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
import com.ultramega.shop.adapter.mylist.MyListRecyclerAdapter;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.MyList;
import com.ultramega.shop.responses.GetCustomerMyListResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyListRecyclerAdapter mAdapter;

    private List<MyList> mGridData;
    private CategoryItems item;

    private String imei = "";
    private String usertype = "";
    private String itemtype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String userid = "";
    private int limit;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;

    private NestedScrollView nested_scroll_view;
    private boolean isloadmore = true;
    private boolean isbottomscroll = false;

    private LinearLayout mSmoothProgressBar;
    private boolean isLoading = false;
    private boolean isfirstload = true;

    private RelativeLayout main_layout;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        try {

            main_layout = (RelativeLayout) view.findViewById(R.id.main_layout);
            mSmoothProgressBar = (LinearLayout) view.findViewById(R.id.mSmoothProgressBar);
            nested_scroll_view = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
            layout_empty.setVisibility(View.GONE);
            text_empty = (TextView) view.findViewById(R.id.text_empty);
            image_empty = (ImageView) view.findViewById(R.id.image_empty);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_list);

            //Initialize with empty data
            mdb = new UltramegaShopUtilities(getViewContext());
            mGridData = new ArrayList<>();
            mGridData = mdb.getCustomerMyList();
            limit = 0;

            ConsumerProfile itemProf = mdb.getConsumerProfile();
            usertype = getCurrentUserType(getViewContext());
            imei = CommonFunctions.getIMEI(getViewContext());

            if (usertype.equals("CONSUMER")) {
                itemtype = "RETAIL";
                userid = itemProf.getUserID();
                customerid = itemProf.getConsumerID();
            } else if (usertype.equals("WHOLESALER")) {
                itemtype = "WHOLESALE";
                userid = mdb.getWholesalerProfile().getUserID();
                customerid = mdb.getWholesalerID();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
            recyclerView.setNestedScrollingEnabled(false);
            mAdapter = new MyListRecyclerAdapter(getViewContext(), mGridData, MyListFragment.this);
            getSession();
            recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
            recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
            recyclerView.setAdapter(mAdapter);

            //updateFirstLoadList(mGridData);

            //SETUP CENTRAL LAYOUT
            setUpCentralLayout(view);

            nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setUpCentralLayout(View view) {
        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), MyListFragment.this);
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

    public void updateFirstLoadList(List<MyList> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            mAdapter.setListData(data);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.emptyicon));
        }
    }

    public void updateList(List<MyList> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            mAdapter.setListData(data);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
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
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    getCustomerMyList(getCustomerMyListSession);
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
                    showError("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    private void getCustomerMyList(Callback<GetCustomerMyListResponse> getCustomerMyListCallback) {
        Call<GetCustomerMyListResponse> list = RetrofitBuild.getCustomerMyListService(getViewContext())
                .getCustomerMyListCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        userid,
                        String.valueOf(limit));
        list.enqueue(getCustomerMyListCallback);
    }

    private final Callback<GetCustomerMyListResponse> getCustomerMyListSession = new Callback<GetCustomerMyListResponse>() {

        @Override
        public void onResponse(Call<GetCustomerMyListResponse> call, Response<GetCustomerMyListResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            ResponseBody errorBody = response.errorBody();

            isLoading = false;
            isfirstload = false;

            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getList().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                    }
                    List<MyList> myLists = response.body().getList();
                    for (MyList list : myLists) {
                        mdb.insertCustomerMyList(list, usertype);
                    }
                    updateList(mdb.getCustomerMyList());
                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                if (isAdded()) {
                    showError("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetCustomerMyListResponse> call, Throwable t) {
            isLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (isAdded()) {
                showError(getString(R.string.generic_internet_error_message));
            }
        }
    };

    @Override
    public void onRefresh() {
        isfirstload = false;
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        layout_empty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        getSession();
    }
}
