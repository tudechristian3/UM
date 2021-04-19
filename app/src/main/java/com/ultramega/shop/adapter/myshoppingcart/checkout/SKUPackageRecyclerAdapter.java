package com.ultramega.shop.adapter.myshoppingcart.checkout;

import android.content.Context;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.util.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 7/14/2017.
 */

public class SKUPackageRecyclerAdapter extends RecyclerView.Adapter<SKUPackageRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<ItemSKU> mGridData;
    private final UltramegaShopUtilities mdb;
    private String mUserType;

    public SKUPackageRecyclerAdapter(Context context, UltramegaShopUtilities mDb, String usertype) {
        this.mContext = context;
        mdb = mDb;
        mUserType = usertype;
        this.mGridData = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

//    public void setPackageData(List<ItemSKU> mGridData) {
//        this.mGridData = mGridData;
//        notifyDataSetChanged();
//    }

    public void add(ItemSKU item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<ItemSKU> mGridData) {
        for (ItemSKU item : mGridData) {
            add(item);
        }
    }

    public void setPackageData(final List<ItemSKU> mGridData) {
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
    public SKUPackageRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_package_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SKUPackageRecyclerAdapter.MyViewHolder holder, int position) {
        ItemSKU item = mGridData.get(position);

        String packageDescription = item.getPackageDescription();
        SpannableStringBuilder ssb = new SpannableStringBuilder(packageDescription);

        applyFontStyle(ssb, 0, ssb.length(), "fonts/ElliotSans-Medium.ttf");
        applyColor(ssb, 0, ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));

        if (item.getPrice() == item.getGrossPrice()) {

            ssb.append(" ");
            String regularprice = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getPrice())));
            ssb.append(regularprice);
            applyFontStyle(ssb, ssb.length() - regularprice.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - regularprice.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.colorTxtProductAmount));

            holder.savingsLayout.setVisibility(View.GONE);

        } else {

            String regularprice = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getGrossPrice())));
            // Create a span that will strikethrough the text
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

            ssb.append(" ");
            ssb.append(regularprice);
            applyFontStyle(ssb, ssb.length() - regularprice.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - regularprice.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.color_9B9B9B));
            ssb.setSpan(strikethroughSpan,
                    ssb.length() - regularprice.length(),
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            String actualprice = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getPrice())));
            ssb.append(" ");
            ssb.append(actualprice);
            applyFontStyle(ssb, ssb.length() - actualprice.length(), ssb.length(), "fonts/ElliotSans-Regular.ttf");
            applyColor(ssb, ssb.length() - actualprice.length(), ssb.length(), ContextCompat.getColor(getContext(), R.color.colorTxtProductAmount));

            holder.savingsLayout.setVisibility(View.VISIBLE);
            String saveAmount = "₱".concat(CommonFunctions.currencyFormatter(String.valueOf(item.getGrossPrice() - item.getPrice())));
            String savings = "Save " + saveAmount;
            holder.txvSavings.setText(savings);

        }
        holder.itempick.setText(ssb, TextView.BufferType.EDITABLE);

    }

    private void applyFontStyle(SpannableStringBuilder ssb, int start, int end, String font) {
        Typeface newFont = Typeface.createFromAsset(getContext().getAssets(), font);
        ssb.setSpan(new CustomTypefaceSpan("", newFont), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void applyColor(SpannableStringBuilder ssb, int start, int end, int color) {
        // Create a span that will make the text red
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                color);
        // Apply the color span
        ssb.setSpan(
                colorSpan,            // the span to add
                start,                // the start of the span (inclusive)
                end,                  // the end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder
        // SPAN_EXCLUSIVE_EXCLUSIVE means to not extend the span when additional
        // text is added in later
    }

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itempick;
        private LinearLayout savingsLayout;
        private TextView txvSavings;

        public MyViewHolder(View itemView) {
            super(itemView);
            itempick = (TextView) itemView.findViewById(R.id.itempick);
            savingsLayout = (LinearLayout) itemView.findViewById(R.id.savingsLayout);
            txvSavings = (TextView) itemView.findViewById(R.id.txvSavings);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
