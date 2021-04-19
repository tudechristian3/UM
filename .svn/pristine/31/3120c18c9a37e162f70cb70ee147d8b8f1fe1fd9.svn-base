package com.ultramega.shop.adapter.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerAddress;

import java.util.ArrayList;
import java.util.List;

public class MyAddressesRecyclerAdapter extends RecyclerView.Adapter<MyAddressesRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ConsumerAddress> mGridData = new ArrayList<>();

    private UltramegaShopUtilities mDb;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView mobile_number;
        public final TextView detailed_address;
        public final TextView barangay_city;
        public final TextView region_provice_zip;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.editAddressName);
            mobile_number = (TextView) itemView.findViewById(R.id.editAddressMobileNumber);
            detailed_address = (TextView) itemView.findViewById(R.id.editAddressDetailedAddress);
            barangay_city = (TextView) itemView.findViewById(R.id.editAddressBarangayCity);
            region_provice_zip = (TextView) itemView.findViewById(R.id.editAddressRegionProviceZip);

        }
    }

    public MyAddressesRecyclerAdapter(Context context, List<ConsumerAddress> mGridData){
        this.mContext = context;
        this.mGridData = mGridData;
        this.mDb = new UltramegaShopUtilities(getContext());
    }

    private Context getContext(){
        return mContext;
    }

    public void setAddressData(List<ConsumerAddress> mGridData){
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


    @Override
    public MyAddressesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_edit_address_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyAddressesRecyclerAdapter.MyViewHolder holder, int position) {
        ConsumerAddress item = mGridData.get(position);
        String detailedaddress = item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry();
        holder.detailed_address.setText(detailedaddress);
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
