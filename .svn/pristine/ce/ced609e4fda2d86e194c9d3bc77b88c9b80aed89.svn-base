package com.ultramega.shop.activity.fullscreenimage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;

public class FullScreenActivity extends BaseActivity implements View.OnClickListener {

    private ImageView close;

    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            Intent intent = getIntent();

            close = (ImageView) findViewById(R.id.close);
            close.setOnClickListener(this);
            photoView = (PhotoView) findViewById(R.id.photo_view);

            String photoPath = intent.getStringExtra("imagePath").replace(".png", "_hd.png");

            Glide.with(getViewContext())
                    .asBitmap()
                    .load(photoPath)
                    .thumbnail(1f)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(new SimpleTarget<Bitmap>(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext())) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startPostponedEnterTransition();
                            }
                            photoView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);

                            Glide.with(getViewContext())
                                    .asBitmap()
                                    .load(R.drawable.ic_um_default_products)
                                    .apply(new RequestOptions()
                                            .fitCenter())
                                    .into(new BaseTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            photoView.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void getSize(SizeReadyCallback cb) {
                                            cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                                        }

                                        @Override
                                        public void removeCallback(SizeReadyCallback cb) {

                                        }
                                    });

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void start(Context context, String imagePath) {
        Intent intent = new Intent(context, FullScreenActivity.class);

        intent.putExtra("imagePath", imagePath);

        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close: {
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
