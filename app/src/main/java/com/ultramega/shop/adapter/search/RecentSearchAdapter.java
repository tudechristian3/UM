package com.ultramega.shop.adapter.search;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.SearchResultActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ban_Lenovo on 9/14/2017.
 */

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder> {

    private Context mContext;
    private List<String> mRecentSearches = new ArrayList<>();
    private String searchType;


    public RecentSearchAdapter(Context context, List<String> recentSearches, String type) {
        mContext = context;
        mRecentSearches = recentSearches;

        searchType = type;
    }

    public void clear() {
        int startPos = this.mRecentSearches.size();
        this.mRecentSearches.clear();
        notifyItemRangeRemoved(0, startPos);
    }

    public void updateData(List<String> searches) {
        mRecentSearches.clear();
        mRecentSearches = searches;
        notifyDataSetChanged();
    }

    @Override
    public RecentSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recent_searches, parent, false);
        RecentSearchViewHolder viewHolder = new RecentSearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecentSearchViewHolder holder, int position) {
        String search = mRecentSearches.get(position);
        holder.mTvSearch.setText(search);
        holder.itemView.setTag(search);
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
        return mRecentSearches.size();
    }

    public class RecentSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvSearch;

        public RecentSearchViewHolder(View itemView) {
            super(itemView);
            mTvSearch = (TextView) itemView.findViewById(R.id.tv_search);
        }
    }
}
