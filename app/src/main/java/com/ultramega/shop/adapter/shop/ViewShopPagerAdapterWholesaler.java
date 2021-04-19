package com.ultramega.shop.adapter.shop;

import android.content.Context;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ultramega.shop.R;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.shop.CategoriesListFragment;
import com.ultramega.shop.fragment.shop.DailyFindsFragment;
import com.ultramega.shop.fragment.shop.PromosFragment;
import com.ultramega.shop.fragment.shop.SuppliersFragment;

/**
 * Created by User-PC on 7/18/2017.
 */

public class ViewShopPagerAdapterWholesaler extends FragmentPagerAdapter {
    private final Context mContext;
    private final Resources mRes;

    public ViewShopPagerAdapterWholesaler(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mRes = mContext.getResources();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DailyFindsFragment.newInstance(mRes.getString(R.string.str_daily_finds));
            case 1:
                return PromosFragment.newInstance(mRes.getString(R.string.str_promos));
            case 2:
                return CategoriesListFragment.newInstance(mRes.getString(R.string.str_categories));
            case 3:
                return SuppliersFragment.newInstance(mRes.getString(R.string.str_suppliers));
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CommonFunctions.setTypeFace(mContext, "fonts/ElliotSans-Bold.ttf", mContext.getResources().getStringArray(R.array.shop_tabs_array)[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }

}
