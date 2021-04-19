package com.ultramega.shop.adapter.mylist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.SelectVariationActivity;
import com.ultramega.shop.activity.ViewShopItemActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.MyListFragment;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.MyList;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.DeleteMyListResponse;
import com.ultramega.shop.responses.GetItemPackagesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyListRecyclerAdapter extends RecyclerView.Adapter<MyListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyList> mGridData = new ArrayList<>();

    private String usertype = "";
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String itemid = "";
    private String itemtype = "";
    private String customerid = "";
    private String userid = "";
    private String catclass = "";
    private String itemcode = "";
    private String wholesalerpricegroup = "";

    private int current_pos = -1;
    private boolean isGetPackages = false;

    private UltramegaShopUtilities mdb;
    private MyListFragment fragment;
    private Class<ViewShopItemActivity> view;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView product_name;
        private final ImageView product_photo;
        private final Button btn_my_list_delete;
        private final Button btn_view_item;
        private LinearLayout layoutAddToCart;
        private LinearLayout layoutViewItem;
        private Button btnAddToCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_my_list_name);
            product_photo = itemView.findViewById(R.id.product_my_list_photo);
            btn_my_list_delete = itemView.findViewById(R.id.btn_my_list_delete);
            btn_view_item = itemView.findViewById(R.id.btn_my_list_view_item);
            btn_my_list_delete.setOnClickListener(this);
            btn_view_item.setOnClickListener(this);

            layoutAddToCart = itemView.findViewById(R.id.layoutAddToCart);
            layoutViewItem = itemView.findViewById(R.id.layoutViewItem);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnAddToCart.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MyList item = mGridData.get(position);

            switch (v.getId()) {
                case R.id.btn_my_list_view_item: {

                    try {
                        ViewShopItemActivity.start(getContext(),
                                new CategoryItems(
                                        item.getCategory(),
                                        item.getCatClass(),
                                        item.getDescription(),
                                        item.getItemPicUrl()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case R.id.btn_my_list_delete: {

                    try {

                        current_pos = position;
                        itemid = item.getCatClass();

                        String msg = "";

                        if (item.getType() != null) {

                            if (item.getType().equals("CATCLASS")) {
                                msg = item.getDescription();
                            } else {
                                msg = item.getItemDescription();
                            }
                            deleteMyListDialog("Are you sure you want to remove " + msg + " from your list?");

                        } else {
                            fragment.openErrorDialog("Invalid Data.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case R.id.btnAddToCart: {

                    try {
                        isGetPackages = true;
                        catclass = item.getCatClass();
                        itemcode = item.getItemCode();
                        getSession();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }

    public MyListRecyclerAdapter(Context context, List<MyList> mGridData, MyListFragment fragment) {
        this.mContext = context;
        this.mGridData = mGridData;
        this.fragment = fragment;

        mdb = new UltramegaShopUtilities(getContext());
        usertype = fragment.getCurrentUserType(getContext());
        imei = CommonFunctions.getIMEI(getContext());

        if (usertype.equals("CONSUMER")) {

            ConsumerProfile itemProf = mdb.getConsumerProfile();

            Log.d("antonhttp", "itemProf: " + new Gson().toJson(itemProf));

            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            wholesalerpricegroup = ".";

            itemtype = "RETAIL";
        } else if (usertype.equals("WHOLESALER")) {

            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();

            Log.d("antonhttp", "wholesalerProfile: " + new Gson().toJson(wholesalerProfile));

            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            wholesalerpricegroup = wholesalerProfile.getPriceGroup();

            itemtype = "WHOLESALE";
        }
    }

    private Context getContext() {
        return mContext;
    }

    public void setListData(List<MyList> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public MyListRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyListRecyclerAdapter.MyViewHolder holder, int position) {
        MyList item = mGridData.get(position);

        if (item.getType() != null) {
            if (item.getType().equals("CATCLASS")) {
                holder.layoutViewItem.setVisibility(View.VISIBLE);
                holder.layoutAddToCart.setVisibility(View.GONE);
                holder.product_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getDescription()));
            } else {
                holder.layoutViewItem.setVisibility(View.GONE);
                holder.layoutAddToCart.setVisibility(View.VISIBLE);
                holder.product_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getItemDescription()));
            }
        }

        Glide.with(mContext)
                .load(item.getItemPicUrl())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.ic_um_default_products)
                        .error(R.drawable.ic_um_default_products)
                        .override(250, 250)
                        .centerCrop())
                .into(holder.product_photo);

        holder.btnAddToCart.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", "Add to Cart"));
        holder.btn_my_list_delete.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", "Remove"));
        holder.btn_view_item.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", "View Item"));
        holder.product_name.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private void deleteMyList(Callback<DeleteMyListResponse> deleteMyListCallBack) {

        Log.d("antonhttp", "userid: " + userid);

        Call<DeleteMyListResponse> deletemylist = RetrofitBuild.deleteMyListService(getContext())
                .deleteMyListCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        userid,
                        itemid);
        deletemylist.enqueue(deleteMyListCallBack);
    }

    private final Callback<DeleteMyListResponse> deleteMyListSession = new Callback<DeleteMyListResponse>() {

        @Override
        public void onResponse(Call<DeleteMyListResponse> call, Response<DeleteMyListResponse> response) {
            ResponseBody errorBody = response.errorBody();
            fragment.hideProgressDialog();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    remove();

                } else if (response.body().getStatus().equals("004")) {

                    fragment.forceLogoutDialog("Invalid User");

                } else {

                    fragment.openErrorDialog(response.body().getMessage());

                }
            } else {
                fragment.openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<DeleteMyListResponse> call, Throwable t) {
            fragment.hideProgressDialog();
            fragment.openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void getItemPackages(Callback<GetItemPackagesResponse> deleteMyListCallBack) {
        Call<GetItemPackagesResponse> deletemylist = RetrofitBuild.getItemPackagesService(getContext())
                .getItemPackagesCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        userid,
                        catclass,
                        wholesalerpricegroup,
                        itemcode);
        deletemylist.enqueue(deleteMyListCallBack);
    }

    private final Callback<GetItemPackagesResponse> getItemPackagesSession = new Callback<GetItemPackagesResponse>() {

        @Override
        public void onResponse(Call<GetItemPackagesResponse> call, Response<GetItemPackagesResponse> response) {
            ResponseBody errorBody = response.errorBody();
            fragment.hideProgressDialog();
            isGetPackages = false;
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    if (mdb != null) {
                        mdb.deleteSKU(itemcode);
                        ItemSKU selectedSKU = null;
                        List<ItemSKU> itemSKUList = response.body().getMylist();
                        for (ItemSKU itemSKU : itemSKUList) {
                            mdb.insertAllItemSKUS(itemSKU, usertype);
                            if(itemSKU.getPackageDescription().equals("Piece")){
                                selectedSKU = itemSKU;
                            }
                        }
                        Log.d("okhttp","ITEM PACKAGES  : "+ new Gson().toJson(itemSKUList));
                        SelectVariationActivity.start(getContext(), (selectedSKU == null ? itemSKUList.get(0) : selectedSKU), true);
                    }
                } else {
                    fragment.openErrorDialog(response.body().getMessage());
                }
            } else {
                fragment.openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetItemPackagesResponse> call, Throwable t) {
            isGetPackages = false;
            fragment.hideProgressDialog();
            fragment.openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    if (isGetPackages) {
                        getItemPackages(getItemPackagesSession);
                    } else {
                        deleteMyList(deleteMyListSession);
                    }
                } else {
                    isGetPackages = false;
                    fragment.hideProgressDialog();
                    fragment.showError(response.body().getMessage());
                }
            } else {
                isGetPackages = false;
                fragment.hideProgressDialog();
                fragment.showError("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isGetPackages = false;
            fragment.hideProgressDialog();
            fragment.showError(fragment.getString(R.string.generic_internet_error_message));
        }
    };


    public void createSession(Callback<GetSessionResponse> sessionCallback) {

        ((BaseActivity) getContext()).createSession(sessionCallback);
    }


    private void deleteMyListDialog(String message) {
        new MaterialDialog.Builder(getContext())
                .content(message)
                .cancelable(false)
                .positiveText("Remove")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getSession();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void getSession() {
        if (!isGetPackages) {
            fragment.showProgressDialog();
        }
        //api access
        createSession(callsession);
    }

    private void remove() {
        mdb.deleteItemList(itemid);
        mGridData.remove(current_pos);
        notifyItemRemoved(current_pos);
        notifyItemRangeChanged(current_pos, mGridData.size());
        fragment.updateList(mdb.getCustomerMyList());
    }
}

