package com.ultramega.shop.adapter.shop;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ViewShopItemActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.util.SquareImageView;

import java.util.ArrayList;
import java.util.List;

public class ShopItemRecyclerViewAdapterV2 extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final Context mContext;
    private List<CategoryItems> mGridData = new ArrayList<>();

    public ShopItemRecyclerViewAdapterV2(Context context, RecyclerView recyclerView) {
        this.mContext = context;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case VIEW_ITEM: {
                            return 1;
                        }
                        case VIEW_PROG: {
                            return 2;
                        }
                        default: {
                            return -1;
                        }
                    }
                }
            });

        }
    }

    public void setGridData(final List<CategoryItems> mGridData) {
//        int startPos = this.mGridData.size() + 1;
//        this.mGridData.clear();
//        this.mGridData.addAll(mGridData);
//        notifyItemRangeInserted(startPos, mGridData.size());

        //add items one by one
        int start = this.mGridData.size();
        int end = mGridData.size();
        for (int i = start; i < end; i++) {
            this.mGridData.add(new CategoryItems(mGridData.get(i).getCategory(),
                    mGridData.get(i).getCatClass(),
                    mGridData.get(i).getDescription(),
                    mGridData.get(i).getItemGroupPicURL()));
            notifyItemInserted(this.mGridData.size());
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deals_item, parent, false);
            vh = new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            vh = new ProgressViewHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            CategoryItems item = mGridData.get(position);
            ((MyViewHolder) holder).item_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getDescription()));
            Glide.with(mContext)
                    .load(item.getItemGroupPicURL())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_um_default_products)
                            .centerCrop()
                            .downsample(DownsampleStrategy.AT_LEAST))
                    .into(((MyViewHolder) holder).item_photo);
            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setTag(item);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CategoryItems item = (CategoryItems) v.getTag();
            ViewShopItemActivity.start(getContext(), item);
        }
    };

    @Override
    public int getItemViewType(int position) {
        return mGridData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    /* VIEW HOLDERS */

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView item_name;
        private final SquareImageView item_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.itemName);
            item_photo = (SquareImageView) itemView.findViewById(R.id.itemPhoto);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
