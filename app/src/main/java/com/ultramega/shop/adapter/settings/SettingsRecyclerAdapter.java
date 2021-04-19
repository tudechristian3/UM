package com.ultramega.shop.adapter.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.SettingsActivitySelected;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;

import java.util.ArrayList;

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private ArrayList<String> mGridData = new ArrayList<>();
    private BaseActivity baseActivity;
    private String usertype = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView settingsName;

        public MyViewHolder(View itemView) {
            super(itemView);
            settingsName = (TextView) itemView.findViewById(R.id.settingsName);
            baseActivity = new BaseActivity();
            usertype = baseActivity.getCurrentUserType(getContext());
        }
    }

    public SettingsRecyclerAdapter(Context context, ArrayList<String> mGridData) {
        this.mContext = context;
        this.mGridData = mGridData;
    }

    private Context getContext() {
        return mContext;
    }

    public void setSettingsData(ArrayList<String> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public SettingsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_settings_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingsRecyclerAdapter.MyViewHolder holder, int position) {
        holder.settingsName.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", mGridData.get(position)));
        holder.itemView.setOnClickListener(onClickListener);

        if (usertype.equals("WHOLESALER")) {
            if (position > 1)
                holder.itemView.setTag(position + 1);
            else
                holder.itemView.setTag(position);
        } else {
            holder.itemView.setTag(position);
        }

    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SettingsActivitySelected.start(getContext(), (Integer) v.getTag());
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
