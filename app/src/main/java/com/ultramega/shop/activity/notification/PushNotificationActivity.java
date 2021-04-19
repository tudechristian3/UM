package com.ultramega.shop.activity.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Log;
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
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.PushNotification;

/**
 * Created by User on 22/09/2017.
 */

public class PushNotificationActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_PUSH_NOTIF_OBJECT = "push_notif_obj";

    public static final String STATUS_COMPLETED = "ORDER COMPLETE";
    public static final String STATUS_ON_DELIVERY = "ORDER READY FOR DELIVERY";
    public static final String STATUS_ON_PROCESS = "ORDER ON PROCESS";
    public static final String STATUS_ON_HOLD = "ORDER CURRENTLY EVALUATED";
    public static final String STATUS_CANCELLED = "CANCELLED ORDER";
    public static final String STATUS_DECLINED = "DECLINED ORDER";
    public static final String STATUS_FOR_PICK_UP = "ORDER READY FOR PICKUP";
    public static final String STATUS_WALLET_APPROVED = "APPROVED WALLET RELOAD REQUEST";
    public static final String STATUS_WALLET_DECLINED = "DECLINED WALLET RELOAD REQUEST";
    public static final String STATUS_PICK_UP_EXPIRED = "FOR PICK-UP ORDER EXPIRED";
    public static final String STATUS_ORDER_CONFIRMATION = "ORDER CONFIRMATION";
    public static final String STATUS_ORDER_WAS_CHANGE = "ORDER WAS CHANGE";
    public static final String STATUS_ORDER_WAS_VERIFIED = "ORDER WAS VERIFIED";


    private TextView mTvNotificationGenericSubject;
    private TextView mTvNotificationGenericMessage;
//    private TextView mTvNotificationGenericNote;

    private ImageView mPushNotificationIcons;
    private ImageView mWalletReloadNotificationsIcons;
    private ImageView push_notif_image;
    private LinearLayout mBgPush;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        init();
    }

    private void init() {
        PushNotification pushNotification = (PushNotification) getIntent().getSerializableExtra(KEY_PUSH_NOTIF_OBJECT);

        findViewById(R.id.notification_generic_close).setOnClickListener(this);

        Log.d("antonhttp", "DATA RECEIVED: " + new Gson().toJson(pushNotification));

        if (pushNotification.getSubject().toUpperCase().contains("ORDER")) {
            //==================================
            //REFRESH ORDERS HERE
            //==================================
            CommonFunctions.hasNewOrderUpdate = true;
        } else if (pushNotification.getSubject().toUpperCase().contains("WALLET")) {
            //==================================
            //REFRESH WALLET HERE
            //==================================
            CommonFunctions.hasNewWalletUpdate = true;
        }

        mTvNotificationGenericSubject = findViewById(R.id.notification_generic_subject);
        mTvNotificationGenericMessage = findViewById(R.id.notification_generic_message);
//        mTvNotificationGenericNote = (TextView) findViewById(R.id.notification_generic_note);

        progressBar = findViewById(R.id.progress);
        push_notif_image = findViewById(R.id.push_notif_image);
        mPushNotificationIcons = findViewById(R.id.push_notification_icons);
        mWalletReloadNotificationsIcons = findViewById(R.id.push_notification_wallet_icons);
        mBgPush = findViewById(R.id.bg_pushNotif);

        if (pushNotification.getSubject().equals("BROADCAST")) {
            mTvNotificationGenericSubject.setVisibility(View.GONE);
        } else {
            mTvNotificationGenericSubject.setVisibility(View.VISIBLE);
            mTvNotificationGenericSubject.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", pushNotification.getSubject()));
        }
        mTvNotificationGenericMessage.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Regular.ttf", pushNotification.getMessage()));
//        mTvNotificationGenericNote.setVisibility(View.GONE);

        int imageRes = 0;
        switch (pushNotification.getSubject().toUpperCase()) {
            case STATUS_COMPLETED: {
                imageRes = R.drawable.push_notification_completed;
                break;
            }
            case STATUS_CANCELLED:
            case STATUS_DECLINED:{
                imageRes = R.drawable.push_notification_cancelled;
                break;
            }
            case STATUS_ON_DELIVERY: {
                imageRes = R.drawable.push_notification_delivery;
                break;
            }
            case STATUS_ON_HOLD: {
                imageRes = R.drawable.push_notification_on_hold;
                break;
            }
            case STATUS_ON_PROCESS: {
                imageRes = R.drawable.push_notification_pending;
                break;
            }
            case STATUS_FOR_PICK_UP: {
                imageRes = R.drawable.push_pickup;
                break;
            }
            case STATUS_PICK_UP_EXPIRED: {
                imageRes = R.drawable.push_nitification_pickup_expired;
                break;
            }
            case STATUS_ORDER_CONFIRMATION:
            case STATUS_ORDER_WAS_VERIFIED: {
                imageRes = R.drawable.push_notification_order_confirmation;
                break;
            }
            case STATUS_ORDER_WAS_CHANGE: {
                imageRes = R.drawable.push_order_confirmation;
                break;
            }

        }
//        Glide.with(getViewContext())
//                .load(imageRes)
//                .into(mPushNotificationIcons);

        Glide.with(getViewContext())
                .asBitmap()
                .load(imageRes)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mPushNotificationIcons.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }
                });


        int imageWallet = 0;
        switch (pushNotification.getSubject().toUpperCase()) {
            case STATUS_WALLET_APPROVED: {
                imageWallet = R.drawable.push_wallet_accepted;
                mBgPush.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.bg_push_wallet));
                break;
            }
            case STATUS_WALLET_DECLINED: {
                imageWallet = R.drawable.push_wallet_declined;
                mBgPush.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.bg_push_wallet));
                break;
            }
        }

        Glide.with(getViewContext())
                .asBitmap()
                .load(imageWallet)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mWalletReloadNotificationsIcons.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }
                });


        push_notif_image.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        RequestOptions myOptions = new RequestOptions()
                .fitCenter()
                .override(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()) / 2);

        Glide.with(getViewContext())
                .load((pushNotification.getMessageimg().equals("N/A") ? R.drawable.um_logo:pushNotification.getMessageimg()))
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
    }

    public static void start(Context context, PushNotification pushNotification) {
        Intent intent = new Intent(context, PushNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_PUSH_NOTIF_OBJECT, pushNotification);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_generic_close: {
                onBackPressed();
                break;
            }
        }
    }
}
