package com.ultramega.shop.adapter.myshoppingcart;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.myshoppingcart.checkout.pickup.SetUpPickUpBranchFragment;
import com.ultramega.shop.pojo.Branches;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BranchesRecyclerAdapter extends RecyclerView.Adapter<BranchesRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Branches> mGridData;
    private List<Branches> oldData;
    private List<Branches> newData;
    private double latitude;
    private double longitude;
    private SetUpPickUpBranchFragment fragment;
    private int currentpos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView branch_name;
        private final TextView branch_address;
        private final TextView branch_distance;
        private final ImageView branch_image;
        private final TextView branch_storeOpen;
        private final TextView branch_storeClose;

        public MyViewHolder(View itemView) {
            super(itemView);
            branch_name = itemView.findViewById(R.id.pick_up_branch_name);
            branch_address = itemView.findViewById(R.id.pick_up_branch_address);
            branch_distance = itemView.findViewById(R.id.pick_up_branch_distance);
            branch_image = itemView.findViewById(R.id.pick_up_branch_image);
            branch_storeOpen = itemView.findViewById(R.id.pick_up_branch_storeopen);
            branch_storeClose = itemView.findViewById(R.id.pick_up_branch_storeclose);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentpos = getAdapterPosition();
            Branches item = mGridData.get(currentpos);
            fragment.selectBranchMarker(item);
            notifyDataSetChanged();
        }
    }

    public BranchesRecyclerAdapter(Context context, List<Branches> data, double latitude, double longitude, SetUpPickUpBranchFragment fragment) {
        mGridData = new ArrayList<>();
        oldData = new ArrayList<>();
        this.mContext = context;
        mGridData = data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fragment = fragment;
    }



    private Context getContext() {
        return mContext;
    }

    public void setPickUpBranchesData(List<Branches> data) {
        oldData = mGridData;
        mGridData.clear();
        mGridData.addAll(data);
        notifyDataSetChanged();
    }

    //Do search
    public void filter(final String searchText) {

        newData = new ArrayList<>();

        //dispatch searching to a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                String charString = searchText.trim().toLowerCase();

                //clear filter list
                newData.clear();

                //if no search apply filter list to original list
                if (charString.length() == 0) {
                    mGridData = oldData;
                } else {

                    //Iterate in the original list to find matches
                    for (Branches branches : mGridData) {
                        if (branches.getBranchName().trim().toLowerCase().contains(charString) ||
                                branches.getStreetAddress().trim().toLowerCase().contains(charString) ||
                                branches.getCity().trim().toLowerCase().contains(charString) ||
                                branches.getProvince().trim().toLowerCase().contains(charString) ||
                                branches.getCountry().trim().toLowerCase().contains(charString)
                        ) {
                            newData.add(branches);
                        }
                    }
                    mGridData = newData;
                }

                //Set on UI Thread
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();


    }

    @Override
    public BranchesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pick_up_maps_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BranchesRecyclerAdapter.MyViewHolder holder, int position) {
        Branches item = mGridData.get(position);

        Log.d("okhttp","ITEMS : "+new Gson().toJson(item));

        float[] results = new float[1];
        holder.branch_name.setText(item.getBranchName());
        holder.branch_address.setText(item.getStreetAddress() + ", " + item.getCity() + ", " + item.getProvince() + ", " + item.getZip() + ", " + item.getCountry());
        holder.branch_distance.setText(CommonFunctions.currencyFormatter(String.valueOf(item.getCalculatedDistance())).concat(" km."));
        if (currentpos == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_E4E4E4));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.branch_storeOpen.setText("Open: "+CommonFunctions.parseTime(item.getStoreHoursStart()));
        holder.branch_storeClose.setText("Close: "+CommonFunctions.parseTime(item.getStoreHoursEnd()));

//        holder.itemView.setOnClickListener(onClickListener);
//        holder.itemView.setTag(item);
    }

//    private final View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Branches item = (Branches) v.getTag();
//            fragment.selectBranchMarker(item);
//        }
//    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }
}
