package com.ultramega.shop.fragment.myshoppingcart.checkout.pickup;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.PickUpActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.util.UserPreferenceManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;

/**
 * Created by User-PC on 8/18/2017.
 */

public class PickUpInstructionsFragment extends BaseFragment implements View.OnClickListener {
    private Branches selectedBranch;

    private MaterialEditText branchname;
    private MaterialEditText branchaddress;
    private MaterialEditText scheduledate;
    private TextView schedulelabel;
    private TextView branchlabel;
    private TextView instructionslabel;
    private EditText instructions;




    Date dates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_up_instructions, container, false);

        setHasOptionsMenu(true);

        //set up title
        ((PickUpActivity) getViewContext()).setActionBarTitle("Checkout");

        instructions = (EditText) view.findViewById(R.id.instructions);
        schedulelabel = (TextView) view.findViewById(R.id.schedulelabel);
        branchlabel = (TextView) view.findViewById(R.id.branchlabel);
        branchname = (MaterialEditText) view.findViewById(R.id.branchname);
        branchaddress = (MaterialEditText) view.findViewById(R.id.branchaddress);
        scheduledate = (MaterialEditText) view.findViewById(R.id.scheduledate);
        scheduledate.setOnClickListener(this);
        instructionslabel = (TextView) view.findViewById(R.id.label);

        //initialize empty data
        selectedBranch = null;
        selectedBranch = getArguments().getParcelable("branches");

        schedulelabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "SCHEDULE OF PICK UP"));
        branchlabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "BRANCH INFO"));
        instructionslabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "SPECIAL INSTRUCTIONS"));
        branchname.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", selectedBranch.getBranchName()));
        branchaddress.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", selectedBranch.getStreetAddress() + ", " + selectedBranch.getCity() + ", " + selectedBranch.getProvince() + ", " + selectedBranch.getZip() + ", " + selectedBranch.getCountry()));


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_skip, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.skip: {

                if (scheduledate.getText().toString().trim().length() != 0) {
                    hideKeyboard(getViewContext());
                    String specialinstructions;
                    if (evaluateForm()) {
                        specialinstructions = instructions.getText().toString().trim();
                    } else {
                        specialinstructions = ".";
                    }
                    ((PickUpActivity) getViewContext()).setInstructions(3, selectedBranch, specialinstructions);
                } else {
                    scheduledate.setError("Invalid! Schedule Date is required.");
                    scheduledate.requestFocus();

                }

                return true;
            }
            case android.R.id.home: {
                hideKeyboard(getViewContext());
                ((PickUpActivity) getViewContext()).displayView(1, null);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean evaluateForm() {
        return instructions.getText().toString().trim().length() > 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scheduledate: {
                SimpleDateFormat fmt = new SimpleDateFormat("d/M/yyyy");
                dates = new Date();
                java.util.Calendar now = java.util.Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                                if(date.equals(fmt.format(dates))){
                                    Toast.makeText(getViewContext(), "Current date is not allowed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    scheduledate.setText(date);
                                    UserPreferenceManager.savePickupSchedule(getViewContext(), date);
                                }
                            }
                        },
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH) + 1
                );
                dpd.setMinDate(now);
                dpd.show(getActivity().getFragmentManager(), "dialog");
                break;
            }
        }
    }
}
