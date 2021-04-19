package com.ultramega.shop.adapter.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.pojo.FactsAndQuestions;

import java.util.ArrayList;
import java.util.HashMap;


public class FactsAndQuestionsExpandableAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final ArrayList<String> expandableListTitle;
    private final HashMap<String,ArrayList<FactsAndQuestions>> expandableListDetail;

    public FactsAndQuestionsExpandableAdapter(Context context, ArrayList<String> expandableListTitle, HashMap<String,ArrayList<FactsAndQuestions>> expandableListDetail){
        this.mContext = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_facts_and_questions_group,null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.factsHeader);
        String headerText = expandableListTitle.get(groupPosition);
        tvHeader.setText(headerText);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_facts_and_questions_item,null);
        }

        FactsAndQuestions item = (FactsAndQuestions) getChild(groupPosition,groupPosition);
        TextView tvDetails = (TextView) convertView.findViewById(R.id.factsDescription);
        tvDetails.setText(item.getDescription());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
