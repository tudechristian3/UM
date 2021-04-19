package com.ultramega.shop.adapter.shop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.ultramega.shop.pojo.CategoryItems;

import java.util.ArrayList;
import java.util.List;

public class TopPicksGridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private List<CategoryItems> mGridData;
    private boolean isPromo = false;

    public TopPicksGridViewAdapter(Context context, boolean isPromo) {
        this.mContext = context;
        this.isPromo = isPromo;
        this.mGridData = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

    public void add(CategoryItems item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<CategoryItems> mGridData) {
        for (CategoryItems item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final CategoryItems item = mGridData.get(position);

        if (convertView == null) {

            itemView = new View(mContext);
            itemView = inflater.inflate(R.layout.fragment_deals_item, null);

        } else {
            itemView = (View) convertView;
        }

        TextView item_name = (TextView) itemView.findViewById(R.id.itemName);
        ImageView item_photo = (ImageView) itemView.findViewById(R.id.itemPhoto);
        ImageView viewImage = (ImageView) itemView.findViewById(R.id.viewImage);
        ImageView photo_image = (ImageView) itemView.findViewById(R.id.photo_image);
//        final ShimmerFrameLayout shimmerLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer_view_container);
        //final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
//        final ShimmerLayout shimmerLayout = (ShimmerLayout) itemView.findViewById(R.id.shimmer_view_container);

        item_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", CommonFunctions.capitalizeWord(item.getDescription())));

        if (item.getIsPromo() == 1) {

            Glide.with(getContext())
                    .load(R.drawable.promo_icon_red_box)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(100, 100))
                    .into(photo_image);

            photo_image.setVisibility(View.VISIBLE);

        } else {

            photo_image.setVisibility(View.GONE);

        }

        //progressBar.setVisibility(View.VISIBLE);
        //shimmerLayout.startShimmerAnimation();

        Glide.with(getContext())
                .load(item.getItemGroupPicURL())
                .error(Glide.with(getContext())
                        .load(R.drawable.ic_um_default_products))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
//                        shimmerLayout.stopShimmer();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
//                        shimmerLayout.stopShimmer();
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_um_default_products)
                        .override(CommonFunctions.getScreenWidth(getContext()) / 2, CommonFunctions.getScreenHeight(getContext()) / 4))
                .thumbnail(0.1f)
                .into(item_photo);

        Glide.with(getContext())
                .load(R.drawable.zoom_out)
                .apply(new RequestOptions()
                        .fitCenter()
                        .override(64, 64))
                .into(viewImage);


        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenActivity.start(getContext(), item.getItemGroupPicURL());
            }
        });


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewShopItemActivity.start(getContext(), item, isPromo);
            }
        });

        return itemView;
    }

}
