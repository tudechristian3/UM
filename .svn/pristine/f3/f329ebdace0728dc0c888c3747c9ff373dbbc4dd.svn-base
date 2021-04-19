package com.ultramega.shop.fragment.settings.support;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.SupportCategoryRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.SupportCategory;

import java.util.ArrayList;

public class SubmitAnIssueFragment extends BaseFragment {

    private ArrayList<SupportCategory> mGridData;
    private SupportCategoryRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_an_issue,container,false);

        TextView selectIssueLabel = (TextView) view.findViewById(R.id.selectIssueLabel);
        selectIssueLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Select an issue category"));

        //Initialize with empty data
        mGridData = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_support_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));

        recyclerView.setNestedScrollingEnabled(false);

        mAdapter = new SupportCategoryRecyclerAdapter(getViewContext(), mGridData);

        recyclerView.setAdapter(mAdapter);

        prepareSupportCategoryData();

        return view;
    }

    private void prepareSupportCategoryData(){

        SupportCategory item = new SupportCategory();
        item.setSupportTitle("Accounts Management");
        item.setSupportDescription("Issues like Registration, Accounts information, and other acoounts related issues.");
        mGridData.add(item);

        item = new SupportCategory();
        item.setSupportTitle("Funds");
        item.setSupportDescription("Issues like withdrawal of funds and other such cases");
        mGridData.add(item);

        item = new SupportCategory();
        item.setSupportTitle("Another category");
        item.setSupportDescription("Issues like Rgistration, Accounts information, and other acoounts related issues.");
        mGridData.add(item);

        item = new SupportCategory();
        item.setSupportTitle("Yet Another category");
        item.setSupportDescription("Issues like Rgistration, Accounts information, and other acoounts related issues.");
        mGridData.add(item);

        item = new SupportCategory();
        item.setSupportTitle("Another category");
        item.setSupportDescription("Issues like Rgistration, Accounts information, and other acoounts related issues.");
        mGridData.add(item);

        item = new SupportCategory();
        item.setSupportTitle("Yet Another category");
        item.setSupportDescription("Issues like Rgistration, Accounts information, and other acoounts related issues.");
        mGridData.add(item);

        mAdapter.setSupportCategoryData(mGridData);

    }
}
