package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;

import java.util.ArrayList;
import java.util.List;

public class PaymentOptionActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout frame_image;
    private ImageView receiptImage, emptyImage;
    private TextView txvChangeImage, txvTapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        setUpToolBar();

        txvChangeImage = (TextView) findViewById(R.id.changeImage);
        txvTapImage = (TextView) findViewById(R.id.tapImage);
        frame_image = (FrameLayout) findViewById(R.id.forPaymentFrameImage);
        Spinner bank_name_spinner = (Spinner) findViewById(R.id.forPaymentBankName);
        Spinner bank_account_number_spinner = (Spinner) findViewById(R.id.forPaymentBankAccountNumber);
        Button btnProceed = (Button) findViewById(R.id.forPaymentConfirm);
        receiptImage = (ImageView) findViewById(R.id.forPaymentReceipt);
        emptyImage = (ImageView) findViewById(R.id.emptyReceipt);

        receiptImage.setVisibility(View.GONE);
        txvChangeImage.setVisibility(View.GONE);
        btnProceed.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", "Confirm"));
        frame_image.setOnClickListener(this);
        txvChangeImage.setOnClickListener(this);

        List<String> bank_name_array = new ArrayList<>();
        bank_name_array.add("Bank Name 1");
        bank_name_array.add("Bank Name 2");
        bank_name_array.add("Bank Name 3");
        bank_name_array.add("Bank Name 4");
        bank_name_array.add("Bank Name 5");
        bank_name_array.add("Bank Name 6");

        ArrayAdapter<String> bank_name_adapter = new ArrayAdapter<>(getViewContext(), R.layout.spinner_row, R.id.spinnerItem, bank_name_array);
        bank_name_adapter.setDropDownViewResource(R.layout.spinner_row_dropdown);
        bank_name_spinner.setAdapter(bank_name_adapter);

        List<String> bank_number_array = new ArrayList<>();
        bank_number_array.add("Account Number 1");
        bank_number_array.add("Account Number 2");
        bank_number_array.add("Account Number 3");
        bank_number_array.add("Account Number 4");
        bank_number_array.add("Account Number 5");
        bank_number_array.add("Account Number 6");
        bank_number_array.add("Account Number 7");
        bank_number_array.add("Account Number 8");
        bank_number_array.add("Account Number 9");
        bank_number_array.add("Account Number 10");
        bank_number_array.add("Account Number 11");
        bank_number_array.add("Account Number 12");

        ArrayAdapter<String> bank_account_number_adapter = new ArrayAdapter<>(getViewContext(), R.layout.spinner_row, R.id.spinnerItem, bank_number_array);
        bank_account_number_adapter.setDropDownViewResource(R.layout.spinner_row_dropdown);
        bank_account_number_spinner.setAdapter(bank_account_number_adapter);

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "For Payment"));
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

    public static void start(Context context) {
        Intent intent = new Intent(context, PaymentOptionActivity.class);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                onImageResult(result.getUri());
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

    private void cropImage(Uri uri){
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

}
