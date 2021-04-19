package com.ultramega.shop.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionMenu;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.adapter.shop.CentralRecyclerViewAdapter;
import com.ultramega.shop.adapter.shop.ViewShopPagerAdapterConsumer;
import com.ultramega.shop.adapter.shop.ViewShopPagerAdapterWholesaler;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.util.SpacesItemDecoration;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String imei = "";
    private String usertype = "";
    private UltramegaShopUtilities mdb;
    private LinearLayout layout_empty;

    //FAB
    private ImageView central_background;
    private AppBarLayout appBarLayout;
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
        View view = inflater.inflate(R.layout.fragment_shop_categories, container, false);

//        try {

        layout_empty = (LinearLayout) view.findViewById(R.id.layout_empty);
        layout_empty.setVisibility(View.GONE);

        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);

        layout_menu_central = (FrameLayout) view.findViewById(R.id.layout_menu_central);
        fab = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        recyclerViewCentral = (RecyclerView) view.findViewById(R.id.recycler_view_central);
        recyclerViewCentral.setLayoutManager(new GridLayoutManager(getViewContext(), 3, GridLayoutManager.VERTICAL, false));
        recyclerViewCentral.setNestedScrollingEnabled(false);
        mCentralAdapter = new CentralRecyclerViewAdapter(getViewContext(), ShopFragment.this);
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
        if (usertype.equals("CONSUMER")) {

            centralList.add(new Central("Pick & Shop", R.drawable.central_pick_and_shop));
//            centralList.add(new Central("Top Picks", R.drawable.central_top_picks));
            centralList.add(new Central("Promos", R.drawable.central_my_promos));
            centralList.add(new Central("My Cart", R.drawable.central_my_cart));

            if (mdb.getConsumerID() != null) {
                centralList.add(new Central("Orders", R.drawable.central_orders));
                centralList.add(new Central("My Wallet", R.drawable.central_my_wallet));
                centralList.add(new Central("Future Buys", R.drawable.central_future_buys));
                centralList.add(new Central("My Points", R.drawable.central_my_points));
                centralList.add(new Central("Notifications", R.drawable.central_event));

            }

        } else if (usertype.equals("WHOLESALER")) {

            centralList.add(new Central("Pick & Shop", R.drawable.central_pick_and_shop));
//            centralList.add(new Central("Top Picks", R.drawable.central_top_picks));
            centralList.add(new Central("Promos", R.drawable.central_my_promos));
            centralList.add(new Central("My Cart", R.drawable.central_my_cart));
            centralList.add(new Central("Orders", R.drawable.central_orders));
            centralList.add(new Central("Future Buys", R.drawable.central_future_buys));
            centralList.add(new Central("My Points", R.drawable.central_my_points));
            centralList.add(new Central("Notifications", R.drawable.central_event));

        }

        //dispatch to a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (isAdded()) {

                            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

                            setTabTheme(UserPreferenceManager.getStringPreference(getViewContext(), BaseFragment.Theme_Current), tabLayout);

                            if (usertype.equals("CONSUMER")) {

                                viewPager.setOffscreenPageLimit(3);

                                viewPager.setAdapter(new ViewShopPagerAdapterConsumer(getChildFragmentManager(), getViewContext()));

                            } else if (usertype.equals("WHOLESALER")) {

                                viewPager.setOffscreenPageLimit(4);

                                viewPager.setAdapter(new ViewShopPagerAdapterWholesaler(getChildFragmentManager(), getViewContext()));

                            }

                            tabLayout.setupWithViewPager(viewPager);

                            //show central by default
                            if (getArguments() != null) {

                                viewSpecificPage(getArguments().getInt("page_index", 0));

                                if (getArguments().getBoolean("isShow", true)) {

                                    showCentral();

                                }

                            } else {

                                showCentral();

                            }

                        }

                    }
                }, 400);

            }
        }).start();

//        } catch (Exception e) {
//            e.printStackTrace();
//        }


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

        return view;
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

                //show tabs
                viewPager.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
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

                //hide tabs
                viewPager.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
            }
        }
    }

    public void viewSpecificPage(int position) {
        if (isAdded()) {
            if (viewPager != null && appBarLayout != null) {
                viewPager.setCurrentItem(position);
                hideCentral();
            }
        }
    }

}
