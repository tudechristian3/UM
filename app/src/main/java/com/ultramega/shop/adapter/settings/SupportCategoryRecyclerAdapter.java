package com.ultramega.shop.adapter.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.NewSupportIssueActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.MyIssues;
import com.ultramega.shop.pojo.SupportCategory;

import java.util.ArrayList;


public class SupportCategoryRecyclerAdapter extends RecyclerView.Adapter<SupportCategoryRecyclerAdapter.MyViewHolder> {


    private final Context mContext;
    private ArrayList<SupportCategory> mGridData = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.supportCategoryName);
            description = (TextView) itemView.findViewById(R.id.supportCategoryDescription);
        }
    }

    public SupportCategoryRecyclerAdapter(Context context, ArrayList<SupportCategory> mGridData){
        this.mContext = context;
        this.mGridData = mGridData;
    }

    private Context getContext(){
        return mContext;
    }

    public void setSupportCategoryData(ArrayList<SupportCategory> mGridData){
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public SupportCategoryRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_support_category_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SupportCategoryRecyclerAdapter.MyViewHolder holder, int position) {

        SupportCategory item = mGridData.get(position);
        holder.title.setText(CommonFunctions.setTypeFace(getContext(),"fonts/ElliotSans-Regular.ttf",item.getSupportTitle()));
        holder.description.setText(CommonFunctions.setTypeFace(getContext(),"fonts/ElliotSans-Regular.ttf",item.getSupportDescription()));
        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SupportCategory item = (SupportCategory) v.getTag();
            MyIssues issueItem = new MyIssues();
            issueItem.setTicketNumber("SPTNO000021");
            issueItem.setCategory("Accounts Management");
            issueItem.setStatus("OPEN");
            issueItem.setSubject("Sample");
            issueItem.setDate("Mar 28, 2017 02:04 PM");
            NewSupportIssueActivity.start(getContext(),item.getSupportTitle(),issueItem);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }


}
