package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.MyIssues;

public class NewSupportIssueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_support_issue);

        setUpToolBar();

        MyIssues item = getIntent().getParcelableExtra("item");

        String mTitle = getIntent().getStringExtra("title");
        TextView chat_category_title = (TextView) findViewById(R.id.chatCategoryTitle);
        TextView ticket_number_label = (TextView) findViewById(R.id.ticketNumberLabel);
        TextView ticket_number_value = (TextView) findViewById(R.id.ticketNumberValue);
        TextView status_label = (TextView) findViewById(R.id.statusLabel);
        TextView status_value = (TextView) findViewById(R.id.statusValue);
        TextView subject_label = (TextView) findViewById(R.id.subjectLabel);
        TextView subject_value = (TextView) findViewById(R.id.subjectValue);
        TextView write_message = (TextView) findViewById(R.id.writeMessage);

        ticket_number_label.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "Ticket#"));
        status_label.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "Status"));
        subject_label.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "Subject"));

        ticket_number_value.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", item.getTicketNumber()));
        status_value.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", item.getStatus()));
        subject_value.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", item.getSubject()));

        write_message.setHint(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", "Write a message..."));

        chat_category_title.setText(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Regular.ttf", mTitle));
        setTitle(CommonFunctions.setTypeFace(getViewContext(),"fonts/ElliotSans-Medium.ttf", mTitle));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context, String title, MyIssues item){
        Intent intent = new Intent(context, NewSupportIssueActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("item",item);
        context.startActivity(intent);
    }

}
