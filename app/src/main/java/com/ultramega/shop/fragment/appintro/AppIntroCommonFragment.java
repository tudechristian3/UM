package com.ultramega.shop.fragment.appintro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseFragment;
import com.ultramega.shop.common.CommonFunctions;

/**
 * Created by Ban_Lenovo on 9/25/2017.
 */

public class AppIntroCommonFragment extends BaseFragment {

    public static final String KEY_APPINTRO_ID = "appintro_id";
    public static final int ID_1 = 111;
    public static final int ID_2 = 222;
    public static final int ID_3 = 333;
    public static final int ID_4 = 444;
    public static final int ID_5 = 555;

    private TextView tvMessage;
    private ImageView imgV;
    private int res_image = 0;
    private String message = "";

    public static AppIntroCommonFragment newInstance(int ID) {
        AppIntroCommonFragment fragment = new AppIntroCommonFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_APPINTRO_ID, ID);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (view == null) {
            int id = getArguments().getInt(KEY_APPINTRO_ID);
            switch (id) {
                case ID_1:
                    view = inflater.inflate(R.layout.fragment_appintro_one, container, false);
                    res_image = R.drawable.appintro1_v2;
                    message = "Tuloy po\nkayo!";
                    break;
                case ID_2:
                    view = inflater.inflate(R.layout.fragment_appintro_two, container, false);
                    res_image = R.drawable.appintro2_v2;
                    message = "Your wholesale and\nretail mart has just\nbeen upgraded!";
                    break;
                case ID_3:
                    view = inflater.inflate(R.layout.fragment_appintro_three, container, false);
                    res_image = R.drawable.appintro3_v2;
                    message = "Available for retailers\nand wholesalers.";
                    break;
                case ID_4:
                    view = inflater.inflate(R.layout.fragment_appintro_four, container, false);
                    res_image = R.drawable.appintro4_v2;
                    message = "Easy browsing for your\nfavorite products.";
                    break;
                case ID_5:
                    view = inflater.inflate(R.layout.fragment_appintro_five, container, false);
                    res_image = R.drawable.appintro5_v2;
                    message = "Fast updates of your\ntransactions.";
                    break;
            }
            assert view != null;
//            tvMessage = (TextView) view.findViewById(R.id.quick_tour_text);
//            tvMessage.setVisibility(View.GONE);
            imgV = (ImageView) view.findViewById(R.id.quick_tour_image);
//            if (id != ID_1)
//                tvMessage.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", message));
//            else
//                tvMessage.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", message));

            Glide.with(getViewContext())
                    .load(res_image)
                    .centerCrop()
                    .into(imgV);

        }
        return view;
    }
}
