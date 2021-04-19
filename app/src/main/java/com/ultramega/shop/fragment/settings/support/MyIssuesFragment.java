package com.ultramega.shop.fragment.settings.support;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.MyIssuesRecyclerAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.pojo.MyIssues;

import java.util.ArrayList;

public class MyIssuesFragment extends BaseFragment {

    private ArrayList<MyIssues> mGridData_open, mGridData_close;
    private RecyclerView recyclerView_open, recyclerView_close;
    private MyIssuesRecyclerAdapter mIssuesAdapter_open, mIssuesAdapter_close;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_issues, container, false);

        //Initialize with empty data
        mGridData_open = new ArrayList<>();
        mGridData_close = new ArrayList<>();

        TextView listTitle_open = (TextView) view.findViewById(R.id.listTitle_open);
        TextView listTitle_close = (TextView) view.findViewById(R.id.listTitle_close);
        recyclerView_open = (RecyclerView) view.findViewById(R.id.recycler_view_my_issues_open);
        recyclerView_close = (RecyclerView) view.findViewById(R.id.recycler_view_my_issues_close);

        listTitle_open.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "OPEN ISSUES"));
        listTitle_close.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "CLOSE ISSUES"));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                recyclerView_open.setLayoutManager(new LinearLayoutManager(getViewContext()));
                recyclerView_close.setLayoutManager(new LinearLayoutManager(getViewContext()));

                recyclerView_open.setNestedScrollingEnabled(false);
                recyclerView_close.setNestedScrollingEnabled(false);

                mIssuesAdapter_open = new MyIssuesRecyclerAdapter(getViewContext(), mGridData_open);
                mIssuesAdapter_close = new MyIssuesRecyclerAdapter(getViewContext(), mGridData_close);

                recyclerView_open.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                recyclerView_open.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));
                recyclerView_close.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
                recyclerView_close.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true, true));

                recyclerView_open.setAdapter(mIssuesAdapter_open);
                recyclerView_close.setAdapter(mIssuesAdapter_close);

                prepareOpenIssuesData();
                prepareCloseIssuesData();

            }
        }, 400);

        return view;
    }

    private void prepareOpenIssuesData() {
        MyIssues item = new MyIssues();
        item.setTicketNumber("SPTNO000021");
        item.setCategory("Accounts Management");
        item.setStatus("OPEN");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_open.add(item);

        item = new MyIssues();
        item.setTicketNumber("SPTNO000022");
        item.setCategory("Accounts Management");
        item.setStatus("OPEN");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_open.add(item);

        item = new MyIssues();
        item.setTicketNumber("SPTNO000023");
        item.setCategory("Accounts Management");
        item.setStatus("OPEN");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_open.add(item);

        item = new MyIssues();
        item.setTicketNumber("SPTNO000024");
        item.setCategory("Accounts Management");
        item.setStatus("OPEN");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_open.add(item);

        mIssuesAdapter_open.setIssuesData(mGridData_open);
    }

    private void prepareCloseIssuesData() {
        MyIssues item = new MyIssues();
        item.setTicketNumber("SPTNO000021");
        item.setCategory("Accounts Management");
        item.setStatus("CLOSE");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_close.add(item);

        item = new MyIssues();
        item.setTicketNumber("SPTNO000022");
        item.setCategory("Accounts Management");
        item.setStatus("CLOSE");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_close.add(item);

        item = new MyIssues();
        item.setTicketNumber("SPTNO000023");
        item.setCategory("Accounts Management");
        item.setStatus("CLOSE");
        item.setSubject("Sample");
        item.setDate("Mar 28, 2017 02:04 PM");
        mGridData_close.add(item);

        mIssuesAdapter_close.setIssuesData(mGridData_close);
    }
}
