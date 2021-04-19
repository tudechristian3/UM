package com.ultramega.shop.adapter.myshoppingcart;


import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.SelectedAddress;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerAddress;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class SelectOrderDeliveryAddressRecyclerAdapter extends RecyclerView.Adapter<SelectOrderDeliveryAddressRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ConsumerAddress> mGridData = new ArrayList<>();
    private final UltramegaShopUtilities mdb;
    private int currentpos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        private final TextView name;
//        private final TextView mobile_number;
        private final TextView address;

        public MyViewHolder(View itemView) {
            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.name);
//            mobile_number = (TextView) itemView.findViewById(R.id.mobilenumber);
            address = (TextView) itemView.findViewById(R.id.address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentpos = getAdapterPosition();
            if (currentpos > -1) {
                notifyDataSetChanged();

                ConsumerAddress item = mGridData.get(currentpos);
                String detailedaddress = item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry();

                SelectedAddress selected = new SelectedAddress();
                selected.setDetailedAddress(detailedaddress);
                selected.setLatitude(item.getLatitude());
                selected.setLongitude(item.getLongitude());

                String selectedaddress = new Gson().toJson(selected);
                UserPreferenceManager.removePreference(getContext(), "SelectedAddress");
                UserPreferenceManager.saveStringPreference(getContext(), "SelectedAddress", selectedaddress);
            }
        }
    }

    public SelectOrderDeliveryAddressRecyclerAdapter(Context context, List<ConsumerAddress> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
        this.mdb = new UltramegaShopUtilities(getContext());
        UserPreferenceManager.removePreference(getContext(), "SelectedAddress");
    }

    private Context getContext() {
        return mContext;
    }

    public void setSelectOrderDeliveryAddressData(List<ConsumerAddress> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public SelectOrderDeliveryAddressRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_select_order_delivery_address_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectOrderDeliveryAddressRecyclerAdapter.MyViewHolder holder, int position) {
        ConsumerAddress item = mGridData.get(position);
        String detailedaddress = item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry();
        holder.address.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Medium.ttf", detailedaddress));

        if (currentpos == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_EB1C24));
            holder.address.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.address.setTextColor(ContextCompat.getColor(getContext(), R.color.color_212121));
        }
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
