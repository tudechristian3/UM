package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.decoration.DividerItemDecoration;

import java.util.ArrayList;

public class NotificationsSettingFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);

        ArrayList<String> mGridData = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));

       // NotificationsRecyclerAdapter mNotificationsAdapter = new NotificationsRecyclerAdapter(getViewContext(), mGridData);

        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(),R.drawable.recycler_divider)));
        recyclerView.addItemDecoration(new DividerItemDecoration(getViewContext(), null, true,true));
       // recyclerView.setAdapter(mNotificationsAdapter);

        mGridData.add("Email Notifications");
        mGridData.add("Push Notifications");
        mGridData.add("Order Updates");
        mGridData.add("Promotions");
       // mNotificationsAdapter.setNotificationsData(mGridData);

        return view;
    }
}
