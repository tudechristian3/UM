package com.ultramega.shop.adapter.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.InterestActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.notification.NotificationActivity;
import com.ultramega.shop.activity.shoppingcart.ViewShoppingCartsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.LoyaltyRewardsFragment;
import com.ultramega.shop.fragment.MyListFragment;
import com.ultramega.shop.fragment.OrdersFragment;
import com.ultramega.shop.fragment.ShopFragment;
import com.ultramega.shop.fragment.WalletReloadFragment;
import com.ultramega.shop.fragment.shop.CategoriesListFragment;
import com.ultramega.shop.fragment.shop.DailyFindsFragment;
import com.ultramega.shop.fragment.shop.PromosFragment;
import com.ultramega.shop.fragment.shop.SuppliersFragment;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 3/1/2018.
 */

public class CentralRecyclerViewAdapter extends RecyclerView.Adapter<CentralRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Central> mGridData;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private boolean isShop = false;

    private ShopFragment mFragment;
    private DailyFindsFragment mDailyFindsFragment;
    private PromosFragment mPromosFragment;
    private CategoriesListFragment mCategoriesFragment;
    private SuppliersFragment mSuppliersFragment;
    private OrdersFragment mOrdersFragment;
    private WalletReloadFragment mWalletFragment;
    private MyListFragment mListFragment;
    private LoyaltyRewardsFragment mLoyaltyFragment;
    private UltramegaShopUtilities mdb;
    private String userType = "";
    private String mNotificaitonItemCount = "";
    private MaterialDialog mPaymentOptionsDialog;
    private AlertDialog.Builder builder;

    public CentralRecyclerViewAdapter(Context context, DailyFindsFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mDailyFindsFragment = mFragment;
        isShop = true;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, PromosFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mPromosFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, CategoriesListFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mCategoriesFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, SuppliersFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mSuppliersFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, ShopFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mFragment = mFragment;
        isShop = true;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, OrdersFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mOrdersFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, WalletReloadFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mWalletFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, MyListFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mListFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    public CentralRecyclerViewAdapter(Context context, LoyaltyRewardsFragment mFragment) {
        mContext = context;
        mGridData = new ArrayList<>();
        this.mLoyaltyFragment = mFragment;
        isShop = false;
        mdb = new UltramegaShopUtilities(mContext);
        userType = mFragment.getCurrentUserType(mContext);
    }

    private Context getContext() {
        return mContext;
    }

    public void setGridData(final List<Central> mGridData) {
        for (Central item : mGridData) {
            add(item);
        }
    }

    public void add(Central item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Central> mGridData) {
        for (Central item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public CentralRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_central_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CentralRecyclerViewAdapter.MyViewHolder holder, int position) {
        Central central = mGridData.get(position);

        Log.d("okhttp","CENTRRAL: "+new Gson().toJson(central));

        if (userType.equals("CONSUMER")) {
            holder.txvCentral.setTextColor(ContextCompat.getColor(mContext, R.color.color_013A80));
        } else if (userType.equals("WHOLESALER")) {
            holder.txvCentral.setTextColor(ContextCompat.getColor(mContext, R.color.color_B90F2B));
        }

        holder.txvCentral.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", central.getName().toUpperCase()));

        Glide.with(mContext)
                .asBitmap()
                .load(central.getImageDrawable())
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        holder.imgCentral.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(200, 200);
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }
                });


        if (central.getName().equals("Notifications")) {
            holder.notification_badge.setVisibility(View.VISIBLE);

            int count = UserPreferenceManager.getIntPreference(mContext, UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT);
            if (count == 0) {
                if (holder.notification_badge.getVisibility() != View.GONE) {
                    holder.notification_badge.setVisibility(View.GONE);
                }
            } else {
                mNotificaitonItemCount = String.valueOf(count);
                holder.notification_badge.setText(mNotificaitonItemCount);
                if (holder.notification_badge.getVisibility() != View.VISIBLE) {
                    holder.notification_badge.setVisibility(View.VISIBLE);
                }
            }

        } else if (central.getName().equals("My Cart")) {
            if (mdb != null) {
                if (mdb.getAllShoppingCartsQueue().size() == 0) {
                    if (holder.notification_badge.getVisibility() != View.GONE) {
                        holder.notification_badge.setVisibility(View.GONE);
                    }
                } else {
                    mNotificaitonItemCount = String.valueOf(mdb.getAllShoppingCartsQueue().size());
                    Log.d("opaw","NOTIFIcATION SHIT : "+mNotificaitonItemCount);
                    holder.notification_badge.setText(mNotificaitonItemCount);
                    if (holder.notification_badge.getVisibility() != View.VISIBLE) {
                        holder.notification_badge.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            holder.notification_badge.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgCentral;
        private TextView txvCentral;
        private TextView notification_badge;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCentral = (ImageView) itemView.findViewById(R.id.imgCentral);
            txvCentral = (TextView) itemView.findViewById(R.id.txvCentral);
            notification_badge = (TextView) itemView.findViewById(R.id.notification_badge);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position > -1) {
                Central central = mGridData.get(position);
                switch (central.getName()) {

                    case "Pick & Shop": {

                        if (userType.equals("CONSUMER")) {
                            if (mdb.getConsumerBehaviourPatterns().size() == 0) {
                                //InterestActivity.start(getContext());
                                MainActivity.start(getContext(), 9);
                            } else {
                                if (isShop) {

//                                    if (mDailyFindsFragment != null) {
//                                        mDailyFindsFragment.hideCentral();
//                                    }
                                    if (mCategoriesFragment != null) {
                                        mCategoriesFragment.hideCentral();
                                    } else {
                                        MainActivity.start(getContext(), 9);
                                    }

                                }
//                                else {
//
//                                    Bundle args = new Bundle();
//                                    args.putBoolean("isShow", false);
//                                    MainActivity.start(getContext(), 0, args);
//
//                                }
                            }
                        } else if (userType.equals("WHOLESALER")) {
                            if (mdb.getWholesalerBehaviourPatterns().size() == 0) {
                                InterestActivity.start(getContext());
                            } else {
                                if (isShop) {

                                    if (mDailyFindsFragment != null) {
                                        mDailyFindsFragment.hideCentral();
                                    }

                                } else {

                                    Bundle args = new Bundle();
                                    args.putBoolean("isShow", false);
                                    MainActivity.start(getContext(), 0, args);

                                }
                            }
                        }

                        break;
                    }
                    case "Top Picks": {

                        if (mDailyFindsFragment != null) {
                            mDailyFindsFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 0);
                        }

                        break;
                    }
                    case "Promos": {

                        if (mPromosFragment != null) {
                            mPromosFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 8);
                        }

                        break;
                    }
                    case "Categories": {

                        if (mCategoriesFragment != null) {
                            mCategoriesFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 9);
                        }

                        break;
                    }
                    case "Suppliers": {

                        if (mSuppliersFragment != null) {
                            mSuppliersFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 10);
//                            ((MainActivity) getContext()).displayFrag(10, null);
                        }

                        break;
                    }
                    case "My Cart": {
                        ViewShoppingCartsActivity.start(getContext());
                        break;
                    }
                    case "Orders": {

                        if (mOrdersFragment != null) {
                            mOrdersFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 3);
                        }

                        break;
                    }
                    case "My Wallet": {

                        if (mWalletFragment != null) {
                            mWalletFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 5);
                        }

                        break;
                    }
                    case "Future Buys": {

                        if (mListFragment != null) {
                            mListFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 4);
                        }

                        break;
                    }
                    case "My Points": {

                        if (mLoyaltyFragment != null) {
                            mLoyaltyFragment.hideCentral();
                        } else {
                            MainActivity.start(getContext(), 6);
                        }

                        break;
                    }

                    case "Notifications": {

                        UserPreferenceManager.saveIntegerPreference(getContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);
                        NotificationActivity.start(getContext());

                        break;
                    }

                    case "U-PAY":
                        setUpUPayPaymentOptionsDialog();
                        break;

                }
            }

        }
    }

    private void setUpUPayPaymentOptionsDialog() {
        mPaymentOptionsDialog = new MaterialDialog.Builder(mContext)
                .cancelable(true)
                .customView(R.layout.upay_redirect_option, false)
                .build();

        View view = mPaymentOptionsDialog.getCustomView();

        RelativeLayout upayMerchant = view.findViewById(R.id.upayMerchantLayout);
        RelativeLayout upayCustomer = view.findViewById(R.id.upayCustomerLayout);

        upayMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentOptionsDialog.dismiss();
                mPaymentOptionsDialog = null;
                boolean isInstalled = CommonFunctions.isPackageInstalled("com.ibayad.abscbn.merchantapp", getContext().getPackageManager());
                if(isInstalled){
                    CommonFunctions.startNewActivity(getContext(),"com.ibayad.abscbn.merchantapp");
                }else{

                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("No U-Pay Merchant App installed");
                    builder.setMessage("Do you want to download the app from play store?");
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                Intent viewIntent = new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.ibayad.abscbn.merchantapp"));
                                getContext().startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(getContext(),"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });

        upayCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentOptionsDialog.dismiss();
                mPaymentOptionsDialog = null;
                boolean isInstalled = CommonFunctions.isPackageInstalled("com.ibayad.abscbn.customerapp", getContext().getPackageManager());
                if(isInstalled){
                    CommonFunctions.startNewActivity(getContext(),"com.ibayad.abscbn.customerapp");
                }else{
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("No U-Pay Customer App installed");
                    builder.setMessage("Do you want to download the app from play store?");
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                Intent viewIntent = new Intent("android.intent.action.VIEW",
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.ibayad.abscbn.customerapp"));
                                getContext().startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(getContext(),"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        mPaymentOptionsDialog.show();
    }
}
