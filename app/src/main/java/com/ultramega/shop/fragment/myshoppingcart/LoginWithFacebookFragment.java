package com.ultramega.shop.fragment.myshoppingcart;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseFragment;

public class LoginWithFacebookFragment extends BaseFragment {

    // Your Facebook APP ID
    private static String APP_ID = "308180782571605"; // Replace your App ID here

    // Instance of Facebook Class
    //private Facebook facebook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_with_facebook, container, false);

        return view;
    }


}
