package com.ultramega.shop.adapter.myorders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.OrderPayments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 2/27/2018.
 */

public class OrderPaymentsRecyclerViewAdapter extends RecyclerView.Adapter<OrderPaymentsRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<OrderPayments> mGridData;

    public OrderPaymentsRecyclerViewAdapter(Context context) {
        mContext = context;
        mGridData = new ArrayList<>();
    }

    public void setPaymentsData(final List<OrderPayments> mGridData) {
        int startPos = this.mGridData.size() + 1;
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        notifyItemRangeInserted(startPos, mGridData.size());
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    @Override
    public OrderPaymentsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_payment_order_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderPaymentsRecyclerViewAdapter.MyViewHolder holder, int position) {
        if (position > -1) {
            OrderPayments payment = mGridData.get(position);
            holder.txv_payment_txn_number.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", "PAYMENT TXN# " + payment.getPaymentTxnNo()));
            holder.txv_amount_paid.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", CommonFunctions.currencyFormatter(String.valueOf(payment.getAmountPaid()))));
            holder.txv_payment_option.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", payment.getPaymentOption()));
        }
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txv_payment_txn_number;
        private TextView txv_payment_option;
//        private TextView txv_total_amount;
        private TextView txv_amount_paid;

        public MyViewHolder(View itemView) {
            super(itemView);

            txv_payment_txn_number = (TextView) itemView.findViewById(R.id.txv_payment_txn_number);
            txv_payment_option = (TextView) itemView.findViewById(R.id.txv_payment_option);
//            txv_total_amount = (TextView) itemView.findViewById(R.id.txv_total_amount);
            txv_amount_paid = (TextView) itemView.findViewById(R.id.txv_amount_paid);

        }
    }
}
