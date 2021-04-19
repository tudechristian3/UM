package com.ultramega.shop.adapter.search;

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
import com.ultramega.shop.activity.SearchResultActivity;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.CategoryItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GoodApps on 13/12/2017.
 */

public class SuggestiveSearchAdapter extends RecyclerView.Adapter<SuggestiveSearchAdapter.SuggestiveViewHolder> {

    private Context mContext;
    private List<CategoryItems> mSuggestiveSearches = new ArrayList<>();
    private UltramegaShopUtilities mDbUtils;
    private String searchType;


    public SuggestiveSearchAdapter(Context context, List<CategoryItems> SuggestiveSearches, String type) {
        mContext = context;
        mSuggestiveSearches = SuggestiveSearches;

        searchType = type;
    }

    public void updateData(List<CategoryItems> searches) {
        mSuggestiveSearches.clear();
        mSuggestiveSearches = searches;
        notifyDataSetChanged();
    }

    @Override
    public SuggestiveSearchAdapter.SuggestiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_suggestive_searches, parent, false);
        SuggestiveViewHolder viewHolder = new SuggestiveViewHolder(view);
        mDbUtils = new UltramegaShopUtilities(mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SuggestiveSearchAdapter.SuggestiveViewHolder holder, int position) {
        CategoryItems search = mSuggestiveSearches.get(position);
        holder.mTvSearch.setText(search.getDescription().toLowerCase());
        holder.itemView.setTag(search.getDescription());

        Glide.with(mContext)
                .load(search.getItemGroupPicURL())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_um_default_products)
                        .error(R.drawable.ic_um_default_products)
                        .fitCenter())
                .into(holder.mTvSearchImg);

        holder.itemView.setOnClickListener(itemClickListener);
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String searchKey = (String) v.getTag();
            mDbUtils.insertRecentSearches(String.valueOf(searchKey));
            SearchResultActivity.start(mContext, String.valueOf(searchKey), searchType);
//            Log.d("krishttp","Search Key" + searchKey);
        }
    };

    @Override
    public int getItemCount() {

        return mSuggestiveSearches.size();
    }

    public class SuggestiveViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvSearch;
        private ImageView mTvSearchImg;

        public SuggestiveViewHolder(View itemView) {
            super(itemView);
            mTvSearch = (TextView) itemView.findViewById(R.id.tv_search);
            mTvSearchImg = (ImageView) itemView.findViewById(R.id.tv_search_image);

        }
    }
}
