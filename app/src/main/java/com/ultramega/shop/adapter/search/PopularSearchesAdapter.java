package com.ultramega.shop.adapter.search;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.SearchResultActivity;
import com.ultramega.shop.pojo.PopularSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ban_Lenovo on 9/14/2017.
 */

public class PopularSearchesAdapter extends RecyclerView.Adapter<PopularSearchesAdapter.PopularSearchViewHolder> {

    private Context mContext;
    private List<PopularSearch> mPopularSearches = new ArrayList<>();
    private String searchType;


    public PopularSearchesAdapter(Context context, List<PopularSearch> searches, String type) {
        mContext = context;
        mPopularSearches = searches;

        searchType = type;
    }

    public void updateData(List<PopularSearch> searches) {
        mPopularSearches.clear();
        mPopularSearches = searches;
        notifyDataSetChanged();
    }

    @Override
    public PopularSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_popular_search, parent, false);
        PopularSearchViewHolder viewHolder = new PopularSearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PopularSearchViewHolder holder, int position) {
        PopularSearch search = mPopularSearches.get(position);
        holder.mTvSearch.setText(search.getSearchValue());
        holder.itemView.setTag(search.getSearchValue());
        holder.itemView.setOnClickListener(itemClickListener);
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String searchKey = (String) v.getTag();
            SearchResultActivity.start(mContext, searchKey, searchType);
        }
    };

    @Override
    public int getItemCount() {
        return mPopularSearches.size();
    }

    public class PopularSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvSearch;

        public PopularSearchViewHolder(View itemView) {
            super(itemView);
            mTvSearch = (TextView) itemView.findViewById(R.id.tv_search);
        }
    }
}
