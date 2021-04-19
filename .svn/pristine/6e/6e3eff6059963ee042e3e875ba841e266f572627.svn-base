package com.ultramega.shop.fragment.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseFragment;

public class TermsAndConditionsFragment extends BaseFragment {

    WebView wv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_and_conditions,container,false);

        wv = view.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/termsandconditions.html");

        return view;
    }
}
