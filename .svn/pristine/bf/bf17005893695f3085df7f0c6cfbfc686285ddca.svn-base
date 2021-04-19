package com.ultramega.shop.adapter.shop;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.items.ViewItemsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 8/25/2017.
 */

public class SupplierRecyclerAdapter extends RecyclerView.Adapter<SupplierRecyclerAdapter.MyViewHolder> {
    private final Context mContext;
    private List<Supplier> mGridData;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView supplier_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            supplier_name = (TextView) itemView.findViewById(R.id.categoriesName);
        }
    }

    public SupplierRecyclerAdapter(Context context) {
        this.mContext = context;
        mGridData = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

    public void add(Supplier item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Supplier> mGridData) {
        for (Supplier item : mGridData) {
            add(item);
        }
    }

    public void setSupplierData(List<Supplier> mGridData) {
        this.mGridData.clear();
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_suppliers_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Supplier item = mGridData.get(position);

        if (item.getSupplierName() != null) {

            try {

                holder.supplier_name.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Medium.ttf", item.getSupplierName().toUpperCase()));

            } catch (StringIndexOutOfBoundsException e) {

                holder.supplier_name.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Medium.ttf", item.getSupplierName().toUpperCase()));
                e.printStackTrace();

            }

        }

        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);

    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Supplier item = (Supplier) v.getTag();
            ViewItemsActivity.startSupplierItems(getContext(), item);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private String capitalizeWord(String word) {
        String[] cap_word_arr = word.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : cap_word_arr) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

}
