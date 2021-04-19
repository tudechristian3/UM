package com.ultramega.shop.adapter.myorders;

import android.content.Context;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.myorders.ItemOrdersFragment;

/**
 * Created by User-PC on 7/18/2017.
 */

public class ViewOrderPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final Resources mRes;

    public ViewOrderPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mRes = mContext.getResources();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ItemOrdersFragment.newInstance(mRes.getString(R.string.str_pending));
            case 1:
                return ItemOrdersFragment.newInstance(mRes.getString(R.string.str_completed));
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", mContext.getResources().getStringArray(R.array.orders_tabs_array)[position]);
    }

    @Override
    public int getCount() {
        return ItemOrdersFragment.mNofItems;
    }
    
}
