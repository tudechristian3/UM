package com.ultramega.shop.fragment.register;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.AmazonCredentials;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetS3keyResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.RegisterWholeSalerResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User-PC on 8/23/2017.
 */

public class RegisterWholeSalerFragment extends BaseFragment implements View.OnClickListener {
    //    private TextView countryCode;
    private TextView profileLabel;
    private MaterialEditText edt_business_name;
    private MaterialEditText edt_first_name;
    private MaterialEditText edt_middle_name;
    private MaterialEditText edt_last_name;
    private MaterialEditText edt_country;
    private MaterialEditText edt_province;
    private MaterialEditText edt_city;
    private MaterialEditText edt_streetaddress;
    private MaterialEditText edt_email_address;
    private MaterialEditText edt_mobile_number;
    private MaterialEditText edt_birth_date;
    private MaterialEditText edt_gender;
    private MaterialEditText edt_branch_name;
    private ImageView profile_image;

    private UltramegaShopUtilities mDbUtils;

    private List<String> mCountryCodes;
    private List<String> mCountryCodesFinal;
    private List<Branches> branches;
    private List<String> branchesList;

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String mobilenumber = "";
    private String businessname = "";
    private String firstname = "";
    private String lastname = "";
    private String country = "";
    private String countrycode = "";
    private String streetaddress = "";
    private String city = "";
    private String zip = "";
    private String province = "";
    private String emailaddress = "";
    private String telno = "";
    private String longitude = "";
    private String latitude = "";
    private String profilepic = "";
    private String branchid = "";

    private Address address;

    private Uri imageUri;
    private File imageFile;
    private String imageurl = ".";

    private MaterialDialog mDialog;

    private AmazonCredentials amazonCredentials;
    private String authenticationid = "";
    private String processtype = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_wholesaler, container, false);
        setHasOptionsMenu(true);

        imageUri = null;
        amazonCredentials = null;
        mDbUtils = new UltramegaShopUtilities(getViewContext());
        mobilenumber = getArguments().getString("MobileNumber");
        countrycode = getArguments().getString("CountryCode");
        country = getArguments().getString("Country");
        address = getArguments().getParcelable("address");
        authenticationid = getArguments().getString("AuthenticationID");
        processtype = getArguments().getString("processtype");

        branches = new ArrayList<>();
        branchesList = new ArrayList<>();

        branches = mDbUtils.getBranches();

        if (address != null) {
            streetaddress = address.getThoroughfare() == null ? "" : address.getThoroughfare();
            city = address.getLocality() == null ? "" : address.getLocality();
            province = address.getSubAdminArea() == null ? "" : address.getSubAdminArea();
            zip = address.getPostalCode() == null ? "" : address.getPostalCode();
            //country = address.getCountryName();
            longitude = String.valueOf(address.getLongitude());
            latitude = String.valueOf(address.getLatitude());
        }

        imei = CommonFunctions.getIMEI(getViewContext());
        profilepic = ".";

        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        edt_business_name = (MaterialEditText) view.findViewById(R.id.edtBusinessName);
        edt_email_address = (MaterialEditText) view.findViewById(R.id.edtEmailAddress);
        edt_mobile_number = (MaterialEditText) view.findViewById(R.id.edtMobileNumber);
        edt_first_name = (MaterialEditText) view.findViewById(R.id.edtFirstName);
        edt_last_name = (MaterialEditText) view.findViewById(R.id.edtLastName);
        edt_country = (MaterialEditText) view.findViewById(R.id.edtCountry);
        edt_country.setOnClickListener(this);
        edt_province = (MaterialEditText) view.findViewById(R.id.province);
        edt_city = (MaterialEditText) view.findViewById(R.id.city);
        edt_streetaddress = (MaterialEditText) view.findViewById(R.id.streetaddress);
        edt_middle_name = (MaterialEditText) view.findViewById(R.id.edtMiddleName);
        edt_birth_date = (MaterialEditText) view.findViewById(R.id.edtBirthDate);
        edt_birth_date.setOnClickListener(this);
        edt_gender = (MaterialEditText) view.findViewById(R.id.edtGender);
        edt_gender.setOnClickListener(this);
        edt_branch_name = (MaterialEditText) view.findViewById(R.id.edtBranchName);
        edt_branch_name.setOnClickListener(this);
//        countryCode = (TextView) view.findViewById(R.id.countryCode);
        profileLabel = (TextView) view.findViewById(R.id.profileLabel);
        profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Profile Info"));

        edt_country.setText(country);
        edt_province.setText(province);
        edt_city.setText(city);
        edt_streetaddress.setText(streetaddress);
        edt_mobile_number.setText("+".concat(countrycode.concat(mobilenumber)));
//        countryCode.setText("+".concat(countrycode));

        mCountryCodes = new ArrayList<>();
        mCountryCodesFinal = new ArrayList<>();
        mCountryCodes = Arrays.asList(getResources().getStringArray(R.array.CountryCodes));
        for (String str : mCountryCodes) {
            String[] country = str.split(",");
            mCountryCodesFinal.add(country[2] + ", " + country[0]);
        }

        Log.d("antonhttp", "branches: " + new Gson().toJson(branches));
        for (Branches branch : branches) {
            branchesList.add(branch.getBranchName());
        }
        Log.d("antonhttp", "branchesList: " + new Gson().toJson(branchesList));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_profile, menu);
        Menu mToolbarMenu = menu;
        menu.findItem(R.id.edit_profile).setVisible(false);
        menu.findItem(R.id.edit_profile_done).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                Bundle args = new Bundle();
                args.putString("AuthenticationID", authenticationid);
                args.putString("processtype", processtype);
                args.putString("MobileNumber", mobilenumber);
                args.putString("CountryCode", countrycode);
                args.putString("Country", country);
                ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(5, args);
                //((ConsumerWholesalerSignUpActivity) getViewContext()).setFinalRegistration(5, mobilenumber, countrycode, country);
                return true;
            }
            case R.id.edit_profile_done: {
                if (evaluateFields()) {
                    if (imageUri != null) {
                        createSession(callS3Session);
                    } else {
                        getSession();
                    }
                } else {

                    if (edt_email_address.getText().toString().trim().length() == 0) {
                        edt_email_address.setError("Invalid! Email Address is required.");
                        edt_email_address.requestFocus();
                    }

                    if (edt_branch_name.getText().toString().trim().length() == 0) {
                        edt_branch_name.setError("Invalid! Branch Name is required.");
                        edt_branch_name.requestFocus();
                    }

                    if (edt_business_name.getText().toString().trim().length() == 0) {
                        edt_business_name.setError("Invalid! Business Name is required.");
                        edt_business_name.requestFocus();
                    }

                    if (edt_first_name.getText().toString().trim().length() == 0) {
                        edt_first_name.setError("Invalid! First Name is required.");
                        edt_first_name.requestFocus();
                    }

                    if (edt_last_name.getText().toString().trim().length() == 0) {
                        edt_last_name.setError("Invalid! Last Name is required.");
                        edt_last_name.requestFocus();
                    }

                    if (edt_country.getText().toString().trim().length() == 0) {
                        edt_country.setError("Invalid! Country is required.");
                        edt_country.requestFocus();
                    }

                    if (edt_province.getText().toString().trim().length() == 0) {
                        edt_province.setError("Invalid! Province is required.");
                        edt_province.requestFocus();
                    }

                    if (edt_city.getText().toString().trim().length() == 0) {
                        edt_city.setError("Invalid! City is required.");
                        edt_city.requestFocus();
                    }

                    if (edt_streetaddress.getText().toString().trim().length() == 0) {
                        edt_streetaddress.setError("Invalid! Street Name is required.");
                        edt_streetaddress.requestFocus();
                    }

                    if (edt_mobile_number.getText().toString().trim().length() == 0) {
                        edt_mobile_number.setError("Invalid! Mobile Number is required.");
                        edt_mobile_number.requestFocus();
                    }

                    //openErrorDialog("Please fill all required fields.");
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Callback<GetSessionResponse> callS3Session = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + countrycode.concat(mobilenumber) + sessionid);
                    getS3key(getS3keySession);
                } else {
                    hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void getS3key(Callback<GetS3keyResponse> getS3keyCallback) {
        Call<GetS3keyResponse> getS3key = RetrofitBuild.getS3keyService(getViewContext())
                .getS3keyCall(imei,
                        countrycode.concat(mobilenumber),
                        authcode,
                        sessionid);
        getS3key.enqueue(getS3keyCallback);
    }

    private final Callback<GetS3keyResponse> getS3keySession = new Callback<GetS3keyResponse>() {

        @Override
        public void onResponse(Call<GetS3keyResponse> call, Response<GetS3keyResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                amazonCredentials = response.body().getAmazonCredentials();
                uploadImagetoAWS(imageUri);
            }
        }

        @Override
        public void onFailure(Call<GetS3keyResponse> call, Throwable t) {
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void uploadImagetoAWS(Uri imageUri) {

        imageFile = new File(imageUri.getPath());

        AsyncTask<String, String, String> uploadTask = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    AmazonS3Client s3Client1 = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.getAPIKey(), amazonCredentials.getAPISecretKey()));
                    PutObjectRequest por = new PutObjectRequest(RetrofitBuild.AWS_BUCKETNAME,
                            countrycode.concat(mobilenumber) + "-wholesaler-profile.jpg", imageFile);

                    //making the object Public
                    por.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client1.putObject(por);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageurl = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + countrycode.concat(mobilenumber) + "-wholesaler-profile.jpg";
                return imageurl;
            }

            @Override
            protected void onPostExecute(String s) {
                getSession();
                Log.d("antonhttp", imageurl);
            }
        };
        uploadTask.execute((String[]) null);
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //showProgressDialog();
            //api session
            createSession(callsession);
        } else {
            hideProgressDialog();
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
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    registerWholeSaler(registerWholeSalerSession);
                } else {
                    hideProgressDialog();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                openErrorDialog("Something went wrong. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void registerWholeSaler(Callback<RegisterWholeSalerResponse> registerWholeSalerCallback) {
        Call<RegisterWholeSalerResponse> register = RetrofitBuild.registerWholeSalerService(getViewContext())
                .registerWholeSalerCall(imei,
                        authcode,
                        sessionid,
                        branchid,
                        mobilenumber,
                        edt_business_name.getText().toString().trim(),
                        edt_first_name.getText().toString().trim(),
                        edt_last_name.getText().toString().trim(),
                        edt_middle_name.getText().toString().trim().length() > 0 ? edt_middle_name.getText().toString().trim() : ".",
                        edt_country.getText().toString().trim(),
                        countrycode,
                        imageurl,
                        edt_streetaddress.getText().toString().trim(),
                        edt_city.getText().toString().trim(),
                        edt_gender.getText().toString().trim().length() > 0 ? edt_gender.getText().toString().trim() : ".",
                        edt_province.getText().toString().trim(),
                        edt_email_address.getText().toString().trim().length() > 0 ? edt_email_address.getText().toString().trim() : ".",
                        edt_birth_date.getText().toString().trim().length() > 0 ? edt_birth_date.getText().toString().trim() : CommonFunctions.getDefaultDate());
        register.enqueue(registerWholeSalerCallback);
    }

    private final Callback<RegisterWholeSalerResponse> registerWholeSalerSession = new Callback<RegisterWholeSalerResponse>() {

        @Override
        public void onResponse(Call<RegisterWholeSalerResponse> call, Response<RegisterWholeSalerResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<RegisterWholeSalerResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void evaluateResponse(Response<RegisterWholeSalerResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                mDbUtils.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);
                mDbUtils.insertWholesalerProfile(
                        new WholesalerProfile(
                                ".",
                                ".",
                                ".",
                                countrycode.concat(edt_mobile_number.getText().toString().trim()),
                                ".",
                                imageurl,
                                "PENDING",
                                ".",
                                ".",
                                ".",
                                0,
                                edt_last_name.getText().toString().trim(),
                                edt_first_name.getText().toString().trim(),
                                edt_middle_name.getText().toString().trim(),
                                edt_business_name.getText().toString().trim(),
                                edt_gender.getText().toString().trim(),
                                edt_birth_date.getText().toString().trim(),
                                edt_mobile_number.getText().toString().trim(),
                                edt_email_address.getText().toString().trim(),
                                imei,
                                edt_streetaddress.getText().toString().trim() + " " + edt_city.getText().toString().trim() + " " + edt_province.getText().toString().trim(),
                                edt_city.getText().toString().trim(),
                                edt_province.getText().toString().trim(),
                                0,
                                0,
                                ".",
                                0,
                                ".",
                                "."));

                //set up login to true
                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);

                mDialog = new MaterialDialog.Builder(getViewContext())
                        .cancelable(false)
                        .customView(R.layout.dialog_wholesaler_application_in_process, false)
                        .show();

                View view = mDialog.getCustomView();
                ImageView closeDialog = (ImageView) view.findViewById(R.id.closeDialog);
                closeDialog.setOnClickListener(this);
                break;
            }
            default: {
                hideKeyboard(getViewContext());
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    private boolean evaluateFields() {
        //edt_business_name.addValidator();
        return edt_business_name.getText().toString().trim().length() > 0 &&
                edt_email_address.getText().toString().trim().length() > 0 &&
                edt_branch_name.getText().toString().trim().length() > 0 &&
                edt_first_name.getText().toString().trim().length() > 0 &&
                edt_last_name.getText().toString().trim().length() > 0 &&
                edt_country.getText().toString().trim().length() > 0 &&
                edt_province.getText().toString().trim().length() > 0 &&
                edt_city.getText().toString().trim().length() > 0 &&
                edt_streetaddress.getText().toString().trim().length() > 0 &&
                edt_mobile_number.getText().toString().trim().length() > 0;
    }

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
//                countryCode.setText("+".concat(countrycode));
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtCountry: {
                openCountryDialog();
                break;
            }
            case R.id.profile_image: {
                openSelectModeDialog();
                break;
            }
            case R.id.closeDialog: {
                //getActivity().finish();

                mDialog.dismiss();

                Intent i = new Intent(getViewContext(), ShoppingModeActivity.class);
                i.putExtra("islogin", true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                break;
            }
            case R.id.edtBirthDate: {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                edt_birth_date.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "dialog");
                break;
            }
            case R.id.edtGender: {
                openGenderDialog();
                break;
            }
            case R.id.edtBranchName: {
                openSupplierDialog();
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                onImageResult(result.getUri());
                imageUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("antonhttp", String.valueOf(error));
            }
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            cropImage(data.getData());

        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                cropImage(fileUri);
                fileUri = null;
            } else {
                onImageResult(data.getData());
                //cropImage(data.getData());
            }
        }
    }

    private void onImageResult(Uri uri) {
        profile_image.setImageBitmap(getImageBitmap(uri));
    }

    private void openGenderDialog() {
        new MaterialDialog.Builder(getViewContext())
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

    private void openSupplierDialog() {
        new MaterialDialog.Builder(getViewContext())
                .title("Branches")
                .items(branchesList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.d("antonhttp", "which: " + String.valueOf(which));
                        edt_branch_name.setText(text);
                        branchid = branches.get(which).getBranchID();
                    }
                })
                .show();
    }

}
