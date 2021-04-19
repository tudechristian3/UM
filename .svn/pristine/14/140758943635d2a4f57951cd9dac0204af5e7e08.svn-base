package com.ultramega.shop.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ultramega.shop.BuildConfig;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.activity.LoginActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.PickUpActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Branches;
import com.ultramega.shop.pojo.Central;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.util.UserPreferenceManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Callback;

public abstract class BaseFragment extends Fragment {

    private Context mContext;
    protected static final String Theme_Current = "AppliedTheme";
    private static final String Theme_Consumer = "Consumer_Theme";
    private static final String Theme_Wholesaler = "Wholesaler_Theme";
    protected static final int REQUEST_CAMERA = 0;
    protected static final int SELECT_FILE = 1;
    protected Uri fileUri = null;
    private UltramegaShopUtilities mdb;

    public String imei = "";
    public String sessionid = "";
    public String customerid = "";
    public String userid = "";

    public MaterialDialog mMaterialDialogError = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void hideSearchBar() {
        ((BaseActivity) getViewContext()).hideSearchBar();
    }

    public void showSearchBar() {
        ((BaseActivity) getViewContext()).showSearchBar();
    }

    protected void setTextBackgroundView(String selectedMode, TextView tab_item, String tab_title, String fontStyle) {

        tab_item.setText(CommonFunctions.setTypeFace(getContext(), fontStyle, tab_title));

        switch (selectedMode) {
            case Theme_Consumer: {
                tab_item.setBackgroundResource(R.color.color_034b9b);
                break;
            }
            case Theme_Wholesaler: {
                tab_item.setBackgroundResource(R.color.color_F83832);
                break;
            }
            default: {
                tab_item.setBackgroundResource(R.color.color_F83832);
                break;
            }
        }

    }

    protected Context getViewContext() {
        return mContext;
    }

    protected void setTabTheme(String selectedMode, TabLayout tabLayout) {

        switch (selectedMode) {
            case Theme_Consumer: {
//                tabLayout.setBackgroundResource(R.color.color_2196F3);
                tabLayout.setBackgroundResource(R.color.color_0381DE);
                tabLayout.setTabTextColors(ContextCompat.getColor(getViewContext(), R.color.color_A6D5FA), ContextCompat.getColor(getViewContext(), R.color.colorWhite));
                break;
            }
            case Theme_Wholesaler: {
//                tabLayout.setBackgroundResource(R.color.color_FF7043);
                tabLayout.setBackgroundResource(R.color.color_0381DE);
                tabLayout.setTabTextColors(ContextCompat.getColor(getViewContext(), R.color.color_A6D5FA), ContextCompat.getColor(getViewContext(), R.color.colorWhite));
                //tabLayout.setTabTextColors(ContextCompat.getColor(getViewContext(), R.color.color_FFC5B3), ContextCompat.getColor(getViewContext(), R.color.colorWhite));
                break;
            }
            default: {
//                tabLayout.setBackgroundResource(R.color.color_2196F3);
                tabLayout.setBackgroundResource(R.color.color_0381DE);
                tabLayout.setTabTextColors(ContextCompat.getColor(getViewContext(), R.color.color_A6D5FA), ContextCompat.getColor(getViewContext(), R.color.colorWhite));
                break;
            }
        }

    }

    public String getCurrentUserType(Context context) {
        switch (UserPreferenceManager.getStringPreference(context, Theme_Current)) {
            case Theme_Consumer: {
                return "CONSUMER";
            }
            case Theme_Wholesaler: {
                return "WHOLESALER";
            }
            default: {
                return "CONSUMER";
            }
        }
    }

    protected void openSelectModeDialog() {

        try {
            new MaterialDialog.Builder(getViewContext())
                    .title("Select Mode")
                    .items(R.array.str_action_profile_picture)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                cameraIntent();
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, SELECT_FILE);
                            }
                            return true;
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openDeliveryOptionDialog() {

//        mdb = new UltramegaShopUtilities(getViewContext());

        new MaterialDialog.Builder(getViewContext())
                .title("Choose your delivery option")
                .items(R.array.str_action_delivery_option)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            PickUpActivity.start(getViewContext(), 1);
                        } else {
                            CheckoutProductsActivity.start(getViewContext(), 0);
                        }
                        return true;
                    }
                })
                .show();

    }

    public void openReloadWalletDialog(String message) {

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Reload Wallet")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MainActivity.start(getViewContext(), 5);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
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

    private void cameraIntent() {
        fileUri = getOutputMediaFileUri(1);
        Log.d("antonhttp", fileUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        List<ResolveInfo> resolvedIntentActivities = getViewContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            getViewContext().grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        Uri photoURI = FileProvider.getUriForFile(getViewContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile(1));

        return photoURI;
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Ultramega-Shop");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Ultramega-Shop", "Oops! Failed create "
                        + "Ultramega-Shop" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void createSession(Callback<GetSessionResponse> sessionCallback) {

        ((BaseActivity) getViewContext()).createSession(sessionCallback);
    }

    public void openErrorDialog(String message) {
        ((BaseActivity) getViewContext()).openErrorDialog(message);
    }

    protected void cropImage(Uri uri) {
        CropImage.activity(uri)
                .setActivityTitle("Crop Image")
                .setAutoZoomEnabled(true)
                .setBorderCornerThickness(0)
                .setOutputCompressQuality(75)
                .setActivityMenuIconColor(R.color.colorWhite)
                .setAllowRotation(true)
                .setAllowFlipping(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .start(getContext(), this);
    }

    public Bitmap getImageBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getViewContext().getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void openLoginDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Proceed")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        LoginActivity.start(getViewContext());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
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

    protected void setEmptyLayout(View view, LinearLayout layout_empty, Drawable image, String message) {
        layout_empty.setVisibility(View.VISIBLE);
        ImageView image_empty = (ImageView) view.findViewById(R.id.image_empty);

    }

    protected void openSuccessfullCheckout(String txnID) {
        new MaterialDialog.Builder(getViewContext())
                .content(Html.fromHtml("Your order with transaction number <b>" + txnID + "</b> has been placed. An Ultra Mega representative will get in touch with you shortly."))
                .cancelable(false)
                .positiveText("GO TO ORDER")
                .neutralText("SHOP AGAIN")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 3);
                        startActivity(intent);
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

    public void showProgressDialog(String titleID, String messageId) {
        ((BaseActivity) getActivity()).progressDialog(titleID, messageId);
    }

    public void showProgressDialog() {
        ((BaseActivity) getActivity()).progressDialog();
    }

    public void hideProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }

    public void showError(String content) {
        ((BaseActivity) getActivity()).showError(content);
    }

    public void hideSoftKeyboard() {
        ((BaseActivity) getViewContext()).hideSoftKeyboard();
    }

    public String getMobileNumber(String number) {
        return ((BaseActivity) getActivity()).getMobileNumber(number);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected void openWalletDialog() {
        ((BaseActivity) Objects.requireNonNull(getActivity())).openWalletDialog();
    }

    public float getDistance(double latitude, double longitude, Branches item) {
        float distance;
        Location startpoint = new Location("startpoint");
        startpoint.setLatitude(latitude);
        startpoint.setLongitude(longitude);

        try {
            Location endpoint = new Location("newlocation");
            endpoint.setLatitude(Double.parseDouble(item.getLatitude()));
            endpoint.setLongitude(Double.parseDouble(item.getLongitude()));
            distance = startpoint.distanceTo(endpoint) / 1000;
        } catch (NumberFormatException e) {
            distance = 0;
            e.printStackTrace();
        }

        return distance;
    }

    //=======================================================
    //CALCULATE EXPECTED LIMIT
    //length: size of array list
    //limitSize: limit value for each call
    //=======================================================
    public int getLimit(final double length, final double limitSize) {
        Log.d("okhttp","Limits: "+ length);
        Log.d("okhttp","Limits1: "+ limitSize);
        return (int) ((length == 0) ? 0 : limitSize * Math.ceil(length / limitSize));
    }

    public String capitalizeWord(String word) {
        return ((BaseActivity) getActivity()).capitalizeWord(word);
    }

    public void forceLogoutDialog(String message) {
        ((BaseActivity) getActivity()).forceLogoutDialog(message);
    }

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    public boolean isAirplaneModeOn(Context context) {
        return ((BaseActivity) getActivity()).isAirplaneModeOn(context);
    }

    /**
     * Gets the state of GPS Mode.
     *
     * @return true if enabled.
     */
    public boolean isGPSModeOn() {
        return ((BaseActivity) getActivity()).isGPSModeOn();
    }

    public void showGPSDisabledAlertToUser() {
        ((BaseActivity) getActivity()).showGPSDisabledAlertToUser();
    }

    public List<Central> getCentralList(String usertype) {
        //sessionid = UserPreferenceManager.getSession(getActivity());
        List<Central> centralList = new ArrayList<>();

        mdb = new UltramegaShopUtilities(getViewContext());

        if (usertype.equals("CONSUMER")) {
            sessionid = UserPreferenceManager.getSession(getActivity());
            centralList.add(new Central("Pick & Shop", R.drawable.central_blue_shop_pick));
            //centralList.add(new Central("Top Picks", R.drawable.central_blue_top_pick));
            centralList.add(new Central("Promos", R.drawable.central_blue_promos));
            //centralList.add(new Central("Categories", R.drawable.central_blue_categories));
            centralList.add(new Central("My Cart", R.drawable.central_blue_mycart));

            if (mdb.getConsumerID() != null) {
                centralList.add(new Central("Orders", R.drawable.central_blue_orders));
                //centralList.add(new Central("My Wallet", R.drawable.central_blue_my_wallet));
                centralList.add(new Central("Future Buys", R.drawable.central_blue_future_buys));
                //centralList.add(new Central("My Points", R.drawable.central_blue_my_points));
                centralList.add(new Central("Notifications", R.drawable.central_blue_notifications));
            }
            //centralList.add(new Central("U-PAY",R.drawable.upaylogo));

        } else if (usertype.equals("WHOLESALER")) {
            sessionid = UserPreferenceManager.getSession(getActivity());
            centralList.add(new Central("Pick & Shop", R.drawable.central_pick_and_shop));
            //centralList.add(new Central("Top Picks", R.drawable.central_top_picks));
            centralList.add(new Central("Promos", R.drawable.central_my_promos));
            centralList.add(new Central("Categories", R.drawable.central_categories));
            centralList.add(new Central("Suppliers", R.drawable.central_suppliers));
            centralList.add(new Central("My Cart", R.drawable.central_my_cart));
            centralList.add(new Central("Orders", R.drawable.central_orders));
            centralList.add(new Central("Future Buys", R.drawable.central_future_buys));
            centralList.add(new Central("My Points", R.drawable.central_my_points));
            centralList.add(new Central("Notifications", R.drawable.central_event));

        }

        return centralList;
    }

}
