package com.ultramega.shop.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.InterestActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class InterestRecyclerAdapter extends RecyclerView.Adapter<InterestRecyclerAdapter.MyViewHolder> {
    private final UltramegaShopUtilities mdb;
    private final Context mContext;

    private List<Category> mGridData = new ArrayList<>();
    private List<ConsumerBehaviourPattern> listBehaviourPattern;

    private SparseBooleanArray selectedItems;
    private int currentpos = -1;

    private boolean isInterestActivity = false;

    private String userType;

    public InterestRecyclerAdapter(Context context, String usertype) {
        this.mContext = context;
        this.mdb = new UltramegaShopUtilities(getContext());
        this.listBehaviourPattern = new ArrayList<>();
        isInterestActivity = true;
        userType = usertype;
    }

    public InterestRecyclerAdapter(Context context, List<Category> mCategories, List<ConsumerBehaviourPattern> mPatterns, String usertype) {
        this.mContext = context;
        this.mGridData = mCategories;
        this.mdb = new UltramegaShopUtilities(getContext());
        this.listBehaviourPattern = new ArrayList<>();
        this.listBehaviourPattern = mPatterns;
        isInterestActivity = false;
        userType = usertype;
    }

    private Context getContext() {
        return mContext;
    }

    public void setCategoriesData(List<Category> mGridData) {
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
    public InterestRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_interest_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InterestRecyclerAdapter.MyViewHolder holder, int position) {


        if (position > -1) {

            Category item = mGridData.get(position);

            if (item.getDescription() != null) {
                holder.categories_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", item.getDescription().toLowerCase()));
            }

            Glide.with(getContext())
                    .load(R.drawable.ic_circle_interest)
                    .into(holder.image);

            if (containsCategory(listBehaviourPattern, item.getCategory())) {
                Glide.with(getContext())
                        .load(R.drawable.ic_circle_interest_click)
                        .into(holder.image);
                holder.categoriesLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.package_border_click));
                holder.categories_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            } else {
                Glide.with(getContext())
                        .load(R.drawable.ic_circle_interest)
                        .into(holder.image);
                holder.categoriesLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.package_border));
                holder.categories_name.setTextColor(ContextCompat.getColor(mContext, R.color.color_9B9B9B));
            }

        }

    }

    private boolean containsCategory(List<ConsumerBehaviourPattern> consumerBehaviourPattern, String category) {
        for (ConsumerBehaviourPattern consumerPattern : consumerBehaviourPattern) {
            if (consumerPattern.getCategoryID().equals(category)) {
                return true;
            }
        }
        return false;
    }

    public void insertCategories() {
        if (mdb != null) {
            if(userType.equals("CONSUMER")){
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_BEHAVIOUR_PATTERN);
            } else if(userType.equals("WHOLESALER")){
                mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_BEHAVIOUR_PATTERN);
            }

            if (listBehaviourPattern.size() > 0) {
                for (ConsumerBehaviourPattern consumerPattern : listBehaviourPattern) {

                    if(userType.equals("CONSUMER")){
                        mdb.insertConsumerBehaviourPattern(consumerPattern);
                    } else if(userType.equals("WHOLESALER")){
                        mdb.insertWholesalerBehaviourPattern(consumerPattern);
                    }

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView categories_name;
        private final LinearLayout categoriesLayout;
        private final ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoriesLayout = (LinearLayout) itemView.findViewById(R.id.categoriesLayout);
            categories_name = (TextView) itemView.findViewById(R.id.categoriesName);
            categories_name.setTextColor(ContextCompat.getColor(mContext, R.color.color_9B9B9B));
            categoriesLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.package_border));
            image = (ImageView) itemView.findViewById(R.id.img_interest);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentpos = getAdapterPosition();
            if (currentpos > -1) {
                //GET DATA FROM CATEGORY
                Category item = mGridData.get(currentpos);

                if (containsCategory(listBehaviourPattern, item.getCategory())) {
                    removeCategory(listBehaviourPattern, item.getCategory());
                    notifyItemChanged(currentpos);
                    if(isInterestActivity){
                        ((InterestActivity) getContext()).changeSkipNext(listBehaviourPattern.size());
                    }
                } else {
                    //SET OBJECT FOR BEHAVIOUR PATTERN
                    ConsumerBehaviourPattern itemPattern = new ConsumerBehaviourPattern();
                    itemPattern.setCategoryID(item.getCategory());
                    itemPattern.setCategoryName(item.getDescription());

                    listBehaviourPattern.add(itemPattern);
                    notifyItemChanged(currentpos);
                    if(isInterestActivity){
                        ((InterestActivity) getContext()).changeSkipNext(listBehaviourPattern.size());
                    }
                }
            }
        }
    }

    private void removeCategory(List<ConsumerBehaviourPattern> consumerBehaviourPattern, String category) {
        try {
            for (ConsumerBehaviourPattern consumerPattern : consumerBehaviourPattern) {
                if (consumerPattern.getCategoryID().equals(category)) {
                    consumerBehaviourPattern.remove(consumerPattern);
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

}
