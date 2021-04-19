package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.AccountNumberReference;
import com.ultramega.shop.pojo.AmazonCredentials;
import com.ultramega.shop.pojo.BankNameReference;
import com.ultramega.shop.pojo.BankReference;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.responses.GetBankReferenceResponse;
import com.ultramega.shop.responses.GetConsumerWalletResponse;
import com.ultramega.shop.responses.GetS3keyResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.ReloadConsumerWalletResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.NumberTextWatcherForThousand;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReloadWalletActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private UltramegaShopUtilities mdb;
    private List<AccountNumberReference> bank_number_array;
    private List<BankNameReference> banknamerefs;
    private FrameLayout frame_image;
    private ImageView receiptImage, emptyImage;
    private TextView txvChangeImage, txvTapImage;
    private MaterialEditText banktransno;
    private MaterialEditText amount;
    private MaterialEditText remarks;
    private MaterialEditText depositdate;
    private MaterialEditText edtBankName;
    private MaterialEditText edtBankAccountNo;

    private String consumerid = "";
    private String userid = "";
    private String authcode = "";
    private String imei = "";
    private String sessionid = "";
    private String consumername = "";
    private String customerremarks = "";
    private String paymentoption = "";
    private String banktxnnumber = "";
    private String bankname = "";
    private String bankcode = "";
    private String bankaccountname = "";
    private String bankaccountnumber = "";
    private String depositdatetime = "";
    private String amountpaid = "";

    private Uri imageUri;
    private File imageFile;
    private String imageurl = "";
    private String fileName = "";

    private AmazonCredentials amazonCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload_wallet);
        setUpToolBar();

        edtBankName = (MaterialEditText) findViewById(R.id.bankname);
        edtBankName.setOnClickListener(this);
        edtBankAccountNo = (MaterialEditText) findViewById(R.id.bankaccountno);
        edtBankAccountNo.setOnClickListener(this);
        edtBankAccountNo.setEnabled(false);
        banktransno = (MaterialEditText) findViewById(R.id.banktransno);
        amount = (MaterialEditText) findViewById(R.id.amount);
        amount.addTextChangedListener(new NumberTextWatcherForThousand(amount));
        remarks = (MaterialEditText) findViewById(R.id.remarks);
        depositdate = (MaterialEditText) findViewById(R.id.depositdate);
        depositdate.setOnClickListener(this);
        txvChangeImage = (TextView) findViewById(R.id.changeImage);
        txvChangeImage.setOnClickListener(this);
        txvChangeImage.setVisibility(View.GONE);
        txvTapImage = (TextView) findViewById(R.id.tapImage);
        frame_image = (FrameLayout) findViewById(R.id.forPaymentFrameImage);
        frame_image.setOnClickListener(this);
        Button btnProceed = (Button) findViewById(R.id.forPaymentConfirm);
        btnProceed.setOnClickListener(this);
        btnProceed.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Confirm"));
        receiptImage = (ImageView) findViewById(R.id.forPaymentReceipt);
        receiptImage.setVisibility(View.GONE);
        emptyImage = (ImageView) findViewById(R.id.emptyReceipt);

        //initialize empty data
        imageUri = null;
        amazonCredentials = null;
        ConsumerProfile itemProf = null;
        mdb = new UltramegaShopUtilities(getViewContext());
        itemProf = mdb.getConsumerProfile();
        bank_number_array = new ArrayList<>();
        banknamerefs = new ArrayList<>();
        banknamerefs = mdb.getBankNameReferences();

        imei = CommonFunctions.getIMEI(getViewContext());
        consumerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
        consumername = itemProf.getFirstName() + " " + itemProf.getLastName();
        paymentoption = "BANK";

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Reload Wallet"));
    }

    private final Callback<GetSessionResponse> getbanksession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);

                    getBankReference(getBankReferenceSession, imei, authcode, sessionid);
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ReloadWalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forPaymentFrameImage: {
                openSelectModeDialog();
                break;
            }
            case R.id.changeImage: {
                openSelectModeDialog();
                break;
            }
            case R.id.forPaymentConfirm: {
                if (evaluateFormResult()) {
                    openErrorDialog("All fields are required");
                } else {
                    amountpaid = amount.getText().toString().trim().replace(",", "");
                    customerremarks = remarks.getText().toString().trim().length() == 0 ? "." : remarks.getText().toString().trim();
                    banktxnnumber = banktransno.getText().toString().trim();
                    depositdatetime = depositdate.getText().toString().trim();
                    fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());

                    if (imageUri != null) {
                        createSession(callS3Session);
                    } else {
                        //api session
                        createSession(callsession);
                    }
                }
                break;
            }
            case R.id.depositdate: {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReloadWalletActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)

                );
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            }
            case R.id.bankname: {
                openBankDialog();
                break;
            }
            case R.id.bankaccountno: {
                openBankAccountNoDialog();
                break;
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
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);
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
                        userid,
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
                progressDialog();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    AmazonS3Client s3Client1 = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.getAPIKey(), amazonCredentials.getAPISecretKey()));
                    PutObjectRequest por = new PutObjectRequest(RetrofitBuild.AWS_BUCKETNAME,
                            fileName + "-wallet.jpg", imageFile);

                    //making the object Public
                    por.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client1.putObject(por);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageurl = "https://s3-us-west-1.amazonaws.com/" + RetrofitBuild.AWS_BUCKETNAME + "/" + fileName + "-wallet.jpg";
                return imageurl;
            }

            @Override
            protected void onPostExecute(String s) {
                //api session
                getSession();
            }
        };
        uploadTask.execute((String[]) null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                onImageResult(result.getUri());
                imageUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {

            cropImage(data.getData());

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            if (data == null) {
                cropImage(fileUri);
                fileUri = null;
            } else {
                onImageResult(data.getData());
            }

        }
    }

    private void cropImage(Uri uri) {
        CropImage.activity(uri)
                .setActivityTitle("Crop Image")
                .setAutoZoomEnabled(true)
                .setBorderCornerThickness(0)
                .setOutputCompressQuality(75)
                .setActivityMenuIconColor(R.color.colorWhite)
                .setAllowRotation(true)
                .setAllowFlipping(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setFixAspectRatio(false)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .start(this);
    }

    private Bitmap getImageBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void onImageResult(Uri uri) {
        emptyImage.setVisibility(View.GONE);
        frame_image.setVisibility(View.GONE);
        txvTapImage.setVisibility(View.GONE);
        receiptImage.setVisibility(View.VISIBLE);
        txvChangeImage.setVisibility(View.VISIBLE);

        receiptImage.setImageBitmap(getImageBitmap(uri));
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
                    reloadConsumerWallet(reloadConsumerWalletSession,
                            consumerid,
                            userid,
                            authcode,
                            imei,
                            sessionid,
                            consumername,
                            customerremarks,
                            paymentoption,
                            imageurl,
                            banktxnnumber,
                            bankname,
                            bankcode,
                            bankaccountname,
                            bankaccountnumber,
                            depositdatetime,
                            amountpaid);
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

    private void reloadConsumerWallet(Callback<ReloadConsumerWalletResponse> reloadConsumerWalletCallback, String consumerid, String userid, String authcode, String imei, String sessionid, String consumername, String customerremarks, String paymentoption, String depositslipurl, String banktxnnumber, String bankname, String bankcode, String bankaccountname, String bankaccountnumber, String depositdatetime, String amountpaid) {
        Call<ReloadConsumerWalletResponse> reloadwallet = RetrofitBuild.reloadConsumerWalletService(getViewContext())
                .reloadConsumerWalletCall(consumerid,
                        userid,
                        authcode,
                        imei,
                        sessionid,
                        consumername,
                        customerremarks,
                        paymentoption,
                        depositslipurl,
                        banktxnnumber,
                        bankname,
                        bankcode,
                        bankaccountname,
                        bankaccountnumber,
                        depositdatetime,
                        amountpaid);
        reloadwallet.enqueue(reloadConsumerWalletCallback);
    }

    private final Callback<ReloadConsumerWalletResponse> reloadConsumerWalletSession = new Callback<ReloadConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<ReloadConsumerWalletResponse> call, Response<ReloadConsumerWalletResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            }
        }

        @Override
        public void onFailure(Call<ReloadConsumerWalletResponse> call, Throwable t) {
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private void evaluateResponse(Response<ReloadConsumerWalletResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {
                CommonFunctions.hasNewWalletUpdate = true;
                openSuccessfullReload("Wallet reload successfully sent for approval.");
                break;
            }
            case "004": {
                forceLogoutDialog("Invalid User");
                break;
            }
            default: {
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    private final Callback<GetSessionResponse> getwalletsession = new Callback<GetSessionResponse>() {


        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    getWallet(getWalletSession);
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            Log.d("antonhttp", t.toString());
        }
    };

    private final Callback<GetConsumerWalletResponse> getWalletSession = new Callback<GetConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<GetConsumerWalletResponse> call, Response<GetConsumerWalletResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                    List<ConsumerWallet> consumerwallet = response.body().getConsumerWallet();
                    for (ConsumerWallet wallet : consumerwallet) {
                        mdb.insertConsumerWallet(wallet);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GetConsumerWalletResponse> call, Throwable t) {

        }
    };

    private void getWallet(Callback<GetConsumerWalletResponse> getWalletCallback) {
        Call<GetConsumerWalletResponse> call = RetrofitBuild.getConsumerWalletService(getViewContext()).getConsumerWalletCall(consumerid, userid, authcode, imei, sessionid);
        call.enqueue(getWalletCallback);
    }


    private void getBankReference(Callback<GetBankReferenceResponse> getBankReferenceCallback, String imei, String authcode, String sessionid) {
        Call<GetBankReferenceResponse> getbankreference = RetrofitBuild.getBankReferenceService(getViewContext()).getBankReferenceCall(imei, authcode, sessionid);
        getbankreference.enqueue(getBankReferenceCallback);
    }

    private final Callback<GetBankReferenceResponse> getBankReferenceSession = new Callback<GetBankReferenceResponse>() {

        @Override
        public void onResponse(Call<GetBankReferenceResponse> call, Response<GetBankReferenceResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ADMIN_BANK_DETAILS);
                    List<BankReference> bankReferences = response.body().getBankReference();
                    for (BankReference bankreference : bankReferences) {
                        mdb.insertBankReference(bankreference);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GetBankReferenceResponse> call, Throwable t) {
            openErrorDialog("Error in connection. Please contact support.");
        }
    };

    private boolean evaluateFormResult() {
        return edtBankAccountNo.getText().toString().trim().length() == 0 ||
                edtBankName.getText().toString().trim().length() == 0 ||
                banktransno.getText().toString().trim().length() == 0 ||
                amount.getText().toString().trim().length() == 0 ||
                depositdate.getText().toString().trim().length() == 0 ||
                imageUri == null;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        depositdate.setText(date);
    }

    private void openBankDialog() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getViewContext());
        dialog.title("Bank");
        dialog.items(mdb.getBankNameReferences());
        dialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                edtBankName.setText(text.toString());
                edtBankAccountNo.setEnabled(true);
                edtBankAccountNo.setText("");
                bankaccountname = ".";
                bankaccountnumber = ".";
                bankname = banknamerefs.get(position).getBankName();
                bankcode = banknamerefs.get(position).getBankCode();
                bank_number_array = mdb.getAccountNumberReferences(bankcode);
            }
        });
        dialog.show();
    }

    private void openBankAccountNoDialog() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getViewContext());
        dialog.title("Bank Account No.");
        dialog.items(bank_number_array);
        dialog.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                edtBankAccountNo.setText(text.toString());
                bankaccountname = bank_number_array.get(position).getBankAccountName();
                bankaccountnumber = bank_number_array.get(position).getBankAccountNumber();
            }
        });
        dialog.show();
    }
}
