package com.ultramega.shop.fragment.register;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.ConsumerWholesalerSignUpActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.UltramegaQuickTourActivity;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.AmazonCredentials;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.InterestPost;
import com.ultramega.shop.pojo.OrderSkus;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.responses.GetS3keyResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.RegisterConsumerResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpEnterUserProfileFragment extends BaseFragment implements View.OnClickListener {
    private String mobilenumber = "";
    private String authcode = "";
    private String imei = "";
    private String firstname = "";
    private String lastname = "";
    private String countrycode = "";
    private String country = "";
    private String sessionid = "";
    private String interest = "";
    private String otherinterest;
    private String orderskus = "";
    private String facebookid = "";

    private String authenticationid = "";
    private String processtype = "";

    private MaterialEditText edtfirstname;
    private MaterialEditText edtlastname;
    private MaterialEditText edtpassword;
    private MaterialEditText edtconfirmpassword;
    private CircleImageView profile_image;

    private UltramegaShopUtilities mdb;
    private List<OrderSkus> listorders;

    private Uri imageUri;
    private File imageFile;
    private String imageurl = "";

    private AmazonCredentials amazonCredentials;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_final_registration_user_profile, container, false);

        setHasOptionsMenu(true);

        //setup title
        ((ConsumerWholesalerSignUpActivity) getViewContext()).setActionBarTitle("Sign Up");

        //initialize empty data
        imageUri = null;
        amazonCredentials = null;
        listorders = new ArrayList<>();
        List<InterestPost> listinterest = new ArrayList<>();
        mdb = new UltramegaShopUtilities(getViewContext());
        List<ConsumerBehaviourPattern> listPattern = new ArrayList<>();
        listPattern = mdb.getConsumerBehaviourPatterns();
        facebookid = ".";
        interest = ".";

        TextView profileLabel = (TextView) view.findViewById(R.id.profileLabel);
        edtfirstname = (MaterialEditText) view.findViewById(R.id.edtfirstname);
        edtlastname = (MaterialEditText) view.findViewById(R.id.edtlastname);
        edtpassword = (MaterialEditText) view.findViewById(R.id.edtpassword);
        edtconfirmpassword = (MaterialEditText) view.findViewById(R.id.edtconfirmpassword);

        for (int i = 0; i < listPattern.size(); i++) {
            listinterest.add(new InterestPost(listPattern.get(i).getCategoryID(), listPattern.get(i).getCategoryName()));
        }

        if (listPattern.size() > 0) {
            interest = listPattern.get(0).getCategoryName();
        }

        Gson gson = new Gson();
        otherinterest = gson.toJson(listinterest);

        imei = CommonFunctions.getIMEI(getViewContext());

        if (getArguments() != null) {
            mobilenumber = getArguments().getString("MobileNumber");
            countrycode = getArguments().getString("CountryCode");
            country = getArguments().getString("Country");
            authenticationid = getArguments().getString("AuthenticationID");
            processtype = getArguments().getString("processtype");
        }

        profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Profile Info"));

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);


        return view;
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);

                    List<ShoppingCartsQueue> listqueue = mdb.getAllShoppingCartsQueue();

                    for (int i = 0; i < listqueue.size(); i++) {
                        listorders.add(new OrderSkus(listqueue.get(i).getItemCode(),
                                listqueue.get(i).getCatClass(),
                                listqueue.get(i).getQuantity(),
                                listqueue.get(i).getPackageCode(),
                                listqueue.get(i).getItemPicUrl()));
                    }
                    orderskus = new Gson().toJson(listorders);

                    registerConsumer(registerConsumerSession);
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

    private boolean evaluateFormResult() {
        return edtfirstname.getText().toString().trim().length() == 0 ||
                edtlastname.getText().toString().trim().length() == 0 ||
                edtpassword.getText().toString().trim().length() == 0 ||
                edtconfirmpassword.getText().toString().trim().length() == 0;
    }

    private boolean evaluatePassword() {
        return !edtpassword.getText().toString().trim().equals(edtconfirmpassword.getText().toString().trim());
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
            }
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            cropImage(data.getData());

        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                cropImage(fileUri);
                fileUri = null;
            } else {
                onImageResult(data.getData());
            }
        }

    }

    private void onImageResult(Uri uri) {
        profile_image.setImageBitmap(getImageBitmap(uri));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu);
        Menu mToolbarMenu = menu;
        menu.findItem(R.id.edit_profile).setVisible(false);
        menu.findItem(R.id.edit_profile_done).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do s.th.
                openConfirmationDialog("You're about to cancel registration.");
                return true;
            case R.id.edit_profile_done: {
                if (evaluateFormResult()) {

                    if (edtfirstname.getText().toString().trim().length() == 0) {
                        edtfirstname.setError("Invalid! First Name is required.");
                        edtfirstname.requestFocus();
                    }

                    if (edtlastname.getText().toString().trim().length() == 0) {
                        edtlastname.setError("Invalid! Last Name is required.");
                        edtlastname.requestFocus();
                    }

                    if (edtpassword.getText().toString().trim().length() == 0) {
                        edtpassword.setError("Invalid! Password is required.");
                        edtpassword.requestFocus();
                    }

                    if (edtconfirmpassword.getText().toString().trim().length() == 0) {
                        edtconfirmpassword.setError("Invalid! Confirm Password is required.");
                        edtconfirmpassword.requestFocus();
                    }
                    //openErrorDialog("All fields are required");
                } else if (evaluatePassword()) {

                    edtpassword.setError("Invalid! Password is not matched with the confirm password below.");
                    edtpassword.requestFocus();

                    edtconfirmpassword.setError("Invalid! Confirm Password is not matched with the password above.");
                    edtconfirmpassword.requestFocus();

                    //openErrorDialog("Password are not match");
                } else {
                    //process register here
                    //api session
                    if (imageUri != null) {
                        createSession(callS3Session);
                    } else {
                        imageurl = ".";
                        getSession();
                    }
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

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            showProgressDialog();
            createSession(callsession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private void openConfirmationDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Proceed")
                .neutralText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.colorAccent)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Bundle args = new Bundle();
                        args.putString("AuthenticationID", authenticationid);
                        args.putString("processtype", processtype);
                        args.putString("MobileNumber", mobilenumber);
                        args.putString("CountryCode", countrycode);
                        args.putString("Country", country);
                        ((ConsumerWholesalerSignUpActivity) getViewContext()).displayView(3, args);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();
    }

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
                            countrycode.concat(mobilenumber) + "-profile.jpg", imageFile);

                    //making the object Public
                    por.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client1.putObject(por);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageurl = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + countrycode.concat(mobilenumber) + "-profile.jpg";
                return imageurl;
            }

            @Override
            protected void onPostExecute(String s) {
                getSession();
            }
        };
        uploadTask.execute((String[]) null);
    }

    private void registerConsumer(Callback<RegisterConsumerResponse> registerConsumerCallback) {
        firstname = edtfirstname.getText().toString().trim();
        lastname = edtlastname.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();

//        orderskus
        Call<RegisterConsumerResponse> registerconsumer = RetrofitBuild.registerConsumerService(getViewContext())
                .registerConsumerCall(mobilenumber,
                        authcode,
                        imei,
                        firstname,
                        lastname,
                        password,
                        countrycode,
                        country,
                        imageurl,
                        sessionid,
                        interest,
                        facebookid,
                        otherinterest,
                        orderskus,
                        FirebaseInstanceId.getInstance().getToken());
        registerconsumer.enqueue(registerConsumerCallback);
    }

    private final Callback<RegisterConsumerResponse> registerConsumerSession = new Callback<RegisterConsumerResponse>() {

        @Override
        public void onResponse(Call<RegisterConsumerResponse> call, Response<RegisterConsumerResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<RegisterConsumerResponse> call, Throwable t) {
            hideProgressDialog();
            openErrorDialog("Something went wrong. Please try again.");
        }
    };

    private void evaluateResponse(Response<RegisterConsumerResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                hideKeyboard(getViewContext());

                //truncate tables
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);

                //set up consumer profile
                ConsumerProfile consumerProfile = response.body().getConsumerProfile();
                ConsumerProfile item = new ConsumerProfile();
                item.setDateTimeRegistered(consumerProfile.getDateTimeRegistered());
                item.setFirstName(firstname);
                item.setLastName(lastname);
                item.setMobileNo(mobilenumber);
                item.setConsumerID(consumerProfile.getConsumerID());
                item.setProfilePicURL(imageurl);
                item.setUserID(countrycode.concat(mobilenumber));
                item.setGender(".");
                item.setOccupation(".");
                item.setCountryCode((countrycode.concat(":::")).concat(country));
                mdb.insertConsumerProfile(item);

                //set up consumer wallet
                ConsumerWallet itemwallet = new ConsumerWallet();
                itemwallet.setConsumerID(consumerProfile.getConsumerID());
                itemwallet.setCurrencyID("PHP");
                itemwallet.setTotalWallet(0.00);
                itemwallet.setWalletType(".");
                itemwallet.setLastWalletReloadAmount(0.00);
                itemwallet.setLastWalletReloadPreWallet(0.00);
                itemwallet.setLastWalletReloadPostWallet(0.00);
                itemwallet.setLastOrderPaymentAmount(0.00);
                itemwallet.setLastOrderPaymentPreWallet(0.00);
                itemwallet.setLastOrderPaymentPostWallet(0.00);
                itemwallet.setStatus("ACTIVE");
                mdb.insertConsumerWallet(itemwallet);

                //set up login to true
                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);

                UltramegaQuickTourActivity.start(getViewContext(), "CONSUMER");

                //show dialog
//                openRegisterSuccessDialog("Registered successfully!");
                break;
            }
            default: {
                hideKeyboard(getViewContext());
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    private void openRegisterSuccessDialog(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("GO TO CART")
                .neutralText("SHOP AGAIN")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        getActivity().finish();
//                        ViewShoppingCartsActivity.start(getViewContext());

//                        Intent intent = new Intent(getViewContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("index", 1);
//                        startActivity(intent);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 0);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image: {
                openSelectModeDialog();
                break;
            }
        }
    }


}
