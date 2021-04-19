package com.ultramega.shop.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 23/09/2017.
 */

public class InterestRecyclerProfileInterestListAdapter extends RecyclerView.Adapter<InterestRecyclerProfileInterestListAdapter.MyViewHolder> {
    private final UltramegaShopUtilities mdb;
    private final Context mContext;

    private List<ConsumerBehaviourPattern> mGridData;
    private List<ConsumerBehaviourPattern> listBehaviourPattern;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView categories_namelist;
        private final RelativeLayout categoriesLayoutlist;

        public MyViewHolder(View itemView) {
            super(itemView);
            categories_namelist = (TextView) itemView.findViewById(R.id.categoriesNameList);
            categoriesLayoutlist = (RelativeLayout) itemView.findViewById(R.id.categoriesLayoutList);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_interest_list_profile, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InterestRecyclerProfileInterestListAdapter.MyViewHolder holder, int position) {
        ConsumerBehaviourPattern item = mGridData.get(position);
        holder.categories_namelist.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", item.getCategoryName().toLowerCase()));
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public InterestRecyclerProfileInterestListAdapter(Context context, List<ConsumerBehaviourPattern> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
        this.mdb = new UltramegaShopUtilities((getContext()));
        this.listBehaviourPattern = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

    public void setNotificationsData(List<ConsumerBehaviourPattern> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
        Log.d("antonhttp", new Gson().toJson(mGridData));
    }
}
