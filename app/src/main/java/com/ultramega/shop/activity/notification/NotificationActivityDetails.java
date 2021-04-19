package com.ultramega.shop.activity.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ultramega.shop.R;
import com.ultramega.shop.adapter.settings.NotificationsRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.Notification;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetNotificationResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 15/09/2017.
 */

public class NotificationActivityDetails extends BaseActivity {
    public static final String KEY_NOTIFICATION = "Notification";

    private Notification notif;
    private UltramegaShopUtilities mDb;
    private ImageView image;
    private TextView sendername;
    private TextView date;
    private TextView message;

    private String imei = "";
    private String userid = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private int year = 0;
    private int month = 0;
    private String txnno = "";

    private LinearLayout mSmoothProgressBar;
    NotificationsRecyclerAdapter mAdapter;
    private List<Notification> mNotification;

    private ImageView push_notif_image;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_notification_details);
        setUpToolBar();

        mDb = new UltramegaShopUtilities(getViewContext());
        notif = getIntent().getParcelableExtra(KEY_NOTIFICATION);
        mAdapter = new NotificationsRecyclerAdapter(getViewContext(), mNotification);

        imei = CommonFunctions.getIMEI(getViewContext());
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        txnno = notif.getTxnNo();
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile consumerProfile = mDb.getConsumerProfile();
            customerid = consumerProfile.getConsumerID();
            userid = consumerProfile.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mDb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
        }
        mSmoothProgressBar = findViewById(R.id.mSmoothProgressBar);
        message = findViewById(R.id.txv_message);
        date = findViewById(R.id.txv_date);
        sendername = findViewById(R.id.txv_sender_name);
        image = findViewById(R.id.img_notification_details);

        push_notif_image = findViewById(R.id.push_notif_image);
        progressBar = findViewById(R.id.progress);

        //======================
        //SET DATA
        //======================
        setNotificationDetails(notif);

        if (notif.getReadStatus().equals("0")) {
            //======================
            //UPDATE NOTIFICATION
            //======================
            getSession();
        }

        try {
            JSONObject jObj = new JSONObject(notif.getPayLoad());
            String data = jObj.getString("data");

            Log.d("antonhttp", "data: " + data);

            JSONObject subObj = new JSONObject(data);
            String messageimg = subObj.getString("messageimg");

            if (!messageimg.equals(".") || !messageimg.equals("")) {
                push_notif_image.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                RequestOptions myOptions = new RequestOptions()
                        .fitCenter()
                        .override(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()) / 2);

                Glide.with(getViewContext())
                        .load(messageimg)
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
                        .apply(myOptions)
                        .into(push_notif_image);
            } else {
                progressBar.setVisibility(View.GONE);
                push_notif_image.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void start(Context context, Notification notification) {
        Intent intent = new Intent(context, NotificationActivityDetails.class);
        intent.putExtra(KEY_NOTIFICATION, notification);
        context.startActivity(intent);
    }

    private void setNotificationDetails(Notification notif) {
        sendername.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", notif.getSender()));
        date.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", CommonFunctions.convertDateNotificationDetails(notif.getDateTimeIN())));
        message.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", notif.getMessage()));
        Glide.with(getViewContext())
                .load(notif.getSenderLogo())
                .into(image);
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

    private void getSession() {

        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //API CALL
            mSmoothProgressBar.setVisibility(View.VISIBLE);
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
                    updateNotificationStatus(updateNotificationStatusSession);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void updateNotificationStatus(Callback<GetNotificationResponse> updateNotificationCallBack) {
        Call<GetNotificationResponse> notif = RetrofitBuild.updateNotificationStatusService(getViewContext())
                .updateNotificationStatusCall(imei,
                        userid,
                        authcode,
                        sessionid,
                        customerid,
                        String.valueOf(year),
                        String.valueOf(month),
                        txnno);
        notif.enqueue(updateNotificationCallBack);
    }

    private final Callback<GetNotificationResponse> updateNotificationStatusSession = new Callback<GetNotificationResponse>() {

        @Override
        public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
            mSmoothProgressBar.setVisibility(View.GONE);
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    //UPDATE ISREAD STATUS TO LOCAL DB
                    if (mDb != null) {
                        mDb.updateNotificationStatus(notif);
                    }

                    int badgeCount = UserPreferenceManager.getIntPreference(getViewContext(), "BADGE_COUNT");

                    badgeCount = badgeCount - 1;

                    if (badgeCount < 0) {
                        ShortcutBadger.removeCount(getViewContext());
                    } else {
                        UserPreferenceManager.saveIntegerPreference(getViewContext(), "BADGE_COUNT", badgeCount);
                        ShortcutBadger.applyCount(getViewContext(), badgeCount); //for 1.1.4+
                    }

                } else {
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                showError("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            showError("Something went wrong with the server.");
        }
    };

    public void insertNotification(Notification notification) {
        mDb.insertNotification(notification);
    }

}
