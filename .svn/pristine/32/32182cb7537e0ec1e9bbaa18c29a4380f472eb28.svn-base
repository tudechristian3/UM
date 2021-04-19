package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.settings.support.MyIssuesFragment;
import com.ultramega.shop.fragment.settings.support.SubmitAnIssueFragment;

public class SupportIssueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_issue);

        setUpToolBar();

        int mIndexSelected = getIntent().getIntExtra("index", -1);
        String mTitle = getIntent().getStringExtra("title");

        displayView(mIndexSelected, null, mTitle);
    }

    private void displayView(int index, Bundle args, String title) {
        Fragment fragment = null;
        switch (index) {
            case 0: {
                fragment = new SubmitAnIssueFragment();
                break;
            }
            case 1: {
                fragment = new MyIssuesFragment();
                break;
            }
        }

        if (args != null) {
            fragment.setArguments(args);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment != null) {
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.add(R.id.container_body, fragment);
        }


        // set the toolbar title
        setActionBarTitle(title);
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context, int index, String title) {
        Intent intent = new Intent(context, SupportIssueActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
