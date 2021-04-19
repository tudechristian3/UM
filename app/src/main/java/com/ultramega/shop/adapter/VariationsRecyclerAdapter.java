package com.ultramega.shop.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.SelectVariationActivity;
import com.ultramega.shop.activity.ViewShopItemActivity;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.adapter.myshoppingcart.checkout.SKUPackageRecyclerAdapter;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ImageSlider;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.responses.AddToMyListResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VariationsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShoppingCartsQueue> mOrderData = new ArrayList<>();
    private List<ItemSKU> mPackageData = new ArrayList<>();
    private List<ItemSKU> mGridData;
    private final Context mContext;
    private UltramegaShopUtilities mdb;
    private String mUserType;

    private String sessionid;
    private String authcode;
    private String customerid;
    private String imei;

    private String itemcode;
    private String catclass;
    private String itemgrouppicurl;
    private String userid;

    private SKUPackageRecyclerAdapter mPackageAdapter;
    private VariationsRecyclerAdapter.MyViewHolder mHolder;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<ImageSlider> imageSliderList;
    private String itemName;

    //private List<ItemSKU> mPackages = new ArrayList<>();
//    private List<ItemSKU> mBulkPackages = new ArrayList<>();
    private List<ItemSKU> mPackages = new ArrayList<>();

    public VariationsRecyclerAdapter(Context context, String usertype, String customerid, String userid) {
        mContext = context;
        mdb = new UltramegaShopUtilities(mContext);
        mUserType = usertype;
        imei = CommonFunctions.getIMEI(mContext);
        this.customerid = customerid;
        this.userid = userid;

//        mOrderData = new ArrayList<>();
        mGridData = new ArrayList<>();
//        mPackageData = new ArrayList<>();
        imageSliderList = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

    public void setVariationsData(List<ItemSKU> mGridData) {
        int startPos = this.mGridData.size() + 1;
        this.mGridData.clear();
        this.mGridData.add(null);
        this.mGridData.addAll(mGridData);
        notifyItemRangeInserted(startPos, mGridData.size());
    }

    public void setHeaderData(List<ImageSlider> imageSliderList, String itemName, String itemgrouppicurl, String catclass, String usertype, String customerid, String userid) {
        this.imageSliderList = imageSliderList;
        this.itemName = itemName;
        this.itemgrouppicurl = itemgrouppicurl;
        this.catclass = catclass;
        this.mUserType = usertype;
        this.customerid = customerid;
        this.userid = userid;
    }

    public void add(ItemSKU item) {
        if (mGridData.size() == 0) {
            mGridData.add(null);
        }
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<ItemSKU> mGridData) {
        for (ItemSKU item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_variation_item_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_variation_item, parent, false);
            return new MyViewHolder(itemView);
        }

        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).productName.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", itemName));
            ((HeaderViewHolder) holder).mDemoSlider.stopAutoCycle();
            ((HeaderViewHolder) holder).mDemoSlider.removeAllSliders();
            ((ViewShopItemActivity) mContext).setUpSlider(imageSliderList, ((HeaderViewHolder) holder).mDemoSlider, ((HeaderViewHolder) holder).pagerIndicator);
        } else if (holder instanceof MyViewHolder) {
//            if (position > -1) {

            ItemSKU item = mGridData.get(position);

            itemcode = item.getItemCode();
            catclass = item.getCatClass();
            mHolder = ((MyViewHolder) holder);

            mHolder.item_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", item.getItemDescription()));
            if (item.getPackSize().equals(".") || item.getPackSize() == null) {
                mHolder.item_pack_size.setVisibility(View.GONE);
            } else {
                mHolder.item_pack_size.setVisibility(View.VISIBLE);
                mHolder.item_pack_size.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getPackSize()));
            }

            Glide.with(getContext())
                    .load(item.getItemPicURL())
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_um_default_products)
                            .override(CommonFunctions.getScreenWidth(getContext()) / 2, CommonFunctions.getScreenHeight(getContext()) / 4))
                    .thumbnail(0.1f)
                    .into(mHolder.item_image_photo);

            mHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mHolder.recyclerView.setNestedScrollingEnabled(false);
            mPackageAdapter = new SKUPackageRecyclerAdapter(mContext, mdb, mUserType);
            mHolder.recyclerView.setAdapter(mPackageAdapter);

            if (mdb.checkisPromoCounter(itemcode) > 0) {

                mHolder.item_promo_photo.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(R.drawable.promo_icon_red_box)
                        .apply(new RequestOptions()
                                .fitCenter()
                                .override(100, 100))
                        .into(mHolder.item_promo_photo);

            } else {

                mHolder.item_promo_photo.setVisibility(View.GONE);

            }

//            new LongCheckPromoOperation().execute();
//            new LongOperation().execute();


//            new Thread(new Runnable() {
//                @Override
//                public void run() {

//                    if (mdb == null) {
//                        mdb = new UltramegaShopUtilities(mContext);
//                    }

            List<ItemSKU> mBulkPackages = new ArrayList<>();
            List<ItemSKU> mPackages = new ArrayList<>();
            if (mUserType.equals("CONSUMER")) {

                mPackages = mdb.getSkuPackages(catclass, itemcode);

            } else if (mUserType.equals("WHOLESALER")) {

                mBulkPackages = mdb.getSkuPackagesByBulk(catclass, itemcode, "1");

                if (mBulkPackages.size() > 0) {

                    mPackages = mBulkPackages;

                } else {

                    mPackages = mdb.getSkuPackagesByBulk(catclass, itemcode, "0");

                }

            }

            mPackageAdapter.clear();
            mPackageAdapter.addAll(mPackages);

            if (mUserType.equals("WHOLESALER")) {

                if (mBulkPackages.size() > 0) {
                    if (mdb.countSKU(catclass, itemcode, "0") > 0) {

                        mHolder.addMoreItems.setVisibility(View.VISIBLE);

                    } else {

                        mHolder.addMoreItems.setVisibility(View.GONE);

                    }
                } else {
                    mHolder.addMoreItems.setVisibility(View.GONE);
                }

            }

//                }
//            }).start();

            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setTag(item);

        }
    }

//    private class LongCheckPromoOperation extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            int count = 0;
//
//            try {
//                if (!isCancelled()) {
//                    count = mdb.checkisPromoCounter(itemcode);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return String.valueOf(count);
//        }
//
//        @Override
//        protected void onPostExecute(String countcheck) {
//
//            try {
//                if (Integer.valueOf(countcheck) > 0) {
//
//                    mHolder.item_promo_photo.setVisibility(View.VISIBLE);
//                    Glide.with(getContext())
//                            .load(R.drawable.promo_icon_red_box)
//                            .apply(new RequestOptions()
//                                    .fitCenter()
//                                    .override(100, 100))
//                            .into(mHolder.item_promo_photo);
//
//                } else {
//
//                    mHolder.item_promo_photo.setVisibility(View.GONE);
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    private class LongOperation extends AsyncTask<List<ItemSKU>, Void, List<ItemSKU>> {
//
//        private List<ItemSKU> mPackages = new ArrayList<>();
//
//        @Override
//        protected List<ItemSKU> doInBackground(List<ItemSKU>... lists) {
//
////            List<ItemSKU> mPackages = new ArrayList<>();
//
//            try {
////                Thread.sleep(1000);
//
//                if (mUserType.equals("CONSUMER")) {
//
//                    mPackages = mdb.getSkuPackages(catclass, itemcode);
//
//                } else if (mUserType.equals("WHOLESALER")) {
//
//                    mBulkPackages = mdb.getSkuPackagesByBulk(catclass, itemcode, "1");
//
//                    if (mBulkPackages.size() > 0) {
//
//                        mPackages = mBulkPackages;
//
//                    } else {
//
//                        mPackages = mdb.getSkuPackagesByBulk(catclass, itemcode, "0");
//
//                    }
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                mPackages.clear();
//            }
//
//            return mPackages;
//        }
//
//        @Override
//        protected void onPostExecute(List<ItemSKU> itemSKUS) {
//
////            try {
//
//            Log.d("antonhttp", "itemSKUS: " + new Gson().toJson(itemSKUS));
//
//            Log.d("antonhttp", "=======================================");
//
//            mPackageAdapter.clear();
//            mPackageAdapter.addAll(itemSKUS);
//
//            if (mUserType.equals("WHOLESALER")) {
//
//                if (mBulkPackages.size() > 0) {
//                    if (mdb.countSKU(catclass, itemcode, "0") > 0) {
//
//                        mHolder.addMoreItems.setVisibility(View.VISIBLE);
//
//                    } else {
//
//                        mHolder.addMoreItems.setVisibility(View.GONE);
//
//                    }
//                } else {
//                    mHolder.addMoreItems.setVisibility(View.GONE);
//                }
//
//            }
//
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
////            mPackages.clear();
////            mBulkPackages.clear();
////            mBulkPackages = new ArrayList<>();
//        }
//    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ItemSKU item = (ItemSKU) v.getTag();
            SelectVariationActivity.start(getContext(), item, false);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SliderLayout mDemoSlider;
        private PagerIndicator pagerIndicator;
        private TextView productName;
        private Button btnAddToList;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mDemoSlider = itemView.findViewById(R.id.slider);
            pagerIndicator = itemView.findViewById(R.id.pagerIndicator);
            mDemoSlider.setCustomIndicator(pagerIndicator);
            mDemoSlider.stopAutoCycle();
            mDemoSlider.removeAllSliders();
            productName = itemView.findViewById(R.id.product_name);
            btnAddToList = itemView.findViewById(R.id.btnAddToList);
            btnAddToList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddToList: {
                    if (((ViewShopItemActivity) mContext).getCurrentUserType(mContext).equals("CONSUMER")) {
                        if (mdb.getConsumerID() == null) {
                            ((ViewShopItemActivity) mContext).openLoginDialog("You are required to login to proceed with adding to list");
                        } else {
                            ((ViewShopItemActivity) mContext).progressDialog();
                            ((ViewShopItemActivity) mContext).createSession(calladdtolistsession);
                        }
                    } else if (((ViewShopItemActivity) mContext).getCurrentUserType(mContext).equals("WHOLESALER")) {
                        if (mdb.getWholesalerID() == null) {
                            ((ViewShopItemActivity) mContext).openLoginDialog("You are required to login to proceed with adding to list");
                        } else {
                            ((ViewShopItemActivity) mContext).progressDialog();
                            ((ViewShopItemActivity) mContext).createSession(calladdtolistsession);
                        }
                    }
                    break;
                }
            }
        }

        private final Callback<GetSessionResponse> calladdtolistsession = new Callback<GetSessionResponse>() {

            @Override
            public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
                ResponseBody errorBody = response.errorBody();
                if (errorBody == null) {
                    if (response.body().getStatus().equals("000")) {
                        sessionid = response.body().getSession();
                        authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                        addToMyList(addToMyListSession);
                    } else {
                        ((ViewShopItemActivity) mContext).hideProgressDialog();
                        ((ViewShopItemActivity) mContext).openErrorDialog(response.body().getMessage());
                    }
                } else {
                    ((ViewShopItemActivity) mContext).hideProgressDialog();
                    ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<GetSessionResponse> call, Throwable t) {
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
            }
        };

        private void addToMyList(Callback<AddToMyListResponse> addToMyListCallback) {
            Call<AddToMyListResponse> addtomylist = RetrofitBuild.addToMyListService(mContext)
                    .addToMyListCall(
                            imei,
                            authcode,
                            sessionid,
                            customerid,
                            mUserType,
                            catclass,
                            itemgrouppicurl,
                            userid,
                            "CATCLASS");
            addtomylist.enqueue(addToMyListCallback);
        }

        private final Callback<AddToMyListResponse> addToMyListSession = new Callback<AddToMyListResponse>() {

            @Override
            public void onResponse(Call<AddToMyListResponse> call, Response<AddToMyListResponse> response) {
                ResponseBody errorBody = response.errorBody();
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                if (errorBody == null) {
                    if (response.body().getStatus().equals("000")) {
                        ((ViewShopItemActivity) mContext).openListDialog();
                    } else if (response.body().getStatus().equals("004")) {
                        ((ViewShopItemActivity) mContext).forceLogoutDialog("Invalid User");
                    } else {
                        ((ViewShopItemActivity) mContext).openErrorDialog(response.body().getMessage());
                    }
                } else {
                    ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AddToMyListResponse> call, Throwable t) {
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
            }
        };

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView item_name;
        private final TextView item_pack_size;
        private final ImageView item_image_photo;
        private final ImageView item_promo_photo;

        private LinearLayout itemnameLayout;
        private ImageView addMoreItems;
        private Button btnAddToList;

        private final RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name_variation);
            item_pack_size = itemView.findViewById(R.id.item_pack_size);
            item_image_photo = itemView.findViewById(R.id.item_image_variation);
            item_image_photo.setOnClickListener(this);
            item_promo_photo = itemView.findViewById(R.id.item_promo_variation);

            recyclerView = itemView.findViewById(R.id.recycler_view_package_list);

            btnAddToList = itemView.findViewById(R.id.btnAddToList);
            btnAddToList.setOnClickListener(this);

//            if (mUserType.equals("CONSUMER")) {
//                recyclerView.setLayoutFrozen(true);
//            }

            if (mUserType.equals("WHOLESALER")) {
                itemnameLayout = itemView.findViewById(R.id.itemnameLayout);
                itemnameLayout.setOnClickListener(this);
                addMoreItems = itemView.findViewById(R.id.addMoreItems);
                addMoreItems.setEnabled(false);
            }

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if (position > -1) {

                ItemSKU item = mGridData.get(position);

                switch (view.getId()) {

                    case R.id.itemnameLayout: {

                        if (addMoreItems.getRotation() == 180) {

                            addMoreItems.setRotation(360);

                            mPackageAdapter.clear();

                            List<ItemSKU> mBulkPackages = mdb.getSkuPackagesByBulk(item.getCatClass(), item.getItemCode(), "1");

                            if (mBulkPackages.size() > 0) {

                                mPackageAdapter.setPackageData(mBulkPackages);

                            } else {

                                mPackageAdapter.setPackageData(mdb.getSkuPackagesByBulk(item.getCatClass(), item.getItemCode(), "0"));
                            }


                        } else {

                            addMoreItems.setRotation(180);

                            mPackageAdapter.clear();

                            mPackageAdapter.setPackageData(mdb.getSkuPackages(item.getCatClass(), item.getItemCode()));

                        }

                        break;
                    }
                    case R.id.item_image_variation: {

                        FullScreenActivity.start(getContext(), item.getItemPicURL());

                        break;
                    }
                    case R.id.btnAddToList: {

                        catclass = item.getItemCode();
                        itemgrouppicurl = item.getItemPicURL();

                        if (mUserType.equals("CONSUMER")) {
                            if (mdb.getConsumerID() == null) {

                                ((ViewShopItemActivity) mContext).openLoginDialog("You are required to login to proceed with adding to list");

                            } else {

                                ((ViewShopItemActivity) mContext).progressDialog();
                                ((ViewShopItemActivity) mContext).createSession(calladdtolistsession);

                            }
                        } else if (mUserType.equals("WHOLESALER")) {
                            if (mdb.getWholesalerID() == null) {

                                ((ViewShopItemActivity) mContext).openLoginDialog("You are required to login to proceed with adding to list");

                            } else {

                                ((ViewShopItemActivity) mContext).progressDialog();
                                ((ViewShopItemActivity) mContext).createSession(calladdtolistsession);

                            }
                        }

                        break;
                    }

                }

            }

        }

        private final Callback<GetSessionResponse> calladdtolistsession = new Callback<GetSessionResponse>() {

            @Override
            public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
                ResponseBody errorBody = response.errorBody();
                if (errorBody == null) {
                    if (response.body().getStatus().equals("000")) {
                        sessionid = response.body().getSession();
                        authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                        addToMyList(addToMyListSession);
                    } else {
                        ((ViewShopItemActivity) mContext).hideProgressDialog();
                        ((ViewShopItemActivity) mContext).openErrorDialog(response.body().getMessage());
                    }
                } else {
                    ((ViewShopItemActivity) mContext).hideProgressDialog();
                    ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<GetSessionResponse> call, Throwable t) {
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
            }
        };

        private void addToMyList(Callback<AddToMyListResponse> addToMyListCallback) {
            Call<AddToMyListResponse> addtomylist = RetrofitBuild.addToMyListService(mContext)
                    .addToMyListCall(
                            imei,
                            authcode,
                            sessionid,
                            customerid,
                            mUserType,
                            catclass,
                            itemgrouppicurl,
                            userid,
                            "ITEMCODE");
            addtomylist.enqueue(addToMyListCallback);
        }

        private final Callback<AddToMyListResponse> addToMyListSession = new Callback<AddToMyListResponse>() {

            @Override
            public void onResponse(Call<AddToMyListResponse> call, Response<AddToMyListResponse> response) {
                ResponseBody errorBody = response.errorBody();
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                if (errorBody == null) {
                    if (response.body().getStatus().equals("000")) {

                        ((ViewShopItemActivity) mContext).openListDialog();

                    } else if (response.body().getStatus().equals("004")) {

                        ((ViewShopItemActivity) mContext).forceLogoutDialog("Invalid User");

                    } else {

                        ((ViewShopItemActivity) mContext).openErrorDialog(response.body().getMessage());

                    }
                } else {
                    ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AddToMyListResponse> call, Throwable t) {
                ((ViewShopItemActivity) mContext).hideProgressDialog();
                ((ViewShopItemActivity) mContext).openErrorDialog("Something went wrong. Please try again.");
            }
        };

    }

}
