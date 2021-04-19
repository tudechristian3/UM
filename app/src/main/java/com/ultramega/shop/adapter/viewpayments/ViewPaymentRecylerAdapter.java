package com.ultramega.shop.adapter.viewpayments;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.WalletReload;

import java.util.List;

/**
 * Created by User on 04/09/2017.
 */

public class ViewPaymentRecylerAdapter extends RecyclerView.Adapter<ViewPaymentRecylerAdapter.WalletViewHolder> {
    private Context mContext;
    private List<WalletReload> mGridData;

    private UltramegaShopUtilities mdb;
    private WalletReload walletReload;

    public ViewPaymentRecylerAdapter(Context viewContext, List<WalletReload> mGridData) {
        this.mContext = viewContext;
        this.mGridData = mGridData;
    }

    public class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //SUMMARY LAYOUT
        private final LinearLayout summarylayout;
        //PAYMENT SUMMARY
        private final ImageView imgstatus;
        private final TextView txvdate;
        private final TextView txvamount;
        private final TextView txvoption;
        private final ImageView imgdown;
        //PAYMENT DETAILS
        private final LinearLayout banklayout;
        private final LinearLayout cardlayout;
        private final TextView txvtxnno;
        private final TextView banktxnno;
        private final TextView txvbankname;
        private final TextView txvbankcode;
        private final TextView txvbankaccountname;
        private final TextView txvbankaccountnumber;
        private final TextView txvdepositslip;
        private final ImageView imgdepositslip;
        private final TextView cardtxnno;
        private final TextView txvdatetime;

        public WalletViewHolder(View itemView) {
            super(itemView);

            summarylayout = (LinearLayout) itemView.findViewById(R.id.summarylayout);
            //PAYMENT STATUS
            imgstatus = (ImageView) itemView.findViewById(R.id.imgstatus);
            txvdate = (TextView) itemView.findViewById(R.id.txvdate);
            txvamount = (TextView) itemView.findViewById(R.id.txvamount);
            txvoption = (TextView) itemView.findViewById(R.id.txvoption);
            imgdown = (ImageView) itemView.findViewById(R.id.imgdown);
            //PAYMENT DETAILS
            banklayout = (LinearLayout) itemView.findViewById(R.id.banklayout);
            banklayout.setVisibility(View.GONE);
            cardlayout = (LinearLayout) itemView.findViewById(R.id.cardlayout);
            cardlayout.setVisibility(View.GONE);
            txvtxnno = (TextView) itemView.findViewById(R.id.txvtxnno);
            banktxnno = (TextView) itemView.findViewById(R.id.banktxnno);
            txvbankname = (TextView) itemView.findViewById(R.id.txvbankname);
            txvbankcode = (TextView) itemView.findViewById(R.id.txvbankcode);
            txvbankaccountname = (TextView) itemView.findViewById(R.id.txvbankaccountname);
            txvbankaccountnumber = (TextView) itemView.findViewById(R.id.txvbankaccountnumber);
            txvdepositslip = (TextView) itemView.findViewById(R.id.txvdepositslip);
            imgdepositslip = (ImageView) itemView.findViewById(R.id.imgdepositslip);
            imgdepositslip.setOnClickListener(this);
            cardtxnno = (TextView) itemView.findViewById(R.id.cardtxnno);
            txvdatetime = (TextView) itemView.findViewById(R.id.txvdatetime);

            summarylayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            WalletReload walletReload = mGridData.get(position);
            switch (v.getId()) {
                case R.id.summarylayout: {
                    switch (walletReload.getPaymentOption()) {
                        case "BANK": {
                            if (banklayout.getVisibility() == View.GONE) {
                                banklayout.setVisibility(View.VISIBLE);
                                imgdown.setRotation(180);
                            } else {
                                banklayout.setVisibility(View.GONE);
                                imgdown.setRotation(360);
                            }
                            txvtxnno.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", walletReload.getPaymentTxnNo()));
                            txvdatetime.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "depositdatetime")));
                            banktxnno.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "banktxnnumber")));
                            txvbankname.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankname")));
                            txvbankcode.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankcode")));
                            txvbankaccountname.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankaccountname")));
                            txvbankaccountnumber.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.parseXML(walletReload.getPaymentDetails(), "bankaccountnumber")));
                            Glide.with(mContext)
                                    .load(CommonFunctions.parseXML(walletReload.getPaymentDetails(), "depositslipurl"))
                                    .apply(RequestOptions.centerInsideTransform()
                                            .error(R.drawable.ic_um_default_products)
                                            .placeholder(R.drawable.ic_um_default_products)
                                            .fitCenter())
                                    .into(imgdepositslip);
                            break;
                        }
                        case "CARD": {
                            if (cardlayout.getVisibility() == View.GONE) {
                                cardlayout.setVisibility(View.VISIBLE);
                                imgdown.setRotation(180);
                            } else {
                                cardlayout.setVisibility(View.GONE);
                                imgdown.setRotation(360);
                            }
                            cardtxnno.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", walletReload.getPaymentTxnNo()));
                            break;
                        }
                    }
                    break;
                }
                case R.id.imgdepositslip: {
                    FullScreenActivity.start(getContext(), CommonFunctions.parseXML(walletReload.getPaymentDetails(), "depositslipurl"));
                    break;
                }
            }
        }
    }

    @Override
    public ViewPaymentRecylerAdapter.WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_wallet_reload_items, parent, false);
        return new WalletViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WalletViewHolder holder, int position) {
        WalletReload walletReload = mGridData.get(position);

        int imageRes = 0;
        switch (walletReload.getStatus()) {
            case "COMPLETED": {
                imageRes = R.drawable.ic_wallet_reload_approved;
                break;
            }
            case "FORAPPROVAL": {
                imageRes = R.drawable.ic_wallet_reload_on_process;
                break;
            }
            case "DECLINED": {
                imageRes = R.drawable.ic_wallet_reload_cancelled;
                break;
            }
            case "EXPIRED": {
                imageRes = R.drawable.ic_expired;
                break;
            }
        }
        Glide.with(getContext())
                .load(imageRes)
                .apply(new RequestOptions()
                        .override(36, 36)
                        .fitCenter())
                .into(holder.imgstatus);

        holder.txvdate.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.convertDateWalletReload(walletReload.getDateTimeCompleted()).toUpperCase()));
        holder.txvamount.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", CommonFunctions.currencyFormatter(walletReload.getAmountPaid())));
        holder.txvoption.setText(CommonFunctions.setTypeFace(getContext(), "fonts/Roboto-Regular.ttf", walletReload.getPaymentOption()));
    }


    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private Context getContext() {
        return mContext;
    }

    public void setListData(List<WalletReload> data) {
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

}
