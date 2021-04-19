package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.SupportIssueActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;

public class SupportFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support,container,false);

        TextView radioButtonSubmitIssue = (TextView) view.findViewById(R.id.radioButtonSubmitIssue);
        TextView radioButtonMyIssues = (TextView) view.findViewById(R.id.radioButtonMyIssues);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupSupport);
        radioGroup.setOnCheckedChangeListener(this);

        radioButtonSubmitIssue.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "Submit an issue"));
        radioButtonMyIssues.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "My Issues"));

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId){
            case R.id.radioButtonSubmitIssue:{
                SupportIssueActivity.start(getViewContext(),0,"Submit an issue");
                break;
            }
            case R.id.radioButtonMyIssues:{
                SupportIssueActivity.start(getViewContext(),1,"My Issues");
                break;
            }
        }
    }
}
