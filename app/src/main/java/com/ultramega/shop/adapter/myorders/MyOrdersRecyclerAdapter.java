package com.ultramega.shop.adapter.myorders;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ViewMyOrdersDetailsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.OrdersQueue;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<OrdersQueue> mGridData;
    private final String mTitle;
    private UltramegaShopUtilities mdb;
    private List<String> listmages;
    private String mUserType = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txvdatetime;
        private final TextView txvtxn;
        private final TextView txvpcs;
        private final TextView txvamount;
        private final TextView txvstatus;
        private final TextView txvIsExpired;

        //for single order
        private final LinearLayout singleorderlayout;
        private final ImageView sgl_photo;

        //for double orders
        private final LinearLayout doubleorderlayout;
        private final ImageView dbl_photo_one;
        private final ImageView dbl_photo_two;

        //for multiple orders
        private final LinearLayout multipleorderlayout;
        private final ImageView mpl_photo_one;
        private final ImageView mpl_photo_two;
        private final ImageView mpl_photo_three;
        private final ImageView mpl_photo_four;

        public MyViewHolder(View itemView) {
            super(itemView);

            txvdatetime = itemView.findViewById(R.id.txndatetime);
            txvtxn = itemView.findViewById(R.id.txvtxn);
            txvpcs = itemView.findViewById(R.id.txvpcs);
            txvamount = itemView.findViewById(R.id.txvamount);
            txvstatus = itemView.findViewById(R.id.txvstatus);
            txvIsExpired = itemView.findViewById(R.id.txvIsExpired);

            //single order
            singleorderlayout = itemView.findViewById(R.id.singleorderlayout);
            singleorderlayout.setVisibility(View.GONE);
            sgl_photo = itemView.findViewById(R.id.single_photo_one);
            //double order
            doubleorderlayout = itemView.findViewById(R.id.doubleorderlayout);
            doubleorderlayout.setVisibility(View.GONE);
            dbl_photo_one = itemView.findViewById(R.id.dbl_photo_one);
            dbl_photo_two = itemView.findViewById(R.id.dbl_photo_two);
            //multiple order
            multipleorderlayout = itemView.findViewById(R.id.multipleorderlayout);
            multipleorderlayout.setVisibility(View.GONE);
            mpl_photo_one = itemView.findViewById(R.id.multiple_photo_one);
            mpl_photo_two = itemView.findViewById(R.id.multiple_photo_two);
            mpl_photo_three = itemView.findViewById(R.id.multiple_photo_three);
            mpl_photo_four = itemView.findViewById(R.id.multiple_photo_four);
        }
    }

    public MyOrdersRecyclerAdapter(Context context, List<OrdersQueue> data, String mTitle, String usertype) {
        this.mContext = context;
        mGridData = data;
        this.mTitle = (mTitle.equals("For Process".toUpperCase()) ? "Paid".toUpperCase() : mTitle.toUpperCase()).replaceAll("\\s+", "");
        this.mdb = new UltramegaShopUtilities(getContext());
        this.listmages = new ArrayList<>();
        mUserType = usertype;
    }

    private Context getContext() {
        return mContext;
    }

    public void setPendingData(List<OrdersQueue> data) {
        int startPos = mGridData.size() + 1;
        mGridData.clear();
        mGridData.addAll(data);
        notifyItemRangeInserted(startPos, data.size());
    }

    public void clear() {
        int startPos = mGridData.size();
        mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public MyOrdersRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_orders_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrdersRecyclerAdapter.MyViewHolder holder, int position) {
        OrdersQueue item = mGridData.get(position);

        Log.d("ronel","ITEM: "+position);

        String status = item.getStatus().replaceAll("\\s+", "").toUpperCase();
        listmages = mdb.getOrderImages(mTitle, item.getOrderTxnID());

        //=====================
        //SETUP ORDER STATUS
        //=====================
        if (mUserType.equals("CONSUMER")) {
            setUpStatus(holder, status);
        } else if (mUserType.equals("WHOLESALER")) {
            setUpWholesalerStatus(holder, item.getStatus());
        }

        //=====================
        //SETUP ITEM FIELDS
        //=====================
        setUpItemFields(holder, item);

        //=====================
        //SETUP IMAGE LAYOUT
        //=====================
        setUpImageLayout(holder);

        //IF EXPIRED
        if (item.getIsExpiredOrder() == 1) {
            holder.txvIsExpired.setVisibility(View.VISIBLE);
            holder.txvIsExpired.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", "EXPIRED"));
        } else {
            holder.txvIsExpired.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);
    }

    private void setUpItemFields(MyOrdersRecyclerAdapter.MyViewHolder holder, OrdersQueue item) {
        holder.txvdatetime.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseOrderDate(item.getCompletedDateTime())));
        holder.txvtxn.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", item.getOrderTxnID()));
        String items = item.getTotalItems() > 1 ? " ITEMS" : " ITEM";
        holder.txvpcs.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", String.valueOf(item.getTotalItems()).concat(items)));
        holder.txvamount.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.currencyFormatter(String.valueOf(item.getTotalOrderAmount()))));
    }

    private void setUpWholesalerStatus(MyOrdersRecyclerAdapter.MyViewHolder holder, String status) {
        String ordertitle = "";
        switch (status) {
            case "MODIFIED": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff9502));
                break;
            }
            case "DELETED": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
            case "NO STOCK": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
            case "FOR DELIVERY-PARTIAL": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_06F5C0));
                break;
            }
            case "FOR DELIVERY-COMPLETE": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_1c9f05));
                break;
            }
            case "RECEIVED BY CUSTOMER": {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_1c9f05));
                break;
            }
            case "ONPROCESS": {
                ordertitle = "ON PROCESS";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_498fe1));
                break;
            }
            default: {
                ordertitle = status;
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
        }

        holder.txvstatus.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", ordertitle));
    }

    private void setUpStatus(MyOrdersRecyclerAdapter.MyViewHolder holder, String status) {
        String ordertitle = "";
        switch (status) {
            case "PAID": {
                ordertitle = "FOR PROCESS";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_f7e71b));
                break;
            }
            case "ONHOLD": {
                ordertitle = "ON HOLD";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff00ff));
                break;
            }
            case "FORPAYMENT": {
                ordertitle = "FOR PAYMENT";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff9502));
                break;
            }
            case "FORPICKUP": {
                ordertitle = " FOR PICKUP";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_A2A2A2));
                break;
            }
            case "ONPROCESS": {
                ordertitle = "ON PROCESS";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_498fe1));
                break;
            }
            case "PENDING": {
                ordertitle = "PENDING";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_F83832));
                break;
            }
            case "UNASSIGNED": {
                ordertitle = "PENDING";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_F83832));
                break;
            }
            case "WAITING": {
                ordertitle = "PENDING";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_F83832));
                break;
            }
            case "ADDED": {
                ordertitle = "PENDING";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_F83832));
                break;
            }
            case "COMPLETED": {
                ordertitle = "COMPLETED";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_1c9f05));
                break;
            }
            case "CANCELLED": {
                ordertitle = "CANCELLED";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
            case "DECLINED": {
                ordertitle = "DECLINED";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
            case "ONDELIVERY": {
                ordertitle = "ON DELIVERY";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_06F5C0));
                break;
            }
            case "PARTIALPAY": {
                ordertitle = "PARTIAL PAID";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
            default: {
                ordertitle = "PROCESSING";
                holder.txvstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
                break;
            }
        }
        holder.txvstatus.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", ordertitle));
    }

    private void setUpImageLayout(MyOrdersRecyclerAdapter.MyViewHolder holder) {
        int total_items = listmages.size();
        if (total_items == 1) {
            holder.singleorderlayout.setVisibility(View.VISIBLE);
            holder.doubleorderlayout.setVisibility(View.GONE);
            holder.multipleorderlayout.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(listmages.get(0))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.sgl_photo);
        } else if (total_items == 2) {
            holder.singleorderlayout.setVisibility(View.GONE);
            holder.doubleorderlayout.setVisibility(View.VISIBLE);
            holder.multipleorderlayout.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(listmages.get(0))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.dbl_photo_one);
            Glide.with(mContext)
                    .load(listmages.get(1))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.dbl_photo_two);
        } else if (total_items == 3) {
            holder.singleorderlayout.setVisibility(View.GONE);
            holder.doubleorderlayout.setVisibility(View.VISIBLE);
            holder.multipleorderlayout.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(listmages.get(0))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.dbl_photo_one);
            Glide.with(mContext)
                    .load(listmages.get(1))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.dbl_photo_two);
        } else if (total_items > 3) {
            holder.singleorderlayout.setVisibility(View.GONE);
            holder.doubleorderlayout.setVisibility(View.GONE);
            holder.multipleorderlayout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(listmages.get(0))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.mpl_photo_one);
            Glide.with(mContext)
                    .load(listmages.get(1))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.mpl_photo_two);
            Glide.with(mContext)
                    .load(listmages.get(2))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.mpl_photo_three);
            Glide.with(mContext)
                    .load(listmages.get(3))
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.mpl_photo_four);
        } else if (total_items == 0) {
            holder.singleorderlayout.setVisibility(View.VISIBLE);
            holder.doubleorderlayout.setVisibility(View.GONE);
            holder.multipleorderlayout.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load("")
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .apply(new RequestOptions()
                            .override(250, 250)
                            .fitCenter())
                    .into(holder.sgl_photo);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrdersQueue item = (OrdersQueue) v.getTag();
            ViewMyOrdersDetailsActivity.start(getContext(), item, mTitle);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
