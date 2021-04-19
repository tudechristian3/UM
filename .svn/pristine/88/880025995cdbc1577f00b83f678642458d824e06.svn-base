package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.FactsAndQuestionsExpandableAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.pojo.FactsAndQuestions;

import java.util.ArrayList;
import java.util.HashMap;

public class FactsAndQuestionsFragment extends BaseFragment {

    private ArrayList<String> mListHeader;
    private HashMap<String, ArrayList<FactsAndQuestions>> mListDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facts_and_questions,container,false);

        ExpandableListView expListview = (ExpandableListView) view.findViewById(R.id.expandedView);

        prepareFAQData();

        FactsAndQuestionsExpandableAdapter mFAQExpAdapter = new FactsAndQuestionsExpandableAdapter(getViewContext(), mListHeader, mListDescription);

        expListview.setAdapter(mFAQExpAdapter);

        return view;
    }

    private void prepareFAQData(){

        ArrayList<FactsAndQuestions> mDescriptionItem = new ArrayList<>();
        mListHeader = new ArrayList<>();
        mListDescription = new HashMap<>();

        mListHeader.add("What is Ultra Mega Wholesale Retailer Online?");
        mListHeader.add("How do I buy merchandises?");

        FactsAndQuestions item = new FactsAndQuestions();
        item.setDescription("Sample item description here. Sample item description here. Sample item description here. ");
        mDescriptionItem.add(item);

        item = new FactsAndQuestions();
        item.setDescription("Sample item description here. Sample item description here. Sample item description here. ");
        mDescriptionItem.add(item);

        mListDescription.put(mListHeader.get(0), mDescriptionItem);
        mListDescription.put(mListHeader.get(1), mDescriptionItem);

    }
}
