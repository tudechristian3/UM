package com.ultramega.shop.adapter.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.activity.items.ViewItemsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.Promos;
import com.ultramega.shop.util.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class PromoRecyclerViewAdapter extends RecyclerView.Adapter<PromoRecyclerViewAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Promos> mGridData = new ArrayList<>();

    public PromoRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setPromoData(final List<Promos> mGridData) {
        int startPos = this.mGridData.size() + 1;
        //this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        notifyItemRangeInserted(startPos, mGridData.size());
    }

    public void add(Promos item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Promos> mGridData) {
        for (Promos item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }


    private Context getContext() {
        return mContext;
    }

    @Override
    public PromoRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_promo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PromoRecyclerViewAdapter.MyViewHolder holder, int position) {

        if (position > -1) {

            Promos item = mGridData.get(position);

//            holder.progressBar.setVisibility(View.VISIBLE);

            Glide.with(getContext())
                    .load(item.getPhotoPath())
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_um_default_products)
                            .override(CommonFunctions.getScreenWidth(getContext()) / 2, CommonFunctions.getScreenHeight(getContext()) / 4))
                    .thumbnail(0.1f)
                    .into(holder.item_photo);

            Glide.with(getContext())
                    .load(R.drawable.zoom_out)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(64, 64))
                    .into(holder.viewImage);

            holder.itemName.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", CommonFunctions.capitalizeWord(item.getName())));

            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setTag(item);

        }

    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Promos item = (Promos) v.getTag();
            ViewItemsActivity.startPromoItems(getContext(), item);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RoundedImageView item_photo;
        private final ImageView viewImage;
        private final TextView itemName;
//        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            item_photo = (RoundedImageView) itemView.findViewById(R.id.itemPhoto);
            viewImage = (ImageView) itemView.findViewById(R.id.viewImage);
            viewImage.setOnClickListener(this);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position > -1) {

                Promos item = mGridData.get(position);

                switch (view.getId()) {
                    case R.id.viewImage: {

                        FullScreenActivity.start(mContext, item.getPhotoPath());

                        break;
                    }
                }

            }

        }
    }

}
