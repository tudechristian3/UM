package com.ultramega.shop.fragment.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ultramega.shop.activity.profile.EditProfileActivity;
import com.ultramega.shop.adapter.InterestRecyclerProfileInterestListAdapter;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.AmazonCredentials;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetS3keyResponse;
import com.ultramega.shop.responses.GetSessionResponse;
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

public class ViewProfileFragment extends BaseFragment implements View.OnClickListener {
    private TextView listitem;
    private TextView profileLabel;
    private TextView interestLabel;
    private MaterialEditText edt_first_name;
    private MaterialEditText edt_last_name;
    private MaterialEditText edt_country;
    private MaterialEditText edt_email_address;
    private MaterialEditText edt_mobile_number;
    private MaterialEditText edt_birthdate;
    private MaterialEditText edt_gender;
    private MaterialEditText edt_occupation;

    private CircleImageView profile_image;
    private Menu mToolbarMenu;
    private List<ConsumerBehaviourPattern> mListConsumerBehaviourPattern;
    private RecyclerView recyclerView;
    private InterestRecyclerProfileInterestListAdapter mAdapter;
    private ListView listView;

    private LinearLayout nointerestlayout;
    private LinearLayout layout_profile;
    private TextView nointerestlabel;
    private TextView nointerestsublabel;

    private AmazonCredentials amazonCredentials;
    private UltramegaShopUtilities mDbUtils;

    private Uri imageUri;

    private String authcode;
    private String sessionid;
    private String imei;
    private String mobilenumber;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);

        //hide keyboard
        hideSoftKeyboard();

        try {

            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);

            //initialize empty data
            imei = CommonFunctions.getIMEI(getViewContext());
            imageUri = null;
            amazonCredentials = null;
            mDbUtils = new UltramegaShopUtilities(getViewContext());
            mListConsumerBehaviourPattern = new ArrayList<>();
            mListConsumerBehaviourPattern = mDbUtils.getConsumerBehaviourPatterns();

            nointerestlabel = (TextView) view.findViewById(R.id.nointerestlabel);
            nointerestlabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Tell us what you're into."));
            nointerestsublabel = (TextView) view.findViewById(R.id.nointerestsublabel);
            nointerestsublabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Click on the top-right icon above to add the interest you like."));
            nointerestlayout = (LinearLayout) view.findViewById(R.id.nointerestlayout);
            layout_profile = (LinearLayout) view.findViewById(R.id.linearProfile);
            profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
            profile_image.setOnClickListener(this);
            profileLabel = (TextView) view.findViewById(R.id.profileLabel);
            profileLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Profile Info"));
            interestLabel = (TextView) view.findViewById(R.id.txv_interestLabel);
            interestLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Interest List"));

            listitem = (TextView) view.findViewById(R.id.categoriesNameList);
            edt_email_address = (MaterialEditText) view.findViewById(R.id.edtEmailAddress);
            edt_mobile_number = (MaterialEditText) view.findViewById(R.id.edtMobileNumber);
            edt_first_name = (MaterialEditText) view.findViewById(R.id.edtFirstName);
            edt_last_name = (MaterialEditText) view.findViewById(R.id.edtLastName);
            edt_country = (MaterialEditText) view.findViewById(R.id.edtCountry);
            edt_birthdate = (MaterialEditText) view.findViewById(R.id.edtBirthDate);
            edt_gender = (MaterialEditText) view.findViewById(R.id.edtGender);
            edt_occupation = (MaterialEditText) view.findViewById(R.id.edtOccupation);

            recyclerView = (RecyclerView) view.findViewById(R.id.rv_profile_interest);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            mAdapter = new InterestRecyclerProfileInterestListAdapter(getViewContext(), mListConsumerBehaviourPattern);
            recyclerView.setItemAnimator(new SlideInUpAnimator());
            recyclerView.setAdapter(mAdapter);
            updateBPatterns(mListConsumerBehaviourPattern);

            setProfileInfo(mDbUtils.getConsumerProfile());

//            Glide.with(getViewContext())
//                    .load(mDbUtils.getConsumerProfile().getProfilePicURL())
//                    .apply(RequestOptions.circleCropTransform()
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
//                            .placeholder(R.drawable.profile_image_empty))
//                    .into(profile_image);

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getViewContext())
                    .load(mDbUtils.getConsumerProfile().getProfilePicURL())
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
                    .into(profile_image);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                onImageResult(result.getUri());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageUri = result.getUri();
                        createSession(callS3Session);
                    }
                }, 1000);

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
                    authcode = CommonFunctions.getSha1Hex(imei + mobilenumber + sessionid);
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
                        mobilenumber,
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu);
        mToolbarMenu = menu;
        menu.findItem(R.id.edit_profile).setVisible(true);
        menu.findItem(R.id.edit_profile_done).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
            case R.id.edit_profile: {
                EditProfileActivity.start(getViewContext());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

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

    private void onImageResult(Uri uri) {
//        profile_image.setImageResource(0);
//        profile_image.setImageBitmap(getImageBitmap(uri));
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
                .into(profile_image);
    }

    //populate the form
    private void setProfileInfo(ConsumerProfile profile) {
        String[] code = profile.getCountryCode().split(":::");

        String fname, lname, gender, occupation;

        if (profile.getFirstName() != null) {
            fname = profile.getFirstName().trim().equals(".") ? "" : profile.getFirstName();
        } else {
            fname = "";
        }

        if (profile.getLastName() != null) {
            lname = profile.getLastName().trim().equals(".") ? "" : profile.getLastName();
        } else {
            lname = "";
        }

        if (profile.getGender() != null) {
            gender = profile.getGender().trim().equals(".") ? "" : profile.getGender();
        } else {
            gender = "";
        }

        if (profile.getOccupation() != null) {
            occupation = profile.getOccupation().trim().equals(".") ? "" : profile.getOccupation();
        } else {
            occupation = "";
        }

        edt_first_name.setText(fname);
        edt_last_name.setText(lname);
        edt_email_address.setText(profile.getEmailAddress());
        edt_birthdate.setText(CommonFunctions.convertDate(profile.getBirthDate()));
        edt_gender.setText(gender);
        edt_occupation.setText(occupation);
        mobilenumber = code[0].trim() + profile.getMobileNo();
        edt_mobile_number.setText("+".concat(mobilenumber));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDbUtils != null) {
            setProfileInfo(mDbUtils.getConsumerProfile());
            if (mAdapter != null) {
                List<ConsumerBehaviourPattern> listPatterns = mDbUtils.getConsumerBehaviourPatterns();
                mAdapter.setNotificationsData(mDbUtils.getConsumerBehaviourPatterns());
                if (listPatterns.size() > 0) {
                    //layout_profile.setVisibility(View.VISIBLE);
                    nointerestlayout.setVisibility(View.GONE);
                } else {
                    //layout_profile.setVisibility(View.GONE);
                    nointerestlayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private File imageFile;
    private String imageurl = "";

    private void uploadImagetoAWS(final Uri imageUri) {

        imageFile = new File(imageUri.getPath());
        onImageResult(imageUri);
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
                            mDbUtils.getConsumerProfile().getUserID() + "-profile.jpg", imageFile);

                    //making the object Public
                    por.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client1.putObject(por);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageurl = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + mDbUtils.getConsumerProfile().getUserID() + "-profile.jpg";
                return imageurl;
            }

            @Override
            protected void onPostExecute(String s) {
                // hideProgressDialog();
                //server call to update profpicurl
                createSession(sessionCallback);
            }
        };
        uploadTask.execute((String[]) null);
    }

    private Callback<GetSessionResponse> sessionCallback = new Callback<GetSessionResponse>() {
        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                sessionid = response.body().getSession();
                if (response.body().getStatus().equals("000")) {
                    updateProfilePic();
                } else {
                    showError(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                showError("Something went wrong while uploading your image. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong while uploading your image. Please try again.");
        }
    };

    private void updateProfilePic() {
        Call<GenericResponse> call = RetrofitBuild.updateConsumerProfileService(getViewContext())
                .updateConsumerProfilePic(
                        sessionid,
                        mDbUtils.getConsumerProfile().getConsumerID(),
                        mDbUtils.getConsumerProfile().getUserID(),
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + mDbUtils.getConsumerProfile().getUserID() + sessionid),
                        CommonFunctions.getIMEI(getViewContext()),
                        imageurl);
        call.enqueue(updateProfilePicCallback);
    }

    private Callback<GenericResponse> updateProfilePicCallback = new Callback<GenericResponse>() {
        @Override
        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
            hideProgressDialog();
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                CommonFunctions.isProfPicUpdated = true;
                mDbUtils.updateProfPic(imageurl, mDbUtils.getConsumerProfile().getUserID());
            } else {
                showError("Something went wrong while uploading your image. Please try again.");
            }
        }

        @Override
        public void onFailure(Call<GenericResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Unknown error has occured. Please try again.");
        }
    };

    public void updateBPatterns(List<ConsumerBehaviourPattern> data) {
        if (data.size() > 0) {
            hideSoftKeyboard();
            //layout_profile.setVisibility(View.VISIBLE);
            if (mAdapter != null) {
                mAdapter.setNotificationsData(data);
            }
        } else {
            //layout_profile.setVisibility(View.GONE);
        }
    }

}
