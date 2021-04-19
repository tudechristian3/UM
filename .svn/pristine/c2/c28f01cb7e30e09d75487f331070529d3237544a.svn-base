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

import java.util.ArrayList;

public class MyIssuesRecyclerAdapter extends RecyclerView.Adapter<MyIssuesRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private ArrayList<MyIssues> mGridData = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView ticket_number_label;
        public final TextView ticket_number_value;
        public final TextView category_label;
        public final TextView category_value;
        public final TextView subject_label;
        public final TextView subject_value;
        public final TextView date_label;
        public final TextView date_value;

        public MyViewHolder(View itemView) {
            super(itemView);
            ticket_number_label = (TextView) itemView.findViewById(R.id.ticketNumberLabel);
            ticket_number_value = (TextView) itemView.findViewById(R.id.ticketNumberValue);
            category_label = (TextView) itemView.findViewById(R.id.categoryLabel);
            category_value = (TextView) itemView.findViewById(R.id.categoryValue);
            subject_label = (TextView) itemView.findViewById(R.id.subjectLabel);
            subject_value = (TextView) itemView.findViewById(R.id.subjectValue);
            date_label = (TextView) itemView.findViewById(R.id.dateLabel);
            date_value = (TextView) itemView.findViewById(R.id.dateValue);
        }
    }

    public MyIssuesRecyclerAdapter(Context context, ArrayList<MyIssues> mGridData){
        this.mContext = context;
        this.mGridData = mGridData;
    }

    private Context getContext(){
        return mContext;
    }

    public void setIssuesData(ArrayList<MyIssues> mGridData){
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public MyIssuesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_issues_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyIssuesRecyclerAdapter.MyViewHolder holder, int position) {
        MyIssues item = mGridData.get(position);
        holder.ticket_number_label.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", "Ticket#"));
        holder.category_label.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", "Category"));
        holder.subject_label.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", "Subject"));
        holder.date_label.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", "Date/Time In"));
        holder.ticket_number_value.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", item.getTicketNumber()));
        holder.category_value.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", item.getCategory()));
        holder.subject_value.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", item.getSubject()));
        holder.date_value.setText(CommonFunctions.setTypeFace(getContext(), "fonts/ElliotSans-Regular.ttf", item.getDate()));
        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(item);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyIssues item = (MyIssues)v.getTag();
            NewSupportIssueActivity.start(getContext(),item.getCategory(),item);
        }
    };

    @Override
    public int getItemCount() {
        return mGridData.size();
    }

}
