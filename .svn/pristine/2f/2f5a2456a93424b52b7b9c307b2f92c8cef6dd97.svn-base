package com.ultramega.shop.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.PromoPts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ban_Lenovo on 9/5/2017.
 */

public class LoyaltyRewardsRecyclerAdapter extends RecyclerView.Adapter<LoyaltyRewardsRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<PromoPts> mGridData;

    public LoyaltyRewardsRecyclerAdapter(Context context) {
        this.mContext = context;
        mGridData = new ArrayList<>();
    }

    public void setPromoPtsData(List<PromoPts> mGridData) {
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
    public LoyaltyRewardsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rewards, parent, false);
        return new LoyaltyRewardsRecyclerAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PromoPts pts = mGridData.get(position);

        holder.tvPromoTitle.setText(pts.getName());
        holder.tvPromoPoints.setText(CommonFunctions.currencyFormatter(String.valueOf(pts.getPoints())));

        Glide.with(mContext)
                .load(pts.getPhotoPath())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.ic_um_default_products)
                        .error(R.drawable.ic_um_default_products)
                        .override(250, 250)
                        .fitCenter())
                .into(holder.imgPromoPhoto);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPromoPhoto;
        private TextView tvPromoTitle;
        private TextView tvPromoPoints;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgPromoPhoto = (ImageView) itemView.findViewById(R.id.promo_image);
            imgPromoPhoto.setOnClickListener(this);
            tvPromoTitle = (TextView) itemView.findViewById(R.id.promo_title);
            tvPromoPoints = (TextView) itemView.findViewById(R.id.promo_points);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position > -1) {

                PromoPts pts = mGridData.get(position);

                switch (view.getId()) {
                    case R.id.promo_image: {

                        FullScreenActivity.start(mContext, pts.getPhotoPath());

                        break;
                    }
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        return mGridData.size();
    }
}
