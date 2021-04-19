package com.ultramega.shop.fragment.wholesalerProfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.profile.EditWholesalerProfileActivity;
import com.ultramega.shop.adapter.InterestRecyclerProfileInterestListAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.AmazonCredentials;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetS3keyResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.RegisterWholeSalerResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 06/09/2017.
 */

public class WholeSalerProfileUpdateFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView profileimage;
    private TextView profileLabel;
    private MaterialEditText edtBranchName;
    private MaterialEditText businessName;
    private MaterialEditText firstName;
    private MaterialEditText middleName;
    private MaterialEditText lastName;
    private MaterialEditText country;
    private MaterialEditText province;
    private MaterialEditText city;
    private MaterialEditText streetAddress;
    private MaterialEditText emailAddress;
    private TextView countryCode;
    private MaterialEditText mobileNumber;
    private MaterialEditText birthDate;
    private MaterialEditText gender;

    private UltramegaShopUtilities mdb;
    private WholesalerProfile wholesalerProfile;

    private String wholesalerid = "";
    private String userid = "";
    private String authcode = "";
    private String imei = "";
    private String sessionid = "";
    private String profilepic = "";

    //interest layout
    private LinearLayout linearProfile;
    private LinearLayout nointerestlayout;
    private RecyclerView recyclerView;
    private TextView txvInterestLabel;
    private TextView txvUpdate;
    private TextView nointerestlabel;
    private TextView nointerestsublabel;
    private InterestRecyclerProfileInterestListAdapter mAdapter;
    private List<ConsumerBehaviourPattern> mPatterns;

    private AmazonCredentials amazonCredentials;
    private Uri imageUri;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_wholesaler, container, false);

        try {

            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);

            nointerestlabel = (TextView) view.findViewById(R.id.nointerestlabel);
            nointerestlabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us what you're into."));
            nointerestsublabel = (TextView) view.findViewById(R.id.nointerestsublabel);
            nointerestsublabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Click on the Add+ to add the interest you like."));
            linearProfile = (LinearLayout) view.findViewById(R.id.linearProfile);
            nointerestlayout = (LinearLayout) view.findViewById(R.id.nointerestlayout);
            txvUpdate = (TextView) view.findViewById(R.id.txvUpdate);
            txvUpdate.setOnClickListener(this);
            txvInterestLabel = (TextView) view.findViewById(R.id.txvInterestLabel);
            txvInterestLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Interest List"));
            profileimage = (CircleImageView) view.findViewById(R.id.profile_image);
            profileimage.setOnClickListener(this);
            profileLabel = (TextView) view.findViewById(R.id.profileLabel);
            profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Profile Info"));

            edtBranchName = (MaterialEditText) view.findViewById(R.id.edtBranchName);
            edtBranchName.setEnabled(false);
            businessName = (MaterialEditText) view.findViewById(R.id.edtBusinessName);
            businessName.setEnabled(false);
            firstName = (MaterialEditText) view.findViewById(R.id.edtFirstName);
            firstName.setEnabled(false);
            middleName = (MaterialEditText) view.findViewById(R.id.edtMiddleName);
            middleName.setEnabled(false);
            lastName = (MaterialEditText) view.findViewById(R.id.edtLastName);
            lastName.setEnabled(false);
            country = (MaterialEditText) view.findViewById(R.id.edtCountry);
            country.setEnabled(false);
            province = (MaterialEditText) view.findViewById(R.id.province);
            province.setEnabled(false);
            city = (MaterialEditText) view.findViewById(R.id.city);
            city.setEnabled(false);
            streetAddress = (MaterialEditText) view.findViewById(R.id.streetaddress);
            streetAddress.setEnabled(false);
            emailAddress = (MaterialEditText) view.findViewById(R.id.edtEmailAddress);
            emailAddress.setEnabled(false);
            countryCode = (TextView) view.findViewById(R.id.countryCode);
            mobileNumber = (MaterialEditText) view.findViewById(R.id.edtMobileNumber);
            mobileNumber.setEnabled(false);
            birthDate = (MaterialEditText) view.findViewById(R.id.edtBirthDate);
            birthDate.setEnabled(false);
            gender = (MaterialEditText) view.findViewById(R.id.edtGender);
            gender.setEnabled(false);

            mdb = new UltramegaShopUtilities(getViewContext());
            amazonCredentials = null;
            imageUri = null;
            mPatterns = new ArrayList<>();
            mPatterns = mdb.getWholesalerBehaviourPatterns();
            if (mPatterns.size() > 0) {
                nointerestlayout.setVisibility(View.GONE);
                txvUpdate.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Edit"));
            } else {
                nointerestlayout.setVisibility(View.VISIBLE);
                txvUpdate.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Add+"));
            }

            wholesalerProfile = mdb.getWholesalerProfile();

            wholesalerid = wholesalerProfile.getWholesalerID();
            profilepic = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + "63".concat(wholesalerProfile.getMobileNo()) + "-wholesaler-profile.jpg";
            imei = CommonFunctions.getIMEI(getViewContext());
            userid = wholesalerProfile.getUserID();

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            mAdapter = new InterestRecyclerProfileInterestListAdapter(getViewContext(), mdb.getWholesalerBehaviourPatterns());
            recyclerView.setItemAnimator(new SlideInUpAnimator());
            recyclerView.setAdapter(mAdapter);
            updateBPatterns(mdb.getWholesalerBehaviourPatterns());

            //=============================
            //SETUP WHOLESALER PROFILE DATA
            //=============================
            setWholeSalerProfile(wholesalerProfile);

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(wholesalerProfile.getProfilePicURL())
                    .error(Glide.with(getContext())
                            .load(R.drawable.ic_um_default_products))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(RequestOptions.circleCropTransform()
                            .fitCenter()
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                    .into(profileimage);

//            Glide.with(getViewContext())
//                    .load(wholesalerProfile.getProfilePicURL())
//                    .apply(RequestOptions.circleCropTransform()
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
//                            .placeholder(R.drawable.profile_image_empty))
//                    .into(profileimage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mdb != null) {
            if (mAdapter != null) {
                List<ConsumerBehaviourPattern> listPatterns = mdb.getWholesalerBehaviourPatterns();
                mAdapter.setNotificationsData(listPatterns);
                if (listPatterns.size() > 0) {
                    nointerestlayout.setVisibility(View.GONE);
                    txvUpdate.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Edit"));
                } else {
                    nointerestlayout.setVisibility(View.VISIBLE);
                    txvUpdate.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Add+"));
                }
            }
        }
    }

    public void updateBPatterns(List<ConsumerBehaviourPattern> data) {
        if (data.size() > 0) {
            hideSoftKeyboard();
            recyclerView.setVisibility(View.VISIBLE);
            if (mAdapter != null) {
                mAdapter.setNotificationsData(data);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setWholeSalerProfile(WholesalerProfile wholeSalerProfile) {
        try {

            if (wholeSalerProfile.getBranchName() != null) {
                edtBranchName.setText(wholeSalerProfile.getBranchName().equals(".") || wholesalerProfile.getBranchName().isEmpty() ? "" : wholeSalerProfile.getBranchName());
            } else {
                edtBranchName.setText("");
            }

            if (wholeSalerProfile.getCompany() != null) {
                businessName.setText(wholeSalerProfile.getCompany().equals(".") || wholesalerProfile.getCompany().isEmpty() ? "" : wholeSalerProfile.getCompany());
            } else {
                businessName.setText("");
            }

            if (wholeSalerProfile.getFirstName() != null) {
                firstName.setText(wholeSalerProfile.getFirstName().equals(".") || wholesalerProfile.getFirstName().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getFirstName()));
            } else {
                firstName.setText("");
            }

            if (wholeSalerProfile.getMiddleName() != null) {
                middleName.setText(wholeSalerProfile.getMiddleName().equals(".") || wholesalerProfile.getMiddleName().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getMiddleName()));
            } else {
                middleName.setText("");
            }

            if (wholeSalerProfile.getLastName() != null) {
                lastName.setText(wholeSalerProfile.getLastName().equals(".") || wholesalerProfile.getLastName().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getLastName()));
            } else {
                lastName.setText("");
            }

            country.setText("Philippines");

            if (wholeSalerProfile.getProvince() != null) {
                province.setText(wholeSalerProfile.getProvince().equals(".") || wholesalerProfile.getProvince().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getProvince()));
            } else {
                province.setText("");
            }


            if (wholeSalerProfile.getCity() != null) {
                city.setText(wholeSalerProfile.getCity().equals(".") || wholesalerProfile.getCity().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getCity()));
            } else {
                city.setText("");
            }

            if (wholeSalerProfile.getAddress() != null) {
                streetAddress.setText(wholeSalerProfile.getAddress().equals(".") || wholesalerProfile.getAddress().isEmpty() ? "" : capitalizeWord(wholeSalerProfile.getAddress()));
            } else {
                streetAddress.setText("");
            }

            if (wholeSalerProfile.getEmailAddress() != null) {
                emailAddress.setText(wholeSalerProfile.getEmailAddress().equals(".") || wholesalerProfile.getEmailAddress().isEmpty() ? "" : wholeSalerProfile.getEmailAddress());
            } else {
                emailAddress.setText("");
            }

            if (wholeSalerProfile.getMobileNo() != null) {
                mobileNumber.setText(wholeSalerProfile.getMobileNo().equals(".") || wholesalerProfile.getMobileNo().isEmpty() ? "" : wholeSalerProfile.getMobileNo());
            } else {
                mobileNumber.setText("");
            }

            if (wholeSalerProfile.getBirthdate() != null) {
                birthDate.setText(CommonFunctions.convertDate(wholeSalerProfile.getBirthdate()));
            } else {
                birthDate.setText("");
            }

            if (wholeSalerProfile.getGender() != null) {
                gender.setText(wholeSalerProfile.getGender().equals(".") || wholesalerProfile.getGender().isEmpty() ? "" : wholeSalerProfile.getGender());
            } else {
                gender.setText("");
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image: {
                openSelectModeDialog();
                break;
            }
            case R.id.txvUpdate: {
                EditWholesalerProfileActivity.start(getViewContext());
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
                createSession(callS3Session);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            cropImage(data.getData());

        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                Log.d("antonhttp", fileUri.toString());
                cropImage(fileUri);
                fileUri = null;
            } else {
                cropImage(data.getData());
            }
        }

    }

    private final Callback<GetSessionResponse> callS3Session = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + "63".concat(wholesalerProfile.getMobileNo()) + sessionid);
                    getS3key(getS3keySession);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };

    private void getS3key(Callback<GetS3keyResponse> getS3keyCallback) {
        Call<GetS3keyResponse> getS3key = RetrofitBuild.getS3keyService(getViewContext())
                .getS3keyCall(imei,
                        "63".concat(wholesalerProfile.getMobileNo()),
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
            if (isAdded()) {
                openErrorDialog("Error in connection. Please contact support.");
            }
        }
    };

    private void onImageResult(Uri uri) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getViewContext())
                .load(getImageBitmap(uri))
                .error(Glide.with(getViewContext())
                        .load(R.drawable.ic_um_default_products))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(RequestOptions.circleCropTransform()
                        .fitCenter()
                        .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                        .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                .into(profileimage);
//        profileimage.setImageBitmap(getImageBitmap(uri));
    }

    private File imageFile;
    private String imageurl = "";

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
                            "63".concat(wholesalerProfile.getMobileNo()) + "-wholesaler-profile.jpg", imageFile);

                    //making the object Public
                    por.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client1.putObject(por);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageurl = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + "63".concat(wholesalerProfile.getMobileNo()) + "-wholesaler-profile.jpg";
                return imageurl;
            }

            @Override
            protected void onPostExecute(String s) {
                // hideProgressDialog();
                //server call to update profpicurl
                getSession();
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
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);
                    updateWholeSalerProfilePic(wholeSalerProfilePicSessionCallBack);
                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };

    private void updateWholeSalerProfilePic(Callback<RegisterWholeSalerResponse> updateWholeSalerProfilePicCallBack) {
        Call<RegisterWholeSalerResponse> wholesalerprofilepic = RetrofitBuild.registerWholeSalerService(getViewContext())
                .updateWholesalerProfilePicCall(wholesalerid,
                        userid,
                        authcode,
                        imei,
                        sessionid,
                        profilepic);
        wholesalerprofilepic.enqueue(updateWholeSalerProfilePicCallBack);
    }

    private final Callback<RegisterWholeSalerResponse> wholeSalerProfilePicSessionCallBack = new Callback<RegisterWholeSalerResponse>() {

        @Override
        public void onResponse(Call<RegisterWholeSalerResponse> call, Response<RegisterWholeSalerResponse> response) {
            ResponseBody errorBody = response.errorBody();
            hideProgressDialog();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    CommonFunctions.isProfPicUpdated = true;
                    mdb.updateWholesalerProfPic(imageurl, wholesalerProfile.getMobileNo());
                    Log.d("tonyt", mdb.getWholesalerProfile().getProfilePicURL());

                } else {
                    hideProgressDialog();
                    if (isAdded()) {
                        openErrorDialog(response.body().getMessage());
                    }
                }
            } else {
                hideProgressDialog();
                if (isAdded()) {
                    openErrorDialog("Something went wrong. Please try again.");
                }
            }
        }

        @Override
        public void onFailure(Call<RegisterWholeSalerResponse> call, Throwable t) {
            hideProgressDialog();
            if (isAdded()) {
                openErrorDialog("Something went wrong. Please try again.");
            }
        }
    };

}