package com.ultramega.shop.diffutil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ultramega.shop.pojo.CategoryItems;

import java.util.List;

public class TopPicksCallback extends DiffUtil.Callback {

    private final List<CategoryItems> mOldCategoryItems;
    private final List<CategoryItems> mNewCategoryItems;

    public TopPicksCallback(List<CategoryItems> mOldCategoryItems, List<CategoryItems> mNewCategoryItems) {
        this.mOldCategoryItems = mOldCategoryItems;
        this.mNewCategoryItems = mNewCategoryItems;
    }

    @Override
    public int getOldListSize() {
        return mOldCategoryItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCategoryItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCategoryItems.get(oldItemPosition).equals(mNewCategoryItems.get(
                newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCategoryItems.get(oldItemPosition).equals(mNewCategoryItems.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
