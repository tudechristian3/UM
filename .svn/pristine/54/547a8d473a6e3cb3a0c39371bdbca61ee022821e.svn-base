package com.ultramega.shop.adapter.shop;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.items.ViewItemsActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Category> mGridData;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView categories_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            categories_name = (TextView) itemView.findViewById(R.id.categoriesName);
        }
    }

    public CategoriesRecyclerAdapter(Context context) {
        this.mContext = context;
        mGridData = new ArrayList<>();
    }

    private Context getContext() {
        return mContext;
    }

    public void add(Category item) {
        mGridData.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Category> mGridData) {
        for (Category item : mGridData) {
            add(item);
        }
    }

    public void clear() {
        int startPos = this.mGridData.size();
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    public void setCategoriesData(List<Category> data) {
        mGridData.clear();
        mGridData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_categories_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category item = mGridData.get(position);

        if (item.getDescription() != null) {
            holder.categories_name.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Medium.ttf", capitalizeWord(item.getDescription())));
        }

        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Category item = (Category) v.getTag();
            ViewItemsActivity.startCategoryItems(getContext(), item);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

    private String capitalizeWord(String word) {
        String[] cap_word_arr = word.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : cap_word_arr) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }
}
