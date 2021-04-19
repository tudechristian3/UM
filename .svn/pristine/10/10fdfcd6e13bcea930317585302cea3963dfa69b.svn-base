package com.ultramega.shop.adapter.myorders;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ViewMyOrdersDetailsActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrdersQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.CancelIndividualOrderResponse;
import com.ultramega.shop.responses.GetOrdersQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.CustomTypefaceSpan;

import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersItemDetailsRecyclerAdapter extends RecyclerView.Adapter<MyOrdersItemDetailsRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<OrdersQueue> mGridData;
    private final String mTitle;
    private String mUserType;
    private OrdersQueue orderedItem;
    private final UltramegaShopUtilities mdb;

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String userid = "";
    private String ordertxnid = "";
    private String itemid = "";
    private String sku = "";
    private String packageid = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView product_name;
        private TextView product_quantity;
        //private TextView product_amount;
        private ImageView product_photo;
        private Button btnCancel;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.my_order_details_item_name);
            product_quantity = itemView.findViewById(R.id.my_order_details_item_quantity);
            //product_amount = (TextView) itemView.findViewById(R.id.my_order_details_item_amount);
            product_photo = itemView.findViewById(R.id.my_order_details_item_photo);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(this);
            btnCancel.setVisibility(View.GONE);
            if (mTitle.equals("ONPROCESS")
                    || mTitle.equals("CANCELLED")
                    || mTitle.equals("COMPLETED")
                    || mTitle.equals("ONDELIVERY")
                    || mTitle.equals("PAID")
                    || mTitle.equals("FORPICKUP")
                    ) {
                //btnCancel.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != -1) {
                orderedItem = null;
                orderedItem = mGridData.get(position);
                switch (v.getId()) {
                    case R.id.btnCancel: {
                        String message = "Are you sure you want to cancel " + orderedItem.getItemCode() + " (" + orderedItem.getPackageCode() + ")" + "?";
                        if (mGridData.size() > 1) {
                            openDeleteOrderConfirmationDialog(message, "Proceed", "No");
                        }
                        //else{                            showCancelOrder();}

                        break;
                    }
                }
            }
        }
    }

    public MyOrdersItemDetailsRecyclerAdapter(Context context, List<OrdersQueue> mGridData, String mTitle, String mUserType) {
        this.mContext = context;
        this.mGridData = mGridData;
        this.mTitle = mTitle;
        this.mUserType = mUserType;
        this.mdb = new UltramegaShopUtilities(getContext());
        imei = CommonFunctions.getIMEI(getContext());
        usertype = ((ViewMyOrdersDetailsActivity) getContext()).getCurrentUserType(getContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
        }
    }

    private Context getContext() {
        return mContext;
    }

    public void setOrderItemDetailsData(List<OrdersQueue> mGridData) {
        this.mGridData.clear();
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyOrdersItemDetailsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_orders_details_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrdersItemDetailsRecyclerAdapter.MyViewHolder holder, int position) {
        OrdersQueue item = mGridData.get(position);
        Log.d("roneldayanan","ONBINDL "+ new Gson().toJson(item));
        try{

            //===============
            //SETUP QUANTITY
            //===============
            setUpOrderDetails(holder, item, mUserType);
            if (!item.getPackSize().equals(".") || item.getPackSize() != null) {
                holder.product_name.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Bold.ttf", item.getItemDescription().concat(" " + item.getPackSize()) + " (" + item.getPackageDescription() + ")"));
            } else {
                holder.product_name.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Bold.ttf", item.getItemDescription() + " (" + item.getPackageDescription() + ")"));
            }

            Glide.with(getContext())
                    .load(item.getItemPicUrl())
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(holder.product_photo);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private void setUpOrderDetails(MyOrdersItemDetailsRecyclerAdapter.MyViewHolder holder, OrdersQueue item, String usertype) {

        SpannableStringBuilder ssb = new SpannableStringBuilder("QTY:");
        applyFontStyle(ssb, 0, ssb.length(), "fonts/ElliotSans-Regular.ttf");
        applyColor(ssb, 0, ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));

        Log.d("roneldayanan","item.getQuantity() "+ item.getQuantity());
        Log.d("roneldayanan","item.getQuantityServed() "+ item.getQuantityServed());

        if (item.getQuantity() == item.getQuantityServed()) {
            Log.d("roneldayanan","item.getQuantity() == item.getQuantityServed()");
            ssb.append(" ");
            String qty = String.valueOf(item.getQuantity()).concat(item.getQuantity() > 1 ? " Items" : " Item");
            ssb.append(qty);
            applyFontStyle(ssb, ssb.length() - qty.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - qty.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));

        }else if(item.getQuantity() != item.getQuantityServed() && usertype.equals("WHOLESALER")){
            ssb.append(" ");
            String qty = String.valueOf(item.getQuantity()).concat(item.getQuantity() > 1 ? " Items" : " Item");
            ssb.append(qty);
            applyFontStyle(ssb, ssb.length() - qty.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - qty.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));

        }else {
            Log.d("roneldayanan","item.getQuantity() != item.getQuantityServed()");
            ssb.append(" ");
            String qty = String.valueOf(item.getQuantity()).concat(item.getQuantity() > 1 ? " Items" : " Item");
            ssb.append(qty);
            applyFontStyle(ssb, ssb.length() - qty.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - qty.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            ssb.setSpan(strikethroughSpan,
                    ssb.length() - qty.length(),
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.append(" ");
            String qtyServed = String.valueOf(item.getQuantityServed()).concat(item.getQuantityServed() > 1 ? " Items" : " Item");
            ssb.append(qtyServed);
            applyFontStyle(ssb, ssb.length() - qtyServed.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - qtyServed.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));

        }

        ssb.append(" ");
        Log.d("roneldayanan","item.getTotalPackageAmount() "+ item.getTotalPackageAmount());
        String amount = "";
        if (usertype.equals("CONSUMER")) {
            amount = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getTotalPackageAmount())));
        }else if(usertype.equals("WHOLESALER") && item.getQuantity() != item.getQuantityServed()){
            amount = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getTotalPackageAmount() * item.getQuantity())));
        }else if (usertype.equals("WHOLESALER")&& item.getQuantity() == item.getQuantityServed()) {
            amount = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getTotalPackageAmount() * item.getQuantityServed())));
        }

        ssb.append(amount);
        applyFontStyle(ssb, ssb.length() - amount.length(), ssb.length(), "fonts/ElliotSans-Bold.ttf");
        applyColor(ssb, ssb.length() - amount.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.colorTxtProductAmount));

        holder.product_quantity.setText(ssb, TextView.BufferType.EDITABLE);

    }

    private void applyFontStyle(SpannableStringBuilder ssb, int start, int end, String font) {
        Typeface newFont = Typeface.createFromAsset(getContext().getAssets(), font);
        ssb.setSpan(new CustomTypefaceSpan("", newFont), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void applyColor(SpannableStringBuilder ssb, int start, int end, int color) {
        // Create a span that will make the text red
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                color);
        // Apply the color span
        ssb.setSpan(
                colorSpan,            // the span to add
                start,                // the start of the span (inclusive)
                end,                  // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder
        // SPAN_EXCLUSIVE_EXCLUSIVE means to not extend the span when additional
        // text is added in later
    }

//    private void showCancelOrder() {
//        new MaterialDialog.Builder(mContext)
//                .content("You are about to cancel last item from this order. Cancelling this cancels the whole order. Proceed?")
//                .positiveText("PROCEED")
//                .negativeText("NO")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        ((ViewMyOrdersDetailsActivity) getContext()).createSession(((ViewMyOrdersDetailsActivity) getContext()).callCancelOrderSession);
//                    }
//                })
//                .show();
//    }

    private void openDeleteOrderConfirmationDialog(String message, String positivetext, String neutraltext) {

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
                        if (customerid != null) {
                            ((ViewMyOrdersDetailsActivity) getContext()).createSession(callcancelindividualsession);
                        } else {
                            ((ViewMyOrdersDetailsActivity) getContext()).openLoginDialog("You are required to login for you to cancel an order");
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    public void cancelindividualorder() {
        if (customerid != null) {
            ((BaseActivity) mContext).progressDialog();
            ((ViewMyOrdersDetailsActivity) getContext()).createSession(callcancelindividualsession);
        } else {
            ((ViewMyOrdersDetailsActivity) getContext()).openLoginDialog("You are required to login for you to cancel an order");
        }
    }

    private final Callback<GetSessionResponse> callcancelindividualsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    ordertxnid = orderedItem.getOrderTxnID();
                    itemid = orderedItem.getItemCode();
                    sku = orderedItem.getCatClass();
                    packageid = orderedItem.getPackageCode();
                    cancelindividualorder(callindividualsession);
                } else {
                    ((BaseActivity) mContext).hideProgressDialog();
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            Log.d("antonhttp", t.toString());
            ((BaseActivity) mContext).hideProgressDialog();
        }
    };

    private void cancelindividualorder(Callback<CancelIndividualOrderResponse> cancelIndividualOrderCallback) {
        Call<CancelIndividualOrderResponse> cancelindividual = RetrofitBuild.cancelIndividualorderService(getContext())
                .cancelIndividualOrderCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        ordertxnid,
                        itemid,
                        userid,
                        sku,
                        packageid);
        cancelindividual.enqueue(cancelIndividualOrderCallback);
    }

    private final Callback<CancelIndividualOrderResponse> callindividualsession = new Callback<CancelIndividualOrderResponse>() {

        @Override
        public void onResponse(Call<CancelIndividualOrderResponse> call, Response<CancelIndividualOrderResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    ((ViewMyOrdersDetailsActivity) getContext()).createSession(callsession);

                } else {
                    ((BaseActivity) mContext).hideProgressDialog();
                }
            }
        }

        @Override
        public void onFailure(Call<CancelIndividualOrderResponse> call, Throwable t) {
            ((BaseActivity) mContext).hideProgressDialog();
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
                    getOrdersQueue(getOrdersQueueSession);
                } else {
                    ((BaseActivity) mContext).hideProgressDialog();
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            Log.d("antonhttp", t.toString());
            ((BaseActivity) mContext).hideProgressDialog();
        }
    };

    private void getOrdersQueue(Callback<GetOrdersQueueResponse> getOrdersQueueCallback) {
        Log.d("roneldayanan","GetORDERS QUEUE 1");
        String limit = "0";
        Call<GetOrdersQueueResponse> fetchitems = RetrofitBuild.getOrdersQueueService(getContext())
                .getOrdersQueueCall(
                        imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        limit,
                        userid,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH) + 1);
        fetchitems.enqueue(getOrdersQueueCallback);
    }

    private final Callback<GetOrdersQueueResponse> getOrdersQueueSession = new Callback<GetOrdersQueueResponse>() {

        @Override
        public void onResponse(Call<GetOrdersQueueResponse> call, Response<GetOrdersQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();

            if (errorBody == null) {
                ((BaseActivity) mContext).hideProgressDialog();
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                    List<OrdersQueue> orderitems = response.body().getOrdersQueue();
                    for (OrdersQueue order : orderitems) {
                        //======================================
                        // VALIDATE DATA PASSED FROM THE SERVER
                        //======================================
                        if (!(order.getStatus().matches(".*\\d.*") || order.getStatus().equals("CANCELLED") || order.getStatus().equals("COMPLETED"))) {
                            mdb.insertOrdersQueue(order, usertype);
                        }
                    }
                    if (mdb.getOrderDetailsForTxnId(mTitle, ordertxnid).size() > 0) {
                        Log.d("antonhttp", new Gson().toJson(mdb.getOrderDetailsForTxnId(mTitle, ordertxnid)));
                        ((ViewMyOrdersDetailsActivity) getContext()).initData(mdb.getOrderDetailsForTxnId(mTitle, ordertxnid).get(0));
                    }

                    setOrderItemDetailsData(mdb.getOrderDetailsForTxnId(orderedItem.getStatus(), orderedItem.getOrderTxnID()));
                }
            }

        }

        @Override
        public void onFailure(Call<GetOrdersQueueResponse> call, Throwable t) {
            ((BaseActivity) mContext).hideProgressDialog();
        }
    };
}
