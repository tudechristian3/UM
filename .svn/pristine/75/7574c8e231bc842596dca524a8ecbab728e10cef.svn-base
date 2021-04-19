package com.ultramega.shop.adapter.myshoppingcart.checkout;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.SelectVariationActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.ItemSKU;

import java.util.List;

/**
 * Created by User-PC on 7/14/2017.
 */

public class SelectVariationPackageRecyclerAdapter extends RecyclerView.Adapter<SelectVariationPackageRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ItemSKU> mGridData;
    private int currentpos = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView packagename;
        private final RelativeLayout packagelayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            packagename = (TextView) itemView.findViewById(R.id.packagename);
            packagelayout = (RelativeLayout) itemView.findViewById(R.id.packagelayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentpos = getAdapterPosition();
            if (currentpos != -1) {
                ItemSKU item = mGridData.get(currentpos);
                ((SelectVariationActivity) mContext).setupDataDisplay(item);
                notifyDataSetChanged();
            }
        }
    }

    public SelectVariationPackageRecyclerAdapter(Context context, List<ItemSKU> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
    }

    private Context getContext() {
        return mContext;
    }

    public void setVariationPackageData(List<ItemSKU> mGridData) {
        this.mGridData.clear();
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public SelectVariationPackageRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_package_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectVariationPackageRecyclerAdapter.MyViewHolder holder, int position) {
        ItemSKU itemsku = mGridData.get(position);
        holder.packagename.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", itemsku.getPackageDescription()));
        holder.packagename.setTextColor(ContextCompat.getColor(mContext, R.color.color_9B9B9B));
        holder.packagelayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.package_border));
        if (currentpos == -1) {
            if (position == 0) {
                holder.packagelayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_2196F3));
                holder.packagename.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            }
        } else {
            if (currentpos == position) {
                holder.packagelayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_2196F3));
                holder.packagename.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            } else {
                holder.packagelayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.package_border));
                holder.packagename.setTextColor(ContextCompat.getColor(mContext, R.color.color_9B9B9B));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }


}
