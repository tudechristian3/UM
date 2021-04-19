package com.ultramega.shop.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.InterestRecyclerAdapter;
import com.ultramega.shop.adapter.InterestRecyclerProfileInterestListAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.FetchShopCategoriesResponse;
import com.ultramega.shop.responses.GetAccountInformationResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.UpdateConsumerProfileResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ban_Lenovo on 8/9/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //private TextView countryCode;
    private TextView interestLabel;
    private MaterialEditText edt_first_name;
    private MaterialEditText edt_last_name;
    private MaterialEditText edt_country;
    private MaterialEditText edt_email_address;
    private MaterialEditText edt_mobile_number;
    private MaterialEditText edt_birthdate;
    private MaterialEditText edt_gender;
    private MaterialEditText edt_occupation;

    private UltramegaShopUtilities mDbUtils;

    private String imei = "";
    private String usertype = "";
    private String itemtype = "";
    private String authcode = "";
    private String sessionid = "";
    private String consumerid = "";
    private String userid = "";
    private String countrycode = "";
    private String firstname = "";
    private String lastname = "";
    private String country = "";
    private String birthdate = "";
    private String gender = "";
    private String occupation = "";
    private String interest = "";
    private String emailaddress = "";
    private String mobilenumber = "";

    private List<ConsumerBehaviourPattern> mPatterns;
    private List<Category> mCategories;
    private List<String> mCountryCodes;
    private List<String> mCountryCodesFinal;
    private List<ConsumerProfile> listprofile;
    private List<ConsumerBehaviourPattern> behaviourPatterns;
    private InterestRecyclerAdapter interestAdapter;
    private InterestRecyclerProfileInterestListAdapter mAdapter;
    private String checkList = "";
    private ImageView image;
    private LinearLayout categoriesLayout;

    private NestedScrollView nestedScrollView;
    private boolean isloadmore = true;
    private boolean isbottomscroll = false;
    private int limit;
    private String pricegroup = "";

    private LinearLayout mSmoothProgressBar;

    private boolean isfirstload = true;
    private boolean isLoading = false;

    private TextView nointerestlabel;
    private TextView nointerestsublabel;

    private ConsumerProfile itemProf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }

    private void initData() {
        mDbUtils = new UltramegaShopUtilities(getViewContext());
        mCountryCodes = new ArrayList<>();
        mCountryCodesFinal = new ArrayList<>();
        listprofile = new ArrayList<>();

        mCategories = new ArrayList<>();
        mPatterns = new ArrayList<>();

        mCategories = mDbUtils.getCategories();
        mPatterns = mDbUtils.getConsumerBehaviourPatterns();

        mCountryCodes = Arrays.asList(getResources().getStringArray(R.array.CountryCodes));
        for (String str : mCountryCodes) {
            String[] country = str.split(",");
            mCountryCodesFinal.add(country[2] + ", " + country[0]);
        }

        if (mPatterns.size() > 0) {
            interest = mPatterns.get(0).getCategoryName();
        } else {
            interest = ".";
        }

        itemProf = mDbUtils.getConsumerProfile();
        //==================
        //apply profile data
        //==================
        setProfileInfo(itemProf);
        imei = CommonFunctions.getIMEI(getViewContext());
        consumerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
        usertype = getCurrentUserType(getViewContext());
        limit = getLimit(mCategories.size(), 30);

        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
        } else if (usertype.equals("WHOLESALER")) {
            if (mDbUtils.getWholesalerID() != null) {
                pricegroup = mDbUtils.getWholesalerProfile().getPriceGroup();
            } else {
                pricegroup = ".";
            }
        }
    }

    private void init() {
        setUpToolBar();

        //hide keyboard
        hideSoftKeyboard();

        nointerestlabel = (TextView) findViewById(R.id.nointerestlabel);
        nointerestlabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us what you're into."));
        nointerestsublabel = (TextView) findViewById(R.id.nointerestsublabel);
        nointerestsublabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Tap once on the interest you like, or tap again the ones you don't."));
        mSmoothProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        edt_email_address = (MaterialEditText) findViewById(R.id.edtEmailAddress);
        edt_mobile_number = (MaterialEditText) findViewById(R.id.edtMobileNumber);
        edt_mobile_number.setEnabled(false);
        edt_first_name = (MaterialEditText) findViewById(R.id.edtFirstName);
        edt_last_name = (MaterialEditText) findViewById(R.id.edtLastName);
        edt_country = (MaterialEditText) findViewById(R.id.edtCountry);
        edt_country.setOnClickListener(this);
        edt_birthdate = (MaterialEditText) findViewById(R.id.edtBirthDate);
        edt_birthdate.setOnClickListener(this);
        edt_gender = (MaterialEditText) findViewById(R.id.edtGender);
        edt_gender.setOnClickListener(this);
        edt_occupation = (MaterialEditText) findViewById(R.id.edtOccupation);
        interestLabel = (TextView) findViewById(R.id.interestLabel);
        interestLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Interest List"));
        edt_first_name.requestFocus();
        image = (ImageView) findViewById(R.id.img_interest);
        categoriesLayout = (LinearLayout) findViewById(R.id.categoriesLayout);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_interest);

        //initialize empty data
        initData();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.update_interest_edit);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        interestAdapter = new InterestRecyclerAdapter(getViewContext(), mDbUtils.getCategories(), mPatterns, usertype);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(interestAdapter);
        updateList(mDbUtils.getCategories());

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    isbottomscroll = true;
                    if (isloadmore) {
                        if (!isfirstload) {
                            limit = limit + 30;
                        }
                        getSessionInterest();
                    }
                }
            }
        });
    }

    private void updateList(List<Category> data) {
        if (interestAdapter != null) {
            if (mDbUtils != null) {
                if (!isLoading) {
                    interestAdapter.setCategoriesData(data);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        menu.findItem(R.id.edit_profile).setVisible(false);
        menu.findItem(R.id.edit_profile_done).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_done:
                //getSession();
                checkEmpty();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void setProfileInfo(ConsumerProfile profile) {
        String[] code = profile.getCountryCode().split(":::");
        String fname = profile.getFirstName().trim().equals(".") ? "" : profile.getFirstName();
        String lname = profile.getLastName().trim().equals(".") ? "" : profile.getLastName();
        String gender = profile.getGender().trim().equals(".") ? "" : profile.getGender();
        String occupation = profile.getOccupation().trim().equals(".") ? "" : profile.getOccupation();
        edt_first_name.setText(fname);
        edt_last_name.setText(lname);

        edt_email_address.setText(profile.getEmailAddress());
        countrycode = code[0].trim();
        edt_country.setText(code[1].trim().equals(".") ? "" : code[1].trim());
        edt_birthdate.setText(CommonFunctions.convertDate(profile.getBirthDate()));
        edt_gender.setText(gender);
        edt_occupation.setText(occupation);
        edt_mobile_number.setText(profile.getMobileNo());
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);

            mobilenumber = edt_mobile_number.getText().toString().trim();
            firstname = edt_first_name.getText().toString().trim();
            lastname = edt_last_name.getText().toString().trim();
            country = edt_country.getText().toString().trim();
            birthdate = edt_birthdate.getText().toString().trim();
            gender = edt_gender.getText().toString().trim();
            occupation = edt_occupation.getText().toString().trim();
            emailaddress = edt_email_address.getText().toString().trim();

            //api session
            createSession(callsession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);
                    updateConsumerProfile(updateConsumerProfileSession);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                showError("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private final Callback<GetSessionResponse> callinfosession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getAccountInformation(getAccountInformationSession);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                showError("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getAccountInformation(Callback<GetAccountInformationResponse> getAccountInformationCallback) {
        Call<GetAccountInformationResponse> accountinfo = RetrofitBuild.getAccountInformationService(getViewContext()).
                getAccountInformationCall(consumerid,
                        userid,
                        authcode,
                        imei,
                        sessionid);
        accountinfo.enqueue(getAccountInformationCallback);
    }

    private final Callback<GetAccountInformationResponse> getAccountInformationSession = new Callback<GetAccountInformationResponse>() {

        @Override
        public void onResponse(Call<GetAccountInformationResponse> call, Response<GetAccountInformationResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mDbUtils.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);

                    listprofile = response.body().getListProfile();
                    for (ConsumerProfile profile : listprofile) {
                        mDbUtils.insertConsumerProfile(profile);
                    }

                    finish();

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetAccountInformationResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog(getString(R.string.generic_internet_error_message));
        }
    };

    private void updateConsumerProfile(Callback<UpdateConsumerProfileResponse> updateConsumerProfileCallback) {
        Call<UpdateConsumerProfileResponse> updateprofile = RetrofitBuild.updateConsumerProfileService(getViewContext()).
                updateConsumerProfileCall(sessionid,
                        consumerid,
                        userid,
                        mobilenumber,
                        authcode,
                        imei,
                        firstname,
                        lastname,
                        countrycode,
                        country,
                        birthdate,
                        gender,
                        occupation,
                        interest,
                        emailaddress);
        updateprofile.enqueue(updateConsumerProfileCallback);
    }

    private final Callback<UpdateConsumerProfileResponse> updateConsumerProfileSession = new Callback<UpdateConsumerProfileResponse>() {

        @Override
        public void onResponse(Call<UpdateConsumerProfileResponse> call, Response<UpdateConsumerProfileResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    createSession(callinfosession);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<UpdateConsumerProfileResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog(getString(R.string.generic_internet_error_message));
        }
    };

    private void openCountryDialog() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getViewContext());
        dialog.title("Country");
        dialog.items(mCountryCodesFinal);
        dialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                String[] country = text.toString().split(",");
                edt_country.setText(country[0].trim());
                countrycode = country[1].trim();
                //countryCode.setText("+".concat(countrycode));
            }
        });
        dialog.show();
    }

    private void openGenderDialog() {
        new MaterialDialog.Builder(this)
                .title("Gender")
                .items(R.array.gender_array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        edt_gender.setText(text);
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtBirthDate: {
                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditProfileActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                Log.d("antonhttp", "now: " + String.valueOf(now));

                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            }
            case R.id.edtGender: {
                openGenderDialog();
                break;
            }
            case R.id.edtCountry: {
                openCountryDialog();
                break;
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        edt_birthdate.setText(date);
    }

    public void updateListNotification(List<ConsumerBehaviourPattern> data) {
        if (data.size() > 0) {
            hideSoftKeyboard();
            mAdapter.setNotificationsData(data);
        }
    }

    public void checkEmpty() {
        final String email = edt_email_address.getText().toString();

        if (edt_first_name.getText().toString().length() != 0 &&
                edt_last_name.getText().toString().length() != 0 &&
                isValidEmail(email) &&
                edt_email_address.getText().toString().length() != 0 &&
                edt_birthdate.getText().toString().length() != 0 &&
                edt_gender.getText().toString().length() != 0 &&
                edt_occupation.getText().toString().length() != 0) {
            getSession();

            //update behaviour patterns
            if (interestAdapter != null) {
                interestAdapter.insertCategories();
            }
            //updateListNotification(mDbUtils.getConsumerBehaviourPatterns());

        } else {

            if (edt_first_name.getText().toString().length() == 0) {
                edt_first_name.setError("Invalid! Firstname required.*");
                edt_first_name.requestFocus();
            }
            if (edt_last_name.getText().toString().length() == 0) {
                edt_last_name.setError("Invalid! Lastname required.*");
                edt_last_name.requestFocus();
            }
            if (!isValidEmail(email)) {
                edt_email_address.setError("Invalid Email Address Format.*");
                edt_email_address.requestFocus();
            }
            if (edt_email_address.getText().toString().length() == 0) {
                edt_email_address.setError("Invalid! Email Address required.*");
                edt_email_address.requestFocus();
            }
            if (edt_birthdate.getText().toString().length() == 0) {
                edt_birthdate.setError("Invalid! Birthdate required.*");
                edt_birthdate.requestFocus();
            }
            if (edt_gender.getText().toString().length() == 0) {
                edt_gender.setError("Invalid! Gender required.*");
                edt_gender.requestFocus();
            }
            if (edt_occupation.getText().toString().length() == 0) {
                edt_occupation.setError("Invalid! Occupation required.*");
                edt_occupation.requestFocus();
            }
        }
    }

    //validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void getAllCategories() {
        Call<FetchShopCategoriesResponse> callcategories = RetrofitBuild.fetchShopCategoriesService(getViewContext())
                .fetchShopCategoriesCall(
                        imei,
                        usertype,
                        CommonFunctions.getSha1Hex(imei + session),
                        String.valueOf(limit),
                        session,
                        pricegroup);
        callcategories.enqueue(getAllCategoriesCallback);
    }

    private Callback<FetchShopCategoriesResponse> getAllCategoriesCallback = new Callback<FetchShopCategoriesResponse>() {
        @Override
        public void onResponse(Call<FetchShopCategoriesResponse> call, Response<FetchShopCategoriesResponse> response) {
            ResponseBody errBody = response.errorBody();

            mSmoothProgressBar.setVisibility(View.GONE);
            isfirstload = false;
            isLoading = false;

            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getCategories().size() > 0;
                    if (!isbottomscroll) {
                        mDbUtils.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
                    }
                    List<Category> categories = response.body().getCategories();
                    for (Category category : categories) {
                        mDbUtils.insertAllCategories(category);
                    }

                    updateList(mDbUtils.getCategories());

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                openErrorDialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<FetchShopCategoriesResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong with the server. Please try again.");
        }
    };

    private final Callback<GetSessionResponse> callsessionIntereset = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {

            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    session = response.body().getSession();
                    getAllCategories();
                } else {
                    isLoading = false;
                    mSmoothProgressBar.setVisibility(View.GONE);
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                isLoading = false;
                mSmoothProgressBar.setVisibility(View.GONE);
                openErrorDialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            isLoading = false;
            mSmoothProgressBar.setVisibility(View.GONE);
            openErrorDialog("Something went wrong with the server. Please try again.");
        }
    };

    private void getSessionInterest() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            isLoading = true;

            createSession(callsessionIntereset);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }
}
