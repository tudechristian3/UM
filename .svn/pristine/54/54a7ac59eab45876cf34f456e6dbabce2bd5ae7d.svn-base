package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.CategoryItems;
import com.ultramega.shop.pojo.ItemSKU;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.pojo.scan.ScannedItem;
import com.ultramega.shop.responses.GetItemViaQRResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRCodeActivity extends BaseActivity {

    private BeepManager mBeepManager;
    private CompoundBarcodeView mCompoundBarcodeView;
    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String itemid = "";
    private String pricegroup = "";
    private String barcode = "";

    private UltramegaShopUtilities mDb;
    public WholesalerProfile wholesalerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        setUpToolBar();
        UltramegaShopUtilities mDb = new UltramegaShopUtilities(getViewContext());
        wholesalerProfile = mDb.getWholesalerProfile();

        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
        } else {
            pricegroup = wholesalerProfile.getPriceGroup();
        }

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Scan QR Code"));
        mBeepManager = new BeepManager(this);
        mCompoundBarcodeView = (CompoundBarcodeView) findViewById(R.id.barcode);

        scanBarcode();
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
        Intent intent = new Intent(context, ScanQRCodeActivity.class);
        context.startActivity(intent);
    }

    private void scanBarcode() {
        try {
            BarcodeCallback callback = null;

            callback = new BarcodeCallback() {
                @Override
                public void barcodeResult(final BarcodeResult result) {
                    mCompoundBarcodeView.pause();
                    mBeepManager.playBeepSoundAndVibrate();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {

                            itemid = String.valueOf(result);

                            if (itemid.length() > 0) {
                                //api session
                                progressDialog();
                                createSession(callsession);
                            }

//                            Cate item = new GridItem();
//                            item.setTitle("Huawei Y6");
//                            item.setImage("https://i.ytimg.com/vi/K3q4t9r7xH8/hqdefault.jpg");
//                            ViewShopItemActivity.start(getViewContext(),item);
                        }
                    });
                }

                @Override
                public void possibleResultPoints(List<ResultPoint> resultPoints) {

                }
            };
            mCompoundBarcodeView.decodeSingle(callback);
        } catch (Exception e) {
            e.printStackTrace();
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
                    getItemsViaQR(getItemsViaQRSession);
                } else {
                    hideProgressDialog();
                    showError(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mCompoundBarcodeView.resume();
            scanBarcode();
            hideProgressDialog();
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getItemsViaQR(Callback<GetItemViaQRResponse> getItemsViaQRCallback) {
        Call<GetItemViaQRResponse> fetchitems = RetrofitBuild.getItemViaQRService(getViewContext())
                .getItemViaQRCall(imei,
                        authcode,
                        sessionid,
                        usertype,
                        itemid,
                        pricegroup);
        fetchitems.enqueue(getItemsViaQRCallback);
    }

    private final Callback<GetItemViaQRResponse> getItemsViaQRSession = new Callback<GetItemViaQRResponse>() {

        @Override
        public void onResponse(Call<GetItemViaQRResponse> call, Response<GetItemViaQRResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);

            }
        }

        @Override
        public void onFailure(Call<GetItemViaQRResponse> call, Throwable t) {
            mCompoundBarcodeView.resume();
            scanBarcode();
        }
    };

    private void evaluateResponse(Response<GetItemViaQRResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {
                List<ScannedItem> scannedItems = response.body().getGetItemsViaQR();
                //List<CategoryItems> catItemsList = response.body().getGetItemsViaQR();

                if (scannedItems.size() > 0) {
                    ScannedItem scannedItem = scannedItems.get(0);

                    String itemGroupPicUrl = ".";
                    String category = scannedItem.getCategory();
                    String catClass = scannedItem.getCatClass();
                    String description = scannedItem.getDescription();
                    String dateTimeIn = null;

                    if (usertype.equals("CONSUMER")) {
                        itemGroupPicUrl = scannedItem.getCatClass() != null ? RetrofitBuild.S3_URL_RETAILER.concat((scannedItem.getCatClass().replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
                    } else if (usertype.equals("WHOLESALER")) {
                        itemGroupPicUrl = scannedItem.getCatClass() != null ? RetrofitBuild.S3_URL_WHOLESALER.concat((scannedItem.getCatClass().replaceAll("\\s+", "") + ".png").toLowerCase()) : "";
                    }
                    finish();

                    ViewShopItemActivity.start(getViewContext(),
                            new CategoryItems(category, catClass, description, dateTimeIn, itemGroupPicUrl),
                            new ItemSKU(scannedItem.getItemCode(), scannedItem.getItemDescription(), scannedItem.getPackSize(), scannedItem.getCategory(), scannedItem.getCatClass(), scannedItem.getSupplierID(), scannedItem.getPackageCode(), scannedItem.getPrice(), scannedItem.getGrossPrice(), scannedItem.getIsPromo(), scannedItem.getPromoDetails(), scannedItem.getMinimumOrderQTY(), scannedItem.getIncrementalOrderQTY(), scannedItem.getBarcode(), scannedItem.getPackageDescription(), scannedItem.getItemPicURL(), scannedItem.getIsBulk()),
                            scannedItem.getBarcodeFrom());

                } else {
                    mCompoundBarcodeView.resume();
                    scanBarcode();
                    openErrorDialog("No item found");
                }
                break;
            }
            default: {
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    @Override
    public void onResume() {

        try {

            mCompoundBarcodeView.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }


    // This code is needed for the BARCODE READER
    @Override
    public void onPause() {
        try {
            mCompoundBarcodeView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
