package com.ultramega.shop.adapter.myshoppingcart;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ShoppingCartsQueue;

import java.util.ArrayList;
import java.util.List;

public class ReviewOrderRecyclerAdapter extends RecyclerView.Adapter<ReviewOrderRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ShoppingCartsQueue> mGridData = new ArrayList<>();

    private ConsumerProfile itemProf;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView product_name;
        private final TextView product_amount;
        private final TextView product_quantity;
        private final ImageView product_photo;

        public MyViewHolder(View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.my_shopping_cart_product_name);
            product_amount = (TextView) itemView.findViewById(R.id.my_shopping_cart_product_amount);
            product_quantity = (TextView) itemView.findViewById(R.id.my_shopping_cart_product_quantity);
            product_photo = (ImageView) itemView.findViewById(R.id.my_shopping_cart_product_photo);
        }
    }

    public ReviewOrderRecyclerAdapter(Context context, List<ShoppingCartsQueue> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
        UltramegaShopUtilities mdb = new UltramegaShopUtilities(getContext());
    }

    private Context getContext() {
        return mContext;
    }

    public void setReviewData(List<ShoppingCartsQueue> mGridData) {
        this.mGridData.clear();
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public ReviewOrderRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewOrderRecyclerAdapter.MyViewHolder holder, int position) {
        try {
            ShoppingCartsQueue item = mGridData.get(position);
            holder.product_name.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Medium.ttf", item.getItemDescription().concat(" (" + item.getPackageDescription() + ")")));
            holder.product_amount.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", CommonFunctions.currencyFormatter(String.valueOf(item.getTotalAmount()))));
            String orderqty = "QTY: ".concat(String.valueOf(item.getQuantity()));
            holder.product_quantity.setText(CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Regular.ttf", orderqty));
            String photoimg = item.getItemPicUrl() == null ? "." : item.getItemPicUrl();
            Glide.with(mContext)
                    .load(photoimg)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_um_default_products)
                            .error(R.drawable.ic_um_default_products)
                            .fitCenter())
                    .into(holder.product_photo);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }
}
