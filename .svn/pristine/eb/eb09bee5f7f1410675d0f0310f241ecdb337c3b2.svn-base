package com.ultramega.shop.adapter.myshoppingcart;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.activity.shoppingcart.ViewShoppingCartsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.AddUdateShoppingCartResponse;
import com.ultramega.shop.responses.DeleteShoppingCartResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShoppingCartRecyclerAdapter extends RecyclerView.Adapter<MyShoppingCartRecyclerAdapter.MyViewHolder> {

    private static final String Theme_Current = "AppliedTheme";
    private static final String Theme_Consumer = "Consumer_Theme";
    private static final String Theme_Wholesaler = "Wholesaler_Theme";

    public int mSelectedItem = -1;
    private final Context mContext;
    private List<ShoppingCartsQueue> mGridData = new ArrayList<>();

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String userid = "";
    private String packageid = "";
    private String itempicurl = "";

    private final UltramegaShopUtilities mdb;
    //private final MyShoppingCartFragment fragment;
    private ShoppingCartsQueue itemQueue;
    private int newQuantity;
    private int currentQuantity;
    private int deletedposition;

    private ViewShoppingCartsActivity shoppingCartsActivity;

    public MyShoppingCartRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mdb = new UltramegaShopUtilities(getContext());
        imei = CommonFunctions.getIMEI(getContext());
        usertype = getCurrentUserType(getContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile itemProf = mdb.getWholesalerProfile();
            customerid = itemProf.getWholesalerID();
            userid = itemProf.getUserID();
        }
        shoppingCartsActivity = ViewShoppingCartsActivity.getInstance();

        this.itemQueue = null;
        this.newQuantity = 0;
        this.currentQuantity = 0;
        this.deletedposition = -1;
    }

    private String getCurrentUserType(Context context) {
        switch (UserPreferenceManager.getStringPreference(context, Theme_Current)) {
            case Theme_Consumer: {
                return "CONSUMER";
            }
            case Theme_Wholesaler: {
                return "WHOLESALER";
            }
            default: {
                return "CONSUMER";
            }
        }
    }

    private Context getContext() {
        return mContext;
    }

    public void removedAt(int position) {
        mGridData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mGridData.size());
    }

    public void setCartData(List<ShoppingCartsQueue> mGridData) {
        int startPos = this.mGridData.size() + 1;
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        notifyItemRangeInserted(startPos, mGridData.size());
    }

    public void clearCartData() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public MyShoppingCartRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_shopping_cart_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyShoppingCartRecyclerAdapter.MyViewHolder holder, int position) {
        try {

            ShoppingCartsQueue item = mGridData.get(position);

            holder.product_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getItemDescription().concat(" " + item.getPackSize()).concat(" (" + item.getPackageDescription() + ")")));

            holder.product_amount.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", CommonFunctions.currencyFormatter(String.valueOf(item.getTotalAmount())).concat(" Php")));

            holder.product_quantity.setTag(R.id.my_shopping_cart_product_quantity, position);
            holder.product_quantity.setText(String.valueOf(item.getQuantity()));

            String minOrderText = String.format("Minimum order is %d.", item.getMinimumOrderQTY());
            String incrementalOrderText = String.format("You're only allowed to increase your order by %d.", item.getIncrementalOrderQTY());

            holder.txvMinimumOrder.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", minOrderText));
            holder.txvIncrementalOrder.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", incrementalOrderText));

            String photoimg = item.getItemPicUrl() == null ? "." : item.getItemPicUrl();
//            Glide.with(mContext).load(photoimg)
//                    .apply(new RequestOptions()
//                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                            .placeholder(R.drawable.ic_um_default_products)
//                            .error(R.drawable.ic_um_default_products)
//                            .override(250, 250)
//                            .fitCenter())
//                    .into(holder.product_photo);

            Glide.with(getContext())
                    .load(photoimg)
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(250, 250))
                    .into(holder.product_photo);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private void updateShoppingCartsQueue(Callback<AddUdateShoppingCartResponse> addUpdateCallback, ShoppingCartsQueue itemQueue, int quantity) {

        Call<AddUdateShoppingCartResponse> call = RetrofitBuild.addUpdateShoppingCartService(getContext())
                .addUpdateShoppingCartCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        itemQueue.getItemCode(),
                        quantity,
                        userid,
                        itemQueue.getPackageCode(),
                        itemQueue.getItemPicUrl());
        call.enqueue(addUpdateCallback);

    }

    private final Callback<AddUdateShoppingCartResponse> callCartSession = new Callback<AddUdateShoppingCartResponse>() {

        @Override
        public void onResponse(Call<AddUdateShoppingCartResponse> call, Response<AddUdateShoppingCartResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {

                switch (response.body().getStatus()) {
                    case "000": {

                        break;
                    }
                    case "004": {

                        shoppingCartsActivity.forceLogoutDialog("Invalid User");

                        break;
                    }
                    default: {

                        updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

                        shoppingCartsActivity.openErrorDialog(response.body().getMessage());

                        break;
                    }
                }

            } else {

                updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

                shoppingCartsActivity.openErrorDialog("Something went wrong");

            }
        }

        @Override
        public void onFailure(Call<AddUdateShoppingCartResponse> call, Throwable t) {

            updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

            shoppingCartsActivity.showError(shoppingCartsActivity.getString(R.string.generic_internet_error_message));

        }
    };

    public final Callback<GetSessionResponse> calldeleteitemsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);

                    deleteItemInShoppingCart(deleteItemInCartSession, itemQueue.getItemCode(), itemQueue.getPackageCode());
                } else {
                    shoppingCartsActivity.openErrorDialog(response.body().getMessage());
                }
            } else {
                shoppingCartsActivity.openErrorDialog("Something went wrong");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            shoppingCartsActivity.showError(shoppingCartsActivity.getString(R.string.generic_internet_error_message));
            Log.d("antonhttp", t.toString());
        }
    };

    private final Callback<DeleteShoppingCartResponse> deleteItemInCartSession = new Callback<DeleteShoppingCartResponse>() {

        @Override
        public void onResponse(Call<DeleteShoppingCartResponse> call, Response<DeleteShoppingCartResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    if (deletedposition != -1) {
                        removedAt(deletedposition);
                        mdb.deleteItemCartsQueue(itemQueue.getItemCode(), itemQueue.getPackageCode());
                        shoppingCartsActivity.updateList(mdb.getAllShoppingCartsQueue());
                        shoppingCartsActivity.setShoppingSummaryUI();

                    }
                } else if (response.body().getStatus().equals("004")) {

                    shoppingCartsActivity.forceLogoutDialog("Invalid User");

                }
            }
        }

        @Override
        public void onFailure(Call<DeleteShoppingCartResponse> call, Throwable t) {
            shoppingCartsActivity.showError(shoppingCartsActivity.getString(R.string.generic_internet_error_message));
        }
    };

    private void deleteItemInShoppingCart(Callback<DeleteShoppingCartResponse> deleteItemInShoppingCartCallback, String itemcode, String packageid) {
        Call<DeleteShoppingCartResponse> deleteitem = RetrofitBuild.deleteItemInShoppingCartService(getContext())
                .deleteShoppingCartCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        itemcode,
                        userid,
                        packageid);
        deleteitem.enqueue(deleteItemInShoppingCartCallback);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RelativeLayout delete_item;
        private final TextView product_name;
        private final TextView product_amount;
        private final EditText product_quantity;
        private final ImageView product_photo;
        private final Button buttonDecrease;
        private final Button buttonIncrease;
        private final TextView txvMinimumOrder;
        private final TextView txvIncrementalOrder;

        public MyViewHolder(View itemView) {
            super(itemView);
            delete_item = (RelativeLayout) itemView.findViewById(R.id.deleteItem);
            product_name = (TextView) itemView.findViewById(R.id.my_shopping_cart_product_name);
            product_amount = (TextView) itemView.findViewById(R.id.my_shopping_cart_product_amount);
            product_quantity = (EditText) itemView.findViewById(R.id.my_shopping_cart_product_quantity);
            product_quantity.setOnClickListener(this);
            product_photo = (ImageView) itemView.findViewById(R.id.my_shopping_cart_product_photo);
            product_photo.setOnClickListener(this);
            buttonDecrease = (Button) itemView.findViewById(R.id.btn_my_shopping_cart_decrease);
            buttonIncrease = (Button) itemView.findViewById(R.id.btn_my_shopping_cart_increase);
            txvMinimumOrder = (TextView) itemView.findViewById(R.id.txvMinimumOrder);
            txvIncrementalOrder = (TextView) itemView.findViewById(R.id.txvIncrementalOrder);

            product_quantity.addTextChangedListener(new QuantityTextWatcher(product_quantity));

            itemView.findViewById(R.id.packageOptionLayout).setOnClickListener(this);

            buttonDecrease.setOnClickListener(this);
            buttonIncrease.setOnClickListener(this);
            delete_item.setOnClickListener(this);
        }

        private class QuantityTextWatcher implements TextWatcher {
            private EditText editText;

            public QuantityTextWatcher(EditText editText) {
                this.editText = editText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0 && s.length() < 7) {

                    if (editText != null) {

                        //get edittext's position
                        int position = (int) editText.getTag(R.id.my_shopping_cart_product_quantity);

                        //get the object data
                        ShoppingCartsQueue item = mGridData.get(position);

                        //update total price
                        int qty = Integer.parseInt(s.toString());

                        if (qty > 0) {

                            product_quantity.removeTextChangedListener(this);

                            updateTotalItemPrice(item, qty);

                            if (qty >= item.getMinimumOrderQTY() && qty % item.getIncrementalOrderQTY() == 0) {

                                product_quantity.setTextColor(ContextCompat.getColor(mContext, R.color.color_000000));
                                disableAllButtons(false);
                                //shoppingCartsActivity.isCheckoutDisabled(false);

                            } else {

                                product_quantity.setTextColor(ContextCompat.getColor(mContext, R.color.color_F83832));
                                disableButtons(true);
                                //shoppingCartsActivity.isCheckoutDisabled(true);

                            }

                            product_quantity.addTextChangedListener(this);

                        }

                    }

//                } else {
//
//                    //get edittext's position
//                    int position = (int) editText.getTag(R.id.my_shopping_cart_product_quantity);
//
//                    if (position > 0) {
//
//                        //get the object data
//                        ShoppingCartsQueue item = mGridData.get(position);
//
//                        //update data in current data set object
//                        item.setQuantity(0);
//
//                        updateTotalItemPrice(item, 0);
//
//                        shoppingCartsActivity.setShoppingSummaryUI();
//
//                        //shoppingCartsActivity.isCheckoutDisabled(true);
//                        disableButtons(true);
//
//                    }
//
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.length() < 7) {

                    int qty = Integer.parseInt(s.toString());
                    product_quantity.removeTextChangedListener(this);
                    product_quantity.setText(String.valueOf(qty));
                    product_quantity.setSelection(product_quantity.getText().length());
                    product_quantity.addTextChangedListener(this);

                }
            }

            private void disableAllButtons(boolean isEnabled) {
//                buttonIncrease.setEnabled(!isEnabled);
//                buttonDecrease.setEnabled(!isEnabled);
                shoppingCartsActivity.isCheckoutDisabled();
            }

            private void disableButtons(boolean isEnabled) {
//                buttonDecrease.setEnabled(!isEnabled);
                shoppingCartsActivity.isCheckoutDisabled();
            }
        }

        private void updateTotalItemPrice(ShoppingCartsQueue shoppingCartsQueue, int quantity) {

            newQuantity = quantity;
            itemQueue = shoppingCartsQueue;

            double amount = quantity * shoppingCartsQueue.getPrice();
            product_amount.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", CommonFunctions.currencyFormatter(String.valueOf(amount)).concat(" Php")));

            mdb.updateItemCartsQueue(quantity, quantity * shoppingCartsQueue.getPrice(), shoppingCartsQueue.getItemCode(), shoppingCartsQueue.getPackageCode());
            shoppingCartsActivity.setShoppingSummaryUI();

            if (customerid != null) {

                if (product_quantity.getText().toString().length() > 0 && product_quantity.getText().toString().length() < 7) {

                    shoppingCartsActivity.createSession(calladdupdatecartsession);

                }

            }

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            if (position > -1) {

                itemQueue = mGridData.get(position);

                packageid = itemQueue.getPackageCode();

                deletedposition = position;

                switch (v.getId()) {

                    case R.id.deleteItem: {

                        try {

                            if (itemQueue.getItemDescription() != null) {

                                String message = "Are you sure you want to remove " + itemQueue.getItemDescription() + " (" + itemQueue.getPackageDescription() + ")" + " ?";

                                openDeleteConfirmationDialog(message, "Remove", "Cancel");

                            } else {

                                openDeleteConfirmationDialog("Are you sure you want to remove this item?", "Remove", "Cancel");

                            }

                        } catch (ArrayIndexOutOfBoundsException e) {

                            e.printStackTrace();
                        }

                        break;
                    }
                    case R.id.btn_my_shopping_cart_decrease: {

                        currentQuantity = Integer.parseInt(product_quantity.getText().toString());

                        int minQty = itemQueue.getMinimumOrderQTY() == 0 ? 1 : itemQueue.getMinimumOrderQTY();

                        if (currentQuantity > minQty) {

                            int increOrder = itemQueue.getIncrementalOrderQTY() == 0 ? 1 : itemQueue.getIncrementalOrderQTY();

                            newQuantity = currentQuantity - increOrder;

                            changeTotalPrice(newQuantity, position);
                        }

                        product_quantity.clearFocus();

                        break;
                    }
                    case R.id.btn_my_shopping_cart_increase: {

                        if (product_quantity.getText().toString().length() > 0 && product_quantity.getText().toString().length() < 7) {

                            currentQuantity = Integer.parseInt(product_quantity.getText().toString());

                            if (Integer.parseInt(product_quantity.getText().toString()) % itemQueue.getIncrementalOrderQTY() == 0) {

                                int increOrder = itemQueue.getIncrementalOrderQTY() == 0 ? 1 : itemQueue.getIncrementalOrderQTY();

                                newQuantity = currentQuantity + increOrder;

                                changeTotalPrice(newQuantity, position);

                            }

                        } else {

                            changeTotalPrice(itemQueue.getIncrementalOrderQTY(), position);

                            notifyItemChanged(position);

                        }

                        product_quantity.clearFocus();

                        break;
                    }
                    case R.id.packageOptionLayout: {

                        //hide keyboard
                        shoppingCartsActivity.hideSoftKeyboard();

                        product_quantity.clearFocus();

                        break;
                    }
                    case R.id.my_shopping_cart_product_photo: {

                        FullScreenActivity.start(getContext(), itemQueue.getItemPicUrl());

                        break;
                    }

                }
            }
        }
    }

    private void changeTotalPrice(int newVal, int position) {

        updateCartQueue(newVal, newVal * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

        notifyItemChanged(position);

        if (customerid != null) {

            shoppingCartsActivity.createSession(calladdupdatecartsession);

        }

    }

    private void updateCartQueue(int quantity, double actualprice, String itemcode, String packageid) {

        mdb.updateItemCartsQueue(quantity, actualprice, itemcode, packageid);

        setCartData(mdb.getAllShoppingCartsQueue());

        shoppingCartsActivity.setShoppingSummaryUI();

    }

    private void openDeleteConfirmationDialog(String message, String positivetext, String neutraltext) {

        new MaterialDialog.Builder(getContext())
                .content(message)
                .cancelable(false)
                .positiveText(positivetext)
                .negativeText(neutraltext)
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteproduct();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    private void deleteproduct() {

        if (customerid != null) {

            //api session
            shoppingCartsActivity.createSession(calldeleteitemsession);

        } else {

            removedAt(deletedposition);

            mdb.deleteItemCartsQueue(itemQueue.getItemCode(), itemQueue.getPackageCode());

            shoppingCartsActivity.updateList(mdb.getAllShoppingCartsQueue());

            shoppingCartsActivity.setShoppingSummaryUI();

        }
    }

    private final Callback<GetSessionResponse> calladdupdatecartsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {

            ResponseBody errorBody = response.errorBody();

            if (errorBody == null) {

                if (response.body().getStatus().equals("000")) {

                    sessionid = response.body().getSession();

                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);

                    if (itemQueue.getItemCode() != null && !itemQueue.getItemCode().isEmpty()) {

                        updateShoppingCartsQueue(callCartSession, itemQueue, newQuantity);

                    }

                } else {

                    updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

                }

            } else {

                updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

                shoppingCartsActivity.openErrorDialog("Something went wrong");

            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {

            updateCartQueue(currentQuantity, currentQuantity * itemQueue.getPrice(), itemQueue.getItemCode(), itemQueue.getPackageCode());

            shoppingCartsActivity.showError(shoppingCartsActivity.getString(R.string.generic_internet_error_message));

        }
    };

}
