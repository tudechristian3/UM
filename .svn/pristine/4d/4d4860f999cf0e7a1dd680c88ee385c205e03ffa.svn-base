package com.ultramega.shop.adapter.supplier;

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
import com.ultramega.shop.activity.ViewShopItemActivity;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.SupplierItems;

import java.util.ArrayList;
import java.util.List;

public class SupplierItemsRecyclerViewAdapter extends RecyclerView.Adapter<SupplierItemsRecyclerViewAdapter.MyViewHolder> {

    private final Context mContext;
    private List<SupplierItems> mGridData;

    public SupplierItemsRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mGridData = new ArrayList<>();
    }

    /**
     * Updates grid data and refresh grid items.
     */
    public void setGridData(List<SupplierItems> data) {
        int startPos = mGridData.size() + 1;
        mGridData.clear();
        mGridData.addAll(data);
        notifyItemRangeInserted(startPos, mGridData.size());
    }

    public void add(SupplierItems item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<SupplierItems> mGridData) {
        for (SupplierItems item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = mGridData.size();
        mGridData.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public SupplierItemsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deals_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SupplierItemsRecyclerViewAdapter.MyViewHolder holder, int position) {
        SupplierItems item = mGridData.get(position);

        if (item.getIsPromo() == 1) {

            Glide.with(getContext())
                    .load(R.drawable.promo_icon_red_box)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(100, 100))
                    .into(holder.photo_image);

            holder.photo_image.setVisibility(View.VISIBLE);
        } else {
            holder.photo_image.setVisibility(View.GONE);
        }

        Glide.with(getContext())
                .load(R.drawable.zoom_out)
                .apply(new RequestOptions()
                        .fitCenter()
                        .override(64, 64))
                .into(holder.viewImage);

        holder.item_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getDescription()));

        Glide.with(getContext())
                .load(item.getPhotoURL())
                .error(Glide.with(getContext())
                        .load(R.drawable.ic_um_default_products))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_um_default_products)
                        .override(CommonFunctions.getScreenWidth(getContext()) / 2, CommonFunctions.getScreenHeight(getContext()) / 4))
                .into(holder.item_photo);

        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SupplierItems item = (SupplierItems) v.getTag();
            ViewShopItemActivity.startSupplier(getContext(), item);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView item_name;
        public final ImageView item_photo;
        private final ImageView viewImage;
        private final ImageView photo_image;
//        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.itemName);
            item_photo = (ImageView) itemView.findViewById(R.id.itemPhoto);
            viewImage = (ImageView) itemView.findViewById(R.id.viewImage);
            viewImage.setOnClickListener(this);
            photo_image = (ImageView) itemView.findViewById(R.id.photo_image);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            SupplierItems item = mGridData.get(position);
            switch (view.getId()) {
                case R.id.viewImage: {
                    FullScreenActivity.start(mContext, item.getPhotoURL());
                    break;
                }
            }
        }
    }

}
